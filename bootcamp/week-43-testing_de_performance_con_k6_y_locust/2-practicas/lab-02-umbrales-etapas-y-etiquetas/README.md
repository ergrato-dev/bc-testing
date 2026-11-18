# Lab 02 - Umbrales, etapas y etiquetas

## Objetivo

Convertir el script del lab 01 en una prueba de carga que aprueba o suspende: criterios de aceptación como umbrales, un perfil de carga con subida, meseta y bajada, y un umbral por cada petición. Después, ver cómo el percentil global esconde una petición lenta, detener la prueba en cuanto un criterio falla y guardar los resultados en JSON y en un informe HTML.

## Tiempo estimado

65 minutos.

## Preparación

La API de la biblioteca arrancada, con los datos iniciales ([índice de labs](../README.md#la-api-de-la-biblioteca)). Si en otro lab creaste un índice, vuelve a los datos iniciales con `docker compose down -v` y `docker compose up -d --wait`.

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `load.js` | Un lector que consulta un libro al azar, espera 1 s, busca un título y espera otro segundo. Cada petición lleva la etiqueta `name`. Los umbrales y las etapas están comentados por PASOS |

## Paso a paso

### Paso 1: la prueba sin criterios

```bash
cd starter
k6 run --vus 30 --duration 30s load.js; echo "exit=$?"
# ...
#      scenarios: (100.00%) 1 scenario, 30 max VUs, 1m0s max duration (incl. graceful stop):
#               * default: 30 looping VUs for 30s (gracefulStop: 30s)
# ...
#     http_req_duration..............: avg=37.59ms min=403.38µs med=28.61ms max=384.3ms p(90)=79.43ms p(95)=90.78ms
# ...
#     http_req_failed................: 0.00%  0 out of 900
#     http_reqs......................: 900    26.193776/s
# ...
# exit=0
```

Con 30 lectores, p95 de 91 ms y ningún error. ¿Es bueno? Sin criterios escritos antes, la respuesta depende de quién mire.

### Paso 2: los criterios de aceptación (PASO 1)

1. En `load.js`, descomenta el bloque `// PASO 1` dentro de `options`. Deja comentado el `// PASO 3` que hay dentro: es otro nivel.
2. Ejecuta lo mismo:

   ```bash
   k6 run --vus 30 --duration 30s load.js; echo "exit=$?"
   # ...
   #   █ THRESHOLDS
   #
   #     checks
   #     ✓ 'rate>0.99' rate=100.00%
   #
   #     http_req_duration
   #     ✓ 'p(95)<300' p(95)=95.8ms
   #
   #     http_req_failed
   #     ✓ 'rate<0.01' rate=0.00%
   # ...
   # exit=0
   ```

| Umbral | Criterio |
|---|---|
| `http_req_failed: ['rate<0.01']` | Menos del 1 % de errores |
| `http_req_duration: ['p(95)<300']` | El 95 % de las peticiones, por debajo de 300 ms |
| `checks: ['rate>0.99']` | Más del 99 % de las comprobaciones cumplidas. Así, un `check` que falla sí hace fallar la prueba ([lab 01, experimento 1](../lab-01-primer-test-de-carga-con-k6/README.md#experimento-1-un-check-que-falla)) |

El resumen empieza ahora por `THRESHOLDS`, con ✓ o ✗ y el valor medido ([teoría 02](../../1-teoria/02-k6.md#5-umbrales-los-criterios-de-aceptación)).

### Paso 3: el perfil de carga (PASO 2)

1. Descomenta el bloque `// PASO 2`: tres `stages`.
2. Ejecuta sin `--vus` ni `--duration`, que tendrían prioridad sobre las etapas:

   ```bash
   k6 run load.js; echo "exit=$?"
   # ...
   #      scenarios: (100.00%) 1 scenario, 30 max VUs, 1m20s max duration (incl. graceful stop):
   #               * default: Up to 30 looping VUs for 50s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)
   # ...
   #     http_req_duration
   #     ✓ 'p(95)<300' p(95)=67.21ms
   # ...
   #     vus............................: 2      min=2         max=30
   # exit=0
   ```

   La carga sube de 0 a 30 VUs en 10 s, se queda en 30 durante 30 s y baja a 0 en otros 10: una prueba de carga ([teoría 01](../../1-teoria/01-performance-testing.md#2-tipos-de-prueba)). Al subir poco a poco, la API no recibe 30 usuarios en el mismo milisegundo.

### Paso 4: el mismo umbral para todo (todavía)

El script hace dos peticiones muy distintas: la consulta por ISBN usa la clave primaria; la búsqueda recorre la tabla. Las dos van a la misma métrica, `http_req_duration`, y el p95 de 67 ms es de la mezcla. Antes de separarlas, una pregunta: ¿qué p95 tiene la búsqueda?

El resumen no lo dice. k6 solo muestra las submétricas que usa algún umbral.

### Paso 5: un umbral por petición (PASO 3)

1. Descomenta el bloque `// PASO 3`, dentro del de umbrales.
2. Ejecuta:

   ```bash
   k6 run load.js; echo "exit=$?"
   # ...
   #     http_req_duration
   #     ✓ 'p(95)<300' p(95)=67.1ms
   #
   #       {name:book}
   #       ✓ 'p(95)<50' p(95)=6.83ms
   #
   #       {name:search}
   #       ✓ 'p(95)<100' p(95)=77.19ms
   # ...
   #     http_req_duration..............: avg=31.88ms min=341.82µs med=36.12ms max=3.05s   p(90)=58.68ms p(95)=67.1ms
   #       { name:book }................: avg=2.02ms  min=341.82µs med=1.04ms  max=60.45ms p(90)=1.63ms  p(95)=6.83ms
   #       { name:search }..............: avg=61.75ms min=36.08ms  med=49.08ms max=3.05s   p(90)=67.1ms  p(95)=77.19ms
   # ...
   # exit=0
   ```

   `{ tags: { name: 'search' } }` en la petición y `'http_req_duration{name:search}'` en el umbral ([teoría 02](../../1-teoria/02-k6.md#6-etiquetas-una-métrica-por-petición)). La consulta tiene p95 de 7 ms y la búsqueda de 77 ms: la media de los dos era un número de nadie.

   El `max=3.05s` de la búsqueda no es de la API: ninguna iteración pasó de 2.11 s (`iteration_duration`), y una petición no puede durar más que la iteración que la contiene. Es un salto del reloj del equipo ([teoría 04](../../1-teoria/04-linea-base-criterios-y-cuellos-de-botella.md#4-el-entorno-también-se-mide)). Si no te aparece, mejor: tu reloj no salta.

3. Ahora con 150 lectores, sin tocar el script. `--stage` sustituye las etapas desde la línea de comandos:

   ```bash
   k6 run --stage 10s:150,30s:150,10s:0 load.js; echo "exit=$?"
   # ...
   #     http_req_duration
   #     ✓ 'p(95)<300' p(95)=143.87ms
   #
   #       {name:book}
   #       ✗ 'p(95)<50' p(95)=109.21ms
   #
   #       {name:search}
   #       ✗ 'p(95)<100' p(95)=164.92ms
   # ...
   #     http_reqs......................: 5820   101.949739/s
   # ...
   # time="2026-11-18T16:34:22-05:00" level=error msg="thresholds on metrics 'http_req_duration{name:book}, http_req_duration{name:search}' have been crossed"
   # exit=99
   ```

   Tres cosas:

   - El umbral global **pasa** (144 ms < 300 ms) y los dos por petición **fallan**. Sin umbrales por petición, esta prueba saldría verde.
   - La consulta por ISBN también falla: de 7 ms a 109 ms. Es la misma consulta por clave primaria. Lo que ha cambiado es que espera una conexión libre mientras las búsquedas ocupan las cuatro del pool. En el lab 04 lo confirmas.
   - Código de salida **99**: un umbral incumplido. Es el que hace fallar un pipeline.

### Paso 6: guardar los resultados

Con los tres PASOS descomentados:

```bash
mkdir -p results
K6_WEB_DASHBOARD=true K6_WEB_DASHBOARD_EXPORT=results/report.html \
  k6 run --summary-export results/summary.json load.js
```

| Archivo | Qué es |
|---|---|
| `results/summary.json` | El resumen en JSON, para compararlo con otra ejecución o leerlo desde un script |
| `results/report.html` | El informe: gráficas de peticiones por segundo, latencias y VUs a lo largo de la prueba. Ábrelo en el navegador (en WSL, `explorer.exe results/report.html`) |

Lee un número del JSON con `jq`:

```bash
jq '.metrics["http_req_duration{name:search}"]["p(95)"]' results/summary.json
# 77.819237
jq -c '.metrics["http_req_duration{name:search}"]' results/summary.json
# {"med":51.236133,"max":171.945032,"p(90)":64.559345,"p(95)":77.819237,"avg":54.10640454076536,"min":36.057706,"thresholds":{"p(95)<100":false}}
```

`"p(95)<100": false` significa que el umbral **no** se incumplió ([teoría 02](../../1-teoria/02-k6.md#7-guardar-los-resultados)).

En el informe HTML, busca el momento en que la carga llega a 30 VUs: las peticiones por segundo suben y la latencia no cambia. Es una prueba sin rodilla.

## Experimentos

### Experimento 1: parar en cuanto falla

Una prueba de 10 minutos que incumple su criterio en el primero no necesita los otros nueve.

1. En el umbral de la búsqueda, cambia `['p(95)<100']` por:

   ```js
   [{ threshold: 'p(95)<100', abortOnFail: true, delayAbortEval: '10s' }],
   ```

2. Ejecuta con 200 lectores:

   ```bash
   k6 run --stage 10s:200,30s:200,10s:0 load.js; echo "exit=$?"
   # ...
   # running (0m12.0s), 000/200 VUs, 530 complete and 200 interrupted iterations
   # default ✗ [  24% ] 158/200 VUs  12.0s/50.0s
   # time="2026-11-18T16:13:01-05:00" level=error msg="thresholds on metrics 'http_req_duration, http_req_duration{name:book}, http_req_duration{name:search}' were crossed; at least one has abortOnFail enabled, stopping test prematurely"
   # exit=99
   ```

   La prueba se detuvo a los 12 s de 50. `delayAbortEval: '10s'` evita que la corten las primeras peticiones, que abren conexiones.

3. Deshaz el cambio.

### Experimento 2: sin la etiqueta `name`

1. Quita `{ tags: { name: 'book' } }` de la consulta por ISBN.
2. Ejecuta:

   ```bash
   k6 run load.js; echo "exit=$?"
   # ...
   #       {name:book}
   #       ✓ 'p(95)<50' p(95)=0s
   # ...
   # exit=0
   ```

   Ninguna petición tiene ya la etiqueta `name:book`: cada consulta lleva como `name` su propia URL, con su ISBN. El umbral se evalúa sobre una submétrica vacía, vale `0s` y **se cumple**. Un umbral con una etiqueta mal escrita (`name:books`) pasa siempre, sin avisar. Cuando escribas un umbral por etiqueta, comprueba en el resumen que su valor no es `0s`.

3. Deshaz el cambio.

## Comprobación final

Con los tres PASOS descomentados:

```bash
k6 run load.js; echo "exit=$?"
# ...
# exit=0
k6 run --stage 10s:150,30s:150,10s:0 load.js; echo "exit=$?"
# ...
# exit=99
```

## Preguntas de cierre

1. Con 150 lectores, el umbral global pasaba y los dos por petición fallaban. ¿Qué umbrales pondrías en un proyecto real, y por qué?
2. ¿Por qué empeoró la consulta por ISBN si su consulta SQL no cambió?
3. ¿Qué diferencia hay entre un `check` que falla y un umbral que falla?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer test de carga con k6](../lab-01-primer-test-de-carga-con-k6/README.md) | [Lab 03 - Locust](../lab-03-locust/README.md) → |

[↑ Semana 43](../../README.md)
