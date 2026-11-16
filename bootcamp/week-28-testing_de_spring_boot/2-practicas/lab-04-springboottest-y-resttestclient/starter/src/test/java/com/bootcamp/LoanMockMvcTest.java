package com.bootcamp;

// PASO 3
// import static org.assertj.core.api.Assertions.assertThat;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.assertj.MockMvcTester;

// PASO 3: la aplicación entera, sin servidor: las peticiones van por MockMvc
// @SpringBootTest
// @AutoConfigureMockMvc
// class LoanMockMvcTest {

    // @Autowired
    // private MockMvcTester mvc;

    // @Test
    // void shouldOpenALoanThroughTheWholeApplication() {
        // assertThat(mvc.post().uri("/api/loans")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"isbn": "978-0307474728", "memberId": "M-9"}
                    // """))
            // .hasStatus(HttpStatus.CREATED)
            // .bodyJson().extractingPath("$.memberId").isEqualTo("M-9");
    // }
// }
