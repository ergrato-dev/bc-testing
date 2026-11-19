# Triage del lunes - Lab 04

Siete reportes nuevos (`New`) de la tienda online, versión 3.2.0. Para cada uno, decide el estado al que pasa y justifícalo. Si pasa a `Open`, asígnale severidad y prioridad.

Estados posibles al revisar: `Open`, `Duplicate`, `Not a Bug`, `Won't Fix`, `Cannot Reproduce`.

## Requisitos de la tienda que necesitas

- **R-07:** los importes se redondean a dos decimales con redondeo al par (redondeo bancario): 2,345 € pasa a 2,34 € y 2,355 € pasa a 2,36 €.
- **R-12:** navegadores soportados: Chrome, Firefox, Safari y Edge en sus versiones actuales. Internet Explorer no está soportado desde la versión 3.0.0.
- **R-19:** el carrito guarda los productos 30 días aunque cierres la sesión.

## Reportes

### BUG-201 - [Carrito] Los productos desaparecen del carrito al cerrar sesión

1. Inicia sesión y añade 2 productos al carrito. 2. Cierra sesión. 3. Vuelve a iniciar sesión.
- **Obtenido:** el carrito está vacío.
- **Esperado:** los 2 productos siguen en el carrito.
- **Entorno:** Chrome 131, Windows 11, v3.2.0. Reproducible 5 de 5 veces.

### BUG-202 - [Factura] El IVA de 2,345 € sale como 2,34 € y debería ser 2,35 €

1. Compra un producto cuyo IVA sea exactamente 2,345 €. 2. Abre la factura.
- **Obtenido:** IVA 2,34 €.
- **Esperado:** IVA 2,35 €, «como se redondea siempre».
- **Entorno:** Firefox 133, Ubuntu 24.04, v3.2.0.

### BUG-203 - [Checkout] Error 500 al pagar con un cupón caducado

1. Añade un producto. 2. Aplica el cupón `VERANO24`, caducado el 30 de septiembre. 3. Pulsa «Pagar».
- **Obtenido:** página «Error 500 - Internal Server Error». No se cobra nada.
- **Esperado:** el mensaje «Este cupón ha caducado» y el pago sin descuento.
- **Entorno:** Safari 18, macOS 15, v3.2.0. Reproducible siempre.

### BUG-204 - [Carrito] Cesta vacía después de salir de la cuenta

Cuando salgo de mi cuenta y vuelvo a entrar, la cesta está vacía. Antes no pasaba.
- **Entorno:** Edge 131, Windows 10, v3.2.0.

### BUG-205 - [Catálogo] Las fotos de los productos no cargan en Internet Explorer 11

1. Abre la tienda en Internet Explorer 11. 2. Entra en cualquier producto.
- **Obtenido:** recuadros vacíos en lugar de fotos.
- **Esperado:** las fotos.
- **Entorno:** Internet Explorer 11, Windows 8.1.

### BUG-206 - [Búsqueda] La tienda se queda colgada

A veces busco y se queda colgada.
- **Entorno:** no indicado.

### BUG-207 - [Checkout] El botón «Pagar» se puede pulsar dos veces y cobra dos pedidos

1. Añade un producto. 2. Pulsa «Pagar» dos veces muy rápido.
- **Obtenido:** dos pedidos idénticos y dos cargos en la tarjeta.
- **Esperado:** un pedido y un cargo; el botón se desactiva tras el primer clic.
- **Entorno:** Chrome 131, Android 15, v3.2.0. Reproducible 3 de 5 veces.

## Parte A - Decisión de cada reporte

| Reporte | Nuevo estado | Severidad | Prioridad | Justificación |
|---|---|---|---|---|
| BUG-201 | TODO | TODO | TODO | TODO |
| BUG-202 | TODO | TODO | TODO | TODO |
| BUG-203 | TODO | TODO | TODO | TODO |
| BUG-204 | TODO | TODO | TODO | TODO |
| BUG-205 | TODO | TODO | TODO | TODO |
| BUG-206 | TODO | TODO | TODO | TODO |
| BUG-207 | TODO | TODO | TODO | TODO |

Para los que no pasan a `Open`, escribe `—` en severidad y prioridad.

## Parte B - Orden de trabajo

Ordena los reportes que quedan en `Open`, del primero que hay que corregir al último:

1. TODO

## Parte C - La historia de BUG-207

Estos son los hechos, en orden. Escribe al lado el estado en que queda el bug después de cada uno.

| # | Hecho | Estado |
|---|---|---|
| 1 | Lo reportas el lunes | New |
| 2 | El QA lead lo revisa en el triage y lo confirma | TODO |
| 3 | Marta, desarrolladora, se lo asigna y empieza a trabajar | TODO |
| 4 | Marta sube la corrección: el botón se desactiva tras el primer clic | TODO |
| 5 | Lo pruebas en Chrome de escritorio: un solo pedido | TODO |
| 6 | Lo pruebas en Android pulsando muy rápido: vuelven a salir dos pedidos | TODO |
| 7 | Marta añade una comprobación en el servidor que rechaza un segundo pago del mismo carrito | TODO |
| 8 | Marta sube la segunda corrección | TODO |
| 9 | Lo pruebas en Android y en escritorio, 10 veces cada uno: un pedido siempre | TODO |
| 10 | El QA lead lo cierra en la revisión del viernes | TODO |

¿Qué faltaba en la primera prueba, la del hecho 5? TODO
