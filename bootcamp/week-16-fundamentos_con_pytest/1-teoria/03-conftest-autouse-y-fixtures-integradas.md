# 03 - conftest.py, autouse y fixtures integradas

## Objetivo

Compartir fixtures entre archivos con `conftest.py`, decidir cuándo una fixture `autouse` está justificada y aprovechar las fixtures que pytest ya trae: `tmp_path`, `monkeypatch` y `capsys`.

---

## Lenguaje de esta semana

**Aplica a**: Python.

---

## Refactor: de setup duplicado a fixture compartida

Antes (mismo diccionario copiado en cada test):

```python
def test_visitor_name_is_kept_when_visitor_is_created():
    visitor = {"id": "v-1", "name": "Ada"}
    assert visitor["name"] == "Ada"


def test_visitor_id_is_kept_when_visitor_is_created():
    visitor = {"id": "v-1", "name": "Ada"}
    assert visitor["id"] == "v-1"
```

Después (una fixture reutilizable):

```python
import pytest


@pytest.fixture
def visitor():
    return {"id": "v-1", "name": "Ada"}


def test_visitor_name_is_kept_when_visitor_is_created(visitor):
    assert visitor["name"] == "Ada"


def test_visitor_id_is_kept_when_visitor_is_created(visitor):
    assert visitor["id"] == "v-1"
```

Si esa fixture la necesitan **varios archivos**, el siguiente paso es moverla a `conftest.py`.

---

## `conftest.py` en la práctica

`conftest.py` es un archivo que pytest carga automáticamente. Sus fixtures están disponibles para todos los tests de su carpeta y subcarpetas, **sin importarlas**.

```text
proyecto/
|-- pyproject.toml
|-- aquarium.py
`-- tests/
    |-- conftest.py        # fixture tank_names
    |-- test_files.py      # usa tank_names
    `-- test_tanks.py      # usa tank_names
```

`tests/conftest.py`:

```python
import pytest


@pytest.fixture
def tank_names():
    """Nombres de los tanques del Acuario usados en varios archivos de test."""
    return ["Arrecife", "Medusas", "Tiburones"]
```

`tests/test_tanks.py` (no hay `import` de la fixture):

```python
def test_tank_names_start_with_reef_when_fixture_is_shared(tank_names):
    # Assert
    assert tank_names[0] == "Arrecife"
```

Reglas prácticas:

- Nunca importes un `conftest.py` desde un test: pytest lo resuelve solo.
- Puede haber un `conftest.py` por carpeta; el más cercano al test tiene prioridad si dos definen la misma fixture.
- Mueve a `conftest.py` solo las fixtures que usan **dos o más archivos**; las de un solo archivo se quedan en ese archivo, junto al test que las usa.
- Documenta cada fixture compartida con un docstring: es lo que muestra `uv run pytest --fixtures`.

---

## `autouse`: fixtures que se aplican solas

Con `autouse=True` la fixture se ejecuta en todos los tests de su alcance (archivo, clase o `conftest.py`) aunque ningún test la pida:

```python
import pytest


@pytest.fixture(autouse=True)
def clean_museum_env(monkeypatch):
    """Elimina MUSEUM_NAME para que ningún test dependa del entorno de la máquina."""
    monkeypatch.delenv("MUSEUM_NAME", raising=False)
```

Úsala con criterio:

| Justificado | No justificado |
|---|---|
| Aislar la suite de algo externo que afecta a todos los tests (variables de entorno, estado global, semilla aleatoria). | Preparar datos que solo usan algunos tests. |
| La fixture no entrega ningún valor que el test tenga que leer. | El test depende del valor que entrega: pídela como parámetro para que se vea. |

Una `autouse` oculta setup: si un lector no puede adivinar su existencia leyendo el test, que sea solo porque protege a **todos** por igual. `--setup-show` la muestra en cada test; úsalo cuando dudes.

---

## `tmp_path`: archivos sin ensuciar el proyecto

`tmp_path` entrega un `pathlib.Path` a un directorio nuevo y vacío para cada test. pytest lo crea fuera del proyecto y conserva solo los de las últimas ejecuciones.

```python
from aquarium import save_tanks


def test_save_tanks_writes_one_line_per_tank_when_list_is_valid(tank_names, tmp_path):
    # Act
    path = save_tanks(tank_names, tmp_path)

    # Assert
    assert path.read_text(encoding="utf-8").splitlines() == tank_names
```

Con `tmp_path` no necesitas teardown propio para borrar archivos, y dos tests nunca comparten directorio.

---

## `monkeypatch`: variables de entorno y atributos

`monkeypatch` cambia algo durante un test y lo **restaura automáticamente** al terminar (es una fixture con teardown incorporado).

| Método | Uso |
|---|---|
| `monkeypatch.setenv("NAME", "value")` | Define una variable de entorno. |
| `monkeypatch.delenv("NAME", raising=False)` | Elimina una variable; `raising=False` evita error si no existía. |
| `monkeypatch.setattr(module, "name", value)` | Reemplaza un atributo (función, constante) de un módulo u objeto. |

Código bajo prueba (`aquarium.py`, extracto):

```python
import os


def max_visitors() -> int:
    return int(os.environ.get("MAX_VISITORS", "50"))
```

Tests:

```python
import aquarium
from aquarium import max_visitors


def test_max_visitors_returns_env_value_when_variable_is_set(monkeypatch):
    # Arrange
    monkeypatch.setenv("MAX_VISITORS", "120")

    # Act
    result = max_visitors()

    # Assert
    assert result == 120


def test_max_visitors_returns_default_when_variable_is_missing(monkeypatch):
    # Arrange
    monkeypatch.delenv("MAX_VISITORS", raising=False)

    # Act
    result = max_visitors()

    # Assert
    assert result == 50


def test_tank_report_path_is_replaced_when_attribute_is_patched(monkeypatch, tmp_path):
    # Arrange
    fake_path = tmp_path / "custom.txt"
    monkeypatch.setattr(aquarium, "tank_report_path", lambda directory: fake_path)

    # Act
    path = aquarium.save_tanks(["Arrecife"], tmp_path)

    # Assert
    assert path == fake_path
```

`setattr` reemplaza el atributo **donde se busca en tiempo de ejecución**: aquí `save_tanks` llama a `tank_report_path` del módulo `aquarium`, por eso se parchea `aquarium`. El reemplazo de dependencias con dobles de prueba se profundiza en la semana 18 con `unittest.mock` y `pytest-mock`.

---

## `capsys`: capturar lo que se imprime (opcional)

```python
from aquarium import greet


def test_greet_prints_visitor_name_when_called(capsys):
    # Act
    greet("Ada")

    # Assert
    assert capsys.readouterr().out == "Bienvenida al Acuario, Ada\n"
```

`capsys.readouterr()` devuelve lo escrito en `out` y `err` desde el inicio del test (o desde la lectura anterior).

---

## Checklist

- [ ] Las fixtures compartidas por varios archivos viven en `conftest.py` y tienen docstring.
- [ ] Cada `autouse` protege a toda la suite y no entrega valores que el test lea.
- [ ] Ningún test escribe archivos fuera de `tmp_path`.
- [ ] Variables de entorno y atributos se cambian con `monkeypatch`, nunca a mano con `os.environ[...] = ...`.

---

← [Fixtures a fondo: yield, scopes y composición](./02-fixtures-yield-scopes-y-composicion.md) | [Volver al README](../README.md)
