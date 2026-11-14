from datetime import date

from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from models import Book, Loan


class DuplicateIsbnError(Exception):
    pass


class OutOfStockError(Exception):
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

    def get_by_isbn(self, isbn: str) -> Book | None:
        stmt = select(Book).where(Book.isbn == isbn)
        return self.session.scalars(stmt).one_or_none()

    def list_available(self) -> list[Book]:
        stmt = select(Book).where(Book.stock > 0).order_by(Book.title)
        return list(self.session.scalars(stmt))

    def lend(self, isbn: str, member_email: str, today: date) -> Loan:
        book = self.get_by_isbn(isbn)
        if book is None or book.stock == 0:
            raise OutOfStockError(isbn)
        book.stock -= 1
        loan = Loan(book=book, member_email=member_email, loaned_on=today)
        self.session.add(loan)
        self.session.commit()
        return loan
