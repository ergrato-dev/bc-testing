# Lab 03 - Parametrización con rstest

## Objetivo

Reescribir con rstest los tests de reglas del museo: un test por caso con `#[case]`, casos con nombre, combinaciones con `#[values]` y una fixture con argumento. Ver que cada caso falla por su cuenta, a diferencia del bucle de la semana 37.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#     Updating crates.io index
#  Downloading crates ...
#   ...
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 6.86s
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab03` con `rstest = "=0.28.0"` en `[dev-dependencies]` |
| `Cargo.lock` | Las versiones exactas de rstest y de sus dependencias |
| `src/lib.rs` | Reglas del museo de la [semana 37](../../../week-37-fundamentos_de_testing_en_rust/README.md): `validate_piece`, `late_fee` y `return_charges`. Al final, los tests comentados por PASOS |

Para añadir rstest a otro proyecto: `cargo add --dev rstest@=0.28.0`.

## Paso a paso

### Paso 1: casos con `#[case]` (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 5 tests
   # test tests::late_fee_by_days::case_1 ... ok
   # test tests::late_fee_by_days::case_2 ... ok
   # test tests::late_fee_by_days::case_3 ... ok
   # test tests::late_fee_by_days::case_4 ... ok
   # test tests::late_fee_by_days::case_5 ... ok
   # ...
   ```

Una sola función, cinco tests:

| Pieza | Qué hace |
|---|---|
| `#[rstest]` | Sustituye a `#[test]`: rstest genera los tests a partir de la función |
| `#[case(31, 1500)]` | Un caso: los valores van, en orden, a los parámetros marcados con `#[case]` |
| `#[case] days: u32` | Un parámetro que recibe su valor de cada `#[case]` |
| `late_fee_by_days::case_4` | El nombre de cada test: un módulo con el nombre de la función y un test por caso |

