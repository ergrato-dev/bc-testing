# Lab 04 - Hooks y quality gate

## Objetivo

Parar los fallos antes del *push* con hooks de pre-commit (pre-commit en `ci-lab` y Husky en un proyecto solo de JavaScript), y evaluar la suite de JavaScript con una quality gate de SonarQube que falla cuando la cobertura no llega.

## Tiempo estimado

70 minutos.

## Requisitos

- La [teoría 04](../../1-teoria/04-quality-gates-y-hooks-de-pre-commit.md).
- Los labs 01 a 03 terminados. Docker en tu equipo, con unos 3 GB libres para las imágenes de SonarQube.
- Descarga las imágenes antes de empezar; tardan unos minutos:

  ```bash
  docker pull sonarqube:26.11.0.134175-community
  docker pull sonarsource/sonar-scanner-cli:12.4.0.4321_8.4.0
  ```

## Punto de partida

```bash
cd ~/ci-lab
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/lab-04-hooks-y-quality-gate/starter/pre-commit-config.yaml .pre-commit-config.yaml
cp <ruta-del-bootcamp>/bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/lab-04-hooks-y-quality-gate/starter/sonar-project.properties js/
```

El primero empieza por punto: es un archivo oculto de la raíz, como `.gitignore`.

---

## Parte A: pre-commit

### Paso 1: instalar pre-commit

pre-commit es una herramienta de Python. Con uv se instala una vez para tu usuario, en su propio entorno:

```bash
uv tool install pre-commit@4.7.0
# Installed 1 executable: pre-commit
pre-commit --version
# pre-commit 4.7.0
```

`uv tool install` deja el comando `pre-commit` en `~/.local/bin`, como `pipx`. No va en las dependencias de ningún proyecto: es una herramienta del repositorio entero.

### Paso 2: hooks publicados

Descomenta el PASO 2 de `.pre-commit-config.yaml` y su `(sigue)`, sin el PASO 3 ni el PASO 4. Son dos repositorios de hooks:

- `pre-commit-hooks`: espacios al final de línea, fin de archivo, YAML válido y archivos grandes.
- `ruff-pre-commit`: `ruff check` y `ruff format` sobre los archivos de Python.

Instala el hook de git y ejecútalo sobre todo el repositorio:

```bash
pre-commit install
# pre-commit installed at .git/hooks/pre-commit
pre-commit run --all-files
```

La primera vez, pre-commit descarga cada repositorio de hooks en `~/.cache/pre-commit` (unos segundos). Dos hooks fallan:

```text
check yaml...............................................................Failed
- hook id: check-yaml
- exit code: 1

expected a single document in the stream
  in "js/pnpm-lock.yaml", line 2, column 1
but found another document
  in "js/pnpm-lock.yaml", line 158, column 1
```

```text
ruff check...............................................................Failed
- hook id: ruff-check
- exit code: 1

I001 [*] Import block is un-sorted or un-formatted
 --> python/tests/test_loans.py:1:1
  |
1 | / import pytest
2 | |
3 | | from library.loans import can_renew, loan_days
  | |______________________________________________^
help: Organize imports
```

Ninguno es un error del código:

- El lockfile de pnpm 12 tiene dos documentos YAML en un archivo, separados por `---`. Es válido; `check-yaml` solo acepta uno si no se le dice otra cosa.
- ruff se ejecuta desde la raíz del repositorio. `python/pyproject.toml` no tiene sección `[tool.ruff]`, así que ruff no lo usa como configuración, no sabe que `library` es código propio y lo trata como un paquete de terceros: quiere juntarlo con `pytest` en el mismo bloque de imports.

### Paso 3: arreglar la configuración, no el código

1. Descomenta el PASO 3: `args: [--allow-multiple-documents]` en `check-yaml`.
2. Añade al final de `python/pyproject.toml`:

   ```toml

   [tool.ruff]
   # Con esta sección, ruff toma python/ como raíz del proyecto aunque se ejecute desde la raíz
   # del repositorio (pre-commit): library es código propio y va en su propio bloque de imports.
   src = ["."]
   ```

```bash
pre-commit run --all-files
# trim trailing whitespace.................................................Passed
# fix end of files.........................................................Passed
# check yaml...............................................................Passed
# check for added large files..............................................Passed
# ruff check...............................................................Passed
# ruff format..............................................................Passed
```

