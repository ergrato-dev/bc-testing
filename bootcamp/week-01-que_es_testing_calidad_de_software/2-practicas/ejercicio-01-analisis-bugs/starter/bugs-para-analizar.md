# Bugs para Analizar

Revisa cada reporte. Identifica los problemas y escribe tu corrección en la sección **"Mi análisis"** de cada uno.

---

## Bug #1

| Campo | Contenido original |
| --- | --- |
| **Título** | `Bug en el login` |
| **Pasos para reproducir** | 1. Ir al login. 2. Intentar entrar. 3. No funciona. |
| **Resultado obtenido** | No entra |
| **Resultado esperado** | *(en blanco)* |
| **Severidad** | Critical |
| **Prioridad** | P1 |
| **Entorno** | *(en blanco)* |

### Mi análisis — Bug #1

**Problemas encontrados:**
- (escribe aquí)

**Título corregido:**

**Severidad corregida:** — justificación:

**Prioridad corregida:** — justificación:

**Pasos corregidos:**
1.
2.
3.

**Resultado esperado:**

---

## Bug #2

| Campo | Contenido original |
| --- | --- |
| **Título** | `[Perfil de usuario] El campo "teléfono" acepta letras sin mostrar error de validación` |
| **Pasos para reproducir** | 1. Iniciar sesión con una cuenta válida. 2. Ir a Configuración → Perfil. 3. En el campo "Teléfono", escribir `abcdef`. 4. Hacer clic en "Guardar". |
| **Resultado obtenido** | El sistema guarda el valor `abcdef` en el campo teléfono sin mostrar ningún mensaje de error. |
| **Resultado esperado** | El sistema debe rechazar el valor y mostrar el mensaje: "El teléfono solo puede contener dígitos". |
| **Severidad** | Critical |
| **Prioridad** | P1 |
| **Entorno** | Chrome 122, Windows 11, app v3.1.0 |

### Mi análisis — Bug #2

**Problemas encontrados:**
- (escribe aquí)

**Severidad corregida:** — justificación:

**Prioridad corregida:** — justificación:

---

## Bug #3

| Campo | Contenido original |
| --- | --- |
| **Título** | `[Checkout] El pedido se duplica al hacer doble clic en "Confirmar compra"` |
| **Pasos para reproducir** | 1. Agregar cualquier producto al carrito. 2. Ir a la pantalla de checkout. 3. Hacer doble clic rápido en el botón "Confirmar compra". |
| **Resultado obtenido** | Se generan dos pedidos idénticos y se realizan dos cargos a la tarjeta de crédito. |
| **Resultado esperado** | Solo debe crearse un pedido y realizarse un único cargo. |
| **Severidad** | Low |
| **Prioridad** | P4 |
| **Entorno** | Safari 17, macOS 14, app v3.1.0 |

### Mi análisis — Bug #3

**Problemas encontrados:**
- (escribe aquí)

**Severidad corregida:** — justificación:

**Prioridad corregida:** — justificación:

---

## Bug #4

| Campo | Contenido original |
| --- | --- |
| **Título** | `[Búsqueda] A veces los resultados están mal` |
| **Pasos para reproducir** | 1. En algunos casos, buscar algo. 2. A veces los resultados no son los correctos. |
| **Resultado obtenido** | Resultados incorrectos |
| **Resultado esperado** | Resultados correctos |
| **Severidad** | High |
| **Prioridad** | P2 |
| **Entorno** | Firefox, app nueva |

### Mi análisis — Bug #4

**Problemas encontrados:**
- (escribe aquí)

**Título corregido:**

**Pasos corregidos (propuesta):**
1.
2.
3.

**Entorno corregido:**

---

## Bug #5

| Campo | Contenido original |
| --- | --- |
| **Título** | `[Registro] El botón "Crear cuenta" permanece deshabilitado aunque todos los campos estén completos y válidos` |
| **Pasos para reproducir** | 1. Ir a la página de registro: `https://app.exemplo.com/register`. 2. Completar todos los campos: nombre, email válido, contraseña con ≥8 caracteres, confirmación de contraseña. 3. Aceptar los términos de uso. 4. Verificar el estado del botón "Crear cuenta". |
| **Resultado obtenido** | El botón "Crear cuenta" permanece deshabilitado (gris) aunque todos los campos están completos y el formulario no muestra errores de validación. |
| **Resultado esperado** | El botón debe habilitarse (activarse) una vez que todos los campos estén correctamente completos. |
| **Severidad** | High |
| **Prioridad** | P1 |
| **Entorno** | Chrome 122 y Firefox 123, Windows 11, app v3.1.0 |
| **Nota adicional** | El bug es 100% reproducible. Se probó en dos navegadores distintos con el mismo resultado. |

### Mi análisis — Bug #5

**Problemas encontrados:**
- (escribe aquí)

*(Si el reporte está bien, justifica por qué no necesita cambios)*
