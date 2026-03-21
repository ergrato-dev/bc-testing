const {
  isAdult,
  calculateDiscount,
  isValidEmail,
} = require("../src/user-utils");

describe("user-utils", () => {
  it("should return true when age is 18", () => {
    // Arrange
    const age = 18;

    // Act
    const result = isAdult(age);

    // Assert
    expect(result).toBe(true);
  });

  it("should return false when age is 17", () => {
    // Arrange
    const age = 17;

    // Act
    const result = isAdult(age);

    // Assert
    expect(result).toBe(false);
  });

  it("should return 80 when price is 100 and discount is 20", () => {
    // Arrange
    const price = 100;
    const percent = 20;

    // Act
    const result = calculateDiscount(price, percent);

    // Assert
    expect(result).toBe(80);
  });

  it("should throw error when discount percent is greater than 100", () => {
    // Arrange
    const price = 100;
    const percent = 120;

    // Act + Assert
    expect(() => calculateDiscount(price, percent)).toThrow("Invalid percent");
  });

  it("should return true when email format is valid", () => {
    // Arrange
    const email = "ana@example.com";

    // Act
    const result = isValidEmail(email);

    // Assert
    expect(result).toBeTruthy();
  });

  it("should return false when email format is invalid", () => {
    // Arrange
    const email = "anaexamplecom";

    // Act
    const result = isValidEmail(email);

    // Assert
    expect(result).toBeFalsy();
  });
});
