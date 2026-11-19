# 04 - Quality gates y hooks de pre-commit

> Herramientas: **SonarQube Community Build** con Docker, **Husky** (JavaScript) y **pre-commit** (todos los lenguajes)

La CI para un fallo cuando ya está subido. Esta teoría mira a los dos lados del workflow: antes, los **hooks** paran en tu equipo lo que se arregla en segundos (formato, lint, un test roto); después, una **quality gate** decide con varias condiciones a la vez si el código está para mezclarse.

---

## 🎯 Objetivos

- Explicar qué es una quality gate y qué mide SonarQube.
- Distinguir las condiciones sobre el código nuevo de las condiciones sobre todo el código, y por qué la primera puerta de un proyecto pasa casi siempre.
- Hacer que un análisis falle cuando la puerta falla.
- Instalar hooks de pre-commit con Husky y con pre-commit, y saber por qué no conviven en el mismo repositorio.

---

## 1. Quality gate

Una **quality gate** (puerta de calidad) es una lista de condiciones que el código tiene que cumplir: cobertura mínima, ningún bug nuevo, poca duplicación. El umbral de cobertura de la [teoría 03](03-matrix-artifacts-paralelismo-y-umbrales.md) es una puerta de una sola condición. **SonarQube** analiza el código, guarda las medidas de cada análisis y evalúa una puerta con varias.

| Lo que mide | Ejemplo |
|---|---|
| Bugs y vulnerabilidades | Una comparación que siempre es verdadera, una contraseña escrita en el código |
| *Code smells* | Una función demasiado compleja, código muerto |
| *Security hotspots* | Código que un humano tiene que revisar (una expresión regular, una petición HTTP sin TLS) |
| Duplicación | Bloques copiados |
| Cobertura | La lee del informe de tus tests (`lcov.info`, `coverage.xml`, `jacoco.xml`): SonarQube no ejecuta los tests |

### Dos ediciones

| | SonarQube Community Build | SonarQube Cloud |
|---|---|---|
| Dónde | Tu servidor, con Docker | Servicio de SonarSource |
| Precio | Gratis | Gratis para repositorios públicos |
| En el curso | Lab 04: `sonarqube:26.11.0.134175-community` en `localhost:9000` | No se usa: necesita una cuenta |

Una instancia en tu equipo no es accesible desde los runners de GitHub. Para usar SonarQube en la CI, el servidor tiene que estar en internet (el de tu empresa, o SonarQube Cloud) y el token va en un secreto del repositorio. La action `SonarSource/sonarqube-scan-action` ejecuta el mismo escáner que el lab 04.

---

## 2. Código nuevo y código total

La puerta que trae SonarQube, **Sonar way**, tiene cuatro condiciones, todas sobre el **código nuevo**:

```json
[{"metric":"new_violations","op":"GT","error":"0"},
 {"metric":"new_coverage","op":"LT","error":"80"},
 {"metric":"new_duplicated_lines_density","op":"GT","error":"3"},
 {"metric":"new_security_hotspots_reviewed","op":"LT","error":"100"}]
```

El código nuevo es el que cambió desde una referencia (la versión anterior, o los últimos 30 días). La idea es *Clean as You Code*: no exigir que un proyecto antiguo llegue al 80 % de golpe, sino que todo lo que se toque llegue. Lo que se ve en el lab 04:

```text
INFO  QUALITY GATE STATUS: PASSED - View details on http://localhost:9000/dashboard?id=ci-lab-js
```

```json
[{"metric":"coverage","value":"66.7"}]
```

La puerta pasa con un 66.7 % de cobertura. En el primer análisis no hay referencia, así que no hay código nuevo y las condiciones `new_*` no se evalúan. Una puerta verde en el primer análisis no dice nada.

Para exigir algo sobre todo el código, la puerta necesita una condición sin `new_`. En el lab 04 creas la puerta `ci-lab` con la condición "cobertura (`coverage`) menor que 85 = error":

```text
ERROR QUALITY GATE STATUS: FAILED - View details on http://localhost:9000/dashboard?id=ci-lab-js
INFO  EXECUTION FAILURE
```

### Que la puerta pare el pipeline

El escáner sube el análisis y SonarQube lo procesa después. Por defecto, el escáner termina con 0 aunque la puerta falle: el resultado solo se ve en la web. Con `-Dsonar.qualitygate.wait=true`, el escáner espera al resultado y termina con código **3** si la puerta falla. En un workflow, ese código pone el job en rojo, igual que el umbral de cobertura.

---

## 3. Hooks de pre-commit

Un **hook** de git es un programa que git ejecuta en un momento concreto. El de `pre-commit` se ejecuta al hacer `git commit`; si termina con un código distinto de 0, el commit no se crea.

