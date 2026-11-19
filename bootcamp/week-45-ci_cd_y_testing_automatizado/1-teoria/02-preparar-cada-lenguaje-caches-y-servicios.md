# 02 - Preparar cada lenguaje, cachés y servicios

> Herramientas: las actions `setup-*` de cada lenguaje, `actions/cache` (dentro de ellas) y contenedores de servicio

El runner `ubuntu-24.04` trae muchas herramientas instaladas, pero no las versiones de tu proyecto. Cada workflow empieza igual: descargar el código, instalar la versión exacta del lenguaje que dice el repositorio, recuperar la caché de dependencias y, si la suite lo necesita, arrancar PostgreSQL. Esta teoría recorre los cinco lenguajes con los workflows reales del proyecto del museo.

---

## 🎯 Objetivos

- Elegir la action de cada lenguaje y hacer que lea la versión del propio repositorio.
- Explicar qué guarda una caché, con qué clave y cuándo se guarda.
- Arrancar PostgreSQL en la CI de tres formas: contenedor de servicio, `docker compose` y Testcontainers.
- Pasar configuración y secretos a un job.

---

## 1. Una versión, un sitio

La versión de cada lenguaje ya está en el repositorio desde las semanas en que lo empezaste. El workflow la lee de ahí, en lugar de repetirla:

| Lenguaje | Action | De dónde sale la versión | Caché |
|---|---|---|---|
| JavaScript | `pnpm/action-setup@v6` + `actions/setup-node@v7` | pnpm: `packageManager` de `package.json`. Node: `node-version: 22` | `cache: pnpm` con `cache-dependency-path` apuntando al lockfile |
| Python | `astral-sh/setup-uv@v10.3.3` | `.python-version` (o la entrada `python-version`) | `enable-cache: true` y `cache-dependency-glob` |
| Java | `actions/setup-java@v6` | `distribution: temurin` y `java-version: "21"` | `cache: maven` |
| Go | `actions/setup-go@v7` | `go-version-file: go/go.mod` | La caché de módulos y de compilación va activada por defecto (`cache-dependency-path`) |
| Rust | `rustup` (ya instalado) + `Swatinem/rust-cache@v2` | `rust-toolchain.toml` | `rust-cache`, con `workspaces: rust` |

Lo que cuentan los logs del proyecto:

- **pnpm** cambia a la versión del proyecto aunque traiga otra:

  ```text
  Switching pnpm from v12.17.0 to v12.6.0...
  Successfully updated pnpm to v12.6.0
  ```

- **Go** lee también la línea `toolchain` de `go.mod`. El `go.mod` del museo dice `go 1.26.5` y `toolchain go1.26.11` (la corrección de la [semana 44](../../week-44-testing_de_seguridad_basico_owasp/README.md)), y `setup-go` instala la segunda:

  ```text
  Setup go version spec 1.26.11
  Found in cache @ /opt/hostedtoolcache/go/1.26.11/x64
  go version go1.26.11 linux/amd64
  ```

  `setup-go` define además `GOTOOLCHAIN=local`: el comando `go` no descarga otra versión por su cuenta durante el job.

- **Rust**: el runner trae `rustup` y una versión estable (1.99.1 el día de la prueba). `rustup toolchain install`, sin argumentos y desde la carpeta del proyecto, instala la de `rust-toolchain.toml` con sus componentes:

  ```text
  info: the active toolchain `1.100.0-x86_64-unknown-linux-gnu` has been installed
  info: it's active because: overridden by '/home/runner/work/qa-museo/qa-museo/rust/rust-toolchain.toml'
  ```

- **Herramientas de Rust compiladas** (`cargo-nextest`, `cargo-llvm-cov`): `cargo install` las compilaría en cada job, varios minutos. `taiki-e/install-action@v2` descarga el binario publicado y comprueba su *checksum*:

  ```yaml
  - uses: taiki-e/install-action@v2
    with:
      tool: cargo-nextest@0.9.152,cargo-llvm-cov@0.9.1
  ```

---

## 2. Cachés

Una **caché** guarda carpetas de un job (las dependencias descargadas, la compilación) para restaurarlas en el siguiente. Se guarda con una **clave** que incluye un *hash* del lockfile: si el lockfile cambia, la clave cambia y la caché vieja no se usa.

```text
pnpm cache is not found                        <- primer run: no hay caché
Cache saved with the key: node-cache-Linux-x64-pnpm-106d084e...   <- al terminar el job
Cache restored from key: node-cache-Linux-x64-pnpm-106d084e...    <- run siguiente
```

Tres reglas que se ven en los labs:

