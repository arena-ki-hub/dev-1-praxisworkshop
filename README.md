# Übung: Schritt 2 — Zeiterfassungs-Einträge

**Lernziel:** Testgetriebene Entwicklung (Rot-Grün-Refactor) im Zusammenspiel mit Claude Code — schau dir dazu den Skill `mattpocock-skills:tdd` an.

**Aufgabe:** Baue eine REST-API für Zeiterfassungs-Einträge (`TimeEntry`) je Mitarbeiter: Datum, Anzahl gebuchter Stunden, eine kurze Tätigkeitsbeschreibung sowie ein Kennzeichen, ob es sich um eine Abwesenheit (z.B. Urlaub) handelt. Auch hier sollen die üblichen CRUD-Operationen möglich sein, diesmal im Kontext eines Mitarbeiters (`/api/employees/{employeeId}/time-entries`).

Validiere dabei:
- Stunden müssen größer als 0 und höchstens 24 sein.
- Das Datum darf nicht in der Zukunft liegen.
- Der referenzierte Mitarbeiter muss existieren.

**Empfohlener Workflow:** Lass Claude Code zunächst Tests für die Validierungsregeln und die CRUD-Operationen schreiben (rot), dann die Implementierung ergänzen (grün), erst danach ggf. Refactoring. Wie immer: Plan vorher mit `/grilling` hinterfragen.

**Akzeptanzkriterien:**
- Neue Endpunkte sind über Swagger UI sichtbar und nutzbar.
- Ungültige Eingaben (Stunden außerhalb 0-24, Datum in der Zukunft, unbekannter Mitarbeiter) liefern passende HTTP-Fehlercodes, keine 500er.
- `./mvnw test` läuft grün, inkl. neuer Tests für die Validierungsregeln.

