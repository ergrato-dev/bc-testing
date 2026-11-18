package main

import (
// "net/http" // PASO 1
// "testing" // PASO 1
)

// PASO 1: sin un token válido, la API no dice nada del préstamo.
// func TestLoanRequiresToken(t *testing.T) {
// 	cases := map[string]string{
// 		"sin cabecera":      "",
// 		"token desconocido": "no-existe",
// 		"token de otra API": "ana-token-falso",
// 	}
// 	for name, token := range cases {
// 		t.Run(name, func(t *testing.T) {
// 			srv := newServer(t)
//
// 			res, body := send(t, srv, http.MethodGet, "/api/loans/1", token)
//
// 			if res.StatusCode != http.StatusUnauthorized {
// 				t.Errorf("status = %d, quiero 401; cuerpo: %s", res.StatusCode, body)
// 			}
// 		})
// 	}
// }
//
// PASO 2: cada socio ve sus préstamos; el administrador ve todos.
// func TestLoanOwnerCanRead(t *testing.T) {
// 	for _, token := range []string{"ana-token", "admin-token"} {
// 		t.Run(token, func(t *testing.T) {
// 			srv := newServer(t)
//
// 			res, body := send(t, srv, http.MethodGet, "/api/loans/1", token)
//
// 			if res.StatusCode != http.StatusOK {
// 				t.Errorf("status = %d, quiero 200; cuerpo: %s", res.StatusCode, body)
// 			}
// 		})
// 	}
// }
//
// PASO 3: IDOR. Luis cambia el id de la URL y pide el préstamo de ana.
// func TestLoanIDOR(t *testing.T) {
// 	srv := newServer(t)
//
// 	res, body := send(t, srv, http.MethodGet, "/api/loans/1", "luis-token")
//
// 	if res.StatusCode != http.StatusNotFound {
// 		t.Errorf("status = %d, quiero 404; cuerpo: %s", res.StatusCode, body)
// 	}
// 	// La respuesta tiene que ser la misma que la de un préstamo que no existe:
// 	// si fuera distinta, luis sabría qué ids existen.
// 	_, missing := send(t, srv, http.MethodGet, "/api/loans/999", "luis-token")
// 	if body != missing {
// 		t.Errorf("préstamo ajeno: %s; préstamo inexistente: %s", body, missing)
// 	}
// }
//
// PASO 4: borrar un libro es solo para administradores.
// func TestDeleteBookRequiresAdmin(t *testing.T) {
// 	cases := []struct {
// 		name  string
// 		token string
// 		want  int
// 	}{
// 		{"sin token", "", http.StatusUnauthorized},
// 		{"socio", "ana-token", http.StatusForbidden},
// 		{"administrador", "admin-token", http.StatusNoContent},
// 	}
// 	for _, tc := range cases {
// 		t.Run(tc.name, func(t *testing.T) {
// 			srv := newServer(t)
//
// 			// El libro 6 no tiene préstamos: se puede borrar.
// 			res, body := send(t, srv, http.MethodDelete, "/api/books/6", tc.token)
//
// 			if res.StatusCode != tc.want {
// 				t.Errorf("status = %d, quiero %d; cuerpo: %s", res.StatusCode, tc.want, body)
// 			}
// 		})
// 	}
// }
