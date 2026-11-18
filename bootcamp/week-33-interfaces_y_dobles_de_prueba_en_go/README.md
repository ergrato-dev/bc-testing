# Semana 33 - Interfaces y Dobles de Prueba en Go

> **Etapa 4 - Testing con Go** | Semana 33 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Aislar las dependencias de un servicio con interfaces pequeñas declaradas en el paquete que las usa.
2. Escribir a mano fakes, stubs y spies, y elegir entre ellos.
3. Generar mocks con mockgen y programarlos con `EXPECT()`, `gomock.Any()`, `Times()` y `Return()`.
4. Elegir entre `assert` y `require` de testify, y leer un mock de `testify/mock`.
5. Comparar estructuras con `cmp.Diff` y probar una salida de texto contra un golden file.
6. Inyectar el reloj y la aleatoriedad para que los tests den siempre el mismo resultado.

---

## Requisitos previos

- Semana 32: `go test`, table-driven tests, helpers y caja negra.
- Semanas 10, 18 y 26: los dobles de prueba en Jest, `unittest.mock` y Mockito (aquí solo se ve la diferencia).
- Semana 31: el servicio Java de tu dominio, de donde portas la operación del proyecto.
- Go 1.24 o superior, por la línea `tool` del `go.mod`. Los ejemplos usan Go 1.26.5.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Interfaces y dobles a mano, mockgen y testify, go-cmp y golden files | 2.5 h |
| Prácticas | 4 labs sobre el mismo servicio: dobles a mano, mockgen, testify, go-cmp y golden files | 4 h |
| Proyecto | El servicio del dominio con un fake, un mock generado, `cmp.Diff` y un golden file | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Interfaces y dobles escritos a mano](./1-teoria/01-interfaces-y-dobles-a-mano.md)
2. [Mocks con mockgen y aserciones con testify](./1-teoria/02-mockgen-y-testify.md)
3. [go-cmp y golden files](./1-teoria/03-go-cmp-y-golden-files.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Interfaces y fakes a mano](./2-practicas/lab-01-interfaces-y-fakes-a-mano/README.md)
- [Lab 02 - Mocks con mockgen](./2-practicas/lab-02-mocks-con-mockgen/README.md)
- [Lab 03 - assert, require y testify/mock](./2-practicas/lab-03-assert-require-y-testify-mock/README.md)
- [Lab 04 - go-cmp y golden files](./2-practicas/lab-04-go-cmp-y-golden-files/README.md)

### Proyecto

- [El servicio del dominio con dobles de prueba](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-33` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 33
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-33-interfaces_y_dobles_de_prueba_en_go/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-interfaces-del-lado-del-consumidor.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-interfaces-y-fakes-a-mano/
|   |-- lab-02-mocks-con-mockgen/
|   |-- lab-03-assert-require-y-testify-mock/
|   `-- lab-04-go-cmp-y-golden-files/
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
| `go.uber.org/mock` (mockgen y gomock) | v0.6.0 | `tool` y `require` en `go.mod` |
| `github.com/stretchr/testify` | v1.12.1 | `require` en `go.mod` (lab 03) |
| `github.com/google/go-cmp` | v0.7.0 | `require` en `go.mod` |

Están registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Un mock comprueba que el código hace unas llamadas concretas, así que un cambio interno que no altera el resultado puede romperlo. Antes de generar un mock, pregúntate si el test comprueba una **interacción** (que el aviso salga una vez, o que no salga) o un **estado** (qué quedó guardado). Para el estado, un fake de diez líneas aguanta mejor los cambios.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 32 - Fundamentos de Testing en Go](../week-32-fundamentos_de_testing_en_go/README.md) | [Semana 34 - HTTP, BD e Integración en Go](../week-34-http_bd_e_integracion_en_go/README.md) |
