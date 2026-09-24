# Auditoría del repositorio — bc-testing

**Fecha**: 2026-07-13
**Alcance**: contenido publicado (Semanas 01-18 de 36 planificadas), configuración raíz, docs, CI.
**Realizada por**: auditoría asistida (Claude), a petición de ergrato-dev.

---

## Metodología

1. Lectura completa de `docs/guia-desarrollo-contenidos.md`, `docs/plan-estudios.md` y `.github/copilot-instructions.md` (no existe `CLAUDE.md` en este repo — `copilot-instructions.md` es la fuente de reglas de contenido).
2. Grep exhaustivo por placeholders/TODO/TBD/lorem-ipsum, rangos de versión flotantes (`^`, `~`, `>=`) en `package.json`/`requirements.txt`/`pom.xml`, y uso de `npm`/`pip` fuera de política.
3. Agente de exploración dedicado que recorrió las 18 semanas verificando: fugas de la política anticopia, enlaces internos rotos, navegación entre semanas, contenido "delgado" (teoría <60 líneas, glosario <8 términos), fugas de lenguaje entre etapas (JS/Python/Java), y formato TODO-en-ejercicios.
4. Comparación de la estructura raíz contra repos hermanos `bc-javascript-es2023-cf` y `bc-fastapi` (`gh api repos/...`) para estándares de la organización `ergrato-dev/bc-*`.
5. Búsqueda web de versiones actuales y CVEs conocidos para cada dependencia pinneada (Jest, pytest, junit-jupiter, maven-surefire-plugin) — sin bases de datos de CVE dedicadas disponibles en el entorno, se usaron los avisos de seguridad públicos de cada proyecto (Snyk, GitHub Advisories) vía búsqueda.
6. Verificación funcional: `pnpm install` + ejecución real de la suite de ejemplo tras el bump de Jest 29→30 (paso sin fallos).

---

## Hallazgos y estado

### 1. Completitud (contenido real, no placeholders)

| Hallazgo | Estado |
|---|---|
| Cero placeholders/TODO/TBD/lorem-ipsum en las 18 semanas publicadas | ✅ Ya cumplía |
| 21 archivos de `1-teoria/` en 8 semanas (06,07,08,09,11,12,13,14) por debajo del objetivo propio de 80-120 líneas (rango real: 27-70) | ✅ Corregido — todos ampliados a 90-125 líneas con ejemplos trabajados, sin inventar temas fuera del alcance declarado por semana |
| 9 glosarios (semanas 08,11-18) con menos de 8 términos | ✅ Corregido — todos ampliados a exactamente 8 términos, mismo formato ya usado |

### 2. Pertinencia y relevancia

| Hallazgo | Estado |
|---|---|
| `docs/plan-estudios.md` listaba temas por semana (S07-S18) desalineados del contenido realmente publicado — el orden de temas cambió durante el desarrollo (ej. Mocks se movió de S08→S10, Coverage de S10→S14, Python ganó una semana de fundamentos en S16 que no existía en el plan original) | ✅ Corregido — tabla de "Contenido Semanal Detallado" y "Estado de Avance" sincronizadas con el contenido real de cada semana |
| El tema "Testing Asíncrono en Python" (plan original S18) quedó sin semana asignada tras el corrimiento — S18 real es "Mocking" | ⚠️ **Abierto** — no se resolvió unilateralmente. Las Semanas 19-36 siguen sin construir; antes de desarrollarlas, el instructor debe decidir dónde reinsertar ese tema (posiblemente absorbido en la futura S19 "APIs con Python", que ya usa `httpx.AsyncClient`) |
| `3-proyecto/README.md` de S01 y S02 listaban los 15 dominios reservados de la política anticopia con un ejemplo de escenario de prueba por dominio — viola la propia regla de "no regalar soluciones" (`docs/guia-desarrollo-contenidos.md`) | ✅ Corregido — reemplazado por instrucción genérica, mismo patrón que S03-S05 |
| `.github/copilot-instructions.md` refería una carpeta raíz `scripts/` que no existe (quedó vacía tras el rename `_scripts→scripts` del commit `2c0cd7d`) | ✅ Corregido — referencia eliminada |
| `week-18/README.md` apuntaba en su navegación "Siguiente" a `week-19/README.md`, inexistente | ✅ Corregido — reemplazado por "Próximamente" |

### 3. Compatibilidad

| Hallazgo | Estado |
|---|---|
| Sin mezcla de lenguajes en semanas de etapa única (S07-S15 JS-only, verificado libre de bloques de código Java/Python) | ✅ Ya cumplía |
| Sin uso de TODOs en `2-practicas/` (solo permitido en `3-proyecto/`) | ✅ Ya cumplía |
| `week-04` (setup pytest) enseñaba únicamente `pip`/`venv`, sin mencionar `uv` pese a que `copilot-instructions.md` ya documenta `uv` como preferente | ✅ Corregido — teoría y ambos ejercicios de S04 lideran ahora con `uv`, `pip`+`venv` como alternativa documentada. S16-S18 ya seguían el patrón correcto |
| Uso de `npm` en cualquier instrucción del repo | ✅ Ninguno encontrado (100% `pnpm`) |

