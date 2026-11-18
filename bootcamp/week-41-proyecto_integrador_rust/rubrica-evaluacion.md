# Rúbrica de Evaluación - Semana 41

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Capas de una suite Rust, fases de `verify.sh`, filtros de nextest, las trampas de cargo-llvm-cov y la lectura de los tiempos y la cobertura |
| Desempeño | 40% | Labs 01 y 02 completos y presentación de la suite |
| Producto | 30% | Suite Rust completa del dominio con `verify.sh`, cobertura de líneas ≥85 % y reporte |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Distingue en Rust un test de integración (desde `tests/`) de un test que necesita base de datos, y explica cómo los separa `verify.sh`.
- Explica qué no ejecutan `cargo test` sin `--no-fail-fast` y `cargo nextest run`, y por qué los doctests van aparte.
- Explica por qué `cargo llvm-cov clean --workspace` va antes de `--no-report` y por qué el umbral necesita su propio mensaje.
- Lee los tiempos: compilación frente a ejecución, el coste fijo de cada `#[sqlx::test]` y el efecto del paralelismo de nextest.
- Compara `verify.sh` de Rust con el de Go ([semana 36](../week-36-proyecto_integrador_go/README.md)).

### Niveles

- **Alto (27-30):** explica cada punto con un dato de su propia suite.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde test de integración con test con base de datos, o no sabe de dónde sale la cifra de cobertura.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el Paso 0 reproducido, los tres PASOS de `verify.sh`, el umbral visto fallar, y los experimentos sin `clean` y con clippy sin `--all-targets`.
- Lab 02: los tiempos por capa, los tests por binario, los más lentos con su explicación, la cobertura por archivo con y sin base de datos, el reporte y el experimento de la compilación instrumentada.
- Presentación de 5 minutos con los datos del reporte: fases, cobertura y qué capa la sostiene, tiempos y el riesgo principal.

### Niveles

- **Alto (36-40):** los dos labs completos, cada experimento explicado y una presentación con datos propios.
- **Medio (28-35):** labs completos sin explicar algún experimento, o una presentación sin datos.
- **Bajo (<28):** labs incompletos o sin presentación.

---

## 3) Producto (30%)

### Criterios

- Los cinco requisitos de `./scripts/verificar-semana.sh 41` en verde.
- Cada test que necesita PostgreSQL está en un archivo que el filtro de `verify.sh` deja fuera de la fase sin base de datos, y ningún otro lo necesita.
- El umbral de `verify.sh` es 85 y nadie lo ha bajado.
- El reporte nombra riesgos concretos del dominio, comprobados o razonados sobre el código, no generalidades.

### Niveles

- **Alto (27-30):** verificación en verde, fases limpias y reporte con riesgos concretos.
- **Medio (21-26):** verificación en verde con algún test en la fase equivocada o un reporte genérico.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Integrador - Suite Rust completa del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 41](README.md)
