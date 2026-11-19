# Lab 03 - Tabla de decisión

## Objetivo

Convertir cuatro reglas de negocio en una tabla de decisión, encontrar las combinaciones que las reglas no deciden, simplificar la tabla y sacar de ella los casos de prueba.

## Tiempo estimado

50 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-tecnicas-de-diseno.md), sección 4.
- La carpeta `~/labs-semana-02` del [lab 01](../lab-01-analisis-de-casos/README.md).

## Punto de partida

```bash
cd ~/labs-semana-02
cp <ruta-del-bootcamp>/bootcamp/week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-03-tabla-de-decision/starter/tabla.md .
```

## Contexto

La tienda online cambia sus gastos de envío. El equipo de negocio ha escrito cuatro reglas al principio de [`tabla.md`](starter/tabla.md) y desarrollo empieza a programarlas la semana que viene. Te piden los casos de prueba. Al leer las reglas parecen claras; la tabla dirá si lo son.

---

## Paso 1: condiciones y acción (parte A, 5 min)

Busca en las reglas lo que el sistema tiene que saber para decidir el importe del envío. Son tres preguntas de sí o no. «30 € o más» es una de ellas, aunque el importe sea un número: para estas reglas solo importa si llega o no a 30 €.

## Paso 2: la tabla completa (parte B, 15 min)

Escribe las condiciones en la primera columna, en el orden de la plantilla: la fila de arriba cambia cada cuatro columnas, la del medio cada dos y la de abajo en cada una. Así salen las 8 combinaciones sin repetir ninguna.

Para cada columna, lee las cuatro reglas y apunta cuáles se aplican. Si solo una decide el importe, escríbelo. Si dos dan importes distintos, o ninguna lo decide, escribe `?`.

## Paso 3: preguntas para negocio (parte C, 10 min)

Cada `?` es una pregunta que hay que hacer **antes** de que se programe. Escríbela de forma que se pueda contestar con una de dos opciones, por ejemplo: «¿Las reglas 1 y 3 se aplican a la vez a ...? ¿El envío cuesta 0 € o 9,90 €?».

Si la tabla no tuviera `?`, desarrollo elegiría una respuesta al programar y nadie sabría cuál hasta que un cliente reclamase.

## Paso 4: simplificar (parte D, 10 min)

Elige una respuesta para cada pregunta y escríbela en **Respuestas supuestas**. Con ella, junta las columnas que dan el mismo importe y solo se diferencian en una condición, como en la sección «Simplificar» de la teoría 02.

Según lo que elijas, la tabla queda con 5 o 6 columnas.

## Paso 5: los casos (parte E, 10 min)

Una fila por columna de la parte D. Donde la tabla dice «30 € o más», usa 30,00 €; donde dice «menos de 30 €», usa 29,99 €. Así cada caso prueba también el valor límite del importe.

Responde la pregunta del final: cada condición nueva de sí o no multiplica las reglas por 2.

---

## Comprobación final

```bash
grep -c TODO tabla.md
# 0
grep -c '^| TC-ENV-' tabla.md
# 5 o 6
```

Repasa:

- [ ] La tabla completa tiene 8 columnas con importe o `?`, y cada columna dice qué reglas aplican.
- [ ] Cada `?` tiene su pregunta en la parte C, con las dos respuestas posibles.
- [ ] Cada columna de la tabla simplificada que tiene un `-` junta dos o más columnas de la completa.
- [ ] Los casos usan 29,99 € y 30,00 €, no 10 € ni 100 €.

## Qué has aprendido

- La tabla de decisión no solo da casos: encuentra reglas que se contradicen o no dicen nada.
- Una pregunta a negocio antes de programar cuesta menos que un bug en producción.
- Las técnicas se combinan: una condición sobre un importe se prueba en su valor límite.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Partición y valores límite](../lab-02-particion-y-valores-limite/README.md) | [Lab 04 - Exploratorio y regresión](../lab-04-exploratorio-y-regresion/README.md) → |

[↑ Semana 02](../../README.md)
