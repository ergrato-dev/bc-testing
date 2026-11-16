# Lab 01 - Del stub a mano al primer mock

## Objetivo

Sustituir dos dobles de prueba escritos a mano por mocks de Mockito, programar sus respuestas con `when(...).thenReturn(...)` y `thenThrow(...)`, y crear los mocks con `@Mock` e `@InjectMocks`. Vas a ver qué devuelve un mock cuando nadie le dijo qué responder.

## Tiempo estimado

70 minutos.

## Preparación

```bash
cd starter
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

La primera ejecución descarga Mockito y `maven-dependency-plugin`, y tarda más. El `pom.xml` ya carga Mockito como agente de Java; la [teoría 01](../../1-teoria/01-mocks-y-stubbing.md) explica cada línea.

- `src/main/java/com/bootcamp/HoldService.java` reserva un libro: comprueba que el socio está activo (`MemberDirectory`), busca el libro y cuenta las reservas que ya tiene (`BookRepository`), y pone al socio al final de la cola. La cola admite 5 reservas.
- `BookRepository` y `MemberDirectory` son interfaces: en producción, una base de datos y una API HTTP. No tienen implementación en este proyecto.
- `src/test/java/com/bootcamp/HoldServiceTest.java` trae dos dobles escritos a mano y dos tests (PASO 0), y los PASOS 1 a 3 comentados.

## Paso a paso

### Paso 1: leer el problema (PASO 0)

Abre `HoldServiceTest.java`. `StubBookRepository` implementa los tres métodos de `BookRepository` aunque los tests solo necesitan dos respuestas, y para cada caso nuevo (un libro que no existe, un directorio que no responde) tendrías que añadirle constructores o clases nuevas. Si mañana la interfaz gana un método, los dos stubs dejan de compilar.

### Paso 2: mock() y when (PASO 1)

1. Borra el bloque del PASO 0 entero: las dos clases `Stub...` y sus dos tests.
2. Descomenta los imports marcados con `PASO 1`.
3. Descomenta los tres campos del bloque `PASO 1 (se borra en el PASO 3)` y los dos tests del PASO 1.

```bash
mvn test
# [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```

`mock(BookRepository.class)` crea en tiempo de ejecución un objeto que implementa la interfaz. Cada `when(...).thenReturn(...)` le dice qué devolver para esa llamada con esos argumentos.

### Paso 3: experimento, la respuesta por defecto

En `shouldPlaceTheFirstHoldWhenNobodyIsWaiting`, borra la línea `when(books.findByIsbn(DUNE.isbn())).thenReturn(Optional.of(DUNE));` y ejecuta:

```bash
mvn test
# [ERROR] com.bootcamp.HoldServiceTest.shouldPlaceTheFirstHoldWhenNobodyIsWaiting -- Time elapsed: 0.009 s <<< ERROR!
# java.util.NoSuchElementException: unknown book: 9780441172719
# [ERROR] Tests run: 2, Failures: 0, Errors: 1, Skipped: 0
```

Un método sin stub no falla: devuelve un valor vacío del tipo, aquí `Optional.empty()`, y el servicio cree que el libro no existe. La tabla de la teoría 01 lista los valores por defecto. Vuelve a poner la línea.

### Paso 4: respuestas de error y thenThrow (PASO 2)

Descomenta los imports marcados con `PASO 2` y los cuatro tests del PASO 2.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

- `thenReturn(false)`, `thenReturn(Optional.empty())` y `thenReturn(HoldService.MAX_HOLDS)` llevan al servicio por cada rama de error sin preparar datos.
- `thenThrow(...)` simula que el directorio de socios no responde. Con un servicio real tendrías que apagarlo.
- `anyString()` acepta cualquier socio: lo importante del test es la excepción, no el argumento.

### Paso 5: @Mock e @InjectMocks (PASO 3)

1. Borra el bloque `PASO 1 (se borra en el PASO 3)` (los tres campos) y el import de `mock` marcado igual.
2. Descomenta los imports marcados con `PASO 3`, la línea `@ExtendWith(MockitoExtension.class)` encima de la clase y los campos del PASO 3.

```bash
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

`MockitoExtension` crea un mock nuevo para cada campo `@Mock` antes de cada test, y construye `HoldService` pasándole esos mocks por el constructor (`@InjectMocks`).

Experimento: comenta `@ExtendWith(MockitoExtension.class)` y ejecuta.

```bash
mvn test
# java.lang.NullPointerException: Cannot invoke "com.bootcamp.MemberDirectory.isActive(String)" because "this.members" is null
# [ERROR] Tests run: 6, Failures: 0, Errors: 6, Skipped: 0
```

Sin la extensión nadie lee las anotaciones y los campos se quedan en `null`. Vuelve a descomentar la línea.

### Paso 6: experimento, Mockito sin agente

En `pom.xml`, borra la línea `<argLine>...</argLine>` del plugin de Surefire y ejecuta:

```bash
mvn test
# Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build as described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
# WARNING: A Java agent has been loaded dynamically (/home/<usuario>/.m2/repository/net/bytebuddy/byte-buddy-agent/1.17.7/byte-buddy-agent-1.17.7.jar)
# ...
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Los tests pasan hoy, pero el JDK avisa de que dejará de permitirlo. Vuelve a poner la línea.

## Comprobación

```bash
cd ../solution
mvn test
# [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

## Preguntas de cierre

1. ¿Qué tendrías que cambiar en `StubBookRepository` para probar el caso "cola llena"? ¿Y con un mock?
2. ¿Por qué el paso 3 dio un error de "libro desconocido" y no un error de Mockito?
3. ¿Qué hace `@InjectMocks` si `HoldService` tiene un constructor con dos parámetros?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - verify y ArgumentCaptor](../lab-02-verify-y-captores/README.md) → |

[↑ Semana 26](../../README.md)
