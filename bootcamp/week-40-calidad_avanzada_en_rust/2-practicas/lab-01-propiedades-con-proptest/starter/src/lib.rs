//! Cálculos del museo para probar con propiedades.

/// Recargo por cada día de retraso.
pub const LATE_FEE_PER_DAY: u32 = 50;
/// Recargo máximo por retraso.
pub const MAX_LATE_FEE: u32 = 1500;

/// Recargo por `days` días de retraso: 50 por día, hasta 1500.
pub fn late_fee(days: u32) -> u32 {
    days.saturating_mul(LATE_FEE_PER_DAY).min(MAX_LATE_FEE)
}

/// Reparte `total` entre `parts` prestatarios. Las partes suman `total` y
/// se diferencian como mucho en 1. `parts` debe ser mayor que 0.
pub fn split_fee(total: u32, parts: u32) -> Vec<u32> {
    let mut shares = vec![total / parts; parts as usize];
    shares[0] += total % parts;
    shares
}

/// Escribe un código de pieza: el prefijo, un guion y el número con tres
/// cifras. `format_code("MUS", 7)` es `"MUS-007"`.
pub fn format_code(prefix: &str, number: u16) -> String {
    format!("{prefix}-{number:03}")
}

/// Lee un código de pieza: tres letras ASCII mayúsculas, un guion y tres
/// dígitos. Devuelve el prefijo y el número.
pub fn parse_code(code: &str) -> Option<(String, u16)> {
    let (prefix, digits) = code.split_once('-')?;
    let letters = prefix.len() == 3 && prefix.bytes().all(|b| b.is_ascii_uppercase());
    let numbers = digits.len() == 3 && digits.bytes().all(|b| b.is_ascii_digit());
    if !letters || !numbers {
        return None;
    }
    Some((prefix.to_string(), digits.parse().ok()?))
}

#[cfg(test)]
mod tests {
    // PASO 1
    // use super::*;
    // use proptest::prelude::*;
    //
    // proptest! {
    //     #[test]
    //     fn late_fee_never_exceeds_the_cap(days in any::<u32>()) {
    //         prop_assert!(late_fee(days) <= MAX_LATE_FEE);
    //     }
    //
    //     #[test]
    //     fn late_fee_never_decreases(a in any::<u32>(), b in any::<u32>()) {
    //         let (short, long) = if a <= b { (a, b) } else { (b, a) };
    //         prop_assert!(late_fee(short) <= late_fee(long));
    //     }
    // }
    //
    // PASO 2
    // proptest! {
    //     #[test]
    //     fn split_keeps_the_total(total in 0u32..100_000, parts in 1u32..10) {
    //         let shares = split_fee(total, parts);
    //         prop_assert_eq!(shares.len(), parts as usize);
    //         prop_assert_eq!(shares.iter().sum::<u32>(), total);
    //     }
    //
    //     #[test]
    //     fn split_is_fair(total in 0u32..100_000, parts in 1u32..10) {
    //         let shares = split_fee(total, parts);
    //         let max = shares.iter().max().unwrap();
    //         let min = shares.iter().min().unwrap();
    //         prop_assert!(max - min <= 1, "shares = {:?}", shares);
    //     }
    // }
    //
    // PASO 3
    // proptest! {
    //     #[test]
    //     fn a_formatted_code_parses_back(prefix in "[A-Z]{3}", number in 0u16..1000) {
    //         let code = format_code(&prefix, number);
    //         prop_assert_eq!(parse_code(&code), Some((prefix, number)));
    //     }
    //
    //     #[test]
    //     fn parse_code_never_panics(code in "\\PC*") {
    //         let _ = parse_code(&code);
    //     }
    // }
    //
    // PASO 4
    // prop_compose! {
    //     fn malformed_code()(
    //         prefix in "[A-Za-z]{1,4}",
    //         digits in "[0-9]{1,4}",
    //     ) -> String {
    //         format!("{prefix}-{digits}")
    //     }
    // }
    //
    // proptest! {
    //     #![proptest_config(ProptestConfig::with_cases(1000))]
    //
    //     #[test]
    //     fn rejects_codes_with_the_wrong_shape(code in malformed_code()) {
    //         let (prefix, digits) = code.split_once('-').unwrap();
    //         prop_assume!(prefix.len() != 3 || digits.len() != 3
    //             || prefix.bytes().any(|b| b.is_ascii_lowercase()));
    //         prop_assert_eq!(parse_code(&code), None);
    //     }
    // }
}
