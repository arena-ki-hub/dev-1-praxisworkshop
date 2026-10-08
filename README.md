# Übung abgeschlossen

Alle sechs Schritte sind hier fertig umgesetzt — dieser Branch (`step-7`) enthält die **vollständige Referenzlösung** inklusive Schritt 6 und dient nur zur Kontrolle (z.B. um eine eigene Lösung zu vergleichen). Er ist kein eigener Übungsschritt.

Glückwunsch, wenn du bis hierhin selbst gekommen bist!

Wer noch Zeit hat: Im Branch `step-8` gibt es eine **optionale Übung**, in der du einen eigenen Skill baust.

## Referenzwerte zum Vergleich

Wenn deine eigene Lösung andere Zahlen liefert als erwartet, kannst du hier gegenprüfen: App starten (`./mvnw spring-boot:run`), <http://localhost:8080/swagger-ui.html> öffnen und

1. `samples/zeiterfassung-import-beispiel.xlsx` über `POST /api/time-entries/import` importieren → **9 importierte Zeilen, 1 übersprungene** (`x.unbekannt`),
2. `GET /api/employees/1/overtime?from=2026-08-31&to=2026-09-06` für `a.schmidt` aufrufen → **Soll 32 h, Ist 20 h, Überstunden −12 h**.
