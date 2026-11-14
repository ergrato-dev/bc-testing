import pytest
from alembic import command
from alembic.config import Config
from sqlalchemy import create_engine, inspect
from sqlalchemy.engine import make_url


# ============================================
# PASO 5: alembic_config
# ============================================
# Descomenta las siguientes líneas:
# @pytest.fixture
# def alembic_config(postgres_url):
#     admin = create_engine(postgres_url, isolation_level="AUTOCOMMIT")
#     with admin.connect() as connection:
#         connection.exec_driver_sql("DROP DATABASE IF EXISTS migrations")
#         connection.exec_driver_sql("CREATE DATABASE migrations")
#     admin.dispose()
#
#     url = make_url(postgres_url).set(database="migrations")
#     config = Config("alembic.ini")
#     config.set_main_option("sqlalchemy.url", url.render_as_string(hide_password=False))
#     return config


# ============================================
# PASO 6: table_names
# ============================================
# Descomenta las siguientes líneas:
# def table_names(config):
#     engine = create_engine(config.get_main_option("sqlalchemy.url"))
#     names = set(inspect(engine).get_table_names())
#     engine.dispose()
#     return names


# ============================================
# PASO 7: test_upgrade_head_matches_the_models
# ============================================
# Descomenta las siguientes líneas:
# def test_upgrade_head_matches_the_models(alembic_config):
#     command.upgrade(alembic_config, "head")
#
#     assert table_names(alembic_config) == {"alembic_version", "books", "loans"}
#     command.check(alembic_config)


# ============================================
# PASO 8: test_downgrade_base_removes_every_table
# ============================================
# Descomenta las siguientes líneas:
# def test_downgrade_base_removes_every_table(alembic_config):
#     command.upgrade(alembic_config, "head")
#
#     command.downgrade(alembic_config, "base")
#
#     assert table_names(alembic_config) == {"alembic_version"}
