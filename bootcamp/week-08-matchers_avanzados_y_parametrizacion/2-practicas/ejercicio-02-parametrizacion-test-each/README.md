# Ejercicio 02 - Parametrizacion con `test.each`

## Objetivo

Reducir duplicacion de tests mediante tablas de casos claras y mantenibles.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/discount.service.test.js`.
2. Descomenta PASO 1 para el caso simple.
3. Descomenta PASO 2 para la tabla de arrays con `test.each`: los `%i` del titulo se rellenan en el orden de las columnas `[price, percentage, expected]`.
4. Descomenta PASO 3 para la tabla con template literal (`` test.each`...` ``) y titulos con `$variable`.
5. Descomenta PASO 4 para agrupar los casos invalidos con `describe.each`.
6. Ejecuta con `--verbose`, revisa que cada titulo describe su fila y compara con `solution/discount.service.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test discount.service.test.js
```
