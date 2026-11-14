# Webgrafía - Semana 23

| Recurso | URL | Para qué |
|---|---|---|
| SQLAlchemy: Joining a Session into an External Transaction | https://docs.sqlalchemy.org/en/21/orm/session_transaction.html | Documentación oficial del patrón de rollback por test y de `join_transaction_mode`. |
| SQLAlchemy: dialecto SQLite | https://docs.sqlalchemy.org/en/21/dialects/sqlite.html | Transacciones y savepoints con `pysqlite`. |
| Python `sqlite3` | https://docs.python.org/3/library/sqlite3.html | El atributo `autocommit` (Python 3.12+) que hace funcionar los savepoints. |
| SQLite: Datatypes | https://www.sqlite.org/datatype3.html | Por qué SQLite no aplica la longitud de `VARCHAR`. |
| PostgreSQL 18: Character Types | https://www.postgresql.org/docs/18/datatype-character.html | `VARCHAR(n)` en PostgreSQL y el error al superar la longitud. |
| Factory Boy: ORMs (SQLAlchemy) | https://factoryboy.readthedocs.io/en/stable/orms.html | `SQLAlchemyModelFactory` y `sqlalchemy_session_persistence`. |
| Faker | https://faker.readthedocs.io/en/master/ | Providers, locales y semillas. |
| Testcontainers for Python | https://testcontainers-python.readthedocs.io/en/latest/ | Referencia de `PostgresContainer` y demás módulos. |
| Getting started with Testcontainers for Python | https://testcontainers.com/guides/getting-started-with-testcontainers-for-python/ | Guía oficial paso a paso con pytest y PostgreSQL. |
| Alembic: Tutorial | https://alembic.sqlalchemy.org/en/latest/tutorial.html | `init`, `revision`, `upgrade` y `downgrade`. |
| Alembic: Auto Generating Migrations | https://alembic.sqlalchemy.org/en/latest/autogenerate.html | Qué detecta `--autogenerate` y qué no, y el comando `check`. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 23](../../README.md)
