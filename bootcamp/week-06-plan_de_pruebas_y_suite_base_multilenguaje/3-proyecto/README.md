# Proyecto Semana 06 - Integrador de Fundamentos (Etapa 0)

> Duración estimada: 5 h

## Objetivo

Documentar un plan de pruebas para tu dominio asignado y construir, a partir de él, una suite base equivalente en **JavaScript, Python y Java**, con trazabilidad desde requerimientos hasta tests.

## Entregables obligatorios

1. `test-plan.md` con estrategia, alcance, riesgos, matriz de trazabilidad y cobertura planificada.
2. Suite de pruebas en los **tres lenguajes** sobre las mismas reglas del dominio:
   - `javascript/item.service.test.js` (Jest)
   - `python/test_item_service.py` (pytest)
   - `java/src/test/java/com/bootcamp/ItemServiceTest.java` (JUnit 5)

## Reglas del proyecto

1. Usa tu dominio asignado por el instructor (renombra `item` a la entidad de tu dominio si lo prefieres).
2. No copies ejemplos de otros aprendices.
3. Mantén nomenclatura técnica en inglés.
4. Explica decisiones en español dentro de `test-plan.md`.

## Requisitos mínimos

- `test-plan.md` completo, sin `TODO` pendientes.
- Mínimo **6 casos de prueba (TC)** en la matriz, cubriendo happy path, validaciones y al menos un caso límite.
- Cada TC implementado en **los tres lenguajes** con el mismo nombre de comportamiento (`should ... when ...`) y patrón AAA.
- Suites independientes de servicios externos reales.
- Evidencia de ejecución de los tres lenguajes (resumen de salida en la sección 8 del plan).

## Estructura del starter

```text
starter/
├── test-plan.md                                  # plantilla con TODOs
├── javascript/
│   ├── package.json
│   └── item.service.test.js                      # test.todo por cada TC
├── python/
│   ├── pyproject.toml
│   └── test_item_service.py                      # pytest.skip por cada TC
└── java/
    ├── pom.xml
    └── src/test/java/com/bootcamp/ItemServiceTest.java
```

Cada lenguaje necesita además el módulo bajo prueba (`item.service.js`, `item_service.py`, `src/main/java/com/bootcamp/ItemService.java`), que escribes tú a partir de tu dominio.

## Pasos sugeridos

1. Completa las secciones 1-3 de `test-plan.md` (alcance, enfoque, riesgos).
2. Define los TC en la matriz de trazabilidad (sección 4).
3. Implementa los TC en JavaScript y ejecuta:

   ```bash
   cd starter/javascript
   pnpm install
   pnpm test
   ```

4. Replica los mismos TC en Python y Java (`mvn test`). Para Python:

   ```bash
   cd starter/python
   uv sync
   uv run pytest -q
   ```

5. Marca en la matriz las columnas JS/Python/Java y pega las evidencias en la sección 8.

> La carpeta `solution/` del proyecto se mantiene fuera de versionado por política del bootcamp.
