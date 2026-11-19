# Lab 01 - Análisis de casos

## Objetivo

Revisar cinco casos de prueba heredados, encontrar lo que impide ejecutarlos igual cada vez y reescribirlos con precondiciones comprobables, pasos de una acción y resultados observables.

## Tiempo estimado

60 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-casos-de-prueba.md).
- Una terminal (en Windows, WSL o Git Bash) y un editor de texto.

## Punto de partida

Crea la carpeta de los labs de la semana y copia la plantilla:

```bash
mkdir -p ~/labs-semana-02
cd ~/labs-semana-02
cp <ruta-del-bootcamp>/bootcamp/week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-01-analisis-de-casos/starter/casos.md .
```

Sustituye `<ruta-del-bootcamp>` por la carpeta donde clonaste el bootcamp. Los comandos `mkdir -p`, `cd` y `cp` se explican en el [lab 01 de la semana 1](../../../week-01-que_es_testing_calidad_de_software/2-practicas/lab-01-calidad-y-niveles/README.md#punto-de-partida).

## Contexto

Entras en un equipo que tiene una colección de casos escrita hace tiempo por varias personas. Antes de ejecutarlos en la próxima release, te piden revisarlos. Cada caso de [`casos.md`](starter/casos.md) tiene un problema distinto; algunos tienen varios.

---

## Paso 1: intentar ejecutarlos en la cabeza (10 min)

Lee cada caso como si tuvieras que ejecutarlo ahora, sin preguntar a nadie. Apunta dónde te paras: qué formulario, qué datos, qué botón, cómo sabes si ha pasado. Cada parada es un problema.

## Paso 2: la lista de comprobación (15 min)

Pasa cada caso por estas preguntas y escribe cada «no» en **Problemas encontrados**, con el campo afectado:

| Pregunta | Teoría 01 |
|---|---|
| ¿El ID sigue el patrón `TC-<MÓDULO>-<NNN>` y el título dice qué y en qué condición? | Sección 5 |
| ¿Las precondiciones se pueden comprobar? ¿Cubren todo lo que el esperado da por hecho? | Sección 2 |
| ¿Los datos de prueba son valores concretos? | Sección 1 |
| ¿Cada paso es una acción, con verbo y dónde? | Sección 3 |
| ¿El esperado permite decidir `Pass` o `Fail` sin opinar? | Sección 4 |
| ¿La prioridad encaja con lo que el caso protege? | Sección 1 |

Dos casos necesitan pensarlo más:

- **D** parece correcto. Busca qué da por hecho el resultado esperado.
- **E** mezcla dos datos inválidos. La [teoría 02](../../1-teoria/02-tecnicas-de-diseno.md#una-clase-inválida-por-caso) explica por qué es un problema; puedes leer solo esa sección.

## Paso 3: reescribir (25 min)

Escribe cada **Caso corregido** con todos los campos. Usa los datos de la tienda del principio del archivo; si te falta un dato que no está ahí (el texto de un mensaje de error, por ejemplo), escribe `[Confirmar: ...]` en lugar de inventarlo, como hiciste con `[Preguntar: ...]` en el lab 03 de la semana 1.

El caso E se convierte en dos casos, uno por dato inválido, cada uno con el resto de datos válidos.

## Paso 4: el resumen (10 min)

Rellena la tabla **Resumen** con el problema principal de cada caso. Si tuvieras que elegir una sola regla para quien escribió estos casos, ¿cuál sería? Escríbela debajo de la tabla en una línea.

---

## Comprobación final

```bash
grep -c '(completar' casos.md
# 0
```

`grep -c` cuenta las líneas que contienen el texto; `0` quiere decir que no queda nada por rellenar.

Repasa:

- [ ] Ningún esperado corregido dice «error», «funciona» o «correctamente» sin decir qué se ve.
- [ ] El caso D corregido tiene una precondición sobre el contenido del carrito.
- [ ] El caso E es ahora dos casos con IDs distintos.
- [ ] Ningún dato es inventado: lo que falta va como `[Confirmar: ...]`.

## Qué has aprendido

- Un caso que no se puede ejecutar igual dos veces no sirve para decidir si algo funciona.
- Las precondiciones tienen que cubrir todo lo que el resultado esperado da por hecho.
- Un dato inválido por caso: si no, no sabes qué regla funcionó.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Partición y valores límite](../lab-02-particion-y-valores-limite/README.md) → |

[↑ Semana 02](../../README.md)