### Paso 4: hooks locales de Go

Descomenta el PASO 4. `repo: local` con `language: system` usa el `gofmt` y el `go` que ya tienes. `files` limita cada hook a los archivos de Go; `pass_filenames: false` hace que `go vet` no reciba la lista de archivos del commit, porque analiza paquetes.

Haz commit de la configuración. Ahora el hook se ejecuta solo, sobre los archivos del commit:

```bash
git add .pre-commit-config.yaml python/pyproject.toml
git commit -m "Add pre-commit hooks"
# trim trailing whitespace.................................................Passed
# fix end of files.........................................................Passed
# check yaml...............................................................Passed
# check for added large files..............................................Passed
# ruff check...........................................(no files to check)Skipped
# ruff format..........................................(no files to check)Skipped
# gofmt................................................(no files to check)Skipped
# go vet...............................................(no files to check)Skipped
```

`Skipped`: el commit no toca archivos de Python ni de Go.

### Paso 5: un commit que el hook para

Desalinea una línea de `go/store.go` como lo haría un editor sin `gofmt`:

```go
var ErrEmptyTitle    = errors.New("empty title")
```

```bash
git commit -am "Align ErrEmptyTitle"
echo $?
# 1
```

```text
gofmt....................................................................Failed
- hook id: gofmt
- files were modified by this hook

go/store.go

go vet...................................................................Passed
```

El commit no se creó. `gofmt -w` ya corrigió el archivo, así que `git status` no muestra cambios: lo que querías subir era un error de formato y ha desaparecido. Si el hook arregla algo que sí querías cambiar, revisa el diff (`git diff`), añádelo y vuelve a hacer commit.

Sube la configuración (`git push`). Los workflows de Python y de JavaScript se ejecutan por el cambio en `python/pyproject.toml` y siguen en verde.

---

## Parte B: Husky en un proyecto de JavaScript

