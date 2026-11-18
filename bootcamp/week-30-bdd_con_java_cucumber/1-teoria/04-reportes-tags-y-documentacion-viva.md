# 04 - Reportes, tags y documentación viva

> Lenguaje: **Java**

---

## 🎯 Objetivos

- Configurar Cucumber desde `junit-platform.properties`.
- Generar el reporte HTML y el JSON con los plugins nativos.
- Filtrar escenarios por tags desde la línea de comandos.
- Elegir entre Surefire y Failsafe para ejecutar la suite.
- Escribir features que sirvan como documentación del sistema.

---

## Dónde se configura Cucumber

Cucumber lee su configuración de tres sitios. Las claves son las mismas en los tres:

| Dónde | Ejemplo | Cuándo usarlo |
|---|---|---|
| `@ConfigurationParameter` en la clase `@Suite` | `@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.bootcamp")` | Configuración de una suite concreta, si hay varias |
| `src/test/resources/junit-platform.properties` | `cucumber.glue=com.bootcamp` | Configuración del proyecto entero. Es la que usa el proyecto del curso |
| `-D` en la línea de comandos | `mvn test -Dcucumber.filter.tags="@smoke"` | Lo que cambia de una ejecución a otra |

Si una clave está en dos sitios, gana el `@ConfigurationParameter` de la suite, después `-D` y por último el archivo. El `junit-platform.properties` del proyecto:

```properties
# Steps, hooks y tipos de parámetro de Cucumber
cucumber.glue=com.bootcamp.bdd
# pretty en la consola; html para leer los features como documentación; json para otras herramientas
cucumber.plugin=pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json
# Nombres largos (feature - escenario - ejemplo): con nombres cortos, Surefire cuenta una sola vez
# los ejemplos de dos esquemas distintos que se llaman igual ("Example #1.1")
cucumber.junit-platform.naming-strategy=long
```

