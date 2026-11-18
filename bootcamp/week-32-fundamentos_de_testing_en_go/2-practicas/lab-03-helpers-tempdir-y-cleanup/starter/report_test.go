package report

// PASO 1: descomenta "io" y "slices". PASO 4: descomenta "errors".
import (
	"context"
	// "errors"
	// "io"
	"os"
	// "slices"
	"strings"
	"testing"
)

var loans = []Loan{
	{Title: "Dune", Member: "ana", DaysLate: 3},
	{Title: "Emma", Member: "luis", DaysLate: 0},
	{Title: "Ulises", Member: "eva", DaysLate: 12},
}

// PASO 0: este test pasa, pero deja un directorio en /tmp cada vez que se ejecuta.
func TestWriteOverdueManualDir(t *testing.T) {
	dir, err := os.MkdirTemp("", "lab03-")
	if err != nil {
		t.Fatal(err)
	}
	path, err := WriteOverdue(context.Background(), dir, loans)
	if err != nil {
		t.Fatalf("WriteOverdue: %v", err)
	}
	data, err := os.ReadFile(path)
	if err != nil {
		t.Fatal(err)
	}
	if !strings.Contains(string(data), "Ulises,eva,12") {
		t.Errorf("report = %q, missing Ulises", data)
	}
}

// PASO 1: helpers y directorio temporal.
// // openReport abre el informe y lo cierra cuando termina el test.
// func openReport(t *testing.T, path string) *os.File {
// 	t.Helper()
// 	f, err := os.Open(path)
// 	if err != nil {
// 		t.Fatalf("opening report: %v", err)
// 	}
// 	t.Cleanup(func() { f.Close() })
// 	return f
// }

// // readLines devuelve las líneas del informe.
// func readLines(t *testing.T, path string) []string {
// 	t.Helper()
// 	data, err := io.ReadAll(openReport(t, path))
// 	if err != nil {
// 		t.Fatalf("reading report: %v", err)
// 	}
// 	return strings.Split(strings.TrimSpace(string(data)), "\n")
// }

// // assertLines compara las líneas del informe con las esperadas.
// func assertLines(t *testing.T, got, want []string) {
// 	t.Helper()
// 	if !slices.Equal(got, want) {
// 		t.Errorf("lines = %q, want %q", got, want)
// 	}
// }

// func TestWriteOverdue(t *testing.T) {
// 	path, err := WriteOverdue(t.Context(), t.TempDir(), loans)
// 	if err != nil {
// 		t.Fatalf("WriteOverdue: %v", err)
// 	}
// 	assertLines(t, readLines(t, path), []string{
// 		"title,member,days_late",
// 		"Dune,ana,3",
// 		"Ulises,eva,12",
// 	})
// }

// PASO 2: el mismo helper en otro test.
// func TestWriteOverdueWithoutLateLoans(t *testing.T) {
// 	path, err := WriteOverdue(t.Context(), t.TempDir(), loans[1:2])
// 	if err != nil {
// 		t.Fatalf("WriteOverdue: %v", err)
// 	}
// 	assertLines(t, readLines(t, path), []string{"title,member,days_late"})
// }

// PASO 4: un contexto cancelado.
// func TestWriteOverdueStopsWhenContextIsCanceled(t *testing.T) {
// 	ctx, cancel := context.WithCancel(t.Context())
// 	cancel()
// 	_, err := WriteOverdue(ctx, t.TempDir(), loans)
// 	if !errors.Is(err, context.Canceled) {
// 		t.Errorf("WriteOverdue error = %v, want %v", err, context.Canceled)
// 	}
// }
