import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from models import Base


# ============================================
# PASO 1: session
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture
# def session():
#     engine = create_engine("sqlite+pysqlite:///:memory:")
#     Base.metadata.create_all(engine)
#     with Session(engine) as session:
#         yield session
#     engine.dispose()
