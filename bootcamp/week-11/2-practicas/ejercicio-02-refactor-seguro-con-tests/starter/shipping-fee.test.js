const { calculateShippingFee } = require("./shipping-fee");

// ============================================
// PASO 1: Capturar comportamiento
// ============================================
// test("should calculate standard shipping fee when priority is false", () => {
//   const result = calculateShippingFee(2, false);
//   expect(result).toBe(10);
// });
//
// test("should calculate priority shipping fee when priority is true", () => {
//   const result = calculateShippingFee(2, true);
//   expect(result).toBe(16);
// });
//
// test("should throw error when weight is not positive", () => {
//   expect(() => calculateShippingFee(0, false)).toThrow("invalid weight");
// });
