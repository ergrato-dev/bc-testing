# Lab 02 - Matchers de objetos y arrays

## Objetivo

Probar las entradas del planetario, que tienen una hora y un contador que cambian en cada ejecución, y encontrar con `toStrictEqual` un campo de más que `toEqual` y `toMatchObject` dejan pasar.

## Tiempo estimado

80 minutos.

## Requisitos

- El [lab 01](../lab-01-de-tests-repetidos-a-tablas/README.md) terminado en `~/labs-semana-08`.
- La [teoría 02](../../1-teoria/02-matchers-de-objetos-y-arrays.md).

## Punto de partida

```bash
cd ~/labs-semana-08
cp -r <ruta-del-bootcamp>/bootcamp/week-08-matchers_avanzados_y_parametrizacion/2-practicas/lab-02-matchers-de-objetos-y-arrays/starter/. .
git status --short
# ?? src/ticket.js
pnpm test
# Tests:       19 passed, 19 total
```

## Contexto

`src/ticket.js` crea las entradas del planetario. Tiene tres funciones:

| Función | Qué hace |
|---|---|
| `createTicket(visitor, session, discountCode)` | Valida el visitante con `validateVisitor` y devuelve su entrada. `discountCode` es opcional |
| `createGroupTickets(visitors, session)` | Una entrada por visitante, sin descuento |
| `formatReceipt(ticket)` | El texto del recibo: una línea por campo de la entrada |

Los precios: 5 € hasta los 11 años, 6 € desde los 65 y 9 € el resto. El código `ESTRELLA` descuenta un 10 % y `COMETA` un 25 %.

---

## Paso 1: una entrada (5 min)

Mira lo que devuelve `createTicket`:

```bash
node -e 'const { createTicket } = require("./src/ticket"); console.log(createTicket({ name: "Ana Ruiz", age: 30, schoolGroup: false }, { date: "2026-12-05", room: "Cúpula" }))'
# {
#   code: 'PLAN-20261205-0001',
#   visitor: { name: 'Ana Ruiz', age: 30 },
#   session: { date: '2026-12-05', room: 'Cúpula' },
#   price: 9,
#   discount: undefined,
#   issuedAt: '2026-11-20T03:52:56.939Z'
# }
```

`node -e` ejecuta el código JavaScript que va entre comillas. Ejecútalo dos veces: `issuedAt` cambia, porque es la hora en que se creó la entrada (en UTC, por eso la `Z`).

## Paso 2: el objeto entero (15 min)

Crea `tests/ticket.test.js` con un test que compara la entrada entera con `toEqual`, como en la semana 3:

```javascript
const { createTicket } = require("../src/ticket");

const ana = { name: "Ana Ruiz", age: 30, schoolGroup: false };
const session = { date: "2026-12-05", room: "Cúpula" };

describe("createTicket", () => {
  describe("when there is no discount code", () => {
    it("should return the full ticket", () => {
      const ticket = createTicket(ana, session);

      expect(ticket).toEqual({
        code: "PLAN-20261205-0001",
        visitor: { name: "Ana Ruiz", age: 30 },
        session: { date: "2026-12-05", room: "Cúpula" },
        price: 9,
        issuedAt: "2026-11-19T10:00:00.000Z",
      });
    });
  });
});
```

```bash
pnpm test tests/ticket.test.js
#     expect(received).toEqual(expected) // deep equality
#
#     - Expected  - 1
#     + Received  + 2
#
#     @@ -1,8 +1,9 @@
#       Object {
#         "code": "PLAN-20261205-0001",
#     -   "issuedAt": "2026-11-19T10:00:00.000Z",
#     +   "discount": undefined,
#     +   "issuedAt": "2026-11-20T03:53:14.298Z",
#         "price": 9,
```

El diff tiene dos líneas `+`, pero solo una es la causa del fallo. `toEqual` ignora los campos con `undefined`: si arreglas la hora, el test pasará, con `discount` y todo. Jest escribe el diff completo para que veas los dos objetos.

