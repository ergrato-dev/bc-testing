# Lab 02 - Matrix, caché y artifacts

## Objetivo

Escribir el workflow de Python de `ci-lab` con una matriz de dos versiones, ver cómo el umbral de cobertura lo pone en rojo, guardar el informe de cobertura como artifact y en el resumen del job, y limitar los runs con `paths` y `concurrency`.

## Tiempo estimado

65 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-preparar-cada-lenguaje-caches-y-servicios.md) y la [teoría 03](../../1-teoria/03-matrix-artifacts-paralelismo-y-umbrales.md).
- El lab 01 terminado.

## Punto de partida

```bash
cd ~/ci-lab
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/lab-02-matrix-cache-y-artifacts/starter/python.yml .github/workflows/python.yml
```

[`starter/python.yml`](starter/python.yml) está comentado por PASOS.

---

## Paso 1: la matriz

Descomenta el PASO 1 y sus `(sigue)`. Lo nuevo respecto al lab 01:

- `strategy.matrix.python-version` crea dos jobs, `test (3.13)` y `test (3.14)`.
- `setup-uv` recibe la versión de la matriz en `python-version`. La action la guarda en la variable `UV_PYTHON`, que manda sobre el `.python-version` del proyecto (3.14): sin esa entrada, los dos jobs usarían 3.14.
- `uv sync --locked` falla si `uv.lock` no coincide con `pyproject.toml`, como `--frozen-lockfile` en pnpm.
- `pytest --cov` con tres informes: en la terminal (`term`), `coverage.xml` y la carpeta `htmlcov/`.
- `fail-fast: false`: si un job falla, el otro termina igualmente.

```bash
git add .github/workflows/python.yml
git commit -m "Add Python workflow with a version matrix and coverage"
git push
gh run list -w python --limit 1
```

Espera a que termine (`gh run watch <id> --exit-status`) y mira los jobs:

```bash
gh run view <id>
# X test (3.13) in 10s (ID 135230707342)
# X test (3.14) in 9s (ID 135230707811)
```

## Paso 2: por qué está en rojo

```bash
gh run view <id> --log-failed
```

El step de pytest del job 3.13, recortado:

```text
  UV_PYTHON: 3.13
platform linux -- Python 3.13.18, pytest-9.1.1, pluggy-1.7.0
collected 7 items
tests/test_loans.py .......
Name                  Stmts   Miss Branch BrPart  Cover   Missing
-----------------------------------------------------------------
library/__init__.py       0      0      0      0   100%
library/loans.py         15      3     10      1    76%   21-23
-----------------------------------------------------------------
TOTAL                    15      3     10      1    76%
FAIL Required test coverage of 80.0% not reached. Total coverage: 76.00%
============================== 7 passed in 0.11s ===============================
##[error]Process completed with exit code 1.
```

Los 7 tests pasan y el job falla. El umbral está en `python/pyproject.toml` (`fail_under = 80`), no en el workflow: pytest-cov termina con 1 y el step falla. Es lo mismo que viste en tu equipo antes del lab 01.

El job 3.14 falla igual, con `Python 3.14.10`. Gracias a `fail-fast: false` lo sabes: el fallo no depende de la versión.

## Paso 3: el informe como artifact y en el resumen

Antes de arreglarlo, guarda el informe para poder leerlo. Descomenta el PASO 3: dos steps con `if: always()`, que se ejecutan aunque pytest haya fallado.

- `Coverage summary` escribe la tabla de cobertura en Markdown en `$GITHUB_STEP_SUMMARY`.
- `upload-artifact` sube `coverage.xml` y `htmlcov/`, con un nombre por versión.

```bash
git commit -am "Upload the coverage report"
git push
```

El run sigue en rojo, pero ahora deja el informe. Abre el run en el navegador (`gh run view <id> --web`): debajo del diagrama de jobs está el resumen de cada job con la tabla de cobertura, y al final de la página, los dos artifacts. Descárgalos:

```bash
mkdir -p /tmp/coverage-ci && cd /tmp/coverage-ci
gh run download <id> -R <tu-usuario>/ci-lab
find . -maxdepth 2
# .
# ./coverage-python-3.13
# ./coverage-python-3.14
# ./coverage-python-3.13/coverage.xml
# ./coverage-python-3.13/htmlcov
# ./coverage-python-3.14/coverage.xml
# ./coverage-python-3.14/htmlcov
cd ~/ci-lab
```

`-R` dice de qué repositorio, porque `/tmp/coverage-ci` no es un repositorio de git. Abre `/tmp/coverage-ci/coverage-python-3.13/htmlcov/index.html` en el navegador y entra en `library/loans.py`: las líneas 21 a 23 están en rojo. Son el socio `visitor` y el tipo de socio desconocido de `loan_days`.

**Experimento: el resumen sin `--fail-under=0`.** Quita `--fail-under=0` del step `Coverage summary`, súbelo y mira los steps del job:

