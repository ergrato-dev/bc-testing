function calculateDiscount(price, membership) {
  if (typeof price !== "number" || price < 0) {
    throw new Error("invalid price");
  }

  const hasPremiumMembership = membership === "premium";
  return hasPremiumMembership ? price * 0.9 : price;
}

module.exports = { calculateDiscount };
