package catalog

import (
	"fmt"
	"sync"
)

// MemStore guarda los libros en memoria. El mutex permite que varias
// peticiones lo usen a la vez, como hace un servidor HTTP.
type MemStore struct {
	mu    sync.Mutex
	books map[string]Book
}

func NewMemStore() *MemStore {
	return &MemStore{books: map[string]Book{}}
}

func (s *MemStore) Get(isbn string) (Book, error) {
	s.mu.Lock()
	defer s.mu.Unlock()
	b, ok := s.books[isbn]
	if !ok {
		return Book{}, fmt.Errorf("%w: %s", ErrNotFound, isbn)
	}
	return b, nil
}

func (s *MemStore) Add(b Book) error {
	s.mu.Lock()
	defer s.mu.Unlock()
	if _, ok := s.books[b.ISBN]; ok {
		return fmt.Errorf("%w: %s", ErrDuplicate, b.ISBN)
	}
	s.books[b.ISBN] = b
	return nil
}
