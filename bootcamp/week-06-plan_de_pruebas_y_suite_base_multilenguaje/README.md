# Semana 06 - Proyecto Integrador: Etapa 0

> **Etapa 0 - Fundamentos de Testing** | Semana 6 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Escribir un plan de pruebas con alcance, reglas, riesgos, estrategia, criterios de entrada y salida, matriz de trazabilidad y evidencia.
2. Priorizar los casos por riesgo, cruzando impacto y probabilidad.
3. Seguir cada regla hasta su test en JavaScript, Python y Java, y encontrar los huecos entre los tres.
4. Ejecutar las tres suites y comprobar la trazabilidad con un solo comando, `verify.sh`, que falla si falla cualquiera.
5. Detectar entradas con veredictos distintos según el lenguaje y decidir si son un bug o una pregunta abierta.

---

## Requisitos previos

- Las semanas [2](../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md) a [5](../week-05-primeros_tests_automatizados_con_java/README.md): los casos de prueba de tu dominio y sus tres suites, en verde.
- Node 22 con pnpm, uv, y Java 21 con Maven.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso: plan de pruebas, riesgo, trazabilidad hasta el test y diferencias entre lenguajes | 0.5 h |
| Planificación | 2 labs: plan por riesgo y `verify.sh` | 2.5 h |
| Desarrollo | Las tres suites del dominio en un proyecto, con su plan y `verify.sh` en verde | 6.5 h |
| Presentación | 5 minutos por aprendiz con los datos del plan | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Plan de pruebas y trazabilidad en tres lenguajes](./1-teoria/01-plan-de-pruebas-y-trazabilidad.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Plan de pruebas por riesgo](./2-practicas/lab-01-plan-de-pruebas-por-riesgo/README.md)
- [Lab 02 - Un comando para tres suites](./2-practicas/lab-02-un-comando-para-tres-suites/README.md)

### Proyecto

- [Plan de pruebas y suite base en tres lenguajes](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-06` con `js/`, `python/`, `java/`, `plan-de-pruebas.md` y `verify.sh`, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-06 ./scripts/verificar-semana.sh 06
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-06-plan_de_pruebas_y_suite_base_multilenguaje/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-de-la-regla-al-test.svg
|-- 1-teoria/
|   `-- 01-plan-de-pruebas-y-trazabilidad.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-plan-de-pruebas-por-riesgo/
|   `-- lab-02-un-comando-para-tres-suites/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|       |-- .gitignore
|       |-- plan-de-pruebas.md
|       `-- verify.sh
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Las mismas de las semanas 3 a 5: Node 22, pnpm 12.6.0 y Jest 30.5.2; Python 3.14 y pytest 9.1.1; JDK 21, Maven 3.9.16, JUnit 6.0.3, AssertJ 3.27.7 y Surefire 3.5.6. Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Con esta semana termina la Etapa 0. El proyecto `~/proyecto-semana-06` es la primera forma del repositorio que crece todo el curso ([proyecto hilo conductor](../../docs/proyecto-hilo-conductor.md)): guárdalo. En la [semana 7](../week-07-jest_avanzado_y_organizacion_de_suites/README.md) empieza la Etapa 1, que hace crecer `js/`.

---

## Navegación

| ← Semana anterior | Siguiente → |
|---|---|
| [Semana 05 - Primeros Tests Automatizados con Java](../week-05-primeros_tests_automatizados_con_java/README.md) | [Semana 07 - Jest Avanzado y Organización de Suites](../week-07-jest_avanzado_y_organizacion_de_suites/README.md) |
