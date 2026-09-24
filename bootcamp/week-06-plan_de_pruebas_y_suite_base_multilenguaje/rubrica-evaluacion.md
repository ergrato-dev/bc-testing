# Rubrica de Evaluacion - Semana 06

> Cierre de Etapa 0: plan de pruebas y suite base multilenguaje

---

## Distribucion de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| Conocimiento | 20% | 20 pts |
| Desempeno | 30% | 30 pts |
| Producto | 50% | 50 pts |
| **Total** | **100%** | **100 pts** |

**Minimo por componente**: 70% (14/20 - 21/30 - 35/50)

> La ponderacion refleja la distribucion del tiempo de la semana: 1 h teoria, 1.5 h practicas, 5 h proyecto y 0.5 h recursos.

---

## Conocimiento (20 pts)

Cuestionario de 10 preguntas (2 pts c/u):

1. Que diferencia existe entre plan de pruebas y casos de prueba.
2. Que significa trazabilidad en testing.
3. Como se priorizan casos por riesgo.
4. Que informacion minima debe tener un caso de prueba.
5. Que son criterios de entrada y salida.
6. Como cambia la sintaxis del assert entre JS, Python y Java.
7. Por que mantener equivalencia de intencion entre lenguajes.
8. Cuando conviene automatizar y cuando mantener manual.
9. Que rol tiene AAA en suites multilenguaje.
10. Que riesgos aparecen cuando los nombres de test son vagos.

---

## Desempeno (30 pts)

### Ejercicio 01 - Plan de pruebas integrador (15 pts)

| Criterio | Pts |
|---|---|
| Define alcance, supuestos y riesgos | 4 |
| Escribe al menos 8 casos trazables | 4 |
| Prioriza casos (alta/media/baja) con justificacion | 4 |
| Declara criterios de entrada/salida claros | 3 |
| **Total** | **15** |

### Ejercicio 02 - Suite base multilenguaje (15 pts)

| Criterio | Pts |
|---|---|
| Implementa suite equivalente en JS/Python/Java | 5 |
| Mantiene patron AAA en los tres lenguajes | 4 |
| Incluye happy path + validaciones | 3 |
| Ejecuta pruebas localmente sin errores de sintaxis | 3 |
| **Total** | **15** |

---

## Producto (50 pts)

### Proyecto integrador: `test-plan.md` + suite en JS, Python y Java (entregable obligatorio)

| Criterio | Pts |
|---|---|
| `test-plan.md` con alcance, enfoque, riesgos y criterios de entrada/salida | 10 |
| Matriz de trazabilidad Requirement ID -> Test Case ID -> test en cada lenguaje | 8 |
| Minimo 6 TC implementados en JavaScript (Jest) | 8 |
| Los mismos TC implementados en Python (pytest) | 6 |
| Los mismos TC implementados en Java (JUnit 5) | 6 |
| Cobertura de happy path, validaciones y casos limite | 4 |
| Nombres descriptivos, AAA y equivalencia de intencion entre lenguajes | 4 |
| Evidencia de ejecucion de las tres suites | 4 |
| **Total** | **50** |

---

## Penalizaciones

| Situacion | Penalizacion |
|---|---|
| Tests con nombres genericos (`test1`, `works`) | -2 c/u (max -8) |
| Casos sin resultado esperado explicito | -2 c/u (max -8) |
| Uso de datos reales sensibles | -5 |
| Evidencia de copia entre dominios asignados | -30 |
