# Lab 04 - Triage y ciclo de vida

## Objetivo

Hacer el *triage* de siete reportes nuevos: decidir cuáles se abren, cuáles son duplicados, cuáles no son bugs, cuáles no se van a corregir y cuáles no se pueden reproducir. Después, ordenar los abiertos y seguir uno de ellos por todo su ciclo de vida.

El **triage** es la revisión periódica de los reportes nuevos: el equipo decide qué hacer con cada uno antes de que nadie empiece a corregir.

## Tiempo estimado

70 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-ciclo-de-vida-del-bug.md), secciones 3 y 4.
- Los labs [02](../lab-02-analisis-de-reportes/README.md) y [03](../lab-03-del-mensaje-al-reporte/README.md) terminados.

## Punto de partida

```bash
cd ~/labs-semana-01
cp <ruta-del-bootcamp>/bootcamp/week-01-que_es_testing_calidad_de_software/2-practicas/lab-04-triage-y-ciclo-de-vida/starter/backlog.md .
```

[`backlog.md`](starter/backlog.md) tiene tres requisitos de la tienda, siete reportes y tres partes que rellenar.

---

## Paso 1: leer los requisitos primero (5 min)

Lee R-07, R-12 y R-19 antes que los reportes. En el *triage*, la pregunta «¿es un bug?» se contesta con los requisitos, no con lo que cada uno espera.

## Paso 2: decidir cada reporte (25 min)

Rellena la **parte A**. Para cada reporte, pregunta en este orden:

| Pregunta | Si la respuesta es sí |
|---|---|
| ¿Ya hay otro reporte del mismo fallo? | `Duplicate`. Di cuál es el original: el reporte más completo, aunque no sea el primero |
| ¿El sistema hace lo que dice un requisito? | `Not a Bug`. Cita el requisito |
| ¿Se puede reproducir con lo que dice el reporte? | Si no, `Cannot Reproduce`. Di qué datos faltan |
| ¿Es un fallo real que el equipo ha decidido no corregir? | `Won't Fix`. Cita el motivo |
| Ninguna de las anteriores | `Open`, con severidad y prioridad justificadas |

Sobre BUG-202: quien lo reportó espera el redondeo «de toda la vida» (el 5 sube). El equipo comprobó lo que hace el código con el redondeo de R-07, en Python:

```python
from decimal import Decimal, ROUND_HALF_EVEN
for x in ("2.345", "2.355"):
    print(x, "->", Decimal(x).quantize(Decimal("0.01"), rounding=ROUND_HALF_EVEN))
```

```text
2.345 -> 2.34
2.355 -> 2.36
```

No hace falta que entiendas el código: el redondeo al par lleva el 5 hacia la cifra par más cercana, a veces hacia abajo y a veces hacia arriba. Compara esa salida con R-07 y con el reporte.

## Paso 3: ordenar los abiertos (10 min)

Rellena la **parte B**. Ordena por prioridad, y dentro de la misma prioridad, por severidad. Si dos reportes quedan empatados, decide cuál afecta a más usuarios y escribe por qué en una línea.

Un criterio que se usa en casi todos los equipos: un fallo que cobra dinero de más va antes que uno que impide cobrar. El primero hay que devolverlo y explicarlo; el segundo solo retrasa la venta.

## Paso 4: la historia de BUG-207 (20 min)

Rellena la **parte C** con los estados de la [sección 4 de la teoría 03](../../1-teoria/03-ciclo-de-vida-del-bug.md#4-el-ciclo-de-vida). Fíjate en los hechos 5 y 6: la primera prueba pasó y la segunda no. Contesta la pregunta final: ¿qué condición del reporte original no repitió la primera prueba?

## Paso 5: repasar con la teoría (10 min)

Vuelve a la tabla de caminos alternativos de la teoría 03 y comprueba que cada estado de la parte A cumple su «Cuándo». Corrige lo que no.

---

## Comprobación final

```bash
grep -c TODO backlog.md
# 0
```

Repasa:

- [ ] Cada `Duplicate` dice cuál es el original.
- [ ] Cada `Not a Bug` y cada `Won't Fix` citan un requisito o un motivo.
- [ ] Cada `Cannot Reproduce` dice qué datos faltan.
- [ ] En la parte C, el estado después del hecho 6 no es `Verified`.

## Qué has aprendido

- El triage decide el destino de cada reporte antes de corregir nada, y lo decide con los requisitos.
- Un duplicado se cierra enlazando el original, y el original es el reporte más útil.
- `Not a Bug` no quiere decir que quien reportó se equivocara al reportar: quiere decir que el requisito dice otra cosa.
- El retest repite las condiciones del reporte original; si no las repite, la corrección no está verificada.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Del mensaje al reporte](../lab-03-del-mensaje-al-reporte/README.md) | [Proyecto — Proyecto - Reporte de bug profesional](../../3-proyecto/README.md) → |

[↑ Semana 01](../../README.md)
