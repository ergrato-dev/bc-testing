# Solución Completa — BVA y Partición de Equivalencia

> Ejercicio 02 — Solution | Semana 02

---

## Partición de Equivalencia — Solución

### Campo: `nombre`

| Clase | Condición | Valor representativo | Tipo |
|---|---|---|---|
| CE1 | longitud < 2 | `"A"` (1 char) | Inválida |
| CE2 | 2 ≤ longitud ≤ 50, solo letras/espacios | `"Ana Ruiz"` | Válida |
| CE3 | longitud > 50 | 51 letras: `"Anaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1"` | Inválida |
| CE4 | contiene números | `"Ana123"` | Inválida |
| CE5 | contiene caracteres especiales | `"Ana@Ruiz"` | Inválida |
| CE6 | campo vacío | `""` | Inválida |

### Campo: `edad`

| Clase | Condición | Valor representativo | Tipo |
|---|---|---|---|
| CE1 | edad < 5 | `3` | Inválida |
| CE2 | 5 ≤ edad ≤ 99 | `30` | Válida |
| CE3 | edad > 99 | `110` | Inválida |
| CE4 | edad = 0 | `0` | Inválida |
| CE5 | edad negativa | `-5` | Inválida |
| CE6 | edad decimal | `12.5` | Inválida |

### Campo: `grupoEscolar` + regla combinada

| Clase | Condición | Valores | Tipo |
|---|---|---|---|
| CE1 | grupoEscolar=true, 3 ≤ edad ≤ 17 | true, 10 | Válida |
| CE2 | grupoEscolar=true, edad < 3 | true, 2 | Inválida |
| CE3 | grupoEscolar=true, edad > 17 | true, 18 | Inválida |
| CE4 | grupoEscolar=false, 5 ≤ edad ≤ 99 | false, 45 | Válida |
| CE5 | grupoEscolar=false, edad fuera de rango | false, 2 | Inválida |

---

## BVA — Solución

### Campo: `edad` (5–99)

| Valor | Posición | Resultado |
|---|---|---|
| 4 | Justo por debajo del mínimo | Rechazado |
| **5** | En el límite mínimo | **Aceptado** |
| 6 | Justo por encima del mínimo | Aceptado |
| 98 | Justo por debajo del máximo | Aceptado |
| **99** | En el límite máximo | **Aceptado** |
| 100 | Justo por encima del máximo | Rechazado |

### Campo: `nombre` — longitud (2–50)

| Longitud | Posición | Ejemplo | Resultado |
|---|---|---|---|
| 1 | Justo por debajo del mínimo | `"A"` | Rechazado |
| **2** | En el límite mínimo | `"Li"` | **Aceptado** |
| 3 | Justo por encima del mínimo | `"Ana"` | Aceptado |
| 49 | Justo por debajo del máximo | 49 letras | Aceptado |
| **50** | En el límite máximo | 50 letras | **Aceptado** |
| 51 | Justo por encima del máximo | 51 letras | Rechazado |

### Campo: `edad` con `grupoEscolar=true` (3–17)

| Valor | Posición | Resultado |
|---|---|---|
| 2 | Justo por debajo del mínimo escolar | Rechazado |
| **3** | En el límite mínimo escolar | **Aceptado** |
| 4 | Justo por encima del mínimo escolar | Aceptado |
| 16 | Justo por debajo del máximo escolar | Aceptado |
| **17** | En el límite máximo escolar | **Aceptado** |
| 18 | Justo por encima del máximo escolar | Rechazado |

---

## Suite Mínima — Solución (19 casos)

