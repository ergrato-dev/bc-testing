//! Préstamos de piezas del museo: el seguro y los plazos para pagarlo.

/// Seguro mínimo de un préstamo.
pub const MIN_PREMIUM: u32 = 100;

/// Motivo por el que no se puede calcular un préstamo.
#[derive(Debug, PartialEq)]
pub enum LoanError {
    /// El valor declarado de la pieza es 0.
    ZeroValue,
    /// El préstamo dura 0 días.
    ZeroDays,
}

/// Seguro de un préstamo: 1 por mil del valor declarado por día, con un mínimo de 100.
pub fn loan_premium(declared_value: u32, days: u32) -> Result<u32, LoanError> {
    if declared_value == 0 {
        return Err(LoanError::ZeroValue);
    }
    if days == 0 {
        return Err(LoanError::ZeroDays);
    }
    Ok((declared_value.saturating_mul(days) / 1000).max(MIN_PREMIUM))
}

/// Reparte `total` en `n` plazos. Lo que sobra de la división va a los primeros.
///
/// Entra en pánico si `n` es 0: es un error del programa, no de los datos.
pub fn installments(total: u32, n: u32) -> Vec<u32> {
    assert!(n > 0, "installments: n must be at least 1");
    let (base, rest) = (total / n, total % n);
    (0..n).map(|i| base + u32::from(i < rest)).collect()
}

/// Recibo de un préstamo: la pieza, el seguro y los plazos.
#[derive(Debug, PartialEq)]
pub struct Receipt {
    pub piece: String,
    pub premium: u32,
    pub installments: Vec<u32>,
}

/// Calcula el recibo de un préstamo pagado en `n` plazos.
pub fn receipt(piece: &str, declared_value: u32, days: u32, n: u32) -> Result<Receipt, LoanError> {
    let premium = loan_premium(declared_value, days)?;
    Ok(Receipt {
        piece: piece.to_string(),
        premium,
        installments: installments(premium, n),
    })
}

#[cfg(test)]
mod tests {
    // PASO 1: un pánico esperado.
    // use super::*;
    //
    // #[test]
    // #[should_panic(expected = "n must be at least 1")]
    // fn installments_reject_zero() {
    //     installments(1000, 0);
    // }

    // PASO 2: un error esperado.
    // #[test]
    // fn premium_rejects_zero_days() {
    //     assert_eq!(loan_premium(5000, 0), Err(LoanError::ZeroDays));
    // }
    //
    // #[test]
    // fn premium_rejects_zero_value() {
    //     assert!(matches!(loan_premium(0, 10), Err(LoanError::ZeroValue)));
    // }

    // PASO 3: un test que devuelve Result.
    // #[test]
    // fn receipt_for_a_month() -> Result<(), LoanError> {
    //     let receipt = receipt("MUS-001", 200_000, 30, 3)?;
    //     assert_eq!(receipt.premium, 6000);
    //     Ok(())
    // }

    // PASO 4: comparar una estructura entera.
    // #[test]
    // fn receipt_splits_the_premium() -> Result<(), LoanError> {
    //     let expected = Receipt {
    //         piece: "MUS-001".to_string(),
    //         premium: 101,
    //         installments: vec![34, 34, 33],
    //     };
    //     assert_eq!(receipt("MUS-001", 101_000, 1, 3)?, expected);
    //     Ok(())
    // }
}
