package report

import (
	"math/rand/v2"
	"testing"
	"time"
	// "flag" // PASO 3
	// "os" // PASO 3
	// "path/filepath" // PASO 3
	// "github.com/google/go-cmp/cmp" // PASO 1
	// "github.com/google/go-cmp/cmp/cmpopts" // PASO 1
)

var (
	now     = time.Date(2026, time.March, 10, 9, 0, 0, 0, time.UTC)
	catalog = []string{"Dune", "Emma", "Rayuela", "Ulises"}
	loans   = []Loan{
		{Title: "Dune", Member: "ana", Due: now.AddDate(0, 0, -3)},
		{Title: "Emma", Member: "luis", Due: now.AddDate(0, 0, 5)},
		{Title: "Ulises", Member: "eva", Due: now.AddDate(0, 0, -12)},
	}
)

// seeded devuelve un generador con semilla fija: da siempre la misma secuencia.
func seeded() *rand.Rand { return rand.New(rand.NewPCG(1, 2)) }

// PASO 0: el resumen tiene el día y el total.
func TestBuildTotal(t *testing.T) {
	got := Build(loans, now, catalog, seeded())
	if got.Date != "2026-03-10" || got.Total != 750 {
		t.Errorf("Date, Total = %q, %d, want %q, %d", got.Date, got.Total, "2026-03-10", 750)
	}
}

// PASO 1: el resumen entero con cmp.Diff.
// func TestBuild(t *testing.T) {
// 	got := Build(loans, now, catalog, seeded())
// 	want := Summary{
// 		Date: "2026-03-10",
// 		Lines: []Line{
// 			{Title: "Ulises", Member: "eva", DaysLate: 12, Fee: 600},
// 			{Title: "Dune", Member: "ana", DaysLate: 3, Fee: 150},
// 		},
// 		Total:    750,
// 		Featured: "Dune",
// 	}
// 	if diff := cmp.Diff(want, got, cmpopts.IgnoreUnexported(Line{})); diff != "" {
// 		t.Errorf("Build() mismatch (-want +got):\n%s", diff)
// 	}
// }

// PASO 2: sin préstamos vencidos, la lista está vacía.
// func TestBuildWithoutOverdueLoans(t *testing.T) {
// 	got := Build(loans[1:2], now, nil, seeded())
// 	want := Summary{Date: "2026-03-10", Lines: []Line{}}
// 	if diff := cmp.Diff(want, got, cmpopts.EquateEmpty()); diff != "" {
// 		t.Errorf("Build() mismatch (-want +got):\n%s", diff)
// 	}
// }

// PASO 3: el texto del resumen contra un golden file.
// var update = flag.Bool("update", false, "reescribe los golden files de testdata/")

// // assertGolden compara got con testdata/<nombre del test>.golden. Con -update,
// // primero reescribe el archivo con got.
// func assertGolden(t *testing.T, got string) {
// 	t.Helper()
// 	path := filepath.Join("testdata", t.Name()+".golden")
// 	if *update {
// 		if err := os.MkdirAll("testdata", 0o755); err != nil {
// 			t.Fatal(err)
// 		}
// 		if err := os.WriteFile(path, []byte(got), 0o644); err != nil {
// 			t.Fatal(err)
// 		}
// 	}
// 	want, err := os.ReadFile(path)
// 	if err != nil {
// 		t.Fatalf("reading golden file: %v (run go test -update)", err)
// 	}
// 	if diff := cmp.Diff(string(want), got); diff != "" {
// 		t.Errorf("%s mismatch (-want +got):\n%s", path, diff)
// 	}
// }

// func TestRender(t *testing.T) {
// 	assertGolden(t, Build(loans, now, catalog, seeded()).Render())
// }
