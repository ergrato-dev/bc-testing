async function charge(amount) {
  if (amount <= 500) {
    return { approved: true, transactionId: "tx-real-1" };
  }

  return { approved: false };
}

module.exports = { charge };
