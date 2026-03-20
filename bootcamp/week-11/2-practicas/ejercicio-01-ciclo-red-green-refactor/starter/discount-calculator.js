// ============================================
// PASO 2: Green inicial
// ============================================
// Descomenta para pasar el primer test:
// function calculateDiscount(price, membership) {
//   if (membership === "premium") {
//     return price * 0.9;
//   }
//   return price;
// }

// ============================================
// PASO 4: Green para borde + refactor pequeno
// ============================================
// Descomenta para cubrir validaciones simples:
// function calculateDiscount(price, membership) {
//   if (typeof price !== "number" || price < 0) {
//     throw new Error("invalid price");
//   }
//
//   const hasPremiumMembership = membership === "premium";
//   return hasPremiumMembership ? price * 0.9 : price;
// }

module.exports = { calculateDiscount };
