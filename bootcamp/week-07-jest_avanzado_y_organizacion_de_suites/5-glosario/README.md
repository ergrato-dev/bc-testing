# Glosario - Semana 07

| Término | Definición |
|---|---|
| `afterAll` | Hook que Jest ejecuta una vez, después del último test de su `describe`. Cierra lo que abrió `beforeAll` |
| `afterEach` | Hook que Jest ejecuta después de cada test de su `describe` y de los que tiene dentro. Deshace lo que el test cambió fuera de él |
| Aislamiento | Propiedad de un test que da el mismo resultado solo, en grupo y en cualquier orden |
| `beforeAll` | Hook que Jest ejecuta una vez, antes del primer test de su `describe`. Solo para lo que los tests no modifican |
| `beforeEach` | Hook que Jest ejecuta antes de cada test de su `describe` y de los que tiene dentro. Prepara un estado nuevo para cada test |
| Colaborador | Objeto del que depende el código que pruebas para hacer su trabajo: un reloj, un notificador, un repositorio |
| Contexto | `describe` que nombra una situación, siempre con `when ...`. Agrupa los tests que comparten esa situación |
| Doble de prueba | Objeto que ocupa el lugar de un colaborador durante un test |
| Estado compartido | Datos que varios tests leen y modifican. Hace que el resultado de un test dependa de los anteriores |
| Hook | Función que Jest ejecuta en un momento fijo, antes o después de los tests de su `describe` |
| Inyección de dependencias | Pasar los colaboradores como parámetros en lugar de importarlos dentro del código, para poder sustituirlos |
| `it.only` / `describe.only` | Ejecuta solo esos tests del archivo; los demás salen como `skipped` y el código de salida sigue siendo 0 |
| `it.skip` / `describe.skip` | No ejecuta ese test; sale como `skipped` |
| `jest.fn()` | Función falsa de Jest: devuelve lo que le digas y anota cada llamada |
| `jest.restoreAllMocks()` | Devuelve a cada método espiado con `jest.spyOn` su función original |
| `jest.spyOn(objeto, "método")` | Cambia el método por una función falsa que anota las llamadas y ejecuta la original |
| Mock | Doble que anota las llamadas para que el test compruebe cómo se le llamó: argumentos y número de veces |
| `mockImplementation(fn)` | Hace que la función falsa ejecute `fn` en lugar de la original |
| `mockReturnValue(valor)` | Hace que la función falsa devuelva `valor` en cada llamada |
| `.mock.calls` | Lista con los argumentos de cada llamada a una función falsa |
| Nombre completo | Los nombres de todos los `describe` y del `it`, unidos. Aparece en la cabecera de un fallo y es lo que busca `-t` |
| `--randomize` | Opción de Jest que desordena los tests y los `describe` de cada archivo |
| `restoreMocks` | Ajuste de la sección `jest` de `package.json` que restaura los spies después de cada test |
| `--seed` | Opción de Jest que fija la semilla de `--randomize` para repetir un desorden |
| Spy | Doble que envuelve una función real: anota las llamadas y, salvo que digas otra cosa, la ejecuta |
| Stub | Doble que devuelve una respuesta fija para llevar el código por un camino; el test mira el resultado, no el stub |
| `toHaveBeenCalledTimes(n)` | Matcher que pasa si la función falsa se llamó exactamente `n` veces |
| `toHaveBeenCalledWith(...)` | Matcher que pasa si la función falsa se llamó alguna vez con esos argumentos |
| `--verbose` | Opción de Jest que lista cada test con su nombre, dentro de sus `describe` |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 08 - JavaScript Testing II: Matchers Avanzados y Parametrización](../../week-08-matchers_avanzados_y_parametrizacion/README.md) → |

[↑ Semana 07](../README.md)
