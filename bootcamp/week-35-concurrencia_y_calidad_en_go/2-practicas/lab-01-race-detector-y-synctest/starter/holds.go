package holds

import (
	"errors"
	"sync"
	"time"
)

// ErrHeld indica que el libro ya está reservado por otro socio.
var ErrHeld = errors.New("book already held")

// Holds guarda las reservas de libros. Cada reserva caduca sola cuando pasa
// TTL desde que se hizo, y el libro queda libre para otro socio.
type Holds struct {
	TTL time.Duration

	mu     sync.Mutex
	byISBN map[string]string // ISBN -> socio
}

func NewHolds(ttl time.Duration) *Holds {
	return &Holds{TTL: ttl, byISBN: map[string]string{}}
}

// Place reserva el libro para el socio, o devuelve ErrHeld si ya lo tiene otro.
func (h *Holds) Place(isbn, member string) error {
	h.mu.Lock()
	defer h.mu.Unlock()
	if _, ok := h.byISBN[isbn]; ok {
		return ErrHeld
	}
	h.byISBN[isbn] = member
	time.AfterFunc(h.TTL, func() {
		h.mu.Lock()
		defer h.mu.Unlock()
		delete(h.byISBN, isbn)
	})
	return nil
}

// Holder devuelve el socio que tiene reservado el libro.
func (h *Holds) Holder(isbn string) (string, bool) {
	h.mu.Lock()
	defer h.mu.Unlock()
	m, ok := h.byISBN[isbn]
	return m, ok
}
