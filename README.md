# Projektüberblick

Dieses Repository ist die Code-Basis für den Praxisworkshop zu Agentischem Arbeiten: ein einfaches Spring-Boot-Backend, das Überstunden aus Zeiterfassungsdaten berechnet (Mitarbeiter-Verwaltung, Zeiterfassungs-Einträge, Excel-Import, Überstunden-Berechnung — fachliche Details stehen in `AGENTS.md`).

Die Übung ist in mehrere aufeinander aufbauende Schritte gegliedert, die jeweils auf einem eigenen Branch liegen:

- **main** (dieser Branch): Einstieg in Claude Code (Installation, Statuszeile, `AGENTS.md`, Skills) — Ziel: Grundlagen für die folgenden Übungsschritte legen
- **step-1**: Mitarbeiter-Verwaltung + Swagger UI — Ziel: Plan Mode & strukturiertes Prompting
- **step-2**: Zeiterfassungs-Einträge — Ziel: Testgetriebene Entwicklung (Rot-Grün-Refactor)
- **step-3**: Excel-Import — Ziel: Plan Mode beim Einbinden einer neuen Dependency und einem komplexeren Feature
- **step-4**: Überstunden-Berechnung — Ziel: Plan Mode bei einer fachlich komplexeren Aufgabe mit mehreren Teilschritten
- **step-5**: Bugfixing — Ziel: Debugging-Workflow
- **step-6**: Konfigurierbares Spalten-Mapping für den Excel-Import — Ziel: Code Review vor einem Refactoring, Subagents
- **step-7**: Vollständige Referenzlösung — Kontrollbranch zum Vergleich, kein eigener Übungsschritt
- **step-8** (optional): Eigenen Skill bauen — Ziel: einen wiederkehrenden Ablauf als Skill verpacken

Jeder Übungsschritt endet mit einem manuellen Test: App starten, Swagger UI im Browser öffnen, das neue Feature selbst durchklicken. Warum das zur Aufgabe gehört und wie es abläuft, steht unten unter „Features selbst testen".

# Arbeiten mit den Übungsschritten

Jeder Schritt liegt auf einem eigenen Branch und bringt seinen eigenen Ausgangsstand mit: `step-2` enthält bereits die fertige Lösung aus Schritt 1, `step-3` die aus Schritt 2 und so weiter. Du kannst also an jeder Stelle einsteigen — aber nichts aus einem Schritt in den nächsten mitnehmen. **Beim Wechsel wird deine Arbeit verworfen, und das ist beabsichtigt.**

So wechselst du von einem Schritt zum nächsten:

```bash
git reset --hard        # verwirft Änderungen an vorhandenen Dateien
git clean -fd           # entfernt neu angelegte Dateien
git checkout step-2
```

Statt die Befehle selbst zu tippen, kannst du Claude auch einfach sagen, was passieren soll:

```bash
Verwirf alle meine Änderungen, auch neu angelegte Dateien, und wechsle auf den Branch step-2.
```

