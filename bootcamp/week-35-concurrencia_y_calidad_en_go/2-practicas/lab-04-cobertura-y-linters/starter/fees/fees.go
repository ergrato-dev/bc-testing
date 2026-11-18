// Package fees calcula los recargos por devolver tarde un libro de la biblioteca.
package fees

import (
	"errors"
	"fmt"
	"strings"
)

const (
	PerDay = 50
	Max    = 1500
)

var ErrNegativeDays = errors.New("Days cannot be negative")

// LateFee devuelve el recargo por days días de retraso: 50 por día, hasta 1500.
// Los socios con categoría "staff" no pagan recargo.
func LateFee(days int, category string) (int, error) {
	if days < 0 {
		return 0, ErrNegativeDays
	}
	if category == "staff" {
		return 0, nil
	}
	return min(days*PerDay, Max), nil
}

// Receipt devuelve el texto del recibo del recargo.
func Receipt(member string, days int, category string) (string, error) {
	fee, err := LateFee(days, category)
	if err != nil {
		return "", err
	}
	return fmt.Sprintf("%s: %d días, %d", strings.Title(member), days, fee), nil
}

func roundUp(n, step int) int {
	return (n + step - 1) / step * step
}
