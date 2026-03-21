function calculateShippingFee(weightKg, isPriority) {
  if (weightKg <= 0) {
    throw new Error("invalid weight");
  }

  const ratePerKg = isPriority ? 8 : 5;
  return weightKg * ratePerKg;
}

module.exports = { calculateShippingFee };
