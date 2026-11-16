# Rúbrica de Evaluación - Semana 25

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Tests parametrizados, `@Nested`, `@Tag`, `@TempDir`, extensiones, AssertJ y soft assertions |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | Suite Java del dominio refactorizada con `@Nested`, tests parametrizados y AssertJ |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Elige la fuente de argumentos adecuada para un caso y explica cuándo no parametrizar.
- Explica el orden de los `@BeforeEach` en clases `@Nested` y por qué la clase interna no es `static`.
- Filtra la suite con `-Dgroups` y `-DexcludedGroups`, y explica el riesgo de una etiqueta mal escrita.
- Diferencia `@ExtendWith` de `@RegisterExtension`.
- Explica cuándo usar soft assertions y qué pasa si se olvida `assertAll()`.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde fallo con error, o no distingue las fuentes de argumentos.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los catorce tests pasan y localiza la fila que falla por su índice y su nombre.
- Lab 02: los ocho tests en tres contextos, el experimento sin `@Nested` y el filtrado por etiquetas.
- Lab 03: el archivo que deja el PASO 0, `@TempDir`, la extensión y los dos errores de `ParameterResolutionException`.
- Lab 04: compara los mensajes de JUnit y AssertJ, y provoca los dos fallos de la soft assertion.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los siete requisitos de `./scripts/verificar-semana.sh 25` en verde.
- Contextos `@Nested` con nombres de operaciones del dominio y `@BeforeEach` propios.
- Tests parametrizados con fronteras reales del dominio y nombres que dicen qué caso es.
- Párrafo en el README: qué test de la semana 6 se convirtió en parametrizado y qué casos nuevos aparecieron.

### Niveles

- **Alto (27-30):** verificación en verde, fronteras completas y párrafo con un caso concreto.
- **Medio (21-26):** verificación en verde con casos frontera incompletos o sin el párrafo.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Suite Java del dominio con JUnit 6 y AssertJ](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 25](README.md)
