const { createBookingService } = require("../src/booking");
const { systemClock } = require("../src/clock");

// Un notificador que no hace nada: estos tests no miran los avisos.
const silentNotifier = { send() {} };

describe("booking", () => {
  it("book works", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    const booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2099-05-10" });
    expect(booking.id).toBe(1);
  });

  it("book seats", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2099-05-10" });
    expect(service.availableSeats("2099-05-10")).toBe(8);
  });

  it("book 0", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    expect(() => service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 0, date: "2099-05-10" })).toThrow("Entre 1 y 6 butacas por reserva");
  });

  it("book 7", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    expect(() => service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 7, date: "2099-05-10" })).toThrow("Entre 1 y 6 butacas por reserva");
  });

  it("book today", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    expect(() => service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: systemClock.today() })).toThrow("La sesión tiene que ser a partir de mañana");
  });

  it("full", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 6, date: "2099-05-10" });
    expect(() => service.book({ name: "Luis Gil", email: "luis@example.com", seats: 5, date: "2099-05-10" })).toThrow("No quedan butacas suficientes");
  });

  it("full other day", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 6, date: "2099-05-10" });
    expect(service.availableSeats("2099-05-11")).toBe(10);
  });

  it("cancel", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    const booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2099-05-10" });
    service.cancel(booking.id);
    expect(service.availableSeats("2099-05-10")).toBe(10);
  });

  it("cancel error", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    expect(() => service.cancel(99)).toThrow("La reserva no existe");
  });

  it("seats", () => {
    const service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
    expect(service.availableSeats("2099-05-10")).toBe(10);
  });
});
