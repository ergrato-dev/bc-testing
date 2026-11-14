# 04 - PostgreSQL real con Testcontainers y migraciones con Alembic

> Lenguaje: **Python**

![La misma suite en SQLite y en PostgreSQL](../0-assets/02-sqlite-y-postgresql-con-testcontainers.svg)

---

## 🎯 Objetivos

- Arrancar un PostgreSQL desechable desde pytest con Testcontainers.
- Ejecutar la misma suite en SQLite y en PostgreSQL con una fixture parametrizada.
- Reconocer un test que pasa en SQLite y falla en PostgreSQL.
- Probar que las migraciones de Alembic suben, coinciden con los modelos y bajan.

---

## Requisito: Docker

Testcontainers arranca contenedores Docker desde el código del test. Antes de empezar, comprueba que Docker responde:

```bash
docker info --format '{{.ServerVersion}}'
# 29.9.3
```

Si el comando falla, arranca Docker Desktop (Windows y macOS) o el servicio `docker` (Linux, Ubuntu 24.04) y vuelve a probar. Docker a fondo está en `bc-docker`.

---

## Testcontainers: PostgreSQL desde una fixture

```bash
uv add "psycopg[binary]==3.3.9"
uv add --dev "testcontainers[postgres]==4.15.0"
```

- **`psycopg`:** el driver que conecta Python con PostgreSQL. El extra `[binary]` trae la librería compilada y evita instalar `libpq`.
- **`testcontainers[postgres]`:** la librería de contenedores con el módulo de PostgreSQL.

```python
from testcontainers.community.postgres import PostgresContainer

POSTGRES_IMAGE = "postgres:18.7-alpine"


@pytest.fixture(scope="session")
def postgres_url():
    with PostgresContainer(POSTGRES_IMAGE, driver="psycopg") as postgres:
        yield postgres.get_connection_url()
```

Qué ocurre al entrar en el `with`:

1. Testcontainers descarga la imagen si no la tienes; solo la primera vez tarda.
2. Arranca el contenedor en un puerto libre elegido al azar, así que nunca choca con otro PostgreSQL.
3. Espera a que PostgreSQL acepte conexiones.
4. `get_connection_url()` devuelve algo como `postgresql+psycopg://test:test@localhost:32771/test`.

Al salir del `with`, el contenedor se detiene y se borra.

**Qué:** un PostgreSQL de la misma versión que producción, creado para esta ejecución de pytest.
**Para qué:** probar contra el motor real sin instalar nada ni compartir una base de datos de desarrollo.
**Impacto:** añade unos segundos por ejecución, no por test: con scope `session` el contenedor se arranca una vez.

La imagen lleva versión exacta (`18.7-alpine`), nunca `latest`, para que todos los aprendices y CI prueben contra el mismo motor. La versión está en [stack-versions](../../../docs/stack-versions.md).

> `from testcontainers.postgres import ...` todavía funciona, pero emite un `DeprecationWarning`. La ruta actual es `testcontainers.community.postgres`.

---

## La misma suite en los dos motores

Si parametrizas la fixture `engine` (como en la [semana 17](../../week-17-parametrizacion_y_marks_con_pytest/README.md)), cada test que usa `session` se ejecuta una vez por motor:

```python
@pytest.fixture(scope="session", params=["sqlite", "postgresql"])
def engine(request):
    if request.param == "sqlite":
        url = "sqlite+pysqlite:///:memory:"
        connect_args = {"autocommit": False}
    else:
        url = request.getfixturevalue("postgres_url")
        connect_args = {}
    engine = create_engine(url, connect_args=connect_args)
    Base.metadata.create_all(engine)
    yield engine
    engine.dispose()
```

- **`request.getfixturevalue("postgres_url")`:** pide la fixture del contenedor solo cuando el parámetro es `postgresql`. Si filtras con `-k sqlite`, Docker no se usa.
- La fixture `session` de la [teoría 02](02-fixtures-y-rollback-por-test.md) no cambia.

```bash
uv run pytest -q -k sqlite       # solo SQLite, sin Docker
uv run pytest -q -k postgresql   # solo PostgreSQL
uv run pytest -q                 # los dos
```

### Un test que SQLite no detecta

```python
def test_title_longer_than_120_characters_is_rejected(session):
    session.add(Book(isbn="9780000000001", title="x" * 121, stock=1))

    with pytest.raises(DataError):
        session.flush()
```

```bash
uv run pytest -q tests/test_both_databases.py
# FAILED tests/test_both_databases.py::test_title_longer_than_120_characters_is_rejected[sqlite]
# 1 failed, 7 passed in 5.09s
```

