# Lab 03 - Reintentos con fake timers

## Objetivo

Probar un aviso con reintentos sin esperar tiempo real: activar el reloj falso de Jest, moverlo hasta el milisegundo exacto de cada reintento y, con eso, encontrar un bug en la espera.

## Tiempo estimado

80 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-fake-timers.md).
- El [lab 02](../lab-02-errores-asincronos/README.md) terminado en `~/labs-semana-09`.

## Punto de partida

```bash
cd ~/labs-semana-09
cp -r <ruta-del-bootcamp>/bootcamp/week-09-testing_asincrono_con_jest/2-practicas/lab-03-reintentos-con-fake-timers/starter/. .
git status --short
# ?? src/retry.js
# ?? tests/retry.test.js
```

## Contexto

`src/retry.js` tiene `sendWithRetry(notifier, to, message, { retries, delay })`. Su comentario dice qué tiene que hacer:

| Regla | Con los valores por defecto (`retries: 2`, `delay: 1000`) |
|---|---|
| Si `notifier.send` rechaza, lo vuelve a intentar hasta `retries` veces más | 3 intentos como mucho |
| Antes del primer reintento espera `delay` ms, y el doble antes de cada siguiente | Intentos a los 0, 1000 y 3000 ms |
| Si un intento funciona, se resuelve con lo que devolvió `send` | Sin más intentos |
| Si todos fallan, rechaza con el último error | |

`tests/retry.test.js` tiene un test, con un notificador que falla siempre (`mockRejectedValue`).

---

## Paso 1: un test que espera de verdad (10 min)

```bash
pnpm test tests/retry.test.js --verbose
#       ✓ should try three times and reject with the last error (2013 ms)
pnpm test
# Tests:       12 passed, 12 total
# Time:        2.557 s, estimated 3 s
```

Un test de 2 s en una suite que tardaba medio segundo. Fíjate en el número: según la regla, los tres intentos llegan a los 0, 1000 y 3000 ms. ¿Por qué tarda 2? Apúntalo; lo comprobarás en el paso 4.

## Paso 2: el reloj falso (10 min)

Activa el reloj falso para todos los tests del archivo. Añade al principio del `describe("sendWithRetry")`:

```javascript
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });
```

```bash
pnpm test tests/retry.test.js
# FAIL tests/retry.test.js (5.197 s)
#   sendWithRetry
#     when every attempt fails
#       ✕ should try three times and reject with the last error (5005 ms)
#
#   ● sendWithRetry › when every attempt fails › should try three times and reject with the last error
#
#     thrown: "Exceeded timeout of 5000 ms for a test.
#     Add a timeout value to this test to increase the timeout, if this is a long-running test. See https://jestjs.io/docs/api#testname-fn-timeout."
```

Peor: el reloj falso no avanza solo, y el `setTimeout` del primer reintento no llega nunca. A los 5 s de reloj real, Jest corta el test. Lo arreglarás en el paso 5; antes, los reintentos uno a uno.

## Paso 3: mover el reloj (15 min)

Añade un test, el primero dentro de `when every attempt fails`, que mueva el reloj 1 s y cuente los intentos:

```javascript
    it("should wait 1 second before the first retry", async () => {
      const notifier = failingNotifier();

      sendWithRetry(notifier, TO, MESSAGE);
      jest.advanceTimersByTime(1000);

      expect(notifier.send).toHaveBeenCalledTimes(2);
    });
```

```bash
pnpm test tests/retry.test.js -t "first retry"
# ...
#     Expected number of calls: 2
#     Received number of calls: 1
```

