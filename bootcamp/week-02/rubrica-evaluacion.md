# Rúbrica de Evaluación — Semana 02

> Casos de Prueba y Técnicas de Diseño

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

1. ¿Qué campos son obligatorios en un caso de prueba profesional? (menciona al menos 5)
2. ¿Cuál es la diferencia entre "resultado esperado" y "resultado obtenido" en un caso de prueba?
3. ¿Qué es la partición de equivalencia y qué problema resuelve?
4. ¿Cuántas clases de equivalencia genera un campo que acepta edades entre 18 y 65?
5. ¿Qué es el análisis de valores límite (BVA) y por qué complementa a la partición de equivalencia?
6. ¿Cuándo conviene usar una tabla de decisión en lugar de casos individuales?
7. ¿Cuál es la diferencia entre testing exploratorio y testing scripted?
8. ¿Qué es un smoke test y cuándo se ejecuta?
9. ¿Cuál es la diferencia entre regression testing y sanity testing?
10. ¿Por qué "100% de cobertura de casos de prueba" no garantiza ausencia de bugs?

### Criterios de Calificación por Pregunta

- **3 pts** — Respuesta completa, precisa y con ejemplo o justificación
- **2 pts** — Respuesta correcta pero incompleta o sin ejemplo
- **1 pt** — Respuesta parcialmente correcta con imprecisiones notables
- **0 pts** — Respuesta incorrecta o en blanco

---

## 💪 Desempeño (40 puntos)

### Ejercicio 01 — Analizar y Completar Casos de Prueba (20 pts)

El estudiante recibe 4 casos de prueba con defectos y los corrige.

| Criterio | Pts |
|---|---|
| Identificó correctamente los campos faltantes o incorrectos (≥3 de 4 casos) | 6 |
| Redactó precondiciones claras y verificables | 4 |
| Redactó pasos reproducibles en orden lógico (sin ambigüedades) | 4 |
| Redactó resultados esperados en términos concretos y observables | 4 |
| Asignó severidad y prioridad coherentes con el contexto | 2 |
| **Total** | **20** |

### Ejercicio 02 — Aplicar BVA y Partición de Equivalencia (20 pts)

El estudiante diseña casos de prueba para una función de validación usando técnicas formales.

| Criterio | Pts |
|---|---|
| Identificó correctamente todas las clases de equivalencia válidas e inválidas | 6 |
| Aplicó BVA correctamente: probó valores en el límite, justo dentro y justo fuera | 6 |
| No generó casos redundantes (aplica el principio de reducción) | 4 |
| Nombró los casos con claridad (qué se testea y por qué) | 4 |
| **Total** | **20** |

---

## 📦 Producto (30 puntos)

### Suite de Casos de Prueba del Dominio Asignado (`casos-de-prueba.md`)

El estudiante entrega una suite manual completa para una funcionalidad de su dominio asignado.

| Criterio | Pts |
|---|---|
| La suite cubre el happy path con al menos 3 casos de prueba | 4 |
| Aplica partición de equivalencia: al menos 2 clases inválidas identificadas y testeadas | 5 |
| Aplica BVA en campos con rangos numéricos o de longitud | 5 |
| Los precondiciones son claras y reproducibles | 4 |
| Los pasos están numerados, son atómicos y no ambiguos | 4 |
| Los resultados esperados son observables y concretos (no "funciona correctamente") | 4 |
| La severidad y prioridad son coherentes entre sí y con el contexto del dominio | 2 |
| La funcionalidad testeada está claramente identificada (nombre, descripción, scope) | 2 |
| **Total** | **30** |

---

## 📝 Penalizaciones

| Situación | Penalización |
|---|---|
| Casos de prueba con pasos ambiguos o no reproducibles | −2 pts c/u (máx −6) |
| Resultados esperados vagos ("debería funcionar", "responde bien") | −1 pt c/u (máx −4) |
| Suite entregada sin identificar la funcionalidad testeada | −5 pts |
| Evidencia de copia de la suite de otro aprendiz | −30 pts (nota mínima) |

---

## 🏆 Escala de Calificación Final

| Rango | Calificación |
|---|---|
| 90–100 pts | Excelente — Dominio sólido de técnicas de diseño |
| 80–89 pts | Muy bien — Maneja las técnicas con pequeños errores |
| 70–79 pts | Aprobado — Comprende los conceptos pero necesita práctica |
| < 70 pts | No aprobado — Requiere refuerzo antes de continuar |
