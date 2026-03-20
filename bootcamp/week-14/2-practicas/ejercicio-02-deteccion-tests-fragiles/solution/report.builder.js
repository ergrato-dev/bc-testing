function buildDeliveryReport({ id, customerName, delivered, items }) {
  if (!id || !customerName) {
    throw new Error("Missing required fields");
  }

  const safeItems = Array.isArray(items) ? items : [];

  return {
    id,
    customerName: customerName.trim(),
    delivered: Boolean(delivered),
    itemCount: safeItems.length,
    status: delivered ? "delivered" : "pending",
  };
}

module.exports = { buildDeliveryReport };
