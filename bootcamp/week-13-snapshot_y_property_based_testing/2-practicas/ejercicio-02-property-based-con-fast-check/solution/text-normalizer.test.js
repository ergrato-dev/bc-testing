const fc = require("fast-check");
const { normalizeText } = require("./text-normalizer");

test("should be idempotent when normalizing text", () => {
  fc.assert(
    fc.property(fc.string(), (value) => {
      const once = normalizeText(value);
      const twice = normalizeText(once);
      expect(twice).toBe(once);
    }),
  );
});

test("should not contain double spaces after normalization", () => {
  fc.assert(
    fc.property(fc.string(), (value) => {
      const normalized = normalizeText(value);
      expect(normalized.includes("  ")).toBe(false);
    }),
  );
});

test("should trim leading and trailing spaces", () => {
  fc.assert(
    fc.property(fc.string(), (value) => {
      const normalized = normalizeText(value);
      expect(normalized).toBe(normalized.trim());
    }),
  );
});
