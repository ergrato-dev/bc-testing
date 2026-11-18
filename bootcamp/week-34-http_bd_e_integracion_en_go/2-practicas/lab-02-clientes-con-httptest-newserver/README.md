# Lab 02 - Clientes HTTP con httptest.NewServer

## Objetivo

Probar un cliente HTTP contra un servidor de pruebas que corre en un puerto local: comprobar la petición que envía, cómo trata cada estado de error y un JSON roto, cómo respeta un plazo con `context`, y cómo habla con un servidor HTTPS.

## Tiempo estimado

60 minutos.

## Preparación

```bash
cd starter
go mod download
go test -count=1 -v ./...
# === RUN   TestLookupCanceledContext
# --- PASS: TestLookupCanceledContext (0.00s)
# PASS
# ok  	bootcamp/lab02	0.004s
```

`openlib.go` tiene un `Client` que consulta un catálogo bibliográfico remoto. `Lookup(ctx, isbn)` pide `GET {BaseURL}/isbn/{isbn}` con la cabecera `Accept: application/json` y devuelve:

| Respuesta del servidor | Resultado de `Lookup` |
|---|---|
| `200` con un JSON válido | El `Book` decodificado |
| `404` | Un error que envuelve `ErrNotFound` |
| Cualquier otro estado | Un error que envuelve `ErrUnavailable` |
| `200` con un JSON que no se puede leer | El error de `encoding/json`, envuelto |
| El plazo de `ctx` vence antes | Un error que envuelve `context.DeadlineExceeded` |

El `Client` recibe la URL base y el `*http.Client` como campos. Así, el test puede pasarle los de su servidor de pruebas.

`TestLookupCanceledContext` (PASO 0) no necesita servidor. Con el contexto ya cancelado, `Do` devuelve el error sin intentar conectar, así que el dominio `catalog.invalid` nunca se consulta.

## Paso a paso

### Paso 1: un servidor de pruebas con respuesta fija (PASO 1)

1. En el import, descomenta `"net/http/httptest"` y `cmp`.
2. Descomenta el bloque del PASO 1 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -v ./...
   # === RUN   TestLookupCanceledContext
   # --- PASS: TestLookupCanceledContext (0.00s)
   # === RUN   TestLookup
   # --- PASS: TestLookup (0.00s)
   # PASS
   # ok  	bootcamp/lab02	0.007s
   ```

`httptest.NewServer(handler)` arranca un servidor HTTP de verdad en `127.0.0.1`, en un puerto libre que elige el sistema ([teoría 01](../../1-teoria/01-httptest-handlers-y-clientes.md#2-clientes-con-newserver)). Desde ahí:

| Línea | Qué hace |
|---|---|
| `srv.URL` | La URL base, por ejemplo `http://127.0.0.1:46743` |
| `srv.Client()` | Un `*http.Client` configurado para ese servidor |
| `t.Cleanup(srv.Close)` | Cierra el servidor al terminar el test, aunque falle |

El handler del servidor es un **spy** ([semana 33](../../../week-33-interfaces_y_dobles_de_prueba_en_go/1-teoria/01-interfaces-y-dobles-a-mano.md)): guarda el método, la ruta y la cabecera `Accept` que recibió, y el test los comprueba **después** de la llamada. El handler corre en otra goroutine, la del servidor. Ahí se puede usar `t.Errorf`, pero no `t.Fatalf`, que solo funciona en la goroutine del test.

### Paso 2: estados de error y un JSON roto (PASO 2)

