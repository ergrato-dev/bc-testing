function buildPublicItem(item) {
  return {
    id: item.id,
    name: item.name.trim(),
    category: item.category,
    status: item.status,
  };
}

function normalizeName(name) {
  return name.trim().replace(/\s+/g, " ").toLowerCase();
}

module.exports = { buildPublicItem, normalizeName };
