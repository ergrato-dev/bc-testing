# Proyecto - Primera suite de tests del dominio

> Entregable obligatorio de la semana 03.

## Objetivo

Escribir en JavaScript tres o más funciones de la funcionalidad de tu dominio que diseñaste en la semana 2, y una suite de Jest que automatice sus casos: camino feliz, entradas inválidas y valores límite. La semana 4 repetirá la misma suite en Python.

## Tiempo estimado

3 horas.

## Requisitos

- Tu `casos-de-prueba.md` de la [semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/3-proyecto/README.md): reglas, clases, límites y casos.
- Los [labs](../2-practicas/README.md) de esta semana.
- El dominio que te asignó el instructor.

## Alcance

Solo **funciones puras**: reciben datos y devuelven un resultado o lanzan un error. No leen la fecha del sistema, no usan bases de datos, ficheros ni red. Si una regla depende de «hoy», la función recibe la fecha de hoy como parámetro, igual que en la semana 2 fijabas la fecha en las precondiciones.

---

## Paso 1: el proyecto (15 min)

```bash
cp -r <ruta-del-bootcamp>/bootcamp/week-03-primeros_tests_automatizados_con_javascript/3-proyecto/starter ~/proyecto-semana-03
cd ~/proyecto-semana-03
pnpm install
git init
echo "node_modules/" > .gitignore
git add .
git commit -m "proyecto semana 03: punto de partida"
```

El starter trae:

| Archivo | Qué es |
|---|---|
| `package.json` | pnpm 12.6.0 y Jest 30.5.2 fijados; no cambies esas versiones |
| `pnpm-workspace.yaml` | `'@parcel/watcher': false`, como en el [lab 01](../2-practicas/lab-01-primer-proyecto-jest/README.md) |
| `pnpm-lock.yaml` | Versiones exactas: súbelo con el proyecto |
| `src/domain.js` | Módulo vacío con `TODO` |
| `tests/domain.test.js` | Suite vacía con `TODO` |
| `mutaciones.md` | Tabla de mutaciones con `TODO` |

## Paso 2: las funciones (45 min)

Renombra `src/domain.js` con el nombre de tu funcionalidad (por ejemplo, `src/reservation.js`) y escribe al menos tres funciones a partir de las reglas de tu `casos-de-prueba.md`:

- Una que **valide** una entrada y lance `new Error("mensaje")` si es inválida. El mensaje es el de tu regla.
- Una que **calcule** algo: un importe, una cantidad, si una fecha está en rango.
- Una que **formatee** o transforme un dato: un código, un nombre, un texto para mostrar.

Exporta las tres con `module.exports = { ... }`. Nombres de funciones y variables en inglés; mensajes en el idioma de tu dominio.

Borra los `TODO` de `src/` cuando termines.

## Paso 3: los tests (60 min)

Renombra `tests/domain.test.js` igual que el módulo (`tests/reservation.test.js`) y escribe al menos **8 tests**:

| Tipo | Mínimo | De dónde salen |
|---|---|---|
| Camino feliz | 3 | Las clases válidas de tu partición |
| Entradas inválidas | 3, uno o más con `toThrow` | Las clases inválidas, una por test |
| Valores límite | 2 | Tu tabla de límites: el límite y su vecino de fuera |

Reglas:

- Nombre `should <resultado> when <condición>` en todos.
- AAA en el orden de siempre, y el ID del caso de la semana 2 en un comentario encima del `it`.
- Un `expect` como mínimo en cada test, con el esperado escrito a mano.
- El matcher de la [teoría 03](../1-teoria/03-matchers-y-ejecucion.md#resumen-de-matchers): nada de `toBeTruthy` para números.
- Ningún `it.only`, `it.skip` ni `xit`.

## Paso 4: verde (15 min)

```bash
pnpm test
```

Si un test falla, decide si el error está en la función o en el test, como en el [lab 03](../2-practicas/lab-03-de-casos-a-tests/README.md). Si encuentras un bug en tu función, apúntalo: es un hallazgo real.

## Paso 5: mutaciones (30 min)

Comprueba que tu suite protege el código, como en el [lab 04](../2-practicas/lab-04-tests-que-mienten/README.md). Haz al menos dos mutaciones en `src/` (cambia un `>=` por `>`, un número, un mensaje), ejecuta `pnpm test` con cada una y deshazla con `git restore src/`.

Anota cada una en `mutaciones.md`, con el nombre exacto del test que la detecta:

```markdown
| M1 | `people > 8` | `people > 9` | should throw an error when the group has 9 people |
```

Si ninguna mutación pone un test en rojo, te falta un test: escríbelo y vuelve a probar. Si la línea tiene un `|`, escríbela sin él: rompe la tabla.

## Paso 6: comprobar y entregar (15 min)

Desde la carpeta del bootcamp:

```bash
PROYECTO=~/proyecto-semana-03 ./scripts/verificar-semana.sh 03
```

Cuando pasen las 9 comprobaciones, haz un commit y entrega por el canal que indique el instructor.

---

## Ejemplo

Un dominio de ejemplo, el **acuario virtual** de la semana 2. Reglas: el grupo tiene de 1 a 8 personas; el tour Familiar exige 3 o más; la fecha va de mañana a 60 días después de hoy.

```javascript
// src/tour.js
function validateGroupSize(people, tourType) {
  if (!Number.isInteger(people) || people < 1 || people > 8) {
    throw new Error("Entre 1 y 8 personas");
  }
  if (tourType === "Familiar" && people < 3) {
    throw new Error("El tour familiar es para 3 personas o más");
  }
  return true;
}
```

```javascript
// tests/tour.test.js
// TC-ACUA-TOUR-006
it("should throw an error when the group has 9 people", () => {
  expect(() => validateGroupSize(9, "Individual")).toThrow("Entre 1 y 8 personas");
});
```

Tu entrega usa tu dominio, tus reglas y tus casos.

---

## ✅ Requisitos verificables

Copia legible de [checks.json](../checks.json):

| Comprobación | Qué mira |
|---|---|
| instala | `pnpm install --frozen-lockfile` funciona con tu lockfile |
| versiones | `package.json` fija `pnpm@12.6.0` y `jest` `30.5.2` |
| funciones | Los archivos de `src/` exportan al menos tres funciones |
| pasa | `pnpm test` termina sin fallos |
| ocho-tests | Al menos 8 tests, y todos se llaman `should ... when ...` |
| errores | Al menos un `toThrow(` en `tests/` |
| sin-trampas | Ningún `.only`, `.skip`, `.todo`, `xit` ni `xdescribe`, y tantos `expect(` como tests |
| mutaciones | `mutaciones.md` sin `TODO` y con dos filas o más que nombran un test `should ... when ...` |
| sin-todo | Ningún `TODO` en `src/` ni en `tests/` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Tests que mienten](../2-practicas/lab-04-tests-que-mienten/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 03](../README.md)
