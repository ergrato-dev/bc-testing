from datetime import date

import pytest
from sqlalchemy.exc import DataError

from models import Book
from repository import BookRepository, DuplicateIsbnError, OutOfStockError

TODAY = date(2026, 3, 2)


def test_list_available_sorts_by_title(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="dune", stock=1))
    repo.add(Book(isbn="9780000000002", title="Babel", stock=1))

    titles = [book.title for book in repo.list_available()]

    assert titles == ["Babel", "dune"]


def test_duplicate_isbn_raises_domain_error(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=1))

    with pytest.raises(DuplicateIsbnError):
        repo.add(Book(isbn="9780000000001", title="Otra edición", stock=1))


def test_lend_without_stock_raises(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=0))

    with pytest.raises(OutOfStockError):
        repo.lend("9780000000001", "ana@example.com", TODAY)


# ============================================
# PASO 3: test_title_longer_than_120_characters_is_rejected
# ============================================
# Descomenta las siguientes líneas:
# def test_title_longer_than_120_characters_is_rejected(session):
#     if session.bind.dialect.name == "sqlite":
#         pytest.skip("SQLite no aplica la longitud de VARCHAR")
#     session.add(Book(isbn="9780000000001", title="x" * 121, stock=1))
#
#     with pytest.raises(DataError):
#         session.flush()
