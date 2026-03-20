const { buildProduct, validatePrice } = require("./product.utils");

// ============================================
// PASO 1: Igualdad de objetos
// ============================================
// test("should build product object when payload is valid", () => {
//   const result = buildProduct("Notebook", 1200);
//   expect(result).toEqual({
//     name: "Notebook",
//     price: 1200,
//     tags: ["catalog", "active"]
//   });
// });

// ============================================
// PASO 2: Inclusion y parcial
// ============================================
// test("should include active tag when product is built", () => {
//   const result = buildProduct("Notebook", 1200);
//   expect(result.tags).toContain("active");
// });
//
// test("should match product partial shape", () => {
//   const result = buildProduct("Notebook", 1200);
//   expect(result).toMatchObject({ name: "Notebook" });
// });

// ============================================
// PASO 3: Error esperado
// ============================================
// test("should throw ValidationError when price is negative", () => {
//   expect(() => validatePrice(-1)).toThrow("ValidationError");
// });
