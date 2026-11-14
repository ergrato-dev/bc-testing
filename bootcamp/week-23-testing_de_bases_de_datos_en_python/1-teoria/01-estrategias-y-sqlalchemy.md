# 01 - Estrategias para testear la capa de datos

> Lenguaje: **Python**

---

## 🎯 Objetivos

- Elegir dónde testear el acceso a datos: con mocks o con un PostgreSQL real.
- Leer y escribir lo mínimo de SQLAlchemy 2.0 que necesita un test: modelos, `Session` y `select()`.
- Arrancar un PostgreSQL desechable desde pytest con Testcontainers.
- Escribir el primer test de repositorio contra ese PostgreSQL.

---

## El problema

Hasta la semana 22 el código bajo prueba vivía en memoria. En la [semana 18](../../week-18-mocking_con_unittest_mock_y_pytest_mock/README.md) viste cómo sustituir una dependencia por un mock. Con la base de datos esa receta se queda corta:

```python
# Qué prueba este test: que el código llama a session.add.
# Qué NO prueba: que la consulta SQL sea correcta ni que la BD acepte el dato.
session = mocker.Mock()
BookRepository(session).add(book)
session.add.assert_called_once_with(book)
```

Los errores típicos de la capa de datos no están en tu código Python, sino en la conversación con la base de datos:

- un `WHERE` mal escrito;
- un orden que depende de mayúsculas;
- una restricción `UNIQUE` o `CHECK` que la tabla no tiene;
- una columna que la migración olvidó.

Un mock no ve ninguno de esos errores.

**Qué:** un *test de repositorio* ejecuta la consulta real contra una base de datos real, aunque sea pequeña y temporal.
**Para qué:** detectar los errores que solo aparecen al hablar SQL.
**Impacto:** el test es más lento que uno unitario (unos segundos de arranque de PostgreSQL por ejecución), así que se reserva para la capa de datos.

## Dos estrategias (y una que descartamos)

| Estrategia | Qué detecta | Coste | Cuándo |
|---|---|---|---|
| Mock de la sesión | Que se llama a la sesión | Nulo | Lógica de negocio que *usa* el repositorio (semana 18) |
| PostgreSQL con Testcontainers | Consultas, orden, `UNIQUE`, `CHECK`, claves foráneas, tipos y longitudes, con el motor de producción | 3–5 s de arranque por ejecución de pytest; necesita Docker | Toda la capa de datos: repositorios y migraciones |

En este bootcamp la base de datos es siempre **PostgreSQL**, también en los tests.

### ¿Y SQLite en memoria?

Muchos tutoriales prueban con SQLite en memoria porque no necesita instalar nada. Aquí no se usa, porque se comporta distinto que PostgreSQL justo en lo que un test de repositorio quiere comprobar:

| Comportamiento | PostgreSQL | SQLite |
|---|---|---|
| Título de 121 caracteres en un `VARCHAR(120)` | Lo rechaza con `DataError` | Lo guarda sin protestar |
| Claves foráneas | Siempre activas | Desactivadas salvo `PRAGMA foreign_keys = ON` |
| `SAVEPOINT` desde Python | Funcionan con `psycopg` | El módulo `sqlite3` los ignora salvo con `autocommit=False` |

Un test verde en SQLite no dice cómo se comportará PostgreSQL. Con Testcontainers, el coste de usar el motor real son unos segundos por ejecución, no por test.

---

## SQLAlchemy 2.0: lo mínimo para testear

SQLAlchemy es el ORM más usado en Python. Aquí solo ves lo que el test necesita; el ORM a fondo está en `bc-python` y el diseño de esquemas en `bc-postgresql`. Las versiones de esta semana están en [stack-versions](../../../docs/stack-versions.md).

### 1. Instalar

```bash
uv add sqlalchemy==2.1.8 "psycopg[binary]==3.3.9"
uv add --dev pytest==9.1.1 "testcontainers[postgres]==4.15.0"
```

