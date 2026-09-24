# Proyecto Semanal - TDD Incremental en Servicio de Dominio

## Objetivo

Construir una suite de tests siguiendo TDD para evolucionar un servicio de dominio desde casos simples hasta validaciones y refactor.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado por el instructor.

## Requisitos

1. Seguir micro-ciclos **Red-Green-Refactor**.
2. Escribir tests antes de implementar cada regla.
3. Mantener nomenclatura: `should [expected] when [condition]`.
4. Incluir al menos un refactor donde la suite siga en verde.
5. Documentar en comentarios breves que parte corresponde a Red, Green y Refactor.
6. Partir de los stubs del starter (`throw new Error("Not implemented")`): ninguna regla se implementa sin un test que antes haya fallado.

## Evidencia del ciclo Red-Green-Refactor

Por cada ciclo entrega evidencia de las tres fases. Opcion recomendada: un commit por fase, por ejemplo:

```text
test(item): red - should throw validation error when name is missing
feat(item): green - validate name in create
refactor(item): extract validation helper
```

Si no usas git, entrega capturas o la salida de `pnpm test` de cada fase: en Red debe verse el test fallando con el motivo esperado (no un error de sintaxis ni de import), y en Green y Refactor la suite completa en verde.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests.
- Minimo 3 iteraciones TDD visibles.
- Minimo 1 validacion de error de negocio.
- Todos los tests en verde al final de cada ciclo.
- Evidencia de al menos 3 ciclos completos (Red, Green y Refactor).

## Ejecucion sugerida

```bash
pnpm install
pnpm test
```
