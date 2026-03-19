# Casos de Prueba — Estructura y Campos

> **Semana 02 — Teoría 01** | Etapa 0: Fundamentos de Testing | Transversal (JS / Python / Java)

---

## ¿Qué es un caso de prueba?

Un **caso de prueba** (test case) es un documento que especifica **de forma precisa y reproducible** cómo verificar que una funcionalidad de software se comporta como se espera.

Un test sin estructura es un test difícil de reproducir, mantener y comunicar. Un caso de prueba bien escrito puede ser ejecutado por cualquier persona del equipo y produce siempre el mismo resultado.

> **Regla de oro**: Si otra persona no puede reproducir el test exactamente con los mismos pasos, el caso de prueba está mal diseñado.

---

## Estructura de un Caso de Prueba

Un caso de prueba profesional contiene los siguientes campos:

| Campo | Descripción | Ejemplo |
|---|---|---|
| **ID** | Identificador único del caso | `TC-LOGIN-001` |
| **Título** | Descripción breve de qué se verifica | "Login exitoso con credenciales válidas" |
| **Funcionalidad** | Módulo o feature que se testea | Módulo de autenticación |
| **Precondiciones** | Estado del sistema antes de ejecutar | Usuario registrado con email y contraseña activos |
| **Datos de prueba** | Valores específicos a usar | email: `tester@example.com`, pass: `Test1234!` |
| **Pasos** | Acciones ordenadas y atómicas | 1. Navegar a `/login` 2. Ingresar email 3. ... |
| **Resultado esperado** | Qué debe ocurrir exactamente | El sistema redirige a `/dashboard` y muestra "Bienvenido, Tester" |
| **Resultado obtenido** | Qué ocurrió realmente (se completa al ejecutar) | *(vacío al escribir — se llena al ejecutar)* |
| **Estado** | Pass / Fail / Blocked / Skipped | — |
| **Severidad** | Impacto si falla | High |
| **Prioridad** | Urgencia de ejecutarlo | P1 |
| **Autor** | Quién escribió el caso | Nombre del tester |
| **Fecha** | Cuándo fue creado/revisado | 2026-03-19 |

---

## El Campo más Ignorado: Precondiciones

Las **precondiciones** son el estado que **debe existir** antes de ejecutar el test. Sin ellas, el test puede fallar por razones completamente ajenas al comportamiento que se prueba.

### Precondiciones incorrectas vs correctas

| ❌ Vaga | ✅ Precisa |
|---|---|
| "El usuario debe estar registrado" | "Existe un usuario con email `tester@example.com` y contraseña `Test1234!`, con estado `activo`" |
| "La base de datos debe tener datos" | "La tabla `products` contiene al menos 5 registros con `stock > 0`" |
| "El sistema debe estar funcionando" | "La aplicación está desplegada en `http://localhost:3000` y responde HTTP 200 en `/health`" |

---

## El Campo más Mal Escrito: Resultado Esperado

El **resultado esperado** debe ser **observable**, **concreto** y **verificable**. Debe describir exactamente qué ve, escucha o recibe el tester al final de la acción.

### Resultados esperados incorrectos vs correctos

| ❌ Vago | ✅ Concreto |
|---|---|
| "El sistema funciona correctamente" | "El sistema redirige al usuario a `/dashboard` con código HTTP 302" |
| "Muestra un error" | "Muestra el mensaje `Credenciales incorrectas. Intenta de nuevo.` en color rojo bajo el campo de contraseña" |
| "La orden se crea" | "La respuesta HTTP es 201, el cuerpo incluye `orderId` de tipo UUID y el campo `status` es `pending`" |
| "No pasa nada" | "El campo permanece vacío, no se ejecuta ninguna acción y no aparece ningún mensaje" |

---

## Pasos: Atómicos y Ordenados

Cada paso debe describir **una única acción**. El orden debe ser lógico y sin saltos implícitos.

### Pasos incorrectos vs correctos

```
❌ Incorrecto:
1. Hacer login con el usuario de prueba
2. Ir al carrito y agregar el producto

✅ Correcto:
1. Navegar a http://localhost:3000/login
2. Ingresar "tester@example.com" en el campo "Email"
3. Ingresar "Test1234!" en el campo "Contraseña"
4. Hacer clic en el botón "Iniciar sesión"
5. Navegar a http://localhost:3000/products
6. Hacer clic en "Agregar al carrito" del primer producto visible
```

---

## Identificadores y Convención de Nombres

Usar una convención consistente de IDs facilita la trazabilidad entre casos, bugs y funcionalidades.

**Patrón recomendado**: `TC-[MÓDULO]-[NÚMERO]`

```
TC-LOGIN-001   → Caso 1 del módulo Login
TC-LOGIN-002   → Caso 2 del módulo Login
TC-CART-001    → Caso 1 del módulo Carrito
TC-PAYMENT-003 → Caso 3 del módulo Pagos
```

**Patrón para suites de dominio en el bootcamp**: `TC-[DOMINIO]-[MÓDULO]-[NÚMERO]`

```
TC-LIBRARY-LOAN-001   → Biblioteca, módulo de préstamos, caso 1
TC-GYM-MEMBER-002     → Gimnasio, módulo de miembros, caso 2
```

---

## Ejemplo Completo

```
ID:              TC-AUTH-001
Título:          Login exitoso con credenciales válidas
Funcionalidad:   Autenticación de usuarios
Precondiciones:
  - Existe un usuario registrado con email "tester@example.com"
    y contraseña "Test1234!" en estado "activo"
  - La aplicación está disponible en http://localhost:3000

Datos de prueba:
  - email: "tester@example.com"
  - password: "Test1234!"

Pasos:
  1. Navegar a http://localhost:3000/login
  2. Ingresar "tester@example.com" en el campo "Email"
  3. Ingresar "Test1234!" en el campo "Contraseña"
  4. Hacer clic en el botón "Iniciar sesión"

Resultado esperado:
  - El sistema redirige al usuario a http://localhost:3000/dashboard
  - La barra superior muestra el nombre del usuario: "Tester"
  - El campo de contraseña no es visible en la URL ni en los logs

Severidad: High
Prioridad:  P1
Autor:      QA Team
```

---

## Happy Path, Sad Path y Edge Cases

Todo módulo debe tener al menos tres tipos de casos:

| Tipo | Descripción | Ejemplo (Login) |
|---|---|---|
| **Happy path** | El flujo normal y correcto | Login con credenciales válidas |
| **Sad path** | Entradas incorrectas o flujos alternativos | Login con contraseña incorrecta |
| **Edge case** | Casos extremos o poco comunes | Login con contraseña de 1 carácter, con espacios al inicio |

---

## Relación entre Caso de Prueba y Bug Report

Cuando un caso de prueba **falla**, genera un **bug report** (semana 01). La relación es directa:

```
Caso de prueba TC-AUTH-003 → FALLA
  └── Se crea Bug Report BUG-2026-047
        Título: "Login acepta contraseña vacía"
        Referencia: TC-AUTH-003
        Pasos del caso de prueba + resultado obtenido
```

Siempre referenciar el ID del caso de prueba en el bug report correspondiente.

---

## Resumen Visual

```
CASO DE PRUEBA
├── Identificación: ID, Título, Funcionalidad
├── Contexto:       Precondiciones, Datos de prueba
├── Ejecución:      Pasos (atómicos, ordenados)
├── Verificación:   Resultado esperado (observable y concreto)
└── Metadatos:      Severidad, Prioridad, Autor, Estado
```

---

## Próximo tema

→ [Técnicas de Diseño — Partición, BVA y Tablas de Decisión](./02-tecnicas-de-diseno.md)