### 4. Seguridad / auditoría de CVEs

Superficie de dependencias real es pequeña — solo herramientas de testing en `2-practicas/` de S03, S04, S05 y S06 (no hay dependencias de producción/runtime en el contenido publicado):

| Paquete | Versión anterior | Versión actual (auditada 2026-07-13) | CVEs conocidos |
|---|---|---|---|
| jest | 29.7.0 | **30.4.2** | Ninguno en 29.7.0 ni en 30.4.2 a la fecha de auditoría |
| pytest | 8.3.5 | **9.1.1** | Ninguno en 8.3.5 ni en 9.1.1 |
| junit-jupiter | 5.10.2 | **5.14.4** | Ninguno en 5.10.2 ni en 5.14.4 |
| maven-surefire-plugin | 3.2.5 | **3.5.6** (última GA, no milestone) | Ninguno |

- Todas las versiones se mantienen **pinneadas exactas** (sin rangos flotantes) antes y después del bump, cumpliendo la "Regla de Oro" de `copilot-instructions.md`.
- `pnpm-lock.yaml` de los 4 ejercicios afectados fue regenerado y verificado (`pnpm install` + ejecución real de la suite de ejemplo, sin fallos).
- **Decisión diferida**: JUnit publicó una v6 (`junit-framework`, renombrado, GA desde sept-2025) que es un cambio de artefacto/paquete, no solo de versión. Todo el contenido de S05+ está narrado como "JUnit 5" — saltar a v6 exigiría reescribir ejemplos y romper el branding del contenido ya publicado. Se mantuvo en la última versión estable de la línea 5.x (5.14.4) y se deja como decisión de instructor para una futura revisión mayor, no como bug de esta auditoría.
- No se encontraron CVEs activos en ninguna versión (antes o después del bump) — el hallazgo era desactualización, no vulnerabilidad.

### 5. Estándares de la organización `ergrato-dev/bc-*`

Comparado contra `bc-javascript-es2023-cf` (repo de referencia citado en `docs/guia-desarrollo-contenidos.md`) y `bc-fastapi`:

| Aspecto | Estado |
|---|---|
| Workflow de CI (`close-prs.yml`) | ✅ Igual al de los repos hermanos |
| Licencia CC BY-NC-SA 4.0 | ✅ Decisión ya tomada deliberadamente (commit `9d3838c`) |
| `SECURITY.md` / `CODE_OF_CONDUCT.md` | No es un estándar real de la organización — presente en `bc-fastapi`, ausente en `bc-javascript-es2023-cf` y en la mayoría de repos `bc-*`. **No se agregó** para no inventar scaffolding no solicitado |
| Tooling raíz (`package.json`/`pyproject.toml` + lockfile, linter) | Presente en repos `bc-*` de un solo lenguaje (con `export-pdf.sh` u otro tooling raíz); `bc-testing` es multilenguaje sin tooling raíz propio — no aplica el mismo patrón, **no se agregó** |

### 6. Cumplimiento de `.github/copilot-instructions.md` (reglas propias del repo)

| Regla | Estado |
|---|---|
| Pinning exacto de dependencias, sin `LATEST`/`RELEASE`/rangos | ✅ Cumplía y se mantiene tras el bump |
| Nomenclatura técnica en inglés, documentación en español | ✅ Verificado en el contenido auditado |
| `pnpm`/`uv` en vez de `npm`/`pip`-only | ✅ Corregido donde faltaba (ver sección 3) |
| Política anticopia — no revelar soluciones por dominio | ✅ Corregido (ver sección 2) |

### 7. Actualidad

| Hallazgo | Estado |
|---|---|
| Versiones de Jest/pytest/JUnit un major detrás de la última estable | ✅ Corregido (ver sección 4) |
| Tablas de versiones en `docs/guia-desarrollo-contenidos.md` y `.github/copilot-instructions.md` desactualizadas | ✅ Corregido |
| Sellos "Última actualización: Marzo 2026" en ambos docs, pese a desarrollo activo hasta abril y esta auditoría en julio | ✅ Corregido — actualizados a julio 2026, versión 1.1 |

---

## Resumen de commits de esta auditoría

1. `fix(deps): bump jest/pytest/junit to current stable, sync docs with reality`
2. `docs(week-04): prefer uv over pip in Python setup flow`
3. `fix(anticopia): remove reserved-domain example table from S01/S02 project README`
4. `docs(glossary): expand 9 short glossaries to meet 8-term minimum`
5. `docs(week-06,07,08,09,11,12,13,14): expand theory files to meet 80-120 line target`
6. `docs: add AUDIT.md with full repo audit results` (este commit)

