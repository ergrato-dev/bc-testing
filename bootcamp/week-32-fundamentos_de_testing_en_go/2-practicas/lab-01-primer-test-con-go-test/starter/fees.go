// Package fees calcula las multas de una biblioteca.
package fees

import "errors"

const (
	FeePerDay = 50
	MaxFee    = 1000
)

// ErrNegativeDays indica que los días de retraso no pueden ser negativos.
var ErrNegativeDays = errors.New("days late cannot be negative")

// LateFee devuelve la multa por devolver un libro daysLate días tarde:
// 50 por día, hasta 1000.
func LateFee(daysLate int) (int, error) {
	if daysLate < 0 {
		return 0, ErrNegativeDays
	}
	return min(daysLate*FeePerDay, MaxFee), nil
}
