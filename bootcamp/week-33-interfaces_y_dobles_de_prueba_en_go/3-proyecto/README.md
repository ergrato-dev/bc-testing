# Proyecto Semanal - El servicio del dominio con dobles de prueba

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Portar a Go el servicio principal de tu dominio, el que en la semana 31 dependía de repositorios, de un notificador y de un `Clock`, y probarlo sin base de datos ni correo:

- una interfaz pequeña por dependencia, declarada en tu paquete, y el reloj como función;
- al menos un fake escrito a mano y un mock generado con mockgen;
- `cmp.Diff` para comparar estructuras y un golden file para el texto que envía el servicio;
- un `dobles.md` que justifica cada doble.

## Dónde va

En la carpeta `go/` de tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)), en el mismo paquete que las reglas de la [semana 32](../../week-32-fundamentos_de_testing_en_go/3-proyecto/README.md). Los ejemplos usan el museo: paquete `museum`, servicio `LendingService`.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: la rama

```bash
cd qa-<nombre>-<apellido>
git switch main && git pull
git switch -c week-33
```

### Paso 2: copiar el starter

```bash
cp -rn <ruta-al-bootcamp>/bootcamp/week-33-interfaces_y_dobles_de_prueba_en_go/3-proyecto/starter/. .
```

El starter trae:

| Archivo | Qué es |
|---|---|
| `go/domain/lending.go` | El archivo del servicio, vacío, con un TODO por paso |
| `go/domain/lending_test.go` | Tres tests con `t.Skip("TODO: ...")` |
| `go/dobles.md` | La plantilla de la justificación de los dobles |

Mueve los dos archivos de Go a tu paquete y cambia su línea `package`. Para el museo:

```bash
mv go/domain/lending*.go go/museum/
rmdir go/domain
sed -i 's/^package domain$/package museum/' go/museum/lending*.go
cd go
go test -count=1 -run 'Lend|Render' -v ./...
# === RUN   TestLend
#     lending_test.go:6: TODO: caso feliz con el fake, el mock y cmp.Diff (pasos 4 y 5)
# --- SKIP: TestLend (0.00s)
# === RUN   TestLendRejects
#     lending_test.go:10: TODO: tabla con cada rechazo, sin llamadas al mock (paso 5)
# --- SKIP: TestLendRejects (0.00s)
# === RUN   TestRenderNotice
#     lending_test.go:14: TODO: el texto del aviso contra un golden file (paso 6)
# --- SKIP: TestRenderNotice (0.00s)
# PASS
# ok  	example.com/qa-museo/go/museum	0.003s
```

Si tu servicio no se llama como el del museo, renombra los tests y el archivo; lo que cuenta son las piezas de cada paso.

### Paso 3: las interfaces y el reloj

1. Abre tu servicio Java de la [semana 31](../../week-31-proyecto_integrador_java/3-proyecto/README.md). Elige la operación con más dependencias: en el museo, `PieceService.lend`, que usa el repositorio de piezas, el de préstamos, el registro de instituciones, el notificador y el `Clock`.
2. En `lending.go`, declara **una interfaz por dependencia, con solo los métodos que usa esa operación** ([teoría 01](../1-teoria/01-interfaces-y-dobles-a-mano.md)). En el museo:

   ```go
   type PieceStore interface {
   	FindByCode(code string) (Piece, error) // ErrUnknownPiece si no existe
   	Save(p Piece) error
   }

   type LoanStore interface{ Open(l Loan) error }

   type Registry interface{ IsAccredited(institutionID string) bool }

   type Notifier interface{ Send(n LoanNotice) error }
   ```

3. El servicio es un struct con un campo por dependencia y el reloj como función:

   ```go
   type LendingService struct {
   	Pieces   PieceStore
   	Loans    LoanStore
   	Registry Registry
   	Notifier Notifier
   	Now      func() time.Time
   }
   ```

4. Porta la operación. Cada rechazo es un error con nombre (`errors.New`), como en la semana 32, y la fecha sale de `s.Now()`. **El código de producción no llama nunca a `time.Now()`**: lo comprueba el autograding.
5. Si el reporte de la semana 31 encontró un hueco en esta operación, arréglalo aquí. En el museo, `lend` prestaba piezas en restauración; en Go, `Lend` rechaza todo lo que no esté `Available`.

### Paso 4: el fake y cmp.Diff

1. Añade go-cmp:

   ```bash
   go get github.com/google/go-cmp@v0.7.0
   ```

