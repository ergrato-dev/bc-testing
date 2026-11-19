# Semana 03 - Primeros Tests Automatizados con JavaScript

> **Etapa 0 - Fundamentos de Testing** | Semana 3 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Crear un proyecto de tests con Node.js 22, pnpm y Jest, y explicar cada archivo de configuración.
2. Escribir tests con `describe`, `it` y `expect`, ordenados en Arrange, Act y Assert y con nombres `should ... when ...`.
3. Elegir el matcher de cada valor: `toBe`, `toEqual`, `toBeCloseTo`, `toBeNull` y `toThrow`.
4. Leer un informe de Jest y distinguir un fallo de aserción de un error de ejecución.
5. Convertir casos de partición y valores límite en tests.
6. Comprobar con mutaciones hechas a mano que un test en verde detecta los bugs.

---

## Requisitos previos

- La [semana 2](../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md): partición, valores límite y tu `casos-de-prueba.md`.
- Una terminal (en Windows, WSL), git y un editor de texto. VS Code es el que usan los ejemplos.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Node.js, pnpm y Jest; estructura de un test; matchers, fallos y ejecución | 2.5 h |
| Prácticas | 4 labs: primer proyecto, AAA y matchers, casos a tests, tests que mienten | 4 h |
| Proyecto | Primera suite de tests de una funcionalidad del dominio | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Node.js, pnpm y Jest](./1-teoria/01-setup-jest.md)
2. [Estructura de un test: describe, it, expect y AAA](./1-teoria/02-estructura-test-jest.md)
3. [Matchers, errores y ejecución](./1-teoria/03-matchers-y-ejecucion.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer proyecto con Jest](./2-practicas/lab-01-primer-proyecto-jest/README.md)
- [Lab 02 - AAA y matchers](./2-practicas/lab-02-aaa-y-matchers/README.md)
- [Lab 03 - De casos de prueba a tests](./2-practicas/lab-03-de-casos-a-tests/README.md)
- [Lab 04 - Tests que mienten](./2-practicas/lab-04-tests-que-mienten/README.md)

### Proyecto

- [Primera suite de tests del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-03` con tus funciones, su suite y `mutaciones.md`, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-03 ./scripts/verificar-semana.sh 03
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-03-primeros_tests_automatizados_con_javascript/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-test-jest.svg
|   `-- 02-patron-aaa.svg
|-- 1-teoria/
|   |-- 01-setup-jest.md
|   |-- 02-estructura-test-jest.md
|   `-- 03-matchers-y-ejecucion.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-proyecto-jest/
|   |-- lab-02-aaa-y-matchers/
|   |-- lab-03-de-casos-a-tests/
|   `-- lab-04-tests-que-mienten/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- package.json
|       |-- pnpm-workspace.yaml
|       |-- pnpm-lock.yaml
|       |-- mutaciones.md
|       |-- src/domain.js
|       `-- tests/domain.test.js
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

| Herramienta | Versión | Dónde se fija |
|---|---|---|
| Node.js | 22 | La instalas tú; `engines` del proyecto |
| pnpm | 12.6.0 | `packageManager` de `package.json` |
| Jest | 30.5.2 | `devDependencies` de `package.json` |

Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

La [semana 4](../week-04-primeros_tests_automatizados_con_python/README.md) escribe la misma suite en Python con pytest. Haz tus funciones pequeñas y claras: tendrás que traducirlas.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Semana 02 - Casos de Prueba y Técnicas de Diseño](../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md) | [Semana 04 - Primeros Tests Automatizados con Python](../week-04-primeros_tests_automatizados_con_python/README.md) |
