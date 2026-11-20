# Lab 01 - Tests que esperan

## Objetivo

Encontrar los tests de un servicio asíncrono que terminan antes que la promesa que comprueban, hacer que la esperen y, con ellos, encontrar un `await` olvidado en el código.

## Tiempo estimado

80 minutos.

## Requisitos

- La [teoría 01](../../1-teoria/01-esperar-a-las-promesas.md).
- El [lab 01 de la semana 7](../../../week-07-jest_avanzado_y_organizacion_de_suites/2-practicas/lab-01-describe-por-comportamiento/README.md): el servicio de reservas de butacas del planetario.

## Punto de partida

La carpeta `~/labs-semana-09` es el proyecto de los tres labs. Créala y copia el código de este lab:

```bash
mkdir ~/labs-semana-09
cd ~/labs-semana-09
cp -r <ruta-del-bootcamp>/bootcamp/week-09-testing_asincrono_con_jest/2-practicas/lab-01-tests-que-esperan/starter/. .
pnpm install
```

Guárdalo en git:

```bash
cat > .gitignore <<'EOF'
node_modules/
EOF
git init
git add .
git commit -m "labs semana 09: punto de partida"
```

## Contexto

Es el servicio de reservas de la semana 7 con un cambio: las butacas ocupadas las lleva la **taquilla**, un servicio aparte, y cada pregunta a la taquilla devuelve una promesa. Por eso `book` y `availableSeats` son `async`.

| Archivo | Qué tiene |
|---|---|
| `src/booking.js` | `createBookingService({ capacity, boxOffice })` con `book` y `availableSeats` |
| `tests/fake-box-office.js` | Una taquilla falsa en memoria (un *fake*, como en la semana 7): responde con promesas que tardan 10 ms, como la real pero sin red. No acaba en `.test.js`, así que Jest no la ejecuta como tests |
| `tests/booking.test.js` | Cinco tests del servicio |

Las reglas: de 1 a 6 butacas por reserva («Entre 1 y 6 butacas por reserva»), y una reserva que no cabe en la sesión se rechaza («No quedan butacas suficientes»). Los tests usan una sesión de 10 butacas.

---

## Paso 1: lee el informe hasta el final (10 min)

```bash
pnpm test
# PASS tests/booking.test.js
#   createBookingService
#     book
#       when there are seats left
#         ✓ should return the booking with its id (16 ms)
#         ✓ should take the seats from the session (1 ms)
#       when the number of seats is not allowed
#         ✓ should reject 0 seats (2 ms)
#         ✓ should reject 7 seats (1 ms)
#       when the session is full
#         ✓ should reject a booking that does not fit (11 ms)
#
# Test Suites: 1 passed, 1 total
# Tests:       5 passed, 5 total
# Snapshots:   0 total
# Time:        0.323 s
# Ran all test suites.
# /home/tu-usuario/labs-semana-09/tests/booking.test.js:31
#         service.book(ANA).then(() => service.availableSeats(DATE)).then(available => expect(available).toBe(8));
#                                                                                                        ^
#
# JestAssertionError: expect(received).toBe(expected) // Object.is equality
#
# Expected: 8
# Received: 4
# ...
# [ELIFECYCLE] Test failed. See above for more details.
echo $?
# 1
```

`echo $?` muestra el código de salida del último comando: 0 si terminó bien, otro número si no.

Cinco tests en verde, y la ejecución termina con error. Responde en un borrador antes de seguir:

1. ¿Qué test tiene ese `expect`? El error no lo dice: búscalo por el código que muestra.
2. ¿Por qué falla después de `Ran all test suites.` y no dentro del informe?
3. ¿De dónde sale el 4, si ese test reserva 2 butacas en una sesión de 10?

