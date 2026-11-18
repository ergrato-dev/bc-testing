// Package holds lleva las visitas y las reservas del catálogo de una biblioteca.
package holds

// Views cuenta cuántas veces se consulta cada libro. La API la llama desde el
// handler de cada petición, así que recibe llamadas desde varias goroutines a la vez.
type Views struct {
	byISBN map[string]int
}

func NewViews() *Views {
	return &Views{byISBN: map[string]int{}}
}

func (v *Views) Record(isbn string) {
	v.byISBN[isbn]++
}

func (v *Views) Count(isbn string) int {
	return v.byISBN[isbn]
}
