# Lab 01 - Primer test con cargo test

## Objetivo

Escribir los primeros tests de Rust para una función de multas, ejecutarlos con `cargo test` y sus filtros, y provocar los fallos que más vas a ver: un `assert!` sin valores frente a un `assert_eq!`, una función sin `#[test]` que nadie ejecuta y un desbordamiento que solo aparece en modo debug.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
cd starter
cargo test
#    Compiling lab01 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.14s
#      Running unittests src/lib.rs (target/debug/build/lab01/5ac2d040a49e6178/out/lab01-5ac2d040a49e6178)
#
# running 0 tests
#
# test result: ok. 0 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
#
#    Doc-tests lab01
#
# running 0 tests
#
# test result: ok. 0 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab01`, edición 2024, sin dependencias ([teoría 01](../../1-teoria/01-cargo-test-y-el-atributo-test.md#paquete-crate-y-módulo-de-tests)) |
| `Cargo.lock` | Las versiones exactas de las dependencias. Aquí no hay ninguna, pero `cargo` lo crea igual |
| `src/lib.rs` | `late_fee(days_late: u32) -> u32`: 50 por día de retraso, hasta 1000. Al final, el módulo `tests` con cuatro tests comentados por PASOS |

`cargo test` compila dos cosas: el binario de tests de `src/lib.rs` (`unittests`) y los ejemplos de la documentación (`Doc-tests`). Las dos dicen `running 0 tests`: el módulo `tests` todavía no tiene ninguna función activa.

## Paso a paso

### Paso 1: el primer test (PASO 1)

1. Abre `src/lib.rs`. Al final está el módulo de tests:

   ```rust
   #[cfg(test)]
   mod tests {
       // PASO 1: el primer test.
       // use super::*;
       // ...
   ```

   `#[cfg(test)]` hace que el módulo solo se compile con `cargo test`: en `cargo build` no existe. `mod tests` es un módulo dentro del archivo; el nombre `tests` es una convención, no una palabra reservada.

2. Descomenta las líneas del bloque `// PASO 1` (borra el `// ` del principio de cada una; en VS Code, selecciónalas y pulsa `Ctrl+/`). Deja la línea `// PASO 1: ...` comentada.
3. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::late_fee_three_days ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   ```

Lee el test:

| Línea | Qué hace |
|---|---|
| `use super::*;` | Trae al módulo `tests` todo lo del módulo padre: `late_fee`, `MAX_FEE`, `FEE_PER_DAY` |
| `#[test]` | Marca la función como test. Sin el atributo es una función normal ([Experimento 2](#experimento-2-una-función-sin-test)) |
| `assert_eq!(late_fee(3), 150);` | Compara los dos valores. Si son distintos, el test entra en pánico y falla |

El `!` indica que `assert_eq!` es una macro, no una función. El nombre que imprime `cargo test` es la ruta del test: `tests::late_fee_three_days`.

### Paso 2: un mensaje propio (PASO 2)

1. Descomenta el bloque `// PASO 2`. El tercer argumento de `assert_eq!` es un mensaje para cuando falla. Compara con la constante `MAX_FEE`, no con un 1000 escrito a mano.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 2 tests
   # test tests::late_fee_is_capped ... ok
   # test tests::late_fee_three_days ... ok
   # ...
   ```

   Los tests se ejecutan en paralelo, en varios hilos, y el orden de las líneas puede cambiar de una ejecución a otra.

### Paso 3: `assert!` y `assert_ne!` (PASO 3)

1. Descomenta el bloque `// PASO 3`:

   - `assert!(condición, "mensaje {fee}")` comprueba que la condición es `true`. El mensaje admite el mismo formato que `println!`: `{fee}` es el valor de la variable `fee`.
   - `assert_ne!(a, b, "mensaje")` comprueba que los dos valores son distintos.

