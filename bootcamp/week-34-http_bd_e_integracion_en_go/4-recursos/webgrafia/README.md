# Webgrafía - Semana 34

| Recurso | URL | Por qué leerlo |
|---|---|---|
| Package httptest | https://pkg.go.dev/net/http/httptest | `NewRequest`, `ResponseRecorder` (y por qué usar `Result()`), `NewServer`, `NewTLSServer` y `Server.Close`. |
| Routing Enhancements for Go 1.22 | https://go.dev/blog/routing-enhancements | Los patrones con método y comodines del `ServeMux`, el `405` automático y el ajuste `httpmuxgo121`, que devuelve el comportamiento anterior. |
| Package testing: Main | https://pkg.go.dev/testing | La sección *Main* explica `TestMain` y `m.Run`; la de `Short`, el flag `-short`. |
| Build constraints | https://pkg.go.dev/cmd/go | La sección *Build constraints*: la sintaxis de `//go:build` y cómo se combinan las tags. |
| Package embed | https://pkg.go.dev/embed | `//go:embed` con un `embed.FS`, como en `Migrate`. |
| Testcontainers for Go: Postgres | https://golang.testcontainers.org/modules/postgres/ | Todas las opciones del módulo, incluidos `Snapshot`, `Restore` y `WithSQLDriver`. |
| Testcontainers for Go: Garbage Collector | https://golang.testcontainers.org/features/garbage_collector/ | Cómo funciona Ryuk y cuándo desactivarlo. |
| Package pgx v5 | https://pkg.go.dev/github.com/jackc/pgx/v5 | `pgxpool`, `pgx.Tx`, `CollectRows` y `ErrNoRows`. |
| PostgreSQL Error Codes | https://www.postgresql.org/docs/current/errcodes-appendix.html | Los códigos `23505`, `23503`, `23514` y `25P02` que traducen los labs y el proyecto. |
| SAVEPOINT | https://www.postgresql.org/docs/current/sql-savepoint.html | Lo que crea `tx.Begin` sobre una transacción de pgx, y por qué deja seguir después de un error. |

## Dinámica sugerida

- *Routing Enhancements* con la teoría 01.
- *Package testing: Main* y *Build constraints* con la teoría 02.
- *PostgreSQL Error Codes* durante el paso 5 del proyecto, para elegir qué errores traducir.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 34](../../README.md)
