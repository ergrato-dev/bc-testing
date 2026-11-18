//go:build integration

package books

import (
// "context" // PASO 2
// "errors" // PASO 1
// "log" // PASO 2
// "testing" // PASO 1
// "github.com/google/go-cmp/cmp" // PASO 1
// "github.com/jackc/pgx/v5/pgxpool" // PASO 1
// "github.com/testcontainers/testcontainers-go" // PASO 1
// "github.com/testcontainers/testcontainers-go/modules/postgres" // PASO 1
)

// image es la imagen de PostgreSQL del curso (docs/stack-versions.md).
const image = "postgres:18.7-alpine"

// PASO 1: un contenedor nuevo para cada test.
// // newStore arranca un PostgreSQL, aplica las migraciones y lo para al
// // terminar el test.
// func newStore(t *testing.T) *Store {
// 	t.Helper()
// 	ctr, err := postgres.Run(t.Context(), image,
// 		postgres.WithDatabase("library"),
// 		postgres.WithUsername("library"),
// 		postgres.WithPassword("library"),
// 		postgres.BasicWaitStrategies(),
// 	)
// 	testcontainers.CleanupContainer(t, ctr)
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	pool, err := pgxpool.New(t.Context(), ctr.MustConnectionString(t.Context(), "sslmode=disable"))
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	t.Cleanup(pool.Close)
// 	if err := Migrate(t.Context(), pool); err != nil {
// 		t.Fatal(err)
// 	}
// 	return &Store{DB: pool}
// }
//
// func TestAddAndGet(t *testing.T) {
// 	s := newStore(t)
// 	want := Book{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2}
// 	if err := s.Add(t.Context(), want); err != nil {
// 		t.Fatal(err)
// 	}
// 	got, err := s.Get(t.Context(), want.ISBN)
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	if diff := cmp.Diff(want, got); diff != "" {
// 		t.Errorf("Get() mismatch (-want +got):\n%s", diff)
// 	}
// }
//
// func TestAddDuplicate(t *testing.T) {
// 	s := newStore(t)
// 	b := Book{ISBN: "978-84-376-0494-7", Title: "Rayuela", Copies: 2}
// 	if err := s.Add(t.Context(), b); err != nil {
// 		t.Fatal(err)
// 	}
// 	if err := s.Add(t.Context(), b); !errors.Is(err, ErrDuplicate) {
// 		t.Errorf("second Add: err = %v, want ErrDuplicate", err)
// 	}
// }
//
// func TestList(t *testing.T) {
// 	s := newStore(t)
// 	want := []Book{{ISBN: "978-0-14-118776-1", Title: "Ficciones", Copies: 1}}
// 	for _, b := range want {
// 		if err := s.Add(t.Context(), b); err != nil {
// 			t.Fatal(err)
// 		}
// 	}
// 	got, err := s.List(t.Context())
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	if diff := cmp.Diff(want, got); diff != "" {
// 		t.Errorf("List() mismatch (-want +got):\n%s", diff)
// 	}
// }
//
// func TestLendLastCopy(t *testing.T) {
// 	s := newStore(t)
// 	if err := s.Add(t.Context(), Book{ISBN: "978-607-07-0398-3", Title: "Pedro Páramo", Copies: 1}); err != nil {
// 		t.Fatal(err)
// 	}
// 	if err := s.Lend(t.Context(), "978-607-07-0398-3"); err != nil {
// 		t.Fatalf("first Lend: %v", err)
// 	}
// 	if err := s.Lend(t.Context(), "978-607-07-0398-3"); !errors.Is(err, ErrNoCopies) {
// 		t.Errorf("second Lend: err = %v, want ErrNoCopies", err)
// 	}
// }

// PASO 2: un contenedor para todo el paquete.
// // pool es la conexión al PostgreSQL que arranca TestMain.
// var pool *pgxpool.Pool
//
// func TestMain(m *testing.M) {
// 	ctx := context.Background()
// 	ctr, err := postgres.Run(ctx, image,
// 		postgres.WithDatabase("library"),
// 		postgres.WithUsername("library"),
// 		postgres.WithPassword("library"),
// 		postgres.BasicWaitStrategies(),
// 	)
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// 	pool, err = pgxpool.New(ctx, ctr.MustConnectionString(ctx, "sslmode=disable"))
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// 	if err := Migrate(ctx, pool); err != nil {
// 		log.Fatal(err)
// 	}
//
// 	m.Run()
//
// 	pool.Close()
// 	if err := testcontainers.TerminateContainer(ctr); err != nil {
// 		log.Print(err)
// 	}
// }
//
// func newStore(t *testing.T) *Store {
// 	t.Helper()
// 	return &Store{DB: pool}
// }

// PASO 3: una transacción por test, deshecha al terminar.
// func newStore(t *testing.T) *Store {
// 	t.Helper()
// 	tx, err := pool.Begin(t.Context())
// 	if err != nil {
// 		t.Fatal(err)
// 	}
// 	t.Cleanup(func() {
// 		// t.Context() ya está cancelado cuando se ejecuta Cleanup.
// 		if err := tx.Rollback(context.Background()); err != nil {
// 			t.Errorf("rollback: %v", err)
// 		}
// 	})
// 	return &Store{DB: tx}
// }
