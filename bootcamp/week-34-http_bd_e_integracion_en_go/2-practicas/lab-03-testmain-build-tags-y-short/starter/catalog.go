// Package catalog sirve por HTTP el catálogo de una biblioteca.
package catalog

import (
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
)

// Book es un libro del catálogo y las copias que quedan en la estantería.
type Book struct {
	ISBN   string `json:"isbn"`
	Title  string `json:"title"`
	Copies int    `json:"copies"`
}

var (
	ErrNotFound  = errors.New("book not found")
	ErrDuplicate = errors.New("duplicate isbn")
)

// Store es lo que el handler necesita del almacén de libros.
type Store interface {
	Get(isbn string) (Book, error)
	Add(b Book) error
}

// NewHandler devuelve las rutas del catálogo:
//
//	GET  /books/{isbn}  el libro, o 404
//	POST /books         crea un libro: 201, 400 si el JSON no vale, 409 si ya existe
func NewHandler(s Store) http.Handler {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /books/{isbn}", func(w http.ResponseWriter, r *http.Request) {
		b, err := s.Get(r.PathValue("isbn"))
		switch {
		case errors.Is(err, ErrNotFound):
			writeError(w, http.StatusNotFound, err)
		case err != nil:
			writeError(w, http.StatusInternalServerError, err)
		default:
			writeJSON(w, http.StatusOK, b)
		}
	})
	mux.HandleFunc("POST /books", func(w http.ResponseWriter, r *http.Request) {
		var b Book
		if err := json.NewDecoder(r.Body).Decode(&b); err != nil {
			writeError(w, http.StatusBadRequest, err)
			return
		}
		if b.ISBN == "" || b.Title == "" || b.Copies < 0 {
			writeError(w, http.StatusBadRequest, errors.New("isbn and title are required and copies cannot be negative"))
			return
		}
		err := s.Add(b)
		switch {
		case errors.Is(err, ErrDuplicate):
			writeError(w, http.StatusConflict, err)
		case err != nil:
			writeError(w, http.StatusInternalServerError, err)
		default:
			w.Header().Set("Location", fmt.Sprintf("/books/%s", b.ISBN))
			writeJSON(w, http.StatusCreated, b)
		}
	})
	return mux
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(v)
}

func writeError(w http.ResponseWriter, status int, err error) {
	writeJSON(w, status, map[string]string{"error": err.Error()})
}
