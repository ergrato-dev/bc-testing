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

  // Reintenta la lectura del repositorio cuando falla (por ejemplo, un timeout de red).
  // Espera `delay` ms entre intentos y rechaza con el ultimo error si se agotan los reintentos.
  async findByIdWithRetry(id, retries = 2, delay = 500) {
    for (let attempt = 0; ; attempt++) {
      try {
        return await this.findById(id);
      } catch (error) {
        if (attempt >= retries || error.message === "id is required") {
          throw error;
        }
        await new Promise((resolve) => setTimeout(resolve, delay));
      }
    }
  }
}

module.exports = { ItemService };
