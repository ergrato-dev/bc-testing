# Lab 02 - Estrategia y README

## Objetivo

Escribir `testing-strategy.md` y el `README.md` de `ci-lab` a partir del inventario del lab 01: riesgos, dónde corre cada prueba, criterios de salida y decisiones. Una de las decisiones cambia el repositorio: un solo umbral, en el proyecto de cada suite.

## Tiempo estimado

75 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-la-estrategia-de-testing-del-repositorio.md), secciones 3 a 7.
- El [lab 01](../lab-01-inventario-del-repositorio/README.md) terminado: `inventario.md` sin `TODO` en la rama `strategy` de `ci-lab`.

## Punto de partida

```bash
cd ~/ci-lab
git switch strategy
cp <ruta-del-bootcamp>/bootcamp/week-46-proyecto_final_bootcamp_testing_zero_to_hero/2-practicas/lab-02-estrategia-y-readme/starter/testing-strategy.md .
cp <ruta-del-bootcamp>/bootcamp/week-46-proyecto_final_bootcamp_testing_zero_to_hero/2-practicas/lab-02-estrategia-y-readme/starter/README.md .
```

Los dos archivos de [`starter/`](starter/) son las plantillas que usarás en el proyecto. Las secciones de `testing-strategy.md` están explicadas en la teoría, con el mismo número.

---

## Paso 1: sistema y alcance

Dos o tres frases. Para `ci-lab`:

```markdown
## 1. Sistema y alcance

`ci-lab` es un repositorio de prácticas de una biblioteca, con una pieza por lenguaje: multas por retraso en `js/`, días de préstamo por tipo de socio en `python/` y el préstamo de libros sobre PostgreSQL en `go/`. Las tres partes son independientes: no se llaman entre sí. Fuera de esta estrategia: rendimiento, seguridad y E2E, porque no hay servidor ni interfaz.
```

Escribe la tuya. El alcance también dice lo que **no** se prueba y por qué.

## Paso 2: inventario

Mueve la tabla **Suites** de `inventario.md` a la sección 2 y completa la línea de la fecha. Después, borra el inventario: su contenido ya vive en la estrategia, y dos copias acabarían diciendo cosas distintas.

```bash
git rm -q inventario.md
```

`git rm` borra el archivo de la carpeta y deja el borrado preparado para el próximo commit; `-q` (*quiet*) no imprime nada.

## Paso 3: riesgos

Lee el código de las tres suites (`js/src/fees.js`, `python/library/loans.py`, `go/store.go`) y escribe qué puede salir mal que importe a la biblioteca. Una fila de ejemplo:

```markdown
| Un libro prestado se presta otra vez | Integración con PostgreSQL | `go/`: `go test` con `DATABASE_URL` (`TestLendTwiceFails`) | CI `go`, con PostgreSQL como contenedor de servicio | Test en verde y cobertura ≥ 85 % |
```

Añade al menos tres más: una multa mal calculada, el texto del recibo (`describeFee`), un tipo de socio que no existe... Si para un riesgo no encuentras ningún test, no inventes la fila: ese riesgo va en la sección 7.

## Paso 4: dónde se ejecuta cada prueba

Lleva la tabla **Dónde corre cada suite** de `inventario.md` al formato de la sección 4: por sitio, no por suite. La fila «A mano» de `ci-lab` es SonarQube: solo existe si lo arrancas en tu equipo.

## Paso 5: criterios de salida y la primera decisión

El inventario encontró tres umbrales distintos. Antes de escribir la sección 5, hay que decidir cuál vale. La decisión, en el formato de la sección 6:

```markdown
### D1 - Un umbral del 85 % en todas las suites, en el proyecto

- **Contexto:** el inventario encontró tres criterios: ninguno en `js/`, 80 % en `python/` y 85 % en `go/`, este último solo en el workflow.
- **Decisión:** 85 % de líneas y ramas en `js/` (`coverageThreshold` en `package.json`, y `pnpm test` con cobertura) y 85 % en `python/` (`fail_under`).
- **Alternativas:** un umbral por suite según su cobertura actual (así se eligió el 80 % de Python); ningún umbral y revisar la cobertura en cada PR.
- **Consecuencias:** las tres suites tienen el mismo criterio y la CI de `js/` lo comprueba sin cambiar `js.yml`. Hoy las tres lo cumplen (100 %, 100 % y 87.5 %); un test que se borre en `js/` o en `python/` pondrá el PR en rojo.
```

Aplica la decisión. En `js/package.json`, cambia el script `test` y añade la clave `jest`:

```diff
   "scripts": {
-    "test": "jest",
+    "test": "jest --coverage",
     "test:coverage": "jest --coverage"
   },
   "devDependencies": {
     "jest": "30.5.2"
+  },
+  "jest": {
+    "coverageThreshold": {
+      "global": {
+        "lines": 85,
+        "branches": 85
+      }
+    }
   }
 }
```

Jest lee su configuración de la clave `jest` de `package.json` si no hay `jest.config.js`. `coverageThreshold.global` se aplica al total de todos los archivos; con `--coverage`, si no se cumple, Jest termina con 1.

