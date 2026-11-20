# Semana 09 - Jest: Testing Asíncrono

> **Etapa 1 - Testing con JavaScript** | Semana 9 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Escribir tests de funciones asíncronas que esperan cada promesa con `async` y `await`.
2. Reconocer un `expect` que nadie espera por lo que muestra Jest: el resumen en verde, un error después y el código de salida 1.
3. Comprobar los rechazos con `await expect(...).rejects`, y encontrar el `try/catch` que pasa sin comprobar nada.
4. Simular un colaborador asíncrono que falla con `mockRejectedValue` y sus versiones `Once`.
5. Probar reintentos y esperas con el reloj falso de Jest, sin esperar tiempo real.

---

## Requisitos previos

- La [semana 7](../week-07-jest_avanzado_y_organizacion_de_suites/README.md): `describe`, hooks, stubs y mocks con `jest.fn()`.
- La [semana 8](../week-08-matchers_avanzados_y_parametrizacion/README.md): tu `~/proyecto-semana-08` con `./verify.sh` en verde.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Esperar a las promesas; errores asíncronos; fake timers | 2.5 h |
| Prácticas | 3 labs sobre la taquilla y los avisos del planetario | 4 h |
| Proyecto | El aviso del dominio, asíncrono y con reintentos | 3 h |
| Recursos | Lecturas y vídeos | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Esperar a las promesas](./1-teoria/01-esperar-a-las-promesas.md)
2. [Errores asíncronos](./1-teoria/02-errores-asincronos.md)
3. [Fake timers: controlar el tiempo](./1-teoria/03-fake-timers.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Tests que esperan](./2-practicas/lab-01-tests-que-esperan/README.md)
- [Lab 02 - Errores asíncronos](./2-practicas/lab-02-errores-asincronos/README.md)
- [Lab 03 - Reintentos con fake timers](./2-practicas/lab-03-reintentos-con-fake-timers/README.md)

### Proyecto

- [Suite asíncrona con Jest](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto (único entregable obligatorio): `~/proyecto-semana-09`, con el aviso asíncrono y sus reintentos, la suite de `js/` esperando cada promesa y el plan actualizado, por el canal que indique el instructor. Antes de entregarlo:

  ```bash
  PROYECTO=~/proyecto-semana-09 ./scripts/verificar-semana.sh 09
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-09-testing_asincrono_con_jest/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-el-test-termina-antes.svg
|   `-- 02-reloj-falso-reintentos.svg
|-- 1-teoria/
|   |-- 01-esperar-a-las-promesas.md
|   |-- 02-errores-asincronos.md
|   `-- 03-fake-timers.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-tests-que-esperan/
|   |-- lab-02-errores-asincronos/
|   `-- lab-03-reintentos-con-fake-timers/
|-- 3-proyecto/
|   |-- README.md
|   `-- starter/retry.js
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

Con código asíncrono, la línea `Tests:` no basta: un `expect` que nadie espera falla después del resumen, o hace que Jest no llegue a imprimirlo. Mira siempre el código de salida de `pnpm test`, como hace `verify.sh`.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 08 - Jest: Matchers Avanzados y Parametrización](../week-08-matchers_avanzados_y_parametrizacion/README.md) | [Semana 10 - Mocks avanzados en Jest](../week-10-mocks_avanzados_en_jest/README.md) |
