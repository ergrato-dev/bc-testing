# Proyecto Integrador - Suite Rust completa del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Cerrar la etapa de Rust con una sola suite en `rust/` de tu repositorio:

- unitarios en `src/` y doctests; tests de integración con mockall, rstest, `#[tokio::test]` y axum con `oneshot`; PostgreSQL con `#[sqlx::test]`;
- las propiedades de proptest y el benchmark de criterion de la semana 40;
- un solo comando, `verify.sh`, que lo ejecuta todo y falla si la cobertura de líneas baja del 85 %;
- un reporte `testing-report-rust.md` con datos reales.

## Dónde va

En tu **[proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)**, el repositorio `qa-<nombre>-<apellido>`, dentro de `rust/`. Partes de tu entrega de la [semana 40](../../week-40-calidad_avanzada_en_rust/3-proyecto/README.md).

Los ejemplos usan el dominio del museo, el paquete `museum`. Tus archivos y tus cifras serán distintos.

## Tiempo

| Parte | Tiempo |
|---|---|
| Desarrollo: pasos 1 a 5 | 6.5 h |
| Presentación: paso 6 | 0.5 h |

## Paso a paso

### Paso 1: inventario y rama

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-41
   ```

2. Copia el starter sin sobrescribir nada:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-41-proyecto_integrador_rust/3-proyecto/starter/. .
   ```

   | Archivo | Qué es |
   |---|---|
   | `rust/verify.sh` | El script del [lab 01](../2-practicas/lab-01-una-suite-un-comando/README.md) con fmt, clippy y la fase sin base de datos, y un `TODO` por cada fase que falta |
   | `rust/testing-report-rust.md` | La plantilla del reporte, con `TODO` en cada sección |

   Si `cp` no conserva el permiso de ejecución, `chmod +x rust/verify.sh`.

3. Haz el inventario de tus tests. Para cada archivo, anota qué piezas de la etapa contiene y si necesita PostgreSQL:

   ```bash
   cd rust
   cargo nextest list | awk '{print $1}' | sort | uniq -c
   #      29 museum
   #      11 museum::api
   #       7 museum::lending
   #       3 museum::museum
   #       1 museum::notice
   #       8 museum::pg
   #       1 museum::pg_properties
   #       7 museum::properties
   #       5 museum::registry
   ```

   En el museo:

   | Archivo | Piezas | ¿PostgreSQL? |
   |---|---|---|
   | `src/fees.rs`, `src/piece.rs` | Unitarios en `#[cfg(test)]`, doctests | No |
   | `tests/api.rs` | axum con `oneshot`, `#[tokio::test]`, fakes, mocks de mockall, rstest | No |
   | `tests/lending.rs`, `tests/common/mod.rs` | Stubs, fakes, mocks de mockall (`mock!`), rstest | No |
   | `tests/registry.rs` | `#[tokio::test]` y wiremock | No |
   | `tests/museum.rs`, `tests/notice.rs` | La API pública desde fuera, snapshot de insta | No |
   | `tests/properties.rs` | Propiedades de proptest | No |
   | `tests/pg.rs` | `#[sqlx::test]` | Sí |
   | `tests/pg_properties.rs` | Propiedad de proptest contra PostgreSQL | Sí |
   | `benches/hot_path.rs` | Benchmark de criterion | No |

   Si te falta alguna pieza (por ejemplo, el mock de mockall o la propiedad de proptest), vuelve a la semana donde se pedía: la verificación las busca.