## Ítems abiertos (no resueltos unilateralmente)

- **Renumeración de S19-S36**: el corrimiento de temas observado en S07-S18 probablemente requiera un ajuste similar en el roadmap no publicado. En particular, el tema "Testing Asíncrono en Python" no tiene semana asignada tras el corrimiento de S16-S18. Requiere decisión del instructor antes de construir esas semanas.
- **JUnit 6 (`junit-framework`)**: evaluar en una revisión futura si migrar el contenido Java (S05, S25-S31 cuando se construyan) al nuevo artefacto, dado el rebranding completo del proyecto.

---

# Revisión integral JavaScript — 2026-09

**Alcance**: contenido JavaScript publicado (S03, S06 parte JS, S07–S15). Criterio: pertinencia frente a `docs/plan-estudios.md`, calidad técnica (Jest 30) y completitud, tomando `bc-expressjs` como referencia de la organización.
**Rama**: `fix/revision-integral-js`.

## Metodología

1. Ejecución real de cada `solution/` y `3-proyecto/starter/` con `pnpm install` + `CI=true pnpm test`.
2. Para cada starter de `2-practicas`: copia temporal con todos los PASO descomentados, comparada contra su solution.
3. Auditoría por semana de pertinencia (temas del plan practicados o no), corrección técnica, coherencia README ↔ starter, solapamientos y recursos.
4. Verificación de URLs de recursos (WebFetch/curl; YouTube vía oEmbed) y de SHAs de GitHub Actions contra sus tags.

## Hallazgos principales y estado

| Hallazgo | Estado |
|---|---|
| S07–S15 sin `package.json`: ningún comando de los README era ejecutable | ✅ `package.json` en cada starter/solution/proyecto (`packageManager: pnpm@10.34.5`, `engines.node >=22`, versiones exactas) |
| `yarn` en 35 archivos pese a la política pnpm-only (la auditoría de julio solo buscó `npm`); reglas contradictorias en `copilot-instructions.md` y la guía | ✅ Migrado a `pnpm`; reglas unificadas a solo pnpm; `.nvmrc` = 22; lockfiles no versionados |
| S09 ej02: solution colgada (`advanceTimersByTime` tras un solo `await Promise.resolve()`) | ✅ `advanceTimersByTimeAsync`, explicado en teoría |
| S11: TDD sin fase Red (starter de ejercicio y proyecto ya implementados) | ✅ Ciclos Red/Green/Refactor reales; proyecto con stubs `Not implemented` |
| S15: `sonar-project.properties` inválido (indexación doble), quality gate no bloqueante, Node 20 EOL | ✅ Corregido; actions pinneadas por SHA |
| S03 instalaba `jest@29`; S08 títulos de `test.each` cruzados; S13 contraejemplo imposible | ✅ Corregido con salidas reales |
| Temas del plan no practicados: `coverageThreshold`/`collectCoverageFrom` (S14), `requireActual`, orden de llamadas, clear/reset/restore (S10), `describe.each` (S08), aislamiento de repos y 500 (S12), shrinking, property matchers, inline snapshots (S13), debounce (S09) | ✅ Añadidos como PASO en ejercicios |
| README ↔ starter incoherentes (S03, S07, S08) y solapamiento S07/S10 | ✅ Alineados 1:1; S10 con ejemplos propios |
| S06 proyecto no seguía el plan (`test-plan.md` + 3 lenguajes) | ✅ Alineado; pesos de rúbrica 30/40/30 mantenidos |
| Proyectos S09–S12 exigían ≥85% de coverage antes de enseñarlo (S14) | ✅ Reemplazado por "todos los tests en verde" |
| Videografía/ebooks sin URLs (S03, S06–S15) | ✅ URLs reales verificadas en formato tabla |

## Resultado

Todas las solutions JS pasan con `CI=true pnpm test`; los starters descomentados equivalen a su solution. Los starters de proyecto (TODOs) reportan "0 tests" por diseño.

## Ítems abiertos

- **`qs` (moderate, GHSA-x5fp-wj9c-mxmx, GHSA-4mjr-xmp4-gh2g)** vía `supertest > superagent > qs` en S12 y S15. Solo dependencia de desarrollo (tests); se acepta hasta que `superagent` publique versión con `qs >= 6.16.0`.
- **`pom.xml` de S06 proyecto** no compilado en esta revisión (sin Maven en el entorno).
- **Menores de estilo JS** fuera de alcance: `describe` en todos los tests, nombres con "when", layout `src/tests` uniforme, tildes en READMEs S07–S15.
- **Python (S04, S16–S18) y Java**: pendiente de revisión equivalente (S04 falla con `pytest -v` por `pythonpath`; S16–S18 sin dependencias declaradas).
