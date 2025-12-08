## Java Users Demo Guidance

### Project Overview
- Minimal Spring Boot app exposing a sample users REST API and a companion static web page.
- In-memory data only; no persistence layer, delete endpoint, or advanced validation by default.
- Starter state for workshops—tests and CI are intentionally omitted unless the user requests them.

### Stack Details
- Java 17, Maven build, Spring Boot `spring-boot-starter-web` as the sole dependency.
- Static assets in `src/main/resources/static`; `/index.html` loads user data from `/api/users` via fetch.
- JSON payloads kept lightweight to mirror the JavaScript reference project.
- When automated tests are introduced, use Spring Boot's default JUnit Jupiter setup via `spring-boot-starter-test`.

### Domain Rules
- `User` record fields: `UUID id`, `String name`, `String emoji`.
- `UserService` maintains a synchronized `LinkedHashMap` seeded with whimsical demo users (Bramble Fright, Sylvie Scream, Eve Eerie).
- `UserWriteModel` accepts optional `id`, `name`, and `emoji`, supplying defaults when absent.
- No delete path; ensure create and update keep the store consistent.

### API Behavior
- Endpoints: `GET /api/users`, `GET /api/users/{id}`, `POST /api/users`, `PUT /api/users/{id}`.
- Missing resources should raise `ResponseStatusException` with HTTP 404.
- Return newly created/updated users directly; avoid additional wrapping structures.

### Development Guidance
- Keep dependencies minimal—do not add Thymeleaf, validation, or database starters unless asked.
- Preserve ASCII when editing files; only introduce Unicode when required (e.g., emoji constants already present).
- Favor clear, concise code; add comments only when logic is non-obvious.
- Reflect any behavior changes in `README.md` and other docs.
- Confirm Maven availability before relying on `mvn` commands; suggest installation or wrapper when missing.
- Leave test scaffolding absent unless the user explicitly requests automated tests.
