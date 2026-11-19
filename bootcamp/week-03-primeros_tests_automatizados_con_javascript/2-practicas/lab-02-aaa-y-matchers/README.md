# Lab 02 - AAA y matchers

## Objetivo

Leer tests escritos con AAA y después escribir los tuyos, eligiendo el matcher que corresponde a cada valor: `toBe`, `toThrow`, `toEqual`, `toBeNull` y `toBeCloseTo`.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-estructura-test-jest.md) y la [03](../../1-teoria/03-matchers-y-ejecucion.md).
- El proyecto `~/labs-semana-03` del [lab 01](../lab-01-primer-proyecto-jest/README.md).

## Punto de partida

```bash
cd ~/labs-semana-03
cp -r <ruta-del-bootcamp>/bootcamp/week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-02-aaa-y-matchers/starter/. .
pnpm test user-utils
```

```text
FAIL tests/user-utils.test.js
  ● Test suite failed to run

    Your test suite must contain at least one test.
```

`pnpm test user-utils` ejecuta solo los archivos cuya ruta contiene `user-utils`. El archivo no tiene ningún test activo todavía.

## Contexto

`src/user-utils.js` tiene cinco funciones del registro de usuarios de una tienda. Léelo entero antes de empezar (son 35 líneas): para escribir el esperado de un test tienes que saber qué debe devolver cada función.

---

## Paso 1: isAdult (5 min)

Descomenta el bloque `PASO 1` de `tests/user-utils.test.js` y ejecuta `pnpm test user-utils`. Pasan 2 tests.

Fíjate en los valores: 18 y 17. Son los valores límite de «mayor de edad» de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/1-teoria/02-tecnicas-de-diseno.md#2-análisis-de-valores-límite). Un 35 y un 10 pasarían igual con `age > 18` en el código; el 18, no.

## Paso 2: calculateDiscount y toThrow (5 min)

Descomenta `PASO 2` y ejecuta. En el segundo test, a `expect` le llega `() => calculateDiscount(price, percent)`: una función, no la llamada. Por eso Act y Assert van en la misma línea.

Prueba a quitar `() =>` y ejecuta. El test falla con `Invalid percent` y la línea del `throw`: el error saltó antes de que `expect` pudiera capturarlo. Vuelve a ponerlo.

## Paso 3: isValidEmail (5 min)

Descomenta `PASO 3` y ejecuta. Llevas 6 tests en verde.

`isValidEmail` devuelve `true` o `false`, así que los tests usan `toBe(true)` y `toBe(false)`, no `toBeTruthy`.

## Paso 4: createUser con toEqual (10 min)

Escribe tú el test del `PASO 4`, con AAA y nombre `should ... when ...`:

- Arrange: `name` es `"  Ana Pérez "` (con espacios) y `email` es `"ANA@Example.com"`.
- Act: llama a `createUser(name, email)`.
- Assert: `toEqual` con el objeto que debe devolver. Escribe el objeto a mano, leyendo qué hace la función con cada campo.

Antes de dejarlo con `toEqual`, prueba `toBe` con el mismo objeto. Lee el mensaje (`serializes to the same string`) y vuelve a `toEqual`.

## Paso 5: findUserByEmail con toEqual y toBeNull (15 min)

Dos tests en el `PASO 5`. En los dos, el Arrange crea una lista de usuarios con `createUser`:

```javascript
const users = [createUser("Ana", "ana@example.com"), createUser("Luis", "luis@example.com")];
```

- Busca `"luis@example.com"`: el esperado es el objeto de Luis.
- Busca un email que no está: el esperado es `null`.

## Paso 6: decimales con toBeCloseTo (10 min)

`PASO 6`: precio 19.99 con un 15 %. Calcula el esperado a mano: 19,99 − 2,9985 = 16,9915, que redondeado a céntimos es 16,99.

Escríbelo primero con `toBe(16.99)` y ejecuta:

```text
    Expected: 16.99
    Received: 16.9915
```

Cámbialo a `toBeCloseTo(16.99)`, que acepta diferencias menores de 0,005, y ejecuta. ¿Qué es mejor para un precio: que el test acepte 16,9915 o que la función redondee a céntimos? Anota tu respuesta: es una pregunta para negocio, como las de la semana 2.

## Paso 7: filtrar por nombre (5 min)

```bash
pnpm test user-utils -t "email"
```

```text
Tests:       5 skipped, 5 passed, 10 total
```

Se ejecutan los 5 tests cuyo nombre contiene `email` y se saltan los demás. Prueba con otra palabra de tus nombres.

Borra las líneas `TODO` del archivo y guarda el lab en git:

```bash
git add .
git commit -m "lab 02: AAA y matchers"
```

---

## Comprobación final

```bash
pnpm test user-utils 2>&1 | grep '^Tests:'
# Tests:       10 passed, 10 total
grep -c TODO tests/user-utils.test.js
# 0
```

Repasa:

- [ ] Los cuatro tests nuevos siguen AAA y su nombre dice resultado y condición.
- [ ] Ningún esperado repite la fórmula del código: están escritos a mano.
- [ ] Usas `toEqual` solo con objetos y `toBeCloseTo` solo con decimales.

## Qué has aprendido

- El matcher depende del tipo de valor: `toBe` para valores simples, `toEqual` para objetos, `toBeCloseTo` para decimales, `toBeNull` para la ausencia y `toThrow` con una función para los errores.
- Un fallo con el matcher equivocado (`toBe` con objetos, con decimales) no es un bug del código: se arregla en el test.
- `-t` y la ruta tras `pnpm test` ejecutan solo lo que estás tocando.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer proyecto con Jest](../lab-01-primer-proyecto-jest/README.md) | [Lab 03 - De casos de prueba a tests](../lab-03-de-casos-a-tests/README.md) → |

[↑ Semana 03](../../README.md)