```
ID: TC-PLANET-REG-001
Título: Happy path — visitante adulto válido sin grupo escolar
Técnica: CE2 nombre + CE2 edad + CE4 grupoEscolar
Datos: nombre="Carlos Pérez", edad=30, grupoEscolar=false
Resultado esperado: Registro exitoso, retorna objeto con ID generado

ID: TC-PLANET-REG-002
Título: Happy path — visitante escolar válido (grupo, 10 años)
Técnica: CE2 nombre + CE1 grupoEscolar + CE2 edad (escolar)
Datos: nombre="Luis Torres", edad=10, grupoEscolar=true
Resultado esperado: Registro exitoso con flag grupoEscolar activo

ID: TC-PLANET-REG-003
Título: Nombre de 1 carácter debe ser rechazado
Técnica: CE1 nombre (longitud < 2)
Datos: nombre="A", edad=30, grupoEscolar=false
Resultado esperado: Error "El nombre debe tener entre 2 y 50 caracteres"

ID: TC-PLANET-REG-004
Título: Nombre de 51 caracteres debe ser rechazado
Técnica: CE3 nombre (longitud > 50)
Datos: nombre=51 letras, edad=30, grupoEscolar=false
Resultado esperado: Error de longitud máxima

ID: TC-PLANET-REG-005
Título: Nombre con número debe ser rechazado
Técnica: CE4 nombre
Datos: nombre="Ana123", edad=30, grupoEscolar=false
Resultado esperado: Error "El nombre solo puede contener letras y espacios"

ID: TC-PLANET-REG-006
Título: Nombre con carácter especial debe ser rechazado
Técnica: CE5 nombre
Datos: nombre="Ana@Ruiz", edad=30, grupoEscolar=false
Resultado esperado: Error de caracteres no permitidos

ID: TC-PLANET-REG-007
Título: Nombre vacío debe ser rechazado
Técnica: CE6 nombre
Datos: nombre="", edad=30, grupoEscolar=false
Resultado esperado: Error "El nombre es requerido"

ID: TC-PLANET-REG-008
Título: Edad menor a 5 debe ser rechazada
Técnica: CE1 edad
Datos: nombre="Ana Ruiz", edad=3, grupoEscolar=false
Resultado esperado: Error "La edad debe estar entre 5 y 99"

ID: TC-PLANET-REG-009
Título: Edad mayor a 99 debe ser rechazada
Técnica: CE3 edad
Datos: nombre="Ana Ruiz", edad=110, grupoEscolar=false
Resultado esperado: Error "La edad debe estar entre 5 y 99"

ID: TC-PLANET-REG-010
Título: Edad 0 debe ser rechazada
Técnica: CE4 edad
Datos: nombre="Ana Ruiz", edad=0, grupoEscolar=false
Resultado esperado: Error de rango

ID: TC-PLANET-REG-011
Título: Edad negativa debe ser rechazada
Técnica: CE5 edad
Datos: nombre="Ana Ruiz", edad=-5, grupoEscolar=false
Resultado esperado: Error de rango

ID: TC-PLANET-REG-012
Título: Edad decimal debe ser rechazada
Técnica: CE6 edad
Datos: nombre="Ana Ruiz", edad=12.5, grupoEscolar=false
Resultado esperado: Error "La edad debe ser un número entero"

ID: TC-PLANET-REG-013
Título: GrupoEscolar=true con edad 18 debe ser rechazado
Técnica: CE3 grupoEscolar
Datos: nombre="Ana Ruiz", edad=18, grupoEscolar=true
Resultado esperado: Error "Los grupos escolares deben tener entre 3 y 17 años"

ID: TC-PLANET-REG-014 — BVA edad mínima (límite inferior 5)
Título: Edad 4 (justo debajo del mínimo) debe ser rechazada
Datos: nombre="Ana Ruiz", edad=4, grupoEscolar=false
Resultado esperado: Rechazado

ID: TC-PLANET-REG-015 — BVA on point inferior
Título: Edad 5 (límite mínimo exacto) debe ser aceptada
Datos: nombre="Ana Ruiz", edad=5, grupoEscolar=false
Resultado esperado: Aceptado

ID: TC-PLANET-REG-016 — BVA on point superior
Título: Edad 99 (límite máximo exacto) debe ser aceptada
Datos: nombre="Ana Ruiz", edad=99, grupoEscolar=false
Resultado esperado: Aceptado

ID: TC-PLANET-REG-017 — BVA edad máxima
Título: Edad 100 (justo encima del máximo) debe ser rechazada
Datos: nombre="Ana Ruiz", edad=100, grupoEscolar=false
Resultado esperado: Rechazado

ID: TC-PLANET-REG-018 — BVA nombre mínimo
Título: Nombre de 2 caracteres (límite mínimo) debe ser aceptado
Datos: nombre="Li", edad=30, grupoEscolar=false
Resultado esperado: Aceptado

ID: TC-PLANET-REG-019 — BVA grupoEscolar on point máximo
Título: GrupoEscolar=true con edad 17 (límite máximo) debe ser aceptado
Datos: nombre="Ana Ruiz", edad=17, grupoEscolar=true
Resultado esperado: Aceptado
```

---

## Conteo final

| Técnica | Casos generados |
|---|---|
| Partición de equivalencia | 13 |
| BVA (edad 5–99) | 4 nuevos |
| BVA (nombre longitud) | 1 nuevo (el de 2 chars; el de 51 ya estaba en CE3) |
| BVA (edad grupoEscolar 3–17) | 1 nuevo (edad 17) |
| **Total** | **19** |
