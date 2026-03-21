# Setup de Entorno Python + pytest

> **Semana 04 — Teoría 01** | Lenguaje: Python

---

## Objetivo

Configurar un entorno mínimo para ejecutar tests unitarios con `pytest` en Python 3.12+.

---

## Requisitos

- Python 3.12 o superior
- Entorno virtual (`venv` recomendado)
- `pytest` instalado en el entorno

Comprobación rápida:

```bash
python --version
```

---

## Estructura recomendada

```text
mi-proyecto-python/
├── src/
│   └── calculator.py
├── tests/
│   └── test_calculator.py
├── pyproject.toml
└── README.md
```

---

## Configuración inicial

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -U pip
python -m pip install pytest
```

Crear `pyproject.toml` mínimo:

```toml
[tool.pytest.ini_options]
testpaths = ["tests"]
python_files = ["test_*.py"]
addopts = "-q"
```

---

## Primer módulo de ejemplo

`src/calculator.py`

```python
def add(a: int, b: int) -> int:
    return a + b


def divide(a: float, b: float) -> float:
    if b == 0:
        raise ValueError("Division by zero")
    return a / b
```

---

## Primer test en pytest

`tests/test_calculator.py`

```python
from src.calculator import add, divide
import pytest


def test_add_returns_five_when_inputs_are_two_and_three() -> None:
    # Arrange
    a = 2
    b = 3

    # Act
    result = add(a, b)

    # Assert
    assert result == 5


def test_divide_raises_error_when_dividing_by_zero() -> None:
    with pytest.raises(ValueError, match="Division by zero"):
        divide(10, 0)
```

---

## Ejecutar tests

```bash
pytest
pytest -v
pytest -k divide
```

- `pytest`: ejecución estándar
- `pytest -v`: muestra nombres completos
- `pytest -k`: filtra por nombre de test

---

## Errores comunes de setup

| Error | Causa probable | Solución |
|---|---|---|
| `ModuleNotFoundError` | Import o estructura de carpetas incorrecta | Revisar `src/` e imports |
| `pytest: command not found` | Entorno virtual no activo | Activar `.venv` |
| No se detectan tests | Nombre de archivo/función no cumple convención | Usar `test_*.py` y `def test_*` |

---

## Buenas prácticas base

- Aislar entorno virtual por proyecto
- Mantener tests en carpeta `tests/`
- Empezar con funciones puras
- Nombrar tests con intención de negocio

---

## Próximo tema

→ [Estructura de un test en pytest y patrón AAA](./02-estructura-test-pytest.md)
