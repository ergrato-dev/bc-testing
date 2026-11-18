package fees

// PASO 1: descomenta el import y el primer test. En el PASO 3 descomentarás "errors".
// import (
// 	// "errors"
// 	"testing"
// )

// func TestLateFeeThreeDays(t *testing.T) {
// 	got, err := LateFee(3)
// 	if err != nil {
// 		t.Fatalf("LateFee(3) returned error: %v", err)
// 	}
// 	if got != 150 {
// 		t.Errorf("LateFee(3) = %d, want 150", got)
// 	}
// }

// PASO 2: la multa tiene un tope.
// func TestLateFeeIsCapped(t *testing.T) {
// 	got, err := LateFee(30)
// 	if err != nil {
// 		t.Fatalf("LateFee(30) returned error: %v", err)
// 	}
// 	if got != MaxFee {
// 		t.Errorf("LateFee(30) = %d, want %d", got, MaxFee)
// 	}
// }

// PASO 3: los días negativos son un error.
// func TestLateFeeRejectsNegativeDays(t *testing.T) {
// 	_, err := LateFee(-1)
// 	if !errors.Is(err, ErrNegativeDays) {
// 		t.Errorf("LateFee(-1) error = %v, want %v", err, ErrNegativeDays)
// 	}
// }
