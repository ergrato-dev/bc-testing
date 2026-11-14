# Proyecto Integrador - Suite Python completa del dominio

> **🎯 ÚNICO ENTREGABLE**: este proyecto es el único entregable obligatorio de la semana.

## Objetivo

Cerrar la etapa de Python con una sola suite en `python/` de tu repositorio:

- tests unitarios, de API y de repositorio, cada capa en su carpeta y con su marca;
- escenarios de Behave;
- cobertura combinada de pytest y Behave de al menos el 85 %;
- un reporte `testing-report-python.md` con datos reales.

## Dónde va

En tu **[proyecto hilo conductor](../../../docs/proyecto-hilo-conductor.md)**, el repositorio `qa-<nombre>-<apellido>`, dentro de `python/`. Partes de lo que entregaste en la semana 23 y de las entregas de las semanas 16 a 22 que copiaste en `python/`.

## Tiempo

| Parte | Tiempo |
|---|---|
| Desarrollo: pasos 1 a 5 | 6.5 h |
| Presentación: paso 6 | 0.5 h |

## Paso a paso

### Paso 1: inventario y rama

1. Crea la rama de la semana:

   ```bash
   cd ~/qa-ana-perez
   git switch main && git pull
   git switch -c week-24
   ```

2. Copia el starter sin sobrescribir nada:

   ```bash
   cp -rn <ruta-al-bootcamp>/bootcamp/week-24-proyecto_integrador_python/3-proyecto/starter/. .
   ```

   Trae `python/tests/conftest.py` y `python/testing-report-python.md`, los dos con `TODO`.

3. Haz el inventario de lo que tienes en `python/`. Para cada archivo de código y de test, anota en qué capa va:

   | Capa | Qué va | Carpeta |
   |---|---|---|
   | Unitaria | Reglas del dominio, servicios con mocks (S16–S18, S20) | `tests/unit/` |
   | API | El cliente `httpx2` con `MockTransport`, síncrono o async (S19) | `tests/api/` |
   | Repositorio | La capa de datos de la semana 23 | `tests/repository/` |
   | BDD | Los escenarios de la semana 22 | `features/` |

   Si dos semanas tienen módulos con el mismo papel (por ejemplo, dos `item_service.py`), quédate con uno y junta sus tests.

### Paso 2: una suite por capas

Referencia: [lab 01](../2-practicas/lab-01-suite-por-capas/README.md).

1. Mueve el código del dominio a paquetes con nombre de tu dominio. Por ejemplo, en un museo: `domain/` (reglas), `catalog/` (cliente HTTP) y `data/` (la capa de datos de la semana 23, que ya existe).
2. Mueve cada test a su carpeta y corrige los `import`.
3. Junta todas las dependencias en el único `python/pyproject.toml`:

   ```bash
   cd python
   uv add httpx2==2.13.1
   uv add --dev behave==1.3.3 coverage==7.17.3 pytest-asyncio==1.4.0
   ```

   Añade también las que usaste en las semanas 16 a 22 y no estén, con la versión de [stack-versions](../../../docs/stack-versions.md).

4. Añade a `[tool.pytest.ini_options]` las marcas y la opción `asyncio_mode` de la semana 19:

   ```toml
   [tool.pytest.ini_options]
   testpaths = ["tests"]
   pythonpath = ["."]
   asyncio_mode = "strict"
   addopts = "--strict-markers"
   markers = [
       "unit: lógica pura, sin red ni base de datos",
       "api: cliente HTTP con transporte simulado",
       "repository: repositorio contra PostgreSQL (necesita Docker)",
   ]
   ```

5. Completa el `TODO` de `tests/conftest.py` con el *hook* del lab 01.
6. Comprueba que cada capa tiene tests:

   ```bash
   uv run pytest --collect-only -q -m unit | tail -1
   # 4/20 tests collected (16 deselected) in 0.34s
   ```

   Repite con `-m api` y `-m repository`. Ninguna capa puede quedar en `0/`.

