# 02 - Fuzzing con cargo-fuzz

> Lenguaje: **Rust**

proptest genera entradas a ciegas, según una estrategia. Un **fuzzer** guiado por cobertura aprende mientras prueba: se queda con las entradas que recorren código nuevo y las muta para llegar más lejos. Go lo trae en `go test -fuzz` ([semana 35](../../week-35-concurrencia_y_calidad_en_go/1-teoria/02-fuzzing-nativo.md)). En Rust se usa **cargo-fuzz**, que ejecuta libFuzzer, el fuzzer de LLVM.

---

## 🎯 Objetivos

- Instalar cargo-fuzz y el toolchain nightly que necesita.
- Crear un fuzz target y ejecutarlo con un límite de tiempo.
- Leer un fallo, reproducirlo, minimizarlo y convertirlo en un test de regresión.
- Decidir qué comprobar dentro del target, además de «no entra en pánico».

---

## Instalar

cargo-fuzz compila con opciones del compilador que solo existen en **nightly**, la versión diaria de Rust. Necesitas Linux o macOS; en Windows, WSL.

```bash
rustup toolchain install nightly --profile minimal
cargo install --locked cargo-fuzz@0.13.2
```

| Comando | Qué / Para qué / Impacto |
|---|---|
| `rustup toolchain install nightly` | Instala nightly junto a tu toolchain estable. No cambia el que usas por defecto |
| `--profile minimal` | Sin documentación ni extras: solo el compilador, cargo y la biblioteca estándar |
| `cargo install --locked cargo-fuzz@0.13.2` | Instala el subcomando `cargo fuzz`. `--locked` usa las versiones exactas de su `Cargo.lock` |

El resto del proyecto sigue en estable. Solo los comandos de fuzzing llevan `+nightly`: `cargo +nightly fuzz run`. Si lo olvidas:

```text
error: the option `Z` is only accepted on the nightly compiler
```

Los ejemplos usan nightly `1.102.0-nightly (59cbdee2e 2026-11-17)`.

---

## 1. El fuzz target

```bash
cargo fuzz init --target parse
```

Crea un crate aparte en `fuzz/`, que depende del tuyo:

| Archivo | Qué es |
|---|---|
| `fuzz/Cargo.toml` | El crate `<tu-crate>-fuzz`, con `libfuzzer-sys` y tu crate por `path = ".."` |
| `fuzz/fuzz_targets/parse.rs` | El target: la función que recibe cada entrada |
| `fuzz/.gitignore` | Ignora `target`, `corpus`, `artifacts` y `coverage` |

```rust
#![no_main]

use libfuzzer_sys::fuzz_target;

fuzz_target!(|data: &str| {
    let _ = lab02::parse(data);
});
```

| Pieza | Qué hace |
|---|---|
| `#![no_main]` | El `main` lo pone libFuzzer |
| `fuzz_target!(\|data: &str\| ...)` | El cuerpo se ejecuta con cada entrada. Con `&[u8]` recibes bytes; con `&str`, libFuzzer descarta los bytes que no son UTF-8 válido |
| `let _ = parse(data)` | La comprobación mínima: que `parse` no entre en pánico con ninguna entrada |

Comparado con Go:

| Qué | Go (S35) | Rust |
|---|---|---|
| Dónde va | `FuzzXxx` en un `_test.go` | Un crate aparte en `fuzz/` |
| Toolchain | El normal | nightly |
| Corpus semilla | `f.Add(...)` en el código | Archivos en `fuzz/corpus/<target>/` |
| Fallos | `testdata/fuzz/` y se repiten con `go test` | `fuzz/artifacts/<target>/`. `cargo test` no los repite: el test de regresión lo escribes tú |

---

## 2. Ejecutar

```bash
cargo +nightly fuzz run parse -- -max_total_time=60
```

Todo lo que va después de `--` son opciones de libFuzzer. `-max_total_time=60` para a los 60 segundos; sin él, sigue hasta encontrar un fallo o hasta que lo pares con `Ctrl+C`. Mientras busca, imprime una línea por cada entrada que recorre código nuevo:

```text
#12	NEW    cov: 53 ft: 72 corp: 4/10b lim: 4 exec/s: 0 rss: 35Mb L: 4/4 MS: 5 CrossOver-ChangeBit-CopyPart-CrossOver-CrossOver-
```

| Campo | Qué es |
|---|---|
| `#12` | Entradas probadas |
| `cov: 53` | Bloques de código distintos alcanzados |
| `corp: 4/10b` | Entradas guardadas en el corpus (4, con 10 bytes en total) |
| `exec/s` | Entradas por segundo |
| `MS: ...` | Las mutaciones que produjeron la entrada |

