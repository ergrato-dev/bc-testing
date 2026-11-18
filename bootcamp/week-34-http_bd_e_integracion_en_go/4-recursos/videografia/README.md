# Videografía - Semana 34

Los videos están en inglés y usan versiones anteriores de Go y de las librerías. Dos diferencias con lo que ves en los labs: si un video registra las rutas con `gorilla/mux` o comprueba el método con un `if r.Method != ...`, desde Go 1.22 el `http.ServeMux` lo hace con patrones como `"GET /books/{isbn}"` ([teoría 01](../../1-teoria/01-httptest-handlers-y-clientes.md#3-el-router-estándar-con-métodos)); y si arranca PostgreSQL con `testcontainers.GenericContainer`, el módulo `postgres` con `postgres.Run` hace lo mismo en menos líneas ([teoría 03](../../1-teoria/03-testcontainers-go-y-postgresql.md#1-el-módulo-postgres)).

| Recurso | URL | Por qué verlo |
|---|---|---|
| justforfunc #16: unit testing HTTP servers | https://www.youtube.com/watch?v=hVFEV-ieeew | De un handler a su test con `NewRecorder`, y del handler al servidor completo con `NewServer`: los labs 01 y 02 en un video. |
| Go (Golang) httptest Tutorial | https://www.youtube.com/watch?v=LqU-0RVyq8I | `httptest` desde los dos lados, servidor y cliente. |
| Getting Started with Testcontainers for Go | https://www.youtube.com/watch?v=PbV31aE-asU | Del equipo de Testcontainers: un PostgreSQL arrancado desde el test y el ciclo de vida del contenedor. |
| Writing Database Tests with PostgreSQL Test Containers in Go | https://www.youtube.com/watch?v=QdoBZfm24hA | Un repositorio probado contra PostgreSQL con testcontainers-go, como el lab 04. |

## Dinámica sugerida

- Videos 1 y 2 antes de los labs 01 y 02.
- Videos 3 y 4 antes del lab 04.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — ebooks](../ebooks-free/README.md) | [Recursos — web](../webgrafia/README.md) → |

[↑ Semana 34](../../README.md)
