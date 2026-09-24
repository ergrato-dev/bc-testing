# Glosario Semana 16 - Fixtures y entorno de pytest

## A

- **addopts**: opción de `[tool.pytest]` con flags que se agregan siempre a la línea de comandos (por ejemplo, `-ra`).
- **autouse**: fixture declarada con `autouse=True` que se aplica a todos los tests de su alcance sin que la pidan.

## C

- **capsys**: fixture integrada que captura lo impreso en `stdout` y `stderr` durante un test.
- **conftest.py**: archivo que pytest carga automáticamente; sus fixtures están disponibles en su carpeta y subcarpetas sin importarlas.

## E

- **error (resultado)**: el test no pudo prepararse o limpiarse porque falló el setup o el teardown de una fixture. Distinto de `failed`.

## F

- **failed**: el cuerpo del test falló (assert no cumplido o excepción inesperada dentro del test).
- **fixture**: función decorada con `@pytest.fixture` que prepara (y opcionalmente limpia) datos o recursos para los tests.

## M

- **monkeypatch**: fixture integrada que cambia variables de entorno o atributos durante un test y los restaura al terminar.

## S

- **scope**: vida de una fixture: `function`, `class`, `module`, `package` o `session`.
- **ScopeMismatch**: error que aparece cuando una fixture pide otra de scope más estrecho.
- **--setup-show**: flag que muestra el orden real de SETUP y TEARDOWN de cada fixture.

## T

- **teardown**: código de limpieza; en una fixture con `yield`, lo que va después del `yield`.
- **testpaths**: opción de `[tool.pytest]` que indica dónde buscar tests.
- **tmp_path**: fixture integrada que entrega un directorio temporal nuevo (`pathlib.Path`) para cada test.

## X

- **xfailed / xpassed**: test marcado como fallo esperado que falla (`xfailed`) o que inesperadamente pasa (`xpassed`).

## Y

- **yield fixture**: fixture que entrega su valor con `yield`; el código posterior se ejecuta como teardown.
