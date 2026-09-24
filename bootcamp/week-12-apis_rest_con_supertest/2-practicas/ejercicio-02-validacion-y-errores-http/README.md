# Ejercicio 02 - Validacion y Errores HTTP

## Objetivo

Validar respuestas de error (400, 404, 409 y 500) y contratos JSON consistentes en endpoints REST.

## Tiempo estimado

90 minutos.

## Preparacion

```bash
cd starter
pnpm install
```

`starter/app-errors.js` exporta `createApp({ repository })`. Sin argumentos usa un repositorio en memoria nuevo; tambien acepta un repositorio inyectado, lo que permite simular fallos. Al final de la app hay un middleware de errores de Express 5 (funcion de 4 argumentos) que convierte cualquier excepcion en un 500 con el mismo formato de error.

## Paso a paso

### Paso 1: Caso de validacion 400

Abre `starter/app-errors.test.js` y descomenta el PASO 1.

### Paso 2: Caso de no encontrado 404

Descomenta el PASO 2 y valida el payload de error.

### Paso 3: Caso de conflicto 409

Descomenta el PASO 3 y verifica la respuesta. Funciona siempre porque cada test parte del repositorio inicial, que ya contiene `Notebook`.

### Paso 4: Caso de error interno 500

Descomenta el PASO 4. El test crea una app con un repositorio cuyo `findById` lanza una excepcion. Express 5 la envia al middleware de errores y el test verifica el 500 y su contrato JSON, sin exponer el mensaje interno (`database down`).

Compara con `solution/app-errors.test.js`.

## Comando sugerido

```bash
pnpm test app-errors.test.js
```
