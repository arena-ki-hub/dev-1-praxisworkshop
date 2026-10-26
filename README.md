# Optional exercise: Step 8 — Build your own skill

## Learning goal

Package a recurring workflow as your own skill — and understand how a skill differs from `AGENTS.md`.

## The method of this step

**What is a skill?** A skill is a set of instructions for Claude that is only loaded when needed. `AGENTS.md` is fully in the context of every session. For a skill, only the name and description are in the context. Claude loads the actual content only when the skill is invoked or when the description matches the question. A project skill is a folder with a `SKILL.md`:

```text
.claude/skills/<name>/SKILL.md
```

The `SKILL.md` starts with YAML frontmatter, followed by the instructions in Markdown:

```markdown
---
name: my-skill
description: What the skill does and when Claude should use it.
---

Instructions for Claude, step by step.
Passed arguments are available in $ARGUMENTS.
```

You invoke the skill with `/my-skill` plus arguments. The `description` also decides whether Claude uses the skill on its own, without a slash command.

## Starting point

This branch contains the complete reference solution from step 7, including the overtime endpoint `GET /api/employees/{id}/overtime?from=...&to=...`. To query overtime, you currently have to know the employee ID and assemble the URL by hand. That is exactly the kind of thing that is well suited to be packaged as a skill.

## Task

Build a skill `/overtime` that queries the overtime of an employee for a period, e.g.:

```bash
/overtime a.schmidt 2026-09-01 2026-09-30
```

The skill should:

- translate the username into the employee ID via `GET /api/employees`,
- call the overtime endpoint with `curl`,
- briefly summarize target, actual and overtime hours in English.

The app has to be running for this (in a second terminal):

```bash
./mvnw spring-boot:run
```

It is then available at `http://localhost:8080`. The seed data contains the employees `a.schmidt`, `m.mueller` and `j.klein`.

## Recommended workflow

Write the skill together with Claude, e.g. with the `skill-creator` skill or following the tips from `mattpocock-skills:writing-for-agents`. Then start over with `/clear` and check with `/context` that only the name and description of the skill are in the context. Then invoke it once via `/overtime` and afterwards ask a question without a slash command:

```bash
How many overtime hours did a.schmidt work in September 2026?
```

> **Task:** Does Claude use your skill on its own for this question? If not, sharpen the `description` and try again. Compare your descriptions with your neighbor's: which wording works more reliably?

## Check the result yourself (mandatory)

A skill that summarizes plausible-sounding numbers goes unnoticed as long as nobody double-checks. So call the endpoint yourself once:

1. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html> (the app is already running in the second terminal; in the Dev Container port 8080 is forwarded, see the "Ports" tab).

2. **Call the endpoint** — `GET /api/employees` → get the ID of `a.schmidt`, then call `GET /api/employees/{id}/overtime?from=2026-09-01&to=2026-09-30`.

3. **Compare the numbers** — Target, actual and overtime hours must match exactly what `/overtime a.schmidt 2026-09-01 2026-09-30` summarizes — including the sign (undertime is negative).

4. **Error cases** — Then play through the two error cases: unknown username and swapped period.

## Acceptance criteria

- `/overtime <username> <from> <to>` returns the same values as the endpoint.
- An unknown username and a swapped period (`from` after `to`) are reported clearly, instead of Claude guessing or passing on a raw error message.
- Claude uses the skill without a slash command when asked about the overtime of an employee.
- You have double-checked the skill's numbers once via the Swagger UI.
