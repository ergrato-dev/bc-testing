# Rubrica de Evaluacion - Semana 17

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Parametrizacion, marks y ejecucion selectiva con criterio |
| Desempeno | 40% | Practicas con tablas de casos y segmentacion de suite |
| Producto | 30% | Proyecto con suites marcadas por riesgo funcional |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica cuando usar `parametrize` en vez de tests separados.
2. Diseña casos tabulares con cobertura de borde y error.
3. Justifica taxonomia de marks (`smoke`, `regression`, `slow`).
4. Diferencia filtros `-m` y `-k` en pytest.

### Niveles

- **Alto (27-30 pts)**: aplica conceptos con criterio de calidad y mantenimiento.
- **Medio (21-26 pts)**: comprension correcta con oportunidades de mejora.
- **Bajo (0-20 pts)**: uso mecanico sin justificacion de decisiones.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Usa `@pytest.mark.parametrize` con casos relevantes.
3. Implementa marks consistentes con objetivo de suite.
4. Ejecuta subconjuntos y verifica resultados esperados.

### Niveles

- **Alto (36-40 pts)**: suite ordenada, legible y con buena senal.
- **Medio (28-35 pts)**: funcionamiento correcto con menor precision.
- **Bajo (0-27 pts)**: parametrizacion trivial o marks desordenados.

---

## 3) Producto (30%)

### Criterios

1. Adapta plantilla al dominio asignado.
2. Incluye minimo 6 casos parametrizados utiles.
3. Define al menos 3 marks con uso claro.
4. Documenta estrategia minima de ejecucion selectiva.

### Niveles

- **Alto (27-30 pts)**: suite escalable y bien segmentada por riesgo.
- **Medio (21-26 pts)**: cumple minimo sin optimizacion clara.
- **Bajo (0-20 pts)**: baja trazabilidad de calidad por categorias.

---

## Penalizaciones

- Parametrize con casos redundantes sin nuevo valor: hasta -8 pts.
- Marks inconsistentes o sin criterio: hasta -8 pts.
- Nombres genericos de tests/casos: hasta -6 pts.
- Mezclar contenido JS/Java en semana Python: hasta -8 pts.

---

## Checklist de entrega

- [ ] Practicas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Evidencia de ejecucion con `pytest -m`.
- [ ] Casos parametrizados cubren happy path, borde y error.
- [ ] Marks documentados y reutilizables.
