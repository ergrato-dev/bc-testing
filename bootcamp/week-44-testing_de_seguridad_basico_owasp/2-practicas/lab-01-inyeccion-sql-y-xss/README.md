# Lab 01 - Inyección SQL y XSS

## Objetivo

Escribir tests que encuentran la inyección SQL de la búsqueda, el error que enseña el SQL al cliente y el XSS de la página de resultados de la [biblioteca insegura](../README.md#la-api-de-la-biblioteca-insegura), y corregir la API hasta que pasan.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-inyeccion-y-xss.md).
- PostgreSQL de la biblioteca arrancado (`docker compose up -d --wait` en `biblioteca-insegura/`).

## Punto de partida

[`starter/injection_test.go`](starter/injection_test.go) tiene cuatro tests comentados por PASOS. Cada import lleva al final el PASO en que se descomenta.

1. Cópialo a la API:

   ```bash
   cd ../biblioteca-insegura
   cp ../lab-01-inyeccion-sql-y-xss/starter/injection_test.go .
   ```

2. Trabaja desde `biblioteca-insegura/` todo el lab. Los cambios de la API se hacen en `main.go`.

---

## Paso 1: la búsqueda funciona

Descomenta el PASO 1 y los imports `encoding/json`, `net/http` y `testing`. El test busca `go` y espera los 2 libros con "Go" en el título. Antes de probar lo que no debe pasar, comprueba que lo normal funciona: si la búsqueda no funcionara, los tests de inyección fallarían por otro motivo.

```bash
go test -run TestSearchFindsBooks -v .
# === RUN   TestSearchFindsBooks
# --- PASS: TestSearchFindsBooks (0.01s)
# PASS
# ok  	example.com/biblioteca	0.018s
```

## Paso 2: tres cargas de inyección SQL

Descomenta el PASO 2 y el import `net/url`. Es un test de tabla con tres cargas; ningún título las contiene, así que cada una espera 200 y una lista vacía. Cada carga corre en su propio servidor (`newServer`): un error de SQL deja abortada la transacción del test.

```bash
go test -run TestSearchSQLInjection .
# --- FAIL: TestSearchSQLInjection (0.04s)
#     --- FAIL: TestSearchSQLInjection/' (0.01s)
#         injection_test.go:44: status = 500, quiero 200; cuerpo: {"error":"ERROR: unterminated quoted string at or near \"' ORDER BY id\" (SQLSTATE 42601)"}
#     --- FAIL: TestSearchSQLInjection/'_OR_'1'='1 (0.01s)
#         injection_test.go:51: libros = 6, quiero 0: [{"id":1,"title":"The Go Programming Language",...
#     --- FAIL: TestSearchSQLInjection/%'_UNION_SELECT_0,_username,_password_hash_FROM_staff_-- (0.01s)
#         injection_test.go:51: libros = 7, quiero 0: [{"id":3,"title":"The Rust Programming Language",...,{"id":0,"title":"admin","author":"$2a$10$Q7d1Jq2nZb1x0lJm7rC8eu8m1J1cZQyq0Kx3Vv4w1n7a8p5s2t6dK"},...
# FAIL
```

Lee cada fallo (las líneas largas están recortadas con `...`):

- `'`: la comilla sin cerrar rompe la consulta. El 500 enseña además el trozo de SQL que hay detrás (`' ORDER BY id`).
- `' OR '1'='1`: la condición vale para todas las filas, 6 libros.
- `UNION SELECT`: 7 filas. La séptima no es un libro: es el usuario `admin` y el *hash* de su contraseña, sacados de la tabla `staff`.

Corrige `searchBooks` en `main.go`: el texto va como parámetro (`$1`) y la consulta lo concatena con los `%` en SQL.

```go
rows, err := db.Query(ctx,
	"SELECT id, title, author FROM books WHERE title ILIKE '%' || $1 || '%' ORDER BY id", q)
```

`||` es la concatenación de cadenas de PostgreSQL. El `%` va en la consulta y el texto del usuario en el parámetro: PostgreSQL nunca lo lee como SQL.

```bash
go test -run TestSearchSQLInjection -v . | grep -E "^(---|    ---|ok)"
# --- PASS: TestSearchSQLInjection (0.04s)
#     --- PASS: TestSearchSQLInjection/' (0.02s)
#     --- PASS: TestSearchSQLInjection/'_OR_'1'='1 (0.01s)
#     --- PASS: TestSearchSQLInjection/%'_UNION_SELECT_0,_username,_password_hash_FROM_staff_-- (0.01s)
# ok  	example.com/biblioteca	0.048s
```

**Experimento: los comodines.** Arranca la API (`go run .`) y busca `%` y `_`:

```bash
curl -s -G localhost:18044/api/books --data-urlencode 'q=%' | jq length
# 6
curl -s -G localhost:18044/api/books --data-urlencode 'q=_' | jq length
# 6
```

`-G` manda los datos en la URL (una petición GET) y `--data-urlencode` los codifica. Ya no hay inyección SQL, pero `%` (cualquier texto) y `_` (cualquier carácter) siguen siendo comodines de `ILIKE`. Aquí no rompe nada; en una tabla grande, buscar `%` recorre la tabla entera ([semana 43](../../../week-43-testing_de_performance_con_k6_y_locust/README.md)). Para buscarlos como texto habría que escaparlos con `\`. Para la API (Ctrl+C).

## Paso 3: los errores no enseñan el SQL

Descomenta el PASO 3 y el import `strings`. El test pide el préstamo `abc` con el token de ana: el id es un número, así que la consulta falla en PostgreSQL. Comprueba que el cuerpo no contiene `SQLSTATE` ni `ERROR:`.

```bash
go test -run TestErrorsDoNotLeakDatabaseDetails .
# --- FAIL: TestErrorsDoNotLeakDatabaseDetails (0.01s)
#     injection_test.go:66: la respuesta contiene "SQLSTATE": {"error":"ERROR: invalid input syntax for type integer: \"abc\" (SQLSTATE 22P02)"}
#     injection_test.go:66: la respuesta contiene "ERROR:": {"error":"ERROR: invalid input syntax for type integer: \"abc\" (SQLSTATE 22P02)"}
# FAIL
```

Corrige `writeError`: un 500 escribe el detalle en el log del servidor y responde un mensaje genérico, como la API del museo desde la semana 34.

```go
func writeError(w http.ResponseWriter, status int, msg string) {
	if status == http.StatusInternalServerError {
		log.Print(msg)
		msg = "internal error"
	}
	writeJSON(w, status, map[string]string{"error": msg})
}
```

```bash
go test -run TestErrorsDoNotLeakDatabaseDetails -v . | grep -E "^(---|ok)"
# --- PASS: TestErrorsDoNotLeakDatabaseDetails (0.01s)
# ok  	example.com/biblioteca	0.014s
```

Con la API arrancada, el detalle no se pierde, cambia de sitio:

```bash
curl -s localhost:18044/api/loans/abc -H 'Authorization: Bearer ana-token'
# {"error":"internal error"}
```

Y en la terminal de la API:

```text
2026/11/18 17:32:36 ERROR: invalid input syntax for type integer: "abc" (SQLSTATE 22P02)
```

La API sigue respondiendo 500 a un id que no es un número; lo correcto sería un 404. No es un fallo de seguridad, pero es un buen test para añadir por tu cuenta.

## Paso 4: XSS en la página de resultados

Descomenta el PASO 4. El test busca `<script>alert(1)</script>` en la página HTML y comprueba dos cosas: que el texto no aparece tal cual y que aparece escapado.

```bash
go test -run TestSearchPageEscapesHTML .
# --- FAIL: TestSearchPageEscapesHTML (0.01s)
#     injection_test.go:79: la página contiene <script>alert(1)</script> sin escapar
#     injection_test.go:82: la página no contiene el texto escapado:
#         <!doctype html>
#         <html lang="es"><head><meta charset="utf-8"><title>Búsqueda</title></head>
#         <body><h1>Resultados para <script>alert(1)</script></h1>
#         <ul>
#         </ul>
#         <p><a href="/">Volver</a></p>
#         </body></html>
# FAIL
```

Un navegador que recibe esa página ejecuta el script. Corrige la página con `html/template`:

1. Añade `"html/template"` a los imports de `main.go`.
2. Encima de `newHandler`, la plantilla. `{{.Q}}` es el texto buscado y `{{range .Books}}` repite la línea por cada libro; `html/template` escapa cada valor:

   ```go
   // searchPage escapa cada valor según dónde lo escribe: en el texto, en un atributo o en una URL.
   var searchPage = template.Must(template.New("buscar").Parse(`<!doctype html>
   <html lang="es"><head><meta charset="utf-8"><title>Búsqueda</title></head>
   <body><h1>Resultados para {{.Q}}</h1>
   <ul>
   {{range .Books}}<li>{{.Title}}, de {{.Author}}</li>
   {{end}}</ul>
   <p><a href="/">Volver</a></p>
   </body></html>
   `))
   ```

3. En el handler de `GET /buscar`, sustituye los tres `fmt.Fprint...` por la plantilla:

   ```go
   w.Header().Set("Content-Type", "text/html; charset=utf-8")
   searchPage.Execute(w, map[string]any{"Q": q, "Books": books})
   ```

`template.Must` hace que el programa se pare al arrancar si la plantilla tiene un error de sintaxis, en lugar de fallar en la primera petición.

```bash
go test -v . | grep -E "^(---|ok)"
# --- PASS: TestSearchFindsBooks (0.02s)
# --- PASS: TestSearchSQLInjection (0.03s)
# --- PASS: TestErrorsDoNotLeakDatabaseDetails (0.01s)
# --- PASS: TestSearchPageEscapesHTML (0.01s)
# ok  	example.com/biblioteca	0.067s
```

Con la API arrancada, lo que recibe el navegador:

```bash
curl -s -G localhost:18044/buscar --data-urlencode 'q=<script>alert(1)</script>' | grep Result
# <body><h1>Resultados para &lt;script&gt;alert(1)&lt;/script&gt;</h1>
```

**Experimento: `text/template`.** Cambia el import por `"text/template"` y ejecuta `go test -run TestSearchPageEscapesHTML .`: compila igual (la sintaxis es la misma) y el test vuelve a fallar. Por eso el test se queda en la suite. Vuelve a `html/template`.

---

## Comprobación final

```bash
go vet .
go test .
# ok  	example.com/biblioteca	0.119s
```

Deja `injection_test.go` y tus cambios en `main.go`: el lab 02 sigue sobre esta API.

## Qué has aprendido

- Una carga de inyección se elige por lo que detecta: `'` rompe la sintaxis, `OR` devuelve de más, `UNION` trae otra tabla. El test comprueba el número exacto de resultados.
- La corrección es el parámetro (`$1`), no escapar comillas. `%` y `_` siguen siendo comodines de `LIKE`.
- Un 500 responde `internal error` y el detalle va al log.
- `html/template` escapa según el contexto; `text/template`, con la misma sintaxis, no. El test de XSS comprueba que el texto peligroso no está y que su versión escapada sí.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Autenticación, autorización e IDOR](../lab-02-autenticacion-autorizacion-e-idor/README.md) → |

[↑ Semana 44](../../README.md)
