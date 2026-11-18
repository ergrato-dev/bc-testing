# 03 - Benchmarks con criterion

> Lenguaje: **Rust**

Un benchmark mide cuánto tarda un trozo de código. En Go, `go test -bench` medía y benchstat comparaba dos mediciones ([semana 35](../../week-35-concurrencia_y_calidad_en_go/1-teoria/03-benchmarks-y-benchstat.md)). En Rust estable no hay benchmarks en la biblioteca estándar: se usa **criterion**, que mide, hace la estadística y compara con la medición anterior. Hace el trabajo de `go test -bench` y de benchstat a la vez.

---

## 🎯 Objetivos

- Escribir un benchmark con criterion y registrarlo en `Cargo.toml`.
- Medir cómo crece el coste con el tamaño de la entrada, con grupos y `Throughput`.
- Comparar antes y después con líneas base guardadas, y leer si el cambio es real o ruido.

---

## Instalar criterion

```bash
cargo add --dev criterion@=0.8.2 --features html_reports
```

`html_reports` añade el informe HTML en `target/criterion/report/index.html`. Sin esa feature, criterion solo escribe en la terminal.

---

## 1. Anatomía de un benchmark

Los benchmarks van en `benches/`, uno por archivo, como los tests de integración en `tests/`:

```rust
// benches/inventory.rs
use criterion::{Criterion, criterion_group, criterion_main};
use lab03::{catalog, merge_copies};
use std::hint::black_box;

fn merge(c: &mut Criterion) {
    let books = catalog(1000);
    c.bench_function("merge_copies 1000", |b| {
        b.iter(|| merge_copies(black_box(&books)))
    });
}

criterion_group!(benches, merge);
criterion_main!(benches);
```

| Pieza | Qué hace |
|---|---|
| `c.bench_function("nombre", ...)` | Un benchmark con su nombre en el informe |
| `b.iter(\|\| ...)` | El código que se mide. Lo que va fuera, como `catalog(1000)`, se prepara una vez y no cuenta |
| `black_box(&books)` | Impide que el compilador optimice sabiendo el valor: sin él, podría calcular el resultado una vez y no repetir el trabajo. Es el `std::hint::black_box` de la biblioteca estándar |
| `criterion_group!`, `criterion_main!` | Agrupan los benchmarks y generan el `main` del ejecutable |

Y en `Cargo.toml`:

```toml
[[bench]]
name = "inventory"
harness = false
```

`harness = false` le dice a cargo que el `main` lo pone criterion. Sin esta sección, cargo compila `benches/inventory.rs` con el arnés de tests de la biblioteca estándar, que ignora el `main` de criterion, y `cargo bench` no mide nada **sin dar ningún error**:

```text
     Running benches/inventory.rs (target/release/build/lab03/.../inventory-...)

running 0 tests

test result: ok. 0 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
```

---

## 2. Ejecutar y leer

```bash
cargo bench --bench inventory
```

`cargo bench` compila en modo `release`, con optimizaciones, y ejecuta cada benchmark: 3 s de calentamiento y 100 muestras en unos 5 s.

```text
merge_copies 1000       time:   [528.80 µs 531.96 µs 535.12 µs]
Found 2 outliers among 100 measurements (2.00%)
  1 (1.00%) high mild
  1 (1.00%) high severe
```

| Parte | Qué es |
|---|---|
| `[528.80 µs 531.96 µs 535.12 µs]` | La estimación del tiempo por iteración (el centro) y su intervalo de confianza del 95 % |
| `outliers` | Muestras muy alejadas del resto, normalmente porque otro proceso ocupó la CPU. Unas pocas no importan |

| Qué | Go (S35) | Rust |
|---|---|---|
| Dónde | `BenchmarkXxx(b *testing.B)` en un `_test.go` | Una función en `benches/` y `[[bench]]` en `Cargo.toml` |
| Bucle | `for b.Loop()` | `b.iter(\|\| ...)` |
| Resultado | Un número por ejecución (`ns/op`); `-count=10` y benchstat para el intervalo | El intervalo en cada ejecución |

### Grupos: cómo crece el coste

