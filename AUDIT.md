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
- **Idioma**: verificado 2026-09 que no hay voseo en el repo (búsqueda de formas `-ás/-és/-ís`, imperativos `-á/-é/-í`, clíticos sin tilde y léxico rioplatense). Se encontraron 8 formas ambiguas sin tilde (`Fijate`, `importalo`, `requierelo`, `declaralo`, `Abrelo`...) y se normalizaron a tuteo con tilde. Regla "tuteo, nunca voseo" añadida a `copilot-instructions.md` y a la guía.
- **Menores de estilo JS** fuera de alcance: `describe` en todos los tests, nombres con "when", layout `src/tests` uniforme, tildes en READMEs S07–S15.
- **Python (S04, S16–S18) y Java**: pendiente de revisión equivalente (S04 falla con `pytest -v` por `pythonpath`; S16–S18 sin dependencias declaradas).

---

# Revisión integral Python — 2026-09

**Alcance**: contenido Python publicado (S04, S06 parte Python, S16–S18) + paso de ortografía (tildes/ñ) en todo el contenido JS (S03, S06, S07–S15).
**Rama**: `fix/revision-integral-python`. Referencias de la organización: `bc-python` (semana 13) y `bc-fastapi`.

## Decisiones

- Python 3.14 (`.python-version`), `uv` como único camino en instrucciones (`uv sync`, `uv run pytest`); `pip` + `venv` solo como alternativa mencionada en S04.
- `pyproject.toml` por starter/solution/proyecto con `[dependency-groups] dev` fijado (`pytest==9.1.1`; `pytest-mock==3.15.1` en S18) y tabla nativa `[tool.pytest]` de pytest 9 (`pythonpath`, `testpaths`, `markers`). Sin `requirements.txt`, `pytest.ini` ni `uv.lock` versionados.
- Nombres de test unificados: `test_[context]_[expected]_when_[condition]`.
- Testing asíncrono en Python reubicado en S19 (`docs/plan-estudios.md`).

## Hallazgos principales y estado

| Hallazgo | Estado |
|---|---|
| Comandos de README no ejecutables: `ModuleNotFoundError: src` en S04 (también en el ejemplo de teoría), `import file mismatch` en S06/S16–S18 por ejecutar desde la carpeta del ejercicio, S16–S18 sin dependencias declaradas | ✅ `pyproject.toml` con `pythonpath`; README con `cd starter && uv sync && uv run pytest` |
| S04 proyecto con dominios restringidos (Biblioteca/Farmacia/Gimnasio) y starter con `IndentationError` | ✅ Museo/Planetario/Acuario; starter válido renombrado a `test_item_service.py` |
| S04 afirmaba que una excepción inesperada en el test da ERROR (es FAILED) | ✅ Corregido con salida real en teoría, glosario y rúbrica |
| S16 repetía S04 casi por completo | ✅ S16 reenfocada en fixtures: `yield`/teardown, scopes, composición, `conftest.py`, `autouse`, `tmp_path`, `monkeypatch`, `capsys`, `--setup-show` |
| S17: marks solo en `pytest.ini`, sin `--strict-markers`/`strict`, sin `skip`/`skipif`/`xfail`, sin `pytest.param`, `-k` no cubierto, estrategia "`-m regression` = regresión completa" incorrecta | ✅ Marks en `[tool.pytest]` con `strict = true`; temas añadidos; estrategia corregida |
| S18: "stub" que verificaba interacción, `try/assert False`, sin demostración de target de patch incorrecto, `autospec` sin práctica, proyecto sin símbolo que parchear, markdown mal renderizado | ✅ Stub/mock/spy separados; PASO con target incorrecto y `autospec` con salidas reales; `assert_not_called`, `call_args`, `monkeypatch` vs `patch`; proyecto con gateway a nivel de módulo |
| S06: `pytest.raises(match=...)` comparaba con el nombre de la clase; ejemplos JS/Java sin la misma verificación | ✅ Los tres lenguajes verifican el mismo mensaje |
| Recursos sin URLs, libros de pago en "ebooks-free" y un título inexistente (S17) | ✅ URLs verificadas en formato tabla; solo recursos gratuitos reales |
| Tildes y ñ ausentes en la prosa de S04, S07–S18 | ✅ Corregidas (prosa y comentarios; identificadores y cadenas verificadas por tests intactos) |

## Resultado

Todas las solutions Python pasan con `uv run pytest` (S17 ej02: 5 passed + 1 xfailed intencional) y todas las solutions JS siguen pasando. Starters descomentados equivalentes a su solution. Sin enlaces internos rotos ni formas de voseo.

## Ítems abiertos

- `skip`/`skipif` en S17 solo en teoría (en la práctica se usa `xfail(strict=True)`; un `skipif` exigía una condición ajena al dominio).
- Revisión equivalente de Java (S05, S06 parte Java) pendiente.

---

# Revisión integral Java — 2026-09

**Alcance**: contenido Java publicado (S05 completa y parte Java de S06) + tildes pendientes en S06.
**Rama**: `fix/revision-integral-java`. Entorno: Temurin 21.0.12 y Maven 3.9.16.

## Decisiones

- `pom.xml` con `maven.compiler.release` 21, `project.build.sourceEncoding` UTF-8 y versiones exactas (`junit-jupiter` 5.14.4, `assertj-core` 3.27.7, `maven-surefire-plugin` 3.5.6). Se mantiene JUnit 5 (el ítem abierto sobre JUnit 6 sigue pendiente).
- AssertJ se introduce en S05 (lo pide el plan) solo con `assertThat` y `assertThatThrownBy`; la profundización queda en S25.
- Tests pendientes de proyecto con `@Disabled("TODO: ...")`, equivalente a `test.todo` y `pytest.skip`.
- Sin configuración extra de Surefire para mostrar `@DisplayName` en consola: la teoría explica que el resumen usa el nombre del método.

