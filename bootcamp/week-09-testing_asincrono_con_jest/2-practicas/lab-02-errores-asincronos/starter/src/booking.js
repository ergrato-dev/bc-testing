// Reservas de butacas para las sesiones del planetario.
// Las butacas ocupadas las lleva la taquilla, un servicio aparte: cada consulta devuelve una promesa.

const MAX_SEATS_PER_BOOKING = 6;

// Un error de reservas con un código, para que quien lo recibe sepa qué pasó sin leer el mensaje.
class BookingError extends Error {
  constructor(message, code, options) {
    super(message, options);
    this.name = "BookingError";
    this.code = code;
  }
}

function createBookingService({ capacity, boxOffice }) {
  async function book({ name, seats, date }) {
    if (!Number.isInteger(seats) || seats < 1 || seats > MAX_SEATS_PER_BOOKING) {
      throw new Error("Entre 1 y 6 butacas por reserva");
    }
    const taken = await boxOffice.seatsTaken(date);
    if (taken + seats > capacity) {
      throw new Error("No quedan butacas suficientes");
    }
    return boxOffice.reserve({ name, seats, date });
  }

  // Cancela una reserva. Si la taquilla falla, rechaza con BOX_OFFICE_DOWN y el error original en cause.
  async function cancel(id) {
    let released;
    try {
      released = await boxOffice.release(id);
    } catch (error) {
      return null;
    }
    if (!released) {
      throw new BookingError("La reserva no existe", "NOT_FOUND");
    }
    return released;
  }

  async function availableSeats(date) {
    const taken = await boxOffice.seatsTaken(date);
    return capacity - taken;
  }

  return { book, cancel, availableSeats };
}

module.exports = { createBookingService, BookingError };
