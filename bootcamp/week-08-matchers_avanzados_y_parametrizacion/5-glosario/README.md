# Glosario - Semana 08

| Término | Definición |
|---|---|
| Asimétrico (matcher) | Matcher que ocupa el lugar de un valor dentro de otro matcher, como `expect.any(String)`. Solo va en lo esperado |
| `describe.each` | Repite un `describe` entero, con todos sus tests, una vez por fila de una tabla |
| Desestructuración | Sintaxis de JavaScript para sacar campos de un objeto en variables: `({ name, expected }) =>` |
| `expect.any(Clase)` | Asimétrico que acepta cualquier valor de ese tipo: `String`, `Number` o una clase propia |
| `expect.arrayContaining` | Asimétrico que acepta un array con esos elementos, en cualquier orden y con otros además |
| `expect.extend` | Añade matchers propios a `expect`, con su condición y su mensaje de fallo |
| `expect.objectContaining` | Asimétrico que acepta un objeto con esos campos, tenga los que tenga además |
| `expect.stringMatching` | Asimétrico que acepta un texto que cumple una expresión regular |
| Expresión regular | Patrón para texto: `/^PLAN-\d{4}$/`. `^` y `$` son el principio y el final, `\d` un dígito |
| Fila | Un elemento de la tabla de un `it.each`. Cada fila genera un test |
| `it.each` | Ejecuta el mismo test una vez por fila de una tabla. Es lo mismo que `test.each` |
| Parametrizar | Escribir un test una vez y darle varios conjuntos de datos |
| `setupFilesAfterEnv` | Opción de Jest con los archivos que se cargan antes de cada archivo de tests, como los matchers propios |
| Tabla de casos | Los datos de los casos que comparten función, contexto y `expect`, uno por fila |
| `toContainEqual` | Matcher que comprueba que un array tiene un elemento igual campo a campo |
| `toHaveLength` | Matcher que comprueba el número de elementos de un array o de caracteres de un texto |
| `toHaveProperty` | Matcher que comprueba que existe un campo, con su ruta (`"session.room"`) y, opcionalmente, su valor |
| `toMatchObject` | Matcher que comprueba los campos que escribes; los demás dan igual |
| `toStrictEqual` | Matcher que compara campo a campo y no deja pasar campos con `undefined` ni tipos distintos |
| `toThrow` | Matcher que comprueba que una función lanza un error. Con un texto busca un trozo del mensaje; vacío, acepta cualquier error |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 09 - Jest: Testing Asíncrono](../../week-09-testing_asincrono_con_jest/README.md) → |

[↑ Semana 08](../README.md)
