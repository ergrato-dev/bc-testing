# Reporte de Calidad - Suite Python

> Dominio: TODO

## 1. Coverage

| Métrica | Antes | Después |
|---|---|---|
| Line coverage | TODO | TODO |
| Branch coverage | TODO | TODO |

Comando: `uv run pytest --cov`

TODO: qué ramas faltaban y qué tests las cubren ahora.

## 2. Mutation testing

| Resultado | Antes | Después |
|---|---|---|
| Mutantes totales | TODO | TODO |
| Muertos (🎉) | TODO | TODO |
| Sobrevivientes (🙁) | TODO | TODO |

Clasificación de los sobrevivientes finales:

| Mutante | Cambio | Clasificación (equivalente / aceptado) | Justificación |
|---|---|---|---|
| TODO | TODO | TODO | TODO |

## 3. Complejidad

TODO: salida de `uv run ruff check src` antes y después, y cómo refactorizaste `shipping_class`.

## 4. Umbrales acordados

| Métrica | Umbral | Herramienta | ¿Se cumple? |
|---|---|---|---|
| Branch coverage | 90% | `fail_under` | TODO |
| Complejidad ciclomática | 5 | `ruff` C901 | TODO |
| Mutantes matables vivos | 0 | `mutmut` | TODO |

## 5. Conclusión

TODO: qué te dijo cada herramienta que las otras no.
