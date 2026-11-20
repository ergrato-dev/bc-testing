# Lab 02 - Errores asíncronos

## Objetivo

Comprobar los rechazos de `cancel` con `.rejects`, encontrar un `try/catch` que pasa sin comprobar nada y, con él, un error de la taquilla que el servicio se traga.

## Tiempo estimado

80 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-errores-asincronos.md).
- El [lab 01](../lab-01-tests-que-esperan/README.md) terminado en `~/labs-semana-09`.

## Punto de partida

El código de este lab trae un `src/booking.js` nuevo, con tu arreglo del lab 01 y una función más, `cancel`. La taquilla falsa aprende a liberar reservas:

```bash
cd ~/labs-semana-09
cp -r <ruta-del-bootcamp>/bootcamp/week-09-testing_asincrono_con_jest/2-practicas/lab-02-errores-asincronos/starter/. .
git status --short
#  M src/booking.js
#  M tests/fake-box-office.js
# ?? tests/booking-errors.test.js
git diff --stat
#  src/booking.js           | 27 +++++++++++++++++++++++++--
#  tests/fake-box-office.js |  8 ++++++++
#  2 files changed, 33 insertions(+), 2 deletions(-)
```

`git status --short` resume los cambios: `M` es un archivo modificado y `??` uno nuevo. Las dos líneas que se quitan son el `return` del servicio y el `module.exports`, que ahora incluyen `cancel` y `BookingError`.

## Contexto

`cancel(id)` pide a la taquilla que libere la reserva:

| Qué pasa | Qué hace `cancel` |
|---|---|
| La reserva existe | Se resuelve con la reserva cancelada |
| No existe (la taquilla devuelve `null`) | Rechaza con un `BookingError` de código `NOT_FOUND`: «La reserva no existe» |
| La taquilla falla | Rechaza con un `BookingError` de código `BOX_OFFICE_DOWN`: «La taquilla no responde», con el error original en `cause` |

`BookingError` es un error con un campo `code`, como el `TicketError` de la [semana 8](../../../week-08-matchers_avanzados_y_parametrizacion/2-practicas/lab-03-errores-y-fallos-legibles/README.md). `tests/booking-errors.test.js` tiene tres tests, uno por fila de la tabla.

---

## Paso 1: un test que no ve el error (15 min)

```bash
pnpm test tests/booking-errors.test.js
# FAIL tests/booking-errors.test.js
#   createBookingService errors
#     cancel
#       when the booking exists
#         ✓ should return the cancelled booking (38 ms)
#       when the booking does not exist
#         ✕ should reject the id (2 ms)
#       when the box office is down
#         ✓ should reject with the box office message (1 ms)
#
#   ● createBookingService errors › cancel › when the booking does not exist › should reject the id
#
#     expect(received).toThrow(expected)
#
#     Expected substring: "La reserva no existe"
#
#     Received function did not throw
# ...
# Tests:       1 failed, 2 passed, 3 total
# ...
# BookingError: La reserva no existe
#   code: 'NOT_FOUND'
# }
```

