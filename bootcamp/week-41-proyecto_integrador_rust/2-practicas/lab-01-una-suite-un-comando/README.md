# Lab 01 - Una suite, un comando

## Objetivo

Construir `verify.sh`, el script que ejecuta la suite Rust completa: formato y clippy, los tests sin base de datos, PostgreSQL, todos los tests con cobertura, los doctests y un umbral del 85 % de líneas. Comprobar que falla cuando debe, y que sin `cargo llvm-cov clean` puede pasar cuando no debe.

## Tiempo estimado

75 minutos.

## Preparación

Qué hay en `starter/`, una biblioteca con su API y su almacén:

| Archivo | Qué es |
|---|---|
| `src/fees.rs` | `late_fee(days)`: 50 por día, hasta 1000. Un doctest y tres unitarios en `#[cfg(test)] mod tests` |
| `src/api.rs` | `router(store)`: `GET /fees?days=N` y `POST /books/{isbn}/lend`. El almacén es un trait, `BookStore` |
| `src/store.rs` | `PgStore`: los libros en PostgreSQL con sqlx |
| `tests/api.rs` | La API con `oneshot` y un stub del almacén ([semana 39](../../../week-39-async_apis_y_bd_en_rust/2-practicas/lab-02-rutas-axum-con-oneshot/README.md)). No necesita base de datos |
| `tests/store.rs` | `PgStore` con `#[sqlx::test]`: cada test recibe una base de datos nueva ([semana 39](../../../week-39-async_apis_y_bd_en_rust/2-practicas/lab-04-postgresql-con-sqlx-test-y-testcontainers/README.md)) |
| `compose.yaml`, `.cargo/config.toml` | El PostgreSQL de los tests y su `DATABASE_URL` |
| `migrations/0001_books.sql` | La tabla `books` |
| `verify.sh` | El script del lab. Solo trae fmt y clippy; los PASOS añaden el resto |

`verify.sh` ya tiene permiso de ejecución. Si al copiarlo lo pierdes (`permission denied`), devuélveselo con `chmod +x verify.sh`.

## Paso a paso

### Paso 0: lo que no se ejecuta

Con PostgreSQL **parado** (si lo tienes arrancado de otro lab, `docker compose down` en su carpeta):

1. Ejecuta todos los tests:

   ```bash
   cd starter
   cargo test
   # ...
   #      Running unittests src/lib.rs (target/debug/build/lab01/.../out/lab01-...)
   # running 3 tests
   # ...
   # test result: ok. 3 passed; 0 failed; ...
   #      Running tests/api.rs (target/debug/build/lab01/.../out/api-...)
   # running 4 tests
   # ...
   # test result: ok. 4 passed; 0 failed; ...
   #      Running tests/store.rs (target/debug/build/lab01/.../out/store-...)
   # running 3 tests
   # test gets_what_was_added ... FAILED
   # test lends_until_no_copies ... FAILED
   # test unknown_book ... FAILED
   # ...
   # failed to connect to setup test database: PoolTimedOut
   # ...
   # test result: FAILED. 0 passed; 3 failed; 0 ignored; 0 measured; 0 filtered out; finished in 30.00s
   # error: test failed, to rerun pass `--test store`
   ```

   Tres cosas:

   - Los tests de `store` tardan **30 segundos** en fallar: sqlx espera a que se agote el tiempo del pool.
   - Falta el bloque `Doc-tests lab01`. `cargo test` se para en el primer binario que falla; el doctest de `late_fee` no se ha ejecutado.
   - `api` pasa: está en `tests/`, pero no necesita PostgreSQL. En Rust, «test de integración» quiere decir «desde fuera del paquete», no «con base de datos».

2. Repite con `--no-fail-fast`:

   ```bash
   cargo test --no-fail-fast
   # ...
   # all doctests ran in 0.30s; merged doctests compilation took 0.29s
   # error: 1 target failed:
   #     `--test store`
   ```

   Ahora llega a los doctests y resume al final qué binario falló.

