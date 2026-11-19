# 01 - El caso de prueba

> Transversal: sin código.

Un **caso de prueba** (test case) describe cómo comprobar una sola cosa de un sistema: en qué estado tiene que estar antes, qué haces y qué tiene que pasar. Si otra persona lo sigue sin preguntarte y llega al mismo resultado, está bien escrito.

En la [semana 1](../../week-01-que_es_testing_calidad_de_software/1-teoria/03-ciclo-de-vida-del-bug.md) escribiste reportes de bug: pasos y resultado esperado de algo que **ya falló**. Un caso de prueba tiene las mismas piezas, pero se escribe **antes** de ejecutar, para algo que todavía no sabes si funciona.

---

## 🎯 Objetivos

- Nombrar los campos de un caso de prueba y para qué sirve cada uno.
- Escribir precondiciones, pasos y resultados esperados que otra persona pueda seguir sin preguntar.
- Distinguir happy path, sad path y caso límite.
- Relacionar un caso que falla con el reporte de bug que genera.

---

## 1. Los campos

| Campo | Qué contiene | Ejemplo |
|---|---|---|
| **ID** | Identificador único, que no cambia aunque cambie el título | `TC-LOGIN-001` |
| **Título** | Qué se comprueba y en qué condición | Login con credenciales válidas redirige al panel |
| **Precondiciones** | El estado del sistema antes del paso 1 | Existe el usuario `tester@example.com` con contraseña `Test1234!`, activo |
| **Datos de prueba** | Los valores exactos que se usan | email `tester@example.com`, contraseña `Test1234!` |
| **Pasos** | Acciones numeradas, una por paso | 1. Abre `http://localhost:3000/login` ... |
| **Resultado esperado** | Lo que tiene que verse o recibirse al terminar | Redirige a `/dashboard` y la cabecera muestra «Hola, Tester» |
| **Prioridad** | Cuánto importa ejecutarlo pronto si hay poco tiempo | Alta |

Al **ejecutar** el caso se añaden dos campos más:

| Campo | Qué contiene |
|---|---|
| **Resultado obtenido** | Lo que pasó de verdad. Se escribe solo si difiere del esperado |
| **Estado** | `Pass` (coincide), `Fail` (no coincide), `Blocked` (no se pudo ejecutar porque algo previo falla) o `Not Run` (no se ejecutó) |

`Blocked` no es `Fail`: si el login está caído, los 40 casos del carrito quedan bloqueados, no fallidos. Contarlos como fallos dice que el carrito tiene 40 bugs que no tiene.

