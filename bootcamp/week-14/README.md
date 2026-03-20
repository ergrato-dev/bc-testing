# Semana 14 - JavaScript Testing VIII: Coverage y Calidad de Suites en Jest

> **Etapa 1 - Testing con JavaScript** | Semana 14 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Ejecutar cobertura en Jest y leer metricas de `statements`, `branches`, `functions` y `lines`.
2. Diferenciar cobertura alta de cobertura util para el negocio.
3. Detectar tests fragiles, redundantes o con bajo poder de deteccion.
4. Priorizar mejoras de suite usando riesgo funcional, no solo porcentaje global.
5. Definir un plan incremental para mantener cobertura saludable en CI.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Coverage en Jest, lectura critica de metricas y calidad de tests | 2.5 h |
| Practicas | Cobertura inteligente y deteccion de fragilidad | 3 h |
| Proyecto | Mejora de suite existente con objetivos de calidad | 2 h |
| Recursos y cierre | Refuerzo, checklist y autoevaluacion | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Fundamentos de cobertura en Jest](./1-teoria/01-fundamentos-cobertura-jest.md)
2. [Interpretar metricas sin autoengano](./1-teoria/02-interpretar-metricas-sin-autoengano.md)
3. [Mejorar calidad de suites de forma incremental](./1-teoria/03-mejorar-calidad-suite-incremental.md)

### Practicas

- [Ejercicio 01 - Cobertura inteligente en Jest](./2-practicas/ejercicio-01-cobertura-inteligente-jest/)
- [Ejercicio 02 - Deteccion de tests fragiles](./2-practicas/ejercicio-02-deteccion-tests-fragiles/)

### Proyecto

- [Proyecto semanal: Hardening de suite con coverage y calidad](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-14/
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

Cobertura es una metrica de observabilidad, no una garantia de calidad. El objetivo es aumentar la probabilidad de detectar regresiones criticas, no perseguir un numero aislado.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 13 - Snapshot y Property-Based Testing](../week-13/README.md) | [Semana 15 - Cierre de etapa JavaScript: integracion de estrategias](../week-15/README.md) |
