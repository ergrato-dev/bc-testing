import type { Locator, Page } from '@playwright/test';

export class CatalogPage {
  readonly page: Page;
  readonly rows: Locator;
  readonly message: Locator;
  readonly tooltip: Locator;

  constructor(page: Page) {
    this.page = page;
    this.rows = page.getByRole('row');
    this.message = page.getByRole('status');
    this.tooltip = page.getByRole('tooltip');
  }

  async goto() {
    await this.page.goto('/');
  }

  async search(text: string, options: { onlyAvailable?: boolean } = {}) {
    await this.page.getByRole('searchbox', { name: 'Buscar' }).fill(text);
    if (options.onlyAvailable) {
      await this.page.getByLabel('Solo disponibles').check();
    }
    await this.page.getByRole('button', { name: 'Buscar' }).click();
  }

  row(title: string) {
    return this.page.getByRole('row', { name: title });
  }

  async hoverStatus(title: string) {
    await this.row(title).getByRole('cell').last().hover();
  }
}
