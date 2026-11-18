# Lab 03 - assert, require y testify/mock

## Objetivo

Reescribir un test de la librería estándar con `assert` y `require` de testify, provocar el `panic` que da `assert` donde hacía falta `require`, y escribir el mock del notificador con `testify/mock` para compararlo con el de mockgen.

## Tiempo estimado

50 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# --- PASS: TestSendReminders (0.00s)
# ok  	bootcamp/lab03	0.002s
```

El servicio es el del lab 01 con un método más, `MostOverdue`: devuelve un puntero al préstamo con más días de retraso, o `ErrNoOverdue` si no hay ninguno vencido. `fakes_test.go` trae el reloj, el almacén y el spy del lab 01.

`go.mod` tiene una sola dependencia directa, `github.com/stretchr/testify v1.12.1`. Las dos líneas `// indirect` (`objx` y `yaml`) son dependencias de testify.

## Paso a paso

### Paso 0: el test con la librería estándar

Lee `TestSendReminders`. Es el del lab 01 con una diferencia: compara las dos listas a mano, con `len` y los índices. Fíjate en cuántas líneas ocupa cada comprobación.

### Paso 1: el mismo test con require y assert (PASO 1)

1. En el import, descomenta `assert` y `require`.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestSendReminders (0.00s)
   # --- PASS: TestSendRemindersWithTestify (0.00s)
   # ok  	bootcamp/lab03	0.003s
   ```

| Línea | Qué hace |
|---|---|
| `require.NoError(t, err)` | Si `err` no es `nil`, marca el fallo y **para** el test, como `t.Fatalf` |
| `assert.Equal(t, 2, sent)` | Si no son iguales, marca el fallo y **sigue**, como `t.Errorf`. El orden es esperado y después obtenido |
| `assert.Equal(t, []string{...}, notifier.sent)` | Compara los slices enteros, sin `slices.Equal` ni índices |

Borra `TestSendReminders` con su comentario del PASO 0: `TestSendRemindersWithTestify` lo sustituye.

### Paso 2: require cuando lo que sigue depende del resultado (PASO 2)

Descomenta el bloque del PASO 2:

```bash
go test -count=1 -run MostOverdue -v ./...
# --- PASS: TestMostOverdue (0.00s)
# --- PASS: TestMostOverdueWithoutOverdueLoans (0.00s)
```

En `TestMostOverdue`, `loan` es un puntero: si hubiera error, sería `nil`, y `loan.Title` haría `panic`. Por eso el error se comprueba con `require`. En `TestMostOverdueWithoutOverdueLoans`, las dos comprobaciones son independientes y no se usa el puntero: basta `assert`. `assert.ErrorIs` es `errors.Is` con mensaje.

### Paso 3: un mock escrito con testify/mock (PASO 3)

1. En el import, descomenta `mock`.
2. Descomenta el bloque del PASO 3 y ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestSendRemindersWithTestify (0.00s)
   # --- PASS: TestMostOverdue (0.00s)
   # --- PASS: TestMostOverdueWithoutOverdueLoans (0.00s)
   # --- PASS: TestSendRemindersWithTestifyMock (0.00s)
   # ok  	bootcamp/lab03	0.004s
   ```

`mockNotifier` está escrito a mano: incluye `mock.Mock` y su método `Notify` llama a `m.Called(member, message)`, que busca una expectativa con esos argumentos y devuelve lo que diga su `Return`. Compáralo con el lab 02:

| | Lab 02 (mockgen) | Lab 03 (testify/mock) |
|---|---|---|
| El mock | Generado con `go generate` | Escrito a mano: un método por cada método de la interfaz |
| La expectativa | `EXPECT().Notify("ana", ...)` | `On("Notify", "ana", ...)`: el nombre del método es un texto |
| Una vez | Por defecto | `.Once()` |
| Comprobar lo pendiente | Automático al terminar el test | `notifier.AssertExpectations(t)`, a mano |

## Experimentos

### Experimento 1: el mensaje de assert.Equal

En `TestSendRemindersWithTestify`, cambia `"ana <- Dune: 3 días de retraso"` por `"ana <- Dune: 4 días de retraso"`:

