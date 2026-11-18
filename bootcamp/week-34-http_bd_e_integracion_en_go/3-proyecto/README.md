# Proyecto Semanal - La API del dominio y su repositorio en PostgreSQL

## Objetivo

Poner delante del servicio de la semana 33 una API HTTP y detrás un almacén en PostgreSQL, y probar cada lado con su herramienta:

- los handlers con `httptest.NewRecorder`, con los fakes y el mock de la semana 33, sin red ni base de datos;
- el almacén contra un PostgreSQL real con testcontainers-go: un contenedor por paquete y una transacción por test;
- las dos suites por separado: `go test -short ./...` sin Docker y `go test -tags integration ./...` con Docker.

## Dónde va

En la carpeta `go/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)). La API va en el paquete del servicio, y el almacén en un paquete nuevo, `pgstore`. Los ejemplos usan el museo: paquete `museum`, servicio `LendingService`.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama y el starter

```bash
cd qa-<nombre>-<apellido>
git switch main && git pull
git switch -c week-34
cp -rn <ruta-al-bootcamp>/bootcamp/week-34-http_bd_e_integracion_en_go/3-proyecto/starter/. .
```

El starter trae:

| Archivo | Qué es |
|---|---|
| `go/domain/api.go` | El archivo de la API, vacío, con un TODO |
| `go/domain/api_test.go` | Tres tests de handlers con `t.Skip("TODO: ...")` |
| `go/pgstore/pgstore.go` | El paquete del almacén, vacío, con un TODO |
| `go/pgstore/pgstore_test.go` | Tres tests de integración con `t.Skip("TODO: ...")` y la línea `//go:build integration` |

Mueve los archivos de `domain/` al paquete de tu servicio y cambia su línea `package`. Para el museo:

```bash
mv go/domain/api*.go go/museum/
rmdir go/domain
sed -i 's/^package domain$/package museum/' go/museum/api*.go
cd go
go test -count=1 -run Endpoint -v ./...
# === RUN   TestGetEndpoint
#     api_test.go:6: TODO: GET con NewRecorder, estado y JSON campo por campo (paso 4)
# --- SKIP: TestGetEndpoint (0.00s)
# ...
# ok  	example.com/qa-museo/go/museum	0.003s
# ?   	example.com/qa-museo/go/pgstore	[no test files]
```

