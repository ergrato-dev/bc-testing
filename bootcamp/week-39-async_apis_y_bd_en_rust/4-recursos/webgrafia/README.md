# Webgrafía - Semana 39

| Recurso | URL | Para qué |
|---|---|---|
| `#[tokio::test]` en docs.rs | https://docs.rs/tokio/1.54.1/tokio/attr.test.html | Todas las opciones del atributo: `flavor`, `worker_threads`, `start_paused`. |
| `tokio::time::advance` en docs.rs | https://docs.rs/tokio/1.54.1/tokio/time/fn.advance.html | Cómo mueve el reloj en pausa y cuándo entra en pánico. |
| axum en docs.rs | https://docs.rs/axum/0.8.9/axum/ | Router, extractores y qué rechazos da cada uno. |
| Ejemplo oficial de tests de axum | https://github.com/tokio-rs/axum/blob/main/examples/testing/src/main.rs | Los tests del propio proyecto axum con `oneshot`, incluido un servidor real para comparar. |
| wiremock en docs.rs | https://docs.rs/wiremock/0.6.5/wiremock/ | Los matchers, `ResponseTemplate` y cómo se comprueban las expectativas. |
| How to write a REST client in Rust (Luca Palmieri) | https://www.lpalmieri.com/posts/how-to-write-a-rest-client-in-rust-with-reqwest-and-wiremock/ | El autor de wiremock escribe un cliente con reqwest y lo prueba contra wiremock, como el lab 03. |
| `#[sqlx::test]` en docs.rs | https://docs.rs/sqlx/0.9.1/sqlx/attr.test.html | Migraciones, fixtures y la base de datos por test. |
| testcontainers-rs | https://rust.testcontainers.org/ | La documentación del crate: imágenes, puertos y ciclo de vida del contenedor. |
| Módulo `postgres` de testcontainers-modules | https://docs.rs/testcontainers-modules/0.15.0/testcontainers_modules/postgres/index.html | `Postgres::default()`, sus credenciales y su espera hasta que acepta conexiones. |

## Dinámica sugerida

- Ten abierta la página de `#[tokio::test]` durante el lab 01.
- Lee el ejemplo oficial de axum después del lab 02.
- La página de `#[sqlx::test]` durante el lab 04 y el paso 8 del proyecto.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 39](../../README.md)
