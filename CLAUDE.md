# CLAUDE.md — bc-testing

Instrucciones de proyecto para Claude Code. Este archivo se carga automáticamente en cada sesión. Las convenciones detalladas de cada lenguaje (nomenclatura de tests, `package.json`, `pyproject.toml`, `pom.xml`, `go.mod`, `Cargo.toml`) están en [.github/copilot-instructions.md](.github/copilot-instructions.md); este archivo no las repite.

---

## Identidad del proyecto

- **Tipo:** bootcamp de testing de software Zero to Hero (QA Engineer / SDET Junior)
- **Duración:** 46 semanas × 10 h = 460 h, en 7 etapas: Fundamentos, JavaScript, Python, Java, Go, Rust y Transversal. Plan completo en [docs/plan-estudios.md](docs/plan-estudios.md); justificación en [docs/decision-semanas.md](docs/decision-semanas.md)
- **Versiones:** única fuente en [docs/stack-versions.md](docs/stack-versions.md)
- **Estado de cada semana:** [bootcamp/README.md](bootcamp/README.md)
- **Estructura de referencia:** ergrato-dev/bc-ssh

## Reglas obligatorias de contenido

- **Idioma:** documentación en español, **siempre tuteo, nunca voseo** (`ejecuta`, `revisa`; nunca `ejecutá`, `revisá`, `vos`). Nomenclatura técnica en inglés (código, nombres de test, archivos, variables, opciones, comandos).
- **Herramientas:**
  - JavaScript siempre con **pnpm** (nunca npm, npx ni yarn).
  - Python siempre con **uv**, FastAPI incluido (nunca pip, ni como alternativa).
  - Base de datos siempre **PostgreSQL** (nunca MySQL ni MariaDB).
  - **Docker** siempre que haga falta un servidor, una red o un servicio. Las imágenes propias parten de `ubuntu:24.04`; los servicios usan su imagen oficial con versión fija (`docs/stack-versions.md`).
- **Paso a paso, nunca asumir:** ejemplos claros y detallados, con pasos numerados, el comando exacto y la salida esperada comentada debajo. Nunca "configura X" sin decir cómo. Los ejercicios no dan nada por sabido: cada comando, opción o concepto nuevo se explica la primera vez que aparece.
- **Qué / Para qué / Impacto:** documenta así cada configuración o decisión donde haga falta, en prosa o en comentarios. Explícala una vez y enlaza; no repitas el mismo comentario en varios sitios.
- **Comparar lenguajes:** cuando un concepto ya se vio en otro lenguaje, enlaza la semana donde se vio y muestra solo la diferencia.
- **Ejecuciones reales:** las salidas que aparecen en la documentación salen de ejecutar el código, nunca se inventan.

## Distribución de tiempo por semana (10 h)

| Sección | Tiempo | Descripción |
|---|---|---|
| 1-teoria | 2.5 h | 2–4 archivos, un concepto por archivo |
| 2-practicas | 4 h | 3–4 labs guiados (`lab-NN-slug`) con `starter/` y `solution/` |
| 3-proyecto | 3 h | Incremento semanal del proyecto hilo conductor |
| 4-recursos | 0.5 h | Ebooks, videos y web |

Las semanas integradoras (S06, S15, S24, S31, S36, S41, S46) no tienen teoría nueva.

## Proyecto hilo conductor

Cada aprendiz construye un repositorio `qa-<nombre>-<apellido>` con el sistema de su dominio y sus suites en los cinco lenguajes. Cada semana suma una pieza. Ver [docs/proyecto-hilo-conductor.md](docs/proyecto-hilo-conductor.md).

## Estructura de carpetas por semana (desde S23)

```
week-NN-slug_con_guiones_bajos/
├── README.md                  # objetivos, contenidos, tiempos, navegación entre semanas
├── rubrica-evaluacion.md      # Conocimiento 30 / Desempeño 40 / Producto 30
├── checks.json                # autograding (docs/autograding.md)
├── 0-assets/                  # solo SVG, cada uno enlazado desde 1-teoria
├── 1-teoria/                  # 01-tema.md ...
├── 2-practicas/
│   ├── README.md              # índice de labs con duración
│   └── lab-NN-slug-con-guiones/
│       ├── README.md
│       ├── starter/
│       └── solution/
├── 3-proyecto/
│   ├── README.md              # paso a paso + "✅ Requisitos verificables" (copia de checks.json)
│   ├── starter/               # piezas nuevas de la semana con TODO
│   └── solution/              # excluida con .gitignore
├── 4-recursos/{ebooks-free,videografia,webgrafia}/README.md
└── 5-glosario/README.md
```

S01–S22 siguen con la estructura anterior (`ejercicio-NN`, navegación a mano) y figuran como 🟡 en el índice hasta que se migren.

## Diagramas SVG (0-assets/)

- **Cuántos:** los que la teoría de esa semana necesite, ni uno más ni uno menos. La cantidad no se copia de otros bootcamps ni de otras semanas.
- **Colores:** tema dark con fondo `#0d1117`, cajas `#161b22`, bordes `#30363d` y texto `#c9d1d9`. Acentos `#58a6ff`, `#3fb950`, `#f85149` y `#d29922`, más el color de cada lenguaje (JS `#F7DF1E`, Python `#3572A5`, Java `#F89820`, Go `#00ADD8`, Rust `#DEA584`).
- **Sin degradados:** `linearGradient` y `radialGradient` están prohibidos.
- **Fuentes:** `sans-serif`; `monospace` solo para código.
- **Enlace:** cada SVG se enlaza desde al menos un `.md`.

## Navegación

- **Pie de cada documento:** cada documento termina con una tabla `| Anterior | Siguiente |` con el formato `← [recurso anterior]` y `[recurso siguiente] →`.
- **Generación:** en las semanas la genera `scripts/generar-navegacion.sh NN`, en el orden teoría, labs, proyecto, rúbrica, recursos y glosario; no se escribe a mano. Los documentos de `docs/` llevan la misma tabla.
- **README de la semana:** lleva su propia tabla de navegación entre semanas.

## Antes de dar una semana por terminada

1. `ENTREGA=solution ./scripts/verificar-semana.sh NN` pasa y `./scripts/verificar-semana.sh NN` (starter) falla.
2. Cada `solution/` de labs pasa con el comando del README y cada starter descomentado coincide con su solución.
3. `./scripts/generar-navegacion.sh NN` y `./scripts/verificar-enlaces.sh` terminan sin errores.
4. La semana queda marcada con ✅ en `bootcamp/README.md` y en la tabla Estado de Avance de `docs/plan-estudios.md`.
