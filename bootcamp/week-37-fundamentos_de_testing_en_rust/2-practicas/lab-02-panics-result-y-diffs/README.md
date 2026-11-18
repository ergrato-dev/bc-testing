# Lab 02 - Pánicos, Result y diffs

## Objetivo

Probar los dos caminos de error de Rust: un pánico esperado con `#[should_panic(expected = "...")]` y un `Err` esperado con `assert_eq!` y `matches!`. Escribir tests que devuelven `Result` para usar `?`, y leer la diferencia entre dos estructuras con `pretty_assertions`.

## Tiempo estimado

65 minutos.

## Preparación

```bash
cd starter
cargo test
#     Updating crates.io index
#  Downloading crates ...
#   Downloaded yansi v1.0.1
#   Downloaded pretty_assertions v1.4.1
#   Downloaded diff v0.1.13
#    Compiling diff v0.1.13
#    Compiling yansi v1.0.1
#    Compiling lab02 v0.1.0 (.../starter)
#    Compiling pretty_assertions v1.4.1
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.54s
# ...
# running 0 tests
# ...
```

La primera vez, `cargo` descarga `pretty_assertions` y sus dependencias, que se usan en el Paso 4. Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab02` con `pretty_assertions = "=1.4.1"` en `[dev-dependencies]`: dependencias que solo se compilan para los tests |
| `Cargo.lock` | Las versiones exactas de `pretty_assertions` y de sus dependencias |
| `src/lib.rs` | El seguro de un préstamo (`loan_premium`), el reparto en plazos (`installments`) y el recibo (`receipt`). Al final, los tests comentados por PASOS |

Lee el código antes de empezar. Hay dos formas de fallar:

| Función | Cuando falla | Cómo |
|---|---|---|
| `loan_premium` | Valor declarado 0 o préstamo de 0 días | Devuelve `Err(LoanError::ZeroValue)` o `Err(LoanError::ZeroDays)`: son datos incorrectos que el que llama debe tratar |
| `installments` | `n` es 0 | Entra en pánico con `assert!`: es un error del programa, nadie debería pedir 0 plazos |

`receipt` llama a las dos. El `?` después de `loan_premium(...)` devuelve el `Err` en cuanto aparece, sin seguir.

## Paso a paso

### Paso 1: un pánico esperado (PASO 1)

1. Descomenta el bloque `// PASO 1` (borra el `// ` del principio de cada línea; en VS Code, `Ctrl+/`). Incluye `use super::*;`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::installments_reject_zero - should panic ... ok
   # ...
   ```

`#[should_panic]` invierte el resultado: el test pasa si la función entra en pánico y falla si termina normalmente. `expected = "n must be at least 1"` exige además que el mensaje del pánico **contenga** ese texto. Es el `assertThrows` de JUnit o el `pytest.raises(..., match=...)` de pytest, pero solo para pánicos.

### Paso 2: un error esperado (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 3 tests
   # test tests::premium_rejects_zero_days ... ok
   # test tests::installments_reject_zero - should panic ... ok
   # test tests::premium_rejects_zero_value ... ok
   # ...
   ```

Un `Err` no es un pánico: es un valor más. Los dos tests lo comprueban de dos formas:

| Forma | Qué necesita | Cuándo |
|---|---|---|
| `assert_eq!(loan_premium(5000, 0), Err(LoanError::ZeroDays))` | Que `LoanError` tenga `PartialEq` y `Debug`: es el `#[derive(Debug, PartialEq)]` de encima del `enum` | Cuando puedes escribir el error exacto |
| `assert!(matches!(loan_premium(0, 10), Err(LoanError::ZeroValue)))` | Nada: `matches!` compara con un patrón | Cuando el error lleva datos que no quieres comparar, como un mensaje |

Con `assert_eq!` también compruebas el camino bueno: `assert_eq!(loan_premium(5000, 10), Ok(100))`.

### Paso 3: un test que devuelve `Result` (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 4 tests
   # ...
   # test tests::receipt_for_a_month ... ok
   # ...
   ```

La firma es `fn receipt_for_a_month() -> Result<(), LoanError>`. Así puedes escribir `receipt(...)?` en lugar de `.unwrap()`: si `receipt` devuelve `Err`, el test termina con ese error y falla. Al final, `Ok(())` dice que todo fue bien. El [Experimento 2](#experimento-2--o-unwrap) muestra qué se pierde con cada forma.

### Paso 4: comparar estructuras con `pretty_assertions` (PASO 4)

1. Descomenta el bloque `// PASO 4`. Compara un `Receipt` entero: `assert_eq!` funciona con cualquier tipo que tenga `PartialEq` y `Debug`.
2. Ejecuta `cargo test`: pasan los 5.
3. Rompe el valor esperado: cambia `vec![34, 34, 33]` por `vec![33, 34, 34]`.

   ```bash
   cargo test splits
   # ---- tests::receipt_splits_the_premium stdout ----
   #
   # thread 'tests::receipt_splits_the_premium' (90139) panicked at src/lib.rs:91:9:
   # assertion `left == right` failed
   #   left: Receipt { piece: "MUS-001", premium: 101, installments: [34, 34, 33] }
   #  right: Receipt { piece: "MUS-001", premium: 101, installments: [33, 34, 34] }
   ```

   Las dos líneas casi iguales hay que compararlas a ojo. Con una estructura más grande, la línea no cabe en la pantalla.

