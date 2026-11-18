# 02 - Inyección SQL y XSS

> Herramientas: `go test`, `httptest` y PostgreSQL (`pgx`), como en la [semana 34](../../week-34-http_bd_e_integracion_en_go/README.md)

Una **inyección** ocurre cuando un dato del usuario se mezcla con código y el intérprete no sabe dónde acaba uno y empieza el otro. Si el código es SQL, es inyección SQL. Si es HTML que ejecuta el navegador, es XSS. La causa y la corrección son las mismas: separar el dato del código. Y el test también: mandar un dato que parece código y comprobar que se trata como dato.

---

## 🎯 Objetivos

- Explicar por qué una consulta concatenada es inyectable y una con parámetros no.
- Escribir un test de inyección que distingue "no hay resultados" de "la consulta se rompió" y de "devolvió de más".
- Explicar el XSS reflejado y almacenado, y comprobar en un test que la salida HTML va escapada.
- Comprobar que un error no enseña detalles internos al cliente.

---

## 1. Inyección SQL

La búsqueda de la [biblioteca insegura](../2-practicas/biblioteca-insegura/main.go) construye la consulta pegando el texto:

```go
db.Query(ctx, "SELECT id, title, author FROM books WHERE title ILIKE '%"+q+"%' ORDER BY id")
```

Con `q=go`, la consulta es la que esperas. Con `q=' OR '1'='1`, la comilla cierra la cadena y lo demás se lee como SQL:

```sql
SELECT id, title, author FROM books WHERE title ILIKE '%' OR '1'='1%' ORDER BY id
```

`'1'='1%'` es falso, pero `ILIKE '%'` ya es verdadero para todos los títulos: la búsqueda devuelve los 6 libros. Con `UNION SELECT` el atacante añade filas de otra tabla. En el lab 01, `%' UNION SELECT 0, username, password_hash FROM staff --` devuelve el *hash* de la contraseña del administrador como si fuera un libro. El `--` comenta el resto de la consulta original.

### La corrección: parámetros

```go
db.Query(ctx, "SELECT id, title, author FROM books WHERE title ILIKE '%' || $1 || '%' ORDER BY id", q)
```

`$1` es un **parámetro**. pgx manda la consulta y el valor por separado; PostgreSQL analiza la consulta antes de conocer el valor, así que el valor nunca puede cambiar su estructura. Es la misma regla en todos los lenguajes del curso: `?` en JDBC (`PreparedStatement`), `%s` o `:name` con el *driver* de Python, `$1` con `sqlx` en Rust. Escapar las comillas a mano no es una corrección: hay codificaciones y casos que se escapan.

Dos detalles que el test no ve solo:

- **Lo que no puede ser parámetro.** Un parámetro sustituye un valor, nunca un nombre de columna ni `ASC`/`DESC`. Si el usuario elige el orden, compáralo con una lista de valores permitidos (`title`, `author`) y rechaza lo demás.
- **Los comodines de `LIKE`.** Con parámetros, `q=%` ya no es inyección SQL, pero `%` sigue siendo el comodín de `LIKE`: la búsqueda devuelve todos los libros. No rompe nada aquí; en una tabla de un millón de filas, una búsqueda de `%` puede ser un problema de rendimiento.

### Las múltiples sentencias

`q='; DROP TABLE loans; --` falla con pgx: `cannot insert multiple commands into a prepared statement (SQLSTATE 42601)`. pgx usa el protocolo extendido de PostgreSQL, que solo admite una sentencia por consulta. Eso no te protege: `UNION` y `OR` funcionan con una sola sentencia.

---

## 2. El test de inyección

Un test de inyección manda cargas (*payloads*) que un atacante usaría y comprueba que se tratan como texto:

```go
payloads := []string{
	`'`,
	`' OR '1'='1`,
	`%' UNION SELECT 0, username, password_hash FROM staff --`,
}
for _, p := range payloads {
	t.Run(p, func(t *testing.T) {
		srv := newServer(t)
		res, body := send(t, srv, http.MethodGet, "/api/books?q="+url.QueryEscape(p), "")
		// ningún título contiene estos textos: 200 y una lista vacía
	})
}
```

Cada carga detecta una cosa distinta:

| Carga | Si la consulta es inyectable | Lo que comprueba el test |
|---|---|---|
| `'` | La comilla deja la cadena sin cerrar: error de sintaxis, 500 | Status 200 |
| `' OR '1'='1` | Devuelve todo: 6 libros | 0 libros |
| `UNION SELECT ... FROM staff` | Devuelve libros y una fila de otra tabla | 0 libros |

El número esperado sale de los datos: ningún título contiene `' OR '1'='1`. Si el test comprobara solo "status 200", la segunda carga pasaría con la consulta vulnerable.

