# Informe de rendimiento - TODO: tu dominio

TODO: sustituye cada TODO por tus datos. Los números salen de `perf/results/`, nunca de memoria.

## 1. Sistema y entorno

| | |
|---|---|
| API | TODO: la API Go de la semana 34 (`go/cmd/server`), commit `TODO` |
| Base de datos | PostgreSQL 18.7 (`compose.yaml`), base `TODO`, pool de TODO conexiones |
| Datos | TODO: cuántos elementos crea `perf/seed.sql` y en qué estado |
| Equipo | TODO: CPU, núcleos, memoria, sistema operativo |
| Generadores | k6 2.3.0 y Locust 2.47.0, en el mismo equipo que la API y la base de datos |

## 2. Criterios de aceptación

Escritos antes de ejecutar las pruebas.

| Petición | Métrica | Límite | Con qué carga | Umbral de k6 |
|---|---|---|---|---|
| TODO | TODO | TODO | TODO | TODO |

## 3. Prueba de carga (k6)

TODO: el perfil (escenarios, VUs, ritmo y duración), la tabla de resultados por petición, qué umbrales se cumplieron y cuáles no. Evidencia: `perf/results/load-summary.json` y `perf/results/load-report.html`.

## 4. Stress test (Locust)

TODO: los escalones, la tabla de `resumen.py`, y dónde está la rodilla o hasta dónde se probó sin encontrarla. Evidencia: `perf/results/stress_stats_history.csv` y `perf/results/stress.html`.

## 5. Cuello de botella

TODO: qué recurso limita, con los datos que lo demuestran (CPU de la API, de PostgreSQL y del generador; `pg_stat_activity`; la ley de Little). Si el límite es el generador, dilo.

## 6. Riesgos y siguientes pasos

TODO: lo que estas pruebas no cubren y lo que harías después.
