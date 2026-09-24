# Ejercicio 02 - Refactor Seguro con Tests

## Objetivo

Refactorizar codigo heredado sin cambiar su comportamiento observable: primero se fija el comportamiento actual con tests y despues se cambia la estructura.

## Tiempo estimado

90 minutos.

## Preparacion

```bash
cd starter
pnpm install
```

`starter/shipping-fee.js` ya contiene la implementacion heredada, que funciona. No hay tests todavia.

## Paso a paso

### Paso 1: Capturar comportamiento actual

Descomenta el PASO 1 en `starter/shipping-fee.test.js` y ejecuta `pnpm test`. Los 3 tests deben pasar **en verde desde el principio**: son tests de caracterizacion, describen lo que el codigo ya hace. Esta es la red de proteccion antes de tocar nada.

### Paso 2: Refactor controlado

En `starter/shipping-fee.js`, borra las 4 lineas marcadas con `// PASO 2: borrar` y descomenta las dos lineas del PASO 2 (`ratePerKg`). Ejecuta de nuevo `pnpm test`: los 3 tests siguen en verde.

Para comprobar que la red de proteccion funciona, cambia temporalmente `8` por `7` y ejecuta los tests: el de envio prioritario debe fallar (`Expected: 16`, `Received: 14`). Deshaz el cambio.

Compara tu resultado con `solution/shipping-fee.js`.

## Comando sugerido

```bash
pnpm test shipping-fee.test.js
```
