class ItemService {
  constructor(repository) {
    this.repository = repository;
  }

  async create(input) {
    if (!input || !input.name) {
      throw new Error("name is required");
    }

    const createdItem = await this.repository.create(input);
    return createdItem;
  }

  async findById(id) {
    if (!id) {
      throw new Error("id is required");
    }

    const item = await this.repository.findById(id);
    if (!item) {
      throw new Error("item not found");
    }

    return item;
  }
}

module.exports = { ItemService };