2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 3 tests
   # test tests::late_fee_below_the_cap ... ok
   # test tests::late_fee_is_capped ... ok
   # test tests::late_fee_three_days ... ok
   # ...
   ```

### Paso 4: el caso que el tipo no elimina (PASO 4)

`days_late` es un `u32`, un entero sin signo: los días negativos no existen y no hace falta un test para ellos, como sí hacía falta en Go ([semana 32](../../../week-32-fundamentos_de_testing_en_go/2-practicas/lab-01-primer-test-con-go-test/README.md)). Pero un `u32` tiene un máximo.

1. Descomenta el bloque `// PASO 4`, que prueba el retraso más grande posible, `u32::MAX`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 4 tests
   # test tests::late_fee_below_the_cap ... ok
   # test tests::huge_delay_is_capped ... FAILED
   # test tests::late_fee_is_capped ... ok
   # test tests::late_fee_three_days ... ok
   #
   # failures:
   #
   # ---- tests::huge_delay_is_capped stdout ----
   #
   # thread 'tests::huge_delay_is_capped' (86271) panicked at src/lib.rs:10:5:
   # attempt to multiply with overflow
   # note: run with `RUST_BACKTRACE=1` environment variable to display a backtrace
   #
   #
   # failures:
   #     tests::huge_delay_is_capped
   #
   # test result: FAILED. 3 passed; 1 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   #
   # error: test failed, to rerun pass `--lib`
   ```

   El pánico no está en el test sino en `src/lib.rs:10`, la multiplicación de `late_fee`: `u32::MAX * 50` no cabe en un `u32`. El número entre paréntesis después del nombre del hilo cambia en cada ejecución.

3. Corrige `late_fee` con `saturating_mul`, que se queda en `u32::MAX` en lugar de desbordar:

   ```rust
   pub fn late_fee(days_late: u32) -> u32 {
       days_late.saturating_mul(FEE_PER_DAY).min(MAX_FEE)
   }
   ```

4. Ejecuta otra vez:

   ```bash
   cargo test
   # ...
   # test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

### Paso 5: filtrar y listar

1. El primer argumento de `cargo test` es un filtro: ejecuta los tests cuya ruta **contiene** ese texto:

   ```bash
   cargo test capped
   # running 2 tests
   # test tests::huge_delay_is_capped ... ok
   # test tests::late_fee_is_capped ... ok
   #
   # test result: ok. 2 passed; 0 failed; 0 ignored; 0 measured; 2 filtered out; finished in 0.00s
   ```

   `2 filtered out` son los que no cumplen el filtro.

2. Lo que va después de `--` no es para `cargo` sino para el binario de tests. `--exact` exige que la ruta sea exactamente el filtro:

   ```bash
   cargo test -- --exact tests::late_fee_is_capped
   # running 1 test
   # test tests::late_fee_is_capped ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 3 filtered out; finished in 0.00s
   ```

3. `--list` enumera los tests sin ejecutarlos:

   ```bash
   cargo test -- --list
   # ...
   # tests::huge_delay_is_capped: test
   # tests::late_fee_below_the_cap: test
   # tests::late_fee_is_capped: test
   # tests::late_fee_three_days: test
   #
   # 4 tests, 0 benchmarks
   # ...
   ```

## Experimentos

### Experimento 1: `assert!` o `assert_eq!`

1. En `late_fee_three_days`, cambia el 150 por 151 y ejecuta solo ese test:

   ```bash
   cargo test three
   # ---- tests::late_fee_three_days stdout ----
   #
   # thread 'tests::late_fee_three_days' (86913) panicked at src/lib.rs:20:9:
   # assertion `left == right` failed
   #   left: 150
   #  right: 151
   ```

   `left` es el primer argumento, el valor obtenido; `right`, el esperado.

2. Cambia la línea por `assert!(late_fee(3) == 151);`:

   ```bash
   cargo test three
   # thread 'tests::late_fee_three_days' (86973) panicked at src/lib.rs:20:9:
   # assertion failed: late_fee(3) == 151
   ```

   `assert!` solo sabe que la condición fue `false`: no imprime cuánto devolvió `late_fee`. Para comparar valores, usa `assert_eq!`.

