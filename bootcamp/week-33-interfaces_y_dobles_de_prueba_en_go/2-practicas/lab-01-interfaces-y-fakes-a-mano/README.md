# Lab 01 - Interfaces y fakes a mano

## Objetivo

Probar un servicio que depende de un almacén, de un notificador y de la hora, sin base de datos, sin correo y sin librerías: con un fake, un spy, dos stubs y un reloj fijo escritos a mano.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go test -count=1 ./...
# ok  	bootcamp/lab01	0.002s [no tests to run]
```

`[no tests to run]`: el paquete compila, pero todos los tests están comentados.

Abre `reminders.go`. `Service.SendReminders` pide al almacén los préstamos activos y envía un aviso por cada uno vencido, con el texto `"<título>: <días> días de retraso"`. Devuelve cuántos avisos envió y se para en el primero que falla. Fíjate en tres cosas:

| En `reminders.go` | Qué es |
|---|---|
| `type LoanStore interface { Active() ([]Loan, error) }` | Lo único que el servicio necesita de un almacén: un método |
| `type Notifier interface { Notify(member, message string) error }` | Lo único que necesita de un notificador |
| `Now func() time.Time` | La hora llega desde fuera. En producción se pasa `time.Now`; aquí, un reloj fijo |

Las interfaces están en el paquete `reminders`, el que las usa ([teoría 01](../../1-teoria/01-interfaces-y-dobles-a-mano.md#las-interfaces-se-cumplen-sin-declararlo)).

## Paso a paso

### Paso 1: un fake, un spy y un reloj fijo (PASO 1)

1. En el import de `reminders_test.go`, descomenta `"slices"`, `"testing"` y `"time"`.
2. Descomenta el bloque del PASO 1: selecciona desde `// var now` hasta el final de `TestSendReminders` y pulsa `Ctrl+/` una vez. Las líneas con `// //` quedan como comentarios normales.
3. Guarda. VS Code formatea el archivo con `gofmt` al guardar; si usas otro editor, ejecuta `gofmt -w .`.
4. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestSendReminders (0.00s)
   # ok  	bootcamp/lab01	0.003s
   ```

Lee lo que descomentaste:

| Código | Doble | Qué hace |
|---|---|---|
| `fixedClock` | Stub del reloj | Siempre devuelve el 10 de marzo de 2026 a las 9:00 UTC |
| `memStore` | Fake | Un almacén que funciona de verdad: `Add` guarda y `Active` devuelve una copia, con `slices.Clone` |
| `recordingNotifier` | Spy | Guarda cada aviso como `"<socio> <- <mensaje>"`; si su campo `err` no es `nil`, lo devuelve |
| `newStore` | — | Un helper que prepara tres préstamos: Dune vencido hace 3 días, Emma vence dentro de 5 y Ulises venció hace 12 |

`TestSendReminders` no pregunta al spy *si* se le llamó: lee `notifier.sent` y lo compara con la lista esperada.

### Paso 2: dos stubs que fallan (PASO 2)

1. En el import, descomenta `"errors"`.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestSendReminders (0.00s)
   # --- PASS: TestSendRemindersWhenStoreFails (0.00s)
   # --- PASS: TestSendRemindersStopsAtFirstFailedNotice (0.00s)
   # ok  	bootcamp/lab01	0.002s
   ```

| Test | Doble que falla | Qué comprueba |
|---|---|---|
| `TestSendRemindersWhenStoreFails` | `errStore`, un stub con un solo campo: el error que devuelve | Que el error del almacén llega envuelto (`errors.Is`) y que no se envía ningún aviso |
| `TestSendRemindersStopsAtFirstFailedNotice` | `recordingNotifier{err: errDown}`: el spy de antes, ahora fallando | Que el servicio intenta un aviso, falla y no sigue con el segundo |

El segundo test necesita el spy: con un stub que solo devolviera el error no podrías contar los intentos.

## Experimentos

### Experimento 1: la hora real

En `reminders.go`, cambia `now := s.Now()` por `now := time.Now()` y ejecuta:

```bash
go test -count=1 ./...
# --- FAIL: TestSendReminders (0.00s)
#     reminders_test.go:54: sent = 3, want 2
#     reminders_test.go:61: notices = ["ana <- Dune: 255 días de retraso" "luis <- Emma: 247 días de retraso" "eva <- Ulises: 264 días de retraso"], want ["ana <- Dune: 3 días de retraso" "eva <- Ulises: 12 días de retraso"]
```

Los números salen de ejecutarlo el 18 de noviembre de 2026; a ti te saldrán otros, porque dependen del día. Por eso falla: el test fija las fechas de los préstamos respecto al 10 de marzo, y el servicio ya no usa ese reloj. Un test que depende de la fecha pasa hoy y falla mañana. Deshaz el cambio.

### Experimento 2: una interfaz más grande

Añade a `LoanStore` un segundo método, como si el servicio también tuviera que guardar préstamos:

```go
type LoanStore interface {
	Active() ([]Loan, error)
	Add(l Loan)
}
```

```bash
go test ./...
# # bootcamp/lab01 [bootcamp/lab01.test]
# ./reminders_test.go:77:23: cannot use errStore{…} (value of struct type errStore) as LoanStore value in struct literal: errStore does not implement LoanStore (missing method Add)
```

`memStore` ya tenía `Add`, así que sigue cumpliendo la interfaz sin tocarlo. `errStore` no: un stub de una línea tendría que implementar un método que su test no usa. Cada método de más en la interfaz es trabajo en cada doble. Deshaz el cambio.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/lab01	0.002s
```

## Preguntas de cierre

1. `memStore` no dice en ningún sitio que implementa `LoanStore`. ¿Dónde comprueba el compilador que la cumple?
2. ¿Por qué `Now` es una función y no un `time.Time` que se pasa al crear el servicio?
3. Si `recordingNotifier` no tuviera el campo `err`, ¿cómo escribirías el test del aviso que falla?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Mocks con mockgen](../lab-02-mocks-con-mockgen/README.md) → |

[↑ Semana 33](../../README.md)
