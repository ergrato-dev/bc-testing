# Lab 02 - Los datos del reporte

## Objetivo

Sacar de las salidas de nextest y cargo-llvm-cov los datos del reporte de testing: cuántos tests tiene cada capa y cuánto tarda, cuáles son los más lentos y por qué, y la cobertura de cada archivo con y sin PostgreSQL. Con ellos, escribir `testing-report-rust.md`.

## Tiempo estimado

75 minutos.

## Preparación

El starter es la biblioteca del [lab 01](../lab-01-una-suite-un-comando/README.md) con `verify.sh` terminado y la plantilla `testing-report-rust.md`. Con Docker en marcha:

```bash
cd starter
./verify.sh
# ...
# == umbral de cobertura de líneas (mínimo: 85 %)
# ...
```

`verify.sh` deja PostgreSQL arrancado y los datos de cobertura que usa el Paso 4.

## Paso a paso

### Paso 1: el tiempo de cada capa

`time` delante de un comando mide cuánto tarda; la línea que interesa es `real`, el tiempo de reloj. Los tres comandos, con todo compilado por `verify.sh`:

```bash
time cargo nextest run -E 'not binary(store)'
# ...
# real	0m0.270s

time cargo nextest run -E 'binary(store)'
# ...
#     Starting 3 tests across 1 binary (2 binaries skipped)
# ...
#      Summary [   0.504s] 3 tests run: 3 passed, 0 skipped
# real	0m0.691s

time cargo test --doc
# ...
# all doctests ran in 0.30s; merged doctests compilation took 0.30s
# real	0m0.471s
```

En zsh, `time` imprime una sola línea con el formato `... 0.279 total`: el último número es el mismo `real`.

Fíjate en la última línea de los doctests: `merged doctests compilation took 0.30s`. Los doctests se compilan **en cada ejecución**, aunque no hayas cambiado nada. Con muchos doctests, esa fase crece.

### Paso 2: cuántos tests tiene cada capa

1. Cuenta los tests de cada binario. `awk '{print $1}'` se queda con la primera palabra de cada línea (el binario), y `sort | uniq -c` cuenta cuántas veces aparece cada una:

   ```bash
   cargo nextest list | awk '{print $1}' | sort | uniq -c
   #       3 lab02
   #       4 lab02::api
   #       3 lab02::store
   ```

   | Binario | Capa |
   |---|---|
   | `lab02` | Unitarios de `src/` (`#[cfg(test)] mod tests`) |
   | `lab02::api` | Integración sin base de datos: `tests/api.rs` |
   | `lab02::store` | Integración con PostgreSQL: `tests/store.rs` |

2. Comprueba la primera fila con otro filtro. `kind(lib)` son los tests de la biblioteca, los de `src/`:

   ```bash
   cargo nextest list -E 'kind(lib)'
   # lab02 fees::tests::capped_at_the_maximum
   # lab02 fees::tests::fifty_per_day
   # lab02 fees::tests::no_fee_on_time
   ```

3. Los doctests no salen en nextest; cuéntalos con `cargo test`:

   ```bash
   cargo test --doc 2>&1 | grep "test result"
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   ```

   La suite tiene 3 unitarios, 4 tests de integración sin base de datos, 3 con PostgreSQL y 1 doctest.

### Paso 3: los tests más lentos

1. Guarda la salida de una ejecución completa. nextest escribe **todo** en la salida de errores (stderr), así que hace falta `2>&1` para que llegue al archivo:

   ```bash
   cargo nextest run > all.log 2>&1
   ```

2. Ordena los tests por su tiempo. `sort -t'[' -k2 -rn` parte cada línea por el `[` y ordena por lo que viene después, como número y de mayor a menor:

   ```bash
   grep -E '^ +PASS' all.log | sort -t'[' -k2 -rn | head -3
   #         PASS [   0.392s] ( 8/10) lab02::store gets_what_was_added
   #         PASS [   0.389s] (10/10) lab02::store unknown_book
   #         PASS [   0.388s] ( 9/10) lab02::store lends_until_no_copies
   grep Summary all.log
   #      Summary [   0.397s] 10 tests run: 10 passed, 0 skipped
   ```

   Los tres de `store` tardan unos 0.39 s; los demás, menos de 0.015 s.

3. ¿Por qué tarda lo mismo `unknown_book`, que hace dos consultas, que `lends_until_no_copies`, que hace cinco? Cada `#[sqlx::test]` crea una base de datos nueva, aplica las migraciones y la borra al terminar ([semana 39](../../../week-39-async_apis_y_bd_en_rust/2-practicas/lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md)). Ese coste es fijo por test: no es la consulta, es la base.

4. ¿Y por qué la ejecución entera tarda 0.397 s, si los tres suman 1.17 s? Repite con un solo test a la vez. `-j 1` le dice a nextest cuántos tests ejecuta en paralelo:

   ```bash
   cargo nextest run -j 1 2>&1 | grep Summary
   #      Summary [   0.933s] 10 tests run: 10 passed, 0 skipped
   ```

   nextest ejecuta cada test en su propio proceso y varios a la vez: los tres de `store` se solapan. Al añadir tests con `#[sqlx::test]`, la fase crece mucho menos que la suma de sus tiempos, mientras haya núcleos libres.

### Paso 4: la cobertura de cada archivo

