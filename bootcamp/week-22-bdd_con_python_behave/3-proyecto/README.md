# Proyecto Semanal - Suite BDD del Flujo Principal del Dominio

> **🎯 ÚNICO ENTREGABLE**: Este proyecto es el **único entregable obligatorio** para aprobar la semana.

## Objetivo

Describir en Gherkin el flujo de negocio principal de tu dominio y automatizarlo con Behave: features legibles para negocio, steps reutilizables y reglas probadas en sus fronteras.

## Tu dominio asignado

**Dominio**: el que te asignó el instructor al inicio del trimestre.

## Punto de partida

```bash
cd starter
uv sync
uv run behave
```

```text
0 features passed, 0 failed, 1 skipped
0 scenarios passed, 0 failed, 0 skipped
```

El starter trae la estructura con TODOs: `domain.py`, `features/environment.py`, `features/steps/dominio_steps.py` y la plantilla `features/flujo_principal.feature`.

Orden de trabajo recomendado para cada escenario: escríbelo en Gherkin, ejecuta `uv run behave` y míralo fallar (`undefined` y después el assert), escribe los steps y, por último, el código de `domain.py`.

## Ejemplos de adaptación por dominio

- **Museo**: registrar una pieza, prestarla a otra sala, rechazar un préstamo de una pieza en restauración.
- **Planetario**: programar una función, vender entradas, rechazar la venta cuando se supera el aforo.
- **Acuario**: registrar un tanque, añadir especies, rechazar una especie cuando se supera la capacidad.

## Requisitos

1. Al menos 3 features (`.feature`) en `# language: es`, con al menos 2 escenarios cada una.
2. Al menos un `Esquema del escenario` con `Ejemplos` que pruebe las fronteras de una regla.
3. Al menos un `Antecedentes` con tabla de datos (`context.table`).
4. Estado nuevo por escenario en `before_scenario`.
5. Al menos un escenario con `@smoke`; `uv run behave --tags=smoke` debe ejecutar solo esos.
6. Escenarios declarativos (reglas de negocio, no clics ni campos) con un único `Cuando`.
7. Steps con parámetros y reutilizados entre escenarios; ningún snippet con valores literales.
8. `uv run behave` en verde, sin pasos `undefined`.

## Entregables

1. `starter/` con features, steps, `environment.py` y `domain.py` adaptados a tu dominio.
2. Salida de `uv run behave --format progress`.
3. Un párrafo en tu README: qué regla de negocio aclaró escribir los ejemplos y qué pregunta de "¿qué pasa si...?" surgió.

> `solution/` del proyecto no se publica en el repositorio.
