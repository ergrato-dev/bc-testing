//! El recargo por devolver un libro tarde.

/// Recargo por cada día de retraso.
pub const FEE_PER_DAY: u32 = 50;
/// Recargo máximo, se tarde lo que se tarde.
pub const MAX_FEE: u32 = 1000;

/// Recargo por `days` días de retraso: 50 por día, hasta 1000.
///
/// ```
/// assert_eq!(lab01::fees::late_fee(3), 150);
/// assert_eq!(lab01::fees::late_fee(365), 1000);
/// ```
pub fn late_fee(days: u32) -> u32 {
    days.saturating_mul(FEE_PER_DAY).min(MAX_FEE)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn no_fee_on_time() {
        assert_eq!(late_fee(0), 0);
    }

    #[test]
    fn fifty_per_day() {
        assert_eq!(late_fee(3), 150);
    }

    #[test]
    fn capped_at_the_maximum() {
        assert_eq!(late_fee(20), 1000);
        assert_eq!(late_fee(u32::MAX), 1000);
    }
}
