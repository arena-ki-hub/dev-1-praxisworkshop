# Schritt 3 — Excel-Import

## Lernziel

Plan Mode beim Einbinden einer neuen Dependency (Apache POI) und beim Bauen eines neuen, technisch komplexeren Features.

## Die Methode dieses Schritts

**Plan Mode** — dieselbe Mechanik wie in Schritt 1 (`Shift+Tab`, bis in der Eingabezeile der Plan Mode steht), aber aus einem anderen Grund. Dort ging es um fachliche Lücken in der Aufgabenstellung. Hier geht es um technische Entscheidungen, die du im fertigen Code nur noch schwer zurückdrehst: eine neue Dependency in der `pom.xml`, die Art des Datei-Uploads, und die Frage, wie eine fehlerhafte Zeile behandelt wird, ohne den ganzen Import mitzureißen.

Lass dir im Plan zeigen, welche Dependency in welcher Version dazukommt und wie das Ergebnis des Imports aussehen soll, bevor du freigibst.

**`/grilling`** bleibt das Werkzeug dafür: Du startest es direkt mit der Aufgabe, der Skill nimmt sie auseinander und legt dir jeden offenen Punkt einzeln mit einer Empfehlung vor.

## Aufgabe

Baue einen Import-Endpoint, der eine Excel-Datei mit Zeiterfassungsdaten entgegennimmt und daraus `TimeEntry`-Einträge anlegt. Eine Beispieldatei liegt unter `samples/zeiterfassung-import-beispiel.xlsx`. Das feste Spaltenlayout (Zeile 1 = Kopfzeile):

| Spalte A | Spalte B | Spalte C | Spalte D | Spalte E |
|---|---|---|---|---|
| Datum (`yyyy-MM-dd`) | Username | Stunden | Task | Abwesenheit (`Ja`/`Nein`) |

Der Username muss zu einem existierenden Mitarbeiter passen (Matching über `Employee.username`). Zeilen mit unbekanntem Mitarbeiter oder ungültigen Werten sollen **nicht** den gesamten Import abbrechen, sondern übersprungen und im Ergebnis aufgelistet werden.

## Empfohlener Workflow

1. **In den Plan Mode wechseln** (`Shift+Tab`, bis in der Eingabezeile der Plan Mode steht).

2. **Die Aufgabe direkt mit `/grilling` starten** — kein separater Plan-Prompt nötig, der Skill befragt dich zur Aufgabe und legt dir jeden offenen Punkt einzeln mit einer Empfehlung vor:

   ```bash
   /grilling Baue einen Import-Endpoint für Excel-Dateien mit Zeiterfassungsdaten (Spaltenlayout und Beispieldatei siehe README). Achte besonders auf die neue Dependency (Apache POI, `poi-ooxml`) samt Version, den Upload und die Behandlung fehlerhafter Zeilen.
   ```

3. **Den Plan freigeben und umsetzen lassen** — danach implementiert Claude den Import und lässt `./mvnw test` laufen. Weiter geht es mit dem nächsten Abschnitt: Import selbst ausprobieren.

## Feature selbst testen (Pflicht)

Der Import ist erst fertig, wenn du die Beispieldatei selbst durch die laufende Anwendung geschickt hast — ein grüner `./mvnw test` reicht nicht.

1. **App starten** — in einem zweiten Terminal, oder von Claude starten lassen (`Starte die App im Hintergrund und sag mir, wenn sie läuft`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Swagger UI öffnen** — <http://localhost:8080/swagger-ui.html>. Im Dev Container ist Port 8080 weitergeleitet (Reiter „Ports" in VS Code); ein Strg+Klick auf die URL im Terminal öffnet sie direkt.

3. **Die Beispieldatei importieren** — `POST /api/time-entries/import` → „Try it out" → bei `file` die Datei `samples/zeiterfassung-import-beispiel.xlsx` aus dem Projektordner auswählen → „Execute". Alternativ aus dem Terminal:

   ```bash
   curl -F file=@samples/zeiterfassung-import-beispiel.xlsx http://localhost:8080/api/time-entries/import
   ```

   - Die Antwort muss 9 importierte Zeilen melden und genau eine übersprungene Zeile mit einer Begründung zu `x.unbekannt`.
   - `GET /api/employees/{employeeId}/time-entries` für `a.schmidt`: die vier importierten Einträge (31.08., 01.09., der Urlaubstag 02.09. und der Samstag 05.09.) stehen wirklich in der Datenbank.
   - Zum Ausprobieren: lade eine beliebige Nicht-Excel-Datei hoch (z. B. die `README.md`). Was passiert? Auch die Referenzlösung quittiert das mit einem 500er — ein gutes Beispiel dafür, was eine grüne Testsuite alles nicht abdeckt. Ob du das abfängst, ist dir überlassen.

4. **Wenn etwas nicht stimmt** — gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („Import liefert importedCount 0, Response: …") statt nur „geht nicht".

## Akzeptanzkriterien

- `POST /api/time-entries/import` (multipart, Feldname `file`) importiert die Beispieldatei und liefert eine Zusammenfassung `{ importedCount, skippedRows }` zurück.
- Die Zeile mit unbekanntem Username (`x.unbekannt`) wird übersprungen und mit Begründung in `skippedRows` gelistet, statt den Import abzubrechen.
- `./mvnw test` läuft grün, inkl. eines Tests, der die Beispieldatei importiert.
- Du hast die Beispieldatei selbst über die Swagger UI (oder per `curl`) importiert und die angelegten Einträge über `GET .../time-entries` geprüft.

## Weiter

Fertig? Dann weiter mit Schritt 4 (Überstunden-Berechnung).

```bash
git reset --hard        # verwirft Änderungen an vorhandenen Dateien
git clean -fd           # entfernt neu angelegte Dateien
git checkout step-4
```

Statt die Befehle selbst zu tippen, kannst du Claude auch einfach sagen, was passieren soll:

```bash
Verwirf alle meine Änderungen, auch neu angelegte Dateien, und wechsle auf den Branch step-4.
```

Danach in Claude einmal `/clear`: Der Kontext aus diesem Schritt hilft im nächsten nicht und kostet bei jeder Nachricht erneut Tokens.
