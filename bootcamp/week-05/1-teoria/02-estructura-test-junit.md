# Estructura de un Test en JUnit 5

> **Semana 05 — Teoría 02** | Lenguaje: Java

---

## Anatomía base

Un test en JUnit 5 se apoya en:

- `@Test` para marcar método de prueba
- `@DisplayName` para legibilidad
- Assertions de `org.junit.jupiter.api.Assertions`

Ejemplo simple:

```java
@Test
@DisplayName("should return true when age is 18")
void shouldReturnTrueWhenAgeIs18() {
    assertTrue(isAdult(18));
}
```

---

## Patrón AAA en Java

```java
@Test
@DisplayName("should return 80 when price is 100 and discount is 20")
void shouldReturn80WhenPriceIs100AndDiscountIs20() {
    // Arrange
    double price = 100;
    int percent = 20;

    // Act
    double result = PriceService.calculateDiscount(price, percent);

    // Assert
    assertEquals(80, result);
}
```

---

## Nombres de métodos de test

Recomendación:

```text
should[ExpectedResult]When[Condition]
```

Ejemplos:

- `shouldReturnFalseWhenEmailIsInvalid`
- `shouldThrowValidationErrorWhenNameIsNull`

Con `@DisplayName` puedes usar una frase más natural para reportes.

---

## Setup compartido con `@BeforeEach`

```java
import org.junit.jupiter.api.BeforeEach;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setup() {
        service = new UserService();
    }
}
```

Esto evita duplicar inicialización en cada test.

---

## Assertions clave

- `assertEquals(expected, actual)`
- `assertTrue(condition)`
- `assertFalse(condition)`
- `assertNotNull(value)`
- `assertThrows(Exception.class, executable)`

---

## Señales de mala calidad en tests

- Verifican varias cosas no relacionadas
- Nombre del test no comunica intención
- No usan AAA
- Dependencia de estado externo
- Fallos intermitentes

---

## Resumen

```text
@Test + @DisplayName
Arrange -> Act -> Assert
assertions claras y específicas
```

---

## Próximo tema

→ [Assertions y ejecución con Maven](./03-assertions-y-ejecucion.md)
