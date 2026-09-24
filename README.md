# Installation Claude Code

Als Erstes möchten wir Claude Code installieren.

Dazu besuchst du die [offizielle Doku](https://code.claude.com/docs/de/quickstart) von Claude und hälst dich an die Anleitung.

# Statuszeile einrichten

Die Statuszeile ist die Leiste unten in der Claude CLI. Sie zeigt uns ab jetzt laufend an, wie viele Tokens wir verbrauchen und wie voll das Kontextfenster ist.

```bash
/statusline Zeige den Tokenverbrauch der Session (Input und Output) und die Auslastung des Kontextfensters in Prozent an. Verwende kein jq, sondern nur grep und sed, damit es auch in Git Bash unter Windows funktioniert.
```

Der Hinweis auf `jq` ist wichtig: Ohne ihn erzeugt Claude oft ein Skript, das `jq` benötigt. Das ist in Git Bash unter Windows nicht installiert, und die Statuszeile bleibt leer.

> **Aufgabe:** Vergleiche deine Statuszeile und das erzeugte Skript mit denen deines Nachbarn. Obwohl ihr denselben Prompt benutzt habt, sehen sie vermutlich unterschiedlich aus. KI ist nicht deterministisch: Dieselbe Eingabe führt nicht immer zum selben Ergebnis.

# Einleitung

Das hier ist ein Brown-Field Projekt. Das bedeutet, das dieses Projekt schon Code enthält.

Um einen Überblick des Projekts zu bekommen, fragen wir Claude.

```bash
# Wechsel zunächst in den Manual Mode
Analysiere das Projekt und gib mir einen Überblick, ignoriere dabei die README.md
```

Claude hat das Projekt analysiert und uns die Informationen gegeben. Das hat uns aber einige Tokens gekostet, da das komplette Projekt analysiert wurde.

> **Aufgabe:** Schau in deine Statuszeile: Wie viele Tokens hat die Analyse verbraucht? Vergleiche die Tokens mit deinen Kollegen (stellt vorher mit `/model` sicher, dass alle dasselbe Modell nutzen). Trotz gleichem Prompt und gleichem Projekt weichen die Zahlen ab, weil Claude jedes Mal andere Dateien liest und anders antwortet. **Notiere dir die Zahl**, wir brauchen sie später noch.

Damit Claude das nicht jedes Mal neu machen muss und unnötig Tokens verbraucht, legen wir uns eine `AGENTS.md` an.

# Projekt einrichten (AGENTS.md)

Die `AGENTS.md` Datei enthält Informationen über das Projekt, die ein KI-Agent in jeder Session braucht. `AGENTS.md` ist ein toolübergreifender Standard und wird von vielen Tools (z. B. Codex, Cursor, GitHub Copilot) gelesen.

```bash
# Wechsel in den Auto Mode
Erstelle anhand der zuvor gesammelten Informationen eine AGENTS.md. Speichere dabei aber nur die Informationen, die für die AGENTS.md relevant sind. Lege außerdem eine CLAUDE.md an, die nur `@AGENTS.md` enthält.

```

> **Hinweis:** Claude Code lädt automatisch nur die `CLAUDE.md`, nicht die `AGENTS.md`. Über den Import `@AGENTS.md` in der `CLAUDE.md` wird die `AGENTS.md` trotzdem in jede Session geladen. So gibt es nur eine Quelle für alle Tools. Ob die Datei geladen wurde, kannst du nach einem `/clear` mit `/context` prüfen (siehe nächster Abschnitt).

# Skills

## Claude Skills

```bash
/clear    # Leert den gesamten Kontext und startet somit eine neue Session
/context  # Zeigt Informationen über den aktuellen Kontext
/config   # Hier können Claude-Einstellungen vorgenommen werden
/status   # Zeigt Informationen über Claude selbst
/plugins  # Plugin Marktplatz um weitere Skills zu installieren
/statusline  # Richtet die Statuszeile ein
/agents   # Subagents anzeigen und eigene anlegen
```

## Überprüfung: Spart die AGENTS.md Tokens?

An dieser Stelle möchten wir uns einen Überblick über den Kontext und Tokenverbrauch verschaffen und prüfen, ob sich die `AGENTS.md` lohnt.

Das Kontextfenster ist das Arbeitsgedächtnis von Claude. Alles darin kostet Tokens: der Systemprompt, die Tools, die `CLAUDE.md`/`AGENTS.md`, jede Nachricht und jede gelesene Datei. Wird es voll, fasst Claude den bisherigen Verlauf automatisch zusammen, und dabei gehen Details verloren.

```bash
/clear    # Neue Session starten
/context  # Anzeigen, was schon im Kontext liegt
```

> **Aufgabe 1:** Die Statuszeile zeigt schon vor der ersten Frage einige Prozent an. Finde mit `/context` heraus, woraus sie bestehen. Taucht deine `AGENTS.md` unter den Memory-Dateien auf?

Stelle jetzt denselben Prompt wie in der Einleitung:

```bash
Analysiere das Projekt und gib mir einen Überblick, ignoriere dabei die README.md
```

> **Aufgabe 2:** Vergleiche den Tokenverbrauch mit der Zahl, die du dir notiert hast. Hat die `AGENTS.md` Tokens gespart? Schau auch, ob Claude diesmal weniger Dateien gelesen hat. Falls kaum etwas gespart wurde: Was müsste in der `AGENTS.md` stehen, damit Claude nicht wieder alles liest?

Nutze `/clear` immer, wenn du mit einer neuen Aufgabe beginnst. Alter Kontext kostet sonst bei jeder Nachricht erneut Tokens.

## Thrid-Party-Skills

Neben den integrierten Skills von Claude gibt es auch weitere Skills.

Bekannte sind:

- [Skills For Real Engineers - Matt Pocock](https://github.com/mattpocock/skills)
- [OpenSpec - Fission-AI](https://github.com/Fission-AI/openspec)

## Installation

Installiere jetzt die Skills von Mat Pocock.
