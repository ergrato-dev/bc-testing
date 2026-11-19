# Lab 01 - Inventario del repositorio

## Objetivo

Hacer el inventario de `ci-lab`, el repositorio de la semana 45, con números de ejecuciones reales: qué suites hay, cuántos tests y qué cobertura tiene cada una, cuánto tardan, dónde corren y qué umbral las para. Al final, el inventario enseña huecos que nadie había escrito.

## Tiempo estimado

75 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-la-estrategia-de-testing-del-repositorio.md), secciones 1 y 2.
- Tu `ci-lab` de la [semana 45](../../../week-45-ci_cd_y_testing_automatizado/2-practicas/README.md) con los cuatro labs terminados: los workflows `js`, `python` y `go`, el `.pre-commit-config.yaml` y los tests de `describeFee`.
- pnpm, uv, Go y Docker en marcha.

## Punto de partida

```bash
cd ~/ci-lab
git switch main
git pull
git switch -c strategy
cp <ruta-del-bootcamp>/bootcamp/week-46-proyecto_final_bootcamp_testing_zero_to_hero/2-practicas/lab-01-inventario-del-repositorio/starter/inventario.md .
```

`git switch -c strategy` crea la rama `strategy` y se cambia a ella: el trabajo de esta semana irá en un PR. [`starter/inventario.md`](starter/inventario.md) tiene cuatro tablas con `TODO`. Cada paso llena una parte.

---

## Paso 1: qué hay en el repositorio

```bash
git ls-files | grep -vE 'lock|\.gitignore'
```

```text
.github/workflows/go.yml
.github/workflows/js.yml
.github/workflows/python.yml
.pre-commit-config.yaml
go/compose.yaml
go/go.mod
go/go.sum
go/store.go
go/store_test.go
js/package.json
js/pnpm-workspace.yaml
js/sonar-project.properties
js/src/fees.js
js/src/fees.test.js
python/.python-version
python/library/__init__.py
python/library/loans.py
python/pyproject.toml
python/tests/test_loans.py
```

- `git ls-files` lista los archivos que git sigue, no los que hay en la carpeta: sin `node_modules/`, `.venv/` ni informes de cobertura.
- `grep -vE` quita las líneas que coinciden con la expresión (`-v` invierte; `-E` permite `|`, «o»). Aquí, los lockfiles y `.gitignore`.

Tres suites (`js/`, `python/`, `go/`), un workflow por suite y un archivo de hooks. Es el mapa del inventario.

## Paso 2: contar los tests sin ejecutarlos

Cada herramienta puede listar lo que ejecutaría:

```bash
cd js && pnpm exec jest --listTests && cd ..
# /home/<usuario>/ci-lab/js/src/fees.test.js
cd python && uv run pytest --collect-only -q | tail -1 && cd ..
# 9 tests collected in 0.01s
cd go && go test -list . && cd ..
# TestLendMarksBookAsUnavailable
# TestLendTwiceFails
# TestAddRejectsEmptyTitle
# ok  	example.com/cilab	0.003s
```

| Comando | Qué lista |
|---|---|
| `jest --listTests` | Los **archivos** de test, no los tests |
| `pytest --collect-only -q` | Cada test (con su ruta) y el total en la última línea |
| `go test -list .` | Las funciones `Test...` del paquete, sin ejecutarlas: no necesita PostgreSQL |

Jest no cuenta tests sin ejecutarlos. El número lo da el paso 3.

## Paso 3: ejecutar con cobertura

Con el comando de cada suite, el mismo que usa su workflow más la cobertura:

```bash
cd js && pnpm exec jest --coverage && cd ..
```

```text
----------|---------|----------|---------|---------|-------------------
File      | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s
----------|---------|----------|---------|---------|-------------------
All files |     100 |      100 |     100 |     100 |
 fees.js  |     100 |      100 |     100 |     100 |
----------|---------|----------|---------|---------|-------------------
Test Suites: 1 passed, 1 total
Tests:       9 passed, 9 total
Snapshots:   0 total
Time:        0.299 s, estimated 1 s
```

```bash
cd python && uv run pytest --cov -q && cd ..
```

```text
TOTAL                    15      0     10      0   100%
Required test coverage of 80.0% reached. Total coverage: 100.00%
9 passed in 0.03s
```

```bash
cd go
docker compose up -d --wait
DATABASE_URL=postgres://library:library@127.0.0.1:55445/library go test -count=1 -race -coverprofile=cover.out ./...
# ok  	example.com/cilab	1.091s	coverage: 87.5% of statements
docker compose down -v
rm cover.out
cd ..
```

