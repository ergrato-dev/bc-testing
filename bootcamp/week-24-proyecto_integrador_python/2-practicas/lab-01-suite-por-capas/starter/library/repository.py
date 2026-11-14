from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from library.models import Book


class DuplicateIsbnError(Exception):
    pass


class BookRepository:
    def __init__(self, session: Session) -> None:
        self.session = session

    def add(self, book: Book) -> Book:
        self.session.add(book)
        try:
            self.session.commit()
        except IntegrityError as error:
            self.session.rollback()
            raise DuplicateIsbnError(book.isbn) from error
        return book

    def list_available(self) -> list[Book]:
        stmt = select(Book).where(Book.stock > 0).order_by(Book.title)
        return list(self.session.scalars(stmt))