```text
X Run uv run pytest --cov --cov-report=term --cov-report=xml --cov-report=html
X Coverage summary
✓ Run actions/upload-artifact@v7
```

El log del resumen termina con `##[error]Process completed with exit code 2.`: `coverage report` también comprueba `fail_under` y falla por la misma causa que pytest. Dos errores para un problema. Vuelve a poner `--fail-under=0`.

## Paso 4: verde

Añade al final de `python/tests/test_loans.py` los dos casos que faltan:

```python


def test_loan_days_for_visitors() -> None:
    assert loan_days("visitor") == 7


def test_loan_days_rejects_unknown_member_type() -> None:
    with pytest.raises(ValueError, match="unknown member type"):
        loan_days("robot")
```

Comprueba en tu equipo y súbelo:

```bash
cd python && uv run pytest --cov -q && cd ..
# TOTAL                    15      0     10      0   100%
# Required test coverage of 80.0% reached. Total coverage: 100.00%
# 9 passed in 0.04s
git commit -am "Test visitor and unknown member loan days"
git push
```

```bash
gh run view <id>
# ✓ test (3.14) in 15s (ID 135232251356)
# ✓ test (3.13) in 12s (ID 135232251746)
```

Busca la caché de uv en el log del job 3.14:

```bash
gh run view <id> --log | grep -E "cache found|cache saved"
```

```text
No GitHub Actions cache found for key: setup-uv-2-x86_64-unknown-linux-gnu-ubuntu-24.04-3.14-c77b5c97...
uv cache saved with key: setup-uv-2-x86_64-unknown-linux-gnu-ubuntu-24.04-3.14-c77b5c97...
```

Es el primer run verde y aun así no encuentra caché: los runs en rojo no la guardaron, porque el step `Post Run astral-sh/setup-uv` solo se ejecuta si el job pasa. La clave incluye `3.14`: cada versión de la matriz tiene la suya.

## Paso 5: `paths` y `concurrency`

Descomenta el PASO 5: los dos bloques `paths` y el bloque `concurrency`.

- `paths`: el workflow solo se ejecuta si el cambio toca `python/` o el propio workflow.
- `concurrency`: un run por workflow y rama; uno nuevo cancela al que está en marcha.

```bash
git commit -am "Run the Python workflow only for Python changes"
git push
```

Prueba la cancelación: haz dos commits que toquen `python/` y súbelos con unos segundos de diferencia, sin esperar al primero. Por ejemplo, añade un comentario a `MAX_RENEWALS` en `python/library/loans.py`, haz commit y *push*; cambia el comentario, commit y *push* otra vez.

```bash
gh run list -w python --limit 3
# *  Document MAX_RENEWALS                      python  main  push  44426386531  7s   ...
# X  Test visitor and unknown member loan days  python  main  push  44426351052  21s  ...
```

```bash
gh run view 44426351052
# X Canceling since a higher priority waiting request for python-refs/heads/main exists
# X The operation was canceled.
```

`python-refs/heads/main` es el grupo: el nombre del workflow y la rama. El run cancelado aparece con `X`, pero su conclusión es `cancelled`, no `failure`.

Por último, comprueba el filtro: haz un commit que solo toque `js/` (un comentario en `js/src/fees.js`) y súbelo. `gh run list --limit 3` muestra un run nuevo de `js` y ninguno de `python`.

---

## Experimento: una etiqueta que no existe

Cambia `astral-sh/setup-uv@v10.3.3` por `astral-sh/setup-uv@v10` y súbelo:

```text
X test (3.13) in 3s (ID 135230105400)
  X Set up job

ANNOTATIONS
X Unable to resolve action `astral-sh/setup-uv@v10`, unable to find version `v10`
```

El job falla en `Set up job`, antes de ejecutar nada: GitHub descarga las actions al preparar el job. `astral-sh/setup-uv` no publica etiquetas mayores, solo versiones completas. Vuelve a `v10.3.3`.

## Comprobación final

```bash
gh run list -w python --limit 1
# ✓ ... python  main  push ...
```

`.github/workflows/python.yml` es el starter descomentado entero, y `test_loans.py` tiene 9 tests.

## Qué has aprendido

- Una matriz crea un job por valor; `${{ matrix.python-version }}` lo lleva a la action. `fail-fast: false` deja ver si el fallo depende de la versión.
- El umbral vive en `pyproject.toml`; el workflow solo ejecuta pytest y el código de salida decide.
- Con `if: always()`, el informe se guarda y se resume también cuando el job falla. `gh run download` lo trae a tu equipo.
- La caché de uv se guarda solo en un job verde, con una clave por versión.
- `paths` evita runs que no tocan nada y `concurrency` cancela los que quedaron viejos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer workflow](../lab-01-primer-workflow/README.md) | [Lab 03 - PostgreSQL como servicio](../lab-03-postgresql-como-servicio/README.md) → |

[↑ Semana 45](../../README.md)
