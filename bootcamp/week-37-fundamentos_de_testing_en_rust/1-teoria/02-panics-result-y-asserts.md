# 02 - Pánicos, Result y control de la ejecución

> Lenguaje: **Rust**

Rust tiene dos formas de fallar: entrar en **pánico**, que para el hilo, o devolver un **`Err`**, que es un valor más. Un test necesita comprobar las dos. Esta teoría cubre cómo, y cómo controlar la ejecución de la suite: la salida de los tests, los hilos y los tests apartados con `#[ignore]`.

---

## 🎯 Objetivos

- Probar un pánico esperado con `#[should_panic(expected = "...")]`.
- Probar un `Err` esperado con `assert_eq!` y `matches!`.
- Escribir tests que devuelven `Result` para usar `?`.
- Leer la diferencia entre dos valores con `pretty_assertions`.
- Usar `--nocapture`, `--show-output`, `--test-threads` e `#[ignore]`.

---

## Pánico o `Err`

```rust
/// Entra en pánico si `n` es 0: es un error del programa, no de los datos.
pub fn installments(total: u32, n: u32) -> Vec<u32> {
    assert!(n > 0, "installments: n must be at least 1");
    // ...
}

/// Devuelve Err si el valor o los días son 0: datos que el que llama debe tratar.
pub fn loan_premium(declared_value: u32, days: u32) -> Result<u32, LoanError> {
    // ...
}
```

| | Pánico | `Err` |
|---|---|---|
| Qué significa | Un error del programa: algo que no debería pasar nunca | Un resultado posible: datos incorrectos, un archivo que no existe |
| Equivale a | Un `panic` de Go, una excepción no controlada | El `error` de Go; una excepción comprobada de Java |
| Cómo se prueba | `#[should_panic(expected = "...")]` | `assert_eq!` o `matches!` sobre el `Result` |

### `#[should_panic(expected = "...")]`

```rust
#[test]
#[should_panic(expected = "n must be at least 1")]
fn installments_reject_zero() {
    installments(1000, 0);
}
```

