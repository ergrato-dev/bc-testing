# Semana 15 - JavaScript Testing IX: Integracion de Estrategias y Calidad Continua

> **Etapa 1 - Testing con JavaScript** | Semana 15 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Integrar en una sola suite los enfoques vistos en la etapa JS (unit, integration, snapshot y properties).
2. Diseñar una estrategia de calidad por capas y riesgo funcional.
3. Implementar una plantilla minima de GitHub Actions para ejecutar tests y coverage.
4. Integrar analisis minimo con SonarQube para quality gate basico.
5. Diferenciar configuracion para repos publicos (Cloud free tier) y privados (Community Edition).

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Integracion de estrategias + CI quality gate con SonarQube | 2.5 h |
| Practicas | Suite integrada y pipeline minimo Actions + Sonar | 3 h |
| Proyecto | Implementacion final de etapa JS con guardrails de calidad | 2 h |
| Recursos y cierre | Retro de etapa + plan de transicion a Python | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Estrategia integrada de testing en JavaScript](./1-teoria/01-estrategia-integrada-testing-javascript.md)
2. [Plantilla minima GitHub Actions + SonarQube](./1-teoria/02-plantilla-minima-github-actions-sonarqube.md)
3. [Criterios de salida de la etapa JavaScript](./1-teoria/03-criterios-de-salida-etapa-javascript.md)

### Practicas

- [Ejercicio 01 - Suite integrada en Jest](./2-practicas/ejercicio-01-suite-integrada-jest/)
- [Ejercicio 02 - CI + SonarQube minimo](./2-practicas/ejercicio-02-ci-sonarqube-minimo/)

### Proyecto

- [Proyecto semanal: Cierre de etapa JS con quality gate](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-15/
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

Esta semana no busca agregar mas tests por cantidad. Busca consolidar criterio de calidad: que testear, por que, y como automatizar una barrera minima en CI.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 14 - Coverage y calidad de suites en Jest](../week-14/README.md) | [Semana 16 - Python Testing I: Fundamentos con pytest](../week-16/README.md) |
