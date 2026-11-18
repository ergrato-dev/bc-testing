# Rúbrica de Evaluación - Semana 32

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | `go test`, el paquete `testing`, table-driven tests, subtests, helpers, caja negra y examples |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | Reglas del dominio en Go con tablas, examples y la comparativa con Jest, pytest y JUnit |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica cuándo usar `t.Fatalf` y cuándo `t.Errorf`, con un ejemplo.
- Explica qué hace `t.Run` que no hace un bucle solo, y escribe el `-run` que ejecuta una fila.
- Explica por qué una tabla en paralelo con `go 1.21` en el `go.mod` puede pasar con una fila mal.
- Diferencia `defer` de `t.Cleanup` dentro de un helper, y explica qué cambia `t.Helper`.
- Diferencia `package x` de `package x_test` y explica cuándo un example no se ejecuta.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde `t.Errorf` con `t.Fatalf`, o no distingue un test de un subtest.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los tres tests, la caché con `-count=1`, el aviso de `go vet` y el test `CheckXxx` que nadie ejecuta.
- Lab 02: la tabla de siete filas, los filtros con `-run` anclado, `t.Parallel` y la fila mal que pasa con `go 1.21`.
- Lab 03: el directorio que deja el PASO 0, los helpers con `t.Helper`, el archivo cerrado por `defer` y el contexto cancelado.
- Lab 04: los dos tipos de paquete de test, el `undefined: catalog.normalize` y los tres fallos de un example.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los nueve requisitos de `./scripts/verificar-semana.sh 32` en verde.
- Errores con nombre (`errors.New`) comparados con `errors.Is`, y fronteras reales del dominio en las tablas.
- Un caso con caracteres no ASCII en la longitud máxima del nombre.
- Una `comparativa.md` con código de los cuatro lenguajes y al menos un hallazgo propio del port.

### Niveles

- **Alto (27-30):** los nueve requisitos en verde, fronteras completas y una comparativa con un hallazgo explicado.
- **Medio (21-26):** los requisitos en verde con fronteras incompletas o una comparativa superficial.
- **Bajo (<21):** algún requisito en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Las reglas del dominio en Go](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 32](README.md)
