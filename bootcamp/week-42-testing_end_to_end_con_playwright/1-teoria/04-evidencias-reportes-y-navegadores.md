# 04 - Evidencias, reportes y navegadores

> Lenguaje: **TypeScript** (Playwright Test)

Cuando un test unitario falla, el mensaje suele bastar. Cuando falla un test E2E, la pregunta es qué había en la pantalla. Playwright guarda una **captura**, un **video** y una **traza** del test que falla, los junta en un **informe HTML** y ejecuta la misma suite en **Chromium, Firefox y WebKit**.

---

## 🎯 Objetivos

- Configurar capturas, videos y trazas de los tests que fallan, y leerlos.
- Generar el informe HTML y añadirle capturas de evidencia con `test.step` y `attach`.
- Ejecutar la suite en varios navegadores y en la imagen Docker oficial de Playwright.

---

## 1. Evidencias de cada fallo

```ts
export default defineConfig({
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: 'http://localhost:3000',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    trace: 'retain-on-failure',
  },
  // ...
});
```

| Opción | Valores habituales | Qué guarda |
|---|---|---|
| `screenshot` | `off`, `on`, `only-on-failure` | Una captura de la página al terminar el test |
| `video` | `off`, `on`, `retain-on-failure`, `on-first-retry` | Un video `.webm` del test completo |
| `trace` | `off`, `on`, `retain-on-failure`, `on-first-retry` | Una traza: cada acción con el DOM antes y después, la red y la consola |