3. Lista los tests que ve nextest:

   ```bash
   cargo nextest list
   # lab01 fees::tests::capped_at_the_maximum
   # lab01 fees::tests::fifty_per_day
   # lab01 fees::tests::no_fee_on_time
   # lab01::api fee_for_three_days
   # lab01::api lends_a_copy
   # lab01::api no_copies_is_409
   # lab01::api unknown_book_is_404
   # lab01::store gets_what_was_added
   # lab01::store lends_until_no_copies
   # lab01::store unknown_book
   ```

   Cada línea empieza por el binario: `lab01` son los unitarios de `src/`, `lab01::api` y `lab01::store` los archivos de `tests/`. El doctest no aparece: nextest no ejecuta doctests ([semana 40](../../../week-40-calidad_avanzada_en_rust/1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#2-cargo-nextest)).

Ni `cargo test` ni `cargo nextest run` solos sirven como «la suite pasa». Hace falta un comando que nadie olvide.

### Paso 1: el análisis y los tests sin base de datos (PASO 1)

1. Ejecuta el script tal como viene:

   ```bash
   ./verify.sh
   # == formato y clippy
   #     Checking axum v0.8.9
   # ...
   #     Finished `dev` profile [unoptimized + debuginfo] target(s) in 14.01s
   ```

   `cargo fmt --check` no imprime nada si el formato es correcto. `cargo clippy --all-targets -- -D warnings` revisa también `tests/` ([teoría 01](../../1-teoria/01-la-suite-rust-completa.md#un-comando-verifysh)).

2. En `verify.sh`, descomenta las líneas del bloque `# PASO 1` (borra el `# ` del principio de cada una; en VS Code, selecciónalas y pulsa `Ctrl+/`). Deja la línea `# PASO 1: ...` comentada.

3. Ejecuta:

   ```bash
   ./verify.sh
   # == formato y clippy
   #     Finished `dev` profile [unoptimized + debuginfo] target(s) in 0.10s
   # == sin base de datos
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.15s
   # ────────────
   #  Nextest run ID ... with nextest profile: default
   #     Starting 7 tests across 2 binaries (1 binary skipped)
   #         PASS [   0.005s] (1/7) lab01 fees::tests::fifty_per_day
   # ...
   # ────────────
   #      Summary [   0.010s] 7 tests run: 7 passed, 0 skipped
   ```

   `-E 'not binary(store)'` es un **filtro** de nextest: todos los tests menos los del binario `store`, el de `tests/store.rs`. `1 binary skipped` es ese binario. Con PostgreSQL parado, esta fase pasa en menos de un segundo.

### Paso 2: PostgreSQL, todos los tests con cobertura y los doctests (PASO 2)

1. Descomenta el bloque `# PASO 2`.

2. Ejecuta:

   ```bash
   ./verify.sh
   # ...
   # == PostgreSQL
   #  Network starter_default Creating
   # ...
   #  Container starter-db-1 Healthy
   # == todos los tests, con cobertura
   # info: cargo-llvm-cov currently setting cfg(coverage); you can opt-out it by passing --no-cfg-coverage
   # ...
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 18.03s
   # ...
   #     Starting 10 tests across 3 binaries
   # ...
   #      Summary [   0.649s] 10 tests run: 10 passed, 0 skipped
   # == doctests
   # ...
   # test src/fees.rs - fees::late_fee (line 10) ... ok
   #
   # test result: ok. 1 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   ```

   | Línea | Qué hace |
   |---|---|
   | `docker compose up -d --wait` | Arranca el PostgreSQL de `compose.yaml` y espera a que esté listo. Si ya lo estaba, no hace nada |
   | `cargo llvm-cov clean --workspace` | Borra los datos de cobertura de ejecuciones anteriores. No imprime nada. El Experimento 1 enseña por qué está |
   | `cargo llvm-cov nextest --no-report` | Todos los tests, ahora también los de `store`, con la instrumentación de cobertura. Guarda los datos sin escribir el informe |
   | `cargo test --doc` | El doctest, que nextest no ejecuta |

   Los 18 s son la primera compilación con instrumentación: cargo-llvm-cov compila en `target/llvm-cov-target`, aparte de `target/debug`. Las siguientes ejecuciones tardan segundos.

### Paso 3: el umbral (PASO 3)

1. Comprueba qué hace `--fail-under-lines` cuando no llega:

   ```bash
   cargo llvm-cov report --summary-only --fail-under-lines 99 > /dev/null; echo "exit=$?"
   # exit=1
   ```

   `$?` es el código con el que terminó el último comando: 0 si fue bien. Termina con 1, pero no escribe ningún mensaje: la tabla iba a `/dev/null` y no queda nada que diga por qué ha fallado.

2. Descomenta el bloque `# PASO 3`. `||` ejecuta el bloque `{ ... }` solo si el comando de la izquierda falla: escribe el motivo y termina con 1.

3. Ejecuta:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   # == umbral de cobertura de líneas (mínimo: 85 %)
   # Filename                      Regions    Missed Regions     Cover   Functions  Missed Functions  Executed       Lines      Missed Lines     Cover  ...
   # -------------------------------------------------------------------------------------------------------------------------------------------------
   # api.rs                             34                 2    94.12%           6                 0   100.00%          21                 1    95.24%  ...
   # fees.rs                            21                 0   100.00%           4                 0   100.00%          13                 0   100.00%  ...
   # store.rs                           27                 4    85.19%           7                 1    85.71%          35                 3    91.43%  ...
   # -------------------------------------------------------------------------------------------------------------------------------------------------
   # TOTAL                              82                 6    92.68%          17                 1    94.12%          69                 4    94.20%  ...
   # exit=0
   ```

   La cifra que compara el umbral es la columna `Lines` → `Cover` de `TOTAL`: 94.20 %.

### Paso 4: ver fallar el umbral

Un umbral que nunca has visto fallar puede no estar comprobando nada.

1. Imagina que alguien copia el filtro del PASO 1 en la fase de cobertura. En `verify.sh`, cambia la línea:

   ```bash
   cargo llvm-cov nextest --no-report
   ```

   por:

   ```bash
   cargo llvm-cov nextest --no-report -E 'not binary(store)'
   ```

2. Ejecuta:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   #     Starting 7 tests across 2 binaries (1 binary skipped)
   # ...
   # store.rs                           27                27     0.00%           7                 7     0.00%          35                35     0.00%  ...
   # -------------------------------------------------------------------------------------------------------------------------------------------------
   # TOTAL                              82                29    64.63%          17                 7    58.82%          69                36    47.83%  ...
   # FALLO: la cobertura de líneas no llega al 85 %
   # exit=1
   ```

   `store.rs` ya no lo ejecuta nadie: el total baja al 47.83 % y el script termina con 1 y el motivo.

3. Deja el cambio puesto para el Experimento 1.

## Experimentos

### Experimento 1: sin `cargo llvm-cov clean`

1. Con el cambio del Paso 4 todavía puesto, borra la línea `cargo llvm-cov clean --workspace` de `verify.sh`.

2. Deshaz un momento el cambio del Paso 4 (quita `-E 'not binary(store)'`), ejecuta `./verify.sh` una vez para que los tests de `store` dejen sus datos, y vuelve a poner el filtro.

3. Ejecuta:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   #     Starting 7 tests across 2 binaries (1 binary skipped)
   # ...
   # TOTAL                              82                 6    92.68%          17                 1    94.12%          69                 4    94.20%  ...
   # exit=0
   ```

   Solo se han ejecutado 7 tests, ninguno de `store`, y la cobertura dice 94.20 %, con `store.rs` cubierto. Con `--no-report`, cargo-llvm-cov **suma** los datos de cada ejecución a los que ya había: el informe cuenta lo que cubrieron los tests de `store` en la ejecución anterior. El umbral pasa sin que se haya ejecutado nada que lo justifique.

4. Vuelve a poner `cargo llvm-cov clean --workspace` y quita el filtro de la fase de cobertura. Ejecuta `./verify.sh`: `exit=0` con los 10 tests.

`--no-report` existe para juntar en un informe varias ejecuciones distintas (por ejemplo, nextest y después un binario de ejemplo). Por eso no borra nada: borrar es tu trabajo.

### Experimento 2: clippy sin `--all-targets`

1. En `tests/api.rs`, en `fee_for_three_days`, cambia `assert_eq!(status, StatusCode::OK);` por:

   ```rust
   assert_eq!(status.is_success(), true);
   ```

2. Ejecuta clippy sin `--all-targets`:

   ```bash
   cargo clippy -- -D warnings; echo "exit=$?"
   #     Checking lab01 v0.1.0 (.../starter)
   #     Finished `dev` profile [unoptimized + debuginfo] target(s) in 6.36s
   # exit=0
   ```

3. Y con `--all-targets`:

   ```bash
   cargo clippy --all-targets -- -D warnings
   # error: used `assert_eq!` with a literal bool
   #   --> tests/api.rs:34:5
   #    |
   # 34 |     assert_eq!(status.is_success(), true);
   #    |     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
   # ...
   #    = note: `-D clippy::bool-assert-comparison` implied by `-D warnings`
   # ...
   ```

   Sin `--all-targets`, clippy solo revisa la biblioteca: los tests, que son la mitad del código de una suite, quedan fuera.

4. Deshaz el cambio.

## Comprobación final

```bash
./verify.sh; echo "exit=$?"
# == formato y clippy
# == sin base de datos
# ...
#      Summary [   0.010s] 7 tests run: 7 passed, 0 skipped
# == PostgreSQL
# ...
# == todos los tests, con cobertura
# ...
#      Summary [   0.391s] 10 tests run: 10 passed, 0 skipped
# == doctests
# ...
# == umbral de cobertura de líneas (mínimo: 85 %)
# ...
# TOTAL                              82                 6    92.68%          17                 1    94.12%          69                 4    94.20%  ...
# exit=0
```

Al terminar, para PostgreSQL con `docker compose down`.

## Preguntas de cierre

1. ¿Por qué `cargo test` en rojo con PostgreSQL parado no te dice si los doctests pasan?
2. ¿Qué diferencia hay en Rust entre un test de integración y un test que necesita base de datos? ¿Cómo separa cada fase `verify.sh`?
3. ¿Qué tres razones distintas pueden hacer que `verify.sh` termine con 1 después de arrancar PostgreSQL?
4. Compara el Experimento 1 con la trampa de `tee` sin `pipefail` de la [semana 36](../../../week-36-proyecto_integrador_go/2-practicas/lab-01-una-suite-un-comando/README.md#experimento-1-tee-sin-pipefail): ¿qué tienen en común?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Los datos del reporte](../lab-02-datos-del-reporte/README.md) → |

[↑ Semana 41](../../README.md)
