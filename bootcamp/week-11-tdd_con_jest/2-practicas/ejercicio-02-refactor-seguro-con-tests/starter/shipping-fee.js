// ============================================
// PASO 2: Implementacion inicial en verde
// ============================================
// function calculateShippingFee(weightKg, isPriority) {
//   if (weightKg <= 0) {
//     throw new Error("invalid weight");
//   }
//
//   if (isPriority) {
//     return weightKg * 8;
//   }
//
//   return weightKg * 5;
// }

// ============================================
// PASO 3: Refactor sin cambiar comportamiento
// ============================================
// function calculateShippingFee(weightKg, isPriority) {
//   if (weightKg <= 0) {
//     throw new Error("invalid weight");
//   }
//
//   const ratePerKg = isPriority ? 8 : 5;
//   return weightKg * ratePerKg;
// }

module.exports = { calculateShippingFee };
