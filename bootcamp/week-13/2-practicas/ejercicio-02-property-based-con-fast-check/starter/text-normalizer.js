function normalizeText(input) {
  return input.trim().replace(/\s+/g, " ").toLowerCase();
}

module.exports = { normalizeText };
