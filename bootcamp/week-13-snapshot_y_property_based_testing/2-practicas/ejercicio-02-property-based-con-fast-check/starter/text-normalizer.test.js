const fc = require("fast-check");
const { normalizeText } = require("./text-normalizer");

// Strings con letras y whitespace real (espacio, tab y salto de línea).
const textWithWhitespace = fc.string({
  unit: fc.constantFrom("a", "B", " ", "\t", "\n"),
});

// ============================================
// PASO 1: Idempotencia
// ============================================
// test("should be idempotent when normalizing text", () => {
//   fc.assert(
//     fc.property(textWithWhitespace, (value) => {
//       const once = normalizeText(value);
//       const twice = normalizeText(once);
//       expect(twice).toBe(once);
//     }),
//   );
// });

// ============================================
// PASO 2: Sin espacios dobles
// ============================================
// test("should not contain double spaces after normalization", () => {
//   fc.assert(
//     fc.property(textWithWhitespace, (value) => {
//       const normalized = normalizeText(value);
//       expect(normalized.includes("  ")).toBe(false);
//     }),
//   );
// });

// ============================================
// PASO 3: Sin espacios extremos
// ============================================
// test("should trim leading and trailing spaces", () => {
//   fc.assert(
//     fc.property(textWithWhitespace, (value) => {
//       const normalized = normalizeText(value);
//       expect(normalized).toBe(normalized.trim());
//     }),
//   );
// });

// ============================================
// PASO 4: Ningún tab ni salto de línea (falla a propósito)
// ============================================
// Con la implementación inicial esta propiedad FALLA: lee el Counterexample,
// el seed y "Shrunk N time(s)" en la salida antes de pasar al PASO 5.
// test("should only contain single spaces as whitespace", () => {
//   fc.assert(
//     fc.property(textWithWhitespace, (value) => {
//       const normalized = normalizeText(value);
//       expect(normalized).not.toMatch(/\t|\n| {2}/);
//     }),
//   );
// });
