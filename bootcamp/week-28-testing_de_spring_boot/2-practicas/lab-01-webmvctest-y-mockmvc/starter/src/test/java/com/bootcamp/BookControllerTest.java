package com.bootcamp;

// PASO 3
// import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// PASO 3
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// PASO 4
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// PASO 3
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// PASO 2
// import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// PASO 3
// import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Slice web: carga BookController y lo que lo rodea (JSON, advices); BookService es un mock. */
@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final Book CIEN_ANOS =
        new Book("978-0307474728", "Cien años de soledad", "Gabriel García Márquez", 1967);

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService service;

    @Test
    void shouldRespond200WithTheBook() throws Exception {
        when(service.find("978-0307474728")).thenReturn(CIEN_ANOS);

        mvc.perform(get("/api/books/978-0307474728"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Cien años de soledad"))
            .andExpect(jsonPath("$.year").value(1967));
    }

    // PASO 1: la excepción del servicio llega al advice y sale como 404
    // @Test
    // void shouldRespond404WhenTheBookDoesNotExist() throws Exception {
        // when(service.find("000")).thenThrow(new BookNotFoundException("000"));

        // mvc.perform(get("/api/books/000"))
            // .andExpect(status().isNotFound())
            // .andExpect(jsonPath("$.detail").value("book not found: 000"));
    // }

    // PASO 2: un parámetro de consulta y una lista
    // @Test
    // void shouldListTheBooksOfAnAuthor() throws Exception {
        // when(service.byAuthor("Gabriel García Márquez")).thenReturn(List.of(CIEN_ANOS));

        // mvc.perform(get("/api/books").param("author", "Gabriel García Márquez"))
            // .andExpect(status().isOk())
            // .andExpect(jsonPath("$.length()").value(1))
            // .andExpect(jsonPath("$[0].isbn").value("978-0307474728"));
    // }

    // @Test
    // void shouldRespond400WhenTheAuthorIsMissing() throws Exception {
        // mvc.perform(get("/api/books"))
            // .andExpect(status().isBadRequest());
    // }

    // PASO 3: un POST con cuerpo JSON
    // @Test
    // void shouldRespond201AndPassTheBodyToTheService() throws Exception {
        // when(service.add(CIEN_ANOS)).thenReturn(CIEN_ANOS);

        // mvc.perform(post("/api/books")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"isbn": "978-0307474728", "title": "Cien años de soledad",
                     // "author": "Gabriel García Márquez", "year": 1967}
                    // """))
            // .andExpect(status().isCreated())
            // .andExpect(header().string("Location", "/api/books/978-0307474728"));

        // verify(service).add(CIEN_ANOS);
    // }

    // PASO 4: el cuerpo entero, comparado como JSON
    // @Test
    // void shouldRespondWithTheWholeBookAsJson() throws Exception {
        // when(service.find("978-0307474728")).thenReturn(CIEN_ANOS);

        // mvc.perform(get("/api/books/978-0307474728"))
            // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // .andExpect(content().json("""
                // {"isbn": "978-0307474728", "title": "Cien años de soledad",
                 // "author": "Gabriel García Márquez", "year": 1967}
                // """));
    // }
}
