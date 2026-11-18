# Rúbrica de Evaluación - Semana 33

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Interfaces del lado del consumidor, tipos de doble, mockgen, testify, go-cmp y golden files |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | El servicio del dominio en Go con un fake, un mock generado, `cmp.Diff`, un golden file y `dobles.md` |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica por qué en Go la interfaz se declara en el paquete que la usa y qué cuesta añadirle un método.
- Diferencia stub, spy, fake y mock con un ejemplo de cada uno, y explica cuándo basta con escribirlo a mano.
- Explica qué hace `go get -tool`, qué ejecuta `go generate` y por qué el mock generado se sube al repositorio.
- Diferencia `assert` de `require` y explica cuándo `assert` termina en un `panic`.
- Explica qué hace `cmpopts.IgnoreUnexported` y por qué un golden file se lee antes del commit.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde un fake con un mock, o no sabe de dónde sale el código del mock.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: el fake, el spy y los stubs, el test que falla con `time.Now()` y el stub que deja de compilar con una interfaz más grande.
- Lab 02: el mock generado, las cuatro expectativas, la llamada inesperada, la llamada que falta y el mock viejo regenerado.
- Lab 03: el test con `assert` y `require`, el `panic` del puntero nulo y los dos fallos de `testify/mock`.
- Lab 04: el diff de `cmp`, el `panic` del campo no exportado, `EquateEmpty`, los fallos sin semilla fija y el golden file regenerado.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 33` en verde.
- Interfaces con solo los métodos que usa la operación, y el reloj inyectado.
- Un fake que se comporta como el almacén real, incluidos sus errores.
- Un mock sin `EXPECT` en los casos rechazados, que demuestra que no hay efectos laterales.
- Un golden file con datos comprobados a mano y un `dobles.md` que justifica cada elección.

### Niveles

- **Alto (27-30):** los once requisitos en verde, cada doble justificado y un hallazgo del port explicado.
- **Medio (21-26):** los requisitos en verde con una justificación superficial o interfaces más grandes de lo necesario.
- **Bajo (<21):** algún requisito en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - El servicio del dominio con dobles de prueba](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 33](README.md)