- **`psycopg`:** el driver que conecta Python con PostgreSQL. El extra `[binary]` trae la librería compilada y evita instalar `libpq`.
- **`testcontainers[postgres]`:** arranca contenedores Docker desde el test; el extra trae el módulo de PostgreSQL.

### 2. Declarar los modelos

```python
# models.py
from datetime import date

from sqlalchemy import CheckConstraint, ForeignKey, String
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship


class Base(DeclarativeBase):
    pass


class Book(Base):
    __tablename__ = "books"
    __table_args__ = (CheckConstraint("stock >= 0", name="stock_non_negative"),)

    id: Mapped[int] = mapped_column(primary_key=True)
    isbn: Mapped[str] = mapped_column(String(13), unique=True)
    title: Mapped[str] = mapped_column(String(120))
    stock: Mapped[int] = mapped_column(default=0)

    loans: Mapped[list["Loan"]] = relationship(back_populates="book")
```

Qué significa cada pieza:

- **`Base`:** la clase de la que heredan todos los modelos. `Base.metadata` guarda la definición de todas las tablas.
- **`Mapped[int]`:** declara el tipo Python de la columna. Si no es `Optional`, la columna es `NOT NULL`.
- **`String(120)`:** se traduce a `VARCHAR(120)`.
- **`unique=True` y `CheckConstraint`:** son restricciones de la tabla. Las aplica la base de datos, no Python, y por eso solo un test contra la BD las comprueba.
- **`relationship`:** permite escribir `loan.book` en lugar de buscar por `book_id`.

El modelo `Loan` (préstamo) completo está en el [lab 01](../2-practicas/lab-01-repositorio-con-postgresql/README.md).

### 3. Crear el engine y las tablas

```python
from sqlalchemy import create_engine

engine = create_engine("postgresql+psycopg://test:test@localhost:32771/test")
Base.metadata.create_all(engine)
```

- **`engine`:** sabe cómo conectarse a la base de datos. La URL dice el motor (`postgresql`), el driver (`psycopg`), el usuario y la contraseña, el host y el puerto, y la base de datos.
- **`create_all`:** ejecuta un `CREATE TABLE` por cada modelo de `Base`. En tests es suficiente; en producción el esquema se crea con migraciones ([teoría 04](04-migraciones-con-alembic.md)).

En los tests no escribes esa URL a mano: te la da Testcontainers.

### 4. Consultar con `Session` y `select()`

```python
from sqlalchemy import select
from sqlalchemy.orm import Session

with Session(engine) as session:
    session.add(Book(isbn="9780000000001", title="Dune", stock=2))
    session.commit()

    stmt = select(Book).where(Book.stock > 0).order_by(Book.title)
    books = list(session.scalars(stmt))
```

- **`Session`:** es la *unidad de trabajo*. Acumula los cambios y los envía a la base de datos.
- **`flush()`:** envía el `INSERT` sin cerrar la transacción. La BD asigna el `id` y comprueba las restricciones.
- **`commit()`:** hace `flush()` y además confirma la transacción.
- **`select(Book)`:** es el estilo 2.0. Sustituye a `session.query(Book)`, que verás en tutoriales antiguos. `session.scalars(stmt)` devuelve objetos `Book` en lugar de filas.

---

## PostgreSQL desechable con Testcontainers

![Ciclo de vida del contenedor de PostgreSQL en una ejecución de pytest](../0-assets/02-postgresql-con-testcontainers.svg)

### Requisito: Docker

Testcontainers arranca contenedores Docker desde el código del test. Comprueba que Docker responde:

```bash
docker info --format '{{.ServerVersion}}'
# 29.9.3
```

Si el comando falla, arranca Docker Desktop (Windows y macOS) o el servicio `docker` (Linux, Ubuntu 24.04) y vuelve a probar. Docker a fondo está en `bc-docker`.

### La fixture del contenedor

