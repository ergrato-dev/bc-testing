# Proyecto Semana 01 — Reporte de Bug Profesional

**Tiempo estimado**: 2 horas (trabajo autónomo)
**Entregable**: `starter/reporte-bug.md` completado
**Tipo de evaluación**: Producto 📦 (30% de la nota)

---

## Descripción

En equipos de desarrollo reales, un buen reporte de bug es la diferencia entre una corrección rápida y semanas de tiempo perdido investigando un problema mal descrito. Esta semana pondrás en práctica los conceptos del ciclo de vida del bug y los campos de un reporte profesional.

**Tu tarea**: Encontrar un bug real en cualquier software que uses habitualmente y documentarlo con el nivel de calidad que se espera en un equipo profesional.

---

## Dominio asignado

Cada aprendiz trabaja en el dominio que le asignó el instructor:

| Dominio | Objeto principal | Ejemplo de funcionalidad a probar |
| --- | --- | --- |
| 📖 Biblioteca | Libro / ejemplar | Búsqueda de catálogo, préstamo, devolución |
| 💊 Farmacia | Medicamento | Registro de stock, venta, caducidad |
| 🏋️ Gimnasio | Membresía / clase | Inscripción, cancelación, reserva de clase |
| 🏫 Escuela | Alumno / materia | Inscripción, calificaciones, asistencia |
| 🏬 Tienda mascotas | Producto / mascota | Inventario, ventas, registro de clientes |
| 🏪 Restaurante | Pedido / mesa | Orden, pago, reserva de mesa |
| 🏦 Banco | Cuenta / transacción | Depósito, retiro, transferencia |
| 🚕 Agencia taxis | Viaje / conductor | Solicitud, asignación, tarifa |
| 🏥 Hospital | Paciente / cita | Agendamiento, diagnóstico, historial |
| 🎥 Cine | Pelicula / función | Cartelera, compra de boleto, sala |
| 🏨 Hotel | Reserva / habitación | Check-in, check-out, disponibilidad |
| ✈️ Agencia viajes | Vuelo / paquete | Búsqueda, reserva, cancelación |
| 🚗 Concesionario | Vehículo / venta | Inventario, prueba de manejo, financiamiento |
| 👗 Tienda ropa | Prenda / talla | Catálogo, carrito, talla y stock |
| 🛠️ Taller mecánico | Orden de trabajo / vehículo | Recepción, diagnóstico, entrega |

---

## Instrucciones

### Paso 1 — Elige el software (15 min)

Usa cualquier aplicación web, móvil o de escritorio que conozcas y que sea relevante para tu dominio. Puede ser:

- Una app real de tu dominio (ej: la app de tu banco, el catálogo de tu biblioteca local)
- Un software de gestión open source relacionado
- La interfaz web de un servicio conocido

> No inventes el bug. Encuentra uno real. Si no encuentras uno en el software de tu dominio, usa cualquier software que uses frecuentemente — lo importante es aprender a documentar.

### Paso 2 — Reproduce el bug (30 min)

Antes de escribir el reporte:
- [ ] Reproduce el bug al menos **3 veces** para confirmar que es consistente
- [ ] Toma nota de los pasos exactos que lo causan
- [ ] Anota el entorno: sistema operativo, navegador/app, versión
- [ ] Captura evidencia: screenshot, video, o texto de error

### Paso 3 — Escribe el reporte (45 min)

Abre `starter/reporte-bug.md` y completa todos los campos.

Aplica lo aprendido esta semana:
- Título con formato `[Componente] Descripción del comportamiento`
- Pasos numerados y reproducibles
- Resultado obtenido vs esperado claramente diferenciados
- Severidad y prioridad con justificación explícita

### Paso 4 — Revisa antes de entregar (15 min)

Usa el checklist de la sección siguiente.

---

## Checklist de calidad

Antes de entregar, verifica:

- [ ] **Título** — sigue el formato `[Componente] Descripción` y describe el problema sin ambigüedad
- [ ] **Pasos** — una persona que no vio el bug puede reproducirlo siguiendo tus pasos
- [ ] **Resultado obtenido** — describe exactamente lo que ocurre, no lo que crees que causa el problema
- [ ] **Resultado esperado** — describe el comportamiento correcto, no solo "que funcione"
- [ ] **Severidad** — está justificada con impacto concreto
- [ ] **Prioridad** — está justificada con el impacto en los usuarios o el negocio
- [ ] **Entorno** — incluye SO, versión de la app/navegador y dispositivo
- [ ] **Originalidad** — el bug y el reporte son tuyos, no de un compañero

---

## Ejemplo de referencia (dominio: Planetario Virtual)

Para que entiendas el nivel esperado, aquí hay un ejemplo del **estilo** del reporte — NO del contenido que debes entregar (tu reporte debe ser de tu dominio asignado):

> **Título**: `[Búsqueda de exhibiciones] Los filtros de fecha no aplican al catálogo al hacer clic en "Buscar"`
>
> **Resultado obtenido**: Al seleccionar un rango de fechas en el filtro y hacer clic en "Buscar", el catálogo muestra todas las exhibiciones sin filtrar por fecha.
>
> **Resultado esperado**: El catálogo debe mostrar solo las exhibiciones cuyas fechas de función caen dentro del rango seleccionado.
>
> **Severidad**: Medium — la búsqueda principal funciona, pero el filtrado por fecha está roto.
>
> **Prioridad**: P2 — afecta la experiencia del usuario pero no bloquea el acceso al catálogo.

---

## Entrega

Sube tu archivo `starter/reporte-bug.md` completado al repositorio de tu dominio asignado (o el canal indicado por el instructor).

> La carpeta `solution/` contiene un reporte de referencia del instructor. Solo está disponible después de la fecha de entrega.
