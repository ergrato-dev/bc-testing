const express = require("express");

const app = express();
app.use(express.json());

// TODO: Reemplazar por entidad de tu dominio asignado
const items = [];

app.get("/items", (_req, res) => {
  // TODO: Retornar lista de recursos
  res.status(200).json({ items });
});

app.get("/items/:id", (req, res) => {
  // TODO: Buscar recurso por id
  const found = items.find((item) => item.id === req.params.id);

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
  const created = { id: `it-${items.length + 1}`, name };
  items.push(created);

  return res.status(201).json(created);
});

module.exports = { app };
