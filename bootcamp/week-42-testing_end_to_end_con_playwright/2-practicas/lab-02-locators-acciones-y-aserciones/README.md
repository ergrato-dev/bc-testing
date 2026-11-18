# Lab 02 - Locators, acciones y aserciones

## Objetivo

Buscar, filtrar, rellenar el formulario de alta, pasar el ratón por un estado y prestar un libro, con locators por rol y por etiqueta y aserciones que reintentan. En los experimentos, romper el test de tres formas que verás en suites reales: leer la página sin esperar, un locator que encuentra varios elementos y datos compartidos entre ejecuciones.

## Tiempo estimado

65 minutos.

## Preparación

```bash
cd starter
pnpm install
```

Es la aplicación del [lab 01](../lab-01-primer-test-e2e/README.md), con la misma configuración. Chromium ya está instalado. Los tests están en tres archivos, comentados por PASOS:

| Archivo | PASOS |
|---|---|
| `tests/search.spec.ts` | 1: buscar, filtrar y pasar el ratón |
| `tests/new-book.spec.ts` | 2: rellenar el formulario; 3: los errores de validación |
| `tests/lending.spec.ts` | 4: preparar datos por la API y prestar |

Arranca la aplicación con `pnpm start` y prueba en el navegador lo que vas a automatizar: busca «rayuela», marca «Solo disponibles», pasa el ratón por un estado, guarda un libro sin rellenar nada y presta un libro desde su ficha. Para la aplicación antes de seguir.

## Paso a paso

### Paso 1: buscar, filtrar y pasar el ratón (PASO 1)

1. En `tests/search.spec.ts`, descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 3 tests using 1 worker
   #
   #   ✓  1 [chromium] › tests/search.spec.ts:4:5 › searching filters the catalog (209ms)
   #   ✓  2 [chromium] › tests/search.spec.ts:15:5 › the filter hides books on loan (172ms)
   #   ✓  3 [chromium] › tests/search.spec.ts:25:5 › hovering a status explains it (129ms)
   #
   #   3 passed (1.2s)
   ```

| Línea | Qué hace |
|---|---|
| `getByRole('searchbox', { name: 'Buscar' }).fill('rayuela')` | Un `<input type="search">` tiene el rol `searchbox`; su nombre es el texto de su `<label>` |
| `toHaveCount(2)` sobre `getByRole('row')` | La fila de cabecera y la de «Rayuela» |
| `getByLabel('Solo disponibles').check()` | La casilla, por el texto de su etiqueta |
| `toHaveCount(0)` | Comprueba que **no** hay ninguna fila con «Veinte poemas»: el libro está prestado |
| `getByRole('row', { name: /Veinte poemas/ }).getByText('Prestado')` | Encadenado: el texto «Prestado» dentro de la fila de ese libro |
| `hover()` y `getByRole('tooltip')` | La explicación del estado aparece con `:hover`. Mientras está oculta no tiene rol, así que `getByRole('tooltip')` solo encuentra la visible |

### Paso 2: el formulario (PASO 2)

1. En `tests/new-book.spec.ts`, descomenta el bloque `// PASO 2`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 4 tests using 2 workers
   # ...
   #   ✓  2 [chromium] › tests/new-book.spec.ts:7:5 › a new book appears in the catalog (730ms)
   #
   #   4 passed (1.5s)
   ```

   Con dos archivos, Playwright usa dos *workers*: los archivos se ejecutan en paralelo.

| Línea | Qué hace |
|---|---|
| `newIsbn()` | Un ISBN aleatorio que empieza por 979. El catálogo no admite dos libros con el mismo, y la aplicación puede seguir arrancada de una ejecución anterior |
| `getByLabel('ISBN').fill(isbn)` | El campo cuyo `<label for>` dice «ISBN» |
| `getByLabel('Género').selectOption('Ensayo')` | Elige la opción por su texto |
| `toHaveText(`${isbn} guardado`)` | El mensaje de confirmación del catálogo, con el rol `status` |

El test tarda unos 700 ms: el servidor espera 400 ms antes de guardar, como una aplicación con base de datos. `click()` en «Guardar» espera a que termine la navegación del formulario, y `toHaveText` reintenta hasta encontrar el mensaje. No hay ninguna pausa en el test.

### Paso 3: los errores de validación (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `pnpm exec playwright test`: pasan 5.

`toHaveText([...])` con una lista comprueba cada `<li>` del `alert`, en orden y sin que sobre ninguno. `toHaveValue('')` comprueba que el campo sigue vacío después del error.

### Paso 4: datos por la API (PASO 4)

1. En `tests/lending.spec.ts`, descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 7 tests using 3 workers
   # ...
   #   ✓  2 [chromium] › tests/lending.spec.ts:6:5 › a book can be lent and returned (1.8s)
   #   ✓  7 [chromium] › tests/lending.spec.ts:25:5 › a reference book cannot be lent (89ms)
   #
   #   7 passed (2.7s)
   ```

| Línea | Qué hace |
|---|---|
| `async ({ page, request })` | `request` es otro fixture: un cliente HTTP con el mismo `baseURL` |
| `request.post('/api/items', { data: {...} })` | Crea el libro por la API JSON de la aplicación, sin pasar por el formulario: el test es del préstamo, no del alta |
| `await expect(created).toBeOK()` | La respuesta tiene un código 2xx. Si la preparación falla, el test falla aquí y no más adelante con un error confuso |
| `toBeDisabled()` | «Prestar» queda deshabilitado con el libro prestado, y siempre con un libro de solo consulta |

