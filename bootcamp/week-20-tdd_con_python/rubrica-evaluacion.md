# Rúbrica de Evaluación - Semana 20

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Ciclo TDD, Red válido, diseño emergente, código legado y propiedades |
| Desempeño | 40% | Kata y ejercicio de propiedades completados con cada ciclo verificado |
| Producto | 30% | Servicio del dominio construido con TDD, con evidencia de los ciclos |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Distingue un Red válido (`NotImplementedError`, `AssertionError` esperado) de uno falso (`ModuleNotFoundError`, `SyntaxError`).
- Explica qué detecta mypy que los tests de ejemplo no ven y por qué.
- Explica qué aportan `dataclass(frozen=True)`, `Protocol` y un fake frente a un `Mock`.
- Describe un test de caracterización y un seam en código legado.
- Elige el tipo de propiedad adecuado (invariante, ida y vuelta, idempotencia, oráculo) y explica el shrinking.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde fases del ciclo o no distingue ejemplos de propiedades.

---

## 2) Desempeño (40%)

### Criterios

- Completa los 6 ciclos del kata viendo cada Red con el mensaje esperado antes del Green.
- Refactoriza el kata con `pytest` y `mypy` en verde.
- En el ejercicio 02 corrige los cuatro problemas en orden: tipo (`mypy`), suma, reparto justo y entrada inválida.
- Explica por qué el primer arreglo del reparto cumplía la suma pero no la justicia.

### Niveles

- **Alto (36-40):** ejercicios completos, cada Red verificado y cada corrección explicada.
- **Medio (28-35):** ejercicios completos sin evidencia de algunos Reds o sin explicar las propiedades.
- **Bajo (<28):** ejercicios incompletos o código escrito antes de su test.

---

## 3) Producto (30%)

### Criterios

- Al menos 15 tests del servicio, incluidas 2 propiedades con `@given`.
- Evidencia de al menos 5 ciclos completos (Red, Green y Refactor), por commits o salidas de consola.
- `dataclass(frozen=True)` para el modelo y el fake en memoria usado desde fixtures.
- `uv run pytest -q` y `uv run mypy .` en verde, sin tests en `skipped`.

### Niveles

- **Alto (27-30):** ciclos claros, propiedades con significado de negocio y diseño limpio.
- **Medio (21-26):** cumple lo mínimo con ciclos poco visibles o propiedades triviales.
- **Bajo (<21):** sin evidencia de TDD o sin propiedades.

---

## Condiciones de aprobación

1. Obtener mínimo 70% en cada tipo de evidencia.
2. Entregar proyecto ejecutable y coherente con el dominio asignado.
3. Mantener originalidad del trabajo y convenciones del bootcamp.
