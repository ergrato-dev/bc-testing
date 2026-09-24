# Ejercicio 02 - Detección de Tests Frágiles

## Objetivo

Reconocer patrones de fragilidad reales (assert débil, dependencia del tiempo, dependencia del orden y over-mocking) y convertirlos en tests estables y útiles.

## Tiempo estimado

90 minutos.

## Requisito previo

```bash
cd starter
pnpm install
```

Cada PASO de `starter/report.builder.test.js` trae la **versión frágil** dentro de un comentario `/* ... */` (no se ejecuta) y debajo la **versión robusta**. Al descomentar el PASO, lee primero la versión frágil y explica por qué falla o por qué no detecta errores.

## Paso a paso

### Paso 1: Reemplazar assert débil por contrato claro

Descomenta PASO 1. `expect(result).toBeTruthy()` pasa con cualquier objeto; el reemplazo valida el contrato completo con `toEqual`.

### Paso 2: Probar ruta de validación

Descomenta PASO 2 para cubrir el error de entrada.

### Paso 3: Dependencia del tiempo -> reloj controlado

Descomenta PASO 3. `isDeliveryOverdue` usa `Date.now()`: un test con el reloj real es una bomba de tiempo. La versión robusta fija la fecha con `jest.useFakeTimers()` y `jest.setSystemTime(...)`, y restaura con `jest.useRealTimers()` en `afterEach`.

### Paso 4: Dependencia del orden -> estado nuevo en cada test

Descomenta PASO 4. Con una instancia compartida, el segundo test depende del primero (prueba ponerle `test.only` a la versión frágil y verás que falla). La versión robusta crea el store en `beforeEach`.

### Paso 5: Over-mocking -> simular solo la frontera externa

Descomenta PASO 5. Mockear `buildDeliveryReport` (lógica propia y pura) deja un test que solo verifica cableado: pasa incluso con un input vacío. La versión robusta usa el builder real y simula solo el `notifier`, verificando el mensaje exacto.

## Cierre

Compara con `solution/report.builder.test.js` y ejecuta `pnpm test` varias veces: el resultado debe ser siempre el mismo.

## Comando sugerido

```bash
pnpm test:coverage
```
