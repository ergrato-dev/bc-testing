# Revisión de casos heredados

Cinco casos de prueba de la tienda online. Para cada uno, rellena **Problemas encontrados** y escribe el caso corregido debajo.

Datos de la tienda que puedes usar:

- URL: `http://localhost:3000`
- Usuario de pruebas: `tester@example.com`, contraseña `Test1234!`, estado activo
- Producto `prod-442` «Auriculares Pro X», 89,99 €, stock 5
- Registro: nombre de 2 a 50 letras, edad de 18 a 99, email con formato válido y no repetido

---

## Caso A - Registro

```text
ID:                 TC-001
Título:             Registro
Precondiciones:     (ninguna)
Datos de prueba:    (ninguno)
Pasos:
  1. Entrar a la web
  2. Llenar el formulario
  3. Darle al botón
Resultado esperado: El usuario queda registrado
Prioridad:          Baja
```

### Problemas encontrados

(completar: un problema por línea, con el campo afectado)

### Caso corregido

```text
(completar)
```

---

## Caso B - Búsqueda

```text
ID:                 TC-SEARCH-007
Título:             Buscar un producto que no existe
Precondiciones:     El catálogo está disponible
Datos de prueba:    término "xyzproductoquenoexiste"
Pasos:
  1. Ir a la barra de búsqueda
  2. Ingresar "xyzproductoquenoexiste"
  3. Esperar resultados
Resultado esperado: No hay resultados
Prioridad:          Alta
```

### Problemas encontrados

(completar)

### Caso corregido

```text
(completar)
```

---

## Caso C - Cambio de contraseña

```text
ID:                 TC-PWD-003
Título:             Cambiar la contraseña con la actual incorrecta muestra un error
Precondiciones:
  - Sesión iniciada como tester@example.com
  - La contraseña actual es Test1234!
Datos de prueba:
  - Contraseña actual: WrongPass999
  - Nueva contraseña: NewPass5678
  - Confirmación: NewPass5678
Pasos:
  1. Abre http://localhost:3000/settings/password
  2. Escribe WrongPass999 en "Contraseña actual"
  3. Escribe NewPass5678 en "Nueva contraseña"
  4. Escribe NewPass5678 en "Confirmar contraseña"
  5. Pulsa "Guardar cambios"
Resultado esperado: Aparece un error
Prioridad:          Alta
```

### Problemas encontrados

(completar)

### Caso corregido

```text
(completar)
```

---

## Caso D - Carrito

```text
ID:                 TC-CART-011
Título:             Añadir dos veces el mismo producto suma la cantidad
Precondiciones:
  - Sesión iniciada como tester@example.com
  - El producto prod-442 tiene stock 5
Datos de prueba:
  - Producto prod-442, «Auriculares Pro X», 89,99 €
Pasos:
  1. Abre http://localhost:3000/products/prod-442
  2. Pulsa "Añadir al carrito"
  3. Pulsa "Añadir al carrito" otra vez
  4. Abre http://localhost:3000/cart
Resultado esperado:
  - El carrito tiene una sola línea, «Auriculares Pro X»
  - La cantidad de esa línea es 2
  - El subtotal es 179,98 €
Prioridad:          Media
```

### Problemas encontrados

(completar: pista, ¿qué pasa si el carrito de tester@example.com ya tenía algo?)

### Caso corregido

```text
(completar)
```

---

## Caso E - Registro con datos inválidos

```text
ID:                 TC-REG-020
Título:             El registro rechaza datos inválidos
Precondiciones:     No existe ningún usuario con el email ana.example.com
Datos de prueba:
  - Nombre: Ana Ruiz
  - Edad: 15
  - Email: ana.example.com
Pasos:
  1. Abre http://localhost:3000/register
  2. Escribe Ana Ruiz en "Nombre"
  3. Escribe 15 en "Edad"
  4. Escribe ana.example.com en "Email"
  5. Pulsa "Crear cuenta"
Resultado esperado:
  - No se crea la cuenta
  - Aparece un mensaje de error
Prioridad:          Media
```

### Problemas encontrados

(completar: pista, si el sistema rechaza el formulario, ¿qué regla ha funcionado?)

### Caso corregido

```text
(completar: este caso se parte en dos casos)
```

---

## Resumen

| Caso | Problema principal | Campo afectado |
|---|---|---|
| A | (completar) | (completar) |
| B | (completar) | (completar) |
| C | (completar) | (completar) |
| D | (completar) | (completar) |
| E | (completar) | (completar) |
