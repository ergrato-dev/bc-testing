# Lab 01 - Handlers con httptest

## Objetivo

Probar los handlers de una API sin abrir ningún puerto: enviar peticiones con `httptest.NewRequest`, grabar la respuesta con `httptest.NewRecorder`, comprobar el estado, las cabeceras y el JSON, y ver qué responde el `http.ServeMux` a las rutas y métodos que no existen.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# === RUN   TestFakeStore
# --- PASS: TestFakeStore (0.00s)
# PASS
# ok  	bootcamp/lab01	0.003s
```

`catalog.go` tiene la API del catálogo de una biblioteca:

| Ruta | Respuesta |
|---|---|
| `GET /books/{isbn}` | `200` con el libro en JSON, o `404` si no existe |
| `POST /books` | `201` con el libro y la cabecera `Location`; `400` si el JSON no se puede leer o le faltan datos; `409` si el ISBN ya existe |

`NewHandler(s Store)` recibe el almacén como interfaz ([semana 33](../../../week-33-interfaces_y_dobles_de_prueba_en_go/1-teoria/01-interfaces-y-dobles-a-mano.md)). En `catalog_test.go` tienes `fakeStore`, un mapa que hace de almacén, y `TestFakeStore`, que comprueba que el fake devuelve los mismos errores que el real.

## Paso a paso

### Paso 1: una petición con NewRequest y NewRecorder (PASO 1)

1. En el import, descomenta `"encoding/json"`, `"net/http"`, `"net/http/httptest"` y `cmp`.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestFakeStore
   # --- PASS: TestFakeStore (0.00s)
   # === RUN   TestGetBook
   # --- PASS: TestGetBook (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.004s
   ```

