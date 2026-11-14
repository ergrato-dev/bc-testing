from data.models import Item
from data.repository import ItemRepository


def test_get_returns_the_stored_item(session):
    item = Item()
    session.add(item)
    session.flush()

    assert ItemRepository(session).get(item.id) is item
