# Lab 04 - Integración y doctests

## Objetivo

Escribir los tres tipos de test de Rust sobre el mismo crate: un unitario de una función privada, tests de integración en `tests/` que solo ven la API pública, y doctests que comprueban los ejemplos de la documentación. Ejecutar cada tipo por separado y ver qué encuentra cada uno.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling lab04 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.20s
#      Running unittests src/lib.rs (target/debug/build/lab04/1bcad83dfd3dffe6/out/lab04-1bcad83dfd3dffe6)
#
# running 0 tests
# ...
#      Running tests/catalog.rs (target/debug/build/lab04/f2a40d6abd824078/out/catalog-f2a40d6abd824078)
#
# running 0 tests
# ...
#    Doc-tests lab04
#
# running 0 tests
# ...
```

Tres bloques, uno por tipo de test ([teoría 03](../../1-teoria/03-unitarios-integracion-y-doctests.md)). Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab04`, sin dependencias. El nombre del paquete es el nombre con el que se importa: `use lab04::...` |
| `src/lib.rs` | `normalize_code(raw)` devuelve `Some("MUS-001")` o `None`; `parse_entry(line)` lee `código;nombre`; `has_code_shape(code)` es privada (sin `pub`). Los ejemplos de la documentación y el módulo `tests` están comentados por PASOS |
| `tests/catalog.rs` | Los tests de integración, comentados |

## Paso a paso

### Paso 1: un test de una función privada (PASO 1)

1. En `src/lib.rs`, descomenta el bloque `// PASO 1` del módulo `tests` (borra el `// ` del principio de cada línea; en VS Code, `Ctrl+/`).
2. Ejecuta solo los unitarios con `--lib`:

   ```bash
   cargo test --lib
   #      Running unittests src/lib.rs (target/debug/build/lab04/1bcad83dfd3dffe6/out/lab04-1bcad83dfd3dffe6)
   #
   # running 1 test
   # test tests::shape_is_three_letters_dash_three_digits ... ok
   # ...
   ```

`has_code_shape` no es `pub`, y el test la llama sin problema. En Rust, un módulo hijo ve todo lo de sus padres, también lo privado, y `tests` es hijo del módulo del crate. En Java o Go harías la función visible en el paquete para probarla; aquí no hace falta tocarla.

Que se pueda no significa que convenga. Un test de una función privada se rompe cuando cambias cómo está hecho el código aunque el comportamiento siga igual. Pruébala directamente cuando tenga casos propios que la API pública no alcanza con facilidad, como aquí las cuatro formas de un código mal formado.

### Paso 2: tests de integración (PASO 2)

1. Descomenta el bloque `// PASO 2` de `tests/catalog.rs`.
2. Ejecuta solo ese archivo con `--test` y su nombre sin `.rs`:

   ```bash
   cargo test --test catalog
   #      Running tests/catalog.rs (target/debug/build/lab04/f2a40d6abd824078/out/catalog-f2a40d6abd824078)
   #
   # running 2 tests
   # test normalizes_spaces_and_case ... ok
   # test rejects_an_entry_with_a_bad_code ... ok
   # ...
   ```

Lee las diferencias con el módulo `tests`:

| | `mod tests` en `src/lib.rs` | `tests/catalog.rs` |
|---|---|---|
| Cómo se compila | Dentro del crate, con `#[cfg(test)]` | Como un crate aparte, que depende de `lab04` |
| Qué ve | Todo, también lo privado | Solo lo `pub` |
| Cómo importa | `use super::*;` | `use lab04::{...};` |
| Nombre del test | `tests::shape_is_...` | `normalizes_spaces_and_case`, sin módulo |
| Lleva `#[cfg(test)]` | Sí | No: `cargo` solo compila `tests/` para los tests |

Cada archivo de `tests/` es un binario de tests distinto: tres archivos son tres bloques `Running tests/...` en la salida.

### Paso 3: un doctest (PASO 3)

1. En `src/lib.rs`, encima de `normalize_code`, descomenta el bloque `// PASO 3`. Son líneas de documentación (`///`) con un bloque de código entre ` ``` `.
2. Ejecuta solo los doctests con `--doc`:

   ```bash
   cargo test --doc
   #    Doc-tests lab04
   #
   # running 1 test
   # test src/lib.rs - normalize_code (line 10) ... ok
   # ...
   ```

`cargo test` extrae cada bloque de código de la documentación, lo compila como un programa aparte que usa `lab04` desde fuera, y lo ejecuta. `(line 10)` es la línea del ` ``` ` que abre el bloque. Si el ejemplo deja de compilar o un `assert_eq!` falla, el doctest falla: la documentación no puede quedarse desfasada sin que la suite lo diga. Es el `ExampleXxx` con `// Output:` de Go ([semana 32](../../../week-32-fundamentos_de_testing_en_go/2-practicas/lab-04-caja-negra-y-examples/README.md)), pero comprobado con asserts en lugar de comparar lo que se imprime.

