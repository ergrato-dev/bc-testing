package domain

import "testing"

func TestConcurrentRequests(t *testing.T) {
	t.Skip("TODO: varios clientes a la vez contra httptest.NewServer, limpio con -race (paso 3)")
}

func FuzzDomain(f *testing.F) {
	// TODO: corpus semilla con f.Add (paso 4)
	f.Fuzz(func(t *testing.T, s string) {
		t.Skip("TODO: una propiedad que se cumpla para cualquier entrada válida (paso 4)")
	})
}

func BenchmarkHotPath(b *testing.B) {
	b.Skip("TODO: la función que vas a optimizar, dentro de for b.Loop() (paso 5)")
}
