# Glosario Semana 33 - Interfaces y Dobles de Prueba en Go

## A

- **`assert`** (testify): paquete de aserciones que marcan el fallo y dejan seguir el test, como `t.Errorf`.

## C

- **`cmp.Diff`**: función de go-cmp que devuelve `""` si dos valores son iguales y, si no, las diferencias campo a campo con `-` (esperado) y `+` (obtenido).
- **`cmpopts`**: opciones de go-cmp para ajustar la comparación: `IgnoreUnexported`, `EquateEmpty`, `IgnoreFields`.
- **Controller** (gomock): el objeto que lleva las expectativas de los mocks de un test y comprueba al terminar que se cumplieron.

## E

- **`EXPECT()`**: método de un mock generado que programa una llamada esperada, con sus argumentos y lo que devuelve.

## F

- **Fake**: doble que funciona de verdad con un atajo, como un almacén en memoria en lugar de una base de datos.
- **Flaky test**: test que a veces pasa y a veces falla sin cambiar el código, por depender de la hora, del azar o del orden.

## G

- **`go generate`**: ejecuta los comandos de las líneas `//go:generate` del código. No se ejecuta al compilar ni al hacer `go test`.
- **`go get -tool`**: registra un programa como herramienta del módulo con la línea `tool` del `go.mod` (Go 1.24+). Se ejecuta con `go tool <nombre>`.
- **Golden file**: archivo en `testdata/` con la salida esperada de un test. Se regenera con un flag como `-update` y se revisa antes del commit.
- **gomock**: el paquete de `go.uber.org/mock` que usan los mocks generados: `Controller`, `Any`, `Times`.

## I

- **Interfaz del lado del consumidor**: interfaz declarada en el paquete que la usa, con solo los métodos que necesita. Es la forma idiomática en Go.

## M

- **Mock**: doble que lleva dentro las llamadas que espera y hace fallar el test si no se cumplen.
- **mockgen**: el programa de `go.uber.org/mock` que lee una interfaz y escribe el código de su mock.

## R

- **`require`** (testify): paquete con las mismas aserciones que `assert`, pero que paran el test al fallar, como `t.Fatalf`.

## S

- **Semilla** (*seed*): el valor inicial de un generador de números aleatorios. Con la misma semilla, `rand.New(rand.NewPCG(1, 2))` da siempre la misma secuencia.
- **Spy**: doble que guarda las llamadas que recibe para que el test las compruebe después.
- **Stub**: doble que devuelve una respuesta fija.

## T

- **`testdata/`**: carpeta que `go build` ignora. Guarda los archivos que usan los tests, como los golden files.
- **`testify/mock`**: paquete de testify para escribir mocks a mano: el método llama a `m.Called(...)` y el test programa con `On(...)`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 33](../README.md)
