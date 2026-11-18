// La API de la biblioteca: el sistema bajo prueba de los labs de la semana 43.
//
//	GET    /health              200 si la base de datos responde
//	GET    /api/books/{isbn}    200 con el libro, o 404
//	GET    /api/books?q=texto   200 con hasta 20 libros cuyo título contiene el texto
//	POST   /api/loans           presta un libro; cuerpo {"isbn": "...", "member": "..."}
//	                            201, 404 si el libro no existe, 409 si ya está prestado,
//	                            422 si falta un campo
//	DELETE /api/loans/{isbn}    devuelve el libro: 204, o 404 si no estaba prestado
//
// Si una consulta no termina en 2 s, la petición responde 503.
//
// Variables de entorno:
//
//	PORT          puerto HTTP (18043)
//	DATABASE_URL  conexión a PostgreSQL (la de compose.yaml)
//	DB_MAX_CONNS  conexiones del pool (4)
package main

import (
	"context"
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
	"github.com/jackc/pgx/v5/pgxpool"
)

type book struct {
	ISBN   string `json:"isbn"`
	Title  string `json:"title"`
	Author string `json:"author"`
	Year   int    `json:"year"`
}

const queryTimeout = 2 * time.Second

func main() {
	cfg, err := pgxpool.ParseConfig(env("DATABASE_URL", "postgres://library:library@localhost:55443/library"))
	if err != nil {
		log.Fatal(err)
	}
	maxConns, err := strconv.Atoi(env("DB_MAX_CONNS", "4"))
	if err != nil {
		log.Fatal("DB_MAX_CONNS: ", err)
	}
	cfg.MaxConns = int32(maxConns)

	pool, err := pgxpool.NewWithConfig(context.Background(), cfg)
	if err != nil {
		log.Fatal(err)
	}
	defer pool.Close()

	addr := "localhost:" + env("PORT", "18043")
	log.Printf("Biblioteca en http://%s (pool de %d conexiones)", addr, cfg.MaxConns)
	log.Fatal(http.ListenAndServe(addr, newHandler(pool)))
}

func env(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}

func newHandler(db *pgxpool.Pool) http.Handler {
	mux := http.NewServeMux()

	mux.HandleFunc("GET /health", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), queryTimeout)
		defer cancel()
		if err := db.Ping(ctx); err != nil {
			writeError(w, err)
			return
		}
		w.Write([]byte("ok\n"))
	})

	mux.HandleFunc("GET /api/books/{isbn}", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), queryTimeout)
		defer cancel()
		var b book
		err := db.QueryRow(ctx,
			`SELECT isbn, title, author, year FROM books WHERE isbn = $1`,
			r.PathValue("isbn")).Scan(&b.ISBN, &b.Title, &b.Author, &b.Year)
		if err != nil {
			writeError(w, err)
			return
		}
		writeJSON(w, http.StatusOK, b)
	})

	// La búsqueda recorre la tabla entera: title no tiene un índice que sirva para ILIKE '%...%'.
	mux.HandleFunc("GET /api/books", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), queryTimeout)
		defer cancel()
		rows, err := db.Query(ctx,
			`SELECT isbn, title, author, year FROM books
			 WHERE title ILIKE '%' || $1 || '%' ORDER BY title LIMIT 20`,
			r.URL.Query().Get("q"))
		if err != nil {
			writeError(w, err)
			return
		}
		books, err := pgx.CollectRows(rows, pgx.RowToStructByPos[book])
		if err != nil {
			writeError(w, err)
			return
		}
		writeJSON(w, http.StatusOK, books)
	})

	mux.HandleFunc("POST /api/loans", func(w http.ResponseWriter, r *http.Request) {
		var req struct {
			ISBN   string `json:"isbn"`
			Member string `json:"member"`
		}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil || req.ISBN == "" || req.Member == "" {
			writeJSON(w, http.StatusUnprocessableEntity, map[string]string{"error": "isbn and member are required"})
			return
		}
		ctx, cancel := context.WithTimeout(r.Context(), queryTimeout)
		defer cancel()
		_, err := db.Exec(ctx, `INSERT INTO loans (isbn, member) VALUES ($1, $2)`, req.ISBN, req.Member)
		if err != nil {
			writeError(w, err)
			return
		}
		writeJSON(w, http.StatusCreated, req)
	})

	mux.HandleFunc("DELETE /api/loans/{isbn}", func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), queryTimeout)
		defer cancel()
		tag, err := db.Exec(ctx, `DELETE FROM loans WHERE isbn = $1`, r.PathValue("isbn"))
		if err != nil {
			writeError(w, err)
			return
		}
		if tag.RowsAffected() == 0 {
			writeJSON(w, http.StatusNotFound, map[string]string{"error": "not lent"})
			return
		}
		w.WriteHeader(http.StatusNoContent)
	})

	return mux
}

// writeError traduce los errores de la base de datos a códigos HTTP.
func writeError(w http.ResponseWriter, err error) {
	var pgErr *pgconn.PgError
	switch {
	case errors.Is(err, pgx.ErrNoRows):
		writeJSON(w, http.StatusNotFound, map[string]string{"error": "not found"})
	case errors.As(err, &pgErr) && pgErr.Code == "23505": // unique_violation: ya prestado
		writeJSON(w, http.StatusConflict, map[string]string{"error": "already lent"})
	case errors.As(err, &pgErr) && pgErr.Code == "23503": // foreign_key_violation: no existe
		writeJSON(w, http.StatusNotFound, map[string]string{"error": "not found"})
	case errors.Is(err, context.DeadlineExceeded):
		writeJSON(w, http.StatusServiceUnavailable, map[string]string{"error": "timeout"})
	default:
		log.Print(err)
		writeJSON(w, http.StatusInternalServerError, map[string]string{"error": "internal error"})
	}
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(v)
}
