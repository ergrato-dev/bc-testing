# 01 - Fundamentos de Cobertura en Jest

## Objetivo

Entender que mide cobertura en Jest y como usarla para observar huecos de prueba sin caer en falsas certezas.

![Mapa de métricas de coverage](../0-assets/01-coverage-metrics-map.svg)

---

## Qué es cobertura

Cobertura es una métrica que indica qué porciones del código fueron ejecutadas durante los tests.

En Jest, las métricas más usadas son:

- `statements`: sentencias ejecutadas.
- `lines`: líneas ejecutadas.
- `functions`: funciones invocadas.
- `branches`: ramas evaluadas (if/else, ternarios, cortocircuitos).

---

## Comando base en Jest

```bash
pnpm install
pnpm test:coverage
```

También puedes apuntar a un archivo:

```bash
pnpm install
pnpm test:coverage pricing.service.test.js
```

---

## Ejemplo breve

```javascript
function calculateFee(amount, isMember) {
  if (amount <= 0) {
    throw new Error("Invalid amount");
  }

  if (isMember) {
    return amount * 0.9;
  }

  return amount;
}
```

Si solo pruebas `isMember = true`, tendrás cobertura de función y líneas altas, pero `branches` incompleta: no validaste el camino de no miembro ni el error.

---

## Regla práctica

Cobertura responde: "que se ejecutó".
Calidad responde: "que tan bien detecta defectos".
Necesitas ambas.

---

## Recomendaciones iniciales

1. Revisa cobertura por módulo, no solo porcentaje global.
2. Prioriza rutas con validaciones, reglas de negocio y manejo de errores.
3. Usa cobertura para descubrir huecos, luego decide si tienen riesgo real.
