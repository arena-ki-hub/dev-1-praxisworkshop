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

**Akzeptanzkriterien:**
- Endpoint liefert `{ employeeId, from, to, sollStunden, istStunden, ueberstunden }`.
- Ein Werktag ganz ohne Buchung zählt voll als Soll-Stunden (führt zu Minderstunden), ein Wochenendtag mit Buchung zählt voll als Überstunden.
- Ein vertauschter Zeitraum (`from` nach `to`) liefert HTTP 400, ein unbekannter Mitarbeiter HTTP 404.
- `./mvnw test` läuft grün, inkl. eines Tests mit gemischtem Szenario (Werktage, ein Urlaubstag, ein Wochenendtag mit Buchung).

