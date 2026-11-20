# 01 - Python, uv y pytest

> Lenguaje: Python.

En la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/01-setup-jest.md) montaste un proyecto de tests con Node.js, pnpm y Jest. Esta semana montas el mismo proyecto en Python, con tres piezas equivalentes: **Python** ejecuta el código, **uv** instala Python y las librerías, y **pytest** ejecuta los tests.

---

## 🎯 Objetivos

- Instalar uv y, con él, Python 3.14.
- Crear un proyecto con `uv init`, instalar pytest y saber qué hace cada archivo que aparece.
- Configurar pytest en `pyproject.toml` para que los tests encuentren el código de `src/`.
- Ejecutar el primer test con `uv run pytest` y leer su código de salida.

---

## 1. De JavaScript a Python

Cada pieza de la semana 3 tiene su pareja:

| Semana 3 (JavaScript) | Semana 4 (Python) | Para qué |
|---|---|---|
| Node.js 22 | Python 3.14 | Ejecutar el código |
| pnpm (con corepack) | uv | Instalar las dependencias del proyecto |
| `package.json` | `pyproject.toml` | Nombre, versión de Python y dependencias |
| `pnpm-lock.yaml` | `uv.lock` | Versiones exactas de todo lo instalado |
| `node_modules/` | `.venv/` | Dónde quedan instaladas las librerías |
| Jest 30.5.2 | pytest 9.1.1 | Ejecutar los tests |
| `pnpm test` | `uv run pytest` | Lanzar la suite |

---

## 2. uv y Python

### uv

