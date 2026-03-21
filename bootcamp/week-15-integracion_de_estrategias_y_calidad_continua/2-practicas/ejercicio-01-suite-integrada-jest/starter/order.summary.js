function buildOrderSummary(order) {
  if (!order || !Array.isArray(order.items)) {
    throw new Error("Invalid order items");
  }

  if (typeof order.shipping !== "number" || order.shipping < 0) {
    throw new Error("Invalid shipping");
  }

  const subtotal = order.items.reduce((acc, item) => {
    if (typeof item.price !== "number" || item.price < 0) {
      throw new Error("Invalid item price");
    }
    return acc + item.price;
  }, 0);

  const total = Number((subtotal + order.shipping).toFixed(2));

  return {
    orderId: order.orderId,
    itemCount: order.items.length,
    subtotal: Number(subtotal.toFixed(2)),
    shipping: Number(order.shipping.toFixed(2)),
    total,
  };
}

module.exports = { buildOrderSummary };