4. Si tus archivos con PostgreSQL no empiezan por `pg`, renómbralos (`git mv tests/store.rs tests/pg_store.rs`) o cambia la expresión regular de `verify.sh`. El criterio tiene que ser uno y estar escrito ([teoría 01](../1-teoria/01-la-suite-rust-completa.md#qué-tests-necesitan-postgresql)).

### Paso 2: `verify.sh` completo

Referencia: [lab 01](../2-practicas/lab-01-una-suite-un-comando/README.md) y [teoría 01](../1-teoria/01-la-suite-rust-completa.md#un-comando-verifysh).

1. Sustituye el `TODO` de PostgreSQL. `compose.yaml` está en la raíz del repositorio, una carpeta por encima del script:

   ```bash
   echo "== PostgreSQL"
   docker compose -f ../compose.yaml up -d --wait
   ```

2. Sustituye el `TODO` de la cobertura:

   ```bash
   echo "== todos los tests, con cobertura"
   cargo llvm-cov clean --workspace
   cargo llvm-cov nextest --no-report
   ```

3. Sustituye el `TODO` de los doctests y los benchmarks:

   ```bash
   echo "== doctests"
   cargo test --doc

   echo "== benchmarks (una iteración)"
   cargo test --benches
   ```

   `cargo test --benches` ejecuta cada benchmark una vez, sin medir: comprueba que siguen funcionando ([semana 40](../../week-40-calidad_avanzada_en_rust/2-practicas/lab-03-benchmarks-con-criterion/README.md#experimento-2-los-benchmarks-como-test)). También repite los unitarios de `src/`, porque la biblioteca es un target de benchmarks; tardan milisegundos.

4. Con Docker en marcha y PostgreSQL parado (`docker compose down` en la raíz del repositorio), ejecuta desde `rust/`:

   ```bash
   ./verify.sh
   # == formato y clippy
   # ...
   #     Finished `dev` profile [unoptimized + debuginfo] target(s) in 21.24s
   # == sin base de datos
   # ...
   #     Starting 63 tests across 7 binaries (2 binaries skipped)
   # ...
   #      Summary [   0.270s] 63 tests run: 63 passed, 0 skipped
   # == PostgreSQL
   # ...
   # == todos los tests, con cobertura
   # ...
   #     Starting 72 tests across 9 binaries
   # ...
   #      Summary [   8.263s] 72 tests run: 72 passed, 0 skipped
   # == doctests
   # ...
   # test result: ok. 3 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   # == benchmarks (una iteración)
   # ...
   # Testing validate_piece/10
   # Success
   # Testing validate_piece/120
   # Success
   # Testing validate_piece/10000
   # Success
   ```

   `2 binaries skipped` son `pg` y `pg_properties`. Las semillas de `proptest-regressions` y del archivo `.proptest-regressions` se prueban en las dos fases de tests, como los demás casos.

### Paso 3: el umbral

1. Sustituye el último `TODO` por el umbral del lab 01:

   ```bash
   echo "== umbral de cobertura de líneas (mínimo: 85 %)"
   cargo llvm-cov report --summary-only --fail-under-lines 85 || {
       echo "FALLO: la cobertura de líneas no llega al 85 %"
       exit 1
   }
   ```

2. Ejecuta y comprueba el código de salida:

   ```bash
   ./verify.sh; echo "exit=$?"
   # ...
   # == umbral de cobertura de líneas (mínimo: 85 %)
   # ...
   # TOTAL                             415                17    95.90%          65                 5    92.31%         343                 7    97.96%  ...
   # exit=0
   ```

3. Comprueba cuánto cubre solo la fase sin base de datos:

   ```bash
   cargo llvm-cov nextest -E 'not binary(/^pg/)' --summary-only 2>/dev/null | awk '/^[a-zA-Z]/ && !/^Filename/ {print $1, $8, $9, $10}'
   # api.rs 51 1 98.04%
   # fees.rs 71 0 100.00%
   # lending.rs 52 0 100.00%
   # pg.rs 51 51 0.00%
   # piece.rs 95 0 100.00%
   # registry.rs 23 1 95.65%
   # TOTAL 343 53 84.55%
   ```

   Sin los tests con PostgreSQL, el museo se quedaría en el 84.55 %, por debajo del umbral: `pg.rs` solo lo cubren ellos. Este comando sustituye los datos de cobertura; vuelve a ejecutar `./verify.sh` después.

### Paso 4: al menos el 85 %

1. Si `verify.sh` termina con `FALLO`, busca lo que falta con las herramientas del [lab 02](../2-practicas/lab-02-datos-del-reporte/README.md#paso-4-la-cobertura-de-cada-archivo):

   ```bash
   cargo llvm-cov report --show-missing-lines
   cargo llvm-cov report --html     # target/llvm-cov/html/index.html
   ```

2. Escribe el test que cubre cada línea en la capa que corresponda a lo que necesita: en `src/` si prueba una función privada; en `tests/` sin base de datos si basta un fake, un stub o un mock; en un archivo `pg*` si necesita PostgreSQL. No bajes el 85 de `verify.sh`: la verificación lo busca.

3. Un 100 % no significa que no haya errores: `cargo llvm-cov` mide las líneas que se ejecutan, no las comprobaciones que faltan. Los mutantes que analizaste en `calidad.md` en la semana 40 lo demuestran. Apunta lo que encuentres para el reporte.

### Paso 5: el reporte de testing

Rellena `rust/testing-report-rust.md` con los datos de tu suite, como en el [lab 02](../2-practicas/lab-02-datos-del-reporte/README.md#paso-5-escribir-el-reporte):

1. **Suite por capas:** una fila por capa (unitarios de `src/`, integración sin base de datos, integración con PostgreSQL, doctests, benchmarks), con dónde están, cuántos tests tiene (`cargo nextest list`, `cargo test --doc`), el comando y el tiempo de `time`. Di cuánto tarda `verify.sh` con todo compilado y desde cero.
2. **Cobertura:** la tabla por archivo de `cargo llvm-cov report`, con el total, y una columna con la cobertura sin base de datos del Paso 3.
3. **Tests más lentos:** los tres primeros de `grep -E '^ +PASS' all.log | sort -t'[' -k2 -rn | head -3` (recuerda el `2>&1` al guardar `all.log`) y a dónde va el tiempo: la base de datos de cada `#[sqlx::test]`, los retrasos de wiremock, la compilación.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba y que podrían fallar en producción. Busca operaciones de varios pasos sin transacción, errores de una dependencia que solo se prueban con un mock, y caminos que no pasan nunca por la API y la base de datos a la vez.
5. **Próximos pasos:** los tests que añadirías primero, en orden.

Puedes enlazar `calidad.md` de la semana 40 para no repetir proptest, criterion y cargo-mutants. No dejes ningún `TODO`: la verificación los busca.

### Paso 6: verificar, entregar y presentar

1. Ejecuta la verificación desde la carpeta del bootcamp, con Docker en marcha:

   ```bash
   PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 41
   ```

2. Haz commit y push de la rama y abre un pull request `week-41` → `main`.
3. Presenta tu suite en 5 minutos: las fases de `verify.sh` y qué capa va en cada una, la cifra de cobertura y qué capa la sostiene, dónde se va el tiempo de la suite y el riesgo más importante del reporte.

## Ejemplos de adaptación por dominio

| Dominio | Sin base de datos | Con PostgreSQL (`tests/pg*.rs`) | Riesgo típico |
|---|---|---|---|
| Museo | Seguro y recargos de un préstamo, API con fakes, registro con wiremock | Piezas y préstamos con `#[sqlx::test]` | El aviso falla después de abrir el préstamo |
| Planetario | Precio de una entrada con descuentos | Funciones y entradas | Dos ventas de la última butaca a la vez |
| Acuario | Capacidad de un tanque | Tanques y ejemplares | Un traslado que mueve el ejemplar y no actualiza la capacidad |

## ✅ Requisitos verificables

`./scripts/verificar-semana.sh 41` comprueba lo siguiente ([checks.json](../checks.json)):

| Comprobación | Cómo se verifica |
|---|---|
| `verify.sh` está completo | Tiene `set -euo pipefail`, `cargo fmt --check`, `cargo clippy --all-targets -- -D warnings`, `cargo nextest run -E`, `cargo llvm-cov clean --workspace`, `cargo llvm-cov nextest --no-report`, `cargo test --doc`, `cargo test --benches`, `--fail-under-lines 85` y ningún `TODO` |
| `verify.sh` pasa | `bash rust/verify.sh` termina con 0. Necesita Docker, cargo-nextest y cargo-llvm-cov |
| La cobertura llega al 85 % | `cargo llvm-cov report --fail-under-lines 85` sobre los datos que deja `verify.sh` |
| La suite tiene todas las piezas | Hay `#[cfg(test)]` y un doctest en `src/`, `automock` o `mock!` de mockall, `#[rstest`, `#[tokio::test`, `.oneshot(`, `#[sqlx::test`, `proptest!` y `criterion_main!` en `benches/` |
| `testing-report-rust.md` tiene sus cinco secciones y ningún `TODO` | Las cinco cabeceras `##` del starter están y no queda ningún `TODO` |

> La `solution/` del proyecto no se publica en el repositorio del bootcamp.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Los datos del reporte](../2-practicas/lab-02-datos-del-reporte/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 41](../README.md)
