# Rubrica de Evaluacion - Semana 06

> Cierre de Etapa 0: plan de pruebas y suite base multilenguaje

---

## Distribucion de Puntos

| Tipo de Evidencia | Peso | Puntos |
|---|---|---|
| Conocimiento | 30% | 30 pts |
| Desempeno | 40% | 40 pts |
| Producto | 30% | 30 pts |
| **Total** | **100%** | **100 pts** |

**Minimo por componente**: 70% (21/30 - 28/40 - 21/30)

---

## Conocimiento (30 pts)

Cuestionario de 10 preguntas (3 pts c/u):

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

## Desempeno (40 pts)

### Ejercicio 01 - Plan de pruebas integrador (20 pts)

| Criterio | Pts |
|---|---|
| Define alcance, supuestos y riesgos | 5 |
| Escribe al menos 8 casos trazables | 5 |
| Prioriza casos (alta/media/baja) con justificacion | 5 |
| Declara criterios de entrada/salida claros | 5 |
| **Total** | **20** |

### Ejercicio 02 - Suite base multilenguaje (20 pts)

| Criterio | Pts |
|---|---|
| Implementa suite equivalente en JS/Python/Java | 6 |
| Mantiene patron AAA en los tres lenguajes | 6 |
| Incluye happy path + validaciones | 4 |
| Ejecuta pruebas localmente sin errores de sintaxis | 4 |
| **Total** | **20** |

---

## Producto (30 pts)

### Proyecto integrador de dominio (entregable obligatorio)

| Criterio | Pts |
|---|---|
| Suite con minimo 10 tests del dominio asignado | 6 |
| Cobertura de escenarios validos e invalidos | 6 |
| Trazabilidad caso -> test documentada | 5 |
| Nombres descriptivos y consistentes | 4 |
| Independencia de servicios externos reales | 4 |
| Evidencia de ejecucion en al menos un lenguaje | 5 |
| **Total** | **30** |

---

## Penalizaciones

| Situacion | Penalizacion |
|---|---|
| Tests con nombres genericos (`test1`, `works`) | -2 c/u (max -8) |
| Casos sin resultado esperado explicito | -2 c/u (max -8) |
| Uso de datos reales sensibles | -5 |
| Evidencia de copia entre dominios asignados | -30 |
