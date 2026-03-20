const { applyDiscount } = require("./discount.service");

// ============================================
// PASO 1: Caso individual
// ============================================
// test("should return discounted price when percentage is valid", () => {
//   const result = applyDiscount(100, 10);
//   expect(result).toBe(90);
// });

// ============================================
// PASO 2: Parametrizacion con test.each
// ============================================
// test.each([
//   [100, 10, 90],
//   [200, 25, 150],
//   [80, 0, 80]
// ])(
//   "should return %i when price is %i and discount is %i",
//   (price, percentage, expected) => {
//     const result = applyDiscount(price, percentage);
//     expect(result).toBe(expected);
//   }
// );

// ============================================
// PASO 3: Casos invalidos
// ============================================
// test.each([
//   [-1, 10],
//   [100, -5],
//   [100, 120]
// ])("should throw ValidationError when inputs are invalid", (price, percentage) => {
//   expect(() => applyDiscount(price, percentage)).toThrow("ValidationError");
// });
