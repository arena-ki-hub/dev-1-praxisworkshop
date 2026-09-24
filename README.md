# Übung: Schritt 5 — Bugfixing

**Lernziel:** Debugging-Workflow mit Claude Code — schau dir dazu den Skill `mattpocock-skills:diagnosing-bugs` an.

**Ausgangslage:** In diesem Stand schlägt `./mvnw test` fehl. In der Überstunden-Berechnung aus Schritt 4 sind gezielt Fehler enthalten, die die Soll/Ist-Regel aus Schritt 4 verletzen (die Regel selbst steht unverändert im Javadoc von `OvertimeService` und in `AGENTS.md`).

**Aufgabe:** Finde und behebe die Fehler, bis alle Tests wieder grün sind — insbesondere `OvertimeControllerTest`. Es kann mehr als einen Fehler geben; verlass dich nicht darauf, dass mit dem ersten Fix schon alles grün ist.

**Empfohlener Workflow:** Nicht direkt drauflos fixen lassen. Erst den fehlschlagenden Test und die Fehlermeldung genau lesen, eine Hypothese bilden, den bestehenden Code gegen die dokumentierte Soll/Ist-Regel prüfen — dann gezielt fixen. Ergänze bei Bedarf weitere Tests, die den jeweiligen Fehler eindeutig belegen.

**Akzeptanzkriterien:**
- `./mvnw test` läuft komplett grün.
- Die Soll/Ist-Regel aus Schritt 4 ist wieder korrekt umgesetzt (insbesondere: Wochenenden zählen nicht in die Soll-Stunden, Abwesenheitstage fließen nicht in die Ist-Stunden ein).