`retain-on-failure` graba siempre y borra lo de los tests que pasan; `on-first-retry` solo graba cuando un test se reintenta ([sección 3](#3-reintentos-y-tests-inestables)). Lo que se guarda va a `test-results/`, una carpeta por test que falla:

```text
    attachment #1: screenshot (image/png) ──────────────────────────────────────────────────────────
    test-results/lending-a-book-can-be-lent-and-returned-chromium/test-failed-1.png
    ────────────────────────────────────────────────────────────────────────────────────────────────
    attachment #2: video (video/webm) ──────────────────────────────────────────────────────────────
    test-results/lending-a-book-can-be-lent-and-returned-chromium/video.webm
    ────────────────────────────────────────────────────────────────────────────────────────────────
    Error Context: test-results/lending-a-book-can-be-lent-and-returned-chromium/error-context.md
    attachment #4: trace (application/zip) ─────────────────────────────────────────────────────────
    test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
    Usage:
        pnpm exec playwright show-trace test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
```

| Archivo | Para qué |
|---|---|
| `test-failed-1.png` | Ver la página en el momento del fallo |
| `video.webm` | Ver cómo llegó ahí |
| `trace.zip` | Recorrer el test acción a acción: qué locator, qué encontró, qué había en la página antes y después |
| `error-context.md` | El error y la página como árbol de accesibilidad, en texto. Se genera siempre que un test falla |

`test-results/` se vacía al empezar cada ejecución. Esa carpeta y `playwright-report/` no se suben al repositorio.

### Abrir el informe y la traza

```bash
pnpm exec playwright show-report
#   Serving HTML report at http://localhost:9323. Press Ctrl+C to quit.

pnpm exec playwright show-trace --port 9322 test-results/lending-a-book-can-be-lent-and-returned-chromium/trace.zip
# Listening on http://localhost:9322
```

| Reporter | Qué escribe |
|---|---|
| `list` | Una línea por test en la terminal. Es el de por defecto |
| `html` | `playwright-report/index.html`: cada test con sus pasos, errores y adjuntos. `open: 'never'` evita que abra el navegador al fallar |
| `junit` | Un XML para la integración continua (semana 45) |

Sin `--port`, `show-trace` abre una ventana; con `--port` sirve el visor en esa dirección, y lo abres en tu navegador (también desde Windows con WSL). En el informe HTML, la traza de cada test se abre con un clic.

---

## 2. Evidencias que pides tú

Las capturas automáticas son de los fallos. Para documentar que un flujo funciona (un entregable, una auditoría, una demo), el test adjunta sus propias capturas al informe:

```ts
test('main flow: add a book and lend it', async ({ page, bookForm, bookPage }) => {
  await test.step('add the book', async () => {
    // ...
    await test.info().attach('1-catalog', { body: await page.screenshot(), contentType: 'image/png' });
  });

  await test.step('lend the book', async () => {
    // ...
    await test.info().attach('2-lent', { body: await page.screenshot(), contentType: 'image/png' });
  });
});
```

| Pieza | Qué hace |
|---|---|
| `test.step(nombre, fn)` | Agrupa acciones bajo un nombre en el informe y en la traza. Si falla, el informe dice en qué paso |
| `page.screenshot()` | Devuelve la captura como `Buffer`. Con `{ path: 'x.png' }` la escribe en un archivo; con `{ fullPage: true }`, la página entera |
| `test.info().attach(nombre, { body, contentType })` | Adjunta la captura al resultado del test: aparece en el informe HTML, pase o falle |

---

## 3. Reintentos y tests inestables

```ts
retries: process.env.CI ? 2 : 0,
```

Con `retries`, un test que falla se vuelve a ejecutar. Si pasa en un reintento, Playwright lo marca como **flaky**: el resultado es verde, pero el informe lo señala. Un test inestable es un error del test (datos compartidos, un locator ambiguo, una espera que falta) o de la aplicación, y los reintentos solo lo esconden. Úsalos en integración continua para no bloquear a todo el equipo, y revisa cada flaky. Con `trace: 'on-first-retry'`, la traza se graba en el reintento: tienes la evidencia del fallo sin pagar la traza en cada ejecución.

Un test que falla siempre por los datos (como el que presta un libro de los datos iniciales) no se vuelve verde con reintentos: falla las tres veces.

---

## 4. Varios navegadores

```ts
projects: [
  { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
  { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
  { name: 'webkit', use: { ...devices['Desktop Safari'] } },
],
```

Cada test se ejecuta una vez por proyecto: 9 tests y 2 proyectos son 18 ejecuciones. `--project=firefox` elige uno; se puede repetir.

```bash
pnpm exec playwright install --with-deps firefox webkit
pnpm exec playwright test --project=chromium --project=firefox
# Running 18 tests using 4 workers
# ...
#   18 passed (8.9s)
```

| Navegador | Motor | Para qué |
|---|---|---|
| Chromium | Blink | Chrome y Edge: la mayoría de usuarios |
| Firefox | Gecko | Diferencias reales en formularios, eventos y CSS |
| WebKit | WebKit | El motor de Safari. Es la forma de probar Safari en Linux y Windows |

WebKit es el que más bibliotecas del sistema necesita. Sin `--with-deps`, en Linux falla al arrancar:

```text
    Error: browserType.launch: 
    ╔══════════════════════════════════════════════════════╗
    ║ Host system is missing dependencies to run browsers. ║
    ║ Missing libraries:                                   ║
    ║     libgtk-4.so.1                                    ║
    ...
```

Si no puedes instalarlas (sin `sudo`, o con otra distribución), usa la **imagen Docker oficial**, que trae Node, los tres navegadores y sus bibliotecas en Ubuntu 24.04:

```bash
docker run --rm --network host --user "$(id -u):$(id -g)" -e HOME=/tmp \
  -v "$PWD":/work -w /work mcr.microsoft.com/playwright:v1.63.0-noble \
  node_modules/.bin/playwright test --project=webkit
# Running 9 tests using 4 workers
# ...
#   9 passed (5.8s)
```

| Parte | Qué / Para qué |
|---|---|
| `mcr.microsoft.com/playwright:v1.63.0-noble` | La versión de la imagen es la de `@playwright/test`: los navegadores de dentro son los que espera esa versión |
| `-v "$PWD":/work -w /work` | Monta tu carpeta, con su `node_modules`, y trabaja en ella |
| `node_modules/.bin/playwright test` | Ejecuta el Playwright que ya instalaste con pnpm, con el Node de la imagen |
| `--network host` | La aplicación que arranca `webServer` dentro del contenedor escucha en el puerto 3000 del equipo |
| `--user ... -e HOME=/tmp` | `test-results/` y el informe quedan a tu nombre, no a nombre de `root` |

Todos los proyectos usan el mismo `webServer`: un test que cambia datos compartidos pasa en el primer navegador y falla en el segundo.

---

## ✅ Resumen

- `screenshot: 'only-on-failure'`, `video` y `trace: 'retain-on-failure'` guardan en `test-results/` la evidencia de cada fallo; el reporter `html` la reúne en `playwright-report/`.
- `test.step` y `test.info().attach` documentan en el informe los flujos que pasan.
- `retries` marca como flaky lo que pasa al reintentar: es un aviso, no un arreglo.
- `projects` ejecuta la suite en cada navegador. Para WebKit en Linux sin dependencias, la imagen `mcr.microsoft.com/playwright` con la misma versión.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Page Object Model y fixtures](03-page-object-model-y-fixtures.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 42](../README.md)
