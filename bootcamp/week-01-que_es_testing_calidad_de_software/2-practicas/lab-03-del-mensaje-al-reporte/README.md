# Lab 03 - Del mensaje al reporte

## Objetivo

Convertir el mensaje informal de un usuario en un reporte de bug completo: separar lo que se sabe de lo que hay que preguntar, deducir los pasos y escribir la respuesta al usuario con esas preguntas.

## Tiempo estimado

50 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-ciclo-de-vida-del-bug.md), secciones 2 y 3.
- El [lab 02](../lab-02-analisis-de-reportes/README.md) terminado.

## Punto de partida

```bash
cd ~/labs-semana-01
cp <ruta-del-bootcamp>/bootcamp/week-01-que_es_testing_calidad_de_software/2-practicas/lab-03-del-mensaje-al-reporte/starter/reporte-incompleto.md .
```

## Contexto

Un usuario escribe en el canal de soporte de la tienda:

> «Oye, encontré algo raro en la app. Cuando subo una foto de perfil grande, como de 5 MB, la página se queda cargando para siempre. Probé varias veces y pasa siempre. Al final tuve que refrescar la página y la foto no quedó guardada. Esto pasó hoy por la mañana en mi computadora con Chrome.»

Los usuarios no escriben reportes de bug: cuentan lo que les pasó. Convertirlo en algo reproducible es tu trabajo.

---

## Paso 1: separar lo que se sabe (10 min)

Antes de abrir la plantilla, copia esta tabla al final de `reporte-incompleto.md` y rellénala con frases del mensaje:

```markdown
## Lo que dice el mensaje

| Dato | Lo sabemos | Cita del mensaje o qué falta |
|---|---|---|
| Componente | | |
| Acción que dispara el fallo | | |
| Tamaño del archivo | | |
| Frecuencia | | |
| Resultado obtenido | | |
| Navegador | | |
| Versión del navegador | | |
| Sistema operativo | | |
| Formato de la imagen | | |
| Mensaje de error | | |
```

En la columna **Lo sabemos** escribe `Sí`, `En parte` o `No`.

## Paso 2: rellenar el reporte (20 min)

Completa cada campo de [`reporte-incompleto.md`](starter/reporte-incompleto.md):

- Lo que la tabla del paso 1 marca con `Sí` va tal cual.
- Lo que marca con `No` o `En parte` va como `[Preguntar: ...]`, con la pregunta concreta. No `[Preguntar: entorno]`, sino `[Preguntar: versión de Chrome (menú ⋮ > Ayuda > Información de Google Chrome)]`.
- Los pasos los deduces tú: el usuario no dice cómo llegó a la foto de perfil. Escríbelos desde un punto de partida claro («Con la sesión iniciada...») y marca el paso que has supuesto con `(supuesto)`.
- El esperado tiene dos opciones razonables: que la foto se suba, o que la app rechace el archivo con un mensaje que diga el tamaño máximo. Elige una y justifícala en una línea. Las dos son mejores que «cargando para siempre».

## Paso 3: la respuesta al usuario (10 min)

Al final del archivo, bajo `## Respuesta al usuario`, escribe el mensaje que le mandarías por el canal de soporte. Como máximo cinco líneas: agradece el aviso, haz las preguntas de los `[Preguntar: ...]` (las tres más importantes, no todas) y dile qué hacer mientras tanto si existe una forma de evitarlo.

## Paso 4: revisar la clasificación (10 min)

Justifica la severidad y la prioridad. Pregunta guía para la severidad: ¿se pierden datos? Para la prioridad: ¿cuántos usuarios suben fotos de más de 5 MB? Una foto de móvil suele pasar de 3 MB.

---

## Comprobación final

```bash
grep -c '(completar' reporte-incompleto.md
# 0
grep -c '^## Respuesta al usuario' reporte-incompleto.md
# 1
```

Repasa:

- [ ] El título sigue el formato `[Componente] Qué está mal`.
- [ ] Cada `[Preguntar: ...]` es una pregunta que el usuario puede contestar.
- [ ] El paso supuesto está marcado.
- [ ] El obtenido y el esperado son distintos y concretos.

## Qué has aprendido

- El mensaje de un usuario tiene datos ciertos, datos a medias y huecos; el reporte los separa.
- Lo que no sabes se pregunta de forma concreta, y lo que supones se marca.
- Responder al usuario forma parte del ciclo: es quien puede completar el reporte.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Análisis de reportes](../lab-02-analisis-de-reportes/README.md) | [Lab 04 - Triage y ciclo de vida](../lab-04-triage-y-ciclo-de-vida/README.md) → |

[↑ Semana 01](../../README.md)
