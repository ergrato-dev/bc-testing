from gateways import ExternalNotifier, ItemRepository, fetch_item_status


class ItemService:
    def __init__(self, repository: ItemRepository, notifier: ExternalNotifier):
        self.repository = repository
        self.notifier = notifier

    def activate(self, item_id: str) -> str:
        remote = fetch_item_status(item_id)
        if remote["blocked"]:
            raise PermissionError(f"item {item_id} is blocked")

        self.repository.save(item_id, "active")

        try:
            self.notifier.send(item_id, "active")
        except ConnectionError:
            return "active_pending_notification"

        return "active"
