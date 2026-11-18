import { test, expect } from '@playwright/test';

// TODO: adapta este test a tu dominio y pásalo a page objects: los tests no usan
// page.getBy... ni page.locator, solo los métodos y locators de pages/.
test('the catalog lists the seeded items', async ({ page }) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { level: 1 })).toBeVisible();
  await expect(page.getByRole('row')).toHaveCount(5);
});
