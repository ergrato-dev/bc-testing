# 01 - JDK, Maven y JUnit 6

> Lenguaje: Java.

En la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/01-setup-jest.md) montaste un proyecto de tests con Node.js, pnpm y Jest, y en la [semana 4](../../week-04-primeros_tests_automatizados_con_python/1-teoria/01-setup-pytest.md) con Python, uv y pytest. Esta semana montas el mismo proyecto en Java, con tres piezas: el **JDK** compila y ejecuta el código, **Maven** descarga las librerías y lanza cada paso, y **JUnit 6** ejecuta los tests.

---

## 🎯 Objetivos

- Instalar el JDK 21 y Maven 3.9.
- Escribir a mano un `pom.xml` con JUnit 6 y AssertJ, y saber qué hace cada parte.
- Colocar código y tests en las carpetas que Maven espera.
- Ejecutar el primer test con `mvn test` y saber cuándo termina bien y cuándo no.

---

## 1. De JavaScript y Python a Java

| Semana 3 (JavaScript) | Semana 4 (Python) | Semana 5 (Java) | Para qué |
|---|---|---|---|
| Node.js 22 | Python 3.14 | JDK 21 | Ejecutar el código |
| pnpm | uv | Maven 3.9 | Instalar dependencias y lanzar los tests |
| `package.json` | `pyproject.toml` | `pom.xml` | Nombre, versión del lenguaje y dependencias |
| `node_modules/` | `.venv/` | `~/.m2/repository/` | Dónde quedan las librerías descargadas |
| Jest 30.5.2 | pytest 9.1.1 | JUnit 6.0.3 | Ejecutar los tests |
| `pnpm test` | `uv run pytest` | `mvn test` | Lanzar la suite |

Dos diferencias de fondo:

- **Java se compila.** Antes de ejecutar un solo test, Maven pasa todo el código por el compilador (`javac`). Si una línea no compila, no se ejecuta ningún test.
- **No hay lockfile.** Maven no tiene un equivalente de `pnpm-lock.yaml` o `uv.lock`: las versiones exactas se escriben en el propio `pom.xml`, y por eso nunca se usan rangos ni `LATEST`.

---

## 2. JDK 21 y Maven

### JDK 21

El **JDK** (*Java Development Kit*) trae el compilador `javac` y la máquina virtual `java`. El curso usa la versión 21, la LTS (soporte a largo plazo). En Ubuntu, y en Windows dentro de WSL:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk-headless
java -version
```

```text
openjdk version "21.0.13.1" 2026-11-17
OpenJDK Runtime Environment (build 21.0.13.1+2-1-24.04.1-Ubuntu)
OpenJDK 64-Bit Server VM (build 21.0.13.1+2-1-24.04.1-Ubuntu, mixed mode, sharing)
```

Cualquier `21.0.x` sirve. `headless` es la versión sin componentes gráficos: para tests no hacen falta. En macOS, instala Temurin 21 desde [adoptium.net](https://adoptium.net/).

### Maven 3.9

El paquete `maven` de Ubuntu 24.04 es la versión 3.8, más antigua que la del curso. Instala la 3.9.16 desde la web de Apache, en tu carpeta personal:

```bash
cd /tmp
curl -fsSLO https://archive.apache.org/dist/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.tar.gz
mkdir -p ~/.local/bin
tar -xzf apache-maven-3.9.16-bin.tar.gz -C ~/.local
ln -s ~/.local/apache-maven-3.9.16/bin/mvn ~/.local/bin/mvn
```

| Comando | Qué hace |
|---|---|
| `curl -fsSLO <url>` | Descarga el archivo con su mismo nombre (`-O`), sin barra de progreso (`-s`) y fallando si no existe (`-f`) |
| `tar -xzf <archivo> -C ~/.local` | Descomprime Maven en `~/.local/apache-maven-3.9.16/` |
| `ln -s <destino> <enlace>` | Crea un acceso directo: `~/.local/bin/mvn` apunta al `mvn` de Maven |

Ubuntu añade `~/.local/bin` a la ruta de comandos al iniciar sesión si la carpeta existe. Abre una terminal nueva y comprueba:

```bash
mvn -v
```

```text
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: /root/.local/apache-maven-3.9.16
Java version: 21.0.13.1, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.18.33.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"
```

`Java version: 21` confirma que Maven usa el JDK que acabas de instalar. Si `mvn` responde `command not found`, ejecuta `source ~/.profile`, que vuelve a leer la configuración de inicio de sesión.

---

## 3. El proyecto

### Carpetas

Maven no se configura con rutas: espera cada cosa en su sitio.

```text
mi-proyecto-java/
|-- pom.xml
`-- src/
    |-- main/java/com/bootcamp/
    |   `-- Calculator.java
    `-- test/java/com/bootcamp/
        `-- CalculatorTest.java
