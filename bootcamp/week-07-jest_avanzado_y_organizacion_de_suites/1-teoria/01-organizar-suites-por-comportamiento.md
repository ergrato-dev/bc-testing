# 01 - Organizar una suite por comportamiento

> Cómo agrupar los tests con `describe` anidados para que el informe de Jest se lea como una lista de reglas.

---

## 🎯 Objetivos

- Anidar `describe` por función y por contexto (`when ...`), y escribir `it` que completan la frase.
- Leer el nombre completo de un test y usarlo con `-t` para ejecutar un contexto.
- Saber qué hacen `.only`, `.skip` y `.todo`, y por qué un `.only` olvidado deja la suite en verde.

---

## 1. Cuando la suite crece

En la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/02-estructura-test-jest.md) tus suites tenían un `describe` y unos pocos `it`. Esta es una suite de diez tests del servicio de reservas del planetario, el de los labs de esta semana, con un solo nivel:

```bash
pnpm test --verbose
# PASS tests/booking.test.js
#   booking
#     ✓ book works (2 ms)
#     ✓ book seats (1 ms)
#     ✓ book 0 (2 ms)
#     ✓ book 7 (1 ms)
#     ✓ book today (1 ms)
#     ✓ full (1 ms)
#     ✓ full other day
#     ✓ cancel
#     ✓ cancel error
#     ✓ seats
#
# Tests:       10 passed, 10 total
```

`--verbose` lista cada test con su nombre; sin él, Jest solo lo hace cuando ejecuta un único archivo. Los tests pasan, pero el informe no dice qué reglas cumple el servicio. `book 0` ¿acepta 0 butacas o las rechaza? Hay que abrir el código para saberlo.

---

## 2. Tres niveles de `describe`

Un `describe` puede contener otros. La convención de este curso tiene tres niveles:

| Nivel | Qué nombra | Ejemplo |
|---|---|---|
| `describe` exterior | El módulo o el objeto que pruebas | `createBookingService` |
| `describe` de función | Una función o un método | `book` |
| `describe` de contexto | La situación, siempre con `when ...` | `when the session is full` |
| `it` | Lo que tiene que pasar, siempre con `should ...` | `should throw when the seats do not fit` |

```javascript
describe("createBookingService", () => {
  describe("book", () => {
    describe("when the session is full", () => {
      it("should throw when the seats do not fit", () => {
        // Arrange, Act, Assert como en la semana 3
      });
    });
  });
});
```

La misma suite, reorganizada así:

```bash
pnpm test --verbose
# PASS tests/booking.test.js
#   createBookingService
#     book
#       when the booking is valid
#         ✓ should return the booking with id 1 when it is the first one (2 ms)
#         ✓ should take the seats from that session
#         ✓ should not take seats from other sessions (1 ms)
#       when the number of seats is out of range
#         ✓ should throw when seats is 0 (3 ms)
#         ✓ should throw when seats is 7
#       when the session is not after today
#         ✓ should throw when the date is today (1 ms)
#       when the session is full
#         ✓ should throw when the seats do not fit (1 ms)
#     cancel
#       when the booking exists
#         ✓ should free its seats
#       when the booking does not exist
#         ✓ should throw (1 ms)
#     availableSeats
#       ✓ should return the capacity when there are no bookings
#
# Tests:       10 passed, 10 total
```

Ahora el informe es la especificación: cada contexto es una clase de equivalencia o un límite de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md), y cada `it` lo que se espera en ella.

Tres reglas para decidir dónde va cada cosa:

- **Un contexto por situación, no por test.** Si dos tests comparten la situación (0 y 7 butacas están fuera de rango), van en el mismo `describe`.
- **Una función sin contextos no los necesita.** `availableSeats` tiene un solo test, y un `describe("when ...")` con un `it` dentro solo añade una línea.
- **Por comportamiento, no por tipo de test.** `describe("happy path")` y `describe("errors")` no dicen qué regla se cumple; `when the session is full` sí.

---

## 3. El nombre completo de un test

Jest une los nombres de todos los `describe` y el del `it` en el nombre completo. Es lo que aparece en la cabecera de un fallo, separado con `›`:

```text
● createBookingService › book › when the session is full › should throw when the seats do not fit
```

Y es el texto en el que busca `-t`, que ya usaste en la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/03-matchers-y-ejecucion.md#4-ejecutar-parte-de-la-suite). Para `-t` los nombres van separados por espacios, así que puedes ejecutar un contexto entero:

```bash
pnpm test -t "book when the session"
# PASS tests/booking.test.js
#   createBookingService
#     book
#       when the booking is valid
#         ○ skipped should return the booking with id 1 when it is the first one
#         ...
#       when the session is not after today
#         ✓ should throw when the date is today (1 ms)
#       when the session is full
#         ✓ should throw when the seats do not fit (1 ms)
#   ...
# Tests:       8 skipped, 2 passed, 10 total
```

Con una suite plana, `-t "full"` encontraría `full` y `full other day`, que prueban cosas distintas.

---

## 4. `.only`, `.skip` y `.todo`

| Escribes | Qué hace Jest | Para qué |
|---|---|---|
| `it.only(...)` o `describe.only(...)` | Ejecuta solo esos tests del archivo; los demás salen como `skipped` | Centrarte en un test mientras lo depuras |
| `it.skip(...)` o `describe.skip(...)` | No ejecuta ese test; sale como `skipped` | Apartar un test un momento, con un motivo |
| `it.todo("should ...")` | Apunta un test que aún no existe; sale como `todo` | La lista de lo que falta, como en la [semana 6](../../week-06-plan_de_pruebas_y_suite_base_multilenguaje/1-teoria/01-plan-de-pruebas-y-trazabilidad.md) |

El peligro es `.only`. Si se queda en el código, la suite ejecuta un test y termina en verde:

```bash
pnpm test
# Tests:       9 skipped, 1 passed, 10 total
# ...
echo $?
# 0
```

`echo $?` muestra el código de salida del último comando. Es 0: para Jest, y para cualquier script o CI que lo ejecute, todo ha ido bien. Nueve tests no se han ejecutado y nadie lo nota si no lee la línea `Tests:`.

Antes de cada commit, búscalos:

```bash
grep -rn "\.only(\|\.skip(" tests/
# tests/booking.test.js:42:      it.only("should throw when seats is 7", () => {
```

`\.` es un punto literal (sin la barra, `.` es «cualquier carácter») y `\|` separa las dos alternativas. Si no sale nada, no hay ninguno.

---

## ✅ Resumen

- Tres niveles: el módulo, la función y el contexto `when ...`; el `it` dice `should ...`.
- El informe de `--verbose` es la especificación del módulo: cada contexto es una clase o un límite.
- El nombre completo une todos los `describe`; `-t` lo usa para ejecutar un contexto entero.
- `.only` olvidado deja la suite en verde con tests sin ejecutar: búscalo con `grep` antes del commit.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 07 - Jest Avanzado y Organización de Suites](../README.md) | [Teoría — 02 - Hooks y aislamiento entre tests](02-hooks-y-aislamiento.md) → |

[↑ Semana 07](../README.md)
