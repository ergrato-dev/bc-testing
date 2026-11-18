// Package report genera el inventario del catálogo de una biblioteca.
package report

import "fmt"

// Book es un libro del catálogo.
type Book struct {
	ISBN   string
	Title  string
	Copies int
}

// Inventory devuelve una línea por libro, con su ISBN, su título y sus copias.
func Inventory(books []Book) string {
	s := ""
	for _, b := range books {
		s += fmt.Sprintf("%s  %-30s %3d\n", b.ISBN, b.Title, b.Copies)
	}
	return s
}
