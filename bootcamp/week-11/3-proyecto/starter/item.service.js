class ItemService {
  constructor(repository) {
    this.repository = repository;
  }

  create(input) {
    if (!input?.name) {
      throw new Error("name is required");
    }

    return this.repository.create(input);
  }

  updateStock(itemId, quantity) {
    if (!itemId) {
      throw new Error("itemId is required");
    }

    if (quantity < 0) {
      throw new Error("quantity cannot be negative");
    }

    return this.repository.updateStock(itemId, quantity);
  }
}

module.exports = { ItemService };
