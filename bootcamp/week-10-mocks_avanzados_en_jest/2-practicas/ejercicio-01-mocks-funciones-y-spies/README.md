# Ejercicio 01 - Mocks de Funciones y Spies

## Objetivo

Profundizar en `jest.fn()` y `jest.spyOn()` sobre un servicio de venta de entradas de Planetario: implementaciones falsas, orden de llamadas, spies que observan a la unidad bajo prueba y limpieza de mocks.

## Tiempo estimado

90 minutos.

## Paso a paso

Abre `starter/ticket.service.test.js` y revisa `starter/ticket.service.js`. `sellTickets` reserva asientos con `seatApi`, imprime una entrada por asiento con `ticketPrinter` y calcula el total con `pricing.basePrice`.

### Paso 1: Dobles con `mockImplementation`

Descomenta el PASO 1. `seatApi.reserve` usa `mockImplementation` para devolver tantos asientos como entradas se piden, y `afterEach` restaura los spies con `jest.restoreAllMocks()`.

### Paso 2: Sobrescribir una sola llamada con `mockImplementationOnce`

Descomenta el PASO 2. La primera llamada falla con `sold out` y la segunda vuelve a la implementación por defecto.

### Paso 3: Orden de llamadas con `toHaveBeenNthCalledWith`

Descomenta el PASO 3 y verifica que las entradas se imprimen en el orden de los asientos.

### Paso 4: Spy sobre la unidad bajo prueba

Descomenta el PASO 4. El spy sobre `pricing.basePrice` se verifica después de llamar a `sellTickets`, no llamando al método desde el test (eso solo comprobaría el propio test). El segundo test sobrescribe el valor con `mockReturnValue`.

### Paso 5: `mockClear` vs `mockReset` vs `mockRestore`

Descomenta el PASO 5 y observa que conserva o borra cada método:

| Método | Historial de llamadas | Implementación falsa | Método original |
|---|---|---|---|
| `mockClear()` | Se borra | Se conserva | No |
| `mockReset()` | Se borra | Se borra (devuelve `undefined`) | No |
| `mockRestore()` | Se borra | Se borra | Se restaura (solo spies) |

### Paso 6: Revisar solución

Compara con `solution/ticket.service.test.js` y analiza diferencias.

## Comando sugerido

```bash
pnpm install
pnpm test ticket.service.test.js
```