Beginne jeden Schritt außerdem mit `/clear` in Claude: Der Kontext des vorigen Schritts hilft im nächsten nicht und kostet bei jeder Nachricht erneut Tokens (mehr dazu unten unter „Kontext prüfen").

# Claude Code starten

Claude Code ist im Dev Container bereits installiert. Öffne in VS Code ein Terminal (Strg+Ö) – es läuft im Container – und starte Claude:

```bash
claude
```

Beim ersten Start meldest du dich mit deinem Konto an. Die Anmeldung und deine Claude-Einstellungen bleiben auch nach einem „Dev Containers: Rebuild Container" erhalten.

# Projekt analysieren

Das hier ist ein Brown-Field Projekt. Das bedeutet, das dieses Projekt schon Code enthält.

Um einen Überblick des Projekts zu bekommen, fragen wir Claude.

Mit `Shift+Tab` schaltest du zwischen den Modi um; der aktive Modus steht in der Eingabezeile.

```bash
# Wechsel zunächst in den Manual Mode
Analysiere das Projekt und gib mir einen Überblick, ignoriere dabei die README.md
```

Claude hat das Projekt analysiert und uns die Informationen gegeben. Das hat uns aber einige Tokens gekostet, da das komplette Projekt analysiert wurde.

> **Aufgabe:** Schau in deine Statuszeile: Wie viele Tokens hat die Analyse verbraucht? Vergleiche die Tokens mit deinen Kollegen (stellt vorher mit `/model` sicher, dass alle dasselbe Modell nutzen). Trotz gleichem Prompt und gleichem Projekt weichen die Zahlen ab, weil Claude jedes Mal andere Dateien liest und anders antwortet.

# Statuszeile einrichten

Die Statuszeile ist die Leiste unten in der Claude CLI. Sie zeigt uns ab jetzt laufend an, wie viele Tokens wir verbrauchen, wie voll das Kontextfenster ist, was uns die Session kostet und wie es um unsere Nutzungslimits steht.

```bash
/statusline Zeige mir Modell, Branch, Input-/Output-Tokens der Session, Kontextfenster-Auslastung (Prozent und Tokens), das 5-Stunden- und 7-Tage-Limit (Prozent und Reset-Zeit/-Tag), die Kosten der Session sowie das aktuelle Reasoning-Effort-Level an. Frag mich vorher, wie ich das Ganze gestaltet haben möchte (Layout, Farben, Text/Icons/beides, zusätzliche Infos), bevor du das Skript schreibst.
```

> **Aufgabe:** Beantworte Claudes Rückfragen zu Layout, Farben, Text/Icons und möglichen Zusatzinfos nach deinem eigenen Geschmack. Dadurch sieht am Ende jede Statuszeile im Raum anders aus: jeder beantwortet die Rückfragen anders, und selbst bei identischen Antworten arbeitet das Modell nicht deterministisch. Falls die Limit-Anzeige bei dir leer bleibt, liegt das an deiner Account-/Anmeldeart – das Skript ist trotzdem korrekt.

> **Tipp:** Funktioniert etwas nicht – die Statuszeile bleibt leer, ein Wert fehlt oder das Skript wirft einen Fehler – dann beschreibe das Problem einfach Claude und lass es dich selbst beheben. Das gilt im ganzen Workshop: Erster Versuch bei Fehlern ist immer, die KI um den Fix zu bitten, statt selbst im Skript oder Code zu suchen.

# Projekt einrichten (AGENTS.md)

Damit Claude das Projekt nicht in jeder Session erneut komplett analysieren muss und dabei unnötig Tokens verbraucht, legen wir uns eine `AGENTS.md` an. Die Datei enthält Informationen über das Projekt, die ein KI-Agent in jeder Session braucht. `AGENTS.md` ist ein toolübergreifender Standard und wird von vielen Tools (z. B. Codex, Cursor, GitHub Copilot) gelesen.

```bash
# Wechsel in den Auto Mode (Shift+Tab)
Erstelle anhand der zuvor gesammelten Informationen eine AGENTS.md. Speichere dabei aber nur die Informationen, die für die AGENTS.md relevant sind. Lege außerdem eine CLAUDE.md an, die nur `@AGENTS.md` enthält.

```

> **Hinweis:** Das `@`-Zeichen bindet den Inhalt einer Datei in den Kontext ein. Hier als Import `@AGENTS.md` in der `CLAUDE.md`, wodurch die `AGENTS.md` trotzdem in jede Session geladen wird, obwohl Claude Code automatisch nur die `CLAUDE.md` lädt. So gibt es nur eine Quelle für alle Tools. Du kannst `@<Dateiname>` genauso direkt in einer Chat-Nachricht verwenden (z. B. `Erkläre mir @Employee.java`), um gezielt eine Datei in den Kontext zu holen. Ob eine Datei geladen wurde, kannst du nach einem `/clear` mit `/context` prüfen (siehe nächster Abschnitt).

# Skills

## Commands vs. Skills

Claude Code unterscheidet zwischen **Commands** und **Skills**:

- **Commands** sind fest eingebaute Steuerbefehle für die CLI selbst (Kontext leeren, Einstellungen ändern, Statuszeile einrichten, …). Sie beginnen immer mit `/` und fügen Claude keine neuen fachlichen Fähigkeiten hinzu.
- **Skills** sind paketierte Anleitungen für wiederkehrende Aufgaben (z. B. ein Code Review durchführen oder einen Report erstellen). Claude lädt einen Skill entweder automatisch, wenn die Aufgabe dazu passt, oder er wird manuell per `/<skill-name>` aufgerufen. Über `/plugins` lassen sich weitere Skills installieren (siehe Abschnitt „Third-Party-Skills" unten).

Zunächst die wichtigsten Commands:

```bash
/clear    # Leert den gesamten Kontext und startet somit eine neue Session
/context  # Zeigt Informationen über den aktuellen Kontext
/config   # Hier können Claude-Einstellungen vorgenommen werden
/status   # Zeigt Informationen über Claude selbst
/plugins  # Plugin Marktplatz um weitere Skills zu installieren
/statusline  # Richtet die Statuszeile ein
/agents   # Subagents anzeigen und eigene anlegen
```

> **Tipp:** Beginnst du eine Nachricht mit `!`, führt Claude Code den Rest direkt als Shell-Befehl aus (z. B. `!git status`), ohne dass Claude dafür gefragt werden muss. Praktisch für schnelle Checks zwischendurch, deren Ausgabe trotzdem im Kontext landet.

## Kontext prüfen

Das Kontextfenster ist das Arbeitsgedächtnis von Claude. Alles darin kostet Tokens: der Systemprompt, die Tools, die `CLAUDE.md`/`AGENTS.md`, jede Nachricht und jede gelesene Datei. Wird es voll, fasst Claude den bisherigen Verlauf automatisch zusammen, und dabei gehen Details verloren.

```bash
/clear    # Neue Session starten
/context  # Anzeigen, was schon im Kontext liegt
```

> **Aufgabe:** Die Statuszeile zeigt schon vor der ersten Frage einige Prozent an. Finde mit `/context` heraus, woraus sie bestehen. Taucht deine `AGENTS.md` unter den Memory-Dateien auf?

Nutze `/clear` immer, wenn du mit einer neuen Aufgabe beginnst. Alter Kontext kostet sonst bei jeder Nachricht erneut Tokens.

## Third-Party-Skills

Neben den integrierten Skills von Claude gibt es auch Skills von Drittanbietern.

### Im Workshop genutzt

Wir installieren [Skills For Real Engineers](https://github.com/mattpocock/skills) von Matt Pocock und nutzen sie im weiteren Verlauf des Workshops. Sie liegen im offiziellen Marketplace von Claude Code, es muss also vorher kein Marketplace hinzugefügt werden:

```bash
claude plugins install mattpocock-skills
```

Oder innerhalb einer laufenden Session:

```bash
/plugin install mattpocock-skills
```

> **Hinweis:** Der offizielle Marketplace hinkt dem Repository oft um Tage oder Wochen hinterher. Wenn du stattdessen immer die aktuellste Version direkt aus dem Repository willst, nutze dessen eigenen Marketplace (Auto-Update dafür unter `/plugin` → Marketplaces aktivieren, bei Marketplaces außerhalb von Anthropic ist es standardmäßig aus):
>
> ```bash
> claude plugin uninstall mattpocock-skills@claude-plugins-official
> claude plugin marketplace add mattpocock/skills
> claude plugin install mattpocock-skills@mattpocock
> ```

### Weitere Beispiele für eigene Projekte

> ⚠️ **Nur zur Orientierung:** Die folgenden Skills werden in diesem Workshop **nicht** installiert und **nicht** verwendet. Sie sind als Anregung gedacht, was du dir nach dem Workshop für deine eigenen Projekte anschauen kannst.

| Skill | Wofür |
| --- | --- |
| [Superpowers - Jesse Vincent](https://github.com/obra/superpowers) | Umfangreiches Framework mit Methodik für Brainstorming, Planung, TDD und Code Review |
| [OpenSpec - Fission-AI](https://github.com/Fission-AI/openspec) | Spec-getriebene Entwicklung: erst Spezifikation abstimmen, dann implementieren |

# Features selbst testen

Ein grüner `./mvnw test` heißt zunächst nur: Claudes Code besteht Claudes Tests. Ob das Feature wirklich funktioniert, siehst du erst in der laufenden Anwendung — und genau dieser Blick fällt beim agentischen Arbeiten als Erstes unter den Tisch. Deshalb endet jeder Übungsschritt mit einem manuellen Test; die konkreten Fälle stehen jeweils im README des Schritts unter „Feature selbst testen".

1. App starten – entweder selbst in einem zweiten Terminal:

   ```bash
   ./mvnw spring-boot:run
   ```

   oder von Claude starten lassen (`Starte die App im Hintergrund und sag mir, wenn sie läuft`).

2. Swagger UI im Browser öffnen: <http://localhost:8080/swagger-ui.html>. Der Dev Container leitet Port 8080 automatisch weiter (Reiter „Ports" in VS Code), ein Strg+Klick auf die URL im Terminal öffnet sie direkt.

3. Die Endpunkte mit „Try it out" durchklicken – immer den Happy Path **und** mindestens einen Fehlerfall. Anschließend prüfen, ob die Daten wirklich angekommen sind (`GET`-Endpunkt oder H2-Konsole).

4. Geht etwas schief, gib Claude die konkrete Anfrage und die Antwort („`POST /api/employees` mit doppeltem Username liefert 500, Response: …") statt nur „geht nicht". Damit hat es genau den Kontext, den der Testlauf nicht liefert.

> **Tipp:** Die H2-Konsole unter <http://localhost:8080/h2-console> (JDBC-URL `jdbc:h2:mem:backend`, Benutzer `sa`, kein Passwort) zeigt dir, was tatsächlich in der Datenbank steht. Die Datenbank liegt im Arbeitsspeicher: nach jedem Neustart der App gelten wieder die Seed-Daten aus `data.sql`.

# Weiter

Fertig? Dann weiter mit Schritt 1 (Mitarbeiter-Verwaltung + Swagger UI).

```bash
git reset --hard        # verwirft Änderungen an vorhandenen Dateien
git clean -fd           # entfernt neu angelegte Dateien
git checkout step-1
```

Statt die Befehle selbst zu tippen, kannst du Claude auch einfach sagen, was passieren soll:

```bash
Verwirf alle meine Änderungen, auch neu angelegte Dateien, und wechsle auf den Branch step-1.
```

Deine `AGENTS.md` und `CLAUDE.md` gehen dabei verloren — jeder Übungsschritt bringt aber bereits eine eigene mit.

Danach in Claude einmal `/clear`: Der Kontext aus diesem Schritt hilft im nächsten nicht und kostet bei jeder Nachricht erneut Tokens.
