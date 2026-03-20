# Semana 10 - JavaScript Testing IV: Mocks Avanzados en Jest

> **Etapa 1 - Testing con JavaScript** | Semana 10 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Diferenciar y aplicar `mock`, `stub` y `spy` en escenarios reales de testing.
2. Controlar dependencias con `jest.fn()` y `jest.spyOn()` sin romper el objetivo del test.
3. Simular colaboraciones externas con `jest.mock()` en modulos completos.
4. Verificar interacciones (llamadas, argumentos, orden, cantidad) de forma confiable.
5. Evitar anti patrones de sobre-mocking que generan tests fragiles.
6. Diseñar suites aisladas y legibles bajo patron AAA para servicios con dependencias.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Test doubles, spies, mocks de modulo y buenas practicas | 2.5 h |
| Practicas | Ejercicios guiados de funciones mockeadas y modulos aislados | 3 h |
| Proyecto | Suite con dependencias mockeadas en dominio asignado | 2 h |
| Recursos y cierre | Refuerzo y checklist final | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Test doubles: mock, stub y spy en Jest](./1-teoria/01-test-doubles-jest.md)
2. [Mocks de funciones y verificacion de interacciones](./1-teoria/02-mocks-funciones-e-interacciones.md)
3. [Mocks de modulos con jest.mock y aislamiento](./1-teoria/03-mocks-de-modulos-jest.md)

### Practicas

- [Ejercicio 01 - Mocks de funciones y spies](./2-practicas/ejercicio-01-mocks-funciones-y-spies/)
- [Ejercicio 02 - Mocks de modulos](./2-practicas/ejercicio-02-mocks-modulos/)

### Proyecto

- [Proyecto semanal: Suite con dependencias aisladas](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-10/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-test-doubles-mapa.svg
|   |-- 02-anatomia-jest-fn.svg
|   |-- 03-spy-vs-mock.svg
|   `-- 04-mock-modulo-flujo.svg
|-- 1-teoria/
|   |-- 01-test-doubles-jest.md
|   |-- 02-mocks-funciones-e-interacciones.md
|   `-- 03-mocks-de-modulos-jest.md
|-- 2-practicas/
|   |-- ejercicio-01-mocks-funciones-y-spies/
|   `-- ejercicio-02-mocks-modulos/
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

## Nota Importante

Un mock aporta valor cuando ayuda a aislar el comportamiento bajo prueba. Si mockeas demasiadas capas, puedes probar la implementacion del mock y no la logica real.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 09 - Testing asincrono con Jest](../week-09/README.md) | [Semana 11 - TDD en JavaScript con Jest](../week-11/README.md) |
