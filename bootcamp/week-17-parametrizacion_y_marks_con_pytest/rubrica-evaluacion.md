# Rúbrica de Evaluación - Semana 17

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Parametrización, marks y ejecución selectiva con criterio |
| Desempeño | 40% | Prácticas con tablas de casos y segmentacion de suite |
| Producto | 30% | Proyecto con suites marcadas por riesgo funcional |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica cuándo usar `parametrize` en vez de tests separados.
2. Diseña casos tabulares con cobertura de borde y error.
3. Justifica la taxonomía de marks (`smoke`, `regression`, `slow`) y el uso de `skip`, `skipif` y `xfail(strict=True)`.
4. Diferencia los filtros `-m` y `-k` y explica qué selecciona cada expresión (`not slow` frente a la suite completa).

### Niveles

- **Alto (27-30 pts)**: aplica conceptos con criterio de calidad y mantenimiento.
- **Medio (21-26 pts)**: comprensión correcta con oportunidades de mejora.
- **Bajo (0-20 pts)**: uso mecánico sin justificación de decisiones.

---

## 2) Desempeño (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Usa `@pytest.mark.parametrize` con casos relevantes.
3. Registra marks en `[tool.pytest]` con `strict = true` y los aplica de forma consistente.
4. Ejecuta subconjuntos y verifica resultados esperados.

### Niveles

- **Alto (36-40 pts)**: suite ordenada, legible y con buena señal.
- **Medio (28-35 pts)**: funcionamiento correcto con menor precision.
- **Bajo (0-27 pts)**: parametrización trivial o marks desordenados.

---

## 3) Producto (30%)

### Criterios

1. Adapta la plantilla al dominio asignado.
2. Incluye mínimo 8 casos parametrizados útiles.
3. Define al menos 3 marks con uso claro.
4. Documenta una estrategia mínima de ejecución selectiva con `-m` y `-k`.

### Niveles

- **Alto (27-30 pts)**: suite escalable y bien segmentada por riesgo.
- **Medio (21-26 pts)**: cumple el mínimo sin optimización clara.
- **Bajo (0-20 pts)**: baja trazabilidad de calidad por categorías.

---

## Penalizaciones

- Parametrize con casos redundantes sin nuevo valor: hasta -8 pts.
- Marks inconsistentes o sin criterio: hasta -8 pts.
- Nombres genéricos de tests/casos: hasta -6 pts.
- Mezclar contenido JS/Java en semana Python: hasta -8 pts.

---

## Checklist de entrega

- [ ] Prácticas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de ejecución con `uv run pytest -m` y `-k`.
- [ ] Casos parametrizados cubren happy path, borde y error.
- [ ] Marks documentados y reutilizables.
