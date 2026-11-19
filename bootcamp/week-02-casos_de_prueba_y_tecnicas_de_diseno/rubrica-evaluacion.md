# Rúbrica de Evaluación - Semana 02

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Campos de un caso, partición, valores límite, tablas de decisión, cobertura, smoke/sanity/regresión, exploratorio |
| Desempeño | 40% | Labs 01 a 04: análisis de casos, partición y límites, tabla de decisión, sesión exploratoria |
| Producto | 30% | `casos-de-prueba.md` de una funcionalidad del dominio asignado |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Nombra los campos de un caso y distingue `Blocked` de `Fail`.
- Distingue una precondición comprobable de una vaga y un esperado observable de uno vago.
- Saca las clases de equivalencia de cada regla de un campo y explica por qué va una clase inválida por caso.
- Elige los valores límite de un rango con las variantes de 2 y 3 valores, y da un error que solo detecta la de 3.
- Construye y simplifica una tabla de decisión, y explica qué hace cuando una columna no tiene respuesta.
- Explica la diferencia entre cobertura de sentencias y de ramas, y por qué el 100 % no garantiza que no haya bugs.
- Distingue smoke, sanity y regresión por el momento en que se ejecutan.
- Describe una sesión exploratoria con charter y notas.

### Niveles

- **Alto (27-30):** cada respuesta va con un ejemplo propio, no el de la teoría.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores (por ejemplo, entre smoke y sanity).
- **Bajo (<21):** confunde partición con valores límite, o cree que la cobertura alta asegura que no hay bugs.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `casos.md` sin `(completar`; el caso D con la precondición del carrito, el E partido en dos y ningún dato inventado.
- Lab 02: `analisis.md` sin `TODO`; entre 15 y 25 casos, una clase inválida por caso, los límites del grupo escolar y el nombre de 50 caracteres con tildes.
- Lab 03: `tabla.md` sin `TODO`; las 8 columnas con sus reglas, las dos contradicciones como preguntas, la tabla simplificada y los casos con 29,99 € y 30,00 €.
- Lab 04: `sesion.md` sin `TODO`; al menos 12 notas con salidas reales, tres hallazgos clasificados con el manual, dos casos guionizados y las tres suites justificadas.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada decisión justificada con la teoría o una regla.
- **Medio (28-35):** los cuatro labs completos, con algún caso repetido o alguna justificación genérica.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 02` pasa las 7 comprobaciones.
- Las reglas tienen números y mensajes; las clases salen de cada regla.
- Los valores límite son los de verdad (el vecino de fuera es el primer valor rechazado) y cada caso tiene una sola clase inválida.
- Otra persona ejecuta cualquier caso sin preguntar: precondiciones comprobables (con la fecha fija si hay reglas de fechas), pasos de una acción y esperado observable.
- La matriz de trazabilidad no deja reglas sin casos.

### Niveles

- **Alto (27-30):** todo lo anterior, con tabla de decisión y una justificación escrita de la variante de valores límite elegida en cada campo.
- **Medio (21-26):** pasan las comprobaciones, pero algún caso mezcla dos clases inválidas o algún límite está desplazado.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Suite de casos de prueba](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 02](README.md)
