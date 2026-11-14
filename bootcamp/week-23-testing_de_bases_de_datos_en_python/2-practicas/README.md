# Prácticas - Semana 23

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código comentado por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo | Necesita Docker |
|---|---|---|---|
| [Lab 01 - Repositorio con SQLite en memoria](lab-01-repositorio-con-sqlite/README.md) | Modelos, `select()`, `UNIQUE` y `CHECK` probados contra una BD real | 60 min | No |
| [Lab 02 - Rollback por test](lab-02-rollback-por-test/README.md) | Esquema una vez por sesión, transacción externa, `create_savepoint` y `sqlite3` con `autocommit=False` | 50 min | No |
| [Lab 03 - Datos de prueba con Faker y Factory Boy](lab-03-datos-con-faker-y-factory-boy/README.md) | `SQLAlchemyModelFactory`, `Sequence`, `SubFactory` y semilla fija | 50 min | No |
| [Lab 04 - PostgreSQL con Testcontainers y migraciones con Alembic](lab-04-postgresql-con-testcontainers-y-alembic/README.md) | La misma suite en dos motores, el `VARCHAR` que SQLite no comprueba, y `upgrade`/`check`/`downgrade` | 80 min | Sí |

## Antes de empezar

```bash
uv --version     # uv 0.13.7 o superior
docker info --format '{{.ServerVersion}}'   # solo para el lab 04
```

Cada lab es un proyecto `uv` independiente:

```bash
cd lab-NN-.../starter
uv sync
uv run pytest -q
```

`.python-version` fija Python 3.14. Si no lo tienes, `uv sync` lo descarga.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - PostgreSQL real con Testcontainers y migraciones con Alembic](../1-teoria/04-postgresql-testcontainers-y-alembic.md) | [Lab 01 - Repositorio con SQLite en memoria](lab-01-repositorio-con-sqlite/README.md) → |

[↑ Semana 23](../README.md)
