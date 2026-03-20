const gateway = require("./payment.gateway");

async function confirmOrder(total) {
  const result = await gateway.charge(total);

  if (!result.approved) {
    throw new Error("payment rejected");
  }

  return { status: "confirmed", transactionId: result.transactionId };
}

module.exports = { confirmOrder };
