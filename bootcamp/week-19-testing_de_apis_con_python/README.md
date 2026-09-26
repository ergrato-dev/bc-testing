# Semana 19 - Python Testing IV: Testing de APIs con httpx2

> **Etapa 2 - Testing con Python** | Semana 19 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Elegir el nivel de aislamiento para probar un cliente HTTP (patch, transporte simulado, servidor local, API real).
2. Probar clientes `httpx2` sin red con `MockTransport` y verificar la request enviada (ruta, query params, Bearer Token y Basic Auth).
3. Probar la traducción de 4xx, 5xx y timeouts a excepciones del dominio.
4. Detectar contratos rotos validando respuestas con `pydantic`.
5. Escribir tests asíncronos con `pytest-asyncio` (modos `strict` y `auto`), fixtures asíncronas y `AsyncMock`.
6. Explicar qué aporta el contract testing con Pact frente a los tests del cliente.

---

## Requisitos previos

- Semana 16: fixtures con `yield` y `conftest.py`.
- Semana 18: `patch`, `side_effect` y verificación de llamadas (`assert_called_once_with`, `call_args_list`). `AsyncMock` es su versión asíncrona y se ve en la teoría 03.

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Clientes HTTP con `httpx2`, errores y contratos, testing asíncrono | 2.5 h |
| Prácticas | Cliente síncrono con `MockTransport` y cliente asíncrono con `pytest-asyncio` | 3 h |
| Proyecto | Suite del cliente HTTP del dominio | 2 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Testing de clientes HTTP con httpx2](./1-teoria/01-testing-de-clientes-http-con-httpx2.md)
2. [Errores HTTP, timeouts y contratos](./1-teoria/02-errores-timeouts-y-contratos.md)
3. [Testing asíncrono: AsyncClient, pytest-asyncio y AsyncMock](./1-teoria/03-testing-asincrono.md)

### Prácticas

- [Ejercicio 01 - Cliente HTTP con MockTransport](./2-practicas/ejercicio-01-cliente-http-con-mocktransport/)
- [Ejercicio 02 - Testing asíncrono](./2-practicas/ejercicio-02-testing-asincrono/)

### Proyecto

- [Proyecto semanal: Suite del cliente HTTP del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): suite del cliente HTTP del dominio ejecutable con `uv run pytest`.

---

## Estructura de Carpetas

```text
week-19-testing_de_apis_con_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-niveles-aislamiento-http.svg
|   |-- 02-mapa-respuestas-a-excepciones.svg
|   `-- 03-ciclo-test-asincrono.svg
|-- 1-teoria/
|   |-- 01-testing-de-clientes-http-con-httpx2.md
|   |-- 02-errores-timeouts-y-contratos.md
|   `-- 03-testing-asincrono.md
|-- 2-practicas/
|   |-- ejercicio-01-cliente-http-con-mocktransport/
|   `-- ejercicio-02-testing-asincrono/
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

`httpx2` 2.13.1, `pydantic` 2.13.5, `pytest` 9.1.1 y `pytest-asyncio` 1.4.0, fijadas en el `pyproject.toml` de cada carpeta. Son las mismas que usa `bc-testing-adso`.

> `respx` no se usa: no intercepta `httpx2` (instala `httpx` 0.28.1 aparte y parchea ese transporte). `MockTransport` viene incluido en `httpx2`.

---

## Nota Importante

Un test de cliente HTTP vale por los casos que el API real casi nunca te da: un 503 con HTML, un timeout, un campo que desaparece. Esos son los que tienes que simular.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 18 - Mocking con unittest.mock y pytest-mock](../week-18-mocking_con_unittest_mock_y_pytest_mock/README.md) | Semana 20 - Próximamente |