```bash
go test -count=1 -run 'WithTestify$' ./...
# --- FAIL: TestSendRemindersWithTestify (0.00s)
#     reminders_test.go:18:
#         	Error Trace:	<lab>/reminders_test.go:18
#         	Error:      	Not equal:
#         	            	expected: []string{"ana <- Dune: 4 días de retraso", "eva <- Ulises: 12 días de retraso"}
#         	            	actual  : []string{"ana <- Dune: 3 días de retraso", "eva <- Ulises: 12 días de retraso"}
#
#         	            	Diff:
#         	            	--- Expected
#         	            	+++ Actual
#         	            	@@ -1,3 +1,3 @@
#         	            	 ([]string) (len=2) {
#         	            	- (string) (len=31) "ana <- Dune: 4 días de retraso",
#         	            	+ (string) (len=31) "ana <- Dune: 3 días de retraso",
#         	            	  (string) (len=34) "eva <- Ulises: 12 días de retraso"
#         	Test:       	TestSendRemindersWithTestify
```

`<lab>` es la ruta de tu copia. El diff marca la línea distinta con `-` (esperado) y `+` (obtenido). Deshaz el cambio.

### Experimento 2: assert donde hacía falta require

En `TestMostOverdue`, cambia `newStore()` por `&memStore{}`, un almacén vacío, y `require.NoError` por `assert.NoError`:

```bash
go test -count=1 -run 'TestMostOverdue$' ./...
# --- FAIL: TestMostOverdue (0.00s)
#     reminders_test.go:29:
#         	Error Trace:	<lab>/reminders_test.go:29
#         	Error:      	Received unexpected error:
#         	            	no overdue loans
#         	Test:       	TestMostOverdue
# panic: runtime error: invalid memory address or nil pointer dereference [recovered, repanicked]
# ...
# 	<lab>/reminders_test.go:30 +0x8a
```

`assert.NoError` marcó el fallo y siguió; la línea 30 leyó `loan.Title` con `loan` a `nil`. Un `panic` en un test aborta el binario de test entero: los tests que faltaban por ejecutar en ese paquete no se ejecutan. Vuelve a poner `require.NoError` y ejecuta otra vez: el test falla solo con `no overdue loans`, sin `panic`. Deshaz los dos cambios.

### Experimento 3: el mock de testify con una llamada de más y una de menos

1. Borra la expectativa de `eva` en `TestSendRemindersWithTestifyMock`:

   ```bash
   go test -count=1 -run TestifyMock ./...
   # --- FAIL: TestSendRemindersWithTestifyMock (0.00s)
   # panic:
   #
   # 	mock: Unexpected Method Call
   # 	-----------------------------
   #
   # 	Notify(string,string)
   # 			0: "eva"
   # 			1: "Ulises: 12 días de retraso"
   #
   # 	The closest call I have is:
   #
   # 	Notify(string,string)
   # 			0: "ana"
   # 			1: "Dune: 3 días de retraso"
   ```

   Una llamada sin expectativa es un `panic`, no un fallo normal del test. Deshaz el cambio.

2. Añade una expectativa que el servicio nunca cumplirá, debajo de la de `eva`:

   ```go
   notifier.On("Notify", "luis", "Emma: 1 días de retraso").Return(nil).Once()
   ```

   ```bash
   go test -count=1 -run TestifyMock ./...
   # --- FAIL: TestSendRemindersWithTestifyMock (0.00s)
   #     reminders_test.go:61: FAIL:	Notify(string,string)
   #         		at: [<lab>/reminders_test.go:56]
   #     reminders_test.go:61: FAIL: 2 out of 3 expectation(s) were met.
   #         	The code you are testing needs to make 1 more call(s).
   #         	at: [<lab>/reminders_test.go:61]
   ```

   Lo detecta `AssertExpectations` en la línea 61. Si la borras, el test pasa con la expectativa sin cumplir. Deshaz el cambio.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/lab03	0.004s
```

## Preguntas de cierre

1. ¿Qué regla usarías para decidir entre `assert` y `require` en cada línea?
2. ¿Qué error del experimento 3 no podría pasar con mockgen, y por qué?
3. testify y go-cmp resuelven parte del mismo problema. ¿Cuál de los dos usarías para comparar dos structs grandes, y por qué?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Mocks con mockgen](../lab-02-mocks-con-mockgen/README.md) | [Lab 04 - go-cmp y golden files](../lab-04-go-cmp-y-golden-files/README.md) → |

[↑ Semana 33](../../README.md)
