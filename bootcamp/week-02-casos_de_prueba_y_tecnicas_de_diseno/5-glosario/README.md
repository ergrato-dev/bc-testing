# Glosario - Semana 02

| Término | Definición |
|---|---|
| Caso de prueba (test case) | Descripción de cómo comprobar una cosa: precondiciones, datos, pasos y resultado esperado. |
| Precondición | Lo que tiene que ser cierto antes del primer paso. Se puede comprobar mirando algo. |
| Datos de prueba | Los valores exactos que usa un caso. |
| Resultado esperado | Lo que tiene que verse o recibirse al terminar el caso; permite decidir `Pass` o `Fail` sin opinar. |
| Resultado obtenido | Lo que pasó de verdad al ejecutar el caso. |
| Pass / Fail | El obtenido coincide / no coincide con el esperado. |
| Blocked | El caso no se pudo ejecutar porque algo previo falla. No es un `Fail`. |
| Happy path | El uso normal de una función, con datos válidos. |
| Sad path | Entradas inválidas o caminos alternativos. |
| Caso límite (edge case) | Un valor en el borde de lo permitido. |
| Técnica de diseño de pruebas | Método para elegir pocas entradas con muchas probabilidades de encontrar errores. |
| Partición de equivalencia | Técnica que agrupa las entradas que el sistema debería tratar igual y prueba un valor de cada grupo. |
| Clase de equivalencia | Grupo de entradas que el sistema debería tratar igual. Puede ser válida o inválida. |
| Análisis de valores límite (BVA) | Técnica que prueba los valores en el borde de cada clase de un dato ordenado. |
| BVA de 2 valores | El límite y su vecino del otro lado: para un mínimo de 18, el 17 y el 18. |
| BVA de 3 valores | El límite y sus dos vecinos: para un mínimo de 18, el 17, el 18 y el 19. |
| Off-by-one | Error por uno: `>` en lugar de `>=`, o un bucle que da una vuelta de más o de menos. |
| Tabla de decisión | Tabla con una columna por cada combinación de condiciones y el resultado de cada una. |
| Regla (de una tabla de decisión) | Una columna de la tabla: una combinación de condiciones y su resultado. |
| Matriz de trazabilidad | Tabla que relaciona cada requisito con los casos que lo prueban. |
| Cobertura de requisitos | Porcentaje de requisitos con al menos un caso de prueba. |
| Cobertura de sentencias (statement coverage) | Porcentaje de líneas de código que ejecutaron los tests. |
| Cobertura de ramas (branch coverage) | Porcentaje de caminos de cada `if` que tomaron los tests. |
| Smoke test | Comprobación rápida de las funciones críticas tras una build nueva: ¿arranca y se puede usar? |
| Sanity test | Comprobación rápida de la zona que cambió tras un arreglo. |
| Regresión | Algo que funcionaba y deja de funcionar por un cambio en otra parte. La suite de regresión existe para detectarlas. |
| Testing guionizado (scripted) | Ejecutar casos escritos antes, paso a paso. |
| Testing exploratorio | Diseñar y ejecutar pruebas a la vez, decidiendo el siguiente paso por lo que se ve. |
| Charter | Objetivo escrito de una sesión exploratoria: qué explorar, con qué y para qué. |
| Sesión exploratoria | Bloque de tiempo fijo de testing exploratorio con un charter y notas. |
| Plan de pruebas | Documento con el alcance, el enfoque, los criterios de entrada y salida y los riesgos del testing de una release. |
| TestRail, Zephyr, Xray | Herramientas de gestión de casos de prueba usadas en empresas. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 03 — Primeros Tests Automatizados con JavaScript](../../week-03-primeros_tests_automatizados_con_javascript/README.md) → |

[↑ Semana 02](../README.md)
