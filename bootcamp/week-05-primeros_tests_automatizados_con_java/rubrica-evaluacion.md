# Rúbrica de Evaluación — Semana 05

> Primeros Tests Automatizados con Java (JUnit 5)

---

## 📊 Distribución de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| 🧠 Conocimiento | 30% | 30 pts |
| 💪 Desempeño | 40% | 40 pts |
| 📦 Producto | 30% | 30 pts |
| **Total** | **100%** | **100 pts** |

**Nota mínima por componente**: 70% (21/30 — 28/40 — 21/30)

---

## 🧠 Conocimiento (30 puntos)

Cuestionario teórico de 10 preguntas — 3 puntos cada una.

### Preguntas

1. ¿Qué función cumple `@Test` en JUnit 5?
2. ¿Por qué `@DisplayName` mejora la legibilidad de reportes?
3. Explica AAA aplicado a un test en Java.
4. ¿Cuándo usar `assertEquals` y cuándo `assertTrue`?
5. ¿Cómo se valida una excepción esperada y su mensaje con JUnit 5? ¿Y con AssertJ?
6. ¿Qué comando se usa para ejecutar tests con Maven?
7. ¿Cuándo reporta Surefire un test como Failure y cuándo como Error? Da un ejemplo de cada uno.
8. ¿Por qué `assertEquals(15.992, result)` puede fallar con un `double` y cómo se corrige?
9. ¿En qué orden se ejecutan `@BeforeAll`, `@BeforeEach`, `@AfterEach` y `@AfterAll`, y por qué un test no debe depender de otro?
10. ¿Cuál es la diferencia entre JUnit 4 y JUnit 5 a nivel de anotaciones básicas?

---

## 💪 Desempeño (40 puntos)

### Ejercicio 01 — Setup y primera ejecución con JUnit (20 pts)

| Criterio | Pts |
|---|---|
| Configuró estructura Maven básica correctamente | 4 |
| Ejecutó `mvn test` con al menos 1 test en pass | 4 |
| Identificó fallo inicial intencional | 4 |
| Corrigió implementación para dejar suite en verde | 4 |
| Explicó comandos y salida principal de Maven | 4 |
| **Total** | **20** |

### Ejercicio 02 — Tests unitarios básicos en Java (20 pts)

| Criterio | Pts |
|---|---|
| Aplicó patrón AAA con claridad | 6 |
| Usó assertions adecuadas (`assertEquals` con delta, `assertThrows`, AssertJ) | 6 |
| Explicó el Error del PASO 5 y corrigió `isValidEmail` | 4 |
| Nombres de métodos y `@DisplayName` descriptivos | 4 |
| **Total** | **20** |

---

## 📦 Producto (30 puntos)

### Suite inicial del dominio (`ItemServiceTest.java`)

| Criterio | Pts |
|---|---|
| Incluye al menos 8 tests funcionales | 4 |
| Cubre happy path, validaciones y errores | 6 |
| Mantiene AAA y usa `@BeforeEach` para el servicio bajo prueba | 6 |
| Nombres de métodos y `@DisplayName` legibles | 4 |
| No depende de recursos externos reales | 4 |
| Incluye la tabla comparativa JS ↔ Python ↔ Java (3 intenciones equivalentes) | 3 |
| `mvn test` sin failures, errors ni tests `@Disabled` | 3 |
| **Total** | **30** |

---

## 📝 Penalizaciones

| Situación | Penalización |
|---|---|
| Nombres genéricos de test (`test1`, `shouldWork`) | −2 pts c/u (máx −6) |
| Tests sin assertions útiles | −2 pts c/u (máx −8) |
| Datos sensibles reales en tests | −5 pts |
| Evidencia de copia de la suite de otro aprendiz | −30 pts (nota mínima) |

---

## 🏆 Escala de Calificación Final

| Rango | Calificación |
|---|---|
| 90–100 pts | Excelente — Base sólida de testing con JUnit |
| 80–89 pts | Muy bien — Buen dominio con ajustes menores |
| 70–79 pts | Aprobado — Base correcta, requiere práctica |
| < 70 pts | No aprobado — Reforzar fundamentos |
