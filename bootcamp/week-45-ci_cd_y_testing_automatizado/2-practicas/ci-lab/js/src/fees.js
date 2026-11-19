// Multa por devolver tarde un libro: 500 por día, con un máximo de 10 000.
const FEE_PER_DAY = 500;
const MAX_FEE = 10000;

function lateFee(daysLate) {
  if (!Number.isInteger(daysLate)) {
    throw new TypeError('daysLate must be an integer');
  }
  if (daysLate <= 0) {
    return 0;
  }
  return Math.min(daysLate * FEE_PER_DAY, MAX_FEE);
}

// Texto del recibo: "Sin multa" o el importe.
function describeFee(daysLate) {
  const fee = lateFee(daysLate);
  if (fee === 0) {
    return 'Sin multa';
  }
  return `Multa: ${fee}`;
}

module.exports = { lateFee, describeFee, FEE_PER_DAY, MAX_FEE };
