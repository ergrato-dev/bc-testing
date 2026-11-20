# Lab 03 - Stubs, mocks y spies

## Objetivo

Arreglar un test que depende del día en que se ejecuta con un stub del reloj, encontrar con un mock un bug que los once tests de reservas no ven, y probar el notificador real con un spy que se restaura después de cada test.

## Tiempo estimado

80 minutos.

## Requisitos

- El [lab 02](../lab-02-hooks-y-estado-compartido/README.md) terminado en `~/labs-semana-07`.
- La [teoría 03](../../1-teoria/03-dobles-de-prueba-basicos.md).

## Punto de partida

```bash
cd ~/labs-semana-07
git status --short
pnpm test
# Tests:       11 passed, 11 total
```

---

## Paso 1: un test con fecha de caducidad (10 min)

Otra persona del equipo escribió unos tests de fechas en marzo de 2026. Cópialos:

```bash
cp <ruta-del-bootcamp>/bootcamp/week-07-jest_avanzado_y_organizacion_de_suites/2-practicas/lab-03-stubs-mocks-y-spies/starter/tests/booking-dates.test.js tests/
pnpm test tests/booking-dates.test.js
# FAIL tests/booking-dates.test.js
#   createBookingService dates
#     when the session is tomorrow
#       ✕ should accept the booking (2 ms)
#     when the session is today
#       ✓ should throw (2 ms)
#
#   ● createBookingService dates › when the session is tomorrow › should accept the booking
#
#     La sesión tiene que ser a partir de mañana
#
#       19 |     }
#       20 |     if (date <= clock.today()) {
#     > 21 |       throw new Error("La sesión tiene que ser a partir de mañana");
```

Abre el archivo. El 15 de marzo de 2026, `2026-03-16` era mañana y los dos tests pasaban. El código no ha cambiado; ha cambiado el día. Y el test que sigue en verde lo está por otro motivo: `2026-03-15` ya no es hoy, sino pasado.

`tests/booking.test.js` tiene el mismo problema escondido: sus sesiones son de 2099, y el test de «hoy» calcula la fecha con el mismo reloj que el servicio. Funciona, pero depende del reloj del equipo.

## Paso 2: stub del reloj (15 min)

En `tests/booking-dates.test.js`, quita la línea que importa `systemClock` y crea un reloj que siempre devuelve el 15 de marzo:

```javascript
const silentNotifier = { send() {} };
const marchClock = { today: () => "2026-03-15" };
```

Usa `marchClock` en el `beforeEach` en lugar de `systemClock`, y cambia el comentario de la primera línea para que diga que el reloj es un stub:

```bash
pnpm test tests/booking-dates.test.js
# Tests:       2 passed, 2 total
```

Ahora prueba la versión de Jest del mismo stub. Cambia la definición por esta y vuelve a ejecutar:

```javascript
const marchClock = { today: jest.fn().mockReturnValue("2026-03-15") };
```

El resultado es el mismo. Usa la que prefieras.

Haz lo mismo en `tests/booking.test.js`: añade `marchClock`, úsalo en el `beforeEach`, quita el import de `systemClock` y cambia el test de «hoy» para que use `"2026-03-15"` en lugar de `systemClock.today()`.

```bash
pnpm test
# Tests:       13 passed, 13 total
grep -rn "systemClock" tests/
```

El `grep` no muestra nada: ningún test depende ya del reloj del equipo.

## Paso 3: mock del notificador (25 min)

Ningún test comprueba los avisos: todos usan `silentNotifier`. Crea `tests/booking-notifications.test.js`:

```javascript
const { createBookingService } = require("../src/booking");

describe("createBookingService notifications", () => {
  let notifier;
  let service;

  beforeEach(() => {
    notifier = { send: jest.fn() };
    service = createBookingService({ capacity: 10, clock: { today: () => "2026-03-15" }, notifier });
  });

  describe("when the booking is valid", () => {
    it("should send one confirmation to the visitor", () => {
      service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2026-03-20" });

      expect(notifier.send).toHaveBeenCalledTimes(1);
      expect(notifier.send).toHaveBeenCalledWith("ana@example.com", "Reserva 1 confirmada: 2 butacas el 2026-03-20");
    });
  });

  describe("when the number of seats is out of range", () => {
    it("should not send anything", () => {
      expect(() => service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 7, date: "2026-03-20" })).toThrow();

      expect(notifier.send).not.toHaveBeenCalled();
    });
  });

  describe("when the session is full", () => {
    it("should not send anything to the rejected visitor", () => {
      service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 6, date: "2026-03-20" });

      expect(() => service.book({ name: "Luis Gil", email: "luis@example.com", seats: 5, date: "2026-03-20" })).toThrow("No quedan butacas suficientes");

      // Solo el aviso de Ana: el de Luis no tiene que salir.
      expect(notifier.send).toHaveBeenCalledTimes(1);
    });
  });

  describe("when a booking is cancelled", () => {
    it("should tell the visitor", () => {
      const booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2026-03-20" });

      service.cancel(booking.id);

      expect(notifier.send).toHaveBeenLastCalledWith("ana@example.com", "Reserva 1 cancelada");
    });
  });
});
```

`toThrow()` sin argumento pasa con cualquier error. Ejecuta:

