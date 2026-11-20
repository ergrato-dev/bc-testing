// Reservas de butacas para las sesiones del planetario.
// Las fechas van como "AAAA-MM-DD": así se comparan como texto.

const MAX_SEATS_PER_BOOKING = 6;

function createBookingService({ capacity, clock, notifier }) {
  const bookings = [];
  let nextId = 1;

  function seatsTaken(date) {
    return bookings
      .filter((booking) => booking.date === date)
      .reduce((total, booking) => total + booking.seats, 0);
  }

  function book({ name, email, seats, date }) {
    if (!Number.isInteger(seats) || seats < 1 || seats > MAX_SEATS_PER_BOOKING) {
      throw new Error("Entre 1 y 6 butacas por reserva");
    }
    if (date <= clock.today()) {
      throw new Error("La sesión tiene que ser a partir de mañana");
    }
    const booking = { id: nextId, name, email, seats, date };
    notifier.send(email, `Reserva ${booking.id} confirmada: ${seats} butacas el ${date}`);
    if (seatsTaken(date) + seats > capacity) {
      throw new Error("No quedan butacas suficientes");
    }
    nextId += 1;
    bookings.push(booking);
    return booking;
  }

  function cancel(id) {
    const index = bookings.findIndex((booking) => booking.id === id);
    if (index === -1) {
      throw new Error("La reserva no existe");
    }
    const [booking] = bookings.splice(index, 1);
    notifier.send(booking.email, `Reserva ${booking.id} cancelada`);
    return booking;
  }

  function availableSeats(date) {
    return capacity - seatsTaken(date);
  }

  return { book, cancel, availableSeats };
}

module.exports = { createBookingService };
