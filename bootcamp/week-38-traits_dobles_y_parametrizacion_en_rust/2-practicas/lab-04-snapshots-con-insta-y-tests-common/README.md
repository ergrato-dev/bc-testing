# Lab 04 - Snapshots con insta y tests/common

## Objetivo

Probar el texto de un aviso con snapshots de insta: crear el primero, revisarlo con `cargo insta review`, ver un cambio como diff y usar snapshots inline y de `Debug`. Mover los datos compartidos de los tests a `tests/common/mod.rs`.

## Tiempo estimado

55 minutos.

## Preparación

1. Instala `cargo-insta`, la herramienta que revisa los snapshots. Se instala una vez por máquina:

   ```bash
   cargo install --locked cargo-insta@1.49.0
   # ...
   #     Finished `release` profile [optimized] target(s) in 21.59s
   #   Installing /home/<usuario>/.cargo/bin/cargo-insta
   #    Installed package `cargo-insta v1.49.0` (executable `cargo-insta`)
   cargo insta --version
   # cargo-insta 1.49.0
   ```

   | Parte | Qué / Para qué / Impacto |
   |---|---|
   | `cargo install` | Compila un programa de crates.io y lo deja en `~/.cargo/bin`. Tarda entre medio minuto y un par de minutos |
   | `--locked` | Usa las versiones exactas del `Cargo.lock` del programa, las que probaron sus autores |
   | `@1.49.0` | La misma versión que el crate `insta` del lab |

2. Ejecuta los tests:

   ```bash
   cd starter
   cargo test
   # ...
   #      Running unittests src/lib.rs (target/debug/build/lab04/6a995d290805644b/out/lab04-6a995d290805644b)
   # running 0 tests
   # ...
   #      Running tests/common.rs (target/debug/build/lab04/8a82d07869209151/out/common-8a82d07869209151)
   # running 0 tests
   # ...
   #      Running tests/notice.rs (target/debug/build/lab04/84f0253774736a51/out/notice-84f0253774736a51)
   # running 0 tests
   # ...
   #      Running tests/subject.rs (target/debug/build/lab04/792f8b4dc16cc521/out/subject-792f8b4dc16cc521)
   # running 0 tests
   # ...
   ```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab04` con `insta = "=1.49.0"` en `[dev-dependencies]` |
| `src/lib.rs` | El aviso de un préstamo (`Notice`), su texto (`render_notice`) y el asunto del correo (`subject`) |
| `tests/common.rs` | `sample_notice()`: el aviso que usan los dos archivos de tests |
| `tests/notice.rs` | El test del texto del aviso, comentado (PASO 1) |
| `tests/subject.rs` | Los tests del asunto y del `Notice`, comentados (PASO 2) |

Fíjate en la salida: Cargo ejecuta `tests/common.rs` como un archivo de tests más, con 0 tests. El Paso 4 lo arregla.

## Paso a paso

### Paso 1: el primer snapshot (PASO 1)

1. Descomenta el bloque `// PASO 1` de `tests/notice.rs`.
2. Ejecuta:

   ```bash
   cargo test --test notice
   # running 1 test
   # stored new snapshot .../starter/tests/snapshots/notice__notice_text.snap.new
   # test notice_text ... FAILED
   #
   # failures:
   #
   # ---- notice_text stdout ----
   # ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ Snapshot Summary ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   # Snapshot file: tests/snapshots/notice__notice_text.snap
   # Snapshot: notice_text
   # Source: tests/notice.rs:9
   # ────────────────────────────────────────────────────────────────────────────────
   # Expression: render_notice(&common::sample_notice())
   # ────────────────────────────────────────────────────────────────────────────────
   # +new results
   # ────────────┬───────────────────────────────────────────────────────────────────
   #           1 │+Préstamo de la pieza MUS-001
   #           2 │+Institución: MNAC
   #           3 │+Duración: 30 días
   #           4 │+Seguro: 6000
   # ────────────┴───────────────────────────────────────────────────────────────────
   # To update snapshots run `cargo insta review` or set `INSTA_UPDATE=always`
   # ...
   # test result: FAILED. 0 passed; 1 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.03s
   ```

   El test falla porque todavía no hay snapshot aprobado. insta guarda la salida en `tests/snapshots/notice__notice_text.snap.new` para que la revises: `+new results` y cada línea con `+` es lo nuevo.

3. Revísalo:

   ```bash
   cargo insta review
   # Reviewing [1/1] lab04@0.1.0:
   # Snapshot file: tests/snapshots/notice__notice_text.snap
   # ...
   #   a accept     keep the new snapshot
   #   r reject     reject the new snapshot
   #   s skip       keep both for now
   #   i hide info  toggles extended snapshot info
   #   d hide diff  toggle snapshot diff
   ```

   Lee el texto. Si es lo que debe recibir la institución, pulsa `a`:

   ```text
   insta review finished
   accepted:
     tests/notice.rs (notice_text.snap)
   ```

