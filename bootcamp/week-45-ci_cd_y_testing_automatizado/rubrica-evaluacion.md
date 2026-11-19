# Rúbrica de Evaluación - Semana 45

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | CI y CD, anatomía de un workflow, versiones y cachés por lenguaje, PostgreSQL en la CI, matriz, artifacts, `paths`, `concurrency`, paralelismo, umbrales de cobertura, quality gates y hooks |
| Desempeño | 40% | Labs 01 a 04 completos, con cada run en rojo leído y explicado antes de ponerlo en verde |
| Producto | 30% | Cinco workflows en el repositorio del dominio, pre-commit y `ci.md` con los runs |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Distingue integración continua, entrega continua y despliegue continuo.
- Explica cada clave de un workflow (`on`, `permissions`, `jobs`, `runs-on`, `steps`, `uses`, `run`) y por qué cada job empieza en una máquina vacía.
- Explica de dónde sale la versión de cada lenguaje en la CI y cuándo se guarda una caché.
- Elige entre contenedor de servicio, `docker compose` y Testcontainers para PostgreSQL.
- Explica qué hacen `fail-fast`, `if: always()`, `paths`, `concurrency` y `timeout-minutes`, y los cuatro niveles de paralelismo.
- Explica por qué el umbral vive en el proyecto y el workflow solo ejecuta el comando.
- Explica por qué Sonar way pasa en el primer análisis, qué hace `sonar.qualitygate.wait` y por qué los hooks no sustituyen a la CI.

### Niveles

- **Alto (27-30):** explica cada concepto con un run o una salida de los labs.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** cree que un check rojo bloquea el merge por sí solo, o que un hook garantiza lo mismo que la CI.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el workflow de JavaScript en verde, el log leído con `gh`, el PR con el check en rojo y el motivo por el que el caso de 20 días no detecta el fallo.
- Lab 02: la matriz en rojo por el umbral, los artifacts descargados y abiertos, el experimento del resumen sin `--fail-under=0`, la caché que no se guarda en rojo, la cancelación por `concurrency` y el filtro de `paths`.
- Lab 03: los dos jobs en paralelo, el `connection refused` sin servicio, el contenedor de servicio con *health check* y el experimento del umbral al 90 %.
- Lab 04: la primera pasada de pre-commit con sus dos fallos de configuración, el commit que para `gofmt`, Husky y `--no-verify`, el choque con `core.hooksPath`, y SonarQube con Sonar way en verde, la puerta propia en rojo (código 3) y en verde.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada experimento ejecutado y explicado.
- **Medio (28-35):** los labs completos, con algún experimento sin explicar.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 45` pasa las 8 comprobaciones.
- Los cinco workflows están en verde en GitHub, y `ci.md` enlaza cada run.
- Un PR real muestra el umbral de cobertura poniendo el check en rojo.
- Los umbrales viven en el proyecto de cada lenguaje; ningún workflow repite un número.
- La primera pasada de pre-commit está explicada, y la suite se ejecutó después de los cambios automáticos.

### Niveles

- **Alto (27-30):** todo lo anterior, con las decisiones de `ci.md` justificadas con datos de los runs (duraciones, cachés, sorpresas).
- **Medio (21-26):** pasan las comprobaciones, con un `ci.md` que enlaza los runs sin explicarlos.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - CI de las cinco suites](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 45](README.md)
