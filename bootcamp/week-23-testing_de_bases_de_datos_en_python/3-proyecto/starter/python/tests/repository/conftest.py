import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from data.models import Base


# TODO (paso 3): engine con scope="session", una sola vez por base de datos.
# TODO (paso 3): session con transacción externa y
#                join_transaction_mode="create_savepoint".
# TODO (paso 6): parametriza engine con ["sqlite", "postgresql"].
@pytest.fixture
def session():
    engine = create_engine("sqlite+pysqlite:///:memory:")
    Base.metadata.create_all(engine)
    with Session(engine) as session:
        yield session
    engine.dispose()
