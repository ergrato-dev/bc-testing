# Semana 08 - Jest: Matchers Avanzados y Parametrización

> **Etapa 1 - Testing con JavaScript** | Semana 8 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Convertir en una tabla con `it.each` los tests que solo cambian de datos, con el ID de cada caso encima de su fila.
2. Escribir nombres de test con los datos de la fila, para saber qué fila falla sin abrir el código.
3. Comparar objetos y arrays con el matcher adecuado: `toStrictEqual`, `toMatchObject`, asimétricos como `expect.any` y matchers de arrays.
4. Comprobar exactamente qué error se lanza, con su mensaje, su clase y sus campos.
5. Escribir un matcher propio con `expect.extend` cuyo fallo explica el problema.

---

## Requisitos previos

- La [semana 3](../week-03-primeros_tests_automatizados_con_javascript/README.md): `toBe`, `toEqual`, `toThrow` y los 19 casos del formulario del planetario.
- La [semana 7](../week-07-jest_avanzado_y_organizacion_de_suites/README.md): tu `~/proyecto-semana-07` con `./verify.sh` en verde.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Tablas con `it.each`; matchers de objetos y arrays; errores y fallos legibles | 2.5 h |
| Prácticas | 3 labs sobre el formulario y las entradas del planetario | 4 h |
| Proyecto | Suite del dominio en tablas y `verify.sh` contando las filas | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Tablas de casos con `it.each`](./1-teoria/01-tablas-de-casos-con-each.md)
2. [Matchers de objetos y arrays](./1-teoria/02-matchers-de-objetos-y-arrays.md)
3. [Errores y fallos que se leen](./1-teoria/03-errores-y-fallos-legibles.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - De tests repetidos a tablas](./2-practicas/lab-01-de-tests-repetidos-a-tablas/README.md)
- [Lab 02 - Matchers de objetos y arrays](./2-practicas/lab-02-matchers-de-objetos-y-arrays/README.md)
- [Lab 03 - Errores y fallos legibles](./2-practicas/lab-03-errores-y-fallos-legibles/README.md)

### Proyecto

- [Suite parametrizada con Jest](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-08`, con la suite de `js/` en tablas, `tablas.md`, `verify.sh` arreglado y el plan actualizado, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-08 ./scripts/verificar-semana.sh 08
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-08-matchers_avanzados_y_parametrizacion/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-de-una-tabla-a-tests.svg
|   `-- 02-de-estricto-a-permisivo.svg
|-- 1-teoria/
|   |-- 01-tablas-de-casos-con-each.md
|   |-- 02-matchers-de-objetos-y-arrays.md
|   `-- 03-errores-y-fallos-legibles.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-de-tests-repetidos-a-tablas/
|   |-- lab-02-matchers-de-objetos-y-arrays/
|   `-- lab-03-errores-y-fallos-legibles/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/tablas.md
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

Una tabla cambia lo que cuenta `verify.sh`: cada fila es un test para Jest, pero `grep` solo ve la línea del `it.each`. El paso 6 del proyecto lo arregla; hasta entonces, una fila sin ID pasa la trazabilidad.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 07 - Jest Avanzado y Organización de Suites](../week-07-jest_avanzado_y_organizacion_de_suites/README.md) | [Semana 09 - Jest: Testing Asíncrono](../week-09-testing_asincrono_con_jest/README.md) |
