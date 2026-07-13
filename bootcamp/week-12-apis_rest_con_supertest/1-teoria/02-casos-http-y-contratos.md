# 02 - Diseno de Casos HTTP y Contratos de Respuesta

> **Lenguaje:** JavaScript (Jest + Supertest)

![Matriz de contratos HTTP](../0-assets/02-http-status-contract-matrix.svg)
![Slice de piramide para API tests](../0-assets/03-api-test-pyramid-slice.svg)

---

## Objetivo

Diseñar casos de prueba que validen comportamiento funcional y contrato API.

---

## Matriz minima sugerida

| Endpoint | Caso | Status esperado |
|---|---|---|
| GET /exhibits | lista disponible | 200 |
| GET /exhibits/:id | item inexistente | 404 |
| POST /exhibits | payload valido | 201 |
| POST /exhibits | campo requerido faltante | 400 |
| PUT /exhibits/:id | reemplazo completo valido | 200 |
| PATCH /exhibits/:id | actualizacion parcial valida | 200 |
| DELETE /exhibits/:id | eliminacion exitosa | 204 |
| DELETE /exhibits/:id | id inexistente | 404 |

---

## Contrato de respuesta

Para cada endpoint, define explicitamente:

1. Shape del JSON de exito.
2. Shape del JSON de error.
3. Reglas de validacion de campos.

---

## Ejemplo de test de contrato

```javascript
test("should return validation error payload when name is missing", async () => {
  const response = await request(app).post("/exhibits").send({});

  expect(response.status).toBe(400);
  expect(response.body).toEqual({
    error: "ValidationError",
    message: "name is required",
  });
});
```

---

## PUT vs PATCH en la practica

```javascript
test("should replace exhibit fully with PUT", async () => {
  const response = await request(app)
    .put("/exhibits/1")
    .send({ name: "Sala de mamiferos", floor: 2 });

  expect(response.status).toBe(200);
  expect(response.body).toEqual({ id: 1, name: "Sala de mamiferos", floor: 2 });
});

test("should update only one field with PATCH", async () => {
  const response = await request(app)
    .patch("/exhibits/1")
    .send({ floor: 3 });

  expect(response.status).toBe(200);
  expect(response.body.floor).toBe(3);
});
```

---

## DELETE y respuestas sin body

```javascript
test("should delete exhibit and return no content", async () => {
  const response = await request(app).delete("/exhibits/1");

  expect(response.status).toBe(204);
  expect(response.body).toEqual({});
});
```

---

## Validar headers y content-type

`Content-Type` confirma que el cliente interpretara el body correctamente; vale la pena assertarlo en al menos un test por tipo de respuesta.

```javascript
test("should respond with json content-type", async () => {
  const response = await request(app).get("/exhibits");

  expect(response.headers["content-type"]).toMatch(/json/);
});
```

---

## Recomendacion

Empieza por los endpoints de mayor impacto de negocio y evoluciona la matriz de casos en paralelo al desarrollo.
