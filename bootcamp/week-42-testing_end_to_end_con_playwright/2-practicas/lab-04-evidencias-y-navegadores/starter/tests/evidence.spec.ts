import { test, expect, newIsbn } from '../fixtures';

// PASO 2: capturas de evidencia del flujo principal
// test('main flow: add a book and lend it', async ({ page, catalog, bookForm, bookPage }) => {
//   const isbn = newIsbn();
//
//   await test.step('add the book', async () => {
//     await bookForm.goto();
//     await bookForm.fill({ isbn, title: 'Evidencias', genre: 'Ensayo' });
//     await bookForm.save();
//     await expect(catalog.message).toHaveText(`${isbn} guardado`);
//     await test.info().attach('1-catalog', { body: await page.screenshot(), contentType: 'image/png' });
//   });
//
//   await test.step('lend the book', async () => {
//     await bookPage.goto(isbn);
//     await bookPage.lend();
//     await expect(bookPage.status).toHaveText('Prestado');
//     await test.info().attach('2-lent', { body: await page.screenshot(), contentType: 'image/png' });
//   });
// });
