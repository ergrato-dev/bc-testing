# Glosario Semana 20 - TDD con Python

## C

- **Contraejemplo**: entrada concreta que hace fallar una propiedad; hypothesis la muestra en `Failing test case`.
- **Código legado**: código sin tests; antes de cambiarlo se caracteriza.

## D

- **dataclass (frozen)**: clase de datos con `__eq__` y `__repr__` generados; con `frozen=True` no se puede modificar (`FrozenInstanceError`).

## E

- **@example**: decorador de hypothesis que añade un caso fijo que se ejecuta siempre, además de los generados.
- **Estrategia**: generador de datos de hypothesis (`st.integers`, `st.lists`, `st.builds`...).

## F

- **Fake**: implementación simplificada pero funcional de una dependencia (por ejemplo, un repositorio en memoria).

## G

- **@given**: decorador de hypothesis que ejecuta el test con muchas entradas generadas por las estrategias indicadas.
- **Green**: fase del ciclo en la que se escribe el código mínimo para que el test pase.

## K

- **Kata**: ejercicio corto y repetible para practicar TDD (Roman Numerals, Bowling Game).

## M

- **mypy**: verificador estático de tipos para Python; con `strict = true` exige anotaciones completas.

## P

- **Propiedad**: regla que debe cumplirse para cualquier entrada válida (invariante, ida y vuelta, idempotencia, oráculo).
- **Protocol**: tipo de `typing` que describe métodos requeridos; una clase lo cumple sin heredar de él (tipado estructural).

## R

- **Red**: fase del ciclo en la que se escribe un test que falla por el motivo esperado.
- **Refactor**: fase del ciclo en la que se mejora el diseño con la suite en verde.

## S

- **Seam**: punto donde se puede cambiar el comportamiento de código legado sin editar su lógica, por ejemplo un parámetro opcional.
- **Shrinking**: proceso con el que hypothesis reduce un contraejemplo a uno más simple que sigue fallando.

## T

- **Test de caracterización**: test que registra lo que el código hace hoy, aunque sea incorrecto, para detectar cambios.
