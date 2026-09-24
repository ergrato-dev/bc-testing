# Rúbrica de Evaluación - Semana 16

## Evidencias y ponderación

| Evidencia | Peso | Descripción |
|---|---:|---|
| Conocimiento | 30% | Comprensión del entorno de pytest y del ciclo de vida de las fixtures |
| Desempeño | 40% | Prácticas guiadas con fixtures `yield`, scopes, `conftest.py` y fixtures integradas |
| Producto | 30% | Suite del dominio organizada con fixtures compartidas y limpieza automática |

---

## 1) Conocimiento (30%)

### Criterios

1. Distingue `failed` de `error` y explica en qué fase (setup, test o teardown) ocurrió cada uno.
2. Explica cuándo se crea y se destruye una fixture según su scope (`function`, `class`, `module`, `package`, `session`) leyendo la salida de `--setup-show`.
3. Justifica cuándo mover una fixture a `conftest.py` y cuándo está justificado `autouse`.
4. Describe qué configura `[tool.pytest]` (`pythonpath`, `testpaths`, `addopts`).

### Niveles

- **Alto (27-30 pts)**: domina el ciclo de vida de las fixtures y justifica cada decisión.
- **Medio (21-26 pts)**: comprensión correcta con ajustes menores.
- **Bajo (0-20 pts)**: confusión de conceptos o aplicación superficial.

---

## 2) Desempeño (40%)

### Criterios

1. Completa los ejercicios guiados descomentando por pasos y ejecuta `uv run pytest --setup-show`.
2. Escribe fixtures con `yield` cuyo teardown deja el estado limpio.
3. Compone fixtures respetando la regla de scopes (sin `ScopeMismatch`).
4. Usa `tmp_path`, `monkeypatch` y `capsys` en lugar de archivos reales, cambios manuales de `os.environ` o `print` sin verificar.

### Niveles

- **Alto (36-40 pts)**: ejecución estable y fixtures claras.
- **Medio (28-35 pts)**: funcional con mejoras de legibilidad.
- **Bajo (0-27 pts)**: pruebas incompletas o sin estructura.

---

## 3) Producto (30%)

### Criterios

1. Adapta el starter al dominio asignado y decide sus propios casos de prueba.
2. Incluye mínimo 8 tests de comportamiento, con al menos 2 de error.
3. Usa `conftest.py` compartido por los dos archivos de test y al menos una fixture con `yield` y teardown.
4. Aísla archivos y entorno con `tmp_path` y `monkeypatch`.

### Niveles

- **Alto (27-30 pts)**: base robusta para evolucionar la etapa Python.
- **Medio (21-26 pts)**: cumple el mínimo con oportunidades de mejora.
- **Bajo (0-20 pts)**: suite incompleta o frágil.

---

## Penalizaciones

- Tests con nombres genéricos (`test1`, `test_ok`, `test_a`): hasta -6 pts.
- Ausencia de AAA en ejercicios clave: hasta -8 pts.
- Setup con recursos (archivos, almacenes abiertos) sin teardown: hasta -6 pts.
- `autouse` o scope amplio sin justificación que acopla tests entre sí: hasta -6 pts.
- Mezclar JS/Java en contenido de semana Python: hasta -8 pts.

---

## Checklist de entrega

- [ ] Prácticas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] `uv run pytest` sin fallos ni errores.
- [ ] `conftest.py` con fixtures documentadas y usadas desde dos archivos.
- [ ] Al menos una fixture con `yield` y teardown.
- [ ] Ningún test escribe fuera de `tmp_path` ni depende de variables de entorno de la máquina.
