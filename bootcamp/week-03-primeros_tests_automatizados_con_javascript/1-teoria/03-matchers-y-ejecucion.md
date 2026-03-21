# Matchers Básicos y Ejecución de Pruebas

> **Semana 03 — Teoría 03** | Lenguaje: JavaScript

---

## ¿Qué es un matcher?

En Jest, un matcher es el método que sigue a `expect(...)` y define la comparación esperada.

```javascript
expect(value).toBe(expected);
```

Un matcher correcto comunica intención y evita falsos positivos.

---

## Matchers fundamentales

### `toBe` — igualdad estricta (`===`)

```javascript
expect(2 + 2).toBe(4);
```

Usar con primitivos: número, string, boolean, `null`, `undefined`.

### `toEqual` — igualdad estructural profunda

```javascript
expect({ name: "Ana" }).toEqual({ name: "Ana" });
```

Usar con objetos y arreglos.

### `toBeTruthy` / `toBeFalsy`

```javascript
expect("text").toBeTruthy();
expect(0).toBeFalsy();
```

Útil para validaciones booleanas generales.

### `toBeNull`

```javascript
expect(result).toBeNull();
```

### `toThrow`

```javascript
expect(() => divide(10, 0)).toThrow("Division by zero");
```

Importante: siempre pasar una **función** a `expect` cuando se espera excepción.

---

## Error de ejecución vs assertion failure

### Assertion failure

El código ejecuta, pero el resultado no coincide con la expectativa.

```text
Expected: 5
Received: 6
```

### Error de ejecución

El test explota antes de llegar al `expect`.

```text
TypeError: Cannot read properties of undefined
```

Diagnóstico rápido:

- Si hay `Expected/Received` -> revisar lógica y assertion
- Si hay stack trace de runtime -> revisar setup, imports, null/undefined

---

## Comandos clave con `pnpm`

```bash
pnpm test
pnpm test -- --watch
pnpm test -- --runInBand
pnpm test -- --testNamePattern="should return"
```

Notas:

- `--watch`: reejecuta tests al guardar cambios
- `--runInBand`: útil para depuración secuencial
- `--testNamePattern`: filtra por nombre de test

---

## Ciclo Red-Green-Refactor (introducción)

Aunque se verá más adelante en profundidad, esta semana se practica su forma mínima:

1. **Red**: escribir un test que falle
2. **Green**: escribir el código mínimo para pasar
3. **Refactor**: mejorar código sin romper tests

Ejemplo breve:

```javascript
// Red: test falla porque función no existe
// Green: implementar función mínima
function isEven(n) {
  return n % 2 === 0;
}
// Refactor: mejorar nombres o extracción si hace falta
```

---

## Checklist para una suite inicial sana

- [ ] Tests tienen nombres descriptivos
- [ ] Cada test verifica una intención principal
- [ ] Se usan matchers apropiados
- [ ] No hay dependencias externas reales
- [ ] La suite corre localmente con `pnpm test`

---

## Cierre de la semana

Con esta base ya puedes escribir tests unitarios simples en Jest. En la semana 04 repetirás los mismos conceptos en Python para comparar estilo y sintaxis.

← [Estructura de un test en Jest](./02-estructura-test-jest.md) | [Volver al README](../README.md)
