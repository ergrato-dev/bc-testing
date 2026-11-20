# Proyecto - Suite asíncrona con Jest

> Entregable obligatorio de la semana 09. Tercer incremento de la Etapa 1 sobre el proyecto de la semana 8.

## Objetivo

Hacer asíncrono el aviso de tu servicio, con reintentos, y adaptar la suite de JS para que cada test espere lo que comprueba: los rechazos con `.rejects` y los reintentos con el reloj falso de Jest, sin esperar tiempo real.

## Tiempo estimado

3 horas.

## Requisitos

- Tu proyecto de la [semana 8](../../week-08-matchers_avanzados_y_parametrizacion/3-proyecto/README.md) en `~/proyecto-semana-08`, con `./verify.sh` en verde.
- Los [labs](../2-practicas/README.md) de esta semana: el proyecto repite sus pasos con tu dominio.

## Alcance

Solo cambian `js/` y el plan. Python y Java no se tocan: su servicio aún no existe, y sus formas de probar código asíncrono llegan con sus etapas.

---

## Paso 1: punto de partida (10 min)

```bash
cp -r ~/proyecto-semana-08 ~/proyecto-semana-09
cd ~/proyecto-semana-09
cp <ruta-del-bootcamp>/bootcamp/week-09-testing_asincrono_con_jest/3-proyecto/starter/retry.js js/src/
./verify.sh
# ...
# OK: tres suites en verde y trazabilidad completa
git add js/src/retry.js
git commit -m "proyecto semana 09: punto de partida"
```

`retry.js` es el `sendWithRetry` del [lab 03](../2-practicas/lab-03-reintentos-con-fake-timers/README.md), ya arreglado: hasta dos reintentos, al cabo de 1 s y de 2 s más, y si todos fallan rechaza con el último error.

## Paso 2: el aviso asíncrono (40 min)

Un servidor de correo tarda y a veces falla. Tu servicio pasa a esperar el aviso y a reintentarlo. Escribe primero la regla en la sección 2 del plan, con el número siguiente al último de tus reglas. En el ejemplo es R10:

```markdown
| R10 | El aviso tarda y puede fallar. Si falla, se reintenta al cabo de 1 s y, si vuelve a fallar, 2 s después. Si llega, la reserva queda con `notified: true`; si fallan los tres intentos, se mantiene con `notified: false` |
```

Usa el nombre de lo que guarda tu dominio (reserva, pedido, cita...). Después, el código:

1. **`js/src/notifier.js`**: `send` pasa a `async send(to, message)`. El cuerpo no cambia; ahora devuelve una promesa, como haría el de verdad.
2. **Tu servicio** (`booking-service.js` en el ejemplo): importa `sendWithRetry` y añade, dentro de la función que crea el servicio, una función que avisa y dice si el aviso llegó:

   ```javascript
   const { sendWithRetry } = require("./retry");
   ```

   ```javascript
     // R10: el aviso se reintenta a 1 s y a 2 s más. Si no llega, la reserva se mantiene igual.
     async function notify(to, message) {
       try {
         await sendWithRetry(notifier, to, message);
         return true;
       } catch {
         return false;
       }
     }
   ```

   `catch` sin `(error)` es un `catch` que no usa el error. Aquí el error no se relanza a propósito: la regla dice que un aviso fallido no deshace la reserva, y `notified: false` lo deja escrito.

3. **Cada función del servicio que avisa** (en el starter de la semana 7, `create`) pasa a `async`, y cambia su `notifier.send(...)` por `await notify(...)`. En la que guarda, el resultado va al campo nuevo, **después** de guardar:

   ```javascript
     async function book({ holderName, email, people, tourType, date }) {
       // ... las validaciones y el guardado, como en la semana 8
       bookings.push(booking);
       booking.notified = await notify(email, `Reserva ${code} confirmada: ${people} personas, ${price} €`);
       return booking;
     }
   ```

   Tiene que quedar escrito `await sendWithRetry(` dentro de `notify`: lo busca una comprobación.

Ejecuta la suite sin tocar los tests:

```bash
cd js
pnpm test
echo $?
```

En el ejemplo, Jest no llega ni a imprimir el resumen:

```bash
pnpm test
# $ jest
# /home/tu-usuario/proyecto-semana-09/js/src/tour.js:10
#     throw new Error("Entre 1 y 8 personas");
#           ^
#
# Error: Entre 1 y 8 personas
#     ... collapsed 4 duplicate lines matching above 2 lines 2 times...
#
# [ELIFECYCLE] Test failed. See above for more details.
```

