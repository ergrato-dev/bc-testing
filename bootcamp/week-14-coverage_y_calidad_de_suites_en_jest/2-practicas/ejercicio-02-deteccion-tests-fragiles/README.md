# Ejercicio 02 - Deteccion de Tests Fragiles

## Objetivo

Reconocer patrones de fragilidad reales (assert debil, dependencia del tiempo, dependencia del orden y over-mocking) y convertirlos en tests estables y utiles.

## Tiempo estimado

90 minutos.

## Requisito previo

```bash
cd starter
pnpm install
```

Cada PASO de `starter/report.builder.test.js` trae la **version fragil** dentro de un comentario `/* ... */` (no se ejecuta) y debajo la **version robusta**. Al descomentar el PASO, lee primero la version fragil y explica por que falla o por que no detecta errores.

## Paso a paso

### Paso 1: Reemplazar assert debil por contrato claro

Descomenta PASO 1. `expect(result).toBeTruthy()` pasa con cualquier objeto; el reemplazo valida el contrato completo con `toEqual`.

### Paso 2: Probar ruta de validacion

Descomenta PASO 2 para cubrir el error de entrada.

### Paso 3: Dependencia del tiempo -> reloj controlado

Descomenta PASO 3. `isDeliveryOverdue` usa `Date.now()`: un test con el reloj real es una bomba de tiempo. La version robusta fija la fecha con `jest.useFakeTimers()` y `jest.setSystemTime(...)`, y restaura con `jest.useRealTimers()` en `afterEach`.

### Paso 4: Dependencia del orden -> estado nuevo en cada test

Descomenta PASO 4. Con una instancia compartida, el segundo test depende del primero (prueba ponerle `test.only` a la version fragil y veras que falla). La version robusta crea el store en `beforeEach`.

### Paso 5: Over-mocking -> simular solo la frontera externa

Descomenta PASO 5. Mockear `buildDeliveryReport` (logica propia y pura) deja un test que solo verifica cableado: pasa incluso con un input vacio. La version robusta usa el builder real y simula solo el `notifier`, verificando el mensaje exacto.

## Cierre

Compara con `solution/report.builder.test.js` y ejecuta `pnpm test` varias veces: el resultado debe ser siempre el mismo.

## Comando sugerido

```bash
pnpm test:coverage
```
