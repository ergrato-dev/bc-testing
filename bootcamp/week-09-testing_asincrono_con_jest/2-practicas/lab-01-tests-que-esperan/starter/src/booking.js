// Reservas de butacas para las sesiones del planetario.
// Las butacas ocupadas las lleva la taquilla, un servicio aparte: cada consulta devuelve una promesa.

const MAX_SEATS_PER_BOOKING = 6;

function createBookingService({ capacity, boxOffice }) {
  async function book({ name, seats, date }) {
    if (!Number.isInteger(seats) || seats < 1 || seats > MAX_SEATS_PER_BOOKING) {
      throw new Error("Entre 1 y 6 butacas por reserva");
    }
    const taken = boxOffice.seatsTaken(date);
    if (taken + seats > capacity) {
      throw new Error("No quedan butacas suficientes");
    }
    return boxOffice.reserve({ name, seats, date });
  }

  async function availableSeats(date) {
    const taken = await boxOffice.seatsTaken(date);
    return capacity - taken;
  }

  return { book, availableSeats };
}

module.exports = { createBookingService };
