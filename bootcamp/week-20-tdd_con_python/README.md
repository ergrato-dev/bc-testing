# Semana 20 - Python Testing V: TDD con Python

> **Etapa 2 - Testing con Python** | Semana 20 de 24

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Aplicar Red → Green → Refactor con pytest en micro-pasos y reconocer un Red válido.
2. Usar `mypy --strict` como segunda red de seguridad dentro del ciclo.
3. Comparar la práctica de TDD en Python con JavaScript (semana 11) y Java.
4. Dejar que los tests pidan `dataclass`, `Protocol` y fakes creados con fixtures.
5. Poner código legado bajo test con tests de caracterización y un seam.
6. Escribir propiedades con `hypothesis` y leer contraejemplos reducidos.

---

## Requisitos previos

- Semana 11: ciclo Red-Green-Refactor con Jest.
- Semana 16: fixtures de pytest.
- Semana 17: `parametrize`.

---

## Distribución del Tiempo (8 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Ciclo TDD en Python, diseño emergente y código legado, property-based testing | 2.5 h |
| Prácticas | Kata de números romanos y propiedades con hypothesis y mypy | 3 h |
| Proyecto | Servicio del dominio construido con TDD | 2 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [TDD en Python: ciclo, herramientas y diferencias con JS y Java](./1-teoria/01-tdd-en-python-ciclo-y-herramientas.md)
2. [Diseño emergente: dataclasses, Protocol, fixtures y código legado](./1-teoria/02-diseno-emergente-y-codigo-legado.md)
3. [Property-based testing con hypothesis](./1-teoria/03-property-based-testing-con-hypothesis.md)

### Prácticas

- [Ejercicio 01 - Kata de números romanos](./2-practicas/ejercicio-01-kata-numeros-romanos/)
- [Ejercicio 02 - Lo que los ejemplos no ven: mypy y hypothesis](./2-practicas/ejercicio-02-propiedades-con-hypothesis/)

### Proyecto

- [Proyecto semanal: Servicio del dominio con TDD](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): servicio del dominio construido con TDD, con evidencia de cada ciclo.

---

## Estructura de Carpetas

```text
week-20-tdd_con_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- 0-assets/
|   |-- 01-ciclo-tdd-python.svg
|   |-- 02-diseno-emergente.svg
|   `-- 03-hypothesis-shrinking.svg
|-- 1-teoria/
|   |-- 01-tdd-en-python-ciclo-y-herramientas.md
|   |-- 02-diseno-emergente-y-codigo-legado.md
|   `-- 03-property-based-testing-con-hypothesis.md
|-- 2-practicas/
|   |-- ejercicio-01-kata-numeros-romanos/
|   `-- ejercicio-02-propiedades-con-hypothesis/
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

`pytest` 9.1.1, `hypothesis` 6.168.0 y `mypy` 2.3.1, fijadas en el `pyproject.toml` de cada carpeta junto a `[tool.mypy] strict = true`.

---

## Nota Importante

TDD no es escribir tests primero por disciplina: es dejar que cada test te diga cuál es el siguiente paso mínimo. Las propiedades y los tipos no sustituyen esos pasos, los protegen.

---

## Navegación

| <- Semana anterior | Siguiente semana -> |
|---|---|
| [Semana 19 - Testing de APIs con httpx2](../week-19-testing_de_apis_con_python/README.md) | [Semana 21 - Coverage y calidad en Python](../week-21-coverage_y_calidad_en_python/README.md) |
