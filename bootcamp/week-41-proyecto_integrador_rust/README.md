# Semana 41 - Proyecto Integrador: Etapa 5 (Rust)

> **Etapa 5 - Testing con Rust** | Semana 41 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Ordenar una suite Rust en capas (unitarios, integración, doctests y benchmarks) y separar los tests que necesitan PostgreSQL con un filtro de nextest.
2. Ejecutar la suite entera con un script, `verify.sh`, que se para en el primer fallo.
3. Hacer fallar ese script cuando la cobertura de líneas baja del 85 %, sin que los datos de ejecuciones anteriores lo engañen.
4. Escribir un reporte de testing con datos reales: capas, cobertura por archivo, a dónde va el tiempo, riesgos y próximos pasos.
5. Presentar tu suite y defender qué prueba cada capa y qué no prueba ninguna.

---

## Requisitos previos

- Semanas 37 a 40 entregadas en `rust/` de tu repositorio `qa-<nombre>-<apellido>`.
- Rust 1.100.0, cargo-nextest 0.9.152 y cargo-llvm-cov 0.9.1, los de la [semana 40](../week-40-calidad_avanzada_en_rust/README.md). Comprueba con `cargo nextest --version` y `cargo llvm-cov --version`.
- **Docker** en marcha. Comprueba con `docker version --format '{{.Server.Version}}'`.

Esta semana no tiene teoría de testing nueva: junta lo que ya sabes. Lo único nuevo es el script que lo ejecuta todo, el equivalente Rust del `verify.sh` de la [semana 36](../week-36-proyecto_integrador_go/README.md).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso: las capas de la suite, `verify.sh`, el umbral y los datos del reporte | 0.5 h |
| Planificación | 2 labs: una suite en un comando, los datos del reporte | 2.5 h |
| Desarrollo | Proyecto integrador en el proyecto hilo conductor | 6.5 h |
| Presentación | 5 minutos por aprendiz con los datos del reporte | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [La suite Rust completa](./1-teoria/01-la-suite-rust-completa.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Una suite, un comando](./2-practicas/lab-01-una-suite-un-comando/README.md)
- [Lab 02 - Los datos del reporte](./2-practicas/lab-02-datos-del-reporte/README.md)

### Proyecto

- [Suite Rust completa del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto integrador (único entregable obligatorio): rama `week-41` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 41
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-41-proyecto_integrador_rust/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-verify-sh-por-fases.svg
|-- 1-teoria/
|   `-- 01-la-suite-rust-completa.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-una-suite-un-comando/
|   `-- lab-02-datos-del-reporte/
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

Ninguna herramienta nueva: Rust 1.100.0, axum 0.8.9, sqlx 0.9.1, cargo-nextest 0.9.152 y cargo-llvm-cov 0.9.1, las de las semanas 37 a 40. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Sin los tests de PostgreSQL, la suite del museo se queda en el 84.55 % de líneas; con ellos, en el 97.96 %. Pero si `verify.sh` no empieza por `cargo llvm-cov clean --workspace`, el informe puede seguir contando lo que cubrieron los tests de PostgreSQL ayer aunque hoy no se ejecuten. Un umbral solo vale si sabes qué ejecución lo alimenta.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 40 - Calidad Avanzada en Rust](../week-40-calidad_avanzada_en_rust/README.md) | Semana 42 - Próximamente |
