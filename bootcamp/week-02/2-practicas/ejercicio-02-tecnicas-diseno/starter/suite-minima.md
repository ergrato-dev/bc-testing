# Suite Mínima de Casos de Prueba

> Ejercicio 02 — Starter | Paso 3

Diseña la suite de casos combinando la partición de equivalencia y el BVA. No dupliques cobertura.

---

## Plantilla de caso de prueba

Copia y adapta esta plantilla para cada caso:

```
ID:                 TC-PLANET-REG-XXX
Título:
Técnica aplicada:   (Partición CE? / BVA límite?)
Datos de prueba:
  - nombre:
  - edad:
  - grupoEscolar:
Resultado esperado:
```

---

## Tus casos de prueba

_(Escribe aquí cada caso — no hay un número fijo: usa tantos como sean necesarios sin redundancias)_

```
ID:                 TC-PLANET-REG-001
Título:             (happy path — registrar visitante adulto válido)
Técnica aplicada:   CE2 (nombre), CE2 (edad), CE4 (grupoEscolar=false)
Datos de prueba:
  - nombre:         (completa)
  - edad:           (completa)
  - grupoEscolar:   false
Resultado esperado: (completa)
```

```
ID:                 TC-PLANET-REG-002
Título:
Técnica aplicada:
Datos de prueba:
  - nombre:
  - edad:
  - grupoEscolar:
Resultado esperado:
```

_(sigue agregando casos...)_

---

## Conteo final

| Técnica | Casos generados |
|---|---|
| Partición de equivalencia | |
| BVA (edad 5–99) | |
| BVA (nombre longitud) | |
| BVA (edad grupoEscolar 3–17) | |
| **Total** | |
