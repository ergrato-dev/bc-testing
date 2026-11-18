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
| Playwright | 1.63.0 (`@playwright/test`; navegadores con `pnpm exec playwright install`) | `devDependencies` | E2E (S42). Sin build scripts: no necesita `pnpm-workspace.yaml` |
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
| maven-failsafe-plugin | 3.5.6 (gestionada por Spring Boot) | `pom.xml` | Tests de integración `*IT` en `mvn verify` (S30, teoría 04; S31) |
| Mockito | 5.23.0 | `pom.xml` | Mocking (S26) |
| maven-dependency-plugin | 3.11.0 | `pom.xml` | Ruta del jar de Mockito para cargarlo como agente (S26) |
| ArchUnit | 1.5.1 (`archunit-junit5`) | `pom.xml` | Fitness functions de arquitectura con JUnit 6 (S27) |
| Spring Boot | 4.1.1 | `pom.xml` (parent) | Testing de Spring Boot (S28) |
| Spring Framework | 7.0.9 (gestionada por Spring Boot) | parent | `MockMvcTester`, `RestTestClient`, `@MockitoBean` y `@TestBean` (S28) |
| Jackson | 3.1.5 (gestionada por Spring Boot) | parent | `@JsonTest` y el JSON de la API (S28) |
| Hibernate Validator | 9.1.3 (gestionada por Spring Boot) | parent | Bean Validation con `spring-boot-starter-validation` (S28) |
| Testcontainers | 2.0.5 (gestionada por Spring Boot; `testcontainers-junit-jupiter`, `-postgresql`, `-kafka`) | `pom.xml` | PostgreSQL y Kafka reales en tests; imagen auxiliar `testcontainers/ryuk:0.14.0` (S29) |
| Flyway | 12.4.0 (gestionada por Spring Boot; con `flyway-database-postgresql`) | `pom.xml` | Migraciones del esquema en producción y en tests (S29) |
| Driver PostgreSQL (JDBC) | 42.7.13 (gestionada por Spring Boot) | `pom.xml` | `org.postgresql:postgresql` (S29) |
| kafka-clients | 4.2.1 (gestionada por Spring Boot) | `pom.xml` | Productor y consumidor de Kafka (S29) |
| JaCoCo | 0.8.16 (`jacoco-maven-plugin`) | `pom.xml` | Cobertura de Surefire y Failsafe en un solo `jacoco.exec`; `check` con el 85 % de líneas (S31) |
| Cucumber | 8.1.0 (`cucumber-bom`: `cucumber-java`, `cucumber-spring`, `cucumber-junit-platform-engine`) | `pom.xml` | BDD con JUnit Platform; `junit-platform-suite` lo gestiona Spring Boot (S30) |

## Go

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Go | 1.26.5 (S32) | directiva `go` de `go.mod` | Stdlib `testing` con `t.Context`, `b.Loop()` y `testing/synctest` |
| go-cmp | v0.7.0 (S33) | `go.mod` | `cmp.Diff` y `cmpopts` para comparar estructuras y golden files |
| go.uber.org/mock | v0.6.0 (S33) | `go.mod`: `tool` (mockgen) + `require` (gomock) | Mocks generados con `go tool mockgen` desde `//go:generate` |
| testify | v1.12.1 (S33) | `go.mod` | `assert`/`require` y `testify/mock` (lab 03; el proyecto usa mockgen y go-cmp) |
| testcontainers-go | v0.45.0 (S34; con el módulo `modules/postgres`) | `go.mod` | PostgreSQL real en tests; imagen auxiliar `testcontainers/ryuk:0.14.0` |
| pgx | v5.13.0 (S34; `github.com/jackc/pgx/v5`) | `go.mod` | Driver de PostgreSQL, `pgxpool` y `pgx.Tx` |
| staticcheck | v0.8.2 (S35; `honnef.co/go/tools`, publicada como 2026.2.2) | `go.mod`: `tool` | Linter: errores probables, estilo y código sin usar que `go vet` no ve |
| benchstat | v0.0.0-20261117185222-4febdb082b2c (S35; `golang.org/x/perf`, sin versiones publicadas) | `go.mod`: `tool` | Comparación estadística de benchmarks antes y después |
| golang.org/x/text | v0.44.0 (S35) | `go.mod` | `cases.Title` en lugar de `strings.Title` (lab 04) |
| golangci-lint | v2.15.0 (S35) | binario aparte (`go install`), opcional | Varios linters en una pasada, normalmente en CI |

