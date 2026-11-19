# Glosario - Semana 01

| Término | Definición |
|---|---|
| Calidad del software | Grado en que un sistema cumple sus requisitos escritos y lo que el usuario espera aunque no esté escrito. Tiene varias dimensiones: funcionalidad, fiabilidad, usabilidad, rendimiento, seguridad, mantenibilidad. |
| Tester (QA Engineer) | Persona que comprueba que el software cumple los requisitos y encuentra defectos antes que el usuario. |
| SDET | *Software Development Engineer in Test*: construye la automatización de pruebas (suites, frameworks, pipelines). |
| Testing manual | Una persona ejecuta los casos de prueba usando el sistema. |
| Testing automatizado | Un programa ejecuta los casos de prueba y compara el resultado con el esperado. |
| Regresión | Comprobar que lo que funcionaba sigue funcionando después de un cambio. |
| Testing preventivo | Probar antes o mientras se escribe el código. |
| Testing correctivo | Probar el software ya terminado para encontrar defectos. |
| Test unitario | Prueba una función o clase aislada, sin base de datos, red ni archivos. |
| Test de integración | Prueba dos o más piezas juntas, con recursos reales como una base de datos. |
| Test E2E (*end to end*) | Prueba un flujo completo como lo hace el usuario, con todas las piezas. |
| Test de aceptación | Comprueba un criterio del negocio, escrito en lenguaje natural (por ejemplo, *Given / When / Then*). |
| Pirámide de testing | Modelo que pide muchos tests unitarios, algunos de integración y pocos E2E. |
| Cono de helado | Antipatrón: más E2E que unitarios. La suite se vuelve lenta y frágil. |
| Error | La equivocación de una persona que introduce un defecto. |
| Defecto (*bug*) | Lo que el error deja en el código: la causa. |
| Fallo (*failure*) | Lo que se ve al ejecutar el código con el defecto: el efecto. |
| Reporte de bug | Documento que describe un fallo con lo necesario para reproducirlo, entenderlo y priorizarlo. |
| Severidad | Cuánto daño hace un fallo: Critical, High, Medium o Low. |
| Prioridad | Cuándo hay que corregirlo: P1 (hoy) a P4 (sin fecha). |
| Camino alternativo (*workaround*) | Forma de conseguir lo que se quería evitando el fallo. Baja la severidad. |
| Ciclo de vida del bug | Estados de un bug desde que se reporta hasta que se cierra: New, Open, In Progress, Fixed, Retest, Verified, Closed. |
| Reopened | Estado de un bug cuya corrección no funcionó en el retest, o que volvió a aparecer. |
| Retest | Repetir la prueba del reporte original, en sus mismas condiciones, después de la corrección. |
| Triage | Revisión periódica de los reportes nuevos para decidir qué se hace con cada uno. |
| Duplicate | Reporte de un fallo que ya está reportado. Se cierra enlazando el original. |
| Not a Bug | El sistema hace lo que dice el requisito. Se cierra citándolo. |
| Won't Fix | Fallo real que el equipo decide no corregir. |
| Cannot Reproduce | Nadie consigue ver el fallo con los datos del reporte. |
| Redondeo al par (bancario) | Redondeo que lleva un 5 final a la cifra par más cercana: 2,345 pasa a 2,34 y 2,355 a 2,36. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | [Semana 02 — Casos de Prueba y Técnicas de Diseño](../../week-02-casos_de_prueba_y_tecnicas_de_diseno/README.md) → |

[↑ Semana 01](../README.md)
