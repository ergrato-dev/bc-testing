# Reporte de Bug — Solución

---

## Información del reporte

| Campo | Contenido |
| --- | --- |
| **ID** | BUG-001 |
| **Fecha** | 19/03/2026 |
| **Reportado por** | Usuario (canal Slack #soporte) |
| **Versión de la app** | [Preguntar: ¿qué versión de la app tienes instalada?] |

---

## Descripción del bug

| Campo | Contenido |
| --- | --- |
| **Título** | `[Perfil] La carga de foto de perfil con archivos ≥5 MB entra en bucle infinito y no guarda la imagen` |
| **Severidad** | Medium |
| **Prioridad** | P2 |

---

## Pasos para reproducir

1. Iniciar sesión con cualquier cuenta válida
2. Ir a la sección de perfil (Configuración → Perfil o equivalente)
3. Hacer clic en "Cambiar foto de perfil"
4. Seleccionar un archivo de imagen con tamaño ≥5 MB (JPG, PNG, etc.)
5. Confirmar la selección y esperar el resultado

---

## Resultados

| Campo | Contenido |
| --- | --- |
| **Resultado obtenido** | La página muestra un indicador de carga indefinido (spinner o barra). La carga nunca finaliza. Al refrescar la página, la foto de perfil no se ha guardado — se mantiene la imagen anterior o el avatar por defecto. |
| **Resultado esperado** | El sistema debe subir la imagen, mostrar una barra de progreso y confirmar que la foto fue guardada correctamente. Si el archivo es demasiado grande, debe mostrar el mensaje de error correspondiente (ej: "El archivo no puede superar 2 MB") en lugar de quedarse cargando. |

---

## Entorno

| Campo | Contenido |
| --- | --- |
| **Sistema operativo** | [Preguntar: ¿qué sistema operativo usas? (Windows, macOS, Linux)] |
| **Navegador y versión** | Chrome (versión exacta por confirmar) |
| **Versión de la app** | [Preguntar: ¿qué versión de la app tienes? Se encuentra en Configuración → Acerca de] |
| **Dispositivo** | Computadora de escritorio o laptop (por confirmar) |

---

## Evidencia

| Campo | Contenido |
| --- | --- |
| **Capturas / Video** | [Preguntar: ¿puedes compartir una captura de pantalla o grabación del problema?] |
| **Logs de error** | Solicitar al equipo de backend los logs del endpoint de upload de imágenes en el horario reportado (esta mañana). Buscar timeouts, errores 413 (payload too large) o 500. |

---

## Justificación de clasificación

**Severidad — Medium:**
El bug afecta la funcionalidad de subida de foto de perfil, que es secundaria — no impide el uso de las funciones principales de la app. El usuario puede seguir usando la app normalmente, solo no puede actualizar su foto. No hay pérdida de datos críticos.

Sería High si la carga fallida también bloqueara otras partes del perfil o si afectara a todos los usuarios al mismo tiempo.

**Prioridad — P2:**
No es urgente resolver hoy, pero debe atenderse en el sprint actual porque afecta la experiencia del usuario de forma visible. No es P1 porque tiene bajo impacto en el flujo core de negocio.

---

## Notas para el desarrollador

- Verificar si existe un límite de tamaño configurado en el servidor (Nginx/load balancer) o en la app
- Probable causa: falta de manejo del error 413 (Request Entity Too Large) — la app no tiene un handler para ese código HTTP
- Revisar si el endpoint `/api/profile/avatar` tiene timeout configurado
