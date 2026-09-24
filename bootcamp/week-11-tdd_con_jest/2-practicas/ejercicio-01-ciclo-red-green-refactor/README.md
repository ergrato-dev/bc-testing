# Ejercicio 01 - Ciclo Red-Green-Refactor

## Objetivo

Practicar TDD en micro-pasos construyendo una funcion de descuento. Cada ciclo sigue el mismo orden: **test (Red) → ejecutar y ver el fallo → codigo minimo (Green) → refactor**.

## Tiempo estimado

90 minutos.

## Preparacion

```bash
cd starter
pnpm install
```

El starter trae el esqueleto `calculateDiscount` sin cuerpo (devuelve `undefined`). Los PASO del test estan en `discount-calculator.test.js` y los de implementacion en `discount-calculator.js`.

> Regla del ejercicio: despues de cada PASO ejecuta `pnpm test` y comprueba que el resultado es el esperado. Si un Red pasa en verde, detente: el test no esta probando nada nuevo.

## Paso a paso

### Paso 1: Red - caso minimo (premium)

Descomenta el PASO 1 en `discount-calculator.test.js` y ejecuta los tests. Fallo esperado:

```text
Expected: 90
Received: undefined
```

### Paso 2: Green - codigo minimo

Descomenta el PASO 2 en `discount-calculator.js` (`return price * 0.9;`). El test pasa, aunque la funcion ya no distingue socios: es lo minimo que pide el unico test existente.

### Paso 3: Red - socio basico

Descomenta el PASO 3 en el test. Fallo esperado (el codigo del PASO 2 aplica descuento a todos):

```text
Expected: 100
Received: 90
```

### Paso 4: Green - precio completo para no premium

Descomenta el PASO 4 en `discount-calculator.js` (esta encima del PASO 2 a proposito: la guarda debe ejecutarse antes del `return`). Los 2 tests pasan.

### Paso 5: Red - precio negativo

Descomenta el PASO 5 en el test. Fallo esperado:

```text
Expected substring: "invalid price"
Received function did not throw
```

### Paso 6: Green - validacion

Descomenta el PASO 6 en `discount-calculator.js`. Los 3 tests pasan.

### Paso 7: Refactor - nombrar el numero magico

Descomenta la constante del PASO 7 y sustituye `0.9` por `PREMIUM_PRICE_FACTOR` en la linea del PASO 2. Ejecuta los tests: siguen en verde, el comportamiento no cambio.

Compara tu resultado con `solution/discount-calculator.js`.

## Comando sugerido

```bash
pnpm test discount-calculator.test.js
```
