# Lab 04 - Exploratorio y regresión

## Objetivo

Hacer una sesión exploratoria real con un charter sobre un programa que ya tienes instalado, convertir dos hallazgos en casos guionizados y elegir qué casos van en smoke, sanity y regresión.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-cobertura-y-tipos.md), secciones 2 y 3.
- La carpeta `~/labs-semana-02` del [lab 01](../lab-01-analisis-de-casos/README.md).
- El comando `date` de GNU. Viene en Linux, WSL y Git Bash. En macOS, el `date` del sistema no tiene la opción `-d`: instala las herramientas de GNU con `brew install coreutils` y usa `gdate` en lugar de `date`.

## Punto de partida

```bash
cd ~/labs-semana-02
cp <ruta-del-bootcamp>/bootcamp/week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-04-exploratorio-y-regresion/starter/sesion.md .
date --version | head -1
# date (GNU coreutils) 8.32
```

La versión puede ser otra. Si sale un error de opción no válida, estás usando el `date` de macOS.

## Contexto

`date` muestra la fecha y la hora. Con `-d` interpreta una fecha escrita como texto y la muestra; con `+%F` la muestra como `año-mes-día`:

```bash
date -d 2028-02-29 +%F
# 2028-02-29
date -d "2026-12-31 +1 day" +%F
# 2027-01-01
```

Lo usan muchos scripts para calcular fechas: vencimientos, copias de seguridad, informes del mes anterior. Nadie te ha dado casos de prueba: vas a explorar.

---

## Paso 1: la sesión (parte A, 30 min)

Rellena el entorno de [`sesion.md`](starter/sesion.md) y pon un temporizador de 30 minutos. Sigue el charter: prueba fechas límite y apunta cada comando, su salida tal cual y si era lo que esperabas.

Algunas ideas para empezar. Decide tú el resto según lo que veas:

- Días que no existen: el 29 de febrero de un año no bisiesto, el 31 de un mes de 30 días, el mes 13.
- Sumas y restas de meses desde el 31 o el 30: `date -d "2026-01-31 +1 month" +%F`.
- La hora `24:00`.
- Una hora que no existe por el cambio de horario de verano. Para usar otra zona horaria sin cambiar la de tu equipo, pon `TZ=Europe/Madrid` delante del comando: `TZ=Europe/Madrid date -d "2026-03-29 02:30" +%F`. En España, ese día los relojes pasan de 02:00 a 03:00.
- Los años extremos: `0000-01-01`, `9999-12-31`.

Cuando algo te sorprenda, explora alrededor: si el 31 de enero más un mes da algo raro, prueba el 30, el 29 y el 28.

## Paso 2: los hallazgos (parte A, 10 min)

Elige tres notas que te hayan sorprendido. Antes de llamarlas bug, busca en el manual de GNU, en la página [Relative items in date strings](https://www.gnu.org/software/coreutils/manual/html_node/Relative-items-in-date-strings.html): algunas sorpresas son comportamiento documentado. Un comportamiento documentado no es un bug, pero sí un riesgo para quien lo usa en un script sin saberlo. Escríbelo así.

## Paso 3: dos casos guionizados (parte B, 10 min)

Convierte dos hallazgos en casos de la [teoría 01](../../1-teoria/01-casos-de-prueba.md). Las precondiciones importan aquí: la versión de `date` y la zona horaria cambian el resultado. El esperado es la salida exacta, copiada de tus notas o del manual.

## Paso 4: smoke, sanity y regresión (parte C, 10 min)

Con los once casos de la tienda y la situación descrita, rellena la tabla. Recuerda: smoke son los happy paths críticos; sanity, los de la zona que cambió; regresión, todo lo que puede romperse antes de la release.

---

## Comprobación final

```bash
grep -c TODO sesion.md
# 0
grep -c '^| [0-9]' sesion.md
# 12 o más
```

`grep -c '^| [0-9]'` cuenta las filas de notas que empiezan por una hora.

Repasa:

- [ ] Las salidas de las notas están copiadas de tu terminal, no escritas de memoria.
- [ ] Cada hallazgo dice si es un bug, un comportamiento documentado o una pregunta, y por qué.
- [ ] Los casos de la parte B tienen la zona horaria o la versión en las precondiciones.
- [ ] La sanity incluye TC-ENV-002 y la smoke no incluye casos de error.

## Qué has aprendido

- Una sesión exploratoria con charter y notas produce hallazgos que se pueden repetir.
- Una sorpresa no siempre es un bug: el manual decide si es un defecto o un comportamiento documentado.
- Smoke, sanity y regresión se eligen de la misma colección de casos, según el momento.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 03 - Tabla de decisión](../lab-03-tabla-de-decision/README.md) | [Proyecto — Proyecto - Suite de casos de prueba](../../3-proyecto/README.md) → |

[↑ Semana 02](../../README.md)
