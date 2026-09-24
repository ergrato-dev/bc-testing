const express = require("express");

// Capa API: traduce HTTP <-> servicio. Recibe el servicio por parámetro
// para poder probarla con Supertest sin levantar un servidor real (sin app.listen).
function createApp(itemService) {
  const app = express();
  app.use(express.json());

  // TODO: POST /items -> llamar a itemService.createItem(req.body) y responder 201 con el item guardado
  // TODO: Traducir los errores del servicio a HTTP: validaciones -> 400, "Duplicated item" -> 409,
  //       siempre con cuerpo JSON { error: <mensaje> }

  return app;
}

module.exports = { createApp };
