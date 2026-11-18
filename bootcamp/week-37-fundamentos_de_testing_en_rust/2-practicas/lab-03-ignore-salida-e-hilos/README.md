# Lab 03 - Ignore, salida e hilos

## Objetivo

Controlar cómo ejecuta `cargo test` la suite: ver lo que imprime un test con `--nocapture` y `--show-output`, provocar un test inestable entre tests que comparten un archivo, diagnosticarlo con `--test-threads=1` y arreglarlo, y apartar un test lento con `#[ignore]`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab03`, sin dependencias |
| `src/lib.rs` | `write_report(path, loans)` escribe un CSV con una línea `código,días` por préstamo; `total_days(path)` lo lee y suma los días. Al final, los tests comentados por PASOS |

Los tests escriben archivos en la carpeta temporal del sistema: `env::temp_dir()` devuelve `/tmp` en Linux y WSL.

## Paso a paso

### Paso 1: lo que imprime un test (PASO 1)

1. Descomenta el bloque `// PASO 1` (borra el `// ` del principio de cada línea; en VS Code, `Ctrl+/`). El test escribe un informe, lo lee y lo imprime con `println!` antes de comprobarlo.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::report_has_a_header ... ok
   # ...
   ```

   El `println!` no aparece: `cargo test` **captura** la salida de cada test y solo la enseña si el test falla.

3. Para verla, pásale una opción al binario de tests, después de `--`:

   ```bash
   cargo test header -- --nocapture
   # running 1 test
   # report:
   # code,days
   # MUS-001,3
   #
   # test tests::report_has_a_header ... ok
   ```

   `--nocapture` imprime la salida en el momento, sin capturarla. Con varios tests en paralelo, las líneas de unos y otros se mezclan.

4. La alternativa ordenada:

   ```bash
   cargo test header -- --show-output
   # running 1 test
   # test tests::report_has_a_header ... ok
   #
   # successes:
   #
   # ---- tests::report_has_a_header stdout ----
   # report:
   # code,days
   # MUS-001,3
   # ...
   ```

   `--show-output` sigue capturando, y al final enseña la salida de cada test que pasó, agrupada bajo su nombre.

### Paso 2: tres tests, un archivo (PASO 2)

1. Descomenta el bloque `// PASO 2`. Los dos tests nuevos usan la misma ruta que el del PASO 1: `lab03-report.csv`.
2. Ejecuta la suite 20 veces y cuenta los resultados. `cargo test -q` imprime menos; `> /dev/null 2>&1` descarta toda la salida; `&&` y `||` eligen qué imprimir según el código de salida; `sort | uniq -c` cuenta las líneas iguales:

   ```bash
   for i in $(seq 20); do cargo test -q > /dev/null 2>&1 && echo ok || echo FALLO; done | sort | uniq -c
   #       9 FALLO
   #      11 ok
   ```

   El número cambia en cada máquina y en cada vuelta, pero no es 0. Repite hasta que `cargo test` falle y lee el fallo:

   ```bash
   cargo test
   # running 3 tests
   # test tests::report_has_a_header ... FAILED
   # test tests::total_of_two_loans ... ok
   # test tests::total_of_a_long_report ... ok
   #
   # failures:
   #
   # ---- tests::report_has_a_header stdout ----
   # report:
   #
   #
   # thread 'tests::report_has_a_header' (96112) panicked at src/lib.rs:39:9:
   # assertion failed: text.starts_with("code,days\n")
   ```

   El test falló, así que `cargo test` enseña lo que imprimió: un informe vacío. Lo leyó justo cuando otro test acababa de vaciar el archivo para escribir el suyo.

3. Repite con un solo hilo:

   ```bash
   for i in $(seq 20); do cargo test -q -- --test-threads=1 > /dev/null 2>&1 && echo ok || echo FALLO; done | sort | uniq -c
   #      20 ok
   ```

   `cargo test` ejecuta los tests en paralelo, en tantos hilos como núcleos tiene la máquina. Con `--test-threads=1`, uno detrás de otro: ya no se pisan. Es un diagnóstico: si un test falla en paralelo y nunca con un hilo, comparte algo con otro test.

