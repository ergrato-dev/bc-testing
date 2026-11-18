# Lab 01 - Traits y fakes a mano

## Objetivo

Aislar las dos dependencias de un servicio con traits y probarlo con dobles escritos a mano: un stub, un fake que guarda lo que recibe y un doble que falla. Ver por qué el fake necesita `RefCell` y cuándo hace falta `dyn Trait`.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
cargo test
#    Compiling lab01 v0.1.0 (.../starter)
#     Finished `test` profile [unoptimized + debuginfo] target(s) in 0.12s
# ...
# running 0 tests
# ...
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `Cargo.toml` | El paquete `lab01`, sin dependencias: los dobles de este lab se escriben a mano |
| `src/lib.rs` | El mostrador de préstamos (`LoanDesk`), sus dos traits (`Registry` y `Notifier`) y la función `broadcast`. Al final, los tests comentados por PASOS |

Lee el código antes de empezar:

| Pieza | Qué hace |
|---|---|
| `trait Registry` | `is_accredited(&self, institution)`: si la institución puede recibir préstamos. En producción consultaría un registro externo |
| `trait Notifier` | `send(&self, notice)`: envía el aviso. En producción sería un correo; devuelve `Err` con el motivo si falla |
| `LoanDesk<R: Registry, N: Notifier>` | El servicio. Es genérico: `R` y `N` son cualquier tipo que implemente cada trait |
| `approve` | Rechaza si la institución no está acreditada; si lo está, envía el aviso y lo devuelve |
| `broadcast` | Envía el aviso por varios canales, que pueden ser de tipos distintos, y cuenta cuántos lo entregaron |

## Paso a paso

### Paso 1: un stub y un servicio genérico (PASO 1)

1. Descomenta el bloque `// PASO 1` (borra el `// ` del principio de cada línea; en VS Code, selecciona las líneas y pulsa `Ctrl+/`).
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 1 test
   # test tests::rejects_unaccredited_institution ... ok
   # ...
   ```

El bloque declara dos dobles:

- `StubRegistry` es un **stub**: devuelve siempre el valor de su campo `accredited`. El `_` de `_institution` dice que el parámetro no se usa a propósito.
- `SilentNotifier` es un `struct` sin campos que acepta cualquier aviso. Aquí no importa lo que envía: el test solo mira el `Err`.

Cada uno implementa su trait con un bloque `impl Registry for StubRegistry { ... }`. Si borras el método `is_accredited` de ese bloque, el compilador lo rechaza (`error[E0046]: not all trait items implemented, missing: is_accredited`): en Rust el tipo dice qué trait cumple y el compilador lo comprueba en el propio `impl` ([teoría 01](../../1-teoria/01-traits-y-fakes-a-mano.md#el-trait-como-costura)).

El test construye `LoanDesk { registry: StubRegistry { ... }, notifier: SilentNotifier }`. El compilador deduce `R = StubRegistry` y `N = SilentNotifier` y genera una versión de `LoanDesk` para esa pareja.

### Paso 2: un fake que guarda los avisos (PASO 2)

1. Descomenta el bloque `// PASO 2`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 3 tests
   # test tests::approved_loan_sends_one_notice ... ok
   # test tests::rejected_loan_sends_nothing ... ok
   # test tests::rejects_unaccredited_institution ... ok
   # ...
   ```

`FakeNotifier` guarda cada aviso en una lista. El trait dice `send(&self, ...)`, una referencia compartida que no deja modificar el `struct`, así que la lista va dentro de un `RefCell`: `self.sent.borrow_mut().push(...)` la modifica y `desk.notifier.sent.borrow()` la lee en el test. El [Experimento 1](#experimento-1-un-fake-sin-refcell) muestra qué pasa sin `RefCell`.

Fíjate en `desk.notifier.sent`: el test llega al campo del fake porque `LoanDesk` es genérico y `desk.notifier` sigue siendo un `FakeNotifier`. `#[derive(Default)]` crea `FakeNotifier::default()` con la lista vacía.

Los dos tests comprueban el **estado** al final: un aviso enviado igual al devuelto, o ninguno si se rechaza.

### Paso 3: un doble que falla (PASO 3)