[uv](https://docs.astral.sh/uv/) es el único gestor de Python del curso: instala Python, crea el entorno del proyecto y sus dependencias. Sigue la [guía de instalación oficial](https://docs.astral.sh/uv/getting-started/installation/) para tu sistema (en Windows, dentro de WSL). Después abre una terminal nueva y comprueba:

```bash
uv --version
# uv 0.13.7 (x86_64-unknown-linux-gnu)
```

Cualquier versión 0.13 o posterior sirve.

### Python 3.14

No hace falta instalar Python por separado: uv lo descarga.

```bash
uv python install 3.14
```

```text
Downloading cpython-3.14.10-linux-x86_64-gnu (download) (35.1MiB)
 Downloaded cpython-3.14.10-linux-x86_64-gnu (download)
Installed Python 3.14.10 in 4.65s
 + cpython-3.14.10-linux-x86_64-gnu (python3.14)
warning: `/root/.local/bin` is not on your PATH. ...
```

El `warning` habla del comando `python3.14` suelto en la terminal. No lo necesitas: en el curso, Python siempre se ejecuta a través de uv (`uv run`), que encuentra la versión instalada.

---

## 3. El proyecto

### Crear el proyecto

```bash
mkdir ~/mi-proyecto-python
cd ~/mi-proyecto-python
uv init --bare --python 3.14 --pin-python
# Initialized project `mi-proyecto-python`
```

| Opción | Qué hace | Para qué |
|---|---|---|
| `--bare` | Crea solo `pyproject.toml`, sin archivos de ejemplo | Partir de una carpeta limpia |
| `--python 3.14` | Escribe `requires-python = ">=3.14"` | Que el proyecto pida la versión del curso |
| `--pin-python` | Crea `.python-version` con `3.14` | Que uv use 3.14 aunque tengas otra más nueva |

Sin `--python 3.14`, uv usa la versión más nueva que encuentre en el equipo. Con un Python 3.15 instalado, `uv init --bare` escribe `requires-python = ">=3.15"`, y el proyecto deja de coincidir con el de tus compañeros.

### `pyproject.toml`

```toml
[project]
name = "mi-proyecto-python"
version = "0.1.0"
requires-python = ">=3.14"
dependencies = []
```

- **Qué:** la ficha del proyecto, como `package.json`.
- **Para qué:** `requires-python` dice con qué Python funciona; `dependencies` lista lo que necesita el código para funcionar (aquí, nada).
- **Impacto:** uv lee este archivo en cada `uv add`, `uv sync` y `uv run`.

### Instalar pytest

```bash
uv add --dev pytest==9.1.1
```

```text
Using CPython 3.14.10
Creating virtual environment at: .venv
Resolved 7 packages in 1.58s
Prepared 5 packages in 1.63s
Installed 5 packages in 82ms
 + iniconfig==2.3.1
 + packaging==26.3
 + pluggy==1.7.0
 + pygments==2.21.0
 + pytest==9.1.1
```

`--dev` hace lo mismo que `-D` en pnpm: pytest solo hace falta para probar el código, no para ejecutarlo. `==9.1.1` fija la versión exacta. uv añade esto a `pyproject.toml`:

```toml
[dependency-groups]
dev = [
    "pytest==9.1.1",
]
```

Y crea dos cosas más:

- **`.venv/`**, el **entorno virtual**: una carpeta con su propio Python y sus librerías, separada del resto del equipo. Es el `node_modules` de Python: no se sube a git y se regenera.
- **`uv.lock`**: las versiones exactas de las 5 librerías instaladas. Se sube a git. Quien clone el proyecto lo reproduce con:

```bash
uv sync --locked
```

`--locked` falla si `uv.lock` no coincide con `pyproject.toml`, en lugar de actualizarlo: lo mismo que `--frozen-lockfile` en pnpm.

No hay paso equivalente a `pnpm-workspace.yaml`: pytest no ejecuta scripts al instalarse.

### Configurar pytest

Añade al final de `pyproject.toml`:

```toml
[tool.pytest]
pythonpath = ["."]
testpaths = ["tests"]
```

| Línea | Qué hace | Impacto si falta |
|---|---|---|
| `pythonpath = ["."]` | Añade la carpeta del proyecto a las rutas donde Python busca módulos | `from src.calculator import add` falla con `ModuleNotFoundError: No module named 'src'` |
| `testpaths = ["tests"]` | pytest busca tests solo en `tests/` | pytest recorre todo el proyecto: tarda más y puede recoger archivos que no son tests |

`[tool.pytest]` es la forma de pytest 9. En proyectos antiguos verás `[tool.pytest.ini_options]` o un archivo `pytest.ini`: usa una sola. Si hay un `pytest.ini`, pytest ignora `pyproject.toml`.

---

## 4. El primer test

### Estructura

```text
mi-proyecto-python/
|-- .python-version
|-- pyproject.toml
|-- uv.lock
|-- src/
|   `-- calculator.py
`-- tests/
    `-- test_calculator.py
```

### El módulo

`src/calculator.py`:

```python
def add(a: int, b: int) -> int:
    return a + b


def divide(a: float, b: float) -> float:
    if b == 0:
        raise ValueError("Division by zero")
    return a / b
```

`a: int` y `-> int` son **anotaciones de tipo**: documentan qué recibe y devuelve la función. Python no las comprueba al ejecutar. `raise` lanza un error, como `throw` en JavaScript; `ValueError` es el tipo de error de Python para «el valor recibido no vale».

En Python no hay `module.exports`: todo lo que defines en un archivo se puede importar.

### El test

`tests/test_calculator.py`:

```python
import pytest

from src.calculator import add, divide


def test_returns_5_when_adding_2_and_3() -> None:
    # Arrange
    a = 2
    b = 3

    # Act
    result = add(a, b)

    # Assert
    assert result == 5


def test_raises_value_error_when_divisor_is_zero() -> None:
    # Arrange
    dividend = 10
    divisor = 0

    # Act + Assert
    with pytest.raises(ValueError, match="Division by zero"):
        divide(dividend, divisor)
```

Un test de pytest es una función cuyo nombre empieza por `test_`. No hay `describe`, `it` ni `expect`: la comprobación es la palabra `assert` de Python. La [teoría 02](./02-estructura-test-pytest.md) lo cuenta pieza a pieza.

### Ejecutar

```bash
uv run pytest -v
```

```text
============================= test session starts ==============================
platform linux -- Python 3.14.10, pytest-9.1.1, pluggy-1.7.0 -- /root/demo/.venv/bin/python
cachedir: .pytest_cache
rootdir: /root/demo
configfile: pyproject.toml
testpaths: tests
collecting ... collected 2 items

tests/test_calculator.py::test_returns_5_when_adding_2_and_3 PASSED      [ 50%]
tests/test_calculator.py::test_raises_value_error_when_divisor_is_zero PASSED [100%]

============================== 2 passed in 0.01s ===============================
```

- `uv run` ejecuta `pytest` con el Python y las librerías de `.venv`. No hace falta «activar» el entorno.
- `-v` (*verbose*) escribe una línea por test; sin `-v` sale un punto por test.
- `configfile: pyproject.toml` y `testpaths: tests` confirman que pytest lee tu configuración.
- `collected 2 items`: pytest **recolecta** (busca e importa) los tests antes de ejecutarlos.

`.pytest_cache/` aparece tras la primera ejecución: pytest guarda ahí qué tests fallaron (la [teoría 03](./03-assertions-y-ejecucion.md#4-ejecutar-parte-de-la-suite) lo usa). Trae su propio `.gitignore`, así que git no lo ve.

---

## 5. Dónde busca pytest y cómo termina

pytest recolecta:

- Archivos `test_*.py` (o `*_test.py`) dentro de `testpaths`.
- En ellos, las funciones `test_*` y los métodos `test_*` de las clases `Test*`.

Un archivo `calculator_tests.py` o una función `check_add` no se ejecutan, sin aviso.

Además del informe, pytest devuelve un **código de salida**, el número que leen los scripts y los sistemas de integración continua:

| Código | Significa | Ejemplo |
|---|---|---|
| 0 | Todos los tests pasaron | `2 passed` |
| 1 | Algún test falló | `1 failed` |
| 2 | Ejecución interrumpida: error al recolectar | `1 error during collection` |
| 5 | No se recolectó ningún test | `no tests ran` |

```bash
uv run pytest -k nada
echo $?
```

```text
============================ 2 deselected in 0.01s =============================
5
```

`-k nada` pide los tests cuyo nombre contiene «nada»: ninguno. `echo $?` muestra el código de salida del último comando. El 5 importa: un proyecto sin tests no «pasa», igual que Jest con `No tests found`.

---

## 6. Errores frecuentes

| Error | Causa | Solución |
|---|---|---|
| `ModuleNotFoundError: No module named 'src'` | Falta `pythonpath = ["."]` en `[tool.pytest]` | Añadirlo a `pyproject.toml` |
| `pytest: command not found` | Has escrito `pytest` sin `uv run` | `uv run pytest` |
| `no tests ran` (código 5) | Archivo o función sin el prefijo `test_` | Renombrar a `test_*.py` y `def test_*` |
| `requires-python = ">=3.15"` en un proyecto nuevo | `uv init` sin `--python 3.14` | Corregir `requires-python` y crear `.python-version` con `3.14` |
| `` error: The lockfile at `uv.lock` needs to be updated, but `--locked` was provided. `` | `pyproject.toml` cambió y `uv.lock` no | `uv lock` y subir el nuevo `uv.lock` |

---

## ✅ Resumen

- uv instala Python 3.14 y las dependencias; `uv run` ejecuta dentro de `.venv` sin activarlo.
- `uv init --bare --python 3.14 --pin-python` crea `pyproject.toml` y `.python-version`; `uv add --dev pytest==9.1.1` instala pytest y escribe `uv.lock`.
- `[tool.pytest]` con `pythonpath = ["."]` y `testpaths = ["tests"]` deja que los tests importen `src`.
- pytest ejecuta las funciones `test_*` de los archivos `test_*.py`, y termina con 0, 1, 2 o 5.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 04 - Primeros Tests Automatizados con Python](../README.md) | [Teoría — 02 - Estructura de un test en pytest](02-estructura-test-pytest.md) → |

[↑ Semana 04](../README.md)
