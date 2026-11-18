//! Mostrador de préstamos del museo, con los dobles generados por mockall.

/// Aviso que recibe la institución cuando se aprueba un préstamo.
#[derive(Debug, Clone, PartialEq)]
pub struct Notice {
    pub institution: String,
    pub code: String,
    pub days: u32,
}

/// Por qué no se aprobó un préstamo.
#[derive(Debug, PartialEq)]
pub enum DeskError {
    /// La institución no está acreditada.
    NotAccredited,
    /// El aviso no se pudo enviar.
    NotSent(String),
}

/// Dice si una institución está acreditada para recibir préstamos.
#[cfg_attr(test, mockall::automock)]
pub trait Registry {
    fn is_accredited(&self, institution: &str) -> bool;
}

/// Envía el aviso a la institución.
#[cfg_attr(test, mockall::automock)]
pub trait Notifier {
    fn send(&self, notice: &Notice) -> Result<(), String>;
}

/// Mostrador de préstamos, genérico sobre sus dos dependencias.
pub struct LoanDesk<R: Registry, N: Notifier> {
    pub registry: R,
    pub notifier: N,
}

impl<R: Registry, N: Notifier> LoanDesk<R, N> {
    /// Aprueba el préstamo de la pieza `code` a `institution` durante `days` días.
    pub fn approve(&self, institution: &str, code: &str, days: u32) -> Result<Notice, DeskError> {
        if !self.registry.is_accredited(institution) {
            return Err(DeskError::NotAccredited);
        }
        let notice = Notice {
            institution: institution.to_string(),
            code: code.to_string(),
            days,
        };
        self.notifier.send(&notice).map_err(DeskError::NotSent)?;
        Ok(notice)
    }
}

#[cfg(test)]
mod tests {
    // PASO 1: mocks generados con automock.
    // use super::*;
    //
    // #[test]
    // fn rejected_loan_sends_nothing() {
    //     let mut registry = MockRegistry::new();
    //     registry.expect_is_accredited().return_const(false);
    //     let mut notifier = MockNotifier::new();
    //     notifier.expect_send().never();
    //
    //     let desk = LoanDesk { registry, notifier };
    //     assert_eq!(
    //         desk.approve("MNAC", "MUS-001", 30),
    //         Err(DeskError::NotAccredited)
    //     );
    // }
    //
    // PASO 2: argumentos y número de llamadas.
    // use mockall::predicate;
    //
    // #[test]
    // fn approved_loan_sends_the_notice() {
    //     let notice = Notice {
    //         institution: "MNAC".to_string(),
    //         code: "MUS-001".to_string(),
    //         days: 30,
    //     };
    //     let mut registry = MockRegistry::new();
    //     registry
    //         .expect_is_accredited()
    //         .with(predicate::eq("MNAC"))
    //         .times(1)
    //         .return_const(true);
    //     let mut notifier = MockNotifier::new();
    //     notifier
    //         .expect_send()
    //         .with(predicate::eq(notice.clone()))
    //         .times(1)
    //         .returning(|_| Ok(()));
    //
    //     let desk = LoanDesk { registry, notifier };
    //     assert_eq!(desk.approve("MNAC", "MUS-001", 30), Ok(notice));
    // }
    //
    // PASO 3: un mock que devuelve un error.
    // #[test]
    // fn notifier_failure_is_reported() {
    //     let mut registry = MockRegistry::new();
    //     registry.expect_is_accredited().return_const(true);
    //     let mut notifier = MockNotifier::new();
    //     notifier
    //         .expect_send()
    //         .returning(|_| Err("smtp down".to_string()));
    //
    //     let desk = LoanDesk { registry, notifier };
    //     let got = desk.approve("MNAC", "MUS-001", 30);
    //     assert_eq!(got, Err(DeskError::NotSent("smtp down".to_string())));
    // }
    //
    // PASO 4: una condición propia con withf.
    // #[test]
    // fn notice_carries_the_days() {
    //     let mut registry = MockRegistry::new();
    //     registry.expect_is_accredited().return_const(true);
    //     let mut notifier = MockNotifier::new();
    //     notifier
    //         .expect_send()
    //         .withf(|n: &Notice| n.days == 90)
    //         .times(1)
    //         .returning(|_| Ok(()));
    //
    //     let desk = LoanDesk { registry, notifier };
    //     assert!(desk.approve("MNAC", "MUS-001", 90).is_ok());
    // }
}
