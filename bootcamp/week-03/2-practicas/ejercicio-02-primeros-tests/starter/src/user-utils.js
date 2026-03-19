function isAdult(age) {
  return age >= 18;
}

function calculateDiscount(price, percent) {
  if (percent < 0 || percent > 100) {
    throw new Error("Invalid percent");
  }
  return price - (price * percent) / 100;
}

function isValidEmail(email) {
  return email.includes("@") && email.includes(".");
}

module.exports = { isAdult, calculateDiscount, isValidEmail };
