# Ejercicio 01 - Promesas y Async/Await

## Objetivo

Escribir tests asincronos confiables con `async/await`, `resolves` y `rejects`, detectar falsos positivos y simular dependencias que rechazan.

## Tiempo estimado

90 minutos.

## Pasos

1. Abre `starter/user.repository.test.js` y revisa `starter/user.repository.js`.
2. Descomenta PASO 1 para el caso exitoso con `await`.
3. Descomenta PASO 2 para `resolves`.
4. Descomenta PASO 3 para `rejects`.
5. Descomenta PASO 4 para ver un falso positivo y corregirlo:
   - Descomenta tambien el test marcado como "NO usar" (usa `getUserById(5)`, que no rechaza) y ejecuta: queda en verde sin haber comprobado nada.
   - Agrega `expect.assertions(1);` al inicio de ese test y ejecuta: ahora falla con `Expected one assertion to be called but received zero assertion calls.`
   - Vuelve a comentarlo. Los tests correctos del PASO usan `expect.assertions(1)` y `return` de la promesa (sin `return` ni `await`, Jest no espera y el `expect` corre cuando el test ya termino).
6. Descomenta PASO 5 para simular una dependencia que falla con `mockRejectedValue`.
7. Compara con `solution/user.repository.test.js`.

## Comando sugerido

```bash
pnpm install
pnpm test user.repository.test.js
```
