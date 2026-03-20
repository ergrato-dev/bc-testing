# Ejercicio 02 - Property-Based con fast-check

## Objetivo

Definir y validar propiedades invariantes en una funcion de limpieza de texto.

## Tiempo estimado

90 minutos.

## Requisito previo

Instalar dependencia de testing:

```bash
yarn add -D fast-check
```

## Paso a paso

### Paso 1: Propiedad de idempotencia

Abre `starter/text-normalizer.test.js` y descomenta PASO 1.

### Paso 2: Propiedad de normalizacion de espacios

Descomenta PASO 2 para validar que no queden espacios dobles.

### Paso 3: Propiedad de trimming

Descomenta PASO 3 para validar que el resultado queda sin espacios extremos.

### Paso 4: Revisar solucion

Compara con `solution/text-normalizer.test.js`.

## Comando sugerido

```bash
yarn test text-normalizer.test.js
```
