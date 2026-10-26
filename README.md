# Step 3 — Excel import

## Learning goal

Plan Mode when adding a new dependency (Apache POI) and when building a new, technically more complex feature.

## The method of this step

**Plan Mode** — the same mechanics as in step 1 (`Shift+Tab` until the input line shows Plan Mode), but for a different reason. There it was about domain gaps in the task description. Here it is about technical decisions that are hard to undo once the code is finished: a new dependency in the `pom.xml`, the kind of file upload, and the question of how a faulty row is handled without taking the whole import down with it.

Have the plan show you which dependency is added in which version and what the result of the import should look like before you approve.

**`/grilling`** remains the tool for this: you start it directly with the task, the skill takes it apart and presents every open point one at a time with a recommendation.

## Task

Build an import endpoint that accepts an Excel file with time-tracking data and creates `TimeEntry` entries from it. A sample file is located at `samples/timesheet-import-example.xlsx`. The fixed column layout (row 1 = header row):

| Column A | Column B | Column C | Column D | Column E |
|---|---|---|---|---|
| Date (`yyyy-MM-dd`) | Username | Hours | Task | Absence (`Yes`/`No`) |

The username must match an existing employee (matching via `Employee.username`). Rows with an unknown employee or invalid values should **not** abort the entire import, but be skipped and listed in the result.

## Recommended workflow

1. **Switch to Plan Mode** (`Shift+Tab` until the input line shows Plan Mode).

2. **Start the task directly with `/grilling`** — no separate plan prompt needed, the skill interviews you about the task and presents every open point one at a time with a recommendation:

   ```bash
   /grilling Build an import endpoint for Excel files with time-tracking data (column layout and sample file see README). Pay special attention to the new dependency (Apache POI, `poi-ooxml`) including its version, the upload and the handling of faulty rows.
   ```

3. **Approve the plan and let it be implemented** — after that Claude implements the import and runs `./mvnw test`. Continue with the next section: try out the import yourself.

## Test the feature yourself (mandatory)

The import is only done once you have sent the sample file through the running application yourself — a green `./mvnw test` is not enough.

1. **Start the app** — in a second terminal, or have Claude start it (`Start the app in the background and tell me when it is running`):

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open the Swagger UI** — <http://localhost:8080/swagger-ui.html>. In the Dev Container, port 8080 is forwarded ("Ports" tab in VS Code); a Ctrl+click on the URL in the terminal opens it directly.

3. **Import the sample file** — `POST /api/time-entries/import` → "Try it out" → for `file`, select the file `samples/timesheet-import-example.xlsx` from the project folder → "Execute". Alternatively from the terminal:

   ```bash
   curl -F file=@samples/timesheet-import-example.xlsx http://localhost:8080/api/time-entries/import
   ```

   - The response must report 9 imported rows and exactly one skipped row with a reason for `x.unknown`.
   - `GET /api/employees/{employeeId}/time-entries` for `a.schmidt`: the four imported entries (31.08., 01.09., the vacation day 02.09. and Saturday 05.09.) are really in the database.
   - To try out: upload any non-Excel file (e.g. the `README.md`). What happens? Even the reference solution answers that with a 500 — a good example of everything a green test suite does not cover. Whether you catch that is up to you.

4. **If something is wrong** — give Claude the concrete request and the response from the Swagger UI ("import returns importedCount 0, response: …") instead of just "doesn't work".

## Acceptance criteria

- `POST /api/time-entries/import` (multipart, field name `file`) imports the sample file and returns a summary `{ importedCount, skippedRows }`.
- The row with the unknown username (`x.unknown`) is skipped and listed in `skippedRows` with a reason, instead of aborting the import.
- `./mvnw test` passes, including a test that imports the sample file.
- You have imported the sample file yourself via the Swagger UI (or with `curl`) and checked the created entries via `GET .../time-entries`.

## Next

Done? Then continue with step 4 (overtime calculation).

```bash
git reset --hard        # discards changes to existing files
git clean -fd           # removes newly created files
git checkout step-4-en
```

Instead of typing the commands yourself, you can also just tell Claude what should happen:

```bash
Discard all my changes, including newly created files, and switch to the branch step-4-en.
```

Afterwards, run `/clear` once in Claude: the context from this step does not help in the next one and costs tokens again with every message.
