🇩🇪 [Deutsche Version](README.md)

# Project overview

This repository is the code base for the practical workshop on agentic working: a simple Spring Boot backend that calculates overtime from time-tracking data (employee management, time entries, Excel import, overtime calculation — domain details are in `AGENTS.md`).

The exercise is divided into several consecutive steps, each on its own branch. Every step exists in two language versions: `-de` (German) and `-en` (English). This README belongs to the English version, so the links below point to the `-en` branches.

- **main** (this branch): Introduction to Claude Code (installation, status line, `AGENTS.md`, skills) — goal: lay the foundations for the following exercise steps
- **step-1-en**: Employee management + Swagger UI — goal: Plan Mode & structured prompting
- **step-2-en**: Time entries — goal: test-driven development (red-green-refactor)
- **step-3-en**: Excel import — goal: Plan Mode when adding a new dependency and a more complex feature
- **step-4-en**: Overtime calculation — goal: Plan Mode for a domain-wise more complex task with several subtasks
- **step-5-en**: Bugfixing — goal: debugging workflow
- **step-6-en**: Configurable column mapping for the Excel import — goal: code review before a refactoring, subagents
- **step-7-en**: Complete reference solution — control branch for comparison, not an exercise step of its own
- **step-8-en** (optional): Build your own skill — goal: package a recurring workflow as a skill

Every exercise step ends with a manual test: start the app, open the Swagger UI in the browser, click through the new feature yourself. Why this is part of the task and how it works is described below under "Test features yourself".

# Working with the exercise steps

Every step lives on its own branch and brings its own starting point: `step-2-en` already contains the finished solution from step 1, `step-3-en` the one from step 2, and so on. So you can join at any point — but you cannot take anything from one step into the next. **When you switch, your work is discarded, and that is intentional.**

The commands for switching are at the end of each step under "Next" — you can also simply let Claude do it.

Also begin every step with `/clear` in Claude: the context of the previous step does not help in the next one and costs tokens again with every message (more on this below under "Check the context").

# Starting Claude Code

