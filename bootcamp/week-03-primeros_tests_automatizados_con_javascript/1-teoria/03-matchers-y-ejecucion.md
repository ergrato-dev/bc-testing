# 03 - Matchers, errores y ejecución

> Lenguaje: JavaScript.

`expect(valor)` no comprueba nada por sí solo: lo hace el **matcher** que va detrás (`toBe`, `toEqual`, `toThrow`...). Elegir el matcher equivocado da tests que fallan sin motivo o, peor, que pasan con el código roto. Esta teoría cubre los básicos, los dos tipos de fallo y las formas de ejecutar solo parte de la suite.

---

## 🎯 Objetivos

- Explicar por qué una comparación con `==` no es una aserción.
- Elegir entre `toBe`, `toEqual`, `toBeCloseTo`, `toBeNull`, `toBeTruthy` y `toThrow`.
- Distinguir un fallo de aserción de un error de ejecución.
- Ejecutar un archivo, un grupo de tests por nombre y el modo *watch*.

---

## 1. Aserción frente a comparación

Una **aserción** es una comprobación que marca el test como fallido si no se cumple. En Jest, cada `expect(...).matcher(...)` es una aserción.

Una comparación con `==` solo calcula `true` o `false`. Si nadie usa ese valor, el test pasa:

```javascript
it("comparacion sin expect", () => {
  const result = add(2, 3);
  result == 6;
});
```

```text
PASS tests/demo.test.js
  ✓ comparacion sin expect (1 ms)
```

`add(2, 3)` es 5, el test dice 6 y aun así pasa: **un test sin `expect` siempre pasa**. Jest solo marca un test como fallido cuando algo lanza un error dentro de él, y los matchers lanzan ese error cuando la comprobación no se cumple.

---

## 2. Matchers básicos

### `toBe`: el mismo valor

Para números, textos, `true`/`false`, `null` y `undefined`. Compara sin convertir tipos:

```javascript
expect("5").toBe(5);
```

```text
    Expected: 5
    Received: "5"
```

Las comillas de `Received` te dicen que llegó un texto, no un número. Con `==` habría pasado, porque `"5" == 5` es `true`: JavaScript convierte el texto a número antes de comparar.

### `toEqual`: el mismo contenido

Dos objetos con el mismo contenido son objetos distintos en memoria, así que `toBe` falla:

```javascript
expect({ name: "Ana" }).toBe({ name: "Ana" });
```

```text
    expect(received).toBe(expected) // Object.is equality

    If it should pass with deep equality, replace "toBe" with "toStrictEqual"

    Expected: {"name": "Ana"}
    Received: serializes to the same string
```

«serializes to the same string» significa «se escriben igual pero no son el mismo objeto». Para objetos y arrays, usa `toEqual`, que compara el contenido campo a campo. Con `createUser`, la función del [lab 02](../2-practicas/lab-02-aaa-y-matchers/README.md) que limpia el nombre y pasa el email a minúsculas:

```javascript
expect(createUser("  Ana Pérez ", "ANA@Example.com")).toEqual({
  name: "Ana Pérez",
  email: "ana@example.com",
  active: true,
});
```

Jest te sugiere `toStrictEqual`, una versión más estricta de `toEqual` que la [semana 8](../../week-08-matchers_avanzados_y_parametrizacion/README.md) compara con ella.

### `toBeCloseTo`: decimales

Los ordenadores guardan los decimales en binario y algunos no son exactos:

```javascript
expect(0.1 + 0.2).toBe(0.3);
```

```text
    Expected: 0.3
    Received: 0.30000000000000004
```

`toBeCloseTo` acepta una diferencia mínima (por defecto, menos de 0,005):

```javascript
expect(0.1 + 0.2).toBeCloseTo(0.3); // pasa
```

Úsalo siempre que el resultado venga de operar con decimales: precios con descuento, medias, conversiones.

### `toBeNull`, `toBeUndefined`

```javascript
expect(findUserByEmail(users, "eva@example.com")).toBeNull();
```

`toBeNull` solo acepta `null`. `toBeUndefined` solo acepta `undefined`, el valor de una variable o un campo que no existe.

### `toBeTruthy`, `toBeFalsy`

`toBeTruthy` pasa con cualquier valor que JavaScript trate como verdadero: `true`, `1`, `35`, `"hola"`, cualquier objeto. `toBeFalsy`, con `false`, `0`, `""`, `null`, `undefined` y `NaN`.

Son cómodos y peligrosos. `expect(cartTotal(items)).toBeTruthy()` pasa si el total es 35, 28 o -4: no comprueba el total. Si la función devuelve `true` o `false`, usa `toBe(true)` o `toBe(false)`; si devuelve un número, `toBe` con el número. En el [lab 04](../2-practicas/lab-04-tests-que-mienten/README.md) verás un test así dejar pasar un bug.