`url.QueryEscape` codifica la carga para la URL (el espacio como `+`, la comilla como `%27`). Sin ella, la petición llegaría mal formada y el test fallaría por otro motivo, como la URL sin codificar del [lab 01 de la semana 43](../../week-43-testing_de_performance_con_k6_y_locust/2-practicas/lab-01-primer-test-de-carga-con-k6/README.md).

### Con un fake no vale

La inyección SQL ocurre en PostgreSQL. Un almacén falso en memoria no ejecuta SQL, así que un test con un fake pasa aunque el código real concatene. El test de inyección va contra la base de datos real: en los labs, con una transacción que se deshace al terminar; en el proyecto, en los tests de integración de `pgstore`.

---

## 3. XSS

**XSS** (*Cross-Site Scripting*) es la inyección en HTML. La página de búsqueda escribe el texto buscado tal cual:

```go
fmt.Fprintf(w, "<h1>Resultados para %s</h1>", q)
```

Con `q=<script>alert(1)</script>`, el navegador recibe una etiqueta `<script>` y la ejecuta. Un atacante manda a la víctima un enlace con esa búsqueda, y el script se ejecuta en el navegador de la víctima con su sesión: puede leer lo que ve o hacer peticiones en su nombre.

| Tipo | De dónde sale el dato | Ejemplo |
|---|---|---|
| **Reflejado** | De la petición, y vuelve en la misma respuesta | La búsqueda que repite `q` |
| **Almacenado** | De la base de datos: alguien lo guardó antes | El título de un libro con `<script>` que ven todos los que abren la ficha |
| **DOM** | JavaScript de la página lo mete en el HTML en el navegador | `element.innerHTML = location.hash` |

### La corrección: escapar según el contexto

**Escapar** es sustituir los caracteres especiales por su forma de texto: `<` por `&lt;`, `>` por `&gt;`, `"` por `&#34;`. El navegador los muestra en lugar de interpretarlos. Qué caracteres hay que escapar depende de dónde va el dato: no es lo mismo el texto de un `<h1>` que un atributo `href`. `html/template` de Go analiza la plantilla y escapa cada valor según su contexto:

```go
var searchPage = template.Must(template.New("buscar").Parse(`<h1>Resultados para {{.Q}}</h1>`))
```

`text/template`, con la misma sintaxis, no escapa nada: sirve para texto plano, nunca para HTML. Jinja (Python), Thymeleaf (Java) y los motores de plantillas de JavaScript escapan por defecto; el peligro está en las funciones que lo desactivan (`|safe`, `th:utext`, `innerHTML`).

### El test

```go
_, body := send(t, srv, http.MethodGet, "/buscar?q="+url.QueryEscape(`<script>alert(1)</script>`), "")
// el cuerpo no contiene <script>alert(1)</script>
// el cuerpo contiene &lt;script&gt;alert(1)&lt;/script&gt;
```

Las dos comprobaciones hacen falta. Sin la segunda, una página que no muestra el texto buscado (o que devuelve un 500) pasaría el test.

La API JSON no tiene este problema en el servidor: `Content-Type: application/json` hace que el navegador no lo interprete como HTML. El riesgo pasa al cliente que pinta ese JSON en una página, que es donde entra el XSS de tipo DOM. La cabecera `X-Content-Type-Options: nosniff` ([teoría 04](04-cabeceras-zap-y-dependencias.md)) impide que el navegador "adivine" que un JSON es HTML.

---

## 4. Los errores no se enseñan

Un error de la base de datos devuelto al cliente le dice al atacante qué base de datos hay, cómo es la consulta y si su carga llegó al SQL:

```json
{"error":"ERROR: invalid input syntax for type integer: \"abc\" (SQLSTATE 22P02)"}
```

Es A10 (manejo de condiciones excepcionales) y ayuda a explotar A05: el mensaje `unterminated quoted string` confirma que la comilla llegó a la consulta. La corrección es la de la API del museo desde la [semana 34](../../week-34-http_bd_e_integracion_en_go/README.md): un 500 responde `internal error` y el detalle va al log del servidor, donde lo ve quien lo opera (A09). El test pide algo que hace fallar la consulta y comprueba que el cuerpo no contiene `SQLSTATE` ni `ERROR:`.

---

## ✅ Resumen

- Inyección = dato mezclado con código. Se corrige separándolos: parámetros (`$1`) en SQL y escape según el contexto (`html/template`) en HTML.
- El test manda cargas y comprueba el resultado exacto (0 libros), no solo el status. La inyección SQL se prueba contra PostgreSQL real, no contra un fake.
- Lo que no puede ser parámetro (columnas, orden) se valida contra una lista de valores permitidos. `%` sigue siendo comodín de `LIKE`.
- Un test de XSS comprueba que el texto peligroso no está y que su versión escapada sí.
- Un 500 responde un mensaje genérico; el detalle va al log.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - OWASP Top 10 desde el tester](01-owasp-top-10-para-testers.md) | [Teoría — 03 - Autenticación, autorización e IDOR](03-autenticacion-autorizacion-e-idor.md) → |

[↑ Semana 44](../README.md)
