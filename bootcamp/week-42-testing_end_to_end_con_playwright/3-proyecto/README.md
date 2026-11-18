# Proyecto - Suite E2E del dominio con Playwright

## Objetivo

Añadir a tu repositorio `qa-<nombre>-<apellido>` la carpeta `e2e/`: una aplicación web de tu dominio y una suite de Playwright de al menos 10 tests sobre su flujo principal, con page objects, fixtures que preparan los datos por la API, capturas de evidencia en un informe HTML y ejecución en Chromium y Firefox.

## Tiempo estimado

3 horas.

## Punto de partida

Las APIs de las etapas anteriores (Express, Spring Boot, Go, axum) no tienen interfaz web, y un test E2E necesita una. `starter/e2e/` trae la misma aplicación de los labs: un servidor Node sin dependencias, con los datos en memoria, cuyo dominio se describe en un solo archivo, `app/domain.mjs`. Tú describes tu dominio en ese archivo; `server.mjs` no se toca.

| Archivo | Qué es | Qué haces |
|---|---|---|
| `package.json` | `@playwright/test` 1.63.0 | Nada |
| `app/server.mjs` | Catálogo con búsqueda y filtro, formulario de alta con validación, ficha con acciones y la API JSON `/api/items` | Nada |
| `app/domain.mjs` | El dominio de ejemplo: la biblioteca de los labs | Lo sustituyes por el tuyo (paso 2) |
| `playwright.config.ts` | `baseURL`, `webServer` y Chromium | Añades informe, evidencias y navegadores (paso 6) |
| `fixtures.ts` | Un `test.extend({})` vacío | Le añades tus fixtures (paso 4) |
| `tests/catalog.spec.ts` | Un test sin page objects | Lo adaptas y lo pasas a page objects (paso 3) |

## Paso a paso

### Paso 1: la carpeta `e2e/`

1. En tu repositorio, crea la rama de la semana:

   ```bash
   git switch -c week-42
   ```

2. Copia `starter/e2e/` en la raíz de tu repositorio, al lado de `js/`, `python/`, `go/` o `rust/`.
3. Instala y ejecuta el test de ejemplo:

   ```bash
   cd e2e
   pnpm install
   pnpm exec playwright install --with-deps chromium firefox
   pnpm exec playwright test
   # Running 1 test using 1 worker
   #
   #   ✓  1 [chromium] › tests/catalog.spec.ts:5:5 › the catalog lists the seeded items (133ms)
   #
   #   1 passed (798ms)
   ```

4. Comprueba que `.gitignore` de tu repositorio excluye `node_modules/`, `test-results/` y `playwright-report/`.

### Paso 2: tu dominio en `app/domain.mjs`

Lee el ejemplo de la biblioteca y sustitúyelo por el sistema de tu dominio, el mismo de las etapas anteriores. Cada propiedad:

| Propiedad | Qué es | En la biblioteca |
|---|---|---|
| `app`, `plural`, `newLabel` | El nombre del sistema, el título del catálogo y el texto del enlace de alta | `'Biblioteca municipal'`, `'Libros'`, `'Nuevo libro'` |
| `fields` | Los campos del formulario. **El primero es la clave**: única y parte de la URL | ISBN, título, género, páginas y «solo consulta en sala» |
| `type` | `text`, `number`, `select` (con `options`) o `checkbox` | |
| `required`, `min`, `max`, `pattern` + `patternMessage` | Las reglas de validación. Usa las de tu dominio: las mismas que validan tus suites de las etapas anteriores | El ISBN tiene 13 cifras |
| `titleField`, `listFields` | El campo que titula la ficha y los que aparecen en el catálogo | `title`; `title` y `genre` |
| `initialStatus`, `filterLabel`, `statusHelp` | El estado de un elemento nuevo, el texto de la casilla que filtra por ese estado y la explicación de cada estado | `'Disponible'`, `'Solo disponibles'` |
| `actions` | Las acciones de la ficha: `label`, `allowed(item)` (cuándo está habilitada), `apply(item)` (qué cambia) y `done` (el mensaje) | Prestar y Devolver |
| `seed` | Los datos iniciales. Pon al menos uno en cada estado y uno al que alguna acción no se le pueda aplicar | Cuatro libros, uno prestado y uno de solo consulta |

Las acciones son la parte importante: elige las reglas de negocio de tu dominio que la interfaz debe respetar (un estado que impide otra acción, un campo que bloquea una operación). Arranca con `pnpm start` y recorre la aplicación en el navegador antes de escribir ningún test.

Quita el `TODO` de la cabecera.

### Paso 3: page objects

