# Suite de Casos de Prueba — [Tu Dominio Aquí]

> Proyecto Semana 02 | Aprendiz: [Tu nombre] | Dominio: [Tu dominio asignado]

---

## 1. Funcionalidad bajo prueba

**Nombre de la funcionalidad**: _(completa aquí)_

**Descripción breve**:
_(¿Qué hace esta funcionalidad? ¿Para qué sirve en tu dominio?)_

**Reglas de negocio**:

| Campo | Tipo | Restricciones |
|---|---|---|
| TODO: campo 1 | (texto/número/boolean) | TODO: describe las reglas |
| TODO: campo 2 | (texto/número/boolean) | TODO: describe las reglas |
| TODO: campo 3 | (texto/número/boolean) | TODO: describe las reglas |

---

## 2. Partición de Equivalencia

### Campo: [nombre del campo 1]

| Clase | Condición | Valor representativo | Tipo |
|---|---|---|---|
| CE1 | TODO | TODO | (válida/inválida) |
| CE2 | TODO | TODO | |
| CE3 | TODO | TODO | |

### Campo: [nombre del campo 2]

| Clase | Condición | Valor representativo | Tipo |
|---|---|---|---|
| CE1 | TODO | TODO | |
| CE2 | TODO | TODO | |

_(agrega más campos si aplica)_

---

## 3. Análisis de Valores Límite (BVA)

### Campo: [campo con rango numérico o de longitud]

Límite inferior: **TODO**
Límite superior: **TODO**

| Valor | Posición | Resultado esperado |
|---|---|---|
| TODO | Justo por debajo del mínimo | |
| TODO | En el límite mínimo (on point) | |
| TODO | Justo por encima del mínimo | |
| TODO | Justo por debajo del máximo | |
| TODO | En el límite máximo (on point) | |
| TODO | Justo por encima del máximo | |

_(agrega una tabla por cada campo con rango)_

---

## 4. Suite de Casos de Prueba

<!-- INSTRUCCIONES:
  - Mínimo 10 casos, máximo 20
  - Al menos 3 happy path
  - Al menos 4 sad path / edge cases de partición
  - Al menos 4 casos BVA en los límites
  - Sin casos redundantes
  - ID en formato TC-[ABREVIATURA DOMINIO]-[MÓDULO]-[NNN]
-->

### TC-[DOM]-[MOD]-001 — Happy Path

```
ID:              TC-[DOM]-[MOD]-001
Título:          TODO: describe el escenario principal exitoso
Técnica:         Partición — CE válida
Precondiciones:
  - TODO: describe el estado del sistema

Datos de prueba:
  - [campo 1]: TODO
  - [campo 2]: TODO

Pasos:
  1. TODO
  2. TODO
  3. TODO

Resultado esperado:
  - TODO: describe qué debe verse o retornar exactamente

Severidad: TODO
Prioridad:  TODO
```

### TC-[DOM]-[MOD]-002 — Sad Path: campo inválido

```
ID:              TC-[DOM]-[MOD]-002
Título:          TODO
Técnica:         Partición — CE inválida
Precondiciones:
  - TODO

Datos de prueba:
  - [campo]: TODO (valor de clase inválida)

Pasos:
  1. TODO
  2. TODO
  3. TODO

Resultado esperado:
  - TODO: mensaje de error específico y observable

Severidad: TODO
Prioridad:  TODO
```

### TC-[DOM]-[MOD]-003 — BVA: límite inferior

```
ID:              TC-[DOM]-[MOD]-003
Título:          TODO: valor justo debajo del límite inferior debe ser rechazado
Técnica:         BVA — off point inferior
Precondiciones:
  - TODO

Datos de prueba:
  - [campo]: TODO (límite - 1)

Pasos:
  1. TODO
  2. TODO

Resultado esperado:
  - TODO

Severidad: TODO
Prioridad:  TODO
```

<!-- CONTINÚA AGREGANDO CASOS HASTA COMPLETAR EL MÍNIMO DE 10 -->

### TC-[DOM]-[MOD]-004

```
ID:              TC-[DOM]-[MOD]-004
Título:          TODO
Técnica:
Precondiciones:

Datos de prueba:

Pasos:
  1.
  2.

Resultado esperado:

Severidad:
Prioridad:
```

---

## 5. Resumen de cobertura

| Técnica | Nro. de casos |
|---|---|
| Happy path (partición válida) | TODO |
| Sad path (partición inválida) | TODO |
| BVA (límites) | TODO |
| **Total** | **TODO** |

---

## 6. Reflexión

_(Escribe 3-5 líneas sobre lo que aprendiste diseñando esta suite:
¿Qué caso inesperado encontraste al aplicar BVA? ¿Qué regla de negocio fue más difícil de modelar?)_
