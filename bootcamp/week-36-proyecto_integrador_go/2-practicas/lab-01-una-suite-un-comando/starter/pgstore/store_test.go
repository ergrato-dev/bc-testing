//go:build integration

package pgstore

import (
	"context"
	"errors"
	"log"
	"os"
	"testing"

	"bootcamp/lab01/catalog"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
)

// pool es la conexión al PostgreSQL que arranca TestMain: un contenedor para
// todo el paquete.
var pool *pgxpool.Pool

func TestMain(m *testing.M) {
	os.Exit(run(m))
}

// run arranca el contenedor, aplica las migraciones y ejecuta los tests. Está
// separado de TestMain para que los defer se ejecuten antes de os.Exit.
func run(m *testing.M) int {
	ctx := context.Background()
	ctr, err := postgres.Run(ctx, "postgres:18.7-alpine",
		postgres.WithDatabase("library"),
		postgres.WithUsername("library"),
		postgres.WithPassword("library"),
		postgres.BasicWaitStrategies(),
	)
	defer testcontainers.TerminateContainer(ctr)
	if err != nil {
		log.Print(err)
		return 1
	}
	pool, err = pgxpool.New(ctx, ctr.MustConnectionString(ctx, "sslmode=disable"))
	if err != nil {
		log.Print(err)
		return 1
	}
	defer pool.Close()
	if err := Migrate(ctx, pool); err != nil {
		log.Print(err)
		return 1
	}
	return m.Run()
}

// newStore abre una transacción que se deshace al terminar el test: cada test
// empieza con la tabla vacía.
func newStore(t *testing.T) *Store {
	t.Helper()
	tx, err := pool.Begin(t.Context())
	if err != nil {
		t.Fatal(err)
	}
	t.Cleanup(func() { tx.Rollback(context.Background()) })
	return &Store{DB: tx}
}

func TestLendUntilNoCopies(t *testing.T) {
	s := newStore(t)
	if err := s.Add(t.Context(), Book{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 1}); err != nil {
		t.Fatal(err)
	}
	if err := s.Lend(t.Context(), "978-84-376-0494-7"); err != nil {
		t.Fatalf("first Lend() = %v", err)
	}
	if err := s.Lend(t.Context(), "978-84-376-0494-7"); !errors.Is(err, catalog.ErrNoCopies) {
		t.Errorf("second Lend() = %v; want ErrNoCopies", err)
	}
}

func TestLendUnknownBook(t *testing.T) {
	s := newStore(t)
	if err := s.Lend(t.Context(), "000"); !errors.Is(err, catalog.ErrNotFound) {
		t.Errorf("Lend() = %v; want ErrNotFound", err)
	}
}

func TestGet(t *testing.T) {
	s := newStore(t)
	want := Book{ISBN: "978-84-204-8250-6", Title: "Ficciones", Copies: 3}
	if err := s.Add(t.Context(), want); err != nil {
		t.Fatal(err)
	}
	got, err := s.Get(t.Context(), want.ISBN)
	if err != nil || got != want {
		t.Errorf("Get() = %+v, %v; want %+v", got, err, want)
	}
	if _, err := s.Get(t.Context(), "000"); !errors.Is(err, catalog.ErrNotFound) {
		t.Errorf("Get(unknown) = %v; want ErrNotFound", err)
	}
}
