package main

import (
	"context"
	"io"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/jackc/pgx/v5/pgxpool"
)

// newServer arranca la API en un servidor de prueba sobre una transacción de
// PostgreSQL que se deshace al terminar el test: lo que un test borra o cambia no
// llega a la base de datos ni a los demás tests.
func newServer(t *testing.T) *httptest.Server {
	t.Helper()
	ctx := context.Background()
	pool, err := pgxpool.New(ctx, env("DATABASE_URL", "postgres://library:library@localhost:55444/library"))
	if err != nil {
		t.Fatal(err)
	}
	t.Cleanup(pool.Close)
	tx, err := pool.Begin(ctx)
	if err != nil {
		t.Fatalf("¿está arrancado PostgreSQL (docker compose up -d --wait)? %v", err)
	}
	t.Cleanup(func() { tx.Rollback(ctx) })

	srv := httptest.NewServer(newHandler(tx))
	t.Cleanup(srv.Close)
	return srv
}

// send hace la petición con el token (vacío: sin cabecera Authorization) y
// devuelve la respuesta con el cuerpo ya leído.
func send(t *testing.T, srv *httptest.Server, method, path, token string) (*http.Response, string) {
	t.Helper()
	req, err := http.NewRequest(method, srv.URL+path, nil)
	if err != nil {
		t.Fatal(err)
	}
	if token != "" {
		req.Header.Set("Authorization", "Bearer "+token)
	}
	res, err := srv.Client().Do(req)
	if err != nil {
		t.Fatal(err)
	}
	defer res.Body.Close()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		t.Fatal(err)
	}
	return res, string(body)
}
