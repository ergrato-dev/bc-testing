# Rúbrica de Evaluación - Semana 38

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Traits como costura, genéricos frente a `dyn`, mutabilidad interior, mocks de mockall, rstest e insta |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | El servicio del dominio en Rust con traits, fakes, un mock, casos de rstest, un snapshot y `dobles.md` |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué cambia entre una interfaz de Go y un trait de Rust al implementarlo y al modificarlo.
- Decide entre un servicio genérico y `dyn Trait`, y explica qué pierde el test con `Box<dyn Trait>`.
- Explica por qué un fake que guarda datos usa `RefCell` en lugar de un trait con `&mut self`.
- Explica cuándo falla un mock de mockall con una llamada inesperada y cuándo con un `times` sin cumplir, y por qué `#[automock]` no sirve para `tests/`.
- Diferencia `#[case]`, `#[values]` y `#[fixture]`, y explica el ciclo de un snapshot con insta.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue un fake de un mock, o no sabe por qué el test necesita el tipo concreto del doble.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el stub, el fake con `RefCell`, el doble que falla, `broadcast` con `Box<dyn Notifier>`, y los errores `E0596` y `E0308`.
- Lab 02: los mocks con `never`, `with`, `times`, `returning` y `withf`, los tres fallos provocados y el mock con `mock!` en `tests/`.
- Lab 03: los casos con y sin nombre, las combinaciones con `#[values]`, la fixture con `#[with]`, los dos casos que fallan por separado, la fila incompleta y el error de bytes.
- Lab 04: el primer snapshot revisado, el cambio rechazado, los snapshots inline y de `Debug`, `tests/common/mod.rs` con el warning de `dead_code` y los snapshots con `CI`.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 38` en verde.
- Un trait por dependencia, con los métodos que el servicio usa y `&self`, y un servicio genérico.
- Fakes para el estado y un mock para la interacción, sin mockear lo que un fake resuelve mejor.
- Un `#[case]` por rechazo, incluido el caso que encontró la semana 31, y un test del fallo de la dependencia mockeada.
- Un snapshot leído antes de aceptarlo y un `dobles.md` que justifica cada doble y los riesgos que fijan los tests.

### Niveles

- **Alto (27-30):** verificación en verde y un `dobles.md` que explica cada decisión con su motivo.
- **Medio (21-26):** verificación en verde con un `dobles.md` genérico o sin el test del fallo de la dependencia.
- **Bajo (<21):** algún requisito verificable en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - El servicio del dominio con traits y dobles](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 38](README.md)
