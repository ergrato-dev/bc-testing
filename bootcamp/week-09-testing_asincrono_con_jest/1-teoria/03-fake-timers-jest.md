# 03 - Fake Timers y Control del Tiempo en Jest

**Tipo**: JavaScript (Jest)

![Ciclo de fake timers](../0-assets/03-fake-timers-ciclo.svg)

---

## Objetivo

Hacer deterministas los tests que dependen de `setTimeout`, `setInterval` o reintentos, sin esperar tiempo real ni introducir tests lentos o flaky.

Un test que espera con `setTimeout` real es lento (agrega segundos reales a la suite) y flaky (depende de la carga de la maquina que ejecuta CI). Fake timers reemplazan el reloj interno de Node: el tiempo solo avanza cuando el test lo pide explicitamente con `jest.advanceTimersByTime()` o funciones equivalentes.

---

## Configuracion base

```javascript
beforeEach(() => {
  jest.useFakeTimers();
});

afterEach(() => {
  jest.useRealTimers();
});
```

---

## Controlando el avance del tiempo

| Metodo | Que hace | Cuando usarlo |
|---|---|---|
| `jest.advanceTimersByTime(ms)` | Avanza el reloj exactamente `ms` milisegundos, disparando los timers vencidos | Retry con delay fijo, debounce, throttle |
| `jest.runOnlyPendingTimers()` | Ejecuta solo los timers ya agendados en este momento, sin correr los nuevos que se creen durante la corrida | Timers que agendan otros timers (evita loops infinitos) |
| `jest.runAllTimers()` | Ejecuta todos los timers, incluidos los que se agendan en cadena | `setInterval` con condicion de corte clara |

---

## Ejemplo end-to-end: reintentos con backoff

Un fetch al catalogo del museo falla dos veces y reintenta cada 500ms antes de resolver:

```javascript
async function fetchExhibitWithRetry(fetcher, maxAttempts = 3) {
  for (let attempt = 1; attempt <= maxAttempts; attempt++) {
    try {
      return await fetcher();
    } catch (error) {
      if (attempt === maxAttempts) throw error;
      await new Promise((resolve) => setTimeout(resolve, 500));
    }
  }
}

test("should resolve after two failed attempts", async () => {
  const fetcher = jest
    .fn()
    .mockRejectedValueOnce(new Error("Timeout"))
    .mockRejectedValueOnce(new Error("Timeout"))
    .mockResolvedValueOnce({ id: "exhibit-1" });

  const promise = fetchExhibitWithRetry(fetcher);

  await jest.advanceTimersByTimeAsync(500);
  await jest.advanceTimersByTimeAsync(500);

  await expect(promise).resolves.toMatchObject({ id: "exhibit-1" });
  expect(fetcher).toHaveBeenCalledTimes(3);
});
```

---

## Ejemplo end-to-end: debounce de busqueda

```javascript
function debounce(fn, delay) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

test("should call search only once after rapid typing", () => {
  const search = jest.fn();
  const debouncedSearch = debounce(search, 300);

  debouncedSearch("dino");
  debouncedSearch("dinosaurio");

  jest.advanceTimersByTime(300);

  expect(search).toHaveBeenCalledTimes(1);
  expect(search).toHaveBeenCalledWith("dinosaurio");
});
```

---

## Buenas practicas

1. Activar fake timers solo donde sea necesario, no en toda la suite.
2. Restaurar timers reales al final de cada test (`afterEach` + `useRealTimers`).
3. Usar `runOnlyPendingTimers()` en vez de `runAllTimers()` si el callback puede reagendar otro timer indefinidamente.

---

## Errores frecuentes

- Dejar fake timers activos entre tests por no restaurar en `afterEach`, o usar `advanceTimersByTime` en vez de `advanceTimersByTimeAsync` cuando hay `await` de por medio.
- Usar `runAllTimers()` sobre un `setInterval` sin condicion de corte: loop infinito que cuelga el test.

---

## Regla practica

Si el codigo usa `setTimeout`, `setInterval` o un delay entre reintentos, el test debe usar fake timers y avanzar el reloj explicitamente. Nunca esperes tiempo real para validar un temporizador.
