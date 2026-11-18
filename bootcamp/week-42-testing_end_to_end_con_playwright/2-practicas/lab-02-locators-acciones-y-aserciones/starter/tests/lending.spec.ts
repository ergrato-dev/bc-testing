import { test, expect } from '@playwright/test';

// PASO 4: preparar datos por la API y prestar
// const newIsbn = () => '979' + String(Math.floor(Math.random() * 1e10)).padStart(10, '0');
//
// test('a book can be lent and returned', async ({ page, request }) => {
//   const isbn = newIsbn();
//   const created = await request.post('/api/items', {
//     data: { isbn, title: 'Préstamo de prueba', genre: 'Novela' },
//   });
//   await expect(created).toBeOK();
//   await page.goto(`/items/${isbn}`);
//
//   await page.getByRole('button', { name: 'Prestar' }).click();
//
//   await expect(page.getByRole('status')).toHaveText('Libro prestado');
//   await expect(page.getByTestId('status')).toHaveText('Prestado');
//   await expect(page.getByRole('button', { name: 'Prestar' })).toBeDisabled();
//
//   await page.getByRole('button', { name: 'Devolver' }).click();
//
//   await expect(page.getByTestId('status')).toHaveText('Disponible');
// });
//
// test('a reference book cannot be lent', async ({ page }) => {
//   await page.goto('/items/9788491050957');
//
//   await expect(page.getByRole('button', { name: 'Prestar' })).toBeDisabled();
//   await expect(page.getByRole('button', { name: 'Devolver' })).toBeDisabled();
// });
