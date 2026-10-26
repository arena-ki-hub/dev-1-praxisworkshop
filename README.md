# Step 2 — Time entries

## Learning goal

Test-driven development (red-green-refactor) together with Claude Code: tests first, then the implementation — instead of writing code and having it checked afterwards.

## The method of this step

The skill **`/tdd`** was installed on `main` together with `mattpocock-skills` and locks Claude into the red-green-refactor cycle: first a failing test, then the smallest implementation that makes it green, and only then refactoring.

The order is what matters. If you let Claude implement first and write tests afterwards, the tests describe what the code does — even when it implements a rule incorrectly. A test that was red beforehand and turns green after the implementation, on the other hand, proves that exactly this rule makes the difference. That is why the following holds: a test that is already green without an implementation checks nothing.

This fits especially well for this step because the three validation rules are given. They can be translated literally into test cases, including the boundary values (0 and 24 hours, today versus tomorrow), before a single line of production code exists.

**`/grilling`** from step 1 remains useful: before you approve the plan for the implementation, the skill takes it apart and presents every open point one at a time with a recommendation.

## Task

The backend should manage time entries (`TimeEntry`) per employee. An entry has a date, the number of booked hours, a short task description and a flag indicating whether it is an absence (e.g. vacation). The REST API should offer the usual CRUD operations, this time in the context of an employee (`/api/employees/{employeeId}/time-entries`).

The following should be validated:

- Hours must be greater than 0 and at most 24.
- The date must not be in the future.
- The referenced employee must exist.

## Recommended workflow

1. **Have the tests written with `/tdd` and see them fail.**

   ```bash
   /tdd First write the tests for the time entries: the three validation rules including boundary values and the CRUD operations. No implementation yet — I want to see the tests fail first.
   ```

   Have the failing tests shown to you before continuing. If one is already green, it does not check the rule.

2. **Challenge the plan for the implementation with `/grilling`** and approve it only afterwards.

3. **Let it run through green and refactor** — Claude adds only as much code as needed until the tests pass, and only then tidies up.

## Test the feature yourself (mandatory)

Green tests initially only mean that Claude's code passes Claude's tests. You only see whether the time tracking really works in the running application.

1. **Start the app** — in a second terminal, or have Claude start it (`Start the app in the background and tell me when it is running`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded ("Ports" tab in VS Code); a Ctrl+click on the URL in the terminal opens it directly.

3. **Click through the endpoints via "Try it out"** — the seed data contains the employees `a.schmidt`, `m.mueller` and `j.klein`, you get the IDs via `GET /api/employees`:
   - `POST /api/employees/{employeeId}/time-entries`: today's date, 8 hours, task "Customer project", no absence → entry is created.
   - `GET /api/employees/{employeeId}/time-entries`: the entry is in the list.
   - Create a second entry as an absence (e.g. vacation).
   - Check the validation: 0 hours, 25 hours, tomorrow's date → each 400 with an understandable message.
   - `POST /api/employees/999/time-entries` (unknown employee) → 404, **not** 500.
   - Change an entry via `PUT` and delete one via `DELETE` → `GET` then shows the change or no longer shows the entry.

4. **If something is wrong** — give Claude the concrete request and the response from the Swagger UI ("25 hours returns 500 instead of 400, response: …") instead of just "doesn't work".

## Acceptance criteria

- New endpoints are visible and usable via the Swagger UI.
- Invalid input (hours outside 0–24, date in the future, unknown employee) returns appropriate HTTP error codes, no 500s.
- `./mvnw test` passes, including new tests for the validation rules.
- You have clicked through the endpoints and all three validation rules yourself in the Swagger UI.

## Next

Done? Then continue with step 3 (Excel import).

```bash
git reset --hard        # discards changes to existing files
git clean -fd           # removes newly created files
git checkout step-3-en
```

Instead of typing the commands yourself, you can also just tell Claude what should happen:

```bash
Discard all my changes, including newly created files, and switch to the branch step-3-en.
```

Afterwards, run `/clear` once in Claude: the context from this step does not help in the next one and costs tokens again with every message.
