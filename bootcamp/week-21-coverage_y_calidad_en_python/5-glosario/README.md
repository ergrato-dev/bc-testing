# Glosario Semana 21 - Coverage y Calidad en Python

## B

- **Branch coverage**: porcentaje de ramas de decisión (verdadero y falso de cada `if`) que ejecutaron los tests.
- **BrPart**: columna del reporte de coverage con el número de decisiones que tienen alguna rama sin recorrer.

## C

- **Complejidad ciclomática**: número de caminos independientes de una función; parte de 1 y suma uno por cada `if`, `elif`, `for`, `while` o `except`.
- **coverage combine**: comando que une los datos de varias ejecuciones de `coverage run` (con `parallel = true`) en un solo reporte.

## E

- **exclude_also**: opción de `[tool.coverage.report]` que excluye del cálculo las líneas que coinciden con una expresión regular.

## F

- **fail_under**: umbral de coverage; si no se alcanza, `pytest --cov` termina con código 1.

## M

- **Mutante**: copia del código con un cambio pequeño (`>=` por `>`, `2` por `3`) para comprobar si la suite lo detecta.
- **Mutante equivalente**: mutante que no cambia el comportamiento para ninguna entrada; ningún test puede matarlo.
- **Mutante muerto (killed)**: mutante que hace fallar al menos un test.
- **Mutante sobreviviente (survived)**: mutante con el que todos los tests pasan.
- **mutmut**: herramienta de mutation testing para Python; necesita `fork` (en Windows, WSL).

## P

- **pragma: no cover**: comentario que excluye una línea o un bloque del coverage.
- **pytest-cov**: plugin que integra `coverage.py` en pytest.

## Q

- **Quality gate**: conjunto de condiciones (coverage, duplicados, bugs) que el código debe cumplir en SonarQube para aprobar.

## R

- **relative_files**: opción de `[tool.coverage.run]` que escribe rutas relativas en `coverage.xml`, necesaria para SonarQube en CI.

## S

- **SonarQube**: plataforma de análisis de calidad; lee `coverage.xml` y aplica un quality gate.
