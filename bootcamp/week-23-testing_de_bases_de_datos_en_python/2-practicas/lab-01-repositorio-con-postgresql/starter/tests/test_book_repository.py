from datetime import date

import pytest
from sqlalchemy import select
from sqlalchemy.exc import DataError, IntegrityError

from models import Book, Loan
from repository import BookRepository, DuplicateIsbnError, OutOfStockError

TODAY = date(2026, 3, 2)


# ============================================
# PASO 3: test_add_assigns_an_id
# ============================================
# Descomenta las siguientes líneas:
# def test_add_assigns_an_id(session):
#     repo = BookRepository(session)
#
#     book = repo.add(Book(isbn="9780000000001", title="Dune", stock=2))
#
#     assert book.id is not None


# ============================================
# PASO 4: test_get_by_isbn_returns_none_when_missing
# ============================================
# Descomenta las siguientes líneas:
# def test_get_by_isbn_returns_none_when_missing(session):
#     repo = BookRepository(session)
#
#     assert repo.get_by_isbn("9789999999999") is None


# ============================================
# PASO 5: test_list_available_skips_out_of_stock_and_sorts_by_title
# ============================================
# Descomenta las siguientes líneas:
# def test_list_available_skips_out_of_stock_and_sorts_by_title(session):
#     repo = BookRepository(session)
#     repo.add(Book(isbn="9780000000001", title="Dune", stock=1))
#     repo.add(Book(isbn="9780000000002", title="Ciropedia", stock=0))
#     repo.add(Book(isbn="9780000000003", title="Babel", stock=3))
#
#     titles = [book.title for book in repo.list_available()]
#
#     assert titles == ["Babel", "Dune"]


# ============================================
# PASO 6: test_lend_decrements_stock_and_records_the_loan
# ============================================
# Descomenta las siguientes líneas:
# def test_lend_decrements_stock_and_records_the_loan(session):
#     repo = BookRepository(session)
#     repo.add(Book(isbn="9780000000001", title="Dune", stock=2))
#
#     repo.lend("9780000000001", "ana@example.com", TODAY)
#
#     assert repo.get_by_isbn("9780000000001").stock == 1
#     loans = session.scalars(select(Loan)).all()
#     assert [(loan.member_email, loan.loaned_on) for loan in loans] == [
#         ("ana@example.com", TODAY)
#     ]


# ============================================
# PASO 7: test_lend_without_stock_raises
# ============================================
# Descomenta las siguientes líneas:
# def test_lend_without_stock_raises(session):
#     repo = BookRepository(session)
#     repo.add(Book(isbn="9780000000001", title="Dune", stock=0))
#
#     with pytest.raises(OutOfStockError):
#         repo.lend("9780000000001", "ana@example.com", TODAY)


# ============================================
# PASO 8: test_duplicate_isbn_raises_domain_error
# ============================================
# Descomenta las siguientes líneas:
# def test_duplicate_isbn_raises_domain_error(session):
#     repo = BookRepository(session)
#     repo.add(Book(isbn="9780000000001", title="Dune", stock=1))
#
#     with pytest.raises(DuplicateIsbnError):
#         repo.add(Book(isbn="9780000000001", title="Otra edición", stock=1))


# ============================================
# PASO 9: test_negative_stock_violates_check_constraint
# ============================================
# Descomenta las siguientes líneas:
# def test_negative_stock_violates_check_constraint(session):
#     session.add(Book(isbn="9780000000001", title="Dune", stock=-1))
#
#     with pytest.raises(IntegrityError, match="stock_non_negative"):
#         session.flush()


# ============================================
# PASO 10: test_title_longer_than_120_characters_is_rejected
# ============================================
# Descomenta las siguientes líneas:
# def test_title_longer_than_120_characters_is_rejected(session):
#     session.add(Book(isbn="9780000000001", title="x" * 121, stock=1))
#
#     with pytest.raises(DataError):
#         session.flush()
