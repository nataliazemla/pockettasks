# Pocket Tasks — SOLID + GraphQL (Apollo) in a small Android app

A minimal Jetpack Compose app built to demonstrate **SOLID** principles in a practical, reviewable codebase.
It includes a small GraphQL integration (Apollo Kotlin) that fits the product: **Task Templates** fetched from a public GraphQL API.

## What this project shows
- Kotlin + Coroutines/Flow + Compose (minimal UI, solid architecture)
- MVVM with explicit state (`StateFlow`)
- SOLID applied in real code (not theoretical)
- GraphQL via Apollo Kotlin: schema, query + fragment, codegen, repository mapping, error handling
- Clean separation: domain models are independent from Apollo/Android

---

## App features (MVP)
- Create tasks locally (in-memory)
- Toggle task done
- Sort tasks (Strategy pattern)
- **Templates (GraphQL):** fetch remote templates and insert title into the input

---

## SOLID in this codebase (map)
### S — Single Responsibility
- `AddTaskUseCase` validates & adds tasks (no UI/network logic)
- `ApolloTemplatesRepository` only fetches/maps remote templates

### O — Open/Closed
- Sorting implemented via `TaskSortStrategy`
- New sort modes can be added by implementing a new strategy (no changes in use cases)

### L — Liskov Substitution
- `InMemoryTaskRepository` can be replaced by fakes in tests without breaking use cases/VM

### I — Interface Segregation
- `TaskReadRepository` vs `TaskWriteRepository`
- `TemplatesReadRepository` (and `TemplatesWriteRepository` if enabled)

### D — Dependency Inversion
- ViewModel/use cases depend on repository interfaces (domain), not concrete data sources (data)
- Wiring happens in `AppGraph` (composition root)

---

## Project structure (high-level)
- `domain/` — pure Kotlin (models, use cases, interfaces)
- `data/` — implementations (in-memory repo, Apollo repo)
- `presentation/` — ViewModel + UI state
- `ui/` — Compose screens/components (optional split)
- `core/` — AppGraph / wiring + factories

---


## GraphQL (Apollo) — schema & codegen
This project uses a public GraphQL endpoint: GraphQLZero.

### Download schema (from terminal)
```bash
./gradlew :app:downloadApolloSchema --endpoint="https://graphqlzero.almansi.me/api" --schema="app/src/main/graphql/templates/schema.graphqls"
