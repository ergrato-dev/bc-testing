# Rúbrica de Evaluación - Semana 29

## Evidencias y ponderación

| Tipo de evidencia | Ponderación | Descripción |
|---|---:|---|
| Conocimiento | 30% | Testcontainers y su ciclo de vida, esquema con migraciones, `@ServiceConnection`, rollback por test, contenedores compartidos, Kafka |
| Desempeño | 40% | Labs 01 a 04 completos, con cada experimento ejecutado y su salida explicada |
| Producto | 30% | Los repositorios del dominio en PostgreSQL con Flyway, probados con Testcontainers y con tests de contrato que también pasa el fake |

---

## Criterios por evidencia

## 1) Conocimiento (30%)

### Criterios

- Explica qué hace Testcontainers, qué necesita para funcionar y qué hace Ryuk.
- Diferencia `@Container static`, `@Container` sin `static`, un contenedor como `@Bean` y un singleton, y dice cuántos contenedores arranca cada uno.
- Explica por qué los tests usan la misma imagen que producción y por qué no se usa H2.
- Explica por qué un esquema con `withInitScript` se desfasa y cómo lo evita Flyway.
- Diferencia `@DynamicPropertySource` de `@ServiceConnection`.
- Explica cuándo `@JdbcTest` deshace un test y cuándo no.
- Explica qué garantiza Kafka sobre el orden y por qué un consumo asíncrono necesita un plazo.

### Niveles

- **Alto (27-30):** explica cada concepto con una salida de los labs o del proyecto.
- **Medio (21-26):** comprende los conceptos con una o dos confusiones menores.
- **Bajo (<21):** no distingue un contenedor por test de uno por clase, o cree que un fake en memoria prueba el SQL.

---

## 2) Desempeño (40%)

### Criterios

- Lab 01: arranca PostgreSQL con `GenericContainer` y con el módulo, y mide el coste de un contenedor por test.
- Lab 02: prueba las restricciones por SQLState y aplica las migraciones con Flyway.
- Lab 03: conecta el contenedor con `@DynamicPropertySource` y con `@ServiceConnection`, y comparte un contenedor entre dos clases.
- Lab 04: prueba el orden por clave en un topic con tres particiones y comparte un broker con el patrón singleton.
- Ejecuta cada experimento, lee el error o el tiempo que produce y lo explica.
- Deja `docker ps` limpio al terminar.

### Niveles

- **Alto (36-40):** los cuatro labs y todos los experimentos, con la explicación de cada salida.
- **Medio (28-35):** los cuatro labs con algún experimento sin hacer o sin explicar.
- **Bajo (<28):** labs incompletos o PASOS descomentados sin ejecutar.

---

## 3) Producto (30%)

### Criterios

- Los repositorios del dominio guardan en PostgreSQL con `JdbcClient`; ninguno guarda en memoria en `src/main`.
- El esquema sale de migraciones de Flyway, con las reglas del dominio repetidas como restricciones (`CHECK`, `NOT NULL`, claves foráneas).
- Los tests usan la misma imagen de PostgreSQL que `compose.yaml`, conectada con `@ServiceConnection`.
- Hay tests de contrato que pasan el fake en memoria y el repositorio JDBC.
- Al menos un test por restricción de la base de datos que el fake no tiene.
- Una quinta regla de ArchUnit mantiene el SQL dentro de `infrastructure`.
- La API arranca contra el PostgreSQL de `compose.yaml` y los datos sobreviven a un reinicio.
- `PROYECTO=... ./scripts/verificar-semana.sh 29` pasa.

### Niveles

- **Alto (27-30):** cumple todos los criterios y documenta en el pull request un sabotaje que el fake no detecta y PostgreSQL sí.
- **Medio (21-26):** cumple los criterios con algún test de restricción o de contrato ausente.
- **Bajo (<21):** los tests siguen usando el repositorio en memoria, o la suite necesita un PostgreSQL arrancado a mano.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto — Proyecto Semanal - Los repositorios del dominio en PostgreSQL](3-proyecto/README.md) | [Recursos — ebooks](4-recursos/ebooks-free/README.md) → |

[↑ Semana 29](README.md)
