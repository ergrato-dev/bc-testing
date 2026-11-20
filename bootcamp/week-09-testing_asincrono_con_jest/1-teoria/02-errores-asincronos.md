# 02 - Errores asíncronos

> Cómo comprobar que una función asíncrona rechaza con el error correcto, y los dos tests que pasan sin comprobar nada.

---

## 🎯 Objetivos

- Explicar por qué `expect(() => f()).toThrow()` no ve el error de una función `async`.
- Comprobar un rechazo con `await expect(promesa).rejects` y los matchers de la semana 8.
- Reconocer un `try/catch` que pasa sin comprobar nada y arreglarlo con `expect.assertions` o con `.rejects`.
- Simular una dependencia que falla con `mockRejectedValue`.

---

## 1. Una función `async` no lanza

En la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/03-matchers-y-ejecucion.md) comprobaste los errores así: `expect(() => validateVisitor(...)).toThrow(...)`. `toThrow` llama a la función y mira si lanza.

Una función `async` no lanza nunca: su `throw` rechaza la promesa que devuelve. Para `toThrow`, la función terminó bien y devolvió algo:

```javascript
it("should reject the id", () => {
  expect(() => service.cancel(99)).toThrow("La reserva no existe");
});
```

```bash
pnpm test tests/booking-errors.test.js
#   ● createBookingService errors › cancel › when the booking does not exist › should reject the id
#
#     expect(received).toThrow(expected)
#
#     Expected substring: "La reserva no existe"
#
#     Received function did not throw
# ...
# Ran all test suites matching tests/booking-errors.test.js.
# /home/tu-usuario/labs-semana-09/src/booking.js:46
#       throw new BookingError("La reserva no existe", "NOT_FOUND");
#             ^
#
# BookingError: La reserva no existe
#   code: 'NOT_FOUND'
# }
```

Dos fallos por el mismo motivo: `toThrow` dice que la función no lanzó, y después del resumen aparece el rechazo que nadie esperó, el mismo caso de la [teoría 01](01-esperar-a-las-promesas.md#2-el-test-no-espera-solo).

---

## 2. `.rejects`

`expect(promesa).rejects` espera a que la promesa termine y aplica el matcher al error con que se rechazó. Si la promesa se resuelve, falla:

```javascript
it("should reject the id", async () => {
  await expect(service.cancel(99)).rejects.toThrow("La reserva no existe");
});
```

Fíjate en dos cambios: `expect` recibe la promesa (`service.cancel(99)`), no una función que la crea, y lleva `await` delante. Detrás de `.rejects` valen los matchers de la [semana 8](../../week-08-matchers_avanzados_y_parametrizacion/1-teoria/03-errores-y-fallos-legibles.md):

| Lo que compruebas | Matcher |
|---|---|
| El mensaje | `.rejects.toThrow("La reserva no existe")` |
| La clase del error | `.rejects.toThrow(BookingError)` |
| Un campo del error | `.rejects.toMatchObject({ code: "NOT_FOUND" })` |
| Un campo dentro de otro | `.rejects.toHaveProperty("cause.message", "ECONNRESET")` |

Cada `expect(service.cancel(99))` vuelve a llamar a `cancel`: si compruebas el mensaje y el código, son dos llamadas.

Si la promesa se resuelve, el fallo dice con qué valor:

```text
    expect(received).rejects.toThrow()

    Received promise resolved instead of rejected
    Resolved to value: null
```

---

## 3. El `try/catch` que no comprueba nada

Otra forma de comprobar un rechazo es esperarlo dentro de un `try` y mirar el error en el `catch`:

```javascript
it("should reject with the box office message", async () => {
  boxOffice.release = jest.fn().mockRejectedValue(new Error("ECONNRESET"));

  try {
    await service.cancel(1);
  } catch (error) {
    expect(error.message).toBe("La taquilla no responde");
  }
});
```

Si `cancel` no rechaza, el `catch` no se ejecuta, el test no llega a ningún `expect` y **pasa**. Es el falso positivo más callado de esta semana: en el lab 02, `cancel` se traga el error de la taquilla y devuelve `null`, y este test sale en verde.

`expect.assertions(n)` dice a Jest cuántos `expect` tiene que ejecutar el test. Si se ejecutan menos, falla:

```text
    expect.assertions(1)

    Expected one assertion to be called but received zero assertion calls.
```

Arregla el falso positivo, pero `.rejects` lo hace sin una línea más y sin `try`. El curso usa `.rejects`; si encuentras un `try/catch` en un test, ponle `expect.assertions` o reescríbelo.

---

## 4. Una dependencia que falla

En la [semana 7](../../week-07-jest_avanzado_y_organizacion_de_suites/1-teoria/03-dobles-de-prueba-basicos.md) un stub devolvía una respuesta fija con `mockReturnValue`. Para un colaborador asíncrono, `jest.fn()` tiene versiones que devuelven promesas:

| Método | Cada llamada devuelve |
|---|---|
| `.mockResolvedValue(valor)` | Una promesa que se resuelve con `valor` |
| `.mockRejectedValue(error)` | Una promesa que se rechaza con `error` |
| `.mockResolvedValueOnce(valor)` y `.mockRejectedValueOnce(error)` | Lo mismo, solo en la siguiente llamada. Se encadenan: la primera llamada falla y la segunda funciona |

```javascript
boxOffice.release = jest.fn().mockRejectedValue(new Error("ECONNRESET"));
```

Con esa línea, la taquilla falla como si se cortara la red, sin red. `ECONNRESET` es el código que da Node cuando el otro lado corta la conexión.

### El error original, en `cause`

Cuando `cancel` recibe ese error, rechaza con uno suyo, que dice qué pasó en palabras del planetario, y guarda el original en `cause`:

```javascript
throw new BookingError("La taquilla no responde", "BOX_OFFICE_DOWN", { cause: error });
```

`{ cause: error }` es una opción de `Error` desde ES2022: el error nuevo lleva dentro el que lo provocó, para quien tenga que investigar. Compruébalo con `toHaveProperty`:

```javascript
await expect(service.cancel(1)).rejects.toHaveProperty("cause.message", "ECONNRESET");
```

```text
    expect(received).rejects.toHaveProperty(path, value)

    Expected path: "cause.message"

    Expected value: "ETIMEDOUT"
    Received value: "ECONNRESET"
```

`toMatchObject({ cause: new Error("ECONNRESET") })` también pasa, pero cuando falla, su diff engaña: `cause` es una propiedad no enumerable, que Jest no muestra, y el diff parece decir que falta:

```text
      Object {
    -   "cause": [Error: ETIMEDOUT],
        "code": "BOX_OFFICE_DOWN",
      }
```

---

## ✅ Resumen

- Una función `async` no lanza: rechaza su promesa. `expect(() => f()).toThrow()` dice «did not throw».
- Comprueba los rechazos con `await expect(promesa).rejects` y el matcher que diga qué esperas: `toThrow`, `toMatchObject`, `toHaveProperty`.
- Un `try/catch` sin `expect.assertions` pasa si no hay error. Usa `.rejects`.
- `mockResolvedValue` y `mockRejectedValue` son stubs de colaboradores asíncronos; las versiones `Once` se encadenan.
- Un error que envuelve a otro lo guarda en `cause`; compruébalo con `toHaveProperty("cause.message", ...)`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Esperar a las promesas](01-esperar-a-las-promesas.md) | [Teoría — 03 - Fake timers: controlar el tiempo](03-fake-timers.md) → |

[↑ Semana 09](../README.md)
