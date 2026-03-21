class ItemService {
  constructor(itemRepository, billingGateway, auditLogger) {
    this.itemRepository = itemRepository;
    this.billingGateway = billingGateway;
    this.auditLogger = auditLogger;
  }

  async create(input) {
    if (!input?.name) {
      throw new Error("name is required");
    }

    return this.itemRepository.create(input);
  }

  async activate(itemId, planPrice) {
    if (!itemId) {
      throw new Error("itemId is required");
    }

    const item = await this.itemRepository.findById(itemId);
    if (!item) {
      throw new Error("item not found");
    }

    const chargeResult = await this.billingGateway.charge(planPrice);
    if (!chargeResult.approved) {
      throw new Error("payment rejected");
    }

    await this.itemRepository.markAsActive(itemId);
    this.auditLogger.log("ITEM_ACTIVATED", itemId);

    return { status: "active", itemId };
  }
}

module.exports = { ItemService };
