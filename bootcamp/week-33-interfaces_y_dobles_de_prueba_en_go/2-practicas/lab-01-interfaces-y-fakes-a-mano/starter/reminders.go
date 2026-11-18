// Package reminders envía recordatorios de los préstamos vencidos.
package reminders

import (
	"fmt"
	"time"
)

// Loan es un préstamo de un libro a un socio.
type Loan struct {
	Title  string
	Member string
	Due    time.Time
}

// LoanStore es lo que Service necesita de un almacén de préstamos: nada más.
type LoanStore interface {
	Active() ([]Loan, error)
}

// Notifier envía un mensaje a un socio.
type Notifier interface {
	Notify(member, message string) error
}

// Service envía un recordatorio por cada préstamo vencido. Sus dependencias
// son interfaces y una función que da la hora, para poder sustituirlas en
// los tests.
type Service struct {
	Loans    LoanStore
	Notifier Notifier
	Now      func() time.Time
}

// SendReminders avisa a cada socio con un préstamo vencido y devuelve cuántos
// avisos envió. Se para en el primer aviso que falla.
func (s *Service) SendReminders() (int, error) {
	loans, err := s.Loans.Active()
	if err != nil {
		return 0, fmt.Errorf("listing loans: %w", err)
	}
	now := s.Now()
	sent := 0
	for _, l := range loans {
		days := int(now.Sub(l.Due).Hours() / 24)
		if days <= 0 {
			continue
		}
		msg := fmt.Sprintf("%s: %d días de retraso", l.Title, days)
		if err := s.Notifier.Notify(l.Member, msg); err != nil {
			return sent, fmt.Errorf("notifying %s: %w", l.Member, err)
		}
		sent++
	}
	return sent, nil
}
