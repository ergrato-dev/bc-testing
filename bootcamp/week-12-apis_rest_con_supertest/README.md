# Semana 12 - JavaScript Testing VI: APIs REST con Supertest

> **Etapa 1 - Testing con JavaScript** | Semana 12 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Escribir tests de integracion para endpoints REST con Supertest.
2. Validar codigos de estado HTTP y estructura de respuesta JSON.
3. Probar flujos GET, POST y validaciones de payload.
4. Diseñar casos para errores esperados (400, 404, 409, 500).
5. Aplicar aislamiento razonable entre capas usando repositorios in-memory.
6. Mantener suites legibles y repetibles bajo patron AAA.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Supertest, contratos HTTP y estrategias de validacion | 2.5 h |
| Practicas | Endpoints basicos, validacion y manejo de errores | 3 h |
| Proyecto | API del dominio con suite REST incremental | 2 h |
| Recursos y cierre | Refuerzo y checklist final | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Fundamentos de testing de APIs REST con Supertest](./1-teoria/01-fundamentos-supertest.md)
2. [Diseno de casos HTTP y contratos de respuesta](./1-teoria/02-casos-http-y-contratos.md)
3. [Manejo de errores y estabilidad en tests de API](./1-teoria/03-errores-y-estabilidad-api-tests.md)

### Practicas

- [Ejercicio 01 - Endpoints GET y POST](./2-practicas/ejercicio-01-endpoints-get-y-post/)
- [Ejercicio 02 - Validacion y errores HTTP](./2-practicas/ejercicio-02-validacion-y-errores-http/)

### Proyecto

- [Proyecto semanal: API REST del dominio asignado](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-12-apis_rest_con_supertest/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-supertest-request-flow.svg
|   |-- 02-http-status-contract-matrix.svg
|   |-- 03-api-test-pyramid-slice.svg
|   `-- 04-error-handling-decision-map.svg
|-- 1-teoria/
|   |-- 01-fundamentos-supertest.md
|   |-- 02-casos-http-y-contratos.md
|   `-- 03-errores-y-estabilidad-api-tests.md
|-- 2-practicas/
|   |-- ejercicio-01-endpoints-get-y-post/
|   `-- ejercicio-02-validacion-y-errores-http/
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

En API testing, no basta con validar `status`; tambien debes verificar contrato de datos, mensajes de error y consistencia del comportamiento para evitar falsos verdes.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 11 - TDD con Jest](../week-11-tdd_con_jest/README.md) | [Semana 13 - Snapshot y Property-Based Testing](../week-13-snapshot_y_property_based_testing/README.md) |
