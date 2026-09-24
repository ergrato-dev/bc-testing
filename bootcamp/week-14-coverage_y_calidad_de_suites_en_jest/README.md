# Semana 14 - JavaScript Testing VIII: Coverage y Calidad de Suites en Jest

> **Etapa 1 - Testing con JavaScript** | Semana 14 de 15

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Ejecutar cobertura en Jest y leer métricas de `statements`, `branches`, `functions` y `lines`.
2. Diferenciar cobertura alta de cobertura útil para el negocio.
3. Detectar tests frágiles, redundantes o con bajo poder de detección.
4. Priorizar mejoras de suite usando riesgo funcional, no solo porcentaje global.
5. Definir un plan incremental para mantener cobertura saludable en CI.

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Coverage en Jest, lectura crítica de métricas y calidad de tests | 2.5 h |
| Prácticas | Cobertura inteligente y detección de fragilidad | 3 h |
| Proyecto | Mejora de suite existente con objetivos de calidad | 2 h |
| Recursos y cierre | Refuerzo, checklist y autoevaluación | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Fundamentos de cobertura en Jest](./1-teoria/01-fundamentos-cobertura-jest.md)
2. [Interpretar métricas sin autoengaño](./1-teoria/02-interpretar-metricas-sin-autoengano.md)
3. [Mejorar calidad de suites de forma incremental](./1-teoria/03-mejorar-calidad-suite-incremental.md)

### Prácticas

- [Ejercicio 01 - Cobertura inteligente en Jest](./2-practicas/ejercicio-01-cobertura-inteligente-jest/)
- [Ejercicio 02 - Detección de tests frágiles](./2-practicas/ejercicio-02-deteccion-tests-fragiles/)

### Proyecto

- [Proyecto semanal: Hardening de suite con coverage y calidad](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-14-coverage_y_calidad_de_suites_en_jest/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-coverage-metrics-map.svg
|   |-- 02-coverage-vs-confidence.svg
|   |-- 03-quality-signals-suite.svg
|   `-- 04-incremental-hardening-loop.svg
|-- 1-teoria/
|   |-- 01-fundamentos-cobertura-jest.md
|   |-- 02-interpretar-metricas-sin-autoengano.md
|   `-- 03-mejorar-calidad-suite-incremental.md
|-- 2-practicas/
|   |-- ejercicio-01-cobertura-inteligente-jest/
|   `-- ejercicio-02-deteccion-tests-fragiles/
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

Cobertura es una métrica de observabilidad, no una garantía de calidad. El objetivo es aumentar la probabilidad de detectar regresiones críticas, no perseguir un número aislado.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 13 - Snapshot y Property-Based Testing](../week-13-snapshot_y_property_based_testing/README.md) | [Semana 15 - Cierre de etapa JavaScript: integración de estrategias](../week-15-integracion_de_estrategias_y_calidad_continua/README.md) |