Al terminar sin fallos:

```text
Done 9205612 runs in 61 second(s)
```

Nueve millones de entradas en un minuto. Las que alcanzaron código nuevo quedan en `fuzz/corpus/parse/`: la siguiente ejecución empieza desde ellas.

---

## 3. Cuando encuentra un fallo

```text
thread '<unnamed>' (440287) panicked at .../lab02/src/lib.rs:23:42:
start byte index 5 is out of bounds for string of length 4
...
Failing input:

	fuzz/artifacts/parse/crash-804525c8009e09a9b3f128e566dab3e3b40f20f8

Output of `std::fmt::Debug`:

	"ISBN"

Reproduce with:

	cargo fuzz run parse fuzz/artifacts/parse/crash-804525c8009e09a9b3f128e566dab3e3b40f20f8

Minimize test case with:

	cargo fuzz tmin parse fuzz/artifacts/parse/crash-804525c8009e09a9b3f128e566dab3e3b40f20f8
```

| Qué | Para qué |
|---|---|
| `fuzz/artifacts/parse/crash-...` | La entrada que falló, en bytes |
| `Output of std::fmt::Debug` | La misma entrada, legible: la que copias al test |
| `cargo +nightly fuzz run parse <archivo>` | Ejecuta solo esa entrada. Añade tú el `+nightly`: el mensaje no lo pone |
| `cargo +nightly fuzz tmin parse <archivo>` | Busca una entrada más corta que falle igual |

libFuzzer no tiene el *shrinking* de proptest: `tmin` acorta la entrada, pero no la simplifica. Con una entrada de 13 bytes que ya no se puede acortar, `tmin` termina con `failed to minimize beyond ... (13 bytes)`.

El paso que cierra el ciclo lo das tú. Copia la entrada a un test normal:

```rust
#[test]
fn a_bare_prefix_is_too_short() {
    assert_eq!(parse("ISBN"), Err(IsbnError::Length));
}
```

`cargo test` lo ejecuta siempre, en estable, sin el fuzzer. En Go, el archivo de `testdata/fuzz/` hacía ese papel solo.

---

## 4. Qué comprobar en el target

Un target que solo llama a la función encuentra pánicos: índices fuera de rango, `unwrap` sobre `None`, desbordamientos (cargo-fuzz compila con `debug-assertions`). En Rust, los errores de memoria que el fuzzing encuentra en C no existen en código seguro, así que el valor está en las comprobaciones que añadas:

```rust
fuzz_target!(|data: &str| {
    if let Ok(isbn) = lab02::parse(data) {
        let digits = isbn.as_str();
        let ok = digits.len() == 13 && digits.bytes().all(|b| b.is_ascii_digit());
        assert!(ok, "{data:?} gave {digits:?}");
    }
});
```

Es la misma idea que en Go ([semana 35](../../week-35-concurrencia_y_calidad_en_go/1-teoria/02-fuzzing-nativo.md#4-qué-comprobar-en-la-función-objetivo)): si `parse` acepta algo, el resultado tiene que ser un ISBN. Con esta comprobación, el fuzzer encontró en menos de un segundo un texto sin un solo dígito que el lector aceptaba como ISBN.

| Fuzzing con cargo-fuzz | Propiedades con proptest |
|---|---|
| Busca durante minutos u horas, guiado por la cobertura | 256 casos en cada `cargo test` |
| Nightly, fuera de `cargo test` | Estable, dentro de `cargo test` |
| Entradas de bytes o texto (o tipos con `Arbitrary`) | Entradas con la forma que describe la estrategia |
| Bueno para lectores de formatos: lo que llega de fuera | Bueno para reglas del dominio |

Las dos se complementan: proptest en cada ejecución de la suite, cargo-fuzz de vez en cuando sobre el código que lee datos de fuera.

---

## ✅ Resumen

- cargo-fuzz necesita nightly: `cargo +nightly fuzz run <target>`. El resto del proyecto sigue en estable.
- Los targets van en un crate aparte, `fuzz/`. Limita cada ejecución con `-- -max_total_time=N`.
- Un fallo queda en `fuzz/artifacts/`. Reprodúcelo, minimízalo con `tmin` y conviértelo tú en un test de `cargo test`.
- Un target que solo llama a la función encuentra pánicos; añade aserciones sobre el resultado para encontrar entradas aceptadas que no deberían serlo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - Propiedades con proptest](01-propiedades-con-proptest.md) | [Teoría — 03 - Benchmarks con criterion](03-benchmarks-con-criterion.md) → |

[↑ Semana 40](../README.md)
