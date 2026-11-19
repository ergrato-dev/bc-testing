package cilab

import (
	"errors"
	"os"
	"testing"

	"github.com/jackc/pgx/v5/pgxpool"
)

// newStore conecta con la base de datos de DATABASE_URL y vacía la tabla books.
func newStore(t *testing.T) *Store {
	t.Helper()
	url := os.Getenv("DATABASE_URL")
	if url == "" {
		t.Fatal("DATABASE_URL no está definida")
	}
	db, err := pgxpool.New(t.Context(), url)
	if err != nil {
		t.Fatal(err)
	}
	t.Cleanup(db.Close)
	s, err := NewStore(t.Context(), db)
	if err != nil {
		t.Fatal(err)
	}
	if _, err := db.Exec(t.Context(), "TRUNCATE books RESTART IDENTITY"); err != nil {
		t.Fatal(err)
	}
	return s
}

func TestLendMarksBookAsUnavailable(t *testing.T) {
	s := newStore(t)
	id, err := s.Add(t.Context(), "Learning Go")
	if err != nil {
		t.Fatal(err)
	}
	if _, err := s.Add(t.Context(), "The Rust Programming Language"); err != nil {
		t.Fatal(err)
	}

	ok, err := s.Lend(t.Context(), id)
	if err != nil || !ok {
		t.Fatalf("Lend = %v, %v; quiero true, nil", ok, err)
	}
	n, err := s.CountAvailable(t.Context())
	if err != nil || n != 1 {
		t.Fatalf("CountAvailable = %d, %v; quiero 1, nil", n, err)
	}
}

func TestLendTwiceFails(t *testing.T) {
	s := newStore(t)
	id, _ := s.Add(t.Context(), "Learning Go")
	s.Lend(t.Context(), id)

	ok, err := s.Lend(t.Context(), id)
	if err != nil || ok {
		t.Fatalf("segundo Lend = %v, %v; quiero false, nil", ok, err)
	}
}

func TestAddRejectsEmptyTitle(t *testing.T) {
	s := newStore(t)
	if _, err := s.Add(t.Context(), ""); !errors.Is(err, ErrEmptyTitle) {
		t.Fatalf("err = %v; quiero ErrEmptyTitle", err)
	}
}
