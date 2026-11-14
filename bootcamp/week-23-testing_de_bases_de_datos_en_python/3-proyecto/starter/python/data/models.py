from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column


class Base(DeclarativeBase):
    pass


# TODO (paso 2): renombra Item por la entidad principal de tu dominio.
# TODO (paso 2): añade un código único (String(N), unique=True), un nombre
#                (String(120)) y un estado limitado con CheckConstraint.
# TODO (paso 2): añade una segunda entidad relacionada con ForeignKey
#                (el préstamo, la venta, la reserva...).
class Item(Base):
    __tablename__ = "items"

    id: Mapped[int] = mapped_column(primary_key=True)
