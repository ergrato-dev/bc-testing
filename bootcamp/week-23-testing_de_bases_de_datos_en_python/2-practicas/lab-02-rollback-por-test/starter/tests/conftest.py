import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from models import Base


@pytest.fixture(scope="session")
def engine():
    # PASO 3: sustituye la línea siguiente por las tres comentadas.
    engine = create_engine("sqlite+pysqlite:///:memory:")
    # engine = create_engine(
    #     "sqlite+pysqlite:///:memory:", connect_args={"autocommit": False}
    # )
    Base.metadata.create_all(engine)
    yield engine
    engine.dispose()


# PASO 1: fixture sin aislamiento. Bórrala en el PASO 2.
@pytest.fixture
def session(engine):
    with Session(engine) as session:
        yield session


# ============================================
# PASO 2: session con transacción externa y savepoint
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture
# def session(engine):
#     with engine.connect() as connection:
#         transaction = connection.begin()
#         session = Session(bind=connection, join_transaction_mode="create_savepoint")
#         yield session
#         session.close()
#         transaction.rollback()
