# Proyecto Semana 05 — Suite Inicial de Tests Unitarios en Java

> **Entregable obligatorio** | Etapa 0 · Semana 05

---

## 🎯 Objetivo

Construir una suite inicial de tests unitarios con JUnit 5 para las mismas funciones puras de tu dominio que testeaste en JavaScript (semana 03) y Python (semana 04), y comparar cómo se expresa cada intención en los tres lenguajes.

Debes aplicar:

- `@Test` y `@DisplayName`
- Patrón AAA
- `@BeforeEach` para crear el servicio bajo prueba
- Assertions de JUnit (`assertEquals` con delta si usas `double`, `assertThrows`) y al menos una assertion de AssertJ

---

## Reglas del proyecto

1. Definir al menos 3 funciones de negocio del dominio en `src/main/java/com/bootcamp/ItemService.java`
2. Escribir mínimo 8 tests unitarios
3. Cubrir al menos:
   - 3 happy path
   - 3 casos inválidos o error
   - 2 edge cases
4. Nombrar métodos con el patrón `should[ExpectedResult]When[Condition]`
5. Ningún test queda con `@Disabled` en la entrega
6. Ejecutar con `mvn test` desde `starter/`

---

## Alcance recomendado

Usar funciones puras, por ejemplo:

- validaciones
- cálculos
- transformaciones de datos

No usar:

- API externas
- base de datos real
- IO de archivos

---

## Punto de partida

```bash
cd starter
mvn test
```

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 3
```

Los tres tests del starter están marcados con `@Disabled` (equivalen a `test.todo` en Jest y a `pytest.skip` en pytest). Un `Skipped` distinto de 0 indica trabajo pendiente.

---

## Guía de trabajo (2 horas)

- **20 min**: definir funciones y reglas
- **25 min**: tests happy path
- **35 min**: tests de validación y errores
- **25 min**: edge cases
- **15 min**: limpieza, ejecución final y tabla comparativa

---

## Entregable

1. `starter/src/main/java/com/bootcamp/ItemService.java` con las funciones de tu dominio.
2. `starter/src/test/java/com/bootcamp/ItemServiceTest.java` completo, con tu suite adaptada al dominio asignado.
3. Salida de `mvn test` con `Failures: 0, Errors: 0, Skipped: 0`.
4. Una tabla comparativa breve (en tu README o al final de la entrega) con 3 tests equivalentes en los tres lenguajes:

| Intención | Jest (semana 03) | pytest (semana 04) | JUnit 5 (semana 05) |
|---|---|---|---|
| Igualdad | `expect(result).toBe(80)` | `assert result == 80` | `assertEquals(80, result)` |
| Excepción | `expect(() => fn()).toThrow("...")` | `with pytest.raises(ValueError, match="..."):` | `assertThrows(IllegalArgumentException.class, () -> ...)` |
| ... | ... | ... | ... |

Añade dos o tres frases con las diferencias que encontraste (tipado, forma de verificar excepciones, cómo reporta cada herramienta un fallo).

> `solution/` del proyecto no se publica en el repositorio.
