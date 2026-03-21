const {
  isAdult,
  calculateDiscount,
  isValidEmail,
} = require("../src/user-utils");

describe("user-utils", () => {
  // ============================================
  // PASO 1: Tests de isAdult
  // ============================================
  // Descomenta las siguientes lineas:
  // it("should return true when age is 18", () => {
  //   const result = isAdult(18);
  //   expect(result).toBe(true);
  // });
  // it("should return false when age is 17", () => {
  //   const result = isAdult(17);
  //   expect(result).toBe(false);
  // });
  // ============================================
  // PASO 2: Tests de calculateDiscount
  // ============================================
  // Descomenta las siguientes lineas:
  // it("should return 80 when price is 100 and discount is 20", () => {
  //   const result = calculateDiscount(100, 20);
  //   expect(result).toBe(80);
  // });
  // it("should throw error when discount percent is greater than 100", () => {
  //   expect(() => calculateDiscount(100, 120)).toThrow("Invalid percent");
  // });
  // ============================================
  // PASO 3: Tests de isValidEmail
  // ============================================
  // Descomenta las siguientes lineas:
  // it("should return true when email format is valid", () => {
  //   const result = isValidEmail("ana@example.com");
  //   expect(result).toBeTruthy();
  // });
  // it("should return false when email format is invalid", () => {
  //   const result = isValidEmail("anaexamplecom");
  //   expect(result).toBeFalsy();
  // });
});
