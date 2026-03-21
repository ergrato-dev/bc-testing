# Semana 16 - Python Testing I: Fundamentos con pytest

> **Etapa 2 - Testing con Python** | Semana 16 de 24

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Configurar y ejecutar pruebas basicas con `pytest`.
2. Escribir tests con nomenclatura clara en `snake_case`.
3. Aplicar el patron AAA en Python de forma consistente.
4. Utilizar fixtures basicas para evitar duplicacion.
5. Migrar criterio de calidad desde JavaScript hacia Python.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Fundamentos pytest, asserts, fixtures iniciales | 2.5 h |
| Practicas | Primeros tests + setup reusable con fixtures | 3 h |
| Proyecto | Suite inicial del dominio en Python | 2 h |
| Recursos y cierre | Refuerzo + checklist de transicion | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Introduccion a pytest y flujo de ejecucion](./1-teoria/01-introduccion-pytest-flujo-ejecucion.md)
2. [Asserts efectivos y estructura AAA en Python](./1-teoria/02-asserts-efectivos-y-patron-aaa-python.md)
3. [Fixtures basicas para reducir duplicacion](./1-teoria/03-fixtures-basicas-reduccion-duplicacion.md)

### Practicas

- [Ejercicio 01 - Primeros tests con pytest](./2-practicas/ejercicio-01-primeros-tests-pytest/)
- [Ejercicio 02 - Fixtures basicas y setup reusable](./2-practicas/ejercicio-02-fixtures-basicas/)

### Proyecto

- [Proyecto semanal: Base de suite Python para dominio asignado](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-16-fundamentos_con_pytest/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-pytest-execution-flow.svg
|   |-- 02-aaa-python-assertions-map.svg
|   `-- 03-fixture-scope-and-reuse.svg
|-- 1-teoria/
|   |-- 01-introduccion-pytest-flujo-ejecucion.md
|   |-- 02-asserts-efectivos-y-patron-aaa-python.md
|   `-- 03-fixtures-basicas-reduccion-duplicacion.md
|-- 2-practicas/
|   |-- ejercicio-01-primeros-tests-pytest/
|   `-- ejercicio-02-fixtures-basicas/
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

Esta semana inicia Python desde fundamentos, pero con criterio profesional: testear comportamiento observable, no solo ejecutar codigo.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 15 - Integracion de estrategias y calidad continua](../week-15-integracion_de_estrategias_y_calidad_continua/README.md) | [Semana 17 - pytest avanzado: parametrizacion y marks](../week-17-parametrizacion_y_marks_con_pytest/README.md) |
