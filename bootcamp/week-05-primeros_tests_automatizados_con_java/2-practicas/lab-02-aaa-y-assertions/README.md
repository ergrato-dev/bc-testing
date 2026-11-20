# Lab 02 - AAA y assertions

## Objetivo

Completar la suite de un módulo de usuarios con tests AAA y elegir, para cada valor, la comprobación que lo comprueba de verdad: `assertEquals`, `assertNull`, `assertTrue`, `assertEquals` con delta, `assertThrows` y, al final, AssertJ.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-estructura-test-junit.md) y la [03](../../1-teoria/03-assertions-y-ejecucion.md).
- El proyecto `~/labs-semana-05` del lab 01.

## Punto de partida

```bash
cd ~/labs-semana-05
cp -r <ruta-del-bootcamp>/bootcamp/week-05-primeros_tests_automatizados_con_java/2-practicas/lab-02-aaa-y-assertions/starter/. .
mvn test -Dtest=UserUtilsTest
```

```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

El archivo no tiene ningún test activo todavía: cero tests y `BUILD SUCCESS`, como avisa la [teoría 01](../../1-teoria/01-setup-junit.md#5-dónde-busca-surefire-y-cómo-termina).

## Contexto

`src/main/java/com/bootcamp/UserUtils.java` es el `user_utils.py` del [lab 02 de la semana 4](../../../week-04-primeros_tests_automatizados_con_python/2-practicas/lab-02-aaa-y-asserts/README.md) en Java: cinco funciones del registro de usuarios de una tienda. Léelo entero antes de empezar. Dos diferencias:

- `createUser` devuelve un `User`, un `record` definido en `User.java`: el diccionario de Python con campos fijos. Se lee con `user.name()`.
- `findUserByEmail` devuelve `null` cuando no encuentra el email.

---

## Paso 1: isAdult (5 min)

Descomenta el bloque `PASO 1` de `src/test/java/com/bootcamp/UserUtilsTest.java`: quita el `// ` de cada línea de los dos tests, desde `@Test` hasta la `}` de cada uno, sin tocar las líneas `// ====` ni `// PASO`. Ejecuta `mvn test -Dtest=UserUtilsTest`: `Tests run: 2`.

Los valores 18 y 17 son los límites de «mayor de edad», como en las semanas 3 y 4.

## Paso 2: calculateDiscount y assertThrows (5 min)

Descomenta `PASO 2` y ejecuta. En el segundo test, la llamada va dentro de la lambda `() -> ...`.

Haz que la llamada se ejecute antes: copia `UserUtils.calculateDiscount(price, percent);` en una línea propia justo encima de `IllegalArgumentException error = assertThrows(`, con la misma sangría, y ejecuta:

```text
[ERROR] com.bootcamp.UserUtilsTest.shouldThrowAnErrorWhenPercentIsGreaterThan100 -- Time elapsed: 0.023 s <<< ERROR!
java.lang.IllegalArgumentException: Invalid percent
...
[ERROR] Errors: 
[ERROR]   UserUtilsTest.shouldThrowAnErrorWhenPercentIsGreaterThan100:69 » IllegalArgument Invalid percent
[INFO] 
[ERROR] Tests run: 4, Failures: 0, Errors: 1, Skipped: 0
```

El error saltó antes de llegar a `assertThrows`, así que nadie lo esperaba: Surefire lo cuenta como `Error`, no como `Failure`. El número de línea depende de tu archivo. Borra esa línea.

## Paso 3: isValidEmail (5 min)

Descomenta `PASO 3` y ejecuta. Llevas 6 tests en verde.

## Paso 4: createUser con assertEquals (10 min)

Escribe tú el test del `PASO 4`, con AAA, `@Test`, `@DisplayName` y nombre `should<Resultado>When<Condición>`:

- Arrange: `name` es `"  Ana Pérez "` (con espacios) y `email` es `"ANA@Example.com"`.
- Act: llama a `UserUtils.createUser(name, email)` y guarda el resultado en `User result`.
- Assert: `assertEquals` con el `User` que debe devolver, escrito a mano leyendo qué hace la función con cada campo: `new User("...", "...", ...)`.

Antes de dejarlo con `assertEquals`, prueba `assertSame` con el mismo `User` (añade `import static org.junit.jupiter.api.Assertions.assertSame;` arriba):

```text
org.opentest4j.AssertionFailedError: expected: com.bootcamp.User@6bc407fd<User[name=Ana Pérez, email=ana@example.com, active=true]> but was: com.bootcamp.User@4671115f<User[name=Ana Pérez, email=ana@example.com, active=true]>
```

