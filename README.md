# Fleet Flux Java API

Simple Quarkus-based API with PostgreSQL, Keycloak, and Mailpit for local development.

---

## Prerequisites

* Java 25+
* Maven (or use `./mvnw`)
* Docker + Docker Compose
* `curl` and `jq` (for scripts)

---

## Run the application (dev mode)

```bash
./mvnw compile quarkus:dev
```

* API: http://localhost:8080
* Dev UI: http://localhost:8080/q/dev/

---

## Run tests

```bash
./mvnw test
```

---

## Package the application

```bash
./mvnw package
```

Run it:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Docker Compose (infra services)

Start all dependencies:

```bash
docker compose up -d
```

### Services

* **PostgreSQL** → `localhost:5432`

    * DB: `fleet-flux-java`
    * User: `fleet-flux`
    * Password: `fleet-flux-password`

* **Keycloak** → http://localhost:8081

    * User: `admin`
    * Password: `admin`

* **Mailpit (email testing)** → http://localhost:8025

Stop services:

```bash
docker compose down
```

---

## Keycloak cleanup script

This script deletes all Keycloak realms except protected ones (`master`, `fleet-flux-admin`).

### Run it

```bash
./clean-realms.sh
```

### Config (optional)

You can override defaults:

```bash
KEYCLOAK_URL=http://localhost:8081 \
ADMIN_USER=admin \
ADMIN_PASS=admin \
./clean-realms.sh
```

### What it does

* Authenticates with Keycloak admin API
* Fetches all realms
* Deletes everything except protected ones
* Prints a summary

---

## Typical workflow

1. Start infra:

   ```bash
   docker compose up -d
   ```

2. Run API:

   ```bash
   ./mvnw quarkus:dev
   ```

3. Run tests:

   ```bash
   ./mvnw test
   ```

4. (Optional) Reset Keycloak:

   ```bash
   ./clean-realms.sh
   ```

---

That’s it — minimal setup to get the project running locally.