```bash
cd js
pnpm test
# Tests:       9 passed, 9 total
```

Comprueba que el umbral muerde. `-t lateFee` ejecuta solo los tests cuyo nombre contiene `lateFee`, y `describeFee` se queda sin cubrir:

```bash
pnpm test -t lateFee
```

```text
Jest: Coverage for branches (66.66%) does not meet "global" threshold (85%)
Jest: Coverage for lines (66.66%) does not meet "global" threshold (85%)
Tests:       2 skipped, 7 passed, 9 total
[ELIFECYCLE] Test failed. See above for more details.
```

Y Python:

```bash
cd ../python
sed -i 's/^fail_under = 80$/fail_under = 85/' pyproject.toml
uv run pytest --cov -q
# Required test coverage of 85.0% reached. Total coverage: 100.00%
# 9 passed in 0.03s
cd ..
```

`sed -i 's/viejo/nuevo/' archivo` reemplaza el texto dentro del archivo; `^` y `$` hacen que solo cambie la línea entera `fail_under = 80`.

Ahora escribe la sección 5: un criterio por fila, con dónde vive y quién lo comprueba.

## Paso 6: dos decisiones más

Escribe D2 y D3 con sus cuatro partes. Las dos están en `ci-lab` sin que nadie las haya escrito:

- **D2: PostgreSQL de `go/`.** Contenedor de servicio en la CI y `compose.yaml` en local. Alternativa: Testcontainers, como en el museo ([teoría 02 de la semana 45](../../../week-45-ci_cd_y_testing_automatizado/1-teoria/02-preparar-cada-lenguaje-caches-y-servicios.md#3-postgresql-en-la-ci)). Consecuencia: los tests necesitan `DATABASE_URL` y fallan sin ella.
- **D3: el umbral de Go se queda en el workflow.** `go test` no tiene opción de umbral. Alternativas: un `verify.sh` como el de la [semana 36](../../../week-36-proyecto_integrador_go/README.md), o el mismo `awk` en un `Makefile`. Consecuencia: en tu equipo, `go test` pasa con cualquier cobertura.

Una decisión que se queda como está también se escribe: D3 dice que alguien lo vio y eligió no cambiarlo.

## Paso 7: riesgos abiertos

Lo que queda de **Huecos** de `inventario.md` y no resolvió ninguna decisión, más los riesgos del paso 3 sin test. Por ejemplo: «`js/` no tiene lint en el hook de pre-commit», «el umbral de Go no se comprueba en local (D3)». Cada uno, con su próximo paso.

## Paso 8: el README

Llena `README.md`. En **Estado**, un *badge* por workflow con tu usuario:

```markdown
![js](https://github.com/<tu-usuario>/ci-lab/actions/workflows/js.yml/badge.svg)
![python](https://github.com/<tu-usuario>/ci-lab/actions/workflows/python.yml/badge.svg)
![go](https://github.com/<tu-usuario>/ci-lab/actions/workflows/go.yml/badge.svg)
```

En **Cómo ejecutar las suites**, los comandos del lab 01, paso 3, desde la raíz. En **Documentación**, el enlace a `testing-strategy.md`.

## Paso 9: el pull request

```bash
grep -c TODO testing-strategy.md README.md
# testing-strategy.md:0
# README.md:0
git add testing-strategy.md README.md js/package.json python/pyproject.toml
git commit -m "Add the testing strategy and the README, with an 85 % threshold in every suite"
git push -u origin strategy
gh pr create --fill
```

- El borrado de `inventario.md` ya está preparado desde el paso 2. Con `git status` antes del commit ves todo lo que entra.
- `git push -u origin strategy` sube la rama y la deja enlazada con la remota.
- `gh pr create --fill` abre el PR contra `main` y rellena el título y la descripción con los commits de la rama.

El hook de pre-commit revisa los archivos al hacer commit. Cuando terminen los checks del PR (`gh pr checks --watch`), abre el log del job `js`: antes no mostraba ninguna tabla de cobertura y ahora sí. Mezcla el PR y comprueba en la página del repositorio que los *badges* del README salen en verde.

---

## Comprobación final

- `testing-strategy.md` y `README.md` sin `TODO`, en `main`.
- `pnpm test` en `js/` comprueba el 85 % y `fail_under` de `python/` es 85.
- Los tres *badges* del README en verde.

## Qué has aprendido

- La estrategia sale del inventario: riesgos, sitios, criterios y decisiones, cada uno en su sección.
- Una decisión tiene contexto, alternativas y consecuencias, y a veces cambia el repositorio (D1). Una decisión de no cambiar nada también se escribe (D3).
- `coverageThreshold` en `package.json` y `jest --coverage` en el script `test` hacen que la misma orden aplique el umbral en local y en la CI.
- Los *badges* del README muestran el estado de cada workflow sin que nadie tenga que actualizarlos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Inventario del repositorio](../lab-01-inventario-del-repositorio/README.md) | [Proyecto — Proyecto final - La estrategia de testing de tu repositorio](../../3-proyecto/README.md) → |

[↑ Semana 46](../../README.md)
