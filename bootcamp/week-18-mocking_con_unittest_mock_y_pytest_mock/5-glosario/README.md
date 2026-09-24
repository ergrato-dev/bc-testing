# Glosario Semana 18 - Mocking en Python

## A

- **assert_not_called**: verificación que falla si el mock recibió alguna llamada.
- **autospec**: opción (`autospec=True`, `create_autospec`) que alinea un mock con la firma real del objeto: rechaza atributos inexistentes y llamadas con argumentos incorrectos.

## C

- **call_args**: argumentos de la última llamada a un mock (`.args`, `.kwargs`).
- **call_args_list**: lista ordenada de todas las llamadas a un mock, comparable con `call(...)`.
- **call_count**: número de veces que un mock fue invocado durante el test.

## M

- **mock**: doble de prueba que permite verificar interacciones y llamadas.
- **monkeypatch**: fixture de pytest que reemplaza atributos, variables de entorno o entradas de diccionarios y los restaura al terminar; no registra llamadas.

## P

- **patch**: técnica para reemplazar temporalmente un símbolo durante un test.

## S

- **side_effect**: comportamiento alternativo del mock (error, secuencia o función).
- **spy**: observador de llamadas sobre una implementación real.
- **stub**: doble que entrega respuestas predefinidas sin verificar interacciones.

## T

- **target (patch)**: ruta del nombre que se reemplaza; debe ser el módulo donde el símbolo se usa, no donde se define.
