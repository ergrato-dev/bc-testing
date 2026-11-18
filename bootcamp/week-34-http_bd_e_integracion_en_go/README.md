# Semana 34 - HTTP, BD e Integración en Go

> **Etapa 4 - Testing con Go** | Semana 34 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Probar handlers HTTP en memoria con `httptest.NewRequest` y `httptest.NewRecorder`, incluido el contrato JSON.
2. Probar un cliente HTTP contra un servidor local con `httptest.NewServer`: errores, plazos y HTTPS.
3. Usar las rutas con método del `http.ServeMux` y documentar en los tests los `404` y `405` del router.
4. Preparar un recurso compartido por el paquete con `TestMain`, y separar la integración con `-short` y `//go:build integration`.
5. Probar un repositorio contra PostgreSQL real con testcontainers-go, con las migraciones incrustadas con `//go:embed`.
6. Aislar cada test con una transacción que se deshace al terminar, y reconocer cuándo hace falta un savepoint o un snapshot.

---

## Requisitos previos

- Semana 33: interfaces del lado del consumidor, fakes, mocks generados y `cmp.Diff`.
- Semanas 12, 19 y 28: tests de APIs en JavaScript, Python y Java (aquí solo se ve la diferencia).
- Semanas 23 y 29: rollback por test y Testcontainers en Python y Java.
- Go 1.25 o superior (los ejemplos usan Go 1.26.5) y Docker en marcha para el lab 04 y el proyecto.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | httptest, `TestMain` y build tags, testcontainers-go y PostgreSQL | 2.5 h |
| Prácticas | 4 labs: handlers, clientes, `TestMain` y tags, PostgreSQL con testcontainers-go | 4 h |
| Proyecto | La API del dominio con `httptest` y su almacén contra PostgreSQL real | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [httptest: handlers y clientes](./1-teoria/01-httptest-handlers-y-clientes.md)
2. [TestMain, build tags y -short](./1-teoria/02-testmain-build-tags-y-short.md)
3. [testcontainers-go y PostgreSQL](./1-teoria/03-testcontainers-go-y-postgresql.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Handlers con httptest](./2-practicas/lab-01-handlers-con-httptest/README.md)
- [Lab 02 - Clientes HTTP con httptest.NewServer](./2-practicas/lab-02-clientes-con-httptest-newserver/README.md)
- [Lab 03 - TestMain, build tags y -short](./2-practicas/lab-03-testmain-build-tags-y-short/README.md)
- [Lab 04 - PostgreSQL con testcontainers-go](./2-practicas/lab-04-postgresql-con-testcontainers-go/README.md)

### Proyecto

- [La API del dominio y su repositorio en PostgreSQL](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-34` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo, con Docker en marcha:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 34
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-34-http_bd_e_integracion_en_go/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-capas-de-test-de-la-api.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-handlers-con-httptest/
|   |-- lab-02-clientes-con-httptest-newserver/
|   |-- lab-03-testmain-build-tags-y-short/
|   `-- lab-04-postgresql-con-testcontainers-go/
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
| `github.com/jackc/pgx/v5` | v5.13.0 | `require` en `go.mod` |
| `github.com/testcontainers/testcontainers-go` y su módulo `postgres` | v0.45.0 | `require` en `go.mod` |
| `github.com/google/go-cmp` | v0.7.0 | `require` en `go.mod` |
| PostgreSQL | `postgres:18.7-alpine` | Imagen de los tests; Ryuk usa `testcontainers/ryuk:0.14.0` |

Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un test de integración no sustituye a los unitarios: los complementa. Los handlers se prueban con fakes y en milisegundos, y el almacén contra PostgreSQL porque sus reglas (`CHECK`, claves, el SQL) solo existen ahí. Si un test de integración comprueba algo que un fake podría comprobar igual, sobra; si un test unitario da por buena una consulta SQL que nunca se ejecutó, falta uno de integración.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 33 - Interfaces y Dobles de Prueba en Go](../week-33-interfaces_y_dobles_de_prueba_en_go/README.md) | [Semana 35 - Concurrencia y Calidad en Go](../week-35-concurrencia_y_calidad_en_go/README.md) |
