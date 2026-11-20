# Proyecto - Plan de pruebas y suite base en tres lenguajes

> Entregable obligatorio de la semana 06 y cierre de la Etapa 0.

## Objetivo

Juntar en un solo proyecto tus suites de las semanas [3](../../week-03-primeros_tests_automatizados_con_javascript/3-proyecto/README.md), [4](../../week-04-primeros_tests_automatizados_con_python/3-proyecto/README.md) y [5](../../week-05-primeros_tests_automatizados_con_java/3-proyecto/README.md), escribir su plan de pruebas por riesgo y demostrar con `verify.sh` que las tres prueban los mismos casos de tu dominio.

## Tiempo estimado

6.5 horas: 6 de desarrollo y 30 minutos para preparar la presentación.

## Requisitos

- Tus proyectos de las semanas 3, 4 y 5, en verde: `~/proyecto-semana-03`, `~/proyecto-semana-04` y `~/proyecto-semana-05`.
- Tu `casos-de-prueba.md` de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/3-proyecto/README.md), con las reglas y los ID de los casos.
- Los [labs](../2-practicas/README.md) de esta semana: el proyecto repite sus pasos con tu dominio.
- El dominio que te asignó el instructor.

## Alcance

Las funciones que ya escribiste, sin funciones nuevas. Si el plan descubre una regla sin función, va a «Fuera» de la sección 1. Lo que sí cambia son los tests: los que falten, los ID y los bugs que encuentres al comparar los tres lenguajes.

---

## Paso 1: un proyecto con tres carpetas (30 min)

```bash
cp -r <ruta-del-bootcamp>/bootcamp/week-06-plan_de_pruebas_y_suite_base_multilenguaje/3-proyecto/starter ~/proyecto-semana-06
cd ~/proyecto-semana-06
cp -r ~/proyecto-semana-03 js
cp -r ~/proyecto-semana-04 python
cp -r ~/proyecto-semana-05 java
rm -rf js/.git python/.git java/.git js/node_modules python/.venv java/target
ls -a
# .  ..  .gitignore  java  js  plan-de-pruebas.md  python  verify.sh
```

`rm -rf` borra sin preguntar, así que revisa la línea antes de pulsar Enter. Quita el historial de git de cada proyecto (el nuevo tiene el suyo) y las carpetas que se regeneran con `pnpm install`, `uv sync` y `mvn test`. Los originales siguen en `~/proyecto-semana-0N`.

El starter trae:

