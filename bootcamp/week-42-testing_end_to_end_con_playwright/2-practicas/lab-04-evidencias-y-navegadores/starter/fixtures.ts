import { test as base, expect } from '@playwright/test';
import { CatalogPage } from './pages/catalog-page';
import { BookFormPage, type Book } from './pages/book-form-page';
import { BookPage } from './pages/book-page';

export const newIsbn = () => '979' + String(Math.floor(Math.random() * 1e10)).padStart(10, '0');

type Fixtures = {
  catalog: CatalogPage;
  bookForm: BookFormPage;
  bookPage: BookPage;
  newBook: Book;
};

export const test = base.extend<Fixtures>({
  catalog: async ({ page }, use) => {
    await use(new CatalogPage(page));
  },
  bookForm: async ({ page }, use) => {
    await use(new BookFormPage(page));
  },
  bookPage: async ({ page }, use) => {
    await use(new BookPage(page));
  },
  // Un libro nuevo para cada test que lo pida, creado por la API.
  newBook: async ({ request }, use) => {
    const book = { isbn: newIsbn(), title: 'Libro de prueba', genre: 'Novela' };
    const response = await request.post('/api/items', { data: book });
    await expect(response).toBeOK();
    await use(book);
  },
});

export { expect };
