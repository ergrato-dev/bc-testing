# Ejercicio 01 - Suite Integrada en Jest

## Objetivo

Unificar en un mismo modulo tests unitarios, de integracion ligera y de invariantes para obtener mejor senal de calidad.

## Tiempo estimado

90 minutos.

## Requisito previo

```bash
yarn add -D fast-check
```

## Paso a paso

### Paso 1: Unit test de regla base

Abre `starter/__tests__/order.summary.test.js` y descomenta PASO 1.

### Paso 2: Validacion de errores

Descomenta PASO 2 para cubrir ramas invalidas.

### Paso 3: Snapshot acotado

Descomenta PASO 3 para validar contrato de salida estable.

### Paso 4: Property-based test

Descomenta PASO 4 para verificar invariante de totales no negativos.

### Paso 5: Comparar con solucion

Revisa `solution/__tests__/order.summary.test.js`.

## Comando sugerido

```bash
yarn test order.summary.test.js --coverage
```
