# Rúbrica de Evaluación - Semana 06

> Cierre de Etapa 0: plan de pruebas y suite base multilenguaje

---

## Distribución de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| Conocimiento | 30% | 30 pts |
| Desempeño | 40% | 40 pts |
| Producto | 30% | 30 pts |
| **Total** | **100%** | **100 pts** |

**Mínimo por componente**: 70% (21/30 - 28/40 - 21/30)

---

## Conocimiento (30 pts)

Cuestionario de 10 preguntas (3 pts c/u):

1. Qué diferencia existe entre plan de pruebas y casos de prueba.
2. Qué significa trazabilidad en testing.
3. Cómo se priorizan casos por riesgo.
4. Qué información mínima debe tener un caso de prueba.
5. Qué son criterios de entrada y salida.
6. Cómo cambia la sintaxis del assert entre JS, Python y Java.
7. Por qué mantener equivalencia de intención entre lenguajes.
8. Cuándo conviene automatizar y cuándo mantener manual.
9. Qué rol tiene AAA en suites multilenguaje.
10. Qué riesgos aparecen cuando los nombres de test son vagos.

---

## Desempeño (40 pts)

### Ejercicio 01 - Plan de pruebas integrador (20 pts)

| Criterio | Pts |
|---|---|
| Define alcance, supuestos y riesgos | 5 |
| Escribe al menos 8 casos trazables | 5 |
| Prioriza casos (alta/media/baja) con justificación | 5 |
| Declara criterios de entrada/salida claros | 5 |
| **Total** | **20** |

### Ejercicio 02 - Suite base multilenguaje (20 pts)

| Criterio | Pts |
|---|---|
| Implementa suite equivalente en JS/Python/Java | 6 |
| Mantiene patrón AAA en los tres lenguajes | 6 |
| Incluye happy path + validaciones | 4 |
| Ejecuta pruebas localmente sin errores de sintaxis | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto integrador: `test-plan.md` + suite en JS, Python y Java (entregable obligatorio)

| Criterio | Puntos |
|---|---|
| `test-plan.md` con alcance, enfoque, riesgos y criterios de entrada/salida | 6 |
| Matriz de trazabilidad Requirement ID -> Test Case ID -> test en cada lenguaje | 4 |
| Mínimo 6 TC implementados en JavaScript (Jest) | 5 |
| Los mismos TC implementados en Python (pytest) | 3 |
| Los mismos TC implementados en Java (JUnit 6) | 3 |
| Cobertura de happy path, validaciones y casos límite | 3 |
| Nombres descriptivos, AAA y equivalencia de intención entre lenguajes | 3 |
| Evidencia de ejecución de las tres suites | 3 |
| **Total** | **30** |

---

## Penalizaciones

| Situación | Penalización |
|---|---|
| Tests con nombres genéricos (`test1`, `works`) | -2 c/u (máx. -8) |
| Casos sin resultado esperado explícito | -2 c/u (máx. -8) |
| Uso de datos reales sensibles | -5 |
| Evidencia de copia entre dominios asignados | -30 |
