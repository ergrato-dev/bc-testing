//! Tests de integración: usan lab04 desde fuera, como cualquier otro crate.

// PASO 2: la API pública desde fuera.
// use lab04::{InvalidCode, normalize_code, parse_entry};
//
// #[test]
// fn normalizes_spaces_and_case() {
//     assert_eq!(normalize_code("  mus-001 "), Some("MUS-001".to_string()));
// }
//
// #[test]
// fn rejects_an_entry_with_a_bad_code() {
//     assert_eq!(parse_entry("MUS-1;Vaso"), Err(InvalidCode("MUS-1".into())));
// }
