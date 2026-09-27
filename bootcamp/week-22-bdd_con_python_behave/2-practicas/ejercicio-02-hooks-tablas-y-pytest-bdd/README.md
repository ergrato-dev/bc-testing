# Ejercicio 02 - Hooks, Tablas, Tags y pytest-bdd

## Objetivo

Usar una tabla de datos en `Antecedentes`, descubrir por qué el alcance de un hook importa (un estado compartido rompe un escenario), filtrar escenarios por tags y ejecutar **el mismo** feature con pytest-bdd para comparar las dos herramientas.

## Tiempo estimado

90 minutos.

## Preparación

```bash
cd starter
uv sync
```

- `starter/workshops.py`: inscripción a talleres con cupos y lista de espera.
- `starter/features/inscripcion.feature`: `Antecedentes` con una tabla y dos escenarios; el primero tiene el tag `@smoke`.
- `starter/features/environment.py`: hooks de Behave.
- `starter/features/steps/inscripcion_steps.py`: steps comentados (PASO 1 a 3).
- `starter/tests/test_inscripcion_bdd.py`: versión pytest-bdd comentada (PASO 4).

## Paso a paso

### Paso 1: steps con tabla de datos (PASO 1 a 3)

Descomenta los PASO 1, 2 y 3 de `inscripcion_steps.py`. En el PASO 1, `context.table` recorre las filas de la tabla de `Antecedentes`: `row["taller"]`, `row["cupos"]` (siempre texto, por eso `int(...)`).

### Paso 2: el estado se filtra entre escenarios

```bash
uv run behave --format progress
```

```text
1 scenario passed, 0 failed, 1 error, 0 skipped
4 steps passed, 0 failed, 1 error, 4 skipped
```

Con `uv run behave` verás dónde falla el segundo escenario:

```text
    Dado los talleres:
      ...
      workshops.RegistrationError: workshop Python already exists
```

Abre `features/environment.py`: el registro se crea en `before_feature`, **una vez por feature**. El primer escenario añade los talleres; `Antecedentes` se repite en el segundo escenario sobre el **mismo** registro y el taller ya existe.

Corrige el hook: cámbialo a `before_scenario(context, scenario)`, que se ejecuta antes de cada escenario. Resultado:

```text
2 scenarios passed, 0 failed, 0 skipped
```

| Hook | Cuándo se ejecuta | Úsalo para |
|---|---|---|
| `before_all` | Una vez, al empezar | Configuración global (lectura de `userdata`) |
| `before_feature` | Antes de cada feature | Recursos caros y de solo lectura |
| `before_scenario` | Antes de cada escenario | Estado que los escenarios modifican |
| `after_scenario` | Después de cada escenario | Limpieza (cerrar conexiones, borrar archivos) |

### Paso 3: tags

```bash
uv run behave --format progress --tags=smoke
```

```text
1 scenario passed, 0 failed, 1 skipped
4 steps passed, 0 failed, 5 skipped
```

Solo se ejecuta el escenario con `@smoke`. Así se separa una suite rápida de humo de la suite completa.

### Paso 4: el mismo feature con pytest-bdd (PASO 4)

Descomenta el PASO 4 en `tests/test_inscripcion_bdd.py` y ejecuta:

```bash
uv run pytest -v
```

```text
tests/test_inscripcion_bdd.py::test_inscribirse_en_un_taller_con_cupo PASSED
tests/test_inscripcion_bdd.py::test_taller_lleno_manda_a_lista_de_espera PASSED
2 passed
```

Compara las dos implementaciones:

| | Behave | pytest-bdd |
|---|---|---|
| Estado entre pasos | `context` | Fixtures (`registry`) y `target_fixture="result"` |
| Estado nuevo por escenario | Hook `before_scenario` | Fixture de alcance function (por defecto) |
| Tabla de datos | `context.table` (filas como diccionarios) | Argumento `datatable` (lista de listas) |
| Tags | `--tags=smoke` | Marcadores de pytest: `-m smoke` |
| Ejecución | `uv run behave` | `uv run pytest` |

Prueba `uv run pytest -q -m smoke`: `1 passed, 1 deselected`.

> `pyproject.toml` filtra dos avisos de dependencias: pytest-bdd 8.1.0 (su última versión, de diciembre de 2024) usa APIs que pytest 10 eliminará, y `gherkin-official` usa un argumento deprecado en Python 3.14. No vienen de tu código, pero conviene saber que pytest-bdd podría no funcionar con pytest 10.

Compara con `solution/`.
