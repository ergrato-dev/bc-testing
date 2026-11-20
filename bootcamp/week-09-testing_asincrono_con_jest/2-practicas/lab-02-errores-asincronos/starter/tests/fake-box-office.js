// Una taquilla falsa, en memoria. Responde como la real: con una promesa que tarda 10 ms.
// No acaba en .test.js, así que Jest no la ejecuta como un archivo de tests.

function createFakeBoxOffice() {
  const bookings = [];
  let nextId = 1;
  const later = (value) => new Promise((resolve) => setTimeout(() => resolve(value), 10));

  return {
    seatsTaken(date) {
      const taken = bookings
        .filter((booking) => booking.date === date)
        .reduce((total, booking) => total + booking.seats, 0);
      return later(taken);
    },
    reserve(booking) {
      const saved = { id: nextId, ...booking };
      nextId += 1;
      bookings.push(saved);
      return later(saved);
    },
    release(id) {
      const index = bookings.findIndex((booking) => booking.id === id);
      if (index === -1) {
        return later(null);
      }
      const [released] = bookings.splice(index, 1);
      return later(released);
    },
  };
}

module.exports = { createFakeBoxOffice };
