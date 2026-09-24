# Glosario — Semana 04

> Primeros Tests Automatizados con Python (pytest)

---

## A

**assert**
Sentencia nativa de Python usada para verificar expectativas en tests.

**AAA (Arrange-Act-Assert)**
Patrón estructural para escribir tests claros y mantenibles.

---

## C

**Conftest**
Archivo especial de pytest para compartir fixtures y configuración (se profundiza en semanas futuras).

---

## E

**Error (ERROR)**
Estado de pytest cuando falla algo fuera del cuerpo del test: la colección del archivo (import roto, error de sintaxis) o el setup/teardown de un fixture. El test no llega a ejecutarse.

**Edge Case**
Caso extremo cercano a los límites, donde suelen aparecer muchos defectos.

---

## F

**Failure (FAILED)**
Estado de pytest cuando el test se ejecuta y lanza cualquier excepción en su cuerpo: una assertion no satisfecha o una excepción inesperada (`TypeError`, `KeyError`…).

**Fixture**
Mecanismo de pytest para preparar y reutilizar contexto de prueba.

---

## K

**-k (pytest -k)**
Filtro por nombre de test para ejecutar subconjuntos rápidamente.

---

## P

**Pass**
Estado de test que cumple todas las verificaciones.

**pytest**
Framework de testing en Python usado esta semana.

**pyproject.toml**
Archivo de configuración del proyecto: dependencias y opciones de pytest en la tabla `[tool.pytest]`.

---

## R

**Red-Green-Refactor**
Ciclo de desarrollo guiado por tests: test falla, solución mínima, mejora.

---

## S

**Setup**
Preparación previa a ejecutar tests (entorno, datos, dependencias).

**Snake Case**
Convención de nombres en Python con palabras separadas por guion bajo.

---

## T

**test_*.py**
Patrón de nombre para que pytest detecte archivos de test.

**test function**
Función con prefijo `test_` que representa un caso de prueba individual.

---

## U

**unittest**
Framework de testing incluido en la biblioteca estándar de Python, basado en clases `TestCase`; pytest puede ejecutar sus tests.

**uv**
Gestor de proyectos Python: instala Python, crea el entorno virtual y las dependencias (`uv sync`) y ejecuta comandos dentro de él (`uv run pytest`).

---

## V

**venv**
Entorno virtual aislado para dependencias Python por proyecto; `uv` lo crea automáticamente en `.venv`.
