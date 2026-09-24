const express = require("express");

// TODO: Reemplazar "item" por la entidad de tu dominio asignado.
// Repositorio en memoria: cada llamada crea un estado nuevo y aislado.
function createItemRepository() {
  const items = [];

  return {
    findAll: () => items,
    findById: (id) => items.find((item) => item.id === id),
    create: (name) => {
      const created = { id: `it-${items.length + 1}`, name };
      items.push(created);
      return created;
    },
  };
}

// Factory: los tests crean una app nueva por caso y pueden inyectar
// un repositorio falso (por ejemplo, uno que lance para probar el 500).
function createApp({ repository = createItemRepository() } = {}) {
  const app = express();
  app.use(express.json());

  app.get("/items", (_req, res) => {
    // TODO: Retornar lista de recursos
    res.status(200).json({ items: repository.findAll() });
  });

  app.get("/items/:id", (req, res) => {
    // TODO: Buscar recurso por id
    const found = repository.findById(req.params.id);

    if (!found) {
      return res.status(404).json({
        error: "NotFoundError",
        message: "item not found",
      });
    }

    return res.status(200).json(found);
  });

  app.post("/items", (req, res) => {
    const { name } = req.body;

    // TODO: Validar payload de entrada
    if (!name) {
      return res.status(400).json({
        error: "ValidationError",
        message: "name is required",
      });
    }

    // TODO: Validar duplicados o conflictos de negocio (409)
    return res.status(201).json(repository.create(name));
  });

  // TODO: Middleware de errores de Express 5 (4 argumentos: err, req, res, next)
  // que responda 500 con el contrato { error, message }.

  return app;
}

module.exports = { createApp };