```python
# tests/conftest.py
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

Al salir del `with`, el contenedor se detiene y se borra. Si pytest se interrumpe, el contenedor auxiliar `testcontainers/ryuk` lo borra igualmente.

**Qué:** un PostgreSQL de la misma versión que producción, creado para esta ejecución de pytest.
**Para qué:** probar contra el motor real sin instalar PostgreSQL ni compartir una base de datos de desarrollo.
**Impacto:** con scope `session`, el contenedor se arranca una vez por ejecución y no una vez por test.

La imagen lleva versión exacta (`18.7-alpine`), nunca `latest`, para que todos los aprendices y CI prueben contra el mismo motor.

> `from testcontainers.postgres import ...` todavía funciona, pero emite un `DeprecationWarning`. La ruta actual es `testcontainers.community.postgres`.

---

## El primer test de repositorio

El repositorio encapsula las consultas. Este es su método más simple:

```python
# repository.py (extracto)
def get_by_isbn(self, isbn: str) -> Book | None:
    stmt = select(Book).where(Book.isbn == isbn)
    return self.session.scalars(stmt).one_or_none()
```

La fixture `session` crea las tablas en el contenedor antes de cada test y las borra después:

```python
# tests/conftest.py
@pytest.fixture
def session(postgres_url):
    engine = create_engine(postgres_url)
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
    Base.metadata.drop_all(engine)
    engine.dispose()
```

Con esa fixture, el test comprueba el orden y el filtro de la consulta real:

```python
def test_list_available_skips_out_of_stock_and_sorts_by_title(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=1))
    repo.add(Book(isbn="9780000000002", title="Ciropedia", stock=0))
    repo.add(Book(isbn="9780000000003", title="Babel", stock=3))

    titles = [book.title for book in repo.list_available()]

    assert titles == ["Babel", "Dune"]
```

```bash
uv run pytest -q
# ........                                                                 [100%]
# 8 passed in 3.92s
```

Casi todo ese tiempo es el arranque del contenedor; los ocho tests juntos tardan una fracción de segundo.

También puedes comprobar las restricciones de la tabla. `match` busca el nombre que le diste al `CheckConstraint` dentro del mensaje de error:

```python
def test_negative_stock_violates_check_constraint(session):
    session.add(Book(isbn="9780000000001", title="Dune", stock=-1))

    with pytest.raises(IntegrityError, match="stock_non_negative"):
        session.flush()


def test_title_longer_than_120_characters_is_rejected(session):
    session.add(Book(isbn="9780000000001", title="x" * 121, stock=1))

    with pytest.raises(DataError):
        session.flush()
```

El segundo es el test que SQLite dejaría pasar: PostgreSQL responde `value too long for type character varying(120)`.

> Crear y borrar las tablas en cada test aísla bien, pero repite el `CREATE TABLE` cada vez. Con decenas de tablas eso se nota. La [teoría 02](02-fixtures-y-rollback-por-test.md) crea el esquema una sola vez y aísla los tests con transacciones.

---

## ✅ Resumen

- El mock de la sesión prueba llamadas; el test de repositorio prueba SQL.
- Los tests de repositorio se ejecutan contra PostgreSQL, el motor de producción. SQLite se descarta porque no aplica `VARCHAR` ni, por defecto, las claves foráneas.
- Para testear, SQLAlchemy 2.0 se reduce a modelos con `Mapped`, `create_all`, `Session` y `select()`.
- Testcontainers arranca un PostgreSQL con versión fija en un puerto libre, una vez por ejecución, y lo borra al terminar.
- `UNIQUE`, `CHECK`, `VARCHAR` y las claves foráneas las aplica la base de datos: solo un test contra la BD las verifica.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 23 - Python Testing VIII: Testing de Bases de Datos](../README.md) | [Teoría — 02 - Fixtures de base de datos y rollback por test](02-fixtures-y-rollback-por-test.md) → |

[↑ Semana 23](../README.md)