3. Genera la documentación y ábrela en el navegador:

   ```bash
   cargo doc --no-deps --open
   ```

   El ejemplo aparece en la página de `normalize_code` tal como lo escribiste.

### Paso 4: un doctest con `?` (PASO 4)

1. Descomenta el bloque `// PASO 4`, encima de `parse_entry`.
2. Ejecuta:

   ```bash
   cargo test --doc
   # running 2 tests
   # test src/lib.rs - normalize_code (line 10) ... ok
   # test src/lib.rs - parse_entry (line 25) ... ok
   ```

El ejemplo usa `?` como lo usaría alguien en su código. Para que compile, el programa que genera `cargo test` tiene que devolver un `Result`. Eso lo hace la última línea:

```rust
/// # Ok::<(), InvalidCode>(())
```

Una línea de un doctest que empieza por `# ` se compila pero no aparece en la documentación. `Ok::<(), InvalidCode>(())` es el valor final del ejemplo, con el tipo de error escrito para que el compilador lo sepa. El [Experimento 3](#experimento-3-un-doctest-sin-la-línea-oculta) muestra qué pasa sin ella.

### Paso 5: los tres tipos juntos

```bash
cargo test
#      Running unittests src/lib.rs (...)
# test result: ok. 1 passed; ...
#      Running tests/catalog.rs (...)
# test result: ok. 2 passed; ...
#    Doc-tests lab04
# test result: ok. 2 passed; ...
```

`cargo test` sin opciones ejecuta los tres, en ese orden. Si los unitarios fallan, se para ahí y no ejecuta los siguientes.

## Experimentos

### Experimento 1: lo privado desde `tests/`

Añade al final de `tests/catalog.rs`:

```rust
#[test]
fn private_shape() {
    assert!(lab04::has_code_shape("MUS-001"));
}
```

```bash
cargo test --test catalog
# error[E0603]: function `has_code_shape` is private
#   --> tests/catalog.rs:18:20
#    |
# 18 |     assert!(lab04::has_code_shape("MUS-001"));
#    |                    ^^^^^^^^^^^^^^ private function
```

No es un fallo del test: el archivo no compila. Los tests de integración usan el crate como lo usaría otro programa. Borra el test.

### Experimento 2: la documentación desfasada

En `parse_entry`, cambia `name.trim().to_string()` por `name.to_string()`: el nombre ya no pierde los espacios.

```bash
cargo test
# ...
# test result: ok. 1 passed; ...
# ...
# test result: ok. 2 passed; ...
#    Doc-tests lab04
# ...
# test src/lib.rs - parse_entry (line 25) ... FAILED
#
# failures:
#
# ---- src/lib.rs - parse_entry (line 25) stdout ----
# Test executable failed (exit status: 101).
#
# stderr:
#
# thread 'main' (118688) panicked at /tmp/rustdoctestZLlmfj/doctest_bundle_2024.rs:21:1:
# assertion `left == right` failed
#   left: " Vasija ceremonial"
#  right: "Vasija ceremonial"
```

Los unitarios y los de integración siguen en verde: ninguno comprobaba el nombre. Solo el ejemplo de la documentación lo hacía. La ruta del pánico es un archivo temporal que genera `cargo test`; para encontrar el ejemplo, usa el nombre y la línea del doctest: `src/lib.rs - parse_entry (line 25)`. Deshaz el cambio.

### Experimento 3: un doctest sin la línea oculta

Borra la línea `/// # Ok::<(), InvalidCode>(())`:

```bash
cargo test --doc
# ---- src/lib.rs - parse_entry (line 25) stdout ----
# error[E0277]: the `?` operator can only be used in a function that returns `Result` or `Option` (or another type that implements `FromResidual`)
#   --> src/lib.rs:29:61
# ...
# Couldn't compile the test.
```

Sin esa línea, el programa del ejemplo devuelve `()` y el `?` no compila. Vuelve a ponerla.

## Comprobación final

```bash
cargo test 2>&1 | grep -E '^test '
# test tests::shape_is_three_letters_dash_three_digits ... ok
# test normalizes_spaces_and_case ... ok
# test rejects_an_entry_with_a_bad_code ... ok
# test src/lib.rs - normalize_code (line 10) ... ok
# test src/lib.rs - parse_entry (line 25) ... ok
```

`cargo test` escribe las líneas `Running` en la salida de error y los resultados en la normal; `2>&1` junta las dos para que `grep` las vea todas.

## Preguntas de cierre

1. ¿Por qué un test del módulo `tests` puede llamar a una función privada y uno de `tests/` no?
2. ¿Qué encontró el doctest del Experimento 2 que no encontraban los otros tres tests?
3. ¿Para qué sirve una línea `# ` en un doctest?
4. ¿Con qué opción ejecutas solo los unitarios? ¿Y solo los doctests?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Ignore, salida e hilos](../lab-03-ignore-salida-e-hilos/README.md) | [Proyecto — Proyecto Semanal - Las reglas del dominio en Rust](../../3-proyecto/README.md) → |

[↑ Semana 37](../../README.md)
