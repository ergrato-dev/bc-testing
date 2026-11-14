from datetime import date

import pytest
from faker import Faker

from repository import BookRepository, OutOfStockError
# PASO 5: descomenta también este import.
# from tests.factories import BookFactory, LoanFactory


# ============================================
# PASO 5: test_faker_with_seed_is_repeatable
# ============================================
# Descomenta las siguientes líneas:
# def test_faker_with_seed_is_repeatable():
#     first, second = Faker("es_ES"), Faker("es_ES")
#     first.seed_instance(42)
#     second.seed_instance(42)
#
#     assert first.name() == second.name()


# ============================================
# PASO 6: test_factory_overrides_only_what_the_test_cares_about
# ============================================
# Descomenta las siguientes líneas:
# def test_factory_overrides_only_what_the_test_cares_about(session):
#     BookFactory(stock=0)
#     available = BookFactory(stock=3)
#
#     assert BookRepository(session).list_available() == [available]


# ============================================
# PASO 7: test_create_batch_builds_many_rows
# ============================================
# Descomenta las siguientes líneas:
# def test_create_batch_builds_many_rows(session):
#     BookFactory.create_batch(5)
#
#     assert len(BookRepository(session).list_available()) == 5


# ============================================
# PASO 8: test_sub_factory_creates_the_related_book
# ============================================
# Descomenta las siguientes líneas:
# def test_sub_factory_creates_the_related_book(session):
#     loan = LoanFactory(book__title="Dune")
#
#     assert loan.book.id is not None
#     assert loan.book.title == "Dune"


# ============================================
# PASO 9: test_lend_last_copy_then_out_of_stock
# ============================================
# Descomenta las siguientes líneas:
# def test_lend_last_copy_then_out_of_stock(session):
#     book = BookFactory(stock=1)
#     repo = BookRepository(session)
#
#     repo.lend(book.isbn, "ana@example.com", date(2026, 3, 2))
#
#     with pytest.raises(OutOfStockError):
#         repo.lend(book.isbn, "luis@example.com", date(2026, 3, 2))
