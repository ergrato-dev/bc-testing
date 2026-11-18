//! Mostrador de préstamos del museo: aprueba un préstamo si la institución
//! está acreditada y le envía el aviso con las condiciones.

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
pub trait Registry {
    fn is_accredited(&self, institution: &str) -> bool;
}

/// Envía el aviso a la institución.
pub trait Notifier {
    fn send(&self, notice: &Notice) -> Result<(), String>;
}

/// Mostrador de préstamos. Es genérico: recibe cualquier tipo que implemente
/// `Registry` y cualquiera que implemente `Notifier`.
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

/// Envía el aviso por todos los canales y devuelve cuántos lo entregaron.
/// Cada canal puede ser un tipo distinto: la lista guarda `Box<dyn Notifier>`.
pub fn broadcast(channels: &[Box<dyn Notifier>], notice: &Notice) -> usize {
    channels.iter().filter(|c| c.send(notice).is_ok()).count()
}

#[cfg(test)]
mod tests {
    // PASO 1: un stub y un servicio genérico.
    // use super::*;
    //
    // struct StubRegistry {
    //     accredited: bool,
    // }
    //
    // impl Registry for StubRegistry {
    //     fn is_accredited(&self, _institution: &str) -> bool {
    //         self.accredited
    //     }
    // }
    //
    // struct SilentNotifier;
    //
    // impl Notifier for SilentNotifier {
    //     fn send(&self, _notice: &Notice) -> Result<(), String> {
    //         Ok(())
    //     }
    // }
    //
    // #[test]
    // fn rejects_unaccredited_institution() {
    //     let desk = LoanDesk {
    //         registry: StubRegistry { accredited: false },
    //         notifier: SilentNotifier,
    //     };
    //     assert_eq!(
    //         desk.approve("MNAC", "MUS-001", 30),
    //         Err(DeskError::NotAccredited)
    //     );
    // }
    //
    // PASO 2: un fake que guarda los avisos desde &self.
    // use std::cell::RefCell;
    //
    // #[derive(Default)]
    // struct FakeNotifier {
    //     sent: RefCell<Vec<Notice>>,
    // }
    //
    // impl Notifier for FakeNotifier {
    //     fn send(&self, notice: &Notice) -> Result<(), String> {
    //         self.sent.borrow_mut().push(notice.clone());
    //         Ok(())
    //     }
    // }
    //
    // #[test]
    // fn approved_loan_sends_one_notice() {
    //     let desk = LoanDesk {
    //         registry: StubRegistry { accredited: true },
    //         notifier: FakeNotifier::default(),
    //     };
    //     let notice = desk.approve("MNAC", "MUS-001", 30).unwrap();
    //     assert_eq!(*desk.notifier.sent.borrow(), vec![notice]);
    // }
    //
    // #[test]
    // fn rejected_loan_sends_nothing() {
    //     let desk = LoanDesk {
    //         registry: StubRegistry { accredited: false },
    //         notifier: FakeNotifier::default(),
    //     };
    //     let _ = desk.approve("MNAC", "MUS-001", 30);
    //     assert!(desk.notifier.sent.borrow().is_empty());
    // }
    //
    // PASO 3: un doble que falla.
    // struct FailingNotifier;
    //
    // impl Notifier for FailingNotifier {
    //     fn send(&self, _notice: &Notice) -> Result<(), String> {
    //         Err("smtp down".to_string())
    //     }
    // }
    //
    // #[test]
    // fn notifier_failure_is_reported() {
    //     let desk = LoanDesk {
    //         registry: StubRegistry { accredited: true },
    //         notifier: FailingNotifier,
    //     };
    //     let got = desk.approve("MNAC", "MUS-001", 30);
    //     assert_eq!(got, Err(DeskError::NotSent("smtp down".to_string())));
    // }
    //
    // PASO 4: canales de tipos distintos con dyn Notifier.
    // #[test]
    // fn broadcast_counts_delivered_channels() {
    //     let channels: Vec<Box<dyn Notifier>> = vec![
    //         Box::new(SilentNotifier),
    //         Box::new(FailingNotifier),
    //         Box::new(FakeNotifier::default()),
    //     ];
    //     let notice = Notice {
    //         institution: "MNAC".to_string(),
    //         code: "MUS-001".to_string(),
    //         days: 30,
    //     };
    //     assert_eq!(broadcast(&channels, &notice), 2);
    // }
}
