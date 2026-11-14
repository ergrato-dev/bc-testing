# 01 - Estrategias para testear la capa de datos

> Lenguaje: **Python**

---

## 🎯 Objetivos

- Elegir dónde testear el acceso a datos: con mocks, con SQLite en memoria o con PostgreSQL real.
- Leer y escribir lo mínimo de SQLAlchemy 2.0 que necesita un test: modelos, `Session` y `select()`.
- Escribir el primer test de repositorio contra una base de datos de verdad.

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
**Impacto:** el test es más lento que uno unitario (milisegundos con SQLite, segundos con PostgreSQL en Docker), así que se reserva para la capa de datos.

## Tres estrategias

| Estrategia | Qué detecta | Coste | Cuándo |
|---|---|---|---|
| Mock de la sesión | Que se llama a la sesión | Nulo | Lógica de negocio que *usa* el repositorio (semana 18) |
| SQLite en memoria | Consultas, `UNIQUE` y `CHECK` (las claves foráneas solo con `PRAGMA foreign_keys = ON`) | Milisegundos, sin instalar nada | Ciclo rápido mientras escribes el repositorio |
| PostgreSQL con Testcontainers | Todo lo anterior + tipos, longitudes y comportamiento del motor de producción | 2–5 s de arranque por sesión de pytest, necesita Docker | Antes de abrir el PR y en CI |

En este bootcamp la base de datos de producción es siempre **PostgreSQL**. SQLite es un atajo para iterar rápido, no un sustituto: la [teoría 04](04-postgresql-testcontainers-y-alembic.md) muestra un test que pasa en SQLite y falla en PostgreSQL.

---

## SQLAlchemy 2.0: lo mínimo para testear

SQLAlchemy es el ORM más usado en Python. Aquí solo ves lo que el test necesita; el ORM a fondo está en `bc-python` y el diseño de esquemas en `bc-postgresql`. Las versiones de esta semana están en [stack-versions](../../../docs/stack-versions.md).

### 1. Instalar

```bash
uv add sqlalchemy==2.1.8
uv add --dev pytest==9.1.1
```

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

El modelo `Loan` (préstamo) completo está en el [lab 01](../2-practicas/lab-01-repositorio-con-sqlite/README.md).

### 3. Crear el engine y las tablas

```python
from sqlalchemy import create_engine

engine = create_engine("sqlite+pysqlite:///:memory:")
Base.metadata.create_all(engine)
```

- **`engine`:** sabe cómo conectarse a la base de datos. La URL dice el motor (`sqlite`), el driver (`pysqlite`) y la base (`:memory:`, que vive en RAM y desaparece al cerrar la conexión).
- **`create_all`:** ejecuta un `CREATE TABLE` por cada modelo de `Base`. En tests es suficiente; en producción el esquema se crea con migraciones ([teoría 04](04-postgresql-testcontainers-y-alembic.md)).

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

## El primer test de repositorio

El repositorio encapsula las consultas. Este es su método más simple:

```python
# repository.py (extracto)
def get_by_isbn(self, isbn: str) -> Book | None:
    stmt = select(Book).where(Book.isbn == isbn)
    return self.session.scalars(stmt).one_or_none()
```

La fixture abre una base de datos nueva para cada test:

```python
# tests/conftest.py
@pytest.fixture
def session():
    engine = create_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
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
# .......                                                                  [100%]
# 7 passed in 0.06s
```

También puedes comprobar una restricción. `match` busca el nombre que le diste al `CheckConstraint` dentro del mensaje de error:

```python
def test_negative_stock_violates_check_constraint(session):
    session.add(Book(isbn="9780000000001", title="Dune", stock=-1))

    with pytest.raises(IntegrityError, match="stock_non_negative"):
        session.flush()
```

> Crear el engine en cada test aísla bien, pero repite el `CREATE TABLE` cada vez. Con decenas de tablas o con PostgreSQL eso se nota. La [teoría 02](02-fixtures-y-rollback-por-test.md) crea el esquema una sola vez y aísla los tests con transacciones.

---

## ✅ Resumen

- El mock de la sesión prueba llamadas; el test de repositorio prueba SQL.
- SQLite en memoria sirve para iterar rápido. PostgreSQL con Testcontainers es la verificación final.
- Para testear, SQLAlchemy 2.0 se reduce a modelos con `Mapped`, `create_all`, `Session` y `select()`.
- `UNIQUE`, `CHECK` y las claves foráneas las aplica la base de datos: solo un test contra la BD las verifica. SQLite no aplica las claves foráneas por defecto; PostgreSQL sí.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 23 - Python Testing VIII: Testing de Bases de Datos](../README.md) | [Teoría — 02 - Fixtures de base de datos y rollback por test](02-fixtures-y-rollback-por-test.md) → |

[↑ Semana 23](../README.md)
