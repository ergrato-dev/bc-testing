// Package fees calcula las multas de una biblioteca según el tipo de socio.
package fees

import "errors"

// Membership es el tipo de socio.
type Membership int

const (
	Basic Membership = iota
	Premium
)

// ErrNegativeDays indica que los días de retraso no pueden ser negativos.
var ErrNegativeDays = errors.New("days late cannot be negative")

// LateFee devuelve la multa por devolver un libro daysLate días tarde.
// Basic: 50 por día, hasta 1000. Premium: 2 días de gracia, después 25 por día, hasta 500.
func LateFee(daysLate int, m Membership) (int, error) {
	if daysLate < 0 {
		return 0, ErrNegativeDays
	}
	if m == Premium {
		return min(max(daysLate-2, 0)*25, 500), nil
	}
	return min(daysLate*50, 1000), nil
}
