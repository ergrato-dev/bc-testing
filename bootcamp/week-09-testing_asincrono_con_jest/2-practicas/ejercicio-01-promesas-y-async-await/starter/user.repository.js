async function getUserById(id) {
  if (id <= 0) {
    throw new Error("ValidationError");
  }

  return Promise.resolve({ id, name: "Ada" });
}

module.exports = { getUserById };
