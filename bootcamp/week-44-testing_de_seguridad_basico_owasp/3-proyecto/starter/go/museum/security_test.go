package museum

// Tests de seguridad de la API (semana 44). Todos se llaman TestSecurity...: el
// autograding los ejecuta con -run '^TestSecurity'. El ejemplo es el del museo;
// cambia rutas, cuerpos, tokens y fakes por los de tu dominio.

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

// send hace la petición con la cabecera Authorization que se le pasa (vacía: sin
// cabecera) y devuelve la respuesta grabada.
func send(t *testing.T, h http.Handler, method, target, auth, body string) *http.Response {
	t.Helper()
	req := httptest.NewRequest(method, target, strings.NewReader(body))
	if auth != "" {
		req.Header.Set("Authorization", auth)
	}
	rec := httptest.NewRecorder()
	h.ServeHTTP(rec, req)
	return rec.Result()
}

// securityHeaders son las cabeceras que lleva cada respuesta de la API, con su
// valor exacto. Una API que solo devuelve JSON no carga nada ni se incrusta en
// otra página: la CSP más estricta.
var securityHeaders = [][2]string{
	{"X-Content-Type-Options", "nosniff"},
	{"Content-Security-Policy", "default-src 'none'; frame-ancestors 'none'"},
	{"Cache-Control", "no-store"},
	{"Referrer-Policy", "no-referrer"},
	{"Cross-Origin-Resource-Policy", "same-origin"},
}

// TODO paso 2: TestSecurityHeaders. Una petición de cada tipo (200, 404 del
// dominio, 401, 405 y 404 del ServeMux) y, en cada una, las cabeceras de
// securityHeaders.

// TODO paso 3: TestSecurityLendRequiresToken. Sin cabecera, con un token
// desconocido, con el esquema Basic y con "Bearer " vacío: 401, cabecera
// WWW-Authenticate: Bearer y nada cambia en el almacén.

// TODO paso 4: el IDOR al escribir. Con el token de una institución y otra
// institución en el cuerpo: 403 y nada cambia. Y otro test: sin institución en
// el cuerpo, el préstamo queda a nombre de la institución del token.

// TODO paso 5: inyección y manipulación de parámetros. Códigos con SQL, rutas de
// archivo y HTML en la URL (404, nunca 500); valores fuera de rango y de otro
// tipo en el cuerpo (422 o 400, y nada cambia); campos que el cliente no
// controla (se ignoran).
