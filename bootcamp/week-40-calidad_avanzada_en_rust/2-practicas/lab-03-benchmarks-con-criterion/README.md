# Lab 03 - Benchmarks con criterion

## Objetivo

Medir con criterion una función que suma los ejemplares de cada ISBN del catálogo, descubrir con un grupo de tamaños que su coste es cuadrático, optimizarla y demostrar con una línea base guardada que la mejora es real. Es el [lab 03 de la semana 35](../../../week-35-concurrencia_y_calidad_en_go/2-practicas/lab-03-benchmarks-con-benchstat/README.md), en Rust, con criterion haciendo el papel de benchstat.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling criterion v0.8.2
#    ...
# running 2 tests
# test tests::adds_copies_of_the_same_isbn ... ok
# test tests::catalog_repeats_each_isbn_twice ... ok
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab03`, con criterion y su feature `html_reports` en `[dev-dependencies]` ([teoría 03](../../1-teoria/03-benchmarks-con-criterion.md#instalar-criterion)) |
| `Cargo.lock` | Las versiones exactas |
| `src/lib.rs` | `merge_copies`, que suma los ejemplares de cada ISBN, y `catalog(n)`, que genera `n` libros con cada ISBN repetido dos veces. Dos tests |
| `benches/inventory.rs` | El benchmark, comentado por PASOS |

Lee `merge_copies`: para cada libro busca su ISBN en `merged` con `position`, que recorre la lista desde el principio.

## Paso a paso

### Paso 1: el primer benchmark (PASO 1)

1. En `benches/inventory.rs`, descomenta los dos bloques `// PASO 1`: el de arriba, con la función `merge`, y el de abajo, con `criterion_group!` y `criterion_main!`.
2. Ejecuta:

   ```bash
   cargo bench
   # ...
   #      Running benches/inventory.rs (target/release/build/lab03/.../inventory-...)
   #
   # running 0 tests
   #
   # test result: ok. 0 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

   Compila, termina bien y no mide nada. Cargo ha compilado `benches/inventory.rs` con el arnés de tests de la biblioteca estándar, que busca funciones `#[bench]` e ignora el `main` de criterion.

   Fíjate también en el bloque de antes: `cargo bench` ejecuta los tests de `src/lib.rs` y los marca como `ignored`. Con `--bench inventory` solo ejecutas ese benchmark.

3. Añade al final de `Cargo.toml`:

   ```toml
   [[bench]]
   name = "inventory"
   harness = false
   ```

4. Ejecuta:

   ```bash
   cargo bench --bench inventory
   # ...
   # Gnuplot not found, using plotters backend
   # Benchmarking merge_copies 1000: Warming up for 3.0000 s
   # Benchmarking merge_copies 1000: Collecting 100 samples in estimated 5.4139 s (10k iterations)
   # Benchmarking merge_copies 1000: Analyzing
   # merge_copies 1000       time:   [528.80 µs 531.96 µs 535.12 µs]
   # Found 2 outliers among 100 measurements (2.00%)
   #   1 (1.00%) high mild
   #   1 (1.00%) high severe
   ```

   Las líneas `Benchmarking ...` se van sobrescribiendo en la terminal; al final queda el resultado. `Gnuplot not found` no es un error: criterion dibuja las gráficas del informe con su propia biblioteca.

| Parte | Qué es |
|---|---|
| `Warming up for 3.0000 s` | Ejecuta el código 3 s sin medir, para que la CPU y las cachés se estabilicen |
| `Collecting 100 samples ... (10k iterations)` | 100 muestras; cada una repite la función muchas veces. 10 000 ejecuciones en total |
| `[528.80 µs 531.96 µs 535.12 µs]` | Medio milisegundo por llamada con 1000 libros, con su intervalo de confianza |

5. Abre `target/criterion/report/index.html` en el navegador: un informe con la distribución de las muestras de cada benchmark.

### Paso 2: cómo crece el coste (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. En `criterion_group!`, añade la función nueva:

   ```rust
   criterion_group!(benches, merge, merge_sizes);
   ```

   Si no la añades, el compilador avisa: `function merge_sizes is never used`.

3. Ejecuta solo el grupo nuevo. El texto después de `--` filtra los benchmarks por nombre:

   ```bash
   cargo bench --bench inventory -- merge_copies/
   # merge_copies/100        time:   [6.1892 µs 6.2606 µs 6.3360 µs]
   #                         thrpt:  [15.783 Melem/s 15.973 Melem/s 16.157 Melem/s]
   # merge_copies/1000       time:   [534.88 µs 539.05 µs 543.42 µs]
   #                         thrpt:  [1.8402 Melem/s 1.8551 Melem/s 1.8696 Melem/s]
   # ...
   # Warning: Unable to complete 100 samples in 5.0s. You may wish to increase target time to 5.0s, or reduce sample count to 90.
   # merge_copies/10000      time:   [50.069 ms 50.539 ms 51.028 ms]
   #                         thrpt:  [195.97 Kelem/s 197.87 Kelem/s 199.73 Kelem/s]
   ```

   De 100 a 1000 libros, el tiempo se multiplica por 86; de 1000 a 10 000, por 94. Diez veces más libros, unas cien veces más tiempo: el coste es cuadrático. La línea `thrpt` (libros por segundo) lo dice sin hacer cuentas: cae de 16 millones a 200 000 por segundo.

   El `Warning` aparece porque 100 muestras de 50 ms no caben en 5 s. criterion alarga la medición y sigue; no hace falta tocar nada.

### Paso 3: antes y después

