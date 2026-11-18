# Lab 02 - Fuzzing con cargo-fuzz

## Objetivo

Dejar que libFuzzer encuentre dos errores en un lector de ISBN que pasa todos los tests de su tabla: primero un pánico, después una entrada inválida que se acepta. Es el lector del [lab 02 de la semana 35](../../../week-35-concurrencia_y_calidad_en_go/2-practicas/lab-02-fuzzing-nativo/README.md), en Rust, y cada error encontrado acaba en un test de regresión.

## Tiempo estimado

55 minutos.

## Preparación

Necesitas nightly y cargo-fuzz ([teoría 02](../../1-teoria/02-fuzzing-con-cargo-fuzz.md#instalar)). Comprueba que están:

```bash
rustup run nightly rustc --version
# rustc 1.102.0-nightly (59cbdee2e 2026-11-17)
cargo fuzz --version
# cargo-fuzz 0.13.2
```

Después:

```bash
cd starter
cargo test
# ...
# running 1 test
# test tests::table ... ok
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab02`, sin dependencias |
| `Cargo.lock` | Generado por cargo |
| `src/lib.rs` | `parse`, que lee un ISBN-13 con o sin el prefijo `ISBN `, guiones y espacios. Un test de tabla con siete casos. Al final, dos tests comentados por PASOS |

Lee `parse`. Los siete casos de la tabla pasan, y aun así tiene dos errores.

## Paso a paso

### Paso 1: crear el fuzz target

1. Ejecuta:

   ```bash
   cargo fuzz init --target parse
   ```

   No imprime nada. Crea la carpeta `fuzz/`:

   | Archivo | Qué es |
   |---|---|
   | `fuzz/Cargo.toml` | El crate `lab02-fuzz`. Depende de `libfuzzer-sys` y de `lab02` por `path = ".."` |
   | `fuzz/fuzz_targets/parse.rs` | El target, con un cuerpo vacío |
   | `fuzz/.gitignore` | Ignora `target`, `corpus`, `artifacts` y `coverage` |

2. Sustituye el contenido de `fuzz/fuzz_targets/parse.rs` por:

   ```rust
   #![no_main]

   use libfuzzer_sys::fuzz_target;

   fuzz_target!(|data: &str| {
       let _ = lab02::parse(data);
   });
   ```

   El target recibe un `&str`: libFuzzer genera bytes, y `libfuzzer-sys` descarta los que no son UTF-8 válido. La única comprobación es que `parse` no entre en pánico.

### Paso 2: el primer fallo

1. Ejecuta:

   ```bash
   cargo +nightly fuzz run parse -- -max_total_time=60
   #    Compiling ...
   #     Finished `release` profile [optimized + debuginfo] target(s) in 23.85s
   # ...
   # INFO: Seed: 1987567113
   # ...
   # #2	INITED cov: 50 ft: 50 corp: 1/1b exec/s: 0 rss: 33Mb
   # #5	NEW    cov: 52 ft: 57 corp: 2/3b lim: 4 exec/s: 0 rss: 34Mb L: 2/2 MS: 3 CrossOver-ChangeByte-InsertByte-
   # ...
   # thread '<unnamed>' (440287) panicked at .../starter/src/lib.rs:23:42:
   # start byte index 5 is out of bounds for string of length 4
   # ...
   # Failing input:
   #
   # 	fuzz/artifacts/parse/crash-804525c8009e09a9b3f128e566dab3e3b40f20f8
   #
   # Output of `std::fmt::Debug`:
   #
   # 	"ISBN"
   # ...
   # Error: Fuzz target exited with exit status: 77
   ```

   La primera vez compila libFuzzer y tu crate con instrumentación: unos 25 s. Encontrar el fallo, menos de un segundo. El nombre del archivo y la semilla cambian en cada ejecución; la entrada suele ser `"ISBN"`.

2. Lee la línea 23 de `src/lib.rs`:

   ```rust
   let s = if s.starts_with("ISBN") { &s[5..] } else { s }; // salta "ISBN "
   ```

   El código supone que después de `ISBN` viene un espacio. Con `"ISBN"` solo, `&s[5..]` corta más allá del final del texto. Es el mismo error que encontró el fuzzer de Go en la semana 35; allí, Go entraba en pánico con `slice bounds out of range`.

### Paso 3: reproducir y fijar el fallo (PASO 3)

1. Reproduce solo esa entrada, con el archivo que te dio el fuzzer:

   ```bash
   cargo +nightly fuzz run parse fuzz/artifacts/parse/crash-804525c8009e09a9b3f128e566dab3e3b40f20f8
   # ...
   # thread '<unnamed>' (...) panicked at .../starter/src/lib.rs:23:42:
   # start byte index 5 is out of bounds for string of length 4
   ```

   El mensaje del fuzzer sugiere `cargo fuzz run ...` sin `+nightly`. Si lo copias tal cual:

   ```text
   error: the option `Z` is only accepted on the nightly compiler
   ```

2. Descomenta el bloque `// PASO 3`: un test normal con la entrada que encontró el fuzzer.
3. Ejecuta `cargo test`:

   ```bash
   cargo test
   # thread 'tests::a_bare_prefix_is_too_short' (443904) panicked at src/lib.rs:23:42:
   # start byte index 5 is out of bounds for string of length 4
   # ...
   # test result: FAILED. 1 passed; 1 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

   El fallo ya no depende del fuzzer: `cargo test` lo repite, en estable.

4. Corrige la línea 23:

   ```rust
   let s = s.strip_prefix("ISBN").unwrap_or(s);
   ```

   `strip_prefix` devuelve `Some(resto)` si el texto empieza por `ISBN`, sin cortar a mano. El espacio que viene después lo quita el filtro de la línea siguiente, junto con los guiones.

5. Ejecuta `cargo test`: pasan 2.

### Paso 4: comprobar el resultado (PASO 4)

Un target que solo llama a `parse` encuentra pánicos. Para encontrar entradas aceptadas que no deberían serlo, comprueba el resultado.

1. Sustituye el cuerpo de `fuzz/fuzz_targets/parse.rs` por:

   ```rust
   fuzz_target!(|data: &str| {
       if let Ok(isbn) = lab02::parse(data) {
           let digits = isbn.as_str();
           let ok = digits.len() == 13 && digits.bytes().all(|b| b.is_ascii_digit());
           assert!(ok, "{data:?} gave {digits:?}");
       }
   });
   ```

2. Ejecuta:

   ```bash
   cargo +nightly fuzz run parse -- -max_total_time=120
   # ...
   # thread '<unnamed>' (440729) panicked at fuzz_targets/parse.rs:9:9:
   # " '\0'\0\0\0\0\0\0\0\0\0\0" gave "'\0'\0\0\0\0\0\0\0\0\0\0"
   # ...
   ```

   Tu entrada será otra: 13 caracteres que no son dígitos, tras quitar espacios y guiones. `parse` la acepta porque `c.to_digit(10).unwrap_or(0)` convierte cualquier carácter que no es un dígito en un `0`, y una ristra de ceros tiene un dígito de control válido. El fuzzer la encontró en menos de un segundo.

3. Intenta acortarla:

   ```bash
   cargo +nightly fuzz tmin parse fuzz/artifacts/parse/crash-<el-tuyo>
   # ...
   # CRASH_MIN: failed to minimize beyond .../minimized-from-... (13 bytes), exiting
   # ...
   # Output of `std::fmt::Debug`:
   #
   # 	"\0\0\u{2}\0\0\0\0\0\0\0\0\0\0"
   ```

   `tmin` busca una entrada más corta que falle igual. Aquí no la hay: cualquier ISBN tiene 13 caracteres. Cambia unos bytes por otros, pero no simplifica como el *shrinking* de proptest.

4. Corrige `parse`. Añade la variante `NotADigit` a `IsbnError`:

   ```rust
   pub enum IsbnError {
       Length,
       Checksum,
       NotADigit,
   }
   ```

   Y cambia la línea del `unwrap_or(0)` por:

   ```rust
   let d = c.to_digit(10).ok_or(IsbnError::NotADigit)?;
   ```

5. Descomenta el bloque `// PASO 4`. Usa un texto legible, `"ABCDEFGHIJKLM"`, que falla por la misma razón que la entrada del fuzzer.
6. Ejecuta `cargo test`: pasan 3.
7. Vuelve a ejecutar el fuzzer un minuto:

   ```bash
   cargo +nightly fuzz run parse -- -max_total_time=60
   # ...
   # #9205612	DONE   cov: 103 ft: 319 corp: 89/5157b lim: 4096 exec/s: 150911 rss: 544Mb
   # Done 9205612 runs in 61 second(s)
   ```

   Nueve millones de entradas sin fallos.

## Experimentos

### Experimento 1: el corpus como prueba rápida

```bash
cargo +nightly fuzz run parse -- -runs=0
# ...
# INFO: seed corpus: files: 100 min: 1b max: 1270b total: 5203b rss: 33Mb
# #101	INITED cov: 103 ft: 319 corp: 81/4892b exec/s: 0 rss: 38Mb
# #101	DONE   cov: 103 ft: 319 corp: 81/4892b lim: 1270 exec/s: 0 rss: 38Mb
```

`-runs=0` ejecuta solo las entradas del corpus, sin generar nuevas. Tarda menos de un segundo y llega a la misma cobertura (`cov: 103`) que el minuto de fuzzing. Es una forma de comprobar que un cambio no rompe lo que el fuzzer ya exploró. El corpus no se sube al repositorio (`fuzz/.gitignore` lo excluye): si quieres conservarlo, cópialo a otra carpeta y pásala como argumento.

### Experimento 2: vuelve a romperlo

1. Vuelve a poner `unwrap_or(0)` en lugar de `ok_or(IsbnError::NotADigit)?` (deja la variante en el `enum`).
2. Ejecuta `cargo test`: falla `rejects_letters`. El test del Paso 4 protege el arreglo sin necesidad de nightly.
3. Ejecuta `cargo +nightly fuzz run parse -- -max_total_time=60`. ¿Cuánto tarda en encontrarlo con el corpus que ya tienes?
4. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 3 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
cargo +nightly fuzz run parse -- -max_total_time=60
# ...
# Done ... runs in 61 second(s)
```

## Preguntas de cierre

1. ¿Por qué cargo-fuzz necesita nightly, y por qué el resto del proyecto no?
2. ¿Qué encontró el target que solo llamaba a `parse`, y qué encontró el que comprobaba el resultado?
3. En Go, el fallo quedaba en `testdata/fuzz/` y `go test` lo repetía. ¿Qué hiciste en Rust para conseguir lo mismo?
4. ¿Qué diferencia hay entre `tmin` y el *shrinking* de proptest?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Propiedades con proptest](../lab-01-propiedades-con-proptest/README.md) | [Lab 03 - Benchmarks con criterion](../lab-03-benchmarks-con-criterion/README.md) → |

[↑ Semana 40](../../README.md)
