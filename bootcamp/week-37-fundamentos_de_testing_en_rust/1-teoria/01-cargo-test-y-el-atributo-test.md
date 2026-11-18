# 01 - `cargo test` y el atributo `#[test]`

> Lenguaje: **Rust**

Rust, como Go, trae el testing de serie: el atributo `#[test]`, las macros `assert!` y el comando `cargo test` vienen con el compilador. Lo nuevo es el compilador: los tipos y el borrow checker eliminan clases enteras de errores antes de ejecutar nada, así que los tests se dedican a lo que el tipo no garantiza. Esta teoría compara cada pieza con Go y con los lenguajes anteriores.

---

## 🎯 Objetivos

- Instalar Rust y crear un paquete con `cargo new --lib`.
- Saber dónde van los tests unitarios: el módulo `#[cfg(test)] mod tests`.
- Escribir tests con `assert!`, `assert_eq!` y `assert_ne!`, con mensajes propios.
- Ejecutar la suite con `cargo test`, filtrar por nombre y pasar opciones al binario de tests después de `--`.

---

## Instalar Rust

Rust se instala con **rustup**, que gestiona las versiones del compilador (`rustc`) y de `cargo`, la herramienta que compila, gestiona dependencias y ejecuta los tests:

```bash
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
```

El script pregunta cómo instalar; la opción por defecto (`1`) basta. Abre una terminal nueva y comprueba:

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
rustc --version
# rustc 1.100.0 (0ef1bc386 2026-11-09)
```

Rust publica una versión estable cada seis semanas y todas son compatibles hacia atrás. Los labs funcionan con cualquier versión desde la 1.85, la primera con la edición 2024. El proyecto fija la versión exacta en `rust-toolchain.toml` ([proyecto](../3-proyecto/README.md#paso-1-el-paquete)).

---

## Paquete, crate y módulo de tests

Un **paquete** es un proyecto de Cargo, definido por `Cargo.toml`: el equivalente de `go.mod`, `package.json` o `pom.xml`. `cargo new --lib` crea uno con una librería:

```bash
cargo new --lib demo
#     Creating library `demo` package
# note: see more `Cargo.toml` keys and their definitions at https://doc.rust-lang.org/cargo/reference/manifest.html
```

```toml
[package]
name = "demo"
version = "0.1.0"
edition = "2024"

