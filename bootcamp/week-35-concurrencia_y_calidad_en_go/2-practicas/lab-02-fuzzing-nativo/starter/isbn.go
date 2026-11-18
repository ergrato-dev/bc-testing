// Package isbn lee los ISBN-13 que teclean los usuarios del catálogo.
package isbn

import (
	"errors"
	"strings"
)

var (
	ErrLength   = errors.New("isbn: must have 13 digits")
	ErrChecksum = errors.New("isbn: wrong check digit")
)

// ISBN son los 13 dígitos de un ISBN-13, sin separadores.
type ISBN string

var separators = strings.NewReplacer("-", "", " ", "")

// Parse lee un ISBN-13 tal como lo escribe un usuario: con guiones o espacios
// entre los grupos, y con el prefijo "ISBN " opcional. Devuelve los 13 dígitos
// sin separadores, o un error si la longitud o el dígito de control no cuadran.
func Parse(s string) (ISBN, error) {
	if strings.HasPrefix(s, "ISBN") {
		s = s[5:] // salta "ISBN "
	}
	s = separators.Replace(s)
	if len(s) != 13 {
		return "", ErrLength
	}
	// Dígito de control: los 13 dígitos, con pesos 1 y 3 alternos, suman un múltiplo de 10.
	sum := 0
	for i := range len(s) {
		d := int(s[i] - '0')
		if i%2 == 1 {
			d *= 3
		}
		sum += d
	}
	if sum%10 != 0 {
		return "", ErrChecksum
	}
	return ISBN(s), nil
}
