# 01 - Fundamentos de Testing de APIs REST con Supertest

> **Lenguaje:** JavaScript (Jest + Supertest)

![Flujo request-response en Supertest](../0-assets/01-supertest-request-flow.svg)

---

## Objetivo

Entender como validar endpoints HTTP de forma automatizada y repetible.

---

## Que es Supertest

Supertest permite hacer requests HTTP contra una app de Express sin levantar servidor real en puerto, facilitando pruebas rapidas y aisladas. Internamente usa `superagent` para construir la request y devuelve un objeto `response` con `status`, `body` y `headers` listos para assertar.

---

## Separar app y servidor

La app de Express se exporta sin invocar `listen()`. Asi cada test importa la app directamente, sin abrir puertos reales ni pelear con conflictos de puerto en ejecucion paralela.

```javascript
// app.js
const express = require("express");
const app = express();
app.use(express.json());

const exhibits = [{ id: 1, name: "Sala de dinosaurios" }];

app.get("/health", (req, res) => res.json({ status: "ok" }));
app.get("/exhibits", (req, res) => res.json(exhibits));

module.exports = { app };
```

```javascript
// server.js (solo para produccion, no se importa en tests)
const { app } = require("./app");

app.listen(3000, () => console.log("API escuchando en :3000"));
```

---

## Patron base

```javascript
const request = require("supertest");
const { app } = require("./app");

test("should return health status", async () => {
  const response = await request(app).get("/health");

  expect(response.status).toBe(200);
  expect(response.body).toEqual({ status: "ok" });
});
```

---

## Anatomia de un test con Supertest (AAA)

1. **Arrange**: preparar `app` y datos de entrada.
2. **Act**: disparar la request con `request(app).<verbo>(ruta)`.
3. **Assert**: validar `status`, `body` y, si aplica, `headers`.

```javascript
test("should return list of exhibits", async () => {
  const response = await request(app).get("/exhibits");

  expect(response.status).toBe(200);
  expect(response.body).toEqual([{ id: 1, name: "Sala de dinosaurios" }]);
});
```

---

## Que validar siempre

1. Codigo de estado HTTP.
2. Estructura y contenido del body.
3. Mensajes de error consistentes.
4. Headers relevantes cuando aplique (`content-type`, `location`, etc.).

---

## Errores comunes

- Probar API real externa en lugar de app local controlada.
- Validar solo `status` y omitir contrato de datos.
- No limpiar estado entre pruebas cuando hay almacenamiento en memoria.
- Exportar la app con `listen()` ya invocado, forzando conflictos de puerto entre suites.
- Repetir logica de arranque de `app` en cada archivo de test en lugar de centralizarla en un modulo comun.
