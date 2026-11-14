from datetime import date

from sqlalchemy import CheckConstraint, ForeignKey, String
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship


class Base(DeclarativeBase):
    pass


class Book(Base):
    __tablename__ = "books"
    __table_args__ = (CheckConstraint("stock >= 0", name="stock_non_negative"),)

    id: Mapped[int] = mapped_column(primary_key=True)
    isbn: Mapped[str] = mapped_column(String(13), unique=True)
    title: Mapped[str] = mapped_column(String(120))
    stock: Mapped[int] = mapped_column(default=0)

    loans: Mapped[list["Loan"]] = relationship(back_populates="book")


class Loan(Base):
    __tablename__ = "loans"

    id: Mapped[int] = mapped_column(primary_key=True)
    book_id: Mapped[int] = mapped_column(ForeignKey("books.id"))
    member_email: Mapped[str] = mapped_column(String(120))
    loaned_on: Mapped[date]

    book: Mapped[Book] = relationship(back_populates="loans")
