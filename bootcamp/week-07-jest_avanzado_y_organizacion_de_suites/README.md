# Semana 07 - Jest Avanzado y Organización de Suites

> **Etapa 1 - Testing con JavaScript** | Semana 7 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Organizar una suite de Jest en `describe` anidados por función y por contexto, de forma que el informe se lea como una lista de reglas.
2. Ejecutar un contexto con `-t` y encontrar un `.only` olvidado antes de que deje la suite en verde sin ejecutarla.
3. Preparar el estado de cada test con `beforeEach`, predecir el orden de los hooks y detectar con `--randomize` los tests que dependen de otros.
4. Sustituir un colaborador por un stub, un mock o un spy de Jest, y saber qué comprueba el test en cada caso.
5. Probar con dobles un servicio que guarda estado y avisa al cliente, sin perder la trazabilidad de la semana 6.

---

## Requisitos previos

- La [semana 3](../week-03-primeros_tests_automatizados_con_javascript/README.md): Jest, `describe`, `it`, AAA y matchers.
- La [semana 6](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md): tu `~/proyecto-semana-06` con `./verify.sh` en verde.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Suites por comportamiento; hooks y aislamiento; stub, mock y spy | 2.5 h |
| Prácticas | 3 labs sobre el servicio de reservas del planetario | 4 h |
| Proyecto | Suite modular del dominio y un servicio probado con dobles | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Organizar una suite por comportamiento](./1-teoria/01-organizar-suites-por-comportamiento.md)
2. [Hooks y aislamiento entre tests](./1-teoria/02-hooks-y-aislamiento.md)
3. [Dobles de prueba: stub, mock y spy](./1-teoria/03-dobles-de-prueba-basicos.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Describe por comportamiento](./2-practicas/lab-01-describe-por-comportamiento/README.md)
- [Lab 02 - Hooks y estado compartido](./2-practicas/lab-02-hooks-y-estado-compartido/README.md)
- [Lab 03 - Stubs, mocks y spies](./2-practicas/lab-03-stubs-mocks-y-spies/README.md)

### Proyecto

- [Suite modular con Jest](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-07`, con la suite de `js/` reorganizada, el servicio y sus tests, y el plan actualizado, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-07 ./scripts/verificar-semana.sh 07
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-07-jest_avanzado_y_organizacion_de_suites/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-orden-de-los-hooks.svg
|   `-- 02-stub-mock-spy.svg
|-- 1-teoria/
|   |-- 01-organizar-suites-por-comportamiento.md
|   |-- 02-hooks-y-aislamiento.md
|   `-- 03-dobles-de-prueba-basicos.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-describe-por-comportamiento/
|   |-- lab-02-hooks-y-estado-compartido/
|   `-- lab-03-stubs-mocks-y-spies/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/js/
|       |-- src/clock.js
|       |-- src/notifier.js
|       |-- src/service.js
|       |-- tests/notifier.test.js
|       `-- tests/service.test.js
|-- 4-recursos/
|   |-- ebooks-free/README.md
|   |-- videografia/README.md
|   `-- webgrafia/README.md
`-- 5-glosario/
    `-- README.md
```

---

## Versiones de la semana

| Herramienta | Versión | Dónde se fija |
|---|---|---|
| Node.js | 22 | La instalas tú; `engines` del proyecto |
| pnpm | 12.6.0 | `packageManager` de `package.json` |
| Jest | 30.5.2 | `devDependencies` de `package.json` |

Las versiones de todo el curso están en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

Desde esta semana hasta la 15 solo crece `js/`. Python y Java no se tocan, pero `./verify.sh` los sigue ejecutando: cada entrega tiene que dejar las tres suites en verde.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 06 - Proyecto Integrador: Etapa 0](../week-06-plan_de_pruebas_y_suite_base_multilenguaje/README.md) | [Semana 08 - Jest: Matchers Avanzados y Parametrización](../week-08-matchers_avanzados_y_parametrizacion/README.md) |
