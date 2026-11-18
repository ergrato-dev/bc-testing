// Package catalog tiene las reglas de préstamo de una biblioteca y su API HTTP.
package catalog

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"strconv"
)

var (
	ErrNotFound     = errors.New("book not found")
	ErrNoCopies     = errors.New("no copies left")
	ErrNegativeDays = errors.New("days late cannot be negative")
)

// LateFee devuelve la multa por devolver un libro con retraso: 50 por día,
// hasta un máximo de 1000.
func LateFee(daysLate int) (int, error) {
	if daysLate < 0 {
		return 0, ErrNegativeDays
	}
	return min(daysLate*50, 1000), nil
}

// Lender es lo que la API necesita del almacén: prestar una copia.
type Lender interface {
	Lend(ctx context.Context, isbn string) error
}

// NewHandler devuelve las rutas de la API:
//
//	GET  /fees?days=N        la multa por N días de retraso, o 400
//	POST /books/{isbn}/lend  presta una copia: 204, 404 si no existe, 409 si no quedan
func NewHandler(l Lender) http.Handler {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /fees", func(w http.ResponseWriter, r *http.Request) {
		days, err := strconv.Atoi(r.URL.Query().Get("days"))
		if err != nil {
			writeError(w, http.StatusBadRequest, err)
			return
		}
		fee, err := LateFee(days)
		if err != nil {
			writeError(w, http.StatusBadRequest, err)
			return
		}
		writeJSON(w, http.StatusOK, map[string]int{"fee": fee})
	})
	mux.HandleFunc("POST /books/{isbn}/lend", func(w http.ResponseWriter, r *http.Request) {
		err := l.Lend(r.Context(), r.PathValue("isbn"))
		switch {
		case errors.Is(err, ErrNotFound):
			writeError(w, http.StatusNotFound, err)
		case errors.Is(err, ErrNoCopies):
			writeError(w, http.StatusConflict, err)
		case err != nil:
			writeError(w, http.StatusInternalServerError, err)
		default:
			w.WriteHeader(http.StatusNoContent)
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
