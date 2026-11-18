package main

import (
// "net/http" // PASO 1
// "testing" // PASO 1
)

// PASO 1: las cabeceras que lleva cada respuesta, con su valor exacto.
// var securityHeaders = [][2]string{
// 	{"X-Content-Type-Options", "nosniff"},
// 	{"Content-Security-Policy", "default-src 'none'; base-uri 'none'; form-action 'self'; frame-ancestors 'none'"},
// 	{"Referrer-Policy", "no-referrer"},
// 	{"Cache-Control", "no-store"},
// 	{"Permissions-Policy", "camera=(), microphone=(), geolocation=()"},
// 	{"Cross-Origin-Opener-Policy", "same-origin"},
// 	{"Cross-Origin-Resource-Policy", "same-origin"},
// 	{"Cross-Origin-Embedder-Policy", "require-corp"},
// }
//
// // Todas las respuestas llevan las cabeceras, también los errores.
// func TestSecurityHeaders(t *testing.T) {
// 	paths := []string{
// 		"/",               // HTML, 200
// 		"/buscar?q=go",    // HTML, 200
// 		"/api/books?q=go", // JSON, 200
// 		"/api/loans/1",    // JSON, 401: sin token
// 		"/no-existe",      // 404 del ServeMux
// 	}
// 	for _, path := range paths {
// 		t.Run(path, func(t *testing.T) {
// 			srv := newServer(t)
//
// 			res, _ := send(t, srv, http.MethodGet, path, "")
//
// 			for _, h := range securityHeaders {
// 				name, want := h[0], h[1]
// 				if got := res.Header.Get(name); got != want {
// 					t.Errorf("%s = %q, quiero %q", name, got, want)
// 				}
// 			}
// 		})
// 	}
// }
//
// PASO 2: la respuesta no dice qué servidor ni qué versión hay detrás.
// func TestNoServerBanner(t *testing.T) {
// 	srv := newServer(t)
//
// 	res, _ := send(t, srv, http.MethodGet, "/", "")
//
// 	for _, name := range []string{"Server", "X-Powered-By"} {
// 		if got := res.Header.Get(name); got != "" {
// 			t.Errorf("%s = %q, quiero que no esté", name, got)
// 		}
// 	}
// }
