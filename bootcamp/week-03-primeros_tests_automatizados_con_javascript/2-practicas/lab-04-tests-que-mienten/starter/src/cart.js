// Carrito de la tienda del planetario.

function cartTotal(items) {
  return items.reduce((sum, item) => sum + item.price * item.quantity, 0);
}

function applyCoupon(total, code) {
  if (code === "PLANETA10") {
    return total * 0.9;
  }
  throw new Error("Invalid coupon");
}

function shippingCost(total) {
  return total >= 30 ? 0 : 3.9;
}

module.exports = { cartTotal, applyCoupon, shippingCost };