Mismo contenido, distinto objeto: `assertSame` pregunta si son el mismo objeto en memoria, como `is` en Python. Vuelve a `assertEquals` y borra el `import` de `assertSame`.

## Paso 5: findUserByEmail con assertEquals y assertNull (15 min)

Dos tests en el `PASO 5`. En los dos, el Arrange crea una lista con `createUser`:

```java
List<User> users = List.of(
        UserUtils.createUser("Ana", "ana@example.com"),
        UserUtils.createUser("Luis", "luis@example.com"));
```

`List.of(...)` crea una lista que no se puede modificar, el `[...]` de JavaScript y Python.

- Busca `"luis@example.com"`: el esperado es el `User` de Luis, con sus tres campos.
- Busca un email que no está: `assertNull(result)`.

## Paso 6: decimales con delta (10 min)

Empieza por el caso de la semana 3: precio `19.99` con un 15 %, esperado `16.99` y delta `0.0001`. Ejecuta:

```text
[ERROR]   UserUtilsTest.shouldReturn1699WhenPriceIs1999AndPercentIs15:176 expected: <16.99> but was: <16.9915>
```

Cambia el delta a `0.01` y ejecuta otra vez: pasa, como pasaba `toBeCloseTo(16.99)` en Jest. El test no ha cambiado; el delta decide qué da por bueno. La función no redondea a céntimos, y la pregunta para negocio de la semana 3 sigue abierta. Con `0.0001`, el test no la tapa.

Ahora escribe el test del `TODO`: precio `29.90` con un 10 %. A mano, 29,90 − 2,99 = 26,91. Escríbelo primero sin delta, `assertEquals(26.91, result)`:

```text
[ERROR]   UserUtilsTest.shouldReturn2691WhenPriceIs2990AndPercentIs10:176 expected: <26.91> but was: <26.909999999999997>
```

El esperado es correcto; el binario no. Añade el delta, `assertEquals(26.91, result, 0.0001)`, y ejecuta: pasa. Borra el test del 19,99.

## Paso 7: dos tests con AssertJ (5 min)

Añade arriba, junto a los otros `import static`:

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
```

Reescribe el Assert de dos tests del paso 2:

- `assertEquals(80, result);` pasa a `assertThat(result).isEqualTo(80);`.
- El `assertThrows` y el `assertEquals` del mensaje pasan a una sola comprobación:

```java
assertThatThrownBy(() -> UserUtils.calculateDiscount(price, percent))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid percent");
```

Cambia un momento `"Invalid percent"` por `"Invalid percentage"` para ver el mensaje de AssertJ:

```text
Expecting message to be:
  "Invalid percentage"
but was:
  "Invalid percent"
```

Déjalo como estaba y ejecuta: los 10 tests pasan.

## Paso 8: filtrar por nombre (5 min)

```bash
mvn test -Dtest='UserUtilsTest#*Email*'
```

```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Se ejecutan los 5 tests cuyo nombre contiene `Email`, con `E` mayúscula. Prueba con `*email*`: `Tests run: 0`.

Borra las líneas `TODO` del archivo y guarda el lab en git:

```bash
git add .
git commit -m "lab 02: UserUtils con AAA y assertions"
```

---

## Comprobación final

```bash
mvn test -Dtest=UserUtilsTest | grep "Tests run" | tail -1
# [INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
grep -c TODO src/test/java/com/bootcamp/UserUtilsTest.java
# 0
grep -c "assertThat" src/test/java/com/bootcamp/UserUtilsTest.java
# 4
```

`4` son los dos `import static` y los dos tests del paso 7.

Repasa:

- [ ] Cada test tiene los tres comentarios AAA, `@DisplayName` y un nombre `should...When...`.
- [ ] `createUser` y la búsqueda con éxito comparan el `User` completo con `assertEquals`, esperado primero.
- [ ] El email que no existe se comprueba con `assertNull` y el descuento de 29,90 con delta `0.0001`.

## Qué has aprendido

- `assertEquals` compara contenido, también de un `record`; `assertSame` compara identidad y casi nunca es lo que quieres.
- Una excepción fuera de `assertThrows` es un `Error` en el informe, no un `Failure`.
- El delta de `assertEquals` lo eliges tú: uno grande tapa redondeos que el código no hace.
- AssertJ escribe la misma comprobación empezando por el valor obtenido.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer proyecto con Maven](../lab-01-primer-proyecto-maven/README.md) | [Lab 03 - De casos de prueba a tests](../lab-03-de-casos-a-tests/README.md) → |

[↑ Semana 05](../../README.md)
