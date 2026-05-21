> Note: this README.md file was generated with Claude Code.
# Epigram

A web app that displays random epigrams, with manual and automatic refresh, and a form to submit your own. Inspired by the `fortune` CLI tool.

## Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Frontend   | React 19 + Vite (TypeScript)      |
| Backend    | Spring Boot 4, Java 21            |
| Database   | PostgreSQL 16                     |
| Migrations | Flyway                            |
| Serving    | nginx (proxies `/api` in Docker)  |

## Running with Docker

```sh
docker compose up --build
```

The app will be available at **http://localhost:5173**.

On first start, Flyway runs the migrations and seeds the database with 20 epigrams. Subsequent starts reuse the `postgres_data` volume.

To stop and remove containers:

```sh
docker compose down
```

To also wipe the database volume:

```sh
docker compose down -v
```

## Running in development

**Prerequisites:** Java 21, Maven, Node 20, a running PostgreSQL instance.

Start postgres:

```sh
docker compose up postgres
```

Start the API:

```sh
cd epigram-api
./mvnw spring-boot:run
```

Start the UI (proxies `/api` to `localhost:8080` via Vite):

```sh
cd epigram-ui
npm install
npm run dev
```

The app will be available at **http://localhost:5173**.

## API

Base path: `/api/epigrams`

| Method | Path      | Description              | Body                        | Response     |
|--------|-----------|--------------------------|-----------------------------|--------------|
| GET    | `/random` | Returns a random epigram | —                           | `EpigramDto` |
| GET    | `/`       | Returns all epigrams     | —                           | `EpigramDto[]` |
| POST   | `/`       | Submits a new epigram    | `{ text, author?, source? }` | `EpigramDto` (201) |

Error responses: `400` for blank text, `409` for duplicate text.

## Architecture

```
Browser
  │
  └─▶ nginx :80 (mapped to host :5173)
        │
        ├─▶ /         → serves static React bundle (dist/)
        └─▶ /api/*    → proxies to epigram-api :8080
                            │
                            └─▶ PostgreSQL :5432
```

The Spring Boot API follows a standard layered structure:

```
EpigramController  →  EpigramService  →  EpigramRepository  →  PostgreSQL
```

Flyway manages the schema. Two migrations run on startup:
- `V1` — creates the `epigrams` table
- `V2` — seeds 20 epigrams from a diverse set of authors
