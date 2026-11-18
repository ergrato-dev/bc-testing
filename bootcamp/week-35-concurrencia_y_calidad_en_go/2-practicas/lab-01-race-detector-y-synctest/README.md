# Lab 01 - Race detector y synctest

## Objetivo

Encontrar con `-race` una carrera de datos que el test normal deja pasar, leer su informe y arreglarla. Después, probar con `testing/synctest` una reserva que caduca a las 48 horas y unos reintentos con espera, sin esperar de verdad y con duraciones exactas.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go test -count=1 -v ./...
# === RUN   TestViewsCount
# --- PASS: TestViewsCount (0.00s)
# PASS
# ok  	bootcamp/lab01	0.002s
```

`-race` necesita cgo y un compilador de C. Compruébalo antes de empezar:

```bash
go env CGO_ENABLED
# 1
gcc --version | head -1
```

Si `gcc` no existe, instálalo (en Ubuntu o WSL, `sudo apt install gcc`). Sin él, o con `CGO_ENABLED=0`, `go test -race` termina con `go: -race requires cgo; enable cgo by setting CGO_ENABLED=1`.

El módulo no tiene dependencias. Tiene tres archivos de código, del servicio de una biblioteca:

| Archivo | Qué hace |
|---|---|
| `views.go` | `Views` cuenta las consultas de cada libro en un mapa. La API la llama desde cada petición, es decir, desde varias goroutines |
| `holds.go` | `Holds` guarda las reservas. Cada reserva caduca sola con un `time.AfterFunc` cuando pasa `TTL` |
| `retry.go` | `Retry` repite una llamada que falla, esperando 1 s, 2 s, 4 s... hasta agotar los intentos o el plazo del `context` |

## Paso a paso

### Paso 1: una carrera que el test no ve (PASO 1)

1. En el import de `views_test.go`, descomenta `"sync"`.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta el test varias veces:

   ```bash
   go test -count=1 -v -run Concurrent ./...
   # === RUN   TestViewsConcurrent
   # --- PASS: TestViewsConcurrent (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.003s
   ```

`TestViewsConcurrent` lanza dos goroutines con `wg.Go` (Go 1.25+) que registran una visita cada una al mismo libro, y comprueba que hay dos. Pasa siempre: dos operaciones tan cortas casi nunca coinciden en el tiempo.

4. Ahora, con el race detector ([teoría 01](../../1-teoria/01-race-detector-y-synctest.md#2-el-race-detector)):

   ```bash
   go test -count=1 -race -run Concurrent ./...
   # ==================
   # WARNING: DATA RACE
   # Read at 0x00c00019c660 by goroutine 9:
   #   runtime.mapaccess1_faststr()
   #       <goroot>/src/internal/runtime/maps/runtime_faststr.go:101 +0x0
   #   bootcamp/lab01.(*Views).Record()
   #       <lab>/views.go:15 +0x56
   #   bootcamp/lab01.TestViewsConcurrent.func1()
   #       <lab>/views_test.go:25 +0x2c
   #
   # Previous write at 0x00c00019c660 by goroutine 10:
   #   runtime.mapassign_faststr()
   #       <goroot>/src/internal/runtime/maps/runtime_faststr.go:263 +0x0
   #   bootcamp/lab01.(*Views).Record()
   #       <lab>/views.go:15 +0x8e
   #   bootcamp/lab01.TestViewsConcurrent.func1()
   #       <lab>/views_test.go:25 +0x2c
   #
   # Goroutine 9 (running) created at:
   #   sync.(*WaitGroup).Go()
   #       <goroot>/src/sync/waitgroup.go:238 +0x72
   #   ...
   # ==================
   # (un segundo informe, sobre otra dirección del mismo mapa)
   # --- FAIL: TestViewsConcurrent (0.00s)
   #     testing.go:1712: race detected during execution of test
   # FAIL
   # FAIL	bootcamp/lab01	0.015s
   ```

   `<lab>` y `<goroot>` sustituyen a las rutas de tu máquina, y las direcciones de memoria cambian en cada ejecución.

Cómo se lee el informe:

| Bloque | Qué dice aquí |
|---|---|
| `Read at ... by goroutine 9` | La goroutine 9 leía el mapa (`mapaccess1`) en `views.go:15`, la línea `v.byISBN[isbn]++` |
| `Previous write at ... by goroutine 10` | La goroutine 10 había escrito en el mismo mapa (`mapassign`), en la misma línea, sin nada que ordenara los dos accesos |
| `views_test.go:25` | Las dos venían del `v.Record` del test |
| `created at: sync.(*WaitGroup).Go()` | Las dos goroutines las lanzó un `wg.Go` |
| `race detected during execution of test` | El test falla, aunque su `Count == 2` se cumplió |

5. Arregla `Views` con un `sync.Mutex`. En `views.go`, añade `import "sync"` debajo de `package holds`, y cambia el tipo y los dos métodos:

   ```go
   type Views struct {
   	mu     sync.Mutex
   	byISBN map[string]int
   }

   func (v *Views) Record(isbn string) {
   	v.mu.Lock()
   	defer v.mu.Unlock()
   	v.byISBN[isbn]++
   }

   func (v *Views) Count(isbn string) int {
   	v.mu.Lock()
   	defer v.mu.Unlock()
   	return v.byISBN[isbn]
   }
   ```

6. Ejecuta otra vez:

   ```bash
   go test -count=1 -race ./...
   # ok  	bootcamp/lab01	1.016s
   ```

`Lock` deja pasar a una goroutine cada vez: la segunda espera a que la primera llame a `Unlock`. `Count` también lo usa, porque leer mientras otra goroutine escribe es igual de carrera. `defer` garantiza el `Unlock` aunque el método termine antes de tiempo.

### Paso 2: una reserva que caduca en tiempo virtual (PASO 2)

1. Abre `holds_test.go`. En el import, descomenta las líneas del PASO 2. Al guardar, gofmt las ordena e indenta.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v -run Hold ./...
   # === RUN   TestHoldExpires
   # --- PASS: TestHoldExpires (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.003s
   ```

