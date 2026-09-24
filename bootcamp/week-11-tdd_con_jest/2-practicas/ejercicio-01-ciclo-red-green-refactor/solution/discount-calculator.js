const PREMIUM_PRICE_FACTOR = 0.9;

function calculateDiscount(price, membership) {
  if (typeof price !== "number" || price < 0) {
    throw new Error("invalid price");
  }

  if (membership !== "premium") {
    return price;
  }

  return price * PREMIUM_PRICE_FACTOR;
}

module.exports = { calculateDiscount };
