# Glosario - Semana 03

| Término | Definición |
|---|---|
| AAA (Arrange, Act, Assert) | Orden de un test: preparar los datos, hacer una sola llamada y comprobar el resultado |
| `allowBuilds` | Ajuste de `pnpm-workspace.yaml` que dice qué dependencias pueden ejecutar scripts al instalarse |
| Aserción | Comprobación que marca el test como fallido si no se cumple. En Jest, cada `expect(...).matcher(...)` |
| CommonJS | Sistema de módulos con `require` y `module.exports`; el que Jest entiende sin configurar |
| corepack | Herramienta de Node.js que instala la versión de pnpm que pide el campo `packageManager` |
| Dependencia de desarrollo | Librería necesaria para probar o construir, no para ejecutar el programa. Va en `devDependencies` (`pnpm add -D`) |
| `describe` | Función de Jest que agrupa tests; su nombre aparece en el informe |
| Error de ejecución | Fallo de un test que se rompió antes de llegar al `expect` (`TypeError`, `Cannot find module`) |
| `expect` | Función de Jest que recibe el valor a comprobar; el matcher que la sigue hace la comprobación |
| Fallo de aserción | Fallo de un test cuyo código se ejecutó pero devolvió otro valor; el informe trae `Expected` y `Received` |
| Función flecha | `() => { ... }`: función sin nombre. Jest guarda la de cada test y la ejecuta cuando le toca |
| Función pura | Función que con los mismos datos devuelve siempre lo mismo y no lee ni cambia nada externo (reloj, ficheros, red) |
| `it` / `test` | Función de Jest que define un test. Son la misma; el curso usa `it` |
| Jest | Framework de tests de JavaScript: busca los archivos `.test.js`, ejecuta los tests y muestra el informe |
| Lockfile | `pnpm-lock.yaml`: las versiones exactas instaladas de cada dependencia. Se sube con el proyecto |
| Matcher | Método que sigue a `expect` y define la comparación: `toBe`, `toEqual`, `toThrow`... |
| Modo watch | `pnpm test --watch`: Jest vuelve a ejecutar los tests al guardar un archivo. Necesita git |
| `module.exports` | Lo que un archivo CommonJS deja usar a otros archivos |
| Mutación | Cambio hecho a propósito en el código para comprobar si algún test lo detecta |
| Node.js | Programa que ejecuta JavaScript fuera del navegador. El curso usa la versión 22 |
| `package.json` | Archivo que describe el proyecto: nombre, versión de pnpm, scripts y dependencias |
| `packageManager` | Campo de `package.json` con la versión exacta de pnpm del proyecto |
| pnpm | Gestor de paquetes del curso: instala las dependencias y ejecuta los scripts |
| `require` | Función de CommonJS que carga lo que exporta otro archivo |
| Rojo y verde | Ver un test fallar por el motivo esperado (rojo) y después pasar al corregir el código (verde) |
| Script | Comando con nombre en `package.json` (`"test": "jest"`) que se lanza con `pnpm <nombre>` |
| Spread (propagación) | `{ ...objeto, campo: valor }`: copia un objeto cambiando algunos campos |
| `toBe` | Matcher de igualdad exacta, para números, textos, `true`/`false` y `null` |
| `toBeCloseTo` | Matcher para decimales: acepta diferencias menores de 0,005 por defecto |
| `toEqual` | Matcher que compara objetos y arrays campo a campo |
| `toThrow` | Matcher que comprueba que una función lanza un error; `expect` recibe la función, no la llamada |
| Test automatizado | Caso de prueba escrito como código, que el ordenador ejecuta y comprueba solo |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 04 — Primeros Tests Automatizados con Python](../../week-04-primeros_tests_automatizados_con_python/README.md) → |

[↑ Semana 03](../README.md)
