import factory.random
import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from models import Base
# PASO 3: descomenta también este import.
# from tests.factories import BookFactory, LoanFactory


@pytest.fixture(scope="session")
def engine():
    engine = create_engine(
        "sqlite+pysqlite:///:memory:", connect_args={"autocommit": False}
    )
    Base.metadata.create_all(engine)
    yield engine
    engine.dispose()


@pytest.fixture
def session(engine):
    with engine.connect() as connection:
        transaction = connection.begin()
        session = Session(bind=connection, join_transaction_mode="create_savepoint")
        yield session
        session.close()
        transaction.rollback()


# ============================================
# PASO 3: bind_factories
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture(autouse=True)
# def bind_factories(session):
#     for factory_class in (BookFactory, LoanFactory):
#         factory_class._meta.sqlalchemy_session = session


# ============================================
# PASO 4: fixed_seed
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture(autouse=True)
# def fixed_seed():
#     factory.random.reseed_random(2026)
