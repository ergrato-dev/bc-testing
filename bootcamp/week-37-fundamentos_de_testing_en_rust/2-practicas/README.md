# Prácticas - Semana 37

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con un paquete de Rust y los tests comentados por PASOS; lo que no es descomentar, como corregir una función o cambiar una línea, está en el README del lab. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer test con cargo test](lab-01-primer-test-con-cargo-test/README.md) | `#[cfg(test)] mod tests`, `#[test]`, `assert!`, `assert_eq!` y `assert_ne!` con mensajes, filtros, `--exact`, `--list`, una función sin `#[test]` y el desbordamiento de `u32` en debug y en `--release` | 55 min |
| [Lab 02 - Pánicos, Result y diffs](lab-02-panics-result-y-diffs/README.md) | `#[should_panic(expected = "...")]`, `Err` con `assert_eq!` y `matches!`, tests que devuelven `Result`, `?` frente a `unwrap` y `pretty_assertions` | 65 min |
| [Lab 03 - Ignore, salida e hilos](lab-03-ignore-salida-e-hilos/README.md) | `--nocapture` y `--show-output`, un test inestable por un archivo compartido, `--test-threads=1` como diagnóstico, `#[ignore]`, `--ignored` y `--include-ignored` | 60 min |
| [Lab 04 - Integración y doctests](lab-04-integracion-y-doctests/README.md) | Un unitario de una función privada, tests en `tests/`, doctests con `?` y la línea oculta `# `, `--lib`, `--test` y `--doc` | 60 min |

## Antes de empezar

```bash
cargo --version
# cargo 1.100.0 (3acc79c0c 2026-11-05)
```

Si no tienes Rust, instálalo con `rustup` ([teoría 01](../1-teoria/01-cargo-test-y-el-atributo-test.md#instalar-rust)). Cada lab es un paquete independiente:

```bash
cd lab-NN-.../starter
cargo test
```

Solo el lab 02 tiene una dependencia, `pretty_assertions`, que `cargo` descarga la primera vez. `cargo` compila en la carpeta `target/` de cada lab; puedes borrarla con `cargo clean` cuando termines.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Unitarios, integración y doctests](../1-teoria/03-unitarios-integracion-y-doctests.md) | [Lab 01 - Primer test con cargo test](lab-01-primer-test-con-cargo-test/README.md) → |

[↑ Semana 37](../README.md)
