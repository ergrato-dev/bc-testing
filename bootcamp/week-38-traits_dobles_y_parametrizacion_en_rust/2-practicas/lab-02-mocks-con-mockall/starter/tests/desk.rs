// PASO 5: un mock declarado con mock! para tests/.
// use lab02::{DeskError, LoanDesk, Notice, Notifier, Registry};
// use mockall::mock;
//
// mock! {
//     pub Registry {}
//     impl Registry for Registry {
//         fn is_accredited(&self, institution: &str) -> bool;
//     }
// }
//
// mock! {
//     pub Notifier {}
//     impl Notifier for Notifier {
//         fn send(&self, notice: &Notice) -> Result<(), String>;
//     }
// }
//
// #[test]
// fn unaccredited_institution_is_rejected() {
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
