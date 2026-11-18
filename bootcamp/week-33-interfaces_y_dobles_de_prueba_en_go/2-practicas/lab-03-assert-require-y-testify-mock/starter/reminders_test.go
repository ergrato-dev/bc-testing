package reminders

import (
	"testing"
	// "github.com/stretchr/testify/assert" // PASO 1
	// "github.com/stretchr/testify/mock" // PASO 3
	// "github.com/stretchr/testify/require" // PASO 1
)

// PASO 0: el test del lab 01, con la librería estándar.
func TestSendReminders(t *testing.T) {
	notifier := &recordingNotifier{}
	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}

	sent, err := s.SendReminders()
	if err != nil {
		t.Fatalf("SendReminders: %v", err)
	}
	if sent != 2 {
		t.Errorf("sent = %d, want 2", sent)
	}
	want := []string{
		"ana <- Dune: 3 días de retraso",
		"eva <- Ulises: 12 días de retraso",
	}
	if len(notifier.sent) != len(want) || notifier.sent[0] != want[0] || notifier.sent[1] != want[1] {
		t.Errorf("notices = %q, want %q", notifier.sent, want)
	}
}

// PASO 1: el mismo test con require y assert.
// func TestSendRemindersWithTestify(t *testing.T) {
// 	notifier := &recordingNotifier{}
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}
//
// 	sent, err := s.SendReminders()
// 	require.NoError(t, err)
// 	assert.Equal(t, 2, sent)
// 	assert.Equal(t, []string{
// 		"ana <- Dune: 3 días de retraso",
// 		"eva <- Ulises: 12 días de retraso",
// 	}, notifier.sent)
// }

// PASO 2: require cuando lo que sigue depende del resultado.
// func TestMostOverdue(t *testing.T) {
// 	s := &Service{Loans: newStore(), Now: fixedClock}
//
// 	loan, err := s.MostOverdue()
// 	require.NoError(t, err)
// 	assert.Equal(t, "Ulises", loan.Title)
// }

// func TestMostOverdueWithoutOverdueLoans(t *testing.T) {
// 	s := &Service{Loans: &memStore{}, Now: fixedClock}
//
// 	loan, err := s.MostOverdue()
// 	assert.ErrorIs(t, err, ErrNoOverdue)
// 	assert.Nil(t, loan)
// }

// PASO 3: un mock escrito con testify/mock.
// // mockNotifier delega cada llamada en mock.Mock, que la compara con las expectativas.
// type mockNotifier struct {
// 	mock.Mock
// }

// func (m *mockNotifier) Notify(member, message string) error {
// 	args := m.Called(member, message)
// 	return args.Error(0)
// }

// func TestSendRemindersWithTestifyMock(t *testing.T) {
// 	notifier := &mockNotifier{}
// 	notifier.On("Notify", "ana", "Dune: 3 días de retraso").Return(nil).Once()
// 	notifier.On("Notify", "eva", "Ulises: 12 días de retraso").Return(nil).Once()
//
// 	s := &Service{Loans: newStore(), Notifier: notifier, Now: fixedClock}
// 	_, err := s.SendReminders()
// 	require.NoError(t, err)
// 	notifier.AssertExpectations(t)
// }
