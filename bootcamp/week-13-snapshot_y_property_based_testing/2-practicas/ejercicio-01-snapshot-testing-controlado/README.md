# Ejercicio 01 - Snapshot Testing Controlado

## Objetivo

Crear snapshots utiles para detectar cambios de contrato en payloads estables.

## Tiempo estimado

90 minutos.

## Preparacion

```bash
cd starter
pnpm install
```

## Paso a paso

### Paso 1: Construir payload estable

Abre `starter/profile.presenter.test.js` y descomenta PASO 1.

### Paso 2: Snapshot con foco

Descomenta PASO 2 y ejecuta `pnpm test`: Jest crea `__snapshots__/profile.presenter.test.js.snap`. Abrelo y revisa que el contenido es el esperado antes de versionarlo.

### Paso 3: Snapshot de lista

Descomenta PASO 3 para validar estructura de lista serializada.

### Paso 4: Property matcher para campo volatil

Descomenta PASO 4. `buildProfileResponse` incluye `generatedAt`, que cambia en cada ejecucion; un snapshot normal fallaria siempre. `toMatchSnapshot({ generatedAt: expect.any(String) })` guarda `Any<String>` en ese campo y compara el resto exacto. Prueba a quitar el argumento, ejecuta dos veces y observa el fallo; despues restauralo.

### Paso 5: Inline snapshot

Descomenta PASO 5 y ejecuta `pnpm test` (sin `CI=true`, que impide escribir snapshots nuevos). Jest rellena `toMatchInlineSnapshot()` dentro del propio test con el valor serializado. Util para salidas cortas: el esperado queda visible junto al test.

### Paso 6: Revisar solucion

Compara con `solution/profile.presenter.test.js` y su carpeta `__snapshots__/`.

## Comando sugerido

```bash
pnpm test profile.presenter.test.js
```
