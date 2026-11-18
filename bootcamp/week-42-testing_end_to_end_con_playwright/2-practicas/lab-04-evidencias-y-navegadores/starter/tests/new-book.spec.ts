import { test, expect } from '@playwright/test';
import { BookFormPage } from '../pages/book-form-page';
import { CatalogPage } from '../pages/catalog-page';

const newIsbn = () => '979' + String(Math.floor(Math.random() * 1e10)).padStart(10, '0');

test('a new book appears in the catalog', async ({ page }) => {
  const form = new BookFormPage(page);
  const catalog = new CatalogPage(page);
  const isbn = newIsbn();
  await form.goto();

  await form.fill({ isbn, title: 'Pruebas de extremo a extremo', genre: 'Ensayo', pages: 120 });
  await form.save();

  await expect(catalog.message).toHaveText(`${isbn} guardado`);
  await expect(catalog.row(isbn)).toBeVisible();
});

test('an empty form shows every error', async ({ page }) => {
  const form = new BookFormPage(page);
  await form.goto();

  await form.save();

  await expect(form.errors).toHaveText([
    'ISBN es obligatorio',
    'Título es obligatorio',
    'Género es obligatorio',
  ]);
});
