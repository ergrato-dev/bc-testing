# Lab 01 - Primer test de carga con k6

## Objetivo

Instalar k6, escribir un script que consulta y busca libros en la API de la biblioteca, y leer el resumen que imprime al terminar. Después, ver tres cosas que el resumen no grita: un `check` que falla no hace fallar la prueba, el tiempo de espera entre peticiones decide cuánta carga generas, y un error rápido mejora las latencias.

## Tiempo estimado

55 minutos.

## Preparación

1. Arranca la API de la biblioteca ([índice de labs](../README.md#la-api-de-la-biblioteca)) y comprueba que responde:

   ```bash
   curl -s localhost:18043/api/books/9780000000001
   # {"isbn":"9780000000001","title":"La casa azul de Quito 1","author":"Luis Rojas","year":1951}
   ```

2. Instala k6 ([teoría 02](../../1-teoria/02-k6.md#1-instalar-k6)) y comprueba la versión:

   ```bash
   k6 version
   # k6 v2.3.0 (commit/e088784614, go1.26.8, linux/amd64)
   ```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `script.js` | El script de k6, con tres bloques comentados por PASOS |

## Paso a paso

### Paso 1: un script vacío

```bash
cd starter
k6 run script.js
# ...
#      scenarios: (100.00%) 1 scenario, 1 max VUs, 10m30s max duration (incl. graceful stop):
#               * default: 1 iterations for each of 1 VUs (maxDuration: 10m0s, gracefulStop: 30s)
#
#   █ TOTAL RESULTS
#
#     EXECUTION
#     iteration_duration...: avg=4.4µs min=4.4µs med=4.4µs max=4.4µs p(90)=4.4µs p(95)=4.4µs
#     iterations...........: 1   6389.245622/s
# ...
```

Sin opciones, k6 ejecuta **una iteración** de la función `default` con **un VU** (usuario virtual). La función está vacía: la iteración tardó 4.4 µs y no hubo ninguna petición, así que no aparece ninguna métrica `http_*`.

### Paso 2: una petición con sus comprobaciones (PASO 1)

1. En `script.js`, descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   k6 run script.js
   # ...
   #   █ TOTAL RESULTS
   #
   #     checks_total.......: 2       150.985957/s
   #     checks_succeeded...: 100.00% 2 out of 2
   #     checks_failed......: 0.00%   0 out of 2
   #
   #     ✓ status is 200
   #     ✓ is the requested book
   #
   #     HTTP
   #     http_req_duration..............: avg=12.13ms min=12.13ms med=12.13ms max=12.13ms p(90)=12.13ms p(95)=12.13ms
   #       { expected_response:true }...: avg=12.13ms min=12.13ms med=12.13ms max=12.13ms p(90)=12.13ms p(95)=12.13ms
   #     http_req_failed................: 0.00% 0 out of 1
   #     http_reqs......................: 1     75.492979/s
   # ...
   ```

| Línea | Qué dice |
|---|---|
| `checks_succeeded` | Las comprobaciones de `check()` que se cumplieron |
| `http_req_duration` | Desde que se envía la petición hasta que llega la respuesta completa ([teoría 02](../../1-teoria/02-k6.md#4-el-resumen)) |
| `{ expected_response:true }` | La misma métrica, solo con las respuestas esperadas (por defecto, 200–399) |
| `http_req_failed` | El porcentaje de respuestas inesperadas: códigos 4xx, 5xx o sin respuesta |
| `http_reqs` | Peticiones totales y por segundo |

Una petición no es una prueba de carga: los 12 ms incluyen abrir la conexión.

### Paso 3: diez usuarios a la vez

```bash
k6 run --vus 10 --duration 30s script.js
# ...
#     checks_succeeded...: 100.00% 515802 out of 515802
# ...
#     http_req_duration..............: avg=1.09ms min=239.81µs med=1.02ms max=24.78ms p(90)=1.45ms p(95)=1.72ms
# ...
#     http_reqs......................: 257901 8596.244224/s
# ...
#     vus............................: 10     min=10          max=10
```

Diez VUs repiten la función sin parar durante 30 s: 8596 peticiones por segundo. Cada VU lanza la siguiente petición en cuanto llega la respuesta, así que la carga la decide la velocidad de la API, no tú. Ningún lector de la biblioteca consulta 860 libros por segundo.

### Paso 4: el tiempo de lectura (PASO 2)

1. Descomenta el bloque `// PASO 2`: `sleep(1)`, un segundo sin hacer nada después de cada consulta.
2. Ejecuta lo mismo:

   ```bash
   k6 run --vus 10 --duration 30s script.js
   # ...
   #     http_req_duration..............: avg=1.81ms min=446.41µs med=1.84ms max=3.24ms p(90)=2.57ms p(95)=2.69ms
   #     http_reqs......................: 300   8.929048/s
   #     iteration_duration.............: avg=1s     min=1s       med=1s     max=1s     p(90)=1s     p(95)=1s
   #     iterations.....................: 300   8.929048/s
   ```

   De 257 901 peticiones a 300, con los mismos 10 VUs. Cada iteración dura ahora 1 s (1.8 ms de petición + 1 s de espera), así que cada VU hace una petición por segundo: 10 VUs × 30 s = 300. Es la fórmula del **modelo cerrado** ([teoría 01](../../1-teoria/01-performance-testing.md#5-modelo-cerrado-y-modelo-abierto)): peticiones por segundo = VUs / (latencia + espera).

   La tasa sale 8.9/s y no 10/s porque k6 la divide entre el tiempo total de la ejecución, que incluye el arranque y la parada.

### Paso 5: una búsqueda (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta:

   ```bash
   k6 run --vus 10 --duration 30s script.js
   # ...
   #     ✓ status is 200
   #     ✓ is the requested book
   #     ✓ search is 200
   #     ✓ search finds 20 books
   #
   #     HTTP
   #     http_req_duration..............: avg=55.07ms min=451.2µs med=54.2ms max=175.76ms p(90)=111.23ms p(95)=118.89ms
   # ...
   #     http_reqs......................: 548    16.109128/s
   # ...
   ```

   `http_req_duration` mezcla ahora dos peticiones muy distintas: la consulta por ISBN (alrededor de 2 ms) y la búsqueda (alrededor de 110 ms). La media de 55 ms no describe ninguna de las dos. En el lab 02 las separas con etiquetas.

   `encodeURIComponent('faro gris')` escribe el espacio como `%20`: una URL no puede llevar espacios.

## Experimentos

### Experimento 1: un `check` que falla

1. En el PASO 1, cambia el ISBN de la URL por `9780000999999`, que no existe. Deja el del `check` como está.
2. Ejecuta y mira el código de salida:

   ```bash
   k6 run --vus 10 --duration 5s script.js; echo "exit=$?"
   # ...
   #     checks_succeeded...: 50.00% 100 out of 200
   #     checks_failed......: 50.00% 100 out of 200
   #
   #     ✗ status is 200
   #       ↳  0% — ✓ 0 / ✗ 50
   #     ✗ is the requested book
   #       ↳  0% — ✓ 0 / ✗ 50
   #     ✓ search is 200
   #     ✓ search finds 20 books
   # ...
   #     http_req_failed................: 50.00% 50 out of 100
   # ...
   # exit=0
   ```

   Las dos comprobaciones de la consulta fallan en todas las iteraciones (la API responde 404), y k6 termina con `exit=0`. Un `check` cuenta, pero no decide: en integración continua esta ejecución saldría verde. Lo que hace fallar una prueba de k6 son los **umbrales**, en el lab 02.

3. Deshaz el cambio.

### Experimento 2: los errores son rápidos

1. En el PASO 3, quita `encodeURIComponent`: deja la URL como `` `${BASE_URL}/api/books?q=faro gris` ``.
2. Ejecuta:

   ```bash
   k6 run --vus 10 --duration 10s script.js; echo "exit=$?"
   # ...
   #     ✗ search is 200
   #       ↳  0% — ✓ 0 / ✗ 100
   #     ✗ search finds 20 books
   #       ↳  0% — ✓ 0 / ✗ 100
   #
   #     HTTP
   #     http_req_duration..............: avg=1.4ms  min=97.28µs  med=1.3ms  max=3.57ms p(90)=2.68ms p(95)=2.96ms
   #       { expected_response:true }...: avg=2.11ms min=687.39µs med=2.11ms max=3.57ms p(90)=2.97ms p(95)=3.29ms
   #     http_req_failed................: 50.00% 100 out of 200
   # ...
   # exit=0
   ```

   El servidor rechaza la URL con el espacio (400 Bad Request) sin llegar a buscar. La latencia baja de 55 ms a 1.4 ms: la API parece 40 veces más rápida y la mitad de las peticiones son errores. Por eso una latencia nunca se lee sin la tasa de errores al lado, y por eso existe `{ expected_response:true }`, que deja fuera los errores.

3. Deshaz el cambio.

## Comprobación final

Con los tres PASOS descomentados:

```bash
k6 run --vus 10 --duration 30s script.js; echo "exit=$?"
# ...
#     checks_succeeded...: 100.00% 1096 out of 1096
# ...
#     http_req_failed................: 0.00%  0 out of 548
# ...
# exit=0
```

## Preguntas de cierre

1. ¿Por qué 10 VUs generaron 8596 peticiones por segundo en el Paso 3 y 10 en el Paso 4? ¿Cuál de las dos cargas se parece a la de 10 lectores reales?
2. ¿Qué tendría que pasar para que el Experimento 1 hiciera fallar un pipeline de integración continua?
3. En el Experimento 2, la latencia mejoró. ¿Qué número del resumen te avisa de que no es una buena noticia?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Umbrales, etapas y etiquetas](../lab-02-umbrales-etapas-y-etiquetas/README.md) → |

[↑ Semana 43](../../README.md)
