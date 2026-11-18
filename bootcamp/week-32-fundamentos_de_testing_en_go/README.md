# Semana 32 - Fundamentos de Testing en Go

> **Etapa 4 - Testing con Go** | Semana 32 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Crear un módulo de Go y escribir tests con el paquete `testing`, sin librerías externas.
2. Elegir entre `t.Errorf` y `t.Fatalf`, y ejecutar la suite con `go test`, `-v`, `-run`, `-count=1` y `go vet`.
3. Escribir table-driven tests con un subtest por caso, ejecutar uno solo con `-run 'TestX/caso'` y ejecutarlos en paralelo.
4. Escribir helpers con `t.Helper` y usar `t.Cleanup`, `t.TempDir` y `t.Context`.
5. Distinguir los tests de caja blanca (`package x`) de los de caja negra (`package x_test`) y escribir examples que `go test` comprueba.

---

## Requisitos previos

- Semanas 8, 17 y 25: parametrización en Jest, pytest y JUnit (aquí solo se ve la diferencia).
- Semana 31: la suite Java de tu dominio, de donde portas las reglas.
- Go 1.24 o superior. Comprueba con `go version`. Los ejemplos usan Go 1.26.5.

No hace falta saber Go: los labs explican cada construcción del lenguaje que usan. Si es tu primer contacto, el [Tour of Go](https://go.dev/tour/) cubre la sintaxis en una tarde.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | `go test` y el paquete `testing`, table-driven tests y subtests, helpers, caja negra y examples | 2.5 h |
| Prácticas | 4 labs: primer test, tablas y subtests, helpers y `t.TempDir`, caja negra y examples | 4 h |
| Proyecto | Las reglas del dominio en Go y la comparativa con Jest, pytest y JUnit | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [`go test` y el paquete `testing`](./1-teoria/01-go-test-y-el-paquete-testing.md)
2. [Table-driven tests y subtests](./1-teoria/02-table-driven-y-subtests.md)
3. [Helpers, caja negra y examples](./1-teoria/03-helpers-y-caja-negra.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Primer test con go test](./2-practicas/lab-01-primer-test-con-go-test/README.md)
- [Lab 02 - Table-driven tests y subtests](./2-practicas/lab-02-table-driven-y-subtests/README.md)
- [Lab 03 - Helpers, t.TempDir y t.Cleanup](./2-practicas/lab-03-helpers-tempdir-y-cleanup/README.md)
- [Lab 04 - Caja negra y examples](./2-practicas/lab-04-caja-negra-y-examples/README.md)

### Proyecto

- [Las reglas del dominio en Go](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-32` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 32
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-32-fundamentos_de_testing_en_go/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-table-driven-y-subtests.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-primer-test-con-go-test/
|   |-- lab-02-table-driven-y-subtests/
|   |-- lab-03-helpers-tempdir-y-cleanup/
|   `-- lab-04-caja-negra-y-examples/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

Go 1.26.5, en la directiva `go` de cada `go.mod`. Esta semana solo usa la librería estándar: los labs y el proyecto no tienen dependencias ni `go.sum`. Está registrado en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

`go test` puede darte un verde que no se ejecutó. La línea `(cached)` repite un resultado anterior, y un `ExampleXxx` sin `// Output:` o una función `CheckXxx` se compilan pero no se ejecutan nunca. Cuando necesites saber qué pasó de verdad, ejecuta con `-count=1 -v` y cuenta los `--- PASS`.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 31 - Proyecto Integrador: Etapa 3 (Java)](../week-31-proyecto_integrador_java/README.md) | [Semana 33 - Interfaces y Dobles de Prueba en Go](../week-33-interfaces_y_dobles_de_prueba_en_go/README.md) |
