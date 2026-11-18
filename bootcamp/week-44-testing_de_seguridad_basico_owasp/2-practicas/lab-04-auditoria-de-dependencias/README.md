# Lab 04 - Auditoría de dependencias

## Objetivo

Auditar cinco proyectos pequeños, uno por lenguaje del curso, cada uno con una dependencia antigua a propósito: encontrar la vulnerabilidad, leer el informe, actualizar y comprobar que la auditoría queda limpia. En Go, distinguir una vulnerabilidad que tu código llama de una que solo está en el módulo.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 04](../../1-teoria/04-cabeceras-zap-y-dependencias.md#3-auditoría-de-dependencias), sección 3.
- pnpm, uv, Go, Rust y Docker, como en las semanas de cada lenguaje.
- Conexión a internet: cada auditor descarga su base de datos de vulnerabilidades.

## Punto de partida

[`starter/`](starter/) tiene cinco carpetas. Ninguna tiene tests: lo que se audita es el manifiesto y el lockfile.

| Carpeta | Dependencia antigua | Auditor |
|---|---|---|
| `js/` | `lodash` 4.17.20 | `pnpm audit` |
| `python/` | `jinja2` 3.1.2 | `uv audit` |
| `java/` | `commons-text` 1.9 | OSV-Scanner con Docker |
| `go/` | `golang.org/x/text` v0.3.7 y `golang.org/x/net` de 2022 | `govulncheck` |
| `rust/` | `smallvec` 1.6.0 | `cargo audit` |

Copia la carpeta para no cambiar la del repositorio:

```bash
cp -r starter /tmp/lab04 && cd /tmp/lab04
```

---

## Paso 1: JavaScript con `pnpm audit`

```bash
cd js
pnpm install
# dependencies:
# + lodash 4.17.20 (4.18.1 is available)
pnpm audit
echo $?
```

`pnpm audit` lee `pnpm-lock.yaml` y consulta la base de datos de avisos de GitHub. Muestra una tabla por aviso (recortada aquí a la primera):

```text
┌─────────────────────┬────────────────────────────────────────────────────────┐
│ high                │ Command Injection in lodash                            │
├─────────────────────┼────────────────────────────────────────────────────────┤
│ Package             │ lodash                                                 │
├─────────────────────┼────────────────────────────────────────────────────────┤
│ Vulnerable versions │ <4.17.21                                               │
├─────────────────────┼────────────────────────────────────────────────────────┤
│ Patched versions    │ >=4.17.21                                              │
├─────────────────────┼────────────────────────────────────────────────────────┤
│ Paths               │ .>lodash                                               │
├─────────────────────┼────────────────────────────────────────────────────────┤
│ More info           │ https://github.com/advisories/GHSA-35jh-r3h4-6jhm      │
└─────────────────────┴────────────────────────────────────────────────────────┘
...
4 vulnerabilities found
Severity: 2 moderate | 2 high
1
```

`Paths` dice por dónde llega el paquete: `.>lodash` es una dependencia directa; una transitiva se vería como `.>paquete>lodash`. Dos opciones para CI:

```bash
pnpm audit --audit-level high > /dev/null; echo $?
# 1
pnpm audit --json | jq -c .metadata.vulnerabilities
# {"info":0,"low":0,"moderate":2,"high":2,"critical":0}
```

`--audit-level high` solo falla con avisos `high` o `critical`; `--json` da un resultado que un script puede leer. Corrige con la última versión:

```bash
pnpm add lodash@4.18.1
pnpm audit
# No known vulnerabilities found
echo $?
# 0
cd ..
```

## Paso 2: Python con `uv audit`

```bash
cd python
uv audit --preview-features audit-command
echo $?
```

```text
Resolved 3 packages in 1ms
Found 5 known vulnerabilities and no adverse project statuses in 2 packages

Vulnerabilities:

jinja2 3.1.2 has 5 known vulnerabilities:

- PYSEC-2026-1474: Jinja vulnerable to HTML attribute injection when passing user input as keys to xmlattr filter

  Fixed in: 3.1.4

  Advisory information: https://nvd.nist.gov/vuln/detail/CVE-2024-34064
...
1
```

Cinco avisos, corregidos en versiones distintas (3.1.3 a 3.1.6). La versión mínima que los corrige todos es la mayor: 3.1.6. Dos de ellos son de XSS por atributos: Jinja escapa el HTML, como `html/template` en el [lab 01](../lab-01-inyeccion-sql-y-xss/README.md), pero hasta 3.1.4 no escapaba las claves que el filtro `xmlattr` convierte en atributos.

En `pyproject.toml`, cambia `"jinja2==3.1.2"` por `"jinja2>=3.1.6"` y vuelve a bloquear:

```bash
uv lock
# Updated jinja2 v3.1.2 -> v3.1.6
uv audit --preview-features audit-command
# Found no known vulnerabilities and no adverse project statuses in 2 packages
cd ..
```

`>=3.1.6` en lugar de `==3.1.6`: el lockfile fija la versión exacta, y el manifiesto dice la mínima aceptable. Así `uv lock --upgrade` puede traer los parches siguientes.

## Paso 3: Java con OSV-Scanner

```bash
cd java
docker run --rm -v "$PWD":/src ghcr.io/google/osv-scanner:v2.7.1 scan source -L /src/pom.xml
echo $?
```

`-v "$PWD":/src` monta la carpeta en el contenedor y `-L` le dice qué archivo leer. OSV-Scanner resuelve las dependencias del `pom.xml` (consultando Maven Central) y busca cada una en OSV:

```text
Total 2 packages affected by 2 known vulnerabilities (1 Critical, 0 High, 1 Medium, 0 Low, 0 Unknown) from 1 ecosystem.
2 vulnerabilities can be fixed.

+-------------------------------------+------+-----------+----------------------------------+---------+---------------+-------------+
| OSV URL                             | CVSS | ECOSYSTEM | PACKAGE                          | VERSION | FIXED VERSION | SOURCE      |
+-------------------------------------+------+-----------+----------------------------------+---------+---------------+-------------+
| https://osv.dev/GHSA-599f-7c49-w659 | 9.8  | Maven     | org.apache.commons:commons-text  | 1.9     | 1.10.0        | src/pom.xml |
| https://osv.dev/GHSA-j288-q9x7-2f5v | 6.5  | Maven     | org.apache.commons:commons-lang3 | 3.11    | 3.18.0        | src/pom.xml |
+-------------------------------------+------+-----------+----------------------------------+---------+---------------+-------------+
1
```

GHSA-599f-7c49-w659 es *Text4Shell* (CVE-2022-42889): `commons-text` evaluaba expresiones dentro del texto que formateaba, y un texto del usuario podía ejecutar código. El **CVSS** es la gravedad de 0 a 10; 9.8 es crítica. La segunda fila no está en el `pom.xml`: `commons-lang3` 3.11 llega como dependencia de `commons-text`. Actualizando la directa se actualiza la transitiva:

```bash
sed -i 's#<version>1.9</version>#<version>1.15.0</version>#' pom.xml
docker run --rm -v "$PWD":/src ghcr.io/google/osv-scanner:v2.7.1 scan source -L /src/pom.xml
# No issues found
cd ..
```

**Experimento: OWASP Dependency-Check.** El auditor clásico de Java es un plugin de Maven; se puede ejecutar sin declararlo en el `pom.xml`:

```bash
mvn -q org.owasp:dependency-check-maven:13.0.0:check
```

Sin clave de la API del NVD falla al descargar la base de datos:

```text
[ERROR] 	UpdateException: Error updating the NVD Data
[ERROR] 		caused by NvdApiException: Invalid API Key, length of 0 too short to provided a masked partial key
```

Si pides la clave (gratuita, en la web del NVD), añade `-DnvdApiKey=TU_CLAVE`. La primera descarga tarda varios minutos.

## Paso 4: Go con `govulncheck`

Instala `govulncheck`, la herramienta oficial del equipo de Go:

```bash
go install golang.org/x/vuln/cmd/govulncheck@v1.10.0
```

`main.go` llama a `language.ParseAcceptLanguage` de `x/text` y usa `html.EscapeString` de `x/net`:

```bash
cd go
govulncheck ./...
echo $?
```

```text
=== Symbol Results ===

Vulnerability #1: GO-2022-1059
    Denial of service via crafted Accept-Language header in
    golang.org/x/text/language
  More info: https://pkg.go.dev/vuln/GO-2022-1059
  Module: golang.org/x/text
    Found in: golang.org/x/text@v0.3.7
    Fixed in: golang.org/x/text@v0.3.8
    Example traces found:
      #1: main.go:14:46: audit.main calls language.ParseAcceptLanguage

Your code is affected by 1 vulnerability from 1 module.
This scan also found 13 vulnerabilities in packages you import and 32
vulnerabilities in modules you require, but your code doesn't appear to call
these vulnerabilities.
Use '-show verbose' for more details.
3
```

Una vulnerabilidad **alcanzable**: la traza dice la línea de tu código que llama a la función vulnerable. Las otras 45 están en paquetes que importas (13) o en módulos que requieres (32), en funciones que no llamas: `x/net/html` tiene vulnerabilidades en su *parser*, y `main.go` solo usa `EscapeString`. Míralas:

```bash
govulncheck -show verbose ./... | grep -E "^Vulnerability|Found in" | head -8
# Vulnerability #1: GO-2022-1059
#     Found in: golang.org/x/text@v0.3.7
# Vulnerability #1: GO-2026-6866
#     Found in: golang.org/x/net@v0.0.0-20220722155237-a158d28d115b
# Vulnerability #2: GO-2026-6860
#     Found in: golang.org/x/net@v0.0.0-20220722155237-a158d28d115b
# Vulnerability #3: GO-2026-6604
#     Found in: os@go1.26.5
```

La última es de la biblioteca estándar (`os@go1.26.5`): `govulncheck` también audita tu versión de Go. Actualiza los dos módulos:

```bash
go get golang.org/x/text@v0.44.0 golang.org/x/net@v0.63.0
# go: upgraded golang.org/x/net v0.0.0-20220722155237-a158d28d115b => v0.63.0
# go: upgraded golang.org/x/text v0.3.7 => v0.44.0
go mod tidy
govulncheck ./...
```

```text
No vulnerabilities found.

Your code is affected by 0 vulnerabilities.
This scan also found 1 vulnerability in packages you import and 22
vulnerabilities in modules you require, but your code doesn't appear to call
these vulnerabilities.
```

Código 0, pero quedan 23: las de Go 1.26.5. La línea `toolchain` de `go.mod` hace que el comando `go` descargue y use otra versión para este módulo:

```bash
go get toolchain@go1.26.11
# go: added toolchain go1.26.11
govulncheck ./...
# No vulnerabilities found.
cd ..
```

La primera vez, `go` descarga Go 1.26.11. En tu proyecto, esta es la corrección de las vulnerabilidades de la biblioteca estándar.

## Paso 5: Rust con `cargo audit`

```bash
cargo install --locked cargo-audit@0.23.0
cd rust
cargo audit
echo $?
```

`cargo install` compila la herramienta (tarda unos minutos). `cargo audit` descarga la base de datos de RustSec a `~/.cargo/advisory-db` y lee `Cargo.lock`:

```text
    Fetching advisory database from `https://github.com/RustSec/advisory-db.git`
      Loaded 1533 security advisories (from /home/ergratodev/.cargo/advisory-db)
    Updating crates.io index
    Scanning Cargo.lock for vulnerabilities (2 crate dependencies)
Crate:     smallvec
Version:   1.6.0
Title:     Buffer overflow in SmallVec::insert_many
Date:      2021-01-08
ID:        RUSTSEC-2021-0003
URL:       https://rustsec.org/advisories/RUSTSEC-2021-0003
Severity:  9.8 (critical)
Solution:  Upgrade to >=0.6.14, <1.0.0 OR >=1.6.1 (patched)

error: 1 vulnerability found!
1
```

Un desbordamiento de búfer en Rust: `smallvec` usa código `unsafe`, y ahí el compilador no protege. Corrige el `Cargo.toml` (`smallvec = "1.16"`) y actualiza el lockfile:

```bash
sed -i 's/smallvec = "=1.6.0"/smallvec = "1.16"/' Cargo.toml
cargo update
#     Updating smallvec v1.6.0 -> v1.16.6
cargo audit
echo $?
# 0
cargo run -q
# [1, 2, 3]
cd ..
```

Sin hallazgos, `cargo audit` no imprime nada después de `Scanning Cargo.lock`.

---

## Comprobación final

| Auditor | Antes | Después |
|---|---|---|
| `pnpm audit` | 4 avisos, código 1 | código 0 |
| `uv audit` | 5 avisos, código 1 | código 0 |
| OSV-Scanner | 2 avisos, código 1 | código 0 |
| `govulncheck` | 1 alcanzable, código 3 | código 0, sin avisos de la biblioteca estándar |
| `cargo audit` | 1 aviso, código 1 | código 0 |

Borra la copia: `rm -rf /tmp/lab04`.

## Qué has aprendido

- Cada auditor lee el lockfile del ecosistema y sale con un código distinto de 0 si encuentra algo: es lo que usa un pipeline.
- La corrección casi siempre es actualizar a la versión que dice el aviso. Una dependencia transitiva se corrige actualizando la directa que la trae.
- `govulncheck` separa lo alcanzable de lo que solo está en el módulo, y audita la biblioteca estándar: se corrige con `toolchain` en `go.mod`.
- OWASP Dependency-Check necesita una clave del NVD; OSV-Scanner audita Maven sin clave.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Cabeceras y ZAP baseline](../lab-03-cabeceras-y-zap-baseline/README.md) | [Proyecto — Proyecto - Seguridad de la API del dominio](../../3-proyecto/README.md) → |

[↑ Semana 44](../../README.md)
