package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
// PASO 2
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// PASO 2
// import static org.mockito.ArgumentMatchers.anyString;
// PASO 1 (se borra en el PASO 3)
// import static org.mockito.Mockito.mock;
// PASO 1
// import static org.mockito.Mockito.when;

// PASO 2
// import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// PASO 3
// import org.junit.jupiter.api.extension.ExtendWith;
// PASO 3
// import org.mockito.InjectMocks;
// PASO 3
// import org.mockito.Mock;
// PASO 3
// import org.mockito.junit.jupiter.MockitoExtension;

// PASO 3
// @ExtendWith(MockitoExtension.class)
class HoldServiceTest {

    private static final Book DUNE = new Book("9780441172719", "Dune");

    // ------------------------------------------------------------------
    // PASO 0: dobles escritos a mano. Cada uno implementa la interfaz
    // entera, aunque el test solo use un método.
    // ------------------------------------------------------------------

    static class StubBookRepository implements BookRepository {

        private final Book book;
        private final int holds;

        StubBookRepository(Book book, int holds) {
            this.book = book;
            this.holds = holds;
        }

        @Override
        public Optional<Book> findByIsbn(String isbn) {
            return book.isbn().equals(isbn) ? Optional.of(book) : Optional.empty();
        }

        @Override
        public int countHolds(String isbn) {
            return holds;
        }

        @Override
        public void saveHold(Hold hold) {
        }
    }

    static class StubMemberDirectory implements MemberDirectory {

        private final boolean active;

        StubMemberDirectory(boolean active) {
            this.active = active;
        }

        @Override
        public boolean isActive(String memberId) {
            return active;
        }
    }

    @Test
    @DisplayName("should place the first hold with hand-written stubs")
    void shouldPlaceTheFirstHoldWithHandWrittenStubs() {
        HoldService service = new HoldService(new StubBookRepository(DUNE, 0), new StubMemberDirectory(true));

        Hold hold = service.placeHold("M-001", DUNE.isbn());

        assertThat(hold).isEqualTo(new Hold("M-001", DUNE.isbn(), 1));
    }

    @Test
    @DisplayName("should queue the member with hand-written stubs")
    void shouldQueueTheMemberWithHandWrittenStubs() {
        HoldService service = new HoldService(new StubBookRepository(DUNE, 2), new StubMemberDirectory(true));

        Hold hold = service.placeHold("M-001", DUNE.isbn());

        assertThat(hold.position()).isEqualTo(3);
    }

    // ------------------------------------------------------------------
    // PASO 1 (se borra en el PASO 3): mocks creados con mock()
    // ------------------------------------------------------------------

    // private final BookRepository books = mock(BookRepository.class);
    // private final MemberDirectory members = mock(MemberDirectory.class);
    // private final HoldService service = new HoldService(books, members);

    // ------------------------------------------------------------------
    // PASO 3: los mismos mocks con anotaciones de Mockito
    // ------------------------------------------------------------------

    // @Mock
    // private BookRepository books;
    //
    // @Mock
    // private MemberDirectory members;
    //
    // @InjectMocks
    // private HoldService service;

    // ------------------------------------------------------------------
    // PASO 1: stubbing con when(...).thenReturn(...)
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should place the first hold when nobody is waiting")
    // void shouldPlaceTheFirstHoldWhenNobodyIsWaiting() {
    //     when(members.isActive("M-001")).thenReturn(true);
    //     when(books.findByIsbn(DUNE.isbn())).thenReturn(Optional.of(DUNE));
    //     when(books.countHolds(DUNE.isbn())).thenReturn(0);
    //
    //     Hold hold = service.placeHold("M-001", DUNE.isbn());
    //
    //     assertThat(hold).isEqualTo(new Hold("M-001", DUNE.isbn(), 1));
    // }
    //
    // @Test
    // @DisplayName("should queue the member behind the existing holds")
    // void shouldQueueTheMemberBehindTheExistingHolds() {
    //     when(members.isActive("M-001")).thenReturn(true);
    //     when(books.findByIsbn(DUNE.isbn())).thenReturn(Optional.of(DUNE));
    //     when(books.countHolds(DUNE.isbn())).thenReturn(2);
    //
    //     Hold hold = service.placeHold("M-001", DUNE.isbn());
    //
    //     assertThat(hold.position()).isEqualTo(3);
    // }

    // ------------------------------------------------------------------
    // PASO 2: respuestas que hacen fallar al servicio, y thenThrow
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should reject an inactive member")
    // void shouldRejectAnInactiveMember() {
    //     when(members.isActive("M-002")).thenReturn(false);
    //
    //     assertThatThrownBy(() -> service.placeHold("M-002", DUNE.isbn()))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessage("inactive member: M-002");
    // }
    //
    // @Test
    // @DisplayName("should reject a book that is not in the catalog")
    // void shouldRejectABookThatIsNotInTheCatalog() {
    //     when(members.isActive("M-001")).thenReturn(true);
    //     when(books.findByIsbn("0000000000000")).thenReturn(Optional.empty());
    //
    //     assertThatThrownBy(() -> service.placeHold("M-001", "0000000000000"))
    //         .isInstanceOf(NoSuchElementException.class)
    //         .hasMessage("unknown book: 0000000000000");
    // }
    //
    // @Test
    // @DisplayName("should reject a hold when the queue is full")
    // void shouldRejectAHoldWhenTheQueueIsFull() {
    //     when(members.isActive("M-001")).thenReturn(true);
    //     when(books.findByIsbn(DUNE.isbn())).thenReturn(Optional.of(DUNE));
    //     when(books.countHolds(DUNE.isbn())).thenReturn(HoldService.MAX_HOLDS);
    //
    //     assertThatThrownBy(() -> service.placeHold("M-001", DUNE.isbn()))
    //         .isInstanceOf(IllegalStateException.class)
    //         .hasMessage("hold queue full: " + DUNE.isbn());
    // }
    //
    // @Test
    // @DisplayName("should let a directory outage reach the caller")
    // void shouldLetADirectoryOutageReachTheCaller() {
    //     when(members.isActive(anyString()))
    //         .thenThrow(new DirectoryUnavailableException("member directory timed out"));
    //
    //     assertThatThrownBy(() -> service.placeHold("M-001", DUNE.isbn()))
    //         .isInstanceOf(DirectoryUnavailableException.class)
    //         .hasMessage("member directory timed out");
    // }
}
