# 03 - Datos de prueba con Faker y Factory Boy

> Lenguaje: **Python**

---

## 🎯 Objetivos

- Generar datos realistas y repetibles con Faker y una semilla fija.
- Crear filas con Factory Boy, indicando solo los campos que le importan al test.
- Construir objetos relacionados con `SubFactory`.
- Conectar las factories a la sesión con rollback de la [teoría 02](02-fixtures-y-rollback-por-test.md).

---

## El problema: el ruido en el *Arrange*

Para comprobar que `list_available` ignora los libros sin stock, el test solo necesita una cosa: un libro con `stock=0` y otro con stock. Escrito a mano, el *Arrange* obliga a inventar datos que el test no usa:

```python
repo.add(Book(isbn="9780000000001", title="Dune", stock=0))
repo.add(Book(isbn="9780000000002", title="Babel", stock=3))
```

Con un modelo de diez columnas obligatorias, el dato que importa queda escondido entre los demás. Las factories resuelven eso: generan los valores por defecto, y el test solo escribe lo que importa.

---

## Faker: datos realistas

```bash
uv add --dev faker==40.54.0
```

```python
from faker import Faker

fake = Faker("es_ES")   # locale: nombres, direcciones y textos en español de España
fake.seed_instance(42)  # semilla: la misma secuencia en cada ejecución
fake.name()
# 'Albano Llopis Hierro'
```

**Qué:** la semilla fija la secuencia de valores aleatorios.
**Para qué:** si un test falla con un dato generado, al volver a ejecutarlo obtienes el mismo dato y puedes reproducir el fallo.
**Impacto:** sin semilla, un test que depende del dato puede fallar una vez de cada cien y no volver a fallar cuando lo investigas.

> Para datos que deben cumplir una regla, como un ISBN único, no uses Faker: usa `factory.Sequence`, que garantiza un valor distinto en cada objeto.

---

## Factory Boy: objetos con valores por defecto

```bash
uv add --dev factory-boy==3.3.3
```

### 1. Declarar la factory

```python
# tests/factories.py
import factory
from factory.alchemy import SQLAlchemyModelFactory

from models import Book


class BookFactory(SQLAlchemyModelFactory):
    class Meta:
        model = Book
        sqlalchemy_session_persistence = "flush"

    isbn = factory.Sequence(lambda n: f"978{n:010d}")
    title = factory.Faker("sentence", nb_words=3, locale="es_ES")
    stock = 1
```

| Declaración | Qué produce |
|---|---|
| `factory.Sequence(lambda n: ...)` | Un valor distinto por objeto: `9780000000000`, `9780000000001`, ... |
| `factory.Faker("sentence", ...)` | Un valor de Faker. El primer argumento es el nombre del *provider* |
| `stock = 1` | Un valor fijo |
| `sqlalchemy_session_persistence = "flush"` | Tras crear el objeto hace `flush()`: la fila existe y tiene `id`, pero la transacción sigue abierta y el rollback del test la borra |

### 2. Darle la sesión del test

La factory necesita saber en qué sesión crear las filas. Una fixture `autouse` se la asigna antes de cada test:

```python
# tests/conftest.py
@pytest.fixture(autouse=True)
def bind_factories(session):
    for factory_class in (BookFactory, LoanFactory):
        factory_class._meta.sqlalchemy_session = session


@pytest.fixture(autouse=True)
def fixed_seed():
    factory.random.reseed_random(2026)
```

`factory.random.reseed_random` fija la semilla del Faker que usan las factories. Con la semilla `2026`, los dos primeros libros son siempre estos:

```text
9780000000000 | Respecto marzo. | 1
9780000000001 | Electoral vio. | 1
```

### 3. Usarla

```python
def test_factory_overrides_only_what_the_test_cares_about(session):
    BookFactory(stock=0)
    available = BookFactory(stock=3)

    assert BookRepository(session).list_available() == [available]
```

El test dice lo que importa: un libro sin stock y otro con stock. Lo demás lo pone la factory.

| Llamada | Resultado |
|---|---|
| `BookFactory()` | Crea y hace `flush` de un libro con los valores por defecto |
| `BookFactory(stock=0)` | Igual, pero con ese campo cambiado |
| `BookFactory.create_batch(5)` | Cinco libros |
| `BookFactory.build()` | El objeto Python, sin guardarlo en la BD |

### 4. Objetos relacionados con `SubFactory`

Un préstamo necesita un libro. `SubFactory` lo crea automáticamente:

```python
class LoanFactory(SQLAlchemyModelFactory):
    class Meta:
        model = Loan
        sqlalchemy_session_persistence = "flush"

    book = factory.SubFactory(BookFactory)
    member_email = factory.Faker("email", locale="es_ES")
    loaned_on = date(2026, 3, 2)
```

```python
loan = LoanFactory(book__title="Dune")
```

El doble guion bajo (`book__title`) cambia un campo del objeto relacionado sin tener que crearlo aparte.

---

## Factory o fixture

| Usa... | Cuando... |
|---|---|
| Una fixture | El dato es el mismo en todos los tests del módulo, como una configuración o un usuario administrador |
| Una factory | Cada test necesita variaciones del mismo objeto |

Las dos se combinan: la fixture `session` da el aislamiento y las factories crean los datos dentro de él.

---

## ✅ Resumen

- Faker genera datos realistas. Con `seed_instance` o `reseed_random`, la secuencia se repite en cada ejecución.
- Los valores que deben ser únicos salen de `factory.Sequence`, no de Faker.
- `SQLAlchemyModelFactory` con `sqlalchemy_session_persistence = "flush"` crea filas que el rollback del test borra.
- `SubFactory` crea los objetos relacionados, y `campo__subcampo` los ajusta.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 02 - Fixtures de base de datos y rollback por test](02-fixtures-y-rollback-por-test.md) | [Teoría — 04 - Migraciones con Alembic](04-migraciones-con-alembic.md) → |

[↑ Semana 23](../README.md)
