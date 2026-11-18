# Lab 04 - El cuello de botella

## Objetivo

Medir la línea base de la búsqueda con carga fija, subir la carga por escalones hasta romper la API, encontrar con datos qué la limita y comprobar que el arreglo funciona con las mismas dos pruebas. Por el camino, ver por qué agrandar el pool de conexiones no es el arreglo.

## Tiempo estimado

60 minutos. Cada stress test dura 2 minutos y se ejecuta tres veces.

## Preparación

La API de la biblioteca arrancada con los datos iniciales ([índice de labs](../README.md#la-api-de-la-biblioteca)): si en otro lab cambiaste algo, `docker compose down -v` y `docker compose up -d --wait`.

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `baseline.js` | k6: búsquedas con un umbral de p95 < 100 ms. El escenario de llegadas está comentado (PASO 1) |
| `stress.py` | Locust: el lector del lab 03 sin espera fija (`between(0.5, 1.5)`). La forma de la carga está comentada (PASO 1) |
| `resumen.py` | Convierte el CSV de historia de Locust en una tabla por escalón |
| `pyproject.toml`, `.python-version`, `uv.lock` | El proyecto de Locust, como en el lab 03 |

```bash
cd starter
uv sync
mkdir -p results
```

Necesitas tres terminales: la de la API (en `biblioteca-api/`), la de las pruebas (en `starter/`) y una para mirar la base de datos (en `biblioteca-api/`).

## Paso a paso

### Paso 1: la línea base (PASO 1 de `baseline.js`)

1. Ejecuta `baseline.js` tal como está:

   ```bash
   k6 run baseline.js; echo "exit=$?"
   # ...
   #               * default: 1 iterations for each of 1 VUs (maxDuration: 10m0s, gracefulStop: 30s)
   # ...
   #     ✓ 'p(95)<100' p(95)=39.24ms
   # ...
   #     http_reqs......................: 1      25.018568/s
   # exit=0
   ```

   Aprobada, con una sola petición. Un umbral sobre una petición no prueba nada: falta la carga.

2. Descomenta el bloque `// PASO 1`: un escenario `constant-arrival-rate` con 40 búsquedas por segundo durante 30 s ([teoría 02](../../1-teoria/02-k6.md#3-las-opciones-cuánta-carga-y-con-qué-forma)). Ejecuta y guarda el resultado:

   ```bash
   k6 run --summary-export results/before.json baseline.js; echo "exit=$?"
   # ...
   #      scenarios: (100.00%) 1 scenario, 200 max VUs, 1m0s max duration (incl. graceful stop):
   #               * search: 40.00 iterations/s for 30s (maxVUs: 20-200, gracefulStop: 30s)
   # ...
   #     http_req_duration
   #     ✓ 'p(95)<100' p(95)=63.56ms
   # ...
   #     http_req_duration..............: avg=53.83ms min=34.44ms med=47.83ms max=2.91s   p(90)=58.8ms  p(95)=63.56ms
   # ...
   #     iteration_duration.............: avg=49.31ms min=34.58ms med=48.08ms max=94.98ms p(90)=59.13ms p(95)=63.76ms
   #     iterations.....................: 1200   36.480476/s
   #     vus............................: 2      min=1         max=3
   # ...
   # exit=0
   ```

   1200 búsquedas en 30 s, con 1 a 3 VUs: a 40 por segundo, la API las atiende sin cola. Es el modelo abierto ([teoría 01](../../1-teoria/01-performance-testing.md#5-modelo-cerrado-y-modelo-abierto)): si la API se volviera lenta, k6 añadiría VUs para mantener el ritmo.

   `max=2.91s` frente a `iteration_duration max=94.98ms`: una petición no tarda más que su iteración. Es el reloj del equipo, no la API ([teoría 04](../../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#4-el-entorno-también-se-mide)).

   `results/before.json` es la línea base.

### Paso 2: el stress test (PASO 1 de `stress.py`)

1. Descomenta el bloque `# PASO 1` de `stress.py`: una `LoadTestShape` que añade 50 usuarios cada 20 s hasta 300 ([teoría 03](../../1-teoria/03-locust.md#5-la-forma-de-la-carga-loadtestshape)).
2. Ejecuta. Mientras dura, sigue con el Paso 3 en la tercera terminal:

   ```bash
   uv run locust -f stress.py --headless --only-summary --csv results/stress --html results/stress.html; echo "exit=$?"
   # ... INFO/locust.runners: Shape test updating to 50 users at 50.00 spawn rate
   # ... INFO/locust.runners: Shape test updating to 100 users at 50.00 spawn rate
   # ...
   # ... INFO/locust.runners: Shape test updating to 300 users at 50.00 spawn rate
   # ... INFO/locust.main: Shutting down (exit code 1)
   # Type     Name                  # reqs      # fails |    Avg     Min     Max    Med |   req/s  failures/s
   # GET      /api/books/{isbn}       2795     1(0.04%) |    846       0    2016    860 |   21.66        0.01
   # GET      /api/books?q=           8327   152(1.83%) |    890      35    2058    910 |   64.52        1.18
   #          Aggregated             11122   153(1.38%) |    879       0    2058    900 |   86.18        1.19
   # ...
   # Error report
   # # occurrences      Error
   # 152                GET /api/books?q=: HTTPError('503 Server Error: Service Unavailable for url: /api/books?q=')
   # 1                  GET /api/books/{isbn}: HTTPError('503 Server Error: Service Unavailable for url: /api/books/{isbn}')
   # exit=1
   ```

   La forma manda: no hacen falta `-u`, `-r` ni `-t`. El código de salida es 1 porque hubo errores. En un stress test es lo esperado: se busca dónde se rompe.

3. El resumen junta 2 minutos de cargas distintas. Para ver cada escalón:

   ```bash
   uv run python resumen.py results/stress_stats_history.csv
   # usuarios  peticiones/s  errores/s  p50 ms  p95 ms
   #       50          46.5        0.0      46     110
   #      100          64.3        0.0      57     190
   #      150         102.9        0.0     130     610
   #      200          95.3        0.0     380    1100
   #      250          97.2        0.0     570    1500
   #      300         105.2        6.6     900    1900
   ```

   Con una espera media de 1 s, cada usuario ofrece casi una petición por segundo. Hasta 150 usuarios, las peticiones por segundo suben con ellos; después se quedan en 95–105 mientras la latencia crece hasta 1.9 s, y a 300 usuarios llegan los 503: consultas que pasaron de 2 s. La **rodilla** está entre 100 y 150 usuarios ([teoría 04](../../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#3-leer-la-curva-la-rodilla)). Abre `results/stress.html`: las gráficas muestran lo mismo.

   El escalón de 100 usuarios dio 64 peticiones por segundo y no 100. Los escalones de 20 s son cortos y la mediana de un escalón varía de una ejecución a otra: en otra ejecución salieron 90.8. Lo que se repite en todas es la meseta y la latencia que crece.

### Paso 3: el diagnóstico

Con el stress test en 200 usuarios o más, en la tercera terminal (desde `biblioteca-api/`):

1. ¿Cuántas consultas tiene PostgreSQL en curso?

   ```bash
   docker compose exec db psql -U library -c "SELECT state, count(*) FROM pg_stat_activity WHERE datname = 'library' AND pid <> pg_backend_pid() GROUP BY state"
   #  state  | count
   # --------+-------
   #  active |     4
   # (1 row)
   ```

   `pg_stat_activity` tiene una fila por conexión. Hay 4, todas ejecutando una consulta: el pool de la API entero (`DB_MAX_CONNS`, 4 por defecto). `pid <> pg_backend_pid()` excluye la conexión de `psql`.

2. ¿Cuánta CPU usa?

   ```bash
   docker stats --no-stream biblioteca-api-db-1
   # CONTAINER ID   NAME                  CPU %     MEM USAGE / LIMIT     MEM %     NET I/O           BLOCK I/O        PIDS
   # f87907e63628   biblioteca-api-db-1   420.95%   69.16MiB / 15.62GiB   0.43%     3.98MB / 20.1MB   8.97MB / 123MB   13
   ```

   421 %: algo más de 4 núcleos. Una consulta por conexión, cada una ocupando un núcleo.

3. Cuando termine el stress test, ¿qué hace la búsqueda?

   ```bash
   docker compose exec db psql -U library -c "EXPLAIN ANALYZE SELECT isbn, title, author, year FROM books WHERE title ILIKE '%faro gris%' ORDER BY title LIMIT 20"
   # ...
   #          ->  Seq Scan on books  (cost=0.00..2401.00 rows=1010 width=61) (actual time=0.033..49.586 rows=535.00 loops=1)
   #                Filter: (title ~~* '%faro gris%'::text)
   #                Rows Removed by Filter: 99465
   # ...
   #  Execution Time: 49.965 ms
   ```

   `EXPLAIN ANALYZE` ejecuta la consulta y muestra el plan: `Seq Scan` lee las 100 000 filas y descarta 99 465 para quedarse con 535. Unos 50 ms con la base de datos tranquila.

4. Junta los tres datos con la ley de Little ([teoría 01](../../1-teoria/01-performance-testing.md#4-la-ley-de-little)): 4 conexiones, cada búsqueda ocupando una unos 45 ms, dan 4 / 0.045 = **89 búsquedas por segundo** como máximo. El stress test se estancó en 95–105 peticiones por segundo, de las que una de cada cuatro es una consulta por ISBN, que apenas ocupa la conexión. La estimación y la medición coinciden: el límite es la búsqueda, con 4 conexiones.

### Paso 4: más conexiones

¿Y si el pool tuviera 16 conexiones?

1. Para la API (`Ctrl+C`) y arráncala con 16:

   ```bash
   DB_MAX_CONNS=16 go run .
   # 2026/11/18 16:22:11 Biblioteca en http://localhost:18043 (pool de 16 conexiones)
   ```

2. Repite el stress test con otro nombre de resultados, y mira la CPU de PostgreSQL a partir de 200 usuarios:

   ```bash
   uv run locust -f stress.py --headless --only-summary --csv results/stress16
   # ...
   #          Aggregated             13843     0(0.00%) |    508       0    4424    430 |  104.52        0.00
   # ...
   uv run python resumen.py results/stress16_stats_history.csv
   # usuarios  peticiones/s  errores/s  p50 ms  p95 ms
   #       50          22.1        0.0      50     230
   #      100          81.2        0.0      54     210
   #      150         125.0        0.0      63     300
   #      200         108.0        0.0     120     570
   #      250         138.5        0.0     290     890
   #      300         107.5        0.0     430    1200
   ```

   ```bash
   docker stats --no-stream --format '{{.Name}} {{.CPUPerc}}' biblioteca-api-db-1
   # biblioteca-api-db-1 668.64%
   ```

   Un 26 % más de peticiones (104.5 frente a 86.2 de media) y sin 503, pero el p95 sigue pasando de 1 s. PostgreSQL usa ahora el 669 % de CPU: más conexiones ejecutan a la vez la misma consulta cara, hasta que la CPU se llena. El cuello de botella se ha movido del pool a la CPU de la base de datos. No ha desaparecido.

   El primer escalón (22 peticiones por segundo con 50 usuarios) es más bajo que en la ejecución anterior. Cuando dos ejecuciones no coinciden en un punto aislado, no saques conclusiones de ese punto: repite la prueba o mira la tendencia.

3. Para la API y arráncala otra vez sin `DB_MAX_CONNS` (pool de 4).

### Paso 5: el arreglo

La búsqueda recorre la tabla porque `ILIKE '%...%'` no puede usar un índice B-tree normal. La extensión `pg_trgm` de PostgreSQL crea índices de **trigramas** (cada grupo de tres letras del título), que sí sirven para buscar texto en cualquier posición.

1. Crea el índice:

   ```bash
   docker compose exec db psql -U library -c "CREATE EXTENSION pg_trgm" -c "CREATE INDEX books_title_trgm ON books USING gin (title gin_trgm_ops)"
   # CREATE EXTENSION
   # CREATE INDEX
   ```

2. Repite el `EXPLAIN ANALYZE` del Paso 3:

   ```bash
   # ...
   #          ->  Bitmap Heap Scan on books  (cost=82.58..1258.50 rows=1010 width=61) (actual time=2.010..3.897 rows=535.00 loops=1)
   #                Recheck Cond: (title ~~* '%faro gris%'::text)
   # ...
   #                ->  Bitmap Index Scan on books_title_trgm  (cost=0.00..82.33 rows=1010 width=0) (actual time=1.969..1.969 rows=535.00 loops=1)
   # ...
   #  Execution Time: 4.315 ms
   ```

   El índice encuentra las 535 filas sin leer las otras 99 465: de 50 ms a 4 ms.

3. La misma línea base, con la misma carga:

   ```bash
   k6 run --summary-export results/after.json baseline.js; echo "exit=$?"
   # ...
   #     ✓ 'p(95)<100' p(95)=5.22ms
   # ...
   # exit=0
   jq -c '.metrics.http_req_duration | {med, "p(95)"}' results/before.json results/after.json
   # {"med":47.8341245,"p(95)":63.56950285}
   # {"med":3.742102,"p(95)":5.226119}
   ```

   p95 de 63.6 ms a 5.2 ms, con las mismas 40 búsquedas por segundo. La comparación vale porque la carga es la misma en las dos.

4. El mismo stress test:

   ```bash
   uv run locust -f stress.py --headless --only-summary --csv results/stress-index
   # ...
   #          Aggregated             20945     0(0.00%) |     10       0    1047      4 |  158.31        0.00
   # ...
   uv run python resumen.py results/stress-index_stats_history.csv
   # usuarios  peticiones/s  errores/s  p50 ms  p95 ms
   #       50          38.1        0.0       4      13
   #      100          75.3        0.0       4       7
   #      150         130.6        0.0       4      78
   #      200         148.6        0.0       4      37
   #      250         241.3        0.0       4      10
   #      300         300.5        0.0       4       8
   ```

   Las peticiones por segundo siguen a los usuarios hasta el final (300 usuarios, 300 por segundo) con una mediana de 4 ms y ningún error. En este rango ya no hay rodilla: para encontrar el nuevo límite habría que seguir subiendo. A 200 usuarios, PostgreSQL usaba el 50 % de CPU, frente al 421 % sin índice.

## Experimentos

### Experimento 1: la línea base con VUs

1. En `baseline.js`, comenta el escenario y pon en su lugar `vus: 2, duration: '30s'` (modelo cerrado).
2. Ejecuta la línea base con el índice. Después quítalo y ejecútala otra vez:

   ```bash
   k6 run baseline.js
   # ...
   #     ✓ 'p(95)<100' p(95)=4.06ms
   # ...
   #     http_reqs......................: 19945  605.154081/s
   docker compose exec db psql -U library -c "DROP INDEX books_title_trgm"    # en biblioteca-api/
   k6 run baseline.js
   # ...
   #     ✓ 'p(95)<100' p(95)=55.25ms
   # ...
   #     http_reqs......................: 1366   41.41921/s
   ```

   Los dos mismos VUs generaron 605 búsquedas por segundo con el índice y 41 sin él: cada VU pide la siguiente en cuanto llega la respuesta. Las dos ejecuciones no recibieron la misma carga, así que comparar sus latencias mezcla dos experimentos. Por eso la línea base del Paso 1 usa `constant-arrival-rate`: 40 por segundo en las dos.

3. Deshaz el cambio en `baseline.js`.

## Comprobación final

1. Deja la base de datos como al principio, para el proyecto y para volver a hacer el lab:

   ```bash
   cd ../../biblioteca-api
   docker compose down -v
   ```

2. Comprueba que tienes en `results/` las dos líneas base y los tres stress tests:

   ```bash
   ls results/*.json results/*history.csv
   # results/after.json
   # results/before.json
   # results/stress-index_stats_history.csv
   # results/stress16_stats_history.csv
   # results/stress_stats_history.csv
   ```

## Preguntas de cierre

1. ¿Qué dos datos, junto con la ley de Little, predicen la capacidad de la búsqueda sin índice?
2. ¿Por qué 16 conexiones no arreglaron la latencia, aunque subieron las peticiones por segundo?
3. ¿Qué tendrías que cambiar en el stress test para encontrar el límite de la API con el índice?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Locust](../lab-03-locust/README.md) | [Proyecto — Proyecto - Rendimiento de la API del dominio con k6 y Locust](../../3-proyecto/README.md) → |

[↑ Semana 43](../../README.md)
