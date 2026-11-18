package holds

import (
// "context" // PASO 3
// "errors" // PASO 3
// "testing" // PASO 2
// "testing/synctest" // PASO 2
// "time" // PASO 2
)

// PASO 2: una reserva de 48 horas caduca, y el test no espera 48 horas.
// func TestHoldExpires(t *testing.T) {
// 	synctest.Test(t, func(t *testing.T) {
// 		h := NewHolds(48 * time.Hour)
// 		if err := h.Place("978-84-376-0494-7", "ana"); err != nil {
// 			t.Fatal(err)
// 		}
//
// 		time.Sleep(48*time.Hour - time.Second)
// 		synctest.Wait()
// 		if _, ok := h.Holder("978-84-376-0494-7"); !ok {
// 			t.Fatal("hold expired early")
// 		}
//
// 		time.Sleep(time.Second)
// 		synctest.Wait()
// 		if m, ok := h.Holder("978-84-376-0494-7"); ok {
// 			t.Errorf("still held by %s after 48h", m)
// 		}
// 		if err := h.Place("978-84-376-0494-7", "luis"); err != nil {
// 			t.Errorf("Place after expiry: %v", err)
// 		}
// 	})
// }
//

// PASO 3: los reintentos esperan 1 s, 2 s y 4 s, y se rinden con el plazo del contexto.
// var errDown = errors.New("catalog down")
//
// func TestRetryBackoff(t *testing.T) {
// 	synctest.Test(t, func(t *testing.T) {
// 		calls := 0
// 		start := time.Now()
// 		err := Retry(t.Context(), 4, time.Second, func() error {
// 			calls++
// 			return errDown
// 		})
// 		if !errors.Is(err, errDown) {
// 			t.Errorf("err = %v, want errDown", err)
// 		}
// 		if calls != 4 {
// 			t.Errorf("calls = %d, want 4", calls)
// 		}
// 		if got := time.Since(start); got != 7*time.Second {
// 			t.Errorf("elapsed = %v, want 7s (1s + 2s + 4s)", got)
// 		}
// 	})
// }
//
// func TestRetryDeadline(t *testing.T) {
// 	synctest.Test(t, func(t *testing.T) {
// 		ctx, cancel := context.WithTimeout(t.Context(), 5*time.Second)
// 		defer cancel()
// 		start := time.Now()
// 		err := Retry(ctx, 10, time.Second, func() error { return errDown })
// 		if !errors.Is(err, context.DeadlineExceeded) || !errors.Is(err, errDown) {
// 			t.Errorf("err = %v, want DeadlineExceeded and errDown", err)
// 		}
// 		if got := time.Since(start); got != 5*time.Second {
// 			t.Errorf("elapsed = %v, want 5s", got)
// 		}
// 	})
// }
