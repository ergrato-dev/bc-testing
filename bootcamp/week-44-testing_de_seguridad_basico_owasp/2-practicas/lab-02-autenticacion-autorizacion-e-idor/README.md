# Lab 02 - Autenticación, autorización e IDOR

## Objetivo

Probar la matriz de acceso de la biblioteca (quién puede leer cada préstamo y quién puede borrar libros), encontrar el IDOR de los préstamos y la ruta que no pide token, y corregir la API.

## Tiempo estimado

55 minutos.

## Requisitos

- La [teoría 03](../../1-teoria/03-autenticacion-autorizacion-e-idor.md).
- El [lab 01](../lab-01-inyeccion-sql-y-xss/README.md) terminado: este lab sigue sobre la misma `biblioteca-insegura/`.

## Punto de partida

[`starter/access_test.go`](starter/access_test.go) tiene cuatro tests comentados por PASOS. La especificación que prueban es la columna "Quién" de la [tabla de rutas](../README.md#la-api-de-la-biblioteca-insegura):

| | `GET /api/loans/1` (de ana) | `DELETE /api/books/6` |
|---|---|---|
| Sin token | 401 | 401 |
| ana | 200 | 403 |
| luis | 404 | 403 |
| admin | 200 | 204 |

```bash
cd ../biblioteca-insegura
cp ../lab-02-autenticacion-autorizacion-e-idor/starter/access_test.go .
```

---

## Paso 1: sin token, 401

Descomenta el PASO 1 y los imports. Tres casos sin un token válido: sin cabecera, un token que no existe y uno que se parece a uno bueno (`ana-token-falso`).

```bash
go test -run TestLoanRequiresToken -v . | grep -E "^(---|    ---|ok)"
# --- PASS: TestLoanRequiresToken (0.03s)
#     --- PASS: TestLoanRequiresToken/token_de_otra_API (0.01s)
#     --- PASS: TestLoanRequiresToken/sin_cabecera (0.01s)
#     --- PASS: TestLoanRequiresToken/token_desconocido (0.01s)
# ok  	example.com/biblioteca	0.029s
```

Pasa a la primera: `GET /api/loans/{id}` sí comprueba el token. Los subtests salen en otro orden que en el código porque los casos están en un mapa, y Go recorre los mapas en orden aleatorio. El test se queda como guardia: en el paso 4 verás una ruta que olvidó la comprobación.

## Paso 2: el dueño y el administrador leen el préstamo

Descomenta el PASO 2. Las dos celdas de la matriz que deben responder 200.

```bash
go test -run TestLoanOwnerCanRead -v . | grep -E "^(---|    ---|ok)"
# --- PASS: TestLoanOwnerCanRead (0.02s)
#     --- PASS: TestLoanOwnerCanRead/ana-token (0.01s)
#     --- PASS: TestLoanOwnerCanRead/admin-token (0.01s)
# ok  	example.com/biblioteca	0.024s
```

También pasa. Este test protege la corrección del paso 3: una comprobación de dueño demasiado estricta dejaría al administrador sin acceso, y este test lo diría.

## Paso 3: IDOR

Descomenta el PASO 3. Luis pide el préstamo 1, que es de ana. El test espera 404 y, además, el mismo cuerpo que para un préstamo que no existe (el 999).

```bash
go test -run TestLoanIDOR .
# --- FAIL: TestLoanIDOR (0.01s)
#     access_test.go:50: status = 200, quiero 404; cuerpo: {"id":1,"bookId":1,"member":"ana"}
#     access_test.go:56: préstamo ajeno: {"id":1,"bookId":1,"member":"ana"}
#         ; préstamo inexistente: {"error":"loan not found"}
# FAIL
```

(El `;` sale en otra línea porque el cuerpo JSON termina en un salto de línea.) Con la API arrancada (`go run .` en otra terminal), luis puede recorrer los ids:

```bash
for i in 1 2 3; do curl -s -o /dev/null -w "luis -> /api/loans/$i: %{http_code}\n" -H 'Authorization: Bearer luis-token' localhost:18044/api/loans/$i; done
# luis -> /api/loans/1: 200
# luis -> /api/loans/2: 200
# luis -> /api/loans/3: 404
```

`-o /dev/null` descarta el cuerpo y `-w` escribe solo el código de estado. Luis ve el préstamo de ana y sabe qué ids existen.

Corrige el handler de `GET /api/loans/{id}` en `main.go`. Guarda el usuario del token (ahora el código lo descarta con `_`):

```go
u, ok := currentUser(r)
if !ok {
	writeError(w, http.StatusUnauthorized, "missing or invalid token")
	return
}
```

Y después de la consulta, un préstamo ajeno responde exactamente lo mismo que uno que no existe:

```go
// El préstamo de otro socio responde igual que uno que no existe.
if errors.Is(err, pgx.ErrNoRows) || (err == nil && l.Member != u.Name && u.Role != "admin") {
	writeError(w, http.StatusNotFound, "loan not found")
	return
}
```

```bash
go test -run TestLoan .
# ok  	example.com/biblioteca	0.058s
```

Reinicia la API y repite el bucle:

```bash
# luis -> /api/loans/1: 404
# luis -> /api/loans/2: 200
# luis -> /api/loans/3: 404
```

## Paso 4: borrar es solo para administradores

Descomenta el PASO 4. Tres filas de la columna `DELETE /api/books/6`: el libro 6 no tiene préstamos, así que el administrador lo puede borrar. La transacción del test se deshace: el libro vuelve a estar ahí en el siguiente test.

```bash
go test -run TestDeleteBook .
# --- FAIL: TestDeleteBookRequiresAdmin (0.03s)
#     --- FAIL: TestDeleteBookRequiresAdmin/sin_token (0.01s)
#         access_test.go:79: status = 204, quiero 401; cuerpo: 
#     --- FAIL: TestDeleteBookRequiresAdmin/socio (0.01s)
#         access_test.go:79: status = 204, quiero 403; cuerpo: 
# FAIL
```

Cualquiera, sin token, borra libros. La ruta no comprueba nada: le falta la autenticación (401) y la autorización (403). Añade las dos al principio del handler de `DELETE /api/books/{id}`:

```go
u, ok := currentUser(r)
if !ok {
	writeError(w, http.StatusUnauthorized, "missing or invalid token")
	return
}
if u.Role != "admin" {
	writeError(w, http.StatusForbidden, "admin only")
	return
}
```

```bash
go test -run TestDeleteBook -v . | grep -E "^(---|    ---|ok)"
# --- PASS: TestDeleteBookRequiresAdmin (0.02s)
#     --- PASS: TestDeleteBookRequiresAdmin/sin_token (0.01s)
#     --- PASS: TestDeleteBookRequiresAdmin/socio (0.01s)
#     --- PASS: TestDeleteBookRequiresAdmin/administrador (0.01s)
# ok  	example.com/biblioteca	0.029s
```

**Experimento: la fila que falta.** Borra temporalmente la fila `{"administrador", "admin-token", http.StatusNoContent},` del test y rompe la corrección cambiando `u.Role != "admin"` por `u.Role != "superadmin"`, un rol que nadie tiene:

```bash
go test -run TestDeleteBook -v . | grep -E "^(---|    ---|ok)"
# --- PASS: TestDeleteBookRequiresAdmin (0.02s)
#     --- PASS: TestDeleteBookRequiresAdmin/sin_token (0.01s)
#     --- PASS: TestDeleteBookRequiresAdmin/socio (0.01s)
# ok  	example.com/biblioteca	0.019s
```

Verde, y nadie puede borrar, tampoco el administrador. Sin la fila del caso permitido, una API que lo prohíbe todo pasa los tests de autorización. Deshaz los dos cambios.

---

## Comprobación final

```bash
go vet .
go test .
# ok  	example.com/biblioteca	0.139s
```

## Qué has aprendido

- La matriz de usuarios por acciones se escribe antes de mirar el código y se prueba entera, incluidas las celdas permitidas.
- Un test que pasa a la primera (`TestLoanRequiresToken`) es una guardia para la próxima ruta; la ruta de borrar es justo la que la olvidó.
- Un IDOR se encuentra con dos usuarios. La respuesta al recurso ajeno es idéntica a la del inexistente, y el test compara los cuerpos.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Inyección SQL y XSS](../lab-01-inyeccion-sql-y-xss/README.md) | [Lab 03 - Cabeceras y ZAP baseline](../lab-03-cabeceras-y-zap-baseline/README.md) → |

[↑ Semana 44](../../README.md)
