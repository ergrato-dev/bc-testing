const express = require("express");

const app = express();
app.use(express.json());

const items = [{ id: "it-1", name: "Notebook" }];

app.get("/health", (_req, res) => {
  res.status(200).json({ status: "ok" });
});

app.get("/items", (_req, res) => {
  res.status(200).json({ items });
});

app.post("/items", (req, res) => {
  const { name } = req.body;
  const created = { id: `it-${items.length + 1}`, name };
  items.push(created);
  res.status(201).json(created);
});

module.exports = { app };
