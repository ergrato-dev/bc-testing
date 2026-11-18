//! Recargos por retraso de la biblioteca del museo.

/// Recargo por cada día de retraso.
pub const PER_DAY: u32 = 50;
/// Recargo máximo.
pub const MAX: u32 = 1500;
/// Días de gracia: un retraso de hasta 2 días no se cobra.
pub const GRACE: u32 = 2;

/// Tipo de usuario de la biblioteca.
#[derive(Debug, Clone, Copy, PartialEq)]
pub enum Category {
    Public,
    Member,
    Staff,
}

/// Días que se cobran: los de retraso menos los de gracia.
pub fn chargeable_days(days: u32) -> u32 {
    if days > GRACE { days - GRACE } else { 0 }
}

/// Recargo por `days` días de retraso. El personal no paga y los socios
/// pagan la mitad.
pub fn late_fee(days: u32, category: Category) -> u32 {
    let fee = chargeable_days(days).saturating_mul(PER_DAY).min(MAX);
    if category == Category::Staff {
        return 0;
    }
    if category == Category::Member {
        return fee / 2;
    }
    return fee;
}

/// El recibo de una devolución.
///
/// ```
/// use lab04::{receipt, Category};
///
/// assert_eq!(receipt("Ana", 5, Category::Public), "Ana: 5 días, 150");
/// ```
pub fn receipt(member: &str, days: u32, category: Category) -> String {
    format!("{}: {} días, {}", member, days, late_fee(days, category))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn public_pays_per_chargeable_day() {
        assert_eq!(late_fee(5, Category::Public), 150);
    }

    #[test]
    fn staff_pays_nothing() {
        assert_eq!(late_fee(5, Category::Staff), 0);
    }

    // PASO 3
    // #[test]
    // fn members_pay_half() {
    //     assert_eq!(late_fee(5, Category::Member), 75);
    // }
    //
    // #[test]
    // fn no_fee_within_grace() {
    //     assert_eq!(late_fee(1, Category::Public), 0);
    // }
    //
    // PASO 4
    // #[test]
    // fn grace_ends_after_two_days() {
    //     assert_eq!(chargeable_days(2), 0);
    //     assert_eq!(chargeable_days(3), 1);
    // }
}
