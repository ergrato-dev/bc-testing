package catalog

import (
	"errors"
	"fmt"
	"testing"
	// "encoding/json" // PASO 1
	// "net/http" // PASO 1
	// "net/http/httptest" // PASO 1
	// "strings" // PASO 2
	// "github.com/google/go-cmp/cmp" // PASO 1
)

// fakeStore guarda los libros en un mapa: se comporta como el almacén real,
// incluidos sus errores, pero en memoria.
type fakeStore map[string]Book

func (s fakeStore) Get(isbn string) (Book, error) {
	b, ok := s[isbn]
	if !ok {
		return Book{}, fmt.Errorf("%w: %s", ErrNotFound, isbn)
	}
	return b, nil
}

func (s fakeStore) Add(b Book) error {
	if _, ok := s[b.ISBN]; ok {
		return fmt.Errorf("%w: %s", ErrDuplicate, b.ISBN)
	}
	s[b.ISBN] = b
	return nil
}

var rayuela = Book{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2}

// PASO 0: el fake responde como el almacén real, errores incluidos.
func TestFakeStore(t *testing.T) {
	s := fakeStore{}
	if err := s.Add(rayuela); err != nil {
		t.Fatal(err)
	}
	if err := s.Add(rayuela); !errors.Is(err, ErrDuplicate) {
		t.Errorf("second Add: err = %v, want ErrDuplicate", err)
	}
	if _, err := s.Get("000"); !errors.Is(err, ErrNotFound) {
		t.Errorf("Get(000): err = %v, want ErrNotFound", err)
	}
}

// PASO 1: una petición al handler con NewRequest y NewRecorder.
// func TestGetBook(t *testing.T) {
// 	h := NewHandler(fakeStore{rayuela.ISBN: rayuela})
// 	req := httptest.NewRequest(http.MethodGet, "/books/"+rayuela.ISBN, nil)
// 	rec := httptest.NewRecorder()
//
// 	h.ServeHTTP(rec, req)
//
// 	res := rec.Result()
// 	if res.StatusCode != http.StatusOK {
// 		t.Fatalf("status = %d, want %d", res.StatusCode, http.StatusOK)
// 	}
// 	if got := res.Header.Get("Content-Type"); got != "application/json" {
// 		t.Errorf("Content-Type = %q, want application/json", got)
// 	}
// 	var got Book
// 	if err := json.NewDecoder(res.Body).Decode(&got); err != nil {
// 		t.Fatal(err)
// 	}
// 	if diff := cmp.Diff(rayuela, got); diff != "" {
// 		t.Errorf("body mismatch (-want +got):\n%s", diff)
// 	}
// }

// PASO 2: las rutas que no existen y los métodos que no admiten.
// // serve envía la petición al handler y devuelve la respuesta grabada.
// func serve(t *testing.T, h http.Handler, method, target, body string) *http.Response {
// 	t.Helper()
// 	req := httptest.NewRequest(method, target, strings.NewReader(body))
// 	rec := httptest.NewRecorder()
// 	h.ServeHTTP(rec, req)
// 	return rec.Result()
// }
//
// func TestRouting(t *testing.T) {
// 	h := NewHandler(fakeStore{rayuela.ISBN: rayuela})
// 	tests := []struct {
// 		name, method, target string
// 		wantStatus           int
// 		wantAllow            string
// 	}{
// 		{"unknown book", http.MethodGet, "/books/000", http.StatusNotFound, ""},
// 		{"unknown path", http.MethodGet, "/authors", http.StatusNotFound, ""},
// 		{"delete not allowed", http.MethodDelete, "/books/" + rayuela.ISBN, http.StatusMethodNotAllowed, "GET, HEAD"},
// 		{"get on collection", http.MethodGet, "/books", http.StatusMethodNotAllowed, "POST"},
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			res := serve(t, h, tt.method, tt.target, "")
// 			if res.StatusCode != tt.wantStatus {
// 				t.Errorf("status = %d, want %d", res.StatusCode, tt.wantStatus)
// 			}
// 			if got := res.Header.Get("Allow"); got != tt.wantAllow {
// 				t.Errorf("Allow = %q, want %q", got, tt.wantAllow)
// 			}
// 		})
// 	}
// }

// PASO 3: crear un libro y comprobar lo que quedó en el fake.
// func TestCreateBook(t *testing.T) {
// 	store := fakeStore{}
// 	h := NewHandler(store)
//
// 	res := serve(t, h, http.MethodPost, "/books", `{"isbn":"978-0","title":"Ficciones","copies":1}`)
//
// 	if res.StatusCode != http.StatusCreated {
// 		t.Fatalf("status = %d, want %d", res.StatusCode, http.StatusCreated)
// 	}
// 	if got := res.Header.Get("Location"); got != "/books/978-0" {
// 		t.Errorf("Location = %q, want /books/978-0", got)
// 	}
// 	want := fakeStore{"978-0": {ISBN: "978-0", Title: "Ficciones", Copies: 1}}
// 	if diff := cmp.Diff(want, store); diff != "" {
// 		t.Errorf("store mismatch (-want +got):\n%s", diff)
// 	}
// }
//
// func TestCreateBookRejects(t *testing.T) {
// 	tests := []struct {
// 		name, body string
// 		wantStatus int
// 	}{
// 		{"duplicate", `{"isbn":"978-84-376-0494-7","title":"Rayuela","copies":1}`, http.StatusConflict},
// 		{"malformed json", `{"isbn":`, http.StatusBadRequest},
// 		{"missing title", `{"isbn":"978-0","copies":1}`, http.StatusBadRequest},
// 		{"negative copies", `{"isbn":"978-0","title":"Ficciones","copies":-1}`, http.StatusBadRequest},
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			store := fakeStore{rayuela.ISBN: rayuela}
// 			res := serve(t, NewHandler(store), http.MethodPost, "/books", tt.body)
// 			if res.StatusCode != tt.wantStatus {
// 				t.Errorf("status = %d, want %d", res.StatusCode, tt.wantStatus)
// 			}
// 			if diff := cmp.Diff(fakeStore{rayuela.ISBN: rayuela}, store); diff != "" {
// 				t.Errorf("store changed (-want +got):\n%s", diff)
// 			}
// 		})
// 	}
// }

// PASO 4: el contrato JSON, campo por campo.
// func TestBookJSON(t *testing.T) {
// 	h := NewHandler(fakeStore{rayuela.ISBN: rayuela})
//
// 	res := serve(t, h, http.MethodGet, "/books/"+rayuela.ISBN, "")
//
// 	var got map[string]any
// 	if err := json.NewDecoder(res.Body).Decode(&got); err != nil {
// 		t.Fatal(err)
// 	}
// 	want := map[string]any{"isbn": "978-84-376-0494-7", "title": "Rayuela", "copies": 2.0}
// 	if diff := cmp.Diff(want, got); diff != "" {
// 		t.Errorf("JSON mismatch (-want +got):\n%s", diff)
// 	}
// }
