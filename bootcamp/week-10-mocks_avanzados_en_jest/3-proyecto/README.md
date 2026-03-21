# Proyecto Semanal - Suite con Dependencias Aisladas

## Objetivo

Construir una suite de tests en Jest para un servicio de dominio que depende de repositorio y gateway externos.

## Contexto

Este proyecto es el entregable obligatorio de la semana. Debes adaptar el starter a tu dominio asignado.

## Requisitos

1. Usar `jest.fn()` para mockear al menos dos dependencias.
2. Incluir un caso con `jest.spyOn()` para observar una llamada relevante.
3. Validar interacciones con `toHaveBeenCalledWith` y `toHaveBeenCalledTimes`.
4. Cubrir casos felices, validaciones y propagacion de errores.
5. Mantener patron AAA en todos los tests.

## Estructura

- `starter/`: plantilla con TODOs para implementar.
- `solution/`: referencia local del instructor (no versionada).

## Criterios minimos

- Minimo 8 tests.
- Minimo 1 error de negocio validado con `rejects.toThrow`.
- Minimo 1 verificacion de orden o cantidad de llamadas.
- Cobertura sugerida: >= 85% en archivos del proyecto.

## Ejecucion sugerida

```bash
yarn test
```
