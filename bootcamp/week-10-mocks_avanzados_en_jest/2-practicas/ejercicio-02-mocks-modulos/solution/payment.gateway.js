function toCents(amount) {
  return Math.round(amount * 100);
}

async function charge(amountInCents) {
  if (amountInCents <= 50000) {
    return { approved: true, transactionId: "tx-real-1" };
  }

  return { approved: false };
}

module.exports = { toCents, charge };
