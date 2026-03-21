# Rúbrica de Evaluación — Semana 04

> Primeros Tests Automatizados con Python (pytest)

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

1. ¿Qué patrón de nombres debe seguir un archivo de test en pytest?
2. ¿Qué diferencia hay entre `assert` nativo y una librería de assertions externa?
3. Explica el patrón AAA en un test de Python.
4. ¿Qué hace `pytest -v` y en qué ayuda?
5. ¿Para qué sirve `pytest -k "texto"`?
6. ¿Cómo se representa en pytest un test que espera excepción?
7. Diferencia entre failure y error en la salida de pytest.
8. ¿Por qué las funciones puras son buen punto de entrada para tests unitarios?
9. ¿Qué beneficio aporta un nombre de test descriptivo en snake_case?
10. ¿Qué problema evita ejecutar tests de forma repetible en un entorno aislado?

### Criterios de Calificación por Pregunta

- **3 pts** — Respuesta completa, precisa y con ejemplo o justificación
- **2 pts** — Respuesta correcta pero incompleta o sin ejemplo
- **1 pt** — Respuesta parcialmente correcta con imprecisiones notables
- **0 pts** — Respuesta incorrecta o en blanco

---

## 💪 Desempeño (40 puntos)

### Ejercicio 01 — Setup y primera ejecución con pytest (20 pts)

| Criterio | Pts |
|---|---|
| Configuró correctamente estructura base (`src/`, `tests/`) | 4 |
| Ejecutó pytest y obtuvo al menos 1 test en estado pass | 4 |
| Identificó y explicó un fallo inicial intencional | 4 |
| Corrigió implementación para pasar en verde | 4 |
| Documentó comandos de ejecución y filtro de tests | 4 |
| **Total** | **20** |

### Ejercicio 02 — Tests unitarios básicos con AAA (20 pts)

| Criterio | Pts |
|---|---|
| Escribió tests con estructura AAA clara | 6 |
| Usó assertions y manejo de excepciones correctamente | 6 |
| Nombres de tests descriptivos en snake_case | 4 |
| Cubre happy path + al menos 2 casos inválidos | 4 |
| **Total** | **20** |

---

## 📦 Producto (30 puntos)

### Suite inicial del dominio (`item_service_test.py`)

| Criterio | Pts |
|---|---|
| Incluye al menos 8 tests funcionales | 4 |
| Cubre happy path, validaciones y errores | 6 |
| Mantiene AAA de forma consistente | 6 |
| Nombres de test legibles y descriptivos | 4 |
| No depende de recursos externos reales | 4 |
| Usa setup compartido cuando aplica | 3 |
| Suite ejecuta correctamente en local | 3 |
| **Total** | **30** |

---

## 📝 Penalizaciones

| Situación | Penalización |
|---|---|
| Nombres genéricos de test (`test1`, `test_ok`) | −2 pts c/u (máx −6) |
| Tests sin assertions útiles | −2 pts c/u (máx −8) |
| Datos sensibles reales en tests | −5 pts |
| Evidencia de copia de la suite de otro aprendiz | −30 pts (nota mínima) |

---

## 🏆 Escala de Calificación Final

| Rango | Calificación |
|---|---|
| 90–100 pts | Excelente — Base sólida en testing con pytest |
| 80–89 pts | Muy bien — Buen dominio con pequeños ajustes |
| 70–79 pts | Aprobado — Base correcta, requiere práctica |
| < 70 pts | No aprobado — Reforzar antes de continuar |