4. Mira el archivo aprobado:

   ```bash
   cat tests/snapshots/notice__notice_text.snap
   # ---
   # source: tests/notice.rs
   # expression: "render_notice(&common::sample_notice())"
   # ---
   # Préstamo de la pieza MUS-001
   # Institución: MNAC
   # Duración: 30 días
   # Seguro: 6000
   ```

   La cabecera entre `---` dice de dónde sale; debajo está la salida. Este archivo se sube al repositorio con el código.

5. Ejecuta otra vez `cargo test --test notice`: ahora pasa.

### Paso 2: un cambio en la salida

1. En `src/lib.rs`, cambia `Seguro: {}` por `Prima: {}`.
2. Ejecuta:

   ```bash
   cargo test --test notice
   # ...
   # -old snapshot
   # +new results
   # ────────────┬───────────────────────────────────────────────────────────────────
   #     1     1 │ Préstamo de la pieza MUS-001
   #     2     2 │ Institución: MNAC
   #     3     3 │ Duración: 30 días
   #     4       │-Seguro: 6000
   #           4 │+Prima: 6000
   # ...
   ```

   El diff muestra solo lo que cambió. Las dos columnas de números son la línea en el snapshot viejo y en la salida nueva.

3. El cambio no estaba pedido: recházalo y deshazlo.

   ```bash
   cargo insta reject
   # insta review finished
   # rejected:
   #   tests/notice.rs (notice_text.snap)
   ```

   `reject` borra el `.snap.new` y deja el `.snap` como estaba. Vuelve a poner `Seguro: {}` en `src/lib.rs` y comprueba que `cargo test --test notice` pasa.

### Paso 3: snapshots inline y de `Debug` (PASO 2)

1. Descomenta el bloque `// PASO 2` de `tests/subject.rs`.
2. Ejecuta:

   ```bash
   cargo test --test subject
   # stored new snapshot .../starter/tests/snapshots/subject__sample_notice_fields.snap.new
   # test sample_notice_fields ... FAILED
   # test subject_line ... FAILED
   # ...
   ```

3. Mira qué queda pendiente:

   ```bash
   cargo insta pending-snapshots
   # Pending snapshots:
   #   tests/subject.rs:9
   #   tests/snapshots/subject__sample_notice_fields.snap
   # ...
   ```

   | Pendiente | De qué test | Dónde se guarda |
   |---|---|---|
   | `tests/subject.rs:9` | `subject_line`, con `@""` | Dentro del propio test: es un snapshot *inline* |
   | `subject__sample_notice_fields.snap` | `sample_notice_fields`, con `assert_debug_snapshot!` | En `tests/snapshots/`, como el del Paso 1 |

4. Acepta los dos:

   ```bash
   cargo insta accept
   # insta review finished
   # accepted:
   #   tests/subject.rs:9 (subject_line)
   #   tests/subject.rs (sample_notice_fields.snap)
   ```

5. Abre `tests/subject.rs`: insta escribió la salida en el código.

   ```rust
   assert_snapshot!(subject(&common::sample_notice()), @"[MUS-001] Préstamo aprobado");
   ```

   Y el snapshot de `Debug` guarda el `Notice` con un campo por línea:

   ```bash
   cat tests/snapshots/subject__sample_notice_fields.snap
   # ---
   # source: tests/subject.rs
   # expression: "common::sample_notice()"
   # ---
   # Notice {
   #     institution: "MNAC",
   #     code: "MUS-001",
   #     days: 30,
   #     premium: 6000,
   # }
   ```

### Paso 4: `tests/common/mod.rs`

1. Mueve el módulo compartido a una carpeta:

   ```bash
   mkdir tests/common
   mv tests/common.rs tests/common/mod.rs
   cargo test 2>&1 | grep Running
   #      Running unittests src/lib.rs (target/debug/build/lab04/6a995d290805644b/out/lab04-6a995d290805644b)
   #      Running tests/notice.rs (target/debug/build/lab04/84f0253774736a51/out/notice-84f0253774736a51)
   #      Running tests/subject.rs (target/debug/build/lab04/792f8b4dc16cc521/out/subject-792f8b4dc16cc521)
   ```

   `tests/common.rs` ya no aparece: Cargo solo compila como tests los archivos que están directamente en `tests/`. `mod common;` encuentra igual `common.rs` o `common/mod.rs`, así que los tests no cambian.

2. Añade al final de `tests/common/mod.rs` otro dato:

   ```rust

   /// Un préstamo de un año de la misma pieza.
   pub fn long_loan() -> Notice {
       Notice {
           days: 365,
           ..sample_notice()
       }
   }
   ```

   `..sample_notice()` copia los campos que no escribes. Y al final de `tests/notice.rs`, un test que lo usa:

   ```rust

   #[test]
   fn long_loan_text() {
       assert_snapshot!(render_notice(&common::long_loan()));
   }
   ```

