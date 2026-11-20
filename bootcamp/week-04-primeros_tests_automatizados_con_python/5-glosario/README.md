# Glosario - Semana 04

Los términos comunes con la semana 3 (AAA, aserción, mutación, rojo y verde) están en su [glosario](../../week-03-primeros_tests_automatizados_con_javascript/5-glosario/README.md).

| Término | Definición |
|---|---|
| `assert` | Palabra de Python que lanza `AssertionError` si la condición es falsa. pytest la usa como única comprobación |
| `AssertionError` | Error que lanza un `assert` falso; pytest lo muestra como `FAILED` con los valores reales |
| Anotación de tipo | `a: int` o `-> None`: documenta qué recibe y devuelve una función. Python no la comprueba al ejecutar |
| Clase `Test*` | Clase sin `__init__` cuyo nombre empieza por `Test`; agrupa tests como `describe` en Jest |
| Código de salida | Número con el que termina pytest: 0 todo pasa, 1 algún fallo, 2 interrumpido, 5 sin tests |
| Diccionario (`dict`) | Pares clave-valor entre llaves, `{"name": "Ana"}`; el objeto de JavaScript |
| Entorno virtual (`.venv`) | Carpeta con el Python y las librerías de un proyecto, separada del resto del equipo. No se sube a git |
| `ERROR` | Estado de un test que no llegó a ejecutarse: import roto o error de sintaxis al recolectar |
| Expresión regular | Patrón de texto con caracteres especiales (`(`, `.`, `+`); `match` de `pytest.raises` lo es |
| `FAILED` | Estado de un test que se ejecutó y falló: un `assert` falso o una excepción del código |
| `float` | Tipo de los números decimales en Python. Se guardan en binario y algunos no son exactos |
| `int` | Tipo de los números enteros en Python |
| `is` | Compara si dos valores son el mismo objeto. Solo para `None`, `True` y `False` |
| `isinstance(x, tipo)` | Devuelve `True` si `x` es de ese tipo; `isinstance(10.5, int)` es `False` |
| `-k` | Opción de pytest que ejecuta los tests cuyo nombre contiene un texto |
| `--lf` | Opción de pytest que ejecuta solo los tests que fallaron la última vez |
| Node id | Ruta completa de un test, `tests/test_x.py::TestA::test_b`; sirve para ejecutarlo solo |
| `None` | El «no hay valor» de Python, como `null` en JavaScript |
| `.pytest_cache/` | Carpeta donde pytest guarda qué tests fallaron; la usa `--lf` |
| `__pycache__/` | Carpeta donde Python guarda el código ya compilado. Se ignora en git |
| `pyproject.toml` | Ficha del proyecto: nombre, versión de Python, dependencias y configuración de pytest |
| pytest | Framework de tests de Python del curso |
| `pytest.approx` | Valor que acepta una diferencia relativa de una millonésima al compararlo con `==` |
| `pytest.raises` | Bloque `with` que comprueba que el código de dentro lanza un error de un tipo |
| `.python-version` | Archivo con la versión de Python que usa uv en el proyecto |
| `pythonpath` | Ajuste de `[tool.pytest]` que añade carpetas a las rutas de importación |
| Recolección | Fase en que pytest busca e importa los tests antes de ejecutarlos |
| `snake_case` | Nombres en minúsculas con guiones bajos: `test_returns_5_when_adding_2_and_3` |
| `testpaths` | Ajuste de `[tool.pytest]` que dice en qué carpetas buscar tests |
| Tupla | Lista fija de valores entre paréntesis con comas; una tupla no vacía es siempre verdadera |
| `unittest` | Framework de tests incluido en Python, al estilo JUnit; pytest ejecuta sus tests |
| uv | Gestor de Python del curso: instala Python, crea `.venv` e instala dependencias |
| `uv.lock` | Versiones exactas de todas las dependencias. Se sube a git |
| `uv run` | Ejecuta un comando con el Python y las librerías de `.venv`, sin activarlo |
| `ValueError` | Tipo de error de Python para «el valor recibido no vale» |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 05 — Primeros Tests Automatizados con Java](../../week-05-primeros_tests_automatizados_con_java/README.md) → |

[↑ Semana 04](../README.md)
