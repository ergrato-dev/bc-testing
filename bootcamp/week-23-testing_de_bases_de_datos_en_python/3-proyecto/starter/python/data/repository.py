from sqlalchemy.orm import Session

from data.models import Item


# TODO (paso 2): renombra ItemRepository según tu entidad y añade:
#   - add(): commit; ante IntegrityError hace rollback y lanza un error del dominio
#   - get_by_code(), list_by_status()
#   - la operación principal de tu dominio (prestar, vender, reservar...)
class ItemRepository:
    def __init__(self, session: Session) -> None:
        self.session = session

    def get(self, item_id: int) -> Item | None:
        return self.session.get(Item, item_id)