1. Guarda la medición actual como línea base:

   ```bash
   cargo bench --bench inventory -- --save-baseline before merge_copies/
   # merge_copies/100        time:   [6.1871 µs 6.2619 µs 6.3443 µs]
   # merge_copies/1000       time:   [527.88 µs 531.88 µs 536.78 µs]
   # merge_copies/10000      time:   [48.459 ms 48.931 ms 49.437 ms]
   ```

2. Optimiza `merge_copies` con un índice: un `HashMap` del ISBN a su posición en `merged`. Añade al principio de `src/lib.rs`, después del comentario `//!`:

   ```rust
   use std::collections::HashMap;
   ```

   Y sustituye el cuerpo de `merge_copies` por:

   ```rust
   let mut merged: Vec<(String, u32)> = Vec::new();
   let mut index: HashMap<&str, usize> = HashMap::new();
   for book in books {
       match index.get(book.isbn.as_str()) {
           Some(&i) => merged[i].1 += book.copies,
           None => {
               index.insert(&book.isbn, merged.len());
               merged.push((book.isbn.clone(), book.copies));
           }
       }
   }
   merged
   ```

   El orden del resultado no cambia: `merged` sigue siendo un `Vec` en el orden de aparición, y el `HashMap` solo sirve para encontrar la posición sin recorrerlo.

3. Ejecuta `cargo test`: los dos tests siguen pasando. Optimizar sin tests es cambiar el comportamiento sin saberlo.
4. Compara con la línea base:

   ```bash
   cargo bench --bench inventory -- --baseline before merge_copies/
   # merge_copies/100        time:   [3.9232 µs 3.9836 µs 4.0520 µs]
   #                         thrpt:  [24.679 Melem/s 25.103 Melem/s 25.489 Melem/s]
   #                  change:
   #                         time:   [−37.127% −35.771% −34.330%] (p = 0.00 < 0.05)
   #                         thrpt:  [+52.277% +55.692% +59.050%]
   #                         Performance has improved.
   # ...
   # merge_copies/1000       time:   [47.510 µs 48.290 µs 49.117 µs]
   #                  change:
   #                         time:   [−91.307% −91.161% −91.014%] (p = 0.00 < 0.05)
   #                         Performance has improved.
   # ...
   # merge_copies/10000      time:   [446.08 µs 451.77 µs 457.91 µs]
   #                  change:
   #                         time:   [−99.098% −99.085% −99.072%] (p = 0.00 < 0.05)
   #                         Performance has improved.
   ```

   La mejora crece con el tamaño: −36 % con 100 libros, −99 % con 10 000. Y el throughput ya no cae: unos 21-25 millones de libros por segundo en los tres tamaños. El coste es lineal.

`--baseline before` compara sin sobrescribir la línea base: puedes probar varias optimizaciones contra la misma.

## Experimentos

### Experimento 1: medir dos veces lo mismo

1. Sin cambiar nada, ejecuta dos veces:

   ```bash
   cargo bench --bench inventory -- "merge_copies 1000"
   cargo bench --bench inventory -- "merge_copies 1000"
   ```

2. Lee la segunda salida. Con el código del Paso 1 salió:

   ```text
   merge_copies 1000       time:   [528.54 µs 532.12 µs 536.09 µs]
                           change: [+0.4968% +1.6164% +2.8304%] (p = 0.01 < 0.05)
                           Change within noise threshold.
   ```

   `p = 0.01` dice que la diferencia es estadísticamente real, pero el intervalo toca la franja de ±1 % que criterion considera ruido. Una diferencia así entre dos ejecuciones del mismo código es la máquina, no el código. Tus números serán otros; puede que salga `No change in performance detected.`

### Experimento 2: los benchmarks como test

```bash
cargo test --benches
# ...
# Testing merge_copies 1000
# Success
# Testing merge_copies/100
# Success
# Testing merge_copies/1000
# Success
# Testing merge_copies/10000
# Success
```

Ejecuta cada benchmark una vez, sin medir. Tarda segundos en lugar de minutos. En la integración continua sirve para comprobar que los benchmarks compilan y no fallan; medir allí no tiene sentido, porque la máquina cambia de una ejecución a otra.

### Experimento 3: el `HashMap` en el caso pequeño

Con 100 libros la mejora fue del 36 %; con 10 000, del 99 %. Prueba con `[10, 100]` en lugar de `[100, 1000, 10_000]` y guarda una línea base antes de optimizar (vuelve a poner la versión con `position` para medirla). ¿Hay un tamaño en el que la versión con `position` gane? Un `HashMap` tiene un coste fijo por elemento (calcular el hash) que una búsqueda lineal en una lista corta no paga.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 2 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
cargo test --benches
# ...
# Success
```

## Preguntas de cierre

1. ¿Qué pasa si un benchmark de criterion no tiene `[[bench]]` con `harness = false` en `Cargo.toml`? ¿Por qué es peligroso?
2. ¿Para qué sirve `black_box`?
3. ¿Qué información da `Throughput::Elements` que el tiempo solo no da?
4. Compara criterion con `go test -bench` y benchstat ([semana 35](../../../week-35-concurrencia_y_calidad_en_go/1-teoria/03-benchmarks-y-benchstat.md)): ¿qué hace cada herramienta, y cuántas ejecuciones hacen falta en cada lenguaje para comparar antes y después?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Fuzzing con cargo-fuzz](../lab-02-fuzzing-con-cargo-fuzz/README.md) | [Lab 04 - Cobertura, mutantes y clippy](../lab-04-cobertura-mutantes-y-clippy/README.md) → |

[↑ Semana 40](../../README.md)
