import { test, expect } from '@playwright/test';

// PASO 1: buscar, filtrar y pasar el ratón
// test('searching filters the catalog', async ({ page }) => {
//   await page.goto('/');
//
//   await page.getByRole('searchbox', { name: 'Buscar' }).fill('rayuela');
//   await page.getByRole('button', { name: 'Buscar' }).click();
//
//   await expect(page).toHaveURL('/?q=rayuela');
//   await expect(page.getByRole('row')).toHaveCount(2);
//   await expect(page.getByRole('cell', { name: 'Rayuela' })).toBeVisible();
// });
//
// test('the filter hides books on loan', async ({ page }) => {
//   await page.goto('/');
//
//   await page.getByLabel('Solo disponibles').check();
//   await page.getByRole('button', { name: 'Buscar' }).click();
//
//   await expect(page.getByLabel('Solo disponibles')).toBeChecked();
//   await expect(page.getByRole('row', { name: /Veinte poemas/ })).toHaveCount(0);
// });
//
// test('hovering a status explains it', async ({ page }) => {
//   await page.goto('/');
//
//   await page.getByRole('row', { name: /Veinte poemas/ }).getByText('Prestado').hover();
//
//   await expect(page.getByRole('tooltip')).toHaveText('Fuera de la biblioteca hasta su devolución');
// });
