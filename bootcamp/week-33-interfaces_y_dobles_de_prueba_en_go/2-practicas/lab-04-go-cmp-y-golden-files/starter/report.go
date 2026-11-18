// Package report construye el resumen mensual de los préstamos vencidos.
package report

import (
	"fmt"
	"math/rand/v2"
	"slices"
	"strings"
	"time"
)

// FeePerDay es el recargo por cada día de retraso.
const FeePerDay = 50

// Loan es un préstamo de un libro a un socio.
type Loan struct {
	Title  string
	Member string
	Due    time.Time
}

// Line es una fila del resumen.
type Line struct {
	Title    string
	Member   string
	DaysLate int
	Fee      int
	due      time.Time // solo para ordenar las filas; no se muestra
}

// Summary es el resumen de un día.
type Summary struct {
	Date     string
	Lines    []Line
	Total    int
	Featured string // libro destacado del mes, elegido al azar del catálogo
}

// Build calcula el resumen a la hora now. Las filas van del préstamo más
// antiguo al más reciente. r elige el libro destacado.
func Build(loans []Loan, now time.Time, catalog []string, r *rand.Rand) Summary {
	s := Summary{Date: now.Format(time.DateOnly)}
	for _, l := range loans {
		days := int(now.Sub(l.Due).Hours() / 24)
		if days <= 0 {
			continue
		}
		line := Line{Title: l.Title, Member: l.Member, DaysLate: days, Fee: days * FeePerDay, due: l.Due}
		s.Lines = append(s.Lines, line)
		s.Total += line.Fee
	}
	slices.SortFunc(s.Lines, func(a, b Line) int { return a.due.Compare(b.due) })
	if len(catalog) > 0 {
		s.Featured = catalog[r.IntN(len(catalog))]
	}
	return s
}

// Render devuelve el resumen como texto, listo para enviar por correo.
func (s Summary) Render() string {
	var b strings.Builder
	fmt.Fprintf(&b, "Préstamos vencidos a %s\n\n", s.Date)
	for _, l := range s.Lines {
		fmt.Fprintf(&b, "%-10s %-6s %3d días %6d\n", l.Title, l.Member, l.DaysLate, l.Fee)
	}
	fmt.Fprintf(&b, "\nTotal: %d\n", s.Total)
	fmt.Fprintf(&b, "Libro destacado: %s\n", s.Featured)
	return b.String()
}
