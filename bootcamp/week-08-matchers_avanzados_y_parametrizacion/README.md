# Semana 08 - JavaScript Testing II: Matchers Avanzados y Parametrizacion

> **Etapa 1 - Testing con JavaScript** | Semana 8 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Seleccionar matchers avanzados de Jest segun tipo de dato y comportamiento esperado.
2. Diseñar tests parametrizados con `test.each` para reducir duplicacion.
3. Mejorar legibilidad de assertions complejas en objetos, arrays y errores.
4. Mantener tests aislados y expresivos con patron AAA.
5. Analizar fallos de assertions para depurar con mayor velocidad.
6. Consolidar una base robusta para testing asincrono y mocking avanzado.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Matchers avanzados, parametrizacion y lectura de errores | 2.5 h |
| Practicas | Ejercicios guiados con `test.each` y assertions complejas | 3 h |
| Proyecto | Suite del dominio con casos parametrizados | 2 h |
| Recursos y cierre | Refuerzo y checklist final | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Matchers avanzados en Jest](./1-teoria/01-matchers-avanzados-jest.md)
2. [Tests parametrizados con `test.each`](./1-teoria/02-tests-parametrizados-jest.md)
3. [Legibilidad de assertions y analisis de fallos](./1-teoria/03-legibilidad-assertions-y-fallos.md)

### Practicas

- [Ejercicio 01 - Matchers avanzados](./2-practicas/ejercicio-01-matchers-avanzados/)
- [Ejercicio 02 - Parametrizacion con `test.each`](./2-practicas/ejercicio-02-parametrizacion-test-each/)

### Proyecto

- [Proyecto semanal: Suite parametrizada del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```
week-08-matchers_avanzados_y_parametrizacion/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-mapa-matchers-avanzados.svg
|   |-- 02-flujo-test-each.svg
|   |-- 03-seleccion-matchers.svg
|   |-- 04-custom-matcher-concepto.svg
|   `-- 05-analisis-fallo-jest.svg
|-- 1-teoria/
|   |-- 01-matchers-avanzados-jest.md
|   |-- 02-tests-parametrizados-jest.md
|   `-- 03-legibilidad-assertions-y-fallos.md
|-- 2-practicas/
|   |-- ejercicio-01-matchers-avanzados/
|   `-- ejercicio-02-parametrizacion-test-each/
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

Esta semana apunta a escribir menos codigo repetido y mas intencion de prueba. Un buen matcher y una buena tabla de casos hacen la suite mas mantenible.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 07 - Jest avanzado y organizacion](../week-07-jest_avanzado_y_organizacion_de_suites/README.md) | [Semana 09 - Testing asincrono con Jest](../week-09-testing_asincrono_con_jest/README.md) |
