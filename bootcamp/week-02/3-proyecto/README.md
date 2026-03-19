# Proyecto Semana 02 — Suite de Casos de Prueba

> **Entregable obligatorio** | Etapa 0 · Semana 02

---

## 🎯 Objetivo

Diseñar una suite completa de **casos de prueba manuales** para una funcionalidad del dominio asignado, aplicando las técnicas de diseño aprendidas esta semana: partición de equivalencia, análisis de valores límite (BVA) y —si aplica— tablas de decisión.

---

## 🏛️ Dominio Asignado

Cada aprendiz trabaja sobre el dominio asignado por el instructor:

| Dominio | Funcionalidad sugerida |
|---|---|
| 📖 Biblioteca | Registro/renovación de préstamo de libro |
| 💊 Farmacia | Venta de medicamento (con/sin receta, stock) |
| 🏋️ Gimnasio | Inscripción de miembro (tipo, edad, plan) |
| 🏫 Escuela | Registro de matrícula (edad, curso, requisitos) |
| 🏬 Tienda mascotas | Adopción de mascota (especie, edad, documentos) |
| 🏪 Restaurante | Reserva de mesa (personas, horario, disponibilidad) |
| 🏦 Banco | Apertura de cuenta (tipo, edad mínima, depósito) |
| 🚕 Agencia taxis | Solicitud de viaje (zona, horario, tipo de vehículo) |
| 🏥 Hospital | Registro de cita médica (especialidad, edad, seguro) |
| 🎥 Cine | Venta de entrada (película, sala, clasificación) |
| 🏨 Hotel | Reserva de habitación (fechas, tipo, capacidad) |
| ✈️ Agencia viajes | Reserva de vuelo (pasajeros, destino, equipaje) |
| 🚗 Concesionario | Registro de test drive (edad, licencia, modelo) |
| 👗 Tienda de ropa | Devolución de prenda (días, estado, comprobante) |
| 🛠️ Taller mecánico | Recepción de vehículo (tipo, problema, urgencia) |

> ⚠️ Adapta el entregable a tu dominio. Los ejemplos de este README usan un **Acuario Virtual** para no revelar soluciones.

---

## 📋 Instrucciones

### Paso 1 — Elegir la funcionalidad (15 min)

Selecciona UNA funcionalidad de tu dominio que tenga:
- Al menos un campo con rango numérico o de longitud (para BVA)
- Al menos 2 clases de equivalencia inválidas (para partición)
- Opcional: múltiples condiciones (para tabla de decisión)

Documenta la funcionalidad y sus reglas de negocio en el starter.

### Paso 2 — Partición de equivalencia (30 min)

Identifica todas las clases de equivalencia para cada campo de entrada.
- Mínimo: 2 clases válidas y 3 inválidas en total
- Un valor representativo por clase

### Paso 3 — Análisis de valores límite (30 min)

Para cada campo con rango numérico o de longitud, aplica BVA:
- Valor justo por debajo del mínimo
- Valor en el mínimo exacto (on point)
- Valor justo por encima del mínimo
- Valor justo por debajo del máximo
- Valor en el máximo exacto (on point)
- Valor justo por encima del máximo

### Paso 4 — Escribir la suite completa (45 min)

Completa el archivo `starter/casos-de-prueba.md` con todos los casos de prueba resultantes. Mínimo 10 casos, máximo 20 (sin redundancias).

### Paso 5 — Revisión final (15 min)

Verifica contra el checklist:
- [ ] Cada caso tiene ID único en formato `TC-[DOMINIO]-[MÓDULO]-[NNN]`
- [ ] Todas las precondiciones son específicas y verificables
- [ ] Los pasos son atómicos y ordenados
- [ ] Los resultados esperados son concretos y observables (sin "funciona bien")
- [ ] Se aplicó partición de equivalencia
- [ ] Se aplicó BVA en al menos un campo
- [ ] No hay casos duplicados

---

## 📐 Ejemplo de referencia — Acuario Virtual

**Funcionalidad**: Registro de visitante para tour guiado

**Reglas**:
- Nombre: 2–50 caracteres, solo letras y espacios
- Edad: 4–80 años
- Tipo de tour: "individual" o "familiar"
  - "familiar" requiere que al menos un acompañante sea mayor de 18

**Ejemplo de caso BVA:**

```
ID:              TC-AQUARIUM-TOUR-015
Título:          Visitante con edad 3 (justo debajo del mínimo) debe ser rechazado
Técnica:         BVA — off point inferior del límite mínimo (4)
Precondiciones:
  - El sistema de reservas está disponible en /reservas
  - No existen restricciones de aforo para la fecha seleccionada
Datos de prueba:
  - nombre: "María Ruiz"
  - edad: 3
  - tipo_tour: "individual"
Pasos:
  1. Navegar a /reservas/nuevo
  2. Ingresar "María Ruiz" en el campo "Nombre"
  3. Ingresar 3 en el campo "Edad"
  4. Seleccionar "Individual" en el campo "Tipo de tour"
  5. Hacer clic en "Confirmar reserva"
Resultado esperado:
  - El sistema no crea la reserva
  - Muestra el mensaje "La edad mínima para visitar el acuario es 4 años"
    debajo del campo "Edad" en color rojo
Severidad: Medium
Prioridad:  P2
```

---

## 📦 Entregable

Archivo `starter/casos-de-prueba.md` completado con:
1. Descripción de la funcionalidad y reglas de negocio
2. Tabla de clases de equivalencia
3. Tabla de valores límite (BVA)
4. Suite completa de casos de prueba (10–20 casos)
