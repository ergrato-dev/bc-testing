package openlib

import (
	"context"
	"errors"
	"net/http"
	"testing"
	// "fmt" // PASO 2
	// "io" // PASO 2
	// "net/http/httptest" // PASO 1
	// "time" // PASO 3
	// "github.com/google/go-cmp/cmp" // PASO 1
)

// PASO 0: un contexto cancelado corta la petición antes de salir a la red.
func TestLookupCanceledContext(t *testing.T) {
	ctx, cancel := context.WithCancel(t.Context())
	cancel()
	c := &Client{BaseURL: "http://catalog.invalid", HTTP: http.DefaultClient}

	_, err := c.Lookup(ctx, "978-0")

	if !errors.Is(err, context.Canceled) {
		t.Errorf("err = %v, want context.Canceled", err)
	}
}

// PASO 1: un servidor de verdad en un puerto local, con respuesta fija.
// func TestLookup(t *testing.T) {
// 	var gotMethod, gotPath, gotAccept string
// 	srv := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		gotMethod, gotPath, gotAccept = r.Method, r.URL.Path, r.Header.Get("Accept")
// 		w.Header().Set("Content-Type", "application/json")
// 		w.Write([]byte(`{"isbn":"978-84-376-0494-7","title":"Rayuela","authors":["Julio Cortázar"]}`))
// 	}))
// 	t.Cleanup(srv.Close)
// 	c := &Client{BaseURL: srv.URL, HTTP: srv.Client()}
//
// 	got, err := c.Lookup(t.Context(), "978-84-376-0494-7")
//
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	want := Book{ISBN: "978-84-376-0494-7", Title: "Rayuela", Authors: []string{"Julio Cortázar"}}
// 	if diff := cmp.Diff(want, got); diff != "" {
// 		t.Errorf("Lookup() mismatch (-want +got):\n%s", diff)
// 	}
// 	if gotMethod != http.MethodGet || gotPath != "/isbn/978-84-376-0494-7" || gotAccept != "application/json" {
// 		t.Errorf("request = %s %s Accept=%q, want GET /isbn/978-84-376-0494-7 Accept=\"application/json\"", gotMethod, gotPath, gotAccept)
// 	}
// }

// PASO 2: las respuestas que no son un 200 con JSON válido.
// // stubServer responde siempre con el mismo estado y el mismo cuerpo.
// func stubServer(t *testing.T, status int, body string) *httptest.Server {
// 	t.Helper()
// 	srv := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		w.WriteHeader(status)
// 		fmt.Fprint(w, body)
// 	}))
// 	t.Cleanup(srv.Close)
// 	return srv
// }
//
// func TestLookupErrors(t *testing.T) {
// 	tests := []struct {
// 		name    string
// 		status  int
// 		body    string
// 		wantErr error
// 	}{
// 		{"not found", http.StatusNotFound, `{"error":"no such isbn"}`, ErrNotFound},
// 		{"server error", http.StatusServiceUnavailable, "<html>maintenance</html>", ErrUnavailable},
// 		{"truncated json", http.StatusOK, `{"title":`, io.ErrUnexpectedEOF},
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			srv := stubServer(t, tt.status, tt.body)
// 			c := &Client{BaseURL: srv.URL, HTTP: srv.Client()}
//
// 			_, err := c.Lookup(t.Context(), "978-0")
//
// 			if !errors.Is(err, tt.wantErr) {
// 				t.Errorf("err = %v, want %v", err, tt.wantErr)
// 			}
// 		})
// 	}
// }

// PASO 3: un servidor que no contesta a tiempo.
// func TestLookupTimeout(t *testing.T) {
// 	srv := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		<-r.Context().Done() // espera hasta que el cliente se rinda
// 	}))
// 	t.Cleanup(srv.Close)
// 	c := &Client{BaseURL: srv.URL, HTTP: srv.Client()}
// 	ctx, cancel := context.WithTimeout(t.Context(), 50*time.Millisecond)
// 	defer cancel()
//
// 	_, err := c.Lookup(ctx, "978-0")
//
// 	if !errors.Is(err, context.DeadlineExceeded) {
// 		t.Errorf("err = %v, want context.DeadlineExceeded", err)
// 	}
// }

// PASO 4: el mismo cliente contra un servidor HTTPS.
// func TestLookupTLS(t *testing.T) {
// 	srv := httptest.NewTLSServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
// 		w.Write([]byte(`{"isbn":"978-0","title":"Ficciones","authors":["Jorge Luis Borges"]}`))
// 	}))
// 	t.Cleanup(srv.Close)
// 	c := &Client{BaseURL: srv.URL, HTTP: srv.Client()}
//
// 	got, err := c.Lookup(t.Context(), "978-0")
//
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	if got.Title != "Ficciones" {
// 		t.Errorf("Title = %q, want Ficciones", got.Title)
// 	}
// }