1. En el import, descomenta `"fmt"` e `"io"`.
2. Descomenta el bloque del PASO 2 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -run Errors -v ./...
   # --- PASS: TestLookupErrors (0.00s)
   #     --- PASS: TestLookupErrors/not_found (0.00s)
   #     --- PASS: TestLookupErrors/server_error (0.00s)
   #     --- PASS: TestLookupErrors/truncated_json (0.00s)
   # PASS
   # ok  	bootcamp/lab02	0.007s
   ```

`stubServer` crea un servidor que responde siempre lo mismo: un **stub**. Cada fila de la tabla arranca el suyo, y `t.Cleanup` lo cierra al terminar la fila. El JSON cortado, `{"title":`, termina en `io.ErrUnexpectedEOF`. Como `Lookup` lo envuelve con `%w`, `errors.Is` lo encuentra.

### Paso 3: un servidor que no contesta a tiempo (PASO 3)

1. En el import, descomenta `"time"`.
2. Descomenta el bloque del PASO 3 y guarda.
3. Ejecuta:

   ```bash
   go test -count=1 -run Timeout -v ./...
   # === RUN   TestLookupTimeout
   # --- PASS: TestLookupTimeout (0.05s)
   # PASS
   # ok  	bootcamp/lab02	0.057s
   ```

El handler no contesta: espera en `<-r.Context().Done()` hasta que el cliente cierra la conexión. El test le da a `Lookup` un contexto con un plazo de 50 ms, y el error envuelve `context.DeadlineExceeded`. El test dura los 50 ms del plazo, no lo que tardaría un servidor lento de verdad.

### Paso 4: el mismo cliente contra HTTPS (PASO 4)

Descomenta el bloque del PASO 4:

```bash
go test -count=1 -run TLS -v ./...
# === RUN   TestLookupTLS
# --- PASS: TestLookupTLS (0.00s)
# PASS
# ok  	bootcamp/lab02	0.008s
```

`httptest.NewTLSServer` arranca el servidor con HTTPS y un certificado propio, generado para el test. `srv.Client()` devuelve un cliente que confía en ese certificado. El código de `Lookup` no cambia: solo cambia la URL, que ahora empieza por `https://`.

## Experimentos

### Experimento 1: la cabecera que se olvida

Con el PASO 1 aplicado, borra de `openlib.go` la línea `req.Header.Set("Accept", "application/json")`:

```bash
go test -count=1 ./...
# --- FAIL: TestLookup (0.00s)
#     openlib_test.go:49: request = GET /isbn/978-84-376-0494-7 Accept="", want GET /isbn/978-84-376-0494-7 Accept="application/json"
# FAIL
```

El servidor de pruebas sigue respondiendo el mismo JSON, así que el `Book` es correcto. Solo el spy ve que falta la cabecera. Un servidor real que elige el formato según `Accept` podría responder otra cosa sin ella. Restaura la línea.

### Experimento 2: un 404 tratado como caída

Con el PASO 2 aplicado, borra de `openlib.go` el `case` del `404` (las dos líneas):

```bash
go test -count=1 -run Errors ./...
# --- FAIL: TestLookupErrors (0.00s)
#     --- FAIL: TestLookupErrors/not_found (0.00s)
#         openlib_test.go:84: err = catalog unavailable: status 404, want isbn not found
# FAIL
```

Para quien llama a `Lookup`, la diferencia importa: un ISBN que no existe no se reintenta, y un catálogo caído sí. Restaura el `case`.

### Experimento 3: un servidor que duerme

Con el PASO 3 aplicado, en el handler de `TestLookupTimeout` cambia `<-r.Context().Done()` por `time.Sleep(2 * time.Second)`:

```bash
go test -count=1 -run Timeout -v ./...
# === RUN   TestLookupTimeout
# --- PASS: TestLookupTimeout (2.00s)
# PASS
# ok  	bootcamp/lab02	2.008s
```

El test pasa: `Lookup` se rinde a los 50 ms. Pero dura 2 segundos, porque `srv.Close`, desde `t.Cleanup`, espera a que terminen todas las peticiones en curso, y el handler sigue durmiendo. Un handler de prueba que espera debe escuchar `r.Context().Done()`. Restaura el handler.

### Experimento 4: HTTPS con un cliente cualquiera

Con el PASO 4 aplicado, en `TestLookupTLS` cambia `HTTP: srv.Client()` por `HTTP: http.DefaultClient`:

```bash
go test -count=1 -run TLS ./...
# 2026/11/18 02:26:11 http: TLS handshake error from 127.0.0.1:38894: remote error: tls: bad certificate
# --- FAIL: TestLookupTLS (0.02s)
#     openlib_test.go:118: lookup 978-0: Get "https://127.0.0.1:46743/isbn/978-0": tls: failed to verify certificate: x509: certificate signed by unknown authority
# FAIL
```

Los puertos cambian en cada ejecución. `http.DefaultClient` no conoce el certificado del servidor de pruebas y rechaza la conexión. La primera línea es el registro del propio servidor, que ve al cliente colgar a mitad de la negociación TLS. La solución es `srv.Client()`. Desactivar la verificación de certificados (`InsecureSkipVerify`) haría pasar el test sin probar nada. Restaura `srv.Client()`.

## Preguntas de cierre

1. ¿Qué diferencia hay entre probar un handler con `NewRecorder` (lab 01) y un cliente con `NewServer`? ¿Qué pasa por la red en cada caso?
2. ¿Por qué el handler del servidor de pruebas usa `t.Errorf` y no `t.Fatalf`?
3. ¿Por qué `TestLookupTimeout` dura 2 segundos con `time.Sleep`, si `Lookup` se rinde a los 50 ms?
4. ¿Qué devuelve `srv.Client()` que no tiene `http.DefaultClient`?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Handlers con httptest](../lab-01-handlers-con-httptest/README.md) | [Lab 03 - TestMain, build tags y -short](../lab-03-testmain-build-tags-y-short/README.md) → |

[↑ Semana 34](../../README.md)
