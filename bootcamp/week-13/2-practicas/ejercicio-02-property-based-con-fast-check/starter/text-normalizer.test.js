const fc = require("fast-check");
const { normalizeText } = require("./text-normalizer");

// ============================================
// PASO 1: Idempotencia
// ============================================
// test("should be idempotent when normalizing text", () => {
//   fc.assert(
//     fc.property(fc.string(), (value) => {
//       const once = normalizeText(value);
//       const twice = normalizeText(once);
//       expect(twice).toBe(once);
//     })
//   );
// });

// ============================================
// PASO 2: Sin espacios dobles
// ============================================
// test("should not contain double spaces after normalization", () => {
//   fc.assert(
//     fc.property(fc.string(), (value) => {
//       const normalized = normalizeText(value);
//       expect(normalized.includes("  ")).toBe(false);
//     })
//   );
// });

// ============================================
// PASO 3: Sin espacios extremos
// ============================================
// test("should trim leading and trailing spaces", () => {
//   fc.assert(
//     fc.property(fc.string(), (value) => {
//       const normalized = normalizeText(value);
//       expect(normalized).toBe(normalized.trim());
//     })
//   );
// });
