# Rubrica de Evaluacion - Semana 13

## Evidencias y ponderacion

| Evidencia | Peso | Descripcion |
|---|---:|---|
| Conocimiento | 30% | Comprension de snapshots utiles e invariantes de propiedades |
| Desempeno | 40% | Practicas guiadas con Jest snapshot y fast-check |
| Producto | 30% | Proyecto con combinacion de tests de ejemplo + properties |

---

## 1) Conocimiento (30%)

### Criterios

1. Diferencia snapshot util vs snapshot fragil.
2. Define que es una propiedad invariante y por que aporta cobertura.
3. Explica flujo de `generate -> test -> shrink` en fast-check.
4. Selecciona casos donde property-based es mas efectivo que ejemplos fijos.

### Niveles

- **Alto (27-30 pts)**: maneja conceptos y toma decisiones tecnicas justificadas.
- **Medio (21-26 pts)**: base correcta con ajustes menores.
- **Bajo (0-20 pts)**: confusion conceptual o aplicacion superficial.

---

## 2) Desempeno (40%)

### Criterios

1. Completa ejercicios guiados descomentando por pasos.
2. Mantiene snapshots pequeños y enfocados.
3. Escribe propiedades claras con aserciones significativas.
4. Interpreta fallos y contraejemplos con diagnostico correcto.

### Niveles

- **Alto (36-40 pts)**: ejecucion precisa, estable y con criterio.
- **Medio (28-35 pts)**: funcional con oportunidades de claridad.
- **Bajo (0-27 pts)**: pruebas fragiles o poco representativas.

---

## 3) Producto (30%)

### Criterios

1. Proyecto adapta plantilla al dominio asignado.
2. Incluye al menos un snapshot con valor real de contrato visual/estructural.
3. Incluye al menos dos propiedades invariantes con fast-check.
4. Minimo sugerido: 8 tests combinando enfoques.

### Niveles

- **Alto (27-30 pts)**: suite robusta, legible y con alta deteccion de regresiones.
- **Medio (21-26 pts)**: cumplimiento base con margen de mejora.
- **Bajo (0-20 pts)**: suite incompleta o sin criterio de uso de snapshots/properties.

---

## Penalizaciones

- Snapshot gigante sin foco (ruido): hasta -8 pts.
- Propiedades triviales sin valor de negocio: hasta -8 pts.
- Uso de `npm` en ruta JavaScript del bootcamp: hasta -5 pts.
- Nomenclatura tecnica fuera de ingles: hasta -5 pts.

---

## Checklist de entrega

- [ ] Practicas guiadas completadas.
- [ ] Proyecto implementado en `3-proyecto/starter/`.
- [ ] Tests ejecutan sin errores en local.
- [ ] Hay evidencia de snapshots utiles y propiedades invariantes.
- [ ] Nombres de tests describen comportamiento esperado.
