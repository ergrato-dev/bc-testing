# Glosario - Semana 44

| Término | Definición |
|---|---|
| OWASP | *Open Worldwide Application Security Project*: fundación sin ánimo de lucro que publica guías y herramientas de seguridad gratuitas (el Top 10, ZAP, la WSTG). |
| OWASP Top 10 | Las diez categorías de riesgo más importantes en aplicaciones web. La edición actual es la de 2025. |
| CWE | *Common Weakness Enumeration*: catálogo de tipos de fallo. La inyección SQL es la CWE-89. |
| CVE | *Common Vulnerabilities and Exposures*: identificador público de una vulnerabilidad concreta de un producto concreto, como CVE-2022-42889. |
| CVSS | Puntuación de 0 a 10 de la gravedad de una vulnerabilidad. 9.0 o más es crítica. |
| Caso de abuso | Test que hace de atacante y comprueba que el sistema se defiende: la respuesta y que nada cambió. |
| Carga (*payload*) | Dato que manda un ataque, como `' OR '1'='1`. |
| Inyección SQL | Un dato del usuario acaba ejecutado como parte de una consulta SQL. Se corrige con parámetros. |
| Parámetro (`$1`) | Hueco de una consulta que se envía separado del valor: el valor nunca puede cambiar la estructura de la consulta. |
| XSS | *Cross-Site Scripting*: un dato del usuario acaba ejecutado como HTML o JavaScript en el navegador de otro. Reflejado, almacenado o DOM. |
| Escapar | Sustituir los caracteres especiales por su forma de texto (`<` por `&lt;`) para que se muestren y no se interpreten. |
| Autenticación | Saber quién hace la petición. Si falla, 401. |
| Autorización | Decidir si quien hace la petición puede hacer lo que pide. Si falla, 403 (o 404 para no revelar que existe). |
| Token Bearer | Cadena secreta en la cabecera `Authorization: Bearer <token>` que identifica al cliente: quien la tiene es el usuario. |
| IDOR | *Insecure Direct Object Reference*: la API busca un recurso por el id de la petición y no comprueba que pertenece a quien lo pide. |
| Enumeración | Descubrir qué recursos existen por las diferencias entre respuestas (403 frente a 404). |
| Asignación masiva (*mass assignment*) | La API copia al objeto campos del cuerpo que el cliente no debería controlar. |
| Cabeceras de seguridad | Cabeceras HTTP que limitan lo que el navegador hace con la respuesta: `X-Content-Type-Options`, CSP, `Cache-Control`, `Referrer-Policy`... |
| CSP | *Content Security Policy*: cabecera que dice de dónde puede cargar la página scripts, estilos e imágenes, y quién puede incrustarla. |
| *Middleware* | Función que envuelve un handler y hace algo antes o después de él, como añadir cabeceras. |
| DAST | *Dynamic Application Security Testing*: probar la seguridad de la aplicación en marcha, desde fuera. ZAP es un DAST. |
| SAST | *Static Application Security Testing*: analizar el código sin ejecutarlo, como `go vet` o staticcheck. |
| SCA | *Software Composition Analysis*: auditar las dependencias de terceros contra una base de datos de vulnerabilidades. |
| OWASP ZAP | Escáner DAST gratuito de OWASP. `zap-baseline.py` analiza de forma pasiva; `zap-full-scan.py` además ataca. |
| Escaneo pasivo | Analiza las respuestas sin mandar nada que un usuario normal no mandaría. |
| Escaneo activo | Manda cargas de ataque para encontrar inyecciones. Puede cambiar o borrar datos. |
| OSV | *Open Source Vulnerabilities*: base de datos abierta de vulnerabilidades de todos los ecosistemas (osv.dev). |
| NVD | *National Vulnerability Database*: la base de datos de CVE del NIST de EE. UU., que usa OWASP Dependency-Check. |
| Vulnerabilidad alcanzable | La que tu código llega a llamar. `govulncheck` la separa de las que solo están en un módulo que usas. |
| `toolchain` (go.mod) | Línea de `go.mod` que hace que el comando `go` descargue y use una versión concreta de Go para el módulo. |
| Pentest | Prueba de penetración: un especialista ataca el sistema con permiso, con creatividad y herramientas propias. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 44](../README.md)
