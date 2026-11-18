# Rúbrica de Evaluación - Semana 43

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Tipos de prueba, métricas y percentiles, ley de Little, modelo cerrado y abierto, umbrales, línea base, la rodilla y el cuello de botella, microbenchmark frente a prueba de carga |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | `perf/` en el repositorio: prueba de carga con k6, stress test con Locust, resultados guardados e informe de rendimiento |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Distingue smoke, load, stress, spike, soak y volume por la pregunta que responden, y un microbenchmark de una prueba de carga.
- Explica por qué se usan percentiles y no la media ni el máximo, y por qué la latencia se lee junto a la tasa de errores.
- Usa la ley de Little para comprobar unos resultados o estimar una capacidad.
- Distingue el modelo cerrado del abierto y explica la omisión coordinada.
- Explica qué hace fallar una prueba de k6 (umbrales, código 99) y qué no (`check`), y cómo se consigue lo mismo en Locust.
- Explica cómo se compara contra una línea base, qué es la rodilla y cómo se confirma un cuello de botella con datos.

### Niveles

- **Alto (27-30):** explica cada concepto con un número de los labs.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde la media con un percentil o no distingue un `check` de un umbral.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los tres PASOS, el `check` que falla con código 0 y la URL sin codificar que «mejora» la latencia.
- Lab 02: umbrales, etapas y umbrales por petición; la ejecución con 150 VUs (global aprobado, peticiones suspendidas, código 99); `abortOnFail`; el umbral sin etiqueta que vale `0s`; JSON e informe HTML.
- Lab 03: lectores y socios con sus pesos, `catch_response`, los criterios en `events.quitting`, el CSV y el informe; el 409 que es y no es un fallo.
- Lab 04: línea base con `constant-arrival-rate`, stress test con `LoadTestShape`, el diagnóstico (`pg_stat_activity`, `docker stats`, `EXPLAIN ANALYZE`, ley de Little), el pool de 16 y el índice comparados con las mismas pruebas.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada experimento ejecutado y explicado.
- **Medio (28-35):** los labs completos, con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 43` pasa las 8 comprobaciones.
- Los criterios de aceptación están escritos antes de medir: el commit del informe con los criterios es anterior al de los resultados.
- La prueba de carga lee y escribe, con un umbral por petición, y las escrituras no chocan entre sí.
- El stress test sube hasta encontrar la rodilla, o hasta demostrar con la CPU que el límite es el generador.
- El informe explica los resultados con los números de `perf/results/` y nombra lo que las pruebas no cubren.

### Niveles

- **Alto (27-30):** todo lo anterior, con un cuello de botella (o un límite del generador) demostrado con datos.
- **Medio (21-26):** pasan las comprobaciones, con un informe que describe los números pero no los explica.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Rendimiento de la API del dominio con k6 y Locust](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 43](README.md)
