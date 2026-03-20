function sum(a, b) {
  if (typeof a !== "number" || typeof b !== "number") {
    throw new Error("Both values must be numbers");
  }

  return a + b;
}

module.exports = { sum };
