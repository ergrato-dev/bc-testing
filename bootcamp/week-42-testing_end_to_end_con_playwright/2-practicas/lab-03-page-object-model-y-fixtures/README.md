# Lab 03 - Page Object Model y fixtures

## Objetivo

Reescribir los tests del [lab 02](../lab-02-locators-acciones-y-aserciones/README.md) con tres page objects (catálogo, formulario y ficha) y un archivo de fixtures que los entrega y crea un libro nuevo por la API para cada test que lo pida. En el experimento, cambiar el texto de un botón y arreglar los tests en una sola línea.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
pnpm install
```

La aplicación y la configuración son las del lab 02. Lo nuevo, todo comentado por PASOS:

| Archivo | PASO | Qué es |
|---|---|---|
| `pages/catalog-page.ts` | 1 | `CatalogPage`: buscar, una fila por título, el mensaje y la explicación del estado |
| `tests/search.spec.ts` | 1 | Los tests del catálogo, con `CatalogPage` |
| `pages/book-form-page.ts` | 2 | `BookFormPage` y el tipo `Book`: rellenar, guardar y la lista de errores |
| `tests/new-book.spec.ts` | 2 | Los tests del formulario |
| `pages/book-page.ts` | 3 | `BookPage`: la ficha, prestar y devolver |
| `fixtures.ts` | 3 | El `test` con los page objects y `newBook` como fixtures |
| `tests/lending.spec.ts` | 3 | Los tests de préstamo, con fixtures |

Abre `tests/search.spec.ts` del lab 02 al lado: son los mismos tests.

## Paso a paso

### Paso 1: el catálogo (PASO 1)

1. Descomenta el bloque `// PASO 1` de `pages/catalog-page.ts` y el de `tests/search.spec.ts`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 3 tests using 1 worker
   #
   #   ✓  1 [chromium] › tests/search.spec.ts:5:5 › searching filters the catalog (212ms)
   #   ✓  2 [chromium] › tests/search.spec.ts:16:5 › the filter hides books on loan (185ms)
   #   ✓  3 [chromium] › tests/search.spec.ts:25:5 › hovering a status explains it (131ms)
   #
   #   3 passed (1.3s)
   ```

Lee `CatalogPage`:

| Pieza | Qué / Para qué |
|---|---|
| `readonly rows`, `message`, `tooltip` | Locators que los tests usan en sus aserciones. Se crean en el constructor sin buscar nada ([teoría 03](../../1-teoria/03-page-object-model-y-fixtures.md#1-un-page-object)) |
| `search(text, { onlyAvailable })` | Una acción del usuario: escribir, marcar la casilla si hace falta y pulsar «Buscar». El test no sabe que hay un `searchbox` |
| `row(title)` | Un método que devuelve un locator: la fila de un libro, para usarla en una aserción o seguir encadenando |
| `hoverStatus(title)` | La última celda de la fila del libro: el test ya no depende del texto del estado |

Compara con el lab 02: `search.spec.ts` ya no tiene ningún `getByRole`. Cada test crea su `CatalogPage` con `new CatalogPage(page)`.

### Paso 2: el formulario (PASO 2)

1. Descomenta el bloque `// PASO 2` de `pages/book-form-page.ts` y el de `tests/new-book.spec.ts`.
2. Ejecuta `pnpm exec playwright test`: pasan 5.

`fill(book: Partial<Book>)` rellena los campos que traiga el objeto: `Partial` hace opcionales todos los campos de `Book`. `setChecked(book.reference ?? false)` deja la casilla como diga el objeto, esté como esté. El test de alta usa dos page objects: el formulario para rellenar y el catálogo para comprobar el mensaje.

### Paso 3: fixtures y datos por la API (PASO 3)

