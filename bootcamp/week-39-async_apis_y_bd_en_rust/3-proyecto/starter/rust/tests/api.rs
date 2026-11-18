//! La API con `oneshot`, con los dobles de `tests/common` en lugar de
//! PostgreSQL y del servicio externo.

mod common;

#[tokio::test]
#[ignore = "TODO: GET con oneshot, estado y JSON comparado con json! (paso 5)"]
async fn get_endpoint() {}

#[tokio::test]
#[ignore = "TODO: la operación principal, sus rechazos con rstest, el 500, el 400 y el 405 (paso 5)"]
async fn write_endpoint() {}
