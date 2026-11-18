# Informe de seguridad - TODO nombre del sistema

## 1. Alcance y entorno

TODO: qué se probó (la API Go: rutas, autenticación) y qué no (las otras APIs, la interfaz). Versiones: Go, ZAP, auditores. Dónde se ejecutó.

## 2. Tests de seguridad

TODO: una fila por test `TestSecurity...`, con la categoría del OWASP Top 10 2025 que cubre y su resultado contra el código anterior a la semana (rojo o verde, y por qué).

| Test | Categoría | Antes de corregir | Después |
|---|---|---|---|
| TODO | TODO | TODO | TODO |

## 3. Hallazgos y correcciones

TODO: cada fallo encontrado, con la petición que lo demuestra, su gravedad, la corrección y el test que lo vigila desde ahora.

## 4. ZAP baseline

TODO: avisos antes y después (con los números de `security/results/zap-baseline.txt`), y cada regla de `security/zap.conf` con su motivo.

## 5. Auditoría de dependencias

TODO: una fila por auditoría de `security/audit.sh`: herramienta, hallazgos, acción (actualizar, ignorar con motivo) y resultado final.

| Lenguaje | Herramienta | Hallazgos | Acción | Después |
|---|---|---|---|---|
| TODO | TODO | TODO | TODO | TODO |

## 6. Riesgos y lo que no se probó

TODO: lo que estas pruebas no cubren y por qué importa.