En la ficha, «Prestar» y «Devolver» envían la petición con `fetch` y actualizan la página sin recargarla.

## Experimentos

### Experimento 1: leer la página sin esperar

1. En `a book can be lent and returned`, sustituye la primera aserción después del clic, `await expect(page.getByRole('status')).toHaveText('Libro prestado');`, por:

   ```ts
   expect(await page.getByTestId('status').textContent()).toBe('Prestado');
   ```

2. Ejecuta:

   ```bash
   pnpm exec playwright test lending -g "lent and returned"
   #   ✘  1 [chromium] › tests/lending.spec.ts:6:5 › a book can be lent and returned (158ms)
   # ...
   #     Error: expect(received).toBe(expected) // Object.is equality
   #
   #     Expected: "Prestado"
   #     Received: "Disponible"
   #
   #       14 |   await page.getByRole('button', { name: 'Prestar' }).click();
   #       15 |
   #     > 16 |   expect(await page.getByTestId('status').textContent()).toBe('Prestado');
   ```

   `textContent()` leyó el estado justo después del clic, cuando `fetch` todavía esperaba al servidor. Es el `expect` de Jest: compara una vez. Con un servidor más rápido pasaría, y sería un test inestable ([teoría 02](../../1-teoria/02-locators-acciones-y-aserciones.md#las-aserciones-que-no-esperan)).

3. Deshaz el cambio.

### Experimento 2: un locator con varios elementos

1. En `hovering a status explains it`, sustituye el locator del `hover()` por `page.getByText('Disponible')`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test search -g hovering
   #   ✘  1 [chromium] › tests/search.spec.ts:25:5 › hovering a status explains it (145ms)
   # ...
   #     Error: locator.hover: Error: strict mode violation: getByText('Disponible') resolved to 4 elements:
   #         1) <label>…</label> aka getByText('Solo disponibles')
   #         2) <span>Disponible</span> aka getByText('Disponible').nth(1)
   #         3) <span>Disponible</span> aka getByText('Disponible').nth(2)
   #         4) <span>Disponible</span> aka getByText('Disponible').nth(3)
   ```

   Cuatro elementos, y el primero ni siquiera es un estado: `getByText` busca texto parcial sin distinguir mayúsculas, y «Solo disponibles» contiene «disponible». Playwright no elige uno: falla. Prueba `page.getByText('Disponible', { exact: true })`: quedan tres, y sigue fallando. La solución es la del Paso 1: la fila primero.

3. Deshaz el cambio.

### Experimento 3: datos compartidos

1. Crea `tests/seed.spec.ts`, que presta un libro de los datos iniciales:

   ```ts
   import { test, expect } from '@playwright/test';

   test('lending a seeded book', async ({ page }) => {
     await page.goto('/items/9788420412146');

     await page.getByRole('button', { name: 'Prestar' }).click();

     await expect(page.getByTestId('status')).toHaveText('Prestado');
   });
   ```

2. Arranca la aplicación en otra terminal con `pnpm start` y ejecuta el test dos veces:

   ```bash
   pnpm exec playwright test seed
   # ...
   #   1 passed (1.6s)

   pnpm exec playwright test seed
   #   ✘  1 [chromium] › tests/seed.spec.ts:3:5 › lending a seeded book (30.0s)
   # ...
   #     Test timeout of 30000ms exceeded.
   #
   #     Error: locator.click: Test timeout of 30000ms exceeded.
   #     Call log:
   #       - waiting for getByRole('button', { name: 'Prestar' })
   #         - locator resolved to <button disabled>Prestar</button>
   #       - attempting click action
   #         2 × waiting for element to be visible, enabled and stable
   #           - element is not enabled
   # ...
   #         58 × waiting for element to be visible, enabled and stable
   #            - element is not enabled
   ```

   La primera ejecución prestó «Rayuela» en la aplicación arrancada ([lab 01, Experimento 2](../lab-01-primer-test-e2e/README.md#experimento-2-la-aplicación-ya-arrancada)). La segunda encuentra el botón deshabilitado y `click()` espera 30 s a que se habilite. Para la aplicación de la otra terminal y vuelve a ejecutar: pasa, porque Playwright arranca una nueva con los datos iniciales.

   Por eso el test del Paso 4 crea su propio libro: los datos iniciales solo se leen ([teoría 03](../../1-teoria/03-page-object-model-y-fixtures.md#3-los-datos-de-cada-test)).

3. Para la aplicación de la otra terminal y borra `tests/seed.spec.ts`.

## Comprobación final

```bash
pnpm exec playwright test
# ...
#   7 passed (2.7s)
```

## Preguntas de cierre

1. ¿Por qué `getByRole('button', { name: 'Prestar' })` es mejor locator que `locator('form button')`? ¿Cuándo usarías `getByTestId`?
2. ¿Qué diferencia hay entre `await expect(locator).toHaveText(x)` y `expect(await locator.textContent()).toBe(x)`?
3. ¿Qué haces cuando una acción falla por modo estricto? ¿Por qué no `.nth(1)`?
4. ¿Por qué el test del préstamo crea el libro con `request` y no rellenando el formulario?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer test E2E](../lab-01-primer-test-e2e/README.md) | [Lab 03 - Page Object Model y fixtures](../lab-03-page-object-model-y-fixtures/README.md) → |

[↑ Semana 42](../../README.md)