| Archivo | Qué es |
|---|---|
| `plan-de-pruebas.md` | Plantilla con las nueve secciones de la [teoría](../1-teoria/01-plan-de-pruebas-y-trazabilidad.md#el-plan-de-pruebas) y `TODO` |
| `verify.sh` | El script terminado del [lab 02](../2-practicas/lab-02-un-comando-para-tres-suites/README.md). No lo cambies: las comprobaciones lo ejecutan tal cual |
| `.gitignore` | `node_modules/`, `.venv/`, `__pycache__/`, `.pytest_cache/` y `target/`, para las tres carpetas |

`verify.sh` espera la estructura de las semanas 3 a 5: tests en `js/tests`, `python/tests` y `java/src/test`, y la matriz en `plan-de-pruebas.md`. Comprueba que las tres suites pasan en su carpeta nueva:

```bash
(cd js && pnpm install && pnpm test)
(cd python && uv sync && uv run pytest -q)
(cd java && mvn test)
```

Guarda el punto de partida:

```bash
git init
git add .
git commit -m "proyecto semana 06: punto de partida"
```

## Paso 2: alcance y reglas (30 min)

Rellena las secciones 1 y 2.

- **Reglas**: copia las de la sección 1 de tu `casos-de-prueba.md` con sus ID. Después lee tus funciones: si implementan una regla que la semana 2 no tenía (un precio, un formato de código), escríbela como regla nueva con el siguiente ID.
- **Alcance**: dentro van las funciones y las reglas que implementan. Fuera, cada regla sin función y cada caso que solo se puede probar con la interfaz, con el motivo.

## Paso 3: riesgos (30 min)

Rellena la sección 3 con un riesgo por regla como mínimo, como en el [paso 2 del lab 01](../2-practicas/lab-01-plan-de-pruebas-por-riesgo/README.md#paso-2-riesgos-15-min). Impacto `Alto`, `Medio` o `Bajo`; probabilidad y prioridad `Alta`, `Media` o `Baja`, escritas así, con la prioridad de la [tabla de la teoría](../1-teoria/01-plan-de-pruebas-y-trazabilidad.md#prioridad-por-riesgo). Si todos tus riesgos salen con prioridad Alta, revisa el impacto de cada uno.

Rellena también las secciones 4 y 5 como en el [paso 5 del lab 01](../2-practicas/lab-01-plan-de-pruebas-por-riesgo/README.md#paso-5-estrategia-y-criterios-10-min). Uno de tus criterios de salida tiene que ser que `./verify.sh` termine con código 0.

## Paso 4: inventario y matriz (75 min)

Haz el inventario de tus tres suites con los comandos del [paso 3 del lab 01](../2-practicas/lab-01-plan-de-pruebas-por-riesgo/README.md#paso-3-inventario-15-min): cuántos tests tiene cada una y qué ID lleva cada test. Lo normal es que en las semanas 3 y 4 solo algunos tests tengan ID.

Escribe la matriz (sección 6) con todos los casos que entran en el alcance:

- Los casos de la semana 2 que tus funciones cubren, con su ID.
- Un caso nuevo para cada test que no tenía caso, con el siguiente ID libre.
- La prioridad de cada caso es la del riesgo que cubre.
- ✅ donde el caso tiene que estar automatizado; — donde un lenguaje no lo puede expresar.

Mínimo **8 casos**. Un caso de prioridad Alta lleva ✅ en los tres lenguajes: si un lenguaje no lo puede expresar, el caso no es tan importante como para ser Alta, o necesitas otro caso que sí se pueda escribir.

## Paso 5: diferencias entre lenguajes (45 min)

Pregunta a tus tres implementaciones por las entradas en las que los lenguajes difieren, como en el [paso 5 del lab 02](../2-practicas/lab-02-un-comando-para-tres-suites/README.md#paso-5-mismo-caso-distinto-veredicto-15-min). Para un número: `5.0` y `true` (`True` en Python). Para un texto: espacios al principio, tildes, mayúsculas. Para una fecha: el último día del mes.

```bash
cd js
node -e 'const { validateGroupSize } = require("./src/tour"); console.log(validateGroupSize(true, "Individual"))'
cd ../python
uv run python -c 'from src.tour import validate_group_size; print(validate_group_size(True, "Individual"))'
cd ..
```

Cambia los nombres por los de tus funciones. Para cada diferencia, decide:

- **Un lenguaje no puede expresar la entrada** (Java no compila `true` como `int`): — en esa columna y el motivo en la sección 7, con el ID del caso.
- **Un lenguaje da otro veredicto**: es un bug o una regla incompleta. Si la regla es clara, crea el caso con ✅ en los lenguajes que pueden expresarlo; su test fallará en el lenguaje con el bug, y lo arreglas en el paso 6. Si la regla no lo dice, apúntalo en la sección 7 como pregunta abierta.

## Paso 6: verify.sh y los huecos (120 min)

```bash
./verify.sh
```

La primera vez fallará con una lista de huecos, como en el [paso 4 del lab 02](../2-practicas/lab-02-un-comando-para-tres-suites/README.md#paso-4-los-huecos-encontrados-por-el-script-20-min). Ciérralos uno a uno, empezando por los de prioridad Alta:

| Mensaje | Qué haces |
|---|---|
| `faltan en los tests: X` | Pon el ID encima del test que ya lo comprueba o escribe el test, con los nombres y el AAA de esa semana |
| `no están en la matriz: X` | Corrige el ID del comentario, o añade el caso a la matriz si es un test que vale |
| `hay tests sin ID` | Busca el test sin comentario y dale su caso |
| Una suite falla | Decide si el error está en la función o en el test, como en el lab 03 de las semanas 3 a 5 |

Cada comentario con el ID va solo en su línea, justo encima del `it(`, del `def test_` o del `@Test`: `verify.sh` no cuenta un ID que comparta línea con otro texto. Apunta cada hueco en la sección 8, con lo que hiciste para cerrarlo. Un bug encontrado al comparar lenguajes es el mejor hallazgo de la semana: apúntalo aunque ya esté arreglado.

## Paso 7: evidencia y entrega (30 min)

Cuando `./verify.sh` termine con `OK: tres suites en verde y trazabilidad completa`, copia en la sección 9 las líneas de resultado de cada suite y el bloque de trazabilidad, en un bloque `text`. Después, desde la carpeta del bootcamp:

```bash
PROYECTO=~/proyecto-semana-06 ./scripts/verificar-semana.sh 06
```

Cuando pasen las 11 comprobaciones, haz un commit y entrega por el canal que indique el instructor.

## Paso 8: presentación (30 min)

Prepara 5 minutos para el grupo, con datos de tu plan: el riesgo de prioridad más alta y por qué, cuántos huecos encontraste y de qué tipo, y la diferencia entre lenguajes que más te sorprendió.

---

## Ejemplo

Un dominio de ejemplo, el **acuario virtual** de las semanas 2 a 5. Al preguntar a las tres implementaciones por `true` como número de personas:

```bash
node -e 'const { validateGroupSize } = require("./src/tour"); console.log(validateGroupSize(true, "Individual"))'
# .../src/tour.js:8
#     throw new Error("Entre 1 y 8 personas");
#     ^
#
# Error: Entre 1 y 8 personas
#     at validateGroupSize (.../src/tour.js:8:11)
# ...

uv run python -c 'from src.tour import validate_group_size; print(validate_group_size(True, "Individual"))'
# True
```

En Python `bool` es una subclase de `int`, así que `isinstance(True, int)` es `True` y la función confirmaba una reserva de 1 persona. La regla R2 dice «un entero de 1 a 8»: es un bug. El caso nuevo entra en la matriz:

```markdown
| TC-ACUA-TOUR-024 | R2 | `true` como número de personas se rechaza | Baja | ✅ | ✅ | — |
```

Con el test escrito en Python, `./verify.sh` se para en pytest:

```text
E       Failed: DID NOT RAISE ValueError

tests/test_tour.py:45: Failed
=========================== short test summary info ============================
FAILED tests/test_tour.py::TestValidateGroupSize::test_raises_value_error_when_the_group_size_is_a_boolean
1 failed, 15 passed in 0.03s
```

La sección 7 explica el —:

```markdown
- TC-ACUA-TOUR-013 y TC-ACUA-TOUR-024 no se automatizan en Java: `people` es un `int`, y ni `2.5` ni `true` compilan como argumento.
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json):

| Comprobación | Qué mira |
|---|---|
| estructura | Existen `js/package.json`, `python/pyproject.toml`, `java/pom.xml`, `plan-de-pruebas.md` y un `verify.sh` ejecutable |
| js-pasa | `pnpm install --frozen-lockfile` y `pnpm test` en `js/`, sin tests `todo`, saltados ni fallidos |
| python-pasa | `uv sync --locked` y `uv run pytest` en `python/`, sin tests saltados ni fallidos |
| java-pasa | La última línea `Tests run:` de `mvn test` en `java/` termina en `Failures: 0, Errors: 0, Skipped: 0` |
| plan-completo | `plan-de-pruebas.md` tiene sus 9 secciones `## N.` y ningún `TODO` |
| riesgos | 3 filas `K` o más con impacto `Alto`/`Medio`/`Bajo`, y probabilidad y prioridad `Alta`/`Media`/`Baja` |
| matriz | 8 filas `TC-` o más, todas con prioridad y con ✅ o — en las tres columnas de lenguaje |
| alta-en-tres | Ninguna fila de prioridad `Alta` tiene — |
| motivos | El ID de cada fila con — aparece en la sección 7 |
| verify | `./verify.sh` termina con código 0 |
| evidencia | La sección 9 contiene `OK: tres suites en verde y trazabilidad completa` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Un comando para tres suites](../2-practicas/lab-02-un-comando-para-tres-suites/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 06](../README.md)
