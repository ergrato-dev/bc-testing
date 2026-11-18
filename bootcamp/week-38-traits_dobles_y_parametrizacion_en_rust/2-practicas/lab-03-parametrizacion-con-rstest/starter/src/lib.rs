//! Reglas del museo: validación de piezas y cargos de una devolución.

/// Longitud máxima del nombre de una pieza, en caracteres.
pub const MAX_NAME_LENGTH: usize = 120;
/// Recargo por cada día de retraso.
pub const LATE_FEE_PER_DAY: u32 = 50;
/// Recargo máximo por retraso.
pub const MAX_LATE_FEE: u32 = 1500;

/// Motivo por el que una pieza no es válida.
#[derive(Debug, PartialEq)]
pub enum PieceError {
    InvalidCode,
    NameRequired,
    NameTooLong,
    ZeroValue,
}

/// Valida una pieza: código `AAA-000`, nombre de 1 a 120 caracteres y valor
/// declarado mayor que 0.
pub fn validate_piece(code: &str, name: &str, declared_value: u32) -> Result<(), PieceError> {
    if !is_code(code) {
        return Err(PieceError::InvalidCode);
    }
    if name.trim().is_empty() {
        return Err(PieceError::NameRequired);
    }
    if name.chars().count() > MAX_NAME_LENGTH {
        return Err(PieceError::NameTooLong);
    }
    if declared_value == 0 {
        return Err(PieceError::ZeroValue);
    }
    Ok(())
}

fn is_code(code: &str) -> bool {
    let b = code.as_bytes();
    b.len() == 7
        && b[..3].iter().all(u8::is_ascii_uppercase)
        && b[3] == b'-'
        && b[4..].iter().all(u8::is_ascii_digit)
}

/// Recargo por `late_days` días de retraso: 50 por día, hasta 1500.
pub fn late_fee(late_days: u32) -> u32 {
    late_days.saturating_mul(LATE_FEE_PER_DAY).min(MAX_LATE_FEE)
}

/// Estado en que vuelve una pieza.
#[derive(Debug, Clone, Copy, PartialEq)]
pub enum Condition {
    Good,
    Damaged,
}

/// Cargos de una devolución.
#[derive(Debug, PartialEq)]
pub struct ReturnCharges {
    pub late_fee: u32,
    pub damage_fee: u32,
    pub total: u32,
}

/// Cargos de la devolución: el retraso y, si vuelve dañada, el 10 % del valor.
pub fn return_charges(declared_value: u32, late_days: u32, condition: Condition) -> ReturnCharges {
    let late_fee = late_fee(late_days);
    let damage_fee = match condition {
        Condition::Good => 0,
        Condition::Damaged => declared_value / 10,
    };
    ReturnCharges {
        late_fee,
        damage_fee,
        total: late_fee.saturating_add(damage_fee),
    }
}

#[cfg(test)]
mod tests {
    // PASO 1: casos con #[case].
    // use super::*;
    // use rstest::rstest;
    //
    // #[rstest]
    // #[case(0, 0)]
    // #[case(1, 50)]
    // #[case(30, 1500)]
    // #[case(31, 1500)]
    // #[case(u32::MAX, 1500)]
    // fn late_fee_by_days(#[case] days: u32, #[case] want: u32) {
    //     assert_eq!(late_fee(days), want);
    // }
    //
    // PASO 2: casos con nombre.
    // #[rstest]
    // #[case::valid("MUS-001", "Ánfora", 1, Ok(()))]
    // #[case::lowercase_code("mus-001", "Ánfora", 1, Err(PieceError::InvalidCode))]
    // #[case::short_code("MUS-01", "Ánfora", 1, Err(PieceError::InvalidCode))]
    // #[case::blank_name("MUS-001", "   ", 1, Err(PieceError::NameRequired))]
    // #[case::zero_value("MUS-001", "Ánfora", 0, Err(PieceError::ZeroValue))]
    // fn validate(
    //     #[case] code: &str,
    //     #[case] name: &str,
    //     #[case] value: u32,
    //     #[case] want: Result<(), PieceError>,
    // ) {
    //     assert_eq!(validate_piece(code, name, value), want);
    // }
    //
    // PASO 3: combinaciones con #[values].
    // #[rstest]
    // fn total_is_the_sum(
    //     #[values(0, 1, 30, 31)] days: u32,
    //     #[values(Condition::Good, Condition::Damaged)] condition: Condition,
    // ) {
    //     let c = return_charges(5000, days, condition);
    //     assert_eq!(c.total, c.late_fee + c.damage_fee);
    // }
    //
    // PASO 4: una fixture con argumento.
    // use rstest::fixture;
    //
    // #[fixture]
    // fn name(#[default(MAX_NAME_LENGTH)] len: usize) -> String {
    //     "á".repeat(len)
    // }
    //
    // #[rstest]
    // fn accepts_name_at_max_length(name: String) {
    //     assert_eq!(validate_piece("MUS-001", &name, 1), Ok(()));
    // }
    //
    // #[rstest]
    // fn rejects_name_over_max_length(#[with(MAX_NAME_LENGTH + 1)] name: String) {
    //     assert_eq!(
    //         validate_piece("MUS-001", &name, 1),
    //         Err(PieceError::NameTooLong)
    //     );
    // }
}
