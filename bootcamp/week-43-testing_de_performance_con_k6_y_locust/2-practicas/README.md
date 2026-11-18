# Prácticas - Semana 43

Cuatro labs guiados (4 h en total) contra la misma API: la de una biblioteca con 100 000 libros, escrita en Go sobre PostgreSQL, en [biblioteca-api/](biblioteca-api/). Los scripts de k6 y de Locust están comentados por PASOS; lo que no es descomentar (cambiar una carga, provocar un fallo, crear un índice) está en el README de cada lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer test de carga con k6](lab-01-primer-test-de-carga-con-k6/README.md) | `k6 run`, VUs y duración, `check`, `sleep`, el resumen; un `check` que no hace fallar la prueba y un error que mejora la latencia | 55 min |
| [Lab 02 - Umbrales, etapas y etiquetas](lab-02-umbrales-etapas-y-etiquetas/README.md) | `thresholds` y el código de salida 99, `stages`, etiquetas `name` con un umbral por petición, `abortOnFail`, `--summary-export` y el informe HTML | 65 min |
| [Lab 03 - Locust](lab-03-locust/README.md) | `HttpUser`, `@task` con pesos, `wait_time`, `on_start`, `catch_response`, el modo `--headless`, el CSV y el informe HTML, y criterios de aceptación con `events.quitting` | 60 min |
| [Lab 04 - El cuello de botella](lab-04-cuello-de-botella/README.md) | Línea base con `constant-arrival-rate`, stress test con `LoadTestShape`, la rodilla de la curva, `pg_stat_activity` y `EXPLAIN ANALYZE`, el pool y el índice | 60 min |

## La API de la biblioteca

| Ruta | Qué hace |
|---|---|
| `GET /health` | 200 si la base de datos responde |
| `GET /api/books/{isbn}` | El libro, o 404. Los ISBN van de `9780000000001` a `9780000100000` |
| `GET /api/books?q=texto` | Hasta 20 libros cuyo título contiene el texto. Recorre la tabla entera: el título no tiene índice |
| `POST /api/loans` | Presta un libro. Cuerpo `{"isbn": "...", "member": "..."}`. 201; 404 si no existe; 409 si ya está prestado; 422 si falta un campo |
| `DELETE /api/loans/{isbn}` | Devuelve el libro: 204, o 404 si no estaba prestado |

Si una consulta a la base de datos tarda más de 2 s, la API responde 503. El pool de conexiones a PostgreSQL tiene 4 conexiones; la variable `DB_MAX_CONNS` lo cambia (lab 04).

Arráncala así, desde `biblioteca-api/`:

1. PostgreSQL, con Docker. La primera vez crea las tablas y los 100 000 libros (`db/init.sql`):

   ```bash
   cd biblioteca-api
   docker compose up -d --wait
   #  Container biblioteca-api-db-1 Healthy
   ```

2. La API, en una terminal propia. La primera vez, `go run` descarga el driver de PostgreSQL (`pgx`):

   ```bash
   go run .
   # 2026/11/18 15:48:04 Biblioteca en http://localhost:18043 (pool de 4 conexiones)
   ```

3. En otra terminal, compruébala:

   ```bash
   curl -s localhost:18043/health
   # ok
   ```

| Para | Comando |
|---|---|
| Parar la API | `Ctrl+C` en su terminal |
| Parar PostgreSQL y conservar los datos | `docker compose stop` |
| Volver a los datos iniciales (sin préstamos ni índices nuevos) | `docker compose down -v` y otra vez `docker compose up -d --wait` |

La API escucha en `localhost:18043` y PostgreSQL en `localhost:55443`, dos puertos poco habituales para no chocar con otros servicios. Si alguno está ocupado, cambia `PORT` (variable de entorno de la API) o el puerto de `compose.yaml`, y la constante `BASE_URL` de los scripts.

## Antes de empezar

```bash
go version
# go version go1.26.5 linux/amd64
uv --version
# uv 0.13.7 (x86_64-unknown-linux-gnu)
docker compose version
# Docker Compose version v5.8.0
```

k6 se instala en el lab 01 y Locust lo instala `uv sync` en el lab 03.

Una prueba de carga mide también el equipo donde se ejecuta. Mientras haces los labs, cierra lo que consuma CPU (otro proyecto con Docker, una compilación) y no compares números de equipos distintos: compara siempre dos ejecuciones en el mismo equipo ([teoría 04](../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#4-el-entorno-también-se-mide)).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Línea base, criterios y cuellos de botella](../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md) | [Lab 01 - Primer test de carga con k6](lab-01-primer-test-de-carga-con-k6/README.md) → |

[↑ Semana 43](../README.md)
