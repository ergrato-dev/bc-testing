# Ejercicio 01 - Cobertura Inteligente en Jest

## Objetivo

Aprender a subir cobertura de forma util: configurar `collectCoverageFrom` y `coverageThreshold`, leer el reporte para encontrar la rama no cubierta y cubrir ramas de negocio, errores y bordes.

## Tiempo estimado

90 minutos.

## Requisito previo

```bash
cd starter
pnpm install
```

## Paso a paso

### Paso 1: Revisar caso feliz actual

Abre `starter/pricing.service.test.js` y descomenta PASO 1.

### Paso 2: Cubrir validacion de entrada

Descomenta PASO 2 para validar el error cuando `basePrice` es invalido.

### Paso 3: Cubrir rama de descuento premium

Descomenta PASO 3 para verificar la regla de descuento.

### Paso 4: Cubrir rama de recargo nocturno

Descomenta PASO 4 para validar el recargo por franja horaria.

### Paso 5: Configurar `collectCoverageFrom` y `coverageThreshold`

Abre `starter/jest.config.js` y descomenta PASO 5. Ejecuta:

```bash
pnpm test:coverage
```

Los 4 tests pasan, pero el comando **termina con error** porque no se alcanza el umbral:

```text
--------------------|---------|----------|---------|---------|-------------------
File                | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s
--------------------|---------|----------|---------|---------|-------------------
All files           |    90.9 |    93.75 |     100 |    90.9 |
 pricing.service.js |    90.9 |    93.75 |     100 |    90.9 | 11
--------------------|---------|----------|---------|---------|-------------------
Jest: Coverage for statements (90.9%) does not meet "global" threshold (100%)
Jest: Coverage for branches (93.75%) does not meet "global" threshold (100%)
Jest: Coverage for lines (90.9%) does not meet "global" threshold (100%)
```

**Tarea de lectura del reporte** (antes de seguir):

1. En la salida de texto, la columna `Uncovered Line #s` indica la linea 11.
2. Abre `coverage/lcov-report/index.html` en el navegador y entra a `pricing.service.js`.
3. Localiza la linea 11 en rojo y el marcador `I` sobre el `if` de la linea 10.
4. Anota que regla de negocio corresponde a esa rama (validacion de `hour`) y por que es riesgosa si nadie la prueba.

### Paso 6: Cubrir la rama detectada (`Invalid hour`)

Descomenta PASO 6. Vuelve a ejecutar `pnpm test:coverage`: el umbral se cumple (100%) y el comando termina sin error.

### Paso 7: Probar los bordes que el coverage no exige

Descomenta PASO 7 para verificar las horas 21, 22, 5 y 6. El coverage ya estaba en 100% antes de este paso: si alguien cambia `hour >= 22` por `hour > 22`, ningun test del Paso 1 al 6 falla. Solo los tests de borde protegen esa regla.

## Cierre

Compara tu resultado con `solution/` y discute que ramas aportan mas confianza y por que el 100% del Paso 6 no era suficiente.

## Comando sugerido

```bash
pnpm test:coverage
```