Cambia la hora por un matcher asimétrico:

```javascript
        issuedAt: expect.any(String),
```

```bash
pnpm test tests/ticket.test.js
# Tests:       1 passed, 1 total
```

## Paso 3: el contador (10 min)

Añade al final del archivo otro test, en un `describe` aparte:

```javascript
describe("createTicket again", () => {
  it("should number the second ticket", () => {
    const ticket = createTicket(ana, session);

    expect(ticket.code).toBe("PLAN-20261205-0001");
  });
});
```

```bash
pnpm test tests/ticket.test.js
#     Expected: "PLAN-20261205-0001"
#     Received: "PLAN-20261205-0002"
# Tests:       1 failed, 1 passed, 2 total
pnpm test tests/ticket.test.js -t "second"
# Tests:       1 skipped, 1 passed, 2 total
```

Solo pasa; con otro test delante, falla. Es el estado compartido de la [semana 7](../../../week-07-jest_avanzado_y_organizacion_de_suites/1-teoria/02-hooks-y-aislamiento.md): `lastNumber` es una variable del módulo y no vuelve a 0 entre tests, ni con un `beforeEach`.

Borra el `describe("createTicket again")` y, en el primer test, cambia el código fijo por un patrón:

```javascript
        code: expect.stringMatching(/^PLAN-20261205-\d{4}$/),
```

El test ya no depende del orden: comprueba la fecha de la sesión y cuatro dígitos, sean los que sean.

## Paso 4: el campo de más (20 min)

Cambia el nombre del test por `should return the ticket without a discount field`, y añade dos tests: otro en el mismo contexto y uno para el recibo, en un `describe` nuevo al final del archivo:

```javascript
    it("should have the full price", () => {
      const ticket = createTicket(ana, session);

      expect(ticket).toMatchObject({ price: 9 });
    });
```

```javascript
describe("formatReceipt", () => {
  describe("when there is no discount code", () => {
    it("should not print a discount line", () => {
      const receipt = formatReceipt(createTicket(ana, session));

      expect(receipt).not.toContain("discount");
    });
  });
});
```

Añade `formatReceipt` al `require` de la primera línea y ejecuta:

```bash
pnpm test tests/ticket.test.js
#   ● formatReceipt › when there is no discount code › should not print a discount line
#
#     expect(received).not.toContain(expected) // indexOf
#
#     Expected substring: not "discount"
#     Received string:        "code: PLAN-20261205-0003
#     ...
#     price: 9
#     discount: undefined
#     issuedAt: 2026-11-20T03:54:05.751Z"
#
# Tests:       1 failed, 2 passed, 3 total
```

Un visitante sin descuento recibe un recibo con la línea `discount: undefined`. Los otros dos tests pasan: `toEqual` ignora el campo, y `toMatchObject({ price: 9 })` no lo mira.

Cambia `toEqual` por `toStrictEqual` en el primer test:

```bash
pnpm test tests/ticket.test.js
#     expect(received).toStrictEqual(expected) // deep equality
#
#     - Expected  - 0
#     + Received  + 1
#
#     @@ -1,7 +1,8 @@
#       Object {
#         "code": StringMatching /^PLAN-20261205-\d{4}$/,
#     +   "discount": undefined,
#         "issuedAt": Any<String>,
#         "price": 9,
#
# Tests:       2 failed, 1 passed, 3 total
```

Ahora el test de la entrada también lo ve. Arregla `createTicket`: sin código, la entrada no lleva el campo `discount`. Cuando lo tengas, el final de la función queda así:

```javascript
  const ticket = {
    code: `PLAN-${session.date.replaceAll("-", "")}-${String(lastNumber).padStart(4, "0")}`,
    visitor: { name: visitor.name, age: visitor.age },
    session: { date: session.date, room: session.room },
    price: basePrice - (basePrice * percent) / 100,
    issuedAt: new Date().toISOString(),
  };
  // Sin código, el ticket no lleva el campo discount: ni siquiera con undefined.
  if (discountCode) {
    ticket.discount = { code: discountCode, percent };
  }
  return ticket;
```

