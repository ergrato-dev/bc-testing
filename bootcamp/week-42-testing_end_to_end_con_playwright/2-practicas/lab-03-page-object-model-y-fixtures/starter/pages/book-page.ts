import type { Locator, Page } from '@playwright/test';

// PASO 3: la ficha de un libro
// export class BookPage {
//   readonly page: Page;
//   readonly heading: Locator;
//   readonly status: Locator;
//   readonly message: Locator;
//   readonly lendButton: Locator;
//   readonly returnButton: Locator;
//
//   constructor(page: Page) {
//     this.page = page;
//     this.heading = page.getByRole('heading', { level: 1 });
//     this.status = page.getByTestId('status');
//     this.message = page.getByRole('status');
//     this.lendButton = page.getByRole('button', { name: 'Prestar' });
//     this.returnButton = page.getByRole('button', { name: 'Devolver' });
//   }
//
//   async goto(isbn: string) {
//     await this.page.goto(`/items/${isbn}`);
//   }
//
//   async lend() {
//     await this.lendButton.click();
//   }
//
//   async giveBack() {
//     await this.returnButton.click();
//   }
// }
