# Glosario Semana 17 - Parametrización y Marks

## B

- **boundary case**: valor en el límite de un rango válido, usado como entrada parametrizada.

## E

- **edge case**: escenario poco común pero válido que debe cubrirse en la suite.

## I

- **id de caso**: etiqueta legible para identificar una fila de `parametrize` (con `ids=` o `pytest.param(..., id=...)`).

## K

- **`-k`**: filtro de pytest que selecciona tests por nombre o id con expresiones `and`/`or`/`not`.

## M

- **mark**: etiqueta de pytest para clasificar pruebas por objetivo o costo.
- **`-m`**: filtro de pytest que selecciona tests por sus marks con expresiones `and`/`or`/`not`.
- **modo estricto (`strict = true`)**: opción de pytest 9 que convierte en error marks no registrados, claves de configuración desconocidas, ids duplicados y `XPASS` de `xfail`.

## P

- **`pytest.param`**: fila de `parametrize` con `id` y `marks` propios.
- **parametrize**: mecanismo para ejecutar un mismo test con múltiples entradas.

## R

- **regression suite**: conjunto amplio de pruebas para detectar regresiones funcionales.

## S

- **skip / skipif**: marks integrados que saltan un test siempre o solo si se cumple una condición.
- **smoke suite**: conjunto mínimo de pruebas críticas para feedback rápido.

## X

- **xfail**: marca de pytest que indica que se espera que un test falle; con `strict=True`, si pasa (`XPASS`) se reporta como fallo.