```rust
fn merge_sizes(c: &mut Criterion) {
    let mut group = c.benchmark_group("merge_copies");
    for n in [100, 1000, 10_000] {
        let books = catalog(n);
        group.throughput(Throughput::Elements(n as u64));
        group.bench_with_input(BenchmarkId::from_parameter(n), &books, |b, books| {
            b.iter(|| merge_copies(black_box(books)))
        });
    }
    group.finish();
}
```

```text
merge_copies/100        time:   [6.1892 µs 6.2606 µs 6.3360 µs]
                        thrpt:  [15.783 Melem/s 15.973 Melem/s 16.157 Melem/s]
merge_copies/1000       time:   [534.88 µs 539.05 µs 543.42 µs]
                        thrpt:  [1.8402 Melem/s 1.8551 Melem/s 1.8696 Melem/s]
merge_copies/10000      time:   [50.069 ms 50.539 ms 51.028 ms]
                        thrpt:  [195.97 Kelem/s 197.87 Kelem/s 199.73 Kelem/s]
```

Cada vez que la entrada se multiplica por 10, el tiempo se multiplica por 100: el coste es cuadrático. `Throughput::Elements` añade la línea `thrpt`, elementos por segundo, que lo hace visible sin dividir: si el código escalara bien, se mantendría constante.

---

## 3. Antes y después: líneas base

criterion guarda cada medición en `target/criterion/` y la compara con la anterior. Para comparar con una versión concreta, guárdala con nombre:

```bash
cargo bench --bench inventory -- --save-baseline before
# ... optimizas ...
cargo bench --bench inventory -- --baseline before
```

```text
merge_copies/10000      time:   [446.08 µs 451.77 µs 457.91 µs]
                        thrpt:  [21.838 Melem/s 22.135 Melem/s 22.418 Melem/s]
                 change:
                        time:   [−99.098% −99.085% −99.072%] (p = 0.00 < 0.05)
                        thrpt:  [+10675% +10834% +10990%]
                        Performance has improved.
```

| Parte | Qué es |
|---|---|
| `change: [−99.098% −99.085% −99.072%]` | El cambio respecto a la línea base, con su intervalo |
| `p = 0.00 < 0.05` | La probabilidad de ver esta diferencia si en realidad no hubiera cambio. Por debajo de 0.05, el cambio se considera real |
| `Performance has improved.` | La conclusión: mejoró, empeoró (`regressed`), o `No change in performance detected.` |

Hay una cuarta conclusión: `Change within noise threshold.` La diferencia es estadísticamente real (`p < 0.05`), pero su intervalo toca la franja de ±1 % que criterion considera ruido. Es lo que sale al repetir la medición sin cambiar el código:

```text
merge_copies 1000       time:   [528.54 µs 532.12 µs 536.09 µs]
                        change: [+0.4968% +1.6164% +2.8304%] (p = 0.01 < 0.05)
                        Change within noise threshold.
```

benchstat hacía lo mismo con `~` en Go ([semana 35](../../week-35-concurrencia_y_calidad_en_go/1-teoria/03-benchmarks-y-benchstat.md#3-benchstat-antes-y-después)): una diferencia pequeña entre dos mediciones del mismo código es ruido, no una mejora.

### Reglas para medir

- Mide con el portátil enchufado y sin otros programas pesados abiertos.
- Compara siempre con una línea base medida en la misma máquina.
- Un benchmark mide el caso que le das. Una optimización puede acelerar un tamaño y frenar otro: mide varios.
- `cargo test --benches` ejecuta cada benchmark **una vez**, sin medir, para comprobar que no falla. Sirve para la integración continua, donde medir no tiene sentido.

---

## ✅ Resumen

- Los benchmarks van en `benches/` con `[[bench]]` y `harness = false`. Sin esa sección, `cargo bench` no mide nada y no avisa.
- `b.iter` mide el cierre; `black_box` evita que el compilador elimine el trabajo.
- Los grupos con `Throughput` muestran cómo crece el coste con el tamaño.
- `--save-baseline` y `--baseline` comparan antes y después. Lee la conclusión: mejora, empeora, sin cambio o dentro del ruido.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Fuzzing con cargo-fuzz](02-fuzzing-con-cargo-fuzz.md) | [Teoría — 04 - Cobertura, nextest, mutantes y clippy](04-cobertura-nextest-mutantes-y-clippy.md) → |

[↑ Semana 40](../README.md)
