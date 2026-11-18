# 03 - Helpers, caja negra y examples

> Lenguaje: **Go**

Go no tiene fixtures ni `@BeforeEach`. El *setup* es una función normal que recibe `t`, y el *teardown* se registra con `t.Cleanup`. Esta teoría cubre esas funciones, los dos tipos de paquete de test y los examples, que son documentación que `go test` comprueba.

---

## 🎯 Objetivos

- Escribir helpers con `t.Helper` y liberar recursos con `t.Cleanup`.
- Usar `t.TempDir` y `t.Context` en lugar de crear el directorio y el contexto a mano.
- Elegir entre un test de caja blanca (`package x`) y uno de caja negra (`package x_test`).
- Escribir un example que `go test` ejecuta y compara con su salida.

---

## Helpers: `t.Helper`

Un helper es una función de test que recibe `t` y puede hacer fallar el test:

```go
func assertLines(t *testing.T, got, want []string) {
	t.Helper()
	if !slices.Equal(got, want) {
		t.Errorf("lines = %q, want %q", got, want)
	}
}
```

**Qué:** `t.Helper()` marca la función como helper.
**Para qué:** que el mensaje de fallo diga la línea del test que llamó al helper, no la línea del `t.Errorf` dentro del helper.
**Impacto:** sin `t.Helper()`, todos los fallos de `assertLines` apuntan a la misma línea y no sabes qué test ni qué llamada falló ([lab 03](../2-practicas/lab-03-helpers-tempdir-y-cleanup/README.md#experimento-1-sin-thelper)).

Por convención, el helper recibe `t` como primer argumento y usa `t.Fatalf` cuando no puede seguir, como un archivo que no se puede abrir.

---

## Recursos: `t.Cleanup`, `t.TempDir` y `t.Context`

| Función | Qué hace | Equivalente que ya conoces |
|---|---|---|
| `t.Cleanup(f)` | Ejecuta `f` cuando termina el test, aunque falle. Varias se ejecutan en orden inverso | `afterEach`, `yield` en una fixture, `@AfterEach` |
| `t.TempDir()` | Crea un directorio nuevo y lo borra al terminar el test | `tmp_path` en pytest, `@TempDir` en JUnit ([semana 25](../../week-25-junit_6_avanzado_y_assertj/1-teoria/03-tempdir-y-extensiones.md)) |
| `t.Context()` | Un `context.Context` que se cancela justo antes de las funciones de `t.Cleanup` | — |
| `t.Setenv(k, v)` | Cambia una variable de entorno y la restaura al terminar | `monkeypatch.setenv` |

### `t.Cleanup` en lugar de `defer`

`defer` ejecuta la función cuando **termina la función donde está**. En un helper, eso es demasiado pronto:

```go
func openReport(t *testing.T, path string) *os.File {
	t.Helper()
	f, err := os.Open(path)
	if err != nil {
		t.Fatalf("opening report: %v", err)
	}
	t.Cleanup(func() { f.Close() }) // con defer f.Close(), el archivo ya llega cerrado al test
	return f
}
```

Con `t.Cleanup`, el helper es dueño de lo que crea: quien lo llama no tiene que acordarse de cerrar nada. `t.TempDir` y `t.Setenv` funcionan así por dentro.

### `t.Context`

Una función que recibe un `context.Context` necesita uno en el test. `t.Context()` da uno ligado al test: se cancela cuando el test termina, así que una goroutine que el test arrancó con ese contexto se para sola. Para probar qué hace el código con un contexto cancelado, se deriva uno y se cancela:

```go
ctx, cancel := context.WithCancel(t.Context())
cancel()
_, err := WriteOverdue(ctx, t.TempDir(), loans)
// errors.Is(err, context.Canceled)
```

---

## Caja blanca y caja negra

Un `_test.go` puede declarar uno de dos paquetes:

| | Caja blanca | Caja negra |
|---|---|---|
| Cabecera | `package catalog` | `package catalog_test` |
| Ve lo no exportado (en minúscula) | Sí | No: `undefined: catalog.normalize` |
| Cómo llama al código | `normalize("Dune")` | `catalog.Add(...)`, importando `bootcamp/catalog` |
| Para qué | Probar un detalle interno difícil de alcanzar desde fuera | Probar la API como la usará otro paquete |

En Go, lo que empieza por mayúscula se exporta; lo demás solo se ve dentro del paquete. Un test de caja negra no puede depender de lo interno, así que sobrevive a un cambio de implementación. Los dos tipos conviven en la misma carpeta, y `go test` los compila juntos.

La regla práctica: caja negra por defecto, y caja blanca solo para una función interna que tiene lógica propia, como `normalize` en el [lab 04](../2-practicas/lab-04-caja-negra-y-examples/README.md).

Si un test de caja negra necesita algo interno, la convención es un archivo `export_test.go` en `package catalog` con `var Normalize = normalize`. Como acaba en `_test.go`, solo existe al ejecutar los tests.

---

## Examples: documentación que se ejecuta

```go
func ExampleCatalog_ByAuthor() {
	var c catalog.Catalog
	c.Add(catalog.Book{Title: "Persuasión", Author: "Jane Austen"})
	c.Add(catalog.Book{Title: "Emma", Author: "Jane Austen"})
	fmt.Println(c.ByAuthor("jane austen"))
	// Output: [Emma Persuasión]
}
```

**Qué:** una función `Example...` en un `_test.go`. `go test` la ejecuta y compara lo que imprime con el comentario `// Output:`.
**Para qué:** que el ejemplo de la documentación no se quede viejo. `pkg.go.dev` lo muestra junto a la función que documenta.
**Impacto:** si la salida no coincide, el example falla con `got:` y `want:`. Sin el comentario `// Output:`, el example se compila pero **no se ejecuta**, y no aparece en `-v`.

El nombre dice qué documenta, y `go vet` lo comprueba:

| Nombre | Documenta |
|---|---|
| `Example` | El paquete |
| `ExampleLateFee` | La función `LateFee` |
| `ExampleCatalog` | El tipo `Catalog` |
| `ExampleCatalog_ByAuthor` | El método `ByAuthor` de `Catalog` |

Con `// Unordered output:` el orden de las líneas no cuenta, para salidas que vienen de un mapa. Es el equivalente de los doctests de Python, pero en un archivo de test y no dentro del comentario.

---

## ✅ Resumen

- Un helper llama a `t.Helper()` y registra su limpieza con `t.Cleanup`, no con `defer`.
- `t.TempDir`, `t.Context` y `t.Setenv` limpian solos al terminar el test.
- `package x_test` prueba la API pública; `package x` también ve lo interno.
- Un `ExampleXxx` con `// Output:` es documentación que `go test` comprueba. Sin `// Output:` no se ejecuta.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Table-driven tests y subtests](02-table-driven-y-subtests.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 32](../README.md)
