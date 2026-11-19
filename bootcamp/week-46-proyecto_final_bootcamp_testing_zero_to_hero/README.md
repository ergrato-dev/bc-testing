# Semana 46 - Proyecto Final: Bootcamp Testing Zero to Hero

> **Etapa 6 - Testing Avanzado Transversal** | Semana 46 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Hacer el inventario de un repositorio con varias suites: tests, cobertura, tiempo, dónde corre cada una y qué umbral la para, con números de ejecuciones reales.
2. Juntar los riesgos de los reportes de cada suite en una lista del sistema y llevar cada riesgo a una capa, un comando, un sitio donde corre y un criterio de salida.
3. Escribir decisiones de testing con contexto, alternativas y consecuencias.
4. Escribir `testing-strategy.md` y el `README.md` de la raíz de tu repositorio.
5. Presentar tu repositorio y defender qué prueba cada suite y qué no prueba ninguna.

---

## Requisitos previos

- Las semanas 23 a 45 entregadas en tu repositorio `qa-<nombre>-<apellido>`: las suites de Python, Java, Go y Rust con sus reportes, `e2e/`, `perf/`, `security/` y los workflows de la [semana 45](../week-45-ci_cd_y_testing_automatizado/README.md).
- `ci-lab`, el repositorio de los labs de la semana 45, en GitHub y en tu equipo.
- Las herramientas de todas las suites y **Docker** en marcha.

Esta semana no tiene teoría de testing nueva: ordena y explica lo que ya existe.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | La estrategia de testing del repositorio: inventario, riesgos, sitios, criterios, decisiones y README | 0.5 h |
| Planificación | 2 labs sobre `ci-lab`: el inventario, la estrategia y el README | 2.5 h |
| Desarrollo | Proyecto final en el proyecto hilo conductor | 6.5 h |
| Presentación | 10 minutos por aprendiz con el repositorio en GitHub | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [La estrategia de testing del repositorio](./1-teoria/01-la-estrategia-de-testing-del-repositorio.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Inventario del repositorio](./2-practicas/lab-01-inventario-del-repositorio/README.md)
- [Lab 02 - Estrategia y README](./2-practicas/lab-02-estrategia-y-readme/README.md)

### Proyecto

- [La estrategia de testing de tu repositorio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto final (único entregable obligatorio): rama `week-46` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`, y la presentación. Antes de abrir el PR, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 46
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md). Comprueban los archivos; que las suites pasan lo demuestran los workflows y los *badges* del README.

---

## Estructura de Carpetas

```text
week-46-proyecto_final_bootcamp_testing_zero_to_hero/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-del-riesgo-al-criterio-de-salida.svg
|-- 1-teoria/
|   `-- 01-la-estrategia-de-testing-del-repositorio.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-inventario-del-repositorio/
|   `-- lab-02-estrategia-y-readme/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- README.md
|       `-- testing-strategy.md
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Ninguna herramienta nueva. Las de cada suite son las de sus semanas, registradas en [stack-versions](../../docs/stack-versions.md); actionlint 1.7.12 y pre-commit 4.7.0 son los de la semana 45.

---

## Nota Importante

Es la última semana. Tu repositorio queda como portafolio: lo que muestres en la presentación es lo que verá quien lo abra después. Un README que no se puede seguir o un *badge* en rojo dicen más que cualquier número de la estrategia. Y una estrategia honesta incluye lo que no probaste.

---

## Navegación

| ← Semana anterior | Siguiente |
|---|---|
| [Semana 45 - CI/CD y Testing Automatizado](../week-45-ci_cd_y_testing_automatizado/README.md) | [Índice del bootcamp](../README.md) |