2. Crea `go/<tu-paquete>/fakes_test.go` con un fake de tu almacén. El nombre del tipo empieza por `fake`: `fakeStore`, `fakeRepository`. Un mapa en memoria basta, pero tiene que comportarse como el real: si el real devuelve un error para un código que no existe, el fake también.
3. Un mismo fake puede cumplir varias interfaces. En el museo, `fakeStore` cumple `PieceStore` y `LoanStore`.
4. En `TestLend`, prueba el caso feliz y compara con `cmp.Diff` lo que devuelve la operación y lo que queda en el fake: las piezas guardadas, el préstamo abierto con su fecha de vencimiento.

Referencia: [lab 01](../2-practicas/lab-01-interfaces-y-fakes-a-mano/README.md) y [lab 04](../2-practicas/lab-04-go-cmp-y-golden-files/README.md).

### Paso 5: el mock generado

1. Registra mockgen como herramienta del módulo:

   ```bash
   go get -tool go.uber.org/mock/mockgen@v0.6.0
   ```

2. Elige la dependencia cuya **interacción** importa: en el museo, el notificador (que las condiciones se envíen una vez, con los datos exactos, y nunca en un préstamo rechazado). Pon la directiva encima de su interfaz:

   ```go
   //go:generate go tool mockgen -destination=mock_notifier_test.go -package=museum . Notifier
   ```

3. Genera el mock y ordena el `go.mod`:

   ```bash
   go generate ./...
   go mod tidy
   ```

   `go mod tidy` pasa go-cmp y gomock de `// indirect` a dependencias directas, ahora que los tests las importan.

4. En `TestLend`, añade el `EXPECT()` con la llamada exacta.
5. Sustituye `TestLendRejects` por una tabla con cada rechazo. **Sin `EXPECT`**: si algún caso rechazado llama al notificador, el mock hace fallar el test. Comprueba también que el fake no cambió.
6. Añade un test en que el mock devuelve un error y la operación lo propaga (`errors.Is`).

Referencia: [lab 02](../2-practicas/lab-02-mocks-con-mockgen/README.md).

### Paso 6: el golden file

1. Escribe una función que devuelva el texto que envía tu servicio: en el museo, `RenderNotice(n LoanNotice) string`, con la pieza, la institución, la duración, la fecha de vencimiento y el seguro.
2. Copia `assertGolden` y el flag `update` del [lab 04](../2-practicas/lab-04-go-cmp-y-golden-files/README.md#paso-3-el-texto-contra-un-golden-file-paso-3) y sustituye `TestRenderNotice`.
3. Crea el golden file. Nombra tu paquete antes de `-update`:

   ```bash
   go test -count=1 ./museum -update
   # ok  	example.com/qa-museo/go/museum	0.005s
   cat museum/testdata/TestRenderNotice.golden
   # Préstamo de la pieza CER-001
   # Institución: MALI
   # Duración: 30 días, hasta el 2026-04-09
   # Seguro: 2700
   ```

   **Lee el archivo y comprueba cada dato a mano** antes del commit. En el museo: 30 días desde el 10 de marzo es el 9 de abril, y el seguro de una pieza de 90 000 durante 30 días es 90 000 × 30 / 1000 = 2700.

### Paso 7: dobles.md

Completa `go/dobles.md`:

- la tabla con cada dependencia, su interfaz, el doble que la sustituye en los tests, el tipo (fake, stub, spy o mock) y por qué;
- cuándo eliges un fake y cuándo un mock, con tus dobles como ejemplo;
- algo que encontraste al portar el servicio.

No dejes ningún `TODO`.

### Paso 8: verificar y entregar

```bash
go vet ./...
go test -count=1 -shuffle=on ./...
# ok  	example.com/qa-museo/go/museum	0.005s
```

Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 33
```

Haz commit (incluidos el mock generado y el golden file), sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `go vet` no encuentra problemas | Paso 8 |
| La suite Go pasa con `-count=1` y en orden aleatorio | Paso 8 |
| No queda ningún `t.Skip` en los tests | Pasos 4 a 6 |
| El código de producción no llama a `time.Now()`: el reloj se inyecta | Paso 3 |
| Hay al menos un fake escrito a mano (`type fakeXxx`) | Paso 4 |
| mockgen está registrado como herramienta en `go.mod` | Paso 5 |
| El mock se genera con una directiva `//go:generate` | Paso 5 |
| Hay un mock generado por mockgen y los tests lo usan con `EXPECT()` | Paso 5 |
| Los tests comparan estructuras con `cmp.Diff` | Paso 4 |
| Hay al menos un golden file en `testdata/` y un flag `-update` | Paso 6 |
| `dobles.md` justifica cada doble (fake y mock), sin TODO | Paso 7 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - go-cmp y golden files](../2-practicas/lab-04-go-cmp-y-golden-files/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 33](../README.md)
