package com.bootcamp;

/** Presta un ejemplar a un socio sin multas pendientes y le avisa. */
public class CheckoutService {

    private final Inventory inventory;
    private final MemberAccounts accounts;
    private final Notifier notifier;

    public CheckoutService(Inventory inventory, MemberAccounts accounts, Notifier notifier) {
        this.inventory = inventory;
        this.accounts = accounts;
        this.notifier = notifier;
    }

    public Checkout checkout(String memberId, String isbn) {
        if (accounts.hasUnpaidFines(memberId)) {
            throw new IllegalStateException("member has unpaid fines: " + memberId);
        }
        if (!inventory.reserveCopy(isbn)) {
            throw new IllegalStateException("no copies available: " + isbn);
        }
        notifier.send(memberId, "You borrowed " + isbn);
        return new Checkout(memberId, isbn);
    }
}
