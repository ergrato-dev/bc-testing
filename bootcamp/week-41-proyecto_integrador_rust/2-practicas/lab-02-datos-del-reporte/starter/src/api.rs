//! La API HTTP: `GET /fees?days=N` y `POST /books/{isbn}/lend`.

use std::sync::Arc;

use axum::extract::{Path, Query, State};
use axum::http::StatusCode;
use axum::response::{IntoResponse, Response};
use axum::routing::{get, post};
use axum::{Json, Router};
use serde::Deserialize;
use serde_json::json;

use crate::fees::late_fee;

/// Por qué no se puede prestar un libro.
#[derive(Debug, PartialEq)]
pub enum LendError {
    NotFound,
    NoCopies,
    Store(String),
}

/// Dónde están los libros. `PgStore` lo implementa con PostgreSQL; los tests
/// de la API, con un stub.
pub trait BookStore: Send + Sync + 'static {
    /// Presta una copia y devuelve cuántas quedan.
    fn lend(&self, isbn: &str) -> impl Future<Output = Result<i32, LendError>> + Send;
}

#[derive(Deserialize)]
struct FeeQuery {
    days: u32,
}

/// Las rutas de la API sobre el almacén `store`.
pub fn router<S: BookStore>(store: S) -> Router {
    Router::new()
        .route("/fees", get(fee))
        .route("/books/{isbn}/lend", post(lend::<S>))
        .with_state(Arc::new(store))
}

async fn fee(Query(query): Query<FeeQuery>) -> Json<serde_json::Value> {
    Json(json!({ "fee": late_fee(query.days) }))
}

async fn lend<S: BookStore>(State(store): State<Arc<S>>, Path(isbn): Path<String>) -> Response {
    match store.lend(&isbn).await {
        Ok(left) => Json(json!({ "isbn": isbn, "copies_left": left })).into_response(),
        Err(LendError::NotFound) => error(StatusCode::NOT_FOUND, "book not found"),
        Err(LendError::NoCopies) => error(StatusCode::CONFLICT, "no copies left"),
        Err(LendError::Store(_)) => error(StatusCode::INTERNAL_SERVER_ERROR, "internal error"),
    }
}

fn error(status: StatusCode, message: &str) -> Response {
    (status, Json(json!({ "error": message }))).into_response()
}
