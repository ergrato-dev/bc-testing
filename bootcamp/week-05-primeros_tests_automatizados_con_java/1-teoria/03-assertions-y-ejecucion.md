# Assertions y Ejecución con Maven

> **Semana 05 — Teoría 03** | Lenguaje: Java

---

## Assertions más usadas en JUnit 5

```java
assertEquals(10, total);
assertTrue(isValid);
assertFalse(isExpired);
assertNotNull(user);
```

Para excepciones:

```java
IllegalArgumentException ex = assertThrows(
    IllegalArgumentException.class,
    () -> service.create(null)
);
assertEquals("payload is required", ex.getMessage());
```

---

## Ejecutar tests

```bash
mvn test
```

Opciones útiles:

```bash
mvn -Dtest=CalculatorTest test
mvn -Dtest=CalculatorTest#shouldReturnFiveWhenAddingTwoAndThree test
```

---

## Failure vs Error en JUnit

### Failure

La assertion no coincide.

```text
expected: <5> but was: <6>
```

### Error

Excepción inesperada antes de llegar al assert.

```text
java.lang.NullPointerException
```

---

## Ciclo Red-Green-Refactor

1. Red: crear test que falla
2. Green: implementar mínimo para pasar
3. Refactor: mejorar diseño manteniendo tests en verde

---

## Checklist de calidad mínima

- [ ] Nombre y `@DisplayName` descriptivos
- [ ] AAA visible
- [ ] Assertions específicas
- [ ] Sin dependencias externas reales
- [ ] Suite pasa con `mvn test`

---

## Cierre

Con esta base ya puedes expresar en Java la misma intención de pruebas que viste en JavaScript y Python.

← [Estructura en JUnit 5](./02-estructura-test-junit.md) | [Volver al README](../README.md)
