package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;

// PASO 1
// import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;

public class CartSteps {

    private Cart cart;

    /** {pesos} reconoce "$12.500" y lo convierte en 12500. */
    @ParameterType("\\$\\d{1,3}(?:\\.\\d{3})*")
    public Integer pesos(String text) {
        return Integer.parseInt(text.replace("$", "").replace(".", ""));
    }

    /** Cada fila de una tabla con columnas producto y precio se convierte en un Product. */
    @DataTableType
    public Product product(Map<String, String> row) {
        return new Product(row.get("producto"), pesos(row.get("precio")));
    }

    // PASO 2
    // @DataTableType
    // public CartLine cartLine(Map<String, String> row) {
    //     return new CartLine(row.get("producto"), Integer.parseInt(row.get("cantidad")), pesos(row.get("importe")));
    // }

    @Given("el catálogo:")
    public void theCatalog(List<Product> products) {
        cart = new Cart(products);
    }

    // PASO 1
    // @When("añado al carrito:")
    // public void iAddToTheCart(DataTable table) {
    //     for (Map<String, String> row : table.asMaps()) {
    //         cart.add(row.get("producto"), Integer.parseInt(row.get("cantidad")));
    //     }
    // }

    @When("añado {int} {word} al carrito")
    public void iAdd(int quantity, String product) {
        cart.add(product, quantity);
    }

    // PASO 1
    // @Then("el subtotal es {pesos}")
    // public void theSubtotalIs(int expected) {
    //     assertThat(cart.subtotal()).isEqualTo(expected);
    // }

    // PASO 3
    // @Then("el descuento es {pesos}")
    // public void theDiscountIs(int expected) {
    //     assertThat(cart.discount()).isEqualTo(expected);
    // }

    @Then("el total es {pesos}")
    public void theTotalIs(int expected) {
        assertThat(cart.total()).isEqualTo(expected);
    }

    // PASO 2
    // @Then("el carrito tiene estas líneas:")
    // public void theCartHasTheseLines(List<CartLine> expected) {
    //     assertThat(cart.lines()).containsExactlyElementsOf(expected);
    // }
}
