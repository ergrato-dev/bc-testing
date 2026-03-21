class ExternalNotifier:
    def send(self, item_id: str, status: str) -> None:
        return None


class ItemService:
    def __init__(self, notifier: ExternalNotifier):
        self.notifier = notifier

    def activate(self, item_id: str, is_blocked: bool) -> str:
        if is_blocked:
            raise PermissionError("blocked item")

        self.notifier.send(item_id, "active")
        return "active"
