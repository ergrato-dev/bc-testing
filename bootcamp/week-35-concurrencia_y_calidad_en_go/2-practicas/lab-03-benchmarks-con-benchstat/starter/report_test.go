package report

import (
	"fmt"
	"strings"
	"testing"
)

func TestInventory(t *testing.T) {
	got := Inventory([]Book{
		{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2},
		{ISBN: "978-0-14-118776-1", Title: "Ficciones", Copies: 12},
	})
	want := "978-84-376-0494-7  Rayuela                          2\n" +
		"978-0-14-118776-1  Ficciones                       12\n"
	if got != want {
		t.Errorf("Inventory() =\n%s\nwant\n%s", got, want)
	}
}

// catalog crea n libros de prueba.
func catalog(n int) []Book {
	books := make([]Book, n)
	for i := range books {
		books[i] = Book{ISBN: fmt.Sprintf("978-0-00-%06d-0", i), Title: strings.Repeat("x", 20), Copies: i % 10}
	}
	return books
}

// PASO 1: el inventario de 1000 libros.
// func BenchmarkInventory(b *testing.B) {
// 	books := catalog(1000)
// 	for b.Loop() {
// 		Inventory(books)
// 	}
// }

// PASO 2: el mismo benchmark con catálogos de 10, 100 y 1000 libros.
// func BenchmarkInventorySizes(b *testing.B) {
// 	for _, n := range []int{10, 100, 1000} {
// 		b.Run(fmt.Sprintf("books=%d", n), func(b *testing.B) {
// 			books := catalog(n)
// 			for b.Loop() {
// 				Inventory(books)
// 			}
// 		})
// 	}
// }
