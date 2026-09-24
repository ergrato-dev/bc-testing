# 02 - Mocks de Funciones e Interacciones

> **Lenguaje:** JavaScript (Jest)

![Anatomía de jest.fn](../0-assets/02-anatomia-jest-fn.svg)
![Spy vs mock](../0-assets/03-spy-vs-mock.svg)

---

## Objetivo

Validar comportamiento observable: llamadas, argumentos, orden e impacto en la salida.

---

## Patrón recomendado

1. **Arrange**: crear dependencia mockeada y configurar retorno.
2. **Act**: ejecutar unidad bajo prueba.
3. **Assert**: validar resultado + interacción mínima necesaria.

---

## Ejemplo de servicio

En la Semana 07 viste `jest.fn()` con `mockReturnValue` para un envío simple. Aquí el doble calcula su respuesta con `mockImplementation` y el test verifica también el orden de las llamadas.

```javascript
async function notifyWaitlist(visitors, smsClient) {
  const results = [];
  for (const visitor of visitors) {
    results.push(await smsClient.send(visitor.phone, `Hola ${visitor.name}, hay cupo en el Acuario`));
  }
  return results.filter((result) => result.ok).length;
}

test("should notify visitors in waitlist order and count deliveries", async () => {
  const smsClient = {
    send: jest.fn().mockImplementation(async (phone) => ({ ok: phone !== "000" })),
  };
  const visitors = [
    { name: "Ana", phone: "111" },
    { name: "Luis", phone: "000" },
  ];

  const delivered = await notifyWaitlist(visitors, smsClient);

  expect(delivered).toBe(1);
  expect(smsClient.send).toHaveBeenNthCalledWith(1, "111", "Hola Ana, hay cupo en el Acuario");
  expect(smsClient.send).toHaveBeenNthCalledWith(2, "000", "Hola Luis, hay cupo en el Acuario");
});
```

`mockImplementationOnce(fn)` sobrescribe solo la siguiente llamada; después el mock vuelve a su implementación por defecto.

---

## Matchers más útiles para interacciones

- `toHaveBeenCalled()`
- `toHaveBeenCalledTimes(n)`
- `toHaveBeenCalledWith(payload)`
- `toHaveBeenNthCalledWith(n, payload)`
- `toHaveBeenLastCalledWith(payload)`

---

## Buenas prácticas

- Verifica solo interacciones relevantes para el comportamiento.
- Evita asserts redundantes sobre implementación interna.
- Limpia mocks con `jest.clearAllMocks()` en `afterEach` cuando aplique.

---

## Limpiar mocks: `mockClear`, `mockReset` y `mockRestore`

| Método | Historial de llamadas | Implementación falsa | Método original |
|---|---|---|---|
| `mockClear()` / `jest.clearAllMocks()` | Se borra | Se conserva | No |
| `mockReset()` / `jest.resetAllMocks()` | Se borra | Se borra (devuelve `undefined`) | No |
| `mockRestore()` / `jest.restoreAllMocks()` | Se borra | Se borra | Se restaura (solo spies creados con `jest.spyOn`) |
