# Proyecto Semanal - Las reglas del dominio en Go

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Empezar la carpeta `go/` del proyecto hilo conductor: portar a Go las reglas de tu dominio que no dependen de nada (validaciones y cálculos) y probarlas con lo que viste esta semana:

- table-driven tests con un subtest por caso, y al menos una tabla en paralelo;
- un test de caja negra con examples que documentan la API;
- una comparativa de la misma suite en Jest, pytest, JUnit y Go.

## Dónde va

En tu repositorio `qa-<nombre>-<apellido>` ([proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)) empieza esta semana la carpeta `go/`, que crecerá hasta la semana 36 con una API, PostgreSQL, fuzzing y benchmarks.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: el módulo

1. Crea la rama de la semana:

   ```bash
   cd qa-<nombre>-<apellido>
   git switch main && git pull
   git switch -c week-32
   ```

2. Crea la carpeta y el módulo. El nombre del módulo es la ruta de tu repositorio más `/go`:

   ```bash
   mkdir go && cd go
   go mod init github.com/<usuario>/qa-<nombre>-<apellido>/go
   # go: creating new go.mod: module github.com/<usuario>/qa-<nombre>-<apellido>/go
   cd ..
   ```

   El `go.mod` queda con `go 1.26.5`, o la versión que tengas instalada. Debe ser 1.22 o superior: con una anterior, los subtests paralelos comparten la variable del bucle ([lab 02](../2-practicas/lab-02-table-driven-y-subtests/README.md#experimento-2-go-121-y-la-variable-del-bucle)).

### Paso 2: copiar el starter

```bash
cp -rn <ruta-al-bootcamp>/bootcamp/week-32-fundamentos_de_testing_en_go/3-proyecto/starter/. .
```

Con `-n`, `cp` no sobrescribe nada que ya exista. El starter trae:

| Archivo | Qué es |
|---|---|
| `go/domain/domain.go` | El paquete vacío, con la doc del paquete y un TODO |
| `go/domain/domain_test.go` | Dos tests con `t.Skip("TODO: ...")`, el equivalente de `test.todo`, `pytest.skip` o `@Disabled` |
| `go/comparativa.md` | La plantilla de la comparativa |

Renombra la carpeta `domain` con el nombre de tu dominio en inglés, en minúsculas y sin guiones, y cambia la línea `package` de los dos archivos. Por ejemplo, para un museo:

```bash
mv go/domain go/museum
sed -i 's/^package domain$/package museum/' go/museum/*.go
cd go
go test -v ./...
# === RUN   TestValidate
#     domain_test.go:6: TODO: tabla con los casos de validación de tu entidad (paso 3)
# --- SKIP: TestValidate (0.00s)
# === RUN   TestCalculation
#     domain_test.go:10: TODO: tabla con un cálculo del dominio, con t.Parallel (paso 4)
# --- SKIP: TestCalculation (0.00s)
# PASS
# ok  	github.com/<usuario>/qa-<nombre>-<apellido>/go/museum	0.002s
```

Los dos tests aparecen como saltados (`SKIP`) hasta que tengan cuerpo.

### Paso 3: la validación (tabla 1)

1. Abre tu servicio Java de la [semana 31](../../week-31-proyecto_integrador_java/3-proyecto/README.md): `PieceService` o el nombre que le diste. Busca la validación de la entidad: formato del código, nombre obligatorio, longitud máxima, valor positivo.
2. Pórtala a una función de Go que devuelve un `error`. Cada motivo de rechazo es un error con nombre, declarado con `errors.New`:

   ```go
   var (
   	ErrInvalidCode  = errors.New("invalid code")
   	ErrNameRequired = errors.New("name is required")
   )

   func ValidatePiece(code, name string, declaredValue int) error
   ```

   Un error con nombre es lo que el test compara con `errors.Is`. Es el `ValidationException` de Java, pero distinto por cada motivo.

3. Sustituye `TestValidate` por un table-driven test con un campo `want error`. Incluye cada caso de rechazo, el caso válido y las fronteras: la longitud máxima exacta y una más, el valor 0 y el 1.
4. **Añade un nombre con tildes en la longitud máxima.** En Go, `len(s)` cuenta bytes, no caracteres, y una `á` ocupa dos. Si tu primera versión usa `len`, este caso falla: usa `utf8.RuneCountInString`.

Referencia: [lab 02](../2-practicas/lab-02-table-driven-y-subtests/README.md).

### Paso 4: los cálculos (tablas 2 y siguientes)

1. Porta los cálculos que no dependen de un repositorio ni de la hora: un recargo, un seguro, un descuento, unos días de retraso. En el museo son `LateDays`, `LateFee`, `DamageFee` y `LoanPremium`.
2. Una función `TestXxx` con su tabla por cada cálculo. Sustituye `TestCalculation` por la primera.
3. En las tablas de cálculos, empieza cada subtest con `t.Parallel()`.
4. Si un cálculo devuelve error, el error esperado es una columna más de la tabla (`wantErr error`), con `t.Fatalf` si no coincide.
5. Si varios tests construyen el mismo dato, como una fecha a partir de un texto, sácalo a un helper con `t.Helper()`.

La suite debe sumar **al menos 15 subtests** entre todas las tablas.

Referencia: [lab 02](../2-practicas/lab-02-table-driven-y-subtests/README.md) y [lab 03](../2-practicas/lab-03-helpers-tempdir-y-cleanup/README.md).

### Paso 5: caja negra y examples

1. Crea `go/<tu-paquete>/example_test.go` con `package <tu-paquete>_test`. Importa tu paquete por su ruta completa: `github.com/<usuario>/qa-<nombre>-<apellido>/go/museum`.
2. Escribe **al menos dos examples** de funciones exportadas, con `// Output:`. Elige los que mejor expliquen la regla, por ejemplo un caso normal y un error.

Referencia: [lab 04](../2-practicas/lab-04-caja-negra-y-examples/README.md).

### Paso 6: la comparativa

Completa `go/comparativa.md`. Elige un cálculo que tengas probado en Jest, pytest y JUnit, y escribe:

- el test parametrizado en los cuatro lenguajes, uno debajo de otro;
- la tabla con el mecanismo, el nombre del caso y cómo se ejecuta un solo caso en cada uno;
- cómo se comprueba un error esperado en cada uno;
- al menos un caso que apareció al portar a Go y que no tenías en las otras suites.

No dejes ningún `TODO`.

### Paso 7: verificar y entregar

```bash
cd go
go vet ./...
go test -count=1 -shuffle=on ./...
# ok  	github.com/<usuario>/qa-<nombre>-<apellido>/go/museum	0.004s
go test -count=1 -v ./... | grep -cE -- '--- PASS: [^ ]+/'
# 32
```

El número de subtests depende de tu dominio; debe ser 15 o más. Desde la raíz del bootcamp:

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 32
```

Haz commit, sube la rama y abre un pull request a `main`.

## ✅ Requisitos verificables

Copia de [checks.json](../checks.json):

| Comprobación | Pista |
|---|---|
| `go vet` no encuentra problemas | Pasos 1 y 7 |
| La suite Go pasa con `-count=1` y en orden aleatorio | Paso 7 |
| No queda ningún `t.Skip` en los tests | Pasos 3 y 4 |
| Al menos 3 table-driven tests con `t.Run` | Pasos 3 y 4 |
| Al menos 15 subtests que pasan | Pasos 3 y 4 |
| Los subtests de una tabla se ejecutan con `t.Parallel` | Paso 4 |
| Hay tests de caja negra (`package x_test`) | Paso 5 |
| Al menos 2 examples que se ejecutan y pasan | Paso 5 |
| `comparativa.md` compara Jest, pytest, JUnit y Go, sin TODO | Paso 6 |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Caja negra y examples](../2-practicas/lab-04-caja-negra-y-examples/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 32](../README.md)
