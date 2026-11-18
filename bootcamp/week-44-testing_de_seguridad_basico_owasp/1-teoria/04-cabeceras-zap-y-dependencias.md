# 04 - Cabeceras, ZAP y auditoría de dependencias

> Herramientas: **OWASP ZAP** 2.17.0 (imagen `zaproxy/zap-stable:2.17.0`), `pnpm audit`, `uv audit`, **OSV-Scanner** 2.7.1, `govulncheck` 1.10.0 y `cargo audit` 0.23.0

Las teorías 02 y 03 prueban tu código. Esta prueba lo que lo rodea: la configuración de las respuestas HTTP, la aplicación vista desde fuera por un escáner y el código de otros que usas (tus dependencias).

---

## 🎯 Objetivos

- Saber qué hace cada cabecera de seguridad y comprobarlas en un test de integración.
- Ejecutar un escaneo baseline de ZAP con Docker, leer sus avisos y distinguirlo de un escaneo activo.
- Auditar las dependencias de los cinco lenguajes del curso y corregir lo que encuentran.
- Decidir qué hace fallar un pipeline y cómo se ignora un hallazgo con motivo.

---

## 1. Cabeceras de seguridad

Las cabeceras de seguridad son instrucciones al navegador: qué puede cargar la página, si se puede incrustar en otra, qué se guarda en caché. No arreglan un XSS, pero limitan lo que un XSS puede hacer. Son A02 (configuración).

| Cabecera | Valor en los labs | Qué evita |
|---|---|---|
| `X-Content-Type-Options` | `nosniff` | Que el navegador "adivine" el tipo: un JSON con `<script>` interpretado como HTML |
| `Content-Security-Policy` (CSP) | `default-src 'none'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'` | Cargar scripts, estilos o imágenes que la página no declara; un XSS no puede traer código de fuera. `frame-ancestors 'none'` impide incrustar la página en un `<iframe>` de otro sitio (*clickjacking*) |
| `Cache-Control` | `no-store` | Que una respuesta con datos de un usuario quede guardada en una caché compartida o en el disco |
| `Referrer-Policy` | `no-referrer` | Que la URL (con sus ids y búsquedas) viaje a otros sitios en la cabecera `Referer` |
| `Permissions-Policy` | `camera=(), microphone=(), geolocation=()` | Que la página, o un script inyectado, pida la cámara, el micrófono o la ubicación |
| `Cross-Origin-Opener-Policy`, `-Resource-Policy`, `-Embedder-Policy` | `same-origin`, `same-origin`, `require-corp` | Que otra web abra o cargue tus respuestas y lea datos con ataques de canal lateral (Spectre) |
| `Strict-Transport-Security` (HSTS) | No en los labs | Que el navegador use HTTP sin cifrar. Solo tiene sentido con HTTPS |

La CSP de una API JSON puede ser la más estricta (`default-src 'none'; frame-ancestors 'none'`): no sirve páginas. La de la biblioteca necesita `form-action 'self'` para que el buscador pueda enviar el formulario. `base-uri` no hereda de `default-src`, por eso va aparte: ZAP lo avisa si falta.

### El test

Las cabeceras se ponen en un *middleware*: una función que envuelve al `ServeMux` y añade las cabeceras antes de que el handler escriba la respuesta. El test recorre una petición de cada tipo, también los errores:

```go
paths := []string{
	"/",               // HTML, 200
	"/api/books?q=go", // JSON, 200
	"/api/loans/1",    // JSON, 401: sin token
	"/no-existe",      // 404 del ServeMux
}
```

Los errores importan: un 404 sin `nosniff` es tan explotable como un 200. Y el *middleware* tiene que estar donde el test lo ve. Si lo pones en `main` (`http.ListenAndServe(addr, secureHeaders(newHandler(pool)))`), el servidor real lo tiene pero los tests, que llaman a `newHandler`, no: siguen en rojo. Va dentro de `newHandler`.

---

## 2. OWASP ZAP

**ZAP** (*Zed Attack Proxy*) es el escáner DAST gratuito de OWASP. Se ejecuta con Docker y trae scripts para los usos habituales:

