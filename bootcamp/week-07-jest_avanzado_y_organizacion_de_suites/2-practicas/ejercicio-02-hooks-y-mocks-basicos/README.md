# Ejercicio 02 - Hooks y Mocks Básicos

## Objetivo

Aplicar hooks de ciclo de vida y dobles de prueba simples para aislar dependencias.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/tests/notification.service.test.js`. Arriba hay un `logger` real (guarda eventos en memoria); no es un doble.
2. Descomenta PASO 1: `beforeEach` crea un `gateway` nuevo con `jest.fn()` y vacía el logger; `afterEach` llama a `jest.restoreAllMocks()` para devolver a su estado original cualquier método espiado.
3. Descomenta PASO 2: usa `gateway.send` como **stub** (`mockReturnValue`) y verifica el valor que devuelve el servicio.
4. Descomenta PASO 3: crea un **spy** con `jest.spyOn(logger, "info")`. Comprueba que se llamó con los argumentos esperados y que, como `spyOn` ejecuta la implementación real, `logger.entries` tiene un registro.
5. Descomenta PASO 4: caso de validación cuando falta el email.
6. Compara con `solution/tests/notification.service.test.js`.

> Diferencia clave: `jest.fn()` crea una función falsa nueva; `jest.spyOn(obj, "metodo")` envuelve un método que ya existe y lo deja funcionar. Por eso el spy se restaura (`mockRestore` / `jest.restoreAllMocks`).

## Comando sugerido

```bash
pnpm install
pnpm test notification.service.test.js
```
