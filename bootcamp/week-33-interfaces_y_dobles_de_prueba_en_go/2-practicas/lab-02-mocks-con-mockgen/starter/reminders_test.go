package reminders

import (
// "errors" // PASO 4
// "testing" // PASO 2
// "go.uber.org/mock/gomock" // PASO 2
)

// PASO 2: un mock con los argumentos exactos.
// func TestSendRemindersNotifiesEachOverdueLoan(t *testing.T) {
// 	ctrl := gomock.NewController(t)
// 	notifier := NewMockNotifier(ctrl)
// 	notifier.EXPECT().Notify("ana", "Dune: 3 días de retraso").Return(nil)
// 	notifier.EXPECT().Notify("eva", "Ulises: 12 días de retraso").Return(nil)
//
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}
// 	sent, err := s.SendReminders()
// 	if err != nil {
// 		t.Fatalf("SendReminders: %v", err)
// 	}
// 	if sent != 2 {
// 		t.Errorf("sent = %d, want 2", sent)
// 	}
// }

// PASO 3: cualquier argumento, un número de veces.
// func TestSendRemindersSendsOneNoticePerOverdueLoan(t *testing.T) {
// 	notifier := NewMockNotifier(gomock.NewController(t))
// 	notifier.EXPECT().Notify(gomock.Any(), gomock.Any()).Return(nil).Times(2)
//
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}
// 	if _, err := s.SendReminders(); err != nil {
// 		t.Fatalf("SendReminders: %v", err)
// 	}
// }

// PASO 4: un mock que devuelve un error.
// func TestSendRemindersStopsAtFirstFailedNotice(t *testing.T) {
// 	errDown := errors.New("connection refused")
// 	notifier := NewMockNotifier(gomock.NewController(t))
// 	notifier.EXPECT().Notify("ana", gomock.Any()).Return(errDown)
//
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}
// 	sent, err := s.SendReminders()
// 	if !errors.Is(err, errDown) {
// 		t.Fatalf("SendReminders error = %v, want %v", err, errDown)
// 	}
// 	if sent != 0 {
// 		t.Errorf("sent = %d, want 0", sent)
// 	}
// }
