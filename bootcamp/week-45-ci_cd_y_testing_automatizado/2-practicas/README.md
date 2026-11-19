# Prácticas - Semana 45

Cuatro labs guiados (4 h en total) sobre el mismo repositorio, [ci-lab/](ci-lab/): una biblioteca con tres proyectos pequeños, uno en JavaScript, uno en Python y uno en Go con PostgreSQL. Lo subes a un repositorio tuyo de GitHub y cada lab le añade algo: un workflow por lenguaje en los labs 01 a 03, y hooks de pre-commit y una quality gate de SonarQube en el lab 04. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Primer workflow](lab-01-primer-workflow/README.md) | Un workflow de JavaScript con pnpm, seguirlo con `gh`, leer el log y ver un check rojo en un *pull request* | 55 min |
| [Lab 02 - Matrix, caché y artifacts](lab-02-matrix-cache-y-artifacts/README.md) | Python en dos versiones con uv, el umbral de cobertura en la CI, artifacts, el resumen del job, `paths` y `concurrency` | 65 min |
| [Lab 03 - PostgreSQL como servicio](lab-03-postgresql-como-servicio/README.md) | Un workflow de Go con dos jobs en paralelo, un contenedor de servicio con *health check* y el umbral de cobertura con `awk` | 50 min |
| [Lab 04 - Hooks y quality gate](lab-04-hooks-y-quality-gate/README.md) | pre-commit con hooks publicados y locales, Husky, y SonarQube con Docker y una puerta que falla | 70 min |

## El repositorio ci-lab

```text
ci-lab/
├── .gitignore
├── js/        # lateFee y describeFee (Jest, pnpm)
├── python/    # can_renew y loan_days (pytest, uv); el umbral de cobertura es del 80 %
└── go/        # Store sobre PostgreSQL (pgx); los tests leen DATABASE_URL
```

Los tres proyectos funcionan en tu equipo antes de empezar:

```bash
cd js && pnpm install && pnpm test && cd ..
# Tests:       7 passed, 7 total

cd python && uv run pytest --cov -q && cd ..
# FAIL Required test coverage of 80.0% not reached. Total coverage: 76.00%
# 7 passed in 0.06s
```

El de Python pasa los tests, pero no llega al umbral: es a propósito, para el lab 02. El de Go necesita PostgreSQL; lo arrancas en el lab 03.

## Preparar el repositorio (antes del lab 01)

Necesitas una cuenta de GitHub y la CLI `gh` (2.105 en estas pruebas).

1. Inicia sesión con `gh`. Abre el navegador para autorizarla:

   ```bash
   gh auth login
   gh auth status
   # ✓ Logged in to github.com account <tu-usuario> (...)
   ```

2. Copia `ci-lab` fuera del bootcamp. Será un repositorio nuevo, con su propio historial:

   ```bash
   cp -r bootcamp/week-45-ci_cd_y_testing_automatizado/2-practicas/ci-lab ~/ci-lab
   cd ~/ci-lab
   git init -b main
   git add .
   git commit -m "Add library projects in JS, Python and Go"
   ```

3. Crea el repositorio en GitHub y sube el commit en un solo comando:

   ```bash
   gh repo create ci-lab --public --source . --push
   # To https://github.com/<tu-usuario>/ci-lab.git
   #  * [new branch]      HEAD -> main
   # branch 'main' set up to track 'origin/main'.
   ```

   `--source .` usa la carpeta actual y `--push` sube la rama. **Público**, por los minutos: en un repositorio público, GitHub Actions no tiene límite de minutos con los runners estándar; en uno privado, la cuenta gratuita tiene una cuota mensual. Los labs ejecutan unos 20 runs cortos.

Trabaja desde `~/ci-lab` todos los labs. Las salidas de ejemplo son del repositorio `ergrato-dev/ci-lab` que se usó para escribirlos: los ids de los runs y las fechas serán otros en el tuyo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Quality gates y hooks de pre-commit](../1-teoria/04-quality-gates-y-hooks-de-pre-commit.md) | [Lab 01 - Primer workflow](lab-01-primer-workflow/README.md) → |

[↑ Semana 45](../README.md)
