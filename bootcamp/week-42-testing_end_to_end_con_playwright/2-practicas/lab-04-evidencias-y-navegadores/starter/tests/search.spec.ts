import { test, expect } from '@playwright/test';
import { CatalogPage } from '../pages/catalog-page';

test('searching filters the catalog', async ({ page }) => {
  const catalog = new CatalogPage(page);
  await catalog.goto();

  await catalog.search('rayuela');

  await expect(page).toHaveURL('/?q=rayuela');
  await expect(catalog.rows).toHaveCount(2);
  await expect(catalog.row('Rayuela')).toBeVisible();
});

test('the filter hides books on loan', async ({ page }) => {
  const catalog = new CatalogPage(page);
  await catalog.goto();

  await catalog.search('', { onlyAvailable: true });

  await expect(catalog.row('Veinte poemas')).toHaveCount(0);
});

test('hovering a status explains it', async ({ page }) => {
  const catalog = new CatalogPage(page);
  await catalog.goto();

  await catalog.hoverStatus('Veinte poemas');

  await expect(catalog.tooltip).toHaveText('Fuera de la biblioteca hasta su devolución');
});
