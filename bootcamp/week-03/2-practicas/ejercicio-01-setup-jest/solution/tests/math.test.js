const { add, isEven } = require("../src/math");

describe("math", () => {
  it("should return 5 when adding 2 and 3", () => {
    // Arrange
    const a = 2;
    const b = 3;

    // Act
    const result = add(a, b);

    // Assert
    expect(result).toBe(5);
  });

  it("should return true when number is even", () => {
    // Arrange
    const input = 10;

    // Act
    const result = isEven(input);

    // Assert
    expect(result).toBeTruthy();
  });

  it("should return false when number is odd", () => {
    // Arrange
    const input = 7;

    // Act
    const result = isEven(input);

    // Assert
    expect(result).toBeFalsy();
  });
});
