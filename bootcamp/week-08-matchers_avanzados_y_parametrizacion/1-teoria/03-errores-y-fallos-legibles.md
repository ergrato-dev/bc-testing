# 03 - Errores y fallos que se leen

> Cómo comprobar exactamente qué error se lanza, y cómo escribir assertions cuyo fallo explica el problema sin abrir el código.

---

## 🎯 Objetivos

- Saber qué compara `toThrow` según lo que le pases, y qué formas pasan con cualquier error.
- Comprobar la clase de un error y sus campos.
- Elegir el matcher cuyo mensaje de fallo dice lo que pasó.
- Escribir un matcher propio con `expect.extend`.

---

## 1. Lo que compara `toThrow`

En la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/03-matchers-y-ejecucion.md#tothrow-errores) usaste `toThrow` con el mensaje. Acepta más cosas, y cada una compara algo distinto:

| Escribes | Pasa si el error... |
|---|---|
| `toThrow("Código de descuento")` | Tiene un mensaje que **contiene** ese texto |
| `toThrow(/^Código de descuento desconocido: LUNA$/)` | Tiene un mensaje que cumple la expresión regular |
| `toThrow(TicketError)` | Es de esa clase (o de una que hereda de ella) |
| `toThrow(expect.objectContaining({ field: "visitor" }))` | Tiene esos campos |
| `toThrow()` | Es cualquier error |

Con un texto, `toThrow` busca un trozo, no el mensaje entero. Por eso `toThrow("")` pasa con cualquier error: el texto vacío está dentro de todos los mensajes.

Hay una forma de llegar a `toThrow()` sin escribirla. En una tabla con un campo mal escrito, la fila no tiene `expected`, el cuerpo recibe `undefined`, y `toThrow(undefined)` es `toThrow()`:

```javascript
{ what: "age 4", change: { age: 4 }, expectd: "La edad debe ser un número entero de 5 a 99" },
```

```bash
pnpm test tests/ticket-errors.test.js --verbose
#       ✓ should throw '$expected' for age 4 (1 ms)
```

