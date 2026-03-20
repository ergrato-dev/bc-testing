# Semana 09 - JavaScript Testing III: Testing Asincrono con Jest

> **Etapa 1 - Testing con JavaScript** | Semana 9 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Escribir tests para funciones asincronas con `async/await` y Promises.
2. Validar errores asincronos con `rejects` y `toThrow` de forma correcta.
3. Controlar el tiempo con fake timers en escenarios de retry y debounce.
4. Evitar falsos positivos por no esperar la finalizacion de tareas asincronas.
5. Diseñar suites asincronas legibles siguiendo patron AAA.
6. Preparar base para integrar mocks avanzados y APIs en semanas siguientes.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Promesas, async/await, rejects y timers | 2.5 h |
| Practicas | Ejercicios guiados de asincronia y control de tiempo | 3 h |
| Proyecto | Suite asincrona del dominio asignado | 2 h |
| Recursos y cierre | Refuerzo y checklist final | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Promesas y async/await en testing](./1-teoria/01-promesas-y-async-await-jest.md)
2. [Validacion de errores asincronos](./1-teoria/02-errores-asincronos-jest.md)
3. [Fake timers y control del tiempo](./1-teoria/03-fake-timers-jest.md)

### Practicas

- [Ejercicio 01 - Promesas y async/await](./2-practicas/ejercicio-01-promesas-y-async-await/)
- [Ejercicio 02 - Fake timers y reintentos](./2-practicas/ejercicio-02-fake-timers-y-reintentos/)

### Proyecto

- [Proyecto semanal: Suite asincrona del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```
week-09/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-flujo-async-await-jest.svg
|   |-- 02-manejo-rejects-jest.svg
|   |-- 03-fake-timers-ciclo.svg
|   |-- 04-event-loop-testing.svg
|   `-- 05-errores-asincronos-diagnostico.svg
|-- 1-teoria/
|   |-- 01-promesas-y-async-await-jest.md
|   |-- 02-errores-asincronos-jest.md
|   `-- 03-fake-timers-jest.md
|-- 2-practicas/
|   |-- ejercicio-01-promesas-y-async-await/
|   `-- ejercicio-02-fake-timers-y-reintentos/
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

En testing asincrono, el error mas frecuente no esta en la logica de negocio sino en no esperar correctamente la promesa. Esta semana prioriza confiabilidad sobre cantidad de tests.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 08 - Matchers avanzados y parametrizacion](../week-08/README.md) | [Semana 10 - Mocks avanzados en Jest](../week-10/README.md) |
