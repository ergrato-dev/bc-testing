# Semana 21 - Python Testing VI: Coverage y Calidad

> **Etapa 2 - Testing con Python** | Semana 21 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Medir line y branch coverage con `pytest-cov` y leer las ramas faltantes (`6->8`).
2. Generar reportes de terminal, HTML y XML, y combinar datos de varias ejecuciones.
3. Excluir código sin lógica (`# pragma: no cover`, `exclude_also`) y fijar un umbral con `fail_under`.
4. Ejecutar mutation testing con `mutmut`, matar mutantes y reconocer los equivalentes.
5. Definir umbrales de coverage, complejidad (`ruff` C901) y duplicados, y configurar SonarQube para Python.

---

## Requisitos previos

- Semana 14: coverage en Jest (mismos conceptos en otro lenguaje).
- Semana 15: SonarQube y GitHub Actions.
- Semana 04: estructura con `src/` y `tests/` (aquí el código vive en `src/` y `pythonpath = ["src"]`).

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | pytest-cov y branch coverage, mutation testing, umbrales y SonarQube | 2.5 h |
| Prácticas | Branch coverage y umbral; mutation testing con mutmut | 3 h |
| Proyecto | Calidad de la suite y `quality-report-python.md` | 2 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [pytest-cov: branch coverage, reportes y exclusiones](./1-teoria/01-pytest-cov-branch-coverage-y-reportes.md)
2. [Mutation testing con mutmut](./1-teoria/02-mutation-testing-con-mutmut.md)
3. [Umbrales de calidad y SonarQube para Python](./1-teoria/03-umbrales-de-calidad-y-sonarqube.md)

### Prácticas

- [Ejercicio 01 - Branch coverage, exclusiones y umbral](./2-practicas/ejercicio-01-branch-coverage-y-reportes/)
- [Ejercicio 02 - Mutation testing con mutmut](./2-practicas/ejercicio-02-mutation-testing-con-mutmut/)

### Proyecto

- [Proyecto semanal: Calidad de la suite Python](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): suite con branch coverage de al menos 90%, mutantes clasificados, complejidad dentro del umbral y `quality-report-python.md`.

---

## Estructura de Carpetas

```text
week-21-coverage_y_calidad_en_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-line-vs-branch-coverage.svg
|   |-- 02-ciclo-mutation-testing.svg
|   `-- 03-umbrales-de-calidad.svg
|-- 1-teoria/
|   |-- 01-pytest-cov-branch-coverage-y-reportes.md
|   |-- 02-mutation-testing-con-mutmut.md
|   `-- 03-umbrales-de-calidad-y-sonarqube.md
|-- 2-practicas/
|   |-- ejercicio-01-branch-coverage-y-reportes/
|   `-- ejercicio-02-mutation-testing-con-mutmut/
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

`pytest` 9.1.1, `pytest-cov` 7.1.0 (la misma que `bc-testing-adso`), `mutmut` 3.8.0 y `ruff` 0.16.7, fijadas en el `pyproject.toml` de cada carpeta.

> ⚠️ `mutmut` necesita `fork`: en Windows se ejecuta dentro de WSL.

---

## Nota Importante

El coverage dice qué líneas se ejecutaron; el mutation testing dice cuáles se verificaron. Un 100% de coverage con asserts débiles es una alarma, no una meta.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 20 - TDD con Python](../week-20-tdd_con_python/README.md) | Semana 22 - Próximamente |
