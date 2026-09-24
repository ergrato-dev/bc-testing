// TODO: Renombrar "item" a la entidad de tu dominio asignado.

function buildPublicItem(item) {
  if (!item || !item.id) {
    throw new Error("item id is required");
  }

  if (typeof item.name !== "string" || item.name.trim() === "") {
    throw new Error("item name is required");
  }

  return {
    id: item.id,
    name: item.name.trim(),
    category: item.category,
    status: item.status,
  };
}

// Divide una lista en páginas de pageSize elementos (la última puede ser menor).
function paginate(items, pageSize) {
  if (!Array.isArray(items)) {
    throw new Error("items must be an array");
  }

  if (!Number.isInteger(pageSize) || pageSize < 1) {
    throw new Error("pageSize must be a positive integer");
  }

  const pages = [];
  for (let start = 0; start < items.length; start += pageSize) {
    pages.push(items.slice(start, start + pageSize));
  }
  return pages;
}

module.exports = { buildPublicItem, paginate };
