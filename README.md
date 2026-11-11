# Healthcare Management Platform

A Java 21, Spring Boot and PostgreSQL learning project. This is a demo application only; use synthetic data and never real patient information.

## Starter milestone
- Patient and appointment REST APIs
- PostgreSQL persistence with Spring Data JPA
- Request validation and centralized API errors
- Docker Compose for local PostgreSQL

See `docs/architecture.md` for the initial design. See `backend/` for the Spring Boot application.

## Run locally

Prerequisites: Docker Desktop, Java 21, and Maven 3.9+.

1. Start PostgreSQL: `docker compose up -d`
2. Run the API: `cd backend && mvn spring-boot:run`
3. API base URL: `http://localhost:8080`

The API reads database settings from environment variables. Defaults are intended only for local development; do not reuse them in a deployed environment.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/v1/patients` | Create a patient |
| GET | `/api/v1/patients` | List patients |
| GET | `/api/v1/patients/{id}` | Get a patient |
| PUT | `/api/v1/patients/{id}` | Update a patient |
| DELETE | `/api/v1/patients/{id}` | Delete a patient |
| POST | `/api/v1/appointments` | Create an appointment |
| GET | `/api/v1/appointments` | List appointments |
| GET | `/api/v1/appointments/{id}` | Get an appointment |

Patient requests require first name, last name, date of birth in the past, and a valid email. Appointment requests require an existing patient, a present/future appointment time, and a reason. Validation failures return HTTP 400; missing resources return 404; duplicate/conflicting data returns 409.

## Tests

Run `cd backend && mvn test`. Integration tests use the configured PostgreSQL database and cover patient creation/retrieval, request validation, duplicate email handling, appointment creation, and missing-patient responses.

## Safety

This is a learning/demo project, not a clinical system. Use synthetic data only. It has no authentication, authorization, audit trail, encryption policy, or production hardening and must not be used to store real patient information.
