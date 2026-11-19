# Lab 02 - Análisis de reportes

## Objetivo

Revisar cinco reportes de bug como lo haría un QA lead antes de pasarlos al equipo de desarrollo: encontrar lo que falta o está mal, corregir títulos y pasos, y reasignar severidad y prioridad con una justificación.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-ciclo-de-vida-del-bug.md), secciones 2 y 3.
- La carpeta `~/labs-semana-01` del [lab 01](../lab-01-calidad-y-niveles/README.md).

## Punto de partida

```bash
cd ~/labs-semana-01
cp <ruta-del-bootcamp>/bootcamp/week-01-que_es_testing_calidad_de_software/2-practicas/lab-02-analisis-de-reportes/starter/bugs-para-analizar.md .
```

[`bugs-para-analizar.md`](starter/bugs-para-analizar.md) tiene cinco reportes de la misma tienda online, cada uno con una sección **Mi análisis** para rellenar. Cada reporte tiene un problema distinto, y uno de ellos está bien.

## Contexto

Eres QA lead. Cinco personas han reportado bugs esta semana. Ningún reporte llega a desarrollo sin pasar por ti: si un reporte no se puede reproducir o está mal clasificado, alguien perderá horas con él.

---

## Paso 1: leer antes de tocar (10 min)

Lee los cinco reportes enteros, sin escribir nada. Para cada uno, intenta contestar: ¿qué ha visto quien lo reportó? Si no puedes contestarlo, ese es el primer problema del reporte.

## Paso 2: la lista de comprobación (15 min)

Pasa cada reporte por estas preguntas y apunta en **Problemas encontrados** cada «no»:

| Pregunta | Dónde mirarlo |
|---|---|
| ¿El título dice qué está mal y dónde, con el formato `[Componente] Qué está mal`? | Teoría 03, «El título» |
| ¿Alguien que no vio el fallo puede seguir los pasos y verlo? | Pasos para reproducir |
| ¿El obtenido describe sin diagnosticar? ¿El esperado es concreto? | Resultados |
| ¿El entorno dice sistema operativo, navegador o app y versión? | Entorno |
| ¿La severidad corresponde al daño, según la tabla de la teoría? | Severidad |
| ¿La prioridad corresponde a la urgencia para el negocio? | Prioridad |

## Paso 3: corregir (25 min)

Rellena el resto de **Mi análisis** de cada reporte. Reglas:

- **No inventes datos.** Si el reporte no dice el navegador, no escribas «Chrome 131»: escribe `[Preguntar: navegador y versión]`. Inventar un dato hace el reporte más difícil de reproducir, no más fácil.
- **Cada severidad y prioridad lleva su justificación**, con el impacto concreto: a quién afecta, qué pierde, si hay forma de evitarlo.
- **Si el reporte está bien, dilo y di por qué.** Reconocer un buen reporte también es parte del trabajo.

## Paso 4: severidad y prioridad, otra vez (10 min)

Vuelve a los reportes 2 y 3. Tienen las clasificaciones más discutibles del archivo. Escribe en un párrafo, al final del archivo, bajo `## Conclusión`, qué te ha hecho cambiarlas (o mantenerlas), usando las palabras «daño» y «urgencia».

---

## Comprobación final

```bash
grep -c '(escribe aquí)' bugs-para-analizar.md
# 0
grep -c '^## Conclusión' bugs-para-analizar.md
# 1
```

No hay una única clasificación correcta para cada reporte. Repasa:

- [ ] Ningún título corregido tiene palabras como «a veces», «mal» o «no funciona» sin decir qué.
- [ ] Cada paso corregido empieza con un verbo de acción y se puede seguir sin preguntar.
- [ ] Ningún dato del entorno es inventado.
- [ ] El reporte que estaba bien tiene la explicación de por qué lo está.

## Qué has aprendido

- Un reporte vago cuesta más que no reportar: alguien tiene que volver a encontrar el fallo.
- Severidad y prioridad se justifican con el impacto concreto, no con la impresión.
- Cuando falta un dato, se pregunta; no se inventa.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Calidad y niveles](../lab-01-calidad-y-niveles/README.md) | [Lab 03 - Del mensaje al reporte](../lab-03-del-mensaje-al-reporte/README.md) → |

[↑ Semana 01](../../README.md)
