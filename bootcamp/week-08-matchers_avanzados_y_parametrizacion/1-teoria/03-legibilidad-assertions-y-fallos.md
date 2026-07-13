# 03 - Legibilidad de Assertions y Analisis de Fallos

**Tipo**: JavaScript (Jest)

![Analisis de fallo en Jest](../0-assets/05-analisis-fallo-jest.svg)

---

## Objetivo

Escribir assertions faciles de entender y diagnosticar cuando fallan, para que un fallo en CI se resuelva en minutos, no en una investigacion larga.

---

## Principios

- Un test debe tener una intencion principal.
- Evita mezclar muchas validaciones no relacionadas en un solo `test`.
- Usa mensajes y nombres de test orientados a comportamiento, no a implementacion.

---

## Assertion pobre vs assertion clara

Mala: valida "algo paso" sin decir que exactamente se esperaba.

```javascript
test("should work", () => {
  const result = registerVisit("Museo Aeroespacial", 3);
  expect(result).toBeTruthy();
});
```

Si esto falla, el reporte solo dice que `result` fue falsy. No hay pista de que campo esta mal.

Buena: nombra el comportamiento y usa un matcher especifico.

```javascript
test("should register a visit with the correct visitor count", () => {
  const result = registerVisit("Museo Aeroespacial", 3);

  expect(result).toMatchObject({ site: "Museo Aeroespacial", visitors: 3 });
});
```

Si falla, Jest muestra exactamente que propiedad no coincide, y el nombre del test ya describe la intencion.

---

## Como leer un diff de fallo en Jest

Ante un fallo, Jest imprime tres bloques clave:

```
expect(received).toEqual(expected)

- Expected
+ Received

  Object {
-   "visitors": 3,
+   "visitors": 2,
    "site": "Museo Aeroespacial",
  }
```

1. **Linea del matcher**: identifica que comparacion fallo (`toEqual`, `toBe`, etc).
2. **`- Expected` / `+ Received`**: lo esperado va con `-`, lo real con `+`; se lee como un diff de git.
3. **Propiedad marcada**: solo la linea que difiere trae el signo; el resto del objeto se muestra igual para dar contexto.

La causa casi siempre esta en la linea marcada, no en todo el bloque: revisa primero que produjo ese valor antes de tocar el test.

---

## Assertion smells (senales de mal diseno)

| Smell | Sintoma | Correccion |
|---|---|---|
| Asserta demasiado | Un test valida 6+ propiedades sin relacion | Dividir en tests por comportamiento |
| Asserta muy poco | Solo `toBeDefined()` o `toBeTruthy()` | Usar matcher especifico (`toEqual`, `toHaveProperty`) |
| Assertion oculta en un helper | El fallo no dice que helper la genero | Mover el `expect` al cuerpo del test |
| Test sin nombre de comportamiento | `test("caso 1", ...)` | Nombrar segun el resultado esperado |
| Multiples act antes de un solo assert | Dificulta saber que accion causo el fallo | Un `act` por test, o parametrizar |

---

## Estrategias

1. Separar escenarios por comportamiento: un test, una razon para fallar.
2. Preferir matchers especificos antes que validaciones genericas (`toBeTruthy`, `toBeDefined`).
3. Revisar el diff del error para corregir causa raiz, no sintomas: no cambies el `expected` solo para que pase.
4. Si un test necesita 3+ asserts distintos para tener sentido, evalua si en realidad son 3 tests.

---

## Nota sobre custom matcher

Jest permite extender `expect` para casos recurrentes de negocio, cuando un mismo assertion complejo se repite en muchos archivos.

```javascript
expect.extend({
  toBeValidTicket(received) {
    const pass = typeof received.id === "string" && received.type !== undefined;
    return {
      pass,
      message: () => `expected ${JSON.stringify(received)} to be a valid ticket`,
    };
  },
});

expect(generateTicket("general")).toBeValidTicket();
```

---

## Regla practica

Si al leer el nombre del test y el mensaje de fallo no puedes adivinar que se rompio sin abrir el codigo fuente, la assertion necesita mas precision.

![Concepto de custom matcher](../0-assets/04-custom-matcher-concepto.svg)