### `toThrow`: errores

```javascript
expect(() => divide(10, 0)).toThrow("Division by zero");
```

A `expect` le pasas una **función** que hace la llamada, no la llamada. Jest ejecuta la función y captura el error. Si escribes la llamada directamente:

```javascript
expect(divide(10, 0)).toThrow("Division by zero");
```

el error salta antes de que `expect` exista y el test falla con él:

```text
  ● toThrow sin funcion

    Division by zero

       5 | function divide(a, b) {
       6 |   if (b === 0) {
    >  7 |     throw new Error("Division by zero");
         |           ^
```

El texto de `toThrow("...")` tiene que aparecer en el mensaje del error. Así compruebas que salta **ese** error y no otro.

### `.not`

Cualquier matcher se invierte con `.not`:

```javascript
expect(findUserByEmail(users, "ana@example.com")).not.toBeNull();
```

Úsalo con cuidado: `.not.toBe(5)` pasa con cualquier valor que no sea 5, incluido uno absurdo. Un esperado concreto comprueba más.

### Resumen de matchers

| Valor que devuelve la función | Matcher |
|---|---|
| Número entero, texto, `true`/`false` | `toBe` |
| Número con decimales | `toBeCloseTo` |
| Objeto o array | `toEqual` |
| `null` / `undefined` | `toBeNull` / `toBeUndefined` |
| Un error | `toThrow` con una función |

---

## 3. Dos tipos de fallo

Los dos aparecen como `FAIL`, pero se arreglan distinto.

**Fallo de aserción.** El código se ejecutó y devolvió otro valor. El informe trae `Expected` y `Received`:

```text
    Expected: 5
    Received: -1
```

Pregunta: ¿está mal el código o el esperado del test?

**Error de ejecución.** El test se rompió antes de llegar al `expect`. El informe trae el error y la línea donde saltó, sin `Expected` ni `Received`:

```text
  ● error de ejecucion

    TypeError: Cannot read properties of undefined (reading 'name')

      19 | it("error de ejecucion", () => {
      20 |   const user = undefined;
    > 21 |   expect(user.name).toBe("Ana");
         |               ^
```

Si el error está en el `require`, no se ejecuta ningún test del archivo:

```text
FAIL tests/math.test.js
  ● Test suite failed to run

    Cannot find module '../src/maths' from 'tests/math.test.js'
```

Pregunta: ¿qué dato, ruta o nombre está mal en el test o en su preparación?

---

## 4. Ejecutar parte de la suite

| Comando | Qué ejecuta |
|---|---|
| `pnpm test` | Todos los archivos de test |
| `pnpm test math` | Los archivos cuya ruta contiene `math` |
| `pnpm test -t "divid"` | Solo los tests cuyo nombre (con sus `describe`) contiene `divid` |
| `pnpm test --watch` | Los tests de los archivos cambiados, y otra vez cada vez que guardas |

Lo que escribes detrás de `pnpm test` se añade al comando `jest`. Con `-t`, los demás tests aparecen como saltados:

```text
$ jest -t divid
PASS tests/calculator.test.js
  calculator
    ✓ should throw an error when dividing by zero (11 ms)
    ○ skipped should return 5 when adding 2 and 3

Test Suites: 1 passed, 1 total
Tests:       1 skipped, 1 passed, 2 total
```

### Modo watch

`--watch` necesita que la carpeta sea un repositorio git, porque pregunta a git qué archivos han cambiado:

```text
$ jest --watch

--watch is not supported without git/hg, please use --watchAll
```

Con `git init` hecho, Jest se queda esperando y vuelve a ejecutar cada vez que guardas un archivo. Abajo muestra sus teclas:

```text
Watch Usage
 › Press a to run all tests.
 › Press f to run only failed tests.
 › Press p to filter by a filename regex pattern.
 › Press t to filter by a test name regex pattern.
 › Press q to quit watch mode.
 › Press Enter to trigger a test run.
```

`q` sale del modo watch.

### `--` no hace falta

Con pnpm no se escribe `--` entre el script y sus opciones. Si lo pones, llega a Jest tal cual y lo toma como el nombre de un archivo:

```text
$ jest -- --watch
No tests found, exiting with code 1
```

---

## ✅ Resumen

- Sin `expect`, el test pasa siempre.
- `toBe` para valores simples, `toEqual` para objetos y arrays, `toBeCloseTo` para decimales, `toThrow` con una función para errores. `toBeTruthy` casi nunca es lo que quieres.
- Fallo de aserción: hay `Expected` y `Received`. Error de ejecución: el test se rompió antes del `expect`.
- `pnpm test <ruta>`, `pnpm test -t "<nombre>"` y `pnpm test --watch` (con git) ejecutan solo lo que estás tocando.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Estructura de un test: describe, it, expect y AAA](02-estructura-test-jest.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 03](../README.md)
