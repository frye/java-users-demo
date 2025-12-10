# Java Users Demo — Workflow Guide

This guide shows how to accomplish the requested tasks using GitHub Copilot agents in VS Code: Copilot Plan Mode (for structured planning), Copilot Agent Mode (for executing changes), Mission Control (to observe agent runs), and Copilot Chat (to review changes). You’ll work in a fork of `java-users-demo`, plan unit tests and delete functionality, create a tracking issue, implement tests via Agent Mode, observe execution, and review changes.

## How Copilot Agents Help
- **Copilot Plan Mode**: Produces clear, sequenced plans for features and tests before implementation.
- **Copilot Agent Mode**: Executes changes in your fork (editing files, running builds/tests) following your plans.
- **Mission Control**: Observes the Agent’s actions, tool calls, diffs, and test runs in real time.
- **Copilot Chat**: Assists with code reviews, summarizes diffs, explains failures, and suggests fixes.

## Quick Start
- **Fork and clone**: Create a fork on GitHub, then `git clone` your fork; set upstream to original.
- **Branch**: `git checkout -b feature/tests-and-delete`.
- **Build check**: `mvn -v`, then `mvn -q -DskipTests=true package` to verify Maven and Java 17.

## Repository Summary and Gaps
- **Structure**:
  - `src/main/java/com/example/usersdemo/UsersDemoApplication.java`: Spring Boot entrypoint.
  - `src/main/resources/static/index.html`: loads `/api/users`.
  - `src/main/java/com/example/usersdemo/user/`: in-memory store in `UserService`; `User`, `UserWriteModel`, `UserNotFoundException`.
  - `src/main/java/com/example/usersdemo/web/api/UserRestController.java`: REST endpoints.
- **Implemented endpoints**:
  - `GET /api/users` → returns `List<User>`.
  - `GET /api/users/{id}` → returns `User` or 404 (mapped via `UserNotFoundException`).
  - `POST /api/users` → creates and returns `User` (201 Created).
  - `PUT /api/users/{id}` → updates and returns `User` or 404.
- **Constraints**:
  - Java 17, Maven; minimal deps — only `spring-boot-starter-web` (tests would use `spring-boot-starter-test`).
  - In-memory synchronized `LinkedHashMap` seeded with demo users.
  - `UserWriteModel` provides defaults when absent; `UserService` handles null/blank as defaults.
- **Gaps**:
  - No delete method in `UserService` or `@DeleteMapping` in controller.
  - No tests or CI.

## Tasks and Agent Usage (with Sample Prompts)

Use these agents and prompts to drive each task efficiently. Run prompts in Copilot Plan Mode for planning and Copilot Agent Mode for execution. Observe runs with Mission Control and review with Copilot Chat.

- **Task 1: Plan Unit Test Generation (Plan Mode)**
  - Purpose: Produce the unit test strategy before coding.
  - Sample prompt (Plan Mode):
    - "Plan unit tests for UserService and UserRestController in java-users-demo per project guidance. Include files, coverage points, status codes, and minimal dependencies."

- **Task 2: Plan Delete Functionality (Plan Mode)**
  - Purpose: Define service and API changes for delete.
  - Sample prompt (Plan Mode):
    - "Plan DELETE /api/users/{id} implementation: service delete(UUID), 204 success, 404 missing, README updates, and test plan."

- **Task 3: Create GitHub Issue (Agent Mode + GitHub MCP)**
  - Purpose: Track delete implementation in your fork.
  - Sample prompt (Agent Mode, GitHub MCP):
    - "Create an issue in my fork your-username/java-users-demo titled 'Add DELETE /api/users/{id} endpoint and service delete()' with requirements, files, and acceptance criteria as specified. Label enhancement, backend; assign to me."

- **Task 4: Implement Unit Test Plan (Agent Mode, read upstream issue with MCP)**
  - Purpose: Execute the unit test plan using the tracking issue as the exact prompt via GitHub MCP.
  - How to read and use the issue with MCP:
    - Ensure GitHub MCP is connected with a PAT (`repo` scope) in VS Code.
    - Ask the Agent: "Fetch the latest open issue titled 'Add DELETE /api/users/{id} endpoint and service delete()' from my fork and display its body."
    - Copy the full issue body and use it verbatim as the Agent prompt for implementation.
  - Sample prompt (Agent Mode):
    - "Using the upstream tracking issue content verbatim, implement the unit test plan: add spring-boot-starter-test to pom.xml; create UserServiceTest and UserRestControllerTest with the specified coverage; run mvn test and fix failures aligned with UserWriteModel defaults. Commit and push a PR referencing the issue."

- **Task 5: Observe Agent Execution (Mission Control)**
  - Purpose: Monitor the agent’s actions and test runs.
  - Sample prompt (Mission Control context):
    - "Start observing the unit test implementation run. Mark checkpoints after pom.xml edit, after each test file creation, and after successful mvn test. Surface failures and suggest corrections."

- **Task 6: Code Review with Copilot (Copilot Chat)**
  - Purpose: Review changes, summarize diffs, and suggest fixes.
  - Sample prompts (Copilot Chat):
    - "Summarize changes in UserService.java, UserRestController.java, and tests. Identify potential issues and suggest minimal fixes."
    - "Explain failing tests and propose corrections consistent with project guidance (status codes, defaults, minimal dependencies)."
    - "Generate a review checklist covering service/controller paths (create/update/find/delete), 404 mapping, 201/200/204 status codes, and README updates."

