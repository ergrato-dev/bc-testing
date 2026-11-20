# Lab 01 - Primer proyecto con pytest

## Objetivo

Montar con uv el proyecto donde harás los cuatro labs de la semana, ver un error de recolección y un test en rojo, arreglar los dos y dejar el proyecto en git.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-setup-pytest.md) y la [02](../../1-teoria/02-estructura-test-pytest.md).
- uv instalado (`uv --version` muestra `0.13` o posterior) y git.

## Punto de partida

La carpeta `~/labs-semana-04` es el proyecto de los cuatro labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-04
cd ~/labs-semana-04
cp -r <ruta-del-bootcamp>/bootcamp/week-04-primeros_tests_automatizados_con_python/2-practicas/lab-01-primer-proyecto-pytest/starter/. .
ls -R src tests
# src:
# math_utils.py
#
# tests:
# test_math_utils.py
```

Es el mismo código del [lab 01 de la semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-01-primer-proyecto-jest/README.md), en Python: `add` tiene el mismo bug.

---

## Paso 1: Python 3.14 (5 min)

```bash
uv python install 3.14
```

La primera vez descarga Python (unos 35 MiB). Si ya lo tienes, responde `Python 3.14 is already installed`.

## Paso 2: el proyecto (10 min)

```bash
uv init --bare --python 3.14 --pin-python
# Initialized project `labs-semana-04`
uv add --dev pytest==9.1.1
```

```text
Using CPython 3.14.10
Creating virtual environment at: .venv
Resolved 7 packages in 1.58s
...
 + pytest==9.1.1
```

Comprueba qué ha creado:

```bash
ls -a
# .  ..  .python-version  .venv  pyproject.toml  src  tests  uv.lock
cat .python-version
# 3.14
```

Abre `pyproject.toml` y localiza `requires-python` y el grupo `dev` con `pytest==9.1.1`. La [teoría 01](../../1-teoria/01-setup-pytest.md#3-el-proyecto) explica cada línea.

## Paso 3: un error de recolección (10 min)

Ejecuta los tests antes de configurar pytest:

```bash
uv run pytest
```

```text
collected 0 items / 1 error

==================================== ERRORS ====================================
__________________ ERROR collecting tests/test_math_utils.py ___________________
...
tests/test_math_utils.py:1: in <module>
    from src.math_utils import add, is_even
E   ModuleNotFoundError: No module named 'src'
=========================== short test summary info ============================
ERROR tests/test_math_utils.py
!!!!!!!!!!!!!!!!!!!! Interrupted: 1 error during collection !!!!!!!!!!!!!!!!!!!!
=============================== 1 error in 0.17s ===============================
```

Es un `ERROR`, no un `FAILED`: pytest no pudo importar el archivo de test y no ejecutó nada. Python no sabe que `src` es una carpeta del proyecto. Añade al final de `pyproject.toml`:

```toml

[tool.pytest]
pythonpath = ["."]
testpaths = ["tests"]
```

## Paso 4: rojo (10 min)

```bash
uv run pytest
```

```text
tests/test_math_utils.py F                                               [100%]

=================================== FAILURES ===================================
______________________ test_returns_5_when_adding_2_and_3 ______________________
...
>       assert result == 5
E       assert -1 == 5

tests/test_math_utils.py:15: AssertionError
```

Ahora sí se ejecuta el test, y falla. Lee el informe completo como en la [teoría 02](../../1-teoria/02-estructura-test-pytest.md#4-leer-un-test-que-falla) y contesta en voz alta o en una nota:

- ¿Qué valor dio el código y cuál esperaba el test?
- ¿En qué línea está el `assert` que falla?
- ¿Por qué este informe dice `FAILED` y el del paso 3 decía `ERROR`?

## Paso 5: verde (5 min)

Abre `src/math_utils.py` y sigue el comentario `PASO 1`: borra `return a - b` y descomenta `return a + b` quitando el `# `. En Python la sangría cuenta: `return` tiene que quedar con cuatro espacios delante, alineado con los comentarios. Ejecuta `uv run pytest`: el test pasa.

## Paso 6: dos tests más (10 min)

En `tests/test_math_utils.py`, descomenta los bloques `PASO 2` y `PASO 3`: quita el `# ` del principio de cada línea del test, desde `def` hasta el `assert`. En VS Code, selecciona las líneas y pulsa `Ctrl+/` (`Cmd+/` en macOS). La línea `def` queda pegada al margen y el resto con cuatro espacios.

Antes de ejecutar, escribe qué esperas que salga. Después:

```bash
uv run pytest -v
```

```text
collecting ... collected 3 items

tests/test_math_utils.py::test_returns_5_when_adding_2_and_3 PASSED      [ 33%]
tests/test_math_utils.py::test_returns_true_when_number_is_even PASSED   [ 66%]
tests/test_math_utils.py::test_returns_false_when_number_is_odd PASSED   [100%]

============================== 3 passed in 0.02s ===============================
```

Los tests de `is_even` usan `assert result` y `assert not result`. La [teoría 03](../../1-teoria/03-assertions-y-ejecucion.md#assert-valor-a-secas) explica por qué `assert result is True` comprueba más; cámbialos y vuelve a ejecutar.

## Paso 7: códigos de salida y git (10 min)

Pide a pytest unos tests que no existen y mira cómo termina:

```bash
uv run pytest -k nada
echo $?
# ============================ 3 deselected in 0.03s =============================
# 5
```

Repite `echo $?` después de `uv run pytest` (todo en verde) y después de volver a poner `return a - b` un momento en `src/math_utils.py`. Anota el código de cada caso y quita el bug otra vez.

Convierte la carpeta en un repositorio git. `.venv/` y `.pytest_cache/` ya traen su propio `.gitignore`; solo falta `__pycache__/`, la carpeta donde Python guarda el código ya compilado.

```bash
git init
echo "__pycache__/" > .gitignore
git add .
git status --short
# A  .gitignore
# A  .python-version
# A  pyproject.toml
# A  src/math_utils.py
# A  tests/test_math_utils.py
# A  uv.lock
git commit -m "lab 01: primer proyecto con pytest"
```

`uv.lock` entra en git; `.venv` no. Si `git commit` responde `Please tell me who you are`, configura tu nombre y correo como en el [lab 01 de la semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-01-primer-proyecto-jest/README.md#paso-7-git-y-modo-watch-10-min).

El lab 04 usará git para deshacer cambios en el código.

---

## Comprobación final

```bash
uv run pytest 2>&1 | tail -1
# ============================== 3 passed in 0.02s ===============================
git log --oneline | wc -l
# 1
```

Repasa:

- [ ] `.python-version` dice `3.14` y `pyproject.toml` tiene `pytest==9.1.1` en el grupo `dev`.
- [ ] `[tool.pytest]` tiene `pythonpath` y `testpaths`.
- [ ] Sabes decir, viendo un informe, si es un `FAILED` o un `ERROR`, y qué código de salida dará.

## Qué has aprendido

- Un proyecto de tests en Python son tres archivos de configuración (`pyproject.toml`, `.python-version` y `uv.lock`) más `src/` y `tests/`.
- `ERROR` significa que el test ni siquiera se ejecutó; `FAILED`, que se ejecutó y algo no se cumplió.
- pytest no tiene modo watch; el código de salida es lo que mira cualquier script que lo ejecute.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - AAA y asserts](../lab-02-aaa-y-asserts/README.md) → |

[↑ Semana 04](../../README.md)