| Script | Qué hace | Tiempo en el lab |
|---|---|---|
| `zap-baseline.py` | Recorre la aplicación con un *spider* (sigue los enlaces) durante 1 minuto como máximo y analiza **pasivamente** las respuestas: no manda nada que un usuario normal no mandaría | ~40 s |
| `zap-full-scan.py` | Lo mismo y después un escaneo **activo**: manda cargas de inyección, XSS, rutas de archivos. Puede cambiar o borrar datos | ~1 min 45 s en la biblioteca, horas en una aplicación grande |
| `zap-api-scan.py` | Escaneo activo guiado por una especificación OpenAPI o GraphQL | Necesita la especificación |

```bash
docker run --rm --network host -v "$PWD":/zap/wrk:rw zaproxy/zap-stable:2.17.0 \
  zap-baseline.py -t http://localhost:18044 -r zap-report.html
```

- `--network host`: el contenedor comparte la red de tu equipo y ve la API en `localhost`.
- `-v "$PWD":/zap/wrk:rw`: la carpeta actual es la de trabajo de ZAP; ahí escribe el informe y ahí lee su configuración.
- `-t`: la URL desde la que empieza. `-r` el informe HTML, `-J` el JSON.

ZAP clasifica cada regla como `PASS`, `WARN` o `FAIL`, y sale con un código que un script o un pipeline puede usar:

| Código | Significa |
|---|---|
| 0 | Todo `PASS` (o ignorado) |
| 1 | Al menos un `FAIL` |
| 2 | Al menos un `WARN`, ningún `FAIL` |
| 3 | ZAP no pudo terminar: la API no responde, o la configuración no se puede leer |

Por defecto todas las reglas avisan (`WARN`). Un **archivo de reglas** (`-c zap.conf`) cambia lo que hace cada una: `IGNORE`, `INFO`, `WARN` o `FAIL`. Una línea por regla, separada por tabuladores:

```text
# Informativa: avisa de que la respuesta no se guarda en caché, que es lo que pide no-store.
10049	IGNORE	(Non-Storable Content)
```

Las líneas que empiezan por `#` son comentarios: el motivo de cada regla ignorada se escribe encima de ella, para que quien lea el archivo sepa por qué.

### Lo que el baseline no encuentra

En el lab 03, el baseline contra la biblioteca con la búsqueda inyectable y la página con XSS solo avisa de cabeceras. Es pasivo: no prueba cargas, así que no puede saber que `q` es inyectable. El escaneo activo (`zap-full-scan.py`) sí encuentra `Cross Site Scripting (Reflected) [40012]` y `SQL Injection [40018]`. Pero tampoco encuentra el IDOR: no sabe que el préstamo 1 es de ana.

---

## 3. Auditoría de dependencias

Tus dependencias son código de otros que se ejecuta con tus permisos. Cada lenguaje tiene un auditor que lee el lockfile (las versiones exactas) y las compara con una base de datos de vulnerabilidades publicadas:

| Lenguaje | Comando | Base de datos | Código de salida con hallazgos |
|---|---|---|---|
| JavaScript | `pnpm audit` | GitHub Advisory Database | 1 |
| Python | `uv audit --preview-features audit-command` | OSV (PyPI) | 1 |
| Java | OSV-Scanner con Docker (`scan source -L pom.xml`) | OSV (Maven) | 1 |
| Go | `govulncheck ./...` | Go Vulnerability Database | 3 |
| Rust | `cargo audit` | RustSec Advisory Database | 1 |

**OSV** (*Open Source Vulnerabilities*, osv.dev) es una base de datos abierta de Google que agrupa las de cada ecosistema. Cada aviso dice qué versiones están afectadas y desde cuál está corregido. La corrección casi siempre es actualizar a esa versión.

### Python: `uv audit`, no `pip-audit`

`pip-audit` es el auditor clásico de Python. uv 0.13 trae el suyo, `uv audit`, que lee `uv.lock` directamente. Todavía es experimental: sin `--preview-features audit-command` avisa `uv audit is experimental and may change without warning`.

### Java: OWASP Dependency-Check y OSV-Scanner

**OWASP Dependency-Check** es el auditor clásico de Java: un plugin de Maven (`org.owasp:dependency-check-maven`) que descarga la base de datos del NVD (*National Vulnerability Database*, la de los CVE del gobierno de EE. UU.). Desde la versión 12 necesita una clave de la API del NVD; sin ella, la versión 13.0.0 no arranca:

