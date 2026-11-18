# Lab 03 - TestMain, build tags y -short

## Objetivo

Preparar un recurso compartido por todo el paquete con `TestMain`, saltar un test lento con `-short`, y separar los tests de integración con la build tag `integration`, de modo que `go test ./...` ejecute solo los rápidos.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go test -count=1 -v ./...
# === RUN   TestGetUnknownBook
# --- PASS: TestGetUnknownBook (0.00s)
# PASS
# ok  	bootcamp/lab03	0.004s
```

`catalog.go` es la API del [lab 01](../lab-01-handlers-con-httptest/README.md). `store.go` añade `MemStore`, un almacén en memoria protegido con un `sync.Mutex`, porque un servidor HTTP atiende varias peticiones a la vez.

Hay dos archivos de test:

| Archivo | Qué prueba | Cómo |
|---|---|---|
| `catalog_test.go` | El handler | En memoria, con `NewRecorder`. Ya pasa |
| `api_test.go` | La API completa, por TCP | Con un servidor compartido. Lo escribes en los pasos |

## Paso a paso

### Paso 1: un servidor para todo el paquete con TestMain (PASO 1)

1. En el import de `api_test.go`, descomenta las líneas del PASO 1. Al guardar, gofmt las ordena e indenta.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # 2026/11/18 02:27:51 setup: server at http://127.0.0.1:44225
   # === RUN   TestCreateThenGet
   # --- PASS: TestCreateThenGet (0.00s)
   # === RUN   TestGetUnknownBook
   # --- PASS: TestGetUnknownBook (0.00s)
   # PASS
   # 2026/11/18 02:27:51 teardown: server closed
   # ok  	bootcamp/lab03	0.009s
   ```