```bash
pnpm test tests/ticket.test.js
# Tests:       3 passed, 3 total
```

Borra el test `should have the full price`: el primero ya comprueba el precio, y además todo lo demás.

## Paso 5: el descuento (10 min)

Un contexto nuevo en `describe("createTicket")`, `when there is a discount code`, con dos tests que miran solo una parte de la entrada:

```javascript
    it("should apply the discount and keep the code", () => {
      const ticket = createTicket(ana, session, "COMETA");

      expect(ticket).toMatchObject({ price: 6.75, discount: { code: "COMETA", percent: 25 } });
    });

    it("should keep the session room", () => {
      const ticket = createTicket(ana, session, "ESTRELLA");

      expect(ticket).toHaveProperty("session.room", "Cúpula");
    });
```

Aquí `toMatchObject` es la elección correcta: el test trata del descuento, y el resto de la entrada ya lo comprueba el primer test.

## Paso 6: grupos (20 min)

Añade `createGroupTickets` al `require` y un `describe` para él, con tres visitantes y un `beforeEach`:

```javascript
const luis = { name: "Luis Gil", age: 8, schoolGroup: false };
const marta = { name: "Marta Sanz", age: 70, schoolGroup: false };
```

```javascript
describe("createGroupTickets", () => {
  describe("when the group has three visitors", () => {
    let tickets;

    beforeEach(() => {
      tickets = createGroupTickets([ana, luis, marta], session);
    });

    it("should return one ticket per visitor", () => {
      expect(tickets).toHaveLength(3);
    });

    it("should charge the child price to the child", () => {
      expect(tickets).toContainEqual(
        expect.objectContaining({ visitor: { name: "Luis Gil", age: 8 }, price: 5 }),
      );
    });
  });
});
```

Las constantes de `luis` y `marta` van arriba, junto a `ana`. Escribe tú otros dos tests en el mismo contexto:

- `should give each ticket a different code`: con `tickets.map((ticket) => ticket.code)` sacas los códigos. `new Set(codes)` quita los repetidos, y su `.size` es el número de códigos distintos.
- `should charge the three prices of the table`: los precios, comprobados con `expect.arrayContaining([5, 6, 9])` dentro de un `toEqual`.

```bash
pnpm test
# Tests:       27 passed, 27 total
```

---

## Comprobación

```bash
pnpm test tests/ticket.test.js --verbose
#   createTicket
#     when there is no discount code
#       ✓ should return the ticket without a discount field (3 ms)
#     when there is a discount code
#       ✓ should apply the discount and keep the code
#       ✓ should keep the session room (1 ms)
#   createGroupTickets
#     when the group has three visitors
#       ✓ should return one ticket per visitor (1 ms)
#       ✓ should charge the child price to the child
#       ✓ should give each ticket a different code (1 ms)
#       ✓ should charge the three prices of the table (1 ms)
#   formatReceipt
#     when there is no discount code
#       ✓ should not print a discount line
#
# Tests:       8 passed, 8 total
```

Guarda el lab:

```bash
git add src tests
git commit -m "lab 02: matchers de objetos y arrays"
```

## Repaso

- [ ] Sabes por qué `toEqual` y `toMatchObject` no vieron el campo `discount: undefined`, y `toStrictEqual` sí.
- [ ] La hora y el código se comprueban con `expect.any` y `expect.stringMatching`, no con un valor fijo.
- [ ] Sabes por qué un test que espera `PLAN-20261205-0001` pasa solo y falla en grupo.
- [ ] Usas `toMatchObject` cuando el test trata de unos campos, y `toStrictEqual` para el objeto entero.
- [ ] Comprobaste un array con `toHaveLength`, `toContainEqual` y `expect.arrayContaining`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - De tests repetidos a tablas](../lab-01-de-tests-repetidos-a-tablas/README.md) | [Lab 03 - Errores y fallos legibles](../lab-03-errores-y-fallos-legibles/README.md) → |

[↑ Semana 08](../../README.md)