El tiempo de cada tabla sale de la propia herramienta: `Time` de Jest, `in 0.03s` de pytest y `1.091s` de `go test`. Go tarda más que las otras dos juntas: arranca conexiones a PostgreSQL y compila con `-race`.

Llena la tabla **Suites** de `inventario.md`. La columna «Capas» dice qué prueba cada suite: `js/` y `python/` son unitarias (funciones puras, sin red ni base de datos); `go/` es de integración (tests contra PostgreSQL real).

## Paso 4: dónde corre cada suite

```bash
grep -nE 'run: [a-z]' .github/workflows/*.yml
```

```text
.github/workflows/js.yml:28:      - run: pnpm install --frozen-lockfile
.github/workflows/js.yml:29:      - run: pnpm test
.github/workflows/go.yml:35:        run: test -z "$(gofmt -l .)" || { gofmt -l .; exit 1; }
.github/workflows/go.yml:36:      - run: go vet ./...
.github/workflows/go.yml:65:      - run: go test -count=1 -race -coverprofile=cover.out ./...
.github/workflows/python.yml:39:      - run: uv sync --locked
.github/workflows/python.yml:40:      - run: uv run pytest --cov --cov-report=term --cov-report=xml --cov-report=html
.github/workflows/python.yml:43:        run: uv run coverage report --format=markdown --fail-under=0 >> "$GITHUB_STEP_SUMMARY"
```

`run: [a-z]` encuentra los `run:` de una línea; los de varias líneas (`run: |`) quedan fuera, y el step del umbral de Go es uno de ellos. Ábrelo en `go.yml` para leerlo entero.

```bash
grep 'id:' .pre-commit-config.yaml
```

```text
      - id: trailing-whitespace
      - id: end-of-file-fixer
      - id: check-yaml
      - id: check-added-large-files
      - id: ruff-check
      - id: ruff-format
      - id: gofmt
      - id: go-vet
```

Llena la tabla **Dónde corre cada suite**. `js/` no tiene hook propio: solo los generales de `pre-commit-hooks`.

## Paso 5: los umbrales

```bash
grep -rnE 'fail_under|coverageThreshold|total < ' js/package.json python/pyproject.toml .github/workflows/
```

```text
python/pyproject.toml:24:fail_under = 80
.github/workflows/go.yml:70:          awk -v total="$total" 'BEGIN { if (total < 85) { print "la cobertura no llega al 85 %"; exit 1 } }'
```

Las tres suites, tres criterios distintos:

| Suite | Umbral | Dónde vive | ¿En local? |
|---|---|---|---|
| `js/` | Ninguno en Jest. La puerta `ci-lab` de SonarQube (85 %) solo existe si arrancas SonarQube en tu equipo | — | No |
| `python/` | 80 % con ramas | `python/pyproject.toml` | Sí: `pytest --cov` lo aplica |
| `go/` | 85 % de sentencias | `.github/workflows/go.yml` | No: hay que copiar el `awk` |

`js.yml` ejecuta `pnpm test`, que es `jest` sin `--coverage`: en la CI, la cobertura de JavaScript puede bajar a cero sin que nada se ponga en rojo. Nadie lo decidió así; cada lab de la semana 45 añadió una pieza, y el inventario es la primera vez que se miran juntas.

Llena la tabla **Umbrales**.

## Paso 6: los huecos

Escribe en **Huecos** lo que el inventario ha encontrado. Como mínimo:

- JavaScript no tiene umbral de cobertura, ni en local ni en la CI.
- Los umbrales no coinciden: 80 % en Python, 85 % en Go.
- El umbral de Go vive en el workflow: en tu equipo, `go test` pasa con cualquier cobertura.
- `js/` no tiene lint en el hook de pre-commit.

No los arregles todavía. Arreglarlos es decidir (¿qué umbral?, ¿dónde vive?), y las decisiones son el [lab 02](../lab-02-estrategia-y-readme/README.md).

---

## Comprobación final

```bash
grep -c TODO inventario.md
# 0
git add inventario.md
git commit -m "Add the test inventory"
```

`grep -c` cuenta las líneas que coinciden: 0 quiere decir que no queda ningún `TODO`. Ojo: con 0 coincidencias, `grep` termina con código 1, aunque el resultado sea el que buscas.

## Qué has aprendido

- El inventario sale de ejecutar: cada herramienta lista sus tests (`--listTests`, `--collect-only`, `-list`) y da su cobertura y su tiempo.
- `git ls-files` y `grep` sobre los workflows y los hooks dicen qué corre y dónde, sin abrir archivo por archivo.
- Las piezas añadidas una a una no forman una estrategia: tres suites, tres umbrales y una sin ninguno.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Estrategia y README](../lab-02-estrategia-y-readme/README.md) → |

[↑ Semana 46](../../README.md)
