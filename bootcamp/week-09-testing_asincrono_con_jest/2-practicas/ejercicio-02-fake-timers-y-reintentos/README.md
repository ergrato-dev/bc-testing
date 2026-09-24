# Ejercicio 02 - Fake Timers y Reintentos

## Objetivo

Controlar el tiempo de ejecucion en tests de retry y debounce usando fake timers.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/retry.service.test.js` y revisa `starter/retry.service.js` y `starter/debounce.js`.
2. Descomenta PASO 1 para activar fake timers.
3. Descomenta PASO 2 para simular el paso del tiempo con `await jest.advanceTimersByTimeAsync(1000)`. La version sincrona (`jest.advanceTimersByTime`) no deja correr los `.then/.catch` que agendan el siguiente reintento, y el test se queda esperando hasta el timeout.
4. Descomenta PASO 3 para validar el agotamiento de reintentos (el `expect(...).rejects` se engancha antes de avanzar el reloj).
5. Descomenta PASO 4 para probar un `debounce`: solo la ultima llamada se ejecuta cuando pasan 300 ms sin nuevas llamadas.
6. Compara con `solution/retry.service.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test retry.service.test.js
```
