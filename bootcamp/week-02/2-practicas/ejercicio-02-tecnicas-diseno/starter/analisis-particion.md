# Análisis de Partición de Equivalencia

> Ejercicio 02 — Starter | Paso 1

Completa la tabla para cada campo de `registrarVisitante(nombre, edad, grupoEscolar)`.

---

## Campo: `nombre` (texto, 2–50 caracteres, solo letras y espacios)

| Clase | Condición | Valor representativo | Tipo (válida/inválida) |
|---|---|---|---|
| CE1 | longitud < 2 | (completa) | |
| CE2 | 2 ≤ longitud ≤ 50, solo letras/espacios | (completa) | |
| CE3 | longitud > 50 | (completa) | |
| CE4 | contiene números | (completa) | |
| CE5 | contiene caracteres especiales | (completa) | |
| CE6 | campo vacío | (completa) | |

---

## Campo: `edad` (entero, 5–99)

| Clase | Condición | Valor representativo | Tipo (válida/inválida) |
|---|---|---|---|
| CE1 | edad < 5 | (completa) | |
| CE2 | 5 ≤ edad ≤ 99 | (completa) | |
| CE3 | edad > 99 | (completa) | |
| CE4 | edad = 0 | 0 | |
| CE5 | edad negativa | (completa) | |
| CE6 | edad no entera (decimal) | (completa) | |

---

## Campo: `grupoEscolar` (booleano) + regla combinada con `edad`

| Clase | Condición | Valores | Tipo (válida/inválida) |
|---|---|---|---|
| CE1 | grupoEscolar=true, 3 ≤ edad ≤ 17 | (completa) | |
| CE2 | grupoEscolar=true, edad < 3 | (completa) | |
| CE3 | grupoEscolar=true, edad > 17 | (completa) | |
| CE4 | grupoEscolar=false, 5 ≤ edad ≤ 99 | (completa) | |
| CE5 | grupoEscolar=false, edad fuera de 5–99 | (completa) | |

---

## Reflexión

¿Cuántos casos de prueba necesitas como mínimo para cubrir todas las clases? ¿Por qué?

_(escribe tu respuesta aquí)_
