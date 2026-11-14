# Autograding

`scripts/verificar-semana.sh` te dice qué te falta antes de entregar. No sabe nada de ninguna semana en concreto: lee `bootcamp/week-NN-*/checks.json` y ejecuta lo que ahí ponga.

## Uso

```bash
./scripts/verificar-semana.sh --doctor        # comprueba tu entorno
PROYECTO=~/qa-ana-perez ./scripts/verificar-semana.sh 23
                                              # comprueba tu repositorio para la S23
./scripts/verificar-semana.sh 23              # comprueba 3-proyecto/starter (debe fallar)
ENTREGA=solution ./scripts/verificar-semana.sh 23
                                              # instructor: valida la solución de referencia
```

Salida:

```
Semana 23 — Testing de Bases de Datos en Python

  ✅ La suite de repositorio pasa contra PostgreSQL
  ❌ Coverage de python/ >= 60 %
     pista: 3-proyecto/README.md, paso 5

  1/2 comprobaciones superadas.
```

Sale con 0 si pasan todas y con 1 si falla alguna. Cada pista dice en qué archivo y en qué paso se arregla.

## Formato de `checks.json`

```json
{
  "semana": "23",
  "titulo": "Testing de Bases de Datos en Python",
  "checks": [
    {
      "id": "repo-tests",
      "descripcion": "La suite de repositorio pasa contra PostgreSQL",
      "cmd": "cd {proyecto}/python && uv run pytest -q tests/repository",
      "assert": "exit0",
      "pista": "3-proyecto/README.md, paso 3"
    }
  ]
}
```

| Campo | Qué es |
|---|---|
| `id` | Identificador corto y único dentro de la semana |
| `descripcion` | Lo que el estudiante ve junto a ✅/❌ |
| `cmd` | Comando bash. Marcadores: `{proyecto}`, `{semana}`, `{raiz}` |
| `assert` | `exit0`, `exit!=0`, `exit=N`, `stdout~RE`, `stdout!~RE` (regex extendida) |
| `pista` | Archivo y paso donde se arregla |

Marcadores:

- `{proyecto}` es `$PROYECTO` si está definido. Si no, es `bootcamp/week-NN-*/3-proyecto/${ENTREGA:-starter}`.
- `{semana}` es la carpeta de la semana.
- `{raiz}` es la raíz de este repositorio.

## Reglas para escribir checks

- **Cuándo:** el `checks.json` se escribe **antes** que el proyecto. Es la especificación verificable.
- **Contrato:** con el `starter/` falla al menos un check y con la `solution/` pasan todos. Una semana no está terminada hasta que ambas cosas son ciertas.
- **Una sola cosa por check:** que el test pase, que el umbral de coverage se cumpla, que exista el reporte. No se mezclan.
- **Comandos:** se usan los mismos comandos que el estudiante ve en el README (`pnpm test`, `uv run pytest`, `mvn -q test`, `go test ./...`, `cargo test`).
- **Docker:** los checks que necesitan PostgreSQL asumen que `docker compose up -d` ya corrió. La pista lo recuerda.
- **Copia visible:** el `3-proyecto/README.md` termina con "✅ Requisitos verificables", una copia legible de los checks.

---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Proyecto hilo conductor](proyecto-hilo-conductor.md) | [Guía de desarrollo de contenidos](guia-desarrollo-contenidos.md) → |

← [Volver al inicio del bootcamp](../README.md)
