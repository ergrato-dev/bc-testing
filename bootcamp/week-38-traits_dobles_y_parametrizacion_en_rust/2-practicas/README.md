# Prácticas - Semana 38

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un paquete de Rust y los tests comentados por PASOS; lo que no es descomentar, como cambiar una línea o crear un archivo, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Traits y fakes a mano](lab-01-traits-y-fakes-a-mano/README.md) | Un servicio genérico sobre dos traits, un stub, un fake con `RefCell`, un doble que falla, `Box<dyn Trait>` para mezclar tipos y los errores `E0596` y `E0308` | 60 min |
| [Lab 02 - Mocks con mockall](lab-02-mocks-con-mockall/README.md) | `#[automock]`, `expect_*()`, `with`, `withf`, `times`, `never`, `returning`, `return_const`, los dos fallos de un mock y `mock!` para `tests/` | 65 min |
| [Lab 03 - Parametrización con rstest](lab-03-parametrizacion-con-rstest/README.md) | `#[case]` con y sin nombre, `#[values]`, fixtures con `#[default]` y `#[with]`, dos casos que fallan por separado y una fila incompleta que no compila | 60 min |
| [Lab 04 - Snapshots con insta y tests/common](lab-04-snapshots-con-insta-y-tests-common/README.md) | `assert_snapshot!`, `cargo insta review`, `accept` y `reject`, snapshots inline y de `Debug`, `tests/common/mod.rs`, el warning de `dead_code` y los snapshots en CI | 55 min |

## Antes de empezar

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
```

Si no tienes Rust, instálalo con `rustup` ([semana 37](../../week-37-fundamentos_de_testing_en_rust/1-teoria/01-cargo-test-y-el-atributo-test.md#instalar-rust)). Cada lab es un paquete independiente:

```bash
cd lab-NN-.../starter
cargo test
```

Los labs 02, 03 y 04 tienen una dependencia cada uno (mockall, rstest e insta), que `cargo` descarga la primera vez; las versiones exactas están en el `Cargo.lock` de cada starter. El lab 04 necesita además la herramienta `cargo-insta`, que se instala en su preparación. `cargo` compila en la carpeta `target/` de cada lab; puedes borrarla con `cargo clean` cuando termines.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - rstest, insta y `tests/common`](../1-teoria/03-rstest-insta-y-tests-common.md) | [Lab 01 - Traits y fakes a mano](lab-01-traits-y-fakes-a-mano/README.md) → |

[↑ Semana 38](../README.md)
