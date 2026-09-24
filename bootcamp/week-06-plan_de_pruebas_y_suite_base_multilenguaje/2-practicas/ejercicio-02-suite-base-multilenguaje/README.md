# Ejercicio 02 - Suite Base Multilenguaje

## Objetivo

Construir la misma intención de test en JavaScript, Python y Java.

## Tiempo estimado

50 minutos.

## Pasos

1. Revisa cada archivo en `starter/`.
2. Descomenta el bloque del PASO 1 en cada lenguaje.
3. Ejecuta las pruebas.
4. Descomenta PASO 2 y PASO 3.
5. Compara con `solution/` y valida equivalencia.

## Comandos sugeridos

### JavaScript (Jest)

```bash
pnpm install
pnpm test
```

### Python (pytest)

```bash
cd starter/python
uv sync
uv run pytest -q
```

Para ejecutar la solución de referencia: `cd solution/python && uv sync && uv run pytest -q`.

### Java (JUnit 5 + Maven)

```bash
mvn test
```
