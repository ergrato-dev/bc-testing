# Proyecto - Primera suite de tests del dominio

> Entregable obligatorio de la semana 04.

## Objetivo

Escribir en Python las funciones de tu dominio de la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/3-proyecto/README.md) y una suite de pytest que automatice los mismos casos: camino feliz, entradas inválidas y valores límite. Al terminar tendrás la misma funcionalidad probada en dos lenguajes.

## Tiempo estimado

3 horas.

## Requisitos

- Tu proyecto de la semana 3: funciones, tests y `mutaciones.md`.
- Tu `casos-de-prueba.md` de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/3-proyecto/README.md), para los ID de los casos.
- Los [labs](../2-practicas/README.md) de esta semana.
- El dominio que te asignó el instructor.

## Alcance

Solo **funciones puras**, como en la semana 3: reciben datos y devuelven un resultado o lanzan un error. No leen la fecha del sistema, no usan bases de datos, ficheros ni red. Si una regla depende de «hoy», la función recibe la fecha de hoy como parámetro (un `date` del módulo `datetime`).

---

## Paso 1: el proyecto (15 min)

```bash
cp -r <ruta-del-bootcamp>/bootcamp/week-04-primeros_tests_automatizados_con_python/3-proyecto/starter ~/proyecto-semana-04
cd ~/proyecto-semana-04
uv sync --locked
git init
echo "__pycache__/" > .gitignore
git add .
git commit -m "proyecto semana 04: punto de partida"
```

El starter trae:

| Archivo | Qué es |
|---|---|
| `pyproject.toml` | Python 3.14 y pytest 9.1.1 fijados, y `[tool.pytest]` configurado; no cambies esas versiones |
| `.python-version` | `3.14` |
| `uv.lock` | Versiones exactas: súbelo con el proyecto |
| `src/domain.py` | Módulo vacío con `TODO` |
| `tests/test_domain.py` | Suite vacía con `TODO` |
| `mutaciones.md` | Tabla de mutaciones con `TODO` |

## Paso 2: las funciones (45 min)

Renombra `src/domain.py` igual que tu módulo de la semana 3, en `snake_case` (por ejemplo, `src/reservation.py`), y traduce tus funciones. Al menos tres:

- Una que **valide** una entrada y lance `ValueError("mensaje")` si es inválida. El mensaje es el de tu regla.
- Una que **calcule** algo: un importe, una cantidad, si una fecha está en rango.
- Una que **formatee** o transforme un dato: un código, un nombre, un texto para mostrar.

Nombres de funciones y variables en inglés y en `snake_case` (`validateGroupSize` pasa a `validate_group_size`); mensajes en el idioma de tu dominio, idénticos a los de la semana 3. En Python no hay `module.exports`: cualquier función del archivo se puede importar.

Traducir no es copiar línea a línea: revisa cada regla. `Number.isInteger(x)` pasa a `isinstance(x, int)`; una fecha `"AAAA-MM-DD"` pasa a `date(2026, 11, 18)` (con `from datetime import date` arriba), y la diferencia en días es `(day - today).days`.

Borra los `TODO` de `src/` cuando termines.

## Paso 3: los tests (60 min)

Renombra `tests/test_domain.py` igual que el módulo (`tests/test_reservation.py`) y traduce tus tests de la semana 3. Al menos **8 tests**:

| Tipo | Mínimo | De dónde salen |
|---|---|---|
| Camino feliz | 3 | Las clases válidas de tu partición |
| Entradas inválidas | 3, uno o más con `pytest.raises` | Las clases inválidas, una por test |
| Valores límite | 2 | Tu tabla de límites: el límite y su vecino de fuera |

Reglas:

- Nombre `test_<resultado>_when_<condición>` en todos; agrupa los de cada función en una clase `Test*` si quieres.
- AAA en el orden de siempre, y el ID del caso de la semana 2 en un comentario encima del `def`.
- Un `assert` o un `pytest.raises` como mínimo en cada test, con el esperado escrito a mano.
- La comparación de la [teoría 03](../1-teoria/03-assertions-y-ejecucion.md#resumen-de-comparaciones): `pytest.approx` para decimales y nada de `assert valor` para números.
- Ningún `@pytest.mark.skip`, `skipif` ni `xfail`.

## Paso 4: verde (15 min)

```bash
uv run pytest
```

Si un test falla, decide si el error está en la función o en el test, como en el [lab 03](../2-practicas/lab-03-de-casos-a-tests/README.md). Si un test que pasaba en Jest falla en pytest, mira primero las diferencias de la teoría 03 (decimales, `is`, `match`). Si encuentras un bug en tu función, apúntalo: es un hallazgo real.

## Paso 5: mutaciones (30 min)

Comprueba que tu suite protege el código, como en el [lab 04](../2-practicas/lab-04-tests-que-mienten/README.md). Repite en Python al menos dos de las mutaciones de la semana 3 (cambia un `>=` por `>`, un número, un mensaje), ejecuta `uv run pytest` con cada una y deshazla con `git restore src/`.

Anota cada una en `mutaciones.md`, con el nombre exacto del test que la detecta:

```markdown
| M1 | `people > 8` | `people > 9` | test_raises_value_error_when_the_group_has_9_people |
```

Si ninguna mutación pone un test en rojo, te falta un test: escríbelo y vuelve a probar. Si la línea tiene un `|`, escríbela sin él: rompe la tabla.

## Paso 6: comprobar y entregar (15 min)

Desde la carpeta del bootcamp:

```bash
PROYECTO=~/proyecto-semana-04 ./scripts/verificar-semana.sh 04
```

Cuando pasen las 9 comprobaciones, haz un commit y entrega por el canal que indique el instructor.

---

## Ejemplo

Un dominio de ejemplo, el **acuario virtual** de la semana 2. Reglas: el grupo tiene de 1 a 8 personas; el tour Familiar exige 3 o más; la fecha va de mañana a 60 días después de hoy.

```python
# src/tour.py
def validate_group_size(people: int, tour_type: str) -> bool:
    if not isinstance(people, int) or people < 1 or people > 8:
        raise ValueError("Entre 1 y 8 personas")
    if tour_type == "Familiar" and people < 3:
        raise ValueError("El tour familiar es para 3 personas o más")
    return True
```

```python
# tests/test_tour.py
class TestValidateGroupSize:
    # TC-ACUA-TOUR-006
    def test_raises_value_error_when_the_group_has_9_people(self) -> None:
        with pytest.raises(ValueError, match="Entre 1 y 8 personas"):
            validate_group_size(9, "Individual")
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json):

| Comprobación | Qué mira |
|---|---|
| instala | `uv sync --locked` funciona con tu lockfile |
| versiones | `.python-version` dice `3.14`, y `pyproject.toml` pide `>=3.14` y fija `pytest==9.1.1` |
| funciones | Los archivos de `src/` definen al menos tres funciones (`def` sin `_` delante) |
| pasa | `uv run pytest` termina sin fallos |
| ocho-tests | Al menos 8 tests, y todos se llaman `test_..._when_...` |
| errores | Al menos un `pytest.raises(` en `tests/` |
| sin-trampas | Ningún `skip`, `skipif` ni `xfail`, y tantas líneas con `assert` o `with pytest.raises(` como tests |
| mutaciones | `mutaciones.md` sin `TODO` y con dos filas o más que nombran un test `test_..._when_...` |
| sin-todo | Ningún `TODO` en `src/` ni en `tests/` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Tests que mienten](../2-practicas/lab-04-tests-que-mienten/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 04](../README.md)
