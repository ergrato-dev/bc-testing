# 02 - Mocks con mockall

> Lenguaje: **Rust**

Un mock lleva las expectativas dentro: qué llamadas espera, con qué argumentos y cuántas veces. En Go lo generaba mockgen y se programaba con `EXPECT()` ([semana 33](../../week-33-interfaces_y_dobles_de_prueba_en_go/1-teoria/02-mockgen-y-testify.md)). En Rust, el crate **mockall** lo genera con una macro durante la compilación: no hay archivo generado ni paso `go generate`.

---

## 🎯 Objetivos

- Generar un mock de un trait con `#[automock]` y saber por qué va dentro de `cfg_attr(test, ...)`.
- Programar expectativas con `expect_*()`, `with`, `withf`, `times`, `never`, `returning` y `return_const`.
- Leer los dos fallos de un mock: la llamada sin expectativa y la expectativa sin cumplir.
- Declarar un mock con `mock!` cuando el test está en `tests/`.

---

## Instalar mockall

mockall solo lo usan los tests, así que va en `[dev-dependencies]`. `cargo add` lo añade a `Cargo.toml` por ti:

```bash
cargo add --dev mockall@=0.15.0
#     Updating crates.io index
#       Adding mockall =0.15.0 to dev-dependencies
# ...
```

| Parte | Qué / Para qué / Impacto |
|---|---|
| `--dev` | Escribe la dependencia en `[dev-dependencies]`: no se compila en el programa final |
| `@=0.15.0` | Fija la versión exacta, como en [stack-versions](../../../docs/stack-versions.md). Sin `=`, Cargo aceptaría cualquier `0.15.x` |

---

## `#[automock]`

```rust
#[cfg_attr(test, mockall::automock)]
pub trait Notifier {
    fn send(&self, notice: &Notice) -> Result<(), String>;
}
```

`automock` genera un `struct MockNotifier` que implementa `Notifier`. El `cfg_attr(test, ...)` lo aplica solo cuando se compila para `cargo test`: el mock no entra en el programa final y mockall puede quedarse en `[dev-dependencies]`.

Por cada método del trait, el mock tiene un `expect_<método>()` que devuelve una expectativa para configurar:

```rust
use mockall::predicate;

let mut notifier = MockNotifier::new();
notifier
    .expect_send()
    .with(predicate::eq(notice.clone()))
    .times(1)
    .returning(|_| Ok(()));
```

| Método | Qué fija | Equivalente en gomock (S33) |
|---|---|---|
| `with(predicate::eq(v))` | Los argumentos esperados, uno por parámetro. Con un parámetro `&Notice`, el predicado compara el `Notice` al que apunta | `EXPECT().Send(notice)` |
| `withf(\|n: &Notice\| n.days == 90)` | Una condición propia sobre los argumentos | `gomock.Cond(...)` |
| `times(1)` / `never()` | Cuántas veces debe llamarse. Sin `times`, cualquier número, también 0 | `.Times(1)` / `.Times(0)` |
| `returning(\|_\| Ok(()))` | Qué devuelve, calculado con un closure que recibe los argumentos | `.DoAndReturn(...)` |
| `return_const(true)` | Qué devuelve, siempre el mismo valor | `.Return(true)` |

El mock se le pasa al servicio como cualquier otro tipo que implemente el trait: `LoanDesk { registry, notifier }`. Las expectativas se configuran **antes** de entregarlo, porque `expect_send()` necesita `&mut` y, una vez dentro del servicio, el test ya no lo tiene.

---

## Los dos fallos de un mock

**1. Una llamada que ninguna expectativa acepta.** El mock entra en pánico en el momento de la llamada:

```text
MockNotifier::send(Notice { institution: "MNAC", code: "MUS-001", days: 30 }): No matching expectation found
```

Pasa si no hay ningún `expect_send()`, o si hay uno pero `with` no coincide. El mensaje muestra los argumentos recibidos, no los esperados: compáralos tú con el `with` del test. La ubicación del pánico es la línea del `#[cfg_attr(test, mockall::automock)]`, no la del test; el nombre del test está en la cabecera `---- tests::... stdout ----`.

**2. Una expectativa sin cumplir.** El mock comprueba `times` cuando se destruye, al final del test:

```text
MockRegistry::is_accredited: Expectation(var == "MNAC") called 1 time(s) which is fewer than expected 2
```

En gomock, esta comprobación la hacía el `ctrl.Finish()` que registra `gomock.NewController(t)`. En mockall no hace falta nada: Rust destruye el mock al salir de su ámbito, y la destrucción hace la comprobación.

`never()` fija lo contrario: si el servicio llama, el test falla en ese momento. Es la forma de comprobar que un préstamo rechazado no envía ningún aviso.

---

## Los mocks y `tests/`

`#[cfg_attr(test, automock)]` genera el mock solo cuando la librería se compila para sus **propios** tests unitarios. Los tests de `tests/` compilan la librería como cualquier otro programa, sin `cfg(test)`, y el mock no existe:

```text
error[E0432]: unresolved imports `lab02::MockNotifier`, `lab02::MockRegistry`
 --> tests/desk.rs:1:23
  |
1 | use lab02::{LoanDesk, MockNotifier, MockRegistry};
  |                       ^^^^^^^^^^^^  ^^^^^^^^^^^^ no `MockRegistry` in the root
  |                       |
  |                       no `MockNotifier` in the root
```

Para esos tests, la macro `mock!` declara el mock en el propio archivo de test, copiando la firma del trait:

```rust
use lab02::{Notice, Notifier};
use mockall::mock;

mock! {
    pub Notifier {}
    impl Notifier for Notifier {
        fn send(&self, notice: &Notice) -> Result<(), String>;
    }
}
```

`pub Notifier {}` genera `MockNotifier`: mockall añade el prefijo `Mock`. Si el trait cambia, el `impl` de dentro deja de compilar y te obliga a actualizarlo.

| Dónde está el test | Cómo se declara el mock |
|---|---|
| `mod tests`, en `src/` | `#[cfg_attr(test, mockall::automock)]` sobre el trait |
| `tests/` | `mock! { ... }` en el archivo de test o en `tests/common/` ([teoría 03](03-rstest-insta-y-tests-common.md#código-compartido-en-testscommon)) |

---

## ¿Mock o fake?

La regla de la [semana 33](../../week-33-interfaces_y_dobles_de_prueba_en_go/1-teoria/01-interfaces-y-dobles-a-mano.md#los-dobles-escritos-a-mano) sigue igual. Un mock fija la interacción: si el servicio cambia cómo llama (dos `save` en lugar de uno), el test falla aunque el resultado sea el mismo. Úsalo para lo que de verdad es una interacción, como enviar un aviso; para un almacén, un fake que guarda en memoria deja al servicio cambiar por dentro sin romper los tests.

---

## ✅ Resumen

- `#[cfg_attr(test, mockall::automock)]` genera `MockTrait` solo para los tests unitarios; `mock!` lo declara para `tests/`.
- Las expectativas se configuran antes de entregar el mock: `expect_*()`, `with`/`withf`, `times`/`never`, `returning`/`return_const`.
- Una llamada inesperada falla en el momento; un `times` sin cumplir falla al destruirse el mock, al final del test.
- Mock para interacciones (avisos, eventos); fake para almacenes.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Traits y fakes a mano](01-traits-y-fakes-a-mano.md) | [Teoría — 03 - rstest, insta y `tests/common`](03-rstest-insta-y-tests-common.md) → |

[↑ Semana 38](../README.md)
