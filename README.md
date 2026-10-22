# Schritt 2 — Zeiterfassungs-Einträge

## Lernziel

Testgetriebene Entwicklung (Rot-Grün-Refactor) im Zusammenspiel mit Claude Code: erst die Tests, dann die Implementierung — statt Code schreiben und hinterher prüfen zu lassen.

## Die Methode dieses Schritts

Der Skill **`/tdd`** wurde zusammen mit `mattpocock-skills` auf `main` installiert und legt Claude auf den Rot-Grün-Refactor-Zyklus fest: erst ein fehlschlagender Test, dann die kleinste Implementierung, die ihn grün macht, erst danach Refactoring.

Entscheidend ist die Reihenfolge. Lässt du Claude erst implementieren und danach Tests schreiben, beschreiben die Tests, was der Code tut — auch dann, wenn er eine Regel falsch umsetzt. Ein Test, der vorher rot war und nach der Implementierung grün wird, belegt dagegen, dass genau diese Regel den Ausschlag gibt. Deshalb gilt: Ein Test, der ohne Implementierung schon grün ist, prüft nichts.

Für diesen Schritt passt das besonders gut, weil die drei Validierungsregeln vorgegeben sind. Sie lassen sich wörtlich in Testfälle übersetzen, samt der Grenzwerte (0 und 24 Stunden, heute gegen morgen), bevor eine Zeile Produktivcode existiert.

**`/grilling`** aus Schritt 1 bleibt nützlich: Bevor du den Plan für die Implementierung freigibst, nimmt der Skill ihn auseinander und legt dir jeden offenen Punkt einzeln mit einer Empfehlung vor.

## Aufgabe

Das Backend soll Zeiterfassungs-Einträge (`TimeEntry`) je Mitarbeiter verwalten. Ein Eintrag hat ein Datum, die Anzahl gebuchter Stunden, eine kurze Tätigkeitsbeschreibung sowie ein Kennzeichen, ob es sich um eine Abwesenheit (z. B. Urlaub) handelt. Die REST-API soll die üblichen CRUD-Operationen anbieten, diesmal im Kontext eines Mitarbeiters (`/api/employees/{employeeId}/time-entries`).

Validiert werden soll dabei:

- Stunden müssen größer als 0 und höchstens 24 sein.
- Das Datum darf nicht in der Zukunft liegen.
- Der referenzierte Mitarbeiter muss existieren.

## Empfohlener Workflow

1. **Die Tests mit `/tdd` schreiben lassen und rot sehen.**

   ```bash
   /tdd Schreib zuerst die Tests für die Zeiterfassungs-Einträge: die drei Validierungsregeln inklusive Grenzwerte und die CRUD-Operationen. Noch keine Implementierung — ich will die Tests zuerst rot sehen.
   ```

   Lass dir die fehlgeschlagenen Tests zeigen, bevor es weitergeht. Läuft einer schon grün, prüft er die Regel nicht.

2. **Den Plan für die Implementierung mit `/grilling` hinterfragen** und erst danach freigeben.

3. **Grün und Refactor durchlaufen lassen** — Claude ergänzt nur so viel Code, bis die Tests durchlaufen, und räumt erst danach auf.

## Feature selbst testen (Pflicht)

Grüne Tests heißen zunächst nur, dass Claudes Code Claudes Tests besteht. Ob die Zeiterfassung wirklich funktioniert, siehst du erst in der laufenden Anwendung.

1. **App starten** — in einem zweiten Terminal, oder von Claude starten lassen (`Starte die App im Hintergrund und sag mir, wenn sie läuft`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Swagger UI öffnen** — <http://localhost:8080/swagger-ui.html>. Im Dev Container ist Port 8080 weitergeleitet (Reiter „Ports" in VS Code); ein Strg+Klick auf die URL im Terminal öffnet sie direkt.

3. **Die Endpunkte per „Try it out" durchklicken** — die Seed-Daten enthalten die Mitarbeiter `a.schmidt`, `m.mueller` und `j.klein`, die IDs holst du dir über `GET /api/employees`:
   - `POST /api/employees/{employeeId}/time-entries`: heutiges Datum, 8 Stunden, Tätigkeit „Kundenprojekt", keine Abwesenheit → Eintrag wird angelegt.
   - `GET /api/employees/{employeeId}/time-entries`: der Eintrag steht in der Liste.
   - Einen zweiten Eintrag als Abwesenheit (z. B. Urlaub) anlegen.
   - Validierung prüfen: 0 Stunden, 25 Stunden, Datum von morgen → jeweils 400 mit verständlicher Meldung.
   - `POST /api/employees/999/time-entries` (unbekannter Mitarbeiter) → 404, **kein** 500.
   - Einen Eintrag per `PUT` ändern und per `DELETE` löschen → `GET` zeigt danach die Änderung bzw. den Eintrag nicht mehr.

4. **Wenn etwas nicht stimmt** — gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („25 Stunden liefern 500 statt 400, Response: …") statt nur „geht nicht".

## Akzeptanzkriterien

- Neue Endpunkte sind über Swagger UI sichtbar und nutzbar.
- Ungültige Eingaben (Stunden außerhalb 0–24, Datum in der Zukunft, unbekannter Mitarbeiter) liefern passende HTTP-Fehlercodes, keine 500er.
- `./mvnw test` läuft grün, inkl. neuer Tests für die Validierungsregeln.
- Du hast die Endpunkte und alle drei Validierungsregeln selbst in der Swagger UI durchgeklickt.

## Weiter

Fertig? Dann weiter mit Schritt 3 (Excel-Import).

```bash
git reset --hard        # verwirft Änderungen an vorhandenen Dateien
git clean -fd           # entfernt neu angelegte Dateien
git checkout step-3
```

Statt die Befehle selbst zu tippen, kannst du Claude auch einfach sagen, was passieren soll:

```bash
Verwirf alle meine Änderungen, auch neu angelegte Dateien, und wechsle auf den Branch step-3.
```

Danach in Claude einmal `/clear`: Der Kontext aus diesem Schritt hilft im nächsten nicht und kostet bei jeder Nachricht erneut Tokens.
