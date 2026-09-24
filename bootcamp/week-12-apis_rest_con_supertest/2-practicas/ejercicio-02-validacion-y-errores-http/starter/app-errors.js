const express = require("express");

// Repositorio en memoria: cada llamada crea un estado nuevo.
function createItemRepository() {
  const items = [{ id: "it-1", name: "Notebook" }];

  return {
    findById: (id) => items.find((item) => item.id === id),
    existsByName: (name) => items.some((item) => item.name === name),
    create: (name) => {
      const created = { id: `it-${items.length + 1}`, name };
      items.push(created);
      return created;
    },
  };
}

// Factory: permite inyectar un repositorio distinto (p.ej. uno que falla).
function createApp({ repository = createItemRepository() } = {}) {
  const app = express();
  app.use(express.json());

  app.post("/items", (req, res) => {
    const { name } = req.body;

    if (!name) {
      return res.status(400).json({
        error: "ValidationError",
        message: "name is required",
      });
    }

    if (repository.existsByName(name)) {
      return res.status(409).json({
        error: "ConflictError",
        message: "item name already exists",
      });
    }

    return res.status(201).json(repository.create(name));
  });

  app.get("/items/:id", (req, res) => {
    const found = repository.findById(req.params.id);

    if (!found) {
      return res.status(404).json({
        error: "NotFoundError",
        message: "item not found",
      });
    }

    return res.status(200).json(found);
  });

  // Middleware de errores (4 argumentos). En Express 5 recibe tanto
  // excepciones síncronas como promesas rechazadas de los handlers.
  app.use((_err, _req, res, _next) => {
    res.status(500).json({
      error: "InternalServerError",
      message: "unexpected error",
    });
  });

  return app;
}

module.exports = { createApp };
