from workshops import WorkshopRegistry


# Un registro nuevo por escenario: ningún escenario ve el estado de otro.
def before_scenario(context, scenario):
    context.registry = WorkshopRegistry()
    context.result = None
