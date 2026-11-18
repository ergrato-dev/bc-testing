package fees

import (
	"errors"
	"testing"
)

func TestLateFeeBasicThreeDays(t *testing.T) {
	got, err := LateFee(3, Basic)
	if err != nil {
		t.Fatalf("LateFee(3, Basic) returned error: %v", err)
	}
	if got != 150 {
		t.Errorf("LateFee(3, Basic) = %d, want 150", got)
	}
}

func TestLateFeeBasicCapped(t *testing.T) {
	got, err := LateFee(30, Basic)
	if err != nil {
		t.Fatalf("LateFee(30, Basic) returned error: %v", err)
	}
	if got != 1000 {
		t.Errorf("LateFee(30, Basic) = %d, want 1000", got)
	}
}

func TestLateFeePremiumWithinGrace(t *testing.T) {
	got, err := LateFee(2, Premium)
	if err != nil {
		t.Fatalf("LateFee(2, Premium) returned error: %v", err)
	}
	if got != 0 {
		t.Errorf("LateFee(2, Premium) = %d, want 0", got)
	}
}

func TestLateFeePremiumAfterGrace(t *testing.T) {
	got, err := LateFee(5, Premium)
	if err != nil {
		t.Fatalf("LateFee(5, Premium) returned error: %v", err)
	}
	if got != 75 {
		t.Errorf("LateFee(5, Premium) = %d, want 75", got)
	}
}

// PASO 1: la misma suite como tabla, con un subtest por fila. En el PASO 3 descomentarás t.Parallel().
// func TestLateFee(t *testing.T) {
// 	tests := []struct {
// 		name string
// 		days int
// 		m    Membership
// 		want int
// 	}{
// 		{name: "basic on time", days: 0, m: Basic, want: 0},
// 		{name: "basic three days", days: 3, m: Basic, want: 150},
// 		{name: "basic just below cap", days: 19, m: Basic, want: 950},
// 		{name: "basic capped", days: 30, m: Basic, want: 1000},
// 		{name: "premium within grace", days: 2, m: Premium, want: 0},
// 		{name: "premium after grace", days: 5, m: Premium, want: 75},
// 		{name: "premium capped", days: 40, m: Premium, want: 500},
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			// t.Parallel()
// 			got, err := LateFee(tt.days, tt.m)
// 			if err != nil {
// 				t.Fatalf("LateFee(%d, %v) returned error: %v", tt.days, tt.m, err)
// 			}
// 			if got != tt.want {
// 				t.Errorf("LateFee(%d, %v) = %d, want %d", tt.days, tt.m, got, tt.want)
// 			}
// 		})
// 	}
// }

func TestLateFeeRejectsNegativeDays(t *testing.T) {
	for _, m := range []Membership{Basic, Premium} {
		_, err := LateFee(-1, m)
		if !errors.Is(err, ErrNegativeDays) {
			t.Errorf("LateFee(-1, %v) error = %v, want %v", m, err, ErrNegativeDays)
		}
	}
}
