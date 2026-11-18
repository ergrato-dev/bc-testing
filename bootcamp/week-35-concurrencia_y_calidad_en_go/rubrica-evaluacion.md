# Rúbrica de Evaluación - Semana 35

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Carreras de datos y `-race`, `testing/synctest`, fuzzing nativo, benchmarks y `benchstat`, cobertura y linters |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | La suite Go del dominio con un test concurrente limpio con `-race`, un fallo encontrado por el fuzzer, una optimización medida con `benchstat` y el informe `calidad.md` |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Define una carrera de datos, explica por qué un test concurrente en verde no demuestra que no la haya, y lee un informe de `-race`.
- Explica cuándo avanza el reloj de una burbuja de `synctest`, qué es un bloqueo duradero y para qué sirve `synctest.Wait()`.
- Diferencia `go test` de `go test -fuzz` para un fuzz test, y explica qué va a `testdata/fuzz/` y qué a la caché.
- Lee una salida de `benchstat` (mediana, `±`, `p`, `~`) y explica por qué hacen falta varias medidas.
- Explica qué mide la cobertura de Go (sentencias), qué añaden `-coverpkg` y `GOCOVERDIR`, y qué no dice un 100 %.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde un test que pasa con un código correcto, o una medida suelta con una mejora demostrada.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: la carrera de `Views` con su informe y su mutex, las pruebas con tiempo virtual, y los experimentos del mapa sin protección, el `Wait` que falta, el test fuera de la burbuja y la goroutine que nadie para.
- Lab 02: los dos fallos encontrados por el fuzzer y arreglados, y los experimentos de `-fuzz` ambiguo, el corpus como regresión, la semilla de otro tipo y la caché.
- Lab 03: el benchmark, los sub-benchmarks por tamaño, la comparación con `benchstat`, y los experimentos de `b.N`, el compilador, una sola medida y el mismo código dos veces.
- Lab 04: la cobertura por función y en HTML, `-coverpkg`, la cobertura del binario sumada a la de los tests, los cuatro avisos de `staticcheck` resueltos y los cuatro experimentos.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 35` en verde.
- El test concurrente prueba una regla del dominio (por ejemplo, que una pieza no se presta dos veces), no solo la ausencia de carreras.
- El fuzz test comprueba una propiedad con sentido para el dominio, y su fallo está arreglado en el código, con una fila nueva en la tabla de tests.
- La optimización está medida con `benchstat` (`-count=10`) y protegida por un test que compara el comportamiento antes y después.
- `calidad.md` explica qué encontró cada herramienta, con salidas reales.

### Niveles

- **Alto (27-30):** los once requisitos en verde y cada hallazgo explicado y arreglado en el código, no en el test.
- **Medio (21-26):** los requisitos en verde con algún hallazgo sin explicar o una optimización sin proteger.
- **Bajo (<21):** algún requisito en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Concurrencia y calidad en la suite Go del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 35](README.md)
