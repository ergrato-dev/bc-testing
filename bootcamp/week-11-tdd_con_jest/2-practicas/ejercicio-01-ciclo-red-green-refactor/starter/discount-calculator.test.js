const { calculateDiscount } = require("./discount-calculator");

// ============================================
// PASO 1: Red - caso mínimo (premium)
// ============================================
// test("should apply 10 percent discount when membership is premium", () => {
//   const result = calculateDiscount(100, "premium");
//   expect(result).toBe(90);
// });

// ============================================
// PASO 3: Red - socio básico
// ============================================
// test("should return full price when membership is basic", () => {
//   const result = calculateDiscount(100, "basic");
//   expect(result).toBe(100);
// });

// ============================================
// PASO 5: Red - validación de precio negativo
// ============================================
// test("should throw error when price is negative", () => {
//   expect(() => calculateDiscount(-10, "premium")).toThrow("invalid price");
// });