## Hallazgos principales y estado

| Hallazgo | Estado |
|---|---|
| S06 proyecto: el starter Java tenía 3 tests vacíos que pasaban en verde (falso positivo), a diferencia de `test.todo`/`pytest.skip` | ✅ `@Disabled` con su TC; `Skipped: 3` |
| S06 teoría 03: el ejemplo Java llamaba `AmountValidator.isValid` como estático sobre un método de instancia (no compilaba) | ✅ Usa una instancia, igual que el ejercicio |
| S05 proyecto: `ItemServiceTest.java` suelto (sin `pom.xml`, sin paquete, fuera del layout Maven), dominios restringidos (Biblioteca/Farmacia/Gimnasio) y sin la comparación JS/Python/Java que pide el plan | ✅ Proyecto Maven ejecutable, dominios Museo/Planetario/Acuario, tabla comparativa de tres lenguajes |
| S05 ejercicio 01: "encontrarás un fallo inicial" en la primera ejecución, que en realidad da `Tests run: 0` y `BUILD SUCCESS` | ✅ Pasos con salida real: 0 tests, rojo al descomentar el PASO 1 y verde tras corregir |
| S05 ejercicio 02: starter idéntico a la solution y un "completar assertions faltantes" sin nada que completar | ✅ PASO 4 `double` con delta (falla real `15.991999999999999`), PASO 5 failure vs error (NPE real con `null`, bug corregido en `src/main`), PASO 6 AssertJ |
| Temas del plan sin cubrir: ciclo de vida completo, JUnit 4 vs 5 (preguntado en la rúbrica), reportes de Surefire, AssertJ | ✅ En teoría con salida real (orden de hooks verificado) |
| La teoría afirmaba que `@DisplayName` mejora los reportes; Surefire muestra el nombre del método | ✅ Corregido con salida real |
| SVG de CLI: "`mvn -q test` muestra salida resumida" (en verde no imprime nada); SVG de S05 sin referenciar desde la teoría | ✅ Texto corregido y SVG enlazados |
| Comandos Maven y pnpm de S06 ejercicio 02 sin `cd` a la carpeta del lenguaje | ✅ Corregido |
| Warning `Using platform encoding` en los `pom.xml` de S05 | ✅ `sourceEncoding` UTF-8 |
| Recursos de S05: videos sin URL y enlace de pago (Effective Java en O'Reilly) | ✅ URLs verificadas (docs JUnit 5.14.4 versionadas, videos vía oEmbed); solo recursos gratuitos |
| Tildes ausentes en S05 (ejercicios) y S06 (plantillas de test plan, rúbrica, SVG) | ✅ Corregidas |

## Resultado

Los 8 proyectos Maven compilan y pasan con `mvn test` (solutions: 3, 10 y 3 tests; starters de proyecto: 3 skipped). Los starters descomentados equivalen a su solution, con los rojos intencionales documentados en cada README. Sin enlaces internos rotos ni formas de voseo.

## Ítems abiertos

- El ciclo de vida (`@BeforeAll`/`@AfterEach`/`@AfterAll`) se practica solo en teoría; en ejercicios y proyecto se usa `@BeforeEach`.
- JUnit 6: sigue pendiente la decisión antes de construir S25–S31.

---

# Unificación de versiones — 2026-09

**Objetivo**: una sola versión por herramienta en todo el repo y la misma que usa `bc-testing-adso`, para que el contenido y la documentación no se contradigan.
**Rama**: `chore/unificar-versiones`.

## Cambios

| Herramienta | Antes | Ahora | Motivo |
|---|---|---|---|
| pnpm | 10.34.5 | **12.6.0** | Igual que `bc-testing-adso` |
| JUnit (Jupiter) | 5.14.4 | **6.0.3** | Versión que gestiona Spring Boot 4.1.1 en `bc-testing-adso`; cierra el ítem abierto sobre JUnit 6 |
| Tablas de herramientas (README, README_EN, reglas, guía, S01, plan) | rangos (`29+`, `3+`, `5.10+`, `1.19+`...) y versiones viejas (Python 3.12, pytest 8) | versiones exactas; las herramientas aún sin semana dicen "se fija en SXX" | Una sola fuente: `.github/copilot-instructions.md`, sección "Herramientas por Lenguaje" |

- pnpm 12 ya no lee el campo `"pnpm"` de `package.json` y termina con `ERR_PNPM_IGNORED_BUILDS` cuando una dependencia trae build scripts sin decidir. Jest arrastra `@parcel/watcher` y `unrs-resolver`, que no necesitan compilar: cada `package.json` tiene al lado un `pnpm-workspace.yaml` idéntico con `allowBuilds` en `false` para ambos.
- JUnit 6 mantiene la API Jupiter: sin cambios en los tests. El texto dice "JUnit 6"; la tabla de S05 compara JUnit 4 con Jupiter (JUnit 5 y 6), y los títulos de videos que dicen "JUnit 5" se conservan con una nota.
- Enlaces de documentación de JUnit fijados a `docs.junit.org/6.0.3`.

## Verificación

- 53 proyectos JS: `pnpm install` limpio con pnpm 12.6.0 (solo el aviso de `glob@10.5.0` deprecado que trae Jest) y `CI=true pnpm test`: las 23 solutions en verde; los starters igual que antes (0 tests o el rojo intencional de S03).
- 8 proyectos Maven con JUnit 6.0.3: todos en verde; las salidas documentadas en S05 (números de línea, mensajes, orden del ciclo de vida, Failure vs Error, AssertJ) son idénticas.
