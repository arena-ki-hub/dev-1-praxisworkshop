# Übung: Schritt 1 — Mitarbeiter-Verwaltung + Swagger UI

**Lernziel:** Plan Mode & strukturiertes Prompting im Zusammenspiel mit Claude Code.

**Aufgabe:** Baue eine REST-API zur Verwaltung von Mitarbeitern (`Employee`): Vor- und Nachname, ein eindeutiger Username sowie die wöchentliche Soll-Arbeitszeit in Stunden. Die API soll die üblichen CRUD-Operationen unterstützen (Anlegen, Auflisten, Einzelabruf, Aktualisieren, Löschen). Ergänze außerdem Swagger UI, damit sich die API im Browser testen lässt.

**Empfohlener Workflow:** Plan Mode nutzen, den Plan mit `/grilling` hinterfragen (z.B.: Wie werden doppelte Usernames behandelt? Welche Felder sind Pflicht? Wie sieht die Fehlerantwort aus?), erst danach umsetzen lassen.

**Akzeptanzkriterien:**
- Die Anwendung startet mit `./mvnw spring-boot:run` (bzw. `mvnw.cmd spring-boot:run` unter Windows).
- Unter `/swagger-ui.html` sind alle Mitarbeiter-Endpunkte sichtbar und über "Try it out" nutzbar.
- Ein doppelter Username wird mit einer sinnvollen Fehlermeldung abgelehnt (nicht mit HTTP 500).
- `./mvnw test` läuft grün.

