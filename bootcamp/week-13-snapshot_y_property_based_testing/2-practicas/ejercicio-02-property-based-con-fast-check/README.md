# Ejercicio 02 - Property-Based con fast-check

## Objetivo

Definir y validar propiedades invariantes en una funcion de limpieza de texto.

## Tiempo estimado

90 minutos.

## Preparacion

`fast-check` ya esta declarado en `package.json`:

```bash
cd starter
pnpm install
```

Los tests usan un generador propio, `textWithWhitespace`, que construye strings solo con `a`, `B`, espacio, tab y salto de linea (`fc.string({ unit: fc.constantFrom(...) })`). Con `fc.string()` por defecto casi nunca aparecerian tabs ni saltos de linea, y el bug de este ejercicio pasaria desapercibido.

La implementacion inicial de `normalizeText` tiene un bug a proposito: solo colapsa espacios, no tabs ni saltos de linea.

## Paso a paso

### Paso 1: Propiedad de idempotencia

Abre `starter/text-normalizer.test.js` y descomenta PASO 1. Pasa.

### Paso 2: Propiedad de normalizacion de espacios

Descomenta PASO 2 para validar que no queden espacios dobles. Tambien pasa, a pesar del bug: la propiedad es demasiado debil (solo mira espacios).

### Paso 3: Propiedad de trimming

Descomenta PASO 3 para validar que el resultado queda sin espacios extremos. Pasa.

### Paso 4: Propiedad que falla y contraejemplo

Descomenta PASO 4 y ejecuta `pnpm test`. La propiedad falla con una salida como esta (el `seed` cambia en cada ejecucion):

```text
Property failed after 1 tests
{ seed: 432883139, path: "0:1:2:3:5", endOnFailure: true }
Counterexample: ["a\ta"]
Shrunk 4 time(s)
```

Anota el `Counterexample` (el input minimo tras el shrinking), cuantas veces se redujo (`Shrunk`) y el `seed`. Opcional: pasa `{ seed: <tu seed>, path: "<tu path>" }` como segundo argumento de `fc.assert` para reproducir exactamente el mismo fallo.

### Paso 5: Corregir la implementacion

En `starter/text-normalizer.js`, borra la linea marcada con `// PASO 5: borrar` y descomenta la version con `/\s+/g`. Ejecuta de nuevo: las 4 propiedades pasan.

### Paso 6: Revisar solucion

Compara con `solution/`.

## Comando sugerido

```bash
pnpm test text-normalizer.test.js
```
