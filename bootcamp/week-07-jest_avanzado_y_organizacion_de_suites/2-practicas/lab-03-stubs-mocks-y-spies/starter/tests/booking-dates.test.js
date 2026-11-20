// Escrito el 15 de marzo de 2026, con todos los tests en verde.
const { createBookingService } = require("../src/booking");
const { systemClock } = require("../src/clock");

const silentNotifier = { send() {} };

describe("createBookingService dates", () => {
  let service;

  beforeEach(() => {
    service = createBookingService({ capacity: 10, clock: systemClock, notifier: silentNotifier });
  });

  describe("when the session is tomorrow", () => {
    it("should accept the booking", () => {
      const booking = service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2026-03-16" });

      expect(booking.date).toBe("2026-03-16");
    });
  });

  describe("when the session is today", () => {
    it("should throw", () => {
      expect(() => service.book({ name: "Ana Ruiz", email: "ana@example.com", seats: 2, date: "2026-03-15" })).toThrow("La sesión tiene que ser a partir de mañana");
    });
  });
});
