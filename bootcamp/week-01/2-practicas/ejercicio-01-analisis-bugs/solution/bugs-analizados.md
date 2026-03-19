# Bugs Analizados — Solución

---

## Bug #1 — Análisis

**Problemas encontrados:**
- Título genérico: no indica qué parte del login falla ni bajo qué condición
- Pasos vagos e irreproducibles: "intentar entrar" no especifica qué credenciales, qué acción exacta ni en qué URL
- Resultado obtenido demasiado ambiguo: "no entra" (¿error? ¿redirección? ¿página en blanco?)
- Resultado esperado en blanco: imposible saber qué debería ocurrir
- Severidad "Critical" sin justificación y posiblemente sobrevalorada si no hay datos
- Entorno faltante: sin SO, navegador ni versión de la app no se puede reproducir

**Título corregido:** `[Login] El formulario no muestra mensaje de error al ingresar credenciales incorrectas`
*(Nota: este es un título propuesto basado en una interpretación razonable. Si el bug real es distinto, el título debe reflejarlo.)*

**Severidad corregida:** Medium — la autenticación es importante, pero sin datos concretos es prematuro clasificar como Critical. Puede ser High si se confirma que ningún usuario puede iniciar sesión.

**Prioridad corregida:** P2 — hasta confirmar el alcance real del bug.

**Pasos corregidos (propuesta):**
1. Ir a `https://app.example.com/login`
2. Ingresar email `test@example.com` y contraseña incorrecta `wrongpassword123`
3. Hacer clic en "Iniciar sesión"

**Resultado esperado:** El sistema muestra el mensaje "Credenciales inválidas" y no redirige al usuario.

**Entorno (mínimo requerido):** Chrome 122, Windows 11, app v3.1.0

---

## Bug #2 — Análisis

**Problemas encontrados:**
- El reporte está bien escrito: título claro, pasos específicos y reproducibles, resultados bien diferenciados, entorno documentado.
- El único problema es la **clasificación de severidad y prioridad: están sobrevaloradas**.

Un campo de validación que acepta letras en un teléfono es un bug de validación de datos — no bloquea funcionalidades críticas del sistema. El impacto es Medium (dato incorrecto guardado) con prioridad P2 o P3 (no bloquea al usuario, hay workaround: escribir correctamente).

**Severidad corregida:** Medium — no bloquea flujos críticos; hay workaround (escribir el valor correcto)

**Prioridad corregida:** P2 — debe resolverse, pero no es urgente hoy

---

## Bug #3 — Análisis

**Problemas encontrados:**
- El reporte está muy bien escrito: reproducible, claro, con entorno especificado.
- El problema está únicamente en la **clasificación**: duplicar pedidos y cargos dobles en tarjeta es un impacto financiero directo sobre el usuario. Clasificarlo como Low/P4 es un error grave de priorización.

**Severidad corregida:** Critical — genera cargos económicos no autorizados al usuario

**Prioridad corregida:** P1 — resolver hoy; tiene impacto financiero y de confianza en la plataforma

---

## Bug #4 — Análisis

**Problemas encontrados:**
- Título vago: "a veces los resultados están mal" no indica qué tipo de búsqueda, qué resultado incorrecto
- Pasos no reproducibles: "a veces", "algo", "en algunos casos" son condiciones imposibles de replicar
- Resultado obtenido/esperado genéricos: no describen el comportamiento real
- Entorno incompleto: "Firefox" sin versión; "app nueva" sin número de versión

**Titulo corregido:** `[Búsqueda] Los resultados no se actualizan al borrar el texto del campo de búsqueda`
*(Este es un título de ejemplo; el título real depende de cuál es el comportamiento concreto que se observó.)*

**Pasos corregidos (propuesta):**
1. Ir a la barra de búsqueda principal
2. Escribir `zapatos negros` y esperar los resultados
3. Borrar completamente el texto del campo
4. Observar los resultados mostrados

**Resultado obtenido:** Los resultados de la búsqueda anterior permanecen visibles en lugar de mostrar el estado vacío o el catálogo completo.

**Resultado esperado:** Al vaciar el campo de búsqueda, los resultados deben limpiarse y mostrar el estado inicial.

**Entorno corregido:** Firefox 123, Windows 11, app v3.1.0

---

## Bug #5 — Análisis

**Este reporte está correcto y no necesita cambios.**

Razones:
- Título sigue el formato `[Componente] Descripción del comportamiento`
- Los pasos son específicos, numerados y reproducibles por cualquier persona
- Resultado obtenido y esperado claramente diferenciados
- Severidad High es apropiada: bloquea completamente el registro de nuevos usuarios
- Prioridad P1 es apropiada: ningún usuario nuevo puede crear cuenta — impacto crítico en el negocio
- Entorno detallado con dos navegadores, SO y versión exacta
- Nota adicional con información valiosa sobre reproducibilidad y pruebas cruzadas

**Este es el estándar al que deben acercarse todos los reportes.**