Claude Code is already installed in the Dev Container. Open a terminal in VS Code (Ctrl+`) – it runs inside the container – and start Claude:

```bash
claude
```

On the first start you sign in with your account. The sign-in and your Claude settings are kept even after a "Dev Containers: Rebuild Container".

# Analyze the project

This is a brownfield project. That means this project already contains code.

To get an overview of the project, we ask Claude.

With `Shift+Tab` you switch between the modes; the active mode is shown in the input line.

```bash
# First switch to Manual Mode
Analyze the project and give me an overview, ignore the README.md
```

Claude has analyzed the project and given us the information. But that cost us quite a few tokens, because the entire project was analyzed.

> **Task:** Look at your status line: how many tokens did the analysis use? Compare the tokens with your colleagues (first make sure with `/model` that everyone uses the same model). Despite the same prompt and the same project, the numbers differ, because Claude reads different files and answers differently every time.

# Set up the status line

The status line is the bar at the bottom of the Claude CLI. From now on it continuously shows us how many tokens we are using, how full the context window is, what the session costs us and how our usage limits are doing.

```bash
/statusline Show me the model, branch, input/output tokens of the session, context window utilization (percent and tokens), the 5-hour and 7-day limit (percent and reset time/day), the cost of the session and the current reasoning effort level. Ask me beforehand how I would like it to be designed (layout, colors, text/icons/both, additional info) before you write the script.
```

> **Task:** Answer Claude's questions about layout, colors, text/icons and possible additional info according to your own taste. As a result, every status line in the room ends up looking different: everyone answers the questions differently, and even with identical answers the model does not work deterministically. If the limit display stays empty for you, that is due to your account/sign-in type – the script is still correct.

> **Tip:** If something does not work – the status line stays empty, a value is missing or the script throws an error – just describe the problem to Claude and let it fix it for you. This applies throughout the workshop: the first attempt on errors is always to ask the AI for the fix instead of searching the script or code yourself.

# Set up the project (AGENTS.md)

So that Claude does not have to analyze the project completely again in every session and waste tokens doing so, we create an `AGENTS.md`. The file contains information about the project that an AI agent needs in every session. `AGENTS.md` is a cross-tool standard and is read by many tools (e.g. Codex, Cursor, GitHub Copilot).

```bash
# Switch to Auto Mode (Shift+Tab)
Create an AGENTS.md based on the information gathered earlier. Only save the information that is relevant for the AGENTS.md, though. Also create a CLAUDE.md that contains only `@AGENTS.md`.

```

> **Note:** The `@` character includes the content of a file in the context. Here as the import `@AGENTS.md` in the `CLAUDE.md`, so that the `AGENTS.md` is still loaded into every session even though Claude Code automatically loads only the `CLAUDE.md`. This way there is only one source for all tools. You can use `@<file name>` directly in a chat message in the same way (e.g. `Explain @Employee.java to me`) to deliberately pull a file into the context. After a `/clear` you can check with `/context` whether a file was loaded (see next section).

# Skills

## Commands vs. skills

Claude Code distinguishes between **commands** and **skills**:

- **Commands** are built-in control commands for the CLI itself (clear the context, change settings, set up the status line, …). They always begin with `/` and do not add any new domain capabilities to Claude.
- **Skills** are packaged instructions for recurring tasks (e.g. performing a code review or creating a report). Claude loads a skill either automatically when the task fits, or it is invoked manually via `/<skill-name>`. You can install further skills via `/plugins` (see the section "Third-party skills" below).

First the most important commands:

```bash
/clear    # Clears the entire context and thus starts a new session
/context  # Shows information about the current context
/config   # Claude settings can be changed here
/status   # Shows information about Claude itself
/plugins  # Plugin marketplace to install further skills
/statusline  # Sets up the status line
/agents   # Show subagents and create your own
```

> **Tip:** If you begin a message with `!`, Claude Code executes the rest directly as a shell command (e.g. `!git status`) without Claude having to be asked. Handy for quick checks in between, the output of which still ends up in the context.

## Check the context

The context window is Claude's working memory. Everything in it costs tokens: the system prompt, the tools, the `CLAUDE.md`/`AGENTS.md`, every message and every file read. When it fills up, Claude automatically summarizes the conversation so far, and details are lost in the process.

```bash
/clear    # Start a new session
/context  # Show what is already in the context
```

> **Task:** The status line already shows a few percent before the first question. Use `/context` to find out what they consist of. Does your `AGENTS.md` show up among the memory files?

Always use `/clear` when you begin a new task. Old context otherwise costs tokens again with every message.

## Third-party skills

In addition to Claude's built-in skills, there are also skills from third parties.

### Used in the workshop

We install [Skills For Real Engineers](https://github.com/mattpocock/skills) by Matt Pocock and use them in the further course of the workshop. They are in Claude Code's official marketplace, so no marketplace has to be added beforehand:

```bash
claude plugins install mattpocock-skills
```

Or within a running session:

```bash
/plugin install mattpocock-skills
```

> **Note:** The official marketplace often lags behind the repository by days or weeks. If you would rather always have the latest version directly from the repository, use its own marketplace (enable auto-update for it under `/plugin` → Marketplaces; for marketplaces outside of Anthropic it is off by default):
>
> ```bash
> claude plugin uninstall mattpocock-skills@claude-plugins-official
> claude plugin marketplace add mattpocock/skills
> claude plugin install mattpocock-skills@mattpocock
> ```

### Further examples for your own projects

> ⚠️ **For orientation only:** The following skills are **not** installed and **not** used in this workshop. They are meant as inspiration for what you can look at for your own projects after the workshop.

| Skill | What for |
| --- | --- |
| [Superpowers - Jesse Vincent](https://github.com/obra/superpowers) | Comprehensive framework with methodology for brainstorming, planning, TDD and code review |
| [OpenSpec - Fission-AI](https://github.com/Fission-AI/openspec) | Spec-driven development: agree on the specification first, then implement |

# Test features yourself

A green `./mvnw test` initially only means: Claude's code passes Claude's tests. Whether the feature really works, you only see in the running application — and this look is the first thing to fall by the wayside in agentic working. That is why every exercise step ends with a manual test; the concrete cases are listed in each step's README under "Test the feature yourself".

1. Start the app – either yourself in a second terminal:

   ```bash
   ./mvnw spring-boot:run
   ```

   or let Claude start it (`Start the app in the background and tell me when it is running`).

2. Open the Swagger UI in the browser: <http://localhost:8080/swagger-ui.html>. The Dev Container forwards port 8080 automatically ("Ports" tab in VS Code), a Ctrl+click on the URL in the terminal opens it directly.

3. Click through the endpoints with "Try it out" – always the happy path **and** at least one error case. Then check whether the data really arrived (`GET` endpoint or H2 console).

4. If something goes wrong, give Claude the concrete request and the response ("`POST /api/employees` with a duplicate username returns 500, response: …") instead of just "doesn't work". That gives it exactly the context that the test run does not provide.

> **Tip:** The H2 console at <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:backend`, user `sa`, no password) shows you what is actually in the database. The database lives in memory: after every restart of the app, the seed data from `data.sql` applies again.

# Next

Done? Then continue with step 1 (employee management + Swagger UI).

```bash
git reset --hard        # discards changes to existing files
git clean -fd           # removes newly created files
git checkout step-1-en
```

Instead of typing the commands yourself, you can also just tell Claude what should happen:

```bash
Discard all my changes, including newly created files, and switch to the branch step-1-en.
```

Your `AGENTS.md` and `CLAUDE.md` are lost in the process — but every exercise step already brings its own.

Afterwards, run `/clear` once in Claude: the context from this step does not help in the next one and costs tokens again with every message.
