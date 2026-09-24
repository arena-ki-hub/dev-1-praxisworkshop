# Projektkontext: Überstunden-Backend

## Zweck
Backend (REST API + Swagger UI) zur Berechnung von Überstunden aus Zeiterfassungsdaten. Kein Frontend, keine Authentifizierung — Fokus liegt auf der Fachlogik und Datenimport.

## Tech-Stack
- Java 25, Spring Boot 4.1.1 (Module `spring-boot-starter-webmvc`, `-data-jpa`, `-data-rest`, `-validation`)
- H2 In-Memory-Datenbank (`jdbc:h2:mem:backend`), H2-Konsole unter `/h2-console`
- Swagger UI via `springdoc-openapi-starter-webmvc-ui` unter `/swagger-ui.html`
- Tests: JUnit 5, MockMvc (`spring-boot-starter-test` + `spring-boot-starter-webmvc-test`)

## Konventionen
- Package je Fachdomäne unter `eu.fincon.backend.<domain>` (z.B. `employee`), mit Entity/Repository/Service/Controller/Exceptions darin.
- Gemeinsame Fehlerbehandlung in `eu.fincon.backend.common` (`NotFoundException`, `ConflictException`, `GlobalExceptionHandler`, `ApiError`).
- REST-Endpunkte unter `/api/...`, DTOs als Java Records mit Bean-Validation-Annotationen für Requests, Entities direkt als Response (kein separates Response-DTO, um die Übung schlank zu halten).
- Seed-Daten in `src/main/resources/data.sql` (Hibernate-Schema wird vor dem Einspielen erzeugt, siehe `spring.jpa.defer-datasource-initialization`).

## Domäne
- **Employee**: Mitarbeiter mit `username` (eindeutig, wird später beim Excel-Import zum Matching genutzt) und `weeklyTargetHours` (Soll-Stunden/Woche).
- Weitere Fachlichkeit (Zeiterfassung, Excel-Import, Überstundenberechnung) folgt in den nächsten Schritten der Übung — siehe README.md.

## Bekannte Stolpersteine (Spring Boot 4)
- `springdoc-openapi-starter-webmvc-ui` braucht eine explizite Version (nicht Teil des Spring-Boot-BOMs); aktuell kompatibel: 3.1.1.
- `@AutoConfigureMockMvc` liegt in Spring Boot 4 unter dem neuen Package `org.springframework.boot.webmvc.test.autoconfigure` (nicht mehr `org.springframework.boot.test.autoconfigure.web.servlet`).
