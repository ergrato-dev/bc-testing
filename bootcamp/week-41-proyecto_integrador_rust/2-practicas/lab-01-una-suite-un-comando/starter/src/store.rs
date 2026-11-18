//! Los libros en PostgreSQL con sqlx.

use sqlx::{FromRow, PgPool};

use crate::api::{BookStore, LendError};

/// Un libro tal como está en la tabla `books`.
#[derive(Debug, PartialEq, FromRow)]
pub struct Book {
    pub isbn: String,
    pub title: String,
    pub copies: i32,
}

/// El almacén de libros sobre un pool de PostgreSQL.
pub struct PgStore {
    pub pool: PgPool,
}

impl PgStore {
    /// Añade un libro.
    pub async fn add(&self, book: &Book) -> Result<(), sqlx::Error> {
        sqlx::query("INSERT INTO books (isbn, title, copies) VALUES ($1, $2, $3)")
            .bind(&book.isbn)
            .bind(&book.title)
            .bind(book.copies)
            .execute(&self.pool)
            .await?;
        Ok(())
    }

    /// Busca un libro por su ISBN.
    pub async fn get(&self, isbn: &str) -> Result<Option<Book>, sqlx::Error> {
        sqlx::query_as("SELECT isbn, title, copies FROM books WHERE isbn = $1")
            .bind(isbn)
            .fetch_optional(&self.pool)
            .await
    }
}

impl BookStore for PgStore {
    async fn lend(&self, isbn: &str) -> Result<i32, LendError> {
        let left: Option<i32> = sqlx::query_scalar(
            "UPDATE books SET copies = copies - 1 WHERE isbn = $1 AND copies > 0 RETURNING copies",
        )
        .bind(isbn)
        .fetch_optional(&self.pool)
        .await
        .map_err(|e| LendError::Store(e.to_string()))?;
        if let Some(left) = left {
            return Ok(left);
        }
        match self.get(isbn).await {
            Ok(Some(_)) => Err(LendError::NoCopies),
            Ok(None) => Err(LendError::NotFound),
            Err(e) => Err(LendError::Store(e.to_string())),
        }
    }
}
