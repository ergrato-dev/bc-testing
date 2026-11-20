// El reloj del sistema: today() devuelve la fecha de hoy como "AAAA-MM-DD" (en UTC).
const systemClock = {
  today() {
    return new Date().toISOString().slice(0, 10);
  },
};

module.exports = { systemClock };
