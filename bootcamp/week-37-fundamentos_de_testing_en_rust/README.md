# Semana 37 - Fundamentos de Testing en Rust

> **Etapa 5 - Testing con Rust** | Semana 37 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Crear un paquete de Rust con `cargo new --lib` y escribir tests unitarios en `#[cfg(test)] mod tests`, sin librerías externas.
2. Elegir entre `assert!`, `assert_eq!` y `assert_ne!`, con mensajes propios, y ejecutar la suite con `cargo test`, sus filtros y las opciones del binario de tests.
3. Probar un pánico con `#[should_panic(expected = "...")]` y un `Err` con `assert_eq!` o `matches!`, y escribir tests que devuelven `Result`.
4. Controlar la ejecución: `--nocapture`, `--show-output`, `--test-threads` e `#[ignore]`.
5. Distinguir unitarios, tests de integración en `tests/` y doctests, y decidir cuándo probar una función privada.

---

## Requisitos previos

- Semana 32: la suite Go de tu dominio, de donde portas las reglas.
- Rust 1.85 o superior (edición 2024). Comprueba con `cargo --version`. Los ejemplos usan Rust 1.100.0; si no lo tienes, la [teoría 01](./1-teoria/01-cargo-test-y-el-atributo-test.md#instalar-rust) explica cómo instalarlo.

No hace falta saber Rust: los labs explican cada construcción del lenguaje que usan. Si es tu primer contacto, los capítulos 3 a 9 de [The Rust Programming Language](https://doc.rust-lang.org/book/) cubren la base.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | `cargo test` y `#[test]`; pánicos, `Result` y control de la ejecución; unitarios, integración y doctests | 2.5 h |
| Prácticas | 4 labs: primer test, pánicos y diffs, ignore y paralelismo, integración y doctests | 4 h |
| Proyecto | Las reglas del dominio en Rust y la comparativa con Go | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [`cargo test` y el atributo `#[test]`](./1-teoria/01-cargo-test-y-el-atributo-test.md)
2. [Pánicos, Result y control de la ejecución](./1-teoria/02-panics-result-y-asserts.md)
3. [Unitarios, integración y doctests](./1-teoria/03-unitarios-integracion-y-doctests.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer test con cargo test](./2-practicas/lab-01-primer-test-con-cargo-test/README.md)
- [Lab 02 - Pánicos, Result y diffs](./2-practicas/lab-02-panics-result-y-diffs/README.md)
- [Lab 03 - Ignore, salida e hilos](./2-practicas/lab-03-ignore-salida-e-hilos/README.md)
- [Lab 04 - Integración y doctests](./2-practicas/lab-04-integracion-y-doctests/README.md)

### Proyecto

- [Las reglas del dominio en Rust](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-37` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 37
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-37-fundamentos_de_testing_en_rust/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-tres-tipos-de-test.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-test-con-cargo-test/
|   |-- lab-02-panics-result-y-diffs/
|   |-- lab-03-ignore-salida-e-hilos/
|   `-- lab-04-integracion-y-doctests/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Rust 1.100.0, fijado en el `rust-toolchain.toml` del proyecto, y `pretty_assertions` 1.4.1 en `[dev-dependencies]`. El resto es la librería estándar. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

El tipo elimina casos de test, pero no todos. Con `u32`, un valor negativo no compila y no necesita test; a cambio aparece el máximo: `u32::MAX * 50` desborda. En modo debug, `cargo test` entra en pánico y lo encuentra; compilado con `--release`, el número da la vuelta sin avisar. Prueba siempre el valor máximo de cada entrada numérica.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 36 - Proyecto Integrador: Etapa 4 (Go)](../week-36-proyecto_integrador_go/README.md) | Semana 38 - Próximamente |
