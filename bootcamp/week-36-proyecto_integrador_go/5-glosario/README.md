# Glosario Semana 36 - Proyecto Integrador: Etapa 4 (Go)

## A

- **`atexit_sleep_ms`**: opción de `GORACE` con los milisegundos que espera un programa compilado con `-race` antes de terminar, para informar de carreras en goroutines que siguen vivas. Por defecto, 1000.

## B

- **Bloque de cobertura**: trozo de código sin ramas que Go instrumenta como una unidad. Cada línea de `cover.out` es un bloque: posición, número de sentencias y contador.

## C

- **`(cached)`**: lo que muestra `go test` en lugar del tiempo cuando repite un resultado guardado sin ejecutar nada. `-count=1` lo evita.
- **Código de salida**: número con el que termina un programa: 0 si fue bien, otro si falló. En el shell se lee con `$?`.

## F

- **Fase**: grupo de comprobaciones de la suite que se ejecutan juntas y cuestan parecido: análisis, unitarios, integración.

## G

- **`GORACE`**: variable de entorno con las opciones del race detector, por ejemplo `GORACE=atexit_sleep_ms=0`.

## L

- **`-list`**: opción de `go test` que imprime los nombres de los tests que cumplen una expresión regular sin ejecutarlos. Sí ejecuta `TestMain`.

## P

- **`pipefail`**: opción de bash (`set -o pipefail`) que hace que una tubería termine con error si falla cualquiera de sus comandos, no solo el último.

## S

- **`set -e`**: opción de bash que para el script en cuanto un comando termina con error.
- **`set -u`**: opción de bash que trata como error usar una variable que no está definida.

## T

- **`tee`**: comando que copia lo que recibe a la salida y a un archivo a la vez. En una tubería, su código de salida tapa el del comando anterior si no hay `pipefail`.

## U

- **Umbral de cobertura**: porcentaje mínimo de cobertura por debajo del cual la suite falla. En Go no existe como opción: lo comprueba `verify.sh` con `go tool cover -func` y `awk`.

## V

- **`verify.sh`**: script en la raíz del módulo que ejecuta la suite completa por fases y se para en el primer fallo. El equivalente Go de `mvn clean verify`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 36](../README.md)
