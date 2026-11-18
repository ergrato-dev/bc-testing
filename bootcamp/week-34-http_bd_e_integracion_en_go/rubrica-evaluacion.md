# Rúbrica de Evaluación - Semana 34

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | `httptest`, rutas del `ServeMux`, `TestMain`, build tags, `-short`, testcontainers-go y aislamiento de tests con base de datos |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento provocado y explicado |
| Producto | 30% | La API del dominio probada con `httptest` y su almacén probado contra PostgreSQL real, en dos suites separadas |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Diferencia `NewRecorder` de `NewServer`: qué se prueba con cada uno y qué pasa por la red.
- Explica por qué se comprueba `rec.Result()` y no `rec.Header()`, y por qué el contrato JSON se compara en un mapa.
- Explica qué hace `TestMain`, qué limita (una por paquete, sin `t`) y por qué `os.Exit` se salta los `defer`.
- Diferencia `-short` de una build tag y sabe comprobar qué tests compila cada combinación.
- Compara contenedor por test, contenedor por paquete con rollback y snapshot, y explica qué es una transacción abortada.

### Niveles

- **Alto (27-30):** explica cada concepto con un ejemplo correcto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** confunde la integración con los tests unitarios o no sabe dónde se aísla cada test.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: los cuatro pasos, la cabecera tras `WriteHeader`, `go 1.21` en el `go.mod`, la validación que falta y el campo JSON renombrado.
- Lab 02: el spy y los stubs del servidor, el plazo con `context`, HTTPS con `srv.Client()` y los cuatro experimentos.
- Lab 03: `TestMain`, `-short`, la tag `integration`, y los experimentos de `os.Exit`, `testing.Short`, `-count=2`, la tag mal escrita y las dos `TestMain`.
- Lab 04: las tres estrategias en orden, la transacción abortada con su savepoint, el contenedor huérfano y el `CHECK` que falta.

### Niveles

- **Alto (36-40):** los cuatro labs completos y cada experimento explicado.
- **Medio (28-35):** labs completos sin explicar alguno de los experimentos.
- **Bajo (<28):** labs incompletos.

---

## 3) Producto (30%)

### Criterios

- Los once requisitos de `./scripts/verificar-semana.sh 34` en verde.
- El contexto de la petición llega hasta la consulta SQL.
- Cada error del dominio tiene su código HTTP, probado en una tabla sin efectos laterales.
- Los tests de integración prueban el esquema (`CHECK`, claves) y la traducción de los errores de PostgreSQL, además de la lectura y la escritura.
- Un test con el servicio completo sobre PostgreSQL, y la limitación que deja sin probar anotada en el pull request.

### Niveles

- **Alto (27-30):** los once requisitos en verde, cada error traducido y probado, y la limitación explicada.
- **Medio (21-26):** los requisitos en verde con algún error del dominio o del esquema sin probar.
- **Bajo (<21):** algún requisito en rojo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - La API del dominio y su repositorio en PostgreSQL](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 34](README.md)
