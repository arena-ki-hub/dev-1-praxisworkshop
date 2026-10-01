# Optionale Übung: Schritt 8 — Eigenen Skill bauen

**Lernziel:** Einen wiederkehrenden Ablauf als eigenen Skill verpacken — und verstehen, wie sich ein Skill von der `AGENTS.md` unterscheidet.

**Ausgangslage:** Dieser Branch enthält die vollständige Referenzlösung aus Schritt 7, inklusive des Überstunden-Endpoints `GET /api/employees/{id}/overtime?from=...&to=...`. Um Überstunden abzufragen, muss man bisher die Mitarbeiter-ID kennen und die URL von Hand zusammenbauen. Genau so etwas lässt sich gut als Skill verpacken.

**Was ist ein Skill?** Ein Skill ist eine Anleitung für Claude, die nur bei Bedarf geladen wird. Die `AGENTS.md` liegt in jeder Session komplett im Kontext. Von einem Skill steht dagegen nur der Name und die Beschreibung im Kontext. Den eigentlichen Inhalt lädt Claude erst, wenn der Skill aufgerufen wird oder die Beschreibung zur Frage passt. Ein Projekt-Skill ist ein Ordner mit einer `SKILL.md`:

```text
.claude/skills/<name>/SKILL.md
```

Die `SKILL.md` beginnt mit einem YAML-Frontmatter, darunter folgen die Anweisungen in Markdown:

```markdown
---
name: mein-skill
description: Was der Skill tut und wann Claude ihn benutzen soll.
---

Anweisungen für Claude, Schritt für Schritt.
Übergebene Argumente stehen in $ARGUMENTS.
```

Aufgerufen wird der Skill mit `/mein-skill` plus Argumenten. Die `description` entscheidet außerdem, ob Claude den Skill auch ohne Slash-Command von selbst benutzt.

**Aufgabe:** Baue einen Skill `/ueberstunden`, der die Überstunden eines Mitarbeiters für einen Zeitraum abfragt, z.B.:

```bash
/ueberstunden a.schmidt 2026-09-01 2026-09-30
```

Der Skill soll:
- den Username über `GET /api/employees` in die Mitarbeiter-ID übersetzen,
- den Überstunden-Endpoint per `curl` aufrufen,
- Soll-, Ist- und Überstunden kurz auf Deutsch zusammenfassen.

Die App muss dafür laufen (in einem zweiten Terminal):

```bash
./mvnw spring-boot:run
```

Sie ist dann unter `http://localhost:8080` erreichbar. Die Seed-Daten enthalten die Mitarbeiter `a.schmidt`, `m.mueller` und `j.klein`.

**Empfohlener Workflow:** Schreib den Skill zusammen mit Claude, z.B. mit dem Skill `skill-creator` oder nach den Tipps aus `mattpocock-skills:writing-for-agents`. Starte danach mit `/clear` neu und prüfe mit `/context`, dass vom Skill nur Name und Beschreibung im Kontext liegen. Ruf ihn dann einmal per `/ueberstunden` auf und stell anschließend eine Frage ohne Slash-Command:

```bash
Wie viele Überstunden hat a.schmidt im September 2026 gemacht?
```

> **Aufgabe:** Benutzt Claude deinen Skill bei dieser Frage von selbst? Falls nicht, schärfe die `description` nach und probiere es erneut. Vergleicht eure Beschreibungen mit dem Nachbarn: Welche Formulierung funktioniert zuverlässiger?

**Akzeptanzkriterien:**
- `/ueberstunden <username> <von> <bis>` liefert dieselben Werte wie der Endpoint.
- Ein unbekannter Username und ein vertauschter Zeitraum (`von` nach `bis`) werden verständlich gemeldet, statt dass Claude rät oder eine Fehlermeldung roh weitergibt.
- Claude benutzt den Skill auch ohne Slash-Command, wenn nach Überstunden eines Mitarbeiters gefragt wird.
