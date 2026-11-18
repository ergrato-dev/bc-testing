package isbn

import (
	"errors"
	"testing"
)

var parseTests = []struct {
	in   string
	want ISBN
	err  error
}{
	{in: "9788437604947", want: "9788437604947"},
	{in: "978-84-376-0494-7", want: "9788437604947"},
	{in: "ISBN 978 0 14 118776 1", want: "9780141187761"},
	{in: "978-84-376-0494-8", err: ErrChecksum},
	{in: "978-84-376-0494", err: ErrLength},
	{in: "", err: ErrLength},
}

func TestParse(t *testing.T) {
	for _, tt := range parseTests {
		got, err := Parse(tt.in)
		if got != tt.want || !errors.Is(err, tt.err) {
			t.Errorf("Parse(%q) = %q, %v; want %q, %v", tt.in, got, err, tt.want, tt.err)
		}
	}
}

// PASO 1: el fuzzer prueba entradas nuevas a partir de las de la tabla.
// func FuzzParse(f *testing.F) {
// 	for _, tt := range parseTests {
// 		f.Add(tt.in)
// 	}
// 	f.Fuzz(func(t *testing.T, s string) {
// 		Parse(s)
// 	})
// }

// PASO 2: lo que Parse acepta tiene que ser un ISBN: 13 dígitos ASCII.
// func FuzzParseDigits(f *testing.F) {
// 	for _, tt := range parseTests {
// 		f.Add(tt.in)
// 	}
// 	f.Fuzz(func(t *testing.T, s string) {
// 		id, err := Parse(s)
// 		if err != nil {
// 			return
// 		}
// 		if len(id) != 13 {
// 			t.Errorf("Parse(%q) = %q: want 13 digits", s, id)
// 		}
// 		for _, c := range []byte(id) {
// 			if c < '0' || c > '9' {
// 				t.Errorf("Parse(%q) = %q: %q is not a digit", s, id, c)
// 			}
// 		}
// 	})
// }
