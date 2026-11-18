# Glosario - Semana 42

| Término | Definición |
|---|---|
| Test end-to-end (E2E) | Test que usa la aplicación como una persona: navegador, interfaz, servidor y datos. |
| Playwright | Biblioteca de Microsoft para controlar Chromium, Firefox y WebKit. |
| Playwright Test | El ejecutor de tests de Playwright (`@playwright/test`): `test`, `expect`, fixtures, paralelismo e informes. |
| Headless | Navegador sin ventana. Es el modo por defecto; `--headed` muestra la ventana. |
| Contexto del navegador | Sesión aislada (cookies, almacenamiento) que Playwright crea para cada test. |
| `webServer` | Opción de configuración con la que Playwright arranca la aplicación antes de los tests. |
| `baseURL` | Dirección base: `page.goto('/')` navega a ella. |
| Proyecto (*project*) | Una configuración con la que se ejecuta toda la suite, normalmente un navegador. |
| Worker | Proceso que ejecuta tests. Playwright reparte los archivos entre varios workers. |
| Locator | Descripción de cómo encontrar un elemento. Busca cada vez que se usa. |
| `getByRole` | Locator por rol de accesibilidad y nombre visible: el preferido. |
| Rol de accesibilidad | Lo que es un elemento para un lector de pantalla: `button`, `link`, `heading`, `row`... |
| `data-testid` | Atributo HTML para encontrar con `getByTestId` lo que no tiene un rol ni un texto estable. |
| Modo estricto | Una acción sobre un locator que encuentra varios elementos falla en lugar de elegir uno. |
| Actionability | Las comprobaciones previas a una acción: único, visible, quieto, habilitado y sin nada encima. |
| Auto-wait | La espera automática de Playwright en acciones y aserciones. |
| Aserción web-first | Aserción que reintenta hasta que se cumple o pasan 5 s: `await expect(locator).toHaveText(...)`. |
| Page Object Model (POM) | Patrón que reúne en una clase los locators y las acciones de una página. |
| Page object | Cada una de esas clases. |
| Fixture | Lo que un test pide en sus parámetros (`page`, `request` o uno propio de `test.extend`). Lo de antes de `use` prepara y lo de después limpia. |
| `request` | Fixture con un cliente HTTP que usa `baseURL`: sirve para preparar datos por la API. |
| Test inestable (*flaky*) | Test que pasa y falla sin cambios en el código. Playwright lo marca así si pasa en un reintento. |
| Trace | Archivo `trace.zip` con cada acción de un test, el DOM antes y después, la red y la consola. |
| Trace viewer | Visor de trazas: `pnpm exec playwright show-trace`. |
| Informe HTML | `playwright-report/index.html`: cada test con pasos, errores y adjuntos. |
| `test.step` | Agrupa acciones bajo un nombre en el informe y en la traza. |
| Adjunto (*attachment*) | Archivo unido al resultado de un test (captura, video, traza) con `test.info().attach`. |
| WebKit | El motor de Safari. Playwright lo usa para probar Safari en cualquier sistema. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 42](../README.md)