`pgstore` dice `[no test files]` porque su test lleva la build tag ([teoría 02](../1-teoria/02-testmain-build-tags-y-short.md#2-build-tags)). Con `-tags integration` aparecen sus tres `SKIP`.

### Paso 2: el contexto en las interfaces del almacén

Una petición HTTP trae su propio `context.Context`, y cuando el cliente se va, se cancela. Para que esa cancelación llegue a la consulta SQL, los métodos del almacén tienen que recibirlo ([teoría 03](../1-teoria/03-testcontainers-go-y-postgresql.md#4-el-contexto-de-la-petición-hasta-la-consulta)).

1. En el archivo del servicio, añade `ctx context.Context` como primer parámetro de los métodos de las interfaces de almacenamiento y de la operación del servicio. En el museo:

   ```go
   type PieceStore interface {
   	FindByCode(ctx context.Context, code string) (Piece, error)
   	Save(ctx context.Context, p Piece) error
   }

   type LoanStore interface {
   	Open(ctx context.Context, l Loan) error
   }

   func (s *LendingService) Lend(ctx context.Context, code, institutionID string, days int) (Piece, error)
   ```

   Dentro de `Lend`, pasa `ctx` a cada llamada: `s.Pieces.FindByCode(ctx, code)`, `s.Pieces.Save(ctx, piece)`, `s.Loans.Open(ctx, ...)`.

2. Compila. El compilador te lleva a cada sitio que hay que cambiar:

   ```bash
   go vet ./...
   # museum/lending_test.go:26:13: cannot use store (variable of type *fakeStore) as PieceStore value in struct literal: *fakeStore does not implement PieceStore (wrong type for method FindByCode)
   # 		have FindByCode(string) (Piece, error)
   # 		want FindByCode(context.Context, string) (Piece, error)
   # ...
   # museum/lending_test.go:43:40: not enough arguments in call to s.Lend
   ```

3. En el fake, añade el parámetro sin usarlo (`FindByCode(_ context.Context, code string)`). En los tests, llama con `s.Lend(t.Context(), ...)`: `t.Context()` se cancela al terminar el test ([semana 32](../../week-32-fundamentos_de_testing_en_go/README.md)).

El registro y el notificador no hablan con la base de datos, así que se quedan como estaban, y el mock generado no cambia.

### Paso 3: la API

Copia la API Java de la [semana 28](../../week-28-testing_de_spring_boot/3-proyecto/README.md): las mismas rutas y los mismos códigos de estado. En el archivo de la API, escribe `NewHandler(s *TuServicio) http.Handler` con un `http.ServeMux` y patrones con método ([teoría 01](../1-teoria/01-httptest-handlers-y-clientes.md#3-el-router-estándar-con-métodos)). En el museo:

| Ruta | Respuesta |
|---|---|
| `GET /api/pieces/{code}` | `200` con la pieza, o `404` |
| `POST /api/pieces/{code}/loan` con `{"institutionId": "MALI", "days": 30}` | `200` con la pieza prestada |

| Error | Código |
|---|---|
| El JSON no se puede leer | `400` |
| `ErrUnknownPiece` | `404` |
| `ErrNotAvailable` | `409` |
| `ErrNotAccredited`, `ErrNonPositiveDays` | `422` |
| Cualquier otro | `500`, con `{"error": "internal error"}`: no enseñes el error interno |

Tres detalles:

- Pasa `r.Context()` al servicio y lee los parámetros de la ruta con `r.PathValue("code")`.
- Traduce los errores con `errors.Is` en una sola función, `statusFor(err) int`. Así cada handler tiene una línea de manejo de errores y la tabla de códigos está en un sitio.
- Pon etiquetas `json:"..."` a los campos del tipo que devuelves, con los mismos nombres que la API Java. Si un campo es un enumerado, como `Status`, dale los métodos `MarshalText` y `UnmarshalText`: `encoding/json` los usa para escribirlo como texto (`"ON_LOAN"`) en lugar de como número. `pgstore` también los usará.

### Paso 4: los tests de los handlers

En el archivo de tests de la API, con los dobles de la semana 33 ([lab 01](../2-practicas/lab-01-handlers-con-httptest/README.md)):

1. Un helper `serve(t, h, method, target, body)` con `httptest.NewRequest`, `httptest.NewRecorder` y `rec.Result()`.
2. **GET**: el estado `200` y el JSON decodificado **en un mapa** y comparado con `cmp.Diff`, campo por campo. Así un nombre de campo cambiado hace fallar el test ([experimento 4 del lab 01](../2-practicas/lab-01-handlers-con-httptest/README.md#experimento-4-renombrar-un-campo-json)).
3. **POST correcto**: el mock del notificador con un `EXPECT()` y `Times(1)`, el estado `200`, el estado de la pieza en el cuerpo y en el fake.
4. **POST rechazado**: una tabla con una fila por código de error, sin `EXPECT` en el mock. Cada fila comprueba el código y que el fake quedó como estaba.
5. **500**: el notificador falla y el cuerpo es `{"error": "internal error"}`.
6. **405**: un `GET` sobre la ruta del préstamo responde `405` con `Allow: POST`.

```bash
go test -count=1 -run 'Endpoint|GetPiece|Routing' -v ./museum
# --- PASS: TestGetPiece (0.00s)
# --- PASS: TestLendEndpoint (0.00s)
# --- PASS: TestLendEndpointRejects (0.00s)
# --- PASS: TestLendEndpointHidesInternalErrors (0.00s)
# --- PASS: TestRouting (0.00s)
# ok  	example.com/qa-museo/go/museum	0.006s
```

(Se omiten las líneas `=== RUN` y las de los subtests.)

### Paso 5: el almacén en PostgreSQL

1. Añade las dependencias:

   ```bash
   go get github.com/jackc/pgx/v5@v5.13.0 \
     github.com/testcontainers/testcontainers-go@v0.45.0 \
     github.com/testcontainers/testcontainers-go/modules/postgres@v0.45.0
   ```

2. Copia la migración de Flyway de tu proyecto Java de la [semana 29](../../week-29-testcontainers_en_java/3-proyecto/README.md) a `go/pgstore/migrations/001_<nombre>.sql`. En el museo, `V1__create_pieces_and_loans.sql` pasa a `001_pieces_and_loans.sql` sin cambiar ni una línea: el esquema es el mismo para los dos lenguajes, con sus `CHECK` y su clave foránea.
3. En `pgstore.go` ([lab 04](../2-practicas/lab-04-postgresql-con-testcontainers-go/README.md)):
   - `//go:embed migrations/*.sql` y una función `Migrate(ctx, db)` que ejecuta los archivos en orden.
   - Una interfaz `DBTX` con `Exec` y `QueryRow`, que cumplen `*pgxpool.Pool` y `pgx.Tx`.
   - Un `Store` con un campo `DB DBTX` que cumpla las interfaces del servicio, y que traduzca los errores de PostgreSQL a los del dominio:

   | Error de PostgreSQL | Error del dominio en el museo |
   |---|---|
   | `pgx.ErrNoRows` en `FindByCode` | `museum.ErrUnknownPiece` |
   | `23503` (clave foránea) en `Open` | `museum.ErrUnknownPiece` |
   | `23505` (clave duplicada) en `Open` | `museum.ErrNotAvailable`: ya hay un préstamo abierto |

   - En el museo, `Save` es un `INSERT ... ON CONFLICT (code) DO UPDATE`: inserta la pieza o actualiza su estado.

Si vienes de Java, `Store` es el `JdbcPieceRepository` de la semana 29, con la consulta escrita a mano en lugar de Spring JDBC.

### Paso 6: los tests de integración

En `pgstore_test.go`, que ya lleva `//go:build integration`:

1. `TestMain` arranca un contenedor `postgres:18.7-alpine` con `postgres.Run` y `postgres.BasicWaitStrategies()`, abre un `pgxpool`, aplica `Migrate`, llama a `m.Run()` y termina el contenedor.
2. `newStore(t)` abre una transacción con `pool.Begin` y la deshace en `t.Cleanup` con `tx.Rollback(context.Background())`.
3. Los tests:
   - **Guardar y leer**, un subtest por cada valor del enumerado. Comprueba que el texto que se escribe en la columna es el que la tabla admite y que vuelve como el mismo valor.
   - **Actualizar** una fila existente.
   - **Lo que no existe**: la clave que no está devuelve el error del dominio.
   - **Lo que la tabla rechaza**: un valor que viola un `CHECK` (en el museo, `declared_value = 0`) devuelve error. Este test prueba el esquema, no el código Go.
   - **Clave foránea y duplicado** en la tabla de préstamos, con su error del dominio.

Recuerda: después de un error esperado de la base de datos, la transacción del test queda abortada ([experimento 1 del lab 04](../2-practicas/lab-04-postgresql-con-testcontainers-go/README.md#experimento-1-un-error-deja-la-transacción-inservible)). Termina el test ahí o usa un savepoint.

### Paso 7: el servicio completo sobre PostgreSQL

Un test de integración más, `TestLendWithPostgres`: el servicio de verdad con el `Store` de verdad en los dos almacenes, y dobles mínimos para lo que no es la base de datos (un stub del registro y un spy del notificador, declarados en el propio test). Comprueba que, después de la operación:

- la pieza quedó con el estado nuevo en la tabla;
- la fila del préstamo existe con la institución y la fecha de vencimiento correctas, leída con un `SELECT` en el test;
- un segundo préstamo de la misma pieza se rechaza.

```bash
go test -count=1 -tags integration -v ./pgstore
# --- PASS: TestSaveAndFind (0.01s)
#     --- PASS: TestSaveAndFind/AVAILABLE (0.01s)
#     --- PASS: TestSaveAndFind/ON_LOAN (0.00s)
#     --- PASS: TestSaveAndFind/IN_RESTORATION (0.00s)
# --- PASS: TestSaveUpdatesExistingPiece (0.00s)
# --- PASS: TestFindUnknownPiece (0.00s)
# --- PASS: TestSchemaRejectsNonPositiveValue (0.00s)
# --- PASS: TestOpenRejects (0.01s)
#     --- PASS: TestOpenRejects/unknown_piece (0.00s)
#     --- PASS: TestOpenRejects/second_open_loan (0.00s)
# --- PASS: TestLendWithPostgres (0.01s)
# ok  	example.com/qa-museo/go/pgstore	2.776s
```

(Se omiten el registro de testcontainers y las líneas `=== RUN`.)

Este test encaja el servicio y el `Store`, pero deja algo sin probar. `Lend` guarda la pieza y después abre el préstamo, y en el test los dos almacenes comparten la transacción. En producción, con el pool, son dos sentencias sueltas: si la segunda falla, la pieza queda prestada sin préstamo. Anótalo en el pull request. Hacerlo atómico es una decisión del servicio, no del test.

### Paso 8: verificar y entregar

```bash
go vet ./... && go vet -tags integration ./...
go test -count=1 -shuffle=on -short ./...
# ok  	example.com/qa-museo/go/museum	0.007s
# ?   	example.com/qa-museo/go/pgstore	[no test files]
go test -count=1 -shuffle=on -tags integration ./...
```

Desde la raíz del bootcamp, con Docker en marcha:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 34
```

Haz commit (incluida la migración), sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `go vet` no encuentra problemas, con y sin la tag `integration` | Paso 8 |
| La suite unitaria pasa con `-short` y en orden aleatorio, sin Docker | Paso 8 |
| La suite de integración pasa contra PostgreSQL (necesita Docker) | Pasos 6 y 7 |
| No queda ningún TODO en el código Go | Pasos 3 a 7 |
| Las interfaces de almacenamiento reciben `context.Context` | Paso 2 |
| Las rutas usan patrones con método del `http.ServeMux` | Paso 3 |
| Los handlers se prueban con `httptest.NewRecorder` | Paso 4 |
| Los tests de integración llevan `//go:build integration` | Paso 6 |
| PostgreSQL arranca con el módulo `postgres` de testcontainers-go y la imagen del curso | Paso 6 |
| Un contenedor por paquete (`TestMain`) y una transacción por test (`Rollback`) | Paso 6 |
| El esquema está en `migrations/*.sql`, incrustado con `//go:embed` | Paso 5 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - PostgreSQL con testcontainers-go](../2-practicas/lab-04-postgresql-con-testcontainers-go/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 34](../README.md)
