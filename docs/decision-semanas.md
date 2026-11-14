# ADR: duración, ritmo y solapes

- **Dedicación:** 10 h por semana (antes 8 h; cambio de noviembre de 2026, alineado con los bootcamps recientes, que usan 10–18 h). La tabla de tiempos de cada README de semana suma exactamente 10 h; si suma más, se recorta alcance. En S01–S22, construidas con 8 h, las 2 h nuevas van a práctica (+1 h) y proyecto (+1 h) sobre el mismo contenido, y su migración (🟡 → ✅) añade los labs y el `checks.json` que llenan ese tiempo.
- **Duración:** 46 semanas (460 h) en 7 etapas. La malla completa está en [plan-estudios.md](plan-estudios.md). El número de semanas lo fijan la completitud y la pertinencia del temario, no la extensión de otros bootcamps.
- **Lenguajes:** JavaScript, Python, Java, Go y Rust. Cada lenguaje tiene su etapa propia porque cada ecosistema de testing tiene mecanismos que los demás no tienen:
  - Go trae el testing en la librería estándar, con table-driven tests, fuzzing nativo, el race detector y `testing/synctest`.
  - Rust trae los doctests, y su modelo de traits cambia cómo se hacen los dobles de prueba.
- **Go y Rust con 5 semanas cada uno, frente a las 7–9 de JS, Python y Java:** el estudiante llega con TDD, BDD, mocking, coverage y testing de APIs ya aprendidos tres veces. Estas etapas enseñan solo lo propio del lenguaje. BDD no se repite (ya se vio con Behave y Cucumber).
- **Por qué no se tocó la Etapa 0:** S01–S06 ya estaban construidas y auditadas con tres lenguajes. La primera semana de Go (S32) y la de Rust (S37) hacen de "primeros tests" y comparan con lo ya visto.
- **Etapa transversal al final (S42–S46):** E2E, performance, seguridad y CI se aplican sobre el sistema políglota que el estudiante ya construyó ([proyecto-hilo-conductor.md](proyecto-hilo-conductor.md)).

## Solapes con otros bootcamps

Se enseña lo justo desde la óptica del testing y se enlaza al bootcamp que lo trata a fondo:

| Tema | Aquí | A fondo en |
|---|---|---|
| Sintaxis de cada lenguaje | Solo lo que el test necesita, explicado la primera vez que aparece | `bc-javascript-es2023`, `bc-python`, `bc-go`, `bc-rust` |
| Express, FastAPI, Spring Boot | Como sistema bajo prueba | `bc-expressjs`, `bc-fastapi`, `bc-javaweb-sb` |
| PostgreSQL y SQL | Esquema mínimo para tests de repositorio | `bc-postgresql`, `bc-sql` |
| Docker | Lo justo para Testcontainers y ZAP | `bc-docker` |
| GitHub Actions | S45: un job de tests por lenguaje, coverage y umbrales | `bc-github-actions` |
| Métricas y latencias en producción | S43: solo pruebas de carga | `bc-observability` |

## Decisiones de diseño heredadas de los bootcamps recientes

Se adoptaron en noviembre de 2026 tomando como referencia `bc-ssh`, `bc-tmux`, `bc-curl`, `bc-github-actions` y `bc-python`:

- **Versiones:** una sola fuente de versiones, [stack-versions.md](stack-versions.md).
- **Base de datos:** solo PostgreSQL, nunca MySQL ni MariaDB.
- **Proyecto:** un proyecto hilo conductor acumulativo por aprendiz, sobre su dominio ([proyecto-hilo-conductor.md](proyecto-hilo-conductor.md)).
- **Autograding:** declarativo con `checks.json` + `scripts/verificar-semana.sh` ([autograding.md](autograding.md)).
- **Navegación:** la genera `scripts/generar-navegacion.sh`, y `scripts/verificar-enlaces.sh` comprueba la estructura y las reglas de estilo.
- **Prácticas:** las carpetas de práctica se llaman `lab-NN-slug`, con `starter/` y `solution/`.
- **Diagramas SVG:** paleta GitHub dark.
- **Alcance:** todo esto se aplica desde S23. S01–S22 siguen con la estructura anterior (`ejercicio-NN`, navegación escrita a mano, paleta slate) y figuran como 🟡 en el [índice de semanas](../bootcamp/README.md) hasta que se migren.

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Versiones del stack](stack-versions.md) | [Proyecto hilo conductor](proyecto-hilo-conductor.md) → |

← [Volver al inicio del bootcamp](../README.md)