Explica el 1 con la [sección 3 de la teoría 03](../../1-teoria/03-fake-timers.md#3-por-qué-la-versión-async). Cámbialo por la versión `Async`, y mira un milisegundo antes y en el momento exacto:

```javascript
      sendWithRetry(notifier, TO, MESSAGE);
      await jest.advanceTimersByTimeAsync(999);
      expect(notifier.send).toHaveBeenCalledTimes(1);

      await jest.advanceTimersByTimeAsync(1);
      expect(notifier.send).toHaveBeenCalledTimes(2);
```

```bash
pnpm test tests/retry.test.js -t "first retry"
# Tests:       1 skipped, 1 passed, 2 total
```

Este test no espera el resultado de `sendWithRetry`: la promesa sigue pendiente cuando el test termina, y `useRealTimers` descarta los timers que quedaban. No es un `expect` suelto, porque ningún `expect` depende de ella.

## Paso 4: el segundo reintento (15 min)

Escribe el test del segundo reintento, que según la regla llega 2 s después del primero, a los 3000 ms:

```javascript
    it("should wait 2 more seconds before the second retry", async () => {
      const notifier = failingNotifier();

      sendWithRetry(notifier, TO, MESSAGE);
      await jest.advanceTimersByTimeAsync(1000 + 1999);
      expect(notifier.send).toHaveBeenCalledTimes(2);

      await jest.advanceTimersByTimeAsync(1);
      expect(notifier.send).toHaveBeenCalledTimes(3);
    });
```

```bash
pnpm test tests/retry.test.js -t "second retry"
# ...
#   ● sendWithRetry › when every attempt fails › should wait 2 more seconds before the second retry
#
#     expect(jest.fn()).toHaveBeenCalledTimes(expected)
#
#     Expected number of calls: 2
#     Received number of calls: 3
#
#       35 |       sendWithRetry(notifier, TO, MESSAGE);
#       36 |       await jest.advanceTimersByTimeAsync(1000 + 1999);
#     > 37 |       expect(notifier.send).toHaveBeenCalledTimes(2);
# ...
#   ● sendWithRetry › when every attempt fails › should wait 2 more seconds before the second retry
#
#     SMTP no responde
# ...
```

A los 2999 ms ya van tres intentos: la espera no se dobla. Es la respuesta del paso 1: 1 s + 1 s = 2 s. Busca en `src/retry.js` la línea que espera y arréglala:

```javascript
      await wait(delay * 2 ** attempt);
```

`2 ** attempt` es 2 elevado a `attempt`: 1 tras el primer intento, 2 tras el segundo.

El segundo fallo, `SMTP no responde`, es el rechazo de `sendWithRetry` tras el tercer intento: llega mientras el test mueve el reloj, y nadie lo espera. Con el arreglo, sigue saliendo. Es el paso siguiente.

## Paso 5: el rechazo mientras avanza el reloj (20 min)

Arregla primero `should try three times and reject with the last error`, el test del paso 2. Mueve el reloj hasta el final con `runAllTimersAsync` y comprueba después:

```javascript
      const sending = sendWithRetry(notifier, TO, MESSAGE);
      await jest.runAllTimersAsync();

      await expect(sending).rejects.toThrow("SMTP no responde");
      expect(notifier.send).toHaveBeenCalledTimes(3);
```

```bash
pnpm test tests/retry.test.js -t "three times"
# ...
#   ● sendWithRetry › when every attempt fails › should try three times and reject with the last error
#
#     SMTP no responde
# ...
# Tests:       1 failed, 2 skipped, 3 total
```

Es el caso de la [sección 4 de la teoría 03](../../1-teoria/03-fake-timers.md#4-un-rechazo-mientras-avanza-el-reloj): la promesa se rechaza durante `runAllTimersAsync`, antes de que llegue el `.rejects`. Prepara la comprobación antes de mover el reloj:

```javascript
      const rejection = expect(sendWithRetry(notifier, TO, MESSAGE)).rejects.toThrow("SMTP no responde");
      await jest.runAllTimersAsync();

      await rejection;
      expect(notifier.send).toHaveBeenCalledTimes(3);
```

Haz lo mismo en `should wait 2 more seconds before the second retry`: guarda el `expect(...).rejects` en `rejection` en lugar de llamar a `sendWithRetry` sin más, y añade `await rejection;` al final.

```bash
pnpm test tests/retry.test.js --verbose
#       ✓ should wait 1 second before the first retry (11 ms)
#       ✓ should wait 2 more seconds before the second retry (4 ms)
#       ✓ should try three times and reject with the last error (5 ms)
# Tests:       3 passed, 3 total
```

## Paso 6: un reintento que funciona (10 min)

Añade un contexto para cuando el servidor de correo vuelve a responder. Con `mockRejectedValueOnce` y `mockResolvedValueOnce` encadenados, el primer `send` falla y el segundo funciona:

```javascript
  describe("when a retry succeeds", () => {
    it("should resolve with what send returns, without more attempts", async () => {
      const notifier = {
        send: jest.fn().mockRejectedValueOnce(new Error("SMTP no responde")).mockResolvedValueOnce("250 OK"),
      };

      const sending = sendWithRetry(notifier, TO, MESSAGE);
      await jest.runAllTimersAsync();

      await expect(sending).resolves.toBe("250 OK");
      expect(notifier.send).toHaveBeenCalledTimes(2);
    });
  });
```

`250 OK` es la respuesta de un servidor de correo cuando acepta un mensaje. Aquí la promesa se resuelve, así que no hay rechazo que preparar.

---

## Comprobación

```bash
pnpm test
# Tests:       15 passed, 15 total
# Time:        0.619 s, estimated 1 s
grep -cE "advanceTimersByTime[(]|runAllTimers[(]" tests/retry.test.js
# 0
```

La suite vuelve a tardar menos de un segundo, con cuatro tests de reintentos que con el reloj real esperarían 1, 3, 3 y 1 s.

Guarda el lab:

```bash
git add src/retry.js tests/retry.test.js
git commit -m "lab 03: reintentos con fake timers"
```

## Repaso

- [ ] Activas el reloj falso en `beforeEach` y lo devuelves en `afterEach`.
- [ ] Mueves el reloj con la versión `Async`, y sabes por qué la síncrona no ve el reintento.
- [ ] Compruebas el tiempo de una espera un milisegundo antes y en el momento exacto.
- [ ] Si la promesa se rechaza mientras avanza el reloj, preparas el `.rejects` antes de moverlo.
- [ ] Encadenas `mockRejectedValueOnce` y `mockResolvedValueOnce` para un fallo que se arregla solo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Errores asíncronos](../lab-02-errores-asincronos/README.md) | [Proyecto — Proyecto - Suite asíncrona con Jest](../../3-proyecto/README.md) → |

[↑ Semana 09](../../README.md)
