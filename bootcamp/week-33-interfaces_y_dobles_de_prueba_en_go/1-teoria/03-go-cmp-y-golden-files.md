# 03 - go-cmp y golden files

> Lenguaje: **Go**

Comparar dos números es `got != want`. Comparar un resumen con diez campos y una lista de filas pide dos cosas que la stdlib no da bien: un mensaje que diga **qué campo** cambió, y una forma de guardar una salida larga, como un informe de texto, sin pegarla en el código del test.

---

## 🎯 Objetivos

- Comparar estructuras con `cmp.Diff` y leer su salida.
- Ajustar la comparación con `cmpopts`: campos no exportados y listas vacías.
- Probar una salida de texto contra un golden file en `testdata/` y regenerarlo con `-update`.

---

## `cmp.Diff`

```go
import "github.com/google/go-cmp/cmp"

if diff := cmp.Diff(want, got); diff != "" {
	t.Errorf("Build() mismatch (-want +got):\n%s", diff)
}
```

**Qué:** `cmp.Diff` devuelve una cadena vacía si los dos valores son iguales, y si no, las diferencias campo a campo: `-` lo esperado, `+` lo obtenido.
**Para qué:** que el mensaje del fallo señale el campo que cambió, aunque el valor tenga muchos.
**Impacto:** con `reflect.DeepEqual` solo sabes que algo es distinto, y el mensaje imprime los dos valores enteros para que busques la diferencia a ojo ([lab 04, experimento 1](../2-practicas/lab-04-go-cmp-y-golden-files/README.md#experimento-1-reflectdeepequal)).

`go-cmp` es una librería de Google pensada para tests. Por convención, el orden es `(want, got)` y el mensaje dice `(-want +got)`.

### Opciones de `cmpopts`

`cmp` es más estricto que `reflect.DeepEqual` en dos casos, y los dos se resuelven con una opción del paquete `cmpopts`:

| Situación | Sin opción | Opción |
|---|---|---|
| El tipo tiene campos no exportados | `panic: cannot handle unexported field` | `cmpopts.IgnoreUnexported(Line{})` los ignora |
| Se compara `[]Line{}` con `nil` | Distintos: `- Lines: []report.Line{}` `+ Lines: nil` | `cmpopts.EquateEmpty()` los trata como iguales |
| Un campo no importa en este test | Aparece en el diff | `cmpopts.IgnoreFields(Summary{}, "Featured")` |

El `panic` de los no exportados es a propósito: un campo interno es un detalle de implementación, y `cmp` te obliga a decidir si cuenta. Es como `usingRecursiveComparison().ignoringFields(...)` de AssertJ ([semana 25](../../week-25-junit_6_avanzado_y_assertj/1-teoria/04-assertj-y-soft-assertions.md)).

---

## Golden files

Un golden file es un archivo con la salida esperada de un test. El test compara lo que produce el código con ese archivo, y una opción del test lo reescribe cuando la salida cambia a propósito. Es el snapshot testing de Jest ([semana 13](../../week-13-snapshot_y_property_based_testing/1-teoria/01-snapshot-testing-con-intencion.md)), sin librería: un flag y dos funciones de `os`.

```go
var update = flag.Bool("update", false, "reescribe los golden files de testdata/")

func assertGolden(t *testing.T, got string) {
	t.Helper()
	path := filepath.Join("testdata", t.Name()+".golden")
	if *update {
		// crea testdata/ si no existe y escribe got en el archivo
	}
	want, err := os.ReadFile(path)
	if err != nil {
		t.Fatalf("reading golden file: %v (run go test -update)", err)
	}
	if diff := cmp.Diff(string(want), got); diff != "" {
		t.Errorf("%s mismatch (-want +got):\n%s", path, diff)
	}
}
```

| Pieza | Por qué así |
|---|---|
| `testdata/` | `go build` ignora las carpetas que se llaman `testdata`, y `go test` ejecuta el test con el directorio del paquete como directorio actual: la ruta relativa funciona |
| `t.Name() + ".golden"` | Un archivo por test, con el nombre del test |
| `flag.Bool("update", ...)` | Un flag propio del binario de test. `go test` le pasa los flags que no conoce |

### El flujo

1. La primera vez no hay archivo: el test falla con `no such file or directory (run go test -update)`.
2. `go test . -update` lo crea con la salida actual.
3. **Lees el archivo** antes de hacer commit: ahora es lo esperado.
4. Si un cambio del código altera la salida, el test falla con el diff. Si el cambio es correcto, `-update` y revisas el diff en git; si no, has encontrado un bug.

El paso 3 es el que hace útil al golden file. Un `-update` sin leer el resultado acepta cualquier salida, igual que un `jest -u` a ciegas.

### Dónde va `-update`

`go test` deja de leer paquetes en el primer flag que no conoce. Con `go test -update ./...`, `./...` se pasa al binario como argumento y `go test` prueba solo el directorio actual. Pon primero los paquetes y después el flag, y nombra el paquete que tiene el golden file:

```bash
go test ./museum -update
```

Con `go test ./... -update`, cada paquete recibe `-update`, y un paquete que no lo declara falla con `flag provided but not defined: -update`.

---

## ✅ Resumen

- `cmp.Diff(want, got)` devuelve `""` si son iguales y si no, el diff campo a campo.
- `cmpopts.IgnoreUnexported`, `EquateEmpty` e `IgnoreFields` ajustan qué cuenta como igual.
- Un golden file en `testdata/` guarda la salida esperada; `-update` lo reescribe y tú lo revisas antes del commit.
- `-update` va después del paquete: `go test ./museum -update`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Mocks con mockgen y aserciones con testify](02-mockgen-y-testify.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 33](../README.md)