PostgreSQL rechaza el título de 121 caracteres porque la columna es `VARCHAR(120)`. SQLite lo guarda sin protestar: no aplica la longitud de `VARCHAR`. Si solo probaras con SQLite, este error llegaría a producción.

Para no dar por roto lo que el motor no puede comprobar, el test se salta en SQLite y lo dice:

```python
    if session.bind.dialect.name == "sqlite":
        pytest.skip("SQLite no aplica la longitud de VARCHAR")
```

```bash
uv run pytest -q -rs tests/test_both_databases.py
# SKIPPED [1] tests/test_both_databases.py:40: SQLite no aplica la longitud de VARCHAR
# 7 passed, 1 skipped in 4.55s
```

---

## Migraciones con Alembic

En producción las tablas no se crean con `create_all`, sino con **migraciones**: scripts versionados que llevan el esquema de una versión a la siguiente. Alembic es la herramienta de migraciones de SQLAlchemy.

```bash
uv add alembic==1.20.1
uv run alembic init migrations
```

`alembic init` crea `alembic.ini` (configuración) y la carpeta `migrations/` con `env.py` y `versions/`. Hay que cambiar dos líneas:

```ini
# alembic.ini: la base de datos de desarrollo (la del compose.yaml)
sqlalchemy.url = postgresql+psycopg://library:library@localhost:55432/library
```

```python
# migrations/env.py: los modelos que Alembic compara con la base de datos
from models import Base

target_metadata = Base.metadata
```

Con PostgreSQL arrancado, Alembic compara los modelos con la base de datos vacía y escribe la migración:

```bash
docker compose up -d --wait
uv run alembic revision --autogenerate -m "create books and loans"
# INFO  [alembic.autogenerate.compare.tables] Detected added table 'books'
# INFO  [alembic.autogenerate.compare.tables] Detected added table 'loans'
```

### Qué se prueba de una migración

| Riesgo | Test |
|---|---|
| La migración falla en un PostgreSQL vacío | `command.upgrade(config, "head")` |
| Alguien cambió un modelo y no generó la migración | `command.check(config)` lanza `AutogenerateDiffsDetected` |
| El `downgrade` no deshace lo que hizo el `upgrade` | `command.downgrade(config, "base")` deja solo `alembic_version` |

```python
@pytest.fixture
def alembic_config(postgres_url):
    admin = create_engine(postgres_url, isolation_level="AUTOCOMMIT")
    with admin.connect() as connection:
        connection.exec_driver_sql("DROP DATABASE IF EXISTS migrations")
        connection.exec_driver_sql("CREATE DATABASE migrations")
    admin.dispose()

    url = make_url(postgres_url).set(database="migrations")
    config = Config("alembic.ini")
    config.set_main_option("sqlalchemy.url", url.render_as_string(hide_password=False))
    return config


def test_upgrade_head_matches_the_models(alembic_config):
    command.upgrade(alembic_config, "head")

    assert table_names(alembic_config) == {"alembic_version", "books", "loans"}
    command.check(alembic_config)
```

- La fixture reutiliza el contenedor, pero crea en él una base de datos vacía, `migrations`, porque la del `engine` ya tiene las tablas de `create_all`.
- `CREATE DATABASE` no puede ejecutarse dentro de una transacción. Por eso se usa `isolation_level="AUTOCOMMIT"`.
- `config.set_main_option` sustituye la URL de `alembic.ini` por la del contenedor.

Si añades una columna al modelo y olvidas la migración, `check` lo detecta:

```text
E   alembic.util.exc.AutogenerateDiffsDetected: New upgrade operations detected: [('add_column', None, 'books', Column('author', String(length=80), table=<books>, nullable=False))]
```

---

## ✅ Resumen

- Testcontainers arranca un PostgreSQL con versión fija en un puerto libre y lo borra al terminar.
- Una fixture `engine` parametrizada ejecuta la misma suite en SQLite y en PostgreSQL. Con `-k` eliges el motor.
- SQLite no aplica la longitud de `VARCHAR`. Los tests que dependen de ella se saltan en SQLite con un motivo explícito.
- Las migraciones se prueban con `upgrade head`, `check` y `downgrade base` sobre una base de datos vacía.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 03 - Datos de prueba con Faker y Factory Boy](03-datos-con-faker-y-factory-boy.md) | [Índice de labs](../2-practicas/README.md) → |

[↑ Semana 23](../README.md)
