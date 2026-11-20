# Rúbrica de Evaluación - Semana 08

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Tablas con `it.each`, nombres con datos, `toEqual` frente a `toStrictEqual`, matchers parciales y asimétricos, `toThrow` y fallos legibles |
| Desempeño | 40% | Labs 01 a 03: el formulario en tablas, las entradas del planetario con sus matchers, errores con clase y un matcher propio |
| Producto | 30% | Suite de JS del dominio en tablas con matchers precisos, y `verify.sh` contando los tests de cada fila |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Reconoce los tests que son el mismo test con otros datos y explica cuándo un caso no va en una tabla.
- Escribe una tabla de objetos con `$campo` en el nombre y explica por qué el ID va encima de cada fila.
- Explica qué deja pasar `toEqual` y no `toStrictEqual`, y cuándo `toMatchObject` es demasiado permisivo.
- Usa un matcher asimétrico para un valor que cambia en cada ejecución, y explica cuándo conviene más un stub.
- Explica qué compara `toThrow` con un texto, una expresión regular y una clase, y por qué `toThrow(undefined)` pasa con cualquier error.
- Elige el matcher cuyo fallo explica el problema (`toHaveLength` frente a `.length` con `toBe`).

### Niveles

- **Alto (27-30):** responde con ejemplos propios y predice qué fila fallará y qué dirá su nombre antes de ejecutar.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, entre `toMatchObject` y `objectContaining`).
- **Bajo (<21):** mezcla en una tabla casos con `expect` distintos o no sabe por qué `toEqual` ignora un campo.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los 19 tests en cinco tablas de objetos y un `it`, con 19 IDs; la fila del límite de 99 encontrada por su nombre; las dos trampas del nombre explicadas.
- Lab 02: la hora y el código comprobados con asimétricos; la dependencia del contador explicada; el campo `discount: undefined` encontrado con `toStrictEqual` y corregido en `src/ticket.js`; los tests de grupos con matchers de arrays.
- Lab 03: la fila con `expectd` encontrada y corregida; `TicketError` con `field` y sus tests; los cuatro fallos comparados; `toBeTicketCodeFor` cargado con `setupFilesAfterEnv`; 35 tests en verde con `--randomize`.

### Niveles

- **Alto (36-40):** los tres labs completos, con cada matcher justificado.
- **Medio (28-35):** los tres labs completos, con algún nombre de test que no dice qué fila es o algún `toEqual` donde iba `toStrictEqual`.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 08` pasa las 14 comprobaciones.
- `tablas.md` explica qué tests van en cada tabla, cuáles se quedan como `it` y por qué.
- La suite conserva todos sus casos y sus ID, ahora en tablas, con al menos un caso nuevo que salió del inventario.
- Los objetos guardados se comparan con `toStrictEqual`, y cada `toThrow` dice el mensaje que espera.
- `verify.sh` toma el número de tests de JS del total de Jest y falla con una fila sin ID.
- El plan tiene los casos nuevos con su motivo en la sección 7, y el hueco de `verify.sh` en la sección 8.

### Niveles

- **Alto (27-30):** todo lo anterior, con un bug o un campo de más encontrado al cambiar a `toStrictEqual`, o una mutación detectada por una tabla.
- **Medio (21-26):** pasan las comprobaciones, pero alguna tabla mezcla casos con `expect` distintos o algún nombre no dice qué fila es.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Suite parametrizada con Jest](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 08](README.md)
