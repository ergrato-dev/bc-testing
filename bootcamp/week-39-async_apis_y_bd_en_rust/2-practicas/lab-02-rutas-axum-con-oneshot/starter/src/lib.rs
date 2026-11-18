//! API del catálogo del museo con axum: consultar y registrar piezas.

use axum::extract::{Path, State};
use axum::http::StatusCode;
use axum::routing::{get, post};
use axum::{Json, Router};
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use std::sync::{Arc, Mutex};

/// Pieza del catálogo. En JSON, los campos van en camelCase, como en la API
/// Java de la semana 28: `declaredValue`.
#[derive(Debug, Clone, PartialEq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct Piece {
    pub code: String,
    pub name: String,
    pub declared_value: u32,
}

/// El catálogo en memoria, compartido entre peticiones.
pub type Catalog = Arc<Mutex<HashMap<String, Piece>>>;

/// Las rutas de la API sobre un catálogo.
pub fn app(catalog: Catalog) -> Router {
    Router::new()
        .route("/pieces", post(create_piece))
        .route("/pieces/{code}", get(get_piece))
        .with_state(catalog)
}

async fn get_piece(
    State(catalog): State<Catalog>,
    Path(code): Path<String>,
) -> Result<Json<Piece>, StatusCode> {
    let catalog = catalog.lock().unwrap();
    catalog
        .get(&code)
        .cloned()
        .map(Json)
        .ok_or(StatusCode::NOT_FOUND)
}

async fn create_piece(
    State(catalog): State<Catalog>,
    Json(piece): Json<Piece>,
) -> Result<(StatusCode, Json<Piece>), StatusCode> {
    if piece.declared_value == 0 {
        return Err(StatusCode::UNPROCESSABLE_ENTITY);
    }
    let mut catalog = catalog.lock().unwrap();
    if catalog.contains_key(&piece.code) {
        return Err(StatusCode::CONFLICT);
    }
    catalog.insert(piece.code.clone(), piece.clone());
    Ok((StatusCode::CREATED, Json(piece)))
}

#[cfg(test)]
mod tests {
    // PASO 1: una petición sin abrir un puerto.
    // use super::*;
    // use axum::body::Body;
    // use axum::http::Request;
    // use axum::response::Response;
    // use http_body_util::BodyExt;
    // use serde_json::{Value, json};
    // use tower::ServiceExt;
    //
    // /// Un catálogo con una pieza.
    // fn catalog() -> Catalog {
    //     let amphora = Piece {
    //         code: "MUS-001".to_string(),
    //         name: "Ánfora romana".to_string(),
    //         declared_value: 200_000,
    //     };
    //     let map = HashMap::from([(amphora.code.clone(), amphora)]);
    //     Arc::new(Mutex::new(map))
    // }
    //
    // /// Lee el cuerpo entero de la respuesta y lo interpreta como JSON.
    // async fn json_body(res: Response) -> Value {
    //     let bytes = res.into_body().collect().await.unwrap().to_bytes();
    //     serde_json::from_slice(&bytes).unwrap()
    // }
    //
    // #[tokio::test]
    // async fn get_returns_the_piece() {
    //     let req = Request::get("/pieces/MUS-001").body(Body::empty()).unwrap();
    //
    //     let res = app(catalog()).oneshot(req).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::OK);
    //     let want = json!({
    //         "code": "MUS-001",
    //         "name": "Ánfora romana",
    //         "declaredValue": 200000
    //     });
    //     assert_eq!(json_body(res).await, want);
    // }
    //
    // PASO 2: lo que no existe.
    // use axum::http::header;
    //
    // #[tokio::test]
    // async fn get_unknown_piece_is_404() {
    //     let req = Request::get("/pieces/MUS-999").body(Body::empty()).unwrap();
    //
    //     let res = app(catalog()).oneshot(req).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::NOT_FOUND);
    // }
    //
    // #[tokio::test]
    // async fn wrong_method_is_405() {
    //     let req = Request::delete("/pieces/MUS-001")
    //         .body(Body::empty())
    //         .unwrap();
    //
    //     let res = app(catalog()).oneshot(req).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::METHOD_NOT_ALLOWED);
    //     assert_eq!(res.headers()[header::ALLOW], "GET,HEAD");
    // }
    //
    // PASO 3: un POST que cambia el estado.
    // fn post(body: &str) -> Request<Body> {
    //     Request::post("/pieces")
    //         .header(header::CONTENT_TYPE, "application/json")
    //         .body(Body::from(body.to_string()))
    //         .unwrap()
    // }
    //
    // #[tokio::test]
    // async fn post_creates_the_piece() {
    //     let catalog = catalog();
    //     let body = r#"{"code": "MUS-002", "name": "Códice", "declaredValue": 5000}"#;
    //
    //     let res = app(catalog.clone()).oneshot(post(body)).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::CREATED);
    //     assert_eq!(catalog.lock().unwrap()["MUS-002"].name, "Códice");
    // }
    //
    // #[tokio::test]
    // async fn post_existing_code_is_409() {
    //     let body = r#"{"code": "MUS-001", "name": "Otra", "declaredValue": 5000}"#;
    //
    //     let res = app(catalog()).oneshot(post(body)).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::CONFLICT);
    // }
    //
    // PASO 4: el JSON que axum rechaza antes del handler.
    // #[tokio::test]
    // async fn malformed_json_is_400() {
    //     let res = app(catalog()).oneshot(post(r#"{"code": "#)).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::BAD_REQUEST);
    // }
    //
    // #[tokio::test]
    // async fn missing_field_is_422() {
    //     let body = r#"{"code": "MUS-002", "name": "Códice"}"#;
    //
    //     let res = app(catalog()).oneshot(post(body)).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::UNPROCESSABLE_ENTITY);
    // }
    //
    // #[tokio::test]
    // async fn missing_content_type_is_415() {
    //     let req = Request::post("/pieces")
    //         .body(Body::from(r#"{"code": "MUS-002"}"#))
    //         .unwrap();
    //
    //     let res = app(catalog()).oneshot(req).await.unwrap();
    //
    //     assert_eq!(res.status(), StatusCode::UNSUPPORTED_MEDIA_TYPE);
    // }
}
