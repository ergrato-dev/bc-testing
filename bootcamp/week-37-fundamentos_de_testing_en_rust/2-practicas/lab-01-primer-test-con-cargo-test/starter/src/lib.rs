//! Multas por retraso de la biblioteca.

/// Multa por cada día de retraso.
pub const FEE_PER_DAY: u32 = 50;
/// Multa máxima: se alcanza a los 20 días de retraso.
pub const MAX_FEE: u32 = 1000;

/// Devuelve la multa por `days_late` días de retraso: 50 por día, hasta 1000.
pub fn late_fee(days_late: u32) -> u32 {
    (days_late * FEE_PER_DAY).min(MAX_FEE)
}

#[cfg(test)]
mod tests {
    // PASO 1: el primer test.
    // use super::*;
    //
    // #[test]
    // fn late_fee_three_days() {
    //     assert_eq!(late_fee(3), 150);
    // }

    // PASO 2: el tope, con un mensaje propio.
    // #[test]
    // fn late_fee_is_capped() {
    //     assert_eq!(late_fee(30), MAX_FEE, "30 days late should hit the cap");
    // }

    // PASO 3: justo antes del tope, y un día de retraso.
    // #[test]
    // fn late_fee_below_the_cap() {
    //     let fee = late_fee(19);
    //     assert!(fee < MAX_FEE, "below the cap, got {fee}");
    //     assert_ne!(late_fee(1), 0, "one day late must cost something");
    // }

    // PASO 4: el caso que el tipo u32 no elimina.
    // #[test]
    // fn huge_delay_is_capped() {
    //     assert_eq!(late_fee(u32::MAX), MAX_FEE);
    // }
}
