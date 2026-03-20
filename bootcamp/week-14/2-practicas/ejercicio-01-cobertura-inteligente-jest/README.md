# Ejercicio 01 - Cobertura Inteligente en Jest

## Objetivo

Aprender a subir cobertura de forma util, priorizando ramas de negocio y manejo de errores.

## Tiempo estimado

90 minutos.

## Paso a paso

### Paso 1: Revisar caso feliz actual

Abre `starter/pricing.service.test.js` y descomenta PASO 1.

### Paso 2: Cubrir validacion de entrada

Descomenta PASO 2 para validar error cuando `basePrice` es invalido.

### Paso 3: Cubrir rama de descuento premium

Descomenta PASO 3 para verificar la regla de descuento.

### Paso 4: Cubrir rama de recargo nocturno

Descomenta PASO 4 para validar el recargo por franja horaria.

### Paso 5: Comparar con solucion

Revisa `solution/pricing.service.test.js` y discute que ramas aportan mas confianza.

## Comando sugerido

```bash
yarn test pricing.service.test.js --coverage
```
