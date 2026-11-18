package fees

import (
	// "errors" // PASO 2
	"testing"
)

func TestLateFee(t *testing.T) {
	tests := []struct {
		days int
		want int
	}{
		{days: 0, want: 0},
		{days: 3, want: 150},
		{days: 40, want: 1500},
	}
	for _, tt := range tests {
		got, err := LateFee(tt.days, "")
		if err != nil || got != tt.want {
			t.Errorf("LateFee(%d) = %d, %v; want %d", tt.days, got, err, tt.want)
		}
	}
}

// PASO 2: las ramas que el informe de cobertura marcó en rojo.
// func TestLateFeeStaff(t *testing.T) {
// 	if got, err := LateFee(40, "staff"); err != nil || got != 0 {
// 		t.Errorf("LateFee(40, staff) = %d, %v; want 0", got, err)
// 	}
// }
//
// func TestLateFeeNegativeDays(t *testing.T) {
// 	if _, err := LateFee(-1, ""); !errors.Is(err, ErrNegativeDays) {
// 		t.Errorf("err = %v, want ErrNegativeDays", err)
// 	}
// }
//
// func TestReceipt(t *testing.T) {
// 	got, err := Receipt("ana lucía", 3, "")
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	if want := "Ana Lucía: 3 días, 150"; got != want {
// 		t.Errorf("Receipt() = %q, want %q", got, want)
// 	}
// 	if _, err := Receipt("ana lucía", -1, ""); !errors.Is(err, ErrNegativeDays) {
// 		t.Errorf("err = %v, want ErrNegativeDays", err)
// 	}
// }
