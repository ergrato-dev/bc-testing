package reminders

import (
// "errors" // PASO 2
// "slices" // PASO 1
// "testing" // PASO 1
// "time" // PASO 1
)

// PASO 1: un reloj fijo, un fake del almacén y un spy del notificador.
// var now = time.Date(2026, time.March, 10, 9, 0, 0, 0, time.UTC)

// // fixedClock sustituye a time.Now: siempre devuelve la misma hora.
// func fixedClock() time.Time { return now }

// // memStore es un fake: un almacén que funciona de verdad, pero en memoria.
// type memStore struct {
// 	loans []Loan
// }

// func (m *memStore) Add(l Loan) { m.loans = append(m.loans, l) }

// func (m *memStore) Active() ([]Loan, error) { return slices.Clone(m.loans), nil }

// // recordingNotifier es un spy: guarda cada aviso para que el test lo compruebe.
// type recordingNotifier struct {
// 	sent []string
// 	err  error
// }

// func (r *recordingNotifier) Notify(member, message string) error {
// 	r.sent = append(r.sent, member+" <- "+message)
// 	return r.err
// }

// // newStore devuelve un almacén con dos préstamos vencidos y uno al día.
// func newStore() *memStore {
// 	store := &memStore{}
// 	store.Add(Loan{Title: "Dune", Member: "ana", Due: now.AddDate(0, 0, -3)})
// 	store.Add(Loan{Title: "Emma", Member: "luis", Due: now.AddDate(0, 0, 5)})
// 	store.Add(Loan{Title: "Ulises", Member: "eva", Due: now.AddDate(0, 0, -12)})
// 	return store
// }

// func TestSendReminders(t *testing.T) {
// 	notifier := &recordingNotifier{}
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}

// 	sent, err := s.SendReminders()
// 	if err != nil {
// 		t.Fatalf("SendReminders: %v", err)
// 	}
// 	if sent != 2 {
// 		t.Errorf("sent = %d, want 2", sent)
// 	}
// 	want := []string{
// 		"ana <- Dune: 3 días de retraso",
// 		"eva <- Ulises: 12 días de retraso",
// 	}
// 	if !slices.Equal(notifier.sent, want) {
// 		t.Errorf("notices = %q, want %q", notifier.sent, want)
// 	}
// }

// PASO 2: stubs que fallan.
// // errStore es un stub: siempre da la misma respuesta, un error.
// type errStore struct {
// 	err error
// }

// func (e errStore) Active() ([]Loan, error) { return nil, e.err }

// var errDown = errors.New("connection refused")

// func TestSendRemindersWhenStoreFails(t *testing.T) {
// 	notifier := &recordingNotifier{}
// 	s := &Service{Loans: errStore{err: errDown}, Notifier: notifier, Now: fixedClock}

// 	_, err := s.SendReminders()
// 	if !errors.Is(err, errDown) {
// 		t.Errorf("SendReminders error = %v, want %v", err, errDown)
// 	}
// 	if len(notifier.sent) != 0 {
// 		t.Errorf("notices = %q, want none", notifier.sent)
// 	}
// }

// func TestSendRemindersStopsAtFirstFailedNotice(t *testing.T) {
// 	notifier := &recordingNotifier{err: errDown}
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}

// 	sent, err := s.SendReminders()
// 	if !errors.Is(err, errDown) {
// 		t.Fatalf("SendReminders error = %v, want %v", err, errDown)
// 	}
// 	if sent != 0 || len(notifier.sent) != 1 {
// 		t.Errorf("sent = %d, notices = %q, want 0 and one attempt", sent, notifier.sent)
// 	}
// }
