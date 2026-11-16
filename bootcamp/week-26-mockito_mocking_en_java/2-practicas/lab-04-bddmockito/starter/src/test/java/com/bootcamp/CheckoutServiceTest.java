package com.bootcamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
// PASO 1
// import static org.mockito.BDDMockito.given;
// PASO 1
// import static org.mockito.BDDMockito.then;
// PASO 4
// import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
// PASO 0
import static org.mockito.Mockito.verify;
// PASO 0
import static org.mockito.Mockito.verifyNoInteractions;
// PASO 0
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// PASO 4
// import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    private static final String DUNE = "9780441172719";

    @Mock
    private Inventory inventory;

    @Mock
    private MemberAccounts accounts;

    @Mock
    private Notifier notifier;

    @InjectMocks
    private CheckoutService service;

    // ------------------------------------------------------------------
    // PASO 0: tests con when y verify
    // ------------------------------------------------------------------

    @Test
    @DisplayName("should lend a copy with when and verify")
    void shouldLendACopyWithWhenAndVerify() {
        when(accounts.hasUnpaidFines("M-001")).thenReturn(false);
        when(inventory.reserveCopy(DUNE)).thenReturn(true);

        Checkout checkout = service.checkout("M-001", DUNE);

        assertThat(checkout).isEqualTo(new Checkout("M-001", DUNE));
        verify(notifier).send("M-001", "You borrowed " + DUNE);
    }

    @Test
    @DisplayName("should refuse a member with fines with when and verify")
    void shouldRefuseAMemberWithFinesWithWhenAndVerify() {
        when(accounts.hasUnpaidFines("M-002")).thenReturn(true);

        assertThatThrownBy(() -> service.checkout("M-002", DUNE))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("member has unpaid fines: M-002");
        verifyNoInteractions(inventory);
        verify(notifier, never()).send(anyString(), anyString());
    }

    // ------------------------------------------------------------------
    // PASO 1: given/willReturn y then().should()
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should lend a copy to a member without fines")
    // void shouldLendACopyToAMemberWithoutFines() {
    //     // given
    //     given(accounts.hasUnpaidFines("M-001")).willReturn(false);
    //     given(inventory.reserveCopy(DUNE)).willReturn(true);
    //
    //     // when
    //     Checkout checkout = service.checkout("M-001", DUNE);
    //
    //     // then
    //     assertThat(checkout).isEqualTo(new Checkout("M-001", DUNE));
    //     then(notifier).should().send("M-001", "You borrowed " + DUNE);
    // }

    // ------------------------------------------------------------------
    // PASO 2: should(never()) y shouldHaveNoInteractions()
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should refuse a member with unpaid fines")
    // void shouldRefuseAMemberWithUnpaidFines() {
    //     // given
    //     given(accounts.hasUnpaidFines("M-002")).willReturn(true);
    //
    //     // when / then
    //     assertThatThrownBy(() -> service.checkout("M-002", DUNE))
    //         .isInstanceOf(IllegalStateException.class)
    //         .hasMessage("member has unpaid fines: M-002");
    //     then(inventory).shouldHaveNoInteractions();
    //     then(notifier).should(never()).send(anyString(), anyString());
    // }
    //
    // @Test
    // @DisplayName("should refuse when no copy is left")
    // void shouldRefuseWhenNoCopyIsLeft() {
    //     // given
    //     given(accounts.hasUnpaidFines("M-001")).willReturn(false);
    //     given(inventory.reserveCopy(DUNE)).willReturn(false);
    //
    //     // when / then
    //     assertThatThrownBy(() -> service.checkout("M-001", DUNE))
    //         .isInstanceOf(IllegalStateException.class)
    //         .hasMessage("no copies available: " + DUNE);
    //     then(notifier).shouldHaveNoInteractions();
    // }

    // ------------------------------------------------------------------
    // PASO 3: willThrow
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should not notify the member when the inventory is down")
    // void shouldNotNotifyTheMemberWhenTheInventoryIsDown() {
    //     // given
    //     given(accounts.hasUnpaidFines("M-001")).willReturn(false);
    //     given(inventory.reserveCopy(DUNE)).willThrow(new InventoryUnavailableException("inventory timed out"));
    //
    //     // when / then
    //     assertThatThrownBy(() -> service.checkout("M-001", DUNE))
    //         .isInstanceOf(InventoryUnavailableException.class);
    //     then(notifier).shouldHaveNoInteractions();
    // }

    // ------------------------------------------------------------------
    // PASO 4: el orden con then().should(inOrder)
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("should check the fines before reserving a copy")
    // void shouldCheckTheFinesBeforeReservingACopy() {
    //     // given
    //     given(accounts.hasUnpaidFines("M-001")).willReturn(false);
    //     given(inventory.reserveCopy(DUNE)).willReturn(true);
    //
    //     // when
    //     service.checkout("M-001", DUNE);
    //
    //     // then
    //     InOrder inOrder = inOrder(accounts, inventory);
    //     then(accounts).should(inOrder).hasUnpaidFines("M-001");
    //     then(inventory).should(inOrder).reserveCopy(DUNE);
    // }
}
