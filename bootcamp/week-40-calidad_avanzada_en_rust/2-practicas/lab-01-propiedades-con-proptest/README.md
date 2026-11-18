# Lab 01 - Propiedades con proptest

## Objetivo

Escribir propiedades con proptest sobre cálculos del museo: un recargo con tope, un reparto entre prestatarios y un código de pieza que se escribe y se vuelve a leer. Una de las propiedades encuentra un error en el reparto; otra, en el experimento, encuentra un número que el código no sabe leer.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling proptest v1.12.0
#    Compiling lab01 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 8.15s
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab01`, con proptest en `[dev-dependencies]` ([teoría 01](../../1-teoria/01-propiedades-con-proptest.md#instalar-proptest)) |
| `Cargo.lock` | Las versiones exactas |
| `src/lib.rs` | `late_fee`, `split_fee`, `format_code` y `parse_code`. Al final, los tests comentados por PASOS |

Lee `split_fee` antes de empezar: reparte un recargo entre varios prestatarios. Su documentación promete dos cosas: las partes suman el total y se diferencian como mucho en 1.

## Paso a paso

### Paso 1: la primera propiedad (PASO 1)

1. Descomenta el bloque `// PASO 1`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 2 tests
   # test tests::late_fee_never_exceeds_the_cap ... ok
   # test tests::late_fee_never_decreases ... ok
   #
   # test result: ok. 2 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.01s
   ```

| Pieza | Qué hace |
|---|---|
| `proptest! { ... }` | Cada función de dentro es un test que se ejecuta 256 veces |
| `days in any::<u32>()` | `days` toma cualquier valor de `u32`, incluido `u32::MAX` |
| `prop_assert!` | La aserción de proptest |
| `let (short, long) = if a <= b ...` | Ordena las dos entradas: la propiedad es «más días nunca cuestan menos» |

Dos tests han probado 512 entradas. Ningún test de ejemplo de la [semana 37](../../../week-37-fundamentos_de_testing_en_rust/README.md) probaba `u32::MAX` por casualidad: había que pensarlo.

### Paso 2: una propiedad que falla (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta:

   ```bash
   cargo test
   # test tests::split_is_fair ... FAILED
   # ...
   # ---- tests::split_is_fair stdout ----
   # proptest: Saving this and future failures in .../starter/proptest-regressions/lib.txt
   # proptest: If this test was run on a CI system, you may wish to add the following line to your copy of the file. (You may need to create it.)
   # cc 3597429603607926c1aa9c40d8a4af23c1810c86faa9ccf2b9c3a00f3208643c
   #
   # thread 'tests::split_is_fair' (434841) panicked at src/lib.rs:59:5:
   # Test failed: shares = [3130, 3128, 3128] at src/lib.rs:72.
   # minimal failing input: total = 9386, parts = 3
   # 	successes: 2
   # 	local rejects: 0
   # 	global rejects: 0
   # ...
   # test result: FAILED. 3 passed; 1 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.01s
   ```

   Tus números serán otros: las entradas son aleatorias. La línea 59 es la del `proptest!`; la 72, la del `prop_assert!`.

   `split_keeps_the_total` pasa: las partes suman el total. `split_is_fair` falla: `split_fee` le suma todo el resto a la primera parte, que acaba 2 por encima de las demás.

3. Mira el archivo de regresiones:

   ```bash
   cat proptest-regressions/lib.txt
   # # Seeds for failure cases proptest has generated in the past. It is
   # # automatically read and these particular cases re-run before any
   # # novel cases are generated.
   # ...
   # cc 3597429603607926c1aa9c40d8a4af23c1810c86faa9ccf2b9c3a00f3208643c # shrinks to total = 9386, parts = 3
   ```

4. Vuelve a ejecutar `cargo test split_is_fair`. Ahora sale `successes: 0`: proptest prueba primero la semilla guardada y falla con el mismo caso.

5. Corrige `split_fee`: en lugar de sumar el resto a la primera parte, suma 1 a tantas partes como indique el resto. Sustituye la línea `shares[0] += total % parts;` por:

   ```rust
   for share in shares.iter_mut().take((total % parts) as usize) {
       *share += 1;
   }
   ```

6. Ejecuta `cargo test`: pasan 4. El caso guardado se sigue probando primero en cada ejecución. El archivo `proptest-regressions/lib.txt` se sube al repositorio con el código.

¿Por qué `9386` y no `2`, que también falla? proptest reduce cada número por la mitad mientras siga fallando, y aquí el fallo depende del resto de la división: muchas mitades pasan, y la búsqueda se para antes de llegar a `2` ([teoría 01](../../1-teoria/01-propiedades-con-proptest.md#3-cuando-falla-el-contraejemplo-reducido)).

### Paso 3: ida y vuelta (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 6.

- `a_formatted_code_parses_back`: lo que `format_code` escribe, `parse_code` lo lee y devuelve lo mismo. Es la propiedad de **ida y vuelta**, la más útil con cualquier par escribir/leer: JSON, formatos de fecha, códigos. El prefijo sale de la expresión regular `"[A-Z]{3}"`.
- `parse_code_never_panics`: con cualquier texto imprimible (`"\\PC*"`), `parse_code` devuelve algo, sin entrar en pánico. Es fuzzing barato, dentro de `cargo test`.

### Paso 4: una estrategia propia (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta `cargo test`: pasan 7.

| Pieza | Qué hace |
|---|---|
| `prop_compose! { fn malformed_code()(...) -> String { ... } }` | Una estrategia nueva: códigos con prefijos y cifras de 1 a 4 caracteres, en mayúsculas o minúsculas |
| `prop_assume!(...)` | Descarta los códigos que por casualidad son válidos: tres mayúsculas y tres cifras |
| `#![proptest_config(ProptestConfig::with_cases(1000))]` | Este bloque prueba 1000 casos en lugar de 256 |

