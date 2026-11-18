import type { Locator, Page } from '@playwright/test';

// PASO 2: el formulario de alta
// export type Book = {
//   isbn: string;
//   title: string;
//   genre: string;
//   pages?: number;
//   reference?: boolean;
// };
//
// export class BookFormPage {
//   readonly page: Page;
//   readonly errors: Locator;
//
//   constructor(page: Page) {
//     this.page = page;
//     this.errors = page.getByRole('alert').getByRole('listitem');
//   }
//
//   async goto() {
//     await this.page.goto('/new');
//   }
//
//   async fill(book: Partial<Book>) {
//     await this.page.getByLabel('ISBN').fill(book.isbn ?? '');
//     await this.page.getByLabel('Título').fill(book.title ?? '');
//     if (book.genre) await this.page.getByLabel('Género').selectOption(book.genre);
//     if (book.pages) await this.page.getByLabel('Páginas').fill(String(book.pages));
//     await this.page.getByLabel('Solo consulta en sala').setChecked(book.reference ?? false);
//   }
//
//   async save() {
//     await this.page.getByRole('button', { name: 'Guardar' }).click();
//   }
// }