```text
[ERROR] 	UpdateException: Error updating the NVD Data
[ERROR] 		caused by NvdApiException: Invalid API Key, length of 0 too short to provided a masked partial key
```

La clave es gratuita y se pide con un correo en la web del NVD; la primera descarga tarda varios minutos. Si la tienes, `mvn org.owasp:dependency-check-maven:13.0.0:check -DnvdApiKey=TU_CLAVE` hace la auditoría. Esta semana usas **OSV-Scanner**, también gratuito y sin clave, con Docker: lee el `pom.xml`, resuelve las dependencias transitivas y consulta OSV.

### Go: `govulncheck` y la alcanzabilidad

`govulncheck` hace algo que los demás no hacen: analiza qué funciones llama tu código. Una vulnerabilidad en una función de `golang.org/x/net/html` que nunca llamas no es lo mismo que una en la que llamas con datos del usuario:

```text
Vulnerability #1: GO-2022-1059
    Denial of service via crafted Accept-Language header in
    golang.org/x/text/language
  ...
    Example traces found:
      #1: main.go:14:46: audit.main calls language.ParseAcceptLanguage

Your code is affected by 1 vulnerability from 1 module.
This scan also found 13 vulnerabilities in packages you import and 32
vulnerabilities in modules you require, but your code doesn't appear to call
these vulnerabilities.
```

Solo la primera hace salir con código 3. Las otras 45 se ven con `-show verbose`. Algunas son de la **biblioteca estándar**: `govulncheck` también audita la versión de Go con la que compilas. Go 1.26.5, la versión fijada del curso, tiene vulnerabilidades corregidas en Go 1.26.11. Se corrige con una línea en `go.mod`, que hace que `go` descargue y use esa versión:

```bash
go get toolchain@go1.26.11
# go: added toolchain go1.26.11
```

### Las versiones fijadas también envejecen

El curso fija versiones para que todo el mundo obtenga las mismas salidas ([stack-versions](../../../docs/stack-versions.md)). Una versión que era la última cuando se fijó tiene CVE meses después: en noviembre de 2026, Spring Boot 4.1.1 trae un Tomcat y un Jackson con vulnerabilidades corregidas en Spring Boot 4.1.2. Encontrarlo es justo para lo que sirve la auditoría, y por eso se repite cada día y no solo cuando cambias una dependencia.

---

## 4. Seguridad en CI

Los tres tipos de prueba encajan en un pipeline (lo montarás en la [semana 45](../../../docs/plan-estudios.md)):

| Prueba | Cuándo | Falla el pipeline si |
|---|---|---|
| Tests de seguridad | En cada push, con los demás tests | Un test falla |
| Auditoría de dependencias | En cada push y en una ejecución diaria | Hay un hallazgo no ignorado (código de salida distinto de 0) |
| ZAP baseline | Contra el entorno de pruebas desplegado | Hay un `FAIL`; los `WARN` se revisan |

Ignorar un hallazgo es legítimo cuando no te afecta, pero siempre con motivo y con fecha: `uv audit --ignore-until-fixed ID` lo ignora mientras no haya versión corregida, `cargo audit --ignore RUSTSEC-...` y la regla `IGNORE` de ZAP lo ignoran siempre. Un pipeline que falla cada día por lo mismo acaba desactivado, y un hallazgo ignorado sin motivo nadie lo vuelve a mirar.

---

## ✅ Resumen

- Las cabeceras de seguridad limitan lo que puede hacer un ataque en el navegador. Se ponen en un *middleware* dentro del handler que prueban los tests, y se comprueban también en los errores.
- ZAP baseline es pasivo y rápido: encuentra configuración, no inyecciones. El escaneo activo encuentra inyecciones, y ninguno encuentra un IDOR. Códigos de salida: 0, 1 (`FAIL`), 2 (`WARN`) y 3 (error).
- Cada lenguaje tiene su auditor de dependencias; `govulncheck` además mira qué funciones llamas y audita la biblioteca estándar.
- OWASP Dependency-Check necesita una clave del NVD; OSV-Scanner audita Maven sin clave.
- En CI, los tests y las auditorías fallan el pipeline; un hallazgo se ignora con motivo y con fecha.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Autenticación, autorización e IDOR](03-autenticacion-autorizacion-e-idor.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 44](../README.md)
