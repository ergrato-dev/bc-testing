# Rúbrica de Evaluación - Semana 07

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | `describe` por comportamiento, nombre completo y `-t`, `.only`, hooks y su orden, aislamiento, `--randomize`, stub, mock y spy |
| Desempeño | 40% | Labs 01 a 03: suite reorganizada, `beforeEach` y estado compartido, dobles de prueba sobre el servicio del planetario |
| Producto | 30% | Suite de JS del dominio reorganizada y un servicio con estado probado con dobles, con la trazabilidad de la semana 6 |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Organiza una suite en módulo, función y contexto `when ...`, y explica por qué el informe de `--verbose` sirve de especificación.
- Explica cómo forma Jest el nombre completo de un test y lo usa con `-t` para ejecutar un contexto.
- Explica por qué un `.only` olvidado termina con código 0 y cómo encontrarlo antes de un commit.
- Explica para qué sirve cada hook y predice el orden de los `beforeEach` y `afterEach` de dos `describe` anidados.
- Explica por qué la preparación no va en el cuerpo del `describe` ni en `beforeAll`, y reconoce un test que pasa solo y falla en grupo.
- Usa `--randomize` y `--seed` para encontrar y repetir una dependencia de orden.
- Distingue stub, mock y spy por lo que comprueba el test, y explica por qué un spy se restaura.

### Niveles

- **Alto (27-30):** responde con ejemplos propios y predice, antes de ejecutar, qué tests fallarán al compartir estado.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, entre stub y mock).
- **Bajo (<21):** pone la preparación en el cuerpo del `describe` o no sabe qué comprueba un mock.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: 10 tests en tres niveles con 6 contextos `when ...`, todos con `should`; el `.only` encontrado con `grep` y quitado.
- Lab 02: el fallo del primer intento explicado con las cuentas; `beforeEach` en el `describe` exterior y otro en `when the booking exists`; 11 tests que pasan con varias semillas; el orden de los hooks anotado antes de ejecutar el experimento.
- Lab 03: ningún test usa `systemClock`; el bug del aviso encontrado con el mock y corregido en `src/booking.js`; el spy de `console.log` silenciado y restaurado; 19 tests en verde con `--randomize`.

### Niveles

- **Alto (36-40):** los tres labs completos, con cada decisión (contexto, hook o doble) justificada.
- **Medio (28-35):** los tres labs completos, con algún contexto mal nombrado o algún doble creado fuera de `beforeEach`.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 07` pasa las 13 comprobaciones.
- La suite del dominio conserva todos sus casos y sus ID, ahora agrupados por función y contexto.
- El servicio valida con las funciones de la semana 3, lee la fecha del reloj inyectado y avisa solo después de guardar.
- Los tests del servicio comprueban el resultado con un stub del reloj y los avisos con un mock, incluido que no se avisa cuando se rechaza.
- El plan tiene las reglas y los riesgos del servicio, sus casos con ✅ — —, el motivo en la sección 7 y un criterio de salida que se puede cumplir.

### Niveles

- **Alto (27-30):** todo lo anterior, con la mutación del aviso detectada y alguna regla que faltaba descubierta al guardar estado.
- **Medio (21-26):** pasan las comprobaciones, pero algún test del servicio no comprueba el aviso o comprueba solo que se llamó, sin argumentos.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Suite modular con Jest](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 07](README.md)
