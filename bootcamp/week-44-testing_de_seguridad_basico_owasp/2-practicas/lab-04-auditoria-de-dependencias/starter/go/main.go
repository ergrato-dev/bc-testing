// Un programa pequeño con dependencias antiguas a propósito, para govulncheck.
package main

import (
	"fmt"
	"os"

	"golang.org/x/net/html"
	"golang.org/x/text/language"
)

func main() {
	// Llama a la función vulnerable de x/text: la cabecera Accept-Language llega del cliente.
	tags, _, err := language.ParseAcceptLanguage(os.Getenv("ACCEPT_LANGUAGE"))
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(tags)

	// Usa x/net/html solo para escapar texto: no llama a las funciones vulnerables.
	fmt.Println(html.EscapeString("<b>"))
}
