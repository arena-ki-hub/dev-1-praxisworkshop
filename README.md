# Übung: Schritt 3 — Excel-Import

**Lernziel:** Plan Mode beim Einbinden einer neuen Dependency (Apache POI) und beim Bauen eines neuen, technisch komplexeren Features.

**Aufgabe:** Baue einen Import-Endpoint, der eine Excel-Datei mit Zeiterfassungsdaten entgegennimmt und daraus `TimeEntry`-Einträge anlegt. Eine Beispieldatei liegt unter `samples/zeiterfassung-import-beispiel.xlsx`. Das feste Spaltenlayout (Zeile 1 = Kopfzeile):

| Spalte A | Spalte B | Spalte C | Spalte D | Spalte E |
|---|---|---|---|---|
| Datum (`yyyy-MM-dd`) | Username | Stunden | Task | Abwesenheit (`Ja`/`Nein`) |

Der Username muss zu einem existierenden Mitarbeiter passen (Matching über `Employee.username`). Zeilen mit unbekanntem Mitarbeiter oder ungültigen Werten sollen **nicht** den gesamten Import abbrechen, sondern übersprungen und im Ergebnis aufgelistet werden.

**Empfohlener Workflow:** Plan Mode nutzen (neue Dependency `org.apache.poi:poi-ooxml`, Datei-Upload, Fehlerbehandlung pro Zeile), Plan mit `/grilling` hinterfragen, dann umsetzen. Zum Testen: Import über Swagger UI mit der Beispieldatei ausprobieren.

**Feature selbst testen (Pflicht):** Der Import ist erst fertig, wenn du die Beispieldatei selbst durch die laufende Anwendung geschickt hast — ein grüner `./mvnw test` reicht nicht.

1. App starten (zweites Terminal, oder von Claude im Hintergrund starten lassen):

   ```bash
   ./mvnw spring-boot:run
   ```

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html> (im Dev Container ist Port 8080 weitergeleitet, siehe Reiter „Ports").

3. `POST /api/time-entries/import` → „Try it out" → bei `file` die Datei `samples/zeiterfassung-import-beispiel.xlsx` aus dem Projektordner auswählen → „Execute". Alternativ aus dem Terminal:

   ```bash
   curl -F file=@samples/zeiterfassung-import-beispiel.xlsx http://localhost:8080/api/time-entries/import
   ```

   - Die Antwort muss 9 importierte Zeilen melden und genau eine übersprungene Zeile mit einer Begründung zu `x.unbekannt`.
   - `GET /api/employees/{employeeId}/time-entries` für `a.schmidt`: die vier importierten Einträge (31.08., 01.09., der Urlaubstag 02.09. und der Samstag 05.09.) stehen wirklich in der Datenbank.
   - Zum Ausprobieren: lade eine beliebige Nicht-Excel-Datei hoch (z.B. die `README.md`). Was passiert? Auch die Referenzlösung quittiert das mit einem 500er — ein gutes Beispiel dafür, was eine grüne Testsuite alles nicht abdeckt. Ob du das abfängst, ist dir überlassen.

4. Stimmt etwas nicht, gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („Import liefert importedCount 0, Response: …") statt nur „geht nicht".

**Akzeptanzkriterien:**
- `POST /api/time-entries/import` (multipart, Feldname `file`) importiert die Beispieldatei und liefert eine Zusammenfassung `{ importedCount, skippedRows }` zurück.
- Die Zeile mit unbekanntem Username (`x.unbekannt`) wird übersprungen und mit Begründung in `skippedRows` gelistet, statt den Import abzubrechen.
- `./mvnw test` läuft grün, inkl. eines Tests, der die Beispieldatei importiert.
- Du hast die Beispieldatei selbst über die Swagger UI (oder per `curl`) importiert und die angelegten Einträge über `GET .../time-entries` geprüft.

