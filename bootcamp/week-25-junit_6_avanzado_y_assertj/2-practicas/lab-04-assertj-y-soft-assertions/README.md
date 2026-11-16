# Lab 04 - AssertJ y soft assertions

## Objetivo

Reescribir con AssertJ dos tests de un catálogo de libros que usan aserciones de JUnit, comparar los mensajes de fallo y comprobar varios campos de un resultado con soft assertions, primero con `assertSoftly` y después con la extensión de AssertJ.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

- `src/main/java/com/bootcamp/Catalog.java` guarda libros (`Book`: ISBN, título, autor y año). `byAuthor` devuelve los de un autor ordenados por año; `search` busca en el título sin distinguir mayúsculas; `findByIsbn` devuelve un `Optional<Book>`.
- `src/test/java/com/bootcamp/CatalogTest.java` añade cinco libros en un `@BeforeEach`, trae dos tests con aserciones de JUnit (PASO 0) y los PASOS 1 a 4 comentados.

La [teoría 04](../../1-teoria/04-assertj-y-soft-assertions.md) explica cada aserción.

## Paso a paso

### Paso 1: experimento, el mensaje de JUnit (PASO 0)

En `Catalog.java`, comenta la línea `.sorted(Comparator.comparingInt(Book::year))` de `byAuthor` y ejecuta:

```bash
mvn test
# [ERROR]   CatalogTest.shouldListTheBooksOfAnAuthorFromOldestToNewestWithJUnit:52 expected: <Dune Messiah> but was: <Children of Dune>
# [ERROR] Tests run: 2, Failures: 1, Errors: 0, Skipped: 0
```

El mensaje dice que un elemento no es el esperado, pero no enseña la lista. **Deja la línea comentada** para el paso 2.

### Paso 2: aserciones sobre colecciones (PASO 1)

1. Borra los dos tests del PASO 0 y los dos imports marcados con `PASO 0`.
2. Descomenta los dos tests del PASO 1.

Con `.sorted` todavía comentado:

```bash
mvn test
# [ERROR]   CatalogTest.shouldListTheBooksOfAnAuthorFromOldestToNewest:NN
# Expecting actual:
#   ["Dune", "Children of Dune", "Dune Messiah"]
# to contain exactly (and in same order):
#   ["Dune", "Dune Messiah", "Children of Dune"]
# but there were differences at these indexes:
#   - element at index 1: expected "Dune Messiah" but was "Children of Dune"
#   - element at index 2: expected "Children of Dune" but was "Dune Messiah"
```

`NN` es el número de línea del test en tu archivo. Ahora ves la lista real y cada diferencia: el problema es el orden. Descomenta `.sorted(...)` en `Catalog.java`:

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

### Paso 3: Optional (PASO 2)

Descomenta los dos tests del PASO 2.

```bash
mvn test
# [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

`hasValueSatisfying` comprueba que hay libro y su título en una sola cadena, sin llamar a `get()`.

### Paso 4: soft assertions con assertSoftly (PASO 3)

Descomenta el import marcado con `PASO 3` y el test del PASO 3.

```bash
mvn test
# [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Experimento: en ese test, cambia `"Ursula K. Le Guin"` por `"Ursula Le Guin"` y `1974` por `1975`.

```bash
mvn test
# [ERROR]   CatalogTest.shouldKeepEveryFieldOfTheBook:NN
# Multiple Failures (2 failures)
# -- failure 1 --
# expected: "Ursula Le Guin"
#  but was: "Ursula K. Le Guin"
# at CatalogTest.lambda$shouldKeepEveryFieldOfTheBook$2(CatalogTest.java:NN)
# -- failure 2 --
# expected: 1975
#  but was: 1974
# at CatalogTest.lambda$shouldKeepEveryFieldOfTheBook$2(CatalogTest.java:NN)
```

Los dos fallos en una sola ejecución. Con `assertThat` normal habrías visto solo el primero. Deshaz los dos cambios.

### Paso 5: la extensión de AssertJ (PASO 4)

1. Descomenta los imports marcados con `PASO 4`.
2. Descomenta `@ExtendWith(SoftAssertionsExtension.class)` encima de la clase.
3. Descomenta el campo `softly` con `@InjectSoftAssertions` y el test del PASO 4.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

La extensión crea un `SoftAssertions` nuevo para cada test y llama a `assertAll()` al terminar. No hay lambda ni `assertAll()` que olvidar.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué información da el mensaje de `containsExactly` que no daba el de `assertEquals`?
2. ¿En qué caso de tu dominio usarías soft assertions y en cuál no?
3. ¿Qué pasa si usas `new SoftAssertions()` y olvidas `assertAll()`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - @TempDir y extensiones](../lab-03-extensiones-y-tempdir/README.md) | [Proyecto — Proyecto Semanal - Suite Java del dominio con JUnit 6 y AssertJ](../../3-proyecto/README.md) → |

[↑ Semana 25](../../README.md)
