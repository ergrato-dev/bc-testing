# Ejercicio 01 - Endpoints GET y POST con Supertest

## Objetivo

Probar endpoints basicos de consulta y creacion con contratos HTTP claros, con cada test aislado de los demas.

## Tiempo estimado

90 minutos.

## Preparacion

Instala las dependencias (`express`, `jest` y `supertest` ya estan declaradas en `package.json`):

```bash
cd starter
pnpm install
```

`starter/app.js` exporta `createApp()`, una factory que devuelve una app Express nueva con su propio repositorio en memoria. El `beforeEach` del test crea una app por test, asi ningun caso depende de lo que hizo otro.

## Paso a paso

### Paso 1: Probar health endpoint

Abre `starter/app.test.js` y descomenta el PASO 1.

### Paso 2: Probar GET /items

Descomenta el PASO 2 para validar estado 200 y estructura del array.

### Paso 3: Probar POST /items

Descomenta el PASO 3 para validar status 201 y payload creado.

El `id` lo genera el servidor, asi que el test no fija su valor: usa `expect.any(String)`, un **matcher asimetrico**. Dentro de `toEqual`, `expect.any(String)` acepta cualquier string en esa posicion y el resto del objeto se compara exacto. Su pariente `expect.objectContaining({ name: "Mouse" })` acepta cualquier objeto que tenga al menos esas propiedades.

### Paso 4: Comprobar el aislamiento

Descomenta el PASO 4. Aunque el PASO 3 creo `Mouse`, este test solo ve el item inicial porque `beforeEach` creo una app nueva. Prueba a mover `app = createApp()` fuera del `beforeEach` (a nivel de modulo) y observa como falla; despues deshaz el cambio.

Compara con `solution/app.test.js`.

## Comando sugerido

```bash
pnpm test app.test.js
```