`cancel(99)` sí rechaza con «La reserva no existe»: el error sale al final, suelto. Pero `toThrow` dice que la función no lanzó. Explícalo con la [sección 1 de la teoría 02](../../1-teoria/02-errores-asincronos.md#1-una-función-async-no-lanza).

Reescribe el test con `.rejects`:

```javascript
      it("should reject the id", async () => {
        await expect(service.cancel(99)).rejects.toThrow("La reserva no existe");
      });
```

## Paso 2: la clase y el código (15 min)

El mensaje es para personas; quien llama a `cancel` decide qué hacer con el código. Añade un test al mismo contexto que compruebe la clase y el código:

```javascript
      it("should reject with the NOT_FOUND code", async () => {
        await expect(service.cancel(99)).rejects.toThrow(BookingError);
        await expect(service.cancel(99)).rejects.toMatchObject({ code: "NOT_FOUND" });
      });
```

`BookingError` hay que importarlo. Cambia la primera línea del archivo:

```javascript
const { createBookingService, BookingError } = require("../src/booking");
```

```bash
pnpm test tests/booking-errors.test.js
# Tests:       4 passed, 4 total
```

Cada `expect(service.cancel(99))` es una llamada nueva a `cancel`.

## Paso 3: el test que pasa sin comprobar nada (20 min)

`should reject with the box office message` está en verde. Lee el test: la taquilla falla con `mockRejectedValue`, y el `catch` comprueba el mensaje. ¿Cuántos `expect` se ejecutan si `cancel` no rechaza?

Compruébalo. Añade al principio del test:

```javascript
        expect.assertions(1);
```

```bash
pnpm test tests/booking-errors.test.js
# ...
#   ● createBookingService errors › cancel › when the box office is down › should reject with the box office message
#
#     expect.assertions(1)
#
#     Expected one assertion to be called but received zero assertion calls.
# ...
# Tests:       1 failed, 3 passed, 4 total
```

Cero: el `catch` no se ejecutó, así que `cancel` no rechazó. Ahora reescribe el test con `.rejects`, sin `try` ni `expect.assertions`, y añade otro para el error original:

```javascript
      it("should reject with the box office message", async () => {
        boxOffice.release = jest.fn().mockRejectedValue(new Error("ECONNRESET"));

        await expect(service.cancel(1)).rejects.toThrow("La taquilla no responde");
        await expect(service.cancel(1)).rejects.toMatchObject({ code: "BOX_OFFICE_DOWN" });
      });

      it("should keep the original error as the cause", async () => {
        boxOffice.release = jest.fn().mockRejectedValue(new Error("ECONNRESET"));

        await expect(service.cancel(1)).rejects.toHaveProperty("cause.message", "ECONNRESET");
      });
```

```bash
pnpm test tests/booking-errors.test.js
# ...
#   ● createBookingService errors › cancel › when the box office is down › should reject with the box office message
#
#     expect(received).rejects.toThrow()
#
#     Received promise resolved instead of rejected
#     Resolved to value: null
# ...
# Tests:       2 failed, 3 passed, 5 total
```

`cancel` se resolvió con `null`. El `try/catch` lo escondía; `.rejects` dice con qué valor.

## Paso 4: el error tragado (15 min)

Abre `src/booking.js` y busca el `catch` de `cancel`:

```javascript
    } catch (error) {
      return null;
    }
```

El error de la taquilla se pierde: quien cancela recibe `null` y cree que no había reserva, o que se canceló. Haz lo que dice el comentario de la función: rechazar con `BOX_OFFICE_DOWN` y el error original en `cause`:

```javascript
    } catch (error) {
      throw new BookingError("La taquilla no responde", "BOX_OFFICE_DOWN", { cause: error });
    }
```

```bash
pnpm test
# Tests:       11 passed, 11 total
```

## Paso 5: un fallo que engaña (15 min)

En `should keep the original error as the cause`, cambia `"ECONNRESET"` del `expect` por `"ETIMEDOUT"` y mira el fallo. Después, prueba la otra forma, que también pasa con el valor correcto:

```javascript
        await expect(service.cancel(1)).rejects.toMatchObject({
          code: "BOX_OFFICE_DOWN",
          cause: new Error("ETIMEDOUT"),
        });
```

Compara los dos fallos con la [sección 4 de la teoría 02](../../1-teoria/02-errores-asincronos.md#el-error-original-en-cause): ¿cuál dice qué valor llegó? Devuelve el test a la versión con `toHaveProperty` y `"ECONNRESET"`.

---

## Comprobación

```bash
pnpm test
# Tests:       11 passed, 11 total
grep -c "try {" tests/booking-errors.test.js
# 0
grep -c "rejects" tests/booking-errors.test.js
# 6
```

Guarda el lab:

```bash
git add src/booking.js tests/
git commit -m "lab 02: errores asíncronos"
```

## Repaso

- [ ] Sabes por qué `expect(() => f()).toThrow()` no ve el error de una función `async`.
- [ ] Compruebas los rechazos con `await expect(promesa).rejects` y el mensaje, la clase o el código.
- [ ] Reconoces un `try/catch` que pasa sin comprobar nada, y sabes demostrarlo con `expect.assertions`.
- [ ] Simulas una dependencia que falla con `mockRejectedValue`.
- [ ] Un error que envuelve a otro lleva el original en `cause`, y sabes comprobarlo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Tests que esperan](../lab-01-tests-que-esperan/README.md) | [Lab 03 - Reintentos con fake timers](../lab-03-reintentos-con-fake-timers/README.md) → |

[↑ Semana 09](../../README.md)
