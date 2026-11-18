package catalog

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

// Test unitario: el handler en memoria, sin red. Es rápido y siempre se ejecuta.
func TestGetUnknownBook(t *testing.T) {
	h := NewHandler(NewMemStore())
	rec := httptest.NewRecorder()

	h.ServeHTTP(rec, httptest.NewRequest(http.MethodGet, "/books/000", nil))

	if rec.Code != http.StatusNotFound {
		t.Errorf("status = %d, want %d", rec.Code, http.StatusNotFound)
	}
}
