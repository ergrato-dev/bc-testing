from datetime import date

import factory
from factory.alchemy import SQLAlchemyModelFactory

from models import Book, Loan


# ============================================
# PASO 1: BookFactory
# ============================================
# Descomenta las siguientes líneas:
# class BookFactory(SQLAlchemyModelFactory):
#     class Meta:
#         model = Book
#         sqlalchemy_session_persistence = "flush"
#
#     isbn = factory.Sequence(lambda n: f"978{n:010d}")
#     title = factory.Faker("sentence", nb_words=3, locale="es_ES")
#     stock = 1


# ============================================
# PASO 2: LoanFactory
# ============================================
# Descomenta las siguientes líneas:
# class LoanFactory(SQLAlchemyModelFactory):
#     class Meta:
#         model = Loan
#         sqlalchemy_session_persistence = "flush"
#
#     book = factory.SubFactory(BookFactory)
#     member_email = factory.Faker("email", locale="es_ES")
#     loaned_on = date(2026, 3, 2)
