# Lab 04 - PostgreSQL con Testcontainers y migraciones con Alembic

## Objetivo

Ejecutar la misma suite en SQLite y en un PostgreSQL real arrancado por Testcontainers, encontrar un error que SQLite no ve, y probar que las migraciones de Alembic suben, coinciden con los modelos y bajan.

## Tiempo estimado

80 minutos.

## Preparación

Este lab necesita **Docker**. Comprueba que responde:

```bash
docker info --format '{{.ServerVersion}}'
# 29.9.3
```

```bash
cd starter
uv sync
```

Qué hay en `starter/`:

- **`pyproject.toml`:**
  - en `dependencies`: `alembic`, `psycopg[binary]` (el driver de PostgreSQL) y `sqlalchemy`;
  - en `dev`: `pytest` y `testcontainers[postgres]`.
- **`compose.yaml`:** un PostgreSQL para generar migraciones (Paso 5). No lo usan los tests.
- **`tests/conftest.py`:** el contenedor (PASO 1) y el `engine` parametrizado (PASO 2), comentados. La fixture `session` es la del lab 02.
- **`tests/test_both_databases.py`:** tres tests activos y uno comentado (PASO 3).
- **`tests/test_migrations.py`:** comentado (PASOS 5 a 8).

La [teoría 04](../../1-teoria/04-postgresql-testcontainers-y-alembic.md) explica Testcontainers y Alembic.

## Paso a paso

### Paso 1: el contenedor (PASO 1)

En `tests/conftest.py`, descomenta `POSTGRES_IMAGE` y la fixture `postgres_url`.

- La imagen lleva versión exacta: `postgres:18.7-alpine`.
- `driver="psycopg"` hace que la URL empiece por `postgresql+psycopg://`, el driver que instalaste.

### Paso 2: un engine por motor (PASO 2)

Descomenta la fixture `engine`. Con `params=["sqlite", "postgresql"]`, pytest crea dos engines y ejecuta cada test una vez con cada uno. Los identificadores `[sqlite]` y `[postgresql]` aparecen en el nombre del test:

```bash
uv run pytest -v -k sqlite
# tests/test_both_databases.py::test_list_available_sorts_by_title[sqlite] PASSED [ 33%]
# ...
uv run pytest -q
# ......                                                                   [100%]
# 6 passed in 19.75s
```

La primera vez, Docker descarga la imagen y tarda más. Mientras corre, abre otra terminal y ejecuta `docker ps`: verás el contenedor `postgres:18.7-alpine` y el `testcontainers/ryuk`, que se encarga de borrarlo si pytest se interrumpe.

### Paso 3: un error que SQLite no ve (PASO 3)

Descomenta `test_title_longer_than_120_characters_is_rejected`, **pero deja comentadas las dos líneas del `if ... pytest.skip(...)`**:

```bash
uv run pytest -q tests/test_both_databases.py
# FAILED tests/test_both_databases.py::test_title_longer_than_120_characters_is_rejected[sqlite]
# 1 failed, 7 passed in 5.09s
```

En PostgreSQL pasa: la columna es `VARCHAR(120)` y un título de 121 caracteres lanza `DataError`. En SQLite falla porque SQLite guarda el título sin comprobar la longitud.

### Paso 4: saltar con motivo

Descomenta las dos líneas del `if`:

```python
    if session.bind.dialect.name == "sqlite":
        pytest.skip("SQLite no aplica la longitud de VARCHAR")
```

```bash
uv run pytest -q -rs tests/test_both_databases.py
# SKIPPED [1] tests/test_both_databases.py:40: SQLite no aplica la longitud de VARCHAR
# 7 passed, 1 skipped in 4.55s
```

`-rs` muestra el motivo de cada test saltado. Un skip con motivo deja constancia de lo que el motor no comprueba.

### Paso 5: generar la migración con Alembic

1. **Arranca el PostgreSQL de desarrollo:**

   ```bash
   docker compose up -d --wait
   ```

   - `--wait` espera a que el `healthcheck` (`pg_isready`) confirme que acepta conexiones.
   - El puerto es `55432` y no `5432`, para no chocar con un PostgreSQL instalado en tu equipo.

2. **Crea la estructura de Alembic:**

   ```bash
   uv run alembic init migrations
   ```

3. **En `alembic.ini`**, cambia la línea `sqlalchemy.url`:

   ```ini
   sqlalchemy.url = postgresql+psycopg://library:library@localhost:55432/library
   ```

4. **En `migrations/env.py`**, sustituye `target_metadata = None` por:

   ```python
   from models import Base

   target_metadata = Base.metadata
   ```

5. **Genera la migración:**

   ```bash
   uv run alembic revision --autogenerate -m "create books and loans"
   # INFO  [alembic.autogenerate.compare.tables] Detected added table 'books'
   # INFO  [alembic.autogenerate.compare.tables] Detected added table 'loans'
   ```

6. **Abre el archivo nuevo de `migrations/versions/`.** `upgrade()` crea las dos tablas con sus restricciones y `downgrade()` las borra en orden inverso: primero `loans`, que depende de `books`.

7. **Para el PostgreSQL de desarrollo:**

   ```bash
   docker compose down
   ```

### Paso 6: la base de datos de migraciones (PASOS 5 y 6)

En `tests/test_migrations.py`, descomenta `alembic_config` y `table_names`.

- `alembic_config` crea una base de datos vacía, `migrations`, dentro del mismo contenedor y apunta Alembic hacia ella.
- `isolation_level="AUTOCOMMIT"` hace falta porque `CREATE DATABASE` no se puede ejecutar dentro de una transacción.

### Paso 7: `upgrade` y `check` (PASO 7)

Descomenta `test_upgrade_head_matches_the_models`. `command.check` compara los modelos con la base de datos ya migrada y falla si hay diferencias.

### Paso 8: `downgrade` (PASO 8)

Descomenta `test_downgrade_base_removes_every_table` y ejecuta todo:

```bash
uv run pytest -q
# 9 passed, 1 skipped in 5.79s
```

### Paso 9: experimento, modelo sin migración

Añade una columna al modelo `Book` sin generar migración:

```python
    author: Mapped[str] = mapped_column(String(80))
```

```bash
uv run pytest -q tests/test_migrations.py
# E           alembic.util.exc.AutogenerateDiffsDetected: New upgrade operations detected: [('add_column', None, 'books', Column('author', String(length=80), table=<books>, nullable=False))]
# 1 failed, 1 passed in 3.75s
```

Ese test es el que impide que un modelo nuevo llegue a producción sin su migración. Borra la línea `author`.

## Comprobación

```bash
cd ../solution
uv sync
uv run pytest -q -rs
# SKIPPED [1] tests/test_both_databases.py:40: SQLite no aplica la longitud de VARCHAR
# 9 passed, 1 skipped in 5.79s
```

## Preguntas de cierre

1. ¿Por qué el contenedor tiene scope `session` y no `function`?
2. Si un test pasa en PostgreSQL y falla en SQLite, ¿cuál de los dos resultados describe mejor lo que pasará en producción? ¿Por qué?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Datos de prueba con Faker y Factory Boy](../lab-03-datos-con-faker-y-factory-boy/README.md) | [Proyecto — Proyecto Semanal - Capa de datos del dominio con tests de repositorio](../../3-proyecto/README.md) → |

[↑ Semana 23](../../README.md)
