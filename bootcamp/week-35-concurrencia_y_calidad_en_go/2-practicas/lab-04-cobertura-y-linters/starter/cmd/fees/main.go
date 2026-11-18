// fees imprime el recibo de un recargo: fees <socio> <días> [categoría].
package main

import (
	"fmt"
	"os"
	"strconv"

	"bootcamp/lab04/fees"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Fprintln(os.Stderr, "usage: fees <member> <days> [category]")
		os.Exit(2)
	}
	days, err := strconv.Atoi(os.Args[2])
	category := ""
	if len(os.Args) > 3 {
		category = os.Args[3]
	}
	receipt, err := fees.Receipt(os.Args[1], days, category)
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
	fmt.Println(receipt)
}
