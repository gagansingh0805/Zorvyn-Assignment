# Zorvyn Finance Backend Assignment

Backend for a finance dashboard system with role-based access control, financial record management, and summary analytics.

## Tech Stack
- Java 17, Spring Boot
- Spring Data JPA (PostgreSQL)
- Spring Security
- Springdoc OpenAPI (Swagger)

## Setup
1) Create database `assignment_db` in PostgreSQL.
2) Update credentials in `src/main/resources/application.properties` if needed.
3) Run the app:
```bash
./mvnw spring-boot:run
```

Docker (app + Postgres):
```bash
docker compose up --build
```

Swagger UI:
- `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Auth Model (Mock)
All protected endpoints require header `X-User-Id` with a valid, ACTIVE user ID.
Roles are enforced at the endpoint level:
- VIEWER: can access dashboard summaries only
- ANALYST: can read records and dashboard summaries
- ADMIN: full access to records and users

Public registration:
- `POST /auth/register` creates a user with role VIEWER by default.

## API Overview

### Users (ADMIN only)
- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `PATCH /users/{id}/status`
- `PATCH /users/{id}/role`

### Auth (public)
- `POST /auth/register`

### Records
- `POST /records` (ADMIN)
- `GET /records` (ANALYST/ADMIN, paged)
- `GET /records/filter` (ANALYST/ADMIN, paged)
- `PUT /records/{id}` (ADMIN)
- `DELETE /records/{id}` (ADMIN)

### Dashboard (VIEWER/ANALYST/ADMIN)
- `GET /dashboard/summary`
  - Optional query params: `start`, `end` (both required together)
  - Returns totals, category totals (net signed by type), recent activity, monthly and weekly trends

### Health
- `GET /health` (public)

## Sample Requests
Create a user (ADMIN):
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"name":"Admin","email":"admin@example.com","role":"ADMIN"}'
```

Create a record (ADMIN):
```bash
curl -X POST http://localhost:8080/records \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"amount":1200,"type":"INCOME","category":"Salary","date":"2026-04-01","notes":"April salary"}'
```

Get dashboard summary (VIEWER/ANALYST/ADMIN):
```bash
curl http://localhost:8080/dashboard/summary \
  -H "X-User-Id: 1"
```

Filter records with pagination:
```bash
curl "http://localhost:8080/records/filter?category=Food&page=0&size=10" \
  -H "X-User-Id: 2"
```

## Testing
Tests use an H2 in-memory database with profile `test`.
```bash
./mvnw test
```

## Assumptions
- Authentication is mocked via `X-User-Id` for assignment clarity.
- Category totals use signed values (income positive, expense negative) to show net impact.
- Date range filters require both `start` and `end`.
