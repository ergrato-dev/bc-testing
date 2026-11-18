# Glosario Semana 31 - Proyecto Integrador (Java)

## A

- **Agente de Java** (*Java agent*): una librería que la JVM carga al arrancar con `-javaagent:` y que puede modificar las clases antes de ejecutarlas. JaCoCo y Mockito son agentes.
- **`argLine`**: las opciones que Surefire y Failsafe pasan a la JVM de los tests. Es a la vez una propiedad de Maven, que define `prepare-agent`, y un parámetro de los dos plugins.
- **`@{argLine}`**: sustitución tardía de la propiedad `argLine`. Surefire y Failsafe la resuelven justo antes de lanzar la JVM, cuando JaCoCo ya la ha definido.

## B

- **`BUNDLE`**: en una regla de `check`, el proyecto entero. Con `CLASS`, la regla se aplica a cada clase.

## C

- **`check`**: goal de JaCoCo que compara la cobertura con las reglas y hace fallar el build si no llega.
- **`COVEREDRATIO`**: proporción de elementos cubiertos, entre 0 y 1. `0.85` es el 85 %.

## F

- **Failsafe**: plugin de Maven que ejecuta los tests de integración (`*IT`) en la fase `integration-test` y comprueba su resultado en `verify`.
- **Fase de Maven**: cada paso del ciclo de vida (`compile`, `test`, `integration-test`, `verify`...). `mvn verify` ejecuta todas las anteriores.

## I

- **IT** (*integration test*): el sufijo de las clases de test que arranca Failsafe. En el curso, las que arrancan un contexto de Spring o un contenedor.

## J

- **JaCoCo**: herramienta de cobertura de código para Java. Mide el *bytecode* que ejecutan los tests con un agente.
- **`jacoco.csv`**: el informe de JaCoCo en CSV, con una fila por clase y columnas `*_MISSED` y `*_COVERED` para cada contador.
- **`jacoco.exec`**: el archivo binario donde el agente guarda lo que se ha ejecutado. Se añade a lo que ya tenía: por eso se mide con `mvn clean verify`.

## P

- **`prepare-agent`**: goal de JaCoCo que define la propiedad `argLine` con el agente y el archivo `jacoco.exec`.

## R

- **`report`**: goal de JaCoCo que lee `jacoco.exec` y escribe el informe en `target/site/jacoco/`.
- **Reporte de testing**: el documento `testing-report-java.md` que resume la suite con datos de una ejecución real: fases, cobertura, tests lentos, riesgos y próximos pasos.

## S

- **`-DskipITs`**: opción de Failsafe que salta los tests de integración. Surefire se ejecuta igual.
- **Surefire**: plugin de Maven que ejecuta los tests unitarios (`*Test`) en la fase `test`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 31](../README.md)
