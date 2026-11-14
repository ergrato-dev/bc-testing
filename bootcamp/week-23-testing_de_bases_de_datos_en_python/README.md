# Semana 23 - Python Testing VIII: Testing de Bases de Datos

> **Etapa 2 - Testing con Python** | Semana 23 de 46

![Bootcamp Testing](../../assets/bootcamp-header.svg)

---

## Objetivos de la Semana

Al finalizar esta semana serás capaz de:

1. Elegir entre mock, SQLite en memoria y PostgreSQL real según lo que quieras detectar.
2. Probar un repositorio de SQLAlchemy 2.0 contra una base de datos real: consultas, orden y restricciones.
3. Aislar cada test con una transacción externa, savepoints y rollback, sin recrear el esquema.
4. Generar datos de prueba con Faker y Factory Boy indicando solo lo que le importa al test.
5. Ejecutar la misma suite en SQLite y en PostgreSQL con Testcontainers.
6. Probar que las migraciones de Alembic suben, coinciden con los modelos y bajan.

---

## Requisitos previos

- Semana 16: fixtures, `yield` y scopes.
- Semana 17: `parametrize` (aquí se parametriza una fixture).
- Semana 18: mocks (para entender qué no pueden detectar).
- **Docker** instalado y en marcha (lab 04 y proyecto). Comprueba con `docker info`.

---

## Distribución del Tiempo (10 horas)

| Actividad | Contenido | Tiempo |
|---|---|---|
| Teoría | Estrategias y SQLAlchemy, rollback por test, factories, Testcontainers y Alembic | 2.5 h |
| Prácticas | 4 labs: SQLite, rollback, Faker/Factory Boy, PostgreSQL + Alembic | 4 h |
| Proyecto | Capa de datos del dominio en el proyecto hilo conductor | 3 h |
| Recursos y cierre | Videos, lecturas y repaso | 0.5 h |

---

## Contenido de la Semana

### Teoría

1. [Estrategias para testear la capa de datos](./1-teoria/01-estrategias-y-sqlalchemy.md)
2. [Fixtures de base de datos y rollback por test](./1-teoria/02-fixtures-y-rollback-por-test.md)
3. [Datos de prueba con Faker y Factory Boy](./1-teoria/03-datos-con-faker-y-factory-boy.md)
4. [PostgreSQL real con Testcontainers y migraciones con Alembic](./1-teoria/04-postgresql-testcontainers-y-alembic.md)

### Prácticas

- [Índice de labs](./2-practicas/README.md)
- [Lab 01 - Repositorio con SQLite en memoria](./2-practicas/lab-01-repositorio-con-sqlite/README.md)
- [Lab 02 - Rollback por test](./2-practicas/lab-02-rollback-por-test/README.md)
- [Lab 03 - Datos de prueba con Faker y Factory Boy](./2-practicas/lab-03-datos-con-faker-y-factory-boy/README.md)
- [Lab 04 - PostgreSQL con Testcontainers y migraciones con Alembic](./2-practicas/lab-04-postgresql-con-testcontainers-y-alembic/README.md)

### Proyecto

- [Capa de datos del dominio con tests de repositorio](./3-proyecto/README.md)

### Recursos

- [Ebooks gratuitos](./4-recursos/ebooks-free/README.md)
- [Videografía](./4-recursos/videografia/README.md)
- [Webgrafía](./4-recursos/webgrafia/README.md)

### Glosario

- [Términos clave de la semana](./5-glosario/README.md)

---

## Entregables

- Proyecto semanal (único entregable obligatorio): rama `week-23` del repositorio `qa-<nombre>-<apellido>` con pull request a `main`. Antes de abrirlo:

  ```bash
  PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 23
  ```

  Las comprobaciones están en [checks.json](./checks.json) y se explican en [autograding](../../docs/autograding.md).

---

## Estructura de Carpetas

```text
week-23-testing_de_bases_de_datos_en_python/
|-- README.md
|-- rubrica-evaluacion.md
|-- checks.json
|-- 0-assets/
|   |-- 01-rollback-por-test.svg
|   `-- 02-sqlite-y-postgresql-con-testcontainers.svg
|-- 1-teoria/
|-- 2-practicas/
|   |-- README.md
|   |-- lab-01-repositorio-con-sqlite/
|   |-- lab-02-rollback-por-test/
|   |-- lab-03-datos-con-faker-y-factory-boy/
|   `-- lab-04-postgresql-con-testcontainers-y-alembic/
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

SQLAlchemy 2.1.8, `psycopg[binary]` 3.3.9, Alembic 1.20.1, pytest 9.1.1, Faker 40.54.0, Factory Boy 3.3.3, `testcontainers[postgres]` 4.15.0 e imagen `postgres:18.7-alpine`. Están fijadas en el `pyproject.toml` de cada carpeta y registradas en [stack-versions](../../docs/stack-versions.md).

---

## Nota Importante

SQLite te da velocidad mientras escribes; PostgreSQL te dice la verdad antes del PR. Un test de repositorio que solo pasa en SQLite no prueba lo que llegará a producción.

---

## Navegación

| ← Semana anterior | Siguiente semana → |
|---|---|
| [Semana 22 - BDD con Python y Behave](../week-22-bdd_con_python_behave/README.md) | Semana 24 - Próximamente |
