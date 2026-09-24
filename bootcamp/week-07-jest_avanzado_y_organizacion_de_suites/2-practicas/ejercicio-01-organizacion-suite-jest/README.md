# Ejercicio 01 - Organizacion de Suite Jest

## Objetivo

Organizar una suite por comportamiento usando `describe` anidados: un `describe` para el servicio y, dentro, uno por método.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/order.service.test.js`. Ya existe el `describe("OrderService")` principal y, dentro, un `describe("validateOrder")` vacío.
2. Descomenta PASO 1: crea el `describe("calculateTotal")` anidado dentro de `OrderService` con su primer test.
3. Descomenta PASO 2: añade al grupo `validateOrder` el caso inválido (lista vacía).
4. Descomenta PASO 3: añade al mismo grupo el caso válido. Observa cómo los nombres del reporte se leen como una frase: `OrderService › validateOrder › should return true when ...`.
5. Compara con `solution/order.service.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test order.service.test.js

# Solo los tests del grupo validateOrder (filtro por nombre)
pnpm test -t validateOrder
```
