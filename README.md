# Übung: Schritt 2 — Zeiterfassungs-Einträge

**Lernziel:** Testgetriebene Entwicklung (Rot-Grün-Refactor) im Zusammenspiel mit Claude Code — schau dir dazu den Skill `mattpocock-skills:tdd` an.

**Aufgabe:** Baue eine REST-API für Zeiterfassungs-Einträge (`TimeEntry`) je Mitarbeiter: Datum, Anzahl gebuchter Stunden, eine kurze Tätigkeitsbeschreibung sowie ein Kennzeichen, ob es sich um eine Abwesenheit (z.B. Urlaub) handelt. Auch hier sollen die üblichen CRUD-Operationen möglich sein, diesmal im Kontext eines Mitarbeiters (`/api/employees/{employeeId}/time-entries`).

Validiere dabei:
- Stunden müssen größer als 0 und höchstens 24 sein.
- Das Datum darf nicht in der Zukunft liegen.
- Der referenzierte Mitarbeiter muss existieren.

**Empfohlener Workflow:** Lass Claude Code zunächst Tests für die Validierungsregeln und die CRUD-Operationen schreiben (rot), dann die Implementierung ergänzen (grün), erst danach ggf. Refactoring. Wie immer: Plan vorher mit `/grilling` hinterfragen.

**Feature selbst testen (Pflicht):** Grüne Tests heißen zunächst nur, dass Claudes Code Claudes Tests besteht. Ob die Zeiterfassung wirklich funktioniert, siehst du erst in der laufenden Anwendung.

1. App starten (zweites Terminal, oder von Claude im Hintergrund starten lassen):

   ```bash
   ./mvnw spring-boot:run
   ```

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html> (im Dev Container ist Port 8080 weitergeleitet, siehe Reiter „Ports").

3. Die Endpunkte per „Try it out" durchklicken — die Seed-Daten enthalten die Mitarbeiter `a.schmidt`, `m.mueller` und `j.klein`, die IDs holst du dir über `GET /api/employees`:
   - `POST /api/employees/{employeeId}/time-entries`: heutiges Datum, 8 Stunden, Tätigkeit „Kundenprojekt", keine Abwesenheit → Eintrag wird angelegt.
   - `GET /api/employees/{employeeId}/time-entries`: der Eintrag steht in der Liste.
   - Einen zweiten Eintrag als Abwesenheit (z.B. Urlaub) anlegen.
   - Validierung prüfen: 0 Stunden, 25 Stunden, Datum von morgen → jeweils 400 mit verständlicher Meldung.
   - `POST /api/employees/999/time-entries` (unbekannter Mitarbeiter) → 404, **kein** 500.
   - Einen Eintrag per `PUT` ändern und per `DELETE` löschen → `GET` zeigt danach die Änderung bzw. den Eintrag nicht mehr.

4. Stimmt etwas nicht, gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („25 Stunden liefern 500 statt 400, Response: …") statt nur „geht nicht".

**Akzeptanzkriterien:**
- Neue Endpunkte sind über Swagger UI sichtbar und nutzbar.
- Ungültige Eingaben (Stunden außerhalb 0-24, Datum in der Zukunft, unbekannter Mitarbeiter) liefern passende HTTP-Fehlercodes, keine 500er.
- `./mvnw test` läuft grün, inkl. neuer Tests für die Validierungsregeln.
- Du hast die Endpunkte und alle drei Validierungsregeln selbst in der Swagger UI durchgeklickt.

