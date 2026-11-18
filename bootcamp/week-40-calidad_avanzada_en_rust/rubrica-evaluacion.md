# Rúbrica de Evaluación - Semana 40

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Propiedades y estrategias, reducción, fuzzing guiado por cobertura, benchmarks y líneas base, cobertura frente a mutantes, nextest y clippy |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | La suite Rust del dominio con propiedades, una de ellas contra PostgreSQL, un benchmark comparado, la cobertura, los mutantes analizados y `calidad.md` |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué es una propiedad, cómo una estrategia demasiado estrecha esconde fallos y para qué sirve `proptest-regressions/`.
- Explica por qué la reducción de proptest puede quedarse en un mínimo local.
- Explica qué hace un fuzzer guiado por cobertura, por qué cargo-fuzz necesita nightly y cómo se convierte un fallo en un test de regresión.
- Lee la salida de criterion: el intervalo, el cambio, `p` y el umbral de ruido.
- Explica por qué una línea ejecutada puede no estar comprobada, qué es un mutante equivalente y qué no cuenta cargo-llvm-cov ni ejecuta nextest.
- Compara cada herramienta con su equivalente en Go (semana 35) y en Python (semanas 20 y 21).

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde cobertura con calidad de los tests, o no distingue una mejora de ruido en un benchmark.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: las propiedades de los cuatro PASOS, el arreglo de `split_fee`, y los experimentos de la estrategia ancha, la reducción desactivada y `assert!` en lugar de `prop_assert!`.
- Lab 02: el fuzz target, los dos fallos encontrados, reproducidos y convertidos en tests, `tmin`, y los experimentos del corpus y del arreglo deshecho.
- Lab 03: el benchmark con `harness = false`, el grupo de tamaños, la comparación con la línea base, y los experimentos del ruido y de `cargo test --benches`.
- Lab 04: nextest y los doctests, la cobertura, los mutantes que sobreviven, el mutante equivalente, la corrección de clippy, y los experimentos de `--fail-under-lines` y de ramas en nightly.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 40` en verde.
- Al menos cinco propiedades del dominio con estrategias que llegan a los extremos de cada tipo.
- Una propiedad contra PostgreSQL que encontró un fallo, con la causa, el arreglo, los tests de frontera y la semilla guardada.
- Un benchmark de la función que recibe datos del cliente, con línea base y comparación de varios tamaños.
- `calidad.md` con la cobertura por archivo y cada mutante superviviente analizado: el test que lo mata o por qué es equivalente.

### Niveles

- **Alto (27-30):** verificación en verde y un `calidad.md` que explica cada hallazgo y cada línea o mutante que queda sin cubrir.
- **Medio (21-26):** verificación en verde con propiedades triviales (que repiten la implementación) o un `calidad.md` que copia salidas sin analizarlas.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Calidad avanzada en la suite Rust del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 40](README.md)
