# Übung: Schritt 3 — Excel-Import

**Lernziel:** Plan Mode beim Einbinden einer neuen Dependency (Apache POI) und beim Bauen eines neuen, technisch komplexeren Features.

**Aufgabe:** Baue einen Import-Endpoint, der eine Excel-Datei mit Zeiterfassungsdaten entgegennimmt und daraus `TimeEntry`-Einträge anlegt. Eine Beispieldatei liegt unter `samples/zeiterfassung-import-beispiel.xlsx`. Das feste Spaltenlayout (Zeile 1 = Kopfzeile):

| Spalte A | Spalte B | Spalte C | Spalte D | Spalte E |
|---|---|---|---|---|
| Datum (`yyyy-MM-dd`) | Username | Stunden | Task | Abwesenheit (`Ja`/`Nein`) |

Der Username muss zu einem existierenden Mitarbeiter passen (Matching über `Employee.username`). Zeilen mit unbekanntem Mitarbeiter oder ungültigen Werten sollen **nicht** den gesamten Import abbrechen, sondern übersprungen und im Ergebnis aufgelistet werden.

**Empfohlener Workflow:** Plan Mode nutzen (neue Dependency `org.apache.poi:poi-ooxml`, Datei-Upload, Fehlerbehandlung pro Zeile), Plan mit `/grilling` hinterfragen, dann umsetzen. Zum Testen: Import über Swagger UI mit der Beispieldatei ausprobieren.

**Akzeptanzkriterien:**
- `POST /api/time-entries/import` (multipart, Feldname `file`) importiert die Beispieldatei und liefert eine Zusammenfassung `{ importedCount, skippedRows }` zurück.
- Die Zeile mit unbekanntem Username (`x.unbekannt`) wird übersprungen und mit Begründung in `skippedRows` gelistet, statt den Import abzubrechen.
- `./mvnw test` läuft grün, inkl. eines Tests, der die Beispieldatei importiert.

