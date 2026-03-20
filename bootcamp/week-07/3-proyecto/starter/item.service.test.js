// ============================================
// TEST SUITE: ItemService (Jest)
// ============================================

describe("ItemService", () => {
  // TODO: Configurar dependencias y servicio bajo prueba

  beforeEach(() => {
    // TODO: Inicializar dobles y estado limpio por test
  });

  describe("createItem", () => {
    // TODO: should create item when payload is valid
    // TODO: should throw ValidationError when name is empty
    // TODO: should throw ValidationError when quantity is negative
  });

  describe("updateItem", () => {
    // TODO: should update item when payload is valid
    // TODO: should throw NotFoundError when id does not exist
  });

  describe("notifyItemCreation", () => {
    // TODO: usar mock o spy para validar llamada a dependencia externa
  });
});
