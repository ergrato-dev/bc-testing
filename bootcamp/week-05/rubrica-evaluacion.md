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
5. ¿Cómo se valida una excepción esperada con JUnit 5?
6. ¿Qué comando se usa para ejecutar tests con Maven?
7. Diferencia entre failure y error en una ejecución de tests.
8. ¿Por qué conviene empezar con funciones puras?
9. ¿Qué problema evita usar nombres de test descriptivos?
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
| Usó assertions adecuadas (`assertEquals`, `assertThrows`) | 6 |
| Nombres de tests y `@DisplayName` descriptivos | 4 |
| Cubre happy path + casos inválidos | 4 |
| **Total** | **20** |

---

## 📦 Producto (30 puntos)

### Suite inicial del dominio (`ItemServiceTest.java`)

| Criterio | Pts |
|---|---|
| Incluye al menos 8 tests funcionales | 4 |
| Cubre happy path, validaciones y errores | 6 |
| Mantiene estructura AAA en la suite | 6 |
| Usa `@DisplayName` para legibilidad | 4 |
| No depende de servicios externos reales | 4 |
| Usa setup compartido cuando aplica | 3 |
| La suite ejecuta correctamente con Maven | 3 |
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
