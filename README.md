# Übung: Schritt 5 — Bugfixing

**Lernziel:** Debugging-Workflow mit Claude Code — schau dir dazu den Skill `mattpocock-skills:diagnosing-bugs` an.

**Ausgangslage:** In diesem Stand schlägt `./mvnw test` fehl. In der Überstunden-Berechnung aus Schritt 4 sind gezielt Fehler enthalten, die die Soll/Ist-Regel aus Schritt 4 verletzen (die Regel selbst steht unverändert im Javadoc von `OvertimeService` und in `AGENTS.md`).

**Aufgabe:** Finde und behebe die Fehler, bis alle Tests wieder grün sind — insbesondere `OvertimeControllerTest`. Es kann mehr als einen Fehler geben; verlass dich nicht darauf, dass mit dem ersten Fix schon alles grün ist.

**Empfohlener Workflow:** Nicht direkt drauflos fixen lassen. Erst den fehlschlagenden Test und die Fehlermeldung genau lesen, eine Hypothese bilden, den bestehenden Code gegen die dokumentierte Soll/Ist-Regel prüfen — dann gezielt fixen. Ergänze bei Bedarf weitere Tests, die den jeweiligen Fehler eindeutig belegen.

**Feature selbst testen (Pflicht):** Grüne Tests beweisen hier wenig — ein Fix kann auch einfach den Test passend machen, statt die Fachlogik zu reparieren. Prüf die Berechnung deshalb an der laufenden Anwendung.

1. App starten (zweites Terminal, oder von Claude im Hintergrund starten lassen):

   ```bash
   ./mvnw spring-boot:run
   ```

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html> (im Dev Container ist Port 8080 weitergeleitet, siehe Reiter „Ports").

3. Testdaten anlegen: `POST /api/time-entries/import` mit `samples/zeiterfassung-import-beispiel.xlsx`. Die ID von `a.schmidt` holst du dir über `GET /api/employees`.

4. `GET /api/employees/{id}/overtime?from=2026-08-31&to=2026-09-06` für `a.schmidt` aufrufen und selbst nachrechnen:
   - **Soll 32 h** (Mo 31.08., Di 01.09., Do 03.09., Fr 04.09. mit je 8 h; der Urlaubstag Mi 02.09. fällt komplett raus, Sa/So haben 0 Soll-Stunden),
   - **Ist 20 h** (8 h + 8 h + die 4 h vom Samstag 05.09.; der Urlaubstag zählt nicht),
   - **Überstunden −12 h.**

   Genau hier zeigen sich die eingebauten Fehler: Wochenenden, die in die Soll-Stunden wandern, oder Abwesenheitsstunden, die in den Ist-Stunden landen.

5. Weicht das Ergebnis ab, gib Claude die Anfrage, die Antwort und deine erwartete Rechnung — und lass den nächsten Fix von einem Test absichern.

**Akzeptanzkriterien:**
- `./mvnw test` läuft komplett grün.
- Die Soll/Ist-Regel aus Schritt 4 ist wieder korrekt umgesetzt (insbesondere: Wochenenden zählen nicht in die Soll-Stunden, Abwesenheitstage fließen nicht in die Ist-Stunden ein).
- Du hast die Berechnung nach dem Fix selbst in der laufenden Anwendung geprüft (für `a.schmidt` vom 31.08. bis 06.09.2026: Soll 32 h, Ist 20 h, Überstunden −12 h).