1. **La caché se guarda al final del job**, en el step `Post Run ...` de la action. Si el job falla, ese step no se ejecuta. En el lab 02, los runs en rojo no guardaron la caché de uv y el primer run verde aún dijo `No GitHub Actions cache found for key`.
2. **La clave incluye lo que cambia el contenido**: el sistema, la versión del lenguaje, el lockfile. En la matriz del lab 02 cada versión de Python tiene su clave (`...-ubuntu-24.04-3.13-...` y `...-3.14-...`).
3. **La caché acelera, no corrige.** Si la suite pasa con caché y falla sin ella, el error está en la suite. Por eso las dependencias se instalan siempre con `--frozen-lockfile` o `--locked`: la caché solo evita descargas.

Las actions de cada lenguaje usan por dentro `actions/cache`. Solo lo escribirías a mano para algo que ninguna action guarda.

---

## 3. PostgreSQL en la CI

Hay tres formas, y el curso ya usa las tres:

| Forma | Cómo | Quién la usa | Cuándo elegirla |
|---|---|---|---|
| **Contenedor de servicio** | `services:` en el job | Lab 03 (Go) | Una base de datos para todo el job, sin código de arranque en los tests |
| **`docker compose up -d --wait`** | Un step que arranca `compose.yaml` | Rust: `verify.sh` del museo | Ya tienes `compose.yaml` y quieres el mismo PostgreSQL en tu equipo y en la CI |
| **Testcontainers** | Los propios tests arrancan el contenedor | Java, Go (`pgstore`) y Python del museo | Cada test o suite quiere su base de datos; nada que añadir al workflow |

Las tres funcionan porque el runner `ubuntu-24.04` tiene Docker. El log de Java lo muestra:

```text
org.testcontainers.DockerClientFactory   : Testcontainers version: 2.0.5
org.testcontainers.DockerClientFactory   : Docker host IP address is localhost
tc.postgres:18.7-alpine                  : Pulling docker image: postgres:18.7-alpine. ...
```

### El contenedor de servicio

```yaml
jobs:
  test:
    runs-on: ubuntu-24.04
    services:
      postgres:
        image: postgres:18.7-alpine
        env:
          POSTGRES_USER: library
          POSTGRES_PASSWORD: library
          POSTGRES_DB: library
        ports:
          - 5432:5432
        options: >-
          --health-cmd "pg_isready -U library -d library"
          --health-interval 1s
          --health-timeout 3s
          --health-retries 30
    env:
      DATABASE_URL: postgres://library:library@localhost:5432/library
```

| Clave | Para qué |
|---|---|
| `services.postgres.image` | La misma imagen fija de `compose.yaml` |
| `ports: 5432:5432` | Publica el puerto en el runner: los steps llegan por `localhost:5432` |
| `options` | Opciones de `docker create`. Con `--health-cmd`, el job espera a que PostgreSQL acepte conexiones antes del primer step |
| `env` del job | Variables para todos sus steps. Aquí, la URL que lee el test |

GitHub arranca el servicio antes de los steps y lo espera:

```text
##[group]Waiting for all services to be ready
postgres service is starting, waiting 2 seconds before checking again.
postgres service is healthy.
```

Sin `--health-cmd`, el primer step puede empezar antes de que PostgreSQL acepte conexiones y fallar con `connection refused` solo a veces: un test inestable que no es culpa del test.

---

## 4. Configuración y secretos

- **`env`** a nivel de workflow, de job o de step: valores que no son secretos, como la URL de una base de datos que solo existe dentro del job.
- **Secretos** (*Settings → Secrets and variables → Actions*): se leen con `${{ secrets.NOMBRE }}` y GitHub los sustituye por `***` en el log. Un token de SonarQube o de un registro de paquetes va aquí, nunca en el YAML.
- **Lo que GitHub tapa por su cuenta**: en el lab 03, la URL con usuario y contraseña aparece como `DATABASE_URL: ***localhost:5432/library`. No cuentes con ello para proteger nada: un secreto de verdad va en `secrets`.

Los workflows de un *pull request* que viene de un *fork* no reciben los secretos del repositorio. Una suite que los necesita para ejecutarse fallará en los PR de colaboradores externos; es una decisión de seguridad de GitHub, no un error tuyo.

---

## ✅ Resumen

- Cada lenguaje tiene su action, y la versión sale del repositorio: `packageManager`, `.python-version`, `go.mod` (incluido `toolchain`), `rust-toolchain.toml`.
- La caché se guarda al final de un job verde, con una clave que cambia con el lockfile. Acelera; no sustituye a `--frozen-lockfile` ni a `--locked`.
- PostgreSQL en la CI: contenedor de servicio con `--health-cmd`, `docker compose up --wait` o Testcontainers. El runner tiene Docker.
- `env` para configuración; `secrets` para lo secreto.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - CI/CD y anatomía de un workflow](01-ci-cd-y-anatomia-de-un-workflow.md) | [Teoría — 03 - Matrix, artifacts, paralelismo y umbrales](03-matrix-artifacts-paralelismo-y-umbrales.md) → |

[↑ Semana 45](../README.md)
