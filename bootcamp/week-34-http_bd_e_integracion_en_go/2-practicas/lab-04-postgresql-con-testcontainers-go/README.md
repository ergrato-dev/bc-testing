# Lab 04 - PostgreSQL con testcontainers-go

## Objetivo

Probar un repositorio contra un PostgreSQL real que arranca el propio test con testcontainers-go. Primero con un contenedor por test, después con uno para todo el paquete, y por último con una transacción por test que se deshace al terminar, para que los tests no se pisen.

## Tiempo estimado

60 minutos.

## Preparación

Necesitas Docker en marcha ([semana 29](../../../week-29-testcontainers_en_java/README.md) usa el mismo requisito). Descarga antes las dos imágenes, para que la primera ejecución no se quede esperando la descarga:

```bash
docker pull postgres:18.7-alpine
docker pull testcontainers/ryuk:0.14.0
```

Después:

```bash
cd starter
go mod download
go test -count=1 ./...
# ?   	bootcamp/lab04	[no test files]
go test -count=1 -tags integration ./...
# ok  	bootcamp/lab04	0.003s [no tests to run]
```

`store_test.go` empieza con `//go:build integration` ([lab 03](../lab-03-testmain-build-tags-y-short/README.md#paso-3-separar-la-integración-con-una-build-tag-paso-3)): sin la tag, Go no ve ningún test.

`store.go` tiene el repositorio:

| Elemento | Qué hace |
|---|---|
| `DBTX` | Interfaz con `Exec`, `Query` y `QueryRow`. La cumplen `*pgxpool.Pool` y `pgx.Tx`, así que el repositorio funciona igual con una conexión del pool o dentro de una transacción |
| `Migrate(ctx, db)` | Ejecuta en orden los `.sql` de `migrations/`, incrustados en el binario con `//go:embed` |
| `Store.Add` | `INSERT`. Traduce la violación de clave primaria (código `23505`) a `ErrDuplicate` |
| `Store.Get` | `SELECT` de un libro. Traduce `pgx.ErrNoRows` a `ErrNotFound` |
| `Store.List` | Todos los libros ordenados por ISBN |
| `Store.Lend` | Resta una copia. Traduce la violación del `CHECK (copies >= 0)` (código `23514`) a `ErrNoCopies` |

`migrations/001_books.sql` crea la tabla `books`. La regla de que no haya copias negativas está en la base de datos, no en Go: la comprueba PostgreSQL.

## Paso a paso

### Paso 1: un contenedor nuevo para cada test (PASO 1)

1. En el import, descomenta las líneas del PASO 1. Al guardar, gofmt las ordena e indenta.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta. testcontainers-go escribe un registro largo; aquí se muestran solo las líneas de los tests:

   ```bash
   go test -count=1 -tags integration -v ./...
   # === RUN   TestAddAndGet
   # --- PASS: TestAddAndGet (2.83s)
   # === RUN   TestAddDuplicate
   # --- PASS: TestAddDuplicate (2.20s)
   # === RUN   TestList
   # --- PASS: TestList (2.17s)
   # === RUN   TestLendLastCopy
   # --- PASS: TestLendLastCopy (2.20s)
   # PASS
   # ok  	bootcamp/lab04	9.430s
   ```

`newStore` arranca un PostgreSQL por test ([teoría 03](../../1-teoria/03-testcontainers-go-y-postgresql.md#1-el-módulo-postgres)):

| Línea | Qué hace |
|---|---|
| `postgres.Run(ctx, image, opciones...)` | Crea y arranca el contenedor con la base de datos, el usuario y la contraseña indicados |
| `postgres.BasicWaitStrategies()` | Espera a que PostgreSQL acepte conexiones, no solo a que el contenedor esté encendido |
| `testcontainers.CleanupContainer(t, ctr)` | Registra en `t.Cleanup` la parada del contenedor. Va **antes** de comprobar `err`: si el contenedor llegó a crearse y la espera falló, también hay que pararlo |
| `ctr.MustConnectionString(ctx, "sslmode=disable")` | La URL de conexión, con el puerto que Docker asignó en el host |
| `Migrate` | Crea la tabla en la base de datos vacía |

Cada test tiene su base de datos limpia, pero el precio son unos 2 segundos de arranque por test.

En el registro verás también un segundo contenedor, `testcontainers/ryuk`. Es el vigilante de testcontainers: si el proceso de test muere sin parar sus contenedores, Ryuk los borra (experimento 2).

### Paso 2: un contenedor para todo el paquete (PASO 2)

1. Borra la función `newStore` del PASO 1, con su comentario.
2. En el import, descomenta `"context"` y `"log"`.
3. Descomenta el bloque del PASO 2 y guarda.
4. Ejecuta:

   ```bash
   go test -count=1 -tags integration ./...
   # --- FAIL: TestAddDuplicate (0.00s)
   #     store_test.go:39: duplicate isbn: 978-84-376-0494-7
   # --- FAIL: TestList (0.00s)
   #     store_test.go:59: List() mismatch (-want +got):
   #           []books.Book{
   #           	{ISBN: "978-0-14-118776-1", Title: "Ficciones", Copies: 1},
   #         + 	{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2},
   #           }
   # FAIL
   # FAIL	bootcamp/lab04	3.809s
   ```

Ahora `TestMain` arranca un solo contenedor ([lab 03](../lab-03-testmain-build-tags-y-short/README.md#paso-1-un-servidor-para-todo-el-paquete-con-testmain-paso-1)) y los tests duran milisegundos. Pero comparten la base de datos: `TestAddDuplicate` no puede insertar Rayuela porque `TestAddAndGet` ya lo hizo, y `TestList` encuentra un libro que no añadió. Es el mismo problema del [experimento 3 del lab 03](../lab-03-testmain-build-tags-y-short/README.md#experimento-3-el-mismo-servidor-en-dos-rondas), ahora con datos que persisten.

En `TestMain` no hay `t`, así que no hay `t.Cleanup` ni `t.Fatal`: los errores se tratan con `log.Fatal`, y el contenedor se para a mano con `testcontainers.TerminateContainer` después de `m.Run()`.

### Paso 3: una transacción por test (PASO 3)

1. Borra la función `newStore` del PASO 2 (las cuatro líneas que devuelven `&Store{DB: pool}`).
2. Descomenta el bloque del PASO 3 y guarda.
3. Ejecuta, con varias rondas y en orden aleatorio:

   ```bash
   go test -count=1 -tags integration -v ./...
   # === RUN   TestAddAndGet
   # --- PASS: TestAddAndGet (0.00s)
   # === RUN   TestAddDuplicate
   # --- PASS: TestAddDuplicate (0.00s)
   # === RUN   TestList
   # --- PASS: TestList (0.00s)
   # === RUN   TestLendLastCopy
   # --- PASS: TestLendLastCopy (0.00s)
   # PASS
   # ok  	bootcamp/lab04	2.992s

   go test -count=3 -shuffle=on -tags integration ./...
   # ok  	bootcamp/lab04	2.774s
   ```

`newStore` abre una transacción con `pool.Begin` y le da al `Store` la transacción en lugar del pool. Como `pgx.Tx` cumple `DBTX`, el repositorio no nota la diferencia. `t.Cleanup` hace `Rollback` al terminar el test: nada de lo que el test escribió llega a guardarse, y el siguiente encuentra la tabla vacía. Doce tests en tres rondas, en un solo contenedor y en menos de 3 segundos.

El `Rollback` usa `context.Background()` y no `t.Context()`, porque cuando se ejecutan las funciones de `t.Cleanup` el contexto del test ya está cancelado.

## Experimentos

### Experimento 1: un error deja la transacción inservible

Con el PASO 3 aplicado, añade al final de `TestAddDuplicate`, después de comprobar el `ErrDuplicate`:

```go
if _, err := s.Get(t.Context(), b.ISBN); err != nil {
	t.Errorf("Get after duplicate: %v", err)
}
```

```bash
go test -count=1 -tags integration -run Duplicate ./...
# --- FAIL: TestAddDuplicate (0.00s)
#     store_test.go:45: Get after duplicate: ERROR: current transaction is aborted, commands ignored until end of transaction block (SQLSTATE 25P02)
# FAIL
```

En PostgreSQL, cuando una sentencia falla dentro de una transacción, la transacción queda abortada y rechaza todo lo que venga después hasta el `ROLLBACK`. En producción no se nota, porque la petición termina con el error. Pero un test que comparte una transacción y quiere seguir después de un error esperado choca con esta regla.

La salida es un **savepoint**: un punto dentro de la transacción al que se puede volver. En pgx, `Begin` sobre una `pgx.Tx` crea un `SAVEPOINT`, y su `Rollback` vuelve a él sin abortar la transacción de fuera. Cambia la segunda llamada a `Add` por esto (añade `"github.com/jackc/pgx/v5"` al import):

```go
sp, err := s.DB.(pgx.Tx).Begin(t.Context()) // SAVEPOINT
if err != nil {
	t.Fatal(err)
}
if err := (&Store{DB: sp}).Add(t.Context(), b); !errors.Is(err, ErrDuplicate) {
	t.Errorf("second Add: err = %v, want ErrDuplicate", err)
}
sp.Rollback(t.Context()) // ROLLBACK TO SAVEPOINT
```

```bash
go test -count=1 -tags integration -run Duplicate -v ./...
# --- PASS: TestAddDuplicate (0.00s)
# ok  	bootcamp/lab04	8.619s
```

Deshaz los cambios.

### Experimento 2: una migración rota y el contenedor huérfano

Con el PASO 3 aplicado, en `migrations/001_books.sql` cambia `CHECK` por `CHEK`:

```bash
go test -count=1 -tags integration ./...
# 2026/11/18 02:32:41 migrations/001_books.sql: ERROR: syntax error at or near "CHEK" (SQLSTATE 42601)
# FAIL	bootcamp/lab04	7.488s
```

`log.Fatal` termina el proceso dentro de `TestMain`, así que `TerminateContainer` no llega a ejecutarse. Mira los contenedores enseguida y otra vez pasados unos 15 segundos:

```bash
docker ps -a --filter ancestor=postgres:18.7-alpine --format '{{.ID}} {{.Status}}'
# 5d1cd50b7aaf Up 2 seconds
docker ps -a --filter ancestor=postgres:18.7-alpine --format '{{.ID}} {{.Status}}'
# (sin salida)
```

El contenedor huérfano desaparece solo: Ryuk vio que el proceso de test se había desconectado y lo borró. Sin Ryuk, cada ejecución fallida dejaría un PostgreSQL encendido. Restaura `CHECK`.

### Experimento 3: la regla en la base de datos

Con el PASO 3 aplicado, borra `CHECK (copies >= 0)` de la migración y ejecuta `TestLendLastCopy`:

```bash
go test -count=1 -tags integration -run LendLastCopy ./...
# --- FAIL: TestLendLastCopy (0.00s)
#     store_test.go:72: second Lend: err = <nil>, want ErrNoCopies
# FAIL
```

El segundo `Lend` deja el libro con `-1` copias sin error: ya nadie rechaza el `UPDATE`. Un fake en memoria no habría encontrado este fallo, porque la regla vive en el esquema SQL, y el esquema solo se prueba contra un PostgreSQL de verdad. Restaura el `CHECK`.

## Preguntas de cierre

1. ¿Por qué `testcontainers.CleanupContainer` se llama antes de comprobar el error de `postgres.Run`?
2. ¿Qué ganas y qué pierdes al pasar de un contenedor por test a uno por paquete?
3. ¿Qué hace que el `Store` acepte una transacción en lugar del pool sin cambiar su código?
4. ¿Por qué un test que espera un error de la base de datos no puede seguir usando la misma transacción, y qué lo arregla?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - TestMain, build tags y -short](../lab-03-testmain-build-tags-y-short/README.md) | [Proyecto — Proyecto Semanal - La API del dominio y su repositorio en PostgreSQL](../../3-proyecto/README.md) → |

[↑ Semana 34](../../README.md)
