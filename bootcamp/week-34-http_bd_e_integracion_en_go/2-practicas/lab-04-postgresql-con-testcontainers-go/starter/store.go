// Package books guarda el catálogo de una biblioteca en PostgreSQL.
package books

import (
	"context"
	"embed"
	"errors"
	"fmt"
	"io/fs"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
)

// Book es un libro del catálogo y las copias que quedan en la estantería.
type Book struct {
	ISBN   string
	Title  string
	Copies int
}

var (
	ErrNotFound  = errors.New("book not found")
	ErrDuplicate = errors.New("duplicate isbn")
	ErrNoCopies  = errors.New("no copies left")
)

// DBTX es lo que Store necesita de la conexión. La cumplen *pgxpool.Pool y
// pgx.Tx, así que un test puede pasarle una transacción.
type DBTX interface {
	Exec(ctx context.Context, sql string, args ...any) (pgconn.CommandTag, error)
	Query(ctx context.Context, sql string, args ...any) (pgx.Rows, error)
	QueryRow(ctx context.Context, sql string, args ...any) pgx.Row
}

//go:embed migrations/*.sql
var migrations embed.FS

// Migrate ejecuta los archivos de migrations/ en orden alfabético.
func Migrate(ctx context.Context, db DBTX) error {
	files, err := fs.Glob(migrations, "migrations/*.sql")
	if err != nil {
		return err
	}
	for _, f := range files {
		sql, err := migrations.ReadFile(f)
		if err != nil {
			return err
		}
		if _, err := db.Exec(ctx, string(sql)); err != nil {
			return fmt.Errorf("%s: %w", f, err)
		}
	}
	return nil
}

// Store lee y escribe libros.
type Store struct {
	DB DBTX
}

// Add inserta un libro. Devuelve ErrDuplicate si el ISBN ya existe.
func (s *Store) Add(ctx context.Context, b Book) error {
	_, err := s.DB.Exec(ctx, `INSERT INTO books (isbn, title, copies) VALUES ($1, $2, $3)`, b.ISBN, b.Title, b.Copies)
	if pgErr, ok := errors.AsType[*pgconn.PgError](err); ok && pgErr.Code == "23505" {
		return fmt.Errorf("%w: %s", ErrDuplicate, b.ISBN)
	}
	return err
}

// Get devuelve un libro o ErrNotFound.
func (s *Store) Get(ctx context.Context, isbn string) (Book, error) {
	var b Book
	err := s.DB.QueryRow(ctx, `SELECT isbn, title, copies FROM books WHERE isbn = $1`, isbn).Scan(&b.ISBN, &b.Title, &b.Copies)
	if errors.Is(err, pgx.ErrNoRows) {
		return Book{}, fmt.Errorf("%w: %s", ErrNotFound, isbn)
	}
	return b, err
}

// List devuelve todos los libros ordenados por ISBN.
func (s *Store) List(ctx context.Context) ([]Book, error) {
	rows, err := s.DB.Query(ctx, `SELECT isbn, title, copies FROM books ORDER BY isbn`)
	if err != nil {
		return nil, err
	}
	return pgx.CollectRows(rows, pgx.RowToStructByPos[Book])
}

// Lend resta una copia. Si no quedan, el CHECK de la tabla rechaza el UPDATE
// y devuelve ErrNoCopies.
func (s *Store) Lend(ctx context.Context, isbn string) error {
	tag, err := s.DB.Exec(ctx, `UPDATE books SET copies = copies - 1 WHERE isbn = $1`, isbn)
	if pgErr, ok := errors.AsType[*pgconn.PgError](err); ok && pgErr.Code == "23514" {
		return fmt.Errorf("%w: %s", ErrNoCopies, isbn)
	}
	if err != nil {
		return err
	}
	if tag.RowsAffected() == 0 {
		return fmt.Errorf("%w: %s", ErrNotFound, isbn)
	}
	return nil
}
