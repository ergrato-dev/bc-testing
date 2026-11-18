# Proyecto Semanal - El servicio del dominio con traits y dobles

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Portar a la carpeta `rust/` el servicio principal de tu dominio que aislaste en Go en la [semana 33](../../week-33-interfaces_y_dobles_de_prueba_en_go/3-proyecto/README.md), con sus dependencias como traits, y probarlo con las herramientas de esta semana:

- fakes escritos a mano y compartidos en `tests/common/mod.rs`;
- un mock de mockall para la dependencia cuya llamada importa;
- los rechazos como casos de rstest;
- el texto que envía el servicio como snapshot de insta;
- un `dobles.md` que justifica cada doble.

## Dónde va

En la carpeta `rust/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), junto a las reglas que portaste en la [semana 37](../../week-37-fundamentos_de_testing_en_rust/3-proyecto/README.md).

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama y el starter

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-38
   ```

2. Copia el starter:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-38-traits_dobles_y_parametrizacion_en_rust/3-proyecto/starter/. .
   ```

   Con `-n`, `cp` no sobrescribe nada de la semana 37. El starter trae:

   | Archivo | Qué es |
   |---|---|
   | `rust/src/lending.rs` | El módulo del servicio, vacío, con un TODO por pieza |
   | `rust/tests/common/mod.rs` | El módulo compartido de los tests, con `#![allow(dead_code)]` y dos TODO |
   | `rust/tests/lending.rs` | Dos tests con `#[ignore = "TODO: ..."]`: el caso feliz y los rechazos |
   | `rust/tests/notice.rs` | Un test con `#[ignore = "TODO: ..."]` para el snapshot |
   | `rust/dobles.md` | La plantilla de la justificación de los dobles |

   Si tu servicio no presta piezas, renombra `lending.rs` y los tests con el nombre de tu servicio: `booking.rs`, `enrollment.rs`...

### Paso 2: las dependencias y el módulo

1. Añade las tres dependencias de los tests:

   ```bash
   cd rust
   cargo add --dev mockall@=0.15.0 rstest@=0.28.0 insta@=1.49.0
   #     Updating crates.io index
   #       Adding mockall =0.15.0 to dev-dependencies
   # ...
   #       Adding rstest =0.28.0 to dev-dependencies
   # ...
   #       Adding insta =1.49.0 to dev-dependencies
   # ...
   ```

   `cargo add` escribe las tres líneas en `[dev-dependencies]` y actualiza `Cargo.lock`. Las versiones son las de [stack-versions](../../../docs/stack-versions.md).

2. Declara el módulo en `src/lib.rs`, junto a los de la semana 37, y reexpórtalo para que `tests/` lo importe como `museum::LendingService`:

   ```rust
   mod fees;
   mod lending;
   mod piece;

   pub use fees::*;
   pub use lending::*;
   pub use piece::*;
   ```

