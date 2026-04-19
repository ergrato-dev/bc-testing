# Decisión de Arquitectura: Nomenclatura de Carpetas de Semanas

**Fecha**: 2026-03-21
**Estado**: Implementada
**Afecta**: `bootcamp/week-XX-*`

---

## Contexto

Las carpetas de semanas originalmente seguían el esquema `week-XX` (ej. `week-07`). Este formato era funcional pero no comunicaba el contenido al leer la estructura de carpetas.

## Decisión

Cambiar el esquema de nombres a **`week-XX-tema_principal`**, donde el tema principal se extrae del título del `README.md` de cada semana y se escribe en **snake_case sin tildes ni ñ**.

### Reglas de conversión

1. Extraer el tema del encabezado H1 del `README.md` de la semana.
2. Si el título tiene prefijo de etapa (`JavaScript Testing IV:`, `Python Testing II:`), usar solo la parte descriptiva tras los dos puntos.
3. Eliminar tildes: `é→e`, `í→i`, `ó→o`, `ú→u`, `ñ→n`, `á→a`.
4. Reemplazar espacios por `_` (snake_case).
5. Usar solo minúsculas.
6. El separador entre número y tema es `-` (guión).

### Mapa completo `old → new`

| Carpeta anterior | Carpeta nueva |
|---|---|
| `week-01` | `week-01-que_es_testing_calidad_de_software` |
| `week-02` | `week-02-casos_de_prueba_y_tecnicas_de_diseno` |
| `week-03` | `week-03-primeros_tests_automatizados_con_javascript` |
| `week-04` | `week-04-primeros_tests_automatizados_con_python` |
| `week-05` | `week-05-primeros_tests_automatizados_con_java` |
| `week-06` | `week-06-plan_de_pruebas_y_suite_base_multilenguaje` |
| `week-07` | `week-07-jest_avanzado_y_organizacion_de_suites` |
| `week-08` | `week-08-matchers_avanzados_y_parametrizacion` |
| `week-09` | `week-09-testing_asincrono_con_jest` |
| `week-10` | `week-10-mocks_avanzados_en_jest` |
| `week-11` | `week-11-tdd_con_jest` |
| `week-12` | `week-12-apis_rest_con_supertest` |
| `week-13` | `week-13-snapshot_y_property_based_testing` |
| `week-14` | `week-14-coverage_y_calidad_de_suites_en_jest` |
| `week-15` | `week-15-integracion_de_estrategias_y_calidad_continua` |
| `week-16` | `week-16-fundamentos_con_pytest` |
| `week-17` | `week-17-parametrizacion_y_marks_con_pytest` |
| `week-18` | `week-18-mocking_con_unittest_mock_y_pytest_mock` |

> Las semanas aún no creadas (week-19 en adelante) deben seguir este mismo esquema desde su creación.

## Consecuencias

### Positivas

- La estructura de carpetas es autodocumentada: al hacer `ls bootcamp/` se ve el tema de cada semana sin leer ningún README.
- Los commits, ramas y PRs hacen referencia al tema, no solo al número.
- Compatible con búsquedas por tema (ej. `find bootcamp -name "*jest*"`).

### Consideraciones

- Los enlaces rotos se corrigieron en el mismo commit de renombrado.
- `week-18` mantiene el enlace a `../week-19/README.md` sin sufijo hasta que esa semana sea creada.
- Los ejemplos de commits en `CONTRIBUTING.md` se actualizaron para reflejar el nuevo esquema.

## Archivos modificados

- `bootcamp/week-*/README.md` — árboles de directorio y navegación prev/next
- `README.md` — enlace "Comenzar Semana 1"
- `README_EN.md` — enlace "Start Week 1"
- `CONTRIBUTING.md` — ejemplos de mensajes de commit
- `bootcamp/week-15-*/1-teoria/03-criterios-de-salida-etapa-javascript.md` — referencia textual
- `bootcamp/week-16-*/1-teoria/03-fixtures-basicas-reduccion-duplicacion.md` — referencia textual

## Referencia

Título fuente de cada semana: `README.md` → H1 → texto tras el separador `—` o `-`.
