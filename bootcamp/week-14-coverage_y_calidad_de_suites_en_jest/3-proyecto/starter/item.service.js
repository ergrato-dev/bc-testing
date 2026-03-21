function createItemService(repository) {
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

    const exists = await repository.findByName(name.toLowerCase());
    if (exists) {
      throw new Error("Duplicated item");
    }

    return repository.save({
      name,
      quantity,
      status: quantity > 0 ? "available" : "empty",
    });
  }

  return { createItem };
}

module.exports = { createItemService };
