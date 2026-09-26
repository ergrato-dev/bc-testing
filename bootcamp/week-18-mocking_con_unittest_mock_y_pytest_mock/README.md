# Semana 18 - Python Testing III: Mocking con unittest.mock y pytest-mock

> **Etapa 2 - Testing con Python** | Semana 18 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Distinguir mock, stub y spy según el objetivo de prueba.
2. Aplicar `patch` en el target correcto para aislar dependencias.
3. Configurar `return_value`, `side_effect` y verificaciones de llamadas (`assert_called_once_with`, `assert_not_called`, `call_args_list`).
4. Usar `pytest-mock` para mejorar legibilidad de tests con dobles y distinguir cuándo basta `monkeypatch`.
5. Evitar mocks frágiles que rompen por detalles de implementación.

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Fundamentos de doubles, patching correcto y pytest-mock | 2.5 h |
| Prácticas | Patch en el target correcto, autospec y verificación de interacciones | 3 h |
| Proyecto | Suite del dominio con estrategia de mocking por riesgo | 2 h |
| Recursos y cierre | Checklist anti-fragilidad y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Fundamentos de mocking en Python](./1-teoria/01-fundamentos-mocking-python.md)
2. [Patch en el lugar correcto y errores frecuentes](./1-teoria/02-patch-target-correcto-errores-frecuentes.md)
3. [pytest-mock para pruebas legibles y mantenibles](./1-teoria/03-pytest-mock-practicas-recomendadas.md)

### Prácticas

- [Ejercicio 01 - Patch y target correcto](./2-practicas/ejercicio-01-patch-y-target-correcto/)
- [Ejercicio 02 - pytest-mock: stub, mock y spy](./2-practicas/ejercicio-02-pytest-mock-spy-y-stub/)

### Proyecto

- [Proyecto semanal: Suite Python con estrategia de mocking](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Estructura de Carpetas

```text
week-18-mocking_con_unittest_mock_y_pytest_mock/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-test-double-decision-map.svg
|   |-- 02-patch-target-resolution-flow.svg
|   `-- 03-mocking-fragility-radar.svg
|-- 1-teoria/
|   |-- 01-fundamentos-mocking-python.md
|   |-- 02-patch-target-correcto-errores-frecuentes.md
|   `-- 03-pytest-mock-practicas-recomendadas.md
|-- 2-practicas/
|   |-- ejercicio-01-patch-y-target-correcto/
|   `-- ejercicio-02-pytest-mock-spy-y-stub/
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

Mockear no es simular todo: es aislar solo lo necesario para validar una decisión de negocio con feedback confiable.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 17 - Parametrización y marks](../week-17-parametrizacion_y_marks_con_pytest/README.md) | [Semana 19 - Testing de APIs con httpx2](../week-19-testing_de_apis_con_python/README.md) |
