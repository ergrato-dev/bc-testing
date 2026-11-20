# Rúbrica de Evaluación - Semana 04

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Proyecto con uv y pytest, tests como funciones y clases, `assert`, `pytest.approx`, `pytest.raises`, `FAILED` frente a `ERROR`, ejecución parcial |
| Desempeño | 40% | Labs 01 a 04: primer proyecto, AAA y asserts, casos del planetario, tests que mienten |
| Producto | 30% | La suite de la semana 3 traducida a pytest, con mutaciones anotadas |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hacen `pyproject.toml`, `.python-version`, `uv.lock` y `.venv`, y su pareja en el proyecto de la semana 3.
- Explica por qué hace falta `pythonpath = ["."]` y qué error sale sin él.
- Escribe un test como función `test_*` y agrupa tests en una clase `Test*`, con AAA y nombre `test_<resultado>_when_<condición>`.
- Elige entre `==`, `is None`, `is True`, `pytest.approx` y `pytest.raises` según el valor, y explica por qué `is` no sirve para comparar diccionarios.
- Explica por qué `pytest.approx` no acepta 16,9915 como 16,99 y `toBeCloseTo` sí.
- Explica por qué `match` falla con paréntesis y por qué `assert (condición, "mensaje")` siempre pasa.
- Distingue `FAILED` de `ERROR` y dice el código de salida de cada caso (0, 1, 2, 5).
- Ejecuta un archivo, un test por su ruta, `-k`, `-x` y `--lf`.

### Niveles

- **Alto (27-30):** responde con ejemplos propios y compara cada pieza con su equivalente de Jest.
- **Medio (21-26):** comprende los conceptos con alguna confusión menor (por ejemplo, entre `is` y `==`).
- **Bajo (<21):** no distingue `FAILED` de `ERROR`, o cree que un test en verde siempre comprueba algo.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: `~/labs-semana-04` con `pyproject.toml`, `.python-version`, `uv.lock`, 3 tests de `math_utils` en verde y un commit.
- Lab 02: 10 tests de `user_utils` en verde, sin `TODO`; `create_user` con `==`, la búsqueda fallida con `is None` y el descuento de 29,90 con `pytest.approx`.
- Lab 03: 19 tests del planetario en tres clases, con el ID de su caso, 4 hallazgos con línea y técnica, y el código corregido sin tocar ningún esperado.
- Lab 04: los cinco tests reescritos, el envío probado con 29,99 y 30, y cada una de las cuatro mutaciones detectada por un test.

### Niveles

- **Alto (36-40):** los cuatro labs completos, con cada decisión (comparación, código o test) justificada.
- **Medio (28-35):** los cuatro labs completos, con algún esperado calculado en lugar de escrito o algún nombre genérico.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- `PROYECTO=... ./scripts/verificar-semana.sh 04` pasa las 9 comprobaciones.
- Las funciones son las de la semana 3, con los mismos mensajes, y siguen siendo puras.
- Los tests cubren los mismos casos que la suite de Jest, con el ID del caso encima.
- Cada comparación corresponde a su valor; todos los esperados están escritos a mano.
- Las mutaciones de `mutaciones.md` son reales: al repetirlas, el test anotado falla.

### Niveles

- **Alto (27-30):** todo lo anterior, con alguna diferencia entre lenguajes encontrada y anotada (un test que pasaba en Jest y falla en pytest, o al revés).
- **Medio (21-26):** pasan las comprobaciones, pero la suite de Python cubre menos casos que la de Jest o algún límite no se prueba.
- **Bajo (<21):** no pasan las comprobaciones.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto - Primera suite de tests del dominio](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 04](README.md)