3. Ejecuta:

   ```bash
   cargo test
   # warning: unused import: `lending::*`
   # ...
   #      Running tests/lending.rs (target/debug/build/museum/0a7b85be44002192/out/lending-0a7b85be44002192)
   #
   # running 2 tests
   # test happy_path ... ignored, TODO: caso feliz con los fakes y el mock (pasos 4 y 5)
   # test rejections ... ignored, TODO: un #[case] por rechazo con rstest, sin llamadas al mock (paso 6)
   # ...
   #      Running tests/notice.rs (target/debug/build/museum/584e66786bee3f89/out/notice-584e66786bee3f89)
   #
   # running 1 test
   # test notice_text ... ignored, TODO: el texto del aviso con assert_snapshot! (paso 7)
   # ...
   ```

   El warning desaparece en cuanto `lending.rs` tenga algo público. Instala también `cargo-insta` si no lo hiciste en el [lab 04](../2-practicas/lab-04-snapshots-con-insta-y-tests-common/README.md#preparación).

### Paso 3: los traits y el servicio

1. Abre tu servicio de Go de la semana 33 (`LendingService` en el museo) y pórtalo a `src/lending.rs`:

   - los tipos que recibe y devuelve (`Piece`, `Status`, `Loan`, `LoanNotice`), con `#[derive(Debug, Clone, PartialEq)]` para compararlos con `assert_eq!`;
   - un `enum` de error con una variante por cada rechazo (`LendError::UnknownPiece`, `NotAvailable`...) y una por cada dependencia que puede fallar (`Store(String)`, `Notify(String)`);
   - **un trait público por dependencia**, con los métodos que el servicio usa y `&self` como receptor:

     ```rust
     /// Lo que `LendingService` necesita del almacén de piezas.
     pub trait PieceStore {
         fn find_by_code(&self, code: &str) -> Option<Piece>;
         fn save(&self, piece: &Piece) -> Result<(), String>;
     }
     ```

   - **el servicio, genérico** sobre sus dependencias, con los campos públicos para que los tests lean los fakes:

     ```rust
     pub struct LendingService<P, L, R, N> {
         pub pieces: P,
         pub loans: L,
         pub registry: R,
         pub notifier: N,
     }

     impl<P: PieceStore, L: LoanStore, R: Registry, N: Notifier> LendingService<P, L, R, N> {
         pub fn lend(&self, code: &str, institution: &str, days: u32) -> Result<Piece, LendError> {
             // ...
         }
     }
     ```

2. Reutiliza los cálculos de la semana 37: en el museo, `lend` llama a `loan_premium` y convierte su error con `map_err(|_| LendError::ZeroDays)?`.
3. Si el servicio de Go usaba la hora (`Now func() time.Time`), deja la fecha fuera: la librería estándar de Rust no tiene fechas de calendario. El aviso lleva los días del préstamo, no la fecha de vencimiento.

Referencia: [teoría 01](../1-teoria/01-traits-y-fakes-a-mano.md) y [lab 01](../2-practicas/lab-01-traits-y-fakes-a-mano/README.md).

### Paso 4: los fakes en `tests/common`

En `tests/common/mod.rs`, escribe:

1. **Un fake por almacén**, que guarde en memoria dentro de un `RefCell` y que se pueda crear con datos:

   ```rust
   use std::cell::RefCell;
   use std::collections::HashMap;

   #[derive(Default)]
   pub struct FakePieces(pub RefCell<HashMap<String, Piece>>);

   impl FakePieces {
       pub fn with(pieces: &[Piece]) -> Self {
           let map = pieces.iter().map(|p| (p.code.clone(), p.clone())).collect();
           FakePieces(RefCell::new(map))
       }
   }

   impl PieceStore for FakePieces {
       fn find_by_code(&self, code: &str) -> Option<Piece> {
           self.0.borrow().get(code).cloned()
       }

       fn save(&self, piece: &Piece) -> Result<(), String> {
           self.0.borrow_mut().insert(piece.code.clone(), piece.clone());
           Ok(())
       }
   }
   ```

   `FakePieces(pub RefCell<...>)` es un `struct` de un solo campo sin nombre; se lee con `.0`. En Go un solo `fakeStore` hacía de los dos almacenes; aquí cada campo del servicio es dueño de su doble, y dos fakes pequeños se leen sin compartir nada.

2. **Un stub** para la dependencia que solo responde, como el registro: `pub struct StubRegistry(pub Vec<&'static str>);`, que acredita las instituciones de la lista.
3. **Los datos de ejemplo** que usarán varios tests: una pieza (`amphora(status)`) y el aviso esperado (`mnac_notice()`).

`tests/lending.rs` y `tests/notice.rs` ya declaran `mod common;`. El `#![allow(dead_code)]` del principio evita el warning falso de las funciones que un archivo no usa ([lab 04](../2-practicas/lab-04-snapshots-con-insta-y-tests-common/README.md#paso-4-testscommonmodrs)).

### Paso 5: el mock y el caso feliz

1. Elige la dependencia cuya **llamada** es lo que importa: en el museo, el notificador. Declara su mock en `tests/common/mod.rs` con `mock!`, porque los tests están en `tests/` y `#[automock]` no generaría nada para ellos ([lab 02](../2-practicas/lab-02-mocks-con-mockall/README.md#paso-5-un-mock-para-tests-paso-5)):

   ```rust
   use mockall::mock;

   mock! {
       pub Notifier {}
       impl Notifier for Notifier {
           fn send(&self, notice: &LoanNotice) -> Result<(), String>;
       }
   }
   ```

2. En `tests/lending.rs`, sustituye `happy_path` por el caso feliz. El mock fija la llamada; los fakes, el estado final:

   ```rust
   #[test]
   fn lends_an_available_piece() {
       let mut notifier = MockNotifier::new();
       notifier
           .expect_send()
           .with(eq(mnac_notice()))
           .times(1)
           .returning(|_| Ok(()));
       let svc = service(Status::Available, notifier);

       let piece = svc.lend("MUS-001", "MNAC", 30).unwrap();

       assert_eq!(piece.status, Status::OnLoan);
       assert_eq!(svc.pieces.0.borrow()["MUS-001"].status, Status::OnLoan);
       assert_eq!(svc.loans.0.borrow().len(), 1);
   }
   ```

   `service(status, notifier)` es una función del propio archivo que construye el servicio con los fakes y la pieza en ese estado: la usarán todos los tests de `tests/lending.rs`. Usa el `assert_eq!` de `pretty_assertions` (`use pretty_assertions::assert_eq;`).

3. Añade un test para el fallo de la dependencia mockeada: `.returning(|_| Err("smtp down".to_string()))`. Comprueba el error y qué queda en los fakes. En el museo, el préstamo **ya está abierto** cuando falla el aviso: es el riesgo que anotaste en el reporte de la semana 36. El test fija el comportamiento actual y `dobles.md` lo explica.

### Paso 6: los rechazos con rstest

Sustituye `rejections` por una función con un `#[case::nombre(...)]` por rechazo, con **al menos cuatro casos**. Cada caso lleva las entradas y el error esperado; el mock lleva `never()`:

```rust
#[rstest]
#[case::unknown_piece("MUS-999", Status::Available, "MNAC", 30, LendError::UnknownPiece)]
#[case::in_restoration("MUS-001", Status::InRestoration, "MNAC", 30, LendError::NotAvailable)]
// ...
fn rejects(
    #[case] code: &str,
    #[case] status: Status,
    #[case] institution: &str,
    #[case] days: u32,
    #[case] want: LendError,
) {
    let mut notifier = MockNotifier::new();
    notifier.expect_send().never();
    let svc = service(status, notifier);

    assert_eq!(svc.lend(code, institution, days), Err(want));
    assert!(svc.loans.0.borrow().is_empty());
}
```

Incluye el caso que la semana 31 encontró y la 33 arregló en Go: en el museo, una pieza **en restauración** no se presta. Compruébalo: cambia en `lend` la condición `piece.status != Status::Available` por `piece.status == Status::OnLoan`. Solo debe fallar `rejects::case_N_in_restoration`; si no falla ninguno, falta ese caso. Deshaz el cambio.

Referencia: [lab 03](../2-practicas/lab-03-parametrizacion-con-rstest/README.md).

### Paso 7: el snapshot del aviso

1. En `src/lending.rs`, escribe la función que genera el texto que se envía (en el museo, `render_notice(&LoanNotice) -> String`), igual que `RenderNotice` en Go.
2. En `tests/notice.rs`, sustituye `notice_text` por:

   ```rust
   use insta::assert_snapshot;
   use museum::render_notice;

   #[test]
   fn notice_text() {
       assert_snapshot!(render_notice(&common::mnac_notice()));
   }
   ```

3. Ejecuta `cargo test --test notice`, **lee** el `.snap.new` y acéptalo con `cargo insta review`. El texto aprobado queda en `tests/snapshots/notice__notice_text.snap`:

   ```text
   ---
   source: tests/notice.rs
   expression: "render_notice(&common::mnac_notice())"
   ---
   Préstamo de la pieza MUS-001
   Institución: MNAC
   Duración: 30 días
   Seguro: 6000
   ```

   Es el golden file de la semana 33. Sube el `.snap` al repositorio y comprueba que no queda ningún `.snap.new`: `cargo insta pending-snapshots`.

Referencia: [lab 04](../2-practicas/lab-04-snapshots-con-insta-y-tests-common/README.md).

### Paso 8: `dobles.md`

Completa `rust/dobles.md`:

- una fila por dependencia: el trait, el doble, su tipo (fake, stub o mock) y por qué ese tipo;
- por qué el servicio es genérico y qué perdería el test con `Box<dyn Trait>`;
- por qué el mock se declara con `mock!` (o con `#[automock]`, si tus tests del servicio están en `src/`);
- qué regla o riesgo fija cada test importante.

No dejes ningún `TODO`.

### Paso 9: verificar y entregar

```bash
cargo test 2>&1 | grep -E '^test (lends|notifier|rejects)'
# test lends_an_available_piece ... ok
# test notifier_failure_leaves_the_loan_open ... ok
# test rejects::case_1_unknown_piece ... ok
# test rejects::case_2_already_on_loan ... ok
# test rejects::case_3_in_restoration ... ok
# test rejects::case_4_not_accredited ... ok
# test rejects::case_5_zero_days ... ok
CI=true cargo test 2>&1 | grep -c FAILED
# 0
cargo test --no-run 2>&1 | grep -c '^warning'
# 0
cargo fmt --check
```

Los nombres son los del museo; los tuyos dependen de tu dominio. `CI=true` ejecuta los snapshots como en CI: si falta uno, el test falla sin escribir un `.snap.new`. Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 38
```

Haz commit (incluidos `Cargo.lock` y `tests/snapshots/`), sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `cargo test` pasa: unitarios, integración, snapshots y doctests | Paso 9 |
| `cargo test` compila sin warnings | Paso 9 |
| No queda ningún `#[ignore]` en los tests | Pasos 5 a 7 |
| Al menos 3 traits públicos en `src/` para las dependencias del servicio | Paso 3 |
| El servicio es genérico sobre sus dependencias | Paso 3 |
| Hay al menos un fake escrito a mano (`impl Trait for FakeXxx`) | Paso 4 |
| Hay un mock de mockall y los tests lo programan con `expect_` | Paso 5 |
| Al menos 4 casos de rstest (`#[case]`) que pasan | Paso 6 |
| Hay al menos un snapshot aprobado (`.snap`) y ninguno pendiente | Paso 7 |
| `tests/common/mod.rs` existe y lo usan al menos 2 archivos de `tests/` | Paso 4 |
| `dobles.md` justifica cada doble (fake y mock), sin TODO | Paso 8 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Snapshots con insta y tests/common](../2-practicas/lab-04-snapshots-con-insta-y-tests-common/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 38](../README.md)
