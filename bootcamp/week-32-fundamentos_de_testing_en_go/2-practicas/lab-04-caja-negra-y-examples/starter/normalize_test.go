package catalog

// PASO 1: test de caja blanca, en el mismo paquete.
// import "testing"
//
// func TestNormalize(t *testing.T) {
// 	tests := []struct {
// 		name string
// 		in   string
// 		want string
// 	}{
// 		{name: "lowercases", in: "Dune", want: "dune"},
// 		{name: "trims", in: "  Dune ", want: "dune"},
// 		{name: "collapses inner spaces", in: "Cien   años", want: "cien años"},
// 		{name: "empty", in: "", want: ""},
// 	}
// 	for _, tt := range tests {
// 		t.Run(tt.name, func(t *testing.T) {
// 			if got := normalize(tt.in); got != tt.want {
// 				t.Errorf("normalize(%q) = %q, want %q", tt.in, got, tt.want)
// 			}
// 		})
// 	}
// }