`--test-threads=1` no es el arreglo: hace la suite más lenta y el problema sigue en el código de los tests. En Go había que pedir el paralelismo con `t.Parallel()` ([semana 32](../../../week-32-fundamentos_de_testing_en_go/2-practicas/lab-02-table-driven-y-subtests/README.md)); en Rust viene activado y los tests no deben compartir archivos, variables de entorno ni el directorio actual.

### Paso 3: una ruta para cada test (PASO 3)

1. Descomenta el bloque `// PASO 3`: la función `temp_path` construye una ruta distinta con el nombre que le pases. No lleva `#[test]`: es un helper.
2. En los tres tests, cambia `env::temp_dir().join("lab03-report.csv")` por `temp_path("...")`, con un nombre distinto en cada uno:

   ```rust
   let path = temp_path("header");   // report_has_a_header
   let path = temp_path("two");      // total_of_two_loans
   let path = temp_path("long");     // total_of_a_long_report
   ```

3. Repite las 20 vueltas en paralelo:

   ```bash
   for i in $(seq 20); do cargo test -q > /dev/null 2>&1 && echo ok || echo FALLO; done | sort | uniq -c
   #      20 ok
   ```

Los archivos se quedan en `/tmp` y la siguiente ejecución los sobrescribe. Si ejecutas dos `cargo test` del mismo lab a la vez, vuelven a pisarse; el crate `tempfile` crea una carpeta temporal distinta por test y la borra al terminar, como `t.TempDir` en Go.

### Paso 4: un test lento con `#[ignore]` (PASO 4)

1. Descomenta el bloque `// PASO 4`. El test escribe y lee un informe de 2 millones de líneas.
2. Ejecuta la suite:

   ```bash
   cargo test
   # ...
   # running 4 tests
   # test tests::total_of_a_huge_report ... ignored, lento: escribe y lee un informe de 2 millones de líneas
   # test tests::report_has_a_header ... ok
   # test tests::total_of_two_loans ... ok
   # test tests::total_of_a_long_report ... ok
   #
   # test result: ok. 3 passed; 0 failed; 1 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

   `#[ignore = "motivo"]` compila el test pero no lo ejecuta, y el motivo aparece en la salida. La suite de cada commit sigue tardando 0.00 s.

3. Ejecuta solo los ignorados, o todos:

   ```bash
   cargo test -- --ignored
   # running 1 test
   # test tests::total_of_a_huge_report ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 3 filtered out; finished in 0.65s
   cargo test -- --include-ignored
   # ...
   # test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.66s
   ```

`#[ignore]` es el `@Disabled` de JUnit, pero con una forma de ejecutarlos desde la línea de comandos. Úsalo para lo lento o lo que necesita algo externo, y ejecuta `--include-ignored` en CI o antes de entregar. Un test ignorado que nadie ejecuta acaba roto sin que nadie lo vea.

## Comprobación final

```bash
cargo test -- --include-ignored
# ...
# running 4 tests
# test tests::report_has_a_header ... ok
# test tests::total_of_two_loans ... ok
# test tests::total_of_a_long_report ... ok
# test tests::total_of_a_huge_report ... ok
#
# test result: ok. 4 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.66s
# ...
```

## Preguntas de cierre

1. ¿Cuándo enseña `cargo test` lo que imprime un test sin que le pidas nada?
2. ¿Qué diferencia hay entre `--nocapture` y `--show-output`?
3. Un test falla una de cada tres veces y nunca con `--test-threads=1`. ¿Qué buscas en el código de los tests?
4. ¿Qué diferencia hay entre `--ignored` y `--include-ignored`? ¿Cuál pondrías en CI?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Pánicos, Result y diffs](../lab-02-panics-result-y-diffs/README.md) | [Lab 04 - Integración y doctests](../lab-04-integracion-y-doctests/README.md) → |

[↑ Semana 37](../../README.md)
