# REST API with Rate Limiting

This is a Spring Boot REST API that provides two endpoints (`/foo` and `/bar`) with per-client rate limiting using different algorithms. The application supports configuration-driven limits, persistent and in-memory storage, and multiple Spring profiles for local development and production.

## ✅ Features

- Two endpoints: `/foo` and `/bar`
- `/foo` uses **Sliding Window Rate Limiter**
- `/bar` uses **Leaky Bucket Rate Limiter**
- Client identification via `Authorization: Bearer <client-id>` header
- Configurable limits per client and path via YAML config
- Rate limiter persistence (Leaky Bucket) using:
  - H2 in-memory database for the `local` profile
  - PostgreSQL for the `prod` profile
- Dockerized for deployment
- Unit tests for key logic

---
## 🛠️ How to Build

To build the application into a runnable JAR file, run the following command from the project root:

```bash
./mvnw clean package -DskipTests
```

This will create the file:

```
target/api-0.0.1-SNAPSHOT.jar
```
---

## 🚀 How to Run

### 1. Local (H2 database)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

or

```bash
java -Dspring.profiles.active=local -jar target/api-0.0.1-SNAPSHOT.jar
```

### 2. Production (PostgreSQL)

Make sure PostgreSQL is available and credentials are configured in `application-prod.yml`, then run:

```bash
java -Dspring.profiles.active=prod -jar target/api-0.0.1-SNAPSHOT.jar
```

---

## 🐳 Run with Docker

To build and run the container:

```bash
docker build -t api-limiter .
docker run -p 8080:8080 api-limiter
```

To use PostgreSQL in production:

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/yourdb \
  -e SPRING_DATASOURCE_USERNAME=youruser \
  -e SPRING_DATASOURCE_PASSWORD=yourpass \
  api-limiter
```

---

## 📬 Endpoints

### `/foo`

- Uses: **Sliding Window Limiter**
- Success: `200 OK` + `{ "success": true }`
- If limited: `429 Too Many Requests` + `{ "error": "rate limit exceeded" }`

### `/bar`

- Uses: **Leaky Bucket Limiter**
- Behavior same as `/foo` but backed by a database for persistence

---

## 🔐 Authentication

All requests must include a header:

```
Authorization: Bearer <client-id>
```

Clients are matched against the configuration, and rate limits are applied accordingly.

---

## ⚙️ Client Configuration (example from YAML)

```yaml
clients:
  - id: client-a
    rateLimits:
      - path: /foo
        numberOfRequests: 3
        interval:
          value: 10
          unit: SECONDS

      - path: /bar
        numberOfRequests: 5
        interval:
          value: 15
          unit: SECONDS

  - id: client-b
    rateLimits:
      - path: /foo
        numberOfRequests: 3
        interval:
          value: 10
          unit: SECONDS

      - path: /bar
        numberOfRequests: 5
        interval:
          value: 15
          unit: SECONDS
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 🏗 Rate Limiter Overview

| Limiter               | Algorithm     | Persistence |
|-----------------------|---------------|-------------|
| `/foo`                | Sliding Window| In-memory   |
| `/bar`                | Leaky Bucket  | H2/Postgres |

---

## 🔧 Spring Profiles

| Profile   | Database     | Usage         |
|-----------|--------------|---------------|
| `local`   | H2           | Dev/Test      |
| `prod`    | PostgreSQL   | Production    |

---

## 📜 License

This project is for demonstration, testing, or technical evaluation purposes.
---

## 🌐 Live Demo (Hosted on Render)

You can test the API live using the following base URL:

```
https://rest-api-with-limiter-deploy-test.onrender.com
> ⚠️ Note: Since the app is hosted on Render (free tier), it may take up to 30 seconds to respond if it hasn't been accessed recently (cold start).

```

Example `curl` command:

```bash
curl -X GET https://rest-api-with-limiter.onrender.com/foo -H "Authorization: Bearer client-a"
```

Make sure to replace `client-a` with a valid client ID from the configuration.