1. Descomenta el bloque `// PASO 3` de `pages/book-page.ts`, el de `fixtures.ts` (el archivo entero) y el de `tests/lending.spec.ts`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   # Running 8 tests using 3 workers
   # ...
   #   ✓  1 [chromium] › tests/lending.spec.ts:4:5 › a book can be lent and returned (1.8s)
   #   ✓  7 [chromium] › tests/lending.spec.ts:18:5 › a reference book cannot be lent (76ms)
   #   ✓  8 [chromium] › tests/lending.spec.ts:25:5 › a book saved as reference cannot be lent (646ms)
   #
   #   8 passed (3.4s)
   ```

| En `fixtures.ts` | Qué hace |
|---|---|
| `base.extend<Fixtures>({...})` | Un `test` que entiende `catalog`, `bookForm`, `bookPage` y `newBook` además de `page` y `request` ([teoría 03](../../1-teoria/03-page-object-model-y-fixtures.md#2-fixtures-con-testextend)) |
| `catalog: async ({ page }, use) => { await use(new CatalogPage(page)); }` | El page object, construido con la `page` de ese test |
| `newBook` | Crea un libro con ISBN aleatorio por `POST /api/items`, comprueba la respuesta y lo entrega al test |
| `export { expect }` | Los tests importan `test` y `expect` del mismo sitio |

`lending.spec.ts` importa de `'../fixtures'` y pide lo que necesita: `async ({ bookPage, newBook })`. Ya no hay `new` ni `request` en el test. El tercer test guarda un libro de solo consulta por el formulario, porque eso es lo que prueba, y comprueba el mensaje antes de ir a la ficha: si navegara antes de que el servidor respondiera, la ficha no existiría todavía.

`search.spec.ts` y `new-book.spec.ts` siguen con `new CatalogPage(page)`. Funcionan igual; con fixtures quedarían más cortos.

## Experimentos

### Experimento 1: cambia la interfaz

1. En `app/server.mjs`, busca `<p><button>Guardar</button></p>` y cambia `Guardar` por `Añadir`.
2. Ejecuta:

   ```bash
   pnpm exec playwright test
   #   ✘  1 [chromium] › tests/new-book.spec.ts:8:5 › a new book appears in the catalog (30.0s)
   #   ✘  7 [chromium] › tests/lending.spec.ts:25:5 › a book saved as reference cannot be lent (30.0s)
   #   ✘  8 [chromium] › tests/new-book.spec.ts:21:5 › an empty form shows every error (30.0s)
   # ...
   #     Error: locator.click: Test timeout of 30000ms exceeded.
   #     Call log:
   #       - waiting for getByRole('button', { name: 'Guardar' })
   #
   #
   #        at ../pages/book-form-page.ts:34
   #
   #       32 |
   #       33 |   async save() {
   #     > 34 |     await this.page.getByRole('button', { name: 'Guardar' }).click();
   #          |                                                              ^
   #       35 |   }
   #       36 | }
   #       37 |
   #         at BookFormPage.save (.../pages/book-form-page.ts:34:62)
   #         at .../tests/new-book.spec.ts:25:14
   # ...
   #   3 failed
   #   5 passed (1.0m)
   ```

   Tres tests de dos archivos fallan en la misma línea del page object. La pila muestra los dos niveles: `BookFormPage.save` y la línea del test que lo llamó.

3. Arregla el page object, no los tests: en `book-form-page.ts`, cambia `'Guardar'` por `'Añadir'`. Ejecuta: pasan 8.
4. Deshaz los dos cambios.

Con los tests del lab 02, el texto «Guardar» estaba en dos tests; con diez tests de alta estaría en diez.

### Experimento 2: un fixture que limpia

Añade una línea después de `await use(book);` en `newBook`:

```ts
console.log(`newBook ${book.isbn} terminado`);
```

Ejecuta:

```bash
pnpm exec playwright test lending
# Running 3 tests using 1 worker
#
# newBook 9799198196689 terminado
#   ✓  1 [chromium] › tests/lending.spec.ts:4:5 › a book can be lent and returned (1.8s)
#   ✓  2 [chromium] › tests/lending.spec.ts:18:5 › a reference book cannot be lent (88ms)
#   ✓  3 [chromium] › tests/lending.spec.ts:25:5 › a book saved as reference cannot be lent (641ms)
#
#   3 passed (3.2s)
```

La línea aparece una vez, al terminar el test que pidió `newBook` y antes de que Playwright lo dé por terminado; los otros dos tests no lo piden y no crean ningún libro. Lo que va después de `use` es el sitio de la limpieza. Esta aplicación no tiene una ruta para borrar libros, y los ISBN aleatorios hacen que no haga falta. Quita la línea.

## Comprobación final

```bash
pnpm exec playwright test
# ...
#   8 passed (3.4s)
```

## Preguntas de cierre

1. ¿Por qué las aserciones están en los tests y no en los page objects?
2. ¿Qué ventaja tiene pedir `bookPage` como fixture frente a escribir `new BookPage(page)` en cada test?
3. ¿Qué hace `await use(book)`? ¿Dónde pondrías la limpieza si la aplicación tuviera `DELETE /api/items/:isbn`?
4. Compara los fixtures de Playwright con los de pytest ([semana 16](../../../week-16-fundamentos_con_pytest/1-teoria/02-fixtures-yield-scopes-y-composicion.md)).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Locators, acciones y aserciones](../lab-02-locators-acciones-y-aserciones/README.md) | [Lab 04 - Evidencias y navegadores](../lab-04-evidencias-y-navegadores/README.md) → |

[↑ Semana 42](../../README.md)
