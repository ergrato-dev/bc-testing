const { add, isEven } = require("../src/math");

describe("math", () => {
  // Test activo que falla por diseño: add tiene un bug en src/math.js.
  // Lee el mensaje de error y corrígelo en el PASO 1 (src/math.js).
  it("should return 5 when adding 2 and 3", () => {
    // Arrange
    const a = 2;
    const b = 3;

    // Act
    const result = add(a, b);

    // Assert
    expect(result).toBe(5);
  });

  // ============================================
  // PASO 2: Test booleano simple
  // ============================================
  // Descomenta las siguientes lineas:
  // it("should return true when number is even", () => {
  //   // Arrange
  //   const input = 10;
  //
  //   // Act
  //   const result = isEven(input);
  //
  //   // Assert
  //   expect(result).toBeTruthy();
  // });

  // ============================================
  // PASO 3: Test para numero impar
  // ============================================
  // Descomenta las siguientes lineas:
  // it("should return false when number is odd", () => {
  //   // Arrange
  //   const input = 7;
  //
  //   // Act
  //   const result = isEven(input);
  //
  //   // Assert
  //   expect(result).toBeFalsy();
  // });
});
