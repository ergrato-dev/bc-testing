# Proyecto hilo conductor: QA Kit del dominio

**Qué:** un repositorio propio con el sistema de tu dominio y todas sus suites de pruebas, en los cinco lenguajes.

**Para qué:** que cada semana sume una pieza real y que al terminar tengas un portafolio de QA/SDET que puedas mostrar.

**Impacto:** el proyecto de cada semana parte de lo que entregaste la anterior. Si una semana queda floja, la siguiente lo nota.

El dominio lo asigna el instructor (Política de Dominios Únicos, ver [README](../README.md)). Dos aprendices de la misma ficha no repiten dominio, y copiar el proyecto de otro dominio anula la entrega.

## Estructura final (S46)

```
qa-<nombre>-<apellido>/
├── README.md               # dominio, cómo correr cada suite, estado de coverage
├── testing-strategy.md     # estrategia y decisiones (S46)
├── compose.yaml            # PostgreSQL y servicios para integración/E2E
├── js/                     # Etapa 1: lógica + API Express, Jest + Supertest
├── python/                 # Etapa 2: lógica + cliente HTTP + BD, pytest + Behave
├── java/                   # Etapa 3: API Spring Boot, JUnit 6 + Mockito + Cucumber
├── go/                     # Etapa 4: servicio Go, go test + testcontainers-go
├── rust/                   # Etapa 5: servicio Rust, cargo test + sqlx
├── e2e/                    # S42: Playwright (pnpm)
├── perf/                   # S43: k6 y Locust
├── security/               # S44: tests de seguridad y reportes
└── .github/workflows/      # S45: un workflow por lenguaje
```

| Etapa | Semanas | Qué crece |
|---|---|---|
| 0 Fundamentos | 1–6 | `test-plan.md` y las primeras funciones del dominio en `js/`, `python/` y `java/` |
| 1 JavaScript | 7–15 | `js/`: suites organizadas, async, mocks, TDD y una API Express con Supertest |
| 2 Python | 16–24 | `python/`: pytest, mocks, cliente HTTP, TDD, BDD y capa de datos sobre PostgreSQL |
| 3 Java | 25–31 | `java/`: API Spring Boot con slices, Testcontainers y Cucumber |
| 4 Go | 32–36 | `go/`: servicio del dominio con `httptest`, testcontainers-go, fuzz y benchmarks |
| 5 Rust | 37–41 | `rust/`: servicio axum con `#[sqlx::test]`, `proptest` y `criterion` |
| 6 Transversal | 42–46 | `e2e/`, `perf/`, `security/`, CI y `testing-strategy.md` sobre todo lo anterior |

## Reglas

- **Un repositorio por aprendiz:** `qa-<nombre>-<apellido>`.
- **Entregable:** el proyecto es el único entregable de cada semana. Los labs de `2-practicas/` son guiados y no se entregan.
- **Ramas:** una rama y un *pull request* por semana (`week-NN` → `main`).
- **Punto de partida:** el `3-proyecto/starter/` de cada semana trae solo las piezas nuevas de esa semana, con `TODO` genéricos adaptables al dominio. Se integran en la carpeta del lenguaje correspondiente, sobre lo que ya entregaste.
- **Autograding:** `PROYECTO=/ruta/a/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh NN` comprueba tu repositorio antes de abrir el PR ([autograding.md](autograding.md)).
- **Herramientas:** nombres de código en inglés; datos y textos visibles en el idioma del dominio. JS siempre con `pnpm`, Python siempre con `uv`. Base de datos siempre PostgreSQL.
- **Secretos:** van en `.env`, nunca en el repositorio. `.env.example` sí se sube.

## Alcance

Este formato se aplica desde S23. En S01–S22 cada proyecto semanal es independiente. Al empezar S23, el estudiante copia sus entregas de JS y Python en `js/` y `python/` del repositorio nuevo.

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Decisión de duración](decision-semanas.md) | [Autograding](autograding.md) → |

← [Volver al inicio del bootcamp](../README.md)
