# 01 - `go test` y el paquete `testing`

> Lenguaje: **Go**

Go es el primer lenguaje del bootcamp que trae el testing en la librería estándar: el paquete `testing` y el comando `go test` vienen con el compilador. No hay Jest, pytest ni JUnit que instalar. Esta teoría compara cada pieza con lo que ya conoces.

---

## 🎯 Objetivos

- Crear un módulo de Go y saber dónde van los tests.
- Escribir un test con `func TestXxx(t *testing.T)` y elegir entre `t.Errorf` y `t.Fatalf`.
- Ejecutar la suite con `go test` y sus opciones `-v`, `-run` y `-count=1`.
- Saber qué comprueba `go vet` y por qué `go test` ya ejecuta una parte.

---

## Módulo, paquete y archivo de test

Un **módulo** es un proyecto de Go. Lo define el archivo `go.mod`, que es el equivalente de `package.json`, `pyproject.toml` o `pom.xml`:

```bash
go mod init bootcamp/lab01
# go: creating new go.mod: module bootcamp/lab01
```

```text
module bootcamp/lab01

go 1.26.5
```

| Línea | Qué / Para qué / Impacto |
|---|---|
| `module bootcamp/lab01` | El nombre del módulo. Los tests de caja negra lo usan para importar el paquete ([teoría 03](03-helpers-y-caja-negra.md#caja-blanca-y-caja-negra)) |
| `go 1.26.5` | La versión mínima de Go del módulo. También decide la semántica del lenguaje: con `go 1.21` o anterior, los bucles `for` comparten la variable entre vueltas ([teoría 02](02-table-driven-y-subtests.md#tests-en-paralelo)) |

Cada carpeta es un **paquete**. Los tests van en la misma carpeta que el código, en archivos que terminan en `_test.go`:

```text
lab-01/
├── go.mod
├── fees.go         # package fees
└── fees_test.go    # package fees: solo lo compila go test
```

| | Jest | pytest | JUnit + Maven | Go |
|---|---|---|---|---|
| Dónde van los tests | `*.test.js` | `test_*.py` | `src/test/java/*Test.java` | `*_test.go`, junto al código |
| Qué funciones son tests | `test(...)` | `def test_*` | métodos con `@Test` | `func TestXxx(t *testing.T)` |
| Librería de aserciones | `expect` | `assert` | AssertJ | ninguna: `if` y `t.Errorf` |
| Comando | `pnpm test` | `uv run pytest` | `mvn test` | `go test ./...` |

`./...` significa "este paquete y todos los de debajo". Es lo que se ejecuta casi siempre.

---

## Un test en Go

```go
func TestLateFeeThreeDays(t *testing.T) {
	got, err := LateFee(3)
	if err != nil {
		t.Fatalf("LateFee(3) returned error: %v", err)
	}
	if got != 150 {
		t.Errorf("LateFee(3) = %d, want 150", got)
	}
}
```

- **El nombre:** `go test` ejecuta las funciones de los `_test.go` que empiezan por `Test` seguido de una mayúscula, un número o `_`. `go test` rechaza `TestlateFee` antes de ejecutar nada ([lab 01](../2-practicas/lab-01-primer-test-con-go-test/README.md#experimento-3-nombres-que-no-son-tests)); `CheckLateFee` no es un test y nadie lo ejecuta, como un `*Check` en Surefire ([semana 31](../../week-31-proyecto_integrador_java/2-practicas/lab-01-test-e-it-con-surefire-y-failsafe/README.md#experimento-1-un-nombre-sin-sufijo)).
- **No hay `assert`:** la comprobación es un `if` normal. El mensaje lo escribes tú, y la convención de Go es `Función(argumentos) = obtenido, want esperado`.
- **Los errores son valores:** `LateFee` devuelve `(int, error)` en lugar de lanzar una excepción. Un error esperado se compara con `errors.Is(err, ErrNegativeDays)`, como `assertThatThrownBy` en AssertJ o `pytest.raises`.

### `t.Errorf` o `t.Fatalf`

| Método | Marca el test como fallido | Sigue ejecutando el test |
|---|---|---|
| `t.Error`, `t.Errorf` | Sí | Sí: el test puede informar de varios fallos |
| `t.Fatal`, `t.Fatalf` | Sí | No: el test termina ahí |
| `t.Log`, `t.Logf` | No | Sí: solo se muestra si el test falla o con `-v` |

La regla: `t.Fatalf` cuando seguir no tiene sentido (si `LateFee` devolvió un error, `got` no vale nada), y `t.Errorf` para todo lo demás. En el [lab 01](../2-practicas/lab-01-primer-test-con-go-test/README.md#experimento-1-errorf-o-fatalf) ves los dos mensajes que da `t.Errorf` donde `t.Fatalf` da uno.

Jest, pytest y JUnit paran en la primera aserción que falla, salvo con las soft assertions de la [semana 25](../../week-25-junit_6_avanzado_y_assertj/README.md). Go deja elegir en cada línea.

---

## Ejecutar la suite

| Comando | Qué hace |
|---|---|
| `go test ./...` | Compila y ejecuta los tests de todos los paquetes. Una línea `ok` o `FAIL` por paquete |
| `go test -v ./...` | Muestra cada test con `=== RUN` y `--- PASS` o `--- FAIL`, y los `t.Log` |
| `go test -run Capped ./...` | Solo los tests cuyo nombre casa con la expresión regular `Capped` |
| `go test -count=1 ./...` | Ejecuta aunque haya un resultado en caché |

### La caché de resultados

```bash
go test ./...
# ok  	bootcamp/lab01	0.003s
go test ./...
# ok  	bootcamp/lab01	(cached)
```

**Qué:** si ni el código ni los tests cambiaron, `go test` repite el resultado anterior sin ejecutar nada.
**Para qué:** que `go test ./...` en un proyecto grande solo ejecute los paquetes que tocaste.
**Impacto:** la caché tiene en cuenta los archivos y las variables de entorno que lee el test, pero no la hora, la red ni una base de datos. Un test que depende de ellos puede salir en verde desde la caché. Con `-count=1` se ejecuta de verdad. Ningún otro runner del bootcamp hace esto por defecto.

---

## `go vet`

`go vet` busca errores que compilan pero casi seguro están mal: un `%d` sin argumento, una comparación que siempre da lo mismo, un test con un nombre que `go test` no reconoce.

```bash
go vet ./...
# fees_test.go:15:26: (*testing.common).Errorf format %d reads arg #1, but call has 0 args
```

`go test` ya ejecuta una parte de `go vet` antes de los tests, y si encuentra algo el paquete no llega a ejecutarse (`[build failed]`). `go vet ./...` ejecuta todas las comprobaciones. Es el linter que en Jest o pytest instalabas aparte.

---

## ✅ Resumen

- `go.mod` define el módulo; cada carpeta es un paquete, y sus tests van al lado en `*_test.go`.
- Un test es `func TestXxx(t *testing.T)`. Sin aserciones: `if` y un mensaje `got, want`.
- `t.Fatalf` para el test; `t.Errorf` marca el fallo y sigue.
- `go test ./...` con `-v`, `-run` y `-count=1`. Sin `-count=1`, el resultado puede venir de la caché.
- `go vet ./...` antes de cada commit; `go test` ya ejecuta una parte.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 32 - Fundamentos de Testing en Go](../README.md) | [Teoría — 02 - Table-driven tests y subtests](02-table-driven-y-subtests.md) → |

[↑ Semana 32](../README.md)
