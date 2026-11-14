from datetime import date

import pytest

from models import Book
from repository import BookRepository, DuplicateIsbnError

TODAY = date(2026, 3, 2)


def test_lend_commits_inside_the_test(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=2))

    repo.lend("9780000000001", "ana@example.com", TODAY)

    assert repo.get_by_isbn("9780000000001").stock == 1


def test_next_test_starts_with_an_empty_database(session):
    repo = BookRepository(session)

    assert repo.list_available() == []


def test_rollback_inside_the_code_keeps_previous_commits(session):
    repo = BookRepository(session)
    repo.add(Book(isbn="9780000000001", title="Dune", stock=1))

    with pytest.raises(DuplicateIsbnError):
        repo.add(Book(isbn="9780000000001", title="Otra edición", stock=1))

    assert repo.get_by_isbn("9780000000001").title == "Dune"
