# Step 7 — Exercise completed

## About this branch

All six steps are fully implemented here — this branch (`step-7-en`) contains the **complete reference solution** including step 6 and serves only for checking (e.g. to compare your own solution). It is not an exercise step of its own.

Congratulations if you got this far on your own!

## Optional bonus exercise

If you still have time: the branch `step-8-en` contains an **optional exercise** in which you build your own skill.

## Reference values for comparison

If your own solution produces different numbers than expected, you can check against these:

1. **Start the app** — `./mvnw spring-boot:run`

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>

3. **Import** — import `samples/timesheet-import-example.xlsx` via `POST /api/time-entries/import` → **9 imported rows, 1 skipped** (`x.unknown`).

4. **Overtime** — call `GET /api/employees/1/overtime?from=2026-08-31&to=2026-09-06` for `a.schmidt` → **target 32 h, actual 20 h, overtime −12 h**.
