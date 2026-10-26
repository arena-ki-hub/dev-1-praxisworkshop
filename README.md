# Step 6 — Configurable column mapping for the Excel import

## Learning goal

Code review before a refactoring, and subagents to keep your own context lean.

## The method of this step

Take a look at the `code-review` or `simplify` skill.

**Subagents:** A subagent is a separate Claude instance with its own fresh context window. Claude hands it a subtask, it reads the necessary files on its own and returns only a summary. The files it has read therefore do not end up in your main context.

## Starting point

The Excel import from step 3 assumes a fixed column layout. In practice, however, different time-tracking tools (or different configurations of the same tool) export different column orders and names — the company's real export format, for example, has significantly more and differently named columns than our sample file.

## Task

Rebuild the import so that the assignment of columns to the business fields (date, username, hours, task, absence) is configurable instead of hard-coded — e.g. via the column headers (header row) instead of fixed column positions, with a default mapping from the configuration and the option to override it per import call.

## Recommended workflow

Before you rebuild: first ask Claude Code for a code review of the existing `TimeEntryImportService` — which places are tightly coupled to the current column layout? Run the review as an experiment with your neighbor. Both of you start with `/clear` and note the context % from the status line.

Person A reviews directly:

```bash
Review the TimeEntryImportService: which places are tightly coupled to the current column layout?
```

Person B reviews via a subagent:

```bash
Have a subagent review the TimeEntryImportService: which places are tightly coupled to the current column layout? Return only the result to me.
```

> **Task:** Compare the context % and tokens in the status line and use `/context` to look at what is in the context. How do the results of the review differ? In this small project the difference is still manageable; in real projects with many files it matters a lot.

Alternatively, you can use the `code-review` or `simplify` skills for the review. Only then plan the rebuild, challenge it with `/grilling` (e.g.: what happens if a column referenced in the mapping is missing from the file? Does the sample file still have to work?) and implement it.

## Test the feature yourself (mandatory)

After a refactoring, the most important test is that the old behavior still works unchanged — in the running application, not just in the test suite.

1. **Start the app** — in a second terminal, or have Claude start it in the background:

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded (see the "Ports" tab).

3. **Default mapping (regression)** — `POST /api/time-entries/import` with `samples/timesheet-import-example.xlsx` without a mapping of your own → still 9 imported rows and one skipped row (`x.unknown`).

4. **Your own mapping** — Have Claude generate a second Excel file with different column names and a different order (e.g. `Day`, `Employee`, `Duration`, `Activity`, `Vacation`), import it with a matching mapping and use `GET /api/employees/{employeeId}/time-entries` to check that the entries really arrived.

5. **Error case** — Import the same file with a mapping that references a column that does not exist → a clear 400 response that names the missing column, instead of silently wrong data.

6. **If something is wrong** — give Claude the concrete request, the mapping you used and the response from the Swagger UI instead of just "doesn't work".

## Acceptance criteria

- The existing import with the sample file and the default mapping still works unchanged.
- A file with different but complete column headers can be imported when a matching mapping is supplied.
- If a required column referenced in the mapping is missing from the file, the import returns a clear error (HTTP 400) instead of an unclear error or wrong data.
- `./mvnw test` passes, including a test with a different column mapping.
- You have tried both imports (default mapping and your own mapping) as well as the error case yourself in the running application.

This is the last mandatory step of this exercise — congratulations! You can find the complete reference solution in `step-7-en`, and an optional bonus exercise (build your own skill) in `step-8-en`.