## Rust

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| Rust | 1.100.0 (S37; edición 2024, mínimo 1.85) | `rust-toolchain.toml`: `channel` y `components = ["rustfmt", "clippy"]` | Toolchain stable; nightly solo para `cargo-fuzz` (S40: `rustup toolchain install nightly --profile minimal`) |
| pretty_assertions | 1.4.1 (S37) | `[dev-dependencies]`: `"=1.4.1"` | `assert_eq!` con diff entre los dos valores |
| mockall | 0.15.0 (S38) | `[dev-dependencies]`: `"=0.15.0"` | Mocks de traits con `#[automock]` y `mock!` |
| rstest | 0.28.0 (S38) | `[dev-dependencies]`: `"=0.28.0"` | Fixtures y tests parametrizados con `#[case]` y `#[values]` |
| insta / cargo-insta | 1.49.0 (S38) | `[dev-dependencies]`: `"=1.49.0"`; la herramienta con `cargo install --locked cargo-insta@1.49.0` | Snapshots y su revisión con `cargo insta review` |
| tokio | 1.54.1 (S39) | `[dev-dependencies]` (`macros`, `rt-multi-thread`; `time` y `test-util` para el reloj) y, si la librería usa su reloj, `[dependencies]` | Runtime async y `#[tokio::test]` con `start_paused` |
| axum / tower | 0.8.9 / 0.5.3 (S39) | `[dependencies]` axum; `[dev-dependencies]` tower con `util` | API HTTP y `oneshot` en los tests |
| serde / serde_json / http-body-util | 1.0.229 / 1.0.154 / 0.1.5 (S39) | serde con `derive` en `[dependencies]`; los otros dos en `[dev-dependencies]` | JSON de la API y lectura del cuerpo en los tests |
| reqwest | 0.13.5 (S39; `default-features = false`, `json`) | `[dependencies]` | Cliente HTTP de servicios externos, sin TLS en los tests |
| wiremock | 0.6.5 (S39) | `[dev-dependencies]` | Servidor HTTP simulado para probar clientes |
| sqlx | 0.9.1 (S39; `default-features = false`, `runtime-tokio`, `postgres`, `migrate`, `macros`) | `[dependencies]`; `DATABASE_URL` en `.cargo/config.toml` | PostgreSQL y `#[sqlx::test]` con una base por test |
| testcontainers-modules | 0.15.0 (S39; `postgres`; trae testcontainers 0.27.3) | `[dev-dependencies]` | PostgreSQL desde el test; `testcontainers` se importa desde `testcontainers_modules` |
| proptest / criterion | 1.12.0 / 0.8.2 (S40; criterion con `html_reports`) | `[dev-dependencies]`; cada benchmark con `[[bench]]` y `harness = false` | Propiedades y benchmarks |
| cargo-llvm-cov / cargo-nextest / cargo-mutants | 0.9.1 / 0.9.152 / 27.1.0 (S40) | `cargo install --locked`; cargo-llvm-cov necesita `llvm-tools-preview` en `components` | Coverage, runner y mutation testing |
| cargo-fuzz | 0.13.2 (S40; libfuzzer-sys 0.4.13; nightly 1.102.0 del 2026-11-17) | `cargo install --locked`; se ejecuta con `cargo +nightly fuzz` | Fuzzing con libFuzzer, solo en el lab 02 |

## Transversal

| Componente | Versión | Dónde se fija | Por qué |
|---|---|---|---|
| PostgreSQL | 18.7 (`postgres:18.7-alpine`) | `compose.yaml` (puerto `127.0.0.1:55432`) y `POSTGRES_IMAGE` de los tests | Única base de datos del curso |
| Apache Kafka | 4.2.1 (`apache/kafka-native:4.2.1`) | `KafkaContainer` de los tests | Broker real para los tests de mensajería (S29); misma versión que `kafka-clients` |
| Imagen de Playwright | `mcr.microsoft.com/playwright:v1.63.0-noble` (Ubuntu 24.04, Node 24 y los tres navegadores) | `docker run` del lab 04 de S42 | WebKit en Linux sin sus bibliotecas del sistema; la versión es siempre la de `@playwright/test` |
| Docker + Compose | v2+ | equipo del estudiante | Testcontainers (desde S23) y OWASP ZAP (S44) |
| jq | 1.7+ | equipo del estudiante | `scripts/verificar-semana.sh` lee `checks.json` |

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Plan de estudios](plan-estudios.md) | [Decisión de duración](decision-semanas.md) → |

← [Volver al inicio del bootcamp](../README.md)
