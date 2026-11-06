# Filter & Routing with an API Gateway (formerly Netflix Zuul)

[![Java CI with Maven](https://github.com/hendisantika/Filter-Routing-With-Netflix-Zuul/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/Filter-Routing-With-Netflix-Zuul/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.9-6DB33F?logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.4-6DB33F?logo=spring)

This bundle consists of two simple Spring Boot projects:

| Module    | Port   | Description                                                                                         |
|-----------|--------|-----------------------------------------------------------------------------------------------------|
| `login`   | `1907` | Login microservice with a single endpoint `GET /login` returning `Login done!`                       |
| `gateway` | `8080` | API gateway / reverse proxy that filters incoming requests and routes them to the proper microservice |

> **Note on Netflix Zuul**
> This project originally used Netflix Zuul 1 (`@EnableZuulProxy`). Zuul was removed from Spring Cloud in
> 2020.0 and is `javax`-based, so it cannot run on Spring Boot 3/4 (`jakarta`). The gateway has been migrated to
> its servlet-based successor, **[Spring Cloud Gateway Server WebMVC](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webmvc.html)**,
> keeping the same route and the same "pre" filter behaviour.

## Tech stack

- Java **25** (Temurin / any JDK 25 distribution)
- Spring Boot **4.0.9**
- Spring Cloud **2025.1.4** (Oakwood) – Spring Cloud Gateway Server WebMVC
- Maven 3.9.11 (via Maven Wrapper)
- JUnit 5

## Architecture

```
                     ┌──────────────────────────────┐            ┌──────────────────────┐
  client ──────────▶ │ gateway :8080                │ ─────────▶ │ login :1907          │
  GET /loginapp/login│  1. LoginPreFilter (log req) │  GET /login│  LoginApplication    │
                     │  2. uri(gateway.routes...)   │            │  "Login done!"       │
                     │  3. StripPrefix=1            │            └──────────────────────┘
                     └──────────────────────────────┘
```

### login

A simple `@RestController` with one request mapping (`/login`) returning a simple string. The server port is set
to `1907` in `login/src/main/resources/application.properties`.

### gateway

- `GatewayRoutesConfig` (`com.hendisantika.gateway.config`) defines the `loginapp` route with the Spring Cloud
  Gateway WebMVC Java DSL: requests matching `/loginapp/**` are forwarded to the login service with the
  `/loginapp` prefix stripped.
- `LoginPreFilter` (`com.hendisantika.gateway.filter`) is a *before* filter (the equivalent of a Zuul `pre`
  filter) that logs every incoming request before it is routed.
- The downstream URL is configured in `gateway/src/main/resources/application.properties`:

  ```properties
  server.port=8080
  gateway.routes.loginapp.url=http://localhost:1907
  ```

## Running locally

Requirements: JDK 25 on your `PATH` (Maven is downloaded automatically by the wrapper).

```bash
# terminal 1 – login microservice
cd login
./mvnw spring-boot:run

# terminal 2 – gateway
cd gateway
./mvnw spring-boot:run
```

Then call the login microservice through the gateway:

```bash
curl http://localhost:8080/loginapp/login
# Login done!
```

The request `http://localhost:8080/loginapp/login` is routed to `http://localhost:1907/login`, and the gateway log
shows:

```
c.h.gateway.filter.LoginPreFilter : incoming request is captured by filter : GET - /loginapp/login
```

## Build & test

```bash
cd login   && ./mvnw verify
cd gateway && ./mvnw verify
```

Tests:

- `login` – MockMvc test for `GET /login`.
- `gateway` – end-to-end routing test (`/loginapp/login` → stub login service) and a unit test for `LoginPreFilter`.

## CI/CD

GitHub Actions ([`.github/workflows/maven.yml`](.github/workflows/maven.yml)):

- Builds and tests `login` and `gateway` in a matrix on **JDK 25 (Temurin)** with `mvn verify`.
- Uploads Surefire test reports as artifacts.
- On push to `master`, submits the Maven dependency graph so Dependabot alerts are accurate.

## Dependency updates

Dependabot ([`.github/dependabot.yml`](.github/dependabot.yml)) checks `login`, `gateway` and GitHub Actions every
Monday at 05:00 (Asia/Jakarta):

- Updates are **grouped** (Spring, Maven plugins, other minor/patch, GitHub Actions) to keep PR noise low.
- PRs are labelled and use conventional commit prefixes (`build(deps)`, `ci(deps)`).
- For `gateway`, Spring Boot minor/major and Spring Cloud release-train major bumps are ignored, because each Spring
  Cloud release train supports only one Spring Boot minor line (2025.1.x ↔ Boot 4.0.x). Upgrade those two together
  manually.

## Author

**Hendi Santika**

- Email: hendisantika@gmail.com
- Telegram: [@hendisantika34](https://t.me/hendisantika34)
- Link: [s.id/hendisantika](https://s.id/hendisantika)
