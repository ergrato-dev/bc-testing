function buildProduct(name, price) {
  return {
    name,
    price,
    tags: ["catalog", "active"],
    stock: { warehouse: "central", units: 10 },
  };
}

function sumPrices(prices) {
  return prices.reduce((total, price) => total + price, 0);
}

function validatePrice(price) {
  if (price < 0) {
    throw new Error("ValidationError");
  }
  return true;
}

module.exports = { buildProduct, sumPrices, validatePrice };
