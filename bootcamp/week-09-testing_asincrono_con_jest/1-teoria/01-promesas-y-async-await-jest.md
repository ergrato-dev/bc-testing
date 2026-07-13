# 01 - Promesas y Async/Await en Testing con Jest

**Tipo**: JavaScript (Jest)

![Flujo async/await en Jest](../0-assets/01-flujo-async-await-jest.svg)

---

## Objetivo

Asegurar que cada test espere correctamente la operacion asincrona antes de validar, y reconocer el falso positivo mas comun de testing asincrono: un test que "pasa" sin haber ejecutado ningun assert.

---

## Por que Jest necesita ayuda con lo asincrono

Jest ejecuta cada test de forma sincrona salvo que le indiques lo contrario. Si una funcion retorna una Promise y el test no la espera, Jest da el test por finalizado antes de que la Promise se resuelva o rechace. El resultado: un test verde que nunca corrio sus expects.

Hay dos formas validas de indicarle a Jest que espere:

1. Marcar el test como `async` y usar `await` dentro.
2. Retornar la Promise directamente desde la funcion de test.

---

## Patrones correctos

### async/await

```javascript
test("should resolve show data when id exists", async () => {
  const result = await getShowById(1);
  expect(result).toMatchObject({ id: 1 });
});
```

### Matcher `.resolves`

```javascript
test("should resolve show data with resolves helper", async () => {
  await expect(getShowById(1)).resolves.toMatchObject({ id: 1 });
});
```

`.resolves` desenvuelve la Promise y aplica el matcher al valor resuelto. Sigue necesitando `await` (o `return`) porque en si mismo tambien es asincrono.

---

## El falso positivo clasico: olvidar `return`/`await`

```javascript
// MAL: el test termina antes de que la Promise se resuelva
test("should reject invalid reservation", () => {
  getReservationById(999).catch((error) => {
    expect(error.message).toBe("NotFound");
  });
  // Jest no espera este catch: el test pasa aunque el expect nunca corra
});
```

```javascript
// BIEN: Jest espera la resolucion antes de cerrar el test
test("should reject invalid reservation", async () => {
  await expect(getReservationById(999)).rejects.toThrow("NotFound");
});
```

Si borras el `expect` de adentro del `.catch()` mal escrito, el test sigue pasando: esa es la señal de alarma. Un test asincrono que "no puede fallar" casi siempre tiene una Promise sin esperar.

---

## Tabla comparativa

| Forma | Cuando usarla | Riesgo si se omite `await`/`return` |
|---|---|---|
| `async/await` | Flujo con multiples pasos asincronos | Test pasa sin ejecutar asserts |
| `return promise.then(...)` | Un solo paso, sin necesidad de `async` | Test pasa sin ejecutar asserts |
| `await expect(promise).resolves` | Validar valor resuelto en una linea | Test pasa sin ejecutar asserts |
| `await expect(promise).rejects` | Validar rechazo en una linea | Test pasa aunque nunca rechace |

---

## Ejemplo de dominio: reservas de Planetario

```javascript
async function getShowById(id) {
  const show = SHOWS.find((s) => s.id === id);
  if (!show) throw new Error("NotFound");
  return show;
}

test("should list available planetarium shows", async () => {
  const shows = await listAvailableShows();
  expect(shows.length).toBeGreaterThan(0);
});

test("should confirm reservation with resolves", async () => {
  await expect(reserveSeat(1, "12:00")).resolves.toMatchObject({
    status: "confirmed",
  });
});
```

---

## Errores frecuentes

- Llamar a una funcion async sin `await` ni `return` dentro del test.
- Poner el `expect` dentro de un `.then()`/`.catch()` que el test no espera.
- Mezclar callback (`done`) con `async/await` en el mismo test.
- Usar `.resolves`/`.rejects` sin `await` delante del `expect`.

---

## Regla practica

Si una funcion retorna una Promise, el test debe tener `async` + `await`, o `return` la Promise. Sin una de las dos, Jest no garantiza que los asserts se ejecuten antes de marcar el test como exitoso.
