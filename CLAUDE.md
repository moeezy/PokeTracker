# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

PokeTracker: a Spring Boot 4 / Java 21 backend for tracking HeartGold/SoulSilver ("heartgold") Pokedex completion — which Pokemon a user has caught/shiny-caught, and where/how to encounter each Pokemon on each in-game route. Pokemon and route-encounter data is bulk-imported from PokeAPI (https://pokeapi.co/api/v2) into Postgres; user progress is tracked separately per-user.

## Commands

```
./mvnw.cmd compile              # compile
./mvnw.cmd test                 # run all tests
./mvnw.cmd test -Dtest=RouteEncounterServiceTest            # run a single test class
./mvnw.cmd test -Dtest=RouteEncounterServiceTest#shouldFindAvailableRoutePokemon   # run a single test method
./mvnw.cmd spring-boot:run       # run the app locally
```

There is no linter configured. On Windows use `mvnw.cmd`; the POSIX `mvnw` wrapper is also present for other shells.

## Architecture

Standard layered structure under `com.moeezy.PokeTracker`: `web` (REST controllers) → `service` → `data.repository` (Spring Data JPA) → `data.entity`. Request/response shapes that aren't entities live in `data.dto`, split into `Auth`, `PokeApi`, and `RouteEncounterMap` sub-packages.

### Auth (JWT, stateless)

- `security/WebSecurityConfig` — the filter chain. Only `/v1/users/register` and `/v1/users/login` are `permitAll()`; everything else requires a valid bearer token. CSRF is disabled and sessions are `STATELESS` since auth is JWT-only, not cookie/session-based.
- `security/JwtTokenFilter` — runs before `UsernamePasswordAuthenticationFilter`, resolves the `Authorization: Bearer <token>` header, validates it, and populates the `SecurityContext`.
- `security/JwtTokenProvider` — issues/parses tokens (jjwt, HMAC-SHA256 key derived from `security.jwt.token.secret-key`). Tokens carry no roles/claims beyond subject+expiry (no role-based auth in this app). When building `Authentication`, the 3-arg `UsernamePasswordAuthenticationToken` constructor must be used — the 2-arg one always yields `isAuthenticated() == false` and every protected endpoint will 403 even with a valid token.
- `security/UserDetailsConfig` — bridges `UserRepository` to Spring Security's `UserDetailsService`.
- `service/AuthService` — register (checks username/email uniqueness, bcrypt-hashes password before persisting) and login (delegates credential check to `AuthenticationManager`, then mints a JWT).
- There is no `@RestControllerAdvice` — exceptions use `@ResponseStatus` directly on the exception class (see `web/exception/*`). Be aware that Spring's default error handling can forward 403s through `/error` in a way that masks the original status if a custom error page/controller is added later.

### PokeAPI ingestion (one-off/admin data population, not a normal request path)

- A single `WebClient` bean (`PokeTrackerApplication`) is configured with `baseUrl` = PokeAPI v2 and a 10MB in-memory buffer (PokeAPI responses can be large).
- `service/PokeApiService` — fetches species/type data per Pokemon ID and upserts into the `pokemon` table (native upsert query); `saveAllPokemon()` loops IDs 1..493 (Sinnoh dex cutoff).
- `service/PokeApiEncounterService` — walks PokeAPI's region → location → location-area → pokemon_encounters hierarchy and persists into `route`/`route_encounters` tables, filtered to `GAME = "heartgold"`. `processPokemonEncounters` de-dupes/merges encounter variants (time-of-day, radio, swarm conditions) per `EncounterKey(method, time)` — prefers the plain (no radio/swarm) variant over conditional ones. IDs extracted from PokeAPI URLs are parsed via substring, not a URL/path library.
- These flows are triggered manually via `web/PokeApiController` GET endpoints (e.g. `/v1/PokeApi/gen4`, `/v1/PokeApi/{id}/Routes`) — they are import jobs invoked through the browser/curl, not part of normal app usage, and are **not** covered by the security permitAll list (must be called with a valid JWT).

### User progress data

- `UserPokemon` is a join entity (composite key `UserPokemonId`: userId + pokedexNumber) tracking `caught`/`shiny` per user per Pokemon.
- `RouteEncounter` has a 5-column composite key (`RouteEncounterId`: routeId, areaId, pokedexNumber, time, method) since the same Pokemon can appear on the same route via multiple methods/times.
- `RouteEncounterService.findAvailablePokemonMap` builds a nested `Route → Area → Encounter` DTO tree from a flat native-SQL row projection (`RouteEncounterSqlRowProjection`), using `LinkedHashMap`s keyed by route/area ID to group rows while preserving query order.
- Several repository methods use native SQL with Postgres `ON CONFLICT ... DO UPDATE` upserts (see `UserPokemonRepository.upsertUserPokemon`) rather than JPA `save()`, since JPA doesn't have first-class upsert support.

### Config

- `application.properties`: local Postgres connection (`poketracker` DB), `spring.jpa.hibernate.ddl-auto=none` — **schema is not managed by Hibernate; DDL changes must be applied manually against the DB.**
- `.properties` values are taken literally (no quoting) — quoting the JWT secret would make the quote characters part of the secret itself.