Las respuestas están en la [sección 2 de la teoría 01](../../1-teoria/01-esperar-a-las-promesas.md#2-el-test-no-espera-solo).

## Paso 2: el test con `.then` (15 min)

Reescribe `should take the seats from the session` con `async` y `await`:

```javascript
      it("should take the seats from the session", async () => {
        await service.book(ANA);

        const available = await service.availableSeats(DATE);

        expect(available).toBe(8);
      });
```

```bash
pnpm test
# PASS tests/booking.test.js
# ...
#         ✓ should take the seats from the session (21 ms)
# ...
# Tests:       5 passed, 5 total
# ...
# Ran all test suites.
# /home/tu-usuario/labs-semana-09/node_modules/.pnpm/expect@30.5.2/node_modules/expect/build/index.js:2116
#   const err = new JestAssertionError();
#               ^
#
# JestAssertionError: expect(received).rejects.toThrow()
#
# Received promise resolved instead of rejected
# Resolved to value: {"date": "2026-12-05", "id": 2, "name": "Luis Gil", "seats": 5}
# ...
```

El test ya espera, y tarda 21 ms en lugar de 1: lo que tardan las dos respuestas de la taquilla. El 8 era correcto; el 4 era de otro test.

Sale otro error suelto. Este no trae ninguna línea de tus tests, pero sí el valor: una reserva de Luis Gil, de 5 butacas, que se **resolvió** cuando un test esperaba que se rechazara.

## Paso 3: los `.rejects` sin `await` (15 min)

Busca los `expect` de promesas que nadie espera:

```bash
grep -nE "^ *expect[(].*[.](rejects|resolves)" tests/booking.test.js
# 36:        expect(service.book({ ...ANA, seats: 7 })).rejects.toThrow("Entre 1 y 6 butacas por reserva");
# 44:        expect(service.book({ name: "Luis Gil", seats: 5, date: DATE })).rejects.toThrow(
```

`grep -n` muestra el número de cada línea encontrada. Son las de `should reject 7 seats` y `should reject a booking that does not fit`. Pon `await` delante de cada `expect` y `async` en su función, como en `should reject 0 seats`:

```javascript
      it("should reject 7 seats", async () => {
        await expect(service.book({ ...ANA, seats: 7 })).rejects.toThrow("Entre 1 y 6 butacas por reserva");
      });
```

```bash
pnpm test
# FAIL tests/booking.test.js
# ...
#       when the session is full
#         ✕ should reject a booking that does not fit (22 ms)
#
#   ● createBookingService › book › when the session is full › should reject a booking that does not fit
#
#     expect(received).rejects.toThrow()
#
#     Received promise resolved instead of rejected
#     Resolved to value: {"date": "2026-12-05", "id": 2, "name": "Luis Gil", "seats": 5}
#
#       43 |         await service.book({ name: "Ana Ruiz", seats: 6, date: DATE });
#       44 |
#     > 45 |         await expect(service.book({ name: "Luis Gil", seats: 5, date: DATE })).rejects.toThrow(
#          |               ^
# ...
# Tests:       1 failed, 4 passed, 5 total
```

El mismo error, ahora dentro del informe, con el nombre del test y su línea. `should reject 7 seats` sigue en verde: su `expect` se cumplía, solo que nadie lo esperaba.

El test está bien: 6 butacas más 5 no caben en 10. El bug está en el código.

## Paso 4: el `await` olvidado en el código (15 min)

Abre `src/booking.js` y busca dónde `book` compara con `capacity`. Para ver qué vale `taken`, añade una línea justo después de calcularlo:

```javascript
    const taken = boxOffice.seatsTaken(date);
    console.log(taken, taken + seats);
```

```bash
pnpm test -t "does not fit" 2>&1 | grep -A1 "console.log"
#   console.log
#     Promise { <pending> } [object Promise]6
# --
#   console.log
#     Promise { <pending> } [object Promise]5
```

`-t "texto"` ejecuta solo los tests cuyo nombre contiene ese texto, y `grep -A1` muestra cada línea encontrada y la siguiente, con `--` entre un grupo y otro. Explica la salida con la [sección 4 de la teoría 01](../../1-teoria/01-esperar-a-las-promesas.md#4-el-await-olvidado-en-el-código). Quita el `console.log` y arregla la línea:

```javascript
    const taken = await boxOffice.seatsTaken(date);
```

```bash
pnpm test
# Tests:       5 passed, 5 total
echo $?
# 0
```

## Paso 5: `.resolves` (15 min)

Añade un test al contexto `when the session is full`: dos reservas que llenan la sesión justo, sin pasarse, y que dejan 0 butacas libres. Compruébalo con `.resolves`:

```javascript
      it("should accept a booking that fills the last seats", async () => {
        await service.book({ name: "Ana Ruiz", seats: 6, date: DATE });

        await service.book({ name: "Luis Gil", seats: 4, date: DATE });

        await expect(service.availableSeats(DATE)).resolves.toBe(0);
      });
```

Es el límite del caso anterior: 6 + 4 caben, 6 + 5 no. Ahora, como prueba, quita el `await` de la última línea y cambia el 0 por un 1. Ejecuta `pnpm test`, mira el resumen, el final de la salida y `echo $?`. Después, devuelve el test a como estaba.

---

## Comprobación

```bash
pnpm test
# Tests:       6 passed, 6 total
grep -c "await expect(" tests/booking.test.js
# 4
grep -cE "^ *expect[(].*[.](rejects|resolves)" tests/booking.test.js
# 0
```

Guarda el lab:

```bash
git add src/booking.js tests/booking.test.js
git commit -m "lab 01: tests que esperan"
```

## Repaso

- [ ] Miras el final de la salida y el código de salida, no solo la línea `Tests:`.
- [ ] Sabes por qué un `.then` sin `return` comprueba datos de otro test.
- [ ] Cada `expect(...).rejects` y `.resolves` lleva `await` delante, en una función `async`.
- [ ] Sabes reconocer `Promise { <pending> }` y `[object Promise]` como un `await` olvidado.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Errores asíncronos](../lab-02-errores-asincronos/README.md) → |

[↑ Semana 09](../../README.md)
