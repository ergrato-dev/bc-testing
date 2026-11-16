package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 6
// import static org.mockito.ArgumentMatchers.any;
// PASO 1
// import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/** El mismo slice del lab 01, con MockMvcTester: las comprobaciones se escriben con AssertJ. */
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    private static final MemberRequest ANA = new MemberRequest("Ana Pérez", "ana@example.com", 1995);

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private MemberService service;

    @Test
    void shouldRespond201WithTheNewMember() {
        when(service.register(ANA)).thenReturn(new Member("M-1", "Ana Pérez", "ana@example.com", 1995));

        assertThat(mvc.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "Ana Pérez", "email": "ana@example.com", "birthYear": 1995}
                    """))
            .hasStatus(HttpStatus.CREATED)
            .hasHeader("Location", "/api/members/M-1")
            .bodyJson().extractingPath("$.id").isEqualTo("M-1");
    }

    // PASO 1: @Valid rechaza el cuerpo antes de llegar al servicio
    // @Test
    // void shouldRespond400WithoutCallingTheServiceWhenTheNameIsBlank() {
        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"name": "", "email": "ana@example.com", "birthYear": 1995}
                    // """))
            // .hasStatus(HttpStatus.BAD_REQUEST);

        // verifyNoInteractions(service);
    // }

    // PASO 2: el cuerpo del 400 dice qué campo falló y por qué
    // @Test
    // void shouldExplainWhichFieldIsWrong() {
        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"name": "Ana Pérez", "email": "ana", "birthYear": 1995}
                    // """))
            // .hasStatus(HttpStatus.BAD_REQUEST)
            // .bodyJson().extractingPath("$.errors.email").isEqualTo("must be a valid email");
    // }

    // PASO 3: todos los errores en una sola respuesta
    // @Test
    // void shouldReportEveryWrongFieldAtOnce() {
        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"name": "", "email": "ana@example.com", "birthYear": 1850}
                    // """))
            // .hasStatus(HttpStatus.BAD_REQUEST)
            // .bodyJson().extractingPath("$.errors").asMap()
            // .containsOnlyKeys("name", "birthYear")
            // .containsEntry("birthYear", "must be 1900 or later");
    // }

    // PASO 4: un JSON mal escrito también es un 400
    // @Test
    // void shouldRespond400WhenTheJsonIsMalformed() {
        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("{\"name\": \"Ana Pérez\","))
            // .hasStatus(HttpStatus.BAD_REQUEST)
            // .bodyJson().extractingPath("$.detail").isEqualTo("Failed to read request");
    // }

    // PASO 5: sin Content-Type JSON, 415
    // @Test
    // void shouldRespond415WhenTheBodyIsNotJson() {
        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.TEXT_PLAIN)
                // .content("Ana Pérez"))
            // .hasStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    // }

    // PASO 6: una regla del dominio, 409
    // @Test
    // void shouldRespond409WhenTheEmailIsAlreadyRegistered() {
        // when(service.register(any())).thenThrow(new DuplicateMemberException("ana@example.com"));

        // assertThat(mvc.post().uri("/api/members")
                // .contentType(MediaType.APPLICATION_JSON)
                // .content("""
                    // {"name": "Ana Pérez", "email": "ana@example.com", "birthYear": 1995}
                    // """))
            // .hasStatus(HttpStatus.CONFLICT)
            // .bodyJson().extractingPath("$.detail").isEqualTo("email already registered: ana@example.com");
    // }
}
