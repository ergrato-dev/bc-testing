# Prácticas - Semana 39

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un paquete de Rust y los tests comentados por PASOS; lo que no es descomentar, como cambiar una línea o arrancar un contenedor, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Async con `#[tokio::test]`](lab-01-async-con-tokio-test/README.md) | `#[tokio::test]`, `flavor = "multi_thread"`, `start_paused`, `timeout`, `tokio::join!` y `tokio::time::advance` | 55 min |
| [Lab 02 - Rutas de axum con `oneshot`](lab-02-rutas-axum-con-oneshot/README.md) | `oneshot` sobre un `Router`, el cuerpo como JSON con `json!`, el estado compartido y los `404`, `405`, `400`, `415` y `422` de axum | 60 min |
| [Lab 03 - Clientes de reqwest con wiremock](lab-03-clientes-reqwest-con-wiremock/README.md) | `MockServer`, `Mock::given`, `ResponseTemplate`, `expect`, `set_delay` y el plazo del cliente | 60 min |
| [Lab 04 - PostgreSQL con `#[sqlx::test]` y testcontainers](lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md) | `#[sqlx::test]` con migraciones y fixtures, la base de un test que falla, y PostgreSQL desde el test con testcontainers-rs | 65 min |

## Antes de empezar

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
docker compose version
# Docker Compose version v5.8.0
```

El lab 04 necesita Docker. Cada lab es un paquete independiente:

```bash
cd lab-NN-.../starter
cargo test
```

La primera vez, `cargo` descarga las dependencias de cada lab; las versiones exactas están en su `Cargo.lock`. tokio, axum, sqlx y reqwest tardan más en compilar que las dependencias de la semana 38: cuenta con hasta un minuto en el lab 04. `cargo clean` borra la carpeta `target/` de un lab cuando termines.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - PostgreSQL con `#[sqlx::test]` y testcontainers](../1-teoria/03-sqlx-test-y-testcontainers.md) | [Lab 01 - Async con `#[tokio::test]`](lab-01-async-con-tokio-test/README.md) → |

[↑ Semana 39](../README.md)
