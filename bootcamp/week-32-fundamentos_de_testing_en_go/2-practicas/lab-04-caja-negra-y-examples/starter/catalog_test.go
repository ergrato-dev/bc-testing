package catalog_test

// PASO 2: tests de caja negra, desde fuera del paquete.
// import (
// 	"errors"
// 	"slices"
// 	"testing"
//
// 	"bootcamp/catalog"
// )
//
// func TestAddRejectsSameTitleWithOtherSpacing(t *testing.T) {
// 	var c catalog.Catalog
// 	if err := c.Add(catalog.Book{Title: "Cien años de soledad", Author: "García Márquez"}); err != nil {
// 		t.Fatalf("first Add: %v", err)
// 	}
// 	err := c.Add(catalog.Book{Title: "cien  AÑOS de soledad", Author: "García Márquez"})
// 	if !errors.Is(err, catalog.ErrDuplicate) {
// 		t.Errorf("second Add error = %v, want %v", err, catalog.ErrDuplicate)
// 	}
// }
//
// func TestByAuthorIgnoresCase(t *testing.T) {
// 	var c catalog.Catalog
// 	c.Add(catalog.Book{Title: "Emma", Author: "Jane Austen"})
// 	c.Add(catalog.Book{Title: "Dune", Author: "Frank Herbert"})
// 	c.Add(catalog.Book{Title: "Persuasión", Author: "jane austen"})
// 	got := c.ByAuthor("JANE AUSTEN")
// 	want := []string{"Emma", "Persuasión"}
// 	if !slices.Equal(got, want) {
// 		t.Errorf("ByAuthor = %q, want %q", got, want)
// 	}
// }
