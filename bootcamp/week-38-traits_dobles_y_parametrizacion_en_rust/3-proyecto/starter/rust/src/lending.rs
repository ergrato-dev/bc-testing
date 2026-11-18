//! El servicio principal de tu dominio, con sus dependencias como traits.
//!
//! TODO (paso 3): porta aquí el servicio de tu suite Go de la semana 33
//! (en el museo, `LendingService` con su método `lend`):
//! - los tipos que recibe y devuelve, con `#[derive(Debug, Clone, PartialEq)]`;
//! - un `enum` de error con una variante por cada rechazo;
//! - un trait público por dependencia, con los métodos que el servicio usa
//!   y `&self` como receptor;
//! - el servicio, genérico sobre esos traits: `pub struct Servicio<A, B, ...>`;
//! - la función que genera el texto que se envía (paso 7).
