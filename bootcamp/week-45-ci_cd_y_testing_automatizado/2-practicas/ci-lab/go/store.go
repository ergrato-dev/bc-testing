// Package cilab guarda los libros de la biblioteca en PostgreSQL.
package cilab

import (
	"context"
	"errors"

	"github.com/jackc/pgx/v5/pgxpool"
)

// ErrEmptyTitle se devuelve al guardar un libro sin título.
var ErrEmptyTitle = errors.New("empty title")

// Store guarda los libros en la tabla books.
type Store struct {
	db *pgxpool.Pool
}

// NewStore crea la tabla books si no existe.
func NewStore(ctx context.Context, db *pgxpool.Pool) (*Store, error) {
	_, err := db.Exec(ctx, `CREATE TABLE IF NOT EXISTS books (
		id SERIAL PRIMARY KEY,
		title TEXT NOT NULL,
		available BOOLEAN NOT NULL DEFAULT TRUE)`)
	if err != nil {
		return nil, err
	}
	return &Store{db: db}, nil
}

// Add guarda un libro disponible y devuelve su id.
func (s *Store) Add(ctx context.Context, title string) (int, error) {
	if title == "" {
		return 0, ErrEmptyTitle
	}
	var id int
	err := s.db.QueryRow(ctx, "INSERT INTO books (title) VALUES ($1) RETURNING id", title).Scan(&id)
	return id, err
}

// Lend marca el libro como prestado. Devuelve false si ya estaba prestado o no existe.
func (s *Store) Lend(ctx context.Context, id int) (bool, error) {
	tag, err := s.db.Exec(ctx, "UPDATE books SET available = FALSE WHERE id = $1 AND available", id)
	if err != nil {
		return false, err
	}
	return tag.RowsAffected() == 1, nil
}

// CountAvailable cuenta los libros disponibles.
func (s *Store) CountAvailable(ctx context.Context) (int, error) {
	var n int
	err := s.db.QueryRow(ctx, "SELECT count(*) FROM books WHERE available").Scan(&n)
	return n, err
}
