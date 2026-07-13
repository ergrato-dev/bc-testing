# 02 - Validacion de Errores Asincronos en Jest

**Tipo**: JavaScript (Jest)

![Manejo de rejects en Jest](../0-assets/02-manejo-rejects-jest.svg)

---

## Objetivo

Comprobar fallos asincronos sin romper legibilidad ni enmascarar errores, y evitar el falso positivo de un `catch` que nunca se ejecuta.

---

## Por que un error asincrono no se valida como uno sincrono

Un `throw` dentro de una funcion `async` no interrumpe el test como lo haria en codigo sincrono: se convierte en una Promise rechazada. Si el test no la espera con `rejects` o un `try/catch` correctamente instrumentado, el rechazo se pierde y el test pasa igual, haya o no haya fallado la funcion real.

---

## Patron recomendado: `.rejects`

```javascript
test("should reject when payload is invalid", async () => {
  await expect(createTankRecordAsync({})).rejects.toThrow("ValidationError");
});
```

`rejects` es la forma mas directa: en una linea valida que la Promise rechace y que el mensaje o tipo de error coincida.

---

## Alternativa con try/catch + expect.assertions

Cuando necesitas inspeccionar mas de una propiedad del error, `try/catch` da mas control, pero abre una trampa: si la funcion no rechaza, el bloque `catch` nunca corre y el test pasa sin haber validado nada.

```javascript
// MAL: si createTankRecordAsync no rechaza, el test pasa sin ejecutar ningun expect
test("should reject with details", async () => {
  try {
    await createTankRecordAsync({});
  } catch (error) {
    expect(error.message).toContain("ValidationError");
    expect(error.code).toBe(422);
  }
});
```

```javascript
// BIEN: expect.assertions(n) obliga a que se ejecuten exactamente n asserts
test("should reject with details", async () => {
  expect.assertions(2);
  try {
    await createTankRecordAsync({});
  } catch (error) {
    expect(error.message).toContain("ValidationError");
    expect(error.code).toBe(422);
  }
});
```

Si `createTankRecordAsync({})` dejara de rechazar por un bug, la version con `expect.assertions(2)` falla porque se ejecutaron 0 asserts en vez de 2. La version sin esa guarda pasaria en silencio.

---

## Mockeando rechazos con `mockRejectedValue`

```javascript
test("should propagate error from repository", async () => {
  const repository = {
    save: jest.fn().mockRejectedValue(new Error("ConnectionTimeout")),
  };

  await expect(saveSpecies(repository, { name: "otter" })).rejects.toThrow(
    "ConnectionTimeout"
  );
});
```

`mockRejectedValue` simula un fallo de dependencia (red, base de datos) sin depender del servicio real, manteniendo el test rapido y deterministico.

---

## Tabla comparativa

| Enfoque | Cuando usarlo | Riesgo si se omite la guarda |
|---|---|---|
| `await expect(p).rejects.toThrow(...)` | Validar tipo/mensaje en una linea | Bajo: la asercion esta implicita en el matcher |
| `try/catch` sin `expect.assertions` | Evitar este patron | Alto: catch vacio pasa como test exitoso |
| `try/catch` con `expect.assertions(n)` | Validar multiples propiedades del error | Bajo: Jest exige que se ejecuten `n` asserts |
| `jest.fn().mockRejectedValue(err)` | Simular fallo de una dependencia externa | Bajo, si se combina con `rejects` o `assertions` |

---

## Errores frecuentes

- Usar `try/catch` sin `expect.assertions(n)`: el test pasa aunque la promesa nunca rechace.
- Olvidar `await` antes de `expect(promise).rejects`.
- Mockear la dependencia con `mockResolvedValue` cuando el escenario requiere `mockRejectedValue`.
- Validar solo el tipo de error sin revisar el mensaje, ocultando regresiones de contenido.

---

## Regla practica

Si usas `try/catch` para validar un error asincrono, agrega `expect.assertions(n)` al inicio del test. Sin esa guarda, un `catch` que nunca se ejecuta es indistinguible de un test exitoso.