El test pasa si la función entra en pánico y el mensaje **contiene** el texto de `expected`. Sin `expected`, pasa con cualquier pánico: una división entre 0, un índice fuera de rango o un `unwrap` sobre `None`. En el [lab 02](../2-practicas/lab-02-panics-result-y-diffs/README.md#experimento-1-should_panic-sin-expected), un test sin `expected` sigue en verde después de borrar la validación que probaba.

| | JUnit | pytest | Go | Rust |
|---|---|---|---|---|
| Excepción o pánico esperado | `assertThrows(X.class, ...)` | `pytest.raises(X, match=...)` | `recover()` en un `defer` | `#[should_panic(expected = "...")]` |
| Comprueba el mensaje | Con `getMessage()` | `match=` | A mano | `expected` |

### Un `Err` esperado

Un `Err` es un valor, así que se compara como cualquier otro:

```rust
#[test]
fn premium_rejects_zero_days() {
    assert_eq!(loan_premium(5000, 0), Err(LoanError::ZeroDays));
}

#[test]
fn premium_rejects_zero_value() {
    assert!(matches!(loan_premium(0, 10), Err(LoanError::ZeroValue)));
}
```

`assert_eq!` necesita que `LoanError` tenga `#[derive(Debug, PartialEq)]`. `matches!(valor, patrón)` devuelve `true` si el valor encaja en el patrón; sirve cuando el error lleva datos que no quieres comparar, como un mensaje: `matches!(r, Err(ParseError::Line(_)))`. Es el `errors.Is` de Go, pero comparando la variante del `enum`.

### Tests que devuelven `Result`

```rust
#[test]
fn receipt_for_a_month() -> Result<(), LoanError> {
    let receipt = receipt("MUS-001", 200_000, 30, 3)?;
    assert_eq!(receipt.premium, 6000);
    Ok(())
}
```

**Qué:** un test puede devolver `Result<(), E>`; si devuelve `Err`, falla e imprime `Error: ` y el error.
**Para qué:** usar `?` en lugar de `.unwrap()` en cada llamada que devuelve `Result`.
**Impacto:** el fallo dice qué error hubo pero no en qué línea. Con varios `?` en el test, `unwrap()` o `expect("...")` señalan la llamada exacta ([lab 02](../2-practicas/lab-02-panics-result-y-diffs/README.md#experimento-2--o-unwrap)).

Un test que devuelve `Result` no puede llevar `#[should_panic]`: no compila (``error: functions using `#[should_panic]` must return `()` ``). Para comprobar un `Err`, compáralo con `assert_eq!`.

---

## `pretty_assertions`

`assert_eq!` imprime los dos valores en una línea cada uno. Con una estructura, encontrar la diferencia es comparar a ojo:

```text
  left: Receipt { piece: "MUS-001", premium: 101, installments: [34, 34, 33] }
 right: Receipt { piece: "MUS-001", premium: 101, installments: [33, 34, 34] }
```

El crate `pretty_assertions` sustituye `assert_eq!` y `assert_ne!` por versiones que imprimen un diff. Se añade solo para los tests, en `[dev-dependencies]`:

```toml
[dev-dependencies]
pretty_assertions = "=1.4.1"
```

```rust
#[cfg(test)]
mod tests {
    use super::*;
    use pretty_assertions::assert_eq;
    // los tests no cambian
}
```

```text
Diff < left / right > :
 Receipt {
     piece: "MUS-001",
     premium: 101,
     installments: [
>        33,
         34,
         34,
<        33,
     ],
 }
```

`<` es lo que solo está en `left` (lo obtenido) y `>` lo que solo está en `right` (lo esperado). En la terminal sale en colores. `"=1.4.1"` fija la versión exacta; sin el `=`, Cargo aceptaría cualquier 1.x posterior. `Cargo.lock` guarda la versión elegida de esa dependencia y de las suyas (`diff` y `yansi`).

---

## Controlar la ejecución

### La salida de los tests

`cargo test` **captura** lo que imprime cada test y solo lo enseña si el test falla, debajo de `---- nombre stdout ----`. Para verlo también cuando pasa:

| Opción | Qué hace |
|---|---|
| `cargo test -- --nocapture` | No captura: la salida aparece en el momento, mezclada entre tests que se ejecutan a la vez |
| `cargo test -- --show-output` | Captura y, al final, enseña la salida de cada test que pasó, agrupada por test |

Es el `-v` de `go test` con `t.Log`, o el `-s` de pytest.

### Tests en paralelo

`cargo test` ejecuta los tests de cada binario en tantos hilos como núcleos tiene la máquina. Dos tests que escriben el mismo archivo se pisan: en el [lab 03](../2-practicas/lab-03-ignore-salida-e-hilos/README.md#paso-2-tres-tests-un-archivo-paso-2), tres tests con la misma ruta fallan 9 de cada 20 ejecuciones.

| Opción | Para qué |
|---|---|
| `cargo test -- --test-threads=1` | Diagnóstico: si el fallo desaparece con un hilo, dos tests comparten algo |

El arreglo no es un hilo, sino que cada test tenga lo suyo: su archivo, su carpeta temporal, sus datos. Comparten estado el sistema de archivos, las variables de entorno (`std::env::set_var`) y el directorio actual.

### `#[ignore]`

```rust
#[test]
#[ignore = "lento: escribe y lee un informe de 2 millones de líneas"]
fn total_of_a_huge_report() -> io::Result<()> {
    // ...
}
```

| Comando | Qué ejecuta |
|---|---|
| `cargo test` | Todos menos los ignorados, que salen como `ignored, <motivo>` |
| `cargo test -- --ignored` | Solo los ignorados |
| `cargo test -- --include-ignored` | Todos |

Es el `@Disabled` de JUnit o el `-short` de Go al revés: en Go el test lento se salta cuando pides `-short`; en Rust se salta siempre, salvo que pidas los ignorados. Ejecuta `--include-ignored` en CI o antes de entregar, o los ignorados se rompen sin que nadie lo vea.

---

## ✅ Resumen

- Un pánico es un error del programa y se prueba con `#[should_panic(expected = "...")]`, siempre con `expected`.
- Un `Err` es un valor: se prueba con `assert_eq!` (si el error tiene `PartialEq`) o con `matches!`.
- Un test que devuelve `Result` puede usar `?`, pero el fallo no dice la línea.
- `pretty_assertions` cambia la salida de `assert_eq!` por un diff, sin cambiar los tests.
- `--show-output` enseña lo que imprimen los tests, `--test-threads=1` diagnostica tests que comparten estado y `#[ignore]` aparta los lentos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - `cargo test` y el atributo `#[test]`](01-cargo-test-y-el-atributo-test.md) | [Teoría — 03 - Unitarios, integración y doctests](03-unitarios-integracion-y-doctests.md) → |

[↑ Semana 37](../README.md)
