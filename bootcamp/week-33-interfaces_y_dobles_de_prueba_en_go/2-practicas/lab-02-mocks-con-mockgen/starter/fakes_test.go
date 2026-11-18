package reminders

import (
	"slices"
	"time"
)

// Los dobles a mano del lab 01 que este lab sigue usando: el reloj y el almacén.

var now = time.Date(2026, time.March, 10, 9, 0, 0, 0, time.UTC)

func fixedClock() time.Time { return now }

type memStore struct {
	loans []Loan
}

func (m *memStore) Add(l Loan) { m.loans = append(m.loans, l) }

func (m *memStore) Active() ([]Loan, error) { return slices.Clone(m.loans), nil }

// newStore devuelve un almacén con dos préstamos vencidos y uno al día.
func newStore() *memStore {
	store := &memStore{}
	store.Add(Loan{Title: "Dune", Member: "ana", Due: now.AddDate(0, 0, -3)})
	store.Add(Loan{Title: "Emma", Member: "luis", Due: now.AddDate(0, 0, 5)})
	store.Add(Loan{Title: "Ulises", Member: "eva", Due: now.AddDate(0, 0, -12)})
	return store
}
