package catalog

import (
	"context"
	"errors"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

func TestLateFee(t *testing.T) {
	tests := []struct {
		name    string
		days    int
		want    int
		wantErr error
	}{
		{"on time", 0, 0, nil},
		{"three days", 3, 150, nil},
		{"capped", 30, 1000, nil},
		{"negative", -1, 0, ErrNegativeDays},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			t.Parallel()
			got, err := LateFee(tt.days)
			if !errors.Is(err, tt.wantErr) || got != tt.want {
				t.Errorf("LateFee(%d) = %d, %v; want %d, %v", tt.days, got, err, tt.want, tt.wantErr)
			}
		})
	}
}

// stubLender devuelve siempre el mismo error.
type stubLender struct{ err error }

func (s stubLender) Lend(context.Context, string) error { return s.err }

func TestFeesEndpoint(t *testing.T) {
	tests := []struct {
		query    string
		wantCode int
		wantBody string
	}{
		{"days=3", http.StatusOK, `{"fee":150}`},
		{"days=abc", http.StatusBadRequest, `"error"`},
		{"days=-2", http.StatusBadRequest, `cannot be negative`},
	}
	h := NewHandler(stubLender{})
	for _, tt := range tests {
		t.Run(tt.query, func(t *testing.T) {
			rec := httptest.NewRecorder()
			h.ServeHTTP(rec, httptest.NewRequest(http.MethodGet, "/fees?"+tt.query, nil))
			if rec.Code != tt.wantCode || !strings.Contains(rec.Body.String(), tt.wantBody) {
				t.Errorf("GET /fees?%s = %d %s; want %d containing %s", tt.query, rec.Code, rec.Body, tt.wantCode, tt.wantBody)
			}
		})
	}
}

func TestLendEndpoint(t *testing.T) {
	tests := []struct {
		name     string
		err      error
		wantCode int
	}{
		{"lent", nil, http.StatusNoContent},
		{"not found", ErrNotFound, http.StatusNotFound},
		{"no copies", ErrNoCopies, http.StatusConflict},
		{"store down", errors.New("connection refused"), http.StatusInternalServerError},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			rec := httptest.NewRecorder()
			NewHandler(stubLender{tt.err}).ServeHTTP(rec, httptest.NewRequest(http.MethodPost, "/books/978-84-376-0494-7/lend", nil))
			if rec.Code != tt.wantCode {
				t.Errorf("POST lend = %d; want %d", rec.Code, tt.wantCode)
			}
		})
	}
}
