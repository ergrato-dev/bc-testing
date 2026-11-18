// La API de la biblioteca insegura: el sistema bajo prueba de los labs de la semana 44.
//
// Tiene errores de seguridad a propósito. Cada lab escribe los tests que los encuentran
// y después los corrige. No la expongas fuera de tu equipo.
//
//	GET    /                  página HTML con el buscador
//	GET    /buscar?q=texto    página HTML con los libros cuyo título contiene el texto
//	GET    /api/books?q=texto JSON con los libros cuyo título contiene el texto
//	GET    /api/loans/{id}    JSON con un préstamo; necesita token
//	DELETE /api/books/{id}    borra un libro; solo para administradores
//
// Los tokens son fijos (ver users): Authorization: Bearer ana-token.
//
// Variables de entorno:
//
//	PORT          puerto HTTP (18044)
//	DATABASE_URL  conexión a PostgreSQL (la de compose.yaml)
package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"net/http"
	"os"
	"strings"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
	"github.com/jackc/pgx/v5/pgxpool"
)

// DBTX es lo que la API usa de PostgreSQL. Lo cumplen el pool y una transacción:
// los tests le pasan una transacción y la deshacen al terminar.
type DBTX interface {
	Query(ctx context.Context, sql string, args ...any) (pgx.Rows, error)
	QueryRow(ctx context.Context, sql string, args ...any) pgx.Row
	Exec(ctx context.Context, sql string, args ...any) (pgconn.CommandTag, error)
}

type book struct {
	ID     int    `json:"id"`
	Title  string `json:"title"`
	Author string `json:"author"`
}

type loan struct {
	ID     int    `json:"id"`
	BookID int    `json:"bookId"`
	Member string `json:"member"`
}

type user struct {
	Name string
	Role string // "member" o "admin"
}

// users asocia cada token con su usuario. En un sistema real el token sale de un
// inicio de sesión; aquí es fijo para que los tests no dependan de él.
var users = map[string]user{
	"ana-token":   {Name: "ana", Role: "member"},
	"luis-token":  {Name: "luis", Role: "member"},
	"admin-token": {Name: "admin", Role: "admin"},
}

func main() {
	pool, err := pgxpool.New(context.Background(), env("DATABASE_URL", "postgres://library:library@localhost:55444/library"))
	if err != nil {
		log.Fatal(err)
	}
	defer pool.Close()

	addr := "localhost:" + env("PORT", "18044")
	log.Printf("Biblioteca insegura en http://%s", addr)
	log.Fatal(http.ListenAndServe(addr, newHandler(pool)))
}

func newHandler(db DBTX) http.Handler {
	mux := http.NewServeMux()

	mux.HandleFunc("GET /{$}", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		fmt.Fprint(w, `<!doctype html>
<html lang="es"><head><meta charset="utf-8"><title>Biblioteca</title></head>
<body><h1>Biblioteca</h1>
<form action="/buscar"><input name="q"><button>Buscar</button></form>
<p><a href="/buscar?q=go">Libros de Go</a></p>
</body></html>`)
	})

	mux.HandleFunc("GET /buscar", func(w http.ResponseWriter, r *http.Request) {
		q := r.URL.Query().Get("q")
		books, err := searchBooks(r.Context(), db, q)
		if err != nil {
			writeError(w, http.StatusInternalServerError, err.Error())
			return
		}
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		fmt.Fprintf(w, "<!doctype html>\n<html lang=\"es\"><head><meta charset=\"utf-8\"><title>Búsqueda</title></head>\n<body><h1>Resultados para %s</h1>\n<ul>\n", q)
		for _, b := range books {
			fmt.Fprintf(w, "<li>%s, de %s</li>\n", b.Title, b.Author)
		}
		fmt.Fprint(w, "</ul>\n<p><a href=\"/\">Volver</a></p>\n</body></html>\n")
	})

	mux.HandleFunc("GET /api/books", func(w http.ResponseWriter, r *http.Request) {
		books, err := searchBooks(r.Context(), db, r.URL.Query().Get("q"))
		if err != nil {
			writeError(w, http.StatusInternalServerError, err.Error())
			return
		}
		writeJSON(w, http.StatusOK, books)
	})

	mux.HandleFunc("GET /api/loans/{id}", func(w http.ResponseWriter, r *http.Request) {
		if _, ok := currentUser(r); !ok {
			writeError(w, http.StatusUnauthorized, "missing or invalid token")
			return
		}
		var l loan
		err := db.QueryRow(r.Context(),
			"SELECT id, book_id, member FROM loans WHERE id = $1", r.PathValue("id"),
		).Scan(&l.ID, &l.BookID, &l.Member)
		if errors.Is(err, pgx.ErrNoRows) {
			writeError(w, http.StatusNotFound, "loan not found")
			return
		}
		if err != nil {
			writeError(w, http.StatusInternalServerError, err.Error())
			return
		}
		writeJSON(w, http.StatusOK, l)
	})

	mux.HandleFunc("DELETE /api/books/{id}", func(w http.ResponseWriter, r *http.Request) {
		tag, err := db.Exec(r.Context(), "DELETE FROM books WHERE id = $1", r.PathValue("id"))
		if err != nil {
			writeError(w, http.StatusInternalServerError, err.Error())
			return
		}
		if tag.RowsAffected() == 0 {
			writeError(w, http.StatusNotFound, "book not found")
			return
		}
		w.WriteHeader(http.StatusNoContent)
	})

	return mux
}

// searchBooks devuelve los libros cuyo título contiene q, sin distinguir mayúsculas.
func searchBooks(ctx context.Context, db DBTX, q string) ([]book, error) {
	rows, err := db.Query(ctx,
		"SELECT id, title, author FROM books WHERE title ILIKE '%"+q+"%' ORDER BY id")
	if err != nil {
		return nil, err
	}
	return pgx.CollectRows(rows, func(row pgx.CollectableRow) (book, error) {
		var b book
		err := row.Scan(&b.ID, &b.Title, &b.Author)
		return b, err
	})
}

// currentUser lee el usuario del token de la cabecera Authorization: Bearer <token>.
func currentUser(r *http.Request) (user, bool) {
	token, ok := strings.CutPrefix(r.Header.Get("Authorization"), "Bearer ")
	if !ok {
		return user{}, false
	}
	u, ok := users[token]
	return u, ok
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(v)
}

func writeError(w http.ResponseWriter, status int, msg string) {
	writeJSON(w, status, map[string]string{"error": msg})
}

func env(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
