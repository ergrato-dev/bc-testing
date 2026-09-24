const fc = require("fast-check");
const { normalizeText } = require("./text-normalizer");

// Strings con letras y whitespace real (espacio, tab y salto de línea).
const textWithWhitespace = fc.string({
  unit: fc.constantFrom("a", "B", " ", "\t", "\n"),
});

test("should be idempotent when normalizing text", () => {
  fc.assert(
    fc.property(textWithWhitespace, (value) => {
      const once = normalizeText(value);
      const twice = normalizeText(once);
      expect(twice).toBe(once);
    }),
  );
});

test("should not contain double spaces after normalization", () => {
  fc.assert(
    fc.property(textWithWhitespace, (value) => {
      const normalized = normalizeText(value);
      expect(normalized.includes("  ")).toBe(false);
    }),
  );
});

test("should trim leading and trailing spaces", () => {
  fc.assert(
    fc.property(textWithWhitespace, (value) => {
      const normalized = normalizeText(value);
      expect(normalized).toBe(normalized.trim());
    }),
  );
});

test("should only contain single spaces as whitespace", () => {
  fc.assert(
    fc.property(textWithWhitespace, (value) => {
      const normalized = normalizeText(value);
      expect(normalized).not.toMatch(/\t|\n| {2}/);
    }),
  );
});