```

- `src/main/java/` es el código que se prueba; `src/test/java/` son los tests.
- `com/bootcamp/` es el **paquete**: la carpeta y la primera línea de cada archivo (`package com.bootcamp;`) tienen que coincidir. El test vive en el mismo paquete que la clase que prueba, así que no necesita importarla.
- Maven crea `target/` al compilar. Es el `node_modules` del código compilado: no se sube a git.

### `pom.xml`

El `pom.xml` (*Project Object Model*) se escribe a mano. Este es el del curso:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.bootcamp</groupId>
  <artifactId>mi-proyecto-java</artifactId>
  <version>1.0.0</version>

  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>6.0.3</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.assertj</groupId>
      <artifactId>assertj-core</artifactId>
      <version>3.27.7</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.5.6</version>
      </plugin>
    </plugins>
  </build>
</project>
```

| Parte | Qué es | Para qué |
|---|---|---|
| `<project ...>` y `modelVersion` | La cabecera fija de todo `pom.xml` | Maven la exige; se copia tal cual |
| `groupId`, `artifactId`, `version` | El nombre del proyecto: organización, proyecto y versión | Identificarlo; `artifactId` es el `name` de `package.json` |
| `maven.compiler.release` | Versión de Java con la que se compila | Que el código sea Java 21 aunque tengas otro JDK más nuevo |
| `project.build.sourceEncoding` | Codificación de los archivos | Que las tildes y la ñ de los mensajes no se rompan |
| `<dependency>` de `junit-jupiter` | JUnit 6: las anotaciones `@Test` y los `assertEquals` | Escribir y ejecutar tests |
| `<dependency>` de `assertj-core` | AssertJ, otra forma de escribir las comprobaciones | La [teoría 03](./03-assertions-y-ejecucion.md#5-assertj-la-comprobación-como-una-frase) la presenta |
| `<scope>test</scope>` | La dependencia solo existe al compilar y ejecutar tests | Lo mismo que `-D` en pnpm o `--dev` en uv |
| `maven-surefire-plugin` 3.5.6 | El **plugin** de Maven que ejecuta los tests | Fijar su versión; sin esta línea Maven usa la que trae de serie |

En el curso lo copias de aquí y cambias solo `artifactId`.

### El código

`src/main/java/com/bootcamp/Calculator.java`:

```java
package com.bootcamp;

public class Calculator {

    public static int add(int a, int b) {
        return a + b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        return a / b;
    }
}
```

- En Java todo el código va dentro de una **clase**, y el archivo se llama como ella: `Calculator.java`.
- `public` hace visible el método desde otros archivos; `static` deja llamarlo sin crear un objeto: `Calculator.add(2, 3)`. Esta semana todas las funciones son `static`, el equivalente más cercano a las funciones de JavaScript y Python.
- `int` (entero), `double` (decimal) y `boolean` son **tipos**: Java los comprueba al compilar. `Calculator.add("2", 3)` no compila.
- `throw new IllegalArgumentException("...")` lanza un error, como `throw new Error()` en JavaScript o `raise ValueError()` en Python.

### El test

`src/test/java/com/bootcamp/CalculatorTest.java`:

```java
package com.bootcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    @Test
    @DisplayName("should return 5 when adding 2 and 3")
    void shouldReturn5WhenAdding2And3() {
        // Arrange
        int a = 2;
        int b = 3;

        // Act
        int result = Calculator.add(a, b);

        // Assert
        assertEquals(5, result);
    }

    @Test
    @DisplayName("should throw an error when divisor is zero")
    void shouldThrowAnErrorWhenDivisorIsZero() {
        // Arrange
        double dividend = 10;
        double divisor = 0;

        // Act + Assert
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> Calculator.divide(dividend, divisor));
        assertEquals("Division by zero", error.getMessage());
    }
}
```

`@Test` marca un método como test; `assertEquals` es la comprobación. La [teoría 02](./02-estructura-test-junit.md) cuenta cada pieza.

---

## 4. Ejecutar

```bash
cd mi-proyecto-java
mvn test
```

La primera vez Maven descarga JUnit, AssertJ, los plugins y lo que necesitan (unas 300 líneas `Downloading` y `Downloaded`) y los guarda en `~/.m2/repository/`. Después:

```text
[INFO] --- compiler:3.15.0:compile (default-compile) @ mi-proyecto-java ---
[INFO] Compiling 1 source file with javac [debug release 21] to target/classes
[INFO] 
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ mi-proyecto-java ---
[INFO] Compiling 1 source file with javac [debug release 21] to target/test-classes
[INFO] 
[INFO] --- surefire:3.5.6:test (default-test) @ mi-proyecto-java ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.bootcamp.CalculatorTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.062 s -- in com.bootcamp.CalculatorTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.620 s
```

`mvn test` no es un solo paso: es una cadena de **fases**. Cada línea `---` es un plugin trabajando:

1. `compiler:compile` compila `src/main/java` en `target/classes`.
2. `compiler:testCompile` compila `src/test/java` en `target/test-classes`.
3. `surefire:test` ejecuta los tests compilados.

Si un paso falla, los siguientes no se ejecutan. La segunda vez, sin descargas, tarda un par de segundos. Con `mvn -q test` Maven solo escribe si algo falla.

---

## 5. Dónde busca Surefire y cómo termina

Surefire ejecuta las clases de `src/test/java` cuyo nombre termina en `Test`, `Tests` o `TestCase` (o empieza por `Test`), y en ellas los métodos con `@Test`.

Una clase con otro nombre no se ejecuta, **y no avisa**. Con el mismo test renombrado a `CalculatorCheck`:

```text
[INFO] --- surefire:3.5.6:test (default-test) @ mi-proyecto-java ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

Ni `T E S T S` ni `Tests run`: cero tests y `BUILD SUCCESS`. Lo mismo pasa con una clase de test sin ningún `@Test` activo: `Tests run: 0` y éxito. pytest termina con código 5 cuando no hay tests; Maven no. Lee siempre la línea `Tests run:`.

Maven devuelve dos códigos de salida:

| Código | Cuándo | Última línea |
|---|---|---|
| 0 | Todo compiló y ningún test falló | `BUILD SUCCESS` |
| 1 | Cualquier otra cosa: un test en rojo, un error de compilación, un filtro sin tests | `BUILD FAILURE` |

```bash
mvn test -Dtest=Nada
echo $?
```

```text
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.5.6:test (default-test) on project mi-proyecto-java: No tests matching pattern "Nada" were executed! ...
1
```

`-Dtest=Nada` pide la clase `Nada`, que no existe. Con un filtro explícito, Surefire sí falla si no encuentra ninguna clase; la [teoría 03](./03-assertions-y-ejecucion.md#4-ejecutar-parte-de-la-suite) explica los filtros.

---

## 6. Errores frecuentes

| Error | Causa | Solución |
|---|---|---|
| `package org.junit.jupiter.api does not exist` | Falta la dependencia `junit-jupiter` en el `pom.xml` | Añadir el bloque `<dependency>` y volver a ejecutar |
| `cannot find symbol` `class Test` `location: package org.junit` | `import org.junit.Test;`, el `@Test` de JUnit 4 | `import org.junit.jupiter.api.Test;` |
| `mvn: command not found` | `~/.local/bin` no está en la ruta de comandos | Abrir una terminal nueva o `source ~/.profile` |
| `BUILD SUCCESS` sin `Tests run` | La clase de test no termina en `Test` | Renombrar archivo y clase a `...Test` |

---

## ✅ Resumen

- JDK 21 con apt; Maven 3.9.16 desde el tarball de Apache en `~/.local`.
- El `pom.xml` se escribe a mano: coordenadas, Java 21, JUnit 6.0.3 y AssertJ 3.27.7 con `scope` `test`, y Surefire 3.5.6.
- Código en `src/main/java`, tests en `src/test/java`, mismo paquete.
- `mvn test` compila y después ejecuta; si no compila, no se ejecuta ningún test.
- Maven termina con 0 o 1, y con cero tests también da `BUILD SUCCESS`: mira `Tests run:`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 05 - Primeros Tests Automatizados con Java](../README.md) | [Teoría — 02 - Estructura de un test en JUnit 6](02-estructura-test-junit.md) → |

[↑ Semana 05](../README.md)