3. Deja la línea como estaba. En `late_fee_is_capped`, cambia `MAX_FEE` por `999`:

   ```bash
   cargo test is_capped
   # thread 'tests::late_fee_is_capped' (87059) panicked at src/lib.rs:26:9:
   # assertion `left == right` failed: 30 days late should hit the cap
   #   left: 1000
   #  right: 999
   ```

   El mensaje propio aparece después de `failed:` y los valores se siguen imprimiendo. Deshaz el cambio.

### Experimento 2: una función sin `#[test]`

Borra la línea `#[test]` de encima de `late_fee_is_capped`:

```bash
cargo test
#    Compiling lab01 v0.1.0 (.../starter)
# warning: function `late_fee_is_capped` is never used
#   --> src/lib.rs:24:8
#    |
# 24 |     fn late_fee_is_capped() {
#    |        ^^^^^^^^^^^^^^^^^^
#    |
#    = note: `#[warn(dead_code)]` (part of `#[warn(unused)]`) on by default
#
# warning: `lab01` (lib test) generated 1 warning
# ...
# running 3 tests
```

La suite sigue en verde con un test menos. El compilador avisa con un `warning`, no con un error: si la salida tiene muchas líneas, se pierde. Es el `CheckXxx` de Go que nunca se ejecuta ([semana 32](../../../week-32-fundamentos_de_testing_en_go/2-practicas/lab-01-primer-test-con-go-test/README.md)). Vuelve a poner `#[test]`.

### Experimento 3: el desbordamiento en `--release`

`cargo test` compila en modo debug, que comprueba los desbordamientos. `cargo test --release` compila optimizado, como el binario que se entrega, y no los comprueba: el número da la vuelta.

1. Vuelve a poner la versión sin `saturating_mul`:

   ```rust
   (days_late * FEE_PER_DAY).min(MAX_FEE)
   ```

2. En `huge_delay_is_capped`, cambia `u32::MAX` por `85_899_346` (el `_` solo separa cifras). Ejecuta en los dos modos:

   ```bash
   cargo test huge
   # thread 'tests::huge_delay_is_capped' (87378) panicked at src/lib.rs:10:5:
   # attempt to multiply with overflow
   cargo test --release huge
   #     Finished `release` profile [optimized] target(s) in 0.07s
   # ...
   # thread 'tests::huge_delay_is_capped' (87412) panicked at src/lib.rs:40:9:
   # assertion `left == right` failed
   #   left: 4
   #  right: 1000
   ```

   En release, `85_899_346 * 50` da la vuelta y vale 4: la multa de un retraso enorme sería 4. Con `u32::MAX` el resultado también da la vuelta, pero queda por encima de 1000 y `min` lo tapa: el test pasaría en release sin que el código esté bien. Los tests en modo debug encuentran el desbordamiento; el arreglo es `saturating_mul`.

3. Deja el código con `saturating_mul` y la prueba con `u32::MAX`.

## Comprobación final

```bash
cargo test
# ...
# running 4 tests
# test tests::huge_delay_is_capped ... ok
# test tests::late_fee_below_the_cap ... ok
# test tests::late_fee_is_capped ... ok
# test tests::late_fee_three_days ... ok
#
# test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Qué pasa con el módulo `tests` cuando ejecutas `cargo build`?
2. ¿Qué información pierdes al escribir `assert!(a == b)` en lugar de `assert_eq!(a, b)`?
3. ¿Qué caso de la multa de Go no hace falta probar en Rust, y cuál aparece a cambio?
4. ¿Por qué `cargo test capped` ejecuta dos tests? ¿Cómo ejecutas solo uno?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Pánicos, Result y diffs](../lab-02-panics-result-y-diffs/README.md) → |

[↑ Semana 37](../../README.md)
