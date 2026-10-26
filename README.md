# Step 1 — Employee management + Swagger UI

## Learning goal

Plan Mode and structured prompting together with Claude Code: let Claude plan first, challenge the plan, then implement — instead of having it write code right away.

## The method of this step

**Plan Mode.** With `Shift+Tab` you switch between the modes until the input line shows Plan Mode. In this mode Claude does not change any files: it reads up on the project, asks questions and finally presents a plan for approval. Only after your approval does it write. This is worthwhile for this task because the task description deliberately leaves gaps — for example what should happen with a duplicate username. In Plan Mode such gaps show up while they are still a decision and not yet 200 lines of code.

**`/grilling`** (from `mattpocock-skills`, installed on `main`). The skill takes a finished plan apart: Claude asks you questions in rounds about everything the plan leaves open and gives its own recommendation for each question. You invoke it once the plan is ready — before approval.

## Task

The backend should manage employees (`Employee`). An employee has a first and last name, a unique username and a weekly target working time in hours. The REST API should offer the usual CRUD operations: create, list, retrieve individually, update, delete. In addition, Swagger UI should be integrated so the API can be tried out in the browser.

## Recommended workflow

1. **Switch to Plan Mode** (`Shift+Tab`).

2. **Prompt the task in your own words, read the plan and challenge it with `/grilling`.**

   ```bash
   /grilling I want to build a REST API for managing employees: first and last name, a unique username and the weekly target working time in hours, plus the usual CRUD operations and Swagger UI for trying it out in the browser. Spring Boot with H2 as in the AGENTS.md, no authentication, no frontend.
   ```

   The task description leaves some questions open. Without being asked, Claude decides them on its own and does not mention it. The grilling brings them to the table and presents each one to you with a recommendation. In this step, for example:

   - What happens with a duplicate username?
   - Which fields are mandatory, which optional?
   - What does an error response look like — status code and body?
   - What does retrieving an unknown ID return?

   Your answers go into the plan. This way you decide the open points before the implementation and not while reading the finished code.

3. **Approve the plan and let it be implemented.**

## Test the feature yourself (mandatory)

The step is only done once you have tried out the API in the running application yourself — not as soon as Claude reports "done" and `./mvnw test` is green.

1. **Start the app** — in a second terminal, or have Claude start it (`Start the app in the background and tell me when it is running`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded ("Ports" tab in VS Code); a Ctrl+click on the URL in the terminal opens it directly.

3. **Click through the endpoints via "Try it out":**
   - `POST /api/employees`: create employee `a.schmidt` with 40 target hours, note the ID from the response.
   - `GET /api/employees`: the new employee is in the list.
   - `POST /api/employees` once more with the same username → understandable error message with a sensible status code (e.g. 409), **not** a 500 and no stack trace.
   - `PUT /api/employees/{id}`: change the target hours to 32 → `GET /api/employees/{id}` returns 32.
   - `DELETE /api/employees/{id}` → afterwards `GET /api/employees/{id}` returns 404.
   - Send an empty username once → 400 instead of 500.

4. **If something is wrong** — give Claude the concrete request and the response from the Swagger UI ("`POST /api/employees` with a duplicate username returns 500, response: …") instead of just "doesn't work".

## Acceptance criteria

- The application starts with `./mvnw spring-boot:run`.
- Under `/swagger-ui.html` all employee endpoints are visible and usable via "Try it out".
- A duplicate username is rejected with a sensible error message (not with HTTP 500).
- `./mvnw test` passes.
- You have clicked through the endpoints yourself in the Swagger UI — including a duplicate username and retrieving a deleted employee.

## Next

Done? Then continue with step 2 (time entries).

```bash
git reset --hard        # discards changes to existing files
git clean -fd           # removes newly created files
git checkout step-2-en
```

Instead of typing the commands yourself, you can also just tell Claude what should happen:

```bash
Discard all my changes, including newly created files, and switch to the branch step-2-en.
```

Afterwards, run `/clear` once in Claude: the context from this step does not help in the next one and costs tokens again with every message.
