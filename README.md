# Übung: Schritt 4 — Überstunden-Berechnung

**Lernziel:** Plan Mode bei einer fachlich komplexeren Aufgabe mit mehreren Teilschritten.

**Aufgabe:** Baue einen Endpoint, der für einen Mitarbeiter und einen Zeitraum die Überstunden berechnet: `GET /api/employees/{id}/overtime?from=...&to=...`.

Berechnungsregel:
- Samstag/Sonntag haben 0 Soll-Stunden.
- An Werktagen gilt Soll-Stunden = `weeklyTargetHours / 5`.
- Tage mit einer Abwesenheits-Buchung (`absence=true`, z.B. Urlaub) werden **komplett** aus der Berechnung ausgeschlossen (weder Soll- noch Ist-Stunden für diesen Tag).
- Ist-Stunden = Summe der gebuchten Stunden aller Nicht-Abwesenheits-Einträge im Zeitraum (auch an Wochenenden, falls dort gebucht wurde).
- Überstunden = Ist-Stunden − Soll-Stunden.

**Empfohlener Workflow:** Diese Aufgabe hat mehrere Teilschritte (Datumsbereich iterieren, Wochenenden erkennen, Abwesenheiten ausschließen, Summen bilden) — lohnt sich besonders, den Plan vor der Umsetzung gründlich mit `/grilling` zu hinterfragen (z.B.: Was passiert an einem Werktag ganz ohne Buchung? Was, wenn `from` nach `to` liegt?).

**Feature selbst testen (Pflicht):** Bei einer Rechenregel reicht „Tests sind grün" nicht — rechne das Ergebnis der laufenden Anwendung einmal selbst nach.

1. App starten (zweites Terminal, oder von Claude im Hintergrund starten lassen):

   ```bash
   ./mvnw spring-boot:run
   ```

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html> (im Dev Container ist Port 8080 weitergeleitet, siehe Reiter „Ports").

3. Testdaten anlegen: `POST /api/time-entries/import` mit `samples/zeiterfassung-import-beispiel.xlsx` (oder ein paar Einträge von Hand über `POST /api/employees/{employeeId}/time-entries`). Die ID von `a.schmidt` holst du dir über `GET /api/employees`.

4. `GET /api/employees/{id}/overtime?from=2026-08-31&to=2026-09-06` für `a.schmidt` aufrufen und das Ergebnis gegen die Regel prüfen:
   - **Soll 32 h:** Mo 31.08., Di 01.09., Do 03.09. und Fr 04.09. mit je 8 h. Der Urlaubstag Mi 02.09. fällt komplett raus, Sa/So haben 0 Soll-Stunden.
   - **Ist 20 h:** 8 h + 8 h + 4 h — die 4 h am Samstag 05.09. zählen mit, der Urlaubstag nicht.
   - **Überstunden −12 h.**
   - Fehlerfälle: `from` nach `to` → 400, `GET /api/employees/999/overtime?...` → 404, **kein** 500.

5. Weicht das Ergebnis ab, gib Claude die Anfrage, die Antwort und deine Rechnung („erwartet Soll 32 / Ist 20, geliefert …") — dann hat es etwas Konkretes zum Nachrechnen.

**Akzeptanzkriterien:**
- Endpoint liefert `{ employeeId, from, to, sollStunden, istStunden, ueberstunden }`.
- Ein Werktag ganz ohne Buchung zählt voll als Soll-Stunden (führt zu Minderstunden), ein Wochenendtag mit Buchung zählt voll als Überstunden.
- Ein vertauschter Zeitraum (`from` nach `to`) liefert HTTP 400, ein unbekannter Mitarbeiter HTTP 404.
- `./mvnw test` läuft grün, inkl. eines Tests mit gemischtem Szenario (Werktage, ein Urlaubstag, ein Wochenendtag mit Buchung).
- Du hast den Endpoint selbst in der laufenden Anwendung aufgerufen und das Ergebnis nachgerechnet (für `a.schmidt` vom 31.08. bis 06.09.2026: Soll 32 h, Ist 20 h, Überstunden −12 h).

