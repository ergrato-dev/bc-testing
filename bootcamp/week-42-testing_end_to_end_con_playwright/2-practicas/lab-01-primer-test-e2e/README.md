# Lab 01 - Primer test E2E

## Objetivo

Instalar Playwright con pnpm, dejar que arranque la aplicación de la biblioteca y escribir los dos primeros tests: uno que lee el catálogo y otro que navega a la ficha de un libro. Después, ver cómo falla una aserción que reintenta y qué hace `reuseExistingServer`.

## Tiempo estimado

55 minutos.

## Preparación

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `package.json` | El paquete `lab01`, con `@playwright/test` 1.63.0 en `devDependencies` ([teoría 01](../../1-teoria/01-e2e-y-playwright.md#3-instalar)) |
| `playwright.config.ts` | La configuración: `baseURL`, `webServer` y un proyecto con Chromium ([teoría 01](../../1-teoria/01-e2e-y-playwright.md#4-la-configuración)) |
| `app/server.mjs` | La aplicación bajo prueba: un catálogo web con Node y sin dependencias. Los datos viven en memoria y vuelven a los iniciales cada vez que arranca |
| `app/domain.mjs` | El dominio: libros con ISBN, título, género, páginas y «solo consulta en sala»; las acciones «Prestar» y «Devolver»; cuatro libros iniciales |
| `tests/catalog.spec.ts` | Los tests, comentados por PASOS |

No hace falta leer `server.mjs`: un test E2E prueba la aplicación desde fuera, como la usa una persona.

1. Instala las dependencias y Chromium:

   ```bash
   cd starter
   pnpm install
   pnpm exec playwright install --with-deps chromium
   ```

   En Linux, `--with-deps` pide tu contraseña de `sudo` para instalar las bibliotecas del navegador. En Windows y macOS no hace falta. Chromium se descarga una vez por equipo en `~/.cache/ms-playwright/`.

2. Arranca la aplicación para verla:

   ```bash
   pnpm start
   # Biblioteca municipal en http://localhost:3000
   ```

   Abre `http://localhost:3000` en el navegador: el catálogo, el buscador, el enlace «Nuevo libro» y la ficha de cada libro. Para la aplicación con `Ctrl+C`: a partir de aquí la arranca Playwright.

## Paso a paso

### Paso 1: sin tests

```bash
pnpm exec playwright test
# Error: No tests found
```

Playwright busca `*.spec.ts` en `tests/` (la opción `testDir`) y todos están comentados. Termina con código 1: una suite vacía es un error, no un éxito.

### Paso 2: el primer test (PASO 1)

1. En `tests/catalog.spec.ts`, descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 1 test using 1 worker
   #
   #   ✓  1 [chromium] › tests/catalog.spec.ts:4:5 › the catalog lists the seeded books (132ms)
   #
   #   1 passed (874ms)
   ```

| Pieza | Qué hace |
|---|---|
| `page.goto('/')` | Navega a `baseURL` + `/`: `http://localhost:3000/` |
| `toHaveTitle('Libros - Biblioteca municipal')` | El `<title>` completo de la página |
| `getByRole('heading', { name: 'Libros' })` | El encabezado que dice «Libros» |
| `getByRole('link', { name: '9788420412146' })` | El enlace con el ISBN de «Rayuela» |
| `[chromium]` | El proyecto en el que se ejecutó |

No has arrancado la aplicación: `webServer` ejecutó `node app/server.mjs`, esperó a que `http://localhost:3000` respondiera, ejecutó el test y la paró.

### Paso 3: navegar (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 2 tests using 1 worker
   #
   #   ✓  1 [chromium] › tests/catalog.spec.ts:4:5 › the catalog lists the seeded books (125ms)
   #   ✓  2 [chromium] › tests/catalog.spec.ts:13:5 › a book opens its detail page (144ms)
   #
   #   2 passed (1.0s)
   ```

   `click()` sobre el enlace navega a la ficha; `toHaveURL('/items/9788420412146')` compara con la dirección relativa a `baseURL`. `getByTestId('status')` encuentra el `<dd data-testid="status">` con el estado del libro: no tiene un rol ni una etiqueta propios.

   Los dos tests están en el mismo archivo y Playwright usa un solo *worker*: reparte los archivos entre procesos, y los tests de un archivo se ejecutan en orden.

3. Prueba las opciones del día a día ([teoría 01](../../1-teoria/01-e2e-y-playwright.md#5-el-primer-test)):

   ```bash
   pnpm exec playwright test -g "detail"
   pnpm exec playwright test --list
   ```

## Experimentos

### Experimento 1: una aserción que no se cumple

1. En el primer test, cambia el título esperado por `'Biblioteca municipal'`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   #   ✘  1 [chromium] › tests/catalog.spec.ts:4:5 › the catalog lists the seeded books (5.1s)
   #   ✓  2 [chromium] › tests/catalog.spec.ts:13:5 › a book opens its detail page (186ms)
   # ...
   #     Error: expect(page).toHaveTitle(expected) failed
   #
   #     Expected: "Biblioteca municipal"
   #     Received: "Libros - Biblioteca municipal"
   #     Timeout:  5000ms
   #
   #     Call log:
   #       - Expect "toHaveTitle" with timeout 5000ms
   #         14 × locator resolved to <html lang="es">…</html>
   #            - unexpected value "Libros - Biblioteca municipal"
   # ...
   #     >  7 |   await expect(page).toHaveTitle('Biblioteca municipal');
   # ...
   #     Error Context: test-results/catalog-the-catalog-lists-the-seeded-books-chromium/error-context.md
   # ...
   #   1 failed
   #     [chromium] › tests/catalog.spec.ts:4:5 › the catalog lists the seeded books ────────────────────
   #   1 passed (6.6s)
   ```

   El test tardó 5.1 s en fallar: leyó el título 14 veces durante 5 s por si cambiaba. Es la espera de las aserciones de Playwright ([teoría 02](../../1-teoria/02-locators-acciones-y-aserciones.md#3-aserciones-que-reintentan)). Abre `error-context.md`: el error y la página escrita como texto.

3. Deshaz el cambio.

### Experimento 2: la aplicación ya arrancada

1. En otra terminal, dentro de `starter/`, arranca la aplicación con `pnpm start`.
2. Ejecuta los tests como en tu equipo y como en integración continua:

   ```bash
   pnpm exec playwright test
   # ...
   #   2 passed (843ms)

   CI=1 pnpm exec playwright test
   # Error: http://localhost:3000 is already used, make sure that nothing is running on the port/url or set reuseExistingServer:true in config.webServer.
   ```

   Sin `CI`, `reuseExistingServer` es `true`: Playwright usa la aplicación que arrancaste. Con `CI=1`, exige arrancarla él, y un puerto ocupado es un error. En integración continua, reutilizar un servidor que nadie sabe quién arrancó es probar otra cosa.

3. Para la aplicación de la otra terminal con `Ctrl+C`.

Recuerda este experimento en el lab 02: con la aplicación reutilizada, los datos que cambia un test siguen ahí en la ejecución siguiente.

## Comprobación final

```bash
pnpm exec playwright test
# ...
#   2 passed (1.0s)
```

## Preguntas de cierre

1. ¿Qué comprueba el test del Paso 3 que no comprobaría un test de API con Supertest ([semana 12](../../../week-12-apis_rest_con_supertest/README.md)) contra `GET /items/9788420412146`?
2. ¿Qué hace `webServer` y qué cambia con `reuseExistingServer`?
3. ¿Por qué el test del Experimento 1 tardó 5 s en fallar y no falló al instante?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Locators, acciones y aserciones](../lab-02-locators-acciones-y-aserciones/README.md) → |

[↑ Semana 42](../../README.md)