1. La tabla de `cargo llvm-cov report` ya viene por archivo. Para el reporte interesan el archivo y las tres columnas de líneas (campos 1, 8, 9 y 10). `/^[a-zA-Z]/ && !/^Filename/` se queda con las filas de archivos y `TOTAL`, sin la cabecera ni las líneas de guiones:

   ```bash
   cargo llvm-cov report --summary-only | awk '/^[a-zA-Z]/ && !/^Filename/ {print $1, $8, $9, $10}'
   # api.rs 21 1 95.24%
   # fees.rs 13 0 100.00%
   # store.rs 35 3 91.43%
   # TOTAL 69 4 94.20%
   ```

   Archivo, líneas, líneas sin cubrir y cobertura.

2. Qué líneas faltan:

   ```bash
   cargo llvm-cov report --show-missing-lines
   # ...
   # Uncovered Lines:
   # .../src/api.rs: 52
   # .../src/store.rs: 49, 56
   ```

   Las tres son el error del almacén: la línea que responde 500 en la API y las dos que convierten un error de sqlx en `LendError::Store`. Probarlas pide un PostgreSQL que falle a mitad de test, o un stub que devuelva `Err(LendError::Store(...))` para la de la API.

3. La cobertura de la capa sin base de datos. Sin `--no-report`, cargo-llvm-cov borra los datos anteriores, ejecuta y escribe el informe:

   ```bash
   cargo llvm-cov nextest -E 'not binary(store)' --summary-only 2>/dev/null | awk '/^[a-zA-Z]/ && !/^Filename/ {print $1, $8, $9, $10}'
   # api.rs 21 1 95.24%
   # fees.rs 13 0 100.00%
   # store.rs 35 35 0.00%
   # TOTAL 69 36 47.83%
   ```

   `store.rs` solo lo cubren los tests con PostgreSQL. Sin ellos, la biblioteca se queda en el 47.83 %.

4. Este último comando ha sustituido los datos de cobertura. Ejecuta `./verify.sh` antes de seguir, para volver a tener los de la suite entera.

### Paso 5: escribir el reporte

Rellena `testing-report-rust.md` con los datos de los pasos anteriores:

1. **Suite por capas:** una fila por capa (unitarios, integración sin base de datos, integración con PostgreSQL, doctests), con dónde están, los tests del Paso 2, el comando y el tiempo del Paso 1.
2. **Cobertura:** la tabla del Paso 4 con una fila de total y una columna con la cobertura sin base de datos.
3. **Tests más lentos:** los tres del Paso 3 y, sobre todo, a dónde va su tiempo y por qué la fase tarda menos que la suma.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba. Pistas en esta biblioteca: ¿hay algún test que pase por la API y por PostgreSQL a la vez? ¿Qué pasa si dos peticiones prestan la última copia al mismo tiempo? ¿Qué responde la API si PostgreSQL se cae a mitad de una petición?
5. **Próximos pasos:** los tests que añadirías primero, en orden.

No dejes ningún `TODO`:

```bash
grep -c TODO testing-report-rust.md
# 0
```

## Experimentos

### Experimento 1: la compilación con instrumentación

1. Borra todo lo que guarda cargo-llvm-cov, también las dependencias compiladas (`clean` sin `--workspace`):

   ```bash
   cargo llvm-cov clean
   ```

2. Ejecuta la fase de cobertura dos veces seguidas:

   ```bash
   time cargo llvm-cov nextest --no-report
   # ...
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 17.14s
   # ...
   #      Summary [   0.539s] 10 tests run: 10 passed, 0 skipped
   # real	0m18.051s

   time cargo llvm-cov nextest --no-report
   # ...
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.08s
   # ...
   #      Summary [   0.415s] 10 tests run: 10 passed, 0 skipped
   # real	0m0.791s
   ```

3. Mira dónde está esa compilación:

   ```bash
   du -sh target/debug target/llvm-cov-target
   # 1.1G	target/debug
   # 777M	target/llvm-cov-target
   ```

   cargo-llvm-cov compila todo otra vez con instrumentación, en su propia carpeta. La primera ejecución tarda 18 s; los tests, medio segundo. En un reporte, «la suite tarda 18 s» y «la suite tarda 0.7 s» son verdad a la vez: di siempre si el tiempo incluye compilar. En CI, donde cada ejecución empieza desde cero si no guardas `target/` en caché, el que cuenta es el primero.

## Comprobación final

```bash
./verify.sh > /dev/null 2>&1; echo "exit=$?"
# exit=0
grep -c TODO testing-report-rust.md
# 0
grep -cE '^## (Suite por capas|Cobertura|Tests más lentos|Riesgos y huecos|Próximos pasos)$' testing-report-rust.md
# 5
```

Al terminar, para PostgreSQL con `docker compose down`.

## Preguntas de cierre

1. ¿Por qué un `#[sqlx::test]` que hace dos consultas tarda lo mismo que uno que hace cinco?
2. ¿Por qué la fase con PostgreSQL tarda menos que la suma de sus tests? ¿Cuándo dejaría de ser así?
3. Compara la tabla del Paso 4 con el `awk` de la [semana 36](../../../week-36-proyecto_integrador_go/2-practicas/lab-02-datos-del-reporte/README.md#paso-4-la-cobertura-de-cada-paquete): ¿qué te da hecho cargo-llvm-cov que en Go tenías que calcular?
4. ¿Qué riesgo de tu reporte no se arreglaría subiendo la cobertura?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Una suite, un comando](../lab-01-una-suite-un-comando/README.md) | [Proyecto — Proyecto Integrador - Suite Rust completa del dominio](../../3-proyecto/README.md) → |

[↑ Semana 41](../../README.md)
