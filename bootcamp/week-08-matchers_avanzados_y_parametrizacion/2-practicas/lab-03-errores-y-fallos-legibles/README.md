# Lab 03 - Errores y fallos legibles

## Objetivo

Encontrar una fila de una tabla de errores que pasa con cualquier error, hacer que un código de descuento inexistente lance un error con clase y campo, y escribir un matcher propio cuyo fallo explica el problema.

## Tiempo estimado

80 minutos.

## Requisitos

- El [lab 02](../lab-02-matchers-de-objetos-y-arrays/README.md) terminado en `~/labs-semana-08`.
- La [teoría 03](../../1-teoria/03-errores-y-fallos-legibles.md).

## Punto de partida

Otra persona del equipo escribió una tabla con los errores de `createTicket`. Cópiala:

```bash
cd ~/labs-semana-08
cp <ruta-del-bootcamp>/bootcamp/week-08-matchers_avanzados_y_parametrizacion/2-practicas/lab-03-errores-y-fallos-legibles/starter/tests/ticket-errors.test.js tests/
pnpm test
# Tests:       31 passed, 31 total
```

---

## Paso 1: la fila que miente (15 min)

```bash
pnpm test tests/ticket-errors.test.js --verbose
# PASS tests/ticket-errors.test.js
#   createTicket errors
#     when the visitor is invalid
#       ✓ should throw 'El nombre debe tener de 2 a 50 caracteres' for a 1 character name (4 ms)
#       ✓ should throw 'El nombre solo admite letras y espacios' for a digit in the name (9 ms)
#       ✓ should throw '$expected' for age 4 (1 ms)
#       ✓ should throw 'Los grupos escolares son para menores de 18 años' for age 18 in a school group
```

La tercera fila tiene `$expected` sin sustituir. Antes de abrir el archivo, comprueba qué está probando. Cambia el mensaje de la edad en `src/visitor.js` por `"Edad no válida"` y vuelve a ejecutar:

```bash
pnpm test tests/ticket-errors.test.js 2>&1 | grep -E '^Tests:|\$expected'
#       ✓ should throw '$expected' for age 4 (8 ms)
# Tests:       4 passed, 4 total
```

Sigue en verde con un mensaje que no es el de la regla. Abre `tests/ticket-errors.test.js` y busca la fila: el campo se llama `expectd`. El cuerpo recibe `expected` como `undefined`, y `toThrow(undefined)` es `toThrow()`, que pasa con cualquier error.

Corrige el nombre del campo:

```bash
pnpm test tests/ticket-errors.test.js 2>&1 | grep -E '^Tests:|✕|Expected substring|Received message'
#       ✕ should throw 'La edad debe ser un número entero de 5 a 99' for age 4 (8 ms)
#     Expected substring: "La edad debe ser un número entero de 5 a 99"
#     Received message:   "Edad no válida"
# Tests:       1 failed, 3 passed, 4 total
```

Ahora la fila prueba algo. Devuelve el mensaje original a `src/visitor.js`:

```bash
git diff --stat src/
pnpm test
# Tests:       31 passed, 31 total
```

El `git diff` no tiene que mostrar nada.

## Paso 2: un código que no existe (15 min)

¿Qué pasa con un código de descuento que no está en la tabla?

```bash
node -e 'const { createTicket } = require("./src/ticket"); console.log(createTicket({ name: "Ana Ruiz", age: 30 }, { date: "2026-12-05", room: "Cúpula" }, "LUNA").price)'
# NaN
```

`NaN` (*Not a Number*) es el resultado de una operación con números que no tiene sentido: aquí, `9 * undefined`. La entrada se crearía con un precio que no es un número. Tendría que lanzar un error.

Añade un contexto al `describe("createTicket errors")`:

```javascript
  describe("when the discount code does not exist", () => {
    it("should throw instead of returning a price", () => {
      expect(() => createTicket(ana, session, "LUNA")).toThrow("Código de descuento desconocido: LUNA");
    });
  });
```

```bash
pnpm test tests/ticket-errors.test.js
#     expect(received).toThrow(expected)
#
#     Expected substring: "Código de descuento desconocido: LUNA"
#
#     Received function did not throw
# Tests:       1 failed, 4 passed, 5 total
```

«Received function did not throw»: la función terminó sin lanzar nada.

## Paso 3: un error con su campo (20 min)

El formulario de la taquilla tiene que saber qué campo marcar en rojo: el visitante o el código. Un `Error` solo tiene el mensaje. Añade a `src/ticket.js`, después de `let lastNumber = 0;`, una clase de error con un campo `field`:

```javascript
// Error de las entradas: field dice qué dato lo causó ("visitor" o "discountCode").
class TicketError extends Error {
  constructor(message, field) {
    super(message);
    this.name = "TicketError";
    this.field = field;
  }
}
```

Úsala en `createTicket`: el error del visitante pasa a ser `new TicketError(message, "visitor")`, y después de esa comprobación añade la del código. `discountCode in DISCOUNTS` es `true` si el código es un campo del objeto `DISCOUNTS`:

```javascript
  if (discountCode && !(discountCode in DISCOUNTS)) {
    throw new TicketError(`Código de descuento desconocido: ${discountCode}`, "discountCode");
  }
```

Exporta `TicketError` en el `module.exports`, junto a las funciones.

```bash
pnpm test
# Tests:       32 passed, 32 total
```

Los tests de los mensajes siguen pasando: `TicketError` es un `Error` con su mensaje. Ahora comprueba la clase y el campo. Importa `TicketError` en `tests/ticket-errors.test.js` y añade un test en cada contexto:

