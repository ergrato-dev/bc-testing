import pytest

from library.models import Book
from library.repository import BookRepository, DuplicateIsbnError


def test_list_available_skips_books_without_stock(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=0))
    babel = repo.add(Book(isbn="9780000000002", title="Babel", stock=2))

    assert repo.list_available() == [babel]


def test_add_rejects_duplicate_isbn(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=1))

    with pytest.raises(DuplicateIsbnError):
        repo.add(Book(isbn="9780000000001", title="Babel", stock=1))
