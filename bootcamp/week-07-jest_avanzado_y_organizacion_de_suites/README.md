# Semana 07 - JavaScript Testing I: Jest Avanzado y Organizacion de Suites

> **Etapa 1 - Testing con JavaScript** | Semana 7 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Organizar suites de tests en Jest con `describe` anidados por comportamiento.
2. Aplicar hooks de ciclo de vida (`beforeAll`, `beforeEach`, `afterEach`, `afterAll`) con criterio.
3. Diferenciar `mock`, `stub` y `spy` en escenarios basicos de aislamiento.
4. Diseñar suites legibles con patron AAA y nombres de test profesionales.
5. Ejecutar tests por archivo y por patron usando `yarn` o `pnpm`.
6. Preparar una base de trabajo para temas avanzados de mocking y asincronia.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Organizacion, hooks y fundamentos de test doubles | 2.5 h |
| Practicas | Ejercicios guiados de estructura y aislamiento basico | 3 h |
| Proyecto | Suite inicial modular del dominio asignado | 2 h |
| Recursos y cierre | Refuerzo y checklist de calidad | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Organizacion de suites y convenciones de Jest](./1-teoria/01-organizacion-suites-jest.md)
2. [Ciclo de vida de pruebas con hooks](./1-teoria/02-ciclo-de-vida-hooks-jest.md)
3. [Introduccion a mocks, stubs y spies](./1-teoria/03-intro-mocks-stubs-spies-jest.md)

### Practicas

- [Ejercicio 01 - Organizacion de suite Jest](./2-practicas/ejercicio-01-organizacion-suite-jest/)
- [Ejercicio 02 - Hooks y mocks basicos](./2-practicas/ejercicio-02-hooks-y-mocks-basicos/)

### Proyecto

- [Proyecto semanal: Suite modular del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```
week-07-jest_avanzado_y_organizacion_de_suites/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-mapa-suites-jest.svg
|   |-- 02-ciclo-hooks-jest.svg
|   |-- 03-test-doubles-jest.svg
|   `-- 04-flujo-ejecucion-jest.svg
|-- 1-teoria/
|   |-- 01-organizacion-suites-jest.md
|   |-- 02-ciclo-de-vida-hooks-jest.md
|   `-- 03-intro-mocks-stubs-spies-jest.md
|-- 2-practicas/
|   |-- ejercicio-01-organizacion-suite-jest/
|   `-- ejercicio-02-hooks-y-mocks-basicos/
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

En esta semana comienza la etapa enfocada solo en JavaScript. La prioridad es mejorar la arquitectura de tests antes de aumentar complejidad tecnica.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 06 - Cierre de Fundamentos](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) | [Semana 08 - Jest: matchers avanzados y parametrizacion](../week-08-matchers_avanzados_y_parametrizacion/README.md) |