`cucumber.junit-platform.naming-strategy` se explica más abajo, en [el nombre de cada test](#el-nombre-de-cada-test).

---

## Plugins de reporte

`cucumber.plugin` es una lista separada por comas. Cada entrada es `nombre` o `nombre:archivo`:

| Plugin | Qué genera | Para quién |
|---|---|---|
| `pretty` | Cada escenario en la consola, con `✔`, `✘`, `■` y `↷` y el método de cada paso | Quien ejecuta los tests |
| `html:target/cucumber-reports/cucumber.html` | Una página autocontenida (unos 950 KB: lleva dentro su JavaScript) con los features, los escenarios, su resultado, los `scenario.log(...)` y los errores | Quien lee el sistema sin leer código: producto, QA, negocio |
| `json:target/cucumber-reports/cucumber.json` | Los mismos resultados en JSON, en el formato que leen herramientas como Jenkins o generadores de reportes | Otras herramientas |
| `summary` | Un resumen al final, con los fragmentos de los pasos indefinidos | Quien ejecuta |

Los archivos se escriben al terminar la suite, aunque haya escenarios fallidos. Están en `target/`, así que `mvn clean` los borra y Git los ignora.

Abre el HTML en el navegador con `xdg-open target/cucumber-reports/cucumber.html` (Linux), `open ...` (macOS) o `start ...` (Windows).

---

## Tags

Un tag es una palabra con `@` en la línea anterior a `Característica`, `Escenario`, `Esquema del escenario` o `Ejemplos`. Los tags de la característica los heredan todos sus escenarios:

```gherkin
@reservas
Característica: Reserva de salas

  @smoke
  Escenario: Reservar una sala libre
```

`cucumber.filter.tags` acepta expresiones con `and`, `or`, `not` y paréntesis:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
# [WARNING] Tests run: 3, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 2.510 s -- in com.bootcamp.RunCucumberTest

mvn test -Dcucumber.filter.tags="not @smoke"
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 2
```

Fíjate en el recuento: los escenarios que el filtro deja fuera no desaparecen, cuentan como **saltados** (`Skipped`). Con `@smoke` se ejecutaron dos y se saltó uno. Por eso el autograding del proyecto, que exige `Skipped: 0`, se ejecuta sin filtro.

Usos típicos de los tags: `@smoke` para la batería corta que se ejecuta en cada cambio, `@wip` para lo que aún no funciona y se excluye con `not @wip`, y tags por área (`@reservas`, `@pagos`) para ejecutar una parte. Un tag también puede activar un hook: `@Before("@reloj")`.

---

## El nombre de cada test

Surefire cuenta y muestra los escenarios por su nombre. Por defecto, cada ejemplo de un esquema se llama `Example #1.1`, `Example #1.2`... sin el nombre del esquema. Con dos esquemas en la suite, los dos tienen un `Example #1.1`.

Mientras todo pasa, el recuento es correcto. Cuando falla un ejemplo con un nombre repetido, Surefire agrupa los tests que se llaman igual y el recuento baja. En el proyecto del museo, con 17 escenarios y un ejemplo roto a propósito:

```bash
mvn test -Dtest=RunCucumberTest
# Con la estrategia por defecto (short):
# [ERROR] Tests run: 16, Failures: 1, Errors: 0, Skipped: 0
# Con cucumber.junit-platform.naming-strategy=long:
# [ERROR] Tests run: 17, Failures: 1, Errors: 0, Skipped: 0
```

Con más fallos, el recuento baja más. En el paso 5 del proyecto, antes del arreglo, fallan seis escenarios de devolución, cuatro de ellos ejemplos de un esquema: la estrategia por defecto dio `Tests run: 14, Failures: 6` y `long`, `Tests run: 17, Failures: 6`. Faltan tres escenarios, y no se sabe cuáles. Con `long`, el nombre lleva el feature, el escenario y el ejemplo:

```text
Devolución de piezas - El recargo por retraso tiene un tope - Ejemplos - Example #1.1
```

- **Qué:** la estrategia de nombres que el motor de Cucumber da a cada test.
- **Para qué:** que cada escenario tenga un nombre único y Surefire cuente bien los fallos.
- **Impacto:** sin ella, en cuanto hay dos esquemas, el recuento de Maven deja de ser fiable justo cuando más importa: cuando algo falla.

---

## Surefire o Failsafe

Los escenarios del proyecto arrancan la aplicación completa y un PostgreSQL: son tests de integración. Maven tiene dos plugins para ejecutar tests:

| Plugin | Fase | Qué clases ejecuta por defecto | Si un test falla |
|---|---|---|---|
| Surefire | `test` (`mvn test`) | `*Test`, `Test*`, `*Tests`, `*TestCase` | El build para ahí |
| Failsafe | `integration-test` y `verify` (`mvn verify`) | `*IT`, `IT*`, `*ITCase` | Termina `integration-test` (para poder parar servidores en `post-integration-test`) y falla en `verify` |

Para pasar la suite a Failsafe, renombra la clase a `RunCucumberIT` y añade el plugin. Spring Boot fija su versión:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-failsafe-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>integration-test</goal>
        <goal>verify</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

```bash
mvn verify
# [INFO] --- surefire:3.5.6:test (default-test) @ lab-04-spring-boot-y-reportes ---
# [INFO] --- failsafe:3.5.6:integration-test (default) @ lab-04-spring-boot-y-reportes ---
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
# [INFO] --- failsafe:3.5.6:verify (default) @ lab-04-spring-boot-y-reportes ---
```

Así, `mvn test` ejecuta solo los tests rápidos y `mvn verify` añade los escenarios. En este curso el proyecto sigue con Surefire: el autograding usa `mvn test` desde la semana 25, y la suite completa tarda menos de un minuto. Cuando una suite de integración se hace lenta, este es el cambio.

---

## Documentación viva

Un feature es documentación que se ejecuta. Si el sistema cambia y el feature no, el escenario falla: esa documentación no puede quedarse desactualizada sin que nadie se entere. Para que funcione como documentación, alguien que no programa tiene que poder leerla:

| Imperativo (prueba la interfaz) | Declarativo (describe el negocio) |
|---|---|
| `Cuando envío POST /api/pieces/MUS-002/return con {"condition":"DAMAGED"}` | `Cuando MUS-PRADO devuelve la pieza MUS-002 dañada` |
| `Entonces el campo "lateFee" vale 150` | `Entonces la devolución cobra:` con la tabla de cargos |

El detalle técnico (la URL, el JSON, el nombre de los campos) vive en los steps, una sola vez. El feature cuenta la regla con las palabras del negocio, y la descripción de la `Característica` dice por qué existe la regla.

Dos señales de que un feature no documenta bien:

- **Escenarios de UI o de API, no de reglas.** Si cada escenario prueba un código HTTP, eso ya lo cubren los tests de `@WebMvcTest` de la semana 28. Un feature cubre el flujo de negocio: registrar, prestar, devolver y cobrar.
- **Datos sin significado.** `Pieza 1` con valor `1000` no le dice nada a nadie; un códice de valor `10000` que vuelve dañado y cobra `1000` de daño explica la regla del 10 %.

Escribir el feature antes que el código, y con alguien de negocio, es lo que hace que BDD encuentre huecos. En el proyecto de esta semana, el escenario de devolución, el primero que recorre préstamo y devolución por la API, encontró que la API no abría el préstamo, aunque los tests unitarios de cada servicio pasaban ([proyecto, paso 5](../3-proyecto/README.md#paso-5-la-devolución)).

---

## ✅ Resumen

- La configuración de Cucumber va en `junit-platform.properties`; lo que cambia en cada ejecución, con `-D`.
- `pretty`, `html:` y `json:` son plugins nativos: no hace falta ninguna dependencia más.
- `cucumber.filter.tags` filtra con `and`, `or` y `not`; lo filtrado cuenta como saltado.
- Con dos esquemas o más, usa `cucumber.junit-platform.naming-strategy=long`: si no, los fallos se cuentan mal.
- Failsafe ejecuta `*IT` en `mvn verify`, separado de los tests rápidos.
- Un feature declarativo es documentación que no puede quedarse desactualizada.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Estado entre steps y cucumber-spring](03-estado-y-cucumber-spring.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 30](../README.md)
