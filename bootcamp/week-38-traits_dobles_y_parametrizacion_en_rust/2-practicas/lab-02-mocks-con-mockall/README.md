# Lab 02 - Mocks con mockall

## Objetivo

Sustituir los dobles a mano del [lab 01](../lab-01-traits-y-fakes-a-mano/README.md) por mocks generados con mockall: programar expectativas sobre argumentos y número de llamadas, leer los dos fallos de un mock y declarar un mock con `mock!` para un test de `tests/`.

## Tiempo estimado

65 minutos.

## Preparación

```bash
cd starter
cargo test
#     Updating crates.io index
#  Downloading crates ...
#   Downloaded fragile v2.1.0
#   Downloaded mockall v0.15.0
#   ...
#    Compiling mockall_derive v0.15.0
#    Compiling mockall v0.15.0
#    Compiling lab02 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 3.75s
# ...
# running 0 tests
# ...
```

La primera vez, `cargo` descarga mockall y sus dependencias. Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab02` con `mockall = "=0.15.0"` en `[dev-dependencies]` |
| `Cargo.lock` | Las versiones exactas de mockall y de sus dependencias |
| `src/lib.rs` | El mostrador de préstamos del lab 01, con `#[cfg_attr(test, mockall::automock)]` encima de cada trait. Al final, los tests comentados por PASOS |
| `tests/desk.rs` | Un test de integración, comentado (PASO 5) |

Lee la línea que hay encima de cada trait:

```rust
#[cfg_attr(test, mockall::automock)]
pub trait Notifier {
```