Los `expect(() => service.book(...)).toThrow(...)` de la semana 8 llaman a una función que ya no lanza: rechaza. Esos rechazos no los espera nadie, y el primero termina el proceso. Lo explica la [sección 1 de la teoría 02](../1-teoria/02-errores-asincronos.md#1-una-función-async-no-lanza).

## Paso 3: los tests esperan (50 min)

Recorre los tests del servicio, como en los labs 01 y 02:

| Antes | Ahora |
|---|---|
| `it("...", () => {` | `it("...", async () => {` en cada test que llame al servicio |
| `service.book(LUCIA);` | `await service.book(LUCIA);` |
| `expect(() => service.book(x)).toThrow(mensaje)` | `await expect(service.book(x)).rejects.toThrow(mensaje)` |
| `beforeEach(() => { booking = service.book(...); })` | `beforeEach(async () => { booking = await service.book(...); })` |
| `toStrictEqual([{ ...LUCIA, code, price }])` | El objeto guardado tiene ahora `notified: true` |

El mock del notificador de la semana 7, `{ send: jest.fn() }`, sigue valiendo: `jest.fn()` devuelve `undefined`, y `await undefined` es un aviso que llega a la primera. Los tests de `notifier.test.js` también llaman a una función `async`: ponles `await` delante de `emailNotifier.send(...)`.

Cambia también los nombres que digan `should throw`: el servicio ya no lanza, rechaza (`should reject`). Los tests de las funciones de la semana 3 (las que no son del servicio) no cambian: siguen siendo síncronas.

```bash
pnpm test
grep -nE "^ *expect[(].*[.](rejects|resolves)" tests/*.test.js
grep -n "expect(() => service" tests/*.test.js
```

Los dos `grep` no tienen que encontrar nada. El primero busca `expect` de promesas sin `await` delante; el segundo, `toThrow` sobre el servicio.

Comprueba que `verify.sh` no deja pasar un `expect` suelto: quita el `await` de uno de tus `.rejects` y cambia el mensaje esperado por otro. `pnpm test` mostrará el resumen en verde, o ni eso, y el error suelto después:

```bash
cd ~/proyecto-semana-09
./verify.sh 2>&1 | grep -E "^==|^Tests:|^OK|^FALLO|^JestAssertion"
# == JavaScript
# JestAssertionError: expect(received).rejects.toThrow(expected)
```

`verify.sh` se para en JavaScript: `pnpm test` termina con código 1, y `set -euo pipefail` lo detiene, aunque su línea `Tests:` no llegue a salir. Devuelve el `await` y el mensaje.

## Paso 4: los reintentos (50 min)

Escribe tres casos nuevos de tu regla en la sección 6 del plan, con los ID siguientes al último de tu matriz:

| Caso | Qué prueba |
|---|---|
| El aviso falla una vez | A los 999 ms hay 1 intento; a los 1000, 2. El resultado tiene `notified: true` |
| El aviso falla dos veces | A los 2999 ms hay 2 intentos; a los 3000, 3. El resultado tiene `notified: true` |
| Los tres intentos fallan | Lo guardado se mantiene, con `notified: false`, y hubo 3 intentos |

Escríbelos en un contexto propio, con el reloj falso:

```javascript
    describe("when the confirmation fails", () => {
      beforeEach(() => {
        jest.useFakeTimers();
      });

      afterEach(() => {
        jest.useRealTimers();
      });

      // TC-ACUA-TOUR-036
      it("should retry after 1 second and mark the booking as notified", async () => {
        notifier.send.mockRejectedValueOnce(new Error("SMTP no responde"));

        const pending = service.book(LUCIA);
        await jest.advanceTimersByTimeAsync(999);
        expect(notifier.send).toHaveBeenCalledTimes(1);
        await jest.advanceTimersByTimeAsync(1);

        await expect(pending).resolves.toMatchObject({ notified: true });
        expect(notifier.send).toHaveBeenCalledTimes(2);
      });
    });
```

`notifier.send` ya es un `jest.fn()` del `beforeEach` de fuera: `mockRejectedValueOnce` cambia solo su siguiente llamada. `pending` es la promesa de `book`, que se espera al final. Aquí no hace falta preparar el `.resolves` antes de mover el reloj: `book` no rechaza nunca por el aviso, porque `notify` captura el error.

Para el tercer caso, `mockRejectedValue` (sin `Once`) hace fallar todos los intentos, y `await jest.runAllTimersAsync()` mueve el reloj hasta el final.

Comprueba dos cosas:

```bash
cd ~/proyecto-semana-09/js
pnpm test --testTimeout=1000
```

`--testTimeout=1000` corta cualquier test a los 1000 ms. Si algún test espera un reintento con el reloj real, falla. Después, quita el `await` de `await sendWithRetry(` en tu servicio y ejecuta `pnpm test`: algún test tiene que fallar. Si todo pasa, ningún test comprueba que `notify` espera el resultado del aviso. Devuelve el `await`.

## Paso 5: el plan (20 min)

- **Sección 4**: dos frases sobre la asincronía: cada test espera con `await`, los errores con `.rejects` y los reintentos con fake timers, sin tiempo real.
- **Sección 5**: un criterio de salida más: `pnpm test --testTimeout=1000` pasa.
- **Sección 6**: los tres casos del paso 4, con ✅ en JS y — en Python y Java.
- **Sección 7**: el motivo de cada —, con el ID entero de cada caso.
- **Sección 8**: lo que encontraste. Como mínimo, lo que hicieron los tests de la semana 8 al volver asíncrono el servicio (paso 2), y que un aviso fallido se perdía sin dejar rastro.

## Paso 6: verify.sh y entrega (10 min)

```bash
cd ~/proyecto-semana-09
./verify.sh
# OK: tres suites en verde y trazabilidad completa
```

Copia el resumen en la sección 9 con la fecha, y comprueba la entrega:

```bash
PROYECTO=~/proyecto-semana-09 <ruta-del-bootcamp>/scripts/verificar-semana.sh 09
git add .
git commit -m "proyecto semana 09: aviso asíncrono con reintentos"
```

---

## Ejemplo

El **acuario virtual** de las semanas anteriores. `book` y `cancel` pasan a `async` y avisan con `notify`. La regla nueva es R10, con tres casos (`TC-ACUA-TOUR-036` a `038`):

```bash
pnpm test --verbose tests/booking-service.test.js 2>&1 | grep -A4 "when the confirmation fails"
#       when the confirmation fails
#         ✓ should retry after 1 second and mark the booking as notified (4 ms)
#         ✓ should wait 2 more seconds before the second retry (1 ms)
#         ✓ should keep the booking unnotified after three failed attempts (1 ms)
#     cancel
```

Toda la suite tarda menos de medio segundo, con 30 tests:

```bash
pnpm test
# Tests:       30 passed, 30 total
# Time:        0.405 s, estimated 1 s
```

Sin el `await` de `await sendWithRetry(`, `notify` devuelve `true` sin esperar al aviso. El caso de los tres intentos fallidos lo encuentra:

```bash
pnpm test 2>&1 | grep -E "^(PASS|FAIL)|^Tests:"
# FAIL tests/booking-service.test.js
# PASS tests/tour.test.js
# PASS tests/notifier.test.js
# Tests:       1 failed, 29 passed, 30 total
```

En el plan, los dos hallazgos van a la sección 8:

```markdown
| Al volver asíncronos `book` y `cancel`, los `expect(() => ...).toThrow()` dejaron de ver los errores: una función `async` no lanza, devuelve una promesa rechazada. Jest no llegó a imprimir el resumen; el proceso terminó con `Error: Entre 1 y 8 personas` | JS | TC-ACUA-TOUR-027 a 029 y 031 | Alta | `await expect(...).rejects.toThrow(...)` en cada error |
| Un aviso que fallaba se perdía: no había reintentos, y nada decía si el titular lo había recibido | JS | TC-ACUA-TOUR-036 a 038 | Alta | Regla R10: `sendWithRetry` y el campo `notified` |
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json). Todos se ejecutan sobre `js/`, salvo `motivos` y `verify`:

| Comprobación | Qué mira |
|---|---|
| js-pasa | `pnpm install --frozen-lockfile` y `pnpm test` terminan con código 0, sin tests `todo`, saltados ni fallidos |
| sin-focos | Ningún archivo de `tests/` tiene `.only(`, `.skip(`, `fit(`, `xit(`, `fdescribe(` ni `xdescribe(` |
| nombres | Todos los `it(` y `test(` empiezan por `should ` |
| filas-con-id | El número de comentarios `// TC-...` en una línea sola es igual al total de tests de Jest |
| reintentos | Existe `src/retry.js`, y algún archivo de `src/` tiene `await sendWithRetry(` |
| rechazos | Algún test tiene una línea con `await expect(` y `.rejects.` |
| sin-expect-suelto | Ninguna línea empieza por `expect(` y tiene `.rejects.` o `.resolves.` |
| fake-timers | Algún test usa `useFakeTimers(` y `advanceTimersByTimeAsync(` o `runAllTimersAsync(` |
| sin-avance-sincrono | Ningún test usa `advanceTimersByTime(`, `runAllTimers(` ni `runOnlyPendingTimers(` |
| relojes-restaurados | Cada archivo de tests con `useFakeTimers(` tiene también `useRealTimers(` |
| sin-esperas-reales | `pnpm test --testTimeout=1000` termina con código 0 |
| mutante-sin-await | En una copia de `js/`, con `await sendWithRetry(` cambiado por `sendWithRetry(`, `pnpm test` falla |
| motivos | El ID de cada fila con — aparece en la sección 7 |
| verify | `./verify.sh` termina con código 0 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Reintentos con fake timers](../2-practicas/lab-03-reintentos-con-fake-timers/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 09](../README.md)
