# Glosario — Semana 05

> Primeros Tests Automatizados con Java (JUnit 5)

---

## A

**AfterEach / AfterAll**
Anotaciones que ejecutan limpieza después de cada test o una sola vez al final de la clase (`static`).

**assertEquals**
Assertion para comparar valor esperado vs valor actual.

**assertThrows**
Assertion para validar que una operación lanza una excepción esperada; devuelve la excepción para revisar su mensaje.

**AssertJ**
Librería de assertions fluentes (`assertThat(x).isEqualTo(y)`) que se añade como dependencia de test.

---

## B

**BeforeEach / BeforeAll**
Anotaciones que ejecutan setup antes de cada test o una sola vez antes de toda la clase (`static`).

**Build Success**
Estado de Maven cuando compilación y tests terminan correctamente.

---

## D

**Delta**
Tolerancia máxima aceptada al comparar `double` con `assertEquals(expected, actual, delta)`.

**Disabled**
Anotación que desactiva un test; Surefire lo cuenta como `Skipped`.

**DisplayName**
Anotación de JUnit 5 para dar un nombre legible al test en el IDE; el resumen de Surefire usa el nombre del método.

---

## E

**Error**
Resultado de Surefire cuando una excepción inesperada interrumpe el test (por ejemplo `NullPointerException`).

---

## F

**Failure**
Resultado de Surefire cuando una assertion no se cumple (`AssertionFailedError`).

---

## J

**JUnit 5**
Framework de testing para Java usado en esta semana.

---

## M

**Maven**
Herramienta de build y gestión de dependencias para Java.

**mvn test**
Comando principal para ejecutar tests en Maven.

---

## P

**Pass**
Resultado exitoso de un test.

---

## S

**Surefire**
Plugin de Maven que ejecuta los tests unitarios y deja reportes `.txt` y `.xml` en `target/surefire-reports/`.

---

## T

**Test Method**
Método anotado con `@Test` que representa un caso de prueba.

**Test Suite**
Conjunto de tests relacionados de un mismo módulo o componente.
