# Glosario Semana 32 - Fundamentos de Testing en Go

## C

- **Caché de tests**: `go test` guarda el resultado de cada paquete y lo repite, con `(cached)`, si no cambió nada de lo que el test usa. `-count=1` la salta.
- **Caja blanca** (*white-box test*): un `_test.go` con `package x`, el mismo paquete que el código. Ve lo no exportado.
- **Caja negra** (*black-box test*): un `_test.go` con `package x_test`. Importa el paquete por su ruta y solo ve lo exportado.

## E

- **`errors.Is`**: comprueba si un error es un error concreto, aunque venga envuelto. Es como se compara un error esperado en un test.
- **Error con nombre** (*sentinel error*): una variable `var ErrX = errors.New("...")` que el código devuelve y el test compara con `errors.Is`.
- **Example**: una función `ExampleXxx` en un `_test.go`. `go test` la ejecuta y compara lo que imprime con su comentario `// Output:`.
- **Exportado**: un nombre que empieza por mayúscula. Solo los exportados se ven desde otro paquete.

## G

- **`go.mod`**: el archivo que define un módulo: su nombre, la versión de Go y las dependencias.
- **`go vet`**: el analizador de la librería estándar. Busca errores que compilan, como un `%d` sin argumento o un nombre de test mal escrito. `go test` ejecuta una parte.

## M

- **Módulo**: un proyecto de Go, definido por su `go.mod`. Contiene uno o más paquetes.

## P

- **Paquete**: el código de una carpeta. Todos sus archivos declaran el mismo `package`, salvo los tests de caja negra.

## S

- **`-shuffle=on`**: ejecuta los tests de cada paquete en orden aleatorio e imprime la semilla para repetirlo.
- **Subtest**: un test creado con `t.Run(nombre, func)` dentro de otro. Tiene nombre propio, `TestX/nombre`, y su propio `t`.

## T

- **`t.Cleanup`**: registra una función que se ejecuta al terminar el test, aunque falle. Varias se ejecutan en orden inverso.
- **`t.Context`**: un contexto que se cancela justo antes de las funciones de `t.Cleanup`.
- **`t.Errorf`**: marca el test como fallido y sigue ejecutándolo.
- **`t.Fatalf`**: marca el test como fallido y lo termina.
- **`t.Helper`**: marca una función como helper, para que los fallos señalen la línea de quien la llamó.
- **`t.Parallel`**: permite que un test o subtest se ejecute a la vez que otros paralelos.
- **`t.TempDir`**: crea un directorio que se borra al terminar el test.
- **Table-driven test**: un test con una tabla de casos (un *slice* de `struct`) y un bucle que ejecuta cada uno, normalmente como subtest.
- **`tt`**: el nombre habitual de la fila de la tabla en el bucle, `for _, tt := range tests`.

## V

- **Variable del bucle**: desde Go 1.22, cada vuelta de un `for` tiene su propia variable. Con `go 1.21` o anterior en el `go.mod`, todas las vueltas comparten una.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 32](../README.md)
