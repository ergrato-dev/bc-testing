// Servicio del dominio: guarda lo que se reserva (o se presta, o se inscribe) y avisa al cliente.
// TODO: renombra el archivo y la función con el nombre de tu dominio (booking-service.js y
// createBookingService, loan-service.js y createLoanService...).

// TODO: importa las funciones de la semana 3 desde su archivo (src/domain.js en el starter de esa semana).
const {} = require("./domain");

function createService({ clock, notifier }) {
  const items = [];

  function create(data) {
    // TODO 1: valida data con tus funciones de la semana 3; si algo no cumple, deja que lancen su error.
    // TODO 2: comprueba la regla de fechas con clock.today(), nunca con new Date().
    // TODO 3: guarda el elemento en items y, solo después, avisa con notifier.send(destinatario, mensaje).
    // TODO 4: devuelve lo que has guardado.
  }

  function cancel(id) {
    // TODO 5: si no existe, lanza un error; si existe, quítalo de items y avisa.
  }

  function list() {
    return [...items];
  }

  return { create, cancel, list };
}

module.exports = { createService };
