//! La API con `oneshot` y un stub del almacén: sin base de datos.

use axum::body::Body;
use axum::http::{Request, StatusCode};
use http_body_util::BodyExt;
use lab02::api::{BookStore, LendError, router};
use serde_json::{Value, json};
use tower::ServiceExt;

/// Responde siempre lo mismo, sea cual sea el ISBN.
struct StubStore(fn() -> Result<i32, LendError>);

impl BookStore for StubStore {
    async fn lend(&self, _isbn: &str) -> Result<i32, LendError> {
        (self.0)()
    }
}

async fn call(store: StubStore, method: &str, uri: &str) -> (StatusCode, Value) {
    let request = Request::builder()
        .method(method)
        .uri(uri)
        .body(Body::empty())
        .unwrap();
    let response = router(store).oneshot(request).await.unwrap();
    let status = response.status();
    let body = response.into_body().collect().await.unwrap().to_bytes();
    (status, serde_json::from_slice(&body).unwrap())
}

#[tokio::test]
async fn fee_for_three_days() {
    let (status, body) = call(StubStore(|| Ok(1)), "GET", "/fees?days=3").await;
    assert_eq!(status, StatusCode::OK);
    assert_eq!(body, json!({ "fee": 150 }));
}

#[tokio::test]
async fn lends_a_copy() {
    let (status, body) = call(StubStore(|| Ok(2)), "POST", "/books/978-84/lend").await;
    assert_eq!(status, StatusCode::OK);
    assert_eq!(body, json!({ "isbn": "978-84", "copies_left": 2 }));
}

#[tokio::test]
async fn unknown_book_is_404() {
    let (status, _) = call(
        StubStore(|| Err(LendError::NotFound)),
        "POST",
        "/books/x/lend",
    )
    .await;
    assert_eq!(status, StatusCode::NOT_FOUND);
}

#[tokio::test]
async fn no_copies_is_409() {
    let (status, _) = call(
        StubStore(|| Err(LendError::NoCopies)),
        "POST",
        "/books/x/lend",
    )
    .await;
    assert_eq!(status, StatusCode::CONFLICT);
}
