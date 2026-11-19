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

function createUser(name, email) {
  return {
    name: name.trim(),
    email: email.toLowerCase(),
    active: true,
  };
}

function findUserByEmail(users, email) {
  const user = users.find((candidate) => candidate.email === email);
  return user === undefined ? null : user;
}

module.exports = {
  isAdult,
  calculateDiscount,
  isValidEmail,
  createUser,
  findUserByEmail,
};
