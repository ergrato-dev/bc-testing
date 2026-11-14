# Proyecto Semanal - Capa de datos del dominio con tests de repositorio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Dar a tu dominio una capa de datos con SQLAlchemy y probarla como en los labs:

- una suite de repositorio con rollback por test;
- factories para los datos;
- la misma suite ejecutada en SQLite y en PostgreSQL con Testcontainers;
- migraciones de Alembic probadas.

## Dónde va

Esta semana empieza tu **[proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)**, el repositorio `qa-<nombre>-<apellido>`, que crecerá hasta la semana 46. Esta semana añades `python/data/`, `python/tests/repository/`, `python/migrations/` y el `compose.yaml` de la raíz.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Paso a paso

### Paso 1: crear el repositorio (solo esta semana)

1. Crea en GitHub un repositorio vacío llamado `qa-<nombre>-<apellido>`, por ejemplo `qa-ana-perez`.
2. Clónalo y crea la rama de la semana:

   ```bash
   git clone git@github.com:<tu-usuario>/qa-ana-perez.git
   cd qa-ana-perez
   git switch -c week-23
   ```

3. Copia tus entregas anteriores en `js/` (semanas 7–15) y `python/` (semanas 16–22). Si `python/` ya tiene un `pyproject.toml`, consérvalo.
4. Copia encima el contenido de `starter/`:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-23-testing_de_bases_de_datos_en_python/3-proyecto/starter/. .
   ```

   `-n` no sobrescribe archivos que ya existan.

5. Si ya tenías `python/pyproject.toml`, el del starter no se copió. Añade las dependencias nuevas:

   ```bash
   cd python
   uv add alembic==1.20.1 "psycopg[binary]==3.3.9" sqlalchemy==2.1.8
   uv add --dev factory-boy==3.3.3 faker==40.54.0 pytest==9.1.1 "testcontainers[postgres]==4.15.0"
   ```

   Comprueba que `[tool.pytest.ini_options]` tenga `pythonpath = ["."]`, igual que el `pyproject.toml` del starter.

### Paso 2: modelos y repositorio

En `python/data/models.py` y `python/data/repository.py`, sigue los `TODO`:

1. **Entidad principal:** renómbrala a la de tu dominio. Necesita un código único (`String(N), unique=True`), un nombre (`String(120)`) y un estado limitado con `CheckConstraint`.
2. **Segunda entidad:** relacionada con la primera mediante `ForeignKey`, por ejemplo un préstamo, una venta o una reserva.
3. **Repositorio:**
   - `add` hace `commit()` y, ante un `IntegrityError`, hace `rollback()` y lanza un error del dominio;
   - `get_by_code`;
   - `list_by_status`, ordenado;
   - la operación principal de tu dominio, que cambia el estado y crea la segunda entidad.

Referencia: [lab 01](../2-practicas/lab-01-repositorio-con-sqlite/README.md).

### Paso 3: rollback por test

En `python/tests/repository/conftest.py`:

1. `engine` con `scope="session"`, que crea las tablas una vez.
2. `session` con transacción externa y `join_transaction_mode="create_savepoint"`.
3. En SQLite, `connect_args={"autocommit": False}`.

Referencia: [lab 02](../2-practicas/lab-02-rollback-por-test/README.md).

### Paso 4: la suite (al menos 10 tests)

En `python/tests/repository/`, renombra `test_item_repository.py` a `test_<entidad>_repository.py`. La suite debe cubrir:

- alta con `id` asignado y búsqueda por código (existente e inexistente);
- código duplicado: lanza el error del dominio y **conserva** el primer registro;
- filtro y orden de `list_by_status`;
- la operación principal: cambia el estado y crea la segunda entidad;
- la operación principal rechazada en cada estado no válido (usa `parametrize`);
- un estado fuera del `CheckConstraint` lanza `IntegrityError`.

```bash
cd python
uv run pytest -q tests/repository -k sqlite
```

### Paso 5: factories

En `python/tests/repository/factories.py`:

1. Crea una factory por entidad con `SQLAlchemyModelFactory`, `sqlalchemy_session_persistence = "flush"` y `factory.Sequence` para el código único.
2. Usa `SubFactory` en la segunda entidad.
3. Conéctalas a la sesión con una fixture `autouse` y fija la semilla.
4. Cambia los tests del Paso 4 que preparaban datos a mano para que usen las factories.

Referencia: [lab 03](../2-practicas/lab-03-datos-con-faker-y-factory-boy/README.md).

### Paso 6: PostgreSQL con Testcontainers

1. Añade a `conftest.py` la fixture `postgres_url`, con `postgres:18.7-alpine`.
2. Parametriza `engine` con `["sqlite", "postgresql"]`.
3. Añade un test de longitud del nombre (121 caracteres). En SQLite debe saltarse con `pytest.skip` y un motivo.

```bash
uv run pytest -q -rs tests/repository
```

Referencia: [lab 04](../2-practicas/lab-04-postgresql-con-testcontainers-y-alembic/README.md), pasos 1 a 4.

### Paso 7: migraciones

1. En `python/`, configura Alembic:
   - ejecuta `uv run alembic init migrations`;
   - en `alembic.ini`, usa la URL del `compose.yaml`;
   - en `migrations/env.py`, pon `from data.models import Base`.
2. Desde la raíz del repositorio, arranca PostgreSQL con `docker compose up -d --wait`.
3. Desde `python/`, genera la migración con `uv run alembic revision --autogenerate -m "..."`.
4. Desde la raíz, detén PostgreSQL con `docker compose down`.
5. Crea `tests/repository/test_migrations.py` con dos tests:
   - `upgrade head` + `check`;
   - `downgrade base`.

Referencia: [lab 04](../2-practicas/lab-04-postgresql-con-testcontainers-y-alembic/README.md), pasos 5 a 8.

### Paso 8: verificar y entregar

1. Ejecuta la verificación desde la carpeta del bootcamp, con Docker en marcha:

   ```bash
   PROYECTO=~/qa-ana-perez ./scripts/verificar-semana.sh 23
   ```

2. Haz commit y push de la rama.
3. Abre un pull request `week-23` → `main`.

## Ejemplos de adaptación por dominio

| Dominio | Entidad principal | Estados | Segunda entidad | Operación |
|---|---|---|---|---|
| Museo | `Piece` (pieza) | `available`, `on_loan`, `restoration` | `Loan` | Prestar una pieza a otra institución |
| Planetario | `Show` (función) | `scheduled`, `sold_out`, `cancelled` | `Ticket` | Vender una entrada |
| Acuario | `Tank` (tanque) | `open`, `full`, `maintenance` | `Specimen` | Añadir un ejemplar |

## ✅ Requisitos verificables

`./scripts/verificar-semana.sh 23` comprueba lo siguiente ([checks.json](../checks.json)):

| Comprobación | Cómo se verifica |
|---|---|
| La suite de repositorio pasa en SQLite | `uv run pytest -q tests/repository -k sqlite` termina con 0 |
| La misma suite pasa en PostgreSQL con Testcontainers | `uv run pytest -q tests/repository -k postgresql` termina con 0 |
| Al menos 10 tests de repositorio por base de datos | `--collect-only -k sqlite` recoge 10 o más |
| La fixture `session` hace rollback por test con savepoint | `conftest.py` usa `join_transaction_mode="create_savepoint"` fuera de un comentario |
| Los datos de prueba salen de factories de Factory Boy | `factories.py` declara al menos una clase `(SQLAlchemyModelFactory):` |
| Las migraciones de Alembic suben, coinciden con los modelos y bajan | `tests/repository/test_migrations.py` pasa |

Además, en el README de tu repositorio escribe un párrafo con:

- qué error encontró PostgreSQL que SQLite no veía;
- qué restricción de tu dominio protege la base de datos y no el código Python.

> La `solution/` del proyecto no se publica en el repositorio del bootcamp.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - PostgreSQL con Testcontainers y migraciones con Alembic](../2-practicas/lab-04-postgresql-con-testcontainers-y-alembic/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 23](../README.md)
