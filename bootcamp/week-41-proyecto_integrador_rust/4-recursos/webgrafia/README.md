# Webgrafía - Semana 41

| Recurso | URL | Para qué |
|---|---|---|
| Filtersets de nextest | https://nexte.st/docs/filtersets/ | `binary()`, `kind()`, `test()` y expresiones regulares para separar las fases de `verify.sh`. |
| Ejecutar tests con nextest | https://nexte.st/docs/running/ | Paralelismo (`-j`), fail-fast y qué significa cada línea de la salida. |
| cargo-llvm-cov | https://github.com/taiki-e/cargo-llvm-cov | `nextest`, `--no-report`, `clean` y `report` con umbrales. |
| Cargo Targets | https://doc.rust-lang.org/cargo/reference/cargo-targets.html | Qué targets tienen `test` y `bench` activados por defecto: por qué `cargo test --benches` repite los unitarios. |
| `#[sqlx::test]` | https://docs.rs/sqlx/0.9.1/sqlx/attr.test.html | Cómo crea y borra una base de datos por test. |
| `docker compose up` | https://docs.docker.com/reference/cli/docker/compose/up/ | `-d`, `--wait` y `-f` para arrancar PostgreSQL desde el script. |
| The Set Builtin (Bash) | https://www.gnu.org/software/bash/manual/html_node/The-Set-Builtin.html | `-e`, `-u` y `-o pipefail`. |
| Faster Rust tests with cargo-nextest | https://blog.jetbrains.com/rust/2026/05/01/faster-rust-tests-with-cargo-nextest/ | El resumen escrito del video de nextest. |

## Dinámica sugerida

- Filtersets durante el paso 1 del lab 01.
- cargo-llvm-cov antes del Experimento 1 del lab 01.
- Cargo Targets y `#[sqlx::test]` mientras escribes el reporte.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 41](../../README.md)
