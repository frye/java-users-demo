# Users Demo

Sample Spring Boot application featuring an in-memory user list that exposes both a REST API and a simple web view. The goal is to mirror the starter functionality from the reference users demo: list and manage sample users, allow creation and update, and intentionally omit delete capability.

## Features

- In-memory store with seeded sample users and runtime create/update support
- REST API at `/api/users` for listing, finding, creating, and updating users
- Static HTML page at `/` that loads the catalog via the REST API and renders a table
- Lightweight responses that mirror the reference application behavior (no delete endpoint)

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+ (`mvn` available on your `PATH`)

### Run the application

```bash
mvn spring-boot:run
```

After startup, visit `http://localhost:8080/` for the web view. The REST API is available under `http://localhost:8080/api/users`.

### Build a runnable JAR

```bash
mvn -DskipTests package
java -jar target/users-demo-*.jar
```

If `mvn` is not found, install Maven or use a wrapper. On macOS:

```bash
brew install maven
```

## API Overview

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Fetch a single user by ID |
| `POST` | `/api/users` | Create a new user |
| `PUT` | `/api/users/{id}` | Update an existing user |

> ℹ️ Delete operations are intentionally omitted to match the reference behavior.

### Data model

User record fields:

- `id` (`UUID`): generated if absent on create; path param controls on update
- `name` (`String`): display name
- `emoji` (`String`): Unicode emoji rendered in the UI

### Request/response examples

List users:

```bash
curl -s http://localhost:8080/api/users | jq
```

Get by ID:

```bash
curl -s http://localhost:8080/api/users/{uuid}
```

Create user:

```json
{
  "name": "Bramble Fright",
  "emoji": "\uD83D\uDC7B"
}
```

```bash
curl -s -X POST http://localhost:8080/api/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"New Name","emoji":"\uD83D\uDE80"}'
```

Update user:

```bash
curl -s -X PUT http://localhost:8080/api/users/{uuid} \
  -H 'Content-Type: application/json' \
  -d '{"name":"Updated Name","emoji":"\u26A1\uFE0F"}'
```

Errors:

- Missing user ID: HTTP `404 Not Found` with `ResponseStatusException` semantics
- Invalid path ID format: HTTP `400 Bad Request` (from Spring path binding)

## Project Structure

- `src/main/java`: Application source organized by feature package
- `src/main/resources/static`: Static web assets
- `pom.xml`: Maven build configuration

## Troubleshooting

- Port in use: set `server.port` via `SPRING_APPLICATION_JSON` or JVM prop: `-Dserver.port=8081`
- Unicode display: the UI uses HTML; emojis are stored as strings and served as JSON
- No tests: this starter intentionally omits automated tests; they can be added with `spring-boot-starter-test` upon request

## Next Steps

- Extend the in-memory store with persistence if needed
- Add authentication or authorization if required for your use case
 - Add CI and tests (`spring-boot-starter-test`) if you want coverage