Husky y pre-commit no conviven en el mismo repositorio ([teoría 04](../../1-teoria/04-quality-gates-y-hooks-de-pre-commit.md#husky-y-pre-commit-en-el-mismo-repositorio-no)), así que Husky va en una copia de `js/` como repositorio aparte.

### Paso 6: instalar Husky

```bash
cp -r ~/ci-lab/js ~/husky-demo
cd ~/husky-demo
rm -rf node_modules coverage sonar-project.properties
printf 'node_modules/\ncoverage/\n' > .gitignore
git init -b main
pnpm install
git add .
git commit -m "Library fees"
pnpm add -D husky@9.1.7
# devDependencies:
# + husky 9.1.7
pnpm exec husky init
cat .husky/pre-commit
# pnpm test
git config core.hooksPath
# .husky/_
git add .
git commit -m "Add Husky"
```

`husky init` hace tres cosas: crea `.husky/pre-commit` con el test del proyecto, añade `"prepare": "husky"` a los `scripts` de `package.json` (pnpm lo ejecuta en cada `pnpm install`, así que quien clone el repositorio tendrá el hook) y apunta `core.hooksPath` de git a `.husky/_`. El commit "Add Husky" ya pasa por el hook: verás la salida de Jest.

### Paso 7: un commit con un test roto

En `src/fees.js`, baja el tope: `const MAX_FEE = 8000;`.

```bash
git commit -am "Lower the fee cap"
echo $?
# 1
```

```text
Tests:       2 failed, 5 passed, 7 total
[ELIFECYCLE] Test failed. See above for more details.
husky - pre-commit script failed (code 1)
```

Ahora sáltalo:

```bash
git commit --no-verify -am "Lower the fee cap"
git log --oneline -1
# 93c2fb3 Lower the fee cap
```

El commit roto existe. Por eso la CI repite lo que hacen los hooks.

### Paso 8: pre-commit no se instala aquí

```bash
cp ~/ci-lab/.pre-commit-config.yaml .
pre-commit install
# [ERROR] Cowardly refusing to install hooks with `core.hooksPath` set.
# hint: `git config --unset-all core.hooksPath`
```

Deja `~/husky-demo` como está; no se sube a ningún sitio. Vuelve a `~/ci-lab`.

---

## Parte C: quality gate con SonarQube

### Paso 9: arrancar SonarQube

```bash
docker run -d --name sonarqube -p 127.0.0.1:9000:9000 \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  sonarqube:26.11.0.134175-community
```

- `-p 127.0.0.1:9000:9000`: solo accesible desde tu equipo.
- `SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true`: SonarQube lleva dentro un Elasticsearch que en Linux exige `vm.max_map_count` de 524288 o más; WSL2 y muchas distribuciones traen 65530. La variable desactiva esa comprobación, válido para una instancia de pruebas; en un servidor de verdad se sube el valor con `sysctl`.

Tarda alrededor de un minuto. Espera a que diga `UP`:

```bash
until curl -s localhost:9000/api/system/status | grep -q '"status":"UP"'; do sleep 3; done
curl -s localhost:9000/api/system/status
# {"id":"147B411E-...","version":"26.11.0.134175","status":"UP"}
```

### Paso 10: contraseña y token

El usuario inicial es `admin` con contraseña `admin`, y SonarQube obliga a cambiarla. Puedes hacerlo entrando en http://localhost:9000, o con la API:

```bash
curl -s -u admin:admin -X POST localhost:9000/api/users/change_password \
  -d login=admin -d previousPassword=admin -d password='<tu-contraseña>' -w '%{http_code}\n'
# 204
```

El escáner no usa la contraseña sino un **token de análisis**:

```bash
curl -s -u 'admin:<tu-contraseña>' -X POST localhost:9000/api/user_tokens/generate \
  -d name=ci-lab -d type=GLOBAL_ANALYSIS_TOKEN
# {"login":"admin","name":"ci-lab","token":"sqa_5ca0...","createdAt":"...","type":"GLOBAL_ANALYSIS_TOKEN"}
export SONAR_TOKEN=sqa_5ca0...
```

Copia el token entero. En una CI, este valor iría en un secreto del repositorio.

### Paso 11: el primer análisis

`js/sonar-project.properties` dice qué analizar: el código de `src/` sin los tests, los tests aparte, y dónde está el informe de cobertura de Jest (`coverage/lcov.info`). SonarQube no ejecuta los tests, así que primero genera el informe:

```bash
cd js && pnpm test:coverage && cd ..
# All files |   66.66 |    66.66 |      50 |   66.66 |
```

El escáner corre en Docker, con la raíz del repositorio montada (para que vea el historial de git) y `js` como carpeta del proyecto:

```bash
docker run --rm --network host -v "$PWD:/usr/src" \
  -e SONAR_HOST_URL=http://localhost:9000 -e SONAR_TOKEN \
  sonarsource/sonar-scanner-cli:12.4.0.4321_8.4.0 \
  -Dsonar.projectBaseDir=/usr/src/js -Dsonar.qualitygate.wait=true
```

- `--network host`: el contenedor llega a `localhost:9000` de tu equipo.
- `-e SONAR_TOKEN` sin valor pasa la variable que exportaste.
- `-Dsonar.qualitygate.wait=true`: esperar al resultado de la puerta y terminar con error si falla.

Las últimas líneas:

```text
INFO  QUALITY GATE STATUS: PASSED - View details on http://localhost:9000/dashboard?id=ci-lab-js
INFO  EXECUTION SUCCESS
```

Pasa, con un 66.7 % de cobertura:

```bash
curl -s -u 'admin:<tu-contraseña>' "localhost:9000/api/measures/component?component=ci-lab-js&metricKeys=coverage,code_smells,ncloc" | jq -c .component.measures
# [{"metric":"coverage","value":"66.7","bestValue":false},{"metric":"code_smells","value":"0","bestValue":true},{"metric":"ncloc","value":"19"}]
```

La puerta Sonar way solo tiene condiciones sobre el código nuevo, y en el primer análisis no hay código nuevo ([teoría 04](../../1-teoria/04-quality-gates-y-hooks-de-pre-commit.md#2-código-nuevo-y-código-total)). Abre el proyecto en http://localhost:9000 y mira la pestaña *Overall Code*: la cobertura y las líneas sin cubrir de `fees.js`.

### Paso 12: una puerta propia

Crea la puerta `ci-lab` con una condición sobre todo el código y asígnala al proyecto. En la web: *Quality Gates → Create*, *Add Condition → On Overall Code → Coverage, is less than 85*, y en el proyecto, *Project Settings → Quality Gate*. Con la API:

```bash
A='admin:<tu-contraseña>'
curl -s -u "$A" -X POST localhost:9000/api/qualitygates/create -d name=ci-lab
# {"name":"ci-lab"}
curl -s -u "$A" -X POST localhost:9000/api/qualitygates/create_condition \
  -d gateName=ci-lab -d metric=coverage -d op=LT -d error=85
# {"id":"e6d93f18-...","metric":"coverage","op":"LT","error":"85"}
curl -s -u "$A" -X POST localhost:9000/api/qualitygates/select \
  -d gateName=ci-lab -d projectKey=ci-lab-js -w '%{http_code}\n'
# 204
```

`op=LT` es "menor que": la condición falla si la cobertura es menor que 85. Repite el análisis (el mismo `docker run` del paso 11):

```text
ERROR QUALITY GATE STATUS: FAILED - View details on http://localhost:9000/dashboard?id=ci-lab-js
INFO  EXECUTION FAILURE
```

```bash
echo $?
# 3
```

Código 3: en un workflow, el job se pondría en rojo.

### Paso 13: verde

`describeFee` no tiene tests. Añádelos al final de `js/src/fees.test.js` y cambia el `require` de la primera línea:

```js
const { lateFee, describeFee } = require('./fees');
```

```js

describe('describeFee', () => {
  test('says there is no fee when the book is on time', () => {
    expect(describeFee(0)).toBe('Sin multa');
  });

  test('shows the amount when the book is late', () => {
    expect(describeFee(7)).toBe('Multa: 3500');
  });
});
```

```bash
cd js && pnpm test:coverage && cd ..
# All files |     100 |      100 |     100 |     100 |
# Tests:       9 passed, 9 total
```

Repite el análisis:

```text
INFO  QUALITY GATE STATUS: PASSED - View details on http://localhost:9000/dashboard?id=ci-lab-js
INFO  EXECUTION SUCCESS
```

Haz commit y súbelo; el hook y el workflow de JavaScript pasan:

```bash
git add js
git commit -m "Test describeFee and add the SonarQube configuration"
git push
```

Cuando termines, para SonarQube: `docker rm -f sonarqube`. Los datos del análisis estaban dentro del contenedor y se borran con él.

---

## Experimento: sin `qualitygate.wait`

Con la puerta en rojo (antes del paso 13), repite el análisis sin `-Dsonar.qualitygate.wait=true`:

```text
INFO  ANALYSIS SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=ci-lab-js
INFO  More about the report processing at http://localhost:9000/api/ce/task?id=50bcd06a-...
INFO  EXECUTION SUCCESS
```

Código 0, y la puerta, consultada unos segundos después:

```bash
curl -s -u "$A" "localhost:9000/api/qualitygates/project_status?projectKey=ci-lab-js" | jq .projectStatus.status
# "ERROR"
```

El escáner sube el análisis y termina; SonarQube lo procesa después. Sin la opción, un pipeline seguiría en verde con la puerta en rojo.

## Comprobación final

```bash
pre-commit run --all-files
```

Todos los hooks en `Passed`. Si haces `git log --oneline -3` en `~/ci-lab`, ves el commit de SonarQube y el de los hooks; el run del workflow de JavaScript del último *push* está en verde.

## Qué has aprendido

- pre-commit ejecuta hooks publicados (con versión fija en `rev`) y locales (`language: system`) solo sobre los archivos que cambian. La primera pasada con `--all-files` encuentra problemas de configuración, no solo de código.
- Un hook que modifica archivos también para el commit. `--no-verify` se lo salta.
- Husky es el equivalente en JavaScript, y no se instala junto a pre-commit.
- SonarQube evalúa una puerta con la cobertura que generan tus tests. Sonar way solo mira el código nuevo; para todo el código, una condición propia. `sonar.qualitygate.wait=true` convierte la puerta en un código de salida.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - PostgreSQL como servicio](../lab-03-postgresql-como-servicio/README.md) | [Proyecto — Proyecto - CI de las cinco suites](../../3-proyecto/README.md) → |

[↑ Semana 45](../../README.md)
