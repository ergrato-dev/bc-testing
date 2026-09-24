// ============================================
// ItemService - se construye con TDD
// ============================================
// Estos metodos son stubs: lanzan "Not implemented" a proposito.
// No escribas logica aqui sin antes tener un test en rojo que la pida.

class ItemService {
  constructor(repository) {
    this.repository = repository;
  }

  // TODO (Green 1): delegar en repository.create cuando el input sea valido
  // TODO (Green 2): validar las reglas de negocio de create que pidan tus tests
  create(input) {
    throw new Error("Not implemented");
  }

  // TODO (Green 3): delegar en repository.updateStock
  // TODO (Green 4-5): validar itemId y quantity segun tus tests
  updateStock(itemId, quantity) {
    throw new Error("Not implemented");
  }
}

module.exports = { ItemService };
