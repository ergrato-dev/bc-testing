# Semana 31 - Proyecto Integrador: Etapa 3 (Java)

> **Etapa 3 - Testing con Java** | Semana 31 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Separar una suite Java en tests unitarios (`*Test`, Surefire) y de integración (`*IT`, Failsafe) según lo que arranca cada test.
2. Ejecutar cada fase por separado y leer los informes de Surefire y Failsafe.
3. Medir con JaCoCo la cobertura de las dos fases en una sola cifra y hacer fallar el build por debajo del 85 %.
4. Escribir un reporte de testing con datos reales: fases, cobertura, tests lentos, riesgos y próximos pasos.
5. Presentar tu suite y defender en qué fase va cada test.

---

## Requisitos previos

- Semanas 25 a 30 entregadas en `java/` de tu repositorio `qa-<nombre>-<apellido>`.
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.
- **Docker** en marcha. Comprueba con `docker version --format '{{.Server.Version}}'`.

Esta semana no tiene teoría de testing nueva: junta lo que ya sabes. La única herramienta nueva es JaCoCo, la versión Java del coverage.py de la [semana 21](../week-21-coverage_y_calidad_en_python/README.md).

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso: la suite completa, Surefire y Failsafe, JaCoCo | 0.5 h |
| Planificación | 2 labs: test e IT, JaCoCo con umbral y reporte | 2.5 h |
| Desarrollo | Proyecto integrador en el proyecto hilo conductor | 6.5 h |
| Presentación | 5 minutos por aprendiz con los datos del reporte | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [La suite Java completa](./1-teoria/01-la-suite-java-completa.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Test e IT con Surefire y Failsafe](./2-practicas/lab-01-test-e-it-con-surefire-y-failsafe/README.md)
- [Lab 02 - JaCoCo, umbral y reporte](./2-practicas/lab-02-jacoco-umbral-y-reporte/README.md)

### Proyecto

- [Suite Java completa del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto integrador (único entregable obligatorio): rama `week-31` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 31
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-31-proyecto_integrador_java/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-suite-java-por-capas.svg
|-- 1-teoria/
|   `-- 01-la-suite-java-completa.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-test-e-it-con-surefire-y-failsafe/
|   `-- lab-02-jacoco-umbral-y-reporte/
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

## Versiones de la semana

JaCoCo 0.8.16 (`jacoco-maven-plugin`) y Maven Failsafe 3.5.6, que gestiona Spring Boot 4.1.1, además de las versiones de las semanas 25 a 30. Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Una cobertura alta dice qué líneas ejecutan los tests, no qué comprueban. En el proyecto del museo, `PieceService` está al 100 % y aun así presta piezas que están en restauración: la comprobación no existe, así que no hay línea que cubrir. Por eso el reporte tiene una sección de riesgos que escribes tú, no JaCoCo.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 30 - Java Testing VI: BDD con Java (Cucumber)](../week-30-bdd_con_java_cucumber/README.md) | [Semana 32 - Fundamentos de Testing en Go](../week-32-fundamentos_de_testing_en_go/README.md) |
