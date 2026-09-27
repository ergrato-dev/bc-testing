# Ejercicio 01 - Primer Feature con Behave

## Objetivo

Automatizar un feature escrito en Gherkin (en español) con Behave: leer los pasos sin definir, escribir step definitions con parámetros, usar `context` para compartir estado entre pasos y dejar que un Scenario Outline encuentre un bug de frontera en el código.

## Tiempo estimado

90 minutos.

## Preparación

```bash
cd starter
uv sync
```

- `starter/booking.py`: calendario de salas de reuniones. Regla: se reserva por horas entre las 8 y las 18 (la sala cierra a las 18, así que la última reserva empieza a las 17).
- `starter/features/reserva_de_salas.feature`: el feature, con `Antecedentes`, dos escenarios y un `Esquema del escenario`.
- `starter/features/steps/reserva_steps.py`: las step definitions, comentadas por PASO.

## Paso a paso

### Paso 1: pasos sin definir

```bash
uv run behave
```

Behave lee el feature y no encuentra ninguna step definition:

```text
0 scenarios passed, 0 failed, 6 error, 0 skipped
0 steps passed, 0 failed, 0 skipped, 20 undefined
```

Además propone snippets:

```text
You can implement step definitions for undefined steps with these snippets:

@when(u'"Ana" reserva la sala "Azul" a las 9')
def step_impl(context):
    raise StepNotImplementedError(u'When "Ana" reserva la sala "Azul" a las 9')
```

El snippet copia los valores literales (`"Ana"`, `9`). Un step reutilizable los convierte en parámetros: `'"{person}" reserva la sala "{room}" a las {hour:d}'`. `{hour:d}` convierte el texto en `int`.

### Paso 2: Antecedentes (PASO 1)

Descomenta el PASO 1. `Antecedentes` se ejecuta antes de cada escenario; el step guarda un calendario nuevo en `context`. Ejecuta con un formato compacto:

```bash
uv run behave --format progress
```

```text
6 steps passed, 0 failed, 0 skipped, 14 undefined
```

### Paso 3: reservar una sala libre (PASO 2)

Descomenta el PASO 2. `context.calendar` viaja del `Dado` al `Cuando` y al `Entonces`. El primer escenario pasa.

### Paso 4: sala ocupada (PASO 3)

Descomenta el PASO 3. `intenta reservar` captura el `BookingError` en `context.error` para que el `Entonces` lo compruebe. Si un `Cuando` lanzara la excepción sin capturarla, el escenario fallaría antes de llegar al `Entonces`.

```text
2 scenarios passed, 0 failed, 4 error, 0 skipped
16 steps passed, 0 failed, 0 skipped, 4 undefined
```

### Paso 5: el Scenario Outline encuentra un bug (PASO 4)

Descomenta el PASO 4. El `Esquema del escenario` se ejecuta una vez por fila de `Ejemplos`. Ejecuta `uv run behave`:

```text
  Esquema del escenario: Solo se reserva en horario de apertura -- @1.4
    Dado un calendario de salas vacío
    Cuando "Ana" intenta reservar la sala "Verde" a las 18
    Entonces la reserva queda rechazada
      ASSERT FAILED: la reserva se aceptó

5 scenarios passed, 1 failed, 0 skipped
```

La fila de las 18 falla: el starter acepta reservas a la hora de cierre. Busca la condición en `booking.py` y corrígela: `OPENING_HOUR <= hour < CLOSING_HOUR`.

### Paso 6: todo en verde

```text
1 feature passed, 0 failed, 0 skipped
6 scenarios passed, 0 failed, 0 skipped
20 steps passed, 0 failed, 0 skipped
```

Compara con `solution/`.

## Opciones útiles

| Comando | Para qué |
|---|---|
| `uv run behave --format progress` | Una línea de puntos en lugar de cada paso |
| `uv run behave --dry-run` | Comprueba qué pasos están definidos sin ejecutarlos |
| `uv run behave features/reserva_de_salas.feature:10` | Solo el escenario que empieza en la línea 10 |
