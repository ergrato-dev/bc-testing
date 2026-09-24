# Semana 16 - Python Testing I: Fundamentos con pytest

> **Etapa 2 - Testing con Python** | Semana 16 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Distinguir los resultados de pytest (`passed`, `failed`, `error`, `skipped`, `xfailed`, `xpassed`) y diagnosticar un `error` de setup o teardown.
2. Configurar pytest en `[tool.pytest]` de `pyproject.toml` e inspeccionar fixtures con `--setup-show` y `--fixtures`.
3. Escribir fixtures con `yield` que limpian el estado automáticamente al terminar cada test.
4. Elegir el scope de una fixture (`function`, `class`, `module`, `package`, `session`) y componer fixtures entre sí.
5. Compartir fixtures con `conftest.py`, usar `autouse` con criterio y aprovechar `tmp_path`, `monkeypatch` y `capsys`.

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso de S04, entorno de pytest, fixtures a fondo, `conftest.py` y fixtures integradas | 2.5 h |
| Prácticas | Fixtures con `yield` y scopes + `conftest.py`, `autouse`, `tmp_path` y `monkeypatch` | 3 h |
| Proyecto | Suite del dominio organizada con fixtures | 2 h |
| Recursos y cierre | Refuerzo con documentación oficial y checklist | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Repaso de la semana 04 y entorno de pytest](./1-teoria/01-repaso-y-entorno-pytest.md)
2. [Fixtures a fondo: yield, scopes y composición](./1-teoria/02-fixtures-yield-scopes-y-composicion.md)
3. [conftest.py, autouse y fixtures integradas](./1-teoria/03-conftest-autouse-y-fixtures-integradas.md)

### Prácticas

- [Ejercicio 01 - Fixtures con yield, composición y scopes](./2-practicas/ejercicio-01-fixtures-yield-y-scopes/README.md)
- [Ejercicio 02 - conftest.py, autouse y fixtures integradas](./2-practicas/ejercicio-02-conftest-y-fixtures-integradas/README.md)

### Proyecto

- [Proyecto semanal: Suite Python con fixtures para el dominio asignado](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-16-fundamentos_con_pytest/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-pytest-execution-flow.svg
|   |-- 02-aaa-python-assertions-map.svg
|   `-- 03-fixture-scope-and-reuse.svg
|-- 1-teoria/
|   |-- 01-repaso-y-entorno-pytest.md
|   |-- 02-fixtures-yield-scopes-y-composicion.md
|   `-- 03-conftest-autouse-y-fixtures-integradas.md
|-- 2-practicas/
|   |-- ejercicio-01-fixtures-yield-y-scopes/
|   `-- ejercicio-02-conftest-y-fixtures-integradas/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Nota Importante

La semana 04 ya cubrió la instalación con `uv`, la estructura AAA, los nombres de test, `pytest.raises` y los filtros `-k`/`-x`: aquí solo se repasan en unos minutos. El foco de esta semana son las **fixtures y el entorno de pytest**, la base sobre la que se apoyan la parametrización (S17) y el mocking con limpieza automática (S18).

Requisitos: Python 3.14 y `uv`. En cada carpeta `starter/` o `solution/`: `uv sync` y `uv run pytest`.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 15 - Integración de estrategias y calidad continua](../week-15-integracion_de_estrategias_y_calidad_continua/README.md) | [Semana 17 - pytest avanzado: parametrización y marks](../week-17-parametrizacion_y_marks_con_pytest/README.md) |