Si un paquete define `func TestMain(m *testing.M)`, `go test` la llama a ella en lugar de ejecutar los tests directamente ([teoría 02](../../1-teoria/02-testmain-build-tags-y-short.md#1-testmain)). Lo que va antes de `m.Run()` se ejecuta una vez antes de todos los tests del paquete, y lo que va después, una vez al final. Aquí arranca un único `httptest.NewServer` con un `MemStore` y guarda su URL en `baseURL`.

`TestCreateThenGet` usa `http.Post` y `http.Get`, el cliente por defecto, contra esa URL: la petición pasa por TCP, el servidor y el almacén, como en producción. Las líneas de `log` salen antes y después de todos los tests, no una vez por test.

### Paso 2: un test lento y -short (PASO 2)

1. En el import, descomenta `"fmt"` y `"sync"`.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta con y sin `-short`:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestCreateThenGet
   # --- PASS: TestCreateThenGet (0.00s)
   # === RUN   TestManyClients
   # --- PASS: TestManyClients (0.09s)
   # === RUN   TestGetUnknownBook
   # --- PASS: TestGetUnknownBook (0.00s)
   # PASS
   # ok  	bootcamp/lab03	0.101s

   go test -count=1 -short -v ./...
   # === RUN   TestCreateThenGet
   # --- PASS: TestCreateThenGet (0.00s)
   # === RUN   TestManyClients
   #     api_test.go:57: slow: 2000 requests over TCP
   # --- SKIP: TestManyClients (0.00s)
   # === RUN   TestGetUnknownBook
   # --- PASS: TestGetUnknownBook (0.00s)
   # PASS
   # ok  	bootcamp/lab03	0.007s
   ```

   (Se omiten las líneas de `log`.)

`TestManyClients` lanza 20 goroutines con `wg.Go` (Go 1.25+), cada una con 100 peticiones `POST`. Comprueba que `MemStore` aguanta peticiones a la vez. `testing.Short()` devuelve `true` cuando se ejecuta con `-short`, y entonces el test se salta con `t.Skip`. Aquí tarda menos de una décima de segundo. En un proyecto real, este patrón se reserva para los tests que tardan segundos.

### Paso 3: separar la integración con una build tag (PASO 3)

1. Añade al principio de `api_test.go`, antes de `package catalog`, esta línea y una línea en blanco:

   ```go
   //go:build integration

   package catalog
   ```

2. Ejecuta sin la tag:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestGetUnknownBook
   # --- PASS: TestGetUnknownBook (0.00s)
   # PASS
   # ok  	bootcamp/lab03	0.004s
   ```

3. Y con ella, junto con `-short`:

   ```bash
   go test -count=1 -tags integration -short -v ./...
   # 2026/11/18 02:28:09 setup: server at http://127.0.0.1:36495
   # === RUN   TestCreateThenGet
   # --- PASS: TestCreateThenGet (0.00s)
   # === RUN   TestManyClients
   #     api_test.go:59: slow: 2000 requests over TCP
   # --- SKIP: TestManyClients (0.00s)
   # === RUN   TestGetUnknownBook
   # --- PASS: TestGetUnknownBook (0.00s)
   # PASS
   # 2026/11/18 02:28:09 teardown: server closed
   # ok  	bootcamp/lab03	0.009s
   ```

`//go:build integration` es una **restricción de compilación**: el archivo solo se compila si se pasa `-tags integration`. Sin la tag, `api_test.go` no existe para el compilador: ni sus tests ni su `TestMain`, así que tampoco se arranca el servidor. La línea de `t.Skip` pasa de la 57 a la 59 por las dos líneas que añadiste.

Con esto quedan dos comandos:

| Comando | Qué ejecuta | Cuándo |
|---|---|---|
| `go test ./...` | Solo los tests unitarios | Mientras programas y en cada commit |
| `go test -tags integration ./...` | Unitarios y de integración | Antes del pull request y en CI |

La tag también afecta a `go vet`: sin ella, `go vet ./...` no revisa `api_test.go`. Revisa los dos conjuntos con `go vet ./... && go vet -tags integration ./...`.

## Experimentos

### Experimento 1: os.Exit no ejecuta los defer

Con el PASO 1 aplicado, cambia el final de `TestMain` por un `defer` y `os.Exit`, como en muchos ejemplos antiguos (añade `"os"` al import):

```go
defer func() {
	srv.Close()
	log.Print("teardown: server closed")
}()
os.Exit(m.Run())
```

```bash
go test -count=1 -v ./...
# 2026/11/18 02:27:59 setup: server at http://127.0.0.1:36889
# === RUN   TestCreateThenGet
# --- PASS: TestCreateThenGet (0.00s)
# === RUN   TestGetUnknownBook
# --- PASS: TestGetUnknownBook (0.00s)
# PASS
# ok  	bootcamp/lab03	0.008s
```

Falta la línea `teardown`. `os.Exit` termina el proceso en el acto, sin ejecutar los `defer`. Aquí no se pierde nada, porque el proceso muere y el servidor con él. Pero un contenedor, un archivo temporal fuera de `t.TempDir()` o un registro en una base de datos compartida sí quedarían vivos. Desde Go 1.15, `TestMain` puede terminar sin `os.Exit`: `go test` usa el resultado de `m.Run()`. Restaura la versión del paso.

### Experimento 2: testing.Short antes de m.Run

Con el PASO 1 aplicado, cambia la línea del `log.Printf` de `TestMain` para mostrar si se pidió `-short`:

```go
log.Printf("setup: server at %s (short=%v)", srv.URL, testing.Short())
```

```bash
go test -count=1 ./...
# panic: testing: Short called before Parse
#
# goroutine 1 [running]:
# testing.Short(...)
# 	.../src/testing/testing.go:693
# bootcamp/lab03.TestMain(0x3add6975e3c0)
# 	<lab>/api_test.go:21 +0x225
```

Los flags de `go test` (`-short`, `-run`, `-v`) se leen dentro de `m.Run()`. Antes de esa llamada, `testing.Short()` no sabe todavía qué se pidió. Si `TestMain` necesita un flag antes de `m.Run()`, llama primero a `flag.Parse()` (añade `"flag"` al import):

```bash
go test -count=1 -short -v ./...
# 2026/11/18 02:28:50 setup: server at http://127.0.0.1:33105 (short=true)
```

Restaura la línea original.

### Experimento 3: el mismo servidor en dos rondas

Con el PASO 3 aplicado, ejecuta la suite de integración dos veces seguidas en el mismo proceso:

```bash
go test -count=2 -short -tags integration ./...
# --- FAIL: TestCreateThenGet (0.00s)
#     api_test.go:39: POST status = 409, want 201
# FAIL
```

`-count=2` repite los tests, pero `TestMain` solo se ejecuta una vez. En la segunda ronda, el servidor compartido ya tiene el libro que creó la primera y responde `409 Conflict`. Pasaría lo mismo si otro test creara ese ISBN antes, en cualquier orden de `-shuffle`. Un recurso compartido ahorra tiempo de arranque, pero cada test tiene que dejarlo como lo encontró o usar datos que no choquen. El lab 04 resuelve este problema con una transacción por test.

### Experimento 4: una tag mal escrita

Con el PASO 3 aplicado, cambia la primera línea de `api_test.go` por `//go:build integraton` (falta una `i`):

```bash
go test -count=1 -tags integration -v ./...
# === RUN   TestGetUnknownBook
# --- PASS: TestGetUnknownBook (0.00s)
# PASS
# ok  	bootcamp/lab03	0.004s
```

Ningún error y ningún aviso, tampoco de `go vet`. La tag es un nombre libre: `integraton` es tan válida como `integration`, y como nadie la pide, el archivo no se compila. Los tests de integración han desaparecido en silencio. Para ver qué tests compila cada combinación, usa `-list`:

```bash
go test -tags integration -list . ./...
# TestGetUnknownBook
# ok  	bootcamp/lab03	0.005s
```

Restaura la línea y vuelve a listar:

```bash
go test -tags integration -list . ./...
# 2026/11/18 02:29:33 setup: server at http://127.0.0.1:43451
# TestCreateThenGet
# TestManyClients
# TestGetUnknownBook
# 2026/11/18 02:29:33 teardown: server closed
# ok  	bootcamp/lab03	0.006s
```

Ahora aparecen los tres tests. `-list` no ejecuta ningún test, pero sí `TestMain`: por eso salen las líneas de `log`. En el lab 04, `TestMain` arranca un contenedor, así que listar con la tag también lo arranca.

### Experimento 5: dos TestMain

Con el PASO 3 aplicado, añade al final de `catalog_test.go`, que no tiene tag:

```go
func TestMain(m *testing.M) {
	m.Run()
}
```

```bash
go test -count=1 ./...
# ok  	bootcamp/lab03	0.003s
go test -count=1 -tags integration ./...
# # bootcamp/lab03
# multiple definitions of TestMain
# FAIL	bootcamp/lab03 [setup failed]
```

Solo puede haber una `TestMain` por paquete. Sin la tag compila, porque la de `api_test.go` no existe, y el error solo aparece al pedir la integración. Si el paquete necesita preparar algo para los dos conjuntos de tests, deja una sola `TestMain` en un archivo sin tag. Borra la que añadiste.

## Preguntas de cierre

1. ¿Qué se ejecuta una vez por paquete y qué una vez por test cuando hay una `TestMain`?
2. ¿Por qué `testing.Short()` hace `panic` antes de `m.Run()` y cómo se evita?
3. ¿Qué compila `go test ./...` sin tags? ¿Y `go vet ./...`?
4. ¿Cómo compruebas que una build tag no está mal escrita?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Clientes HTTP con httptest.NewServer](../lab-02-clientes-con-httptest-newserver/README.md) | [Lab 04 - PostgreSQL con testcontainers-go](../lab-04-postgresql-con-testcontainers-go/README.md) → |

[↑ Semana 34](../../README.md)
