package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 3: descomenta este import.
// import static org.assertj.core.api.SoftAssertions.assertSoftly;
// PASO 0: borra este import junto con los dos tests de JUnit.
import static org.junit.jupiter.api.Assertions.assertEquals;

// PASO 0: borra este import junto con los dos tests de JUnit.
import java.util.List;
// PASO 4: descomenta estos tres imports.
// import org.assertj.core.api.SoftAssertions;
// import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
// import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// PASO 4: descomenta este import.
// import org.junit.jupiter.api.extension.ExtendWith;

// PASO 4: descomenta la extensión.
// @ExtendWith(SoftAssertionsExtension.class)
class CatalogTest {

    private final Catalog catalog = new Catalog();

    // PASO 4: AssertJ inyecta aquí un SoftAssertions nuevo en cada test.
    // @InjectSoftAssertions
    // private SoftAssertions softly;

    @BeforeEach
    void addBooks() {
        catalog.add(new Book("9780441172719", "Dune", "Frank Herbert", 1965));
        catalog.add(new Book("9780593098233", "Children of Dune", "Frank Herbert", 1976));
        catalog.add(new Book("9780061054884", "The Dispossessed", "Ursula K. Le Guin", 1974));
        catalog.add(new Book("9780593098226", "Dune Messiah", "Frank Herbert", 1969));
        catalog.add(new Book("9780441478125", "The Left Hand of Darkness", "Ursula K. Le Guin", 1969));
    }

    // ------------------------------------------------------------------
    // PASO 0: los mismos casos con aserciones de JUnit. Ejecútalos, haz el
    // experimento del README y bórralos en el PASO 1.
    // ------------------------------------------------------------------

    @Test
    @DisplayName("should list the books of an author from oldest to newest (JUnit)")
    void shouldListTheBooksOfAnAuthorFromOldestToNewestWithJUnit() {
        List<Book> books = catalog.byAuthor("Frank Herbert");

        assertEquals(3, books.size());
        assertEquals("Dune", books.get(0).title());
        assertEquals("Dune Messiah", books.get(1).title());
        assertEquals("Children of Dune", books.get(2).title());
    }

    @Test
    @DisplayName("should search titles ignoring case (JUnit)")
    void shouldSearchTitlesIgnoringCaseWithJUnit() {
        List<Book> books = catalog.search("dune");

        assertEquals(3, books.size());
        for (Book book : books) {
            assertEquals("Frank Herbert", book.author());
        }
    }

    // ------------------------------------------------------------------
    // PASO 1: aserciones sobre colecciones.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should list the books of an author from oldest to newest")
    // void shouldListTheBooksOfAnAuthorFromOldestToNewest() {
    //     assertThat(catalog.byAuthor("Frank Herbert"))
    //         .extracting(Book::title)
    //         .containsExactly("Dune", "Dune Messiah", "Children of Dune");
    // }

    // @Test
    // @DisplayName("should search titles ignoring case")
    // void shouldSearchTitlesIgnoringCase() {
    //     assertThat(catalog.search("dune"))
    //         .hasSize(3)
    //         .allSatisfy(book -> assertThat(book.author()).isEqualTo("Frank Herbert"));
    // }

    // ------------------------------------------------------------------
    // PASO 2: aserciones sobre Optional.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should find a book by isbn")
    // void shouldFindABookByIsbn() {
    //     assertThat(catalog.findByIsbn("9780441172719"))
    //         .hasValueSatisfying(book -> assertThat(book.title()).isEqualTo("Dune"));
    // }

    // @Test
    // @DisplayName("should return empty for an unknown isbn")
    // void shouldReturnEmptyForAnUnknownIsbn() {
    //     assertThat(catalog.findByIsbn("0000000000000")).isEmpty();
    // }

    // ------------------------------------------------------------------
    // PASO 3: soft assertions con assertSoftly.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should keep every field of the book")
    // void shouldKeepEveryFieldOfTheBook() {
    //     Book book = catalog.findByIsbn("9780061054884").orElseThrow();
    //
    //     assertSoftly(soft -> {
    //         soft.assertThat(book.title()).isEqualTo("The Dispossessed");
    //         soft.assertThat(book.author()).isEqualTo("Ursula K. Le Guin");
    //         soft.assertThat(book.year()).isEqualTo(1974);
    //     });
    // }

    // ------------------------------------------------------------------
    // PASO 4: soft assertions con la extensión de AssertJ.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should keep every field of the book with the extension")
    // void shouldKeepEveryFieldOfTheBookWithTheExtension() {
    //     Book book = catalog.findByIsbn("9780441478125").orElseThrow();
    //
    //     softly.assertThat(book.title()).isEqualTo("The Left Hand of Darkness");
    //     softly.assertThat(book.author()).isEqualTo("Ursula K. Le Guin");
    //     softly.assertThat(book.year()).isEqualTo(1969);
    // }
}
