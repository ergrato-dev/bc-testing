# Lab 02 - Hooks y estado compartido

## Objetivo

Quitar la preparación repetida de la suite del lab 01, primero de la forma que rompe el aislamiento y después con `beforeEach`, y comprobar con un experimento el orden en que Jest ejecuta los hooks.

## Tiempo estimado

80 minutos.

## Requisitos

- El [lab 01](../lab-01-describe-por-comportamiento/README.md) terminado en `~/labs-semana-07`.
- La [teoría 02](../../1-teoria/02-hooks-y-aislamiento.md).

## Punto de partida

```bash
cd ~/labs-semana-07
git status --short
pnpm test
# Tests:       10 passed, 10 total
```

`git status --short` no muestra nada: todo está guardado. Si algo sale mal en un paso, `git checkout tests/booking.test.js` devuelve el archivo al último commit.

---

## Paso 1: el primer intento (15 min)

Los diez tests empiezan con la misma línea. Bórrala de los diez y escríbela una sola vez, en el cuerpo del `describe` exterior:

```javascript
describe("createBookingService", () => {
  const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });

  describe("book", () => {
```

Antes de ejecutar, apunta qué crees que pasará. Después:

```bash
pnpm test
# ✕ should take the seats from that session (2 ms)
# ✕ should throw when the seats do not fit
# ✕ should free its seats
# ✕ should return the capacity when there are no bookings (1 ms)
# ...
# Tests:       4 failed, 6 passed, 10 total
```

Lee el primer fallo:

```text
● createBookingService › book › when the booking is valid › should take the seats from that session

  expect(received).toBe(expected) // Object.is equality

  Expected: 8
  Received: 6
```

Haz las cuentas: ¿qué test reservó las otras 2 butacas? Ahora ejecuta ese test solo:

```bash
pnpm test -t "should take the seats"
# Tests:       9 skipped, 1 passed, 10 total
```

Pasa solo y falla en grupo. Los diez tests comparten un único servicio, y cada reserva se queda para los siguientes.

## Paso 2: desordenar (10 min)

En el orden escrito, `should return the booking with id 1` pasa porque es el primero en reservar. Desordena la suite:

```bash
pnpm test --randomize --seed=1
# Seed:        1
# Tests:       5 failed, 5 passed, 10 total
pnpm test --randomize --seed=4
# Seed:        4
# Tests:       4 failed, 6 passed, 10 total
```

Busca en la salida de la semilla 1 el test del id 1: ahora falla. Ejecuta `pnpm test --randomize` sin semilla dos o tres veces y mira la línea `Seed:`; con ese número puedes repetir el mismo desorden.

## Paso 3: `beforeEach` (15 min)

Cambia la línea del paso 1 por una variable y un hook:

```javascript
describe("createBookingService", () => {
  let service;

  beforeEach(() => {
    service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
  });
```

```bash
pnpm test
# Tests:       10 passed, 10 total
pnpm test --randomize --seed=1
# Tests:       10 passed, 10 total
```

Prueba también con las semillas 2 y 3. Después, cambia `beforeEach` por `beforeAll`:

```bash
pnpm test
# Tests:       4 failed, 6 passed, 10 total
```

Es el paso 1 otra vez: un único servicio para todos. Vuelve a `beforeEach`.

## Paso 4: un hook dentro de un contexto (15 min)

Un hook vale para su `describe` y los que tiene dentro. Los tests de `when the booking exists` necesitan una reserva hecha. Añade otro test a ese contexto y pon la reserva en un `beforeEach` propio:

```javascript
describe("when the booking exists", () => {
  let booking;

  beforeEach(() => {
    booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2099-05-10" });
  });

  it("should free its seats", () => {
    service.cancel(booking.id);

    expect(service.availableSeats("2099-05-10")).toBe(10);
  });

  it("should throw when it is cancelled twice", () => {
    service.cancel(booking.id);

    expect(() => service.cancel(booking.id)).toThrow("La reserva no existe");
  });
});
```

```bash
pnpm test --randomize --seed=1
# Tests:       11 passed, 11 total
```

Este `beforeEach` usa `service`, que crea el del `describe` exterior. Funciona porque Jest ejecuta los `beforeEach` de fuera hacia dentro. El paso siguiente lo comprueba.

## Paso 5: el orden de los hooks (20 min)

Copia el experimento de este lab:

```bash
cp <ruta-del-bootcamp>/bootcamp/week-07-jest_avanzado_y_organizacion_de_suites/2-practicas/lab-02-hooks-y-estado-compartido/starter/tests/hooks-order.test.js tests/
```

Ábrelo. Cada hook, cada test y el cuerpo de cada `describe` escriben una línea. Antes de ejecutarlo, escribe en papel el orden en que crees que saldrán las doce líneas. Después:

```bash
pnpm test tests/hooks-order.test.js 2>/dev/null
# outer: cuerpo del describe
# inner: cuerpo del describe
# outer: beforeAll
# outer: beforeEach
#     test first
# outer: afterEach
# outer: beforeEach
# inner: beforeEach
#     test second
# inner: afterEach
# outer: afterEach
# outer: afterAll
```

`2>/dev/null` descarta la salida de errores, que es donde Jest escribe su informe; así solo ves las líneas del experimento. Compara con tu papel:

- Los cuerpos de los `describe` salen antes que todo: Jest los ejecuta al leer el archivo. Por eso el servicio del paso 1 se creaba una vez.
- `beforeEach` va de fuera hacia dentro y `afterEach` de dentro hacia fuera.
- `beforeAll` y `afterAll` salen una vez.

Una curiosidad del experimento: `log` es una función con llaves. Cámbiala por una función flecha sin llaves, `` const log = (text) => process.stdout.write(`${text}\n`); ``, y vuelve a ejecutar:

```text
● outer › first

  test functions can only return Promise or undefined.
  Returned value: true
```

Una flecha sin llaves devuelve lo que vale su expresión, y `process.stdout.write` devuelve `true`. Jest acepta que un test devuelva una promesa (lo verás en la [semana 9](../../../week-09-testing_asincrono_con_jest/README.md)) o nada, así que rechaza el `true`. Deja `log` como estaba.

El experimento no prueba nada del planetario. Bórralo:

```bash
rm tests/hooks-order.test.js
```

---

## Comprobación

```bash
pnpm test --randomize --seed=3
# Tests:       11 passed, 11 total
grep -c "createBookingService({" tests/booking.test.js
# 1
```

La preparación está escrita una vez. Guarda el lab:

```bash
git add tests/booking.test.js
git commit -m "lab 02: beforeEach y aislamiento"
```

## Repaso

- [ ] Sabes reconocer un test que pasa solo y falla en grupo, y su causa.
- [ ] La suite pasa con `--randomize` y cualquier semilla.
- [ ] Sabes por qué la preparación va en `beforeEach` y no en el cuerpo del `describe` ni en `beforeAll`.
- [ ] Sabes en qué orden se ejecutan los `beforeEach` y los `afterEach` de dos `describe` anidados.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Describe por comportamiento](../lab-01-describe-por-comportamiento/README.md) | [Lab 03 - Stubs, mocks y spies](../lab-03-stubs-mocks-y-spies/README.md) → |

[↑ Semana 07](../../README.md)
