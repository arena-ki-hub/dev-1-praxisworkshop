# Step 4 — Overtime calculation

## Learning goal

Plan Mode for a more complex domain task with several subtasks.

## The method of this step

**Plan Mode** (`Shift+Tab` until the input line shows Plan Mode) for a task with several subtasks: iterate over a date range, detect weekends, exclude absences, calculate sums.

**`/grilling`** is the tool for questioning the task thoroughly before implementing it, e.g.: what happens on a weekday with no booking at all? What if `from` is after `to`?

## Task

Build an endpoint that calculates the overtime for an employee and a period: `GET /api/employees/{id}/overtime?from=...&to=...`.

Calculation rule:

- Saturday/Sunday have 0 target hours.
- On weekdays, target hours = `weeklyTargetHours / 5`.
- Days with an absence booking (`absence=true`, e.g. vacation) are excluded from the calculation **completely** (neither target nor actual hours for that day).
- Actual hours = sum of the booked hours of all non-absence entries in the period (also on weekends, if something was booked there).
- Overtime = actual hours − target hours.

## Recommended workflow

1. **Switch to Plan Mode** (`Shift+Tab` until the input line shows Plan Mode).

2. **Start the task directly with `/grilling`** — no separate plan prompt needed, the skill interviews you about the task and presents every open point one at a time with a recommendation:

   ```bash
   /grilling Build GET /api/employees/{id}/overtime?from=...&to=... with the calculation rule from the README. Pay special attention to weekdays without a booking and to a swapped period.
   ```

3. **Approve the plan and let it be implemented** — after that Claude implements the endpoint and runs `./mvnw test`. Continue with the next section: try out the endpoint yourself.

## Test the feature yourself (mandatory)

For a calculation rule, "tests are green" is not enough — recalculate the result of the running application yourself once.

1. **Start the app** — in a second terminal, or have Claude start it in the background:

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded (see the "Ports" tab).

3. **Create test data** — `POST /api/time-entries/import` with `samples/timesheet-import-example.xlsx` (or a few entries by hand via `POST /api/employees/{employeeId}/time-entries`). You get the ID of `a.schmidt` via `GET /api/employees`.

4. **Query the overtime and recalculate it** — call `GET /api/employees/{id}/overtime?from=2026-08-31&to=2026-09-06` for `a.schmidt` and check the result against the rule:
   - **Target 32 h:** Mon 31.08., Tue 01.09., Thu 03.09. and Fri 04.09. with 8 h each. The vacation day Wed 02.09. is excluded completely, Sat/Sun have 0 target hours.
   - **Actual 20 h:** 8 h + 8 h + 4 h — the 4 h on Saturday 05.09. count, the vacation day does not.
   - **Overtime −12 h.**
   - Error cases: `from` after `to` → 400, `GET /api/employees/999/overtime?...` → 404, **not** 500.

5. **If something is wrong** — give Claude the request, the response and your calculation ("expected target 32 / actual 20, got …"), so it has something concrete to recalculate.

## Acceptance criteria

- The endpoint returns `{ employeeId, from, to, targetHours, actualHours, overtimeHours }`.
- A weekday with no booking at all counts fully as target hours (leading to undertime), a weekend day with a booking counts fully as overtime.
- A swapped period (`from` after `to`) returns HTTP 400, an unknown employee HTTP 404.
- `./mvnw test` passes, including a test with a mixed scenario (weekdays, one vacation day, one weekend day with a booking).
- You have called the endpoint yourself in the running application and recalculated the result (for `a.schmidt` from 31.08. to 06.09.2026: target 32 h, actual 20 h, overtime −12 h).

## Next

Done? Then continue with step 5.

```bash
git reset --hard        # discards changes to existing files
git clean -fd           # removes newly created files
git checkout step-5-en
```

Instead of typing the commands yourself, you can also just tell Claude what should happen:

```bash
Discard all my changes, including newly created files, and switch to the branch step-5-en.
```

Afterwards, run `/clear` once in Claude: the context from this step does not help in the next one and costs tokens again with every message.
