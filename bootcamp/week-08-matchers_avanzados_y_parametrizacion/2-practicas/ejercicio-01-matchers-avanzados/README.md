# Ejercicio 01 - Matchers Avanzados

## Objetivo

Aplicar matchers avanzados para validar estructuras, inclusiones y errores con mayor precisión.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/product.utils.test.js` y revisa `starter/product.utils.js`.
2. Descomenta PASO 1 y compara `toEqual` vs `toStrictEqual`: una clave con valor `undefined` solo la detecta `toStrictEqual`.
3. Descomenta PASO 2 para `toContain` (primitivo en array), `toContainEqual` (objeto en array), `toMatchObject` (forma parcial) y `toHaveProperty` (ruta anidada).
4. Descomenta PASO 3 para `toBeCloseTo`: `0.1 + 0.2` no es exactamente `0.3`.
5. Descomenta PASO 4 para validar el error con `toThrow`.
6. Ejecuta la suite y compara con `solution/product.utils.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test product.utils.test.js
```
