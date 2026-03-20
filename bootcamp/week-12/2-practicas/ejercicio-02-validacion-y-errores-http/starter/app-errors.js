const express = require("express");

const app = express();
app.use(express.json());

const items = [{ id: "it-1", name: "Notebook" }];

app.post("/items", (req, res) => {
  const { name } = req.body;

  if (!name) {
    return res.status(400).json({
      error: "ValidationError",
      message: "name is required",
    });
  }

  if (items.some((item) => item.name === name)) {
    return res.status(409).json({
      error: "ConflictError",
      message: "item name already exists",
    });
  }

  const created = { id: `it-${items.length + 1}`, name };
  items.push(created);
  return res.status(201).json(created);
});

app.get("/items/:id", (req, res) => {
  const found = items.find((item) => item.id === req.params.id);

  if (!found) {
    return res.status(404).json({
      error: "NotFoundError",
      message: "item not found",
    });
  }

  return res.status(200).json(found);
});

module.exports = { app };
