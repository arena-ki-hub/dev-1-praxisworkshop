# Übung: Schritt 1 — Mitarbeiter-Verwaltung + Swagger UI

**Lernziel:** Plan Mode & strukturiertes Prompting im Zusammenspiel mit Claude Code.

**Aufgabe:** Baue eine REST-API zur Verwaltung von Mitarbeitern (`Employee`): Vor- und Nachname, ein eindeutiger Username sowie die wöchentliche Soll-Arbeitszeit in Stunden. Die API soll die üblichen CRUD-Operationen unterstützen (Anlegen, Auflisten, Einzelabruf, Aktualisieren, Löschen). Ergänze außerdem Swagger UI, damit sich die API im Browser testen lässt.

**Empfohlener Workflow:** Plan Mode nutzen, den Plan mit `/grilling` hinterfragen (z.B.: Wie werden doppelte Usernames behandelt? Welche Felder sind Pflicht? Wie sieht die Fehlerantwort aus?), erst danach umsetzen lassen.

**Feature selbst testen (Pflicht):** Der Schritt ist erst fertig, wenn du die API in der laufenden Anwendung selbst ausprobiert hast — nicht schon, wenn Claude „fertig" meldet und `./mvnw test` grün ist.

1. App starten (zweites Terminal, oder von Claude im Hintergrund starten lassen):

   ```bash
   ./mvnw spring-boot:run
   ```

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html> (im Dev Container ist Port 8080 weitergeleitet, siehe Reiter „Ports").

3. Die Endpunkte per „Try it out" durchklicken:
   - `POST /api/employees`: Mitarbeiter `a.schmidt` mit 40 Soll-Stunden anlegen, die ID aus der Antwort merken.
   - `GET /api/employees`: der neue Mitarbeiter steht in der Liste.
   - `POST /api/employees` noch einmal mit demselben Username → verständliche Fehlermeldung mit sinnvollem Statuscode (z.B. 409), **kein** 500 und kein Stacktrace.
   - `PUT /api/employees/{id}`: Soll-Stunden auf 32 ändern → `GET /api/employees/{id}` liefert 32.
   - `DELETE /api/employees/{id}` → danach liefert `GET /api/employees/{id}` 404.
   - Einmal einen leeren Username schicken → 400 statt 500.

4. Stimmt etwas nicht, gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („`POST /api/employees` mit doppeltem Username liefert 500, Response: …") statt nur „geht nicht".

**Akzeptanzkriterien:**
- Die Anwendung startet mit `./mvnw spring-boot:run`.
- Unter `/swagger-ui.html` sind alle Mitarbeiter-Endpunkte sichtbar und über "Try it out" nutzbar.
- Ein doppelter Username wird mit einer sinnvollen Fehlermeldung abgelehnt (nicht mit HTTP 500).
- `./mvnw test` läuft grün.
- Du hast die Endpunkte selbst in der Swagger UI durchgeklickt — inklusive doppeltem Username und dem Abruf eines gelöschten Mitarbeiters.

