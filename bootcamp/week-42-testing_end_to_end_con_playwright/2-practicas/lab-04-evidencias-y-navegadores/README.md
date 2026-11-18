# Lab 04 - Evidencias y navegadores

## Objetivo

Hacer fallar un test a propósito y leer todo lo que Playwright guarda: captura, video, traza e informe HTML. Después, documentar el flujo principal con capturas en el informe y ejecutar la suite en Chromium, Firefox y WebKit, este último en la imagen Docker oficial de Playwright.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
pnpm install
pnpm exec playwright install --with-deps firefox
```

Es la suite del [lab 03](../lab-03-page-object-model-y-fixtures/README.md) terminada: page objects, fixtures y 8 tests. Lo nuevo, comentado por PASOS:

| Archivo | PASO | Qué es |
|---|---|---|
| `playwright.config.ts` | 1 | El reporter HTML y las evidencias de cada fallo |
| `tests/evidence.spec.ts` | 2 | El flujo principal con `test.step` y capturas adjuntas |
| `playwright.config.ts` | 3 | Los proyectos `firefox` y `webkit` |

Para el Paso 4 necesitas Docker en marcha.

## Paso a paso

### Paso 1: lo que queda de un fallo (PASO 1)

1. Haz fallar un test: en `tests/lending.spec.ts`, cambia `toHaveText('Libro prestado')` por `toHaveText('Libro reservado')`.
2. Ejecuta y mira qué queda:

   ```bash
   pnpm exec playwright test
   # ...
   #   1 failed
   #     [chromium] › tests/lending.spec.ts:3:5 › a book can be lent and returned ───────────────────────
   #   7 passed (7.3s)

   ls test-results/*/
   # error-context.md
   ```

   Sin configurar nada, solo queda `error-context.md`: el error y la página en texto.

3. En `playwright.config.ts`, descomenta las dos líneas bajo `// PASO 1`: la de `reporter` y las tres de `use`.
4. Ejecuta otra vez:

   ```bash
   pnpm exec playwright test
   # Running 8 tests using 3 workers
   #
   #   ✘  3 [chromium] › tests/lending.spec.ts:3:5 › a book can be lent and returned (5.7s)
   # ...
   #     Error: expect(locator).toHaveText(expected) failed
   #
   #     Locator:  getByRole('status')
   #     Expected: "Libro reservado"
   #     Received: "Libro prestado"
   #     Timeout:  5000ms
   # ...
   #     attachment #1: screenshot (image/png) ──────────────────────────────────────────────────────────
   #     test-results/lending-a-book-can-be-lent-and-returned-chromium/test-failed-1.png
   #     ────────────────────────────────────────────────────────────────────────────────────────────────
   #     attachment #2: video (video/webm) ──────────────────────────────────────────────────────────────
   #     test-results/lending-a-book-can-be-lent-and-returned-chromium/video.webm
   #     ────────────────────────────────────────────────────────────────────────────────────────────────
   #     Error Context: test-results/lending-a-book-can-be-lent-and-returned-chromium/error-context.md
   #     attachment #4: trace (application/zip) ─────────────────────────────────────────────────────────
   #     test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
   #     Usage:
   #         pnpm exec playwright show-trace test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
   # ...
   #   1 failed
   #   7 passed (8.0s)

   find test-results -type f | sort
   # test-results/.last-run.json
   # test-results/lending-a-book-can-be-lent-and-returned-chromium/error-context.md
   # test-results/lending-a-book-can-be-lent-and-returned-chromium/test-failed-1.png
   # test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
   # test-results/lending-a-book-can-be-lent-and-returned-chromium/video.webm
   ```

   Solo hay una carpeta: con `only-on-failure` y `retain-on-failure`, lo de los 7 tests que pasan se borra. `.last-run.json` es lo que usa `--last-failed`.

5. Abre las evidencias ([teoría 04](../../1-teoria/04-evidencias-reportes-y-navegadores.md#abrir-el-informe-y-la-traza)):

   ```bash
   pnpm exec playwright show-report
   #   Serving HTML report at http://localhost:9323. Press Ctrl+C to quit.
   ```

   Abre `http://localhost:9323`. Pulsa el test que falló: el error, los pasos con su duración, la captura (el mensaje dice «Libro prestado», la aserción esperaba otra cosa), el video y la traza. Para el servidor con `Ctrl+C`.

   ```bash
   pnpm exec playwright show-trace --port 9322 test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
   # Listening on http://localhost:9322
   ```

   Abre `http://localhost:9322`. A la izquierda, cada acción; al pulsar una, la página antes y después, y en las pestañas de abajo el log de la acción, la consola y la red. Busca la petición `POST /items/.../actions/0` que hizo el botón «Prestar». Para el servidor con `Ctrl+C`.

6. Deshaz el cambio de `lending.spec.ts`.

### Paso 2: capturas de evidencia (PASO 2)

1. En `tests/evidence.spec.ts`, descomenta el bloque `// PASO 2`.
2. Ejecuta solo ese test:

   ```bash
   pnpm exec playwright test evidence
   # Running 1 test using 1 worker
   #
   #   ✓  1 [chromium] › tests/evidence.spec.ts:4:5 › main flow: add a book and lend it (1.7s)
   #
   #   1 passed (2.5s)
   ```

3. Abre el informe con `pnpm exec playwright show-report`. El test aparece con dos pasos, `add the book` y `lend the book`, y dos adjuntos, `1-catalog` y `2-lent`: el catálogo con el mensaje de alta y la ficha con el libro prestado. Esas capturas se guardan aunque el test pase.

| Pieza | Qué hace |
|---|---|
| `test.step('add the book', async () => {...})` | Agrupa las acciones del alta bajo un nombre en el informe y en la traza |
| `page.screenshot()` | La captura de la página, como `Buffer` |
| `test.info().attach('1-catalog', { body, contentType })` | La adjunta al informe del test |

La captura va después de la aserción: si el test llega a la captura, lo que se ve ya está comprobado.

### Paso 3: Firefox (PASO 3)

1. En `playwright.config.ts`, descomenta las dos líneas bajo `// PASO 3`.
2. Ejecuta Chromium y Firefox:

   ```bash
   pnpm exec playwright test --project=chromium --project=firefox
   # Running 18 tests using 4 workers
   #
   #   ✓   1 [chromium] › tests/search.spec.ts:4:5 › searching filters the catalog (478ms)
   # ...
   #   ✓   9 [firefox] › tests/lending.spec.ts:3:5 › a book can be lent and returned (3.1s)
   # ...
   #   18 passed (8.9s)
   ```

   Nueve tests en dos proyectos: 18 ejecuciones. Cada uno indica su navegador entre corchetes.

3. Prueba WebKit en tu equipo:

   ```bash
   pnpm exec playwright install webkit
   pnpm exec playwright test --project=webkit
   ```

   En Windows y macOS pasa. En Linux, sin las bibliotecas del sistema, falla cada test al arrancar el navegador:

   ```text
     ✘  1 [webkit] › tests/lending.spec.ts:3:5 › a book can be lent and returned (2ms)
   ...
       Error: browserType.launch: 
       ╔══════════════════════════════════════════════════════╗
       ║ Host system is missing dependencies to run browsers. ║
       ║ Missing libraries:                                   ║
       ║     libgtk-4.so.1                                    ║
       ║     libgraphene-1.0.so.0                             ║
   ...
     9 failed
   ```

   `pnpm exec playwright install --with-deps webkit` las instala con `sudo`. Si no puedes, sigue con el Paso 4.

### Paso 4: WebKit en Docker

La imagen oficial de Playwright trae los tres navegadores con sus bibliotecas. Su versión es la de `@playwright/test` ([teoría 04](../../1-teoria/04-evidencias-reportes-y-navegadores.md#4-varios-navegadores)):

```bash
docker pull mcr.microsoft.com/playwright:v1.63.0-noble
docker run --rm --network host --user "$(id -u):$(id -g)" -e HOME=/tmp \
  -v "$PWD":/work -w /work mcr.microsoft.com/playwright:v1.63.0-noble \
  node_modules/.bin/playwright test --project=webkit
# Running 9 tests using 4 workers
#
#   ✓  1 [webkit] › tests/search.spec.ts:4:5 › searching filters the catalog (1.0s)
# ...
#   ✓  9 [webkit] › tests/lending.spec.ts:24:5 › a book saved as reference cannot be lent (1.1s)
#
#   9 passed (5.8s)
```

La imagen ocupa 3.5 GB: la primera descarga tarda. `--network host` comparte la red del equipo con el contenedor; en Docker Desktop hay que activar *host networking* en su configuración. El informe y `test-results/` quedan en tu carpeta, a tu nombre.

## Experimentos

### Experimento 1: dos navegadores, un servidor

1. Crea `tests/seed.spec.ts`, el test del [lab 02](../lab-02-locators-acciones-y-aserciones/README.md#experimento-3-datos-compartidos) que presta un libro inicial, ya con page objects:

   ```ts
   import { test, expect } from '../fixtures';

   test('lending a seeded book', async ({ bookPage }) => {
     await bookPage.goto('9788420412146');

     await bookPage.lend();

     await expect(bookPage.status).toHaveText('Prestado');
   });
   ```

2. Ejecuta en los dos navegadores:

   ```bash
   pnpm exec playwright test seed --project=chromium --project=firefox
   #   ✓  2 [chromium] › tests/seed.spec.ts:3:5 › lending a seeded book (1.2s)
   #   ✘  1 [firefox] › tests/seed.spec.ts:3:5 › lending a seeded book (30.3s)
   # ...
   #           - element is not enabled
   # ...
   #   1 failed
   #   1 passed (34.3s)
   ```

   Los proyectos comparten el `webServer`: el primero que llega presta «Rayuela» y el segundo encuentra el botón deshabilitado. Cuál de los dos falla puede cambiar de una ejecución a otra. Con un ISBN propio (el fixture `newBook`) no pasa.

3. Borra `tests/seed.spec.ts`.

### Experimento 2: cuánto cuesta grabar siempre

```bash
rm -rf test-results
time pnpm exec playwright test --project=chromium
# ... 4.446 total
rm -rf test-results
time pnpm exec playwright test --project=chromium --trace on
# ... 4.415 total
du -sh test-results
# 992K	test-results
```

`--trace on` graba la traza de todos los tests, también de los que pasan. En esta suite no se nota en el tiempo: 9 trazas, unos 110 KB cada una. En una suite de cientos de tests en integración continua, son cientos de megas por ejecución que nadie mira. Por eso `retain-on-failure` o `on-first-retry`: la evidencia de lo que falla, nada más.

## Comprobación final

```bash
pnpm exec playwright test --project=chromium --project=firefox
# ...
#   18 passed (8.9s)
```

## Preguntas de cierre

1. ¿Qué te dice la traza que no te dicen la captura ni el mensaje de error?
2. ¿Qué diferencia hay entre `retain-on-failure` y `on-first-retry`? ¿Cuál usarías en integración continua con `retries: 2`?
3. ¿Para qué sirve adjuntar capturas a un test que pasa?
4. ¿Por qué la imagen Docker tiene que ser `v1.63.0`, la misma versión que `@playwright/test`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Page Object Model y fixtures](../lab-03-page-object-model-y-fixtures/README.md) | [Proyecto — Proyecto - Suite E2E del dominio con Playwright](../../3-proyecto/README.md) → |

[↑ Semana 42](../../README.md)
