package com.bootcamp;

import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

/** Existencias de un almacén. La reserva no deja el stock en negativo: lo impide el WHERE. */
@Repository
public class StockRepository {

    private final JdbcClient jdbc;

    public StockRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void add(String sku, int quantity) {
        jdbc.sql("""
                INSERT INTO stock (sku, quantity) VALUES (:sku, :quantity)
                ON CONFLICT (sku) DO UPDATE SET quantity = stock.quantity + EXCLUDED.quantity
                """)
            .param("sku", sku)
            .param("quantity", quantity)
            .update();
    }

    public Optional<Integer> quantity(String sku) {
        return jdbc.sql("SELECT quantity FROM stock WHERE sku = :sku")
            .param("sku", sku)
            .query(Integer.class)
            .optional();
    }

    /** Devuelve false, sin tocar nada, si no hay unidades suficientes. */
    public boolean reserve(String sku, int quantity) {
        int updated = jdbc.sql("UPDATE stock SET quantity = quantity - :quantity WHERE sku = :sku AND quantity >= :quantity")
            .param("sku", sku)
            .param("quantity", quantity)
            .update();
        return updated == 1;
    }
}