### Paso 3: la suite completa en verde

Con Docker en marcha:

```bash
uv run pytest -q
# ....................                                                     [100%]
# 20 passed in 4.22s
uv run behave
# ...
# 1 feature passed, 0 failed, 0 skipped
```

Tus cifras serán distintas. Si los escenarios de Behave importaban un módulo que has movido, corrige el `import` en `features/steps/`.

### Paso 4: cobertura combinada de al menos el 85 %

Referencia: [lab 02](../2-practicas/lab-02-coverage-combinado-y-reporte/README.md).

1. Añade la configuración de coverage a `pyproject.toml`, con tus paquetes en `source`:

   ```toml
   [tool.coverage.run]
   source = ["catalog", "data", "domain"]
   branch = true

   [tool.coverage.report]
   show_missing = true
   fail_under = 85
   ```

   No incluyas `tests/` ni `migrations/`.

2. Mide:

   ```bash
   uv run coverage run -m pytest -q
   uv run coverage run --append -m behave
   uv run coverage report
   ```

3. Si no llegas al 85 %, la columna `Missing` dice qué líneas faltan. Escribe el test que las cubre en la capa que corresponda. No bajes `fail_under`: la verificación usa `--fail-under=85` igualmente.
4. Añade `.coverage` a `.gitignore` si no está.

### Paso 5: el reporte de testing

Rellena `python/testing-report-python.md` con los datos de tu suite, como en el lab 02:

1. **Suite por capas:** tests y tiempo de cada capa (`uv run pytest -q -m <capa>`, `uv run behave`).
2. **Cobertura:** la salida de `uv run coverage report --format=markdown` y qué capa cubre qué.
3. **Tests más lentos:** `uv run pytest -q --durations=3`, explicado.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba y que podrían fallar en producción.
5. **Próximos pasos:** los tests que añadirías primero, en orden.

No dejes ningún `TODO`: la verificación los busca.

### Paso 6: verificar, entregar y presentar

1. Ejecuta la verificación desde la carpeta del bootcamp, con Docker en marcha:

   ```bash
   PROYECTO=~/qa-ana-perez ./scripts/verificar-semana.sh 24
   ```

2. Haz commit y push de la rama y abre un pull request `week-24` → `main`.
3. Presenta tu suite en 5 minutos: la estructura por capas, la cifra de cobertura, el test más lento y el riesgo más importante del reporte.

## Ejemplos de adaptación por dominio

| Dominio | Unitaria | API | Repositorio | BDD |
|---|---|---|---|---|
| Museo | Seguro de un préstamo | Artista de una pieza en el catálogo | Piezas y préstamos | Cálculo del seguro |
| Planetario | Precio de una entrada con descuentos | Horarios de un proveedor externo | Funciones y entradas | Venta de entradas |
| Acuario | Capacidad de un tanque | Datos de una especie | Tanques y ejemplares | Alta de un ejemplar |

## ✅ Requisitos verificables

`./scripts/verificar-semana.sh 24` comprueba lo siguiente ([checks.json](../checks.json)):

| Comprobación | Cómo se verifica |
|---|---|
| La suite pytest completa pasa, incluida la capa de repositorio | `uv run pytest -q` termina con 0 |
| Cada capa (unit, api, repository) tiene tests marcados | `--collect-only -q -m <capa>` recoge al menos un test de cada capa |
| Los escenarios de Behave pasan | `uv run behave` termina con 0 |
| La cobertura combinada de pytest y Behave llega al 85 % | `coverage run -m pytest`, `coverage run --append -m behave` y `coverage report --fail-under=85` terminan con 0 |
| `testing-report-python.md` tiene sus cinco secciones y ningún `TODO` | Las cinco cabeceras `##` del starter están y no queda ningún `TODO` |

> La `solution/` del proyecto no se publica en el repositorio del bootcamp.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Coverage combinado y reporte](../2-practicas/lab-02-coverage-combinado-y-reporte/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 24](../README.md)
