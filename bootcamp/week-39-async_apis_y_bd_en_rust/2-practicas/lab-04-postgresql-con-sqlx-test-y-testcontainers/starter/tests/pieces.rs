//! Tests del repositorio con `#[sqlx::test]`: cada uno recibe una base de
//! datos nueva, creada en el PostgreSQL de `DATABASE_URL` y migrada.

// PASO 1: una base de datos para el test.
// use lab04::{Piece, find, save};
// use sqlx::PgPool;
//
// fn amphora() -> Piece {
//     Piece {
//         code: "MUS-001".to_string(),
//         name: "Ánfora romana".to_string(),
//         declared_value: 200_000,
//     }
// }
//
// #[sqlx::test]
// async fn saves_and_finds(pool: PgPool) {
//     save(&pool, &amphora()).await.unwrap();
//
//     let got = find(&pool, "MUS-001").await.unwrap();
//
//     assert_eq!(got, Some(amphora()));
// }
//
// PASO 2: cada test empieza de cero.
// use lab04::count;
//
// #[sqlx::test]
// async fn save_twice_updates(pool: PgPool) {
//     save(&pool, &amphora()).await.unwrap();
//     let mut renamed = amphora();
//     renamed.name = "Ánfora".to_string();
//
//     save(&pool, &renamed).await.unwrap();
//
//     assert_eq!(count(&pool).await.unwrap(), 1);
//     assert_eq!(find(&pool, "MUS-001").await.unwrap(), Some(renamed));
// }
//
// #[sqlx::test]
// async fn unknown_code_is_none(pool: PgPool) {
//     assert_eq!(find(&pool, "MUS-001").await.unwrap(), None);
// }
//
// #[sqlx::test]
// async fn schema_rejects_zero_value(pool: PgPool) {
//     let mut piece = amphora();
//     piece.declared_value = 0;
//
//     let err = save(&pool, &piece).await.unwrap_err();
//
//     let code = err.as_database_error().and_then(|e| e.code());
//     assert_eq!(code.as_deref(), Some("23514"));
// }
//
// PASO 3: datos de partida con fixtures.
// use lab04::worth_more_than;
//
// #[sqlx::test(fixtures("pieces"))]
// async fn lists_valuable_pieces(pool: PgPool) {
//     let got = worth_more_than(&pool, 100_000).await.unwrap();
//
//     assert_eq!(got, vec!["MUS-001", "MUS-003"]);
// }
