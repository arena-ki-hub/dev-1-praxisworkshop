# Step 5 — Bugfixing

## Learning goal

Debugging workflow with Claude Code.

## The method of this step

Take a look at the `mattpocock-skills:diagnosing-bugs` skill.

## Starting point

In this state `./mvnw test` fails. The overtime calculation from step 4 contains deliberate bugs that violate the target/actual rule from step 4 (the rule itself is unchanged in the Javadoc of `OvertimeService` and in `AGENTS.md`).

## Task

Find and fix the bugs until all tests are green again — in particular `OvertimeControllerTest`. There may be more than one bug; don't rely on everything being green after the first fix.

## Recommended workflow

Don't just let it start fixing right away. First read the failing test and the error message carefully, form a hypothesis, check the existing code against the documented target/actual rule — then fix in a targeted way. If needed, add further tests that clearly prove each bug.

## Test the feature yourself (mandatory)

Green tests prove little here — a fix can also simply make the test pass instead of repairing the domain logic. So check the calculation in the running application.

1. **Start the app** — in a second terminal, or have Claude start it in the background:

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded (see the "Ports" tab).

3. **Create test data** — `POST /api/time-entries/import` with `samples/timesheet-import-example.xlsx`. You get the ID of `a.schmidt` via `GET /api/employees`.

4. **Query the overtime and recalculate it** — call `GET /api/employees/{id}/overtime?from=2026-08-31&to=2026-09-06` for `a.schmidt` and recalculate it yourself:
   - **Target 32 h:** Mon 31.08., Tue 01.09., Thu 03.09. and Fri 04.09. with 8 h each. The vacation day Wed 02.09. is excluded completely, Sat/Sun have 0 target hours.
   - **Actual 20 h:** 8 h + 8 h + the 4 h from Saturday 05.09. — the vacation day does not count.
   - **Overtime −12 h.**

   This is exactly where the planted bugs show up: weekends that end up in the target hours, or absence hours that end up in the actual hours.

5. **If something is wrong** — give Claude the request, the response and your expected calculation and have the next fix secured by a test.

## Acceptance criteria

- `./mvnw test` passes completely.
- The target/actual rule from step 4 is implemented correctly again (in particular: weekends do not count towards the target hours, absence days do not flow into the actual hours).
- After the fix you have checked the calculation yourself in the running application (for `a.schmidt` from 31.08. to 06.09.2026: target 32 h, actual 20 h, overtime −12 h).
