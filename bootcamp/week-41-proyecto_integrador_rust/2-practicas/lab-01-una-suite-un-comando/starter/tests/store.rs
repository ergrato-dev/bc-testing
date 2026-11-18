//! `PgStore` contra PostgreSQL con `#[sqlx::test]`: cada test recibe una base
//! de datos nueva con las migraciones de `migrations/` aplicadas.

use lab01::api::{BookStore, LendError};
use lab01::store::{Book, PgStore};
use sqlx::PgPool;

fn book(copies: i32) -> Book {
    Book {
        isbn: "978-84".to_string(),
        title: "Rayuela".to_string(),
        copies,
    }
}

#[sqlx::test]
async fn gets_what_was_added(pool: PgPool) {
    let store = PgStore { pool };
    store.add(&book(2)).await.unwrap();
    assert_eq!(store.get("978-84").await.unwrap(), Some(book(2)));
}

#[sqlx::test]
async fn lends_until_no_copies(pool: PgPool) {
    let store = PgStore { pool };
    store.add(&book(2)).await.unwrap();
    assert_eq!(store.lend("978-84").await, Ok(1));
    assert_eq!(store.lend("978-84").await, Ok(0));
    assert_eq!(store.lend("978-84").await, Err(LendError::NoCopies));
}

#[sqlx::test]
async fn unknown_book(pool: PgPool) {
    let store = PgStore { pool };
    assert_eq!(store.lend("000").await, Err(LendError::NotFound));
}
