# Rúbrica de Evaluación — Semana 01
## ¿Qué es Testing? Calidad de Software

**Puntaje total**: 100 puntos
**Criterio de aprobación**: Mínimo 70 puntos (≥70% en cada tipo de evidencia)

---

## 📊 Distribución por Tipo de Evidencia

| Tipo | Peso | Puntos |
| --- | :---: | :---: |
| 🧠 Conocimiento — Cuestionario teórico | 30% | 30 pts |
| 💪 Desempeño — Ejercicios prácticos | 40% | 40 pts |
| 📦 Producto — Reporte de bug profesional | 30% | 30 pts |

---

## 🧠 Conocimiento (30 puntos)

Cuestionario de 10 preguntas de selección múltiple y respuesta corta sobre los conceptos de la semana.

| # | Pregunta / Criterio | Puntos |
| --- | --- | :---: |
| 1 | Define con tus palabras qué es la calidad del software | 3 |
| 2 | Diferencia entre testing manual y testing automatizado | 3 |
| 3 | Nombra y describe los 4 niveles de testing (unitario, integración, sistema, aceptación) | 4 |
| 4 | Explica la forma de la pirámide de testing y qué representa cada nivel | 4 |
| 5 | ¿Cuál es la diferencia entre el rol de tester y el de SDET? | 3 |
| 6 | ¿Qué es un bug? ¿En qué se diferencia de un error y de un failure? | 4 |
| 7 | Describe las etapas del ciclo de vida de un bug | 4 |
| 8 | ¿Qué es testing preventivo vs correctivo? Da un ejemplo de cada uno | 3 |
| 9 | ¿Por qué los tests unitarios son la base de la pirámide? | 2 |
| 10 | ¿Qué ventaja tiene automatizar un test de regresión? | 2 |

### Escala de calificación

| Rango | Nivel |
| --- | --- |
| 27–30 pts | Excelente — dominio sólido de los conceptos |
| 21–26 pts | Aprobado — comprensión básica con algunas brechas |
| < 21 pts | No aprobado — requiere revisión del material |

---

## 💪 Desempeño (40 puntos)

Evaluación de los dos ejercicios prácticos de la semana.

### Ejercicio 01 — Análisis de Bugs (20 puntos)

El estudiante recibe 5 reportes de bugs incompletos o mal escritos y debe:
- Identificar qué información falta
- Clasificar la severidad y prioridad correctamente
- Corregir el título del bug siguiendo el formato `[Componente] Descripción del comportamiento incorrecto`

| Criterio | Puntos |
| --- | :---: |
| Identifica correctamente la información faltante en ≥4 de 5 reportes | 6 |
| Clasifica severidad y prioridad correctamente en ≥4 de 5 casos | 7 |
| Reformula los títulos con el formato correcto en ≥4 de 5 casos | 7 |

### Ejercicio 02 — Completar Reporte de Bug (20 puntos)

El estudiante recibe un reporte a medio completar y debe llenarlo con todos los campos requeridos de forma coherente.

| Criterio | Puntos |
| --- | :---: |
| Todos los campos obligatorios están presentes (título, pasos, resultado esperado, resultado obtenido, severidad, prioridad, entorno) | 8 |
| Los pasos para reproducir son claros, numerados y reproducibles | 6 |
| El resultado esperado y obtenido son distintos y bien descritos | 6 |

### Escala de calificación — Desempeño

| Rango | Nivel |
| --- | --- |
| 36–40 pts | Excelente |
| 28–35 pts | Aprobado |
| < 28 pts | No aprobado |

---

## 📦 Producto (30 puntos)

### Entregable: `reporte-bug.md`

El estudiante escribe un reporte de bug profesional sobre un software real de su elección, adaptado a su dominio asignado.

**Ejemplo de referencia (dominio genérico — Planetario):**
> _Al buscar una exhibición por nombre en el buscador del Planetario Virtual, el sistema devuelve resultados vacíos incluso cuando la exhibición existe en el catálogo._

| Criterio | Descripción | Puntos |
| --- | --- | :---: |
| **Título** | Claro, descriptivo, sigue el formato `[Componente] Descripción` | 4 |
| **Pasos para reproducir** | Numerados, específicos, reproducibles por cualquier persona | 5 |
| **Resultado obtenido** | Describe exactamente qué ocurre (comportamiento actual) | 4 |
| **Resultado esperado** | Describe qué debería ocurrir (comportamiento correcto) | 4 |
| **Severidad** | Valor correcto (Critical/High/Medium/Low) con justificación | 4 |
| **Prioridad** | Valor correcto (P1/P2/P3/P4) con justificación | 4 |
| **Entorno** | SO, versión de la app/navegador, dispositivo | 3 |
| **Originalidad** | El reporte es propio, no copiado de otro aprendiz | 2 |

### Escala de calificación — Producto

| Rango | Nivel |
| --- | --- |
| 27–30 pts | Excelente — reporte listo para usar en un equipo real |
| 21–26 pts | Aprobado — reporte funcional con detalles mejorables |
| < 21 pts | No aprobado — faltan campos esenciales o información no reproducible |

---

## ✅ Checklist de Entrega

Antes de enviar, el estudiante debe verificar:

- [ ] El cuestionario de conocimiento está respondido completamente
- [ ] Los dos ejercicios prácticos están completados
- [ ] El archivo `reporte-bug.md` está en la carpeta `3-proyecto/starter/`
- [ ] El reporte tiene **todos** los campos (ninguno en blanco)
- [ ] Los pasos para reproducir son numerados y claros
- [ ] La severidad y prioridad están justificadas

---

## 📝 Notas para el Instructor

- La semana 01 no tiene código — evaluar la calidad del pensamiento crítico y la comunicación escrita.
- Priorizar el razonamiento sobre la respuesta "correcta" en preguntas conceptuales.
- Un reporte de bug bien escrito en esta semana predice la calidad de las suites de tests en semanas posteriores.
- Severidades sugeridas para el cuestionario: **Critical** (sistema caído), **High** (funcionalidad principal rota), **Medium** (funcionalidad secundaria afectada), **Low** (cosmético o mínimo impacto).
