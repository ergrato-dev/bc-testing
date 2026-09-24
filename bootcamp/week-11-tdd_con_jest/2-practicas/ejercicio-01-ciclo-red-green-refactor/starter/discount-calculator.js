// ============================================
// PASO 7: Refactor - nombrar el numero magico
// ============================================
// Descomenta la constante y, en la linea del PASO 2, sustituye `0.9`
// por `PREMIUM_PRICE_FACTOR`. Ejecuta los tests: deben seguir en verde.
// const PREMIUM_PRICE_FACTOR = 0.9;

// Esqueleto: la funcion existe pero aun no hace nada (devuelve undefined).
function calculateDiscount(price, membership) {
  // ============================================
  // PASO 6: Green - validar el precio
  // ============================================
  // if (typeof price !== "number" || price < 0) {
  //   throw new Error("invalid price");
  // }

  // ============================================
  // PASO 4: Green - socios no premium pagan precio completo
  // ============================================
  // if (membership !== "premium") {
  //   return price;
  // }

  // ============================================
  // PASO 2: Green - codigo minimo para el primer test
  // ============================================
  // return price * 0.9;
}

module.exports = { calculateDiscount };
