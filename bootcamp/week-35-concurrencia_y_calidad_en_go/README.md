# Semana 35 - Concurrencia y Calidad en Go

> **Etapa 4 - Testing con Go** | Semana 35 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Encontrar carreras de datos con `go test -race`, leer su informe y arreglarlas.
2. Probar código con temporizadores, esperas y plazos de `context` en una burbuja de `testing/synctest`, con tiempo virtual y duraciones exactas.
3. Escribir fuzz tests con corpus semilla, ejecutar el fuzzer y convertir cada fallo en un test de regresión de `testdata/fuzz/`.
4. Medir con benchmarks y `b.Loop()`, y demostrar con `benchstat` que una optimización mejora de verdad.
5. Medir la cobertura con `-coverprofile`, `-covermode=atomic` y `-coverpkg`, y la de un binario con `go build -cover` y `GOCOVERDIR`.
6. Revisar el código con `staticcheck` como herramienta del módulo, y conocer `golangci-lint`.

---

## Requisitos previos

- Semanas 32 a 34: `go test`, tablas, dobles, `httptest` y la suite del dominio con PostgreSQL.
- Semanas 13 y 20: property-based testing con fast-check e hypothesis (el fuzzing se compara con ellos).
- Semanas 9, 14, 21 y 31: fake timers de Jest y cobertura en JavaScript, Python y Java (aquí solo se ve la diferencia).
- Go 1.25 o superior (los ejemplos usan Go 1.26.5), un compilador de C (`gcc`) para `-race`, y Docker en marcha para la suite de integración del proyecto.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Race detector y synctest, fuzzing, benchmarks y benchstat, cobertura y linters | 2.5 h |
| Prácticas | 4 labs: carreras y tiempo virtual, fuzzing, benchmarks, cobertura y linters | 4 h |
| Proyecto | La suite del dominio con un test concurrente, un fuzz test, un benchmark y el informe `calidad.md` | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Race detector y testing/synctest](./1-teoria/01-race-detector-y-synctest.md)
2. [Fuzzing nativo](./1-teoria/02-fuzzing-nativo.md)
3. [Benchmarks y benchstat](./1-teoria/03-benchmarks-y-benchstat.md)
4. [Cobertura y linters](./1-teoria/04-cobertura-y-linters.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Race detector y synctest](./2-practicas/lab-01-race-detector-y-synctest/README.md)
- [Lab 02 - Fuzzing nativo](./2-practicas/lab-02-fuzzing-nativo/README.md)
- [Lab 03 - Benchmarks con benchstat](./2-practicas/lab-03-benchmarks-con-benchstat/README.md)
- [Lab 04 - Cobertura y linters](./2-practicas/lab-04-cobertura-y-linters/README.md)

### Proyecto

- [Concurrencia y calidad en la suite Go del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-35` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 35
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-35-concurrencia_y_calidad_en_go/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-ciclo-del-fuzzer.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-race-detector-y-synctest/
|   |-- lab-02-fuzzing-nativo/
|   |-- lab-03-benchmarks-con-benchstat/
|   `-- lab-04-cobertura-y-linters/
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

| Componente | Versión | Dónde |
|---|---|---|
| Go | 1.26.5 | Directiva `go` de cada `go.mod` |
| `honnef.co/go/tools` (staticcheck 2026.2.2) | v0.8.2 | `tool` en `go.mod` |
| `golang.org/x/perf` (benchstat) | v0.0.0-20261117185222-4febdb082b2c | `tool` en `go.mod` |
| `golang.org/x/text` | v0.44.0 | `require` en `go.mod` (lab 04) |
| golangci-lint | v2.15.0 | Binario aparte (`go install`), opcional |

Están registradas en [stack-versions](../../docs/stack-versions.md). `golang.org/x/perf` no publica versiones: se fija con la pseudo-versión de un commit.

---

## Nota Importante

Cada herramienta de esta semana encuentra algo que un test en verde deja pasar: una carrera que casi nunca ocurre, una entrada que nadie escribió en la tabla, un coste que crece con el cuadrado del tamaño, una línea que ningún test ejecuta. Ninguna demuestra que el código esté bien. Un fallo que encuentran se arregla en el código y se queda como test (`testdata/fuzz/`, una fila de la tabla), no en una nota.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 34 - HTTP, BD e Integración en Go](../week-34-http_bd_e_integracion_en_go/README.md) | Semana 36 - Próximamente |
