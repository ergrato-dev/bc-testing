# Semana 27 - Java Testing III: TDD con Java

> **Etapa 3 - Testing con Java** | Semana 27 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Recorrer el ciclo Red → Green → Refactor en Java y distinguir un Red de compilación de uno de aserción.
2. Dejar que los tests decidan los tipos de un programa: `enum`, `record`, clase inmutable e interfaz.
3. Refactorizar con la suite como red de seguridad, un cambio y una ejecución cada vez.
4. Construir un servicio outside-in con Mockito, con sus dependencias como interfaces y la fecha por un `Clock`.
5. Proteger decisiones de arquitectura con fitness functions de ArchUnit.
6. Trabajar con un commit por ciclo y un registro de ciclos (TDD-LOG).

---

## Requisitos previos

- Semana 26: Mockito, `@Mock`, `@Captor`, `InOrder` y el servicio de tu dominio con tres dependencias.
- Semana 25: `@ParameterizedTest`, `@Nested` y AssertJ.
- Semanas 11 y 20: TDD con Jest y con pytest (aquí solo se ve la diferencia).
- JDK 21 y Maven 3.9 o superior. Comprueba con `java -version` y `mvn -v`.
- Recomendado: IntelliJ IDEA Community. Los atajos de la semana son los suyos; con VS Code también se puede.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Ciclo TDD en Java, diseño emergente y refactor, outside-in con Mockito, ArchUnit | 2.5 h |
| Prácticas | 4 labs: Prime Factors, Game of Life, outside-in, ArchUnit | 4 h |
| Proyecto | Un servicio nuevo del dominio construido con TDD | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [El ciclo Red → Green → Refactor en Java](./1-teoria/01-ciclo-tdd-en-java.md)
2. [Diseño emergente y refactoring seguro](./1-teoria/02-diseno-emergente-y-refactoring.md)
3. [Outside-in TDD con Mockito](./1-teoria/03-outside-in-con-mockito.md)
4. [Fitness functions de arquitectura con ArchUnit](./1-teoria/04-archunit-fitness-functions.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Kata Prime Factors](./2-practicas/lab-01-prime-factors/README.md)
- [Lab 02 - Kata Game of Life](./2-practicas/lab-02-game-of-life/README.md)
- [Lab 03 - Outside-in con Mockito](./2-practicas/lab-03-outside-in-con-mockito/README.md)
- [Lab 04 - Fitness functions con ArchUnit](./2-practicas/lab-04-archunit/README.md)

### Proyecto

- [Un servicio del dominio construido con TDD](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-27` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`, un commit por ciclo. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 27
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-27-tdd_con_java/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-ciclo-tdd-en-java.svg
|   `-- 02-outside-in.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-prime-factors/
|   |-- lab-02-game-of-life/
|   |-- lab-03-outside-in-con-mockito/
|   `-- lab-04-archunit/
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

JDK 21, JUnit Jupiter 6.0.3, AssertJ 3.27.7, Mockito 5.23.0, maven-dependency-plugin 3.11.0, maven-surefire-plugin 3.5.6 y ArchUnit 1.5.1 (`archunit-junit5`). Están fijadas en el `pom.xml` de cada carpeta y registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Ejecuta cada test **antes** de escribir su Green y lee por qué falla. Un Green escrito sin haber visto el Red no es TDD: es escribir tests después, en otro orden.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 26 - Mockito — Mocking en Java](../week-26-mockito_mocking_en_java/README.md) | Semana 28 - Próximamente |
