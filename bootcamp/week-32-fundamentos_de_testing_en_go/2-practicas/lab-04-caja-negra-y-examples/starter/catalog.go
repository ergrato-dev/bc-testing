// Package catalog guarda los libros de una biblioteca.
package catalog

import (
	"errors"
	"slices"
	"strings"
)

// ErrDuplicate indica que el catálogo ya tiene un libro con ese título.
var ErrDuplicate = errors.New("duplicate title")

// Book es un libro del catálogo.
type Book struct {
	Title  string
	Author string
}

// Catalog es un catálogo en memoria. El valor cero está listo para usarse.
type Catalog struct {
	books []Book
}

// Add añade b al catálogo. Dos títulos que solo se diferencian en
// mayúsculas o espacios son el mismo libro.
func (c *Catalog) Add(b Book) error {
	for _, existing := range c.books {
		if normalize(existing.Title) == normalize(b.Title) {
			return ErrDuplicate
		}
	}
	c.books = append(c.books, b)
	return nil
}

// ByAuthor devuelve los títulos de author en orden alfabético.
func (c *Catalog) ByAuthor(author string) []string {
	var titles []string
	for _, b := range c.books {
		if normalize(b.Author) == normalize(author) {
			titles = append(titles, b.Title)
		}
	}
	slices.Sort(titles)
	return titles
}

func normalize(s string) string {
	return strings.ToLower(strings.Join(strings.Fields(s), " "))
}
