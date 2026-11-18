# Semana 38 - Traits, Dobles y Parametrización en Rust

> **Etapa 5 - Testing con Rust** | Semana 38 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Aislar las dependencias de un servicio con traits y elegir entre un servicio genérico y `dyn Trait`.
2. Escribir stubs y fakes a mano, con `RefCell` cuando el doble guarda lo que recibe desde `&self`.
3. Generar mocks con mockall (`#[automock]` y `mock!`) y programar sus expectativas: argumentos, número de llamadas y valores de retorno.
4. Parametrizar tests con rstest (`#[case]`, `#[values]`) y escribir fixtures con argumentos.
5. Probar una salida de texto con snapshots de insta, revisarlos con `cargo insta review` y compartir dobles entre archivos con `tests/common/mod.rs`.

---

## Requisitos previos

- Semana 37: la carpeta `rust/` con las reglas de tu dominio, sobre la que crece el proyecto.
- Semana 33: el servicio de Go con sus dependencias como interfaces, que portas a Rust.
- Rust 1.85 o superior (edición 2024). Los ejemplos usan Rust 1.100.0.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Traits y fakes a mano; mocks con mockall; rstest, insta y `tests/common` | 2.5 h |
| Prácticas | 4 labs: traits y fakes, mockall, rstest, insta y `tests/common` | 4 h |
| Proyecto | El servicio del dominio con traits y dobles | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Traits y fakes a mano](./1-teoria/01-traits-y-fakes-a-mano.md)
2. [Mocks con mockall](./1-teoria/02-mocks-con-mockall.md)
3. [rstest, insta y `tests/common`](./1-teoria/03-rstest-insta-y-tests-common.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Traits y fakes a mano](./2-practicas/lab-01-traits-y-fakes-a-mano/README.md)
- [Lab 02 - Mocks con mockall](./2-practicas/lab-02-mocks-con-mockall/README.md)
- [Lab 03 - Parametrización con rstest](./2-practicas/lab-03-parametrizacion-con-rstest/README.md)
- [Lab 04 - Snapshots con insta y tests/common](./2-practicas/lab-04-snapshots-con-insta-y-tests-common/README.md)

### Proyecto

- [El servicio del dominio con traits y dobles](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-38` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 38
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-38-traits_dobles_y_parametrizacion_en_rust/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-trait-como-costura.svg
|   `-- 02-ciclo-de-un-snapshot.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-traits-y-fakes-a-mano/
|   |-- lab-02-mocks-con-mockall/
|   |-- lab-03-parametrizacion-con-rstest/
|   `-- lab-04-snapshots-con-insta-y-tests-common/
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

Rust 1.100.0 y, en `[dev-dependencies]`, mockall 0.15.0, rstest 0.28.0 e insta 1.49.0, más la herramienta `cargo-insta` 1.49.0. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un mock comprueba cómo se llama a una dependencia, no qué queda al final. Si mockeas un almacén, el test se rompe cuando el servicio cambia dos `save` por uno, aunque el resultado sea el mismo. Usa fakes para los almacenes y reserva el mock para lo que de verdad es una interacción, como enviar un aviso.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 37 - Fundamentos de Testing en Rust](../week-37-fundamentos_de_testing_en_rust/README.md) | Semana 39 - Próximamente |
