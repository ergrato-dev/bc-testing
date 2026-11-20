# Proyecto - Suite parametrizada con Jest

> Entregable obligatorio de la semana 08. Segundo incremento de la Etapa 1 sobre el proyecto de la semana 7.

## Objetivo

Convertir en tablas los tests de JS de tu dominio que solo cambian de datos, comprobar los objetos y los errores con el matcher que dice exactamente qué se espera, y arreglar `verify.sh` para que cuente los tests que genera cada fila.

## Tiempo estimado

3 horas.

## Requisitos

- Tu proyecto de la [semana 7](../../week-07-jest_avanzado_y_organizacion_de_suites/3-proyecto/README.md) en `~/proyecto-semana-07`, con `./verify.sh` en verde.
- Los [labs](../2-practicas/README.md) de esta semana: el proyecto repite sus pasos con tu dominio.

## Alcance

Solo cambian `js/`, `verify.sh` y el plan. Python y Java no se tocan; sus tablas llegan en las semanas 17 (`@pytest.mark.parametrize`) y 25 (`@ParameterizedTest`).

---

## Paso 1: punto de partida (10 min)

```bash
cp -r ~/proyecto-semana-07 ~/proyecto-semana-08
cd ~/proyecto-semana-08
cp <ruta-del-bootcamp>/bootcamp/week-08-matchers_avanzados_y_parametrizacion/3-proyecto/starter/tablas.md .
./verify.sh
# ...
# OK: tres suites en verde y trazabilidad completa
git add tablas.md
git commit -m "proyecto semana 08: punto de partida"
```

`tablas.md` es el inventario del paso 2, con sus tres secciones y un `TODO` en cada una.

## Paso 2: inventario (25 min)