1. Descomenta el bloque `// PASO 3`.
2. Ejecuta `cargo test`: pasan 4.

`FailingNotifier` siempre devuelve `Err("smtp down")`. Es la forma de probar el camino de error sin un servidor de correo caído: `approve` convierte el error con `map_err(DeskError::NotSent)` y el test compara el resultado con `assert_eq!`.

### Paso 4: canales de tipos distintos (PASO 4)

1. Descomenta el bloque `// PASO 4`.
2. Ejecuta:

   ```bash
   cargo test
   # ...
   # running 5 tests
   # test tests::approved_loan_sends_one_notice ... ok
   # test tests::notifier_failure_is_reported ... ok
   # test tests::broadcast_counts_delivered_channels ... ok
   # test tests::rejected_loan_sends_nothing ... ok
   # test tests::rejects_unaccredited_institution ... ok
   #
   # test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
   # ...
   ```

`broadcast` recibe `&[Box<dyn Notifier>]`: una lista de punteros a "algo que implementa `Notifier`". Cada elemento puede ser de un tipo distinto, y `Box::new(...)` lo guarda en memoria dinámica. De los tres canales del test, `FailingNotifier` falla y los otros dos entregan: el resultado es 2.

Aquí `dyn` es necesario porque la lista mezcla tipos. En `LoanDesk` no lo es, y con `Box<dyn Notifier>` el test ya no podría leer `desk.notifier.sent` ([teoría 01](../../1-teoria/01-traits-y-fakes-a-mano.md#genéricos-o-dyn)).

## Experimentos

### Experimento 1: un fake sin `RefCell`

1. En `FakeNotifier`, cambia el campo por `sent: Vec<Notice>,` y la línea de `send` por `self.sent.push(notice.clone());`.
2. Ejecuta `cargo test`. Hay tres errores; el que importa es este:

   ```text
   error[E0596]: cannot borrow `self.sent` as mutable, as it is behind a `&` reference
      --> src/lib.rs:105:13
       |
   105 |             self.sent.push(notice.clone());
       |             ^^^^^^^^^ `self` is a `&` reference, so it cannot be borrowed as mutable
   ```

   Los otros dos (`no method named borrow found for struct Vec<Notice>`) vienen de los tests, que leen la lista con `borrow()`.

3. El compilador propone cambiar el trait a `fn send(&mut self, ...)`. Funcionaría, pero obligaría a `approve` a recibir `&mut self` solo por el doble: un cliente de correo real envía con `&self`. El trait se diseña para producción; el doble se adapta con `RefCell`.
4. Deshaz los cambios.

### Experimento 2: dos tipos en un `vec!`

1. En `broadcast_counts_delivered_channels`, sustituye la declaración de `channels` (las cinco líneas) por:

   ```rust
   let channels = vec![SilentNotifier, FailingNotifier];
   ```

2. Ejecuta `cargo test`:

   ```text
   error[E0308]: mismatched types
      --> src/lib.rs:152:45
       |
   152 |         let channels = vec![SilentNotifier, FailingNotifier];
       |                                             ^^^^^^^^^^^^^^^ expected `SilentNotifier`, found `FailingNotifier`
   ```

   Un `Vec` guarda un solo tipo: el del primer elemento. Para mezclar tipos, cada elemento va en un `Box<dyn Notifier>`.

3. Deshaz el cambio.

## Comprobación final

```bash
cargo test
# ...
# test result: ok. 5 passed; 0 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s
# ...
```

## Preguntas de cierre

1. ¿Qué diferencia hay entre cómo cumple un tipo una interfaz en Go y cómo implementa un trait en Rust? ¿Qué ventaja tiene cuando cambias el trait?
2. ¿Por qué el test puede leer `desk.notifier.sent`? ¿Qué pasaría si `LoanDesk` guardara un `Box<dyn Notifier>`?
3. ¿Por qué el fake usa `RefCell` en lugar de cambiar el trait a `&mut self`?
4. ¿Cuál de los dobles del lab es un stub y cuál un fake? ¿Qué comprueba el test con cada uno?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Mocks con mockall](../lab-02-mocks-con-mockall/README.md) → |

[↑ Semana 38](../../README.md)