La severidad, en cambio, no es un campo del caso: es del **bug** que aparece si el caso falla (la viste en la [semana 1](../../week-01-que_es_testing_calidad_de_software/1-teoria/03-ciclo-de-vida-del-bug.md#3-severidad-y-prioridad)). Lo que tiene el caso es la prioridad: qué ejecutar primero.

---

## 2. Precondiciones

Las precondiciones dicen qué tiene que ser cierto **antes** del paso 1. Si faltan, el caso puede fallar por algo que no tiene que ver con lo que pruebas, y alguien perderá tiempo buscando un bug que no existe.

| Vaga | Precisa |
|---|---|
| El usuario está registrado | Existe el usuario `tester@example.com` con contraseña `Test1234!` y estado `activo` |
| Hay productos | El catálogo tiene el producto `prod-442` «Auriculares Pro X», precio 89,99 €, stock 5 |
| El sistema funciona | La aplicación responde en `http://localhost:3000` |

Una regla práctica: si una precondición no se puede comprobar mirando algo (una pantalla, una tabla, una respuesta), es vaga.

---

## 3. Pasos

Cada paso es **una acción** con su objeto. Empieza con un verbo y dice dónde: qué campo, qué botón, qué URL.

```text
Vago:
1. Hacer login
2. Añadir un producto al carrito

Preciso:
1. Abre http://localhost:3000/login
2. Escribe tester@example.com en el campo "Email"
3. Escribe Test1234! en el campo "Contraseña"
4. Pulsa "Iniciar sesión"
5. Abre http://localhost:3000/products/prod-442
6. Pulsa "Añadir al carrito"
```

«Hacer login» parece suficiente hasta que la página tiene login con Google, con email y con enlace mágico. Quien ejecuta el caso no tiene que elegir.

Un caso que necesita 25 pasos suele probar varias cosas. Pártelo: el login va en su caso, y el carrito parte de la precondición «sesión iniciada como `tester@example.com`».

---

## 4. Resultado esperado

El resultado esperado tiene que ser **observable**: algo que se ve en pantalla, se recibe en una respuesta o se encuentra en un dato guardado. Con él, cualquiera decide `Pass` o `Fail` sin opinar.

| Vago | Observable |
|---|---|
| Funciona correctamente | Redirige a `/dashboard` y la cabecera muestra «Hola, Tester» |
| Muestra un error | Bajo «Contraseña» aparece «Credenciales incorrectas» y la URL sigue siendo `/login` |
| El pedido se crea | La respuesta es `201`, el cuerpo tiene `orderId` y `status` vale `pending` |
| No pasa nada | El formulario no se envía, el campo queda vacío y no aparece ningún mensaje |

«Muestra un error» pasa aunque el error sea «500 Internal Server Error». Por eso el texto del mensaje va en el esperado.

Un caso puede tener varias comprobaciones en el esperado, siempre que todas sean de la misma acción: el redirect y el saludo son dos caras del mismo login.

---

## 5. IDs y títulos

El ID identifica el caso para siempre: aparece en reportes de bug, en informes de ejecución y en la conversación («el TC-CART-011 falla otra vez»). Un patrón habitual es `TC-<MÓDULO>-<NNN>`:

```text
TC-LOGIN-001   caso 1 del módulo de login
TC-CART-011    caso 11 del carrito
```

En el proyecto añadirás delante la abreviatura de tu dominio: `TC-<DOMINIO>-<MÓDULO>-<NNN>`.

El título resume **qué** se comprueba y **en qué condición**, como el título de un reporte de bug: «Añadir dos veces el mismo producto suma la cantidad», no «Carrito» ni «Prueba 3».

---

## 6. Happy path, sad path y casos límite

Un módulo necesita casos de tres tipos:

| Tipo | Qué prueba | Ejemplo en el login |
|---|---|---|
| **Happy path** | El uso normal, con datos válidos | Credenciales correctas |
| **Sad path** | Entradas inválidas o caminos alternativos | Contraseña incorrecta, usuario bloqueado |
| **Caso límite** | Valores en el borde de lo permitido | Contraseña de exactamente 8 caracteres, el mínimo |

Una suite solo con happy paths dice que el sistema funciona cuando todo va bien, que es justo cuando menos falla. Los sad paths y los límites son donde salen los bugs. Las técnicas de la [teoría 02](02-tecnicas-de-diseno.md) sirven para elegirlos sin olvidar ninguno.

---

## 7. Del caso que falla al reporte de bug

Cuando un caso termina en `Fail`, el reporte de bug sale casi hecho: los pasos y el esperado son los del caso, y el obtenido es lo que viste.

```text
TC-AUTH-003  "Login con contraseña vacía muestra error"  ->  Fail
   |
   `-- BUG-047  [Login] Se inicia sesión con la contraseña vacía
         Pasos y esperado: los de TC-AUTH-003
         Obtenido: redirige a /dashboard
         Referencia: TC-AUTH-003
```

El reporte cita el ID del caso. Así, cuando el bug se corrija, se sabe qué caso volver a ejecutar para cerrarlo.

---

## Ejemplo completo

```text
ID:                 TC-AUTH-001
Título:             Login con credenciales válidas redirige al panel
Precondiciones:
  - Existe el usuario tester@example.com con contraseña Test1234!, estado activo
  - No hay ninguna sesión iniciada en el navegador
Datos de prueba:
  - email: tester@example.com
  - contraseña: Test1234!
Pasos:
  1. Abre http://localhost:3000/login
  2. Escribe tester@example.com en el campo "Email"
  3. Escribe Test1234! en el campo "Contraseña"
  4. Pulsa "Iniciar sesión"
Resultado esperado:
  - La URL pasa a ser http://localhost:3000/dashboard
  - La cabecera muestra "Hola, Tester"
Prioridad:          Alta
```

---

## ✅ Resumen

- Un caso de prueba dice el estado de partida, las acciones y lo que tiene que pasar; otra persona lo ejecuta sin preguntar.
- Las precondiciones se pueden comprobar; los pasos son una acción cada uno; el esperado es observable.
- `Blocked` no es `Fail`. La severidad es del bug, la prioridad es del caso.
- Una suite útil tiene happy paths, sad paths y casos límite.
- Un caso que falla da el reporte de bug casi escrito, con su ID como referencia.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 02 - Casos de Prueba y Técnicas de Diseño](../README.md) | [Teoría — 02 - Técnicas de diseño: partición, valores límite y tablas de decisión](02-tecnicas-de-diseno.md) → |

[↑ Semana 02](../README.md)
