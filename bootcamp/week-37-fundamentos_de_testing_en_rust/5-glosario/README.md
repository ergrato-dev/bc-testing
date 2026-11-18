# Glosario Semana 37 - Fundamentos de Testing en Rust

## A

- **`assert!`**: macro que entra en pánico si la condición es `false`. Al fallar imprime el código de la condición, no los valores.
- **`assert_eq!` / `assert_ne!`**: macros que comparan dos valores e imprimen `left` (obtenido) y `right` (esperado) si la comprobación falla. Necesitan `PartialEq` y `Debug`.

## B

- **Binario de tests**: el ejecutable que `cargo test` compila por cada crate de tests (`src/lib.rs`, cada archivo de `tests/`, los doctests). Las opciones después de `--` son para él.

## C

- **Cargo**: la herramienta de Rust que compila, descarga dependencias y ejecuta los tests. Se configura en `Cargo.toml`.
- **`Cargo.lock`**: las versiones exactas de todas las dependencias, también las indirectas. Se sube al repositorio.
- **`#[cfg(test)]`**: atributo que compila lo que marca solo para `cargo test`.
- **Crate**: lo que el compilador compila de una vez: una librería (`src/lib.rs`) o un binario. Cada archivo de `tests/` es un crate aparte.

## D

- **`[dev-dependencies]`**: dependencias que solo se compilan para los tests y los ejemplos, como `pretty_assertions`.
- **Doctest**: un bloque de código en un comentario `///`. `cargo test` lo compila como un programa aparte y lo ejecuta.

## E

- **Edición** (*edition*): la versión de las reglas del lenguaje de un paquete (`edition = "2024"`). Distinta de la versión del compilador.

## I

- **`#[ignore]`**: atributo que compila un test pero no lo ejecuta salvo con `--ignored` o `--include-ignored`. Admite un motivo: `#[ignore = "lento"]`.
- **Test de integración**: en Rust, un archivo de `tests/` que usa el crate desde fuera y solo ve lo `pub`.

## L

- **Línea oculta**: una línea de un doctest que empieza por `# `. Se compila pero no aparece en la documentación.

## M

- **Macro**: código que genera código; su nombre termina en `!`, como `assert_eq!` o `println!`.
- **`matches!`**: macro que devuelve `true` si un valor encaja en un patrón: `matches!(r, Err(LoanError::ZeroDays))`.
- **`mod tests`**: el módulo de tests unitarios, al final del archivo que prueba. Con `use super::*;` ve todo lo del módulo padre, también lo privado.

## P

- **Pánico** (*panic*): la parada de un hilo por un error del programa: un `assert!` que falla, una división entre 0, un `unwrap` sobre `Err`. Un test que entra en pánico falla.
- **`pretty_assertions`**: crate que sustituye `assert_eq!` por una versión que imprime un diff entre los dos valores.
- **`pub`**: hace un elemento visible fuera de su módulo. Sin `pub`, es privado.

## R

- **`Result<T, E>`**: el tipo de un resultado que puede fallar: `Ok(valor)` o `Err(error)`. Un test puede devolver `Result<(), E>` para usar `?`.
- **`rust-toolchain.toml`**: archivo que fija la versión de Rust de una carpeta; `rustup` la instala si falta.
- **rustup**: el instalador y gestor de versiones de Rust.

## S

- **`saturating_mul`**: multiplicación que se queda en el valor máximo del tipo en lugar de desbordar.
- **`#[should_panic(expected = "...")]`**: atributo que hace pasar el test solo si entra en pánico con un mensaje que contiene ese texto.

## U

- **`u32`**: entero sin signo de 32 bits, de 0 a 4 294 967 295. Un valor negativo no compila; un resultado mayor que el máximo desborda.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 38 - Traits, Dobles y Parametrización en Rust](../../week-38-traits_dobles_y_parametrizacion_en_rust/README.md) → |

[↑ Semana 37](../README.md)
