# Prácticas - Semana 40

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un paquete de Rust; los tests o los benchmarks están comentados por PASOS, y lo que no es descomentar (corregir una función, añadir una sección a `Cargo.toml`, crear el fuzz target) está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Propiedades con proptest](lab-01-propiedades-con-proptest/README.md) | `proptest!`, estrategias con rangos, `any`, expresiones regulares y `prop_compose!`, `prop_assume!`, el contraejemplo reducido y `proptest-regressions/` | 60 min |
| [Lab 02 - Fuzzing con cargo-fuzz](lab-02-fuzzing-con-cargo-fuzz/README.md) | `cargo fuzz init`, un fuzz target con nightly, reproducir y minimizar un fallo, convertirlo en test y comprobar el resultado dentro del target | 55 min |
| [Lab 03 - Benchmarks con criterion](lab-03-benchmarks-con-criterion/README.md) | `[[bench]]` con `harness = false`, `bench_function`, grupos con `Throughput`, `--save-baseline` y `--baseline` | 55 min |
| [Lab 04 - Cobertura, mutantes y clippy](lab-04-cobertura-mutantes-y-clippy/README.md) | `cargo nextest run`, `cargo llvm-cov`, `cargo mutants` con un mutante equivalente y `cargo clippy -- -D warnings` | 70 min |

## Antes de empezar

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
```

Los labs 02 y 04 necesitan herramientas que no vienen con Rust. Instálalas una vez ([teoría 02](../1-teoria/02-fuzzing-con-cargo-fuzz.md#instalar) y [teoría 04](../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md#instalar)):

```bash
rustup toolchain install nightly --profile minimal
rustup component add llvm-tools-preview
cargo install --locked cargo-fuzz@0.13.2 cargo-llvm-cov@0.9.1 cargo-nextest@0.9.152 cargo-mutants@27.1.0
```

`cargo install` compila cada herramienta desde el código fuente: tarda varios minutos la primera vez. El lab 02 necesita Linux o macOS; en Windows, WSL.

Cada lab es un paquete independiente:

```bash
cd lab-NN-.../starter
cargo test
```

`cargo clean` borra la carpeta `target/` de un lab cuando termines. En el lab 02, la de `fuzz/` está en `fuzz/target/`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Cobertura, nextest, mutantes y clippy](../1-teoria/04-cobertura-nextest-mutantes-y-clippy.md) | [Lab 01 - Propiedades con proptest](lab-01-propiedades-con-proptest/README.md) → |

[↑ Semana 40](../README.md)
