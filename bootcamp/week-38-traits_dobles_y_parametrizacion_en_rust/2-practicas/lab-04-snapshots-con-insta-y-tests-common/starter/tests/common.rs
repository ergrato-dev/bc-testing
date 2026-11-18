//! Datos compartidos por los tests de tests/.

use lab04::Notice;

/// El aviso del préstamo de MUS-001 al MNAC durante 30 días.
pub fn sample_notice() -> Notice {
    Notice {
        institution: "MNAC".to_string(),
        code: "MUS-001".to_string(),
        days: 30,
        premium: 6000,
    }
}
