package holds

import (
	// "sync" // PASO 1
	"testing"
)

// TestViewsCount prueba el contador desde una sola goroutine. Ya pasa.
func TestViewsCount(t *testing.T) {
	v := NewViews()
	v.Record("978-84-376-0494-7")
	v.Record("978-84-376-0494-7")
	v.Record("978-0-14-118776-1")
	if got := v.Count("978-84-376-0494-7"); got != 2 {
		t.Errorf("Count = %d, want 2", got)
	}
}

// PASO 1: dos peticiones a la vez registran una visita al mismo libro.
// func TestViewsConcurrent(t *testing.T) {
// 	v := NewViews()
// 	var wg sync.WaitGroup
// 	for range 2 {
// 		wg.Go(func() {
// 			v.Record("978-84-376-0494-7")
// 		})
// 	}
// 	wg.Wait()
// 	if got := v.Count("978-84-376-0494-7"); got != 2 {
// 		t.Errorf("Count = %d, want 2", got)
// 	}
// }
