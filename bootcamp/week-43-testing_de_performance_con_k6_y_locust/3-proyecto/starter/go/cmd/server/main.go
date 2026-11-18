// El servidor de la API de la semana 34, para las pruebas de rendimiento de la semana 43.
//
// Arranca NewHandler contra PostgreSQL, con un registro que acredita a todas las
// instituciones y un notificador que no envía nada: las pruebas de carga miden la API
// y la base de datos, no los servicios externos.
//
// Variables de entorno:
//
//	DATABASE_URL  conexión a PostgreSQL (la base museum_perf que crea perf/smoke.sh)
//	PORT          puerto HTTP (18080)
//	DB_MAX_CONNS  conexiones del pool (si no se pone, las de pgxpool: 4 o el número de CPU)
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"

	// TODO: el paquete de tu dominio y el de tu almacén, con la ruta de tu go.mod.
	"example.com/qa-museo/go/museum"
	"example.com/qa-museo/go/pgstore"
)

// TODO: las dependencias de tu servicio que llaman a sistemas externos, con una
// implementación que no llama a nada. En el museo, Registry y Notifier.
type acceptAll struct{}

func (acceptAll) IsAccredited(string) bool { return true }

type discard struct{}

func (discard) Send(museum.LoanNotice) error { return nil }

func main() {
	ctx := context.Background()

	cfg, err := pgxpool.ParseConfig(env("DATABASE_URL", "postgres://museum:museum@localhost:55432/museum_perf"))
	if err != nil {
		log.Fatal(err)
	}
	if v := os.Getenv("DB_MAX_CONNS"); v != "" {
		n, err := strconv.Atoi(v)
		if err != nil {
			log.Fatal("DB_MAX_CONNS: ", err)
		}
		cfg.MaxConns = int32(n)
	}
	pool, err := pgxpool.NewWithConfig(ctx, cfg)
	if err != nil {
		log.Fatal(err)
	}
	defer pool.Close()

	// La base de datos es nueva en cada ejecución de perf/smoke.sh: se crean las tablas.
	if err := pgstore.Migrate(ctx, pool); err != nil {
		log.Fatal(err)
	}

	// TODO: tu servicio, con el almacén de PostgreSQL y las dependencias de arriba.
	store := &pgstore.Store{DB: pool}
	service := &museum.LendingService{
		Pieces:   store,
		Loans:    store,
		Registry: acceptAll{},
		Notifier: discard{},
		Now:      time.Now,
	}

	addr := "localhost:" + env("PORT", "18080")
	log.Printf("API en http://%s (pool de %d conexiones)", addr, cfg.MaxConns)
	log.Fatal(http.ListenAndServe(addr, museum.NewHandler(service)))
}

func env(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
