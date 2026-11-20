const { createBookingService } = require("../src/booking");
const { createFakeBoxOffice } = require("./fake-box-office");

const ANA = { name: "Ana Ruiz", seats: 2, date: "2026-12-05" };

describe("createBookingService errors", () => {
  let boxOffice;
  let service;

  beforeEach(() => {
    boxOffice = createFakeBoxOffice();
    service = createBookingService({ capacity: 10, boxOffice });
  });

  describe("cancel", () => {
    describe("when the booking exists", () => {
      it("should return the cancelled booking", async () => {
        const booking = await service.book(ANA);

        const cancelled = await service.cancel(booking.id);

        expect(cancelled).toStrictEqual(booking);
      });
    });

    describe("when the booking does not exist", () => {
      it("should reject the id", () => {
        expect(() => service.cancel(99)).toThrow("La reserva no existe");
      });
    });

    describe("when the box office is down", () => {
      it("should reject with the box office message", async () => {
        boxOffice.release = jest.fn().mockRejectedValue(new Error("ECONNRESET"));

        try {
          await service.cancel(1);
        } catch (error) {
          expect(error.message).toBe("La taquilla no responde");
        }
      });
    });
  });
});