Para `cargo test`, `automock` genera `MockNotifier` y `MockRegistry`: dos `struct` que implementan cada trait y se programan en el test ([teoría 02](../../1-teoria/02-mocks-con-mockall.md#automock)). En el programa final no se genera nada.

## Paso a paso

### Paso 1: el mock que no debe recibir llamadas (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test --lib
   # ...
   # running 1 test
   # test tests::rejected_loan_sends_nothing ... ok
   # ...
   ```

   `--lib` ejecuta solo los tests de `src/` ([semana 37](../../../week-37-fundamentos_de_testing_en_rust/2-practicas/lab-04-integracion-y-doctests/README.md)).

El test usa los dos mocks:

| Línea | Qué hace |
|---|---|
| `let mut registry = MockRegistry::new();` | Un mock sin expectativas. `mut` porque configurarlo lo modifica |
| `registry.expect_is_accredited().return_const(false);` | Cualquier llamada a `is_accredited` devuelve `false` |
| `notifier.expect_send().never();` | Si alguien llama a `send`, el test falla en ese momento |

Con `never()`, el mock comprueba la interacción por ti: en el lab 01 necesitabas un fake y un `assert!` sobre la lista vacía.

### Paso 2: argumentos y número de llamadas (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test --lib`: pasan 2.

```rust
registry
    .expect_is_accredited()
    .with(predicate::eq("MNAC"))
    .times(1)
    .return_const(true);
```

- `with(predicate::eq("MNAC"))`: la expectativa solo acepta llamadas con ese argumento. `predicate` viene de `use mockall::predicate;`.
- `times(1)`: debe llamarse exactamente una vez. Se comprueba al final del test.
- `returning(|_| Ok(()))`, en el notificador: el valor se calcula con un closure que recibe los argumentos. `|_|` ignora el aviso.

El parámetro de `send` es `&Notice`, pero el predicado compara con un `Notice`: mockall compara el valor al que apunta la referencia. Por eso `Notice` tiene `#[derive(Debug, Clone, PartialEq)]`: `PartialEq` para comparar, `Debug` para imprimirlo si falla y `Clone` para usar `notice` en el `with` y en el `assert_eq!`.

### Paso 3: un mock que devuelve un error (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test --lib`: pasan 3.

El mock sustituye al `FailingNotifier` del lab 01 con una línea: `.returning(|_| Err("smtp down".to_string()))`. Sin `times`, la expectativa acepta cualquier número de llamadas, también ninguna.

### Paso 4: una condición propia (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   cargo test --lib
   # ...
   # running 4 tests
   # test tests::approved_loan_sends_the_notice ... ok
   # test tests::notice_carries_the_days ... ok
   # test tests::notifier_failure_is_reported ... ok
   # test tests::rejected_loan_sends_nothing ... ok
   #
   # test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

`withf(|n: &Notice| n.days == 90)` acepta la llamada si el closure devuelve `true`. Sirve cuando solo importa un campo: con `with(predicate::eq(...))` tendrías que escribir el aviso entero.

### Paso 5: un mock para `tests/` (PASO 5)

1. Antes de descomentar nada, añade esta línea al principio de `tests/desk.rs`:

   ```rust
   use lab02::{LoanDesk, MockNotifier, MockRegistry};
   ```

2. Ejecuta:

   ```bash
   cargo test --test desk
   # error[E0432]: unresolved imports `lab02::MockNotifier`, `lab02::MockRegistry`
   #  --> tests/desk.rs:1:23
   #   |
   # 1 | use lab02::{LoanDesk, MockNotifier, MockRegistry};
   #   |                       ^^^^^^^^^^^^  ^^^^^^^^^^^^ no `MockRegistry` in the root
   #   |                       |
   #   |                       no `MockNotifier` in the root
   ```

   Los tests de `tests/` compilan la librería sin `cfg(test)`, y para ellos `automock` no generó nada.

3. Borra esa línea y descomenta el bloque `// PASO 5`.
4. Ejecuta:

   ```bash
   cargo test --test desk
   # ...
   #      Running tests/desk.rs (target/debug/build/lab02/c7cb6b582630bc4c/out/desk-c7cb6b582630bc4c)
   #
   # running 1 test
   # test unaccredited_institution_is_rejected ... ok
   # ...
   ```

La macro `mock!` declara el mock en el propio archivo: `pub Registry {}` genera `MockRegistry`, y el `impl` de dentro copia la firma del trait. Si el trait cambia, ese `impl` deja de compilar. Si varios archivos de `tests/` necesitan el mismo mock, va en `tests/common/mod.rs` ([lab 04](../lab-04-snapshots-con-insta-y-tests-common/README.md)).

## Experimentos

### Experimento 1: una llamada sin expectativa

1. En `approved_loan_sends_the_notice`, sustituye las seis líneas del notificador (desde `let mut notifier` hasta `.returning(|_| Ok(()));`) por:

   ```rust
   let notifier = MockNotifier::new();
   ```

2. Ejecuta:

   ```bash
   cargo test --lib approved
   # ---- tests::approved_loan_sends_the_notice stdout ----
   #
   # thread 'tests::approved_loan_sends_the_notice' (229534) panicked at src/lib.rs:27:18:
   # MockNotifier::send(Notice { institution: "MNAC", code: "MUS-001", days: 30 }): No matching expectation found
   ```

   El mock entra en pánico en la llamada. La ubicación, `src/lib.rs:27`, es la línea del `#[cfg_attr(test, mockall::automock)]` de `Notifier`, no la del test: el nombre del test está en la cabecera.

3. Deshaz el cambio.

### Experimento 2: una expectativa sin cumplir

1. En el mismo test, cambia el `.times(1)` del registro por `.times(2)`.
2. Ejecuta:

   ```bash
   cargo test --lib approved
   # thread 'tests::approved_loan_sends_the_notice' (229549) panicked at src/lib.rs:21:18:
   # MockRegistry::is_accredited: Expectation(var == "MNAC") called 1 time(s) which is fewer than expected 2
   ```

   Esta comprobación no ocurre en ninguna llamada, sino cuando el mock se destruye al final del test. No hace falta ningún `verify` ni `Finish`.

3. Deshaz el cambio.

### Experimento 3: un argumento distinto

1. En el mismo test, cambia el `30` de `desk.approve("MNAC", "MUS-001", 30)` por `31`.
2. Ejecuta:

   ```bash
   cargo test --lib approved
   # thread 'tests::approved_loan_sends_the_notice' (229564) panicked at src/lib.rs:27:18:
   # MockNotifier::send(Notice { institution: "MNAC", code: "MUS-001", days: 31 }): No matching expectation found
   ```

   Es el mismo mensaje que el del Experimento 1: hay una expectativa, pero su `with` no acepta el aviso. El mensaje muestra lo que llegó (`days: 31`), no lo que se esperaba; compáralo tú con el `with` del test.

3. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
# test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Por qué `automock` va dentro de `cfg_attr(test, ...)`? ¿Qué consecuencia tiene para los tests de `tests/`?
2. ¿Qué diferencia hay entre `return_const` y `returning`? ¿Y entre `with` y `withf`?
3. ¿En qué momento falla un mock con una llamada inesperada? ¿Y uno con un `times` sin cumplir?
4. Compara el test del Paso 1 con `rejected_loan_sends_nothing` del lab 01: ¿qué comprueba cada uno y cuál es más fácil de leer?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Traits y fakes a mano](../lab-01-traits-y-fakes-a-mano/README.md) | [Lab 03 - Parametrización con rstest](../lab-03-parametrizacion-con-rstest/README.md) → |

[↑ Semana 38](../../README.md)
