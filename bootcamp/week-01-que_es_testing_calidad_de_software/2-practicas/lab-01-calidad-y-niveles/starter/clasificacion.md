# Clasificación - Lab 01

Sustituye cada `TODO` por tu respuesta. Cada respuesta lleva su porqué en una frase.

## Parte A - Dimensiones de calidad

| # | Fallo | Dimensión | Por qué |
|---|---|---|---|
| A1 | La búsqueda tarda 9 segundos cuando el catálogo pasa de 10 000 productos | TODO | TODO |
| A2 | El total del carrito suma dos veces los gastos de envío | TODO | TODO |
| A3 | La URL `/pedidos/1042` muestra el pedido de otro cliente si cambias el número | TODO | TODO |
| A4 | El botón «Pagar» es gris claro sobre blanco y muchos usuarios no lo encuentran | TODO | TODO |
| A5 | La app se cierra una de cada veinte veces al volver del segundo plano | TODO | TODO |
| A6 | Añadir un método de pago obliga a tocar 14 archivos y siempre rompe algo en envíos | TODO | TODO |

## Parte B - Nivel de cada test

| # | Test | Nivel | Qué usa |
|---|---|---|---|
| B1 | Llama a `formatPrice(1999)` y comprueba que devuelve `"19,99 €"` | TODO | TODO |
| B2 | Abre Chrome, añade un producto al carrito, paga con una tarjeta de pruebas y comprueba que aparece «Pedido confirmado» | TODO | TODO |
| B3 | Guarda un pedido en PostgreSQL con el repositorio y lo lee de vuelta | TODO | TODO |
| B4 | Envía `POST /orders` a la API arrancada en memoria y comprueba que responde `201` con el JSON del pedido | TODO | TODO |
| B5 | Comprueba que `isValidEmail("ana@")` devuelve `false` | TODO | TODO |
| B6 | Escenario escrito por el equipo de ventas: «Dado un cliente con 3 pedidos este mes, cuando hace el cuarto, entonces recibe envío gratis», ejecutado contra la API | TODO | TODO |
| B7 | Lee un CSV de ejemplo del disco con el importador y comprueba que crea 50 productos | TODO | TODO |
| B8 | Llama a `applyCoupon(cart, "SAVE10")` con un carrito creado en el propio test | TODO | TODO |

## Parte C - ¿Pirámide o cono?

| Proyecto | Unitarios | Integración | E2E | Tiempo de la suite en la CI |
|---|---|---|---|---|
| Tienda | 420 | 60 | 15 | 4 min |
| Reservas | 35 | 20 | 240 | 52 min |

1. Forma de cada proyecto y por qué: TODO
2. Dos síntomas que esperarías en el equipo de Reservas: TODO
3. Un test E2E de Reservas que bajarías de nivel, a cuál y qué comprobaría: TODO

## Parte D - ¿Manual o automatizado?

| # | Situación | Manual o automatizado | Por qué |
|---|---|---|---|
| D1 | Comprobar en cada *pull request* que el cálculo de impuestos sigue dando lo mismo | TODO | TODO |
| D2 | Ver si el nuevo diseño del formulario de registro se entiende a la primera | TODO | TODO |
| D3 | Explorar una funcionalidad que llegó ayer y aún no tiene requisitos escritos | TODO | TODO |
| D4 | Repetir los 40 casos del plan de pruebas antes de cada versión mensual | TODO | TODO |
