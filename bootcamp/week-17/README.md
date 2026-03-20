# Semana 17 - Python Testing II: Parametrizacion y Marks con pytest

> **Etapa 2 - Testing con Python** | Semana 17 de 24

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Usar `@pytest.mark.parametrize` para cubrir multiples casos sin duplicacion.
2. Diseñar tablas de casos con entradas validas, bordes y errores.
3. Aplicar marks (`smoke`, `regression`, `slow`) para organizar la suite.
4. Ejecutar subconjuntos de pruebas con `-m` y `-k`.
5. Mejorar trazabilidad de calidad con suites segmentadas por riesgo.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Parametrizacion efectiva, marks y seleccion de pruebas | 2.5 h |
| Practicas | Casos tabulares + ejecucion segmentada con marks | 3 h |
| Proyecto | Suite del dominio con parametrizacion y taxonomia de marks | 2 h |
| Recursos y cierre | Checklist y refuerzo de patrones | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Parametrizacion efectiva con pytest](./1-teoria/01-parametrizacion-efectiva-pytest.md)
2. [Marks para organizar y priorizar suites](./1-teoria/02-marks-organizacion-priorizacion-suites.md)
3. [Estrategia de ejecucion selectiva en CI local](./1-teoria/03-estrategia-ejecucion-selectiva-ci-local.md)

### Practicas

- [Ejercicio 01 - Parametrize con casos de negocio](./2-practicas/ejercicio-01-pytest-parametrize/)
- [Ejercicio 02 - Marks y seleccion de suite](./2-practicas/ejercicio-02-marks-y-seleccion-suite/)

### Proyecto

- [Proyecto semanal: Suite Python segmentada por riesgo](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-17/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-parametrize-case-design.svg
|   |-- 02-marks-test-portfolio-map.svg
|   `-- 03-selective-run-strategy.svg
|-- 1-teoria/
|   |-- 01-parametrizacion-efectiva-pytest.md
|   |-- 02-marks-organizacion-priorizacion-suites.md
|   `-- 03-estrategia-ejecucion-selectiva-ci-local.md
|-- 2-practicas/
|   |-- ejercicio-01-pytest-parametrize/
|   `-- ejercicio-02-marks-y-seleccion-suite/
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

## Nota Importante

Parametrizar no es meter datos al azar: cada fila debe representar una decision de calidad (happy path, borde o error esperado).

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 16 - Fundamentos con pytest](../week-16/README.md) | [Semana 18 - Mocking en Python con unittest.mock y pytest-mock](../week-18/README.md) |
