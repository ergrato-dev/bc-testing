//! Los almacenes en PostgreSQL con `#[sqlx::test]`: cada test recibe una base
//! de datos nueva con las migraciones de `migrations/` aplicadas.

mod common;

#[tokio::test]
#[ignore = "TODO: guardar y leer, lo que no existe y lo que el esquema rechaza, con #[sqlx::test] (paso 8)"]
async fn store_in_postgres() {}

#[tokio::test]
#[ignore = "TODO: el servicio completo sobre PostgreSQL (paso 8)"]
async fn service_with_postgres() {}