El último caso, `u32::MAX`, es la frontera que el tipo no elimina ([semana 37](../../../week-37-fundamentos_de_testing_en_rust/README.md#nota-importante)).

### Paso 2: casos con nombre (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta `cargo test`: pasan 10. Entre ellos:

   ```text
   test tests::validate::case_1_valid ... ok
   test tests::validate::case_2_lowercase_code ... ok
   test tests::validate::case_3_short_code ... ok
   test tests::validate::case_4_blank_name ... ok
   test tests::validate::case_5_zero_value ... ok
   ```

`#[case::zero_value(...)]` da nombre al caso, y el nombre aparece en la salida: es el `t.Run(tt.name, ...)` de Go ([semana 32](../../../week-32-fundamentos_de_testing_en_go/1-teoria/02-table-driven-y-subtests.md)) o el `ids=` de pytest. El valor esperado también es un parámetro: `Result<(), PieceError>` con `Ok(())` o el `Err` de cada caso.

3. Ejecuta un solo caso con el filtro:

   ```bash
   cargo test validate::case_3
   # running 1 test
   # test tests::validate::case_3_short_code ... ok
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 19 filtered out; finished in 0.00s
   ```

### Paso 3: combinaciones con `#[values]` (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 18. Los 8 nuevos son todas las combinaciones de 4 días por 2 estados:

   ```text
   test tests::total_is_the_sum::days_1_0::condition_1_Condition__Good ... ok
   test tests::total_is_the_sum::days_1_0::condition_2_Condition__Damaged ... ok
   ...
   test tests::total_is_the_sum::days_4_31::condition_2_Condition__Damaged ... ok
   ```

`#[values(...)]` no da un resultado esperado por caso: el test comprueba una regla que vale para todos, aquí que el total es la suma de los dos cargos. Es útil cuando una regla debe cumplirse para cualquier combinación de entradas. El número de tests es el producto de los valores: añadir un tercer parámetro de 4 valores los llevaría a 32.

### Paso 4: una fixture con argumento (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # test result: ok. 20 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   ```

| Pieza | Qué hace |
|---|---|
| `#[fixture] fn name(...) -> String` | Una fixture: construye un nombre de `len` letras `á` |
| `#[default(MAX_NAME_LENGTH)] len: usize` | Si el test no dice otra cosa, el nombre tiene 120 caracteres |
| `fn accepts_name_at_max_length(name: String)` | El test pide la fixture por el nombre del parámetro, `name`, como en pytest |
| `#[with(MAX_NAME_LENGTH + 1)] name: String` | Este test pide la misma fixture con 121 caracteres |

La `á` ocupa dos bytes: un nombre de 120 `á` mide 120 caracteres y 240 bytes. Es el caso que en la semana 37 encontró el error de contar bytes; el [Experimento 3](#experimento-3-bytes-en-lugar-de-caracteres) lo repite.

## Experimentos

### Experimento 1: dos casos que fallan

1. En `late_fee`, borra `.min(MAX_LATE_FEE)`: el recargo deja de tener tope.
2. Ejecuta:

   ```bash
   cargo test late_fee
   # running 5 tests
   # test tests::late_fee_by_days::case_1 ... ok
   # test tests::late_fee_by_days::case_2 ... ok
   # test tests::late_fee_by_days::case_3 ... ok
   # test tests::late_fee_by_days::case_4 ... FAILED
   # test tests::late_fee_by_days::case_5 ... FAILED
   #
   # failures:
   #
   # ---- tests::late_fee_by_days::case_4 stdout ----
   #
   # thread 'tests::late_fee_by_days::case_4' (232761) panicked at src/lib.rs:92:9:
   # assertion `left == right` failed
   #   left: 1550
   #  right: 1500
   # ...
   # ---- tests::late_fee_by_days::case_5 stdout ----
   #
   # thread 'tests::late_fee_by_days::case_5' (232762) panicked at src/lib.rs:92:9:
   # assertion `left == right` failed
   #   left: 4294967295
   #  right: 1500
   # ...
   # test result: FAILED. 3 passed; 2 failed; 0 ignored; 0 measured; 15 filtered out; finished in 0.00s
   ```

   Fallan los dos casos, cada uno con su nombre. Con el bucle sobre un array de la semana 37, el test se paraba en el `case_4` y el `case_5` no llegaba a ejecutarse. La línea, `src/lib.rs:92`, es la del `assert_eq!`, la misma para todos los casos: el que dice qué caso falla es el nombre.

3. Deshaz el cambio.

### Experimento 2: un caso incompleto

1. Cambia `#[case(0, 0)]` por `#[case(0)]`.
2. Ejecuta `cargo test`:

   ```text
   error: Wrong case signature: should match the given parameters list.
     --> src/lib.rs:86:12
      |
   86 |     #[case(0)]
      |            ^
   ```

   rstest comprueba al compilar que cada caso tenga un valor por parámetro. En Go, a una fila de la tabla le faltaba un campo y valía cero sin avisar.

3. Deshaz el cambio.

### Experimento 3: bytes en lugar de caracteres

1. En `validate_piece`, cambia `name.chars().count()` por `name.len()`.
2. Ejecuta:

   ```bash
   cargo test name
   # running 3 tests
   # test tests::accepts_name_at_max_length ... FAILED
   # test tests::rejects_name_over_max_length ... ok
   # test tests::validate::case_4_blank_name ... ok
   # ...
   # thread 'tests::accepts_name_at_max_length' (233511) panicked at src/lib.rs:131:9:
   # assertion `left == right` failed
   #   left: Err(NameTooLong)
   #  right: Ok(())
   ```

   `len()` cuenta 240 bytes y rechaza un nombre de 120 caracteres. El test que rechaza 121 sigue en verde: solo la frontera exacta encuentra el error. El filtro `name` también ejecuta `case_4_blank_name`, porque su nombre contiene esa palabra.

3. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 20 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Qué ganas con `#[case]` frente a un bucle sobre un array de casos dentro de un `#[test]`?
2. ¿Cuándo usarías `#[values]` en lugar de `#[case]`? ¿Qué riesgo tiene?
3. ¿Qué diferencia hay entre `#[default(...)]` y `#[with(...)]` en una fixture?
4. Compara un `#[case::nombre]` con la tabla de Go y con `@ParameterizedTest` de JUnit: ¿cuál detecta antes una fila incompleta?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Mocks con mockall](../lab-02-mocks-con-mockall/README.md) | [Lab 04 - Snapshots con insta y tests/common](../lab-04-snapshots-con-insta-y-tests-common/README.md) → |

[↑ Semana 38](../../README.md)
