# Semana 13 - JavaScript Testing VII: Snapshot y Property-Based Testing

> **Etapa 1 - Testing con JavaScript** | Semana 13 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Aplicar snapshot testing con criterio para detectar cambios no esperados.
2. Evitar snapshots fragiles usando foco en salidas relevantes.
3. Diseñar propiedades invariantes para funciones de negocio.
4. Implementar pruebas property-based con `fast-check`.
5. Interpretar contraejemplos y simplificar casos fallidos.
6. Combinar tests de ejemplo + propiedades para mayor confianza.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Snapshot testing, invariantes y fast-check | 2.5 h |
| Practicas | Snapshot controlado y propiedades en funciones puras | 3 h |
| Proyecto | Suite combinada de ejemplos + propiedades | 2 h |
| Recursos y cierre | Refuerzo y checklist final | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Snapshot testing con intencion](./1-teoria/01-snapshot-testing-con-intencion.md)
2. [Fundamentos de property-based testing](./1-teoria/02-property-based-fundamentos.md)
3. [Diseno de propiedades con fast-check](./1-teoria/03-diseno-de-propiedades-fast-check.md)

### Practicas

- [Ejercicio 01 - Snapshot testing controlado](./2-practicas/ejercicio-01-snapshot-testing-controlado/)
- [Ejercicio 02 - Property-based con fast-check](./2-practicas/ejercicio-02-property-based-con-fast-check/)

### Proyecto

- [Proyecto semanal: Suite combinada de snapshots y propiedades](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-13-snapshot_y_property_based_testing/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-snapshot-signal-vs-noise.svg
|   |-- 02-property-testing-loop.svg
|   |-- 03-invariant-design-map.svg
|   `-- 04-shrinking-counterexample-flow.svg
|-- 1-teoria/
|   |-- 01-snapshot-testing-con-intencion.md
|   |-- 02-property-based-fundamentos.md
|   `-- 03-diseno-de-propiedades-fast-check.md
|-- 2-practicas/
|   |-- ejercicio-01-snapshot-testing-controlado/
|   `-- ejercicio-02-property-based-con-fast-check/
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

Un snapshot no reemplaza criterio de negocio. Si no sabes que cambio quieres detectar, el snapshot se vuelve ruido. Define primero la intencion del test.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 12 - APIs REST con Supertest](../week-12-apis_rest_con_supertest/README.md) | [Semana 14 - Coverage y calidad de suites en Jest](../week-14-coverage_y_calidad_de_suites_en_jest/README.md) |
