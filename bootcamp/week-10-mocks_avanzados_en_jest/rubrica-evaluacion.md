# Rúbrica de Evaluación - Semana 10

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Comprensión de mocks, stubs, spies y aislamiento de dependencias |
| Desempeño | 40% | Ejecución correcta de prácticas guiadas con Jest |
| Producto | 30% | Proyecto semanal funcional con suite aislada y legible |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica diferencias entre `mock`, `stub` y `spy` con ejemplos concretos.
2. Identifica cuándo usar `jest.fn()`, `jest.spyOn()` y `jest.mock()`.
3. Describe riesgos de sobre-mocking y estrategias para evitarlos.
4. Interpreta fallos de tests basados en interacciones (args/calls/order).

### Niveles

- **Alto (27-30 pts)**: domina conceptos y decide técnica adecuada por escenario.
- **Medio (21-26 pts)**: comprende base, con confusiones menores en elección de técnica.
- **Bajo (0-20 pts)**: aplica mocks sin criterio o no distingue tipos de doubles.

---

## 2) Desempeño (40%)

### Criterios

1. Completa ejercicios siguiendo formato guiado (descomentar).
2. Usa patrón AAA con claridad.
3. Valida interacciones con matchers apropiados (`toHaveBeenCalledWith`, `toHaveBeenCalledTimes`).
4. Limpia estado entre tests (`clearAllMocks`, restauración de spies).

### Niveles

- **Alto (36-40 pts)**: pruebas limpias, aisladas y estables.
- **Medio (28-35 pts)**: pruebas funcionales con pequeños problemas de aislamiento.
- **Bajo (0-27 pts)**: pruebas inestables o con dependencias reales no controladas.

---

## 3) Producto (30%)

### Criterios

1. Proyecto adapta plantilla al dominio asignado sin copiar ejemplos.
2. Suite incluye casos positivos, validaciones y propagación de errores.
3. Dependencias externas aisladas con mocks coherentes.
4. Mínimo sugerido: 8 tests descriptivos y ejecutables.

### Niveles

- **Alto (27-30 pts)**: suite robusta, consistente y fácil de mantener.
- **Medio (21-26 pts)**: cumple objetivos con areas de mejora en claridad o cobertura.
- **Bajo (0-20 pts)**: suite incompleta, frágil o sin aislamiento adecuado.

---

## Penalizaciones

- Uso de gestor no recomendado en JavaScript de la ruta formativa (`npm` en lugar de `pnpm`): hasta -5 pts.
- Nombres de tests genéricos (`test1`, `works`): hasta -5 pts.
- Mezcla de idiomas en nomenclatura técnica (variables/metodos en español): hasta -5 pts.
- Evidencias sin ejecución verificable de tests: hasta -10 pts.

---

## Checklist de entrega

- [ ] Ejercicios guiados completados.
- [ ] Proyecto semanal implementado en `3-proyecto/starter/`.
- [ ] Tests ejecutan en local sin errores.
- [ ] Nombres de tests describen comportamiento esperado.
- [ ] Uso intencional de mocks/spies documentado en README del proyecto.
