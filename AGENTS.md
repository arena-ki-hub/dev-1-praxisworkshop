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
- REST endpoints under `/api/...`, DTOs as Java records with Bean Validation annotations for requests, entities returned directly as responses (no separate response DTO, to keep the exercise lean).
- Seed data in `src/main/resources/data.sql` (the Hibernate schema is created before it is loaded, see `spring.jpa.defer-datasource-initialization`).

## Domain
- **Employee**: employee with `username` (unique, used later for matching during the Excel import) and `weeklyTargetHours` (target hours per week).
- Further domain logic (time tracking, Excel import, overtime calculation) follows in the next steps of the exercise — see README.md.

## Known pitfalls (Spring Boot 4)
- `springdoc-openapi-starter-webmvc-ui` needs an explicit version (not part of the Spring Boot BOM); currently compatible: 3.1.1.
- `@AutoConfigureMockMvc` lives in a new package in Spring Boot 4: `org.springframework.boot.webmvc.test.autoconfigure` (no longer `org.springframework.boot.test.autoconfigure.web.servlet`).
