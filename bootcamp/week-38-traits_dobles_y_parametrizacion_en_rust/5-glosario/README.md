# Glosario Semana 38 - Traits, Dobles y Parametrización en Rust

## A

- **`#[automock]`**: atributo de mockall que genera `MockTrait` a partir de un trait. Con `#[cfg_attr(test, mockall::automock)]` solo se genera para los tests unitarios.

## B

- **`Box<dyn Trait>`**: un puntero a un valor de cualquier tipo que implemente el trait. Permite guardar tipos distintos en una misma lista.

## C

- **`#[case]`**: atributo de rstest que da valores a los parámetros de un test. Cada `#[case(...)]` genera un test; `#[case::nombre(...)]` le pone nombre.
- **`cargo insta`**: herramienta aparte (`cargo install cargo-insta`) que revisa los snapshots: `review`, `accept`, `reject` y `pending-snapshots`.
- **Costura** (*seam*): el punto del código donde una dependencia se puede cambiar sin tocar el código que la usa. En Rust, un trait.

## D

- **Despacho dinámico**: con `dyn Trait`, el método se busca al ejecutar en una tabla (*vtable*).
- **Despacho estático**: con un genérico, el compilador sabe el tipo y llama al método directamente.

## E

- **`expect_<método>()`**: el método de un mock de mockall que crea una expectativa para esa llamada. Se configura con `with`, `withf`, `times`, `never`, `returning` y `return_const`.

## F

- **Fake**: un doble que funciona de verdad con un atajo, como un almacén en memoria.
- **`#[fixture]`**: atributo de rstest que convierte una función en un dato que los tests piden por el nombre del parámetro. Admite argumentos con `#[default(...)]` y `#[with(...)]`.

## G

- **Genérico**: un tipo o función con parámetros de tipo (`LoanDesk<R, N>`). El test conserva el tipo concreto de cada doble.

## I

- **`impl Trait for Tipo`**: el bloque que dice que un tipo implementa un trait. El compilador comprueba ahí cada método.
- **insta**: crate de snapshots. Guarda la salida aprobada en `tests/snapshots/*.snap` o dentro del test (`@"..."`).

## M

- **`mock!`**: macro de mockall que declara un mock copiando la firma del trait. Se usa en `tests/`, donde `#[automock]` no genera nada.
- **Monomorfización**: el compilador genera una copia del código genérico para cada combinación de tipos que se usa.
- **Mutabilidad interior**: modificar un valor a través de `&self`. `RefCell` comprueba los préstamos al ejecutar; `Mutex`, entre hilos.

## P

- **`predicate::eq(v)`**: predicado de mockall que acepta un argumento igual a `v`. Con un parámetro `&T`, compara el `T`.

## R

- **`RefCell<T>`**: contenedor que permite `borrow_mut()` desde `&self`. Si dos préstamos chocan, entra en pánico con `already borrowed`.
- **rstest**: crate de fixtures y tests parametrizados para Rust.

## S

- **`.snap.new`**: la salida de un snapshot nuevo o cambiado, pendiente de revisar. No se sube al repositorio.
- **Snapshot**: la salida aprobada de una función, guardada para compararla en cada ejecución.
- **Stub**: un doble que devuelve respuestas fijas.

## T

- **Trait**: un conjunto de métodos que un tipo puede implementar. Es el equivalente de la interfaz de Go o Java.
- **`tests/common/mod.rs`**: el lugar del código compartido entre archivos de `tests/`. Cargo no lo trata como un archivo de tests.

## V

- **`#[values(...)]`**: atributo de rstest que genera un test por cada combinación de valores de los parámetros.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 39 - Async, APIs y BD en Rust](../../week-39-async_apis_y_bd_en_rust/README.md) → |

[↑ Semana 38](../README.md)
