// Codigo heredado: funciona, pero repite la multiplicacion en cada rama.
function calculateShippingFee(weightKg, isPriority) {
  if (weightKg <= 0) {
    throw new Error("invalid weight");
  }

  // ============================================
  // PASO 2: Refactor sin cambiar comportamiento
  // ============================================
  // Borra las 4 lineas marcadas con "PASO 2: borrar" y descomenta estas dos:
  // const ratePerKg = isPriority ? 8 : 5;
  // return weightKg * ratePerKg;

  if (isPriority) { // PASO 2: borrar
    return weightKg * 8; // PASO 2: borrar
  } // PASO 2: borrar
  return weightKg * 5; // PASO 2: borrar
}

module.exports = { calculateShippingFee };
