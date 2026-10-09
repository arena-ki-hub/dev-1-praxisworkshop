# Schritt 1 — Mitarbeiter-Verwaltung + Swagger UI

## Lernziel

Plan Mode und strukturiertes Prompting im Zusammenspiel mit Claude Code: Claude erst planen lassen, den Plan hinterfragen, dann umsetzen — statt direkt Code schreiben zu lassen.

## Die Methode dieses Schritts

**Plan Mode.** Mit `Shift+Tab` schaltest du zwischen den Modi um, bis in der Eingabezeile der Plan Mode angezeigt wird. In diesem Modus ändert Claude keine Dateien: Es liest sich ein, stellt Rückfragen und legt am Ende einen Plan zur Freigabe vor. Erst nach deiner Freigabe wird geschrieben. Für diese Aufgabe lohnt sich das, weil die Aufgabenstellung bewusst Lücken lässt — etwa, was bei einem doppelten Username passieren soll. Im Plan Mode fallen solche Lücken auf, solange sie noch eine Entscheidung sind und nicht schon 200 Zeilen Code.

**`/grilling`** (aus `mattpocock-skills`, auf `main` installiert). Der Skill nimmt einen fertigen Plan auseinander: Claude stellt dir in Runden Fragen zu allem, was der Plan offenlässt, und gibt zu jeder Frage seine eigene Empfehlung ab. Du rufst ihn auf, wenn der Plan steht — vor der Freigabe.

## Aufgabe

Das Backend soll Mitarbeiter (`Employee`) verwalten. Ein Mitarbeiter hat Vor- und Nachnamen, einen eindeutigen Username und eine wöchentliche Soll-Arbeitszeit in Stunden. Die REST-API soll die üblichen CRUD-Operationen anbieten: anlegen, auflisten, einzeln abrufen, aktualisieren, löschen. Außerdem soll Swagger UI eingebunden sein, damit sich die API im Browser ausprobieren lässt.

## Empfohlener Workflow

1. **In den Plan Mode wechseln** (`Shift+Tab`).

2. **Die Aufgabe in eigenen Worten prompten, den Plan lesen und mit `/grilling` hinterfragen.**

   ```bash
   /grilling Ich will eine REST-API zur Verwaltung von Mitarbeitern bauen: Vor- und Nachname, ein eindeutiger Username und die wöchentliche Soll-Arbeitszeit in Stunden, dazu die üblichen CRUD-Operationen und Swagger UI zum Ausprobieren im Browser. Spring Boot mit H2 wie in der AGENTS.md, keine Authentifizierung, kein Frontend.
   ```

   Die Aufgabenstellung lässt einige Fragen offen. Ohne Nachfrage entscheidet Claude sie selbst, ohne es zu erwähnen. Das Grilling bringt sie auf den Tisch und legt dir jede einzeln mit einer Empfehlung vor. In diesem Schritt zum Beispiel:

   - Was passiert bei einem doppelten Username?
   - Welche Felder sind Pflicht, welche optional?
   - Wie sieht eine Fehlerantwort aus — Statuscode und Body?
   - Was liefert der Abruf einer unbekannten ID?

   Deine Antworten gehen in den Plan ein. So entscheidest du die offenen Punkte vor der Umsetzung und nicht beim Lesen des fertigen Codes.

3. **Den Plan freigeben und umsetzen lassen.**

## Feature selbst testen (Pflicht)

Der Schritt ist erst fertig, wenn du die API in der laufenden Anwendung selbst ausprobiert hast — nicht schon, wenn Claude „fertig" meldet und `./mvnw test` grün ist.

1. **App starten** — in einem zweiten Terminal, oder von Claude starten lassen (`Starte die App im Hintergrund und sag mir, wenn sie läuft`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Swagger UI öffnen** — <http://localhost:8080/swagger-ui.html>. Im Dev Container ist Port 8080 weitergeleitet (Reiter „Ports" in VS Code); ein Strg+Klick auf die URL im Terminal öffnet sie direkt.

3. **Die Endpunkte per „Try it out" durchklicken:**
   - `POST /api/employees`: Mitarbeiter `a.schmidt` mit 40 Soll-Stunden anlegen, die ID aus der Antwort merken.
   - `GET /api/employees`: der neue Mitarbeiter steht in der Liste.
   - `POST /api/employees` noch einmal mit demselben Username → verständliche Fehlermeldung mit sinnvollem Statuscode (z. B. 409), **kein** 500 und kein Stacktrace.
   - `PUT /api/employees/{id}`: Soll-Stunden auf 32 ändern → `GET /api/employees/{id}` liefert 32.
   - `DELETE /api/employees/{id}` → danach liefert `GET /api/employees/{id}` 404.
   - Einmal einen leeren Username schicken → 400 statt 500.

4. **Wenn etwas nicht stimmt** — gib Claude die konkrete Anfrage und die Antwort aus der Swagger UI („`POST /api/employees` mit doppeltem Username liefert 500, Response: …") statt nur „geht nicht".

## Akzeptanzkriterien

- Die Anwendung startet mit `./mvnw spring-boot:run`.
- Unter `/swagger-ui.html` sind alle Mitarbeiter-Endpunkte sichtbar und über „Try it out" nutzbar.
- Ein doppelter Username wird mit einer sinnvollen Fehlermeldung abgelehnt (nicht mit HTTP 500).
- `./mvnw test` läuft grün.
- Du hast die Endpunkte selbst in der Swagger UI durchgeklickt — inklusive doppeltem Username und dem Abruf eines gelöschten Mitarbeiters.

## Weiter

Fertig? Dann weiter mit Schritt 2 (Zeiterfassungs-Einträge).

```bash
git reset --hard        # verwirft Änderungen an vorhandenen Dateien
git clean -fd           # entfernt neu angelegte Dateien
git checkout step-2
```

Statt die Befehle selbst zu tippen, kannst du Claude auch einfach sagen, was passieren soll:

```bash
Verwirf alle meine Änderungen, auch neu angelegte Dateien, und wechsle auf den Branch step-2.
```

Danach in Claude einmal `/clear`: Der Kontext aus diesem Schritt hilft im nächsten nicht und kostet bei jeder Nachricht erneut Tokens.
