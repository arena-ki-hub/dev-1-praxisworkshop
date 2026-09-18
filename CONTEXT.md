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
- REST-Endpunkte unter `/api/...`, DTOs als Java Records mit Bean-Validation-Annotationen für Requests. Response: Entities ohne Relationen werden direkt zurückgegeben; Entities mit Relationen (z.B. `TimeEntry` → `Employee`) über ein Response-Record gemappt, um Probleme mit Lazy-Loading/Jackson-Serialisierung zu vermeiden.
- Seed-Daten in `src/main/resources/data.sql` (Hibernate-Schema wird vor dem Einspielen erzeugt, siehe `spring.jpa.defer-datasource-initialization`).

## Domäne
- **Employee**: Mitarbeiter mit `username` (eindeutig, wird später beim Excel-Import zum Matching genutzt) und `weeklyTargetHours` (Soll-Stunden/Woche).
- **TimeEntry**: Zeiterfassungs-Eintrag je Mitarbeiter (`date`, `hours`, `taskDescription`, `absence`). `absence=true` markiert Abwesenheiten (z.B. Urlaub) — solche Tage fließen später nicht in die Überstundenberechnung ein.
- **Excel-Import**: `POST /api/time-entries/import` (Multipart-Feld `file`, optionales Multipart-Textfeld `mapping` als JSON). Spalten werden **über die Kopfzeile (Zeile 1) per Namen** aufgelöst, nicht über feste Positionen — siehe `ColumnMapping`. Ohne `mapping`-Parameter gilt das Default-Mapping aus `application.yaml` (`import.excel.column-mapping.*`), das zu `samples/zeiterfassung-import-beispiel.xlsx` passt. Fehlt eine im Mapping referenzierte Spalte in der Datei, wird der Import mit HTTP 400 abgelehnt (Konfigurationsfehler); fehlerhafte/unbekannte *Zeilen* brechen den Import dagegen nicht ab, sondern landen mit Begründung in `skippedRows`. Die Zeilenvalidierung nutzt bewusst denselben `TimeEntryRequest`/Bean-Validation-Mechanismus wie die REST-API (`jakarta.validation.Validator` manuell aufgerufen), um Regeln nicht doppelt zu pflegen.
- Die Beispieldatei ist bewusst stark vereinfacht und anonymisiert (fiktive Mitarbeiter/Usernames aus `data.sql`) und hat **keinen** Bezug zu echten Firmendaten — das reale Export-Format ist deutlich komplexer (viele weitere Spalten wie Kostenstelle, Tarif, Job Order). Das konfigurierbare Mapping macht den Import perspektivisch auch für andere Spaltenlayouts nutzbar, ohne Code ändern zu müssen.
- **Überstunden-Berechnung**: `GET /api/employees/{id}/overtime?from=...&to=...`, siehe `OvertimeService` für die maßgebliche Soll/Ist-Regel (Wochenende = 0 Soll, Abwesenheitstage komplett ausgeschlossen, Ist zählt auch Wochenend-Buchungen voll).

## Bekannte Stolpersteine (Spring Boot 4)
- `springdoc-openapi-starter-webmvc-ui` braucht eine explizite Version (nicht Teil des Spring-Boot-BOMs); aktuell kompatibel: 3.1.1.
- `@AutoConfigureMockMvc` liegt in Spring Boot 4 unter dem neuen Package `org.springframework.boot.webmvc.test.autoconfigure` (nicht mehr `org.springframework.boot.test.autoconfigure.web.servlet`).
