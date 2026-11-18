// Package report escribe el informe de préstamos con retraso.
package report

import (
	"context"
	"fmt"
	"os"
	"path/filepath"
	"strings"
)

// Loan es un préstamo con los días que lleva de retraso.
type Loan struct {
	Title    string
	Member   string
	DaysLate int
}

// WriteOverdue escribe dir/overdue.csv con los préstamos que tienen retraso
// y devuelve la ruta del archivo. No escribe nada si ctx ya está cancelado.
func WriteOverdue(ctx context.Context, dir string, loans []Loan) (string, error) {
	if err := ctx.Err(); err != nil {
		return "", err
	}
	var b strings.Builder
	b.WriteString("title,member,days_late\n")
	for _, l := range loans {
		if l.DaysLate > 0 {
			fmt.Fprintf(&b, "%s,%s,%d\n", l.Title, l.Member, l.DaysLate)
		}
	}
	path := filepath.Join(dir, "overdue.csv")
	return path, os.WriteFile(path, []byte(b.String()), 0o644)
}
