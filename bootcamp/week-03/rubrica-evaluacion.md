# Rúbrica de Evaluación — Semana 03

> Primeros Tests Automatizados con JavaScript (Jest)

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

1. ¿Cuál es la diferencia entre `describe()` y `test()` en Jest?
2. ¿Qué rol cumple `expect()` en una prueba automatizada?
3. Explica el patrón AAA y da un ejemplo breve de cada fase.
4. ¿Cuándo usar `toBe()` y cuándo `toEqual()`?
5. ¿Qué verifica `toThrow()` y cómo se usa correctamente?
6. ¿Cuál es la diferencia entre un error de ejecución y un assertion failure?
7. ¿Qué comando se usa para ejecutar los tests con `pnpm`?
8. ¿Qué problema resuelve el modo watch (`pnpm test -- --watch`)?
9. ¿Por qué una función pura es buen candidato para tests unitarios iniciales?
10. ¿Qué características debe tener un nombre de test profesional?

### Criterios de Calificación por Pregunta

- **3 pts** — Respuesta completa, precisa y con ejemplo o justificación
- **2 pts** — Respuesta correcta pero incompleta o sin ejemplo
- **1 pt** — Respuesta parcialmente correcta con imprecisiones notables
- **0 pts** — Respuesta incorrecta o en blanco

---

## 💪 Desempeño (40 puntos)

### Ejercicio 01 — Setup y primera ejecución con Jest (20 pts)

| Criterio | Pts |
|---|---|
| Configuró correctamente estructura base (`src/`, `tests/`) | 4 |
| Ejecutó Jest y obtuvo al menos 1 test en estado pass | 4 |
| Identificó y explicó un fallo inicial intencional (red) | 4 |
| Corrigió el test para pasar (green) sin romper legibilidad | 4 |
| Documentó comandos usados con `pnpm` de forma correcta | 4 |
| **Total** | **20** |

### Ejercicio 02 — Tests unitarios básicos con AAA (20 pts)

| Criterio | Pts |
|---|---|
| Escribió tests con estructura AAA clara | 6 |
| Usó matchers adecuados (`toBe`, `toEqual`, `toThrow`) | 6 |
| Nombres de test descriptivos y orientados a comportamiento | 4 |
| Suite cubre happy path + al menos 2 sad paths | 4 |
| **Total** | **20** |

---

## 📦 Producto (30 puntos)

### Suite inicial de tests unitarios del dominio (`item.service.test.js`)

| Criterio | Pts |
|---|---|
| Incluye al menos 8 tests unitarios funcionales | 4 |
| Cubre happy path, validaciones y errores de entrada | 6 |
| Mantiene estructura AAA en todos los tests | 6 |
| Usa nombres de test consistentes (`should ... when ...`) | 4 |
| No depende de servicios externos ni base de datos real | 4 |
| Usa correctamente `beforeEach` cuando hay setup común | 3 |
| Los tests son reproducibles y pasan localmente | 3 |
| **Total** | **30** |

---

## 📝 Penalizaciones

| Situación | Penalización |
|---|---|
| Uso de `npm` en lugar de `pnpm`/`yarn` | −3 pts |
| Nombres genéricos de test (`test1`, `it works`) | −2 pts c/u (máx −6) |
| Tests sin assertions reales | −2 pts c/u (máx −8) |
| Uso de datos reales sensibles en fixtures | −5 pts |
| Evidencia de copia de la suite de otro aprendiz | −30 pts (nota mínima) |

---

## 🏆 Escala de Calificación Final

| Rango | Calificación |
|---|---|
| 90–100 pts | Excelente — Fundamentos sólidos de testing automatizado con Jest |
| 80–89 pts | Muy bien — Buen dominio con detalles por mejorar |
| 70–79 pts | Aprobado — Base correcta, requiere más práctica |
| < 70 pts | No aprobado — Reforzar antes de continuar a Python |
