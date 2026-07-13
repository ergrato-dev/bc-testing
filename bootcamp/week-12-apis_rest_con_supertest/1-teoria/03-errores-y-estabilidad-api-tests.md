# 03 - Manejo de Errores y Estabilidad en API Tests

> **Lenguaje:** JavaScript (Jest + Supertest)

![Mapa de decision para errores HTTP](../0-assets/04-error-handling-decision-map.svg)

---

## Objetivo

Asegurar que la suite de API sea estable y diagnostique fallos con claridad.

---

## Tipos de errores frecuentes

1. **Validacion**: request invalido (400).
2. **No encontrado**: recurso inexistente (404).
3. **Conflicto**: duplicidad de datos (409).
4. **Interno**: excepcion no controlada (500).

---

## Estrategias de estabilidad

- Usar datos deterministas por test.
- Resetear estado de repositorio in-memory entre casos.
- Evitar dependencia de orden de ejecucion.
- Mantener asserts enfocados y descriptivos.
- No compartir fixtures mutables entre archivos de test.

---

## Repositorio in-memory y reset entre tests

Cuando la app guarda datos en un arreglo o Map en memoria, cada test puede dejar el estado sucio para el siguiente. `beforeEach` reinicia el repositorio antes de cada caso.

```javascript
const request = require("supertest");
const { app, resetExhibits } = require("./app");

beforeEach(() => {
  resetExhibits();
});

test("should create exhibit with unique id", async () => {
  const response = await request(app)
    .post("/exhibits")
    .send({ name: "Sala de aves" });

  expect(response.status).toBe(201);
  expect(response.body.id).toBeDefined();
});
```

---

## Testear un 404

```javascript
test("should return 404 when exhibit does not exist", async () => {
  const response = await request(app).get("/exhibits/999");

  expect(response.status).toBe(404);
  expect(response.body).toEqual({
    error: "NotFoundError",
    message: "exhibit not found",
  });
});
```

---

## Testear un 409

```javascript
test("should return 409 when exhibit name already exists", async () => {
  await request(app).post("/exhibits").send({ name: "Sala de aves" });

  const response = await request(app)
    .post("/exhibits")
    .send({ name: "Sala de aves" });

  expect(response.status).toBe(409);
  expect(response.body).toEqual({
    error: "ConflictError",
    message: "exhibit name already exists",
  });
});
```

---

## Plantilla de error recomendada

```json
{
  "error": "ValidationError",
  "message": "name is required"
}
```

---

## Regla practica

Cuando un test de API falla, primero revisa si el contrato esperado sigue vigente antes de culpar al framework o al entorno.

---

## Errores frecuentes

- Compartir un unico array de datos entre tests sin resetearlo en `beforeEach`.
- Asumir orden de ejecucion entre tests para que un recurso "ya exista".
- Devolver 500 para errores de validacion que deberian ser 400.
- No distinguir 404 (no existe) de 409 (conflicto con estado actual).