```javascript
    it("should say that the visitor caused it", () => {
      const visitor = { ...ana, age: 4 };

      expect(() => createTicket(visitor, session)).toThrow(expect.objectContaining({ field: "visitor" }));
    });
```

```javascript
    it("should throw a TicketError caused by the discount code", () => {
      const act = () => createTicket(ana, session, "LUNA");

      expect(act).toThrow(TicketError);
      expect(act).toThrow(expect.objectContaining({ field: "discountCode" }));
    });
```

Guardar la función en `act` evita escribirla dos veces. Prueba a cambiar `"discountCode"` por `"visitor"` en el último test y mira el fallo:

```bash
pnpm test tests/ticket-errors.test.js 2>&1 | grep -E 'Expected asymmetric|Received name|Received message'
#     Expected asymmetric matcher: ObjectContaining {"field": "visitor"}
#     Received name:    "TicketError"
#     Received message: "Código de descuento desconocido: LUNA"
```

Devuélvelo a `"discountCode"`:

```bash
pnpm test
# Tests:       34 passed, 34 total
```

## Paso 4: fallos que se leen (10 min)

Crea un archivo de prueba, `tests/readable.test.js`, con cuatro tests que fallan a propósito: el grupo tiene 2 entradas (9 € y 5 €), y los tests esperan 3 entradas y una de 6 €:

```javascript
const { createGroupTickets } = require("../src/ticket");

const session = { date: "2026-12-05", room: "Cúpula" };
const group = [{ name: "Ana Ruiz", age: 30 }, { name: "Luis Gil", age: 8 }];

describe("readable failures", () => {
  it("length with toBe", () => {
    expect(createGroupTickets(group, session).length).toBe(3);
  });

  it("length with toHaveLength", () => {
    expect(createGroupTickets(group, session)).toHaveLength(3);
  });

  it("includes with toBe", () => {
    const prices = createGroupTickets(group, session).map((ticket) => ticket.price);
    expect(prices.includes(6)).toBe(true);
  });

  it("includes with toContain", () => {
    const prices = createGroupTickets(group, session).map((ticket) => ticket.price);
    expect(prices).toContain(6);
  });
});
```

```bash
pnpm test tests/readable.test.js 2>&1 | grep -E '●|Expected|Received'
#   ● readable failures › length with toBe
#     Expected: 3
#     Received: 2
#   ● readable failures › length with toHaveLength
#     Expected length: 3
#     Received length: 2
#     Received array:  [{"code": "PLAN-20261205-0003", "issuedAt": "2026-11-20T03:55:17.100Z", "price": 9, ...
#   ● readable failures › includes with toBe
#     Expected: true
#     Received: false
#   ● readable failures › includes with toContain
#     Expected value: 6
#     Received array: [9, 5]
```

Compara cada pareja. Solo con el fallo de `toContain`, ¿sabes qué precio falta y cuáles hay? ¿Y con el de `includes`? Apunta en un borrador qué te dice cada uno. Después, borra el archivo:

```bash
rm tests/readable.test.js
```

## Paso 5: un matcher propio (15 min)

Los códigos de entrada aparecen en varios tests, siempre con la misma expresión regular. Crea `tests/matchers.js` con el matcher de la [teoría 03](../../1-teoria/03-errores-y-fallos-legibles.md#4-un-matcher-propio), `toBeTicketCodeFor`.

Dile a Jest que lo cargue antes de cada archivo de tests. En `package.json`, después de `devDependencies` (cuidado con la coma entre los dos):

```json
  "jest": {
    "setupFilesAfterEnv": ["<rootDir>/tests/matchers.js"]
  }
```

En `tests/ticket.test.js`, usa el matcher en lugar de la expresión regular, y añade un test en `when there is a discount code`:

```javascript
        code: expect.toBeTicketCodeFor("2026-12-05"),
```

```javascript
    it("should use the session date in the code", () => {
      const ticket = createTicket(ana, session, "ESTRELLA");

      expect(ticket.code).toBeTicketCodeFor("2026-12-05");
    });
```

Cambia la fecha del último test por `"2026-12-06"` para ver el mensaje:

```bash
pnpm test tests/ticket.test.js 2>&1 | grep -A2 "●"
#   ● createTicket › when there is a discount code › should use the session date in the code
#
#     expected "PLAN-20261205-0003" to be a ticket code for 2026-12-06 (PLAN-20261206-NNNN)
```

El número de la entrada puede ser otro: depende de cuántas se crearon antes. Devuelve la fecha a `"2026-12-05"`.

---

## Comprobación

```bash
pnpm test
# Tests:       35 passed, 35 total
pnpm test --randomize --seed=1
# Tests:       35 passed, 35 total
pnpm test --randomize --seed=2
# Tests:       35 passed, 35 total
grep -rn "toThrow()" tests/
```

El `grep` no muestra nada. Guarda el lab:

```bash
git add package.json src tests
git commit -m "lab 03: errores y fallos legibles"
```

## Repaso

- [ ] Sabes por qué una fila con `expectd` pasaba con cualquier error, y cómo lo delató el nombre.
- [ ] Un código de descuento que no existe lanza un `TicketError` con `field`, y lo comprueba un test.
- [ ] Sabes comprobar la clase de un error y sus campos con `toThrow`.
- [ ] Sabes qué matcher da un fallo que se entiende sin abrir el código.
- [ ] Tu matcher propio se carga con `setupFilesAfterEnv` y funciona también dentro de `toStrictEqual`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Matchers de objetos y arrays](../lab-02-matchers-de-objetos-y-arrays/README.md) | [Proyecto — Proyecto - Suite parametrizada con Jest](../../3-proyecto/README.md) → |

[↑ Semana 08](../../README.md)