[dependencies]
```

| Línea | Qué / Para qué / Impacto |
|---|---|
| `name = "demo"` | El nombre del paquete y de su librería. Los tests de `tests/` y los doctests lo importan con `use demo::...` ([teoría 03](03-unitarios-integracion-y-doctests.md)) |
| `edition = "2024"` | La edición del lenguaje: decide algunas reglas de sintaxis. Es compatible con cualquier compilador desde Rust 1.85 |
| `[dependencies]` | Las dependencias del código. Las que solo usan los tests van en `[dev-dependencies]` ([teoría 02](02-panics-result-y-asserts.md#pretty_assertions)) |

Un **crate** es lo que el compilador compila de una vez: aquí, la librería que empieza en `src/lib.rs`. `cargo new --lib` ya trae un test:

```rust
pub fn add(left: u64, right: u64) -> u64 {
    left + right
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let result = add(2, 2);
        assert_eq!(result, 4);
    }
}
```

| Pieza | Qué hace |
|---|---|
| `#[cfg(test)]` | Compila el módulo solo para `cargo test`. En `cargo build`, el módulo y todo lo que usa no existen |
| `mod tests { ... }` | Un módulo dentro del mismo archivo. `tests` es una convención, no una palabra reservada |
| `use super::*;` | Trae todo lo del módulo padre, también lo privado ([teoría 03](03-unitarios-integracion-y-doctests.md#funciones-privadas)) |
| `#[test]` | Marca la función como test. Sin él, es una función normal que nadie llama |

| | Jest | pytest | JUnit + Maven | Go | Rust |
|---|---|---|---|---|---|
| Dónde van los unitarios | `*.test.js` | `test_*.py` | `src/test/java` | `*_test.go`, junto al código | `mod tests` en el mismo archivo |
| Qué funciones son tests | `test(...)` | `def test_*` | `@Test` | `func TestXxx(t *testing.T)` | `#[test] fn cualquier_nombre()` |
| Aserciones | `expect` | `assert` | AssertJ | `if` y `t.Errorf` | `assert!`, `assert_eq!`, `assert_ne!` |
| Comando | `pnpm test` | `uv run pytest` | `mvn test` | `go test ./...` | `cargo test` |

---

## Aserciones

Las tres son **macros**: el `!` las distingue de una función. Si la comprobación falla, entran en **pánico**: el test se para en esa línea y falla, como un `t.Fatalf` de Go.

| Macro | Comprueba | Al fallar imprime |
|---|---|---|
| `assert!(cond)` | `cond` es `true` | `assertion failed: late_fee(3) == 151`: el código, sin los valores |
| `assert_eq!(a, b)` | `a == b` | `left: 150` y `right: 151` |
| `assert_ne!(a, b)` | `a != b` | Los dos valores |

Las tres aceptan un mensaje propio después de los argumentos, con el formato de `println!`:

```rust
assert_eq!(late_fee(30), MAX_FEE, "30 days late should hit the cap");
assert!(fee < MAX_FEE, "below the cap, got {fee}");
```

```text
assertion `left == right` failed: 30 days late should hit the cap
  left: 1000
 right: 999
```

Por convención, `left` es el valor obtenido y `right` el esperado, como el `got, want` de Go. Para comparar valores, usa `assert_eq!`: `assert!(a == b)` pierde los dos valores ([lab 01](../2-practicas/lab-01-primer-test-con-cargo-test/README.md#experimento-1-assert-o-assert_eq)).

`assert_eq!` compara cualquier tipo que implemente `PartialEq` (para comparar) y `Debug` (para imprimirlo): números, cadenas, vectores, y tus estructuras si llevan `#[derive(Debug, PartialEq)]`.

---

## Ejecutar la suite

```bash
cargo test
#    Compiling demo v0.1.0 (.../demo)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.11s
#      Running unittests src/lib.rs (target/debug/build/demo/9fbc808110816e76/out/demo-9fbc808110816e76)
#
# running 1 test
# test tests::it_works ... ok
#
# test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
#
#    Doc-tests demo
#
# running 0 tests
#
# test result: ok. 0 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
```

`cargo test` compila un **binario de tests** por cada crate de tests y los ejecuta uno detrás de otro: aquí, el de `src/lib.rs` y el de los ejemplos de la documentación (`Doc-tests`). El nombre de cada test es su ruta de módulos: `tests::it_works`. Si un binario falla, `cargo test` no ejecuta los siguientes.

| | Qué / Para qué / Impacto |
|---|---|
| Perfil `test` | Compila sin optimizar y con comprobaciones: un desbordamiento de entero entra en pánico. Con `--release` se compila optimizado y el número da la vuelta sin avisar ([lab 01](../2-practicas/lab-01-primer-test-con-cargo-test/README.md#experimento-3-el-desbordamiento-en---release)) |
| Sin caché de resultados | A diferencia de `go test`, `cargo test` siempre ejecuta los tests. Lo que reutiliza es la compilación: si nada cambió, `Finished` sale en 0.00 s |
| En paralelo | Los tests de cada binario se ejecutan en varios hilos y el orden de las líneas cambia. En Go había que pedirlo con `t.Parallel()`; en Rust viene activado ([teoría 02](02-panics-result-y-asserts.md#tests-en-paralelo)) |

### Opciones de `cargo` y opciones del binario

La línea de `cargo test` tiene dos partes separadas por `--`:

```bash
cargo test [filtro] [opciones de cargo] -- [opciones del binario de tests]
```

| Comando | Qué hace |
|---|---|
| `cargo test capped` | Ejecuta los tests cuya ruta **contiene** `capped` |
| `cargo test -- --exact tests::late_fee_is_capped` | Solo el test con esa ruta exacta |
| `cargo test -- --list` | Lista los tests sin ejecutarlos |
| `cargo test -q` | Una línea por binario, con un `.` por test que pasa |
| `cargo test --release` | Compila optimizado |
| `cargo test --lib`, `--test nombre`, `--doc` | Solo un tipo de test ([teoría 03](03-unitarios-integracion-y-doctests.md)) |

El filtro no es una expresión regular como `-run` en Go: es un texto que tiene que aparecer en la ruta. `cargo test fee` ejecuta todos los tests que tengan `fee` en el nombre o en su módulo. La teoría 02 añade las opciones de salida, hilos e `#[ignore]`.

---

## ✅ Resumen

- Rust se instala con `rustup`; `cargo test` compila y ejecuta los tests, sin librerías externas.
- Los unitarios van en `#[cfg(test)] mod tests` dentro del mismo archivo, con `use super::*;`.
- `assert_eq!` imprime `left` y `right`; `assert!` solo el código. Las tres macros aceptan un mensaje.
- `cargo test` siempre ejecuta, en paralelo y en modo debug, donde un desbordamiento entra en pánico.
- Lo que va antes de `--` es para `cargo` (filtro, `--release`, `--lib`); lo que va después, para el binario de tests (`--exact`, `--list`).

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 37 - Fundamentos de Testing en Rust](../README.md) | [Teoría — 02 - Pánicos, Result y control de la ejecución](02-panics-result-y-asserts.md) → |

[↑ Semana 37](../README.md)
