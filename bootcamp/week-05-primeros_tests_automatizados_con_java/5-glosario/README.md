# Glosario - Semana 05

Los términos comunes con las semanas 3 y 4 (AAA, aserción, mutación, rojo y verde, código de salida) están en sus glosarios: [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/5-glosario/README.md) y [semana 4](../../week-04-primeros_tests_automatizados_con_python/5-glosario/README.md).

| Término | Definición |
|---|---|
| Anotación | Etiqueta con `@` sobre una clase o un método (`@Test`, `@Nested`) que JUnit lee para saber qué hacer con él |
| `assertEquals` | Comprueba que dos valores son iguales. Recibe primero el esperado y después el obtenido |
| `assertEquals` con delta | Versión para `double` con un tercer argumento: la diferencia máxima que se da por buena |
| `assertNull` | Comprueba que un valor es `null` |
| `assertSame` | Comprueba que dos referencias son el mismo objeto, no solo iguales en contenido |
| `assertThat` | Punto de entrada de AssertJ: `assertThat(obtenido).isEqualTo(esperado)` |
| `assertThatThrownBy` | Comprobación de AssertJ que ejecuta una lambda y comprueba el error que lanza y su mensaje |
| `assertThrows` | Ejecuta una lambda y comprueba que lanza un error de un tipo; devuelve ese error |
| `assertTrue` / `assertFalse` | Comprueban un `boolean`; con otro tipo no compilan |
| AssertJ | Librería de comprobaciones fluentes que se encadenan empezando por el valor obtenido |
| `BUILD SUCCESS` / `BUILD FAILURE` | Última línea de Maven: todo compiló y ningún test falló, o algo salió mal. Con cero tests también hay `BUILD SUCCESS` |
| Compilar | Traducir el código Java a clases que ejecuta la máquina virtual. Si falla, no se ejecuta ningún test |
| `COMPILATION ERROR` | Bloque de Maven con los errores de compilación, con archivo, `[línea,columna]` y mensaje |
| Coordenadas | `groupId`, `artifactId` y `version`: el nombre de un proyecto o una dependencia en Maven |
| `@DisplayName` | Frase legible de un test o una clase `@Nested`; la muestran el IDE y, para `@Nested`, Surefire |
| `@Disabled` | Desactiva un test; Surefire lo cuenta como `Skipped` |
| `double` | Tipo de los números decimales en Java. Se guardan en binario y algunos no son exactos |
| `equals` | Método que decide si dos objetos son iguales en contenido. Un `record` lo trae; una clase normal, no |
| `Error` (Surefire) | Test que terminó por una excepción, no por una comprobación. Sale con `<<< ERROR!` y `»` |
| `Failure` (Surefire) | Test con una comprobación que no se cumplió. Sale con `<<< FAILURE!` y `expected: ... but was: ...` |
| `IllegalArgumentException` | Error de Java para «el argumento recibido no vale», el `ValueError` de Python |
| `import static` | Importa un método `static` para llamarlo sin la clase delante: `assertEquals` en lugar de `Assertions.assertEquals` |
| JDK | *Java Development Kit*: compilador `javac` y máquina virtual `java`. El curso usa la 21 |
| JUnit Jupiter | La API de tests de JUnit 5 y 6: `org.junit.jupiter.api` |
| Lambda | Función sin nombre, `() -> expresión`; el `() => ...` de JavaScript |
| `LocalDate` | Fecha sin hora de Java: `LocalDate.of(2026, 11, 18)` |
| Maven | Herramienta que descarga dependencias, compila y ejecuta los tests de un proyecto Java |
| `@Nested` | Clase interna de test que agrupa tests, como un `describe` dentro de otro |
| `null` | El «no hay valor» de Java |
| Paquete | Carpeta de clases de Java; coincide con la línea `package com.bootcamp;` del archivo |
| Plugin | Pieza de Maven que hace un paso: `compiler` compila y `surefire` ejecuta los tests |
| `pom.xml` | Archivo del proyecto Maven: coordenadas, versión de Java, dependencias y plugins |
| `record` | Clase que solo guarda datos, con `equals` y un texto legible (`User[name=Ana, ...]`) ya incluidos |
| `scope` `test` | Dependencia que solo existe al compilar y ejecutar los tests |
| `static` | Método que se llama desde la clase, sin crear un objeto: `Calculator.add(2, 3)` |
| Surefire | Plugin de Maven que ejecuta los tests de las clases `*Test` y escribe el informe |
| `-Dtest` | Opción de Surefire que elige qué ejecutar: `Clase`, `Clase#método` o con comodines `*` |
| `target/` | Carpeta donde Maven deja lo compilado y los informes. Se ignora en git |
| `@Test` | Anotación que marca un método como test |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 06 - Cierre Etapa 0: Plan de Pruebas y Suite Base Multilenguaje](../../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) → |

[↑ Semana 05](../README.md)
