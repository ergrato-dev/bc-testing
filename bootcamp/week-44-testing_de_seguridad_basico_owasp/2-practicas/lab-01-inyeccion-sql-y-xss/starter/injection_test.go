package main

import (
// "encoding/json" // PASO 1
// "net/http" // PASO 1
// "net/url" // PASO 2
// "strings" // PASO 3
// "testing" // PASO 1
)

// PASO 1: la búsqueda funciona con un texto normal.
// func TestSearchFindsBooks(t *testing.T) {
// 	srv := newServer(t)
//
// 	res, body := send(t, srv, http.MethodGet, "/api/books?q=go", "")
//
// 	if res.StatusCode != http.StatusOK {
// 		t.Fatalf("status = %d, quiero 200; cuerpo: %s", res.StatusCode, body)
// 	}
// 	var books []book
// 	if err := json.Unmarshal([]byte(body), &books); err != nil {
// 		t.Fatal(err)
// 	}
// 	if len(books) != 2 {
// 		t.Errorf("libros = %d, quiero 2: %s", len(books), body)
// 	}
// }
//
// PASO 2: un texto con comillas es un texto, no SQL. Ningún título contiene
// // estos textos, así que la búsqueda tiene que devolver una lista vacía.
// func TestSearchSQLInjection(t *testing.T) {
// 	payloads := []string{
// 		`'`,
// 		`' OR '1'='1`,
// 		`%' UNION SELECT 0, username, password_hash FROM staff --`,
// 	}
// 	for _, p := range payloads {
// 		t.Run(p, func(t *testing.T) {
// 			srv := newServer(t)
//
// 			res, body := send(t, srv, http.MethodGet, "/api/books?q="+url.QueryEscape(p), "")
//
// 			if res.StatusCode != http.StatusOK {
// 				t.Fatalf("status = %d, quiero 200; cuerpo: %s", res.StatusCode, body)
// 			}
// 			var books []book
// 			if err := json.Unmarshal([]byte(body), &books); err != nil {
// 				t.Fatal(err)
// 			}
// 			if len(books) != 0 {
// 				t.Errorf("libros = %d, quiero 0: %s", len(books), body)
// 			}
// 		})
// 	}
// }
//
// PASO 3: un error de la base de datos no llega al cliente.
// func TestErrorsDoNotLeakDatabaseDetails(t *testing.T) {
// 	srv := newServer(t)
//
// 	// El id de un préstamo es un número: "abc" hace fallar la consulta.
// 	_, body := send(t, srv, http.MethodGet, "/api/loans/abc", "ana-token")
//
// 	for _, leak := range []string{"SQLSTATE", "ERROR:"} {
// 		if strings.Contains(body, leak) {
// 			t.Errorf("la respuesta contiene %q: %s", leak, body)
// 		}
// 	}
// }
//
// PASO 4: lo que escribe el usuario sale escapado en la página HTML.
// func TestSearchPageEscapesHTML(t *testing.T) {
// 	srv := newServer(t)
// 	payload := `<script>alert(1)</script>`
//
// 	_, body := send(t, srv, http.MethodGet, "/buscar?q="+url.QueryEscape(payload), "")
//
// 	if strings.Contains(body, payload) {
// 		t.Errorf("la página contiene %s sin escapar", payload)
// 	}
// 	if !strings.Contains(body, "&lt;script&gt;alert(1)&lt;/script&gt;") {
// 		t.Errorf("la página no contiene el texto escapado:\n%s", body)
// 	}
// }