Antes de tocar un test, decide qué va en una tabla. Recorre los archivos de `js/tests/` y rellena `tablas.md`, como en el [paso 1 del lab 01](../2-practicas/lab-01-de-tests-repetidos-a-tablas/README.md#paso-1-lo-que-se-repite-10-min):

- **Tablas**: cada grupo de tests con la misma función, el mismo contexto y el mismo `expect`. Escribe los IDs de sus casos, las columnas que tendrá cada fila y el nombre del test con sus `$variables`.
- **Tests que se quedan como `it`**: los que están solos o comprueban algo distinto, con el motivo.
- **Casos nuevos**: mientras haces el inventario, mira los límites de tus reglas. Con una tabla, un caso que faltaba cuesta una fila: apúntalo aquí.

Si dos contextos de la semana 7 solo se distinguen por el mensaje de error esperado, pueden ser una tabla con una columna `expected`. Si una tabla necesitaría un `if` en el cuerpo, son dos tablas.

## Paso 3: las tablas (50 min)

Convierte cada fila del inventario en un `it.each` con filas de objetos, como en el [lab 01](../2-practicas/lab-01-de-tests-repetidos-a-tablas/README.md):

- El ID de cada caso, en una línea sola, encima de su fila.
- El nombre del test, con `$campo` para los datos de la fila. Si un dato no se lee bien en el nombre, añade una columna solo para el nombre (`what`).
- El nombre sigue empezando por `should`, y va en la misma línea que el cierre de la tabla: `])("should ...", ...)`.

```bash
cd js
pnpm test --verbose
grep -c "\.each(" tests/*.test.js
grep -rn "\.only(\|\.skip(" tests/
```

Comprueba que tus tablas encuentran los bugs: mueve un límite en tu código (un `<` por un `<=`) y mira que el nombre del test que falla dice qué fila es. Devuelve el código a su sitio.

## Paso 4: casos nuevos (15 min)

Añade las filas de la sección «Casos nuevos» de `tablas.md`, cada una con un ID nuevo, a continuación del último de tu matriz. En el plan:

- **Sección 6**: una fila por caso nuevo, con ✅ en JS y — en Python y Java.
- **Sección 7**: el motivo, con el ID entero de cada caso: las tablas de Python y Java llegan en sus semanas, y entonces se añadirán.

## Paso 5: matchers (30 min)

Repasa los `expect` de tu suite con la [teoría 02](../1-teoria/02-matchers-de-objetos-y-arrays.md) y la [teoría 03](../1-teoria/03-errores-y-fallos-legibles.md):

| Lo que compruebas | Matcher |
|---|---|
| Un objeto entero que guarda tu servicio | `toStrictEqual` |
| Un campo que cambia en cada ejecución o que el test no controla | Un asimétrico: `expect.any`, `expect.stringMatching`, `expect.objectContaining` |
| Un array: cuántos hay, si está un elemento | `toHaveLength`, `toContain`, `toContainEqual` |
| Un error | `toThrow` con el mensaje completo; nunca `toThrow()` vacío |

Cambia los `toEqual` de objetos por `toStrictEqual`. Si alguno falla, mira el diff: un campo de más en lo que guarda tu servicio es un hallazgo para la sección 8 del plan.

Tu servicio de la semana 7 guarda varias cosas: escribe al menos un test que guarde dos y compruebe la lista con `toHaveLength` y `toContainEqual(expect.objectContaining(...))`. Es un caso nuevo; añádelo a la matriz como en el paso 4.

```bash
grep -rn "toThrow()" tests/
```

No tiene que salir nada.

## Paso 6: verify.sh cuenta las filas (20 min)

Ejecuta `verify.sh` y mira la línea de JS de la trazabilidad:

```bash
cd ~/proyecto-semana-08
./verify.sh 2>&1 | grep -E "^Tests:|^JavaScript"
```

`2>&1` junta la salida de errores con la normal, para que `grep` también vea el informe de Jest.

El número de tests de JS es menor que el de la línea `Tests:` de Jest. `verify.sh` cuenta las líneas `it(` con `grep`, y un `it.each` es una sola línea para varios tests. Así no ve una fila sin ID. Compruébalo: añade a una tabla una fila nueva sin comentario encima y ejecuta `./verify.sh`. Termina en `OK`.

Arréglalo para que use el total de Jest. Cambia el bloque de JavaScript:

```bash
echo "== JavaScript"
# Jest cuenta un test por fila de cada it.each; grep solo vería la línea del it.each.
# tee muestra la salida de Jest y deja pasar una copia para leer el total de la línea Tests:.
js_total=$(cd js && pnpm test 2>&1 | tee /dev/stderr | grep '^Tests:' | grep -oE '[0-9]+ total' | grep -oE '[0-9]+')
```

| Parte | Qué hace |
|---|---|
| `2>&1` | Jest escribe el informe en la salida de errores; así llega a la tubería |
| `tee /dev/stderr` | Copia lo que recibe en la pantalla y lo pasa al siguiente comando |
| `grep '^Tests:'` y `grep -oE '[0-9]+ total'` | La línea `Tests:` y, de ella, el número que va antes de `total` |

Si Jest falla, `pipefail` hace que falle la asignación, y `set -e` para el script igual que antes. En la función `trazar`, usa ese total cuando llegue como sexto argumento:

```bash
  # Si llega el total del ejecutor (sexto argumento), se usa; si no, se cuentan los tests con grep.
  tests=${6:-$(grep -rhIE --include="$4" "$5" "$3" | wc -l)}
```

`${6:-valor}` es el sexto argumento o, si no llega, `valor`. Pásalo en la llamada de JavaScript:

```bash
trazar JavaScript 6 js/tests '*.js' '^ *(it|test)[(]' "$js_total"
```

Python y Java no cambian: aún no tienen tablas. Vuelve a ejecutar con la fila sin ID:

```bash
./verify.sh 2>&1 | grep -E "^JavaScript|sin ID|^OK|^FALLO"
# JavaScript: 28 tests, 27 con ID
#   hay tests sin ID
# FALLO: la matriz y los tests no coinciden
```

Tus números serán otros. Quita la fila sin ID y comprueba que vuelve a `OK`.

## Paso 7: el plan (15 min)

- **Sección 4**: dos frases sobre las tablas (qué casos van en una, dónde va el ID) y sobre los matchers (`toStrictEqual` para lo guardado, `toThrow` con el mensaje).
- **Sección 8**: lo que encontraste. Como mínimo, el hueco de `verify.sh` del paso 6 y los casos que faltaban.

## Paso 8: verify.sh y entrega (5 min)

```bash
cd ~/proyecto-semana-08
./verify.sh
# OK: tres suites en verde y trazabilidad completa
```

Copia el resumen en la sección 9 con la fecha, y comprueba la entrega:

```bash
PROYECTO=~/proyecto-semana-08 <ruta-del-bootcamp>/scripts/verificar-semana.sh 08
git add .
git commit -m "proyecto semana 08: tablas de casos y matchers"
```

---

## Ejemplo

El **acuario virtual** de las semanas anteriores. Sus 16 tests de `tour.test.js` pasan a cuatro tablas y un `it`, con dos casos nuevos que salieron en el inventario: el Familiar con 8 personas (`TC-ACUA-TOUR-033`) y su precio mínimo (`TC-ACUA-TOUR-034`):

```bash
pnpm test --verbose tests/tour.test.js
# PASS tests/tour.test.js
#   tour
#     validateGroupSize
#       when the group size is allowed
#         ✓ should accept 1 people on the Individual tour (2 ms)
#         ...
#         ✓ should accept 8 people on the Familiar tour
#       when the group size is not allowed
#         ✓ should throw 'Entre 1 y 8 personas' for 0 people on the Individual tour (12 ms)
#         ...
#         ✓ should throw 'El tour familiar es para 3 personas o más' for 2 people on the Familiar tour (1 ms)
```

Los cinco contextos de `validateGroupSize` de la semana 7 quedan en dos: los que aceptan y los que rechazan, con el mensaje como columna. El archivo pasa de 123 líneas a 82.

`verify.sh` sin arreglar, con 27 tests en Jest:

```bash
./verify.sh 2>&1 | grep -E "^Tests:|^JavaScript|^OK"
# Tests:       27 passed, 27 total
# JavaScript: 8 tests, 27 con ID
# OK: tres suites en verde y trazabilidad completa
```

Con una fila sin ID (`{ people: 6, tourType: "Individual" }`), Jest cuenta 28 y `verify.sh` sigue en `OK`:

```bash
./verify.sh 2>&1 | grep -E "^Tests:|^JavaScript|^OK|^FALLO"
# Tests:       28 passed, 28 total
# JavaScript: 8 tests, 27 con ID
# OK: tres suites en verde y trazabilidad completa
```

Con el arreglo del paso 6 y la misma fila:

```bash
./verify.sh 2>&1 | grep -E "^JavaScript|sin ID|^OK|^FALLO"
# JavaScript: 28 tests, 27 con ID
#   hay tests sin ID
# FALLO: la matriz y los tests no coinciden
```

En el plan, el hueco va a la sección 8:

```markdown
| `verify.sh` contaba 8 tests de JS cuando Jest ejecutaba 27: su `grep` buscaba líneas `it(` y no ve las filas de un `it.each`. Una fila sin ID pasaba la comprobación | JS | Todos | Alta | `verify.sh` toma el total de la línea `Tests:` de Jest |
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json). Todos se ejecutan sobre `js/`, salvo `inventario`, `verify-cuenta-filas`, `motivos` y `verify`:

| Comprobación | Qué mira |
|---|---|
| js-pasa | `pnpm install --frozen-lockfile` y `pnpm test` terminan con código 0, sin tests `todo`, saltados ni fallidos |
| inventario | `tablas.md` existe y no tiene ningún `TODO` |
| sin-focos | Ningún archivo de `tests/` tiene `.only(`, `.skip(`, `fit(`, `xit(`, `fdescribe(` ni `xdescribe(` |
| nombres | Todos los `it(` y `test(` empiezan por `should ` |
| tablas | 3 `.each(` o más |
| nombres-con-datos | Cada línea `])("...` o `` `)("... `` lleva un `$` o un `%` |
| filas-con-id | El número de comentarios `// TC-...` en una línea sola es igual al total de tests de Jest |
| estricto | Algún test usa `toStrictEqual(` |
| asimetrico | Algún test usa `expect.any`, `expect.anything`, `expect.stringMatching`, `expect.stringContaining`, `expect.objectContaining` o `expect.arrayContaining` |
| coleccion | Algún test usa `toHaveLength(`, `toContain(` o `toContainEqual(` |
| error-exacto | Hay algún `toThrow(` y ninguno es `toThrow()` |
| verify-cuenta-filas | En una copia del proyecto, con un archivo de tests nuevo que tiene dos filas sin ID, `./verify.sh` falla |
| motivos | El ID de cada fila con — aparece en la sección 7 |
| verify | `./verify.sh` termina con código 0 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Errores y fallos legibles](../2-practicas/lab-03-errores-y-fallos-legibles/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 08](../README.md)
