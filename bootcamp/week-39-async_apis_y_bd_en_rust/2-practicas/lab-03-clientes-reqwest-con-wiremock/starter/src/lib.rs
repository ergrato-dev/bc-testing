//! Cliente de un catálogo bibliográfico remoto: el `openlib` de la suite Go
//! (semana 34) con reqwest.

use reqwest::StatusCode;
use serde::Deserialize;
use std::time::Duration;

/// Datos que el catálogo remoto devuelve de un ISBN.
#[derive(Debug, PartialEq, Deserialize)]
pub struct Book {
    pub isbn: String,
    pub title: String,
    pub year: u16,
}

/// Por qué falló una consulta.
#[derive(Debug, PartialEq)]
pub enum LookupError {
    /// El catálogo respondió 404: el ISBN no existe.
    NotFound,
    /// El catálogo respondió con otro estado distinto de 200.
    Unavailable(u16),
    /// El catálogo no respondió dentro del plazo.
    Timeout,
    /// La respuesta no se pudo leer o la conexión falló.
    Invalid(String),
}

/// Consulta el catálogo que hay en `base_url`. Los tests le dan la URL de su
/// servidor simulado.
pub struct Client {
    base_url: String,
    http: reqwest::Client,
}

impl Client {
    /// Un cliente que abandona cada petición pasado `timeout`.
    pub fn new(base_url: &str, timeout: Duration) -> Self {
        let http = reqwest::Client::builder().timeout(timeout).build().unwrap();
        Client {
            base_url: base_url.to_string(),
            http,
        }
    }

    /// Pide `GET {base_url}/isbn/{isbn}` con la cabecera
    /// `Accept: application/json`.
    pub async fn lookup(&self, isbn: &str) -> Result<Book, LookupError> {
        let res = self
            .http
            .get(format!("{}/isbn/{}", self.base_url, isbn))
            .header("Accept", "application/json")
            .send()
            .await
            .map_err(classify)?;
        match res.status() {
            StatusCode::OK => res.json().await.map_err(classify),
            StatusCode::NOT_FOUND => Err(LookupError::NotFound),
            other => Err(LookupError::Unavailable(other.as_u16())),
        }
    }
}

fn classify(e: reqwest::Error) -> LookupError {
    if e.is_timeout() {
        LookupError::Timeout
    } else {
        LookupError::Invalid(e.to_string())
    }
}

#[cfg(test)]
mod tests {
    // PASO 1: un servidor simulado con una respuesta fija.
    // use super::*;
    // use serde_json::json;
    // use wiremock::matchers::{method, path};
    // use wiremock::{Mock, MockServer, ResponseTemplate};
    //
    // const ISBN: &str = "9788491050278";
    //
    // fn client(server: &MockServer) -> Client {
    //     Client::new(&server.uri(), Duration::from_millis(200))
    // }
    //
    // #[tokio::test]
    // async fn lookup_reads_the_book() {
    //     let server = MockServer::start().await;
    //     Mock::given(method("GET"))
    //         .and(path(format!("/isbn/{ISBN}")))
    //         .respond_with(ResponseTemplate::new(200).set_body_json(json!({
    //             "isbn": ISBN,
    //             "title": "El nombre de la rosa",
    //             "year": 1980
    //         })))
    //         .mount(&server)
    //         .await;
    //
    //     let got = client(&server).lookup(ISBN).await;
    //
    //     let want = Book {
    //         isbn: ISBN.to_string(),
    //         title: "El nombre de la rosa".to_string(),
    //         year: 1980,
    //     };
    //     assert_eq!(got, Ok(want));
    // }
    //
    // PASO 2: estados de error y un JSON roto.
    // async fn respond(template: ResponseTemplate) -> Result<Book, LookupError> {
    //     let server = MockServer::start().await;
    //     Mock::given(method("GET"))
    //         .respond_with(template)
    //         .mount(&server)
    //         .await;
    //     client(&server).lookup(ISBN).await
    // }
    //
    // #[tokio::test]
    // async fn not_found_is_its_own_error() {
    //     let got = respond(ResponseTemplate::new(404)).await;
    //
    //     assert_eq!(got, Err(LookupError::NotFound));
    // }
    //
    // #[tokio::test]
    // async fn server_error_is_unavailable() {
    //     let got = respond(ResponseTemplate::new(503)).await;
    //
    //     assert_eq!(got, Err(LookupError::Unavailable(503)));
    // }
    //
    // #[tokio::test]
    // async fn broken_json_is_invalid() {
    //     let got = respond(ResponseTemplate::new(200).set_body_string("{\"isbn\":")).await;
    //
    //     assert!(matches!(got, Err(LookupError::Invalid(_))), "{got:?}");
    // }
    //
    // PASO 3: la petición que el servidor debe recibir.
    // use wiremock::matchers::header;
    //
    // #[tokio::test]
    // async fn sends_accept_header_once() {
    //     let server = MockServer::start().await;
    //     Mock::given(method("GET"))
    //         .and(path(format!("/isbn/{ISBN}")))
    //         .and(header("accept", "application/json"))
    //         .respond_with(ResponseTemplate::new(404))
    //         .expect(1)
    //         .mount(&server)
    //         .await;
    //
    //     let got = client(&server).lookup(ISBN).await;
    //
    //     assert_eq!(got, Err(LookupError::NotFound));
    // }
    //
    // PASO 4: un servidor que no contesta a tiempo.
    // #[tokio::test]
    // async fn slow_server_times_out() {
    //     let template = ResponseTemplate::new(200).set_delay(Duration::from_secs(2));
    //
    //     let got = respond(template).await;
    //
    //     assert_eq!(got, Err(LookupError::Timeout));
    // }
}
