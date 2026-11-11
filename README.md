# Healthcare Management Platform

A Java 21, Spring Boot and PostgreSQL learning project. This is a demo application only; use synthetic data and never real patient information.

## Starter milestone
- Patient and appointment REST APIs
- PostgreSQL persistence with Spring Data JPA
- Request validation and centralized API errors
- Docker Compose for local PostgreSQL
- JWT bearer-token authentication with BCrypt password hashing

See `docs/architecture.md` for the initial design. See `backend/` for the Spring Boot application.

## Run locally

Prerequisites: Docker Desktop, Java 21, and Maven 3.9+.

1. Start PostgreSQL: `docker compose up -d`
2. Set `APP_JWT_SECRET` to a securely generated secret of at least 32 bytes.
3. Run the API: `cd backend && mvn spring-boot:run`
4. API base URL: `http://localhost:8080`

The API reads database and JWT settings from environment variables. The default JWT signing secret is for local development only. Never commit production secrets.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/v1/auth/register` | Register a patient account |
| POST | `/api/v1/auth/login` | Login and receive a JWT |
| POST | `/api/v1/patients` | Create a patient |
| GET | `/api/v1/patients` | List patients |
| GET | `/api/v1/patients/{id}` | Get a patient |
| PUT | `/api/v1/patients/{id}` | Update a patient |
| DELETE | `/api/v1/patients/{id}` | Delete a patient |
| POST | `/api/v1/appointments` | Create an appointment |
| GET | `/api/v1/appointments` | List appointments |
| GET | `/api/v1/appointments/{id}` | Get an appointment |

## Authentication

Register with `POST /api/v1/auth/register` using JSON fields `email` and `password` (12–72 characters). Login uses the same fields. Both return an access token; send it as `Authorization: Bearer <token>` on protected API calls. Passwords are stored as BCrypt hashes. Tokens expire after 60 minutes by default; configure with `APP_JWT_TTL_MINUTES`. Set `APP_JWT_SECRET` to a private secret of at least 32 bytes.

Public self-registration always assigns the `PATIENT` role and cannot assign elevated roles. Role claims and authenticated access are in place, but administrator/clinician provisioning, patient-to-record ownership rules, refresh tokens, rate limiting, and account recovery are not implemented yet.

## Tests

Run `cd backend && mvn test`. Integration tests use PostgreSQL and cover patient creation/retrieval, request validation, duplicate email handling, appointment creation, missing-patient responses, login, and rejection of unauthenticated requests.

## Safety

This is a learning/demo project, not a clinical system. Use synthetic data only. It has no production security review, complete authorization/ownership controls, audit trail, or clinical validation and must not store real patient information.
