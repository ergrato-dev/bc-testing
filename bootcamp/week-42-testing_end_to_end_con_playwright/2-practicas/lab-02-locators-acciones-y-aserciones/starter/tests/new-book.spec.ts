import { test, expect } from '@playwright/test';

// PASO 2: rellenar el formulario
// // Un ISBN distinto en cada ejecución: el catálogo no admite dos libros con el mismo.
// const newIsbn = () => '979' + String(Math.floor(Math.random() * 1e10)).padStart(10, '0');
//
// test('a new book appears in the catalog', async ({ page }) => {
//   const isbn = newIsbn();
//   await page.goto('/');
//   await page.getByRole('link', { name: 'Nuevo libro' }).click();
//
//   await page.getByLabel('ISBN').fill(isbn);
//   await page.getByLabel('Título').fill('Pruebas de extremo a extremo');
//   await page.getByLabel('Género').selectOption('Ensayo');
//   await page.getByLabel('Páginas').fill('120');
//   await page.getByRole('button', { name: 'Guardar' }).click();
//
//   await expect(page.getByRole('status')).toHaveText(`${isbn} guardado`);
//   await expect(page.getByRole('link', { name: isbn })).toBeVisible();
// });
//
// PASO 3: los errores de validación
// test('an empty form shows every error', async ({ page }) => {
//   await page.goto('/new');
//
//   await page.getByRole('button', { name: 'Guardar' }).click();
//
//   await expect(page.getByRole('alert').getByRole('listitem')).toHaveText([
//     'ISBN es obligatorio',
//     'Título es obligatorio',
//     'Género es obligatorio',
//   ]);
//   await expect(page.getByLabel('ISBN')).toHaveValue('');
// });
