# Rúbrica de Evaluación - Semana 03

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Proyecto con pnpm y Jest, `describe`/`it`/`expect`, AAA, nombres, matchers, tipos de fallo, ejecución parcial |
| Desempeño | 40% | Labs 01 a 04: primer proyecto, AAA y matchers, casos del planetario, tests que mienten |
| Producto | 30% | Suite de Jest de una funcionalidad del dominio asignado, con mutaciones anotadas |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hace cada campo del `package.json` del curso y por qué pnpm pide `pnpm-workspace.yaml` al instalar Jest.
- Explica qué hacen `describe`, `it` y `expect`, y ordena un test en Arrange, Act y Assert.
- Pone nombres `should ... when ...` y explica por qué el esperado se escribe a mano.
- Elige entre `toBe`, `toEqual`, `toBeCloseTo`, `toBeNull` y `toThrow` según el valor, y explica por qué `toThrow` recibe una función.
- Explica por qué un test sin `expect` pasa y por qué `toBeTruthy` con un número casi nunca comprueba nada.
- Distingue un fallo de aserción de un error de ejecución leyendo el informe.
- Ejecuta un archivo, un nombre de test y el modo watch, y sabe por qué `--watch` necesita git.

### Niveles

- **Alto (27-30):** responde con ejemplos propios y un informe de Jest leído en voz alta, línea a línea.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, entre `toBe` y `toEqual`).
- **Bajo (<21):** no distingue un fallo de aserción de un error de ejecución, o cree que un test en verde siempre comprueba algo.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `~/labs-semana-03` con `package.json`, `pnpm-workspace.yaml`, 3 tests de `math` en verde y un commit.
- Lab 02: 10 tests de `user-utils` en verde, sin `TODO`; `createUser` con `toEqual`, la búsqueda fallida con `toBeNull` y el descuento con `toBeCloseTo`.
- Lab 03: 19 tests del planetario con el ID de su caso, 4 hallazgos con línea y técnica, y el código corregido sin tocar ningún esperado.
- Lab 04: los cinco tests reescritos, el envío probado con 29,99 y 30, y cada una de las cuatro mutaciones detectada por un test.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada decisión (matcher, código o test) justificada.
- **Medio (28-35):** los cuatro labs completos, con algún esperado calculado en lugar de escrito o algún nombre genérico.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 03` pasa las 9 comprobaciones.
- Las funciones salen de las reglas de la semana 2 y son puras: si una regla depende de hoy, la fecha llega como parámetro.
- Los tests cubren camino feliz, una clase inválida por test y los valores límite, con el ID del caso encima.
- Cada matcher corresponde a su valor; todos los esperados están escritos a mano.
- Las mutaciones de `mutaciones.md` son reales: al repetirlas, el test anotado falla.

### Niveles

- **Alto (27-30):** todo lo anterior, con mutaciones en cada función y algún bug propio encontrado y anotado.
- **Medio (21-26):** pasan las comprobaciones, pero algún test mezcla dos clases inválidas o algún límite no se prueba.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Primera suite de tests del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 03](README.md)
