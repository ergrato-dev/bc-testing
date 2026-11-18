# Lab 01 - Primer test con go test

## Objetivo

Escribir los primeros tests de Go para una función de multas, ejecutarlos con `go test` y sus opciones, y provocar los fallos que más vas a ver: un `t.Errorf` frente a un `t.Fatalf`, un aviso de `go vet` y un test que nadie ejecuta.

## Tiempo estimado

55 minutos.

## Preparación

```bash
go version
# go version go1.26.5 linux/amd64
cd starter
go test ./...
# ok  	bootcamp/lab01	0.002s [no tests to run]
```

Qué hay en `starter/`:

| Archivo | Qué es |
|---|---|
| `go.mod` | El módulo `bootcamp/lab01` con `go 1.26.5` ([teoría 01](../../1-teoria/01-go-test-y-el-paquete-testing.md#módulo-paquete-y-archivo-de-test)) |
| `fees.go` | `LateFee(daysLate int) (int, error)`: 50 por día de retraso, hasta 1000. Con días negativos devuelve `ErrNegativeDays` |
| `fees_test.go` | Tres tests comentados por PASOS |

`[no tests to run]` significa que el paquete compila pero no tiene ninguna función `Test...` activa.

## Paso a paso

### Paso 1: el primer test (PASO 1)

1. En `fees_test.go`, descomenta el bloque del PASO 1: el `import` y `TestLateFeeThreeDays`. La línea `// "errors"` del import queda comentada hasta el PASO 3.
2. Ejecuta:

   ```bash
   go test ./...
   # ok  	bootcamp/lab01	0.003s
   ```

3. Con `-v` ves cada test:

   ```bash
   go test -v ./...
   # === RUN   TestLateFeeThreeDays
   # --- PASS: TestLateFeeThreeDays (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.003s
   ```

Lee el test: `LateFee` devuelve dos valores, la multa y un error. Primero se comprueba el error con `t.Fatalf`, porque si hay error la multa no vale nada. Después se compara la multa con un `if` y `t.Errorf`. No hay `expect` ni `assertThat`: el mensaje sigue la convención de Go, `LateFee(3) = obtenido, want 150`.

### Paso 2: la caché (PASO 2)

1. Descomenta `TestLateFeeIsCapped`. Comprueba el tope con la constante `MaxFee` del código, no con un 1000 escrito a mano.
2. Ejecuta dos veces seguidas:

   ```bash
   go test ./...
   # ok  	bootcamp/lab01	0.003s
   go test ./...
   # ok  	bootcamp/lab01	(cached)
   ```

   La segunda vez no se ejecuta nada: nada cambió, y `go test` repite el resultado guardado.

3. Para ejecutar de verdad:

   ```bash
   go test -count=1 ./...
   # ok  	bootcamp/lab01	0.002s
   ```

### Paso 3: el error esperado (PASO 3)

1. Descomenta la línea `// "errors"` del import y `TestLateFeeRejectsNegativeDays`.
2. Ejecuta solo ese test con `-run`, que recibe una expresión regular:

   ```bash
   go test -run Negative -v ./...
   # === RUN   TestLateFeeRejectsNegativeDays
   # --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.003s
   ```

`errors.Is(err, ErrNegativeDays)` comprueba que el error es ese en concreto, no uno cualquiera. Es el `pytest.raises(ValueError)` de Go.

### Paso 4: go vet

```bash
go vet ./...
```

No imprime nada: no hay avisos. Ejecútalo antes de cada commit.

## Experimentos

### Experimento 1: Errorf o Fatalf

1. En `fees.go`, cambia `daysLate < 0` por `daysLate < 5`. Ahora `LateFee(3)` devuelve un error.

   ```bash
   go test ./...
   # --- FAIL: TestLateFeeThreeDays (0.00s)
   #     fees_test.go:12: LateFee(3) returned error: days late cannot be negative
   # FAIL
   ```

   Un mensaje: `t.Fatalf` terminó el test.

2. En `TestLateFeeThreeDays`, cambia `t.Fatalf` por `t.Errorf`:

   ```bash
   go test ./...
   # --- FAIL: TestLateFeeThreeDays (0.00s)
   #     fees_test.go:12: LateFee(3) returned error: days late cannot be negative
   #     fees_test.go:15: LateFee(3) = 0, want 150
   # FAIL
   ```

   El segundo mensaje sobra: compara un valor que no significa nada porque hubo error. Por eso el error se comprueba con `t.Fatalf`. Deshaz los dos cambios.

### Experimento 2: go vet dentro de go test

En `TestLateFeeThreeDays`, borra el argumento `, got` del `t.Errorf`:

```bash
go test ./...
# # bootcamp/lab01
# # [bootcamp/lab01]
# ./fees_test.go:15:26: (*testing.common).Errorf format %d reads arg #1, but call has 0 args
# FAIL	bootcamp/lab01 [build failed]
```

El código compila, pero `go test` ejecuta parte de `go vet` antes de los tests y no llega a ejecutarlos. El fallo no está en `LateFee` sino en el mensaje del test. Deshaz el cambio.

### Experimento 3: nombres que no son tests

1. Renombra `TestLateFeeIsCapped` a `TestlateFeeIsCapped`, con la `l` minúscula:

   ```bash
   go test ./...
   # ./fees_test.go:20:6: TestlateFeeIsCapped has malformed name: first letter after 'Test' must not be lowercase
   ```

   `go vet` lo detecta: después de `Test` va una mayúscula.

2. Ahora renómbralo a `CheckLateFeeIsCapped`:

   ```bash
   go test -v ./...
   # === RUN   TestLateFeeThreeDays
   # --- PASS: TestLateFeeThreeDays (0.00s)
   # === RUN   TestLateFeeRejectsNegativeDays
   # --- PASS: TestLateFeeRejectsNegativeDays (0.00s)
   # PASS
   # ok  	bootcamp/lab01	0.002s
   ```

   Dos tests y ningún aviso: `CheckLateFeeIsCapped` es una función normal que nadie llama. Es el `*Check` que Surefire ignoraba en la [semana 31](../../../week-31-proyecto_integrador_java/2-practicas/lab-01-test-e-it-con-surefire-y-failsafe/README.md#experimento-1-un-nombre-sin-sufijo). Deshaz el cambio.

### Experimento 4: sin go.mod

Mueve `go.mod` fuera de la carpeta y ejecuta:

```bash
mv go.mod ../go.mod.bak
go test ./...
# # ./...
# pattern ./...: directory prefix . does not contain main module or its selected dependencies
# FAIL	./... [setup failed]
mv ../go.mod.bak go.mod
```

Sin `go.mod` no hay módulo, y `go test` no sabe qué paquetes son tuyos. Para empezar un proyecto nuevo se crea con `go mod init <nombre>`.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/lab01	0.002s
```

## Preguntas de cierre

1. Un test hace `t.Errorf` en tres comprobaciones seguidas y la primera falla. ¿Cuántos mensajes ves? ¿Y con `t.Fatalf`?
2. Un test consulta un servicio por la red y el servicio cambia su respuesta, pero el código no cambia. ¿Qué puede pasar con `go test ./...` y qué opción lo evita?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Table-driven tests y subtests](../lab-02-table-driven-y-subtests/README.md) → |

[↑ Semana 32](../../README.md)
