//! Catálogo de piezas del museo: códigos y entradas del inventario.

/// Código de pieza que no tiene la forma `AAA-000`.
#[derive(Debug, PartialEq)]
pub struct InvalidCode(pub String);

/// Normaliza un código de pieza: quita los espacios de los extremos y pasa a
/// mayúsculas. Devuelve `None` si el resultado no tiene la forma `AAA-000`.
// PASO 3: un ejemplo que se ejecuta.
// ///
// /// ```
// /// use lab04::normalize_code;
// ///
// /// assert_eq!(normalize_code(" mus-001 "), Some("MUS-001".to_string()));
// /// assert_eq!(normalize_code("MUS-1"), None);
// /// ```
pub fn normalize_code(raw: &str) -> Option<String> {
    let code = raw.trim().to_uppercase();
    has_code_shape(&code).then_some(code)
}

/// Lee una entrada del inventario, `código;nombre`, y devuelve el código
/// normalizado y el nombre sin espacios en los extremos.
// PASO 4: un ejemplo con `?`.
// ///
// /// ```
// /// use lab04::{parse_entry, InvalidCode};
// ///
// /// let (code, name) = parse_entry("mus-001; Vasija ceremonial")?;
// /// assert_eq!(code, "MUS-001");
// /// assert_eq!(name, "Vasija ceremonial");
// /// # Ok::<(), InvalidCode>(())
// /// ```
pub fn parse_entry(line: &str) -> Result<(String, String), InvalidCode> {
    let (raw_code, name) = line.split_once(';').unwrap_or((line, ""));
    let code = normalize_code(raw_code).ok_or_else(|| InvalidCode(raw_code.to_string()))?;
    Ok((code, name.trim().to_string()))
}

/// Tres letras ASCII mayúsculas, un guion y tres dígitos.
fn has_code_shape(code: &str) -> bool {
    let b = code.as_bytes();
    b.len() == 7
        && b[..3].iter().all(u8::is_ascii_uppercase)
        && b[3] == b'-'
        && b[4..].iter().all(u8::is_ascii_digit)
}

#[cfg(test)]
mod tests {
    // PASO 1: un test de una función privada.
    // use super::*;
    //
    // #[test]
    // fn shape_is_three_letters_dash_three_digits() {
    //     assert!(has_code_shape("MUS-001"));
    //     assert!(!has_code_shape("MU-0001"), "two letters");
    //     assert!(!has_code_shape("mus-001"), "lowercase");
    //     assert!(!has_code_shape("MUS-0010"), "too long");
    // }
}