La reserva dura 48 horas y el test termina en 0.00 s. Todo pasa dentro de `synctest.Test(t, func(t *testing.T) {...})`, una **burbuja** con su propio reloj ([teoría 01](../../1-teoria/01-race-detector-y-synctest.md#3-synctest-tiempo-virtual)):

| Línea | Qué pasa |
|---|---|
| `h.Place(...)` | La reserva arranca un `time.AfterFunc` de 48 h. El temporizador pertenece a la burbuja |
| `time.Sleep(48*time.Hour - time.Second)` | El test se bloquea. Como no hay nada más que ejecutar en la burbuja, el reloj salta 47 h 59 min 59 s |
| `synctest.Wait()` | Espera a que las demás goroutines de la burbuja estén bloqueadas. Aquí no hay ninguna |
| `h.Holder(...)` → sigue reservado | Falta un segundo |
| `time.Sleep(time.Second)` | El reloj llega a las 48 h y el temporizador lanza su función, que borra la reserva |
| `synctest.Wait()` | Espera a que esa función termine antes de comprobar |

Probar justo antes y justo después del límite es el análisis de valores límite de la [semana 2](../../../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md), aplicado al tiempo. Sin la burbuja, este test tardaría dos días.

### Paso 3: reintentos con tiempo exacto (PASO 3)

1. En el import, descomenta `"context"` y `"errors"`.
2. Descomenta el bloque del PASO 3 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v -run Retry ./...
   # === RUN   TestRetryBackoff
   # --- PASS: TestRetryBackoff (0.00s)
   # === RUN   TestRetryDeadline
   # --- PASS: TestRetryDeadline (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.003s
   ```

`TestRetryBackoff` llama a `Retry` con 4 intentos y una espera inicial de 1 s, con una función que siempre falla. Entre intentos espera 1 s, 2 s y 4 s, y el test comprueba que pasaron **exactamente** 7 segundos con `time.Since(start) != 7*time.Second`. En la burbuja el reloj solo avanza cuando todo está bloqueado, así que no hay milisegundos de más.

`TestRetryDeadline` le da a `Retry` un contexto que vence a los 5 s, con 10 intentos. Tras esperar 1 s y 2 s, la tercera espera (4 s) se corta a los 5 s, y el error envuelve a la vez `context.DeadlineExceeded` y el último error de la función: `Retry` usa dos `%w` en el mismo `fmt.Errorf`, y `errors.Is` encuentra los dos.

4. Comprueba la suite completa con el race detector:

   ```bash
   go test -count=20 -race ./...
   # ok  	bootcamp/lab01	1.029s
   ```

## Experimentos

### Experimento 1: sin -race y con mucha concurrencia

Este experimento necesita `Views` sin el mutex: hazlo antes del punto 5 del paso 1, o quita el mutex un momento. Cambia el bucle de `TestViewsConcurrent` por 100 goroutines con 1000 visitas cada una:

```go
for range 100 {
	wg.Go(func() {
		for range 1000 {
			v.Record("978-84-376-0494-7")
		}
	})
}
```

Cambia también el valor esperado a `100_000` y ejecuta **sin** `-race`:

```bash
go test -count=1 -run Concurrent ./...
# fatal error: concurrent map writes
#
# goroutine 10 [running]:
# ...
# FAIL	bootcamp/lab01	0.010s
```

Con suficiente concurrencia, el propio runtime de Go detecta que dos goroutines escriben en el mismo mapa y mata el proceso. No es un fallo del test: es el programa entero el que muere, como moriría el servidor en producción. Esta comprobación del runtime solo cubre los mapas y solo salta si el choque ocurre; `-race` vigila cualquier variable, y lo detectó con dos goroutines. Restaura el test y el mutex.

### Experimento 2: sin synctest.Wait

Con el PASO 2 aplicado, borra el segundo `synctest.Wait()` de `TestHoldExpires` (el que va después de `time.Sleep(time.Second)`) y ejecuta el test 20 veces:

```bash
go test -count=20 -run Hold ./...
# --- FAIL: TestHoldExpires (0.00s)
#     holds_test.go:27: still held by ana after 48h
# --- FAIL: TestHoldExpires (0.00s)
#     holds_test.go:27: still held by ana after 48h
#     holds_test.go:30: Place after expiry: book already held
# ...
```

En esta ejecución fallaron 9 de las 20 rondas; el número cambia cada vez. Al llegar a las 48 horas, el `time.Sleep` del test y la función del temporizador se despiertan a la vez, en goroutines distintas. Sin `Wait`, a veces el test mira la reserva antes de que la función la borre. Un test que pasa 11 veces de 20 es un test inestable (flaky). `Wait` lo vuelve determinista. Restaura la línea.

### Experimento 3: el mismo test fuera de la burbuja

Con el PASO 3 aplicado, en `TestRetryBackoff` cambia `synctest.Test(t, func(t *testing.T) {` por `func() {`, y el `})` que lo cierra por `}()`. El código es el mismo, pero ahora se ejecuta con el reloj real:

```bash
go test -count=1 -run RetryBackoff ./...
# --- FAIL: TestRetryBackoff (7.00s)
#     holds_test.go:54: elapsed = 7.002622367s, want 7s (1s + 2s + 4s)
# FAIL
# FAIL	bootcamp/lab01	7.005s
```

Tarda 7 segundos reales, y la duración ya no es exacta: el planificador y el propio test añaden unos milisegundos. Fuera de la burbuja solo podrías comprobar un rango (`entre 7 y 7.1 s`), y el test sería lento e inestable en una máquina de CI cargada. Restaura `synctest.Test`.

### Experimento 4: una goroutine que nadie para

Con el PASO 2 aplicado, añade al principio de `TestHoldExpires`, justo después de `h := NewHolds(48 * time.Hour)`, una goroutine que revisa las reservas cada hora hasta que se cierra `stop`:

```go
stop := make(chan struct{})
go func() { // revisa las reservas cada hora hasta que se cierre stop
	for {
		select {
		case <-time.After(time.Hour):
		case <-stop:
			return
		}
	}
}()
```

```bash
go test -count=1 -run HoldExpires ./...
# --- FAIL: TestHoldExpires (0.00s)
# panic: deadlock: main bubble goroutine has exited but blocked goroutines remain [recovered, repanicked]
# ...
# goroutine 22 [select (durable), synctest bubble 1]:
# bootcamp/lab01.TestHoldExpires.func1.1()
# 	<lab>/holds_test.go:18 +0x65
# created by bootcamp/lab01.TestHoldExpires.func1 in goroutine 21
```

Nadie cierra `stop`, así que la goroutine sigue viva cuando el test termina. `synctest.Test` espera a todas las goroutines de la burbuja, ve que esta solo puede esperar para siempre, y falla señalando dónde está bloqueada (`select (durable)`, línea 18). Fuera de la burbuja, esa fuga de goroutine habría pasado desapercibida. Añade `defer close(stop)` debajo de `stop := make(...)`:

```bash
go test -count=1 -run HoldExpires ./...
# ok  	bootcamp/lab01	0.003s
```

Borra la goroutine y el `defer`.

## Preguntas de cierre

1. ¿Por qué `TestViewsConcurrent` pasa sin `-race` si el código tiene una carrera?
2. En el informe del race detector, ¿qué dos accesos chocaron y qué tenían en común?
3. ¿Cuándo avanza el reloj de una burbuja de `synctest`? ¿Por qué eso hace que las duraciones sean exactas?
4. ¿Qué hace `synctest.Wait()` y qué pasa en `TestHoldExpires` si falta?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Fuzzing nativo](../lab-02-fuzzing-nativo/README.md) → |

[↑ Semana 35](../../README.md)
