# Prácticas - Semana 42

Cuatro labs guiados (4 h en total) sobre la misma aplicación: el catálogo web de una biblioteca, en `starter/app/`, que Playwright arranca solo. Los tests, los page objects y las opciones nuevas de la configuración están comentados por PASOS; lo que no es descomentar (provocar un fallo, cambiar un texto de la aplicación) está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer test E2E](lab-01-primer-test-e2e/README.md) | Instalar Playwright con pnpm, `webServer` y `baseURL`, `page.goto`, `toHaveTitle`, `toHaveURL` y una aserción que reintenta | 55 min |
| [Lab 02 - Locators, acciones y aserciones](lab-02-locators-acciones-y-aserciones/README.md) | `getByRole`, `getByLabel`, `getByTestId`, `fill`, `selectOption`, `check`, `hover`, datos por la API con `request`; modo estricto, lecturas sin espera y datos compartidos | 65 min |
| [Lab 03 - Page Object Model y fixtures](lab-03-page-object-model-y-fixtures/README.md) | Tres page objects, `test.extend` con page objects y un libro nuevo por test, y un cambio de interfaz que se arregla en una línea | 60 min |
| [Lab 04 - Evidencias y navegadores](lab-04-evidencias-y-navegadores/README.md) | Captura, video, traza e informe HTML de un fallo; `test.step` y `attach`; Chromium, Firefox y WebKit, este en la imagen Docker de Playwright | 60 min |

## Antes de empezar

```bash
node --version
# v24.19.0
pnpm --version
# 12.6.0
```

Necesitas Node 22 o superior. Si `pnpm --version` da otra versión, no pasa nada: el campo `packageManager` de cada `package.json` hace que pnpm use la 12.6.0.

Instala Chromium una vez para todos los labs ([teoría 01](../1-teoria/01-e2e-y-playwright.md#3-instalar)), desde el `starter/` del lab 01 después de `pnpm install`:

```bash
pnpm exec playwright install --with-deps chromium
```

El lab 04 añade Firefox y WebKit, y usa Docker para WebKit si tu Linux no tiene sus bibliotecas.

Cada lab es un paquete independiente:

```bash
cd lab-NN-.../starter
pnpm install
pnpm exec playwright test
```

`test-results/` y `playwright-report/` se crean al ejecutar los tests y no se suben al repositorio.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Evidencias, reportes y navegadores](../1-teoria/04-evidencias-reportes-y-navegadores.md) | [Lab 01 - Primer test E2E](lab-01-primer-test-e2e/README.md) → |

[↑ Semana 42](../README.md)