4. Debajo de `use super::*;`, añade:

   ```rust
   use pretty_assertions::assert_eq;
   ```

   Esta línea sustituye el `assert_eq!` estándar por el de `pretty_assertions` en el módulo `tests`. Los tests no cambian.

5. Ejecuta otra vez:

   ```bash
   cargo test splits
   # ---- tests::receipt_splits_the_premium stdout ----
   #
   # thread 'tests::receipt_splits_the_premium' (90232) panicked at src/lib.rs:92:9:
   # assertion failed: `(left == right)`
   #
   # Diff < left / right > :
   #  Receipt {
   #      piece: "MUS-001",
   #      premium: 101,
   #      installments: [
   # >        33,
   #          34,
   #          34,
   # <        33,
   #      ],
   #  }
   ```

   Ahora la salida es un diff, en colores en la terminal: `<` es lo que solo está en `left` (el valor obtenido) y `>` lo que solo está en `right` (el esperado). Lo demás coincide. El código reparte el resto de la división en los primeros plazos, y el valor esperado lo ponía en los últimos.

6. Deja `vec![34, 34, 33]` y conserva la línea `use pretty_assertions::assert_eq;`.

## Experimentos

### Experimento 1: `should_panic` sin `expected`

1. En `installments`, borra la línea `assert!(n > 0, ...)`. Ahora, con `n` igual a 0, la función entra en pánico por otro motivo: divide entre 0.

   ```bash
   cargo test reject_zero
   # running 1 test
   # test tests::installments_reject_zero - should panic ... FAILED
   #
   # failures:
   #
   # ---- tests::installments_reject_zero stdout ----
   #
   # thread 'tests::installments_reject_zero' (90772) panicked at src/lib.rs:30:25:
   # attempt to divide by zero
   # note: run with `RUST_BACKTRACE=1` environment variable to display a backtrace
   # note: panic did not contain expected string
   #       panic message: "attempt to divide by zero"
   #  expected substring: "n must be at least 1"
   ```

   El test falla: hay pánico, pero no el que esperaba.

2. Cambia el atributo por `#[should_panic]`, sin `expected`:

   ```bash
   cargo test reject_zero
   # test tests::installments_reject_zero - should panic ... ok
   ```

   En verde, aunque la validación ya no existe. Sin `expected`, cualquier pánico vale: un índice fuera de rango, un `unwrap` sobre `None` o una división entre 0. Por eso `should_panic` siempre lleva `expected`.

3. Deshaz los dos cambios.

### Experimento 2: `?` o `unwrap`

1. En `receipt_for_a_month`, cambia los 30 días por 0. `receipt` devuelve `Err(LoanError::ZeroDays)`:

   ```bash
   cargo test for_a_month
   # running 1 test
   # test tests::receipt_for_a_month ... FAILED
   #
   # failures:
   #
   # ---- tests::receipt_for_a_month stdout ----
   # Error: ZeroDays
   ```

   Sabes qué error hubo, pero no en qué línea: el `?` devolvió el error y el test terminó.

2. Cambia `?` por `.unwrap()`:

   ```bash
   cargo test for_a_month
   # ---- tests::receipt_for_a_month stdout ----
   #
   # thread 'tests::receipt_for_a_month' (91297) panicked at src/lib.rs:78:57:
   # called `Result::unwrap()` on an `Err` value: ZeroDays
   ```

   `unwrap` entra en pánico en el punto exacto: línea 78, columna 57. Con un solo `?` en el test da igual; con varios, el `Error:` no dice cuál falló. Usa `?` para no llenar el test de `unwrap`, y `unwrap` o `expect("...")` cuando necesites la línea.

3. Deja `?` y los 30 días.

## Comprobación final

```bash
cargo test
# ...
# running 5 tests
# test tests::installments_reject_zero - should panic ... ok
# test tests::premium_rejects_zero_days ... ok
# test tests::premium_rejects_zero_value ... ok
# test tests::receipt_for_a_month ... ok
# test tests::receipt_splits_the_premium ... ok
#
# test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

El orden de las líneas `test` puede cambiar: los tests se ejecutan en paralelo.

## Preguntas de cierre

1. ¿Por qué `installments` entra en pánico y `loan_premium` devuelve `Err`? ¿Qué cambiaría en el test si fuera al revés?
2. ¿Qué deja pasar un `#[should_panic]` sin `expected`?
3. ¿Qué necesita un tipo para poder compararlo con `assert_eq!`?
4. ¿Qué ganas y qué pierdes al escribir un test que devuelve `Result`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer test con cargo test](../lab-01-primer-test-con-cargo-test/README.md) | [Lab 03 - Ignore, salida e hilos](../lab-03-ignore-salida-e-hilos/README.md) → |

[↑ Semana 37](../../README.md)
