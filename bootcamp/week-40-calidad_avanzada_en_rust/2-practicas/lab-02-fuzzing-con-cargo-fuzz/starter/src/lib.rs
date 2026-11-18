//! Lector de ISBN-13 para el catálogo de la biblioteca del museo.

/// Un ISBN-13 válido: 13 dígitos, sin guiones ni espacios.
#[derive(Debug, PartialEq)]
pub struct Isbn(String);

impl Isbn {
    pub fn as_str(&self) -> &str {
        &self.0
    }
}

/// Por qué un texto no es un ISBN-13.
#[derive(Debug, PartialEq)]
pub enum IsbnError {
    Length,
    Checksum,
}

/// Lee un ISBN-13. Acepta el prefijo `ISBN `, guiones y espacios:
/// `"ISBN 978-84-9105-027-8"` es `9788491050278`.
pub fn parse(s: &str) -> Result<Isbn, IsbnError> {
    let s = if s.starts_with("ISBN") { &s[5..] } else { s }; // salta "ISBN "
    let digits: String = s.chars().filter(|c| *c != '-' && *c != ' ').collect();
    if digits.chars().count() != 13 {
        return Err(IsbnError::Length);
    }
    let mut sum = 0;
    for (i, c) in digits.chars().enumerate() {
        let d = c.to_digit(10).unwrap_or(0);
        sum += if i % 2 == 1 { d * 3 } else { d };
    }
    if sum % 10 != 0 {
        return Err(IsbnError::Checksum);
    }
    Ok(Isbn(digits))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn table() {
        let cases = [
            ("9788491050278", Ok("9788491050278")),
            ("978-84-9105-027-8", Ok("9788491050278")),
            ("ISBN 978 84 9105 027 8", Ok("9788491050278")),
            ("978849105027", Err(IsbnError::Length)),
            ("97884910502789", Err(IsbnError::Length)),
            ("9788491050279", Err(IsbnError::Checksum)),
            ("", Err(IsbnError::Length)),
        ];
        for (input, want) in cases {
            assert_eq!(
                parse(input).map(|i| i.0),
                want.map(String::from),
                "{input:?}"
            );
        }
    }

    // PASO 3
    // #[test]
    // fn a_bare_prefix_is_too_short() {
    //     assert_eq!(parse("ISBN"), Err(IsbnError::Length));
    // }
    //
    // PASO 4
    // #[test]
    // fn rejects_letters() {
    //     assert_eq!(parse("ABCDEFGHIJKLM"), Err(IsbnError::NotADigit));
    // }
}