Crea `pages/` con al menos dos page objects, como en el [lab 03](../2-practicas/lab-03-page-object-model-y-fixtures/README.md): el catálogo, el formulario de alta y la ficha. Cada uno con sus locators como campos y métodos con nombre de acción de tu dominio ([teoría 03](../1-teoria/03-page-object-model-y-fixtures.md#1-un-page-object)).

Después, adapta `tests/catalog.spec.ts` a tu dominio y pásalo a page objects. Ningún archivo de `tests/` usa `page.getBy...` ni `page.locator`: si un test necesita encontrar algo, falta un locator o un método en su page object. `page` en sí sí se puede usar, por ejemplo para `expect(page).toHaveURL(...)` o `page.screenshot()`.

### Paso 4: fixtures

En `fixtures.ts`, un fixture por page object y uno que cree por la API un elemento nuevo, con clave aleatoria, para cada test que lo pida ([teoría 03](../1-teoria/03-page-object-model-y-fixtures.md#3-los-datos-de-cada-test)). La API es `POST /api/items` con los campos en JSON; responde 201 con el elemento o 422 con `{ "errors": [...] }`.

La clave aleatoria tiene que cumplir tu `pattern`. Y tiene que ser variada de verdad: con 1000 claves posibles y una aplicación que se reutiliza entre ejecuciones, dos tests acaban creando la misma.

Los tests importan `test` y `expect` de `../fixtures`.

### Paso 5: al menos 10 tests

Reparte los tests por lo que prueban. Una referencia:

| Archivo | Qué prueba |
|---|---|
| `catalog.spec.ts` | Buscar, filtrar por estado, la explicación de un estado con `hover`, abrir una ficha |
| `<elemento>-form.spec.ts` | Alta correcta; formulario vacío; un campo con formato inválido; un número fuera de rango; una clave repetida |
| `<acción>.spec.ts` | Cada acción permitida y su vuelta atrás; cada acción bloqueada por una regla del dominio |
| `evidence.spec.ts` | El flujo principal de principio a fin (paso 6) |

Reglas de la [teoría 02](../1-teoria/02-locators-acciones-y-aserciones.md) y la [teoría 03](../1-teoria/03-page-object-model-y-fixtures.md):

- Todas las comprobaciones con `await expect(...)`: nada de `expect(await ...)`, `waitForTimeout` ni `setTimeout`.
- Los datos iniciales solo se leen. Lo que un test cambia, lo crea él.
- Lo que crea un test se localiza por su clave, que es única, no por un nombre que otro test (u otro navegador) puede repetir.
- La validación detallada es de los tests unitarios: aquí basta con comprobar que la interfaz muestra los errores.

```bash
pnpm exec playwright test --list --project=chromium | tail -1
# Total: 13 tests in 4 files
```

### Paso 6: evidencias, informe y navegadores

1. En `playwright.config.ts`, como en el [lab 04](../2-practicas/lab-04-evidencias-y-navegadores/README.md):
   - `reporter: [['list'], ['html', { open: 'never' }]]`
   - en `use`: `screenshot: 'only-on-failure'`, `video: 'retain-on-failure'` y `trace: 'retain-on-failure'`
   - en `projects`: `firefox` y `webkit`
2. Escribe `tests/evidence.spec.ts`: el flujo principal de tu dominio en dos o tres `test.step`, con una captura adjunta al final de cada paso (`test.info().attach`) ([teoría 04](../1-teoria/04-evidencias-reportes-y-navegadores.md#2-evidencias-que-pides-tú)).
3. Ejecuta en los dos navegadores y abre el informe. Con 13 tests:

   ```bash
   pnpm exec playwright test --project=chromium --project=firefox
   # Running 26 tests using 4 workers
   # ...
   #   26 passed (11.3s)

   pnpm exec playwright show-report
   ```

   Comprueba en el informe que el test de evidencias tiene sus pasos y sus capturas.

4. Si tienes Docker, ejecuta también WebKit con la imagen oficial ([lab 04, paso 4](../2-practicas/lab-04-evidencias-y-navegadores/README.md#paso-4-webkit-en-docker)).

5. Ejecuta tres veces seguidas los dos navegadores. Si algún test falla una vez y pasa otra, es inestable: busca el dato compartido o la aserción que no espera, y arréglalo antes de entregar.

### Paso 7: verificar y entregar

```bash
cd ~/bc-testing
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 42
```

Haz commit de `e2e/` sin `node_modules/`, `test-results/` ni `playwright-report/`, sube la rama y abre el pull request `week-42` → `main`. En la descripción del PR, pega la lista de tests (`--list`) y una de las capturas de evidencia.

## ✅ Requisitos verificables

Los comprueba `scripts/verificar-semana.sh 42` ([checks.json](../checks.json)):

| Comprobación | Paso |
|---|---|
| La suite E2E pasa en Chromium y en Firefox, con la aplicación arrancada por Playwright | 1, 5 y 6 |
| Al menos 10 tests E2E | 5 |
| Al menos dos page objects en `pages/` | 3 |
| Los tests no buscan elementos: usan los page objects | 3 |
| `fixtures.ts` entrega los page objects y crea por la API los datos que cambia cada test | 4 |
| Informe HTML, captura y traza de cada fallo, y capturas de evidencia adjuntas al informe | 6 |
| Ninguna espera fija: ni `waitForTimeout` ni `setTimeout` | 5 |
| No queda ningún `TODO` en `e2e/` | 2 a 6 |

La primera comprobación ejecuta `pnpm install` y la suite con `CI=1`: necesita Chromium y Firefox instalados.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Evidencias y navegadores](../2-practicas/lab-04-evidencias-y-navegadores/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 42](../README.md)
