# Videografía - Semana 39

Los videos están en inglés y usan versiones anteriores de los crates. La API de `#[tokio::test]` y de testcontainers que muestran es la misma de los labs; en axum cambian detalles, como la sintaxis de las rutas (`/:code` en axum 0.7, `/{code}` en 0.8).

| Recurso | URL | Por qué verlo |
|---|---|---|
| How to Test Asynchronous Rust Programs with Tokio (cudidotdev) | https://www.youtube.com/watch?v=gCM2l3Z-yM8 | `#[tokio::test]` y los tests de código async, como en el lab 01. |
| Axum - API testing (Learning Rust) | https://www.youtube.com/watch?v=X96FuaSgpcs | Tests de una API de axum con reqwest contra un servidor arrancado: la alternativa a `oneshot`. Compárala con el lab 02. |
| Really simple API testing with axum-test! (MathiasCodes) | https://www.youtube.com/watch?v=xZ2ln27qCxE | El crate axum-test, que envuelve lo que hace el lab 02 a mano. Útil para saber que existe; los labs usan `oneshot` para ver qué pasa por debajo. |
| Integration testing with Test Containers using RUST (Semicolon) | https://www.youtube.com/watch?v=okTb1Qdp6X0 | testcontainers-rs con una dependencia real, como el PASO 4 del lab 04. |

## Dinámica sugerida

- Video 1 antes del lab 01.
- Videos 2 y 3 después del lab 02, para comparar enfoques.
- Video 4 antes del PASO 4 del lab 04.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — ebooks](../ebooks-free/README.md) | [Recursos — web](../webgrafia/README.md) → |

[↑ Semana 39](../../README.md)
