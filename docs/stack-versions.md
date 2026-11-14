# Versiones del stack

Fuente única de versiones del bootcamp. Cuando una versión cambia, se actualiza **esta tabla** y los archivos de la columna "Dónde se fija"; ningún otro documento copia las versiones.

"Se fija en SXX" significa que la versión exacta se elige y se verifica con ejecuciones reales al construir esa semana.

## JavaScript

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Node.js | 22 | `.nvmrc`, `engines` de cada `package.json` | LTS activa |
| pnpm | 12.6.0 | `packageManager` de cada `package.json` | Único gestor de JS del curso; necesita `pnpm-workspace.yaml` con `allowBuilds` |
| Jest | 30.5.2 | `devDependencies` | Framework de la Etapa 1 |
| Supertest | 7.3.0 | `devDependencies` | Testing de APIs Express (S12, S15) |
| Express | 5.2.1 | `dependencies` | API bajo prueba (S12, S15) |
| fast-check | 4.10.2 | `devDependencies` | Property-based testing (S13) |
| Playwright | 1.63.0 | `devDependencies` | E2E (S42) |
| k6 | se fija en S43 | binario / imagen Docker | Pruebas de carga |

## Python

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Python | 3.14 | `.python-version`, `requires-python` | Versión estable actual |
| uv | última estable | equipo del estudiante | Único gestor de Python del curso |
| pytest | 9.1.1 | `[dependency-groups] dev` | Framework de la Etapa 2 |
| pytest-mock | 3.15.1 | `[dependency-groups] dev` | Wrapper de `unittest.mock` (S18) |
| pytest-cov | 7.1.0 | `[dependency-groups] dev` | Coverage (S21) |
| coverage | 7.17.3 | `[dependency-groups] dev` | `coverage run --append` para combinar pytest y Behave (S24) |
| mutmut | 3.8.0 | `[dependency-groups] dev` | Mutation testing (S21; en Windows, dentro de WSL) |
| ruff | 0.16.7 | `[dependency-groups] dev` | Umbral de complejidad `C901` (S21) |
| httpx2 | 2.13.1 | `dependencies` | Cliente HTTP con `MockTransport` (S19; `respx` no intercepta `httpx2`) |
| pydantic | 2.13.5 | `dependencies` | Validación de contratos (S19) |
| pytest-asyncio | 1.4.0 | `[dependency-groups] dev` | Tests async, `asyncio_mode = "strict"` (S19) |
| hypothesis | 6.168.0 | `[dependency-groups] dev` | Property-based testing (S20) |
| mypy | 2.3.1 | `[dependency-groups] dev` | Tipado estricto (S20) |
| Behave | 1.3.3 | `[dependency-groups] dev` | BDD con Gherkin en español (S22) |
| pytest-bdd | 8.1.0 | `[dependency-groups] dev` | BDD dentro de pytest; filtrar `PytestRemovedIn10Warning` (S22) |
| SQLAlchemy | 2.1.8 | `dependencies` | ORM de la capa de datos; estilo 2.0 con `select()` (S23) |
| psycopg | 3.3.9 (`[binary]`) | `dependencies` | Driver de PostgreSQL (S23) |
| Alembic | 1.20.1 | `dependencies` | Migraciones; `command.check` en tests (S23) |
| Faker | 40.54.0 | `[dependency-groups] dev` | Datos realistas con semilla (S23) |
| Factory Boy | 3.3.3 | `[dependency-groups] dev` | `SQLAlchemyModelFactory` (S23) |
| testcontainers | 4.15.0 (`[postgres]`) | `[dependency-groups] dev` | PostgreSQL desechable; importar de `testcontainers.community.postgres` (S23) |
| Locust | se fija en S43 | `[dependency-groups] dev` | Pruebas de carga |

## Java

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| JDK | 21 LTS | `maven.compiler.release` | LTS |
| Maven | 3.9+ | equipo del estudiante | Build tool del curso |
| JUnit (Jupiter) | 6.0.3 | `pom.xml` | La versión que gestiona Spring Boot 4.1.1 |
| AssertJ | 3.27.7 | `pom.xml` | Aserciones fluentes |
| maven-surefire-plugin | 3.5.6 | `pom.xml` | Ejecución de tests |
| Mockito | 5.23.0 | `pom.xml` | Mocking (S26) |
| Spring Boot | 4.1.1 | `pom.xml` (parent) | Testing de Spring Boot (S28) |
| Testcontainers | 2.0.5 | `pom.xml` | PostgreSQL real en tests (S29) |
| JaCoCo | 0.8.15 | `pom.xml` | Coverage |
| Cucumber | se fija en S30 | `pom.xml` | BDD con JUnit Platform |

## Go

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Go | se fija en S32 | directiva `go` de `go.mod` | Stdlib `testing` con `t.Context`, `b.Loop()` y `testing/synctest` |
| go-cmp | se fija en S33 | `go.mod` | Comparación de estructuras con diff |
| go.uber.org/mock | se fija en S33 | `go.mod` + `go tool` | Mocks generados con mockgen |
| testify | se fija en S33 | `go.mod` | `assert`/`require` |
| testcontainers-go | se fija en S34 | `go.mod` | PostgreSQL real en tests |

## Rust

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Rust | se fija en S37 | `rust-toolchain.toml` | Toolchain stable; nightly solo para `cargo-fuzz` (S40) |
| mockall / rstest / insta | se fija en S38 | `[dev-dependencies]` | Mocks de traits, fixtures y snapshots |
| tokio / axum / sqlx / wiremock / testcontainers | se fija en S39 | `Cargo.toml` | Async, APIs y PostgreSQL |
| proptest / criterion | se fija en S40 | `[dev-dependencies]` | Propiedades y benchmarks |
| cargo-llvm-cov / cargo-nextest / cargo-mutants | se fija en S40 | `cargo install --locked` | Coverage, runner y mutation testing |

## Transversal

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| PostgreSQL | 18.7 (`postgres:18.7-alpine`) | `compose.yaml` (puerto `127.0.0.1:55432`) y `POSTGRES_IMAGE` de los tests | Única base de datos del curso |
| Docker + Compose | v2+ | equipo del estudiante | Testcontainers (desde S23) y OWASP ZAP (S44) |
| jq | 1.7+ | equipo del estudiante | `scripts/verificar-semana.sh` lee `checks.json` |

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Plan de estudios](plan-estudios.md) | [Decisión de duración](decision-semanas.md) → |

← [Volver al inicio del bootcamp](../README.md)
