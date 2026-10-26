# Schritt 7 — Übung abgeschlossen

## Über diesen Branch

Alle sechs Schritte sind hier fertig umgesetzt — dieser Branch (`step-7-de`) enthält die **vollständige Referenzlösung** inklusive Schritt 6 und dient nur zur Kontrolle (z.B. um eine eigene Lösung zu vergleichen). Er ist kein eigener Übungsschritt.

Glückwunsch, wenn du bis hierhin selbst gekommen bist!

## Optionale Zusatzübung

Wer noch Zeit hat: Im Branch `step-8-de` gibt es eine **optionale Übung**, in der du einen eigenen Skill baust.

## Referenzwerte zum Vergleich

Wenn deine eigene Lösung andere Zahlen liefert als erwartet, kannst du hier gegenprüfen:

1. **App starten** — `./mvnw spring-boot:run`

2. **Swagger UI öffnen** — <http://localhost:8080/swagger-ui.html>

3. **Import** — `samples/zeiterfassung-import-beispiel.xlsx` über `POST /api/time-entries/import` importieren → **9 importierte Zeilen, 1 übersprungene** (`x.unbekannt`).

4. **Überstunden** — `GET /api/employees/1/overtime?from=2026-08-31&to=2026-09-06` für `a.schmidt` aufrufen → **Soll 32 h, Ist 20 h, Überstunden −12 h**.