## Detailed Steps

### Task 1: Copilot Plan Mode — Unit Test Generation
- Add test dependency in `pom.xml`: include `spring-boot-starter-test` with `<scope>test</scope>`.
- Create test files:
  - `src/test/java/com/example/usersdemo/user/UserServiceTest.java`.
  - `src/test/java/com/example/usersdemo/web/api/UserRestControllerTest.java`.
- `UserServiceTest` coverage:
  - `findAll()` returns seeded users in insertion order.
  - `findById(UUID)` returns present for existing, empty for missing.
  - `create(UserWriteModel)` defaults: generates UUID, name `"Anonymous"`, emoji wave `"\uD83D\uDC4B"` when absent/blank.
  - `update(UUID, UserWriteModel)` updates provided fields, retains others; throws `UserNotFoundException` if id missing.
  - Seed users exist: "Bramble Fright" 👻, "Sylvie Scream" 🎃, "Eve Eerie" 🧙.
- `UserRestControllerTest` with `@WebMvcTest(UserRestController.class)`, `@MockBean UserService`, `MockMvc`:
  - `GET /api/users` → 200, JSON array.
  - `GET /api/users/{id}` → 200 for existing, 404 for missing.
  - `POST /api/users` → 201, JSON payload.
  - `PUT /api/users/{id}` → 200 for existing, 404 for missing.
- Align tests with `UserWriteModel` semantics (nullable fields handled by service). Keep dependencies minimal.

### Task 2: Copilot Plan Mode — Delete Functionality
- Service: add `public User delete(UUID id)` in `UserService` to remove existing or throw `UserNotFoundException`.
- Controller: add `@DeleteMapping("/api/users/{id}")` in `UserRestController` returning `204 No Content` on success; 404 on missing.
- Documentation: update `README.md` for DELETE endpoint behavior and status codes.
- Optional UI: add delete button and `fetch('/api/users/{id}', { method: 'DELETE' })` in `index.html`.
- Tests: plan service and controller delete tests (added in Task 4 execution).

### Task 3: Agent Mode + GitHub MCP — Create Issue
- Configure GitHub MCP with PAT (`repo` scope) in VS Code.
- Start Copilot Agent Mode with GitHub MCP provider.
- Create issue in `your-username/java-users-demo`:
  - Title: `Add DELETE /api/users/{id} endpoint and service delete()`.
  - Body: summary, requirements (service delete, controller mapping, README updates, unit tests), files, acceptance criteria.
  - Labels: `enhancement`, `backend`. Assignee: yourself. Verify on GitHub.

### Task 4: Agent Mode — Implement Unit Test Plan (Read Upstream Issue as Prompt)
- Start by reading the upstream tracking issue content (created in Task 3) and use it verbatim as the Agent Mode prompt. This ensures the agent executes exactly against the acceptance criteria and file targets from the issue.
- Validate Maven: `mvn -v`; add wrapper or install Maven if needed.
- Update `pom.xml`: add `spring-boot-starter-test` with `<scope>test</scope>`.
- Implement `UserServiceTest`: seed order, `findById`, `create` defaults, `update` behavior and exception.
- Implement `UserRestControllerTest`: `@WebMvcTest`, `@MockBean UserService`, `MockMvc` for all endpoints.
- Run tests: `mvn -q -DskipTests=false test`; fix failures aligned with `UserWriteModel` semantics.
- Commit/push: `git add pom.xml src/test/java` → commit → push → open PR referencing the issue.

### Task 5: Mission Control — Observe Agent Execution
- Open Mission Control panel; start Agent run for Task 4.
- Observe file edits (`pom.xml`, test files), tool calls, and `mvn test`.
- Checkpoints: after dependency addition, after each test file creation, after successful test run.
- Intervene with guidance if the agent stalls or assertions fail.
- Export logs/diffs for review.

### Task 6: Code Review — Review with Copilot
- Enable Copilot Chat.
- Summarize diffs in `UserService.java`, `UserRestController.java`, and tests; ask Copilot for potential issues.
- Validate: `mvn -q test`; ask Copilot to explain failures and propose minimal fixes.
- Checklist: coverage (create/update/find/delete), 404 mapping and status codes (201/200/204), minimal deps, README updates.
- Provide PR feedback using Copilot suggestions; request changes or approve when criteria are met.

## Troubleshooting
- **Maven not found**: Install Maven or use Maven Wrapper.
- **Java version issues**: Ensure Java 17 is active (`java -version`); configure VS Code Java runtime accordingly.
- **Build fails on emojis**: Keep Unicode only where constants exist; otherwise preserve ASCII; ensure source encoding is UTF-8 (consider `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>` in `pom.xml` if needed).
- **Test failures due to `UserWriteModel` semantics**: Verify fields are treated as nullable in `UserService` and adjust tests; ensure defaults match service logic.
- **404/response mismatches**: Confirm `UserNotFoundException` is mapped to 404 in controller; for DELETE, return 204 on success.
- **`mvn` command hangs or network issues**: Add `-q` to reduce output; check proxy/network settings; run `mvn -U clean test` to refresh snapshots.
