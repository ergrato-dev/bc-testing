# Semana 40 - Calidad Avanzada en Rust

> **Etapa 5 - Testing con Rust** | Semana 40 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Escribir propiedades con proptest, elegir estrategias que lleguen a los extremos y leer el contraejemplo reducido.
2. Encontrar pánicos y entradas aceptadas por error con cargo-fuzz, y convertir cada hallazgo en un test de regresión.
3. Medir con criterion, comparar con una línea base y distinguir una mejora real del ruido.
4. Medir la cobertura con cargo-llvm-cov y encontrar con cargo-mutants lo que los tests ejecutan sin comprobar.
5. Usar cargo-nextest y clippy con `-D warnings` como parte de la verificación de la suite.

---

## Requisitos previos

- Semana 39: la suite Rust del dominio en `rust/`, con la API, los almacenes en PostgreSQL y sus tests.
- Semana 35: la misma calidad en Go (fuzzing, benchmarks, cobertura, linters), con la que se compara cada herramienta.
- Semanas 20 y 21: Hypothesis y mutmut en Python.
- Docker en marcha y Rust 1.85 o superior (edición 2024). Los ejemplos usan Rust 1.100.0. El lab 02 necesita además Rust nightly, en Linux, macOS o WSL.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | proptest; cargo-fuzz; criterion; cobertura, nextest, mutantes y clippy | 2.5 h |
| Prácticas | 4 labs: proptest, cargo-fuzz, criterion, cobertura y mutantes | 4 h |
| Proyecto | Calidad avanzada en la suite Rust del dominio | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Propiedades con proptest](./1-teoria/01-propiedades-con-proptest.md)
2. [Fuzzing con cargo-fuzz](./1-teoria/02-fuzzing-con-cargo-fuzz.md)
3. [Benchmarks con criterion](./1-teoria/03-benchmarks-con-criterion.md)
4. [Cobertura, nextest, mutantes y clippy](./1-teoria/04-cobertura-nextest-mutantes-y-clippy.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Propiedades con proptest](./2-practicas/lab-01-propiedades-con-proptest/README.md)
- [Lab 02 - Fuzzing con cargo-fuzz](./2-practicas/lab-02-fuzzing-con-cargo-fuzz/README.md)
- [Lab 03 - Benchmarks con criterion](./2-practicas/lab-03-benchmarks-con-criterion/README.md)
- [Lab 04 - Cobertura, mutantes y clippy](./2-practicas/lab-04-cobertura-mutantes-y-clippy/README.md)

### Proyecto

- [Calidad avanzada en la suite Rust del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-40` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 40
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). La primera arranca el PostgreSQL de tu `compose.yaml`: necesita Docker. Además necesitas cargo-llvm-cov instalado.

---

## Estructura de Carpetas

```text
week-40-calidad_avanzada_en_rust/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-shrinking-de-proptest.svg
|   `-- 02-cobertura-frente-a-mutantes.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-propiedades-con-proptest/
|   |-- lab-02-fuzzing-con-cargo-fuzz/
|   |-- lab-03-benchmarks-con-criterion/
|   `-- lab-04-cobertura-mutantes-y-clippy/
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

Rust 1.100.0 y nightly 1.102.0 (2026-11-17) para cargo-fuzz; en `[dev-dependencies]` proptest 1.12.0 y criterion 0.8.2; con `cargo install`, cargo-fuzz 0.13.2 (con libfuzzer-sys 0.4.13), cargo-llvm-cov 0.9.1, cargo-nextest 0.9.152 y cargo-mutants 27.1.0. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Ninguna de estas herramientas sustituye a las otras. La cobertura dice qué se ejecutó; los mutantes, qué se comprueba; las propiedades y el fuzzer, qué entradas no se te ocurrieron; el benchmark, cuánto cuesta. En el proyecto del museo, la propiedad contra PostgreSQL encontró un valor que la validación aceptaba y la base de datos no podía guardar, con un 97 % de cobertura y todos los tests en verde.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 39 - Async, APIs y BD en Rust](../week-39-async_apis_y_bd_en_rust/README.md) | [Semana 41 - Proyecto Integrador: Etapa 5 (Rust)](../week-41-proyecto_integrador_rust/README.md) |
