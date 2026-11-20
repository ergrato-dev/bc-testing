# Glosario - Semana 06

Los términos de casos de prueba (partición, valores límite, matriz de trazabilidad de la semana 2) están en el [glosario de la semana 2](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/5-glosario/README.md); los de código de salida y mutación, en el de la [semana 3](../../week-03-primeros_tests_automatizados_con_javascript/5-glosario/README.md).

| Término | Definición |
|---|---|
| Alcance | Lo que el plan prueba (dentro) y lo que no (fuera), con el motivo de cada exclusión |
| `awk` | Programa que lee un texto línea a línea y lo corta en campos; `-F'\|'` corta por `\|` |
| `comm` | Compara dos listas ordenadas: `-23` deja lo que solo está en la primera, `-13` lo que solo está en la segunda |
| Criterio de entrada | Condición que se cumple antes de empezar a probar |
| Criterio de salida | Condición comprobable, con un comando o un número, que da la prueba por terminada |
| Evidencia de ejecución | Salida real de las pruebas que demuestra que se cumplen los criterios de salida |
| Hueco | Diferencia entre la matriz y los tests: un caso sin test, un test sin caso o una regla sin caso |
| Impacto | Cuánto daño hace un fallo: Alto, Medio o Bajo |
| Plan de pruebas | Documento que dice qué se prueba, por qué, cómo y cuándo se da por terminado |
| Prioridad | Importancia de un caso, que sale de cruzar el impacto y la probabilidad de su riesgo |
| Probabilidad | Cuánto se espera que algo falle: Alta, Media o Baja |
| Riesgo | Algo que puede fallar, medido con su impacto y su probabilidad |
| `set -euo pipefail` | Opciones de bash: parar en el primer error, tratar como error una variable sin definir y hacer fallar una tubería si falla cualquiera de sus comandos |
| Shebang | Primera línea de un script (`#!/usr/bin/env bash`) que dice qué programa lo ejecuta |
| Subshell | Copia de la shell que abren los paréntesis: un `cd` dentro de `( ... )` no cambia la carpeta de fuera |
| Trazabilidad | Poder seguir cada regla hasta su caso y cada caso hasta su test en cada lenguaje |
| `verify.sh` | Script del curso que ejecuta las tres suites y compara la matriz del plan con los ID de los tests |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 07 - JavaScript Testing I: Jest Avanzado y Organización de Suites](../../week-07-jest_avanzado_y_organizacion_de_suites/README.md) → |

[↑ Semana 06](../README.md)
