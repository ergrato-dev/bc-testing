const { createBookingService } = require("../src/booking");
const { createFakeBoxOffice } = require("./fake-box-office");

const DATE = "2026-12-05";
const ANA = { name: "Ana Ruiz", seats: 2, date: DATE };

describe("createBookingService", () => {
  let service;

  beforeEach(() => {
    service = createBookingService({ capacity: 10, boxOffice: createFakeBoxOffice() });
  });

  describe("book", () => {
    describe("when there are seats left", () => {
      it("should return the booking with its id", async () => {
        const booking = await service.book(ANA);

        expect(booking).toStrictEqual({ id: 1, ...ANA });
      });

      it("should take the seats from the session", () => {
        service
          .book(ANA)
          .then(() => service.availableSeats(DATE))
          .then((available) => expect(available).toBe(8));
      });
    });

    describe("when the number of seats is not allowed", () => {
      it("should reject 0 seats", async () => {
        await expect(service.book({ ...ANA, seats: 0 })).rejects.toThrow("Entre 1 y 6 butacas por reserva");
      });

      it("should reject 7 seats", () => {
        expect(service.book({ ...ANA, seats: 7 })).rejects.toThrow("Entre 1 y 6 butacas por reserva");
      });
    });

    describe("when the session is full", () => {
      it("should reject a booking that does not fit", async () => {
        await service.book({ name: "Ana Ruiz", seats: 6, date: DATE });

        expect(service.book({ name: "Luis Gil", seats: 5, date: DATE })).rejects.toThrow(
          "No quedan butacas suficientes",
        );
      });
    });
  });
});
