function applyDiscount(price, percentage) {
  if (price < 0 || percentage < 0 || percentage > 100) {
    throw new Error("ValidationError");
  }

  return price - (price * percentage) / 100;
}

module.exports = { applyDiscount };