## Experimentos

### Experimento 1: una estrategia más ancha

1. En `a_formatted_code_parses_back`, cambia `number in 0u16..1000` por `number in any::<u16>()`.
2. Ejecuta:

   ```bash
   cargo test parses_back
   # Test failed: assertion failed: `(left == right)`
   #   left: `None`,
   #  right: `Some(("AAA", 1000))` at src/lib.rs:83.
   # minimal failing input: prefix = "AAA", number = 1000
   # 	successes: 0
   ```

   `format_code("AAA", 1000)` escribe `AAA-1000`, con cuatro cifras, y `parse_code` lo rechaza. La estrategia del Paso 3 solo generaba números que el autor sabía que funcionaban. Aquí la reducción sí llega al mínimo exacto: todos los números a partir de 1000 fallan, y la búsqueda por mitades encuentra la frontera.

3. Deshaz el cambio y borra la línea nueva de `proptest-regressions/lib.txt`.

¿Es un error? Depende del dominio: si los códigos tienen tres cifras, `format_code` debería rechazar `1000` en lugar de escribir un código que nadie puede leer. La propiedad no decide; te obliga a decidir.

### Experimento 2: sin reducción

1. Repite el cambio del Experimento 1.
2. Ejecuta:

   ```bash
   PROPTEST_MAX_SHRINK_ITERS=0 cargo test parses_back
   # minimal failing input: prefix = "BYP", number = 29706
   ```

   Esta es la entrada que proptest generó, sin reducir. Con `1000` ves la frontera; con `29706`, solo que algo falla.

3. Deshaz el cambio y borra la línea nueva de `proptest-regressions/lib.txt`.

### Experimento 3: `assert!` en lugar de `prop_assert!`

1. Vuelve a poner el error en `split_fee` (la línea `shares[0] += total % parts;`) y borra `proptest-regressions/`.
2. En `split_is_fair`, cambia `prop_assert!` por `assert!`.
3. Ejecuta:

   ```bash
   cargo test split_is_fair 2>&1 | grep -c "panicked at"
   # 15
   ```

   El fallo se encuentra y se reduce igual, pero cada intento de reducción que falla entra en pánico e imprime su mensaje: 15 pánicos para un solo fallo. Con `prop_assert!`, proptest recibe el fallo como un valor y solo imprime el resultado.

4. Deshaz los dos cambios y borra `proptest-regressions/`.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 7 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.06s
# ...
```

## Preguntas de cierre

1. ¿Qué diferencia hay entre un test de ejemplo y una propiedad? ¿Qué propiedad tiene sentido para un par de funciones que escriben y leen un formato?
2. ¿Por qué la reducción llegó a `1000` exacto en el Experimento 1 y no a `2` en el Paso 2?
3. ¿Para qué sirve `proptest-regressions/`? ¿Qué pasa si cambias la estrategia de una propiedad que tiene semillas guardadas?
4. Compara proptest con Hypothesis ([semana 20](../../../week-20-tdd_con_python/1-teoria/03-property-based-testing-con-hypothesis.md)): ¿qué cambia en cómo se declara la estrategia y en cómo se guardan los fallos?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Fuzzing con cargo-fuzz](../lab-02-fuzzing-con-cargo-fuzz/README.md) → |

[↑ Semana 40](../../README.md)
