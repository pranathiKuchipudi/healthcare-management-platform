# Healthcare Management Platform

A Java 21, Spring Boot and PostgreSQL learning project. This is a demo application only; use synthetic data and never real patient information.

## Starter milestone
- Patient and appointment REST APIs
- PostgreSQL persistence with Spring Data JPA
- Request validation and centralized API errors
- Docker Compose for local PostgreSQL

See `docs/architecture.md` for the initial design. See `backend/` for the Spring Boot application.

## Run locally
1. Start PostgreSQL: `docker compose up -d`
2. Run the API: `cd backend && mvn spring-boot:run`
3. API base URL: `http://localhost:8080`

Java 21 and Maven 3.9+ are required. Tests should be run with `mvn test` before treating this milestone as verified.