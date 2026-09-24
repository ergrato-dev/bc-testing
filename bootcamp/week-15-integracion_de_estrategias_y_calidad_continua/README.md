# Semana 15 - JavaScript Testing IX: Integración de Estrategias y Calidad Continua

> **Etapa 1 - Testing con JavaScript** | Semana 15 de 15

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Integrar en una sola suite los enfoques vistos en la etapa JS (unit, integration, snapshot y properties).
2. Diseñar una estrategia de calidad por capas y riesgo funcional.
3. Implementar una plantilla mínima de GitHub Actions para ejecutar tests y coverage.
4. Integrar análisis mínimo con SonarQube para quality gate básico.
5. Diferenciar configuración para repos públicos (Cloud free tier) y privados (Community Edition).

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Integración de estrategias + CI quality gate con SonarQube | 0.5 h |
| Prácticas | Suite integrada y pipeline mínimo Actions + Sonar | 2 h |
| Proyecto integrador | API Express + Supertest, coverage con umbral y pipeline con quality gate | 5 h |
| Recursos y cierre | Retro de etapa + plan de transición a Python | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Estrategia integrada de testing en JavaScript](./1-teoria/01-estrategia-integrada-testing-javascript.md)
2. [Plantilla mínima GitHub Actions + SonarQube](./1-teoria/02-plantilla-minima-github-actions-sonarqube.md)
3. [Criterios de salida de la etapa JavaScript](./1-teoria/03-criterios-de-salida-etapa-javascript.md)

### Prácticas

- [Ejercicio 01 - Suite integrada en Jest](./2-practicas/ejercicio-01-suite-integrada-jest/)
- [Ejercicio 02 - CI + SonarQube mínimo](./2-practicas/ejercicio-02-ci-sonarqube-minimo/)

### Proyecto

- [Proyecto semanal: Cierre de etapa JS con quality gate](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-15-integracion_de_estrategias_y_calidad_continua/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-mapa-integracion-estrategias.svg
|   |-- 02-ci-quality-gate-flow.svg
|   `-- 03-sonarqube-public-vs-private.svg
|-- 1-teoria/
|   |-- 01-estrategia-integrada-testing-javascript.md
|   |-- 02-plantilla-minima-github-actions-sonarqube.md
|   `-- 03-criterios-de-salida-etapa-javascript.md
|-- 2-practicas/
|   |-- ejercicio-01-suite-integrada-jest/
|   `-- ejercicio-02-ci-sonarqube-minimo/
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

Esta semana no busca agregar más tests por cantidad. Busca consolidar criterio de calidad: qué testear, por qué, y cómo automatizar una barrera mínima en CI.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 14 - Coverage y calidad de suites en Jest](../week-14-coverage_y_calidad_de_suites_en_jest/README.md) | [Semana 16 - Python Testing I: Fundamentos con pytest](../week-16-fundamentos_con_pytest/README.md) |
