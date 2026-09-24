const express = require("express");

// Repositorio en memoria: cada llamada crea un estado nuevo.
function createItemRepository() {
  const items = [{ id: "it-1", name: "Notebook" }];

  return {
    findAll: () => items,
    create: (name) => {
      const created = { id: `it-${items.length + 1}`, name };
      items.push(created);
      return created;
    },
  };
}

// Factory: cada test puede pedir una app con su propio repositorio.
function createApp({ repository = createItemRepository() } = {}) {
  const app = express();
  app.use(express.json());

  app.get("/health", (_req, res) => {
    res.status(200).json({ status: "ok" });
  });

  app.get("/items", (_req, res) => {
    res.status(200).json({ items: repository.findAll() });
  });

  app.post("/items", (req, res) => {
    const created = repository.create(req.body.name);
    res.status(201).json(created);
  });

  return app;
}

module.exports = { createApp };
