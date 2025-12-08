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

## API Overview

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Fetch a single user by ID |
| `POST` | `/api/users` | Create a new user |
| `PUT` | `/api/users/{id}` | Update an existing user |

> ℹ️ Delete operations are intentionally omitted to match the reference behavior.

### Sample payload

```json
{
  "name": "Bramble Fright",
  "emoji": "\uD83D\uDC7B"
}
```

## Project Structure

- `src/main/java`: Application source organized by feature package
- `src/main/resources/static`: Static web assets
- `pom.xml`: Maven build configuration

## Next Steps

- Extend the in-memory store with persistence if needed
- Add authentication or authorization if required for your use case
