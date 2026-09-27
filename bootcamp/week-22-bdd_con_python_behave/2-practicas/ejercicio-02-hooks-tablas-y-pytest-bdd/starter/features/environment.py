from workshops import WorkshopRegistry


# Un registro por feature: se crea una sola vez y lo comparten todos sus escenarios.
def before_feature(context, feature):
    context.registry = WorkshopRegistry()
    context.result = None