El test pasa aunque cambie el mensaje de la edad: pasaría con cualquier error. Lo delata el nombre, con el `$expected` sin sustituir de la [teoría 01](01-tablas-de-casos-con-each.md#6-dos-trampas-del-nombre). Con el campo bien escrito y el mensaje cambiado, el test falla:

```text
    Expected substring: "La edad debe ser un número entero de 5 a 99"
    Received message:   "Edad no válida"
```

Escribe siempre el mensaje completo y, si pueden ser varios, ponlos en constantes, como en el lab 01.

---

## 2. Errores con clase y campos

Un `Error` solo tiene un mensaje. Si el código que llama necesita saber qué dato causó el error (por ejemplo, para marcar el campo del formulario), el error puede llevarlo en un campo propio:

```javascript
class TicketError extends Error {
  constructor(message, field) {
    super(message);
    this.name = "TicketError";
    this.field = field;
  }
}

throw new TicketError("Código de descuento desconocido: LUNA", "discountCode");
```

`extends Error` hace que `TicketError` sea un error como los demás, y `super(message)` le pone el mensaje. El test comprueba cada parte:

```javascript
const act = () => createTicket(ana, session, "LUNA");

expect(act).toThrow(TicketError);
expect(act).toThrow(expect.objectContaining({ field: "discountCode" }));
```

Si el campo no coincide (aquí el test espera `"visitor"` para un código de descuento), el fallo dice qué esperaba y qué error llegó:

```text
    Expected asymmetric matcher: ObjectContaining {"field": "visitor"}
    Received name:    "TicketError"
    Received message: "Código de descuento desconocido: LUNA"
```

`Received name` es el campo `name` del error: por eso el constructor pone `this.name = "TicketError"`. Sin esa línea diría `"Error"`.

---

## 3. Un fallo que explica el problema

Un test en rojo tiene que decir qué falla sin abrir el código. Compara estos fallos: los mismos datos (un grupo de 2 entradas), con un `expect` distinto:

| `expect` | Lo que dice el fallo |
|---|---|
| `expect(tickets.length).toBe(3)` | `Expected: 3`, `Received: 2` |
| `expect(tickets).toHaveLength(3)` | `Expected length: 3`, `Received length: 2` y `Received array:` con las dos entradas |
| `expect(prices.includes(6)).toBe(true)` | `Expected: true`, `Received: false` |
| `expect(prices).toContain(6)` | `Expected value: 6`, `Received array: [9, 5]` |

El de `includes` es el peor: `true` y `false` no dicen qué precio faltaba ni cuáles había. Con `toContain`, el fallo ya dice que el grupo no tiene ninguna entrada de jubilado (6 €).

La regla: **el valor que compruebas va en `expect()`, y el matcher dice cómo**. Si `expect()` recibe un `true` o un número que calculaste tú, el fallo pierde todo lo demás.

En una tabla, el fallo señala la línea del cuerpo, que es la misma para todas las filas. Lo que dice qué fila falló es el nombre. Con el límite de la edad mal puesto (`age > 98` en lugar de `age > 99`):

```text
  ● validateVisitor › age › when the age is valid › should register the visitor when the age is 99
    Expected: "Visitante registrado"
    Received: "La edad debe ser un número entero de 5 a 99"
    > 71 |         expect(result).toBe(REGISTERED);
```

Un nombre como `should register the visitor (%#)` diría «fila 4», y habría que contar filas.

---

## 4. Un matcher propio

Si un mismo `expect` aparece en muchos tests, o su fallo no se entiende, puedes escribir tu matcher con `expect.extend`. Recibe el valor y devuelve si pasa y el mensaje de fallo:

```javascript
// tests/matchers.js
expect.extend({
  // Un código de entrada para la sesión de esa fecha: PLAN-AAAAMMDD-NNNN.
  toBeTicketCodeFor(received, date) {
    const prefix = `PLAN-${date.replaceAll("-", "")}-`;
    const pass = typeof received === "string" && new RegExp(`^${prefix}\\d{4}$`).test(received);
    return {
      pass,
      message: () =>
        `expected ${this.utils.printReceived(received)} ${pass ? "not " : ""}to be a ticket code for ${date} (${prefix}NNNN)`,
    };
  },
});
```

| Parte | Qué es |
|---|---|
| `received` | Lo que va en `expect(...)` |
| `date` | Lo que va en `.toBeTicketCodeFor(...)` |
| `pass` | `true` si se cumple |
| `message` | Una función que devuelve el texto del fallo. Con `.not`, Jest la llama cuando `pass` es `true`; por eso el texto cambia con `pass` |
| `this.utils.printReceived` | Escribe el valor como lo hace Jest, con comillas y color |

Para que todos los archivos de tests tengan el matcher, `package.json` le dice a Jest que cargue el archivo antes de cada uno:

```json
"jest": {
  "setupFilesAfterEnv": ["<rootDir>/tests/matchers.js"]
}
```

`<rootDir>` es la carpeta de `package.json`. `matchers.js` no termina en `.test.js`, así que Jest no lo ejecuta como un archivo de tests. Se usa como cualquier matcher, y también como asimétrico:

```javascript
expect(ticket.code).toBeTicketCodeFor("2026-12-05");
expect(ticket).toMatchObject({ code: expect.toBeTicketCodeFor("2026-12-05") });
```

Con la fecha equivocada, el fallo usa el mensaje del matcher:

```text
  ● createTicket › should use the session date in the code
    expected "PLAN-20261205-0001" to be a ticket code for 2026-12-06 (PLAN-20261206-NNNN)
```

No hagas un matcher para cada `expect`. Compensa cuando la regla se repite y tiene nombre en tu dominio: un código, un DNI, una matrícula.

---

## ✅ Resumen

- `toThrow("texto")` busca un trozo del mensaje. `toThrow()`, `toThrow("")` y `toThrow(undefined)` pasan con cualquier error.
- Un error con clase y campos se comprueba con `toThrow(Clase)` y `toThrow(expect.objectContaining({ ... }))`.
- El valor va en `expect()` y el matcher dice cómo compararlo: `toHaveLength` y `toContain` explican el fallo; `toBe(true)` no.
- En una tabla, el nombre con los datos dice qué fila falló.
- `expect.extend` crea un matcher con su propio mensaje; `setupFilesAfterEnv` lo carga para todos los tests.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Matchers de objetos y arrays](02-matchers-de-objetos-y-arrays.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 08](../README.md)
