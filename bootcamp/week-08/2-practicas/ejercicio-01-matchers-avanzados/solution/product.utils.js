function buildProduct(name, price) {
  return {
    name,
    price,
    tags: ["catalog", "active"],
  };
}

function validatePrice(price) {
  if (price < 0) {
    throw new Error("ValidationError");
  }
  return true;
}

module.exports = { buildProduct, validatePrice };
