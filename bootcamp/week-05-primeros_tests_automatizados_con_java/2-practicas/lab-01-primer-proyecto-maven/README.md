# Lab 01 - Primer proyecto con Maven

## Objetivo

Montar con Maven el proyecto donde harás los cuatro labs de la semana, ver un error de compilación y un test en rojo, arreglar los dos y dejar el proyecto en git.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-setup-junit.md) y la [02](../../1-teoria/02-estructura-test-junit.md).
- JDK 21 y Maven 3.9 instalados (`java -version` y `mvn -v`, [teoría 01](../../1-teoria/01-setup-junit.md#2-jdk-21-y-maven)) y git.

## Punto de partida

La carpeta `~/labs-semana-05` es el proyecto de los cuatro labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-05
cd ~/labs-semana-05
cp -r <ruta-del-bootcamp>/bootcamp/week-05-primeros_tests_automatizados_con_java/2-practicas/lab-01-primer-proyecto-maven/starter/. .
find src -type f
# src/main/java/com/bootcamp/Calculator.java
# src/test/java/com/bootcamp/CalculatorTest.java
```

Es el mismo código del [lab 01 de la semana 4](../../../week-04-primeros_tests_automatizados_con_python/2-practicas/lab-01-primer-proyecto-pytest/README.md), en Java: `add` tiene el mismo bug.

---

## Paso 1: JDK y Maven (5 min)

```bash
java -version
mvn -v
```

La primera línea de `java -version` tiene que decir `21`, y `mvn -v` tiene que mostrar `Apache Maven 3.9` y `Java version: 21`. Si alguno falla, vuelve a la [teoría 01](../../1-teoria/01-setup-junit.md#2-jdk-21-y-maven).

## Paso 2: el `pom.xml`, sin dependencias (10 min)

Crea `pom.xml` en `~/labs-semana-05` con la cabecera, las coordenadas y las propiedades del `pom.xml` de la [teoría 01](../../1-teoria/01-setup-junit.md#pomxml), **sin** los bloques `<dependencies>` ni `<build>`. Cambia el `artifactId` a `labs-semana-05`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.bootcamp</groupId>
  <artifactId>labs-semana-05</artifactId>
  <version>1.0.0</version>

  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
</project>
```

## Paso 3: un error de compilación (10 min)

```bash
mvn test
```

```text
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /root/labs-semana-05/src/test/java/com/bootcamp/CalculatorTest.java:[3,36] package org.junit.jupiter.api does not exist
[ERROR] /root/labs-semana-05/src/test/java/com/bootcamp/CalculatorTest.java:[3,1] static import only from classes and interfaces
...
[INFO] 11 errors 
[INFO] -------------------------------------------------------------
[INFO] BUILD FAILURE
```

`Calculator.java` compiló; `CalculatorTest.java`, no: sin JUnit en el `pom.xml`, `org.junit.jupiter.api` no existe. No se ejecutó ningún test, y no hay línea `Tests run`. Once errores, una sola causa: la primera línea.

Añade los bloques `<dependencies>` y `<build>` de la teoría 01 antes de `</project>`.

## Paso 4: rojo (10 min)

```bash
mvn test
```

La primera vez descarga JUnit, AssertJ y los plugins. Después:

```text
[INFO] Running com.bootcamp.CalculatorTest
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.052 s <<< FAILURE! -- in com.bootcamp.CalculatorTest
[ERROR] com.bootcamp.CalculatorTest.shouldReturn5WhenAdding2And3 -- Time elapsed: 0.032 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <5> but was: <-1>
...
[ERROR] Failures: 
[ERROR]   CalculatorTest.shouldReturn5WhenAdding2And3:25 expected: <5> but was: <-1>
[INFO] 
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
```

Ahora sí compila, y el test falla. Lee el informe completo como en la [teoría 02](../../1-teoria/02-estructura-test-junit.md#4-leer-un-test-que-falla) y contesta en voz alta o en una nota:

- ¿Qué valor dio el código y cuál esperaba el test?
- ¿En qué línea está el `assertEquals` que falla?
- ¿Por qué este informe tiene una línea `Tests run` y el del paso 3 no?

## Paso 5: verde (5 min)

Abre `src/main/java/com/bootcamp/Calculator.java` y sigue el comentario `PASO 1`: borra `return a - b;` y descomenta `return a + b;` quitando el `// `. Ejecuta `mvn test`: `Tests run: 1, Failures: 0` y `BUILD SUCCESS`.

## Paso 6: dos tests más (10 min)

En `src/test/java/com/bootcamp/CalculatorTest.java`, descomenta los bloques `PASO 2` y `PASO 3`: quita el `// ` del principio de cada línea del test, desde `@Test` hasta la `}` que lo cierra. En VS Code, selecciona las líneas y pulsa `Ctrl+/` (`Cmd+/` en macOS). Deja comentadas las líneas `// ====`, `// PASO` y `// Descomenta`.

Antes de ejecutar, escribe qué esperas que salga. Después:

```bash
mvn test
```

```text
[INFO] Running com.bootcamp.CalculatorTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.043 s -- in com.bootcamp.CalculatorTest
```

Los tests de `isEven` usan `assertTrue(result)` y `assertFalse(result)`. En Python, `assert result is True` comprobaba más que `assert result`; en Java no hace falta: `result` es un `boolean` y solo puede ser `true` o `false`. Prueba a cambiar `boolean result` por `int result` en uno de ellos y ejecuta: no compila. Deshaz el cambio.

## Paso 7: códigos de salida y git (10 min)

Pide a Maven una clase de test que no existe y mira cómo termina:

```bash
mvn test -Dtest=Nada
echo $?
```

```text
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.5.6:test (default-test) on project labs-semana-05: No tests matching pattern "Nada" were executed! ...
1
```

Repite `echo $?` después de `mvn test` (todo en verde) y después de volver a poner `return a - b;` un momento. Anota el código de cada caso: Maven solo distingue `0` (todo bien) y `1` (algo falló). Quita el bug otra vez.

Convierte la carpeta en un repositorio git. `target/` es lo que compila Maven; no se sube:

```bash
git init
echo "target/" > .gitignore
git add .
git status --short
# A  .gitignore
# A  pom.xml
# A  src/main/java/com/bootcamp/Calculator.java
# A  src/test/java/com/bootcamp/CalculatorTest.java
git commit -m "lab 01: primer proyecto con Maven"
```

Si `git commit` responde `Please tell me who you are`, configura tu nombre y correo como en el [lab 01 de la semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-01-primer-proyecto-jest/README.md#paso-7-git-y-modo-watch-10-min).

El lab 04 usará git para deshacer cambios en el código.

---

## Comprobación final

```bash
mvn test | grep "Tests run" | tail -1
# [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
git log --oneline | wc -l
# 1
```

Repasa:

- [ ] El `pom.xml` fija Java 21, `junit-jupiter` 6.0.3 y `assertj-core` 3.27.7 con `<scope>test</scope>`, y Surefire 3.5.6.
- [ ] Sabes decir, viendo un informe, si no compiló, si falló una comprobación o si todo pasó.
- [ ] `target/` está en `.gitignore`.

## Qué has aprendido

- Un proyecto Maven es un `pom.xml` escrito a mano más `src/main/java` y `src/test/java`.
- Si no compila, no se ejecuta ningún test: lee el primer error de compilación.
- Maven termina con 0 o 1; lo que pasó dentro lo dice la línea `Tests run`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - AAA y assertions](../lab-02-aaa-y-assertions/README.md) → |

[↑ Semana 05](../../README.md)
