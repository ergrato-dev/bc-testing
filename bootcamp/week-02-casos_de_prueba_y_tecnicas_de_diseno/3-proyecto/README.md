# Proyecto - Suite de casos de prueba

## Objetivo

Diseñar la suite de casos de prueba manuales de una funcionalidad de tu dominio con partición de equivalencia, valores límite y, si la funcionalidad lo pide, una tabla de decisión. La suite tiene que poder ejecutarla otra persona sin preguntarte.

## Tiempo estimado

3 horas.

## Dominio

Trabajas con el dominio que te asignó el instructor. Elige tú la funcionalidad: una con campos que tengan rangos (números, longitudes o fechas) y, mejor aún, una regla que combine dos campos. Un formulario de alta, de reserva o de pedido suele servir.

Los ejemplos de este README usan un **acuario virtual** como dominio de ejemplo.

## Punto de partida

```bash
mkdir -p ~/proyecto-semana-02
cd ~/proyecto-semana-02
cp <ruta-del-bootcamp>/bootcamp/week-02-casos_de_prueba_y_tecnicas_de_diseno/3-proyecto/starter/casos-de-prueba.md .
```

[`casos-de-prueba.md`](starter/casos-de-prueba.md) tiene seis secciones con `TODO`. Respeta el formato de las tablas y de las líneas `ID:`, `Técnica:` y demás: las comprobaciones automáticas las buscan tal cual.

---

## Paso 1: la funcionalidad y sus reglas (30 min)

Rellena **Dominio**, **Funcionalidad** y la tabla de reglas. Cada regla con sus números y el mensaje que da el sistema cuando no se cumple. Como tu sistema todavía no existe, las reglas las decides tú; escríbelas como si fueran el requisito que te ha dado negocio.

```text
| R2 | El número de personas es un entero de 1 a 8. Mensaje: «Entre 1 y 8 personas» |
```

Al menos tres reglas. Si añades filas, añade también su fila en la matriz de la sección 6.

## Paso 2: partición (25 min)

Una fila por clase, con el campo, la condición, un valor concreto y `Válida` o `Inválida`. Al menos 2 válidas y 3 inválidas. Saca las clases de cada regla, como en el [lab 02](../2-practicas/lab-02-particion-y-valores-limite/README.md): por los dos lados del rango, vacío, tipo incorrecto.

## Paso 3: valores límite (25 min)

Al menos 6 filas, con los vecinos de fuera del mínimo y del máximo. Aplica la variante de 3 valores al campo más importante. En el resto puedes usar la de 2 valores si la suite pasa de 20 casos; si lo haces, di por qué en una línea encima de la tabla.

La columna **Posición** usa exactamente las seis expresiones que lista la plantilla.

## Paso 4: tabla de decisión y casos (75 min)

Si una regla combina condiciones, escribe su tabla en la sección 4 como en el [lab 03](../2-practicas/lab-03-tabla-de-decision/README.md). Si no, sustituye la tabla por una línea que explique por qué no hace falta.

Después escribe de 10 a 20 casos en la sección 5:

- ID con el formato `TC-<DOMINIO>-<MÓDULO>-<NNN>`, en mayúsculas y sin tildes: `TC-ACUA-TOUR-006`.
- **Técnica:** `Partición`, `Valores límite` o `Tabla de decisión`. Al menos 4 casos de valores límite.
- **Regla:** los IDs de las reglas que prueba.
- Precondiciones comprobables, pasos de una acción, esperado observable y prioridad `Alta`, `Media` o `Baja`. Repasa la [teoría 01](../1-teoria/01-casos-de-prueba.md).
- Una clase inválida por caso.

```text
ID:                 TC-ACUA-TOUR-006
Título:             Reserva para 9 personas se rechaza
Técnica:            Valores límite
Regla:              R2
Precondiciones:
  - La fecha del entorno de pruebas es 2026-11-18
  - El tour de la fecha elegida tiene 20 plazas libres
Datos de prueba:
  - nombre: Ana Ruiz
  - personas: 9
  - tipo: Individual
  - fecha: 2026-11-28
Pasos:
  1. Abre http://localhost:3000/tours/nueva
  2. Escribe el nombre en "Nombre del titular"
  3. Escribe el número en "Personas"
  4. Elige el tipo en "Tipo de tour"
  5. Elige la fecha en "Fecha"
  6. Pulsa "Reservar"
Resultado esperado:
  - No se crea la reserva; bajo "Personas" aparece «Entre 1 y 8 personas»
Prioridad:          Alta
```

Fíjate en la primera precondición: con reglas de fechas, el día de hoy tiene que ser fijo, o el caso da un resultado distinto cada día.

## Paso 5: trazabilidad y revisión (25 min)

En la sección 6, cada regla con los casos que la prueban. Una regla sin casos es lo primero que hay que arreglar.

Después, pasa las comprobaciones:

```bash
cd <ruta-del-bootcamp>
PROYECTO=~/proyecto-semana-02 ./scripts/verificar-semana.sh 02
```

Cada comprobación que falla dice el paso de este README donde se arregla.

---

## ✅ Requisitos verificables

Los comprueba `scripts/verificar-semana.sh` con [checks.json](../checks.json):

| Comprobación | Qué exige |
|---|---|
| `completo` | `casos-de-prueba.md` existe y no le queda ningún `TODO` |
| `reglas` | Dominio, funcionalidad y al menos tres reglas con sus números |
| `particion` | Al menos 2 clases válidas y 3 inválidas en la partición |
| `limites` | Al menos 6 valores límite, con los vecinos de fuera del mínimo y del máximo |
| `casos` | De 10 a 20 casos con IDs `TC-<DOMINIO>-<MÓDULO>-<NNN>` sin repetir |
| `campos` | Cada caso tiene título, técnica, regla, precondiciones, pasos, esperado y prioridad; al menos 4 casos de valores límite; ningún esperado dice «funciona bien» |
| `trazabilidad` | Cada regla de la sección 1 tiene al menos un caso en la matriz de trazabilidad |

Las comprobaciones miran la forma. Lo que no pueden mirar (que los valores sean los límites de verdad, que cada caso tenga una sola clase inválida, que otra persona pueda ejecutarlo) lo evalúa la [rúbrica](../rubrica-evaluacion.md).

## Entrega

`casos-de-prueba.md`, por el canal que indique el instructor.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Exploratorio y regresión](../2-practicas/lab-04-exploratorio-y-regresion/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 02](../README.md)
