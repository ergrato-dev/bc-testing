# 02 - TestMain, build tags y -short

> Lenguaje: **Go**

Separar los tests rápidos de los lentos ya lo hiciste con los markers de pytest en la [semana 17](../../week-17-parametrizacion_y_marks_con_pytest/README.md) y con Surefire y Failsafe en la [semana 31](../../week-31-proyecto_integrador_java/README.md): los `*Test` en cada build, los `*IT` antes de integrar. Go no tiene nada que configurar: usa dos mecanismos de la propia herramienta, `-short` y las build tags. Y para preparar algo una sola vez por paquete, como un contenedor, tiene `TestMain`.

---

## 🎯 Objetivos

- Preparar y liberar un recurso compartido por todos los tests de un paquete con `TestMain`.
- Saltar los tests lentos con `-short` y `testing.Short()`.
- Separar los tests de integración con `//go:build integration` y comprobar qué compila cada combinación.

---

## 1. TestMain

Si un paquete de test define esta función, `go test` la llama a ella en lugar de ejecutar los tests directamente:

```go
func TestMain(m *testing.M) {
	srv := httptest.NewServer(NewHandler(NewMemStore())) // una vez, antes de todos
	baseURL = srv.URL

	m.Run() // ejecuta los tests del paquete

	srv.Close() // una vez, después de todos
}
```

| Regla | Por qué |
|---|---|
| Una sola `TestMain` por paquete | Es una función normal del paquete: dos dan `multiple definitions of TestMain` |
| No hay `t`: ni `t.Cleanup`, ni `t.Fatal` | Los errores se tratan con `log.Fatal`, y la limpieza se escribe después de `m.Run()` |
| No hace falta `os.Exit(m.Run())` | Desde Go 1.15, si `TestMain` termina, `go test` usa el resultado de `m.Run()`. Con `os.Exit`, los `defer` no se ejecutan |
| Los flags se leen dentro de `m.Run()` | Antes, `testing.Short()` hace `panic: testing: Short called before Parse`. Si los necesitas antes, llama a `flag.Parse()` |

Lo que `TestMain` prepara lo **comparten** todos los tests del paquete. Eso ahorra tiempo de arranque, pero cada test tiene que dejar el recurso como lo encontró. Si no, el resultado depende del orden y de `-count` ([experimento 3 del lab 03](../2-practicas/lab-03-testmain-build-tags-y-short/README.md#experimento-3-el-mismo-servidor-en-dos-rondas)). La [teoría 03](03-testcontainers-go-y-postgresql.md#3-aislar-cada-test) resuelve ese problema para una base de datos.

Equivalencias: `TestMain` es el `@BeforeAll`/`@AfterAll` de JUnit, o una fixture de pytest con `scope="session"`, pero para todo el paquete y escrita como código normal.

---

## 2. Build tags

Una línea al principio del archivo, antes de `package` y seguida de una línea en blanco, decide si el archivo se compila:

```go
//go:build integration

package pgstore
```

| Comando | Compila el archivo |
|---|---|
| `go test ./...` | No |
| `go test -tags integration ./...` | Sí |
| `go vet ./...` | No: **tampoco lo revisa** |
| `go vet -tags integration ./...` | Sí |

Sin la tag, el archivo no existe para el compilador: ni sus tests, ni su `TestMain`, ni sus imports. Por eso un paquete con todos sus tests detrás de la tag responde `[no test files]`, y `go test ./...` funciona en una máquina sin Docker.

La expresión admite `&&`, `||` y `!`: `//go:build integration && linux`. El nombre de la tag es libre, y eso tiene un riesgo: una tag mal escrita (`integraton`) no da ningún error y los tests desaparecen en silencio. Comprueba la lista de tests que compila cada combinación con `-list`:

```bash
go test -tags integration -list . ./...
```

`-list` no ejecuta los tests, pero sí `TestMain`. Si `TestMain` arranca un contenedor, listar con la tag también lo arranca ([experimento 4 del lab 03](../2-practicas/lab-03-testmain-build-tags-y-short/README.md#experimento-4-una-tag-mal-escrita)).

---

## 3. -short

`-short` es un flag de `go test`, y `testing.Short()` dice si se pasó. Cada test decide qué hacer:

```go
func TestManyClients(t *testing.T) {
	if testing.Short() {
		t.Skip("slow: 2000 requests over TCP")
	}
	// ...
}
```

Con `-v`, el test aparece como `--- SKIP` junto con el motivo, así que se ve qué se saltó.

---

## Cuál usar

| | `-short` | Build tag |
|---|---|---|
| El test se compila | Siempre | Solo con la tag |
| Por defecto, el test... | Se ejecuta. Hay que pedir `-short` para saltarlo | No existe. Hay que pedir la tag para tenerlo |
| Sirve para | Tests lentos que no necesitan nada externo | Tests que necesitan algo que puede no estar: Docker, una red, credenciales |
| Puede tener su propia `TestMain` | No: la `TestMain` del paquete es una | Sí, si el paquete no tiene otra sin tag |

En el proyecto de la semana se usan los dos:

```bash
go test -short ./...                  # mientras programas: sin Docker, en segundos
go test -tags integration ./...       # antes del pull request y en CI: todo
```

La [semana 45](../../../docs/plan-estudios.md) lleva estos dos comandos a un pipeline de GitHub Actions.

---

## Resumen

| Concepto | Para qué |
|---|---|
| `func TestMain(m *testing.M)` | Preparar y liberar una vez por paquete lo que comparten los tests |
| `m.Run()` | Ejecutar los tests y leer los flags; su resultado decide el código de salida |
| `//go:build integration` | Compilar un archivo solo con `-tags integration` |
| `go vet -tags integration ./...` | Revisar también los archivos con tag |
| `go test -list .` | Ver qué tests compila cada combinación de tags |
| `testing.Short()` + `t.Skip` | Saltar un test lento con `-short` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 01 - httptest: handlers y clientes](01-httptest-handlers-y-clientes.md) | [Teoría — 03 - testcontainers-go y PostgreSQL](03-testcontainers-go-y-postgresql.md) → |

[↑ Semana 34](../README.md)
