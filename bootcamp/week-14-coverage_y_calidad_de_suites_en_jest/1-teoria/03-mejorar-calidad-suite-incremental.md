# 03 - Mejorar Calidad de Suite de Forma Incremental

## Objetivo

Aplicar una estrategia progresiva para endurecer suites de tests sin bloquear el avance del equipo.

![Loop incremental de hardening](../0-assets/04-incremental-hardening-loop.svg)

---

## Enfoque incremental en 4 pasos

1. **Mapear riesgo**: identifica modulos mas sensibles (dinero, identidad, estados).
2. **Cerrar huecos criticos**: agrega tests para ramas de fallo y bordes de dominio.
3. **Fortalecer asserts**: valida comportamiento observable, no detalles internos fragiles.
4. **Automatizar guardrails**: aplica umbrales de coverage y ejecucion estable en CI.

---

## Ejemplo de criterio de prioridad

Prioridad alta:

- validaciones de entrada,
- transformaciones de datos de negocio,
- reglas condicionales con impacto economico,
- manejo de errores que afectan UX/API.

Prioridad baja:

- getters triviales,
- wrappers sin logica,
- codigo de bajo impacto con bajo riesgo.

---

## Anti-patrones a evitar

- Tests que solo validan que "no crashea".
- Snapshots gigantes sin foco.
- Assert unico y ambiguo para multiples reglas.
- Dependencia de reloj/sistema/red en unit tests.

---

## Ejemplo: de test debil a test que realmente protege

Un test debil suma cobertura pero no detecta regresiones: sigue en verde aunque se borre la logica interna. La prueba de fuego es simple: si reemplazas el cuerpo de la funcion por un valor fijo, ¿el test falla?

```javascript
// feeding-alert.js - decide si una especie del Acuario necesita alerta de alimentacion
function needsFeedingAlert(hoursSinceLastFeeding, species) {
  const thresholds = { shark: 48, jellyfish: 72, default: 24 };
  const limit = thresholds[species] ?? thresholds.default;
  return hoursSinceLastFeeding >= limit;
}

module.exports = { needsFeedingAlert };
```

**Antes (test debil):**

```javascript
test("needsFeedingAlert works", () => {
  const result = needsFeedingAlert(50, "shark");
  expect(typeof result).toBe("boolean");
});
```

Este test pasa aunque reemplaces toda la funcion por `return true;`. No verifica el valor esperado, ni el umbral por especie, ni el caso "no necesita alerta". Suma a `functions`/`lines` coverage sin aportar deteccion real.

**Despues (test fortalecido):**

```javascript
test("flags a shark not fed in 48+ hours", () => {
  expect(needsFeedingAlert(50, "shark")).toBe(true);
});

test("does not flag a shark fed within its threshold", () => {
  expect(needsFeedingAlert(40, "shark")).toBe(false);
});

test("falls back to the default threshold for unknown species", () => {
  expect(needsFeedingAlert(30, "otter")).toBe(true);
});
```

Ahora, si alguien hardcodea `return true;` o borra la logica de `thresholds`, el segundo test (caso `false`) falla de inmediato. La suite dejo de ser un contador de lineas ejecutadas y paso a ser una red de seguridad real.

---

## Heuristica de priorizacion: que modulo cubrir primero

No todo el codigo merece la misma inversion de tests. Cruza dos ejes: **frecuencia de cambio** (que tanto se toca el archivo) y **riesgo de negocio** (que tan grave es un defecto ahi).

| | Riesgo de negocio alto | Riesgo de negocio bajo |
|---|---|---|
| **Cambia seguido** | Prioridad 1: cubrir ya (ej. pricing, reservas, control de aforo) | Prioridad 3: cubrir cuando haya tiempo |
| **Cambia poco** | Prioridad 2: cubrir antes del proximo refactor grande | Prioridad 4: baja prioridad (helpers estables) |

Para estimar frecuencia de cambio sin adivinar, revisa el historial de commits:

```bash
git log --since="3 months ago" --name-only --pretty=format: -- src/ \
  | sort | uniq -c | sort -rn | head -10
```

Los archivos que aparecen mas veces son los que mas riesgo de regresion acumulan con cada cambio; si ademas manejan dinero, identidad o estados criticos, van primero en la lista de hardening.

---

## Resultado esperado al cerrar la semana

Una suite que no solo "cubre" codigo, sino que ofrece confianza operativa para cambiarlo y desplegar con menor riesgo.
