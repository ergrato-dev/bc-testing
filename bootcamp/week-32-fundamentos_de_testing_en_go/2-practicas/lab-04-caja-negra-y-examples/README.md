# Lab 04 - Caja negra y examples

## Objetivo

Probar el mismo paquete desde dentro (`package catalog`) y desde fuera (`package catalog_test`), ver qué deja de compilar desde fuera, y escribir examples que `go test` ejecuta y compara con su salida.

## Tiempo estimado

55 minutos.

## Preparación

```bash
cd starter
go test ./...
# ok  	bootcamp/catalog	0.002s [no tests to run]
```

`catalog.go` es un catálogo de libros en memoria:

| Nombre | Exportado | Qué hace |
|---|---|---|
| `Catalog`, `Book` | Sí | El catálogo y un libro. `var c catalog.Catalog` ya está listo para usarse |
| `Add(b)` | Sí | Añade un libro. Devuelve `ErrDuplicate` si el título ya está, sin contar mayúsculas ni espacios |
| `ByAuthor(author)` | Sí | Los títulos del autor, en orden alfabético |
| `normalize(s)` | No | Minúsculas y un solo espacio entre palabras. Lo usan `Add` y `ByAuthor` |

Lo que empieza por minúscula solo se ve dentro del paquete `catalog` ([teoría 03](../../1-teoria/03-helpers-y-caja-negra.md#caja-blanca-y-caja-negra)).

Los tres archivos de test empiezan con su cláusula `package` sin comentar, para que el paquete compile en cada paso.

## Paso a paso

### Paso 1: caja blanca (PASO 1)

1. En `normalize_test.go`, descomenta todo lo que hay debajo del comentario del PASO 1. El archivo es `package catalog`: el mismo paquete que el código.
2. Ejecuta:

   ```bash
   go test -v -run TestNormalize ./...
   # --- PASS: TestNormalize (0.00s)
   #     --- PASS: TestNormalize/lowercases (0.00s)
   #     --- PASS: TestNormalize/trims (0.00s)
   #     --- PASS: TestNormalize/collapses_inner_spaces (0.00s)
   #     --- PASS: TestNormalize/empty (0.00s)
   ```

El test llama a `normalize` directamente. Puede, porque está dentro del paquete.

### Paso 2: caja negra (PASO 2)

1. En `catalog_test.go`, descomenta todo lo que hay debajo del PASO 2. El archivo es `package catalog_test` e importa el paquete por su ruta, `bootcamp/catalog`, que es el nombre del módulo en `go.mod`.
2. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # --- PASS: TestNormalize (0.00s)
   # --- PASS: TestAddRejectsSameTitleWithOtherSpacing (0.00s)
   # --- PASS: TestByAuthorIgnoresCase (0.00s)
   # ok  	bootcamp/catalog	0.003s
   ```

Los dos tests usan el paquete como lo usaría otro programa: `catalog.Catalog`, `catalog.Book`, `catalog.ErrDuplicate`. Prueban que `normalize` funciona a través de `Add` y `ByAuthor`, sin nombrarla.

`go list` muestra cómo los separa `go test`:

```bash
go list -f '{{.TestGoFiles}} {{.XTestGoFiles}}' .
# [normalize_test.go] [catalog_test.go example_test.go]
```

`TestGoFiles` son los de caja blanca; `XTestGoFiles` (de *external*), los de caja negra. `example_test.go` ya cuenta aunque todavía esté comentado: lo que decide es su cláusula `package catalog_test`.

### Paso 3: examples (PASO 3)

1. En `example_test.go`, descomenta todo lo que hay debajo del PASO 3.
2. Ejecuta solo los examples:

   ```bash
   go test -count=1 -run Example -v ./...
   # --- PASS: ExampleCatalog_ByAuthor (0.00s)
   # --- PASS: ExampleCatalog_Add (0.00s)
   # ok  	bootcamp/catalog	0.003s
   ```

Un example no recibe `t` ni comprueba nada con `if`: imprime con `fmt.Println`, y `go test` compara lo que salió con el comentario `// Output:`. `ExampleCatalog_ByAuthor` documenta el método `ByAuthor` del tipo `Catalog`; `ExampleCatalog_Add`, el método `Add`. Si publicas el módulo, `pkg.go.dev` los muestra en la documentación de cada método.

## Experimentos

### Experimento 1: lo interno desde fuera

Crea `outside_test.go` con un test de caja negra que llama a `normalize`:

```go
package catalog_test

import (
	"testing"

	"bootcamp/catalog"
)

func TestNormalizeFromOutside(t *testing.T) {
	_ = catalog.normalize("Dune")
}
```

```bash
go test ./...
# # bootcamp/catalog_test [bootcamp/catalog.test]
# ./outside_test.go:10:14: undefined: catalog.normalize
# FAIL	bootcamp/catalog [build failed]
```

Desde fuera, `normalize` no existe. Es lo que verá cualquier paquete que use `catalog`. Borra el archivo.

### Experimento 2: un example que no coincide

En `ExampleCatalog_ByAuthor`, cambia el comentario por `// Output: [Persuasión Emma]`:

```bash
go test ./...
# --- FAIL: ExampleCatalog_ByAuthor (0.00s)
# got:
# [Emma Persuasión]
# want:
# [Persuasión Emma]
# FAIL
```

El example falla como un test: la documentación decía un orden que el código no cumple. Deshaz el cambio.

### Experimento 3: sin `// Output:`

Borra la línea `// Output: [Emma Persuasión]`:

```bash
go test -count=1 -v ./...
# --- PASS: TestNormalize (0.00s)
# --- PASS: TestAddRejectsSameTitleWithOtherSpacing (0.00s)
# --- PASS: TestByAuthorIgnoresCase (0.00s)
# --- PASS: ExampleCatalog_Add (0.00s)
# ok  	bootcamp/catalog	0.004s
```

`ExampleCatalog_ByAuthor` ya no aparece. Se compila, así que no puede usar una función que no existe, pero no se ejecuta. Deshaz el cambio.

### Experimento 4: un nombre que no documenta nada

Renombra `ExampleCatalog_Add` a `ExampleCatalog_Insert`:

```bash
go test ./...
# ./example_test.go:18:1: ExampleCatalog_Insert refers to unknown field or method: Catalog.Insert
```

`go vet` comprueba que el nombre corresponde a algo del paquete. Deshaz el cambio.

## Comprobación final

```bash
go vet ./... && go test -count=1 ./...
# ok  	bootcamp/catalog	0.003s
```

## Preguntas de cierre

1. Cambias `normalize` para que también quite las tildes. ¿Qué tests tendrías que tocar: los de caja blanca, los de caja negra o los examples?
2. ¿Qué ventaja tiene un example frente a un bloque de código en el README del paquete?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Helpers, t.TempDir y t.Cleanup](../lab-03-helpers-tempdir-y-cleanup/README.md) | [Proyecto — Proyecto Semanal - Las reglas del dominio en Go](../../3-proyecto/README.md) → |

[↑ Semana 32](../../README.md)
