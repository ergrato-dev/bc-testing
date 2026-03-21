# Semana 11 - JavaScript Testing V: TDD con Jest

> **Etapa 1 - Testing con JavaScript** | Semana 11 de 15

![Bootcamp Testing](../../_assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana seras capaz de:

1. Aplicar el ciclo **Red-Green-Refactor** de forma disciplinada con Jest.
2. Diseñar tests pequeños que expresen comportamiento antes de implementar codigo.
3. Implementar solo el codigo minimo para pasar cada test.
4. Refactorizar con seguridad usando la suite como red de proteccion.
5. Detectar anti patrones de TDD (tests fragiles, sobreespecificacion, saltar Red).
6. Construir diseño emergente guiado por casos de prueba incrementales.

---

## Distribucion del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoria | Ciclo TDD, micro-pasos y refactor seguro | 2.5 h |
| Practicas | Dos ejercicios guiados de Red-Green-Refactor | 3 h |
| Proyecto | Suite incremental TDD para dominio asignado | 2 h |
| Recursos y cierre | Refuerzo, autoevaluacion y checklist | 0.5 h |

---

## Contenido de la Semana

### Teoria

1. [Fundamentos del ciclo Red-Green-Refactor](./1-teoria/01-red-green-refactor-fundamentos.md)
2. [Micro-ciclos TDD y diseno emergente](./1-teoria/02-micro-ciclos-y-diseno-emergente.md)
3. [Refactor seguro con respaldo de tests](./1-teoria/03-refactor-seguro-con-jest.md)

### Practicas

- [Ejercicio 01 - Ciclo Red-Green-Refactor](./2-practicas/ejercicio-01-ciclo-red-green-refactor/)
- [Ejercicio 02 - Refactor seguro con tests](./2-practicas/ejercicio-02-refactor-seguro-con-tests/)

### Proyecto

- [Proyecto semanal: TDD incremental en servicio de dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografia](./4-recursos/videografia/README.md)
- [Webgrafia](./4-recursos/webgrafia/README.md)

### Glosario

- [Terminos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-11-tdd_con_jest/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-ciclo-red-green-refactor.svg
|   |-- 02-micro-pasos-tdd.svg
|   |-- 03-arbol-decision-siguiente-test.svg
|   `-- 04-refactor-safety-net.svg
|-- 1-teoria/
|   |-- 01-red-green-refactor-fundamentos.md
|   |-- 02-micro-ciclos-y-diseno-emergente.md
|   `-- 03-refactor-seguro-con-jest.md
|-- 2-practicas/
|   |-- ejercicio-01-ciclo-red-green-refactor/
|   `-- ejercicio-02-refactor-seguro-con-tests/
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

En TDD, la velocidad no esta en escribir mas codigo, sino en acortar el feedback loop. Si no pasas por Red, pierdes evidencia de que el test realmente protege comportamiento.

---

## Navegacion

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 10 - Mocks avanzados en Jest](../week-10-mocks_avanzados_en_jest/README.md) | [Semana 12 - Testing de APIs REST con Supertest](../week-12-apis_rest_con_supertest/README.md) |
