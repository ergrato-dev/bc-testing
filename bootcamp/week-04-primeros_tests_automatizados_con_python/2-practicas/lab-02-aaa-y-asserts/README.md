# Lab 02 - AAA y asserts

## Objetivo

Completar la suite de un módulo de usuarios con tests AAA y elegir, para cada valor, la comparación que lo comprueba de verdad: `==`, `is None`, `is True`, `pytest.approx` y `pytest.raises`.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-estructura-test-pytest.md) y la [03](../../1-teoria/03-assertions-y-ejecucion.md).
- El proyecto `~/labs-semana-04` del lab 01.

## Punto de partida

```bash
cd ~/labs-semana-04
cp -r <ruta-del-bootcamp>/bootcamp/week-04-primeros_tests_automatizados_con_python/2-practicas/lab-02-aaa-y-asserts/starter/. .
uv run pytest tests/test_user_utils.py
```

```text
collected 0 items

============================ no tests ran in 0.01s =============================
```

El archivo no tiene ningún test activo todavía; `echo $?` daría 5.

## Contexto

`src/user_utils.py` es el `user-utils.js` del [lab 02 de la semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/2-practicas/lab-02-aaa-y-matchers/README.md) en Python: cinco funciones del registro de usuarios de una tienda. Léelo entero antes de empezar. Dos diferencias:

- `create_user` devuelve un **diccionario** (`dict`), el objeto de JavaScript: claves entre comillas, `{"name": "Ana"}`, y se lee con `user["name"]`.
- `find_user_by_email` devuelve `None` cuando no encuentra el email.

---

## Paso 1: is_adult (5 min)

Descomenta el bloque `PASO 1` de `tests/test_user_utils.py`: quita el `# ` de cada línea de los dos tests, sin tocar las líneas `# ====` ni `# PASO`. La línea `def` queda pegada al margen y el cuerpo con cuatro espacios. Ejecuta `uv run pytest tests/test_user_utils.py`: pasan 2 tests.

Los valores 18 y 17 son los límites de «mayor de edad», como en la semana 3.

## Paso 2: calculate_discount y pytest.raises (5 min)

Descomenta `PASO 2` y ejecuta. En el segundo test, la llamada va dentro de `with pytest.raises(...)`.

Saca la llamada del bloque: copia `calculate_discount(price, percent)` en una línea propia justo encima del `with`, con la misma sangría, y ejecuta:

```text
E           ValueError: Invalid percent
FAILED tests/test_user_utils.py::test_raises_value_error_when_percent_is_greater_than_100
```

El error saltó antes de llegar al `with`, así que nadie lo esperaba. Borra esa línea.

## Paso 3: is_valid_email (5 min)

Descomenta `PASO 3` y ejecuta. Llevas 6 tests en verde.

`is_valid_email` devuelve `True` o `False`, así que los tests usan `is True` e `is False`, no `assert result` a secas.

## Paso 4: create_user con == (10 min)

Escribe tú el test del `PASO 4`, con AAA y nombre `test_<resultado>_when_<condición>`:

- Arrange: `name` es `"  Ana Pérez "` (con espacios) y `email` es `"ANA@Example.com"`.
- Act: llama a `create_user(name, email)`.
- Assert: `==` con el diccionario que debe devolver, escrito a mano leyendo qué hace la función con cada campo.

Antes de dejarlo con `==`, prueba `is` con el mismo diccionario:

```text
E       AssertionError: assert {'name': 'Ana Pérez', 'email': 'ana@example.com', 'active': True} is {'name': 'Ana Pérez', 'email': 'ana@example.com', 'active': True}
```

Mismo contenido, distinto objeto: `is` pregunta si son el mismo objeto en memoria, como `toBe` con objetos en Jest. Vuelve a `==`.

## Paso 5: find_user_by_email con == e is None (15 min)

Dos tests en el `PASO 5`. En los dos, el Arrange crea una lista con `create_user`:

```python
users = [create_user("Ana", "ana@example.com"), create_user("Luis", "luis@example.com")]
```

- Busca `"luis@example.com"`: el esperado es el diccionario de Luis, con sus tres claves.
- Busca un email que no está: `assert result is None`.

## Paso 6: decimales con pytest.approx (10 min)

Empieza por el caso de la semana 3: precio 19.99 con un 15 %, y el esperado de entonces, `pytest.approx(16.99)`. Ejecuta:

```text
>       assert result == pytest.approx(16.99)
E       assert 16.9915 == 16.99 ± 1.7e-05
```

En Jest, `toBeCloseTo(16.99)` aceptaba 16,9915. `pytest.approx` solo tolera una millonésima: 16,9915 no es 16,99, y la función no redondea a céntimos. La pregunta para negocio de la semana 3 sigue abierta; aquí el test no la tapa.

Ahora escribe el test del `TODO`: precio 29.90 con un 10 %. A mano, 29,90 − 2,99 = 26,91. Escríbelo primero con `== 26.91`:

```text
>       assert result == 26.91
E       assert 26.909999999999997 == 26.91
```

El esperado es correcto; el binario no. Cámbialo a `== pytest.approx(26.91)` y ejecuta: pasa. Borra el test del 19,99.

## Paso 7: filtrar por nombre (5 min)

```bash
uv run pytest tests/test_user_utils.py -k email
```

```text
======================= 5 passed, 5 deselected in 0.01s ========================
```

Se ejecutan los 5 tests cuyo nombre contiene `email`. Prueba con otra palabra de tus nombres.

Borra las líneas `TODO` del archivo y guarda el lab en git:

```bash
git add .
git commit -m "lab 02: user_utils con AAA y asserts"
```

---

## Comprobación final

```bash
uv run pytest tests/test_user_utils.py 2>&1 | tail -1
# ============================== 10 passed in 0.02s ==============================
grep -c TODO tests/test_user_utils.py
# 0
grep -cE 'assert result$|assert not result' tests/test_user_utils.py
# 0
```

Repasa:

- [ ] Cada test tiene los tres comentarios AAA y un nombre `test_..._when_...`.
- [ ] `create_user` y la búsqueda con éxito comparan el diccionario completo con `==`.
- [ ] El email que no existe se comprueba con `is None` y el descuento de 29,90 con `pytest.approx`.

## Qué has aprendido

- `==` compara valores y contenidos; `is` compara identidad y solo sirve con `None`, `True` y `False`.
- `pytest.raises` solo captura lo que pasa dentro de su bloque.
- `pytest.approx` es mucho más estricto que `toBeCloseTo`: absorbe el error del binario, no un redondeo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Primer proyecto con pytest](../lab-01-primer-proyecto-pytest/README.md) | [Lab 03 - De casos de prueba a tests](../lab-03-de-casos-a-tests/README.md) → |

[↑ Semana 04](../../README.md)
