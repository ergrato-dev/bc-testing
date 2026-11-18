// Package openlib consulta un catálogo bibliográfico remoto por HTTP.
package openlib

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"net/url"
)

// Book son los datos que el catálogo remoto devuelve de un ISBN.
type Book struct {
	ISBN    string   `json:"isbn"`
	Title   string   `json:"title"`
	Authors []string `json:"authors"`
}

var (
	ErrNotFound    = errors.New("isbn not found")
	ErrUnavailable = errors.New("catalog unavailable")
)

// Client consulta el catálogo que hay en BaseURL con el cliente HTTP que se
// le pasa: los tests le dan el de su servidor de pruebas.
type Client struct {
	BaseURL string
	HTTP    *http.Client
}

// Lookup pide GET {BaseURL}/isbn/{isbn}. Devuelve ErrNotFound con un 404,
// ErrUnavailable con cualquier otro estado distinto de 200, y respeta la
// cancelación y el plazo de ctx.
func (c *Client) Lookup(ctx context.Context, isbn string) (Book, error) {
	req, err := http.NewRequestWithContext(ctx, http.MethodGet, c.BaseURL+"/isbn/"+url.PathEscape(isbn), nil)
	if err != nil {
		return Book{}, err
	}
	req.Header.Set("Accept", "application/json")
	res, err := c.HTTP.Do(req)
	if err != nil {
		return Book{}, fmt.Errorf("lookup %s: %w", isbn, err)
	}
	defer res.Body.Close()
	switch {
	case res.StatusCode == http.StatusNotFound:
		return Book{}, fmt.Errorf("%w: %s", ErrNotFound, isbn)
	case res.StatusCode != http.StatusOK:
		return Book{}, fmt.Errorf("%w: status %d", ErrUnavailable, res.StatusCode)
	}
	var b Book
	if err := json.NewDecoder(res.Body).Decode(&b); err != nil {
		return Book{}, fmt.Errorf("decoding %s: %w", isbn, err)
	}
	return b, nil
}
