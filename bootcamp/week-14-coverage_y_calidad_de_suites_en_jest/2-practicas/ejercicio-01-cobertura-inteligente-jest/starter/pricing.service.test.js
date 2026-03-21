const { calculateFinalPrice } = require("./pricing.service");

// ============================================
// PASO 1: Caso feliz sin descuentos
// ============================================
// test("should return base price when customer is not premium and hour is daytime", () => {
//   const result = calculateFinalPrice({ basePrice: 20, isPremium: false, hour: 12 });
//   expect(result).toBe(20);
// });

// ============================================
// PASO 2: Validacion de entrada
// ============================================
// test("should throw error when base price is invalid", () => {
//   expect(() =>
//     calculateFinalPrice({ basePrice: 0, isPremium: false, hour: 10 })
//   ).toThrow("Invalid base price");
// });

// ============================================
// PASO 3: Rama premium
// ============================================
// test("should apply premium discount", () => {
//   const result = calculateFinalPrice({ basePrice: 30, isPremium: true, hour: 14 });
//   expect(result).toBe(27);
// });

// ============================================
// PASO 4: Rama nocturna
// ============================================
// test("should add night surcharge between 22 and 05", () => {
//   const result = calculateFinalPrice({ basePrice: 20, isPremium: false, hour: 23 });
//   expect(result).toBe(25);
// });
