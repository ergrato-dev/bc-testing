# Lab 03 - Cabeceras y ZAP baseline

## Objetivo

Comprobar en un test las cabeceras de seguridad de cada respuesta, añadirlas con un *middleware*, y ver el antes y el después desde fuera con un escaneo baseline de OWASP ZAP. Al final, comparar el baseline con un escaneo activo contra la API original.

## Tiempo estimado

65 minutos.

## Requisitos

- La [teoría 04](../../1-teoria/04-cabeceras-zap-y-dependencias.md#1-cabeceras-de-seguridad), secciones 1 y 2.
- Los labs [01](../lab-01-inyeccion-sql-y-xss/README.md) y [02](../lab-02-autenticacion-autorizacion-e-idor/README.md) terminados.
- La imagen de ZAP (unos 2 GB; descárgala antes de empezar):

  ```bash
  docker pull zaproxy/zap-stable:2.17.0
  ```

## Punto de partida

[`starter/headers_test.go`](starter/headers_test.go) tiene la lista de cabeceras y dos tests comentados por PASOS.

```bash
cd ../biblioteca-insegura
cp ../lab-03-cabeceras-y-zap-baseline/starter/headers_test.go .
```

---

## Paso 1: ZAP antes de tocar nada

Arranca la API en una terminal (`go run .`) y, en otra, desde una carpeta vacía para los informes:

```bash
mkdir -p /tmp/zap-lab03 && cd /tmp/zap-lab03
docker run --rm --network host -v "$PWD":/zap/wrk:rw zaproxy/zap-stable:2.17.0 \
  zap-baseline.py -t http://localhost:18044 -r zap-report.html
echo $?
```

Las opciones están explicadas en la [teoría 04](../../1-teoria/04-cabeceras-zap-y-dependencias.md#2-owasp-zap). Tarda unos 40 s. La salida tiene una línea `PASS` por cada regla que no encontró nada (62); las que importan son las demás:

```text
Total of 5 URLs
WARN-NEW: Missing Anti-clickjacking Header [10020] x 3
	http://localhost:18044 (200 OK)
	http://localhost:18044/ (200 OK)
	http://localhost:18044/buscar?q=go (200 OK)
WARN-NEW: X-Content-Type-Options Header Missing [10021] x 3
	...
WARN-NEW: Content Security Policy (CSP) Header Not Set [10038] x 3
	...
WARN-NEW: Storable and Cacheable Content [10049] x 3
	...
WARN-NEW: Permissions Policy Header Not Set [10063] x 3
	...
WARN-NEW: Cross-Origin-Embedder-Policy Header Missing or Invalid [90004] x 9
	...
FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 6	WARN-INPROG: 0	INFO: 0	IGNORE: 0	PASS: 62
2
```

Seis avisos, todos de cabeceras, y código de salida 2 (hay `WARN`). Cada aviso lleva el número de la regla entre corchetes y las URL donde lo vio. Abre `zap-report.html` en el navegador: cada aviso explica el riesgo y la solución.

ZAP visitó `/buscar?q=go` y `/buscar?q=ZAP` (rellenó el formulario), pero no dice nada de inyección: el baseline no manda cargas. Lo verás en el paso 5.

## Paso 2: el test de cabeceras

Vuelve a `biblioteca-insegura/`. Descomenta el PASO 1 y los imports. `securityHeaders` es la lista de cabeceras con su valor exacto (la [teoría 04](../../1-teoria/04-cabeceras-zap-y-dependencias.md#1-cabeceras-de-seguridad) explica cada una) y el test pide cinco rutas: dos HTML, una JSON, un 401 y un 404 del `ServeMux`.

```bash
go test -run TestSecurityHeaders . 2>&1 | head -12
# --- FAIL: TestSecurityHeaders (0.05s)
#     --- FAIL: TestSecurityHeaders// (0.01s)
#         headers_test.go:38: X-Content-Type-Options = "", quiero "nosniff"
#         headers_test.go:38: Content-Security-Policy = "", quiero "default-src 'none'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'"
#         headers_test.go:38: Referrer-Policy = "", quiero "no-referrer"
#         headers_test.go:38: Cache-Control = "", quiero "no-store"
#         headers_test.go:38: Permissions-Policy = "", quiero "camera=(), microphone=(), geolocation=()"
#         headers_test.go:38: Cross-Origin-Opener-Policy = "", quiero "same-origin"
#         headers_test.go:38: Cross-Origin-Resource-Policy = "", quiero "same-origin"
#         headers_test.go:38: Cross-Origin-Embedder-Policy = "", quiero "require-corp"
#     --- FAIL: TestSecurityHeaders//buscar?q=go (0.01s)
#         headers_test.go:38: X-Content-Type-Options = "", quiero "nosniff"
```

Cuenta los fallos:

```bash
go test -run TestSecurityHeaders . 2>&1 | grep -c quiero
# 39
```

5 rutas por 8 cabeceras son 40. Falta una: el 404 del `ServeMux` ya trae `X-Content-Type-Options: nosniff`, porque `http.Error` de Go la pone en todas sus respuestas de error.

## Paso 3: el *middleware*, en el sitio correcto

Añade a `main.go`, debajo de `newHandler`, una función que envuelve un handler y pone las cabeceras antes de llamarlo:

```go
// secureHeaders añade a cada respuesta las cabeceras de seguridad antes de que el
// handler escriba nada: también llegan a los 404 y 405 del ServeMux.
func secureHeaders(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		h := w.Header()
		h.Set("X-Content-Type-Options", "nosniff")
		h.Set("Content-Security-Policy", "default-src 'none'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'")
		h.Set("Referrer-Policy", "no-referrer")
		h.Set("Cache-Control", "no-store")
		h.Set("Permissions-Policy", "camera=(), microphone=(), geolocation=()")
		h.Set("Cross-Origin-Opener-Policy", "same-origin")
		h.Set("Cross-Origin-Resource-Policy", "same-origin")
		h.Set("Cross-Origin-Embedder-Policy", "require-corp")
		next.ServeHTTP(w, r)
	})
}
```

Las cabeceras se ponen antes de `next.ServeHTTP`: en cuanto el handler escribe el código de estado, las cabeceras ya se enviaron y añadir más no tiene efecto.

Primero, ponlo donde parece natural, en `main`:

```go
log.Fatal(http.ListenAndServe(addr, secureHeaders(newHandler(pool))))
```

```bash
go vet . && go test -run TestSecurityHeaders . 2>&1 | grep -c quiero
# 39
```

Los mismos 39 fallos. El servidor real tiene las cabeceras, pero los tests llaman a `newHandler` y no pasan por `main`. Deshaz el cambio en `main` y envuelve el `ServeMux` dentro de `newHandler`, en su última línea:

```go
	return secureHeaders(mux)
}
```

```bash
go test -v -run TestSecurityHeaders . | grep -E "^(---|    ---|ok)"
# --- PASS: TestSecurityHeaders (0.04s)
#     --- PASS: TestSecurityHeaders// (0.01s)
#     --- PASS: TestSecurityHeaders//buscar?q=go (0.01s)
#     --- PASS: TestSecurityHeaders//api/books?q=go (0.01s)
#     --- PASS: TestSecurityHeaders//api/loans/1 (0.01s)
#     --- PASS: TestSecurityHeaders//no-existe (0.01s)
# ok  	example.com/biblioteca	0.048s
```

Descomenta el PASO 2: comprueba que la respuesta no dice qué servidor hay detrás (`Server`, `X-Powered-By`). Go no manda ninguna de las dos; el test protege esa propiedad si un día pones un proxy delante.

```bash
go test -run TestNoServerBanner -v . | grep -E "^(---|ok)"
# --- PASS: TestNoServerBanner (0.01s)
# ok  	example.com/biblioteca	0.012s
```

## Paso 4: ZAP después, y el archivo de reglas

Reinicia la API (Ctrl+C y `go run .`) y repite el escaneo del paso 1:

```text
Total of 5 URLs
WARN-NEW: Non-Storable Content [10049] x 4
	http://localhost:18044 (200 OK)
	http://localhost:18044/ (200 OK)
	http://localhost:18044/buscar?q=go (200 OK)
	http://localhost:18044/robots.txt (404 Not Found)
FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 1	WARN-INPROG: 0	INFO: 0	IGNORE: 0	PASS: 67
```

De seis avisos a uno, y no es un problema: la regla 10049 informa de si una respuesta se puede guardar en caché, y ahora dice que no (`Non-Storable Content`), que es lo que pide `Cache-Control: no-store`. En el informe HTML su riesgo es `Informational`.

Para que el escaneo termine con 0, márcala como ignorada en un archivo de reglas, con su motivo en un comentario. Los campos van separados por **tabuladores**; `printf` con `\t` los escribe sin depender de tu editor:

```bash
cd /tmp/zap-lab03
printf '# Informativa: la respuesta no se guarda en caché, que es lo que pide no-store.\n10049\tIGNORE\t(Non-Storable Content)\n' > zap.conf
docker run --rm --network host -v "$PWD":/zap/wrk:rw zaproxy/zap-stable:2.17.0 \
  zap-baseline.py -t http://localhost:18044 -c zap.conf -r zap-report.html
echo $?
```

```text
IGNORE: Non-Storable Content [10049] x 3
	http://localhost:18044 (200 OK)
	http://localhost:18044/buscar?q=go (200 OK)
	http://localhost:18044/robots.txt (404 Not Found)
FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 0	WARN-INPROG: 0	INFO: 0	IGNORE: 1	PASS: 67
0
```

`-c zap.conf` lee el archivo de la carpeta montada. El número de URLs de cada aviso puede cambiar de una ejecución a otra: el *spider* no siempre visita lo mismo en el mismo orden.

## Paso 5: baseline contra activo

El baseline no encontró la inyección ni el XSS del lab 01. Comprueba qué encuentra un escaneo **activo** contra la API original, sin tus correcciones. `git show HEAD:./main.go` imprime el `main.go` tal como está en el repositorio, sin tus cambios. Desde `biblioteca-insegura/`, en otra terminal:

```bash
mkdir -p /tmp/biblioteca-original
cp go.mod go.sum /tmp/biblioteca-original/
git show HEAD:./main.go > /tmp/biblioteca-original/main.go
cd /tmp/biblioteca-original
PORT=18045 go run .
```

Usa la misma base de datos: los labs no cambiaron sus datos.

Y lanza el escaneo completo:

```bash
cd /tmp/zap-lab03
docker run --rm --network host -v "$PWD":/zap/wrk:rw zaproxy/zap-stable:2.17.0 \
  zap-full-scan.py -t http://localhost:18045 -m 1 -r full.html
```

`-m 1` limita el *spider* a un minuto. Tarda casi 2 minutos (1 min 43 s aquí). Además de los avisos de cabeceras:

```text
WARN-NEW: Cross Site Scripting (Reflected) [40012] x 1
	http://localhost:18045/buscar?q=%3C%2Fh1%3E%3CscrIpt%3Ealert%281%29%3B%3C%2FscRipt%3E%3Ch1%3E (200 OK)
WARN-NEW: SQL Injection [40018] x 1
	http://localhost:18045/buscar?q=%27 (500 Internal Server Error)
WARN-NEW: Cross Site Scripting (DOM Based) [40026] x 3
	...
FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 8	WARN-INPROG: 0	INFO: 0	IGNORE: 0	PASS: 135
```

El escaneo activo encontró el XSS cerrando el `</h1>` y escribiendo `scrIpt` con mayúsculas mezcladas, y la inyección por el 500 que provoca una comilla. No encontró el `UNION` con la tabla `staff`, ni el IDOR ni el borrado sin token: no sabe quién es ana, ni que borrar es solo para administradores. Para la API original (Ctrl+C) y borra la copia.

> El escaneo activo manda cientos de peticiones con cargas de ataque y puede cambiar o borrar datos. Solo contra tu equipo, o contra un entorno con permiso escrito.

---

## Comprobación final

```bash
cd <tu clon>/bootcamp/week-44-testing_de_seguridad_basico_owasp/2-practicas/biblioteca-insegura
go vet .
go test .
# ok  	example.com/biblioteca	0.172s
```

La biblioteca ya no es insegura: los tres archivos de tests pasan. Para la API y PostgreSQL (`docker compose down -v`) si no vas a repetir los labs.

## Qué has aprendido

- Las cabeceras se ponen en un *middleware* antes de que el handler escriba, y dentro de la función que prueban los tests: en `main`, los tests no las ven.
- Un test de cabeceras recorre también los errores (401, 404). `http.Error` ya pone `nosniff`.
- ZAP baseline mide la configuración desde fuera: de 6 avisos a 0 con las cabeceras y una regla informativa ignorada, con su motivo en un comentario.
- El escaneo activo encuentra inyecciones evidentes; ninguno de los dos encuentra un IDOR ni un fallo de autorización.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Autenticación, autorización e IDOR](../lab-02-autenticacion-autorizacion-e-idor/README.md) | [Lab 04 - Auditoría de dependencias](../lab-04-auditoria-de-dependencias/README.md) → |

[↑ Semana 44](../../README.md)
