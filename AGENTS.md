# Project context: Overtime backend

## Purpose
Backend (REST API + Swagger UI) for calculating overtime from time-tracking data. No frontend, no authentication — the focus is on the domain logic and data import.

## Tech stack
- Java 25, Spring Boot 4.1.1 (modules `spring-boot-starter-webmvc`, `-data-jpa`, `-data-rest`, `-validation`)
- H2 in-memory database (`jdbc:h2:mem:backend`), H2 console at `/h2-console`
- Swagger UI via `springdoc-openapi-starter-webmvc-ui` at `/swagger-ui.html`
- Tests: JUnit 5, MockMvc (`spring-boot-starter-test` + `spring-boot-starter-webmvc-test`)

## Conventions
- One package per domain under `eu.fincon.backend.<domain>` (e.g. `employee`), containing entity/repository/service/controller/exceptions.
- Shared error handling in `eu.fincon.backend.common` (`NotFoundException`, `ConflictException`, `GlobalExceptionHandler`, `ApiError`).
- REST endpoints under `/api/...`, DTOs as Java records with Bean Validation annotations for requests. Responses: entities without relations are returned directly; entities with relations (e.g. `TimeEntry` → `Employee`) are mapped to a response record to avoid problems with lazy loading/Jackson serialization.
- Seed data in `src/main/resources/data.sql` (the Hibernate schema is created before it is loaded, see `spring.jpa.defer-datasource-initialization`).

## Domain
- **Employee**: employee with `username` (unique, used later for matching during the Excel import) and `weeklyTargetHours` (target hours per week).
- **TimeEntry**: time-tracking entry per employee (`date`, `hours`, `taskDescription`, `absence`). `absence=true` marks absences (e.g. vacation) — such days are excluded from the overtime calculation later.
- **Excel import**: `POST /api/time-entries/import` expects a fixed column layout (Date, Username, Hours, Task, Absence), see `samples/timesheet-import-example.xlsx`. Faulty/unknown rows do not abort the import but end up in `skippedRows` with a reason. Row validation deliberately uses the same `TimeEntryRequest`/Bean Validation mechanism as the REST API (`jakarta.validation.Validator` called manually) so the rules are not maintained twice.
- The sample file is deliberately heavily simplified and anonymized (fictitious employees/usernames from `data.sql`) and has **no** connection to real company data — the real export format is much more complex (many more columns such as cost center, rate, job order). Step 6 makes the column mapping configurable so that other formats could be imported in the future.
- Further domain logic (overtime calculation) follows in the next steps of the exercise — see README.md.

## Known pitfalls (Spring Boot 4)
- `springdoc-openapi-starter-webmvc-ui` needs an explicit version (not part of the Spring Boot BOM); currently compatible: 3.1.1.
- `@AutoConfigureMockMvc` lives in a new package in Spring Boot 4: `org.springframework.boot.webmvc.test.autoconfigure` (no longer `org.springframework.boot.test.autoconfigure.web.servlet`).
