# Ejercicio 02 - Refactor Seguro con Tests

## Objetivo

Refactorizar código heredado sin cambiar su comportamiento observable: primero se fija el comportamiento actual con tests y después se cambia la estructura.

## Tiempo estimado

90 minutos.

## Preparación

```bash
cd starter
pnpm install
```

`starter/shipping-fee.js` ya contiene la implementación heredada, que funciona. No hay tests todavía.

## Paso a paso

### Paso 1: Capturar comportamiento actual

Descomenta el PASO 1 en `starter/shipping-fee.test.js` y ejecuta `pnpm test`. Los 3 tests deben pasar **en verde desde el principio**: son tests de caracterización, describen lo que el código ya hace. Esta es la red de protección antes de tocar nada.

### Paso 2: Refactor controlado

En `starter/shipping-fee.js`, borra las 4 líneas marcadas con `// PASO 2: borrar` y descomenta las dos líneas del PASO 2 (`ratePerKg`). Ejecuta de nuevo `pnpm test`: los 3 tests siguen en verde.

Para comprobar que la red de protección funciona, cambia temporalmente `8` por `7` y ejecuta los tests: el de envío prioritario debe fallar (`Expected: 16`, `Received: 14`). Deshaz el cambio.

Compara tu resultado con `solution/shipping-fee.js`.

## Comando sugerido

```bash
pnpm test shipping-fee.test.js
```
