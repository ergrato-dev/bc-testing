# Rubrica de Evaluacion - Semana 10

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Comprension de mocks, stubs, spies y aislamiento de dependencias |
| Desempeno | 40% | Ejecucion correcta de practicas guiadas con Jest |
| Producto | 30% | Proyecto semanal funcional con suite aislada y legible |

---

## 1) Conocimiento (30%)

### Criterios

1. Explica diferencias entre `mock`, `stub` y `spy` con ejemplos concretos.
2. Identifica cuando usar `jest.fn()`, `jest.spyOn()` y `jest.mock()`.
3. Describe riesgos de sobre-mocking y estrategias para evitarlos.
4. Interpreta fallos de tests basados en interacciones (args/calls/order).

### Niveles

- **Alto (27-30 pts)**: domina conceptos y decide tecnica adecuada por escenario.
- **Medio (21-26 pts)**: comprende base, con confusiones menores en eleccion de tecnica.
- **Bajo (0-20 pts)**: aplica mocks sin criterio o no distingue tipos de doubles.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios siguiendo formato guiado (descomentar).
2. Usa patron AAA con claridad.
3. Valida interacciones con matchers apropiados (`toHaveBeenCalledWith`, `toHaveBeenCalledTimes`).
4. Limpia estado entre tests (`clearAllMocks`, restauracion de spies).

### Niveles

- **Alto (36-40 pts)**: pruebas limpias, aisladas y estables.
- **Medio (28-35 pts)**: pruebas funcionales con pequenos problemas de aislamiento.
- **Bajo (0-27 pts)**: pruebas inestables o con dependencias reales no controladas.

---

## 3) Producto (30%)

### Criterios

1. Proyecto adapta plantilla al dominio asignado sin copiar ejemplos.
2. Suite incluye casos positivos, validaciones y propagacion de errores.
3. Dependencias externas aisladas con mocks coherentes.
4. Minimo sugerido: 8 tests descriptivos y ejecutables.

### Niveles

- **Alto (27-30 pts)**: suite robusta, consistente y facil de mantener.
- **Medio (21-26 pts)**: cumple objetivos con areas de mejora en claridad o cobertura.
- **Bajo (0-20 pts)**: suite incompleta, fragil o sin aislamiento adecuado.

---

## Penalizaciones

- Uso de gestor no recomendado en JavaScript de la ruta formativa (`npm` en lugar de `yarn/pnpm`): hasta -5 pts.
- Nombres de tests genericos (`test1`, `works`): hasta -5 pts.
- Mezcla de idiomas en nomenclatura tecnica (variables/metodos en espanol): hasta -5 pts.
- Evidencias sin ejecucion verificable de tests: hasta -10 pts.

---

## Checklist de entrega

- [ ] Ejercicios guiados completados.
- [ ] Proyecto semanal implementado en `3-proyecto/starter/`.
- [ ] Tests ejecutan en local sin errores.
- [ ] Nombres de tests describen comportamiento esperado.
- [ ] Uso intencional de mocks/spies documentado en README del proyecto.
