# Semana 24 - Proyecto Integrador: Etapa 2 (Python)

> **Etapa 2 - Testing con Python** | Semana 24 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Organizar una suite Python por capas (unitaria, API, repositorio y BDD) y ejecutar cada capa por separado.
2. Medir con una sola cifra la cobertura de pytest y de Behave y hacer fallar la suite por debajo del 85 %.
3. Escribir un reporte de testing con datos reales: capas, cobertura, tests lentos, riesgos y próximos pasos.
4. Presentar tu suite y defender en qué capa va cada test.

---

## Requisitos previos

- Semanas 16 a 23 entregadas en `python/` de tu repositorio `qa-<nombre>-<apellido>`.
- **Docker** instalado y en marcha. Comprueba con `docker info`.

Esta semana no tiene teoría nueva: junta lo que ya sabes.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Repaso: la suite completa, capas y cobertura combinada | 0.5 h |
| Planificación | 2 labs: suite por capas, coverage combinado y reporte | 2.5 h |
| Desarrollo | Proyecto integrador en el proyecto hilo conductor | 6.5 h |
| Presentación | 5 minutos por aprendiz con los datos del reporte | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [La suite Python completa](./1-teoria/01-la-suite-python-completa.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Una suite por capas](./2-practicas/lab-01-suite-por-capas/README.md)
- [Lab 02 - Coverage combinado y reporte](./2-practicas/lab-02-coverage-combinado-y-reporte/README.md)

### Proyecto

- [Suite Python completa del dominio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto integrador (único entregable obligatorio): rama `week-24` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 24
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-24-proyecto_integrador_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   `-- 01-suite-python-por-capas.svg
|-- 1-teoria/
|   `-- 01-la-suite-python-completa.md
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-suite-por-capas/
|   `-- lab-02-coverage-combinado-y-reporte/
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

pytest 9.1.1, pytest-asyncio 1.4.0, coverage 7.17.3, Behave 1.3.3, httpx2 2.13.1, SQLAlchemy 2.1.8, `psycopg[binary]` 3.3.9, `testcontainers[postgres]` 4.15.0 e imagen `postgres:18.7-alpine`. Están fijadas en el `pyproject.toml` de cada carpeta y registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

La cifra de cobertura solo vale si mide tu código con todas las capas. Sin Behave, o sin Docker, la cifra baja; con los tests dentro de `source`, sube sin que pruebes más.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 23 - Testing de Bases de Datos en Python](../week-23-testing_de_bases_de_datos_en_python/README.md) | Semana 25 - Próximamente |
