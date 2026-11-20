# Lab 01 - Describe por comportamiento

## Objetivo

Reorganizar una suite plana de diez tests en `describe` anidados por función y por contexto, ejecutar un contexto con `-t` y ver cómo un `.only` olvidado deja la suite en verde.

## Tiempo estimado

80 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-organizar-suites-por-comportamiento.md).
- Node.js 22 y pnpm, como en la [semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/01-setup-jest.md).

## Punto de partida

La carpeta `~/labs-semana-07` es el proyecto de los tres labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-07
cd ~/labs-semana-07
cp -r <ruta-del-bootcamp>/bootcamp/week-07-jest_avanzado_y_organizacion_de_suites/2-practicas/lab-01-describe-por-comportamiento/starter/. .
pnpm install
pnpm test
# Tests:       10 passed, 10 total
```

Guárdalo en git:

```bash
cat > .gitignore <<'EOF'
node_modules/
EOF
git init
git add .
git commit -m "labs semana 07: punto de partida"
```

## Contexto

Es el servicio de reservas de butacas del planetario, en `src/booking.js`. `createBookingService` recibe el aforo de cada sesión, un reloj y un notificador, y devuelve tres funciones:

| Función | Qué hace |
|---|---|
| `book({ name, email, seats, date })` | Reserva de 1 a 6 butacas para una sesión a partir de mañana, si caben. Devuelve la reserva con su `id` |
| `cancel(id)` | Anula la reserva y libera sus butacas |
| `availableSeats(date)` | Butacas libres de esa sesión |

Las fechas son textos `AAAA-MM-DD`. Los tests de `tests/booking.test.js` pasan, pero sus nombres (`book 0`, `full`, `seats`) no dicen qué comprueban.

---

## Paso 1: leer el informe (10 min)

```bash
pnpm test --verbose
# PASS tests/booking.test.js
#   booking
#     ✓ book works (2 ms)
#     ✓ book seats (1 ms)
#     ✓ book 0 (2 ms)
#     ✓ book 7 (1 ms)
#     ✓ book today (1 ms)
#     ✓ full (1 ms)
#     ✓ full other day
#     ✓ cancel
#     ✓ cancel error
#     ✓ seats
```

Sin abrir el archivo, intenta responder: ¿`book 0` acepta 0 butacas o las rechaza? ¿Qué comprueba `full other day`? No se puede. Esa es la razón de este lab.

## Paso 2: inventario (15 min)

Abre `tests/booking.test.js` y, para cada test, anota en un borrador la función que prueba, la situación y lo que espera:

| Test | Función | Situación | Espera |
|---|---|---|---|
| `book works` | `book` | Reserva válida, la primera | `id` 1 |
| `book 0` | `book` | 0 butacas | Error «Entre 1 y 6 butacas por reserva» |
| ... | | | |

Agrupa las filas con la misma función y situación. Deberían salir tres funciones y siete situaciones; `availableSeats` tiene una sola y no necesita contexto.

## Paso 3: reorganizar (30 min)

Reescribe el archivo con tres niveles: `createBookingService`, la función y el contexto `when ...`. Cada `it` empieza por `should`. Mantén la primera línea de cada test (`const service = ...`): el lab 02 se encarga de ella.

Este es el primer contexto, con las líneas en blanco del patrón AAA de la [semana 3](../../../week-03-primeros_tests_automatizados_con_javascript/1-teoria/02-estructura-test-jest.md#2-patrón-aaa):

```javascript
describe("createBookingService", () => {
  describe("book", () => {
    describe("when the booking is valid", () => {
      it("should return the booking with id 1 when it is the first one", () => {
        const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });

        const booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2099-05-10" });

        expect(booking.id).toBe(1);
      });

      it("should take the seats from that session", () => {
        // ...
      });
    });
  });
});
```

Escribe tú los demás. Al terminar, el informe tiene que quedar así:

```bash
pnpm test --verbose
# PASS tests/booking.test.js
#   createBookingService
#     book
#       when the booking is valid
#         ✓ should return the booking with id 1 when it is the first one (2 ms)
#         ✓ should take the seats from that session
#         ✓ should not take seats from other sessions (1 ms)
#       when the number of seats is out of range
#         ✓ should throw when seats is 0 (3 ms)
#         ✓ should throw when seats is 7
#       when the session is not after today
#         ✓ should throw when the date is today (1 ms)
#       when the session is full
#         ✓ should throw when the seats do not fit (1 ms)
#     cancel
#       when the booking exists
#         ✓ should free its seats
#       when the booking does not exist
#         ✓ should throw (1 ms)
#     availableSeats
#       ✓ should return the capacity when there are no bookings
#
# Test Suites: 1 passed, 1 total
# Tests:       10 passed, 10 total
```

`full other day` ya no es un test de «sesión llena»: comprueba que reservar una sesión no quita butacas de otra, así que va en `when the booking is valid`. Ordenar la suite te obliga a decidir qué prueba cada test.

Si un test falla después de moverlo, revisa los paréntesis y las llaves: cada `describe(..., () => {` se cierra con `});`.

## Paso 4: ejecutar un contexto (10 min)

```bash
pnpm test -t "book when the session"
# Tests:       8 skipped, 2 passed, 10 total
```

Se ejecutan los dos contextos de `book` que empiezan por `when the session`. Prueba también:

```bash
pnpm test -t "cancel"
# Tests:       8 skipped, 2 passed, 10 total
pnpm test -t "should throw"
# Tests:       5 skipped, 5 passed, 10 total
```

El tercero encuentra los cinco tests de error, estén en la función que estén.

## Paso 5: el `.only` olvidado (10 min)

Cambia `it("should throw when seats is 7"` por `it.only("should throw when seats is 7"`, y ejecuta la suite:

```bash
pnpm test
# Tests:       9 skipped, 1 passed, 10 total
echo $?
# 0
```

Verde y con código 0, con nueve tests sin ejecutar. Búscalo como lo harías antes de un commit:

```bash
grep -rn "\.only(\|\.skip(" tests/
# tests/booking.test.js:42:      it.only("should throw when seats is 7", () => {
```

Vuelve a dejar `it(` y comprueba que `grep` ya no muestra nada.

---

## Comprobación

```bash
pnpm test
# Tests:       10 passed, 10 total
grep -c "describe(\"when " tests/booking.test.js
# 6
grep -rn "\.only(\|\.skip(" tests/
```

El último no muestra nada. Guarda el lab:

```bash
git add tests/booking.test.js
git commit -m "lab 01: describe por comportamiento"
```

## Repaso

- [ ] El informe de `--verbose` se lee como una lista de reglas, sin abrir el código.
- [ ] Cada contexto empieza por `when` y cada test por `should`.
- [ ] Sabes ejecutar un contexto con `-t` y por qué el nombre completo lo hace posible.
- [ ] Sabes por qué un `.only` olvidado no rompe la suite y cómo encontrarlo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Hooks y estado compartido](../lab-02-hooks-y-estado-compartido/README.md) → |

[↑ Semana 07](../../README.md)