```bash
pnpm test tests/booking-notifications.test.js
# ● createBookingService notifications › when the session is full › should not send anything to the rejected visitor
#
#   expect(jest.fn()).toHaveBeenCalledTimes(expected)
#
#   Expected number of calls: 1
#   Received number of calls: 2
#
# Tests:       1 failed, 3 passed, 4 total
```

¿Quién recibió el segundo aviso? Añade `console.log(notifier.send.mock.calls);` justo antes del último `expect` y vuelve a ejecutar:

```text
console.log
  [
    [
      'ana@example.com',
      'Reserva 1 confirmada: 6 butacas el 2026-03-20'
    ],
    [
      'luis@example.com',
      'Reserva 2 confirmada: 5 butacas el 2026-03-20'
    ]
  ]
```

Luis recibe la confirmación de una reserva que el servicio rechazó. El bug estaba en `src/booking.js` desde el lab 01, con todos los tests en verde: ninguno miraba los avisos.

Abre `book` en `src/booking.js` y arréglalo tú: el aviso tiene que salir solo cuando la reserva ya está guardada. Cuando lo tengas, el final de `book` queda así:

```javascript
    if (seatsTaken(date) + seats > capacity) {
      throw new Error("No quedan butacas suficientes");
    }
    const booking = { id: nextId, name, email, seats, date };
    nextId += 1;
    bookings.push(booking);
    notifier.send(email, `Reserva ${booking.id} confirmada: ${seats} butacas el ${date}`);
    return booking;
```

Quita el `console.log` del test:

```bash
pnpm test
# Tests:       17 passed, 17 total
```

## Paso 4: spy del notificador real (20 min)

`emailNotifier` (en `src/notifier.js`) es el notificador que usaría la aplicación, y no tiene tests. Escribe en la consola con `console.log`. Crea `tests/notifier.test.js`:

```javascript
const { emailNotifier } = require("../src/notifier");

describe("emailNotifier", () => {
  describe("send", () => {
    it("should write the address and the message", () => {
      const log = jest.spyOn(console, "log");

      emailNotifier.send("ana@example.com", "Reserva 1 cancelada");

      expect(log).toHaveBeenCalledWith("Correo a ana@example.com: Reserva 1 cancelada");
    });

    it("should write one line per message", () => {
      const log = jest.spyOn(console, "log");

      emailNotifier.send("luis@example.com", "Reserva 2 cancelada");

      expect(log).toHaveBeenCalledTimes(1);
    });
  });
});
```

```bash
pnpm test tests/notifier.test.js
#   console.log
#     Correo a ana@example.com: Reserva 1 cancelada
#
#       at console.<anonymous> (node_modules/.pnpm/jest-mock@30.5.2/node_modules/jest-mock/build/index.js:740:25)
#
#   console.log
#     Correo a luis@example.com: Reserva 2 cancelada
#   ...
#       ✓ should write the address and the message (14 ms)
#       ✕ should write one line per message (2 ms)
#
#   ● emailNotifier › send › should write one line per message
#
#     Expected number of calls: 1
#     Received number of calls: 2
```

Dos cosas que ver:

- Los mensajes salen en el informe: el spy anota las llamadas **y** ejecuta el `console.log` original.
- El segundo test cuenta 2 llamadas: el spy del primero sigue puesto, y `jest.spyOn` lo reutiliza.

Arregla las dos. Añade `.mockImplementation(() => {})` a cada `jest.spyOn(console, "log")`, para que no se ejecute el original, y restaura los spies después de cada test, al principio del `describe("emailNotifier")`:

```javascript
describe("emailNotifier", () => {
  afterEach(() => {
    // Devuelve a console.log su función original y borra las llamadas anotadas.
    jest.restoreAllMocks();
  });
```

```bash
pnpm test tests/notifier.test.js
#   emailNotifier
#     send
#       ✓ should write the address and the message (2 ms)
#       ✓ should write one line per message
#
# Tests:       2 passed, 2 total
```

Sin mensajes en el informe y con cada test independiente del anterior.

## Paso 5: todo junto, desordenado (5 min)

```bash
pnpm test --randomize --seed=1
# Tests:       19 passed, 19 total
pnpm test --randomize --seed=2
# Tests:       19 passed, 19 total
```

---

## Comprobación

```bash
pnpm test
# Tests:       19 passed, 19 total
grep -rn "systemClock\|\.only(" tests/
```

El `grep` no muestra nada. Guarda el lab:

```bash
git add src tests
git commit -m "lab 03: stubs, mocks y spies"
```

## Repaso

- [ ] Ningún test depende del día en que se ejecuta.
- [ ] Sabes distinguir el stub (respuesta fija, se mira el resultado) del mock (se miran las llamadas).
- [ ] Un test comprueba que un visitante rechazado no recibe aviso, y encontró un bug real.
- [ ] El spy de `console.log` no ejecuta el original y se restaura en `afterEach`.
- [ ] Las funciones falsas se crean en `beforeEach`, no en el cuerpo del `describe`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 02 - Hooks y estado compartido](../lab-02-hooks-y-estado-compartido/README.md) | [Proyecto — Proyecto - Suite modular con Jest](../../3-proyecto/README.md) → |

[↑ Semana 07](../../README.md)
