# Casos de Prueba con Defectos — Para Analizar

> Ejercicio 01 — Starter | Semana 02

Analiza cada caso y detecta sus defectos antes de pasar a corregirlos.

---

## Caso A — Módulo: Registro de usuarios

```
ID:              TC-001
Título:          Registro
Precondiciones:  (vacío)
Datos de prueba: (vacío)
Pasos:
  1. Entrar a la web
  2. Llenar el formulario
  3. Darle al botón
Resultado esperado: El usuario queda registrado
Severidad: Critical
Prioridad: P4
```

**¿Qué defectos detectas en este caso?**

_(escribe tu análisis aquí antes de ver la solución)_

---

## Caso B — Módulo: Búsqueda de productos

```
ID:              TC-SEARCH-007
Título:          Buscar un producto que no existe
Precondiciones:  El catálogo de productos está disponible
Datos de prueba: término de búsqueda: "xyzproductoquenoexiste"
Pasos:
  1. Ir a la barra de búsqueda
  2. Ingresar "xyzproductoquenoexiste"
  3. Esperar resultados
Resultado esperado: No hay resultados
Severidad: Low
Prioridad: P1
```

**¿Qué defectos detectas en este caso?**

_(escribe tu análisis aquí antes de ver la solución)_

---

## Caso C — Módulo: Cambio de contraseña

```
ID:              TC-PWD-003
Título:          Cambiar contraseña con contraseña actual incorrecta debe lanzar error
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
Resultado esperado: Aparece un error
Severidad: High
Prioridad: P2
```

**¿Qué defectos detectas en este caso?**

_(escribe tu análisis aquí antes de ver la solución)_

---

## Caso D — Módulo: Carrito de compras

```
ID:              TC-CART-011
Título:          Agregar el mismo producto dos veces actualiza la cantidad en el carrito
Precondiciones:
  - Usuario autenticado
  - El producto "Auriculares Pro X" tiene stock disponible
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
Prioridad: P2
```

**¿Qué defectos detectas en este caso?**

_(escribe tu análisis aquí antes de ver la solución)_

---

## Tu análisis — Completa aquí

Usa esta tabla para registrar tu análisis antes de ver la solución:

| Caso | Defecto identificado | Campo afectado |
|---|---|---|
| A | | |
| B | | |
| C | | |
| D | | |
