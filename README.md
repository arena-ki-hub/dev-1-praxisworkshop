# Installation Claude Code

Als Erstes möchten wir Claude Code installieren.

Dazu besuchst du die [offizielle Doku](https://code.claude.com/docs/de/quickstart) von Claude und hälst dich an die Anleitung.

# Einleitung

Das hier ist ein Brown-Field Projekt. Das bedeutet, das dieses Projekt schon Code enthält.

Um einen Überblick des Projekts zu bekommen, fragen wir Claude.

```bash
# Wechsel zunächst in den Manual Mode
Analysiere das Projekt und gib mir einen Überblick, ignoriere dabei die README.md
```

Claude hat das Projekt analysiert und uns die Informationen gegeben. Das hat uns aber einige Tokens gekostet, da das komplette Projekt analysiert wurde.

Damit Claude das nicht jedes Mal neu machen muss und unnötig Tokens verbraucht, legen wir uns eine `AGENTS.md` an.

# Projekt einrichten (AGENTS.md)

Die `AGENTS.md` Datei enthält Informationen über das Projekt, die ein KI-Agent in jeder Session braucht. `AGENTS.md` ist ein toolübergreifender Standard und wird von vielen Tools (z. B. Codex, Cursor, GitHub Copilot) gelesen.

```bash
# Wechsel in den Auto Mode
Erstelle anhand der zuvor gesammelten Informationen eine AGENTS.md. Speichere dabei aber nur die Informationen, die für die AGENTS.md relevant sind. Lege außerdem eine CLAUDE.md an, die nur `@AGENTS.md` enthält.

```

> **Hinweis:** Claude Code lädt automatisch nur die `CLAUDE.md`, nicht die `AGENTS.md`. Über den Import `@AGENTS.md` in der `CLAUDE.md` wird die `AGENTS.md` trotzdem in jede Session geladen. So gibt es nur eine Quelle für alle Tools. Ob die Datei geladen wurde, kannst du nach einem `/clear` mit `/context` prüfen (siehe nächster Abschnitt).

# Skills

An dieser Stelle möchten wir uns einen Überblick über den Kontext und Tokenverbrauch verschaffen.

Dies machen wir über den Skill `/context`.

## Claude Skills

```bash
/clear    # Leert den gesamten Kontext und startet somit eine neue Session
/context  # Zeigt Informationen über den aktuellen Kontext
/config   # Hier können Claude-Einstellungen vorgenommen werden
/status   # Zeigt Informationen über Claude selbst
/plugins  # Plugin Marktplatz um weitere Skills zu installieren
```

Um den Kontext zu leeren (und eine frische Sitzung zu starten) rufen wir den Skill `/clear` auf.

## Thrid-Party-Skills

Neben den integrierten Skills von Claude gibt es auch weitere Skills.

Bekannte sind:

- [Skills For Real Engineers - Matt Pocock](https://github.com/mattpocock/skills)
- [OpenSpec - Fission-AI](https://github.com/Fission-AI/openspec)

## Installation

Installiere jetzt die Skills von Mat Pocock.
