//! Repositorio de piezas del museo en PostgreSQL con sqlx.

use sqlx::{FromRow, PgPool};

/// Pieza tal como está en la tabla `pieces`.
#[derive(Debug, PartialEq, FromRow)]
pub struct Piece {
    pub code: String,
    pub name: String,
    pub declared_value: i32,
}

/// Inserta la pieza o, si su código ya existe, actualiza sus datos.
pub async fn save(pool: &PgPool, piece: &Piece) -> Result<(), sqlx::Error> {
    sqlx::query(
        "INSERT INTO pieces (code, name, declared_value) VALUES ($1, $2, $3)
         ON CONFLICT (code) DO UPDATE
         SET name = EXCLUDED.name, declared_value = EXCLUDED.declared_value",
    )
    .bind(&piece.code)
    .bind(&piece.name)
    .bind(piece.declared_value)
    .execute(pool)
    .await?;
    Ok(())
}

/// Busca una pieza por su código.
pub async fn find(pool: &PgPool, code: &str) -> Result<Option<Piece>, sqlx::Error> {
    sqlx::query_as("SELECT code, name, declared_value FROM pieces WHERE code = $1")
        .bind(code)
        .fetch_optional(pool)
        .await
}

/// Cuántas piezas hay.
pub async fn count(pool: &PgPool) -> Result<i64, sqlx::Error> {
    sqlx::query_scalar("SELECT count(*) FROM pieces")
        .fetch_one(pool)
        .await
}

/// Códigos de las piezas que valen más de `min`, ordenados.
pub async fn worth_more_than(pool: &PgPool, min: i32) -> Result<Vec<String>, sqlx::Error> {
    sqlx::query_scalar("SELECT code FROM pieces WHERE declared_value > $1 ORDER BY code")
        .bind(min)
        .fetch_all(pool)
        .await
}
