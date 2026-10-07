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

# Claude Code starten

Claude Code ist im Dev Container bereits installiert. Öffne in VS Code ein Terminal (Strg+Ö) – es läuft im Container – und starte Claude:

```bash
claude
```

Beim ersten Start meldest du dich mit deinem Konto an. Die Anmeldung und deine Claude-Einstellungen bleiben auch nach einem „Dev Containers: Rebuild Container" erhalten.

# Projekt analysieren

Das hier ist ein Brown-Field Projekt. Das bedeutet, das dieses Projekt schon Code enthält.

Um einen Überblick des Projekts zu bekommen, fragen wir Claude.

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

# Projekt einrichten (AGENTS.md)

Damit Claude das Projekt nicht in jeder Session erneut komplett analysieren muss und dabei unnötig Tokens verbraucht, legen wir uns eine `AGENTS.md` an. Die Datei enthält Informationen über das Projekt, die ein KI-Agent in jeder Session braucht. `AGENTS.md` ist ein toolübergreifender Standard und wird von vielen Tools (z. B. Codex, Cursor, GitHub Copilot) gelesen.

```bash
# Wechsel in den Auto Mode
Erstelle anhand der zuvor gesammelten Informationen eine AGENTS.md. Speichere dabei aber nur die Informationen, die für die AGENTS.md relevant sind. Lege außerdem eine CLAUDE.md an, die nur `@AGENTS.md` enthält.

```

> **Hinweis:** Das `@`-Zeichen bindet den Inhalt einer Datei in den Kontext ein. Hier als Import `@AGENTS.md` in der `CLAUDE.md`, wodurch die `AGENTS.md` trotzdem in jede Session geladen wird, obwohl Claude Code automatisch nur die `CLAUDE.md` lädt. So gibt es nur eine Quelle für alle Tools. Du kannst `@<Dateiname>` genauso direkt in einer Chat-Nachricht verwenden (z. B. `Erkläre mir @Employee.java`), um gezielt eine Datei in den Kontext zu holen. Ob eine Datei geladen wurde, kannst du nach einem `/clear` mit `/context` prüfen (siehe nächster Abschnitt).

# Skills

## Commands vs. Skills

Claude Code unterscheidet zwischen **Commands** und **Skills**:

- **Commands** sind fest eingebaute Steuerbefehle für die CLI selbst (Kontext leeren, Einstellungen ändern, Statuszeile einrichten, …). Sie beginnen immer mit `/` und fügen Claude keine neuen fachlichen Fähigkeiten hinzu.
- **Skills** sind paketierte Anleitungen für wiederkehrende Aufgaben (z. B. ein Code Review durchführen oder einen Report erstellen). Claude lädt einen Skill entweder automatisch, wenn die Aufgabe dazu passt, oder er wird manuell per `/<skill-name>` aufgerufen. Über `/plugins` lassen sich weitere Skills installieren (siehe Abschnitt „Thrid-Party-Skills" unten).

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

## Thrid-Party-Skills

Neben den integrierten Skills von Claude gibt es auch weitere Skills.

Bekannte sind:

- [Skills For Real Engineers - Matt Pocock](https://github.com/mattpocock/skills)
- [OpenSpec - Fission-AI](https://github.com/Fission-AI/openspec)

## Installation

Installiere jetzt die Skills von Mat Pocock.
