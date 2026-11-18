# Prácticas - Semana 44

Cuatro labs guiados (4 h en total). Los tres primeros trabajan sobre la misma API: la de una biblioteca escrita en Go sobre PostgreSQL, con errores de seguridad a propósito, en [biblioteca-insegura/](biblioteca-insegura/). Cada lab copia un archivo de tests en esa carpeta, lo ve fallar contra la API vulnerable y corrige la API hasta que pasa: al terminar el lab 03, la biblioteca ya no es insegura. El lab 04 audita las dependencias de cinco proyectos pequeños, uno por lenguaje. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Inyección SQL y XSS](lab-01-inyeccion-sql-y-xss/README.md) | Cargas de inyección en un test de tabla, consultas con parámetros, errores que no enseñan el SQL y `html/template` | 60 min |
| [Lab 02 - Autenticación, autorización e IDOR](lab-02-autenticacion-autorizacion-e-idor/README.md) | 401, 403 y 404; la matriz de usuarios por acción; un IDOR con dos usuarios y la enumeración | 55 min |
| [Lab 03 - Cabeceras y ZAP baseline](lab-03-cabeceras-y-zap-baseline/README.md) | Un *middleware* de cabeceras comprobado en un test, ZAP baseline con Docker antes y después, el archivo de reglas y el escaneo activo | 65 min |
| [Lab 04 - Auditoría de dependencias](lab-04-auditoria-de-dependencias/README.md) | `pnpm audit`, `uv audit`, OSV-Scanner, `govulncheck` y `cargo audit`: encontrar, leer y corregir | 60 min |

## La API de la biblioteca insegura

| Ruta | Qué hace | Quién |
|---|---|---|
| `GET /` | Página HTML con el buscador | Cualquiera |
| `GET /buscar?q=texto` | Página HTML con los libros cuyo título contiene el texto | Cualquiera |
| `GET /api/books?q=texto` | JSON con los libros cuyo título contiene el texto | Cualquiera |
| `GET /api/loans/{id}` | JSON con un préstamo | El socio dueño del préstamo y el administrador |
| `DELETE /api/books/{id}` | Borra un libro: 204 | Solo el administrador |

Hay 6 libros (ids 1 a 6) y dos préstamos: el 1 es de ana y el 2 de luis. La columna "Quién" es la especificación: lo que la API debería hacer, no lo que hace.

Los usuarios se identifican con un token fijo en la cabecera `Authorization: Bearer <token>`:

| Token | Usuario | Rol |
|---|---|---|
| `ana-token` | ana | socia |
| `luis-token` | luis | socio |
| `admin-token` | admin | administrador |

La base de datos tiene además una tabla `staff` con el usuario y el *hash* de la contraseña del administrador, que la API nunca consulta: si aparece en una respuesta, hay inyección.

> **La API es vulnerable a propósito.** Escucha solo en `localhost`; no la expongas fuera de tu equipo ni copies su código a un proyecto.

Arráncala así, desde `biblioteca-insegura/`:

1. PostgreSQL, con Docker. La primera vez crea las tablas y los datos (`db/init.sql`):

   ```bash
   cd biblioteca-insegura
   docker compose up -d --wait
   #  Container biblioteca-insegura-db-1 Healthy
   ```

2. La API, en una terminal propia. Solo hace falta en los pasos con `curl` y en el lab 03 (ZAP); los tests arrancan su propio servidor:

   ```bash
   go run .
   # 2026/11/18 18:45:40 Biblioteca insegura en http://localhost:18044
   ```

3. Comprueba que responde:

   ```bash
   curl -s "localhost:18044/api/books?q=go"
   # [{"id":1,"title":"The Go Programming Language","author":"Alan Donovan"},{"id":2,"title":"Learning Go","author":"Jon Bodner"}]
   ```

### Los tests

Los tests van en la misma carpeta que la API (`package main`) y usan dos funciones de [`helpers_test.go`](biblioteca-insegura/helpers_test.go):

- `newServer(t)` arranca la API en un servidor de prueba (`httptest.NewServer`, [semana 34](../../week-34-http_bd_e_integracion_en_go/README.md)) sobre una **transacción** de PostgreSQL que se deshace al terminar el test. Lo que un test borra no llega a la base de datos. Necesita PostgreSQL arrancado.
- `send(t, srv, método, ruta, token)` hace la petición, con `Authorization: Bearer <token>` si el token no está vacío, y devuelve la respuesta y el cuerpo.

Una consulta que falla dentro de una transacción la deja abortada: las siguientes consultas de ese test fallan con `current transaction is aborted` (SQLSTATE 25P02). Por eso cada carga de los tests de inyección arranca su propio servidor con `newServer`.

Al terminar la semana, para PostgreSQL y borra sus datos:

```bash
docker compose down -v
```

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Cabeceras, ZAP y auditoría de dependencias](../1-teoria/04-cabeceras-zap-y-dependencias.md) | [Lab 01 - Inyección SQL y XSS](lab-01-inyeccion-sql-y-xss/README.md) → |

[↑ Semana 44](../README.md)
