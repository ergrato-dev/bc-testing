//go:build integration

package pgstore

import "testing"

// TODO: TestMain con un contenedor postgres:18.7-alpine para todo el paquete
// y un helper newStore(t) con una transacción por test (paso 6).

func TestSaveAndFind(t *testing.T) {
	t.Skip("TODO: guardar y leer, un subtest por estado (paso 6)")
}

func TestStoreErrors(t *testing.T) {
	t.Skip("TODO: lo que no existe y lo que la tabla rechaza (paso 6)")
}

func TestServiceWithPostgres(t *testing.T) {
	t.Skip("TODO: el servicio completo sobre el Store, en la misma transacción (paso 7)")
}
