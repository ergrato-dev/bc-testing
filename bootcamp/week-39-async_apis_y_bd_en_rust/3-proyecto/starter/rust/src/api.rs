//! API HTTP de tu servicio con axum.
//!
//! TODO (paso 4): las rutas de tu API Go de la semana 34, con los mismos
//! códigos de estado:
//! - `pub fn router(service) -> Router`, genérico sobre los traits del servicio;
//! - un handler por ruta, con `State`, `Path` y `Json`;
//! - `status_for(&ErrorDelDominio) -> StatusCode`, la tabla de códigos en un sitio;
//! - `impl IntoResponse` para el error: `{"error": "..."}`, y un 500 que no
//!   enseña el error interno.
