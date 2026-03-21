# Casos Corregidos — Solución Completa

> Ejercicio 01 — Solution | Semana 02

---

## Caso A — Análisis de defectos

| Defecto | Descripción |
|---|---|
| **Título vago** | "Registro" no indica qué se está probando ni cuál es el resultado esperado |
| **Precondiciones ausentes** | No se sabe el estado de la plataforma, si el email ya existe, etc. |
| **Datos de prueba ausentes** | No hay valores específicos — no es reproducible |
| **Pasos ambiguos** | "Entrar a la web", "llenar el formulario", "darle al botón" no son atómicos |
| **Resultado esperado vago** | "El usuario queda registrado" no es observable sin acceder a la BD |
| **Severidad/Prioridad incoherentes** | Critical + P4 es contradictorio (lo crítico normalmente es P1 o P2) |

## Caso A — Corregido

```
ID:              TC-REG-001
Título:          Registro exitoso con email y contraseña válidos redirige al dashboard
Precondiciones:
  - La aplicación está disponible en http://localhost:3000
  - No existe ningún usuario registrado con el email "nuevo@example.com"

Datos de prueba:
  - nombre: "Ana Gómez"
  - email: "nuevo@example.com"
  - contraseña: "Secure1234!"
  - confirmar contraseña: "Secure1234!"

Pasos:
  1. Navegar a http://localhost:3000/register
  2. Ingresar "Ana Gómez" en el campo "Nombre completo"
  3. Ingresar "nuevo@example.com" en el campo "Email"
  4. Ingresar "Secure1234!" en el campo "Contraseña"
  5. Ingresar "Secure1234!" en el campo "Confirmar contraseña"
  6. Hacer clic en el botón "Crear cuenta"

Resultado esperado:
  - El sistema redirige al usuario a http://localhost:3000/dashboard
  - La barra superior muestra "Bienvenida, Ana Gómez"
  - Se recibe un correo de confirmación en "nuevo@example.com"
    (verificable en el servidor de correo de pruebas)

Severidad: High
Prioridad:  P1
```

---

## Caso B — Análisis de defectos

| Defecto | Descripción |
|---|---|
| **Resultado esperado incompleto** | "No hay resultados" no dice qué se muestra en pantalla exactamente |
| **Precondición insuficiente** | Dice que el catálogo está disponible, pero no especifica si el término buscado realmente no existe en BD |
| **Inconsistencia severidad/prioridad** | Low + P1: si es Low, no puede ser la máxima prioridad |
| **Paso 3 ambiguo** | "Esperar resultados" no es una acción atómica — no dice qué presionar |

## Caso B — Corregido

```
ID:              TC-SEARCH-007
Título:          Buscar un término sin coincidencias muestra el mensaje "Sin resultados"
Precondiciones:
  - La aplicación está disponible en http://localhost:3000
  - El catálogo de productos NO contiene ningún producto con el término "xyzproductoquenoexiste"
  - El usuario puede ser anónimo (no requiere autenticación para buscar)

Datos de prueba:
  - término de búsqueda: "xyzproductoquenoexiste"

Pasos:
  1. Navegar a http://localhost:3000/products
  2. Hacer clic en el campo de búsqueda
  3. Ingresar "xyzproductoquenoexiste"
  4. Presionar Enter o hacer clic en el botón de búsqueda

Resultado esperado:
  - La página muestra el mensaje: "No encontramos resultados para xyzproductoquenoexiste"
  - No se muestran tarjetas de productos
  - Se muestra una sugerencia de búsqueda alternativa o un enlace al catálogo completo

Severidad: Low
Prioridad:  P3
```

---

## Caso C — Análisis de defectos

| Defecto | Descripción |
|---|---|
| **Resultado esperado vago** | "Aparece un error" no indica qué mensaje, en qué lugar de la pantalla, ni qué código HTTP |

*(El resto del caso estaba bien escrito)*

## Caso C — Corregido

```
ID:              TC-PWD-003
Título:          Cambiar contraseña con contraseña actual incorrecta debe mostrar error en el campo
Precondiciones:
  - Usuario "tester@example.com" está autenticado en la sesión activa
  - La contraseña actual del usuario es "Correct1234!"

Datos de prueba:
  - Contraseña actual: "WrongPass999"
  - Nueva contraseña: "NewPass5678!"
  - Confirmar nueva contraseña: "NewPass5678!"

Pasos:
  1. Navegar a /settings/password
  2. Ingresar "WrongPass999" en el campo "Contraseña actual"
  3. Ingresar "NewPass5678!" en el campo "Nueva contraseña"
  4. Ingresar "NewPass5678!" en el campo "Confirmar nueva contraseña"
  5. Hacer clic en el botón "Guardar cambios"

Resultado esperado:
  - El sistema NO cambia la contraseña del usuario
  - Aparece el mensaje de error "La contraseña actual es incorrecta"
    directamente debajo del campo "Contraseña actual", en color rojo
  - El campo "Nueva contraseña" y "Confirmar nueva contraseña" se vacían
  - El usuario permanece en la página /settings/password (no redirige)

Severidad: High
Prioridad:  P2
```

---

## Caso D — Análisis de defectos

| Defecto | Descripción |
|---|---|
| **Precondición faltante** | No especifica si el usuario tiene una sesión autenticada activa (dice "autenticado" pero no da el email ni cómo lograrlo) |
| **El caso en sí está bien** | Es el caso más completo de los cuatro — título, pasos, resultado esperado y datos están correctos |

## Caso D — Corregido

```
ID:              TC-CART-011
Título:          Agregar el mismo producto dos veces actualiza la cantidad en el carrito
Precondiciones:
  - Usuario "buyer@example.com" está autenticado con contraseña "Buy1234!"
  - El producto "Auriculares Pro X" (ID: prod-442) tiene stock ≥ 2 unidades
  - El carrito del usuario está vacío al inicio del test

Datos de prueba:
  - Producto: "Auriculares Pro X" (ID: prod-442, precio: $89.99)

Pasos:
  1. Navegar a /products/prod-442
  2. Hacer clic en "Agregar al carrito"
  3. Navegar a /products/prod-442
  4. Hacer clic en "Agregar al carrito"
  5. Navegar a /cart

Resultado esperado:
  - El carrito muestra 1 ítem del tipo "Auriculares Pro X"
  - La cantidad del ítem es 2
  - El subtotal muestra $179.98

Severidad: Medium
Prioridad:  P2
```
