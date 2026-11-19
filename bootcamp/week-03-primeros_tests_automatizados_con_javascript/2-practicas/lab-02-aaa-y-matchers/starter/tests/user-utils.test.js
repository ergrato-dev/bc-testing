const {
  isAdult,
  calculateDiscount,
  isValidEmail,
  createUser,
  findUserByEmail,
} = require("../src/user-utils");

describe("user-utils", () => {
  // ============================================
  // PASO 1: tests de isAdult (descomenta)
  // ============================================
  // it("should return true when age is 18", () => {
  //   // Arrange
  //   const age = 18;
  //
  //   // Act
  //   const result = isAdult(age);
  //
  //   // Assert
  //   expect(result).toBe(true);
  // });
  //
  // it("should return false when age is 17", () => {
  //   // Arrange
  //   const age = 17;
  //
  //   // Act
  //   const result = isAdult(age);
  //
  //   // Assert
  //   expect(result).toBe(false);
  // });

  // ============================================
  // PASO 2: tests de calculateDiscount (descomenta)
  // ============================================
  // it("should return 80 when price is 100 and percent is 20", () => {
  //   // Arrange
  //   const price = 100;
  //   const percent = 20;
  //
  //   // Act
  //   const result = calculateDiscount(price, percent);
  //
  //   // Assert
  //   expect(result).toBe(80);
  // });
  //
  // it("should throw an error when percent is greater than 100", () => {
  //   // Arrange
  //   const price = 100;
  //   const percent = 120;
  //
  //   // Act + Assert
  //   expect(() => calculateDiscount(price, percent)).toThrow("Invalid percent");
  // });

  // ============================================
  // PASO 3: tests de isValidEmail (descomenta)
  // ============================================
  // it("should return true when email has an at sign and a dot", () => {
  //   // Arrange
  //   const email = "ana@example.com";
  //
  //   // Act
  //   const result = isValidEmail(email);
  //
  //   // Assert
  //   expect(result).toBe(true);
  // });
  //
  // it("should return false when email has no at sign", () => {
  //   // Arrange
  //   const email = "anaexample.com";
  //
  //   // Act
  //   const result = isValidEmail(email);
  //
  //   // Assert
  //   expect(result).toBe(false);
  // });

  // ============================================
  // PASO 4: createUser con toEqual (escríbelo tú)
  // ============================================
  // TODO: un test que llame a createUser("  Ana Pérez ", "ANA@Example.com")
  // y compruebe con toEqual el objeto completo que devuelve.

  // ============================================
  // PASO 5: findUserByEmail con toEqual y toBeNull (escríbelos tú)
  // ============================================
  // TODO: un test que busque un email que está en la lista (toEqual).
  // TODO: un test que busque un email que no está (toBeNull).

  // ============================================
  // PASO 6: calculateDiscount con decimales (escríbelo tú)
  // ============================================
  // TODO: un test con precio 19.99 y 15 % que use toBeCloseTo.
});