3. Ejecuta `cargo test`:

   ```text
   warning: function `long_loan` is never used
     --> tests/common/mod.rs:16:8
   ...
   warning: `lab04` (test "subject") generated 1 warning
   ...
   stored new snapshot .../starter/tests/snapshots/notice__long_loan_text.snap.new
   test long_loan_text ... FAILED
   ```

   Dos cosas:

   - El warning es de `tests/subject.rs`: cada archivo de tests compila `common` por su cuenta, y `subject.rs` no usa `long_loan`. El warning es falso, porque `notice.rs` sí la usa.
   - El snapshot nuevo dice `Duración: 365 días` y `Seguro: 6000`, el mismo seguro que el préstamo de 30 días: `long_loan` lo copió de `sample_notice`. `render_notice` no calcula nada y el test es válido, pero así se ve por qué un snapshot se lee antes de aceptarlo: aprueba todo lo que hay en la salida, también un dato incoherente.

4. Acepta el snapshot con `cargo insta accept`.
5. Añade `#![allow(dead_code)]` como segunda línea de `tests/common/mod.rs`, debajo del comentario `//!`:

   ```rust
   //! Datos compartidos por los tests de tests/.
   #![allow(dead_code)]
   ```

6. Ejecuta `cargo test 2>&1 | grep -c '^warning'`: imprime `0`.

## Experimentos

### Experimento 1: snapshots en CI

1. Cambia otra vez `Seguro: {}` por `Prima: {}` en `src/lib.rs`.
2. Ejecuta con la variable `CI`, como lo haría GitHub Actions:

   ```bash
   CI=true cargo test --test notice
   # ...
   # test long_loan_text ... FAILED
   # test notice_text ... FAILED
   # ...
   ls tests/snapshots
   # notice__long_loan_text.snap
   # notice__notice_text.snap
   # subject__sample_notice_fields.snap
   ```

   Los tests fallan con el diff, pero no hay ningún `.snap.new` ni la línea `To update snapshots run ...`. En CI nadie puede revisar: el snapshot se acepta en local y llega al repositorio en el pull request.

3. Deshaz el cambio.

### Experimento 2: un snapshot que no se puede aprobar

1. En `src/lib.rs`, añade debajo de la primera línea (el comentario `//!`):

   ```rust
   use std::time::SystemTime;
   ```

   Tiene que ir después del `//!`: un `use` delante de él no compila (`E0753 expected outer doc comment`).

2. En `render_notice`, añade la hora al texto: cambia `Seguro: {}\n",` por `Seguro: {}\nEmitido: {:?}\n",` y `n.premium` por `n.premium, SystemTime::now()`.
3. Ejecuta, acepta y vuelve a ejecutar:

   ```bash
   cargo test --test notice
   # test long_loan_text ... FAILED
   # test notice_text ... FAILED
   # ...
   #           5 │+Emitido: SystemTime { tv_sec: 1795024505, tv_nsec: 306685184 }
   cargo insta accept
   # ...
   cargo test --test notice
   # test notice_text ... FAILED
   # test long_loan_text ... FAILED
   # ...
   #     5       │-Emitido: SystemTime { tv_sec: 1795024505, tv_nsec: 306719342 }
   #           5 │+Emitido: SystemTime { tv_sec: 1795024505, tv_nsec: 409768869 }
   ```

   El snapshot aceptado deja de valer en la siguiente ejecución: la hora cambió. Un snapshot solo sirve para salidas estables. La hora es una dependencia, como el reloj de la [semana 33](../../../week-33-interfaces_y_dobles_de_prueba_en_go/1-teoria/01-interfaces-y-dobles-a-mano.md#el-reloj-y-la-aleatoriedad-también-son-dependencias): se recibe desde fuera o se deja fuera del texto.

4. Deshaz los cambios de `src/lib.rs` y ejecuta `cargo test --test notice`. Los snapshots aceptados tienen la línea `Emitido`, y el diff la quita:

   ```text
       5       │-Emitido: SystemTime { tv_sec: 1795024505, tv_nsec: 306685184 }
   ```

   Comprueba que es lo único que cambia y acéptalo con `cargo insta accept`. Los tests vuelven a pasar.

## Comprobación final

```bash
cargo test 2>&1 | grep -E '[.]{3} ok$'
# test notice_text ... ok
# test long_loan_text ... ok
# test subject_line ... ok
# test sample_notice_fields ... ok
ls tests/snapshots
# notice__long_loan_text.snap
# notice__notice_text.snap
# subject__sample_notice_fields.snap
```

No debe quedar ningún `.snap.new` ni `.pending-snap`: `cargo insta pending-snapshots` lo comprueba.

## Preguntas de cierre

1. ¿Por qué falla un snapshot la primera vez? ¿Qué harías si el `.snap.new` tuviera un error?
2. ¿Cuándo usarías `assert_snapshot!` con `@""`, cuándo sin él y cuándo `assert_debug_snapshot!`?
3. ¿Por qué no basta con `tests/common.rs`? ¿Por qué aparece un warning que no es real?
4. Compara insta con el golden file de Go y el flag `-update` de la semana 33: ¿qué te da `cargo insta review` que no te daba `-update`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Parametrización con rstest](../lab-03-parametrizacion-con-rstest/README.md) | [Proyecto — Proyecto Semanal - El servicio del dominio con traits y dobles](../../3-proyecto/README.md) → |

[↑ Semana 38](../../README.md)
