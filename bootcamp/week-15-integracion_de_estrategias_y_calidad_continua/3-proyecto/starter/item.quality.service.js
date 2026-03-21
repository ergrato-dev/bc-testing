function createItemQualityService(repository) {
  async function createItem(input) {
    if (!input || typeof input !== "object") {
      throw new Error("Invalid payload");
    }

    const name = typeof input.name === "string" ? input.name.trim() : "";
    const quantity = Number(input.quantity);

    if (!name) {
      throw new Error("Name is required");
    }

    if (!Number.isInteger(quantity) || quantity < 0) {
      throw new Error("Quantity must be a non-negative integer");
    }

    const found = await repository.findByName(name.toLowerCase());
    if (found) {
      throw new Error("Duplicated item");
    }

    const payload = {
      name,
      quantity,
      status: quantity > 0 ? "available" : "empty",
    };

    return repository.save(payload);
  }

  return { createItem };
}

module.exports = { createItemQualityService };
