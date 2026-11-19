# Rúbrica de Evaluación - Semana 01

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Calidad y sus dimensiones, roles, manual y automatizado, niveles y pirámide, error/defecto/fallo, severidad y prioridad, ciclo de vida |
| Desempeño | 40% | Labs 01 a 04: clasificación, análisis de reportes, del mensaje al reporte y triage |
| Producto | 30% | `reporte-bug.md` de un bug real, reproducible por otra persona |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Define la calidad del software con al menos cuatro de sus dimensiones y da un ejemplo de fallo en cada una.
- Distingue tester, desarrollador y SDET por lo que hace cada uno.
- Explica cuándo conviene el testing manual y cuándo el automatizado, con la regresión como ejemplo.
- Describe los cuatro niveles de prueba y decide el nivel de un test por lo que usa.
- Explica la forma de la pirámide y los síntomas del cono de helado.
- Distingue error, defecto y fallo en un ejemplo.
- Distingue severidad de prioridad y da un caso en que no coinciden.
- Recorre el ciclo de vida de un bug, con Reopened y los caminos alternativos desde New.

### Niveles

- **Alto (27-30):** cada respuesta va con un ejemplo propio, no el de la teoría.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores (por ejemplo, entre E2E y aceptación).
- **Bajo (<21):** confunde severidad con prioridad, o error con fallo.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `clasificacion.md` sin `TODO`; cada nivel de la parte B se deduce de su columna «Qué usa»; la parte C con porcentajes.
- Lab 02: los cinco reportes analizados, sin datos de entorno inventados, con el reporte correcto reconocido y la conclusión escrita.
- Lab 03: el reporte sin `(completar`, con los huecos como `[Preguntar: ...]` concretos, el paso supuesto marcado y la respuesta al usuario.
- Lab 04: los siete reportes con su estado y justificación (duplicado con su original, `Not a Bug` con su requisito), el orden de los abiertos y la historia de BUG-207 con `Reopened`.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada decisión justificada con la teoría o un requisito.
- **Medio (28-35):** los cuatro labs completos, con justificaciones genéricas («es importante») en algunos casos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 01` pasa las 6 comprobaciones.
- El bug es real y otra persona lo reproduce solo con los pasos y el punto de partida.
- El resultado obtenido describe sin diagnosticar; el esperado es concreto y dice dónde lo dice.
- La severidad y la prioridad están justificadas con el impacto concreto.
- El entorno tiene versiones y la evidencia está adjunta o copiada tal cual.

### Niveles

- **Alto (27-30):** todo lo anterior, con la búsqueda de reportes previos en **Notas** y un camino alternativo si existe.
- **Medio (21-26):** pasan las comprobaciones, pero la persona que intenta reproducirlo tiene que preguntar algo.
- **Bajo (<21):** no pasan las comprobaciones, o el bug no se puede reproducir.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Reporte de bug profesional](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 01](README.md)
