import { test, expect } from '@playwright/test';

// PASO 1: el primer test
// test('the catalog lists the seeded books', async ({ page }) => {
//   await page.goto('/');
//
//   await expect(page).toHaveTitle('Libros - Biblioteca municipal');
//   await expect(page.getByRole('heading', { name: 'Libros' })).toBeVisible();
//   await expect(page.getByRole('link', { name: '9788420412146' })).toBeVisible();
// });
//
// PASO 2: navegar a otra página
// test('a book opens its detail page', async ({ page }) => {
//   await page.goto('/');
//
//   await page.getByRole('link', { name: '9788420412146' }).click();
//
//   await expect(page).toHaveURL('/items/9788420412146');
//   await expect(page.getByRole('heading', { name: 'Rayuela' })).toBeVisible();
//   await expect(page.getByTestId('status')).toHaveText('Disponible');
// });
