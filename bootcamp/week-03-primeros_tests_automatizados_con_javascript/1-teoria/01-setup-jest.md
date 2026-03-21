# Setup del Entorno con Node.js y Jest

> **Semana 03 — Teoría 01** | Lenguaje: JavaScript

---

## Objetivo

Configurar un entorno mínimo y profesional para ejecutar tests unitarios con Jest usando `pnpm` (o `yarn`).

---

## Requisitos previos

- Node.js 20 LTS instalado
- `pnpm` instalado globalmente (`corepack enable` + `corepack prepare pnpm@latest --activate`)
- Editor VS Code

Verificación rápida:

```bash
node -v
pnpm -v
```

---

## Estructura recomendada

```text
mi-proyecto/
├── package.json
├── src/
│   └── calculator.js
└── tests/
    └── calculator.test.js
```

---

## Inicialización del proyecto

```bash
mkdir mi-proyecto
cd mi-proyecto
pnpm init
pnpm add -D jest@29
```

Agregar scripts en `package.json`:

```json
{
  "scripts": {
    "test": "jest",
    "test:watch": "jest --watch"
  }
}
```

---

## Primer módulo de ejemplo

`src/calculator.js`

```javascript
function add(a, b) {
  return a + b;
}

function divide(a, b) {
  if (b === 0) {
    throw new Error("Division by zero");
  }
  return a / b;
}

module.exports = { add, divide };
```

---

## Primer test de ejemplo

`tests/calculator.test.js`

```javascript
const { add, divide } = require("../src/calculator");

describe("calculator", () => {
  test("should return 5 when adding 2 and 3", () => {
    // Arrange
    const a = 2;
    const b = 3;

    // Act
    const result = add(a, b);

    // Assert
    expect(result).toBe(5);
  });

  test("should throw error when dividing by zero", () => {
    expect(() => divide(10, 0)).toThrow("Division by zero");
  });
});
```

---

## Ejecutar la suite

```bash
pnpm test
```

Salida esperada (resumen):

```text
PASS tests/calculator.test.js
  calculator
    ✓ should return 5 when adding 2 and 3
    ✓ should throw error when dividing by zero
```

---

## Errores comunes al configurar Jest

| Error | Causa probable | Solución |
|---|---|---|
| `jest: command not found` | Jest no instalado como dev dependency | `pnpm add -D jest@29` |
| No encuentra tests | Nombre o ruta no coincide | Usar sufijo `.test.js` en carpeta `tests/` |
| `Cannot find module` | Ruta de import incorrecta | Revisar `../src/...` |
| Tests no corren tras cambios | Watch no activo | Ejecutar `pnpm test:watch` |

---

## Buenas prácticas desde el inicio

- Crear tests para funciones puras primero
- Mantener cada test enfocado en un comportamiento
- Evitar dependencias externas en tests unitarios
- Nombrar tests como documentación viva

---

## Próximo tema

→ [Estructura de un test en Jest (AAA)](./02-estructura-test-jest.md)
