function calculateFinalPrice({ basePrice, isPremium, hour }) {
  if (
    typeof basePrice !== "number" ||
    Number.isNaN(basePrice) ||
    basePrice <= 0
  ) {
    throw new Error("Invalid base price");
  }

  if (typeof hour !== "number" || hour < 0 || hour > 23) {
    throw new Error("Invalid hour");
  }

  let total = basePrice;

  if (isPremium) {
    total = total * 0.9;
  }

  if (hour >= 22 || hour <= 5) {
    total = total + 5;
  }

  return Number(total.toFixed(2));
}

module.exports = { calculateFinalPrice };
