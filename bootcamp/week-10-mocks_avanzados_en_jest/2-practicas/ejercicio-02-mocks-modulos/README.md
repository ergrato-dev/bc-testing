# Ejercicio 02 - Mocks de Modulos con jest.mock

## Objetivo

Aislar una dependencia de infraestructura usando `jest.mock`, mockeando solo parte del modulo con `jest.requireActual`.

## Tiempo estimado

90 minutos.

## Paso a paso

### Paso 1: Mock parcial del modulo con `jest.requireActual`

Abre `starter/order.service.test.js` y descomenta PASO 1. El factory de `jest.mock` copia el modulo real con `jest.requireActual` y solo reemplaza `charge`, que es la funcion que saldria a la red.

### Paso 2: Verificar que parte es real y que parte es mock

Descomenta PASO 2 y comprueba con `jest.isMockFunction` que `toCents` es real y `charge` es mock.

### Paso 3: Simular aprobacion de pago

Descomenta PASO 3 y valida la respuesta exitosa. `charge` recibe `20000` porque `toCents` real convirtio `200` a centimos.

### Paso 4: Simular rechazo del pago

Descomenta PASO 4 y verifica el error esperado.

### Paso 5: Simular fallo de red con `mockImplementation`

Descomenta PASO 5: la implementacion falsa lanza `gateway timeout` y el servicio lo propaga.

### Paso 6: Revisar solucion

Compara con `solution/order.service.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test order.service.test.js
```
