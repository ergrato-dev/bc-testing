# Glosario - Semana 43

| Término | Definición |
|---|---|
| Prueba de rendimiento (*performance testing*) | Prueba que mide cómo responde el sistema con carga: rapidez, capacidad y estabilidad. |
| Microbenchmark | Medición de una función aislada, millones de veces (`b.Loop()`, criterion). No incluye la red ni la base de datos. |
| Smoke test | Prueba con 1 o 2 usuarios durante unos segundos para comprobar que el script y el sistema funcionan. |
| Load test (prueba de carga) | Prueba con la carga esperada que se aprueba o suspende frente a unos criterios. |
| Stress test | Prueba que sube la carga hasta pasar el límite, para saber dónde y cómo se rompe el sistema. |
| Spike test | Prueba con un salto brusco de carga y vuelta: ¿aguanta y se recupera? |
| Soak test | Prueba con carga normal durante horas, para encontrar degradaciones con el tiempo. |
| Volume test | Prueba con la base de datos llena, para ver si el volumen de datos cambia los tiempos. |
| Throughput (RPS) | Trabajo completado por unidad de tiempo; en una API, peticiones por segundo. |
| Latencia | Tiempo que espera una petición su respuesta. En k6, `http_req_duration`. |
| Percentil (p50, p95, p99) | Valor por debajo del cual queda ese porcentaje de las mediciones. El p50 es la mediana. |
| Tasa de errores | Proporción de respuestas de error. En k6, `http_req_failed`. |
| Ley de Little | Peticiones en curso = peticiones por segundo × latencia. Sirve para comprobar resultados y estimar capacidades. |
| VU (usuario virtual) | Cada usuario simulado por k6; repite la función del escenario mientras dura la prueba. |
| Iteración | Una ejecución de la función `default` (o la de un escenario) por un VU. |
| *Think time* | La espera de un usuario entre acciones (`sleep` en k6, `wait_time` en Locust). |
| Modelo cerrado | Carga definida por un número de usuarios que esperan cada respuesta. Si el sistema se vuelve lento, llegan menos peticiones. |
| Modelo abierto | Carga definida por un ritmo de llegadas que no depende de las respuestas. |
| Omisión coordinada | Error de medición del modelo cerrado: el generador deja de enviar justo las peticiones que encontrarían el sistema saturado. |
| Escenario | En k6, una carga con su *executor*, su función y sus opciones. Varios escenarios se ejecutan a la vez. |
| *Executor* | Lo que decide cómo se reparte la carga de un escenario: `ramping-vus`, `constant-arrival-rate`... |
| `stages` | Tramos de una prueba de k6, cada uno con su duración y su objetivo de VUs. |
| `check` | Comprobación de k6 sobre una respuesta. Se cuenta, pero no hace fallar la prueba. |
| Umbral (*threshold*) | Criterio de k6 sobre una métrica. Si se incumple, k6 termina con código 99. |
| Etiqueta (*tag*) | Dato que acompaña a cada medición en k6 (`name`, `status`, `method`). Permite umbrales por petición. |
| `dropped_iterations` | Iteraciones que un escenario de ritmo fijo no pudo iniciar por falta de VUs. |
| `HttpUser` | Clase base de un usuario de Locust con un cliente HTTP (`self.client`). |
| `FastHttpUser` | Variante de `HttpUser` con un cliente más rápido (`geventhttpclient`): el generador gasta menos por petición. |
| `@task` | Decorador de Locust que marca una tarea; el número es su peso. |
| `catch_response` | Opción de una petición de Locust para decidir con `success()` o `failure()` si es un fallo. |
| `LoadTestShape` | Clase de Locust que da forma a la carga: su `tick()` devuelve usuarios y ritmo de arranque cada segundo. |
| Criterio de aceptación de rendimiento | Qué petición, qué métrica, qué límite y con qué carga. Se escribe antes de medir. |
| Línea base (*baseline*) | Medición de referencia, con carga fija, guardada para comparar con las siguientes. |
| Rodilla | Punto de un stress test donde las peticiones por segundo dejan de subir y la latencia empieza a crecer. |
| Capacidad | Carga máxima que el sistema atiende cumpliendo los criterios de aceptación. |
| Cuello de botella | El recurso que se llena primero y limita la capacidad: CPU, conexiones, una consulta, el generador. |
| Pool de conexiones | Conjunto fijo de conexiones a la base de datos que comparten las peticiones de la API. |
| `pg_stat_activity` | Vista de PostgreSQL con una fila por conexión y lo que está haciendo. |
| `EXPLAIN ANALYZE` | Ejecuta una consulta y muestra su plan con los tiempos reales. |
| Índice de trigramas | Índice GIN de `pg_trgm` que permite buscar texto en cualquier posición (`ILIKE '%...%'`). |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 43](../README.md)