| | Husky | pre-commit |
|---|---|---|
| Para | Proyectos JavaScript | Cualquier lenguaje |
| Se instala con | `pnpm add -D husky@9.1.7` y `pnpm exec husky init` | `uv tool install pre-commit@4.7.0` y `pre-commit install` |
| Configuración | `.husky/pre-commit`: un script de shell | `.pre-commit-config.yaml`: una lista de hooks de repositorios publicados o locales |
| Qué ejecuta | Lo que escribas (`pnpm test`) | Solo los hooks cuyos archivos cambiaron en el commit |

### Husky

`husky init` añade a `package.json` el script `"prepare": "husky"` (pnpm lo ejecuta en cada `pnpm install`, así que el hook se instala solo en el equipo de cada persona) y crea `.husky/pre-commit` con el test del proyecto:

```text
pnpm test
```

Con un test roto, el commit se para:

```text
Tests:       2 failed, 5 passed, 7 total
[ELIFECYCLE] Test failed. See above for more details.
husky - pre-commit script failed (code 1)
```

### pre-commit

```yaml
repos:
  - repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v6.0.0
    hooks:
      - id: trailing-whitespace
      - id: check-yaml
        args: [--allow-multiple-documents]
  - repo: local
    hooks:
      - id: gofmt
        name: gofmt
        entry: gofmt -l -w
        language: system
        files: \.go$
```

Los hooks de un `repo` publicado se descargan con la versión de `rev` y corren en su propio entorno. Los de `repo: local` con `language: system` ejecutan una herramienta que ya tienes instalada (`gofmt`, `cargo`). `files` es una expresión regular: el hook solo se ejecuta si el commit cambia un archivo que coincide.

Un hook que **modifica** archivos (`gofmt -w`, `end-of-file-fixer`) también para el commit: deja el arreglo en tu copia de trabajo para que lo revises y lo añadas.

```text
gofmt....................................................................Failed
- hook id: gofmt
- files were modified by this hook

go/store.go
```

### Lo que se aprende al instalarlos en un repositorio que ya existe

`pre-commit run --all-files` ejecuta los hooks sobre todo el repositorio, no solo lo que cambia. La primera vez encuentra lo que nadie había mirado, y no siempre es un error del código:

- `check-yaml` falla con el lockfile de pnpm 12, que tiene dos documentos YAML en un archivo (`expected a single document in the stream`). Se arregla con `--allow-multiple-documents`.
- `ruff` se ejecuta desde la raíz del repositorio y no encuentra la configuración del proyecto en `python/` si `pyproject.toml` no tiene sección `[tool.ruff]`. Entonces trata tu propio paquete como si fuera de terceros y pide reordenar los imports (`I001`).
- En el museo, la primera pasada modificó seis archivos de Python (espacios al final de línea, formato de `ruff format`) y `ruff check --fix` quitó unas comillas en una anotación de tipo. Los tests siguieron al 100 %; sin ellos, ese cambio sería un acto de fe.

### Por qué los hooks no bastan

- `git commit --no-verify` se salta los hooks, y el commit se crea.
- Un hook solo existe en el equipo de quien lo instaló.

Por eso todo lo que comprueba un hook se repite en la CI. Los hooks son para que el error llegue antes y cueste segundos, no para garantizar nada.

### Husky y pre-commit en el mismo repositorio, no

Husky configura `core.hooksPath=.husky/_`, y git deja de mirar `.git/hooks/`, que es donde instala pre-commit. pre-commit lo detecta y se niega:

```text
[ERROR] Cowardly refusing to install hooks with `core.hooksPath` set.
hint: `git config --unset-all core.hooksPath`
```

En un proyecto solo de JavaScript, Husky es lo natural: va en `devDependencies` y se instala con `pnpm install`. En un repositorio con varios lenguajes, como el del museo, pre-commit los cubre todos con un archivo.

---

## ✅ Resumen

- Una quality gate es una lista de condiciones. SonarQube mide bugs, *smells*, *hotspots*, duplicación y la cobertura que tus tests ya generaron.
- Sonar way solo mira el código nuevo: el primer análisis pasa siempre. Para exigir sobre todo el código, una condición sin `new_`.
- `sonar.qualitygate.wait=true` hace que el escáner termine con 3 si la puerta falla.
- Husky (JS) y pre-commit (cualquier lenguaje) paran el commit en tu equipo. Se saltan con `--no-verify`, así que la CI repite todo. No se instalan los dos en el mismo repositorio.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Matrix, artifacts, paralelismo y umbrales](03-matrix-artifacts-paralelismo-y-umbrales.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 45](../README.md)
