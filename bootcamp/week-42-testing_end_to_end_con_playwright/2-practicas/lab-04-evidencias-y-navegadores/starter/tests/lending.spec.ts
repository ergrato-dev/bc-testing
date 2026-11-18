import { test, expect, newIsbn } from '../fixtures';

test('a book can be lent and returned', async ({ bookPage, newBook }) => {
  await bookPage.goto(newBook.isbn);

  await bookPage.lend();

  await expect(bookPage.message).toHaveText('Libro prestado');
  await expect(bookPage.status).toHaveText('Prestado');
  await expect(bookPage.lendButton).toBeDisabled();

  await bookPage.giveBack();

  await expect(bookPage.status).toHaveText('Disponible');
});

test('a reference book cannot be lent', async ({ bookPage }) => {
  await bookPage.goto('9788491050957');

  await expect(bookPage.lendButton).toBeDisabled();
  await expect(bookPage.returnButton).toBeDisabled();
});

test('a book saved as reference cannot be lent', async ({ bookForm, bookPage, catalog }) => {
  const isbn = newIsbn();
  await bookForm.goto();
  await bookForm.fill({ isbn, title: 'Atlas del mundo', genre: 'Ensayo', reference: true });
  await bookForm.save();
  await expect(catalog.message).toHaveText(`${isbn} guardado`);

  await bookPage.goto(isbn);

  await expect(bookPage.heading).toHaveText('Atlas del mundo');
  await expect(bookPage.lendButton).toBeDisabled();
});
