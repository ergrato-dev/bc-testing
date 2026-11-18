//! El mismo repositorio contra un PostgreSQL que arranca el propio test con
//! testcontainers: no necesita `compose.yaml` ni `DATABASE_URL`.

// PASO 4: un contenedor por test.
// use lab04::{Piece, find, save};
// use sqlx::PgPool;
// use testcontainers_modules::postgres::Postgres;
// use testcontainers_modules::testcontainers::ImageExt;
// use testcontainers_modules::testcontainers::runners::AsyncRunner;
//
// #[tokio::test]
// async fn saves_and_finds_in_a_container() {
//     let container = Postgres::default()
//         .with_tag("18.7-alpine")
//         .start()
//         .await
//         .unwrap();
//     let port = container.get_host_port_ipv4(5432).await.unwrap();
//     let url = format!("postgres://postgres:postgres@127.0.0.1:{port}/postgres");
//     let pool = PgPool::connect(&url).await.unwrap();
//     sqlx::migrate!().run(&pool).await.unwrap();
//     let amphora = Piece {
//         code: "MUS-001".to_string(),
//         name: "Ánfora romana".to_string(),
//         declared_value: 200_000,
//     };
//
//     save(&pool, &amphora).await.unwrap();
//
//     assert_eq!(find(&pool, "MUS-001").await.unwrap(), Some(amphora));
// }
