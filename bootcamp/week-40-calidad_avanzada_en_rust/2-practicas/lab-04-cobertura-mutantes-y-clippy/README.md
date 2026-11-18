# Lab 04 - Cobertura, mutantes y clippy

## Objetivo

Pasar las cuatro herramientas de la [teoría 04](../../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md) por un módulo de recargos: ejecutar la suite con nextest, medir su cobertura con cargo-llvm-cov, encontrar con cargo-mutants lo que la cobertura no ve (incluido un mutante que ningún test puede matar) y limpiar con clippy dos avisos, uno de los cuales hace desaparecer ese mutante.

## Tiempo estimado

70 minutos.

## Preparación

Necesitas las herramientas de la [teoría 04](../../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#instalar). Comprueba que están:

```bash
cargo llvm-cov --version
# cargo-llvm-cov 0.9.1
cargo nextest --version
# cargo-nextest 0.9.152 (26ebeda79 2026-11-13)
cargo mutants --version
# cargo-mutants 27.1.0
```

Después:

```bash
cd starter
cargo test
# ...
# running 2 tests
# test tests::public_pays_per_chargeable_day ... ok
# test tests::staff_pays_nothing ... ok
# ...
#    Doc-tests lab04
#
# running 1 test
# test src/lib.rs - receipt (line 38) ... ok
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab04`, sin dependencias |
| `Cargo.lock` | Generado por cargo |
| `src/lib.rs` | `chargeable_days` (resta 2 días de gracia), `late_fee` (el personal no paga, los socios pagan la mitad) y `receipt`, con un doctest. Dos tests y, al final, dos bloques comentados por PASOS |

## Paso a paso

### Paso 1: la suite con nextest

1. Ejecuta:

   ```bash
   cargo nextest run
   #     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.11s
   # ────────────
   #  Nextest run ID f366bbb2-ece7-4681-bfb5-9972fe2a0dbe with nextest profile: default
   #     Starting 2 tests across 1 binary
   #         PASS [   0.005s] (1/2) lab04 tests::staff_pays_nothing
   #         PASS [   0.005s] (2/2) lab04 tests::public_pays_per_chargeable_day
   # ────────────
   #      Summary [   0.005s] 2 tests run: 2 passed, 0 skipped
   ```

   Dos tests, no tres: nextest no ejecuta los doctests. El de `receipt` se ejecuta aparte:

   ```bash
   cargo test --doc
   # ...
   # test src/lib.rs - receipt (line 38) ... ok
   ```

   Si cambias `cargo test` por `cargo nextest run` en un script, añade `cargo test --doc` o perderás los doctests sin que nada avise.

### Paso 2: la cobertura

1. Ejecuta:

   ```bash
   cargo llvm-cov
   # ...
   # test result: ok. 2 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   #
   # Filename          Regions    Missed Regions     Cover   Functions  Missed Functions  Executed       Lines      Missed Lines     Cover    Branches   Missed Branches     Cover
   # ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   # .../src/lib.rs         34                 9    73.53%           5                 1    80.00%          22                 4    81.82%           0                 0         -
   # ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   # TOTAL                  34                 9    73.53%           5                 1    80.00%          22                 4    81.82%           0                 0         -
   ```

   La primera vez compila todo con instrumentación. La tabla es ancha: amplía la terminal o lee las columnas `Lines` (22 líneas, 4 sin ejecutar, 81.82 %) y `Functions` (una de las 5 sin ejecutar).

2. Mira qué falta:

   ```bash
   cargo llvm-cov --text
   # ...
   #    19|      2|pub fn chargeable_days(days: u32) -> u32 {
   #    20|      2|    if days > GRACE { days - GRACE } else { 0 }
   #                                                           ^0
   #    21|      2|}
   # ...
   #    30|      1|    if category == Category::Member {
   #    31|      0|        return fee / 2;
   #    32|      1|    }
   # ...
   #    43|      0|pub fn receipt(member: &str, days: u32, category: Category) -> String {
   #    44|      0|    format!("{}: {} días, {}", member, days, late_fee(days, category))
   #    45|      0|}
   ```

   | Qué | Por qué |
   |---|---|
   | `^0` debajo de `else { 0 }` | La línea se ejecutó, pero nunca por la rama `else`: ningún test usa 2 días o menos |
   | Línea 31 con `0` | Ningún test calcula el recargo de un socio |
   | `receipt` con `0` | Tiene un doctest que pasa, pero cargo-llvm-cov no cuenta los doctests en estable |

3. Genera el informe HTML y ábrelo en el navegador:

   ```bash
   cargo llvm-cov --html
   #     Finished report saved to .../starter/target/llvm-cov/html
   ```

   Abre `target/llvm-cov/html/index.html`: el mismo informe, con las líneas sin ejecutar en rojo.

### Paso 3: los mutantes (PASO 3)

1. Mira qué va a cambiar cargo-mutants:

   ```bash
   cargo mutants --list
   # src/lib.rs:20:5: replace chargeable_days -> u32 with 0
   # src/lib.rs:20:5: replace chargeable_days -> u32 with 1
   # src/lib.rs:20:13: replace > with == in chargeable_days
   # src/lib.rs:20:13: replace > with < in chargeable_days
   # src/lib.rs:20:13: replace > with >= in chargeable_days
   # src/lib.rs:20:28: replace - with + in chargeable_days
   # src/lib.rs:20:28: replace - with / in chargeable_days
   # src/lib.rs:26:5: replace late_fee -> u32 with 0
   # src/lib.rs:26:5: replace late_fee -> u32 with 1
   # src/lib.rs:27:17: replace == with != in late_fee
   # src/lib.rs:30:17: replace == with != in late_fee
   # src/lib.rs:31:20: replace / with % in late_fee
   # src/lib.rs:31:20: replace / with * in late_fee
   # src/lib.rs:44:5: replace receipt -> String with String::new()
   # src/lib.rs:44:5: replace receipt -> String with "xyzzy".into()
   ```

   Quince versiones del código con un cambio cada una: operadores cambiados y funciones que devuelven un valor fijo.

2. Ejecútalos:

   ```bash
   cargo mutants
   # Found 15 mutants to test
   # ok       Unmutated baseline in 0s build + 0s test
   #  INFO Auto-set test timeout to 20s
   # MISSED   src/lib.rs:20:13: replace > with >= in chargeable_days in 0s build + 0s test
   # MISSED   src/lib.rs:31:20: replace / with % in late_fee in 0s build + 0s test
   # MISSED   src/lib.rs:31:20: replace / with * in late_fee in 0s build + 0s test
   # 15 mutants tested in 4s: 3 missed, 12 caught
   ```

   Tres sobreviven. Los de la línea 31 eran de esperar: la cobertura ya decía que nadie la ejecuta. El de la línea 20 no: esa línea se ejecuta, y aun así cambiar `>` por `>=` no rompe ningún test.

   Fíjate en `receipt`: la cobertura la daba por no ejecutada, pero sus dos mutantes están entre los `caught`. cargo-mutants ejecuta `cargo test`, que incluye el doctest.

3. Los resultados quedan en `mutants.out/`:

   ```bash
   cat mutants.out/missed.txt
   # src/lib.rs:20:13: replace > with >= in chargeable_days
   # src/lib.rs:31:20: replace / with % in late_fee
   # src/lib.rs:31:20: replace / with * in late_fee
   ```

   Si el lab está dentro de un repositorio de git, añade `mutants.out*` al `.gitignore`.

4. Descomenta el bloque `// PASO 3`: un test de socios y uno dentro de los días de gracia.
5. Ejecuta `cargo llvm-cov` otra vez:

   ```bash
   cargo llvm-cov
   # ...
   # TOTAL                  46                 7    84.78%           7                 1    85.71%          28                 3    89.29%           0                 0         -
   ```

   Solo quedan las tres líneas de `receipt`, que el doctest prueba aunque la cobertura no lo vea.

6. Ejecuta `cargo mutants`:

   ```bash
   cargo mutants
   # ...
   # MISSED   src/lib.rs:20:13: replace > with >= in chargeable_days in 0s build + 0s test
   # 15 mutants tested in 3s: 1 missed, 14 caught
   ```

   El test de socios mató los dos de la línea 31. Queda uno.

### Paso 4: el mutante que no muere (PASO 4)

El superviviente cambia `days > GRACE` por `days >= GRACE`. La diferencia está en `days == 2`: la frontera. Un test de frontera debería matarlo.

1. Descomenta el bloque `// PASO 4`: comprueba `chargeable_days(2)` y `chargeable_days(3)`.
2. Ejecuta `cargo test`: pasa.
3. Ejecuta `cargo mutants`:

   ```bash
   cargo mutants
   # ...
   # MISSED   src/lib.rs:20:13: replace > with >= in chargeable_days in 0s build + 0s test
   # 15 mutants tested in 3s: 1 missed, 14 caught
   ```

   Sigue vivo. Haz la cuenta con `days = 2`:

   | Versión | Rama | Resultado |
   |---|---|---|
   | Original: `2 > 2` es falso | `else { 0 }` | `0` |
   | Mutante: `2 >= 2` es verdadero | `days - GRACE` = `2 - 2` | `0` |

   Los dos devuelven lo mismo con cualquier entrada. Es un **mutante equivalente** ([teoría 04](../../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#mutantes-equivalentes)): ningún test puede matarlo, porque no cambia el comportamiento. El test de frontera sigue valiendo (comprueba que la gracia son 2 días), pero no es lo que hacía falta aquí.

Ante un mutante equivalente tienes dos opciones: anotarlo como analizado, o reescribir el código para que no exista. El Paso 5 hace lo segundo.

### Paso 5: clippy

1. Ejecuta clippy como lo haría la integración continua:

   ```bash
   cargo clippy --all-targets -- -D warnings
   # error: manual arithmetic check found
   #   --> src/lib.rs:20:5
   #    |
   # 20 |     if days > GRACE { days - GRACE } else { 0 }
   #    |     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ help: replace it with: `days.saturating_sub(GRACE)`
   #    |
   #    = help: for further information visit https://rust-lang.github.io/rust-clippy/rust-1.100.0/index.html#implicit_saturating_sub
   #    = note: `-D clippy::implicit-saturating-sub` implied by `-D warnings`
   #    = help: to override `-D warnings` add `#[allow(clippy::implicit_saturating_sub)]`
   #
   # error: unneeded `return` statement
   #   --> src/lib.rs:33:5
   #    |
   # 33 |     return fee;
   #    |     ^^^^^^^^^^
   # ...
   # error: could not compile `lab04` (lib) due to 2 previous errors
   ```

   `cargo test` compila y pasa; clippy encuentra dos cosas que el compilador no avisa. Con `-D warnings`, el comando termina con código `101`.

   | Aviso | Qué dice |
   |---|---|
   | `implicit_saturating_sub` | El `if` resta sin bajar de 0 a mano. `saturating_sub` hace lo mismo y se lee de un vistazo |
   | `needless_return` | Un `return` en la última línea de una función sobra: en Rust, la última expresión es el valor devuelto |

2. Aplica las sugerencias:

   ```bash
   cargo clippy --fix --allow-dirty
   #        Fixed src/lib.rs (2 fixes)
   ```

   `--allow-dirty` hace falta si la carpeta está en un repositorio de git con cambios sin confirmar: clippy no quiere mezclar sus cambios con los tuyos sin que lo pidas. Si no está en un repositorio, usa `--allow-no-vcs`.

3. Mira `chargeable_days`:

   ```rust
   pub fn chargeable_days(days: u32) -> u32 {
       days.saturating_sub(GRACE)
   }
   ```

4. Comprueba los tres:

   ```bash
   cargo clippy --all-targets -- -D warnings
   #     Finished `dev` profile [unoptimized + debuginfo] target(s) in 0.04s
   cargo test
   # ...
   # test result: ok. 5 passed; 0 failed; ...
   cargo mutants
   # ...
   # 10 mutants tested in 2s: 10 caught
   ```

   Sin el `if`, no hay `>` que cambiar: el mutante equivalente desaparece. Y los tests de los Pasos 3 y 4 confirman que el cambio de clippy no alteró el comportamiento.

## Experimentos

### Experimento 1: un mínimo de cobertura

```bash
cargo llvm-cov --fail-under-lines 95
# ...
# TOTAL                  52                 7    86.54%           8                 1    87.50%          32                 3    90.62%           0                 0         -
echo $?
# 1
```

Termina con código `1` porque la cobertura de líneas (90.62 %) no llega al 95 %, pero no imprime ningún mensaje que lo diga. En un script, el código de salida es lo único que cuenta.

### Experimento 2: ramas y doctests en nightly

Si tienes nightly ([lab 02](../lab-02-fuzzing-con-cargo-fuzz/README.md#preparación)), añade las herramientas de LLVM a ese toolchain y mide ramas y doctests:

```bash
rustup component add llvm-tools-preview --toolchain nightly
cargo +nightly llvm-cov --branch --doctests --text
# warning: --branch option is unstable
# warning: --doctests option is unstable
# ...
```

Vuelve al código del principio (el del starter, con el `if` y solo dos tests) para ver la diferencia. Con `--branch`, debajo de cada `if` aparece cuántas veces fue verdadero y cuántas falso:

```text
   20|      3|    if days > GRACE { days - GRACE } else { 0 }
                                                          ^0
  ------------------
  |  Branch (20:8): [True: 2, False: 0]
  ------------------
  |  Branch (20:8): [True: 1, False: 0]
```

Hay dos líneas `Branch` porque la función se compiló dos veces: una para los tests y otra para el doctest, que ahora sí cuenta. `False: 0` en las dos: nadie probó la rama `else`.

## Comprobación final

```bash
cargo clippy --all-targets -- -D warnings && cargo nextest run && cargo test --doc
# ...
#      Summary [   0.008s] 5 tests run: 5 passed, 0 skipped
# ...
# test src/lib.rs - receipt (line 38) ... ok
```

## Preguntas de cierre

1. ¿Por qué `receipt` aparece sin ejecutar en la cobertura y sus mutantes salen `caught`?
2. La línea 20 estaba ejecutada al 100 %. ¿Qué dijo cargo-mutants de ella, y por qué el test de frontera no lo arregló?
3. ¿Qué no ejecuta nextest que `cargo test` sí? ¿Cómo lo resuelves en un script?
4. Compara el mutante equivalente de este lab con los de mutmut ([semana 21](../../../week-21-coverage_y_calidad_en_python/1-teoria/02-mutation-testing-con-mutmut.md#mutantes-equivalentes)). ¿Qué hiciste aquí con él?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Benchmarks con criterion](../lab-03-benchmarks-con-criterion/README.md) | [Proyecto — Proyecto Semanal - Calidad avanzada en la suite Rust del dominio](../../3-proyecto/README.md) → |

[↑ Semana 40](../../README.md)
