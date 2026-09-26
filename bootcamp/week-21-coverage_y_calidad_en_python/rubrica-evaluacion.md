# Rúbrica de Evaluación - Semana 21

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Branch coverage, exclusiones, mutation testing, umbrales y SonarQube |
| Desempeño | 40% | Ejercicios guiados con cada salida interpretada |
| Producto | 30% | Suite con umbrales cumplidos y `quality-report-python.md` |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Interpreta `6->8` y `BrPart` en un reporte de branch coverage.
- Distingue cuándo excluir código y cuándo escribir un test.
- Explica por qué 100% de coverage puede dejar sobrevivir mutantes y qué es un mutante equivalente.
- Explica por qué `pytest.raises(match=...)` sin anclar no mata los mutantes de cadena.
- Relaciona cada umbral con la herramienta que lo hace cumplir y describe qué lee SonarQube.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde coverage con calidad de los asserts.

---

## 2) Desempeño (40%)

### Criterios

- Ejercicio 01: configura `branch`, `exclude_also` y `fail_under`, interpreta cada reporte y llega a 100% de ramas.
- Ejercicio 02: identifica qué test mata cada mutante y justifica los tres equivalentes.
- Usa `mutmut show` para leer los mutantes, no solo el recuento.

### Niveles

- **Alto (36-40):** ejercicios completos con cada salida interpretada.
- **Medio (28-35):** ejercicios completos con interpretaciones parciales.
- **Bajo (<28):** ejercicios incompletos.

---

## 3) Producto (30%)

### Criterios

- Branch coverage de al menos 90% con `fail_under` activo.
- Todos los mutantes matables muertos; los equivalentes justificados.
- `ruff check src` en verde con `max-complexity = 5` tras un refactor protegido por la suite.
- `quality-report-python.md` con números de antes y después y decisiones explicadas.

### Niveles

- **Alto (27-30):** umbrales cumplidos y reporte que explica decisiones.
- **Medio (21-26):** umbrales cumplidos con un reporte que solo copia números.
- **Bajo (<21):** umbrales sin cumplir o sin reporte.

---

## Condiciones de aprobación

1. Obtener mínimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