`TestGetBook` hace en memoria lo que haría un cliente por la red ([teoría 01](../../1-teoria/01-httptest-handlers-y-clientes.md#1-handlers-con-newrecorder)):

| Línea | Qué hace |
|---|---|
| `httptest.NewRequest(method, target, body)` | Construye la petición que recibiría el servidor. No devuelve error: si `target` no se puede leer, hace `panic` |
| `httptest.NewRecorder()` | Un `http.ResponseWriter` que guarda en memoria el estado, las cabeceras y el cuerpo que escribe el handler |
| `h.ServeHTTP(rec, req)` | Llama al handler. Pasa por el `ServeMux`, que elige la ruta y rellena `r.PathValue("isbn")` |
| `rec.Result()` | Devuelve un `*http.Response` como el que recibiría el cliente |

El cuerpo se decodifica en un `Book` y se compara con `cmp.Diff`, como en la semana 33.

### Paso 2: rutas que no existen y métodos que no se admiten (PASO 2)

1. En el import, descomenta `"strings"`.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -run TestRouting -v ./...
   # === RUN   TestRouting
   # === RUN   TestRouting/unknown_book
   # === RUN   TestRouting/unknown_path
   # === RUN   TestRouting/delete_not_allowed
   # === RUN   TestRouting/get_on_collection
   # --- PASS: TestRouting (0.00s)
   #     --- PASS: TestRouting/unknown_book (0.00s)
   #     --- PASS: TestRouting/unknown_path (0.00s)
   #     --- PASS: TestRouting/delete_not_allowed (0.00s)
   #     --- PASS: TestRouting/get_on_collection (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.004s
   ```

`serve` es un helper con `t.Helper()` ([semana 32](../../../week-32-fundamentos_de_testing_en_go/README.md)) que junta las tres líneas del paso 1. La tabla tiene cuatro casos:

- `/books/000` llega al handler, que responde `404` porque el fake no tiene ese libro.
- `/authors` no coincide con ningún patrón: el `404` lo responde el `ServeMux`.
- `DELETE /books/{isbn}` y `GET /books` sí coinciden con una ruta, pero no con su método. El `ServeMux` responde `405 Method Not Allowed` y pone en `Allow` los métodos que sí admite esa ruta. `GET` incluye `HEAD`.

Ninguno de estos casos está programado en `catalog.go`. Los resuelve el router, y el test documenta lo que hace.

### Paso 3: crear un libro y mirar el fake (PASO 3)

Descomenta el bloque del PASO 3:

```bash
go test -count=1 -run Create -v ./...
# --- PASS: TestCreateBook (0.00s)
# --- PASS: TestCreateBookRejects (0.00s)
#     --- PASS: TestCreateBookRejects/duplicate (0.00s)
#     --- PASS: TestCreateBookRejects/malformed_json (0.00s)
#     --- PASS: TestCreateBookRejects/missing_title (0.00s)
#     --- PASS: TestCreateBookRejects/negative_copies (0.00s)
# PASS
# ok  	bootcamp/lab01	0.005s
```

(Se omiten las líneas `=== RUN`.)

`TestCreateBook` comprueba la respuesta y también **el estado del fake**: el libro quedó guardado con los datos del JSON. `TestCreateBookRejects` comprueba lo contrario: en cada petición rechazada, el almacén sigue igual que antes. Un `400` que guardara el libro de todas formas pasaría un test que solo mirara el estado.

### Paso 4: el contrato JSON campo por campo (PASO 4)

Descomenta el bloque del PASO 4:

```bash
go test -count=1 -v ./...
# --- PASS: TestFakeStore (0.00s)
# --- PASS: TestGetBook (0.00s)
# --- PASS: TestRouting (0.00s)
# --- PASS: TestCreateBook (0.00s)
# --- PASS: TestCreateBookRejects (0.00s)
# --- PASS: TestBookJSON (0.00s)
# PASS
# ok  	bootcamp/lab01	0.004s
```

`TestBookJSON` decodifica la respuesta en un `map[string]any`, no en un `Book`. Así compara los nombres de los campos tal como salen por la red. Los números de JSON se decodifican como `float64`, por eso el valor esperado es `2.0`. El experimento 4 enseña qué atrapa este test que `TestGetBook` deja pasar.

## Experimentos

### Experimento 1: una cabecera después de WriteHeader

Con el PASO 1 aplicado, en `writeJSON` de `catalog.go` cambia el orden de las dos primeras líneas:

```go
w.WriteHeader(status)
w.Header().Set("Content-Type", "application/json")
```

```bash
go test -count=1 ./...
# --- FAIL: TestGetBook (0.00s)
#     catalog_test.go:63: Content-Type = "", want application/json
# FAIL
# FAIL	bootcamp/lab01	0.003s
```

`WriteHeader` envía el estado y las cabeceras. Lo que se cambie después ya no sale. `rec.Result()` devuelve las cabeceras tal como estaban en ese momento, igual que las recibiría el cliente.

Ahora, en `TestGetBook`, cambia `res.Header.Get("Content-Type")` por `rec.Header().Get("Content-Type")`:

```bash
go test -count=1 ./...
# ok  	bootcamp/lab01	0.003s
```

El test pasa con el error dentro. `rec.Header()` es el mapa que el handler sigue modificando, no lo que se envió. Comprueba siempre sobre `rec.Result()`. Deshaz los dos cambios.

### Experimento 2: los patrones con método necesitan Go 1.22

Con el PASO 2 aplicado, cambia en `go.mod` la línea `go 1.26.5` por `go 1.21`:

```bash
go vet ./...
# catalog.go:36:21: http.PathValue requires go1.22 or later (module is go1.21)
go test -count=1 -run TestRouting ./...
# --- FAIL: TestRouting (0.00s)
#     --- FAIL: TestRouting/delete_not_allowed (0.00s)
#         catalog_test.go:100: status = 404, want 405
#         catalog_test.go:103: Allow = "", want "GET, HEAD"
#     --- FAIL: TestRouting/get_on_collection (0.00s)
#         catalog_test.go:100: status = 404, want 405
#         catalog_test.go:103: Allow = "", want "POST"
# FAIL
```

Con `go 1.21`, el `ServeMux` vuelve a su comportamiento anterior: lee `"GET /books/{isbn}"` como una ruta literal, con espacio y llaves incluidos, y ninguna petición coincide. El código compila igual. `go vet` avisa, pero `go test` solo ejecuta una parte de los análisis de `vet` y este no está entre ellos. Por eso los proyectos del curso ejecutan `go vet` aparte. Deja `go 1.26.5`.

### Experimento 3: una validación que falta

Con el PASO 3 aplicado, en `catalog.go` borra `|| b.Copies < 0` de la validación del `POST`:

```bash
go test -count=1 -run Create ./...
# --- FAIL: TestCreateBookRejects (0.00s)
#     --- FAIL: TestCreateBookRejects/negative_copies (0.00s)
#         catalog_test.go:143: status = 201, want 400
#         catalog_test.go:146: store changed (-want +got):
#               catalog.fakeStore{
#             + 	"978-0":             {ISBN: "978-0", Title: "Ficciones", Copies: -1},
#               	"978-84-376-0494-7": {ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2},
#               }
# FAIL
```

Fallan las dos comprobaciones: el estado HTTP y el almacén. El diff de `cmp` enseña el libro con `-1` copias que el handler dejó guardar. Restaura la validación.

### Experimento 4: renombrar un campo JSON

Con el PASO 4 aplicado, en `Book` cambia la etiqueta `json:"copies"` por `json:"Copies"`:

```bash
go test -count=1 -v ./...
# --- PASS: TestFakeStore (0.00s)
# --- PASS: TestGetBook (0.00s)
# --- PASS: TestRouting (0.00s)
# --- PASS: TestCreateBook (0.00s)
# --- PASS: TestCreateBookRejects (0.00s)
#     catalog_test.go:164: JSON mismatch (-want +got):
#           map[string]any{
#         + 	"Copies": float64(2),
#         - 	"copies": float64(2),
#           	"isbn":   string("978-84-376-0494-7"),
#           	"title":  string("Rayuela"),
#           }
# --- FAIL: TestBookJSON (0.00s)
# FAIL
```

El cliente de la API que lee `copies` acaba de perder el campo, y aun así `TestGetBook` y `TestCreateBook` pasan. `encoding/json` no distingue mayúsculas de minúsculas al decodificar en un struct: `"Copies"` llena el campo `Copies` igual que `"copies"`. Un test que decodifica la respuesta en el mismo tipo que la produjo no ve el contrato. El mapa sí. Restaura la etiqueta.

## Preguntas de cierre

1. ¿Qué diferencia hay entre `rec.Header()` y `rec.Result().Header`, y cuál refleja lo que recibe el cliente?
2. ¿Quién responde el `405` de `DELETE /books/{isbn}`: tu código o el `ServeMux`? ¿Qué cabecera lo acompaña?
3. ¿Por qué `TestCreateBookRejects` compara el fake además del estado HTTP?
4. ¿Por qué `TestBookJSON` decodifica en un mapa y no en un `Book`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Clientes HTTP con httptest.NewServer](../lab-02-clientes-con-httptest-newserver/README.md) → |

[↑ Semana 34](../../README.md)
